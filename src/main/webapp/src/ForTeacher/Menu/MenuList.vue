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
        <mu-list-item title="公告管理" @click.native="goNotice">
          <mu-icon slot="left" value="note" color="#2196f3"/>
        </mu-list-item>
        <mu-list-item title="班级学生" @click.native="goStudentOfRoom">
          <mu-icon slot="left" value="face" color="#9c27b0"/>
        </mu-list-item>
        <mu-list-item title="社团学生" @click.native="goStudentOfTeam">
          <mu-icon slot="left" value="face" color="#673ab7"/>
        </mu-list-item>
        <mu-list-item title="班级消息" @click.native="goRoomwork">
          <mu-icon slot="left" value="note" color="#9c27b0"/>
        </mu-list-item>
        <mu-list-item title="社团消息" @click.native="goTeamwork">
          <mu-icon slot="left" value="note" color="#673ab7"/>
        </mu-list-item>
        <mu-list-item title="用户信息" @click.native="goPersonal">
          <mu-icon slot="left" value="person" color="#2196f3"/>
        </mu-list-item>
        <mu-list-item title="退出" @click.native="goExit">
          <mu-icon slot="left" value="exit_to_app" color="#212121"/>
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
        window.location.href = '/forTeacher'
      },
      goStudentOfRoom () {
        window.location.href = '/student/forRoomTeacher'
      },
      goStudentOfTeam () {
        window.location.href = '/student/forTeamTeacher'
      },
      goRoomwork () {
        window.location.href = '/roomwork/forTeacher'
      },
      goTeamwork () {
        window.location.href = '/teamwork/forTeacher'
      },
      goNotice () {
        window.location.href = '/notice/forTeacher'
      },
      goPersonal () {
        window.location.href = '/teacher/forTeacherPersonal'
      },
      goExit () {
        window.location.href = '/exit'
      }
    }
  }
</script>
