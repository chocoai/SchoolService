<template>
  <div class="query">
    <Input type="text" v-model="keyword" placeholder="请输入关键词" style="width:300px;">
      <span slot="prepend">关键词</span>
      <Button slot="append" icon="search" @click="goQuery">搜索</Button>
    </Input>
  </div>
</template>
<script>
  export default {
    data () {
      return {
        keyword: ''
      }
    },
    methods: {
      goQuery () {
        this.$emit('goQuery', this.keyword)
      }
    }
  }
</script>
<style>
  .query{
    float:right ;
  }
</style>
