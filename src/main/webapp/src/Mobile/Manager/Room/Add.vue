<template>
  <div>
    <mu-appbar title="创建新班级">
      <mu-icon-button icon='reply' slot="right" @click="gorReply"/>
    </mu-appbar>
    <mu-text-field label="入学年份" v-model="year" :errorColor="yearErrorColor" :errorText="yearErrorText" @input="checkYear" fullWidth labelFloat icon="title" maxLength="4"/><br/>
    <mu-text-field label="班级序号" v-model="order" :errorColor="orderErrorColor" :errorText="orderErrorText" @input="checkOrder" fullWidth labelFloat icon="title" maxLength="2"/><br/>
    <mu-text-field label="班级名称" v-model="name" :errorColor="nameErrorColor" :errorText="nameErrorText" @input="checkName" fullWidth labelFloat icon="title" disable="true"/><br/>
    <mu-text-field label="班级口号" v-model="slogan" fullWidth labelFloat icon="title"/><br/>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-flat-button :label="label" @click="openCourse=true" :icon="icons" :backgroundColor="back" color="#FFFFFF"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-dialog :open="Saving" title="正在保存" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-dialog :open="Reading" title="正在读取" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="cached" @click="goReset" backgroundColor="orange"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button :disabled="Save_Able" icon="save" @click="goSave" backgroundColor="green"/>
      </mu-flexbox-item>
    </mu-flexbox>
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
        <mu-list-item v-for="course in courses" :title="course.name" :describeText="course.type.toString() === '1'?'必修课':'选修课'">
          <mu-checkbox v-model="course" label="" labelLeft :nativeValue="course.id" uncheckIcon="favorite_border" checkedIcon="favorite" slot="right"/>
        </mu-list-item>
      </mu-list>
    </mu-drawer>
  </div>
</template>
<script>
import * as API from './API.js'
export default {
  name: 'Add',
  data () {
    return {
      bottomPopup: false,
      Saving: false,
      Reading: true,
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
      openCourse: false,
      label: '请选择课程',
      icons: 'bookmark_border',
      back: '#66CCCC'
    }
  },
  created: function () {
    this.$http.get(
      API.list,
      { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
    ).then((response) => {
      this.courses = response.body
      this.$http.get(
        API.all,
        { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
      ).then((response) => {
        this.course_All = response.body
        this.course = this.course_All
        this.Reading = false
      }, (response) => {
        this.openPopup('服务器内部错误！', 'error', 'red')
      })
    }, (response) => {
      this.openPopup('服务器内部错误！', 'error', 'red')
    })
  },
  computed: {
    Save_Able: function () {
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
    gorReply () {
      this.$router.push({ path: '/list' })
    },
    openPopup (message, icon, color) {
      this.message = message
      this.icon = icon
      this.color = color
      this.bottomPopup = true
      setTimeout(() => { this.bottomPopup = false }, 1500)
    },
    goReset () {
      this.name = ''
      this.nameErrorText = ''
      this.nameErrorColor = ''
      this.year = ''
      this.yearErrorText = ''
      this.yearErrorColor = ''
      this.order = ''
      this.orderErrorText = ''
      this.orderErrorColor = ''
      this.slogan = ''
      this.course = []
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
          API.checkNameForAdd,
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
    },
    goSave () {
      this.Saving = true
      this.$http.get(
        API.save,
        { params: {
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
          this.openPopup('保存成功！', 'check_circle', 'green')
          setTimeout(() => { this.$router.push({ path: '/list' }) }, 1000)
        } else {
          this.openPopup(response.body, 'report_problem', 'orange')
        }
        this.Saving = false
      }, (response) => {
        this.Saving = false
        this.openPopup('服务器内部错误!', 'error', 'red')
      })
    }
  }
}
</script>
<style lang="css">
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
  .flex-demo {
    height: 70px;
    text-align: center;
    line-height: 32px;
  }
</style>
