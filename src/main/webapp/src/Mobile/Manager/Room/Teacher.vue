<template>
  <div class="box">
    <mu-checkbox v-for="list in lists" label="list"/>
  </div>
</template>
<script>
export default {
  name: 'Edit',
  data () {
    return {
      lists: [ 1, 2, 3, 4, 5, 6, 7, 8, 9, 0 ]
    }
  }
}
</script>
<style lang="css">
  .box{
    display: flex;
    flex-wrap: wrap;
  }
</style>
