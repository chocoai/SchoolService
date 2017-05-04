<template>
  <div class="layout">
    <Row>
      <Col><MenuList active="student" :name="name" three="修改" :menu="menu"></MenuList></Col>
    </Row>
    <Row>
      <Col span="8">&nbsp;</Col>
      <Col span="8">
      <Form :label-width="100" :rules="validate" ref="editForm" :model="object">
        <Form-item label="姓名" prop="name" required>
          <Input size="large" v-model="object.name" placeholder="请输入学生的姓名" style="width: 400px"></Input>
        </Form-item>
        <Form-item label="身份证号码" prop="number" required>
          <Input size="large" v-model="object.number" placeholder="请输入学生的身份证号码" style="width: 400px"></Input>
        </Form-item>
        <Form-item label="学籍号码" prop="code" required>
          <Input size="large" v-model="object.code" placeholder="请输入学生的学籍号码" style="width: 400px"></Input>
        </Form-item>
        <Form-item label="家庭住址" prop="address" required>
          <Input size="large" v-model="object.address" placeholder="请输入学生的家庭住址" style="width: 400px"></Input>
        </Form-item>
        <Form-item size="large" label="学生状态" required>
          <Radio-group v-model="object.state" type="button">
            <Radio label="1">激活</Radio>
            <Radio label="0">注销</Radio>
          </Radio-group>
        </Form-item>
        <Form-item>
          <Button size="large" type="primary" @click="goEdit">修改</Button>
          <Button size="large" type="ghost" style="margin-left: 8px" @click="goReset">重置</Button>
          <Button size="large" style="margin-left: 8px" @click="goBack">返回</Button>
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
  import { getCookie, checkNumber } from '../../../cookieUtil.js'
  export default {
    name: 'edit',
    components: { Copy, MenuList },
    data () {
      const numberCheck = (rule, value, callback) => {
        if (checkNumber(value)) {
          this.$http.get(
            API.checkNumberForEdit,
            { params: {
              id: this.$route.params.id,
              number: value
            } },
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
        } else {
          callback(new Error('身份证号码错误!'))
        }
      }
      const codeCheck = (rule, value, callback) => {
        this.$http.get(
          API.checkCodeForEdit,
          { params: {
            id: this.$route.params.id,
            code: value
          } },
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
            { required: true, message: '学生姓名不能为空!', trigger: 'change' }
          ],
          number: [
            { required: true, message: '学生的身份证号码不能为空!', trigger: 'change' },
            { validator: numberCheck, trigger: 'change' }
          ],
          code: [
            { required: true, message: '学生的学籍编号不能为空!', trigger: 'change' },
            { validator: codeCheck, trigger: 'change' }
          ],
          address: [
            { required: true, message: '学生的家庭住址不能为空!', trigger: 'change' }
          ]
        },
        name: '',
        permission: [],
        menu: [],
        showLoad: true,
        object: {
          name: '',
          number: '',
          code: '',
          address: '',
          state: '1'
        }
      }
    },
    created: function () {
      this.fetchData(this.$route.params.id)
      if (getCookie('menu') === null || getCookie('menu') === undefined || getCookie('menu') === '' || getCookie('StudentDesktop') === null || getCookie('StudentDesktop') === undefined || getCookie('StudentDesktop') === '') {
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
              this.permission = JSON.parse(JSON.parse(getCookie('StudentDesktop')))
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
        this.permission = JSON.parse(JSON.parse(getCookie('StudentDesktop')))
        this.menu = JSON.parse(JSON.parse(getCookie('menu')))
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
      },
      goEdit () {
        this.$Loading.start()
        this.$Message.info('正在进行修改操作，请稍后...')
        this.$http.get(
          API.edit,
          { params: {
            id: this.$route.params.id,
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
              desc: '学生：' + this.object.name + '已修改！'
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
