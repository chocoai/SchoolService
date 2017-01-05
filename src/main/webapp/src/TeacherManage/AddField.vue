<template>
  <div class="AddField">
    <form>
      <mu-appbar title="请核实后输入以下信息">
        <mu-flat-button color="white" label="返回" slot="right" icon="reply" labelPosition="before" @click="reply"/>
      </mu-appbar>
      <mu-paper>
        <mu-text-field label="教师姓名" underlineShow="false" v-model="name" :errorColor="nameErrorColor" :errorText="nameErrorText" @input="checkName" fullWidth labelFloat icon="person"/><br/>
        <mu-text-field label="联系电话" underlineShow="false" v-model="phone" :errorColor="phoneErrorColor" :errorText="phoneErrorText" @input="checkPhone" fullWidth labelFloat icon="phone" maxLength="11"/><br/>
        <mu-text-field label="微信号码" underlineShow="false" v-model="weixin" :errorColor="weixinErrorColor" :errorText="weixinErrorText" @input="checkWeixin" fullWidth labelFloat icon="chat"/><br/>
        <mu-text-field label="电子邮箱" underlineShow="false" v-model="email" :errorColor="emailErrorColor" :errorText="emailErrorText" @input="checkEmail" fullWidth labelFloat icon="email"/><br/>
        <mu-text-field label="备注信息" underlineShow="false" v-model="remark" fullWidth labelFloat icon="bookmark"/><br/>
      </mu-paper>
    </form>
    <mu-raised-button label="新增" fullWidth @click="save" primary/>
    <mu-toast v-if="toast" :message="message"/>
  </div>
</template>

<script>
import * as AF from '../Util/AjaxFunction.js'
export default {
  name: 'AddField',
  data () {
    return {
      toast: false,
      name: '',
      phone: '',
      weixin: '',
      email: '',
      remark: '',
      message: '',
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
    reply () {
      window.location.href = '#/'
    },
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
          }
        }, (response) => {
          this.message = '服务器内部错误！'
          this.toast = true
          if (this.toastTimer) clearTimeout(this.toastTimer)
          this.toastTimer = setTimeout(() => { this.toast = false; this.message = '' }, 1500)
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
          this.message = '服务器内部错误！'
          this.toast = true
          if (this.toastTimer) clearTimeout(this.toastTimer)
          this.toastTimer = setTimeout(() => { this.toast = false; this.message = '' }, 1500)
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
          this.message = '服务器内部错误！'
          this.toast = true
          if (this.toastTimer) clearTimeout(this.toastTimer)
          this.toastTimer = setTimeout(() => { this.toast = false; this.message = '' }, 1500)
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
          this.message = '服务器内部错误！'
          this.toast = true
          if (this.toastTimer) clearTimeout(this.toastTimer)
          this.toastTimer = setTimeout(() => { this.toast = false; this.message = '' }, 1500)
        })
    },
    save () {
      this.$http.get(
        AF.TeacherSave,
        { params: {
          name: this.name,
          phone: this.phone,
          weixin: this.weixin,
          email: this.email,
          remark: this.remark
        }
        }).then((response) => {
          switch (response.body) {
            case '0':
              window.location.href = '#/'
              break
            case '1':
              this.message = '姓名为必填项！'
              this.toast = true
              if (this.toastTimer) clearTimeout(this.toastTimer)
              this.toastTimer = setTimeout(() => { this.toast = false }, 1500)
              break
            case '2':
              this.message = '输入的姓名应该为两个以上汉字！'
              this.toast = true
              if (this.toastTimer) clearTimeout(this.toastTimer)
              this.toastTimer = setTimeout(() => { this.toast = false; this.message = '' }, 1500)
              break
            case '3':
              this.message = '手机号码与微信号必须填写其中一项！'
              this.toast = true
              if (this.toastTimer) clearTimeout(this.toastTimer)
              this.toastTimer = setTimeout(() => { this.toast = false; this.message = '' }, 1500)
              break
            case '4':
              this.message = '输入的手机号码应为11位数字！'
              this.toast = true
              if (this.toastTimer) clearTimeout(this.toastTimer)
              this.toastTimer = setTimeout(() => { this.toast = false; this.message = '' }, 1500)
              break
            case '5':
              this.message = '输入的手机号码已存在！'
              this.toast = true
              if (this.toastTimer) clearTimeout(this.toastTimer)
              this.toastTimer = setTimeout(() => { this.toast = false; this.message = '' }, 1500)
              break
            case '6':
              this.message = '输入的微信号不应包含中文！'
              this.toast = true
              if (this.toastTimer) clearTimeout(this.toastTimer)
              this.toastTimer = setTimeout(() => { this.toast = false; this.message = '' }, 1500)
              break
            case '7':
              this.message = '输入的微信号已存在！'
              this.toast = true
              if (this.toastTimer) clearTimeout(this.toastTimer)
              this.toastTimer = setTimeout(() => { this.toast = false; this.message = '' }, 1500)
              break
            case '8':
              this.message = '输入的电子邮箱格式不正确！'
              this.toast = true
              if (this.toastTimer) clearTimeout(this.toastTimer)
              this.toastTimer = setTimeout(() => { this.toast = false; this.message = '' }, 1500)
              break
            case '9':
              this.message = '输入的电子邮箱已存在！'
              this.toast = true
              if (this.toastTimer) clearTimeout(this.toastTimer)
              this.toastTimer = setTimeout(() => { this.toast = false; this.message = '' }, 1500)
              break
            default:
          }
        }, (response) => {
          this.message = '服务器内部错误！'
          this.toast = true
          if (this.toastTimer) clearTimeout(this.toastTimer)
          this.toastTimer = setTimeout(() => { this.toast = false; this.message = '' }, 1500)
        })
    }
  }
}
</script>
