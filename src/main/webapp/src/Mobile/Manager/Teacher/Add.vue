<template>
  <div>
    <mu-appbar title="添加新教师">
      <mu-icon-button icon='reply' slot="right" @click="gorReply"/>
    </mu-appbar>
    <mu-text-field label="姓名" v-model="name" :errorColor="nameErrorColor" :errorText="nameErrorText" @input="checkName" fullWidth labelFloat icon="person"/><br/>
    <mu-text-field label="手机" v-model="mobile" :errorColor="mobileErrorColor" :errorText="mobileErrorText" @input="checkMobile" fullWidth labelFloat icon="phone" maxLength="11"/><br/>
    <mu-text-field label="微信" v-model="wx" :errorColor="wxErrorColor" :errorText="wxErrorText" @input="checkWX" fullWidth labelFloat icon="email" /><br/>
    <mu-text-field label="QQ" v-model="qq" :errorColor="qqErrorColor" :errorText="qqErrorText" @input="checkQQ" fullWidth labelFloat icon="email" /><br/>
    <mu-text-field label="电子邮箱" v-model="email" :errorColor="emailErrorColor" :errorText="emailErrorText" @input="checkEmail" fullWidth labelFloat icon="email" /><br/>
    <mu-select-field hintText="类别" v-model="type" icon="settings" fullWidth>
      <mu-menu-item value="0" title="一般教师"/>
      <mu-menu-item value="1" title="管理人员"/>
    </mu-select-field>
    <mu-dialog :open="Saving" title="正在保存" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="cached" @click="goReset" backgroundColor="orange"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button :disabled="Save_Able" icon="save" @click="goSave" backgroundColor="green"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-popup position="bottom" :overlay="false" popupClass="popup-bottom" :open="bottomPopup">
      <mu-icon :value="icon" :size="36" :color="color"/>&nbsp;{{ message }}
    </mu-popup>
  </div>
</template>

<script>
import * as API from './API.js'
export default {
  name: 'Add',
  data () {
    return {
      bottomPopup: false,
      Saving: false,
      icon: '',
      color: '',
      name: '',
      mobile: '',
      wx: '',
      qq: '',
      email: '',
      type: '0',
      message: '',
      nameErrorText: '',
      nameErrorColor: '',
      mobileErrorText: '',
      mobileErrorColor: '',
      wxErrorText: '',
      wxErrorColor: '',
      qqErrorText: '',
      qqErrorColor: '',
      emailErrorText: '',
      emailErrorColor: ''
    }
  },
  computed: {
    Save_Able: function () {
      if (this.nameErrorText.toString() === 'OK' && this.mobileErrorText.toString() === 'OK') {
        return false
      } else {
        return true
      }
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
      this.qq = ''
      this.qqErrorText = ''
      this.qqErrorColor = ''
      this.wx = ''
      this.wxErrorText = ''
      this.wxErrorColor = ''
      this.email = ''
      this.emailErrorText = ''
      this.emailErrorColor = ''
      this.type = '0'
    },
    checkName (value) {
      if (value === null || value === undefined || value === '') {
        this.nameErrorText = '姓名为必填项!'
        this.nameErrorColor = 'orange'
      } else if (value.length > 20) {
        this.nameErrorText = '姓名不得超过20字符'
        this.nameErrorColor = 'orange'
      } else {
        this.$http.get(
          API.checkName,
          { params: { name: value } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          if (response.body === 'error') {
            this.openPopup('请重新登录!', 'report_problem', 'orange')
            window.location.href = '/'
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
    checkEmail (value) {
      if (value === null || value === undefined || value === '') {
        this.emailErrorText = ''
        this.emailErrorColor = ''
      } else if (!value.match(/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$/)) {
        this.emailErrorText = '电子邮箱格式错误!'
        this.emailErrorColor = 'orange'
      } else {
        this.$http.get(
          API.checkEmailForAdd,
          { params: { email: value } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          if (response.body === 'error') {
            this.openPopup('请重新登录!', 'report_problem', 'orange')
            window.location.href = '/'
          } else if (response.body === 'OK') {
            this.emailErrorText = 'OK'
            this.emailErrorColor = 'green'
          } else {
            this.emailErrorText = response.body
            this.emailErrorColor = 'red'
          }
        }, (response) => {
          this.openPopup('服务器内部错误!', 'error', 'red')
        })
      }
    },
    checkQQ (value) {
      if (value === null || value === undefined || value === '') {
        this.qqErrorText = ''
        this.qqErrorColor = ''
      } else if (!value.match(/^\d{5,12}$/)) {
        this.qqErrorText = 'QQ格式错误!'
        this.qqErrorColor = 'orange'
      } else {
        this.$http.get(
          API.checkQQForAdd,
          { params: { qq: value } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          if (response.body === 'error') {
            this.openPopup('请重新登录!', 'report_problem', 'orange')
            window.location.href = '/'
          } else if (response.body === 'OK') {
            this.qqErrorText = 'OK'
            this.qqErrorColor = 'green'
          } else {
            this.qqErrorText = response.body
            this.qqErrorColor = 'red'
          }
        }, (response) => {
          this.openPopup('服务器内部错误!', 'error', 'red')
        })
      }
    },
    checkWX (value) {
      if (value === null || value === undefined || value === '') {
        this.wxErrorText = ''
        this.wxErrorColor = ''
      } else if (!value.match(/^[a-zA-Z]{1}[-_a-zA-Z0-9]{5,19}+$/)) {
        this.wxErrorText = '微信格式错误!'
        this.wxErrorColor = 'orange'
      } else {
        this.$http.get(
          API.checkWXForAdd,
          { params: { wx: value } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          if (response.body === 'error') {
            this.openPopup('请重新登录!', 'report_problem', 'orange')
            window.location.href = '/'
          } else if (response.body === 'OK') {
            this.wxErrorText = 'OK'
            this.wxErrorColor = 'green'
          } else {
            this.wxErrorText = response.body
            this.wxErrorColor = 'red'
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
          email: this.email,
          qq: this.qq,
          wx: this.wx,
          type: this.type
        } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        this.Saving = false
        if (response.body === 'error') {
          this.openPopup('请重新登录!', 'report_problem', 'orange')
          window.location.href = '/'
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
