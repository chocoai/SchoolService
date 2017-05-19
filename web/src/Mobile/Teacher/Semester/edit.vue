<template>
  <div>
    <mu-appbar title="学期详情">
      <mu-icon-button icon='reply' slot="right" @click="goReply" v-if="permission.Page"/>
    </mu-appbar>
    <mu-text-field label="学期名称" v-model="name" :errorColor="nameErrorColor" :errorText="nameErrorText" @input="checkName" fullWidth labelFloat icon="title" maxLength="20"/><br/>
    <mu-date-picker label="开始时间" v-model="timeStart" fullWidth labelFloat icon="schedule"/><br/>
    <mu-date-picker label="终止时间" v-model="timeEnd" fullWidth labelFloat icon="schedule"/><br/>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="edit" v-if="permission.Edit" @click="goEdit" backgroundColor="blue"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="cached" v-if="permission.Get" @click="goReset" backgroundColor="orange"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-dialog :open="Reading" title="正在读取" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-dialog :open="Saving" title="正在保存" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-popup position="bottom" :overlay="false" popupClass="popup-bottom" :open="bottomPopup">
      <mu-icon :value="icon" :size="36" :color="color"/>&nbsp;{{ message }}
    </mu-popup>
  </div>
</template>

<script>
import * as API from './API.js'
import { getCookie } from '../../../cookieUtil.js'
export default {
  name: 'Edit',
  data () {
    return {
      Saving: false,
      Reading: true,
      bottomPopup: false,
      permission: [],
      menu: [],
      icon: '',
      color: '',
      message: '',
      object: [],
      name: '',
      timeStart: '',
      timeEnd: '',
      state: '',
      nameErrorText: '',
      nameErrorColor: ''
    }
  },
  created () {
    this.fetchData(this.$route.params.id)
    if (getCookie('MenuMobile') === null || getCookie('MenuMobile') === undefined || getCookie('MenuMobile') === '' || getCookie(API.base) === null || getCookie(API.base) === undefined || getCookie(API.base) === '') {
      this.$http.get(
        API.menu
      ).then((response) => {
        if (response.body.toString() === 'illegal' || response.body.toString() === 'overdue') {
          this.openPopup('登录过期或非法操作!', 'error', 'red')
        } else {
          this.$http.get(
            API.permission
          ).then((res) => {
            this.permission = JSON.parse(JSON.parse(getCookie(API.base)))
            this.menu = JSON.parse(JSON.parse(getCookie('MenuMobile')))
          }, (res) => {
            this.openPopup('服务器内部错误!', 'error', 'red')
          })
        }
      }, (response) => {
        this.openPopup('服务器内部错误!', 'error', 'red')
      })
    } else {
      this.permission = JSON.parse(JSON.parse(getCookie(API.base)))
      this.menu = JSON.parse(JSON.parse(getCookie('MenuMobile')))
    }
  },
  watch: {
    // 如果路由有变化，会再次执行该方法
    '$route': 'fetchData'
  },
  methods: {
    goReply () {
      this.$router.push({ path: '/list' })
    },
    goReset () {
      this.Reading = false
      this.fetchData(this.$route.params.id)
    },
    openPopup (message, icon, color) {
      this.message = message
      this.icon = icon
      this.color = color
      this.bottomPopup = true
      setTimeout(() => { this.bottomPopup = false }, 1500)
    },
    goSave () {
      this.Saving = true
      this.$http.get(
        API.edit,
        { params: {
          id: this.$route.params.id,
          name: this.name,
          time_start: this.timeStart,
          time_end: this.timeEnd
        } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        this.Saving = false
        if (response.body === 'illegal' || response.body.toString() === 'overdue') {
          this.openPopup('请重新登录!', 'report_problem', 'orange')
          window.location.href = '/MainMobile'
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
        this.object = response.body
        this.name = this.object.name
        this.timeStart = this.object.time_start
        this.timeEnd = this.object.time_end
        this.state = this.object.state
        this.Reading = false
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
          { params: { id: this.$route.params.id, name: value } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          if (response.body === 'illegal' || response.body.toString() === 'overdue') {
            this.openPopup('请重新登录!', 'report_problem', 'orange')
            window.location.href = '/MainMobile'
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
