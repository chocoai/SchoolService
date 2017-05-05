<template>
  <div class="layout">
    <Row>
      <Col><MenuList active="studentParent" :name="name" three="新增" :menu="menu"></MenuList></Col>
    </Row>
    <div class="layout-content">
    <Row>
      <Col span="12">
        <div class="right"><Search @goQuery="getQuery" :download="download"></Search></div>
        <br>
        <div>
          <Table
            highlight-row
            height="450"
            size="small"
            :columns="columns"
            :data="pageList">
          </Table>
        </div>
        <br>
        <div>
          <Page
            ref="studentPages"
            @goList="getList"
            @savePageCurrent="saveCurrent"
            @savePageCurrentAndKeyword="CurrentAndKeyword"
            :queryURL="studentQuery"
            :totalURL="studentTotal"
            :keyword="studentKeyword"
          >
          </Page>
        </div>
      </Col>
      <Col span="12">

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
        query: API.query,
        total: API.total,
        keyword: '',
        pageList: [],
        showLoad: true,
        download: false,
        del: false,
        index: '',
        names: '',
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
            key: 'name',
            sortable: true
          },
          {
            title: '身份证号码',
            key: 'number',
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
    methods: {
      goReset () {
        this.$refs.addForm.resetFields()
        this.object.name = ''
        this.object.number = ''
        this.object.code = ''
        this.object.address = ''
        this.object.state = '1'
      },
      goSave () {
        this.$Loading.start()
        this.$Message.info('正在进行保存操作，请稍后...')
        this.$http.get(
          API.save,
          { params: {
            name: this.object.name,
            number: this.object.number,
            code: this.object.code,
            address: this.object.address,
            state: this.object.state
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
              desc: '学生：' + this.object.name + '已保存！'
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
