<template>
  <div class="TeacherEdit">
    <mu-appbar title="请核实后输入以下信息">
      <mu-icon-button icon='reply' slot="right" @click="goReply"/>
    </mu-appbar>
    <mu-text-field label="姓名" :disabled="edit" underlineShow="false" v-model="name" :errorColor="nameErrorColor" :errorText="nameErrorText" @input="checkName" fullWidth labelFloat icon="person"/><br/>
    <mu-text-field label="账号" disabled v-model="userId" fullWidth labelFloat icon="assignment"/><br/>
    <mu-text-field label="手机" :disabled="edit" underlineShow="false" v-model="mobile" :errorColor="mobileErrorColor" :errorText="mobileErrorText" @input="checkMobile"  fullWidth labelFloat icon="phone" maxLength="11"/><br/>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-switch label="管理人员" v-model="isManager" :disabled="edit"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-switch label="学生家长" v-model="isParent" :disabled="edit"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-dialog :open="forSave" title="正在保存" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="edit" v-if="edit" @click="goEdit" primary/>
        <mu-float-button icon="cancel" v-if="save" @click="goCancel" secondary/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="delete" v-if="inactive"  @click="openInactive" backgroundColor="red"/>
        <mu-float-button icon="compare_arrows" v-if="active" @click="openActive" backgroundColor="green"/>
        <mu-float-button icon="done" v-if="save" @click="goSave" backgroundColor="green"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="dialer_sip" v-if="edit" @click="goCall" backgroundColor="#6633CC"/>
        <mu-float-button icon="cached" v-if="save" @click="goReset" backgroundColor="orange"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-dialog :open="forInactive" :title="inactiveTitle" @close="goClose">
      <mu-flat-button label="取消" @click="goClose" />
      <mu-flat-button label="确定" @click="goInactive" secondary/>
    </mu-dialog>
    <mu-dialog :open="forActive" :title="activeTitle" @close="goClose">
      <mu-flat-button label="取消" @click="goClose" />
      <mu-flat-button label="确定" @click="goActive" secondary/>
    </mu-dialog>
    <mu-popup position="bottom" :overlay="false" popupClass="popup-bottom" :open="bottomPopup">
      <mu-icon :value="icon" :size="36" :color="color"/>&nbsp;{{ message }}
    </mu-popup>
  </div>
</template>

