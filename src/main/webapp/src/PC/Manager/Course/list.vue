<template>
  <div class="layout">
    <Row>
      <Col><MenuList active="course" :name="name" three="列表"></MenuList></Col>
    </Row>
    <Row>
      <Col>
        <div>
          <div class="left"><Button type="info" shape="circle"><Icon type="plus-round"></Icon>新增</Button></div>
          <div class="right"><Search @goQuery="getQuery"></Search></div>
        </div>
      </Col>
    </Row>
    <Row>
      <Col>
        <div class="layout-content">
          <Table
            :height="height"
            highlight-row
            :border="border"
            :stripe="stripe"
            :size="size"
            :context="self"
            :columns="columns"
            :data="pageList"></Table>
        </div>
      </Col>
    </Row>
    <Row>
      <Col>
      <div>
        <div class="left">
          <Options
            @showBorder="getBorder"
            @showStripe="getStripe"
            @tableSize="getSize"
          >
          </Options>
        </div>
        <div class="right">
          <Page
          ref="pages"
          @goList="getList"
          :queryURL="query"
          :totalURL="total"
          :keyword="keyword"
        >
        </Page></div>
      </div>
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
  import Options from '../../Common/options.vue'
  import * as API from './API.js'
  export default {
    name: 'list',
    components: { Copy, MenuList, Search, Page, Options },
    data () {
      return {
        name: 'xxx',
        query: API.query,
        total: API.total,
        keyword: '',
        pageList: [],
        border: false,
        stripe: false,
        size: 'default',
        height: 520,
        self: this,
        columns: [
          {
            title: '序号',
            key: 'id'
          },
          {
            title: '课程名称',
            key: 'name'
          },
          {
            title: '课程描述',
            key: 'detail'
          },
          {
            title: '课程类型',
            key: 'tname'
          },
          {
            title: '课程状态',
            key: 'sname'
          },
          {
            title: '操作',
            key: 'action',
            align: 'center',
            render (row, column, index) {
              return `<i-button type="primary" size="small" @click="show(${index})">查看</i-button> <i-button type="error" size="small" @click="remove(${index})">删除</i-button>`
            }
          }
        ]
      }
    },
    methods: {
      getQuery (keyword) {
        this.keyword = keyword
        this.$refs.pages.query(keyword)
      },
      getBorder (border) {
        this.border = border
      },
      getStripe (stripe) {
        this.stripe = stripe
      },
      getSize (tableSize) {
        this.size = tableSize
        if (tableSize.toString() === 'large') {
          this.height = 660
        } else if (tableSize.toString() === 'small') {
          this.height = 440
        } else {
          this.height = 520
        }
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
    margin:0px 15px 0px 15px;
    overflow: hidden;
    background: #fff;
    border-radius: 4px;
  }
  .layout-content-main{
    padding: 5px;
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
