<template>
  <div class="RoomAdd">
    <mu-appbar title="请核实后输入以下信息">
      <mu-icon-button icon='reply' slot="right" @click="reply"/>
    </mu-appbar>
    <mu-select-field v-model="game1" label="入学学年">
      <mu-menu-item v-for="text,index in yearList" ccc="dddd" ttt="ddd" :value="String(index)" :title="text" />
    </mu-select-field>
    <mu-text-field label="入学学年" underlineShow="false" v-model="nameA" :errorColor="nameErrorColor" :errorText="nameErrorText" @input="checkName" fullWidth labelFloat icon="person"/>级<br/>
    <mu-select-field v-model="nameOrder" label="班次">
      <mu-menu-item value="1班" title="1班"/>
      <mu-menu-item value="2班" title="2班"/>
      <mu-menu-item value="3班" title="3班"/>
      <mu-menu-item value="4班" title="4班"/>
      <mu-menu-item value="5班" title="5班"/>
      <mu-menu-item value="6班" title="6班"/>
    </mu-select-field>
    <mu-popup position="bottom" :overlay="false" popupClass="popup-bottom" :open="bottomPopup">
      <mu-icon :value="icon" :size="36" :color="color"/>&nbsp;{{ message }}
    </mu-popup>
  </div>
</template>

