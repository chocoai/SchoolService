<template>
  <div class="RoomworkEdit">
    <mu-appbar title="请核实后输入以下信息">
      <mu-icon-button icon='reply' slot="right" @click="gorReply"/>
    </mu-appbar>
    <mu-text-field label="班级" disabled underlineShow="false" v-model="roomName" fullWidth labelFloat icon="person"/><br/>
    <mu-text-field label="课程" disabled underlineShow="false" v-model="courseName" fullWidth labelFloat icon="person"/><br/>
    <mu-text-field label="教师" disabled underlineShow="false" v-model="teacherName" fullWidth labelFloat icon="person"/><br/>
    <mu-text-field label="时间" disabled underlineShow="false" v-model="time" fullWidth labelFloat icon="person"/><br/>
    <mu-text-field label="内容" disabled underlineShow="false" v-model="content" fullWidth labelFloat icon="note" multiLine :rows="9" :rowsMax="12" :maxLength="500"/><br/>
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
        <mu-float-button icon="done" @click="forSend=true" backgroundColor="green"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-dialog :open="forSends" title="正在再次发送" >
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
        <mu-list-item v-for="student in students" :title="student.sname" :describeText="student.dname" :afterText="student.pname">
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
        <mu-list-item v-for="student in studentz" :title="student.sname" :describeText="student.dname" :afterText="student.pname">
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
import * as API from './RoomworkAPI.js'
export default {
  name: 'RoomworkEdit',
  data () {
    return {
      bottomPopup: false,
      forSend: false,
      forSends: false,
      forInactive: false,
      openRead: false,
      openUnread: false,
      states: true,
      icon: '',
      color: '',
      roomwork: '',
      room_id: '',
      course_id: '',
      teacher_id: '',
      roomName: '',
      courseName: '',
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
    this.fetchData(this.$route.params.roomworkId)
  },
  watch: {
    '$route': 'fetchData',
    room_id: function (val) {
      this.$http.get(
        API.GetRoomName,
        { params: { id: val } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
      ).then((response) => {
        this.roomName = response.body
      }, (response) => {
        this.openPopup('服务器内部错误！', 'error', 'red')
      })
    },
    course_id: function (val) {
      this.$http.get(
        API.GetCourseName,
        { params: { id: val } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
      ).then((response) => {
        this.courseName = response.body
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
  methods: {
    goClose () {
      this.forSend = false
      this.forInactive = false
      this.openRead = false
      this.openUnread = false
    },
    gorReply () {
      this.$router.push({ path: '/roomworkList' })
    },
    goInactive () {
      console.log('111')
    },
    openPopup (message, icon, color) {
      this.message = message
      this.icon = icon
      this.color = color
      this.bottomPopup = true
      setTimeout(() => { this.bottomPopup = false }, 1500)
    },
    fetchData (roomworkId) {
      this.$http.get(
        API.GetById,
        { params: { id: roomworkId } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        this.roomwork = response.body
        this.teacher_id = this.roomwork.teacher_id
        this.room_id = this.roomwork.room_id
        this.course_id = this.roomwork.course_id
        this.time = this.roomwork.time
        this.state = this.roomwork.state
      }, (response) => {
      })
      this.$http.get(
        API.GetReadRoomwork,
        { params: { id: roomworkId, state: 1 } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        this.students = response.body
      }, (response) => {
      })
      this.$http.get(
        API.GetReadRoomwork,
        { params: { id: roomworkId, state: 0 } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        this.studentz = response.body
      }, (response) => {
      })
    },
    goSend () {
      this.forSends = true
      this.$http.get(
        API.Save,
        { params: {
          content: this.content,
          room_id: this.room_id,
          course_id: this.course_id,
          student_id: this.student_id
        } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        this.forSends = false
        if (response.body === 'error') {
          this.openPopup('请重新登录!', 'report_problem', 'orange')
          window.location.href = '/'
        } else if (response.body === 'OK') {
          this.openPopup('保存成功！', 'check_circle', 'green')
          setTimeout(() => { this.$router.push({ path: '/roomworkList' }) }, 1000)
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
