<template>
  <div>
    <Page :total="pageTotal" :current="pageCurrent" show-sizer show-elevator show-total :page-size="pageSize" :on-page-size-change="sizeChange()" on-change="pageChange()"></Page>
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
