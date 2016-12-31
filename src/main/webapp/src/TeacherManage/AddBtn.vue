<template>
  <div class="AddBtn">
    <router-link to="/add"><mt-button type="primary" size="large">
      {{ msg }}
    </mt-button></router-link>
  </div>
</template>

<script>
export default {
  name: 'AddBtn',
  data () {
    return {
      msg: '新增教师'
    }
  }
}
</script>
<style>
  a{
text-decoration:none;
}
</style>
