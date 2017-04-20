<template>
  <div class="layout">
    <Menu mode="horizontal" theme="dark" active-name="1">
      <div class="layout-logo"></div>
      <div class="layout-nav">
        <Menu-item name="3">
          <Icon type="ios-analytics"></Icon>
          退出
        </Menu-item>
      </div>
    </Menu>
    <div class="layout-content">
      <br>
      <Row type="flex" justify="end">
        <Col span="2">
          <Button shape="circle" type="info" @click="goAdd">新建</Button>
        </Col>
        <Col span="20">
          <Form inline>
          <Form-item>
            <Input type="text" v-model="queryName" placeholder="本人/配偶/子女的姓名"><span slot="prepend">姓名</span></Input>
          </Form-item>
          <Form-item>
            <Input type="text" v-model="queryNumber" placeholder="本人/配偶/子女的证件号码"><span slot="prepend">证件号码</span></Input>
          </Form-item>
          <Form-item>
            <Select v-model="queryCategory" clearable style="width:150px" placeholder="请选择申请类别">
              <Option value="灵活就业人员" >灵活就业人员</Option>
              <Option value="公益性岗位" >公益性岗位</Option>
              <Option value="企业吸纳" >企业吸纳</Option>
            </Select>
          </Form-item>
          <Form-item>
            <Button type="primary" @click="goQuery">查找</Button>
          </Form-item>
            <Form-item>
              <Button type="primary" @click="goQuery">导出</Button>
            </Form-item>
        </Form>
        </Col>
      </Row>
      <Row>
        <Col>
          <Table height="200" highlight-row :context="self" :columns="columns" :data="list"></Table>
        </Col>
      </Row>
      <br>
      <Row type="flex" justify="end">
        <Col>
          <Page :total="pageTotal" :current="pageCurrent" show-sizer show-elevator show-total :page-size="pageSize" :on-page-size-change="sizeChange()" on-change="pageChange()"></Page>
        </Col>
      </Row>
    </div>
    <div class="layout-copy">
      2017 &copy; WangYiXin
    </div>
 </div>
</template>
<script>
  import * as API from './API.js'
  export default {
    name: 'add',
    data () {
      return {
        self: this,
        columns: [
          {
            title: '姓名',
            key: 'name'
          },
          {
            title: '证件号码',
            key: 'number'
          },
          {
            title: '申请类别',
            key: 'category'
          },
          {
            title: '申请条件',
            key: 'type'
          },
          {
            title: '所属中心',
            key: 'location'
          },
          {
            title: '操作',
            key: 'action',
            align: 'center',
            render (row, column, index) {
              return `<i-button type="primary" size="small" @click="show(${index})">查看</i-button> <i-button type="error" size="small" @click="remove(${index})">删除</i-button>`
            }
          }
        ],
        queryName: '',
        queryNumber: '',
        queryCategory: '',
        pageTotal: '',
        pageSize: '10',
        pageCurrent: '',
        list: []
      }
    },
    created: function () {
      this.queryName = this.$store.state.queryName
      this.queryNumber = this.$store.state.queryNumber
      this.queryCategory = this.$store.state.queryCategory
      this.pageCurrent = this.$store.state.pageCurrent
      this.query(this.queryName, this.queryNumber, this.queryCategory, this.pageCurrent, this.pageSize)
      this.total(this.queryName, this.queryNumber, this.queryCategory)
    },
    methods: {
      goAdd () {
        this.$router.push({ path: '/add' })
      },
      query (queryName, queryNumber, queryCategory, pageCurrent, pageSize) {
        this.$http.get(
          API.query,
          { params: {
            queryName: queryName,
            queryNumber: queryNumber,
            queryCategory: queryCategory,
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
      total (queryName, queryNumber, queryCategory) {
        this.$http.get(
          API.total,
          { params: {
            queryName: queryName,
            queryNumber: queryNumber,
            queryCategory: queryCategory
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
      goQuery (queryName, queryNumber, queryCategory) {
        this.queryName = queryName
        this.queryNumber = queryNumber
        this.queryCategory = queryCategory
        this.pageCurrent = '1'
        this.$store.commit('save', {
          queryName: this.queryName,
          queryNumber: this.queryNumber,
          queryCategory: this.queryCategory,
          pageCurrent: this.pageCurrent
        })
        this.query(this.queryName, this.queryNumber, this.queryCategory, this.pageCurrent, this.pageSize)
        this.total(this.queryName, this.queryNumber, this.queryCategory)
      },
      sizeChange (value) {
        this.pageSize = value
      },
      pageChange (value) {
        this.pageCurrent = value
      },
      goEdit (id) {
        this.$router.push({ path: '/edit/' + id })
        this.$store.commit('save', {
          queryName: this.$store.state.queryName,
          queryNumber: this.$store.state.queryNumber,
          queryCategory: this.$store.state.queryCategory,
          pageCurrent: this.pageCurrent
        })
      },
      goDelete (id) {
        console.log(id)
      }
    }
  }
</script>
<style scoped>
    .layout{
        border: 1px solid #d7dde4;
        background: #f5f7f9;
    }
    .layout-logo{
        width: 100px;
        height: 30px;
        background: #5b6270;
        border-radius: 3px;
        float: left;
        position: relative;
        top: 15px;
        left: 20px;
    }
    .layout-nav{
        width: 420px;
        margin: 0 auto;
    }
    .layout-assistant{
        width: 300px;
        margin: 0 auto;
        height: inherit;
    }
    .layout-breadcrumb{
        padding: 10px 15px 0;
    }
    .layout-content{
        min-height: 300px;
        margin: 15px;
        overflow: hidden;
        background: #fff;
        border-radius: 4px;
    }
    .layout-content-main{
        padding: 10px;
    }
    .layout-copy{
        text-align: center;
        padding: 10px 0 20px;
        color: #9ea7b4;
    }
</style>
