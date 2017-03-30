<template>
  <div>
    <mu-appbar title="消息详情">
      <mu-icon-button icon='reply' slot="right" @click="gorReply"/>
    </mu-appbar>
    <mu-text-field label="标题" disabled v-model="title" fullWidth labelFloat icon="title" maxLength="20"/><br/>
    <mu-text-field label="时间" disabled v-model="time" fullWidth labelFloat icon="query_builder"/><br/>
    <mu-text-field label="发布人" disabled v-model="name" fullWidth labelFloat icon="assignment_ind"/><br/>
    <mu-text-field label="内容" disabled v-model="content" fullWidth labelFloat icon="note" multiLine :rows="5" :rowsMax="8" :maxLength="200"/><br/>
    <mu-text-field label="确认回复" disabled v-model="replyString" fullWidth labelFloat icon="send"/><br/>
    <mu-text-field label="状态" disabled v-model="stateString" fullWidth labelFloat icon="spa"/><br/>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo" v-if="sends">
        <mu-flat-button label="已读" @click="openRead=true" icon="visibility" backgroundColor="#9999CC" color="#FFFFFF"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo" v-if="sends">
        <mu-flat-button label="未读" @click="openUnread=true" icon="visibility_off" backgroundColor="#9999CC" color="#FFFFFF"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button v-if="states" icon="delete" @click="Inactive=true" backgroundColor="red"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button v-if="states" icon="send" @click="Send=true" backgroundColor="green"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-dialog :open="Reading" title="正在读取" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-dialog :open="Sending" title="正在再次发送" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-dialog :open="Inactiving" title="正在注销消息" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-dialog :open="Send" title="确定向未阅读教师再次推送消息?" @close="goClose">
      <mu-flat-button label="取消" @click="goClose" />
      <mu-flat-button label="确定" @click="goSend" secondary/>
    </mu-dialog>
    <mu-dialog :open="Inactive" title="确定注销本消息?" @close="goClose">
      <mu-flat-button label="取消" @click="goClose" />
      <mu-flat-button label="确定" @click="goInactive" secondary/>
    </mu-dialog>
    <mu-popup position="bottom" :overlay="false" popupClass="popup-bottom" :open="bottomPopup">
      <mu-icon :value="icon" :size="36" :color="color"/>&nbsp;{{ message }}
    </mu-popup>
    <mu-drawer right :open="openRead" docked="false">
      <mu-appbar title="已阅读的教师" @click.native="goClose">
        <mu-icon-button icon='clear' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item v-for="teacher in teachers" :title="teacher.name" :describeText="teacher.time">
          <mu-avatar v-if="teacher.state.toString() === '1'" :src="teacher.picUrl" slot="leftAvatar" :size="40"/>
          <mu-icon v-if="teacher.state.toString() === '2'" slot="left" color="#9e9e9e" value="sentiment_very_dissatisfied" :size="40" />
          <mu-icon v-if="teacher.state.toString() === '3'" slot="left" color="#8bc34a" value="sentiment_neutral" :size="40" />
          <mu-icon v-if="teacher.state.toString() === '4'" slot="left" color="#3f51b5" value="sentiment_dissatisfied" :size="40" />
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-drawer right :open="openUnread" docked="false">
      <mu-appbar title="未阅读的教师" @click.native="goClose">
        <mu-icon-button icon='clear' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item v-for="teacher in teacherz" :title="teacher.name">
          <mu-avatar v-if="teacher.state.toString() === '1'" :src="teacher.picUrl" slot="leftAvatar" :size="40"/>
          <mu-icon v-if="teacher.state.toString() === '2'" slot="left" color="#9e9e9e" value="sentiment_very_dissatisfied" :size="40" />
          <mu-icon v-if="teacher.state.toString() === '3'" slot="left" color="#8bc34a" value="sentiment_neutral" :size="40" />
          <mu-icon v-if="teacher.state.toString() === '4'" slot="left" color="#3f51b5" value="sentiment_dissatisfied" :size="40" />
        </mu-list-item>
      </mu-list>
    </mu-drawer>
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
      Inactive: false,
      Send: false,
      openRead: false,
      openUnread: false,
      icon: '',
      color: '',
      title: '',
      content: '',
      name: '',
      reply: '',
      replyString: '',
      state: '',
      stateString: '',
      time: '',
      message: '',
      teachers: [],
      teacherz: []
    }
  },
  created () {
    this.fetchData(this.$route.params.id)
  },
  watch: {
    // 如果路由有变化，会再次执行该方法
    '$route': 'fetchData',
    state: function (val) {
      if (val.toString() === '1') {
        this.states = true
      } else {
        this.states = false
      }
    },
    reply: function (val) {
      if (val.toString() === '1') {
        this.sends = true
      } else {
        this.sends = false
      }
    }
  },
  methods: {
    gorReply () {
      this.$router.push({ path: '/list' })
    },
    goClose () {
      this.openRead = false
      this.openUnread = false
      this.Inactive = false
      this.Send = false
    },
    goInactive () {
      this.Inactiving = true
      this.Inactive = false
      this.$http.get(
        API.inactive,
        { params: { id: this.$route.params.id } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
      ).then((response) => {
        if (response.body === 'error') {
          this.openPopup('请重新登录!', 'report_problem', 'orange')
          window.location.href = '/'
        } else if (response.body === 'OK') {
          this.Inactiving = false
          this.openPopup('注销成功!', 'check_circle', 'green')
          setTimeout(() => { this.$router.push({ path: '/list' }) }, 1000)
        } else {
          this.openPopup(response.body, 'report_problem', 'orange')
        }
      }, (response) => {
        this.openPopup('服务器内部错误!', 'error', 'red')
      })
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
        this.teachers = response.body.teachers
        this.teacherz = response.body.teacherz
        this.title = response.body.message[0].title
        this.content = response.body.message[0].content
        this.time = response.body.message[0].time
        this.state = response.body.message[0].state
        this.reply = response.body.message[0].reply
        this.name = response.body.message[0].name
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
    goSend () {
      this.Sending = true
      this.$http.get(
        API.send,
        { params: { id: this.$route.params.id } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        this.Sending = false
        if (response.body === 'error') {
          this.openPopup('请重新登录!', 'report_problem', 'orange')
          window.location.href = '/'
        } else if (response.body === 'OK') {
          this.openPopup('发送成功！', 'check_circle', 'green')
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
