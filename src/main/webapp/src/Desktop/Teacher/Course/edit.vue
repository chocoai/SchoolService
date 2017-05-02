<template>
  <div class="layout">
    <Row>
      <Col><MenuList active="course" :name="name" three="修改" :permission="permission"></MenuList></Col>
    </Row>
    <Row>
      <Col span="8">&nbsp;</Col>
      <Col span="8">
      <Form :label-width="100" :rules="course_validate" ref="editForm" :model="course">
        <Form-item label="课程名称" prop="name" required>
          <Input size="large" v-model="course.name" placeholder="请输入课程名称" style="width: 400px"></Input>
        </Form-item>
        <Form-item label="课程描述" prop="detail" required>
          <Input size="large" v-model="course.detail" type="textarea" :autosize="{minRows: 3,maxRows: 5}" placeholder="请输入对该课程的描述" style="width: 400px"></Input>
        </Form-item>
        <Form-item label="选课人数" prop="amount" required>
          <Input-number size="large" :max="60" :min="0" v-model="course.amount" style="width: 400px"></Input-number>
        </Form-item>
        <Form-item size="large" label="课程类型" required>
          <Radio-group v-model="course.type" type="button">
            <Radio label="1">必修课</Radio>
            <Radio label="2">选修课</Radio>
          </Radio-group>
        </Form-item>
        <Form-item size="large" label="课程状态" required>
          <Radio-group v-model="course.state" type="button">
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
  import { getCookie } from '../../../cookieUtil.js'
  export default {
    name: 'edit',
    components: { Copy, MenuList },
    data () {
      const nameCheck = (rule, value, callback) => {
        this.$http.get(
          API.checkNameForEdit,
          { params: {
            id: this.$route.params.id,
            name: value
          } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          if (response.body === 'illegal' || response.body.toString() === 'overdue') {
            callback(new Error('登录过期或非法操作!'))
          } else if (response.body === 'OK') {
          } else {
            callback(new Error(response.body))
          }
        }, (response) => {
          callback(new Error('服务器内部错误!'))
        })
      }
      return {
        course_validate: {
          name: [
            { required: true, message: '课程名称不能为空!', trigger: 'change' },
            { validator: nameCheck, trigger: 'change' }
          ],
          detail: [
            { required: true, message: '课程详情不能为空!', trigger: 'change' }
          ]
        },
        name: 'xxx',
        showLoad: true,
        course: {
          name: '',
          detail: '',
          amount: 0,
          type: '',
          state: ''
        }
      }
    },
    created: function () {
      this.fetchData(this.$route.params.id)
      if (getCookie('permission') === null || getCookie('permission') === undefined || getCookie('permission') === '') {
        this.$http.get(
          API.permission
        ).then((response) => {
          if (response.body.toString() === 'illegal' || response.body.toString() === 'overdue') {
            this.$Notice.error({
              title: '登录过期或非法操作!'
            })
          } else {
            this.permission = JSON.parse(JSON.parse(getCookie('permission')))
            this.name = decodeURI(getCookie('name')).substring(1, decodeURI(getCookie('name')).length - 1)
          }
        }, (response) => {
          this.$Notice.error({
            title: '服务器内部错误!'
          })
        })
      } else {
        this.permission = JSON.parse(JSON.parse(getCookie('permission')))
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
            window.location.href = '/MainDesktop'
          } else {
            this.course = response.body
            this.name = this.course.name
            this.detail = this.course.detail
            this.amount = this.course.amount
            this.type = this.course.type
            this.state = this.course.state
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
            name: this.course.name,
            detail: this.course.detail,
            amount: this.course.amount,
            type: this.course.type,
            state: this.course.state
          } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          if (response.body === 'illegal' || response.body.toString() === 'overdue') {
            this.$Loading.error()
            this.$Notice.error({
              title: '登录过期或非法操作!'
            })
            window.location.href = '/MainDesktop'
          } else if (response.body === 'OK') {
            this.$Loading.finish()
            this.$Notice.success({
              title: '操作完成!',
              desc: '课程：' + this.course.name + '已修改！'
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
