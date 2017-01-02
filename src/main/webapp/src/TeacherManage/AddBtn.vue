<template>
  <div class="AddBtn">
    <router-link to="/add"><mt-button type="primary" size="large" v-on:click.native="doTheThing">
      {{ buttonName }}
    </mt-button></router-link>
  </div>
</template>

<script>
export default {
  name: 'AddBtn',
  props: { 'buttonName': { type: String, default: '未命名' } },
  data () {
    return {
      msg: '新增教师'
    }
  },
  methods: {
    doTheThing: function () {
      this.$emit('doTheThing')
    }
  }
}
</script>
<style>
  a{
text-decoration:none;
}
</style>
