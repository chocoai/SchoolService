<template>
  <div class="StudentAdd">
    <mu-appbar title="请核实后输入以下信息">
      <mu-icon-button icon='reply' slot="right" @click="goReply"/>
    </mu-appbar>
    <mu-text-field v-model="name" label="姓名" icon="comment" :errorColor="nameErrorColor" :errorText="nameErrorText" @input="checkName" fullWidth labelFloat/><br/>
    <mu-text-field v-model="number" label="证件号码" icon="comment" :errorColor="numberErrorColor" :errorText="numberErrorText" @input="checkNumber" fullWidth labelFloat maxLength="18"/><br/>
    <mu-text-field v-model="code" label="学籍号码" icon="comment" :errorColor="codeErrorColor" :errorText="codeErrorText" @input="checkCode" fullWidth labelFloat maxLength="15"/><br/>
    <mu-select-field hintText="身份" icon="settings" v-model="identity_id" fullWidth>
      <mu-menu-item v-for="indentity in identities" :value="indentity.id" :title="indentity.name"/>
    </mu-select-field>
    <mu-popup position="bottom" :overlay="false" popupClass="popup-bottom" :open="bottomPopup">
      <mu-icon :value="icon" :size="36" :color="color"/>&nbsp;{{ message }}
    </mu-popup>
    <mu-dialog :open="forSave" title="正在保存" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-dialog :open="forRead" title="正在读取" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="cached" @click="goReset" backgroundColor="orange"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="done" @click="goSave" :disabled="saveAble" backgroundColor="green"/>
      </mu-flexbox-item>
    </mu-flexbox>
  </div>
</template>

<script>
import * as API from './StudentAPI.js'
export default {
  name: 'StudentAdd',
  data () {
    return {
      bottomPopup: false,
      forSave: false,
      forRead: true,
      icon: '',
      color: '',
      name: '',
      number: '',
      code: '',
      identity_id: '1',
      identities: [],
      message: '',
      nameErrorText: '',
      nameErrorColor: '',
      numberErrorText: '',
      numberErrorColor: '',
      codeErrorText: '',
      codeErrorColor: '',
      saveAble: true
    }
  },
  created: function () {
    this.$http.get(
      API.IdentityList,
      { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
    ).then((response) => {
      this.identities = response.body
      this.forRead = false
    }, (response) => {
      this.openPopup('服务器内部错误！', 'error', 'red')
    })
  },
  computed: {
    saveAble: function () {
      if (this.nameErrorText.toString() === 'OK' && this.numberErrorText.toString() === 'OK' && this.codeErrorText.toString() === 'OK') {
        return false
      } else {
        return true
      }
    }
  },
  methods: {
    goReply () {
      this.$router.push({ path: '/studentList' })
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
      this.nameErrorColor = ''
      this.number = ''
      this.numberErrorColor = ''
      this.code = ''
      this.codeErrorColor = ''
      this.identity_id = '1'
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
        this.nameErrorText = '证件号码为必填项!'
        this.nameErrorColor = 'orange'
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
      this.forSave = true
      this.$http.get(
        API.Add,
        { params: {
          name: this.name,
          number: this.number,
          code: this.code,
          identity_id: this.identity_id
        } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        this.forSave = false
        if (response.body === 'error') {
          this.openPopup('请重新登录!', 'report_problem', 'orange')
          window.location.href = '/'
        } else if (response.body === 'OK') {
          this.openPopup('保存成功！', 'check_circle', 'green')
          setTimeout(() => { this.$router.push({ path: '/studentList' }) }, 1000)
        } else {
          this.openPopup(response.body, 'report_problem', 'orange')
        }
      }, (response) => {
        this.forSave = false
        this.openPopup('服务器内部错误！', 'error', 'red')
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
