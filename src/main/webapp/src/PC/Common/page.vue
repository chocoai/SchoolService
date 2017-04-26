<template>
  <Page :total="pageTotal" :current="pageCurrent" :page-size="pageSize" @on-page-size-change="sizeChange" @on-change="pageChange" show-sizer show-elevator show-total></Page>
</template>
<script>
  export default {
    props: ['queryURL', 'totalURL', 'keyword'],
    data () {
      return {
        pageCurrent: 1,
        pageSize: 10,
        pageTotal: 0,
        pageList: []
      }
    },
    created: function () {
      console.log(this.queryURL)
      console.log(this.totalURL)
      this.getLists(this.queryURL, this.keyword, this.pageCurrent, this.pageSize)
      this.getTotal(this.totalURL, this.keyword)
    },
    methods: {
      getLists (queryURL, keyword, pageCurrent, pageSize) {
        this.$http.get(
          'query',
          { params: {
            keyword: keyword,
            pageCurrent: pageCurrent,
            pageSize: pageSize
          } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
        ).then((response) => {
          this.pageList = response.body
          this.$emit('goList', this.pageList)
        }, (response) => {
          this.$Notice.error({
            title: '服务器内部错误!'
          })
        })
      },
      getTotal (totalURL, keyword) {
        this.$http.get(
          'total',
          { params: {
            keyword: keyword
          } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          this.pageTotal = response.body
        }, (response) => {
          this.$Notice.error({
            title: '服务器内部错误!'
          })
        })
      },
      sizeChange (value) {
        this.pageSize = value
        this.pageCurrent = '1'
        this.$store.commit('save', {
          pageCurrent: this.pageCurrent
        })
        this.getLists(this.queryURL, this.keyword, this.pageCurrent, this.pageSize)
      },
      pageChange (value) {
        this.pageCurrent = value
        this.getLists(this.queryURL, this.keyword, this.pageCurrent, this.pageSize)
      },
      query (keyword) {
        this.keyword = keyword
        this.pageCurrent = '1'
        this.$store.commit('save', {
          keyword: this.keyword,
          pageCurrent: this.pageCurrent
        })
        this.getLists(this.queryURL, this.keyword, this.pageCurrent, this.pageSize)
        this.getTotal(this.totalURL, this.keyword)
      }
    }
  }
</script>
<style>
  .query{
    float:right ;
  }
</style>
