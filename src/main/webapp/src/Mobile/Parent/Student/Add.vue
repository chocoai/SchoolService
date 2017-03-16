<template>
  <div>
    <mu-appbar title="关联新学生">
      <mu-icon-button icon='reply' slot="right" @click="gorReply"/>
    </mu-appbar>
    <mu-text-field v-model="name" label="姓名" icon="comment" :errorColor="nameErrorColor" :errorText="nameErrorText" @input="checkName" fullWidth labelFloat/><br/>
    <mu-text-field v-model="number" label="证件号码" icon="comment" :errorColor="numberErrorColor" :errorText="numberErrorText" @input="checkNumber" fullWidth labelFloat maxLength="18"/><br/>
    <mu-text-field v-model="code" label="学籍号码" icon="comment" :errorColor="codeErrorColor" :errorText="codeErrorText" @input="checkCode" fullWidth labelFloat maxLength="15"/><br/>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-raised-button :label="identityName" @click="openIdentity=true" primary/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-dialog :open="Reading" title="正在读取" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
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
    <mu-drawer right :open="openIdentity" docked="false">
      <mu-appbar title="请选择您的身份"/>
      <mu-list :value="identity_id" @itemClick="identityChange">
        <mu-list-item v-for="identity in identities" :title="identity.name" :value="identity.id">
          <mu-icon v-if="identity.id.toString() === '1'"  slot="left" :size="40" color="#009688" value="account_box"/>
          <mu-icon v-if="identity.id.toString() === '2'"  slot="left" :size="40" color="#ff5722" value="account_circle"/>
          <mu-icon v-if="identity.id.toString() === '3'"  slot="left" :size="40" color="#4caf50" value="account_box"/>
          <mu-icon v-if="identity.id.toString() === '4'"  slot="left" :size="40" color="#ff9800" value="account_circle"/>
          <mu-icon v-if="identity.id.toString() === '5'"  slot="left" :size="40" color="#8bc34a" value="account_box"/>
          <mu-icon v-if="identity.id.toString() === '6'"  slot="left" :size="40" color="#ffc107" value="account_circle"/>
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
      Reading: true,
      Saving: false,
      icon: '',
      color: '',
      name: '',
      number: '',
      code: '',
      identityName: '请选择您的身份',
      identities: [],
      nameErrorText: '',
      nameErrorColor: '',
      numberErrorText: '',
      numberErrorColor: '',
      codeErrorText: '',
      codeErrorColor: '',
      message: ''
    }
  },
  created: function () {
    this.$http.get(
      API.list,
      { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
    ).then((response) => {
      this.identities = response.body
      this.Reading = false
    }, (response) => {
      this.openPopup('服务器内部错误！', 'error', 'red')
    })
  },
  computed: {
    Save_Able: function () {
      if (this.nameErrorText.toString() === 'OK' && this.numberErrorText.toString() === 'OK' && this.codeErrorText.toString() === 'OK' && this.identityName.toString() !== '请选择您的身份') {
        return false
      } else {
        return true
      }
    }
  },
  methods: {
    identityChange (val) {
      this.identity_id = val.value
      this.identityName = val.title
      this.openIdentity = false
    },
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
      this.identityName = '请选择您的身份'
      this.identity_id = ''
    },
    checkName (value) {
      if (value === null || value === undefined || value === '') {
        this.nameErrorText = '姓名为必填项!'
        this.nameErrorColor = 'orange'
      } else if (!/^[\u4e00-\u9fa5]{2,}$/.test(value)) {
        this.nameErrorText = '姓名应为2个以上汉字'
        this.nameErrorColor = 'orange'
      } else {
        this.nameErrorText = 'OK'
        this.nameErrorColor = 'green'
      }
    },
    checkNumber (value) {
      if (value === null || value === undefined || value === '') {
        this.numberErrorText = '证件号码为必填项!'
        this.numberErrorColor = 'orange'
      } else if (!/\d{17}[0-9,X]/.test(value)) {
        this.numberErrorText = '证件号码应为18位字符'
        this.numberErrorColor = 'orange'
      } else {
        this.numberErrorText = 'OK'
        this.numberErrorColor = 'green'
      }
    },
    checkCode (value) {
      if (value === null || value === undefined || value === '') {
        this.codeErrorText = '学籍号码为必填项!'
        this.codeErrorColor = 'orange'
      } else {
        this.codeErrorText = 'OK'
        this.codeErrorColor = 'green'
      }
    },
    goSave () {
      this.Saving = true
      this.$http.get(
        API.save,
        { params: {
          identity_id: this.identity_id,
          name: this.name,
          number: this.number,
          code: this.code
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
