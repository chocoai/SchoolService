<template>
  <div class="layout">
    <Row>
      <Col><MenuList active="studentParent" :name="name" three="新增" :menu="menu"></MenuList></Col>
    </Row>
    <div class="layout-content">
    <Row>
      <Col span="8">
        <div class="right"><Search @goQuery="getQuery1" :download="download"></Search></div>
        <br>
        <div>
          <Table
            highlight-row
            height="450"
            size="small"
            :columns="columns1"
            :data="pageList1">
          </Table>
        </div>
        <br>
        <div>
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
      <Col span="8">
        <div class="right"><Search @goQuery="getQuery2" :download="download"></Search></div>
        <br>
        <div>
          <Table
            highlight-row
            height="450"
            size="small"
            :columns="columns2"
            :data="pageList2">
          </Table>
        </div>
        <br>
        <div>
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
      <Col span="8">
        <div class="right"><Search @goQuery="getQuery3" :download="download"></Search></div>
        <br>
        <div>
          <Table
            highlight-row
            height="450"
            size="small"
            :columns="columns3"
            :data="pageList3">
          </Table>
        </div>
        <br>
        <div>
          <Page
            ref="pages3"
            @goList="getList3"
            @savePageCurrent="saveCurrent3"
            @savePageCurrentAndKeyword="CurrentAndKeyword3"
            :queryURL="query3"
            :totalURL="total3"
            :keyword="keyword3"
          >
          </Page>
        </div>
      </Col>
    </Row>
    <Row>
      <Col span="8">&nbsp;</Col>
      <Col span="8">
      <Form :label-width="100" ref="addForm" :model="object">
        <Form-item>
          <Button size="large" type="primary" @click="goSave">保存</Button>
          <Button size="large" type="ghost" style="margin-left: 8px" @click="goReset">重置</Button>
          <Button size="large" style="margin-left: 8px" @click="goBack">返回</Button>
        </Form-item>
      </Form>
      </Col>
      <Col span="8">&nbsp;</Col>
    </Row>
    </div>
    <Row><Col><Copy></Copy></Col></Row>
  </div>
</template>
<script>
  import Copy from '../../Common/copy.vue'
  import MenuList from '../Menu/menuList.vue'
  import * as API from './API.js'
  import { getCookie, checkNumber } from '../../../cookieUtil.js'
  export default {
    name: 'add',
    components: { Copy, MenuList },
    data () {
      return {
        name: '',
        permission: [],
        menu: [],
        query1: API.queryStudent,
        total1: API.totalStudent,
        query2: API.queryParent,
        total2: API.totalParent,
        query3: API.queryIdentity,
        total3: API.totalIdentity,
        keyword1: '',
        pageList1: [],
        keyword2: '',
        pageList2: [],
        keyword3: '',
        pageList3: [],
        showLoad: true,
        download: false,
        index: '',
        names: '',
        columns1: [
          {
            title: '序号',
            key: 'id',
            sortable: true,
            render (row, column, index) {
              return `${index + 1}`
            }
          },
          {
            title: '学生姓名',
            key: 'name',
            sortable: true
          },
          {
            title: '身份证号码',
            key: 'number',
            sortable: true
          }
        ],
        columns2: [
          {
            title: '序号',
            key: 'id',
            sortable: true,
            render (row, column, index) {
              return `${index + 1}`
            }
          },
          {
            title: '家长姓名',
            key: 'name',
            sortable: true
          },
          {
            title: '联系电话',
            key: 'mobile',
            sortable: true
          }
        ],
        columns3: [
          {
            title: '序号',
            key: 'id',
            sortable: true,
            render (row, column, index) {
              return `${index + 1}`
            }
          },
          {
            title: '身份',
            key: 'name',
            sortable: true
          }
        ]
      }
    },
    created: function () {
      if (getCookie('menu') === null || getCookie('menu') === undefined || getCookie('menu') === '' || getCookie('StudentParentDesktop') === null || getCookie('StudentParentDesktop') === undefined || getCookie('StudentParentDesktop') === '') {
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
              this.permission = JSON.parse(JSON.parse(getCookie('StudentParentDesktop')))
              this.menu = JSON.parse(JSON.parse(getCookie('menu')))
              this.name = decodeURI(getCookie('name')).substring(1, decodeURI(getCookie('name')).length - 1)
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
        this.permission = JSON.parse(JSON.parse(getCookie('StudentParentDesktop')))
        this.menu = JSON.parse(JSON.parse(getCookie('menu')))
        this.name = decodeURI(getCookie('name')).substring(1, decodeURI(getCookie('name')).length - 1)
      }
    },
    computed: {
      showLoad: function () {
        if (this.pageList1.length.toString() === '0' || this.pageList2.length.toString() === '0' || this.pageList3.length.toString() === '0') {
          return true
        } else {
          return false
        }
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
      getQuery2 (keyword) {
        this.keyword2 = keyword
        this.$refs.pages2.query(keyword)
      },
      getQueryNoChange2 (keyword) {
        this.keyword2 = keyword
        this.$refs.pages2.queryNoChange(keyword)
      },
      getQuery3 (keyword) {
        this.keyword3 = keyword
        this.$refs.pages3.query(keyword)
      },
      getQueryNoChange3 (keyword) {
        this.keyword3 = keyword
        this.$refs.pages3.queryNoChange(keyword)
      },
      getList1 (pageList) {
        this.pageList1 = pageList
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
      getList2 (pageList) {
        this.pageList2 = pageList
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
      getList3 (pageList) {
        this.pageList3 = pageList
      },
      saveCurrent3 (pageCurrent) {
        this.$store.commit('save', {
          pageCurrent3: pageCurrent
        })
      },
      CurrentAndKeyword3 (keyword, pageCurrent) {
        this.$store.commit('save', {
          keyword3: keyword,
          pageCurrent3: pageCurrent
        })
      },
      goReset () {
        this.keyword1 = ''
        this.$refs.pages1.query(this.keyword1)
        this.keyword2 = ''
        this.$refs.pages1.query(this.keyword2)
        this.keyword3 = ''
        this.$refs.pages1.query(this.keyword3)
      },
      goSave () {
        this.$Loading.start()
        this.$Message.info('正在进行保存操作，请稍后...')
        this.$http.get(
          API.save,
          { params: {
            student_id: this.student_id,
            parent_id: this.parent_id,
            identity_id: this.identity_id
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
</style>