<script>
import * as API from './RoomAPI.js'
export default {
  name: 'RoomAdd',
  data () {
    return {
      bottomPopup: false,
      icon: '',
      color: '',
      nameA: '',
      nameOrder: '1班',
      yearList: [],
      phone: '',
      weixin: '',
      email: '',
      remark: '',
      message: '',
      nameErrorText: '',
      loginErrorText: '',
      phoneErrorText: '',
      weixinErrorText: '',
      emailErrorText: '',
      nameErrorColor: '',
      loginErrorColor: '',
      phoneErrorColor: '',
      weixinErrorColor: '',
      emailErrorColor: ''
    }
  },
   created () {
    this.list = [
      new Date().getFullYear() - 7,
      new Date().getFullYear() - 6,
      new Date().getFullYear() - 5,
      new Date().getFullYear() - 4,
      new Date().getFullYear() - 3,
      new Date().getFullYear() - 2,
      new Date().getFullYear() - 1,
      new Date().getFullYear()
    ]
  },
  methods: {
    reply () {
      this.$router.push({ path: '/roomList' })
    },
    openPopup (message, icon, color) {
      this.message = message
      this.icon = icon
      this.color = color
      this.bottomPopup = true
      setTimeout(() => { this.bottomPopup = false }, 1500)
    },
    reset () {
      this.name = ''
      this.phone = ''
      this.weixin = ''
      this.email = ''
      this.remark = ''
      this.nameErrorText = ''
      this.loginErrorText = ''
      this.phoneErrorText = ''
      this.weixinErrorText = ''
      this.emailErrorText = ''
      this.remarkErrorText = ''
      this.nameErrorColor = ''
      this.loginErrorColor = ''
      this.phoneErrorColor = ''
      this.weixinErrorColor = ''
      this.emailErrorColor = ''
      this.remarkErrorColor = ''
    },
    checkName (value) {
      this.$http.get(
        API.CheckNameForNew,
        { params: {
          name: value
        }
        },
        {
          headers:
          {
            'X-Requested-With': 'XMLHttpRequest'
          }
        }).then((response) => {
          switch (response.body) {
            case '0':
              this.nameErrorText = ''
              this.nameErrorColor = 'green'
              break
            case '1':
              this.nameErrorText = '请输入两个以上汉字！'
              this.nameErrorColor = 'red'
              break
            case '2':
              this.nameErrorText = '该姓名存在重名情况！'
              this.nameErrorColor = 'orange'
              break
            default:
              this.nameErrorText = ''
              this.nameErrorColor = 'blue'
              window.location.href = '/'
          }
        }, (response) => {
          this.openPopup('服务器内部错误！', 'report_problem', 'orange')
        })
    },
    checkLogin (value) {
      this.$http.get(
        API.CheckLoginForNew,
        { params: {
          login: value
        }
        },
        {
          headers:
          {
            'X-Requested-With': 'XMLHttpRequest'
          }
        }).then((response) => {
          switch (response.body) {
            case '0':
              this.weixinErrorColor = 'green'
              break
            case '1':
              this.weixinErrorText = '登录名称不应包含中文！'
              this.weixinErrorColor = 'red'
              break
            case '2':
              this.weixinErrorText = '该登录名称已使用，请更换！'
              this.weixinErrorColor = 'orange'
              break
            default:
              this.weixinErrorText = ''
              this.weixinErrorColor = 'blue'
              window.location.href = '/'
          }
        }, (response) => {
          this.openPopup('服务器内部错误！', 'report_problem', 'orange')
        })
    },
    checkPhone (value) {
      this.$http.get(
        API.CheckPhoneForNew,
        { params: {
          phone: value
        }
        },
        {
          headers:
          {
            'X-Requested-With': 'XMLHttpRequest'
          }
        }).then((response) => {
          switch (response.body) {
            case '0':
              this.phoneErrorText = '此处为绑定微信的手机号码！'
              this.phoneErrorColor = 'green'
              break
            case '1':
              this.phoneErrorText = '请输入11位数字！'
              this.phoneErrorColor = 'red'
              break
            case '2':
              this.phoneErrorText = '该手机号码已使用，请更换！'
              this.phoneErrorColor = 'orange'
              break
            default:
              this.phoneErrorText = ''
              this.phoneErrorColor = 'blue'
              window.location.href = '/'
          }
        }, (response) => {
          this.openPopup('服务器内部错误！', 'report_problem', 'orange')
        })
    },
    checkWeixin (value) {
      this.$http.get(
        API.CheckWeixinForNew,
        { params: {
          weixin: value
        }
        },
        {
          headers:
          {
            'X-Requested-With': 'XMLHttpRequest'
          }
        }).then((response) => {
          switch (response.body) {
            case '0':
              this.weixinErrorText = '此处为微信号，不是微信昵称！'
              this.weixinErrorColor = 'green'
              break
            case '1':
              this.weixinErrorText = '微信号不应包含中文！'
              this.weixinErrorColor = 'red'
              break
            case '2':
              this.weixinErrorText = '该微信号已使用，请更换！'
              this.weixinErrorColor = 'orange'
              break
            default:
              this.weixinErrorText = ''
              this.weixinErrorColor = 'blue'
              window.location.href = '/'
          }
        }, (response) => {
          this.openPopup('服务器内部错误！', 'report_problem', 'orange')
        })
    },
    checkEmail (value) {
      this.$http.get(
        API.CheckEmailForNew,
        { params: {
          email: value
        }
        },
        {
          headers:
          {
            'X-Requested-With': 'XMLHttpRequest'
          }
        }).then((response) => {
          switch (response.body) {
            case '0':
              this.emailErrorText = ''
              this.emailErrorColor = 'green'
              break
            case '1':
              this.emailErrorText = '电子邮箱格式错误！'
              this.emailErrorColor = 'red'
              break
            case '2':
              this.emailErrorText = '该电子邮箱已使用，请更换！'
              this.emailErrorColor = 'orange'
              break
            default:
              this.emailErrorText = ''
              this.emailErrorColor = 'blue'
              window.location.href = '/'
          }
        }, (response) => {
          this.openPopup('服务器内部错误！', 'report_problem', 'orange')
        })
    },
    save () {
      this.$http.get(
        API.Save,
        { params: {
          name: this.name,
          login: this.login,
          phone: this.phone,
          weixin: this.weixin,
          email: this.email,
          remark: this.remark
        }
        },
        {
          headers:
          {
            'X-Requested-With': 'XMLHttpRequest'
          }
        }).then((response) => {
          if (response.body === 0) {
            this.openPopup('保存成功！', 'check_circle', 'green')
            setTimeout(() => { this.$router.push({ path: '/teacherList' }) }, 1000)
          } else {
            this.openPopup(response.body, 'report_problem', 'orange')
          }
        }, (response) => {
          this.openPopup('服务器内部错误！', 'report_problem', 'orange')
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
