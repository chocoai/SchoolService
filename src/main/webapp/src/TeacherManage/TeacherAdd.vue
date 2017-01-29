<template>
  <div class="TeacherAdd">
    <mu-appbar title="请核实后输入以下信息">
      <mu-icon-button icon='reply' slot="right" @click="gorReply"/>
    </mu-appbar>
    <mu-text-field label="姓名" :disabled="saved" underlineShow="false" v-model="name" :errorColor="nameErrorColor" :errorText="nameErrorText" @input="checkName" fullWidth labelFloat icon="person"/><br/>
    <mu-text-field label="账号" :disabled="saved" underlineShow="false" v-model="userId" :errorColor="userIdErrorColor" :errorText="userIdErrorText" @input="checkUserId" fullWidth labelFloat icon="assignment"/><br/>
    <mu-text-field label="手机" :disabled="saved" underlineShow="false" v-model="mobile" :errorColor="mobileErrorColor" :errorText="mobileErrorText" @input="checkMobile" fullWidth labelFloat icon="phone" maxLength="11"/><br/>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-switch label="管理人员" v-model="isManager" :disabled="saved"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-switch label="学生家长" v-model="isParent" :disabled="saved"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-dialog :open="forSave" title="正在保存" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="cached" @click="goReset" backgroundColor="orange"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="done" @click="goSave" backgroundColor="green"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-popup position="bottom" :overlay="false" popupClass="popup-bottom" :open="bottomPopup">
      <mu-icon :value="icon" :size="36" :color="color"/>&nbsp;{{ message }}
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
      saved: false,
      forSave: false,
      icon: '',
      color: '',
      name: '',
      userId: '',
      mobile: '',
      message: '',
      isManager: false,
      isParent: false,
      isManagers: '',
      isParents: '',
      nameErrorText: '',
      userIdErrorText: '',
      mobileErrorText: '',
      nameErrorColor: '',
      userIdErrorColor: '',
      mobileErrorColor: ''
    }
  },
  computed: {
    isManagers: function () {
      if (this.isManager) {
        return '1'
      } else {
        return '0'
      }
    },
    isParents: function () {
      if (this.isParent) {
        return '1'
      } else {
        return '0'
      }
    }
  },
  methods: {
    gorReply () {
      this.$router.push({ path: '/teacherList' })
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
      this.userId = ''
      this.mobile = ''
      this.remark = ''
      this.saved = false
      this.isManager = false
      this.isParent = false
      this.nameErrorText = ''
      this.userIdErrorText = ''
      this.mobileErrorText = ''
      this.nameErrorColor = ''
      this.userIdErrorColor = ''
      this.mobileErrorColor = ''
    },
    checkName (value) {
      if (value === null || value === undefined || value === '') {
        this.nameErrorText = '姓名为必填项!'
        this.nameErrorColor = 'orange'
        this.userId = ''
      } else if (!/^[\u4e00-\u9fa5]{2,}$/.test(value)) {
        this.nameErrorText = '姓名应为2个以上汉字'
        this.nameErrorColor = 'orange'
        this.userId = ''
      } else {
        this.$http.get(
          API.GetUserId,
          { params: { name: value } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          this.userId = response.body
        }, (response) => {
          this.openPopup('服务器内部错误!', 'error', 'red')
        })
      }
    },
    checkUserId (value) {
      if (value === null || value === undefined || value === '') {
        this.userIdErrorText = '账号为必填项!'
        this.userIdErrorColor = 'orange'
      } else if (!/^[A-Za-z0-9]+$/.test(value)) {
        this.userIdErrorText = '账号名应为字母或数字的组合!'
        this.userIdErrorColor = 'orange'
      } else {
        this.$http.get(
          API.CheckUserIdForNew,
          { params: { userId: value } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          if (response.body === 'error') {
            this.openPopup('请重新登录!', 'report_problem', 'orange')
            window.location.href = '/'
          } else if (response.body === 'OK') {
            this.userIdErrorText = ''
            this.userIdErrorColor = 'green'
          } else {
            this.userIdErrorText = response.body
            this.userIdErrorColor = 'red'
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
      } else if (!/^1(3|4|5|7|8)\d{9}$/.test(value)) {
        this.mobileErrorText = '手机号码格式错误!'
        this.mobileErrorColor = 'orange'
      } else {
        this.$http.get(
          API.CheckMobileForNew,
          { params: { mobile: value } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          if (response.body === 'error') {
            this.openPopup('请重新登录!', 'report_problem', 'orange')
            window.location.href = '/'
          } else if (response.body === 'OK') {
            this.mobileErrorText = ''
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
    goSave () {
      this.forSave = true
      this.$http.get(
        API.Save,
        { params: {
          name: this.name,
          userId: this.userId,
          mobile: this.mobile,
          isManager: this.isManagers,
          isParent: this.isParents
        } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        this.saved = true
        this.forSave = false
        if (response.body === 'error') {
          this.openPopup('请重新登录!', 'report_problem', 'orange')
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
    height: 150px;
    text-align: center;
    line-height: 32px;
  }
</style>
