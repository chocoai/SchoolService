<template>
  <div>
    <mu-appbar title="欢迎使用校园管理系统"/>
    <mu-flexbox>
      <mu-flexbox-item >
        <mu-text-field hintText="用户名/联系电话/微信号/电子邮箱/QQ" icon="face" v-model="lmewuq" fullWidth/><br/>
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
        lmewuq: '',
        pass: '',
        type: 'teacher'
      }
    },
    methods: {
      loginTo () {
        if (this.lmewuq.toString() === '') {
          this.openPopup('用户名不能为空!', 'report_problem', 'orange')
        } else if (this.pass.toString() === '') {
          this.openPopup('密码不能为空!', 'report_problem', 'orange')
        } else {
          this.$http.get(
            API.login,
            { params: {
              lmewuq: this.lmewuq,
              pass: this.pass,
              type: this.type
            } },
            { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
          ).then((response) => {
            switch (response.body) {
              case 'Mobile_Manager_Home':
                window.location.href = '/Mobile_Manager_Home'
                break
              case 'Mobile_Teacher_Home':
                window.location.href = '/Mobile_Teacher_Home'
                break
              case 'Mobile_Parent_Home':
                window.location.href = '/Mobile_Parent_Home'
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
        setTimeout(() => { this.bottomPopup = false }, 2000)
      }
    }
  }
</script>

