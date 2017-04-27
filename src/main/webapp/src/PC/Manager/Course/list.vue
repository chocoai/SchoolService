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
            highlight-row
            :height="height"
            :context="self"
            :border="border"
            :stripe="stripe"
            :size="size"
            :columns="columns"
            :data="pageList">
          </Table>
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
        size: 'small',
        height: 440,
        self: this,
        columns: [
          {
            title: '序号',
            key: 'id',
            sortable: true
          },
          {
            title: '课程名称',
            key: 'name',
            sortable: true
          },
          {
            title: '课程描述',
            key: 'detail',
            sortable: true
          },
          {
            title: '课程类型',
            key: 'tname',
            sortable: true
          },
          {
            title: '课程状态',
            key: 'state',
            sortable: true,
            render (row) {
              const color = row.state === '1' ? 'green' : row.state === '0' ? 'yellow' : 'red'
              const text = row.state === '1' ? '可用' : row.state === '0' ? '停用' : '错误'
              return `<tag type="dot" color="${color}">${text}</tag>`
            }
          },
          {
            title: '操作',
            key: 'id',
            align: 'center',
            render (row, column, index) {
              return `
              <i-button type="primary" size="small" @click="show(${row.id})">查看</i-button>
              <i-button type="error" size="small" @click="remove(${index})">删除</i-button>
              <i-button type="error" size="small" @click="r(${index})">注销/i-button>
              `
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
        if (tableSize.toString() === 'true') {
          this.height = 664
          this.size = 'large'
        } else {
          this.height = 440
          this.size = 'small'
        }
      },
      getList (pageList) {
        this.pageList = pageList
      },
      show (row) {
        console.log('row: ' + row)
      },
      remove (column) {
        console.log('column: ' + column)
      },
      r (index) {
        console.log('index: ' + index)
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