<script>
import * as API from './TeacherAPI.js'
export default {
  name: 'TeacherEdit',
  data () {
    return {
      edit: true,
      save: false,
      inactive: false,
      active: false,
      forInactive: false,
      forActive: false,
      bottomPopup: false,
      forSave: false,
      icon: '',
      color: '',
      message: '',
      inactiveTitle: '',
      activeTitle: '',
      teacher: '',
      name: '',
      mobile: '',
      userId: '',
      isManager: false,
      isParent: false,
      isManagers: '',
      isParents: '',
      state: '',
      nameErrorText: '',
      mobileErrorText: '',
      nameErrorColor: '',
      mobileErrorColor: ''
    }
  },
  created () {
    this.fetchData(this.$route.params.teacherId)
  },
  watch: {
    // 如果路由有变化，会再次执行该方法
    '$route': 'fetchData'
  },
  computed: {
    isManagers: function () {
      if (this.isManager) {
        return '1'
      } else {
        return '0'
      }
    },
    isParents: function () {
      if (this.isParent) {
        return '1'
      } else {
        return '0'
      }
    }
  },
  methods: {
    goReply () {
      this.$router.push({ path: '/teacherList' })
    },
    openInactive () {
      this.forInactive = true
    },
    openActive () {
      this.forActive = true
    },
    openPopup (message, icon, color) {
      this.message = message
      this.icon = icon
      this.color = color
      this.bottomPopup = true
      setTimeout(() => { this.bottomPopup = false }, 1500)
    },
    goCall () {
      this.mobile.toString() === '' ? this.openPopup('无联系电话!', 'report_problem', 'orange') : window.location.href = 'tel:' + this.mobile
    },
    goEdit () {
      this.edit = false
      this.save = true
      this.inactive = false
      this.active = false
      this.$http.get(
        API.Update,
        { params: { userId: this.userId } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
      ).then((response) => {
      }, (response) => {
      })
    },
    goCancel () {
      this.edit = true
      this.save = false
      this.fetchData(this.$route.params.teacherId)
    },
    goClose () {
      this.forInactive = false
      this.forActive = false
    },
    goReset () {
      this.fetchData(this.$route.params.teacherId)
    },
    goInactive () {
      this.$http.get(
        API.InactiveById,
        { params: { id: this.$route.params.teacherId } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        if (response.body === 'error') {
          this.openPopup('请重新登录!', 'report_problem', 'orange')
          window.location.href = '/'
        } else if (response.body === 'OK') {
          this.edit = true
          this.save = false
          if (this.state.toString() === '1' || this.state.toString() === '2') {
            this.inactive = true
            this.active = false
          } else {
            this.inactive = false
            this.active = true
          }
          this.openPopup('取消关注成功!', 'check_circle', 'green')
          setTimeout(() => { this.$router.push({ path: '/teacherList' }) }, 1000)
        } else if (response.body === '要取消关注的人员不存在!') {
          this.edit = true
          this.save = false
          if (this.state.toString() === '1' || this.state.toString() === '2') {
            this.inactive = true
            this.active = false
          } else {
            this.inactive = false
            this.active = true
          }
          this.openPopup(response.body, 'report_problem', 'orange')
          setTimeout(() => { this.$router.push({ path: '/teacherList' }) }, 1000)
        } else {
          this.openPopup(response.body, 'report_problem', 'orange')
          setTimeout(() => { this.$router.push({ path: '/teacherList' }) }, 1000)
        }
      }, (response) => {
        this.openPopup('服务器内部错误!', 'error', 'red')
      })
    },
    goActive () {
      this.$http.get(
        API.ActiveById,
        { params: { id: this.$route.params.teacherId } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        if (response.body === 'error') {
          this.openPopup('请重新登录!', 'report_problem', 'red')
          window.location.href = '/'
        } else if (response.body === 'OK') {
          this.edit = true
          this.save = false
          if (this.state.toString() === '1' || this.state.toString() === '2') {
            this.inactive = true
            this.active = false
          } else {
            this.inactive = false
            this.active = true
          }
          this.openPopup('重新邀请关注成功!', 'check_circle', 'green')
          setTimeout(() => { this.$router.push({ path: '/teacherList' }) }, 1000)
        } else if (response.body === '要重新邀请关注的人员不存在!') {
          this.edit = true
          this.save = false
          if (this.state.toString() === '1' || this.state.toString() === '2') {
            this.inactive = true
            this.active = false
          } else {
            this.inactive = false
            this.active = true
          }
          this.openPopup(response.body, 'report_problem', 'red')
          setTimeout(() => { this.$router.push({ path: '/teacherList' }) }, 1000)
        } else {
          this.openPopup(response.body, 'report_problem', 'red')
          setTimeout(() => { this.$router.push({ path: '/teacherList' }) }, 1000)
        }
      }, (response) => {
        this.openPopup('服务器内部错误!', 'error', 'red')
      })
    },
    goSave () {
      this.forSave = true
      this.$http.get(
        API.Edit,
        { params: {
          id: this.$route.params.teacherId,
          name: this.name,
          mobile: this.mobile,
          isManager: this.isManagers,
          isParent: this.isParents
        } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        this.forSave = false
        if (response.body === 'error') {
          this.openPopup('请重新登录!', 'report_problem', 'orange')
          window.location.href = '/'
        } else if (response.body === 'OK') {
          this.openPopup('修改成功！', 'check_circle', 'green')
          setTimeout(() => { this.$router.push({ path: '/teacherList' }) }, 1000)
        } else {
          this.openPopup(response.body, 'report_problem', 'orange')
        }
      }, (response) => {
        this.forSave = false
        this.openPopup('服务器内部错误!', 'error', 'red')
      })
    },
    fetchData (teacherId) {
      this.$http.get(
        API.GetById,
        { params: { id: teacherId } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        this.teacher = response.body
        this.name = this.teacher.name
        this.mobile = this.teacher.mobile
        this.userId = this.teacher.userId
        this.isManagers = this.teacher.isManager.toString()
        this.isParents = this.teacher.isParent.toString()
        if (this.teacher.isManager.toString() === '0') {
          this.isManager = false
        } else {
          this.isManager = true
        }
        if (this.teacher.isParent.toString() === '0') {
          this.isParent = false
        } else {
          this.isParent = true
        }
        this.state = this.teacher.state
        if (this.teacher.state.toString() === '1' || this.teacher.state.toString() === '2') {
          this.inactive = true
          this.active = false
        } else {
          this.inactive = false
          this.active = true
        }
        this.inactiveTitle = '确认要取消' + this.name + '的关注吗?'
        this.activeTitle = '确认要邀请' + this.name + '再次关注吗?'
      }, (response) => {
      })
    },
    checkName (value) {
      if (value === null || value === undefined || value === '') {
        this.nameErrorText = '姓名为必填项!'
        this.nameErrorColor = 'orange'
      } else if (!/^[\u4e00-\u9fa5]{2,}$/.test(value)) {
        this.nameErrorText = '姓名应为2个以上汉字'
        this.nameErrorColor = 'orange'
      } else {

      }
    },
    checkMobile (value) {
      if (value === null || value === undefined || value === '') {
        this.mobileErrorText = '手机号码为必填项!'
        this.mobileErrorColor = 'orange'
      } else if (!/^1(3|4|5|7|8)\d{9}$/.test(value)) {
        this.mobileErrorText = '手机号码格式错误!'
        this.mobileErrorColor = 'orange'
      } else {
        this.$http.get(
          API.CheckMobileForEdit,
          { params: {
            id: this.$route.params.teacherId,
            mobile: value
          } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          if (response.body === 'error') {
            this.openPopup('请重新登录!', 'report_problem', 'orange')
            window.location.href = '/'
          } else if (response.body === 'OK') {
            this.mobileErrorText = ''
            this.mobileErrorColor = 'green'
          } else {
            this.mobileErrorText = response.body
            this.mobileErrorColor = 'red'
          }
        }, (response) => {
          this.openPopup('服务器内部错误!', 'error', 'red')
        })
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
