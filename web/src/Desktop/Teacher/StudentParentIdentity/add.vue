<template>
  <div class="layout">
    <Row>
      <Col><MenuList :active="base" :name="name" three="新增" :menu="menu"></MenuList></Col>
    </Row>
    <Row v-if="showLoad">
      <Col><Loading></Loading></Col>
    </Row>
    <Row v-show="!showLoad">
      <Col span="9">
        <div class="right">
          <div class="queryLeft">
            <Input type="text" v-model="keyword1" placeholder="请输入关键词" style="width:270px;">
            <span slot="prepend">关键词</span>
            </Input>
          </div>
          <div class="queryRight">
            <Button-group>
              <Button type="ghost" @click="goQuery1">
                <Icon type="search"></Icon>
                搜索
              </Button>
              <Button type="ghost" @click="goQueryReset1">
                <Icon type="refresh"></Icon>
                重置
              </Button>
            </Button-group>
          </div>
        </div>
      </Col>
      <Col span="9">
        <div class="right">
          <div class="queryLeft">
            <Input type="text" v-model="keyword2" placeholder="请输入关键词" style="width:270px;">
            <span slot="prepend">关键词</span>
            </Input>
          </div>
          <div class="queryRight">
            <Button-group>
              <Button type="ghost" @click="goQuery2">
                <Icon type="search"></Icon>
                搜索
              </Button>
              <Button type="ghost" @click="goQueryReset2">
                <Icon type="refresh"></Icon>
                重置
              </Button>
            </Button-group>
          </div>
        </div>
      </Col>
      <Col span="6">
        <div class="right">
          <div class="queryLeft">
            <Input type="text" v-model="keyword3" placeholder="请输入关键词" style="width:150px;">
            <span slot="prepend">关键词</span>
            </Input>
          </div>
          <div class="queryRight">
            <Button-group>
              <Button type="ghost" @click="goQuery3">
                <Icon type="search"></Icon>
                搜索
              </Button>
              <Button type="ghost" @click="goQueryReset3">
                <Icon type="refresh"></Icon>
                重置
              </Button>
            </Button-group>
          </div>
        </div>
      </Col>
    </Row>
    <Row v-show="!showLoad">
      <Col span="9">
        <div class="layout-content">
          <Table
            highlight-row
            @on-row-dblclick="getData1"
            height="450"
            size="small"
            :columns="columns1"
            :data="pageList1">
          </Table>
        </div>
      </Col>
      <Col span="9">
        <div class="layout-content">
          <Table
            highlight-row
            @on-row-dblclick="getData2"
            height="450"
            size="small"
            :columns="columns2"
            :data="pageList2">
          </Table>
        </div>
      </Col>
      <Col span="6">
      <div class="layout-content">
        <Table
          highlight-row
          @on-row-dblclick="getData3"
          height="450"
          size="small"
          :columns="columns3"
          :data="pageList3">
        </Table>
      </div>
      </Col>
    </Row>
    <Row v-show="!showLoad">
      <Col span="9">
        <div class="right">
          <Page
            :total="pageTotal1"
            :current="pageCurrent1"
            :page-size="pageSize1"
            @on-page-size-change="sizeChange1"
            @on-change="pageChange1"
            placement="top"
            show-sizer
            show-elevator
            show-total>
          </Page>
        </div>
      </Col>
      <Col span="9">
        <div class="right">
          <Page
            :total="pageTotal2"
            :current="pageCurrent2"
            :page-size="pageSize2"
            @on-page-size-change="sizeChange2"
            @on-change="pageChange2"
            placement="top"
            show-sizer
            show-elevator
            show-total>
          </Page>
        </div>
      </Col>
      <Col span="6">
        <div class="right">
          <Page
            :total="pageTotal3"
            :current="pageCurrent3"
            :page-size="pageSize3"
            @on-page-size-change="sizeChange3"
            @on-change="pageChange3"
            placement="top"
            show-sizer
            show-total>
          </Page>
        </div>
      </Col>
    </Row>
    <Row v-show="!showLoad">
      <Col span="16">
        <div class="rights">
          <Button size="large" type="info">请双击行选择相关信息</Button>
          <Tag type="dot" color="blue">学生：{{name1}}</Tag>
          <Tag type="dot" color="green">家长：{{name2}}</Tag>
          <Tag type="dot" color="yellow">身份：{{name3}}</Tag>
        </div>
      </Col>
      <Col span="8">
        <Form :label-width="100" ref="addForm" :model="object">
          <Form-item>
            <Button size="large" type="success" @click="goSave" v-if="permission.Save">保存</Button>
            <Button size="large" type="warning" style="margin-left: 8px" @click="goReset">重置</Button>
            <Button size="large" type="ghost" style="margin-left: 8px" @click="goBack" v-if="permission.Page">返回</Button>
          </Form-item>
        </Form>
      </Col>
    </Row>
    <Row><Col><Copy></Copy></Col></Row>
  </div>
