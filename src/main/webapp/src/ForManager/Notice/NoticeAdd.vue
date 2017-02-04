<template>
  <div class="NoticeAdd">
    <mu-appbar title="请核实后输入以下信息">
      <mu-icon-button icon='reply' slot="right" @click="gorReply"/>
    </mu-appbar>
    <mu-text-field label="标题" underlineShow="false" v-model="title" :errorColor="titleErrorColor" :errorText="titleErrorText" @input="checkTitle" fullWidth labelFloat icon="title" maxLength="20"/><br/>
    <mu-text-field label="内容" underlineShow="false" v-model="content" :errorColor="contentErrorColor" :errorText="contentErrorText" @input="checkContent" fullWidth labelFloat icon="note" multiLine :rows="6" :rowsMax="8" :maxLength="300"/><br/>
    <mu-select-field hintText="发送范围" icon="settings" v-model="type" fullWidth>
      <mu-menu-item value="1" title="全体人员"/>
      <mu-menu-item value="2" title="教师"/>
      <mu-menu-item value="3" title="家长"/>
    </mu-select-field>
    <mu-dialog :open="forSave" title="正在保存" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-dialog :open="forSend" title="确定要发送这条消息吗?" @close="goClose">
      <mu-flat-button label="取消" @click="goClose" />
      <mu-flat-button label="确定" @click="goSave" secondary/>
    </mu-dialog>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="cached" @click="goReset" backgroundColor="orange"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button :disabled="saveAble" icon="send" @click="forSend=true" backgroundColor="green"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-popup position="bottom" :overlay="false" popupClass="popup-bottom" :open="bottomPopup">
      <mu-icon :value="icon" :size="36" :color="color"/>&nbsp;{{ message }}
    </mu-popup>
  </div>
</template>

<script>
import * as API from './NoticeAPI.js'
export default {
  name: 'NoticeAdd',
  data () {
    return {
      bottomPopup: false,
      forSave: false,
      forSend: false,
      icon: '',
      color: '',
      title: '',
      content: '',
      type: '1',
      message: '',
      titleErrorText: '',
      contentErrorText: '',
      titleErrorColor: '',
      contentErrorColor: '',
      saveAble: true
    }
  },
  computed: {
    saveAble: function () {
      if (this.titleErrorText.toString() === 'OK' && this.contentErrorText.toString() === 'OK') {
        return false
      } else {
        return true
      }
    }
  },
  methods: {
    gorReply () {
      this.$router.push({ path: '/noticeList' })
    },
    goClose () {
      this.forSend = false
    },
    openPopup (message, icon, color) {
      this.message = message
      this.icon = icon
      this.color = color
      this.bottomPopup = true
      setTimeout(() => { this.bottomPopup = false }, 1500)
    },
    goReset () {
      this.title = ''
      this.content = ''
      this.type = '1'
      this.titleErrorText = ''
      this.contentErrorText = ''
      this.titleErrorColor = ''
      this.contentErrorColor = ''
    },
    checkTitle (value) {
      if (value === null || value === undefined || value === '') {
        this.titleErrorText = '标题为必填项!'
        this.titleErrorColor = 'orange'
      } else {
        this.titleErrorText = 'OK'
        this.titleErrorColor = 'green'
      }
    },
    checkContent (value) {
      if (value === null || value === undefined || value === '') {
        this.contentErrorText = '内容为必填项!'
        this.contentErrorColor = 'orange'
      } else if (value.length > 300) {
        this.contentErrorText = '内容不得超过300字符'
        this.contentErrorColor = 'orange'
      } else {
        this.contentErrorText = 'OK'
        this.contentErrorColor = 'green'
      }
    },
    goSave () {
      this.forSend = false
      this.forSave = true
      this.$http.get(
        API.Save,
        { params: {
          title: this.title,
          content: this.content,
          type: this.type
        } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        this.forSave = false
        if (response.body === 'error') {
          this.openPopup('请重新登录!', 'report_problem', 'orange')
          window.location.href = '/'
        } else if (response.body === 'OK') {
          this.openPopup('保存成功！', 'check_circle', 'green')
          setTimeout(() => { this.$router.push({ path: '/noticeList' }) }, 1000)
        } else {
          this.openPopup(response.body, 'report_problem', 'orange')
        }
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
    height: 70px;
    text-align: center;
    line-height: 32px;
  }
</style>
