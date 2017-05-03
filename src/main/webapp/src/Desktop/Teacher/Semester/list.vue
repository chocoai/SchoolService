<template>
  <div class="layout">
    <Row>
      <Col><MenuList active="semester" :name="name" three="列表" :menu="menu"></MenuList></Col>
    </Row>
    <Row v-if="showLoad">
      <Col><Loading></Loading></Col>
    </Row>
    <Row v-show="!showLoad">
      <Col>
        <div>
          <div class="left"><Button type="info" size="large" @click="goAdd" v-if="permission.Save">新增</Button></div>
          <div class="right"><Search @goQuery="getQuery" @goDownload="getDownload" :download="download"></Search></div>
        </div>
      </Col>
    </Row>
    <Row v-show="!showLoad">
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
    <Row v-show="!showLoad">
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
            @savePageCurrent="saveCurrent"
            @savePageCurrentAndKeyword="CurrentAndKeyword"
            :queryURL="query"
            :totalURL="total"
            :keyword="keyword"
          >
          </Page>
        </div>
      </div>
      </Col>
    </Row>
    <Row><Col><Copy></Copy></Col></Row>
    <Modal v-model="del" width="360" :styles="{top: '40px'}">
      <p slot="header" style="color:#FF0000;text-align:center">
        <Icon type="information-circled"></Icon>
        <span>删除确认</span>
      </p>
      <div style="text-align:center">
        <p>学期：{{names}}删除后，关联信息会一并删除。</p>
        <p>该功能必须慎用！</p>
        <p>是否继续删除？</p>
      </div>
      <div slot="footer">
        <Button type="error" size="large" long @click="goDelete">删除</Button>
      </div>
    </Modal>
    <Modal v-model="active" width="360" :styles="{top: '40px'}">
      <p slot="header" style="color:#66CDAA;text-align:center">
        <Icon type="information-circled"></Icon>
        <span>激活确认</span>
      </p>
      <div style="text-align:center">
        <p>学期：{{names}}激活后，原激活学期将注销。</p>
        <p>是否继续激活？</p>
      </div>
      <div slot="footer">
        <Button type="success" size="large" long @click="goActive">激活</Button>
      </div>
    </Modal>
    <Modal v-model="inactive" width="360" :styles="{top: '40px'}">
      <p slot="header" style="color:#66CDAA;text-align:center">
        <Icon type="information-circled"></Icon>
        <span>注销确认</span>
      </p>
      <div style="text-align:center">
        <p>是否继续注销？</p>
      </div>
      <div slot="footer">
        <Button type="success" size="large" long @click="goInactive">注销</Button>
      </div>
    </Modal>
  </div>
