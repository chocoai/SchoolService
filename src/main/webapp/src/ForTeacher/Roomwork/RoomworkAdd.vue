<template>
  <div class="RoomworkAdd">
    <mu-appbar title="输入/选择后发送消息">
      <mu-icon-button icon='reply' slot="right" @click="gorReply"/>
    </mu-appbar>
    <mu-text-field label="标题" underlineShow="false" v-model="title" :errorColor="titleErrorColor" :errorText="titleErrorText" @input="checkTitle" fullWidth labelFloat icon="title" maxLength="20"/><br/>
    <mu-text-field label="内容" underlineShow="false" v-model="content" :errorColor="contentErrorColor" :errorText="contentErrorText" @input="checkContent" fullWidth labelFloat icon="note" multiLine :rows="9" :rowsMax="12" :maxLength="300"/><br/>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-flat-button :label="roomName" @click="openRoom=true" :icon="roomIcon" :backgroundColor="roomBack" color="#FFFFFF"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-flat-button :label="courseName" @click="openCourse=true" :icon="courseIcon" :backgroundColor="courseBack" color="#FFFFFF"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-flat-button :label="studentName" @click="openStudent=true" :icon="studentIcon" :backgroundColor="studentBack" color="#FFFFFF"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-dialog :open="forSave" title="正在保存并发送" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="cached" @click="goReset" backgroundColor="orange"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="send" @click="forSaved=true" :disabled="saveAble" backgroundColor="green"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-dialog :open="forSaved" title="确定保存并发送消息?" @close="goClose">
      <mu-flat-button label="取消" @click="goClose" />
      <mu-flat-button label="确定" @click="goSave" secondary/>
    </mu-dialog>
    <mu-drawer right :open="openRoom" docked="false">
      <mu-appbar title="请选择所属班级"/>
      <mu-list :value="room_id" @itemClick="roomChange">
        <mu-list-item v-for="room in rooms" :title="room.name" :value="room.id">
          <mu-icon slot="left" :size="40" value="store" color="#9c27b0"/>
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-drawer right :open="openCourse" docked="false">
      <mu-appbar title="请选择所授课程" />
      <mu-list :value="course_id" @itemClick="courseChange">
        <mu-list-item v-for="course in courses" :title="course.name" :value="course.id">
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-drawer right :open="openStudent" docked="false">
      <mu-appbar title="请选择发送范围" @click.native="closeStudent">
        <mu-icon-button icon='done' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item title="全选" @click.native="student_id=studentAll">
          <mu-icon slot="left" value="supervisor_account" :size="40"/>
        </mu-list-item>
        <mu-list-item title="清空" @click.native="student_id=[]">
          <mu-icon slot="left" value="delete_forever" :size="40"/>
        </mu-list-item>
        <mu-list-item v-for="student in students" :title="student.name" :describeText="student.number">
          <mu-icon v-if="student.sex.toString() === '1'" slot="left" :size="40" color="#00bcd4" value="account_box"/>
          <mu-icon v-if="student.sex.toString() === '2'" slot="left" :size="40" color="#e91e63" value="account_circle" />
          <mu-checkbox v-model="student_id" label="" labelLeft :nativeValue="student.id" slot="right"/>
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
  name: 'RoomworkAdd',
  data () {
    return {
      bottomPopup: false,
      forSave: false,
      forSaved: false,
      icon: '',
      color: '',
      room_id: '',
      course_id: '',
      student_id: [],
      rooms: [],
      courses: [],
      students: [],
      studentAll: '',
      roomName: '班级',
      courseName: '课程',
      studentName: '发送范围',
      roomAble: false,
      courseAble: true,
      studentAble: true,
      openRoom: false,
      openCourse: false,
      openStudent: false,
      roomIcon: 'looks_one',
      courseIcon: 'looks_two',
      studentIcon: 'looks_3',
      roomBack: '#66CCCC',
      courseBack: '#66CCCC',
      studentBack: '#66CCCC',
      title: '',
      content: '',
      message: '',
      titleErrorText: '',
      titleErrorColor: '',
      contentErrorText: '',
      contentErrorColor: '',
      saveAble: true
    }
  },
  created: function () {
    this.$http.get(
      API.TeacherRoomList,
      { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
    ).then((response) => {
      this.rooms = response.body
      this.$http.get(
        API.TeacherRoomFirst,
        { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
      ).then((response) => {
        this.room_id = response.body
        this.$http.get(
          API.RoomCourseList,
          { params: { id: this.room_id } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
        ).then((response) => {
          this.courses = response.body
          this.$http.get(
            API.RoomCourseFirst,
            { params: { id: this.room_id } },
            { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
          ).then((response) => {
            this.course_id = response.body
          }, (response) => {
            this.openPopup('服务器内部错误！', 'error', 'red')
          })
        }, (response) => {
          this.openPopup('服务器内部错误！', 'error', 'red')
        })
      }, (response) => {
        this.openPopup('服务器内部错误！', 'error', 'red')
      })
    }, (response) => {
      this.openPopup('服务器内部错误！', 'error', 'red')
    })
  },
  computed: {
    saveAble: function () {
      if (this.contentErrorText.toString() === 'OK' && this.student_id.toString !== '') {
        return false
      } else {
        return true
      }
    },
    roomBack: function () {
      if (this.room_id.toString() !== '') {
        return '#9999CC'
      } else {
        return '#66CCCC'
      }
    },
    courseBack: function () {
      if (this.course_id.toString() !== '') {
        return '#9999CC'
      } else {
        return '#66CCCC'
      }
    },
    studentBack: function () {
      if (this.student_id.toString() !== '' || this.student_id.toString() !== this.studentAll) {
        return '#9999CC'
      } else {
        return '#66CCCC'
      }
    },
    roomIcon: function () {
      if (this.room_id.toString() !== '') {
        return 'filter_1'
      } else {
        return 'looks_one'
      }
    },
    courseIcon: function () {
      if (this.course_id.toString() !== '') {
        return 'filter_2'
      } else {
        return 'looks_two'
      }
    },
    studentIcon: function () {
      if (this.student_id.toString() !== '' || this.student_id.toString() !== this.studentAll) {
        return 'filter_3'
      } else {
        return 'looks_3'
      }
    }
  },
  watch: {
    room_id: function (val) {
      if (val.toString() !== '') {
        this.$http.get(
          API.GetRoomName,
          { params: { id: val } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
        ).then((response) => {
          this.roomName = response.body
        }, (response) => {
          this.openPopup('服务器内部错误！', 'error', 'red')
        })
        this.$http.get(
          API.StudentList,
          { params: { id: val } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
        ).then((response) => {
          this.students = response.body
        }, (response) => {
          this.openPopup('服务器内部错误！', 'error', 'red')
        })
        this.$http.get(
          API.StudentCode,
          { params: { id: val } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
        ).then((response) => {
          this.studentAll = response.body
          this.student_id = this.studentAll
        }, (response) => {
          this.openPopup('服务器内部错误！', 'error', 'red')
        })
        this.$http.get(
          API.RoomCourseList,
          { params: { id: val } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
        ).then((response) => {
          this.courses = response.body
          this.$http.get(
            API.RoomCourseFirst,
            { params: { id: val } },
            { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
          ).then((response) => {
            this.course_id = response.body
          }, (response) => {
            this.openPopup('服务器内部错误！', 'error', 'red')
          })
        }, (response) => {
          this.openPopup('服务器内部错误！', 'error', 'red')
        })
      } else {
        this.courses = []
        this.course_id = ''
      }
    },
    course_id: function (val) {
      if (val.toString() !== '') {
        this.$http.get(
          API.GetCourseName,
          { params: { id: val } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
        ).then((response) => {
          this.courseName = response.body
        }, (response) => {
          this.openPopup('服务器内部错误！', 'error', 'red')
        })
      }
    }
  },
  methods: {
    goClose () {
      this.forSaved = false
    },
    gorReply () {
      this.$router.push({ path: '/roomworkList' })
    },
    openPopup (message, icon, color) {
      this.message = message
      this.icon = icon
      this.color = color
      this.bottomPopup = true
      setTimeout(() => { this.bottomPopup = false }, 1500)
    },
    goReset () {
      this.content = ''
      this.saved = false
      this.contentErrorText = ''
      this.contentErrorColor = ''
    },
    roomChange (val) {
      this.room_id = val.value
      this.roomName = val.title
      this.openRoom = false
    },
    courseChange (val) {
      this.course_id = val.value
      this.courseName = val.title
      this.openCourse = false
    },
    closeStudent () {
      this.openStudent = false
      if (this.student_id.toString() === this.studentAll.toString()) {
        this.studentName = '已全选'
      } else if (this.student_id.toString() !== '') {
        this.studentName = '部分选择'
      } else {
        this.studentName = '发送范围未设置'
      }
    },
    checkTitle (value) {
      if (value === null || value === undefined || value === '') {
        this.titleErrorText = '标题为必填项!'
        this.titleErrorColor = 'orange'
      } else if (value.length > 20) {
        this.contentErrorText = '标题不得超过20字符'
        this.contentErrorColor = 'orange'
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
      this.forSave = true
      this.forSaved = false
      this.$http.get(
        API.Save,
        { params: {
          title: this.title,
          content: this.content,
          room_id: this.room_id,
          course_id: this.course_id,
          student_id: this.student_id
        } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        this.forSave = false
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
