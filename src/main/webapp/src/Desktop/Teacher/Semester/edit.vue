<template>
  <div class="layout">
    <Row>
      <Col><MenuList active="semester" :name="name" three="修改" :permission="permission"></MenuList></Col>
    </Row>
    <Row>
      <Col span="8">&nbsp;</Col>
      <Col span="8">
      <Form :label-width="100" :rules="validate" ref="editForm" :model="semester">
        <Form-item label="学期名称" prop="name" required>
          <Input size="large" v-model="semester.name" placeholder="请输入学期名称" style="width: 400px"></Input>
        </Form-item>
        <Form-item label="开始日期" prop="time_start" required>
          <Date-picker type="date" v-model="semester.time_start" placement="bottom-end" placeholder="选择开始日期" style="width: 400px"></Date-picker>
        </Form-item>
        <Form-item label="终止日期" prop="time_end" required>
          <Date-picker type="date" v-model="semester.time_end" placement="bottom-end" placeholder="选择终止日期" style="width: 400px"></Date-picker>
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
        validate: {
          name: [
            { required: true, message: '学期名称不能为空!', trigger: 'change' },
            { validator: nameCheck, trigger: 'change' }
          ],
          time_start: [
            { required: true, type: 'date', message: '开始日期不能为空!', trigger: 'change' }
          ],
          time_end: [
            { required: true, type: 'date', message: '终止日期不能为空!', trigger: 'change' }
          ]
        },
        name: '',
        showLoad: true,
        semester: {
          name: '',
          time_start: '',
          time_end: ''
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
            window.location.href = '../MainDesktop'
          } else {
            this.semester = response.body
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
        console.log(this.semester.time_start)
        console.log(this.semester.time_end)
        this.$Loading.start()
        this.$Message.info('正在进行修改操作，请稍后...')
        this.$http.get(
          API.edit,
          { params: {
            id: this.$route.params.id,
            name: this.semester.name,
            time_start: new Date(this.semester.time_start).getTime(),
            time_end: new Date(this.semester.time_end).getTime()
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
              desc: '学期：' + this.semester.name + '已修改！'
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
