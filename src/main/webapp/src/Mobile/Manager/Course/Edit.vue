<template>
  <div>
    <mu-appbar title="课程详情">
      <mu-icon-button icon='reply' slot="right" @click="goReply"/>
    </mu-appbar>
    <mu-text-field label="课程名称" :disabled="Edit_Able" v-model="name" :errorColor="nameErrorColor" :errorText="nameErrorText" @input="checkName" fullWidth labelFloat icon="title" maxLength="20"/><br/>
    <mu-text-field label="课程描述" :disabled="Edit_Able" v-model="describe" :errorColor="describeErrorColor" :errorText="describeErrorText" @input="checkDescribe" fullWidth labelFloat icon="title" maxLength="100" multiLine :rows="3" :rowsMax="6"/><br/>
    <mu-select-field hintText="课程类别" :disabled="Edit_Able" icon="settings" v-model="type" fullWidth>
      <mu-menu-item value="1" title="必修课"/>
      <mu-menu-item value="2" title="选修课"/>
    </mu-select-field>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="edit" v-if="Edit_Able" @click="goEdit" primary/>
        <mu-float-button icon="cancel" v-if="Save_Able" @click="goCancel" secondary/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="compare_arrows" v-if="Edit_Able && Inactive_Able" :disabled="Active_Able" @click="openActive" backgroundColor="green"/>
        <mu-float-button icon="compare_arrows" v-if="Edit_Able && Active_Able" :disabled="Inactive_Able" @click="openInactive" backgroundColor="orange"/>
        <mu-float-button icon="done" v-if="Save_Able" :disabled="saveAble" @click="goSave" backgroundColor="green"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-dialog :open="Saving" title="正在保存" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-dialog :open="Activing" title="确定要激活该课程吗?" @close="goClose">
      <mu-flat-button label="取消" @click="goClose" />
      <mu-flat-button label="确定" @click="goActive" secondary/>
    </mu-dialog>
    <mu-dialog :open="Inactiving" title="确定要注销该课程吗?" @close="goClose">
      <mu-flat-button label="取消" @click="goClose" />
      <mu-flat-button label="确定" @click="goInactive" secondary/>
    </mu-dialog>
    <mu-popup position="bottom" :overlay="false" popupClass="popup-bottom" :open="bottomPopup">
      <mu-icon :value="icon" :size="36" :color="color"/>&nbsp;{{ message }}
    </mu-popup>
  </div>
</template>