</template>
<script>
  import Copy from '../../Common/copy.vue'
  import MenuList from '../Menu/menuList.vue'
  import Search from '../../Common/search.vue'
  import Page from '../../Common/page.vue'
  import Options from '../../Common/options.vue'
  import Loading from '../../Common/loading.vue'
  import * as API from './API.js'
  import { getCookie } from '../../../cookieUtil.js'
  export default {
    name: 'list',
    components: { Copy, MenuList, Search, Page, Options, Loading },
    data () {
      return {
        name: '',
        permission: [],
        menu: [],
        download: false,
        query: API.query,
        total: API.total,
        keyword: '',
        pageList: [],
        showLoad: true,
        del: false,
        active: false,
        inactive: false,
        index: '',
        names: '',
        border: false,
        stripe: false,
        size: 'small',
        height: 450,
        self: this,
        columns: [
          {
            title: '序号',
            key: 'id',
            sortable: true,
            render (row, column, index) {
              return `${index + 1}`
            }
          },
          {
            title: '学期名称',
            key: 'name',
            sortable: true
          },
          {
            title: '开始时间',
            key: 'time_start',
            sortable: true
          },
          {
            title: '终止时间',
            key: 'time_end',
            sortable: true
          },
          {
            title: '课程状态',
            key: 'state',
            sortable: true,
            render (row) {
              const color = row.state.toString() === '1' ? 'green' : row.state.toString() === '0' ? 'yellow' : 'red'
              const text = row.state.toString() === '1' ? '当前学期' : row.state.toString() === '0' ? '非当前学期' : '错误'
              return `<tag type="dot" color="${color}">${text}</tag>`
            }
          },
          {
            title: '操作',
            key: 'state',
            align: 'center',
            width: 300,
            render (row, column, index) {
              const states1 = row.state.toString() === '1'
              const states2 = row.state.toString() === '0'
              return `
              <i-button type="primary" @click="goEdit(${index})" v-if="permission.Edit">修改</i-button>
              <i-button type="warning" v-if="${states1} && permission.Inactive" @click="showInactive(${index})">注销</i-button>
              <i-button type="success" v-if="${states2} && permission.Active" @click="showActive(${index})">激活</i-button>
              <i-button type="error" @click="showDelete(${index})" v-if="permission.Delete">删除</i-button>
              `
            }
          }
        ]
      }
    },
    created: function () {
      if (getCookie('menu') === null || getCookie('menu') === undefined || getCookie('menu') === '' || getCookie('SemesterDesktop') === null || getCookie('SemesterDesktop') === undefined || getCookie('SemesterDesktop') === '') {
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
              this.permission = JSON.parse(JSON.parse(getCookie('SemesterDesktop')))
              this.download = this.permission.Download
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
        this.permission = JSON.parse(JSON.parse(getCookie('SemesterDesktop')))
        this.download = this.permission.Download
        this.menu = JSON.parse(JSON.parse(getCookie('menu')))
        this.name = decodeURI(getCookie('name')).substring(1, decodeURI(getCookie('name')).length - 1)
        this.showLoad = false
      }
    },
    computed: {
      showLoad: function () {
        if (this.pageList.length.toString() === '0') {
          return true
        } else {
          return false
        }
      }
    },
    methods: {
      showDelete (index) {
        this.del = true
        this.index = index
        this.names = this.pageList[index].name
      },
      showInactive (index) {
        this.inactive = true
        this.index = index
        this.names = this.pageList[index].name
      },
      showActive (index) {
        this.active = true
        this.index = index
        this.names = this.pageList[index].name
      },
      getQuery (keyword) {
        this.keyword = keyword
        this.$refs.pages.query(keyword)
      },
      getQueryNoChange (keyword) {
        this.keyword = keyword
        this.$refs.pages.queryNoChange(keyword)
      },
      getDownload (keyword) {
        this.keyword = keyword
        this.$Loading.start()
        this.$Message.info('正在进行导出操作，请稍后...')
        this.inactive = false
        window.location.href = API.download + '?keyword=' + keyword
      },
      getBorder (border) {
        this.border = border
      },
      getStripe (stripe) {
        this.stripe = stripe
      },
      getSize (tableSize) {
        if (tableSize.toString() === 'true') {
          this.height = 665
          this.size = 'large'
        } else {
          this.height = 450
          this.size = 'small'
        }
      },
      getList (pageList) {
        this.pageList = pageList
      },
      saveCurrent (pageCurrent) {
        this.$store.commit('save', {
          pageCurrent: pageCurrent
        })
      },
      CurrentAndKeyword (keyword, pageCurrent) {
        this.$store.commit('save', {
          keyword: keyword,
          pageCurrent: pageCurrent
        })
      },
      goAdd () {
        this.$router.push({ path: '/add' })
      },
      goEdit (index) {
        this.$router.push({ path: '/edit/' + this.pageList[index].id })
      },
      goDelete () {
        this.$Loading.start()
        this.$Message.info('正在进行删除操作，请稍后...')
        this.del = false
        this.$http.get(
          API.del,
          { params: {
            id: this.pageList[this.index].id
          } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          if (response.body.toString() === 'OK') {
            this.getQueryNoChange(this.keyword)
            this.$Notice.success({
              title: '操作完成!',
              desc: '学期：' + this.pageList[this.index].name + '已删除！'
            })
            this.$Loading.finish()
          } else if (response.body.toString() === 'illegal' || response.body.toString() === 'overdue') {
            this.$Notice.error({
              title: '登录过期或非法操作!'
            })
            this.$Loading.error()
          } else {
            this.$Notice.error({
              title: response.body
            })
            this.$Loading.error()
          }
        }, (response) => {
          this.$Notice.error({
            title: '服务器内部错误!'
          })
          this.$Loading.error()
        })
      },
      goInactive () {
        this.$Loading.start()
        this.$Message.info('正在进行注销操作，请稍后...')
        this.inactive = false
        this.$http.get(
          API.inactive,
          { params: {
            id: this.pageList[this.index].id
          } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          if (response.body.toString() === 'OK') {
            this.getQueryNoChange(this.keyword)
            this.$Notice.success({
              title: '操作完成!',
              desc: '学期：' + this.pageList[this.index].name + '已注销！'
            })
            this.$Loading.finish()
          } else if (response.body.toString() === 'illegal' || response.body.toString() === 'overdue') {
            this.$Notice.error({
              title: '登录过期或非法操作!'
            })
            this.$Loading.error()
          } else {
            this.$Notice.error({
              title: response.body
            })
            this.$Loading.error()
          }
        }, (response) => {
          this.$Notice.error({
            title: '服务器内部错误!'
          })
          this.$Loading.error()
        })
      },
      goActive () {
        this.$Loading.start()
        this.$Message.info('正在进行激活操作，请稍后...')
        this.active = false
        this.$http.get(
          API.active,
          { params: {
            id: this.pageList[this.index].id
          } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          if (response.body.toString() === 'OK') {
            this.getQueryNoChange(this.keyword)
            this.$Notice.success({
              title: '操作完成!',
              desc: '学期：' + this.pageList[this.index].name + '已激活！'
            })
            this.$Loading.finish()
          } else if (response.body.toString() === 'illegal' || response.body.toString() === 'overdue') {
            this.$Notice.error({
              title: '登录过期或非法操作!'
            })
            this.$Loading.error()
          } else {
            this.$Notice.error({
              title: response.body
            })
            this.$Loading.error()
          }
        }, (response) => {
          this.$Notice.error({
            title: '服务器内部错误!'
          })
          this.$Loading.error()
        })
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
