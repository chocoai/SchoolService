<template>
  <div class="Menu">
    <mu-drawer :open="open" @close="closeMenu">
      <mu-list>
        <mu-list-item @click.native="closeMenu" title="关闭"/>
        <mu-list-item title="主页"/>
        <mu-list-item title="教师管理"/>
      </mu-list>
    </mu-drawer>
  </div>
</template>
<script>
  export default {
    name: 'Menu',
    props: ['open'],
    methods: {
      openMenu () {
        this.open = true
      },
      closeMenu () {
        this.open = false
        this.$emit('closeMenu')
      }
    }
  }
</script>
