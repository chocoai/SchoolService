<template>
  <div class="layout">
    <Row>
      <Col><MenuList active="course" :name="name" three="列表"></MenuList></Col>
    </Row>
    <Row v-if="showLoad">
      <Col><Loading></Loading></Col>
    </Row>
    <Row v-show="!showLoad">
      <Col>
        <div>
          <div class="left"><Button type="info" size="large" @click="goAdd" v-if="power[1] === '1'">新增</Button></div>
          <div class="right"><Search @goQuery="getQuery" @goDownload="getDownload"></Search></div>
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
  import Search from '../../Common/search.vue'
  import Page from '../../Common/page.vue'
  import Options from '../../Common/options.vue'
  import Loading from '../../Common/loading.vue'
  import * as API from './API.js'
  import { setCookie, getCookie } from '../../../cookieUtil.js'
  export default {
    name: 'list',
    components: { Copy, MenuList, Search, Page, Options, Loading },
    data () {
      return {
        name: 'xxx',
        power: [],
        query: API.query,
        total: API.total,
        keyword: '',
        pageList: [],
        inactive: false,
        active: false,
        index: '',
        names: '',
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
            key: 'state',
            align: 'center',
            render (row, column, index) {
              const states1 = row.state.toString() === '1'
              const states2 = row.state.toString() === '0'
              return `
              <i-button type="primary" @click="goEdit(${index})" v-if="power[1] === '1'">修改</i-button>
              <i-button type="warning" v-if="${states1} && power[1] === '1'" @click="showInactive(${index})">注销</i-button>
              <i-button type="success" v-if="${states2} && power[1] === '1'" @click="showActive(${index})">激活</i-button>
              `
            }
          }
        ]
      }
    },
    created: function () {
      if (getCookie('power') === null || getCookie('power') === undefined || !/^\d{6}$/i.test(getCookie('power'))) {
        window.power = '000000'
        this.power = window.power.split(';')[1].split('')
      } else {
        window.power = getCookie('power')
        this.power = window.power.split(';')[1].split('')
        setCookie('power', '', -1)
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
          this.height = 440
          this.size = 'small'
        }
      },
      getList (pageList) {
        this.pageList = pageList
      },
      goAdd () {
        this.$router.push({ path: '/add' })
      },
      goEdit (index) {
        this.$router.push({ path: '/edit/' + this.pageList[index].id })
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
          } else if (response.body.toString() === 'error') {
            this.$Notice.error({
              title: '权限异常，请重新登录!'
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
          } else if (response.body.toString() === 'error') {
            this.$Notice.error({
              title: '权限异常，请重新登录!'
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
