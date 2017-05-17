<template>
  <div class="layout">
    <Row>
      <Col><MenuList :active="base" :name="name" three="新增" :menu="menu"></MenuList></Col>
    </Row>
    <Row v-if="showLoad">
      <Col><Loading></Loading></Col>
    </Row>
    <Row v-show="!showLoad">
      <Col span="12"><div class="right"><Search @goQuery="getQuery1" :download="download"></Search></div></Col>
      <Col span="12"><div class="right"><Search @goQuery="getQuery2" :download="download"></Search></div></Col>
    </Row>
    <Row v-show="!showLoad">
      <Col span="12">
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
      <Col span="12">
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
    </Row>
    <Row v-show="!showLoad">
      <Col span="12">
        <div class="right">
          <Page
            ref="pages1"
            @goList="getList1"
            @savePageCurrent="saveCurrent1"
            @savePageCurrentAndKeyword="CurrentAndKeyword1"
            :queryURL="query1"
            :totalURL="total1"
            :keyword="keyword1"
          >
          </Page>
        </div>
      </Col>
      <Col span="12">
        <div class="right">
          <Page
            ref="pages2"
            @goList="getList2"
            @savePageCurrent="saveCurrent2"
            @savePageCurrentAndKeyword="CurrentAndKeyword2"
            :queryURL="query2"
            :totalURL="total2"
            :keyword="keyword2"
          >
          </Page>
        </div>
      </Col>
    </Row>
    <Row v-show="!showLoad">
      <Col span="16">
        <div class="rights">
          <Button size="large" type="info">请双击行选择相关信息</Button>
          <Tag type="dot" color="blue">班级：{{name1}}</Tag>
          <Tag type="dot" color="green">学生：{{name2}}</Tag>
        </div>
      </Col>
      <Col span="8">
        <Form :label-width="100" ref="addForm" :model="object">
          <Form-item>
            <Button size="large" type="success" @click="goSave">保存</Button>
            <Button size="large" type="warning" style="margin-left: 8px" @click="goReset">重置</Button>
            <Button size="large" type="ghost" style="margin-left: 8px" @click="goBack">返回</Button>
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
  import Search from '../../Common/search.vue'
  import Page from '../../Common/page.vue'
  import Loading from '../../Common/loading.vue'
  import * as API from './API.js'
  import { getCookie } from '../../../cookieUtil.js'
  export default {
    name: 'add',
    components: { Copy, MenuList, Search, Page, Loading },
    data () {
      return {
        name: '',
        base: API.base,
        permission: [],
        menu: [],
        query1: API.queryRoom,
        total1: API.totalRoom,
        query2: API.queryStudent,
        total2: API.totalStudent,
        keyword1: '',
        keyword2: '',
        pageList1: [],
        pageList2: [],
        pageTotal1: '',
        pageTotal2: '',
        name1: '未知',
        name2: '未知',
        id1: '',
        id2: '',
        showLoad: true,
        download: false,
        columns1: [
          {
            title: '班级名称',
            key: 'name',
            align: 'center'
          }
        ],
        columns2: [
          {
            title: '学生姓名',
            key: 'name',
            align: 'center'
          },
          {
            title: '身份证号码',
            key: 'number',
            align: 'center'
          }
        ]
      }
    },
    created: function () {
      if (getCookie('menu') === null || getCookie('menu') === undefined || getCookie('menu') === '' || getCookie(API.base) === null || getCookie(API.base) === undefined || getCookie(API.base) === '') {
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
              this.menu = JSON.parse(JSON.parse(getCookie('menu')))
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
        this.menu = JSON.parse(JSON.parse(getCookie('menu')))
        this.name = decodeURI(getCookie('name')).substring(1, decodeURI(getCookie('name')).length - 1)
        this.showLoad = false
      }
    },
    methods: {
      getQuery1 (keyword) {
        this.keyword1 = keyword
        this.$refs.pages1.query(keyword)
      },
      getQueryNoChange1 (keyword) {
        this.keyword1 = keyword
        this.$refs.pages1.queryNoChange(keyword)
      },
      getData1 (value) {
        this.id1 = value.id
        this.name1 = value.name
        this.$Notice.info({
          title: '已选择班级：' + this.name1
        })
      },
      getList1 (pageList, pageTotal) {
        this.pageList1 = pageList
        this.pageTotal1 = pageTotal
      },
      saveCurrent1 (pageCurrent) {
        this.$store.commit('save', {
          pageCurrent1: pageCurrent
        })
      },
      CurrentAndKeyword1 (keyword, pageCurrent) {
        this.$store.commit('save', {
          keyword1: keyword,
          pageCurrent1: pageCurrent
        })
      },
      getQuery2 (keyword) {
        this.keyword2 = keyword
        this.$refs.pages2.query(keyword)
      },
      getQueryNoChange2 (keyword) {
        this.keyword2 = keyword
        this.$refs.pages2.queryNoChange(keyword)
      },
      getData2 (value) {
        this.id2 = value.id
        this.name2 = value.name
        this.$Notice.info({
          title: '已选择学生：' + this.name2
        })
      },
      getList2 (pageList, pageTotal) {
        this.pageList2 = pageList
        this.pageTotal2 = pageTotal
      },
      saveCurrent2 (pageCurrent) {
        this.$store.commit('save', {
          pageCurrent2: pageCurrent
        })
      },
      CurrentAndKeyword2 (keyword, pageCurrent) {
        this.$store.commit('save', {
          keyword2: keyword,
          pageCurrent2: pageCurrent
        })
      },
      goReset () {
        this.keyword1 = ''
        this.$refs.pages1.query(this.keyword1)
        this.name1 = '未知'
        this.id1 = ''
        this.keyword2 = ''
        this.$refs.pages2.query(this.keyword2)
        this.name2 = '未知'
        this.id2 = ''
      },
      goSave () {
        this.$Loading.start()
        this.$Message.info('正在进行保存操作，请稍后...')
        this.$http.get(
          API.save,
          { params: {
            rid: this.id1,
            sid: this.id2
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
</style>
