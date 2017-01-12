<template>
  <div class="Login">
    <mu-appbar title="欢迎使用校园管理系统"/>
    <mu-text-field hintText="用户名" icon="phone"/><br/>
    <mu-text-field hintText="密码" icon="phone"/><br/>
  </div>
</template>
<script>
  export default {
    name: 'Login',
    data () {
      return {
        open: false,
        schoolName: ''
      }
    },
    methods: {
      login () {

      }
    }
  }
</script>

