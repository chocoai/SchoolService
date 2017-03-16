<template>
  <div class="Menu">
    <mu-drawer :open="open" @close="closeMenu">
      <mu-appbar :title="menuTitle" @click.native="closeMenu">
        <mu-icon-button icon='close' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item title="主页" @click.native="goHome">
          <mu-icon slot="left" value="school" color="#795548"/>
        </mu-list-item>
        <mu-list-item title="学期管理" @click.native="goSemester">
          <mu-icon slot="left" value="account_balance_wallet" color="red300"/>
        </mu-list-item>
        <mu-list-item title="课程管理" @click.native="goCourse">
          <mu-icon slot="left" value="chrome_reader_mode" color="purple300"/>
        </mu-list-item>
        <mu-list-item title="教师管理" @click.native="goTeacher">
          <mu-icon slot="left" value="group" color="indigo300"/>
        </mu-list-item>
        <mu-list-item title="班级管理" @click.native="goRoom">
          <mu-icon slot="left" value="store" color="lightBlue300"/>
        </mu-list-item>
        <mu-list-item title="学生管理" @click.native="goStudent">
          <mu-icon slot="left" value="face" color="teal300"/>
        </mu-list-item>
        <mu-list-item title="家长管理" @click.native="goParent">
          <mu-icon slot="left" value="accessibility" color="lightGreen300"/>
        </mu-list-item>
        <mu-list-item title="教师消息管理" @click.native="goTeacherMessage">
          <mu-icon slot="left" value="note" color="yellow300"/>
        </mu-list-item>
        <mu-list-item title="退出" @click.native="goExit">
          <mu-icon slot="left" value="exit_to_app" color="grey900"/>
        </mu-list-item>
      </mu-list>
    </mu-drawer>
  </div>
</template>
<script>
  export default {
    name: 'Menu',
    props: ['open'],
    data () {
      return {
        menuTitle: '济南市育明小学'
      }
    },
    methods: {
      openMenu () {
        this.open = true
      },
      closeMenu () {
        this.open = false
        this.$emit('closeMenu')
      },
      goHome () {
        window.location.href = '/Mobile_Manager_Home'
      },
      goSemester () {
        window.location.href = '/semester/Mobile_Manager_Semester'
      },
      goCourse () {
        window.location.href = '/course/Mobile_Manager_Course'
      },
      goTeacher () {
        window.location.href = '/teacher/Mobile_Manager_Teacher'
      },
      goRoom () {
        window.location.href = '/room/Mobile_Manager_Room'
      },
      goStudent () {
        window.location.href = '/student/Mobile_Manager_Student'
      },
      goParent () {
        window.location.href = '/parent/Mobile_Manager_Parent'
      },
      goTeacherMessage () {
        window.location.href = '/teacherMessage/Mobile_Manager_TeacherMessage'
      },
      goExit () {
        window.location.href = '/exit'
      }
    }
  }
</script>
