<template>
  <div class="RoomworkAdd">
    <mu-appbar title="请核实后输入以下信息">
      <mu-icon-button icon='reply' slot="right" @click="gorReply"/>
    </mu-appbar>
    <mu-text-field label="内容" underlineShow="false" v-model="content" :errorColor="contentErrorColor" :errorText="contentErrorText"  fullWidth labelFloat icon="note"  multiLine :rows="6" :rowsMax="9"/><br/>
    <mu-sub-header>请分别选择班级、课程、发送范围后保存并自动发送</mu-sub-header>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-flat-button :label="roomName" @click="openRoom=true" :icon="roomIcon" :backgroundColor="roomBack" color="#FFFFFF"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-flat-button :label="courseName" @click="openCourse=true" :icon="courseIcon" :backgroundColor="courseBack" color="#FFFFFF"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-flat-button :label="studentName" @click="openStudent=true" :icon="studentIcon" :backgroundColor="studentBack" color="#FFFFFF"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-dialog :open="forSave" title="正在保存" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="cached" @click="goReset" backgroundColor="orange"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="done" @click="goSave" backgroundColor="green"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-drawer right :open="openRoom" docked="false">
      <mu-appbar title="请选择所属班级" @click.native="closeRoom">
        <mu-icon-button icon='done' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item v-for="room in rooms" :title="room.name">
          <mu-radio v-model="room_id" label="" labelLeft :nativeValue="room.id" slot="right" iconClass="color: #215E21"/>
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-drawer right :open="openCourse" docked="false">
      <mu-appbar title="请选择所授课程" @click.native="closeCourse">
        <mu-icon-button icon='done' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item v-for="course in courses" :title="course.name">
          <mu-radio v-model="course_id" label="" labelLeft :nativeValue="course.id" slot="right" iconClass="color: #215E21"/>
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-drawer right :open="openStudent" docked="false">
      <mu-appbar title="请选择发送范围" @click.native="closeStudent">
        <mu-icon-button icon='done' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item title="全选" @click.native="student_id=true">
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
      icon: '',
      color: '',
      room_id: '',
      course_id: '',
      student_id: [],
      rooms: [],
      courses: [],
      students: [],
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
      content: '',
      message: '',
      contentErrorText: '',
      contentErrorColor: ''
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
      if (this.student_id.toString() !== '') {
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
      if (this.student_id.toString() !== '') {
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
    closeRoom () {
      this.openRoom = false
    },
    closeCourse () {
      this.openCourse = false
    },
    closeStudent () {
      this.openStudent = false
      if (this.student_id.toString() !== '') {
        this.studentName = '发送范围已设置'
      } else {
        this.studentName = '发送范围未设置'
      }
    },
    checkContent (value) {
      if (value === null || value === undefined || value === '') {
        this.contentErrorText = '内容为必填项!'
        this.contentErrorColor = 'orange'
        this.userId = ''
      } else {
        this.contentErrorText = ''
        this.contentErrorColor = 'green'
      }
    },
    goSave () {
      this.forSave = true
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
