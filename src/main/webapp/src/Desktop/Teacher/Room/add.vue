<template>
  <div class="layout">
    <Row>
      <Col><MenuList active="room" :name="name" three="新增" :menu="menu"></MenuList></Col>
    </Row>
    <Row>
      <Col span="8">&nbsp;</Col>
      <Col span="8">
        <Form :label-width="100" :rules="validate" ref="addForm" :model="room">
          <Form-item label="入学年份" prop="year" required>
            <Input-number size="large" v-model="room.year" :max="2050" :min="2000" style="width: 400px"></Input-number>
          </Form-item>
          <Form-item label="班序" prop="order" required>
            <Input-number size="large" v-model="room.order" :max="30" :min="1" style="width: 400px"></Input-number>
          </Form-item>
          <Form-item size="large" label="班级状态" required>
            <Radio-group v-model="room.state" type="button">
              <Radio label="1">激活</Radio>
              <Radio label="0">注销</Radio>
            </Radio-group>
          </Form-item>
          <Form-item>
            <Button size="large" type="primary" @click="goSave">保存</Button>
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
    name: 'add',
    components: { Copy, MenuList },
    data () {
      return {
        validate: {
          year: [
            { required: true, type: 'number', message: '入学年份不能为空!', trigger: 'change' }
          ],
          order: [
            { required: true, type: 'number', message: '班序不能为空!', trigger: 'change' }
          ]
        },
        name: '',
        permission: [],
        menu: [],
        showLoad: true,
        room: {
          name: '',
          year: '2017',
          order: '1',
          state: '1'
        }
      }
    },
    created: function () {
      if (getCookie('menu') === null || getCookie('menu') === undefined || getCookie('menu') === '' || getCookie('RoomDesktop') === null || getCookie('RoomDesktop') === undefined || getCookie('RoomDesktop') === '') {
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
              this.permission = JSON.parse(JSON.parse(getCookie('RoomDesktop')))
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
        this.permission = JSON.parse(JSON.parse(getCookie('RoomDesktop')))
        this.menu = JSON.parse(JSON.parse(getCookie('menu')))
        this.name = decodeURI(getCookie('name')).substring(1, decodeURI(getCookie('name')).length - 1)
      }
    },
    methods: {
      goReset () {
        this.$refs.addForm.resetFields()
        this.room.year = ''
        this.room.order = ''
        this.room.state = '1'
      },
      goSave () {
        this.$Loading.start()
        this.$Message.info('正在进行保存操作，请稍后...')
        this.$http.get(
          API.save,
          { params: {
            year: this.room.year,
            order: this.room.order,
            state: this.room.state
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
              desc: '班级：' + this.room.name + '已保存！'
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
