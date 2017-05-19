<template>
  <div>
    <mu-appbar title="添加新教师">
      <mu-icon-button icon='reply' slot="right" @click="gorReply"/>
    </mu-appbar>
    <mu-text-field label="姓名" v-model="name" :errorColor="nameErrorColor" :errorText="nameErrorText" @input="checkName" fullWidth labelFloat icon="person"/><br/>
    <mu-text-field label="手机" v-model="mobile" :errorColor="mobileErrorColor" :errorText="mobileErrorText" @input="checkMobile" fullWidth labelFloat icon="phone" maxLength="11"/><br/>
    <mu-text-field label="微信" v-model="weixinId" :errorColor="weixinIdErrorColor" :errorText="weixinIdErrorText" @input="checkWeixinId" fullWidth labelFloat icon="email" /><br/>
    <mu-text-field label="QQ" v-model="qq" :errorColor="qqErrorColor" :errorText="qqErrorText" @input="checkQQ" fullWidth labelFloat icon="email" /><br/>
    <mu-text-field label="电子邮箱" v-model="email" :errorColor="emailErrorColor" :errorText="emailErrorText" @input="checkEmail" fullWidth labelFloat icon="email" /><br/>
    <mu-text-field label="联系地址" v-model="address" fullWidth labelFloat icon="email" /><br/>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-flat-button :label="manager" @click="openManager=true" backgroundColor="#ce93d8" color="#000"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-flat-button :label="types" @click="openType=true" backgroundColor="#ce93d8" color="#000"/>
      </mu-flexbox-item>
    </mu-flexbox>
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
    <mu-drawer right :open="openManager" docked="false">
      <mu-appbar title="请选择管理类别" @click.native="openManager=false">
        <mu-icon-button icon='done' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item title="一般教师">
          <mu-radio  v-model="isManager" label="" labelLeft :nativeValue="0" uncheckIcon="favorite_border" checkedIcon="favorite" slot="right" />
        </mu-list-item>
        <mu-list-item title="管理人员">
          <mu-radio  v-model="isManager" label="" labelLeft :nativeValue="1" uncheckIcon="favorite_border" checkedIcon="favorite" slot="right" />
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-drawer right :open="openType" docked="false">
      <mu-appbar title="请选择人员类别" @click.native="openType=false">
        <mu-icon-button icon='done' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item title="在编教师">
          <mu-radio  v-model="type" label="" labelLeft :nativeValue="1" uncheckIcon="favorite_border" checkedIcon="favorite" slot="right" />
        </mu-list-item>
        <mu-list-item title="聘用教师">
          <mu-radio  v-model="type" label="" labelLeft :nativeValue="2" uncheckIcon="favorite_border" checkedIcon="favorite" slot="right" />
        </mu-list-item>
        <mu-list-item title="外校教师">
          <mu-radio  v-model="type" label="" labelLeft :nativeValue="3" uncheckIcon="favorite_border" checkedIcon="favorite" slot="right" />
        </mu-list-item>
      </mu-list>
    </mu-drawer>
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
      openManager: false,
      openType: false,
      icon: '',
      color: '',
      name: '',
      mobile: '',
      weixinId: '',
      qq: '',
      email: '',
      address: '',
      isManager: '0',
      type: '1',
      message: '',
      nameErrorText: '',
      nameErrorColor: '',
      mobileErrorText: '',
      mobileErrorColor: '',
      weixinIdErrorText: '',
      weixinIdErrorColor: '',
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
    },
    manager: function () {
      if (this.isManager.toString() === '0') {
        return '一般教师'
      } else if (this.isManager.toString() === '1') {
        return '管理人员'
      } else {
        return ''
      }
    },
    types: function () {
      if (this.type.toString() === '1') {
        return '在编教师'
      } else if (this.type.toString() === '2') {
        return '聘用教师'
      } else if (this.type.toString() === '3') {
        return '外校教师'
      } else {
        return ''
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
      this.weixinId = ''
      this.weixinIdErrorText = ''
      this.weixinIdErrorColor = ''
      this.email = ''
      this.emailErrorText = ''
      this.emailErrorColor = ''
      this.isManager = '0'
      this.type = '1'
      this.address = ''
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
    checkWeixinId (value) {
      if (value === null || value === undefined || value === '') {
        this.weixinIdErrorText = ''
        this.weixinIdErrorColor = ''
      } else if (!value.match(/^[a-zA-Z]{1}[-_a-zA-Z0-9]{5,19}$/)) {
        this.weixinIdErrorText = '微信格式错误!'
        this.weixinIdErrorColor = 'orange'
      } else {
        this.$http.get(
          API.checkWeixinIdForAdd,
          { params: { weixinId: value } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          if (response.body === 'error') {
            this.openPopup('请重新登录!', 'report_problem', 'orange')
            window.location.href = '/'
          } else if (response.body === 'OK') {
            this.weixinIdErrorText = 'OK'
            this.weixinIdErrorColor = 'green'
          } else {
            this.weixinIdErrorText = response.body
            this.weixinIdErrorColor = 'red'
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
          weixinId: this.weixinId,
          isManager: this.isManager,
          address: this.address,
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