</template>
<script>
  import Copy from '../../Common/copy.vue'
  import MenuList from '../Menu/menuList.vue'
  import Loading from '../../Common/loading.vue'
  import * as API from './API.js'
  import { getCookie } from '../../../cookieUtil.js'
  export default {
    name: 'add',
    components: { Copy, MenuList, Loading },
    data () {
      return {
        name: '',
        base: API.base,
        permission: [],
        menu: [],
        keyword1: '',
        keyword2: '',
        keyword3: '',
        pageList1: [],
        pageList2: [],
        pageList3: [],
        pageTotal1: '',
        pageTotal2: '',
        pageTotal3: '',
        pageCurrent1: '',
        pageCurrent2: '',
        pageCurrent3: '',
        name1: '未知',
        name2: '未知',
        name3: '未知',
        id1: '',
        id2: '',
        id3: '',
        showLoad: true,
        columns1: [
          {
            title: '学生姓名',
            key: 'name',
            align: 'center'
          },
          {
            title: '身份证号码',
            key: 'number',
            align: 'center'
          },
          {
            title: '学籍号码',
            key: 'code',
            sortable: true
          },
          {
            title: '性别',
            key: 'sex',
            sortable: true,
            render: (h, params) => {
              const sexes = params.row.sex.toString() === '1' ? 'male' : params.row.sex.toString() === '2' ? 'female' : 'person'
              const color = params.row.sex.toString() === '1' ? '#0099CC' : params.row.sex.toString() === '2' ? '#CC6699' : 'red'
              const text = params.row.sex.toString() === '1' ? '男生' : params.row.sex.toString() === '2' ? '女生' : '错误'
              return h('div', [
                h('Icon', {
                  props: {
                    type: sexes,
                    color: color
                  }
                }),
                h('i', text)
              ])
            }
          }
        ],
        columns2: [
          {
            title: '家长姓名',
            key: 'name',
            align: 'center'
          },
          {
            title: '联系电话',
            key: 'mobile',
            align: 'center'
          }
        ],
        columns3: [
          {
            title: '身份',
            key: 'name',
            align: 'center'
          }
        ]
      }
    },
    created: function () {
      this.keyword1 = this.$store.state.keyword1
      this.pageCurrent1 = parseInt(this.$store.state.pageCurrent1)
      this.pageSize1 = parseInt(this.$store.state.pageSize1)
      this.getLists1()
      this.keyword2 = this.$store.state.keyword2
      this.pageCurrent2 = parseInt(this.$store.state.pageCurrent2)
      this.pageSize2 = parseInt(this.$store.state.pageSize2)
      this.getLists2()
      this.keyword3 = this.$store.state.keyword3
      this.pageCurrent3 = parseInt(this.$store.state.pageCurrent3)
      this.pageSize3 = parseInt(this.$store.state.pageSize3)
      this.getLists3()
      if (getCookie('MenuDesktop') === null || getCookie('MenuDesktop') === undefined || getCookie('MenuDesktop') === '' || getCookie(API.base) === null || getCookie(API.base) === undefined || getCookie(API.base) === '') {
        this.$http.get(
          API.menu
        ).then((response) => {
          if (response.body.toString() === 'illegal' || response.body.toString() === 'overdue') {
            this.$Notice.error({
              title: '登录过期或非法操作!'
            })
          } else {
            this.$http.get(
              API.permission
            ).then((res) => {
              this.permission = JSON.parse(JSON.parse(getCookie(API.base)))
              this.menu = JSON.parse(JSON.parse(getCookie('MenuDesktop')))
              this.name = decodeURI(getCookie('name')).substring(1, decodeURI(getCookie('name')).length - 1)
              this.showLoad = false
            }, (res) => {
              this.$Notice.error({
                title: '服务器内部错误!'
              })
            })
          }
        }, (response) => {
          this.$Notice.error({
            title: '服务器内部错误!'
          })
        })
      } else {
        this.permission = JSON.parse(JSON.parse(getCookie(API.base)))
        this.menu = JSON.parse(JSON.parse(getCookie('MenuDesktop')))
        this.name = decodeURI(getCookie('name')).substring(1, decodeURI(getCookie('name')).length - 1)
        this.showLoad = false
      }
    },
    methods: {
      getLists1 () {
        this.$http.get(
          API.queryStudent,
          { params: {
            keyword: this.$store.state.keyword1,
            pageCurrent: this.$store.state.pageCurrent1,
            pageSize: this.$store.state.pageSize1
          } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
        ).then((res) => {
          if (res.body.toString() === 'illegal' || res.body.toString() === 'overdue') {
          } else {
            this.$http.get(
              API.totalStudent,
              { params: {
                keyword: this.$store.state.keyword1
              } },
              { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
            ).then((response) => {
              if (response.body.toString() === 'illegal' || response.body.toString() === 'overdue') {
                this.$Notice.error({
                  title: '登录过期或非法操作!'
                })
                window.location.href = '/MainDesktop'
              } else {
                this.pageList1 = res.body
                this.pageTotal1 = parseInt(response.body)
              }
            }, (response) => {
              this.$Notice.error({
                title: '服务器内部错误!'
              })
            })
          }
        }, (res) => {
          this.$Notice.error({
            title: '服务器内部错误!'
          })
        })
      },
      getLists2 () {
        this.$http.get(
          API.queryParent,
          { params: {
            keyword: this.$store.state.keyword2,
            pageCurrent: this.$store.state.pageCurrent2,
            pageSize: this.$store.state.pageSize2
          } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
        ).then((res) => {
          if (res.body.toString() === 'illegal' || res.body.toString() === 'overdue') {
          } else {
            this.$http.get(
              API.totalParent,
              { params: {
                keyword: this.$store.state.keyword2
              } },
              { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
            ).then((response) => {
              if (response.body.toString() === 'illegal' || response.body.toString() === 'overdue') {
                this.$Notice.error({
                  title: '登录过期或非法操作!'
                })
                window.location.href = '/MainDesktop'
              } else {
                this.pageList2 = res.body
                this.pageTotal2 = parseInt(response.body)
              }
            }, (response) => {
              this.$Notice.error({
                title: '服务器内部错误!'
              })
            })
          }
        }, (res) => {
          this.$Notice.error({
            title: '服务器内部错误!'
          })
        })
      },
      getLists3 () {
        this.$http.get(
          API.queryIdentity,
          { params: {
            keyword: this.$store.state.keyword3,
            pageCurrent: this.$store.state.pageCurrent3,
            pageSize: this.$store.state.pageSize3
          } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
        ).then((res) => {
          if (res.body.toString() === 'illegal' || res.body.toString() === 'overdue') {
          } else {
            this.$http.get(
              API.totalIdentity,
              { params: {
                keyword: this.$store.state.keyword3
              } },
              { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
            ).then((response) => {
              if (response.body.toString() === 'illegal' || response.body.toString() === 'overdue') {
                this.$Notice.error({
                  title: '登录过期或非法操作!'
                })
                window.location.href = '/MainDesktop'
              } else {
                this.pageList3 = res.body
                this.pageTotal3 = parseInt(response.body)
              }
            }, (response) => {
              this.$Notice.error({
                title: '服务器内部错误!'
              })
            })
          }
        }, (res) => {
          this.$Notice.error({
            title: '服务器内部错误!'
          })
        })
      },
      goQuery1 () {
        this.pageCurrent1 = 1
        this.$store.commit('save', {
          keyword1: this.keyword1,
          pageCurrent1: this.pageCurrent1,
          pageSize1: this.pageSize1
        })
        this.getLists1()
      },
      goQuery2 () {
        this.pageCurrent2 = 1
        this.$store.commit('save', {
          keyword2: this.keyword2,
          pageCurrent2: this.pageCurrent2,
          pageSize2: this.pageSize2
        })
        this.getLists2()
      },
      goQuery3 () {
        this.pageCurrent3 = 1
        this.$store.commit('save', {
          keyword3: this.keyword3,
          pageCurrent3: this.pageCurrent3,
          pageSize3: this.pageSize3
        })
        this.getLists3()
      },
      goQueryReset1 () {
        this.pageCurrent1 = 1
        this.keyword1 = ''
        this.$store.commit('save', {
          keyword1: this.keyword1,
          pageCurrent1: this.pageCurrent1,
          pageSize1: this.pageSize1
        })
        this.getLists1()
      },
      goQueryReset2 () {
        this.pageCurrent2 = 1
        this.keyword2 = ''
        this.$store.commit('save', {
          keyword2: this.keyword2,
          pageCurrent2: this.pageCurrent2,
          pageSize2: this.pageSize2
        })
        this.getLists2()
      },
      goQueryReset3 () {
        this.pageCurrent3 = 1
        this.keyword3 = ''
        this.$store.commit('save', {
          keyword3: this.keyword3,
          pageCurrent3: this.pageCurrent3,
          pageSize3: this.pageSize3
        })
        this.getLists3()
      },
      sizeChange1 (value) {
        this.pageSize1 = value
        this.pageCurrent1 = 1
        this.$store.commit('save', {
          keyword1: this.keyword1,
          pageCurrent1: this.pageCurrent1,
          pageSize1: this.pageSize1
        })
        this.getLists1()
      },
      sizeChange2 (value) {
        this.pageSize2 = value
        this.pageCurrent2 = 1
        this.$store.commit('save', {
          keyword2: this.keyword2,
          pageCurrent2: this.pageCurrent2,
          pageSize2: this.pageSize2
        })
        this.getLists2()
      },
      sizeChange3 (value) {
        this.pageSize3 = value
        this.pageCurrent3 = 1
        this.$store.commit('save', {
          keyword3: this.keyword3,
          pageCurrent3: this.pageCurrent3,
          pageSize3: this.pageSize3
        })
        this.getLists3()
      },
      pageChange1 (value) {
        this.pageCurrent1 = value
        this.$store.commit('save', {
          keyword1: this.keyword1,
          pageCurrent1: this.pageCurrent1,
          pageSize1: this.pageSize1
        })
        this.getLists1()
      },
      pageChange2 (value) {
        this.pageCurrent2 = value
        this.$store.commit('save', {
          keyword2: this.keyword2,
          pageCurrent2: this.pageCurrent2,
          pageSize2: this.pageSize2
        })
        this.getLists2()
      },
      pageChange3 (value) {
        this.pageCurrent3 = value
        this.$store.commit('save', {
          keyword3: this.keyword3,
          pageCurrent3: this.pageCurrent3,
          pageSize3: this.pageSize3
        })
        this.getLists3()
      },
      getData1 (value) {
        this.id1 = value.id
        this.name1 = value.name
        this.$Notice.info({
          title: '已选择学生：' + this.name1
        })
      },
      getData2 (value) {
        this.id2 = value.id
        this.name2 = value.name
        this.$Notice.info({
          title: '已选择家长：' + this.name2
        })
      },
      getData3 (value) {
        this.id3 = value.id
        this.name3 = value.name
        this.$Notice.info({
          title: '已选择身份：' + this.name3
        })
      },
      goReset () {
        this.keyword1 = ''
        this.pageCurrent1 = ''
        this.name1 = '未知'
        this.id1 = ''
        this.keyword2 = ''
        this.pageCurrent2 = ''
        this.name2 = '未知'
        this.id2 = ''
        this.keyword3 = ''
        this.pageCurrent3 = ''
        this.name3 = '未知'
        this.id3 = ''
        this.$store.commit('save', {
          keyword1: this.keyword1,
          keyword2: this.keyword2,
          keyword3: this.keyword3,
          pageCurrent1: this.pageCurrent1,
          pageCurrent2: this.pageCurrent2,
          pageCurrent3: this.pageCurrent3
        })
        this.getLists1()
        this.getLists2()
        this.getLists3()
      },
      goSave () {
        this.$Loading.start()
        this.$Message.info('正在进行保存操作，请稍后...')
        this.$http.get(
          API.save,
          { params: {
            sid: this.id1,
            pid: this.id2,
            iid: this.id3
          } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          if (response.body === 'illegal' || response.body.toString() === 'overdue') {
            this.$Loading.error()
            this.$Notice.error({
              title: '登录过期或非法操作!'
            })
            window.location.href = '../MainDesktop'
          } else if (response.body === 'OK') {
            this.$Loading.finish()
            this.$Notice.success({
              title: '操作完成!',
              desc: '关系已保存！'
            })
            setTimeout(() => { this.$router.push({ path: '/list' }) }, 1000)
          } else {
            this.$Loading.error()
            this.$Notice.error({
              title: response.body
            })
          }
        }, (response) => {
          this.$Loading.error()
          this.$Notice.error({
            title: '服务器内部错误!'
          })
        })
      },
      goBack () {
        this.$router.push({ path: '/list' })
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
  .rights{
    margin: 0px;
    border-radius: 4px;
    float: right;
  }
  .queryLeft{
    float: left;
  }
  .queryRight{
    float: right;
  }
</style>
