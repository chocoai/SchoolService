<template>
  <div>
    <mu-appbar title="创建新课程">
      <mu-icon-button icon='reply' slot="right" @click="gorReply" v-if="permission.Page"/>
    </mu-appbar>
    <mu-text-field label="课程名称" v-model="name" :errorColor="nameErrorColor" :errorText="nameErrorText" @input="checkName" fullWidth labelFloat icon="title" maxLength="20"/><br/>
    <mu-text-field label="课程描述" v-model="detail" :errorColor="detailErrorColor" :errorText="detailErrorText" @input="checkDetail" fullWidth labelFloat icon="title" maxLength="100" multiLine :rows="3" :rowsMax="6"/><br/>
    <mu-text-field label="选课人数" v-model="amount" fullWidth labelFloat icon="supervisor_account" type="number" max="60" min="0"/><br/>
    <mu-select-field label="课程类别" icon="settings" v-model="type" fullWidth>
      <mu-menu-item value="1" title="必修课"/>
      <mu-menu-item value="2" title="选修课"/>
    </mu-select-field>
    <mu-select-field label="课程状态" icon="settings" v-model="state" fullWidth disabled >
      <mu-menu-item value="1" title="激活"/>
      <mu-menu-item value="0" title="注销"/>
    </mu-select-field>
    <mu-dialog :open="Saving" title="正在保存" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="save" @click="goSave" backgroundColor="green" v-if="permission.Save"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="cached" @click="goReset" backgroundColor="orange"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-popup position="bottom" :overlay="false" popupClass="popup-bottom" :open="bottomPopup">
      <mu-icon :value="icon" :size="36" :color="color"/>&nbsp;{{ message }}
    </mu-popup>
  </div>
</template>

<script>
import * as API from './API.js'
import { getCookie } from '../../../cookieUtil.js'
export default {
  name: 'Add',
  data () {
    return {
      bottomPopup: false,
      Saving: false,
      permission: [],
      menu: [],
      icon: '',
      color: '',
      name: '',
      detail: '',
      amount: 0,
      type: '1',
      state: '1',
      message: '',
      nameErrorText: '',
      nameErrorColor: '',
      detailErrorText: '',
      detailErrorColor: ''
    }
  },
  created: function () {
    if (getCookie('MenuMobile') === null || getCookie('MenuMobile') === undefined || getCookie('MenuMobile') === '' || getCookie(API.base) === null || getCookie(API.base) === undefined || getCookie(API.base) === '') {
      this.$http.get(
        API.menu
      ).then((response) => {
        if (response.body.toString() === 'illegal' || response.body.toString() === 'overdue') {
          this.openPopup('登录过期或非法操作!', 'error', 'red')
        } else {
          this.$http.get(
            API.permission
          ).then((res) => {
            this.permission = JSON.parse(JSON.parse(getCookie(API.base)))
            this.menu = JSON.parse(JSON.parse(getCookie('MenuMobile')))
          }, (res) => {
            this.openPopup('服务器内部错误!', 'error', 'red')
          })
        }
      }, (response) => {
        this.openPopup('服务器内部错误!', 'error', 'red')
      })
    } else {
      this.permission = JSON.parse(JSON.parse(getCookie(API.base)))
      this.menu = JSON.parse(JSON.parse(getCookie('MenuMobile')))
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
      this.detail = ''
      this.detailErrorText = ''
      this.detailErrorColor = ''
      this.type = '1'
      this.state = '1'
      this.amount = 0
    },
    checkName (value) {
      if (value === null || value === undefined || value === '') {
        this.nameErrorText = '名称为必填项!'
        this.nameErrorColor = 'orange'
      } else if (value.length > 20) {
        this.nameErrorText = '名称不得超过20字符'
        this.nameErrorColor = 'orange'
      } else {
        this.$http.get(
          API.checkNameForAdd,
          { params: { name: value } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          if (response.body === 'illegal' || response.body.toString() === 'overdue') {
            this.openPopup('请重新登录!', 'report_problem', 'orange')
            window.location.href = '/MainMobile'
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
    checkDetail (value) {
      if (value === null || value === undefined || value === '') {
        this.detailErrorText = '课程表述为必填项!'
        this.detailErrorColor = 'orange'
      } else if (value.length > 100) {
        this.detailErrorText = '课程表述不得超过100字符'
        this.detailErrorColor = 'orange'
      } else {
        this.detailErrorText = 'OK'
        this.detailErrorColor = 'green'
      }
    },
    goSave () {
      this.Saving = true
      this.$http.get(
        API.save,
        { params: {
          name: this.name,
          detail: this.detail,
          amount: this.amount,
          type: this.type,
          state: this.state
        } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        this.Saving = false
        if (response.body === 'illegal' || response.body.toString() === 'overdue') {
          this.openPopup('请重新登录!', 'report_problem', 'orange')
          window.location.href = '/MainMobile'
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
