<template>
  <div class="layout">
    <Row>
      <Col><MenuList :active="base" :name="name" three="列表" :menu="menu"></MenuList></Col>
    </Row>
    <Row v-if="showLoad">
      <Col><Loading></Loading></Col>
    </Row>
    <Row v-show="!showLoad">
      <Col>
      <div>
        <div class="left"><Button type="info" size="large" @click="goAdd" v-if="permission.Save">新增</Button></div>
        <div class="right">
          <div class="queryLeft">
            <Input type="text" v-model="keyword" placeholder="请输入关键词" style="width:270px;">
            <span slot="prepend">关键词</span>
            </Input>
          </div>
          <div class="queryRight">
            <Button-group>
              <Button type="ghost" @click="goQuery">
                <Icon type="search"></Icon>
                搜索
              </Button>
              <Button type="ghost" @click="goQueryReset">
                <Icon type="refresh"></Icon>
                重置
              </Button>
              <Button type="ghost" @click="goDownload" v-if="permission.Download">
                <Icon type="android-download"></Icon>
                下载
              </Button>
            </Button-group>
          </div>
        </div>
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
            :total="pageTotal"
            :current="pageCurrent"
            :page-size="pageSize"
            @on-page-size-change="sizeChange"
            @on-change="pageChange"
            placement="top"
            show-sizer
            show-elevator
            show-total>
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
        <p>即将要删除教师（{{tname}}）在班级（{{rname}}）的课程（{{cname}}）</p>
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
  import Options from '../../Common/options.vue'
  import Loading from '../../Common/loading.vue'
  import listBtn from '../../Common/listBtn_Delete.vue'
  import * as API from './API.js'
  import { getCookie } from '../../../cookieUtil.js'
  import { bus } from '../../Common/bus.js'
  export default {
    name: 'list',
    components: { Copy, MenuList, Options, Loading, listBtn },
    data () {
      return {
        name: '',
        base: API.base,
        permission: [],
        menu: [],
        keyword: '',
        pageCurrent: '',
        pageList: [],
        pageTotal: '',
        showLoad: true,
        del: false,
        index: '',
        cname: '',
        tname: '',
        rname: '',
        sname: '',
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
            render: (h, params) => {
              return h('p', {}, params.index + 1)
            }
          },
          {
            title: '班级名称',
            key: 'rname',
            sortable: true
          },
          {
            title: '课程名称',
            key: 'cname',
            sortable: true
          },
          {
            title: '教师姓名',
            key: 'tname',
            sortable: true
          },
          {
            title: '学期名称',
            key: 'sname',
            sortable: true
          },
          {
            title: '操作',
            key: 'state',
            align: 'center',
            width: 200,
            render (h, params) {
              return h(listBtn, {
                props: {
                  params: params,
                  cookieName: API.base,
                  url: API.permission
                }
              })
            }
          }
        ]
      }
    },
    created: function () {
      this.keyword = this.$store.state.keyword
      this.pageCurrent = parseInt(this.$store.state.pageCurrent)
      this.pageSize = parseInt(this.$store.state.pageSize)
      this.getLists()
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
      bus.$on('forDelete', (index) => {
        this.showDelete(index)
      })
    },
    methods: {
      showDelete (index) {
        this.del = true
        this.index = index
        this.cname = this.pageList[index].cname
        this.rname = this.pageList[index].rname
        this.tname = this.pageList[index].tname
        this.sname = this.pageList[index].sname
      },
      getLists () {
        this.$http.get(
          API.query,
          { params: {
            keyword: this.$store.state.keyword,
            pageCurrent: this.$store.state.pageCurrent,
            pageSize: this.$store.state.pageSize
          } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
        ).then((res) => {
          if (res.body.toString() === 'illegal' || res.body.toString() === 'overdue') {
          } else {
            this.$http.get(
              API.total,
              { params: {
                keyword: this.$store.state.keyword
              } },
              { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
            ).then((response) => {
              if (response.body.toString() === 'illegal' || response.body.toString() === 'overdue') {
                this.$Notice.error({
                  title: '登录过期或非法操作!'
                })
                window.location.href = '/MainDesktop'
              } else {
                this.pageList = res.body
                this.pageTotal = parseInt(response.body)
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
      goQuery () {
        this.pageCurrent = 1
        this.$store.commit('save', {
          keyword: this.keyword,
          pageCurrent: this.pageCurrent,
          pageSize: this.pageSize
        })
        this.getLists()
      },
      goQueryReset () {
        this.pageCurrent = 1
        this.keyword = ''
        this.$store.commit('save', {
          keyword: this.keyword,
          pageCurrent: this.pageCurrent,
          pageSize: this.pageSize
        })
        this.getLists()
      },
      sizeChange (value) {
        this.pageSize = value
        this.pageCurrent = 1
        this.$store.commit('save', {
          keyword: this.keyword,
          pageCurrent: this.pageCurrent,
          pageSize: this.pageSize
        })
        this.getLists()
      },
      pageChange (value) {
        this.pageCurrent = value
        this.$store.commit('save', {
          keyword: this.keyword,
          pageCurrent: this.pageCurrent,
          pageSize: this.pageSize
        })
        this.getLists()
      },
      goDownload () {
        this.$Loading.start()
        this.$Message.info('正在进行导出操作，请稍后...')
        window.location.href = API.download + '?keyword=' + this.keyword
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
            cid: this.pageList[this.index].cid,
            rid: this.pageList[this.index].rid,
            tid: this.pageList[this.index].tid,
            sid: this.pageList[this.index].sid
          } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          if (response.body.toString() === 'OK') {
            this.getLists()
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
  .queryLeft{
    float: left;
  }
  .queryRight{
    float: right;
  }
</style>
