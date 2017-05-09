<template>
  <div>
    <mu-appbar title="欢迎使用育明小学信息管理系统"/>
    <mu-flexbox>
      <mu-flexbox-item >
        <mu-text-field hintText="手机号码" icon="face" v-model="mobile" fullWidth/><br/>
        <mu-text-field hintText="密码" icon="blur_on" v-model="pass" type="password" fullWidth/><br/>
        <mu-select-field hintText="用户类别" icon="settings" v-model="type" fullWidth>
          <mu-menu-item value="teacher" title="教师"/>
          <mu-menu-item value="parent" title="家长"/>
        </mu-select-field>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-raised-button label="登录" fullWidth @click="goLogin" primary/>
    <mu-raised-button label="切换到桌面版" fullWidth @click="goDesktop"/>
    <mu-popup position="bottom" :overlay="false" popupClass="popup-bottom" :open="bottomPopup">
      <mu-icon :value="icon" :size="36" :color="color"/>&nbsp;{{ message }}
    </mu-popup>
  </div>
</template>
<script>
  import * as API from './API.js'
  export default {
    name: 'Login',
    data () {
      return {
        open: false,
        bottomPopup: false,
        message: '',
        icon: '',
        color: '',
        mobile: '',
        pass: '',
        type: 'teacher'
      }
    },
    methods: {
      goLogin () {
        if (this.mobile.toString() === '') {
          this.openPopup('手机号码不能为空!', 'report_problem', 'orange')
        } else if (this.pass.toString() === '') {
          this.openPopup('密码不能为空!', 'report_problem', 'orange')
        } else {
          this.$http.get(
            API.login,
            { params: {
              mobile: this.mobile,
              pass: this.pass,
              type: this.type
            } },
            { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
          ).then((response) => {
            if (response.body === 'OK') {
              window.location.href = '/MainMobile/Teacher_Home'
            } else {
              this.openPopup('服务器内部错误!', 'error', 'red')
            }
          }, (response) => {
            this.openPopup('服务器内部错误!', 'error', 'red')
          })
        }
      },
      goDesktop () {
        window.location.href = '/MainDesktop'
      },
      openPopup (message, icon, color) {
        this.message = message
        this.icon = icon
        this.color = color
        this.bottomPopup = true
        setTimeout(() => { this.bottomPopup = false }, 2000)
      }
    }
  }
</script>

