<template>
  <div class="TeamworkEdit">
    <mu-appbar :title="titleAll">
      <mu-icon-button icon='reply' slot="right" @click="gorReply"/>
    </mu-appbar>
    <mu-text-field label="发布时间" disabled underlineShow="false" v-model="time" fullWidth labelFloat icon="query_builder"/><br/>
    <mu-text-field label="发布内容" disabled underlineShow="false" v-model="content" fullWidth labelFloat icon="note" multiLine :rows="9" :rowsMax="12" :maxLength="300"/><br/>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-flat-button label="已读" @click="openRead=true" icon="visibility" backgroundColor="#9999CC" color="#FFFFFF"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-flat-button label="未读" @click="openUnread=true" icon="visibility_off" backgroundColor="#9999CC" color="#FFFFFF"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button v-if="states" icon="delete" @click="forInactive=true" backgroundColor="red"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button v-if="states" icon="send" @click="forSend=true" backgroundColor="green"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-dialog :open="forSends" title="正在再次发送" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-dialog :open="forInactives" title="正在注销消息" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-dialog :open="forSend" title="确定向未阅读家长再次推送消息?" @close="goClose">
      <mu-flat-button label="取消" @click="goClose" />
      <mu-flat-button label="确定" @click="goSend" secondary/>
    </mu-dialog>
    <mu-dialog :open="forInactive" title="确定注销本消息?" @close="goClose">
      <mu-flat-button label="取消" @click="goClose" />
      <mu-flat-button label="确定" @click="goInactive" secondary/>
    </mu-dialog>
    <mu-drawer right :open="openRead" docked="false">
      <mu-appbar title="已阅读的学生/家长" @click.native="goClose">
        <mu-icon-button icon='clear' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item v-for="student in students" :title="student.sname" :describeText="student.time" :afterText="student.dname">
          <mu-icon v-if="student.sex.toString() === '1'" slot="left" :size="40" color="#00bcd4" value="account_box"/>
          <mu-icon v-if="student.sex.toString() === '2'" slot="left" :size="40" color="#e91e63" value="account_circle" />
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-drawer right :open="openUnread" docked="false">
      <mu-appbar title="未阅读的学生/家长" @click.native="goClose">
        <mu-icon-button icon='clear' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item v-for="student in studentz" :title="student.sname">
          <mu-icon v-if="student.sex.toString() === '1'" slot="left" :size="40" color="#00bcd4" value="account_box"/>
          <mu-icon v-if="student.sex.toString() === '2'" slot="left" :size="40" color="#e91e63" value="account_circle" />
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-popup position="bottom" :overlay="false" popupClass="popup-bottom" :open="bottomPopup">
      <mu-icon :value="icon" :size="36" :color="color"/>&nbsp;{{ message }}
    </mu-popup>
  </div>
</template>

<script>
import * as API from './TeamworkAPI.js'
export default {
  name: 'TeamworkEdit',
  data () {
    return {
      bottomPopup: false,
      forSend: false,
      forSends: false,
      forInactives: false,
      forInactive: false,
      openRead: false,
      openUnread: false,
      states: true,
      icon: '',
      color: '',
      teamwork: '',
      titleAll: '',
      team_id: '',
      teacher_id: '',
      teamName: '',
      teacherName: '',
      students: '',
      studentz: '',
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
        this.reamName = response.body
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
        this.openPopup('服务器内部错误！', 'error', 'red')
      })
    },
    state: function (val) {
      if (val.toString() === '1') {
        this.states = true
      } else {
        this.states = false
      }
    }
  },
  computed: {
    titleAll: function () {
      return this.teamName + '->' + this.teacherName + '老师'
    }
  },
  methods: {
    goClose () {
      this.forSend = false
      this.forInactive = false
      this.openRead = false
      this.openUnread = false
    },
    gorReply () {
      this.$router.push({ path: '/teamworkList' })
    },
    goInactive () {
      this.forInactive = false
      this.forInactives = true
      this.$http.get(
        API.Inactive,
        { params: { id: this.$route.params.teamworkId } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
      ).then((response) => {
        if (response.body === 'error') {
          this.openPopup('请重新登录!', 'report_problem', 'orange')
          window.location.href = '/'
        } else if (response.body === 'OK') {
          this.forInactives = false
          this.openPopup('注销成功!', 'check_circle', 'green')
          setTimeout(() => { this.$router.push({ path: '/teamworkList' }) }, 1000)
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
        this.content = this.teamwork.content
        this.state = this.teamwork.state
      }, (response) => {
        this.openPopup('服务器内部错误!', 'error', 'red')
      })
      this.$http.get(
        API.GetReadTeamwork,
        { params: { id: teamworkId } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        if (response.body.toString() !== 'null') {
          this.students = response.body
        } else {
          this.students = []
        }
      }, (response) => {
        this.openPopup('服务器内部错误!', 'error', 'red')
      })
      this.$http.get(
        API.GetUnreadTeamwork,
        { params: { id: teamworkId } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        if (response.body.toString() !== 'null') {
          this.studentz = response.body
        } else {
          this.studentz = []
        }
      }, (response) => {
        this.openPopup('服务器内部错误!', 'error', 'red')
      })
    },
    goSend () {
      this.forSends = true
      this.$http.get(
        API.Send,
        { params: { teamwork_id: this.$route.params.teamworkId } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        this.forSends = false
        if (response.body === 'error') {
          this.openPopup('请重新登录!', 'report_problem', 'orange')
          window.location.href = '/'
        } else if (response.body === 'OK') {
          this.openPopup('发送成功！', 'check_circle', 'green')
          setTimeout(() => { this.$router.push({ path: '/teamworkList' }) }, 1000)
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
    height: 50px;
    text-align: center;
    line-height: 32px;
  }
</style>
