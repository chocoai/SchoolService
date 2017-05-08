<template>
  <div class="layout">
    <Row>
      <Col><MenuList active="parent" :name="name" three="新增" :menu="menu"></MenuList></Col>
    </Row>
    <Row>
      <Col span="8">&nbsp;</Col>
      <Col span="8">
        <Form :label-width="100" :rules="validate" ref="addForm" :model="object">
          <Form-item label="家长姓名" prop="name" required>
            <Input size="large" v-model="object.name" placeholder="请输入教师姓名" style="width: 400px"></Input>
          </Form-item>
          <Form-item label="联系电话" prop="mobile" required>
            <Input size="large" v-model="object.mobile" placeholder="请输入联系电话" style="width: 400px"></Input>
          </Form-item>
          <Form-item>
            <Button size="large" type="success" @click="goSave">保存</Button>
            <Button size="large" type="warning" style="margin-left: 8px" @click="goReset">重置</Button>
            <Button size="large" type="ghost" style="margin-left: 8px" @click="goBack">返回</Button>
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
    name: 'add',
    components: { Copy, MenuList },
    data () {
      const mobileCheck = (rule, value, callback) => {
        this.$http.get(
          API.checkMobileForAdd,
          { params: { mobile: value } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          if (response.body === 'illegal' || response.body.toString() === 'overdue') {
            callback(new Error('登录过期或非法操作!'))
          } else if (response.body === 'OK') {
            callback()
          } else {
            callback(new Error(response.body))
          }
        }, (response) => {
          callback(new Error('服务器内部错误!'))
        })
      }
      return {
        validate: {
          name: [
            { required: true, message: '家长姓名不能为空!', trigger: 'change' }
          ],
          mobile: [
            { required: true, message: '联系电话不能为空!', trigger: 'change' },
            { message: '请输入11位手机号码', trigger: 'change', pattern: /^1(3|4|5|7|8)\d{9}$/ },
            { validator: mobileCheck, trigger: 'change' }
          ]
        },
        name: '',
        permission: [],
        menu: [],
        showLoad: true,
        object: {
          name: '',
          mobile: ''
        }
      }
    },
    created: function () {
      if (getCookie('menu') === null || getCookie('menu') === undefined || getCookie('menu') === '' || getCookie('ParentDesktop') === null || getCookie('ParentDesktop') === undefined || getCookie('ParentDesktop') === '') {
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
              this.permission = JSON.parse(JSON.parse(getCookie('ParentDesktop')))
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
        this.permission = JSON.parse(JSON.parse(getCookie('ParentDesktop')))
        this.menu = JSON.parse(JSON.parse(getCookie('menu')))
        this.name = decodeURI(getCookie('name')).substring(1, decodeURI(getCookie('name')).length - 1)
      }
    },
    methods: {
      goReset () {
        this.$refs.addForm.resetFields()
        this.object.name = ''
        this.object.mobile = ''
      },
      goSave () {
        this.$Loading.start()
        this.$Message.info('正在进行保存操作，请稍后...')
        this.$http.get(
          API.save,
          { params: {
            name: this.object.name,
            mobile: this.object.mobile
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
              desc: '家长：' + this.object.name + '已保存！'
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
