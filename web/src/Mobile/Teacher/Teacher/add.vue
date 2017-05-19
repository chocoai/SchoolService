<template>
  <div>
    <mu-appbar title="添加新教师">
      <mu-icon-button icon='reply' slot="right" @click="gorReply" v-if="permission.Page"/>
    </mu-appbar>
    <mu-text-field label="姓名" v-model="name" :errorColor="nameErrorColor" :errorText="nameErrorText" @input="checkName" fullWidth labelFloat icon="person"/><br/>
    <mu-text-field label="手机" v-model="mobile" :errorColor="mobileErrorColor" :errorText="mobileErrorText" @input="checkMobile" fullWidth labelFloat icon="phone" maxLength="11"/><br/>
    <mu-select-field label="类型" icon="settings" v-model="type" fullWidth>
      <mu-menu-item value="1" title="在编"/>
      <mu-menu-item value="2" title="聘用"/>
      <mu-menu-item value="3" title="校外"/>
    </mu-select-field>
    <mu-dialog :open="Saving" title="正在保存" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="save" @click="goSave" backgroundColor="green" v-if="permission.Save"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="cached" @click="goReset" backgroundColor="orange"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-popup position="bottom" :overlay="false" popupClass="popup-bottom" :open="bottomPopup">
      <mu-icon :value="icon" :size="36" :color="color"/>&nbsp;{{ message }}
    </mu-popup>
  </div>
</template>

<script>
import * as API from './API.js'
import { getCookie } from '../../../cookieUtil.js'
export default {
  name: 'Add',
  data () {
    return {
      bottomPopup: false,
      Saving: false,
      permission: [],
      menu: [],
      icon: '',
      color: '',
      name: '',
      mobile: '',
      type: '1',
      message: '',
      nameErrorText: '',
      nameErrorColor: '',
      mobileErrorText: '',
      mobileErrorColor: ''
    }
  },
  created: function () {
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
      this.mobile = ''
      this.mobileErrorText = ''
      this.mobileErrorColor = ''
      this.type = '1'
    },
    checkName (value) {
      if (value === null || value === undefined || value === '') {
        this.nameErrorText = '姓名为必填项!'
        this.nameErrorColor = 'orange'
      } else if (!/^[\u4e00-\u9fa5]{2,}$/.test(value)) {
        this.nameErrorText = '姓名应为2个以上汉字'
        this.nameErrorColor = 'orange'
      } else {
        this.nameErrorText = 'OK'
        this.nameErrorColor = 'green'
      }
    },
    checkMobile (value) {
      if (value === null || value === undefined || value === '') {
        this.mobileErrorText = '手机号码为必填项!'
        this.mobileErrorColor = 'orange'
      } else if (!value.match(/^1(3|4|5|7|8)\d{9}$/)) {
        this.mobileErrorText = '手机号码格式错误!'
        this.mobileErrorColor = 'orange'
      } else {
        this.$http.get(
          API.checkMobileForAdd,
          { params: { mobile: value } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          if (response.body === 'error') {
            this.openPopup('请重新登录!', 'report_problem', 'orange')
            window.location.href = '/'
          } else if (response.body === 'OK') {
            this.mobileErrorText = 'OK'
            this.mobileErrorColor = 'green'
          } else {
            this.mobileErrorText = response.body
            this.mobileErrorColor = 'red'
          }
        }, (response) => {
          this.openPopup('服务器内部错误!', 'error', 'red')
        })
      }
    },
    goSave () {
      this.Saving = true
      this.$http.get(
        API.save,
        { params: {
          name: this.name,
          mobile: this.mobile,
          type: this.type
        } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        this.Saving = false
        if (response.body === 'illegal' || response.body.toString() === 'overdue') {
          this.openPopup('请重新登录!', 'report_problem', 'orange')
          window.location.href = '/MainMobile'
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
