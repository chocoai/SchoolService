<template>
  <div class="layout">
    <Row>
      <Col><MenuList :active="base" :name="name" three="新增" :menu="menu"></MenuList></Col>
    </Row>
    <Row>
      <Col span="8">&nbsp;</Col>
      <Col span="8">
        <Form :label-width="100" :rules="validate" ref="addForm" :model="object">
          <Form-item label="教师姓名" prop="name" required>
            <Input size="large" v-model="object.name" placeholder="请输入教师姓名" style="width: 400px"></Input>
          </Form-item>
          <Form-item label="联系电话" prop="mobile" required>
            <Input size="large" v-model="object.mobile" placeholder="请输入联系电话" style="width: 400px"></Input>
          </Form-item>
          <Form-item size="large" label="教师类型" required>
            <Radio-group v-model="object.type" type="button">
              <Radio label="1">在编</Radio>
              <Radio label="2">聘用</Radio>
              <Radio label="3">校外</Radio>
            </Radio-group>
          </Form-item>
          <Form-item>
            <Button size="large" type="success" @click="goSave" v-if="permission.Save">保存</Button>
            <Button size="large" type="warning" style="margin-left: 8px" @click="goReset">重置</Button>
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
            { required: true, message: '教师姓名不能为空!', trigger: 'change' }
          ],
          mobile: [
            { required: true, message: '联系电话不能为空!', trigger: 'change' },
            { message: '请输入11位手机号码', trigger: 'change', pattern: /^1(3|4|5|7|8)\d{9}$/ },
            { validator: mobileCheck, trigger: 'change' }
          ]
        },
        name: '',
        base: API.base,
        permission: [],
        menu: [],
        showLoad: true,
        object: {
          name: '',
          mobile: '',
          type: '1'
        }
      }
    },
    created: function () {
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
    methods: {
      goReset () {
        this.$refs.addForm.resetFields()
        this.object.name = ''
        this.object.mobile = ''
        this.object.type = '1'
      },
      goSave () {
        this.$Loading.start()
        this.$Message.info('正在进行保存操作，请稍后...')
        this.$http.get(
          API.save,
          { params: {
            name: this.object.name,
            mobile: this.object.mobile,
            type: this.object.type
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
              desc: '教师：' + this.object.name + '已保存！'
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
