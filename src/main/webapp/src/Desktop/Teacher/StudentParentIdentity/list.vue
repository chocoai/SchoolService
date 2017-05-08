<template>
  <div class="layout">
    <Row>
      <Col><MenuList active="studentParentIdentity" :name="name" three="列表" :menu="menu"></MenuList></Col>
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
        <p>即将要删除学生（{{sname}}）与家长（{{pname}}）的身份（{{iname}}）</p>
        <p>关系删除后需要重新绑定。</p>
        <p>该功能必须慎用！</p>
        <p>是否继续删除？</p>
      </div>
      <div slot="footer">
        <Button type="error" size="large" long @click="goDelete">删除</Button>
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
        index: '',
        sname: '',
        dname: '',
        iname: '',
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
            title: '学生姓名',
            key: 'sname',
            sortable: true
          },
          {
            title: '家长姓名',
            key: 'pname',
            sortable: true
          },
          {
            title: '关系',
            key: 'iname',
            sortable: true
          },
          {
            title: '操作',
            key: 'state',
            align: 'center',
            width: 100,
            render (row, column, index) {
              return `
              <i-button type="error" @click="showDelete(${index})" v-if="permission.Delete">删除</i-button>
              `
            }
          }
        ]
      }
    },
    created: function () {
      if (getCookie('menu') === null || getCookie('menu') === undefined || getCookie('menu') === '' || getCookie('StudentParentIdentityDesktop') === null || getCookie('StudentParentIdentityDesktop') === undefined || getCookie('StudentParentIdentityDesktop') === '') {
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
              this.permission = JSON.parse(JSON.parse(getCookie('StudentParentIdentityDesktop')))
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
        this.permission = JSON.parse(JSON.parse(getCookie('StudentParentIdentityDesktop')))
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
        this.sname = this.pageList[index].sname
        this.dname = this.pageList[index].dname
        this.iname = this.pageList[index].iname
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
      goDelete () {
        this.$Loading.start()
        this.$Message.info('正在进行删除操作，请稍后...')
        this.del = false
        this.$http.get(
          API.del,
          { params: {
            iid: this.pageList[this.index].iid,
            pid: this.pageList[this.index].pid,
            sid: this.pageList[this.index].sid
          } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          if (response.body.toString() === 'OK') {
            this.getQueryNoChange(this.keyword)
            this.$Notice.success({
              title: '操作完成!',
              desc: '关系已删除！'
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
