<template>
  <div class="TeamworkLook">
    <mu-appbar :title="titleAll">
      <mu-icon-button icon='reply' slot="right" @click="gorReply"/>
    </mu-appbar>
    <mu-text-field label="标题" disabled underlineShow="false" v-model="title" fullWidth labelFloat icon="title" maxLength="20"/><br/>
    <mu-text-field label="发布时间" disabled underlineShow="false" v-model="time" fullWidth labelFloat icon="query_builder"/><br/>
    <mu-text-field label="发布内容" disabled underlineShow="false" v-model="content" fullWidth labelFloat icon="note" multiLine :rows="9" :rowsMax="12" :maxLength="300"/><br/>
    <mu-raised-button :label="labels" :disabled="states" fullWidth @click="goRead" primary/>
    <mu-dialog :open="forRead" title="正在确认阅读" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-dialog :open="init" title="正在读取数据" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-popup position="bottom" :overlay="false" popupClass="popup-bottom" :open="bottomPopup">
      <mu-icon :value="icon" :size="36" :color="color"/>&nbsp;{{ message }}
    </mu-popup>
  </div>
</template>

<script>
import * as API from './TeamworkAPI.js'
export default {
  name: 'TeamworkLook',
  data () {
    return {
      bottomPopup: false,
      states: true,
      forRead: false,
      init: true,
      labels: '',
      icon: '',
      color: '',
      teamwork: '',
      titleAll: '',
      team_id: '',
      teacher_id: '',
      teamName: '',
      teacherName: '',
      title: '',
      content: '',
      time: '',
      state: '',
      message: ''
    }
  },
  created: function () {
    this.fetchData(this.$route.params.teamworkId)
  },
  watch: {
    '$route': 'fetchData',
    team_id: function (val) {
      this.$http.get(
        API.GetTeamName,
        { params: { id: val } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
      ).then((response) => {
        this.teamName = response.body
      }, (response) => {
        this.openPopup('服务器内部错误！', 'error', 'red')
      })
    },
    teacher_id: function (val) {
      this.$http.get(
        API.GetTeacherName,
        { params: { id: val } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
      ).then((response) => {
        this.teacherName = response.body
      }, (response) => {
        this.openPopup('服务器内部错误!', 'error', 'red')
      })
    },
    state: function (val) {
      if (val.toString() === '1') {
        this.states = true
        this.labels = '该消息您已确认阅读!'
      } else {
        this.states = false
        this.labels = '确认阅读该消息'
      }
    }
  },
  computed: {
    titleAll: function () {
      return this.teamName + '-' + this.teacherName + '老师'
    }
  },
  methods: {
    gorReply () {
      this.$router.push({ path: '/teamworkList' })
    },
    openPopup (message, icon, color) {
      this.message = message
      this.icon = icon
      this.color = color
      this.bottomPopup = true
      setTimeout(() => { this.bottomPopup = false }, 1500)
    },
    goRead () {
      this.forRead = true
      this.$http.get(
        API.Read,
        { params: { id: this.$route.params.teamworkId } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        this.forRead = false
        if (response.body === 'error') {
          this.openPopup('请重新登录!', 'report_problem', 'orange')
          window.location.href = '/'
        } else if (response.body === 'OK') {
          this.openPopup('确认阅读成功!', 'check_circle', 'green')
          setTimeout(() => { this.$router.push({ path: '/teamworkList' }) }, 1000)
        } else {
          this.openPopup(response.body, 'report_problem', 'orange')
        }
      }, (response) => {
        this.openPopup('服务器内部错误!', 'error', 'red')
      })
    },
    fetchData (teamworkId) {
      this.$http.get(
        API.GetById,
        { params: { id: teamworkId } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        this.teamwork = response.body
        this.teacher_id = this.teamwork.teacher_id
        this.team_id = this.teamwork.team_id
        this.time = this.teamwork.time
        this.title = this.teamwork.title
        this.content = this.teamwork.content
      }, (response) => {
        this.openPopup('服务器内部错误!', 'error', 'red')
      })
      this.$http.get(
        API.GetTeamworkreadState,
        { params: { id: teamworkId } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        if (response.body.toString() === 'null') {
          this.openPopup('无权阅读！', 'report_problem', 'orange')
          setTimeout(() => { this.$router.push({ path: '/teamworkList' }) }, 1000)
        } else {
          this.state = response.body
          this.init = false
        }
      }, (response) => {
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
    height: 50px;
    text-align: center;
    line-height: 32px;
  }
</style>
