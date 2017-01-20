<template>
  <div class="RoomAdd">
    <mu-appbar title="请核实后输入以下信息">
      <mu-icon-button icon='reply' slot="right" @click="reply"/>
    </mu-appbar>
    <mu-text-field v-model="name" label="班级名称" icon="comment" :errorColor="nameErrorColor" :errorText="nameErrorText" @input="checkName" fullWidth labelFloat/><br/>
    <mu-select-field v-for="course in courses" :v-model="course.name" :label="course.name" icon="comment" fullWidth :maxHeight="300" multiple>
      <mu-menu-item v-for="teacher in teachers"  :value="teacher.id" :title="teacher.name"/>
    </mu-select-field>
    <mu-popup position="bottom" :overlay="false" popupClass="popup-bottom" :open="bottomPopup">
      <mu-icon :value="icon" :size="36" :color="color"/>&nbsp;{{ message }}
    </mu-popup>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="cached" @click="reset" backgroundColor="orange"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="done" @click="save" backgroundColor="green"/>
      </mu-flexbox-item>
    </mu-flexbox>
  </div>
</template>

<script>
import * as API from './RoomAPI.js'
export default {
  name: 'RoomAdd',
  data () {
    return {
      bottomPopup: false,
      icon: '',
      color: '',
      name: '',
      message: '',
      nameErrorText: '',
      nameErrorColor: '',
      courses: [],
      teachers: []
    }
  },
  created: function () {
    this.$http.get(
      API.CourseList,
      {
        headers:
        {
          'X-Requested-With': 'XMLHttpRequest'
        },
        emulateJSON: true
      }
    ).then((response) => {
      this.courses = response.body
    }, (response) => {
      this.openPopup('服务器内部错误！', 'report_problem', 'orange')
    })
    this.$http.get(
      API.TeacherList,
      {
        headers:
        {
          'X-Requested-With': 'XMLHttpRequest'
        },
        emulateJSON: true
      }
    ).then((response) => {
      this.teachers = response.body
    }, (response) => {
      this.openPopup('服务器内部错误！', 'report_problem', 'orange')
    })
  },
  methods: {
    reply () {
      this.$router.push({ path: '/roomList' })
    },
    openPopup (message, icon, color) {
      this.message = message
      this.icon = icon
      this.color = color
      this.bottomPopup = true
      setTimeout(() => { this.bottomPopup = false }, 1500)
    },
    reset () {
      this.name = ''
      this.nameErrorColor = ''
      this.course1 = ''
      this.course2 = ''
      this.course3 = ''
      this.course4 = ''
    },
    checkName (value) {
      this.$http.get(
        API.CheckNameForNew,
        { params: {
          name: value
        }
        },
        {
          headers:
          {
            'X-Requested-With': 'XMLHttpRequest'
          }
        }).then((response) => {
          if (response.body === 'error') {
            window.location.href = '/'
          } else if (response.body === 'OK') {
            this.nameErrorText = ''
            this.nameErrorColor = 'green'
          } else {
            this.nameErrorText = response.body
            this.nameErrorColor = 'red'
          }
        }, (response) => {
          this.openPopup('服务器内部错误！', 'report_problem', 'orange')
        })
    },
    save () {
      console.log(this.班主任)
      console.log(this.数学)
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
