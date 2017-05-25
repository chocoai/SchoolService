<template>
  <div class="layout">
    <Row>
      <Col><MenuList :active="base" :name="name" three="详情" :menu="menu"></MenuList></Col>
    </Row>
    <Row>
      <Col span="8">&nbsp;</Col>
      <Col span="8">
      <Form :label-width="100" ref="editForm" :model="object">
        <Form-item label="请假学生姓名" required>
          <Input disabled size="large" v-model="object.sname" style="width: 400px"></Input>
        </Form-item>
        <Form-item label="申请家长姓名" required>
          <Input disabled size="large" v-model="object.pname" style="width: 400px"></Input>
        </Form-item>
        <Form-item label="批准教师姓名" required>
          <Input disabled size="large" v-model="object.tname" style="width: 400px"></Input>
        </Form-item>
        <Form-item size="large" label="请假类型" required>
          <Radio-group v-model="object.type" type="button">
            <Radio disabled label="1">病假</Radio>
            <Radio disabled label="2">事假</Radio>
            <Radio disabled label="3">其他</Radio>
          </Radio-group>
        </Form-item>
        <Form-item size="large" label="审批状态" required>
          <Radio-group v-model="object.state" type="button">
            <Radio disabled label="0">未回应</Radio>
            <Radio disabled label="1">已批准</Radio>
            <Radio disabled label="2">已拒绝</Radio>
          </Radio-group>
        </Form-item>
        <Form-item label="请假说明">
          <Input disabled v-model="object.reason" type="textarea" :autosize="{minRows: 2,maxRows: 5}"></Input>
        </Form-item>
        <Form-item label="回复信息">
          <Input disabled v-model="object.reply" type="textarea" :autosize="{minRows: 2,maxRows: 5}"></Input>
        </Form-item>
        <Form-item label="请假开始日期" required>
          <Date-picker disabled type="date" v-model="object.time_start" placement="bottom-end" style="width: 400px"></Date-picker>
        </Form-item>
        <Form-item label="请假终止日期" required>
          <Date-picker disabled type="date" v-model="object.time_end" placement="bottom-end" style="width: 400px"></Date-picker>
        </Form-item>
        <Form-item label="家长申请时间" required>
          <Date-picker disabled type="date" v-model="object.time_apply" placement="bottom-end" style="width: 400px"></Date-picker>
        </Form-item>
        <Form-item label="教师批准时间" required>
          <Date-picker disabled type="date" v-model="object.time_ratify" placement="bottom-end" style="width: 400px"></Date-picker>
        </Form-item>
        <Form-item>
          <Button size="large" type="warning" style="margin-left: 8px" @click="goReset"  v-if="permission.Get">重置</Button>
          <Button size="large" type="ghost" style="margin-left: 8px" @click="goBack" v-if="permission.Page">返回</Button>
        </Form-item>
      </Form>
      </Col>
      <Col span="8">&nbsp;</Col>
    </Row>
    <Row><Col><Copy></Copy></Col></Row>
  </div>
</template>
<script>
  import Copy from '../../Common/copy.vue'
  import MenuList from '../Menu/menuList.vue'
  import * as API from './API.js'
  import { getCookie } from '../../../cookieUtil.js'
  export default {
    name: 'detail',
    components: { Copy, MenuList },
    data () {
      return {
        name: '',
        base: API.base,
        permission: [],
        menu: [],
        showLoad: true,
        object: {
          sname: '',
          pname: '',
          tname: '',
          reason: '',
          reply: '',
          time_start: '',
          time_end: '',
          time_apply: '',
          time_ratify: '',
          state: '',
          type: ''
        }
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
</style>
