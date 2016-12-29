<template>
  <div class="AddBtn">
    <mt-button @click.native="handleClick" type="primary" size="large">{{ msg }}</mt-button>
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
