<template>
  <div class="layout">
    <Row>
      <Col><MenuList active="course" :name="name" three="列表" :menu="menu"></MenuList></Col>
    </Row>
    <Row v-if="showLoad">
      <Col><Loading></Loading></Col>
    </Row>
    <Row v-show="!showLoad">
      <Col>
        <div>
          <div class="left"><Button type="info" size="large" @click="goAdd" v-if="permission.Save">新增</Button></div>
          <div class="right">
            <div class="left">
              <Input type="text" v-model="keyword" placeholder="请输入关键词" style="width:270px;">
              <span slot="prepend">关键词</span>
              </Input>
            </div>
            <div class="right">
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
        <p>课程：{{names}}删除后，关联信息会一并删除。</p>
        <p>该功能必须慎用！</p>
        <p>是否继续删除？</p>
      </div>
      <div slot="footer">
        <Button type="error" size="large" long @click="goDelete">删除</Button>
      </div>
    </Modal>
    <Modal v-model="inactive" width="360" :styles="{top: '40px'}">
      <p slot="header" style="color:#f60;text-align:center">
        <Icon type="information-circled"></Icon>
        <span>注销确认</span>
      </p>
      <div style="text-align:center">
        <p>课程：{{names}}注销后，关联信息会一并失效。</p>
        <p>是否继续注销？</p>
      </div>
      <div slot="footer">
        <Button type="warning" size="large" long @click="goInactive">注销</Button>
      </div>
    </Modal>
    <Modal v-model="active" width="360" :styles="{top: '40px'}">
      <p slot="header" style="color:#66CDAA;text-align:center">
        <Icon type="information-circled"></Icon>
        <span>激活确认</span>
      </p>
      <div style="text-align:center">
        <p>课程：{{names}}激活后，请重新设置关联信息。</p>
        <p>是否继续激活？</p>
      </div>
      <div slot="footer">
        <Button type="success" size="large" long @click="goActive">激活</Button>
      </div>
    </Modal>
  </div>
</template>
<script>
  import Copy from '../../Common/copy.vue'
  import MenuList from '../Menu/menuList.vue'
  import Options from '../../Common/options.vue'
  import Loading from '../../Common/loading.vue'
  import listBtn from './listBtn.vue'
  import * as API from './API.js'
  import { getCookie } from '../../../cookieUtil.js'
  import { bus } from '../../Common/bus.js'
  export default {
    name: 'list',
    components: { Copy, MenuList, Options, Loading, listBtn },
    data () {
      return {
        name: '',
        permission: [],
        menu: [],
        query: API.query,
        total: API.total,
        keyword: '',
        pageCurrent: 1,
        pageSize: 10,
        pageTotal: 0,
        pageList: [],
        showLoad: true,
        del: false,
        inactive: false,
        active: false,
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
            render: (h, params) => {
              return h('p', {}, params.index + 1)
            }
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
            title: '课程人数',
            key: 'amount',
            sortable: true,
            render: (h, params) => {
              const num = params.row.amount.toString() === '0' ? '不限' : params.row.amount + '人'
              return h('p', {}, num)
            }
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
            render: (h, params) => {
              const color = params.row.state.toString() === '1' ? 'green' : params.row.state.toString() === '0' ? 'yellow' : 'red'
              const text = params.row.state.toString() === '1' ? '可用' : params.row.state.toString() === '0' ? '停用' : '错误'
              return h('Tag', {
                props: {
                  type: 'dot',
                  color: color
                }
              }, text)
            }
          },
          {
            title: '操作',
            key: 'state',
            align: 'center',
            width: 300,
            render (h, params) {
              return h(listBtn, {
                props: {
                  params: params,
                  cookieName: API.base
                }
              })
            }
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
      this.goQuery()
      bus.$on('forEdit', (index) => {
        this.$router.push({ path: '/edit/' + this.pageList[index].id })
      })
      bus.$on('forInactive', (index) => {
        this.showInactive(index)
      })
      bus.$on('forActive', (index) => {
        this.showActive(index)
      })
      bus.$on('forDelete', (index) => {
        this.showDelete(index)
      })
    },
    mounted: function () {
      this.goQueryAccurate()
    },
    methods: {
      getLists (keyword, pageCurrent, pageSize) {
        this.$http.get(
          API.query,
          { params: {
            keyword: keyword,
            pageCurrent: pageCurrent,
            pageSize: pageSize
          } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
        ).then((res) => {
          if (res.body.toString() === 'illegal' || res.body.toString() === 'overdue') {
          } else {
            this.$http.get(
              API.total,
              { params: {
                keyword: keyword
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
                this.pageTotal = response.body
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
      sizeChange (value) {
        this.pageSize = value
        this.pageCurrent = 1
        this.$store.commit('save', {
          pageCurrent: this.pageCurrent,
          pageSize: this.pageSize
        })
        this.getLists(this.keyword, this.pageCurrent, this.pageSize)
      },
      pageChange (value) {
        this.pageCurrent = value
        this.$store.commit('save', {
          pageCurrent: this.pageCurrent
        })
        this.getLists(this.keyword, this.pageCurrent, this.pageSize)
      },
      goQueryReset () {
        this.pageCurrent = 1
        this.keyword = ''
        this.$store.commit('save', {
          pageCurrent: this.pageCurrent,
          keyword: this.keyword
        })
        this.getLists(this.keyword, this.pageCurrent, this.pageSize)
      },
      goQuery () {
        this.$store.commit('save', {
          pageCurrent: this.pageCurrent,
          pageSize: this.pageSize,
          keyword: this.keyword
        })
        this.getLists(this.keyword, this.pageCurrent, this.pageSize)
      },
      goQueryAccurate () {
        this.getLists(this.$store.state.keyword, this.$store.state.pageCurrent, this.$store.state.pageSize)
      },
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
            id: this.pageList[this.index].id
          } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          if (response.body.toString() === 'OK') {
            this.getQueryNoChange(this.keyword)
            this.$Notice.success({
              title: '操作完成!',
              desc: '课程：' + this.pageList[this.index].name + '已删除！'
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
              desc: '课程：' + this.pageList[this.index].name + '已注销！'
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
              desc: '课程：' + this.pageList[this.index].name + '已激活！'
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
