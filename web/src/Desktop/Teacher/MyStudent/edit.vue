<template>
  <div class="layout">
    <Row>
      <Col><MenuList :active="base" :name="name" three="修改" :menu="menu"></MenuList></Col>
    </Row>
    <Row>
      <Col span="8">&nbsp;</Col>
      <Col span="8">
      <Form :label-width="100" ref="editForm" :model="object">
        <Form-item label="姓名" prop="name" required>
          <Input size="large" disabled v-model="object.name" placeholder="请输入学生的姓名" style="width: 400px"></Input>
        </Form-item>
        <Form-item label="身份证号码" prop="number" required>
          <Input size="large" disabled v-model="object.number" placeholder="请输入学生的身份证号码" style="width: 400px"></Input>
        </Form-item>
        <Form-item label="学籍号码" prop="code" required>
          <Input size="large" disabled v-model="object.code" placeholder="请输入学生的学籍号码" style="width: 400px"></Input>
        </Form-item>
        <Form-item label="家庭住址" prop="address" required>
          <Input size="large" disabled v-model="object.address" placeholder="请输入学生的家庭住址" style="width: 400px"></Input>
        </Form-item>
        <Form-item>
          <Button size="large" type="warning" style="margin-left: 8px" @click="goReset"  v-if="permission.Get">重置</Button>
          <Button size="large" type="ghost" style="margin-left: 8px" @click="goBack" v-if="permission.Page">返回</Button>
        </Form-item>
      </Form>
      </Col>
      <Col span="8">&nbsp;</Col>
    </Row>
    <Row>
      <Col>
        <div class="layout-content">
          <Table
            highlight-row
            height="450"
            :columns="columns"
            :data="pageList">
          </Table>
        </div>
      </Col>
    </Row>
    <Row><Col><Copy></Copy></Col></Row>
  </div>
</template>
<script>
  import Copy from '../../Common/copy.vue'
  import MenuList from '../Menu/menuList.vue'
  import * as API from './API.js'
  import { getCookie, checkNumber } from '../../../cookieUtil.js'
  export default {
    name: 'edit',
    components: { Copy, MenuList },
      return {
        name: '',
        base: API.base,
        permission: [],
        menu: [],
        showLoad: true,
        object: {
          name: '',
          number: '',
          code: '',
          address: '',
          state: '1'
        },
        columns: [
          {
            title: '家长身份',
            key: 'iname',
            sortable: true
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
          },
          {
            title: '家长状态',
            key: 'state',
            sortable: true,
            render: (h, params) => {
              const color = params.row.state.toString() === '1' ? 'green' : params.row.state.toString() === '3' ? 'yellow' : params.row.state.toString() === '4' ? 'blue' : 'red'
              const text = params.row.state.toString() === '1' ? '已关注' : params.row.state.toString() === '3' ? '取消关注' : params.row.state.toString() === '4' ? '未关注' : '错误'
              return h('Tag', {
                props: {
                  type: 'dot',
                  color: color
                }
              }, text)
            }
          }
        ]
      }
    },
    created: function () {
      this.fetchData(this.$route.params.id)
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
      }
    },
    watch: {
      // 如果路由有变化，会再次执行该方法
      '$route': 'fetchData'
    },
    methods: {
      goBack () {
        this.$router.push({ path: '/list' })
      },
      fetchData (id) {
        this.$http.get(
          API.get,
          { params: { id: id } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          if (response.body === 'illegal' || response.body.toString() === 'overdue') {
            this.$Notice.error({
              title: '登录过期或非法操作!'
            })
            window.location.href = '../MainDesktop'
          } else {
            this.object = response.body
          }
        }, (response) => {
          this.$Notice.error({
            title: '服务器内部错误!'
          })
        })
      },
      goReset () {
        this.fetchData(this.$route.params.id)
      }
    }
  }
</script>
<style scoped>
  .layout{
    border: 1px solid #d7dde4;
    background: #f5f7f9;
  }
  .layout-content{
    margin:0px 15px 0px 15px;
    overflow: hidden;
    background: #fff;
    border-radius: 4px;
  }
</style>
