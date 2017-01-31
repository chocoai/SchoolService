<template>
  <div class="Login">
    <mu-appbar title="欢迎使用校园管理系统"/>
    <mu-flexbox>
      <mu-flexbox-item >
        <mu-text-field hintText="用户名" icon="face" v-model="userId" fullWidth/><br/>
        <mu-text-field hintText="密码" icon="blur_on" v-model="pass" type="password" fullWidth/><br/>
        <mu-select-field hintText="用户类别" icon="settings" v-model="type" fullWidth>
          <mu-menu-item value="teacher" title="教师"/>
          <mu-menu-item value="manager" title="管理"/>
          <mu-menu-item value="parent" title="家长"/>
        </mu-select-field>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-raised-button label="登录" fullWidth @click="loginTo" primary/>
    <mu-popup position="bottom" :overlay="false" popupClass="popup-bottom" :open="bottomPopup">
      <mu-icon :value="icon" :size="36" :color="color"/>&nbsp;{{ message }}
    </mu-popup>
  </div>
</template>
<script>
  import * as API from './LoginAPI.js'
  export default {
    name: 'Login',
    data () {
      return {
        open: false,
        bottomPopup: false,
        message: '',
        icon: '',
        color: '',
        userId: '',
        pass: '',
        type: 'teacher'
      }
    },
    methods: {
      loginTo () {
        if (this.userId.toString() === '') {
          this.openPopup('用户名不能为空!', 'report_problem', 'orange')
        } else if (this.pass.toString() === '') {
          this.openPopup('密码不能为空!', 'report_problem', 'orange')
        } else {
          this.$http.get(
            API.Login,
            { params: {
              userId: this.userId,
              pass: this.pass,
              type: this.type
            } },
            { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
          ).then((response) => {
            switch (response.body) {
              case 'forManager':
                window.location.href = '/forManager'
                break
              case 'forTeacher':
                window.location.href = '/forTeacher'
                break
              case 'forParent':
                window.location.href = '/forParent'
                break
              case 'noPower':
                this.openPopup('用户无管理权限!', 'report_problem', 'orange')
                break
              case 'error':
                this.openPopup('用户名或密码错误!', 'report_problem', 'orange')
                break
              default:
                window.location.href = '/'
            }
          }, (response) => {
            this.openPopup('服务器内部错误!', 'error', 'red')
          })
        }
      },
      openPopup (message, icon, color) {
        this.message = message
        this.icon = icon
        this.color = color
        this.bottomPopup = true
        setTimeout(() => { this.bottomPopup = false }, 1500)
      }
    }
  }
</script>

