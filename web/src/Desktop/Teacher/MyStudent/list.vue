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
  </div>
</template>
<script>
  import Copy from '../../Common/copy.vue'
  import MenuList from '../Menu/menuList.vue'
  import Options from '../../Common/options.vue'
  import Loading from '../../Common/loading.vue'
  import listBtn from '../../Common/listBtn_Detail.vue'
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
        pageSize: '',
        pageTotal: 0,
        pageList: [],
        showLoad: true,
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
            title: '学生姓名',
            key: 'name',
            sortable: true
          },
          {
            title: '身份证号码',
            key: 'number',
            sortable: true,
            width: 200
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
          },
          {
            title: '所属班级',
            key: 'rname',
            sortable: true
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
      bus.$on('forEdit', (index) => {
        this.$router.push({ path: '/edit/' + this.pageList[index].id })
      })
    },
    methods: {
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
      goDownload () {
        this.$Loading.start()
        this.$Message.info('正在进行导出操作，请稍后...')
        window.location.href = API.download + '?keyword=' + this.keyword
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
