<template>
  <div>
    <mu-appbar title="班级详情">
      <mu-icon-button icon='reply' slot="right" @click="goReply"/>
    </mu-appbar>
    <mu-text-field label="入学年份" :disabled="Edit_Able" v-model="year" :errorColor="yearErrorColor" :errorText="yearErrorText" @input="checkYear" fullWidth labelFloat icon="title" maxLength="4"/><br/>
    <mu-text-field label="班级序号" :disabled="Edit_Able" v-model="order" :errorColor="orderErrorColor" :errorText="orderErrorText" @input="checkOrder" fullWidth labelFloat icon="title" maxLength="2"/><br/>
    <mu-text-field label="班级名称" :disabled="Edit_Able" v-model="name" :errorColor="nameErrorColor" :errorText="nameErrorText" @input="checkName" fullWidth labelFloat icon="title" disable="true"/><br/>
    <mu-text-field label="班级口号" :disabled="Edit_Able" v-model="slogan" fullWidth labelFloat icon="title"/><br/>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-flat-button :label="label" :disabled="Edit_Able" @click="openCourse=true" :icon="icons" :backgroundColor="back" color="#FFFFFF"/>
      </mu-flexbox-item>
    </mu-flexbox>
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
    <mu-dialog :open="Reading" title="正在读取" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-dialog :open="Saving" title="正在保存" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-dialog :open="Activing" title="确定要激活该班级吗?" @close="goClose">
      <mu-flat-button label="取消" @click="goClose" />
      <mu-flat-button label="确定" @click="goActive" secondary/>
    </mu-dialog>
    <mu-dialog :open="Inactiving" title="确定要注销该班级吗?" @close="goClose">
      <mu-flat-button label="取消" @click="goClose" />
      <mu-flat-button label="确定" @click="goInactive" secondary/>
    </mu-dialog>
    <mu-popup position="bottom" :overlay="false" popupClass="popup-bottom" :open="bottomPopup">
      <mu-icon :value="icon" :size="36" :color="color"/>&nbsp;{{ message }}
    </mu-popup>
    <mu-drawer right :open="openCourse" docked="false">
      <mu-appbar title="请选择课程" @click.native="openCourse=false">
        <mu-icon-button icon='done' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item title="全选" @click.native="course=course_All">
          <mu-icon slot="left" value="supervisor_account" :size="40"/>
        </mu-list-item>
        <mu-list-item title="清空" @click.native="course=[]">
          <mu-icon slot="left" value="delete_forever" :size="40"/>
        </mu-list-item>
        <mu-list-item v-for="course in courseas" :title="course.name" :describeText="course.type.toString() === '1'?'必修课':'选修课'">
          <mu-checkbox v-model="course" label="" labelLeft  :nativeValue="course.id" uncheckIcon="favorite_border" checkedIcon="favorite" slot="right"/>
        </mu-list-item>
      </mu-list>
    </mu-drawer>
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
      Activing: false,
      Saving: false,
      Reading: true,
      bottomPopup: false,
      icon: '',
      color: '',
      message: '',
      course: [],
      course_All: [],
      name: '',
      nameErrorText: '',
      nameErrorColor: '',
      year: '',
      yearErrorText: '',
      yearErrorColor: '',
      order: '',
      orderErrorText: '',
      orderErrorColor: '',
      slogan: '',
      openCourseA: false,
      openCourseB: false,
      label: '请选择课程',
      icons: 'bookmark_border',
      back: '#66CCCC'
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
      if (this.nameErrorText.toString() === 'OK' && this.yearErrorText.toString() === 'OK' && this.orderErrorText.toString() === 'OK') {
        return false
      } else {
        return true
      }
    },
    name: function () {
      return this.year + '级' + this.order + '班'
    },
    back: function () {
      if (this.course.length > 0) {
        return '#9999CC'
      } else {
        return '#66CCCC'
      }
    },
    icons: function () {
      if (this.course.length > 0) {
        return 'bookmark'
      } else {
        return 'bookmark_border'
      }
    },
    label: function () {
      if (this.course.length > 0) {
        return '课程已选择'
      } else {
        return '请选择课程'
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
        { params: { id: this.$route.params.id } },
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
        } else if (response.body === '要激活的学期不存在!') {
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
        { params: { id: this.$route.params.id } },
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
        } else if (response.body === '要注销的教师不存在!') {
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
          id: this.$route.params.id,
          name: this.name,
          year: this.year,
          order: this.order,
          slogan: this.slogan,
          course: this.course
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
    fetchData (id) {
      this.$http.get(
        API.get,
        { params: { id: id } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        this.room = response.body
        this.name = this.room.name
        this.year = this.room.year
        this.order = this.room.order
        this.slogan = this.room.slogan
        this.state = this.room.state
        if (this.room.state.toString() === '0') {
          this.Active_Able = false
          this.Inactive_Able = true
        } else {
          this.Active_Able = true
          this.Inactive_Able = false
        }
        this.Reading = false
      }, (response) => {
      })
      this.$http.get(
        API.getCourse,
        { params: { id: id } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        if (response.body.toString() !== 'null') {
          this.course = response.body
        } else {
          this.course = []
        }
      }, (response) => {
        this.openPopup('服务器内部错误!', 'error', 'red')
      })
    },
    checkName (value) {
      if (value === null || value === undefined || value === '') {
        this.nameErrorText = '班级名称为必填项!'
        this.nameErrorColor = 'orange'
      } else if (!/\d{4}[\u7ea7]\d{1,2}[\u73ed]/.test(value)) {
        this.nameErrorText = '班级名称格式应为：XXXX级XX班'
        this.nameErrorColor = 'orange'
      } else {
        this.$http.get(
          API.checkNameForEdit,
          { params: { name: value } },
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
    checkYear (value) {
      if (value === null || value === undefined || value === '') {
        this.yearErrorText = '入学年份为必填项!'
        this.yearErrorColor = 'orange'
      } else if (!value.match(/^\d{4}/)) {
        this.yearErrorText = '入学年份应为4位数字!'
        this.yearErrorColor = 'orange'
      } else {
        this.yearErrorText = 'OK'
        this.yearErrorColor = 'green'
      }
    },
    checkOrder (value) {
      if (value === null || value === undefined || value === '') {
        this.orderErrorText = '班级序号为必填项!'
        this.orderErrorColor = 'orange'
      } else if (!value.match(/^\d{1,2}/)) {
        this.orderErrorText = '班级序号应为1~2位数字!'
        this.orderErrorColor = 'orange'
      } else {
        this.orderErrorText = 'OK'
        this.orderErrorColor = 'green'
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
