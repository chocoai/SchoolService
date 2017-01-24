<template>
  <div class="ParentAdd">
    <mu-appbar title="请核实后输入以下信息">
      <mu-icon-button icon='reply' slot="right" @click="reply"/>
    </mu-appbar>
    <mu-text-field label="姓名" :disabled="saved" underlineShow="false" v-model="name" :errorColor="nameErrorColor" :errorText="nameErrorText" @input="checkName" fullWidth labelFloat icon="person"/><br/>
    <mu-text-field label="账号" :disabled="saved" underlineShow="false" v-model="userId" :errorColor="userIdErrorColor" :errorText="userIdErrorText" @input="checkUserId" fullWidth labelFloat icon="assignment"/><br/>
    <mu-text-field label="手机" :disabled="saved" underlineShow="false" v-model="mobile" :errorColor="mobileErrorColor" :errorText="mobileErrorText" @input="checkMobile" fullWidth labelFloat icon="phone" maxLength="11"/><br/>
    <mu-text-field label="住址" :disabled="saved" underlineShow="false" v-model="address" fullWidth labelFloat icon="phone"/><br/>
    <mu-text-field label="工作" :disabled="saved" underlineShow="false" v-model="work" fullWidth labelFloat icon="phone"/><br/>
    <mu-dialog :open="forSave" title="正在保存" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="cached" @click="reset" backgroundColor="orange"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="done" @click="save" backgroundColor="green"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-popup position="bottom" :overlay="false" popupClass="popup-bottom" :open="bottomPopup">
      <mu-icon :value="icon" :size="36" :color="color"/>&nbsp;{{ message }}
    </mu-popup>
  </div>
</template>

<script>
import * as API from './ParentAPI.js'
export default {
  name: 'ParentAdd',
  data () {
    return {
      bottomPopup: false,
      saved: false,
      forSave: false,
      icon: '',
      color: '',
      name: '',
      userId: '',
      mobile: '',
      address: '',
      work: '',
      message: '',
      isManager: '0',
      nameErrorText: '',
      userIdErrorText: '',
      mobileErrorText: '',
      nameErrorColor: '',
      userIdErrorColor: '',
      mobileErrorColor: ''
    }
  },
  methods: {
    reply () {
      this.$router.push({ path: '/parentList' })
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
      this.userId = ''
      this.mobile = ''
      this.address = ''
      this.work = ''
      this.remark = ''
      this.saved = false
      this.nameErrorText = ''
      this.userIdErrorText = ''
      this.mobileErrorText = ''
      this.nameErrorColor = ''
      this.userIdErrorColor = ''
      this.mobileErrorColor = ''
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
          if (response.body === 'error') {
            this.openPopup('请重新登录!', 'report_problem', 'red')
            window.location.href = '/'
          } else if (response.body === 'OK') {
            this.nameErrorText = ''
            this.nameErrorColor = 'green'
          } else {
            this.nameErrorText = response.body
            this.nameErrorColor = 'red'
          }
        }, (response) => {
          this.openPopup('服务器内部错误!', 'report_problem', 'orange')
        })
      this.$http.get(
        API.GetUserId,
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
          if (value === '') {
            this.userId = ''
          } else {
            this.userId = response.body
          }
        }, (response) => {
          this.openPopup('服务器内部错误!', 'report_problem', 'orange')
        })
    },
    checkUserId (value) {
      this.$http.get(
        API.CheckUserIdForNew,
        { params: {
          userId: value
        }
        },
        {
          headers:
          {
            'X-Requested-With': 'XMLHttpRequest'
          }
        }).then((response) => {
          if (response.body === 'error') {
            this.openPopup('请重新登录!', 'report_problem', 'red')
            window.location.href = '/'
          } else if (response.body === 'OK') {
            this.userIdErrorText = ''
            this.userIdErrorColor = 'green'
          } else {
            this.userIdErrorText = response.body
            this.userIdErrorColor = 'red'
          }
        }, (response) => {
          this.openPopup('服务器内部错误!', 'report_problem', 'orange')
        })
    },
    checkMobile (value) {
      this.$http.get(
        API.CheckMobileForNew,
        { params: {
          mobile: value
        }
        },
        {
          headers:
          {
            'X-Requested-With': 'XMLHttpRequest'
          }
        }).then((response) => {
          if (response.body === 'error') {
            this.openPopup('请重新登录!', 'report_problem', 'red')
            window.location.href = '/'
          } else if (response.body === 'OK') {
            this.mobileErrorText = '此处应填写微信绑定的手机号码.'
            this.mobileErrorColor = 'green'
          } else {
            this.mobileErrorText = response.body
            this.mobileErrorColor = 'red'
          }
        }, (response) => {
          this.openPopup('服务器内部错误!', 'report_problem', 'orange')
        })
    },
    save () {
      this.forSave = true
      this.$http.get(
        API.Save,
        { params: {
          name: this.name,
          userId: this.userId,
          mobile: this.mobile,
          isManager: this.isManager
        }
        },
        {
          headers:
          {
            'X-Requested-With': 'XMLHttpRequest'
          }
        }).then((response) => {
          this.saved = true
          this.forSave = false
          if (response.body === 'error') {
            this.openPopup('请重新登录!', 'report_problem', 'red')
            window.location.href = '/'
          } else if (response.body === 'OK') {
            this.openPopup('保存成功！', 'check_circle', 'green')
            setTimeout(() => { this.$router.push({ path: '/teacherList' }) }, 1000)
          } else {
            this.openPopup(response.body, 'report_problem', 'orange')
          }
          this.saved = false
          this.forSave = false
        }, (response) => {
          this.forSave = false
          this.openPopup('服务器内部错误!', 'report_problem', 'orange')
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
    height: 150px;
    text-align: center;
    line-height: 32px;
  }
</style>
