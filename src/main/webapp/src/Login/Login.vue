<template>
  <div class="Login">
    <mu-appbar title="欢迎使用校园管理系统"/>
    <mu-text-field hintText="用户名" icon="phone" v-model="loginAccount"/><br/>
    <mu-text-field hintText="密码" icon="phone" v-model="loginPass"/><br/>
    <mu-raised-button label="登录" fullWidth/>
    <mu-popup position="bottom" :overlay="false" popupClass="popup-bottom" :open="bottomPopup">
      <mu-icon :value="icon" :size="36" :color="color"/>{{ message }}
    </mu-popup>
  </div>
</template>
<script>
  export default {
    name: 'Login',
    data () {
      return {
        open: false,
        bottomPopup: false,
        message: '',
        icon: '',
        color: '',
        loginAccount: '',
        loginPass: ''
      }
    },
    methods: {
      login () {
        if (this.loginAccount.toString() === '') {
          this.openPopup('用户名不能为空！', 'report_problem', 'orange')
        } else if (this.loginPass.toString() === '') {
          this.openPopup('密码不能为空！', 'report_problem', 'orange')
        } else {
          this.$http.get(
            API.Login,
            {
              params:
            {
              loginAccount: this.loginAccount,
              loginPass: this.loginPass
            }
            },
            {
              headers:
              {
                'X-Requested-With': 'XMLHttpRequest'
              }
            }
            ).then((response) => {
              switch (response.body) {
                case '0':
                  window.location.href = '/'
                  break
                case '1':
                  this.openPopup('用户名或密码错误！', 'report_problem', 'red')
                  break
                default:
                  this.nameErrorText = ''
                  this.nameErrorColor = 'blue'
              }
            }, (response) => {
              this.openPopup('服务器内部错误！', 'report_problem', 'orange')
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

