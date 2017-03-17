<template>
  <div>
    <mu-appbar title="新建消息">
      <mu-icon-button icon='reply' slot="right" @click="gorReply"/>
    </mu-appbar>
    <mu-text-field label="标题" underlineShow="false" v-model="title" :errorColor="titleErrorColor" :errorText="titleErrorText" @input="checkTitle" fullWidth labelFloat icon="title" maxLength="20"/><br/>
    <mu-text-field label="内容" underlineShow="false" v-model="content" :errorColor="contentErrorColor" :errorText="contentErrorText" @input="checkContent" fullWidth labelFloat icon="note" multiLine :rows="5" :rowsMax="8" :maxLength="200"/><br/>
    <mu-select-field hintText="确认回复" icon="settings" v-model="reply" fullWidth>
      <mu-menu-item value="0" title="不需要回复确认"/>
      <mu-menu-item value="1" title="需要回复确认"/>
    </mu-select-field>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-raised-button :label="courseName" @click="openTeacher=true" primary/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo" v-if="Room_Able">
        <mu-raised-button :label="roomName" @click="openRoom=true" primary/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo" v-if="Student_Able">
        <mu-raised-button :label="studentName" @click="openStudent=true" primary/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-dialog :open="Saving" title="正在保存" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-dialog :open="Reading" title="正在读取" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-dialog :open="Sending" title="确定要发送这条消息吗?" @close="goClose">
      <mu-flat-button label="取消" @click="goClose" />
      <mu-flat-button label="确定" @click="goSave" secondary/>
    </mu-dialog>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="cached" @click="goReset" backgroundColor="orange"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button :disabled="Save_Able" icon="send" @click="Sending=true" backgroundColor="green"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-popup position="bottom" :overlay="false" popupClass="popup-bottom" :open="bottomPopup">
      <mu-icon :value="icon" :size="36" :color="color"/>&nbsp;{{ message }}
    </mu-popup>
    <mu-drawer right :open="openCourse" docked="false">
      <mu-appbar title="请选择课程"/>
      <mu-list :value="course_id" @itemClick="courseChange">
        <mu-list-item v-for="course in courses" :title="course.name" :value="course.id" :describeText="course.detail" :afterText="getType(course.type)">
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-drawer right :open="openRoom" docked="false">
      <mu-appbar title="请选择班级"/>
      <mu-list :value="room_id" @itemClick="roomChange">
        <mu-list-item v-for="room in rooms" :title="room.name" :value="room.id" :describeText="room.slogan">
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-drawer right :open="openStudent" docked="false">
      <mu-appbar title="请选择学生" @click.native="closeStudent">
        <mu-icon-button icon='done' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item title="全选" @click.native="student_id=studentAll">
          <mu-icon slot="left" value="supervisor_account" :size="40"/>
        </mu-list-item>
        <mu-list-item title="清空" @click.native="student_id=[]">
          <mu-icon slot="left" value="delete_forever" :size="40"/>
        </mu-list-item>
        <mu-list-item v-for="student in students" :title="student.name" :describeText="student.number" :afterText="getBind(student.bind)">
          <mu-icon v-if="student.sex.toString() === '1'" slot="left" :size="40" color="#00bcd4" value="account_box"/>
          <mu-icon v-if="student.sex.toString() === '2'" slot="left" :size="40" color="#e91e63" value="account_circle" />
          <mu-checkbox v-model="student_id" label="" labelLeft :nativeValue="student.id" slot="right"/>
        </mu-list-item>
      </mu-list>
    </mu-drawer>
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
      Sending: false,
      Reading: true,
      openTeacher: false,
      Room_Able: false,
      Student_Able: false,
      icon: '',
      color: '',
      title: '',
      content: '',
      reply: '1',
      message: '',
      courseName: '请选择课程',
      course_id: [],
      courseAll: '',
      roomName: '请选择班级',
      room_id: [],
      roomAll: '',
      studentName: '请选择学生',
      student_id: [],
      studentAll: '',
      courses: '',
      rooms: '',
      students: '',
      titleErrorText: '',
      contentErrorText: '',
      titleErrorColor: '',
      contentErrorColor: ''
    }
  },
  created: function () {
    this.$http.get(
      API.listCourse,
      { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
    ).then((response) => {
      this.courses = response.body
      this.Reading = false
    }, (response) => {
      this.openPopup('服务器内部错误！', 'error', 'red')
    })
  },
  computed: {
    Save_Able: function () {
      if (this.titleErrorText.toString() === 'OK' && this.contentErrorText.toString() === 'OK') {
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
    goClose () {
      this.Sending = false
    },
    openPopup (message, icon, color) {
      this.message = message
      this.icon = icon
      this.color = color
      this.bottomPopup = true
      setTimeout(() => { this.bottomPopup = false }, 1500)
    },
    getBind (bind) {
      if (bind.toString() === '0') {
        return '未绑定家长'
      } else if (bind.toString() === '1') {
        return '已绑定家长'
      } else {
        return '绑定错误'
      }
    },
    getType (type) {
      if (type.toString() === '1') {
        return '必修课'
      } else if (type.toString() === '2') {
        return '选修课'
      } else {
        return '错误'
      }
    },
    courseChange (val) {
      this.course_id = val.value
      this.courseName = val.title
      if (val.afterText.toString() === '必修课') {
        this.Room_Able = true
        this.Student_Able = false
        this.openCourse = false
        this.Reading = true
        this.$http.get(
          API.listRoom,
          { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
        ).then((response) => {
          this.rooms = response.body.rooms
          this.Reading = false
        }, (response) => {
          this.openPopup('服务器内部错误！', 'error', 'red')
        })
      } else if (val.afterText.toString() === '选修课') {
        this.Room_Able = false
        this.Student_Able = true
        this.openCourse = false
        this.Reading = true
        this.$http.get(
          API.listStudentA,
          { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
        ).then((response) => {
          this.students = response.body.students
          this.studentAll = response.body.select
          this.student_id = this.studentAll
          this.Reading = false
        }, (response) => {
          this.openPopup('服务器内部错误！', 'error', 'red')
        })
      } else {
        this.openPopup('课程状态错误！', 'error', 'red')
      }
    },
    roomChange (val) {
      this.room_id = val.value
      this.roomName = val.title
      this.Student_Able = true
      this.openRoom = false
      this.Reading = true
      this.$http.get(
        API.listStudentB,
        { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
      ).then((response) => {
        this.students = response.body.students
        this.studentAll = response.body.select
        this.student_id = this.studentAll
        this.Reading = false
      }, (response) => {
        this.openPopup('服务器内部错误！', 'error', 'red')
      })
    },
    goReset () {
      this.title = ''
      this.content = ''
      this.reply = '1'
      this.titleErrorText = ''
      this.contentErrorText = ''
      this.titleErrorColor = ''
      this.contentErrorColor = ''
      this.Room_Able = false
      this.Student_Able = false
      this.courseName = '请选择课程'
      this.course_id = []
      this.courseAll = ''
      this.roomName = '请选择班级'
      this.room_id = []
      this.roomAll = ''
      this.studentName = '请选择学生'
      this.student_id = []
      this.studentAll = ''
    },
    closeStudent () {
      this.openStudent = false
      if (this.student_id.toString() === this.studentAll.toString()) {
        this.studentName = '已全选'
      } else if (this.room_id.toString() !== '') {
        this.studentName = '部分选择'
      } else {
        this.studentName = '请选择学生'
      }
    },
    checkTitle (value) {
      if (value === null || value === undefined || value === '') {
        this.titleErrorText = '标题为必填项!'
        this.titleErrorColor = 'orange'
      } else if (value.length > 20) {
        this.titleErrorText = '标题不得超过20字符'
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
      } else if (value.length > 200) {
        this.contentErrorText = '内容不得超过200字符'
        this.contentErrorColor = 'orange'
      } else {
        this.contentErrorText = 'OK'
        this.contentErrorColor = 'green'
      }
    },
    goSave () {
      this.Sending = false
      this.Saving = true
      this.$http.get(
        API.save,
        { params: {
          title: this.title,
          content: this.content,
          reply: this.reply,
          teacher_id: this.teacher_id
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
