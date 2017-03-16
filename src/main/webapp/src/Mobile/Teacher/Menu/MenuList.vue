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
        <mu-list-item title="教师消息" @click.native="goTeacherMessage">
          <mu-icon slot="left" value="chrome_reader_mode" color="purple300"/>
        </mu-list-item>
        <mu-list-item title="班级课程" @click.native="goCourse">
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
        window.location.href = '/Mobile_Teacher_Home'
      },
      goTeacherMessage () {
        window.location.href = '/teacherMessageRead/Mobile_Teacher_TeacherMessage'
      },
      goCourse () {
        window.location.href = '/course/Mobile_Teacher_Course'
      },
      goExit () {
        window.location.href = '/exit'
      }
    }
  }
</script>
