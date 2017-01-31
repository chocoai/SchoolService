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
        <mu-list-item title="教师管理" @click.native="goTeacher">
          <mu-icon slot="left" value="group" color="#f44336"/>
        </mu-list-item>
        <mu-list-item title="班级管理" @click.native="goRoom">
          <mu-icon slot="left" value="store" color="#9c27b0"/>
        </mu-list-item>
        <mu-list-item title="社团管理" @click.native="goTeam">
          <mu-icon slot="left" value="account_balance" color="#673ab7"/>
        </mu-list-item>
        <mu-list-item title="学生管理" @click.native="goStudent">
          <mu-icon slot="left" value="face" color="#4caf50"/>
        </mu-list-item>
        <mu-list-item title="家长管理" @click.native="goParent">
          <mu-icon slot="left" value="accessibility" color="#ffc107"/>
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
        window.location.href = '/forManager'
      },
      goTeacher () {
        window.location.href = '/teacher/forManager'
      },
      goRoom () {
        window.location.href = '/room/forManager'
      },
      goTeam () {
        window.location.href = '/team/forManager'
      },
      goStudent () {
        window.location.href = '/student/forManager'
      },
      goParent () {
        window.location.href = '/parent/forManager'
      }
    }
  }
</script>
