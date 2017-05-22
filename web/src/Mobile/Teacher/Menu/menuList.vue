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
        <mu-list-item title="学期管理" @click.native="goSemester" v-if="menu.SemesterMobile_Page">
          <mu-icon slot="left" value="chrome_reader_mode" color="purple300"/>
        </mu-list-item>
        <mu-list-item title="课程管理" @click.native="goCourse" v-if="menu.CourseMobile_Page">
          <mu-icon slot="left" value="chrome_reader_mode" color="purple300"/>
        </mu-list-item>
        <mu-list-item title="班级管理" @click.native="goRoom" v-if="menu.RoomMobile_Page">
          <mu-icon slot="left" value="chrome_reader_mode" color="purple300"/>
        </mu-list-item>
        <mu-list-item title="教师管理" @click.native="goTeacher" v-if="menu.TeacherMobile_Page">
          <mu-icon slot="left" value="chrome_reader_mode" color="purple300"/>
        </mu-list-item>
        <mu-list-item title="家长管理" @click.native="goParent" v-if="menu.ParentMobile_Page">
          <mu-icon slot="left" value="chrome_reader_mode" color="purple300"/>
        </mu-list-item>
        <mu-list-item title="学生管理" @click.native="goStudent" v-if="menu.StudentMobile_Page">
          <mu-icon slot="left" value="chrome_reader_mode" color="purple300"/>
        </mu-list-item>
        <mu-list-item title="我的学生" @click.native="goMyStudent" v-if="menu.MyStudentMobile_Page">
          <mu-icon slot="left" value="chrome_reader_mode" color="purple300"/>
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
    props: ['open', 'menu'],
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
        window.location.href = '../MainMobile/Teacher_Home'
      },
      goSemester () {
        window.location.href = '/SemesterMobile/Page'
      },
      goCourse () {
        window.location.href = '/CourseMobile/Page'
      },
      goRoom () {
        window.location.href = '/RoomMobile/Page'
      },
      goTeacher () {
        window.location.href = '/TeacherMobile/Page'
      },
      goParent () {
        window.location.href = '/ParentMobile/Page'
      },
      goStudent () {
        window.location.href = '/StudentMobile/Page'
      },
      goMyStudent () {
        window.location.href = '/MyStudentMobile/Page'
      },
      goExit () {
        window.location.href = '/exit'
      }
    }
  }
</script>
