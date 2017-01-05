<template>
  <div class="AddField">
    <form>
      <mu-appbar title="新增教师">
        <mu-flat-button color="white" label="保存" slot="right"/>
      </mu-appbar>
      <mu-paper>
        <mu-text-field label="教师姓名" :errorColor="nameErrorColor" :errorText="nameErrorText" @input="checkName" fullWidth labelFloat icon="person"/><br/>
        <mu-text-field label="联系电话" :errorColor="phoneErrorColor" :errorText="phoneErrorText" @input="checkPhone" fullWidth labelFloat icon="phone" maxLength="11"/><br/>
        <mu-text-field label="微信号码" :errorColor="weixinErrorColor" :errorText="weixinErrorText" @input="checkWeixin" fullWidth labelFloat icon="chat"/><br/>
        <mu-text-field label="电子邮箱" :errorColor="emailErrorColor" :errorText="emailErrorText" @input="checkEmail" fullWidth labelFloat icon="email"/><br/>
        <mu-text-field label="备注信息" fullWidth labelFloat icon="bookmark"/><br/>
      </mu-paper>
    </form>
    <back iconName="reply" hrefLocation="#/"></back>
    <mu-toast v-if="toast" message="服务器内部错误!" @close="hideToast"/>
  </div>
</template>

<script>
import * as AF from '../Util/AjaxFunction.js'
import Back from './CircleBtn'
export default {
  name: 'AddField',
  components: {
    'back': Back
  },
  data () {
    return {
      toast: false,
      nameErrorText: '',
      phoneErrorText: '',
      weixinErrorText: '',
      emailErrorText: '',
      nameErrorColor: '',
      phoneErrorColor: '',
      weixinErrorColor: '',
      emailErrorColor: ''
    }
  },
  methods: {
    hideToast () {
      this.toast = false
      if (this.toastTimer) clearTimeout(this.toastTimer)
    },
    checkName (value) {
      this.$http.get(
        AF.TeacherCheckName,
        { params: {
          name: value
        }
        }).then((response) => {
          switch (response.body) {
            case '0':
              this.nameErrorText = ''
              this.nameErrorColor = 'green'
              break
            case '1':
              this.nameErrorText = '请输入汉字！'
              this.nameErrorColor = 'red'
              break
            case '2':
              this.nameErrorText = '请输入两个以上汉字！'
              this.nameErrorColor = 'red'
              break
            case '3':
              this.nameErrorText = '该姓名存在重名情况！'
              this.nameErrorColor = 'orange'
              break
            default:
              this.nameErrorText = ''
              this.nameErrorColor = 'blue'
          }
        }, (response) => {
          this.toast = true
          if (this.toastTimer) clearTimeout(this.toastTimer)
          this.toastTimer = setTimeout(() => { this.toast = false }, 1000)
        })
    },
    checkPhone (value) {
      this.$http.get(
        AF.TeacherCheckPhone,
        { params: {
          phone: value
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
          }
        }, (response) => {
          this.toast = true
          if (this.toastTimer) clearTimeout(this.toastTimer)
          this.toastTimer = setTimeout(() => { this.toast = false }, 1000)
        })
    },
    checkWeixin (value) {
      this.$http.get(
        AF.TeacherCheckWeixin,
        { params: {
          weixin: value
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
          }
        }, (response) => {
          this.toast = true
          if (this.toastTimer) clearTimeout(this.toastTimer)
          this.toastTimer = setTimeout(() => { this.toast = false }, 1000)
        })
    },
    checkEmail (value) {
      this.$http.get(
        AF.TeacherCheckEmail,
        { params: {
          email: value
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
          }
        }, (response) => {
          this.toast = true
          if (this.toastTimer) clearTimeout(this.toastTimer)
          this.toastTimer = setTimeout(() => { this.toast = false }, 1000)
        })
    }
  }
}
</script>
