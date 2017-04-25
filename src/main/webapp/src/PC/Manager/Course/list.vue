<template>
  <div class="layout">
    <Row>
      <Col><MenuList active="course" :name="name" three="列表"></MenuList></Col>
    </Row>
    <Row>
      <Col>
        <div>
          <div class="left"><Button type="info" shape="circle">新增</Button></div>
          <div class="right"><Search v-on:goQuery="query"></Search></div>
        </div>
      </Col>
    </Row>
    <Row>
      <Col>
        <div class="layout-content">
          内容区域
        </div>
      </Col>
    </Row>
    <Row>
      <Col>
        <Page :total="pageTotal" :current="pageCurrent" show-sizer show-elevator show-total :page-size="pageSize" :on-page-size-change="sizeChange" on-change="pageChange"></Page>
      </Col>
    </Row>
    <Row><Col><Copy></Copy></Col></Row>
  </div>
</template>
<script>
  import Copy from '../../Common/copy.vue'
  import MenuList from '../Menu/menuList.vue'
  import Search from '../../Common/search.vue'
  export default {
    name: 'course',
    components: { Copy, MenuList, Search },
    data () {
      return {
        name: 'xxx',
        keyword: '',
        pageTotal: '',
        pageSize: '10',
        pageCurrent: '',
        list: []
      }
    },
    created: function () {
      this.keyword = this.$store.state.keyword
      this.pageCurrent = this.$store.state.pageCurrent
      this.query(this.keyword, this.pageCurrent, this.pageSize)
      this.total(this.keyword)
    },
    methods: {
      query (keyword, pageCurrent, pageSize) {
        this.$http.get(
          API.query,
          { params: {
            keyword: keyword,
            pageCurrent: pageCurrent,
            pageSize: pageSize
          } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
        ).then((response) => {
          this.list = response.body
          this.pageCurrent = pageCurrent
        }, (response) => {
          this.$Notice.error({
            title: '服务器内部错误!'
          })
        })
      },
      total (keyword) {
        this.$http.get(
          API.total,
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
      },
      pageChange (value) {
        this.pageCurrent = value
      },
      query (keyword) {
        console.log(keyword)
      }
    }
  }
</script>
<style scoped>
  .layout{
    border: 1px solid #d7dde4;
    background: #f5f7f9;
  }
  .layout-assistant{
    width: 300px;
    margin: 0 auto;
    height: inherit;
  }
  .layout-content{
    min-height: 400px;
    margin: 15px;
    overflow: hidden;
    background: #fff;
    border-radius: 4px;
  }
  .layout-content-main{
    padding: 10px;
  }
  .left{
    margin: 15px;
    border-radius: 4px;
    float: left;
  }
  .right{
    margin: 15px;
    border-radius: 4px;
    float: right;
  }
</style>
