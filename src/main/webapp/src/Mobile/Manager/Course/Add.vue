<template>
  <div>
    <mu-appbar title="创建新课程">
      <mu-icon-button icon='reply' slot="right" @click="gorReply"/>
    </mu-appbar>
    <mu-text-field label="课程名称" v-model="name" :errorColor="nameErrorColor" :errorText="nameErrorText" @input="checkName" fullWidth labelFloat icon="title" maxLength="20"/><br/>
    <mu-text-field label="课程描述" v-model="detail" :errorColor="detailErrorColor" :errorText="detailErrorText" @input="checkDetail" fullWidth labelFloat icon="title" maxLength="100" multiLine :rows="3" :rowsMax="6"/><br/>
    <mu-select-field hintText="课程类别" icon="settings" v-model="type" fullWidth>
      <mu-menu-item value="1" title="必修课"/>
      <mu-menu-item value="2" title="选修课"/>
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
      detail: '',
      type: '1',
      message: '',
      nameErrorText: '',
      nameErrorColor: '',
      detailErrorText: '',
      detailErrorColor: ''
    }
  },
  computed: {
    Save_Able: function () {
      if (this.nameErrorText.toString() === 'OK' && this.detailErrorText.toString() === 'OK') {
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
      this.describe = ''
      this.describeErrorText = ''
      this.describeErrorColor = ''
      this.type = '1'
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
