<template>
  <div class="TeacherAdd">
    <mu-appbar title="请核实后输入以下信息">
      <mu-icon-button icon='reply' slot="right" @click="reply"/>
    </mu-appbar>
    <mu-paper>
      <mu-text-field label="教师姓名" underlineShow="false" v-model="name" :errorColor="nameErrorColor" :errorText="nameErrorText" @input="checkName" fullWidth labelFloat icon="person"/><br/>
      <mu-text-field label="联系电话" underlineShow="false" v-model="phone" :errorColor="phoneErrorColor" :errorText="phoneErrorText" @input="checkPhone" fullWidth labelFloat icon="phone" maxLength="11"/><br/>
      <mu-text-field label="微信号码" underlineShow="false" v-model="weixin" :errorColor="weixinErrorColor" :errorText="weixinErrorText" @input="checkWeixin" fullWidth labelFloat icon="chat"/><br/>
      <mu-text-field label="电子邮箱" underlineShow="false" v-model="email" :errorColor="emailErrorColor" :errorText="emailErrorText" @input="checkEmail" fullWidth labelFloat icon="email"/><br/>
      <mu-text-field label="备注信息" underlineShow="false" v-model="remark" fullWidth labelFloat icon="bookmark"/><br/>
    </mu-paper>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="cached" @click="reset" backgroundColor="orange"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="done" @click="save" backgroundColor="green"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-popup position="bottom" :overlay="false" popupClass="popup-bottom" :open="bottomPopup">
      <mu-icon :value="icon" :size="36" :color="color"/>{{ message }}
    </mu-popup>
  </div>
</template>

<script>
import * as API from './TeacherAPI.js'
export default {
  name: 'TeacherAdd',
  data () {
    return {
      bottomPopup: false,
      icon: '',
      color: '',
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
      this.$router.push({ path: '/' })
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
      this.phoneErrorText = ''
      this.weixinErrorText = ''
      this.emailErrorText = ''
      this.remarkErrorText = ''
      this.nameErrorColor = ''
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
          this.openPopup('服务器内部错误！', 'report_problem', 'orange')
        })
    },
    checkPhone (value) {
      this.$http.get(
        API.CheckPhoneForNew,
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
          this.openPopup('服务器内部错误！', 'report_problem', 'orange')
        })
    },
    checkWeixin (value) {
      this.$http.get(
        API.CheckWeixinForNew,
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
          this.openPopup('服务器内部错误！', 'report_problem', 'orange')
        })
    },
    checkEmail (value) {
      this.$http.get(
        API.CheckEmailForNew,
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
          this.openPopup('服务器内部错误！', 'report_problem', 'orange')
        })
    },
    save () {
      this.$http.get(
        API.Save,
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
              this.openPopup('保存成功！', 'check_circle', 'green')
              setTimeout(() => { this.$router.push({ path: '/' }) }, 1000)
              break
            case '1':
              this.openPopup('姓名为必填项！', 'report_problem', 'orange')
              break
            case '2':
              this.openPopup('输入的姓名应该为两个以上汉字！', 'report_problem', 'orange')
              break
            case '3':
              this.openPopup('手机号码与微信号必须填写其中一项！', 'report_problem', 'orange')
              break
            case '4':
              this.openPopup('输入的手机号码应为11位数字！', 'report_problem', 'orange')
              break
            case '5':
              this.openPopup('输入的手机号码已存在！', 'report_problem', 'orange')
              break
            case '6':
              this.openPopup('输入的微信号不应包含中文！', 'report_problem', 'orange')
              break
            case '7':
              this.openPopup('输入的微信号已存在！', 'report_problem', 'orange')
              break
            case '8':
              this.openPopup('输入的电子邮箱格式不正确！', 'report_problem', 'orange')
              break
            case '9':
              this.openPopup('输入的电子邮箱已存在！', 'report_problem', 'orange')
              break
            default:
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
