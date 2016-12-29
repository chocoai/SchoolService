<template>
  <div class="AddBtn">
    <mt-button @click.native="handleClick" type="primary">{{ msg }}</mt-button>
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
