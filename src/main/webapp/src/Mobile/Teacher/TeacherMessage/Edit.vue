<template>
  <div>
    <mu-appbar title="消息详情">
      <mu-icon-button icon='reply' slot="right" @click="gorReply"/>
    </mu-appbar>
    <mu-text-field label="标题" disabled v-model="title" fullWidth labelFloat icon="title" maxLength="20"/><br/>
    <mu-text-field label="时间" disabled v-model="time" fullWidth labelFloat icon="query_builder"/><br/>
    <mu-text-field label="发布人" disabled v-model="name" fullWidth labelFloat icon="query_builder"/><br/>
    <mu-text-field label="内容" disabled v-model="content" fullWidth labelFloat icon="note" multiLine :rows="5" :rowsMax="8" :maxLength="200"/><br/>
    <mu-text-field label="确认回复" disabled v-model="replyString" fullWidth labelFloat icon="query_builder"/><br/>
    <mu-text-field label="状态" disabled v-model="stateString" fullWidth labelFloat icon="query_builder"/><br/>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button v-if="Able" icon="send" @click="goRead" backgroundColor="green"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-dialog :open="Reading" title="正在读取" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-dialog :open="Read" title="正在确认读取" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-popup position="bottom" :overlay="false" popupClass="popup-bottom" :open="bottomPopup">
      <mu-icon :value="icon" :size="36" :color="color"/>&nbsp;{{ message }}
    </mu-popup>
  </div>
</template>
<script>
import * as API from './API.js'
export default {
  name: 'Edit',
  data () {
    return {
      bottomPopup: false,
      Reading: true,
      Read: false,
      icon: '',
      color: '',
      title: '',
      content: '',
      name: '',
      reply: '',
      replyString: '',
      state: '',
      states: '',
      stateString: '',
      time: '',
      message: ''
    }
  },
  created () {
    this.fetchData(this.$route.params.id)
  },
  watch: {
    // 如果路由有变化，会再次执行该方法
    '$route': 'fetchData'
  },
  computed: {
    Able: function () {
      if (this.state.toString() === '1' && this.states.toString() === '0' && this.reply.toString() === '1') {
        return true
      } else {
        return false
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
    fetchData (id) {
      this.$http.get(
        API.get,
        { params: { id: id } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        this.title = response.body.teacherMessage[0].title
        this.content = response.body.teacherMessage[0].content
        this.time = response.body.teacherMessage[0].time
        this.state = response.body.teacherMessage[0].state
        this.reply = response.body.teacherMessage[0].reply
        this.name = response.body.teacherMessage[0].name
        this.states = response.body.teacherMessageRead[0].state
        if (this.state.toString() === '1') {
          this.stateString = '可用'
        } else if (this.state.toString() === '0') {
          this.stateString = '停用'
        } else {
          this.stateString = '状态错误'
        }
        if (this.reply.toString() === '1') {
          this.replyString = '要求回复'
        } else if (this.reply.toString() === '0') {
          this.replyString = '不要求回复'
        } else {
          this.replyString = '状态错误'
        }
        this.Reading = false
      }, (response) => {
        this.openPopup('服务器内部错误!', 'error', 'red')
      })
    },
    goRead () {
      this.Read = true
      this.$http.get(
        API.reading,
        { params: { id: this.$route.params.id } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        this.Read = false
        if (response.body === 'error') {
          this.openPopup('请重新登录!', 'report_problem', 'orange')
          window.location.href = '/'
        } else if (response.body === 'OK') {
          this.openPopup('发送成功！', 'check_circle', 'green')
          setTimeout(() => { this.$router.push({ path: '/list' }) }, 1000)
        } else {
          this.openPopup(response.body, 'report_problem', 'orange')
        }
        this.Read = false
      }, (response) => {
        this.Read = false
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