<script>
import * as API from './API.js'
export default {
  name: 'Edit',
  data () {
    return {
      Edit_Able: true,
      Save_Able: false,
      Active_Able: false,
      Inactive_Able: false,
      Activing: false,
      Inactiving: false,
      Saving: false,
      bottomPopup: false,
      icon: '',
      color: '',
      message: '',
      semester: [],
      name: '',
      describe: '',
      type: '',
      state: '',
      nameErrorText: '',
      nameErrorColor: '',
      describeErrorText: '',
      describeErrorColor: ''
    }
  },
  created () {
    this.fetchData(this.$route.params.id)
  },
  watch: {
    // 如果路由有变化，会再次执行该方法
    '$route': 'fetchData'
  },
  computed: {
    saveAble: function () {
      if (this.nameErrorText.toString() === 'OK' && this.describeErrorText.toString() === 'OK') {
        return false
      } else {
        return true
      }
    }
  },
  methods: {
    goReply () {
      this.$router.push({ path: '/list' })
    },
    openActive () {
      this.Activing = true
    },
    openInactive () {
      this.Inactiving = true
    },
    openPopup (message, icon, color) {
      this.message = message
      this.icon = icon
      this.color = color
      this.bottomPopup = true
      setTimeout(() => { this.bottomPopup = false }, 1500)
    },
    goEdit () {
      this.Edit_Able = false
      this.Save_Able = true
    },
    goCancel () {
      this.Edit_Able = true
      this.Save_Able = false
      this.fetchData(this.$route.params.id)
    },
    goClose () {
      this.Activing = false
      this.Inactiving = false
    },
    goActive () {
      this.$http.get(
        API.active,
        { params: { courseId: this.$route.params.id } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        if (response.body === 'error') {
          this.openPopup('请重新登录!', 'report_problem', 'red')
          window.location.href = '/'
        } else if (response.body === 'OK') {
          this.Edit_Able = true
          this.Save_Able = false
          this.Active_Able = true
          this.Inactive_Able = false
          this.openPopup('激活成功!', 'check_circle', 'green')
          setTimeout(() => { this.$router.push({ path: '/list' }) }, 1000)
        } else if (response.body === '要激活的课程不存在!') {
          this.Edit_Able = true
          this.Save_Able = false
          this.Active_Able = true
          this.Inactive_Able = false
          this.openPopup(response.body, 'report_problem', 'red')
          setTimeout(() => { this.$router.push({ path: '/list' }) }, 1000)
        } else {
          this.openPopup(response.body, 'report_problem', 'red')
          setTimeout(() => { this.$router.push({ path: '/list' }) }, 1000)
        }
      }, (response) => {
        this.openPopup('服务器内部错误!', 'error', 'red')
      })
    },
    goInactive () {
      this.$http.get(
        API.inactive,
        { params: { courseId: this.$route.params.id } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        if (response.body === 'error') {
          this.openPopup('请重新登录!', 'report_problem', 'red')
          window.location.href = '/'
        } else if (response.body === 'OK') {
          this.Edit_Able = true
          this.Save_Able = false
          this.Active_Able = false
          this.Inactive_Able = true
          this.openPopup('注销成功!', 'check_circle', 'green')
          setTimeout(() => { this.$router.push({ path: '/list' }) }, 1000)
        } else if (response.body === '要注销的课程不存在!') {
          this.Edit_Able = true
          this.Save_Able = false
          this.Active_Able = false
          this.Inactive_Able = true
          this.openPopup(response.body, 'report_problem', 'red')
          setTimeout(() => { this.$router.push({ path: '/list' }) }, 1000)
        } else {
          this.openPopup(response.body, 'report_problem', 'red')
          setTimeout(() => { this.$router.push({ path: '/list' }) }, 1000)
        }
      }, (response) => {
        this.openPopup('服务器内部错误!', 'error', 'red')
      })
    },
    goSave () {
      this.Saving = true
      this.$http.get(
        API.edit,
        { params: {
          courseId: this.$route.params.id,
          name: this.name,
          describe: this.describe,
          type: this.type
        } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        this.Saving = false
        if (response.body === 'error') {
          this.openPopup('请重新登录!', 'report_problem', 'orange')
          window.location.href = '/'
        } else if (response.body === 'OK') {
          this.openPopup('修改成功！', 'check_circle', 'green')
          setTimeout(() => { this.$router.push({ path: '/list' }) }, 1000)
        } else {
          this.openPopup(response.body, 'report_problem', 'orange')
        }
      }, (response) => {
        this.Saving = false
        this.openPopup('服务器内部错误!', 'error', 'red')
      })
    },
    fetchData (courseId) {
      this.$http.get(
        API.get,
        { params: { courseId: courseId } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        this.course = response.body
        this.name = this.course.name
        this.describe = this.course.describe
        this.type = this.course.type
        this.state = this.course.state
        if (this.course.state.toString() === '0') {
          this.Active_Able = false
          this.Inactive_Able = true
        } else {
          this.Active_Able = true
          this.Inactive_Able = false
        }
      }, (response) => {
      })
    },
    checkName (value) {
      if (value === null || value === undefined || value === '') {
        this.nameErrorText = '名称为必填项!'
        this.nameErrorColor = 'orange'
      } else if (value.length > 20) {
        this.nameErrorText = '名称不得超过20字符'
        this.nameErrorColor = 'orange'
      } else {
        this.$http.get(
          API.checkNameForEdit,
          { params: {
            courseId: this.$route.params.id,
            name: value
          } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          if (response.body === 'error') {
            this.openPopup('请重新登录!', 'report_problem', 'orange')
            window.location.href = '/'
          } else if (response.body === 'OK') {
            this.nameErrorText = 'OK'
            this.nameErrorColor = 'green'
          } else {
            this.nameErrorText = response.body
            this.nameErrorColor = 'red'
          }
        }, (response) => {
          this.openPopup('服务器内部错误!', 'error', 'red')
        })
      }
    },
    checkDescribe (value) {
      if (value === null || value === undefined || value === '') {
        this.describeErrorText = '课程表述为必填项!'
        this.describeErrorColor = 'orange'
      } else if (value.length > 100) {
        this.describeErrorText = '课程表述不得超过100字符'
        this.describeErrorColor = 'orange'
      } else {
        this.describeErrorText = 'OK'
        this.describeErrorColor = 'green'
      }
    }
  }
}
</script>
<style lang="css">
  .flex-demo {
    height: 70px;
    text-align: center;
    line-height: 32px;
  }
  .popup-bottom {
    width: 100%;
    opacity: .8;
    height: 48px;
    line-height: 48px;
    display: flex;
    align-items: center;
    justify-content: center;
    max-width: 300px;
  }
</style>
