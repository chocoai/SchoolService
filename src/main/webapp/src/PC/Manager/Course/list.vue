<template>
  <div class="layout">
    <Row>
      <Col><MenuList active="course" :name="name" three="列表"></MenuList></Col>
    </Row>
    <Row>
      <Col>
        <div>
          <div class="left"><Button type="info" shape="circle">新增</Button></div>
          <div class="right"><Search @goQuery="getQuery"></Search></div>
        </div>
      </Col>
    </Row>
    <Row>
      <Col>
        <div class="layout-content">
          内容区域
          {{pageList}}
        </div>
      </Col>
    </Row>
    <Row type="flex" justify="end">
      <Col>
        <Page
          ref="pages"
          @goList="getList"
          :queryURL="query"
          :totalURL="total"
          :keyword="keyword"
        >
        </Page>
      </Col>
    </Row>
    <Row><Col><Copy></Copy></Col></Row>
  </div>
</template>
<script>
  import Copy from '../../Common/copy.vue'
  import MenuList from '../Menu/menuList.vue'
  import Search from '../../Common/search.vue'
  import Page from '../../Common/page.vue'
  import * as API from './API.js'
  export default {
    name: 'list',
    components: { Copy, MenuList, Search, Page },
    data () {
      return {
        name: 'xxx',
        query: API.query,
        total: API.total,
        keyword: '',
        pageList: []
      }
    },
    methods: {
      getQuery (keyword) {
        this.keyword = keyword
        this.$refs.pages.query(keyword)
      },
      getList (pageList) {
        this.pageList = pageList
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
