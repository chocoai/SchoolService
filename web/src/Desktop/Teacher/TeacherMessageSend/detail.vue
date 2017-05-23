<template>
  <div class="layout">
    <Row>
      <Col><MenuList :active="base" :name="name" three="修改" :menu="menu"></MenuList></Col>
    </Row>
    <Row>
      <Col span="8">&nbsp;</Col>
      <Col span="8">
      <Form :label-width="100" :rules="validate" ref="editForm" :model="object">
        <Form-item label="消息标题" prop="title" required>
          <Input disabled size="large" v-model="object.title" placeholder="请输入消息标题" style="width: 400px"></Input>
        </Form-item>
        <Form-item label="消息内容" prop="content" required>
          <Input disabled size="large" v-model="object.content" type="textarea" :rows="4" placeholder="请输入消息内容" style="width: 400px"></Input>
        </Form-item>
        <Form-item>
          <Button size="large" type="success" @click="goSend">发送</Button>
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
    name: 'edit',
    components: { Copy, MenuList },
    data () {
      return {
        name: '',
        base: API.base,
        permission: [],
        menu: [],
        showLoad: false,
        object: {
          title: '',
          content: '',
          ids: []
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
      },
      goSend () {
        this.$Loading.start()
        this.$Message.info('正在进行修改操作，请稍后...')
        this.$http.get(
          API.edit,
          { params: {
            id: this.$route.params.id
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
              desc: '消息：' + this.object.name + '已发送！'
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
