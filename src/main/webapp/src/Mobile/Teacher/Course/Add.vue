<template>
  <div>
    <mu-appbar title="教授新课程">
      <mu-icon-button icon='reply' slot="right" @click="gorReply"/>
    </mu-appbar>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-raised-button :label="courseName" @click="openCourse=true" primary/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-raised-button :label="roomName" @click="openRoom=true" primary/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-dialog :open="Reading" title="正在读取" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
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
    <mu-drawer right :open="openCourse" docked="false">
      <mu-appbar title="请选择所授课程"/>
      <mu-list :value="course_id" @itemClick="courseChange">
        <mu-list-item v-for="course in lists" :title="course.name" :value="course.id" :describeText="course.detail" :afterText="getType(course.type)">
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-drawer right :open="openRoom" docked="false">
      <mu-appbar title="请选择所授班级"/>
      <mu-list :value="room_id" @itemClick="roomChange">
        <mu-list-item v-for="room in listz" :title="room.name" :value="room.id" :describeText="room.slogan">
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
      Reading: true,
      Saving: false,
      icon: '',
      color: '',
      courseName: '请选择所授课程',
      roomName: '请选择所授班级',
      lists: [],
      listz: [],
      message: ''
    }
  },
  created: function () {
    this.$http.get(
      API.list,
      { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
    ).then((response) => {
      this.lists = response.body.lists
      this.listz = response.body.listz
      this.Reading = false
    }, (response) => {
      this.openPopup('服务器内部错误！', 'error', 'red')
    })
  },
  computed: {
    Save_Able: function () {
      if (this.courseName.toString() !== '请选择所授课程' && this.roomName.toString() !== '请选择所授班级') {
        return false
      } else {
        return true
      }
    }
  },
  methods: {
    getType (type) {
      if (type.toString() === '1') {
        return '必修课'
      } else if (type.toString() === '2') {
        return '选修课'
      } else {
        return '错误'
      }
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
      this.courseName = '请选择所授课程'
      this.roomName = '请选择所授班级'
      this.room_id = ''
      this.course_id = ''
    },
    goSave () {
      this.Saving = true
      this.$http.get(
        API.save,
        { params: {
          room_id: this.room_id,
          course_id: this.course_id
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
