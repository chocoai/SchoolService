<template>
  <div class="Menu">
    <mu-drawer :open="open" @close="closeMenu">
      <mu-appbar :title="menuTitle" @click.native="closeMenu">
        <mu-icon-button icon='close' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item title="主页" @click.native="goHome">
          <mu-icon slot="left" value="school"/>
        </mu-list-item>
        <mu-list-item title="教师管理" @click.native="goTeacherManage">
          <mu-icon slot="left" value="inbox"/>
        </mu-list-item>
        <mu-list-item title="班级管理" @click.native="goRoomManage">
          <mu-icon slot="left" value="inbox"/>
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
        window.location.href = '/'
      },
      goTeacherManage () {
        window.location.href = '/teacher'
      },
      goRoomManage () {
        window.location.href = '/room'
      }
    }
  }
</script>
