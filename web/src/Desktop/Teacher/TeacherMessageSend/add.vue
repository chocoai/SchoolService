<template>
  <div class="layout">
    <Row>
      <Col><MenuList :active="base" :name="name" three="新增" :menu="menu"></MenuList></Col>
    </Row>
    <Row>
      <Col span="8">&nbsp;</Col>
      <Col span="8">
        <Form :label-width="100" :rules="validate" ref="addForm" :model="object">
          <Form-item label="消息标题" prop="title" required>
            <Input size="large" v-model="object.title" placeholder="请输入消息标题" style="width: 400px"></Input>
          </Form-item>
          <Form-item label="消息内容" prop="content" required>
            <Input size="large" v-model="object.content" type="textarea" :rows="4" placeholder="请输入消息内容" style="width: 400px"></Input>
          </Form-item>
          <Form-item label="消息接收人" v-if="showLoad">
            <Checkbox-group v-model="object.ids">
              <Checkbox :label="list.id" v-for="list in lists">
                <span>{{list.name}}-{{list-mobile}}</span>
              </Checkbox>
            </Checkbox-group>
          </Form-item>
          <Form-item>
            <Button size="large" type="success" @click="goSave" v-if="permission.Save">发送</Button>
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
      return {
        validate: {
          title: [
            { required: true, message: '消息标题不能为空!', trigger: 'change' }
          ],
          content: [
            { required: true, message: '消息内容不能为空!', trigger: 'change' },
            { type: 'string', max: 400, message: '内容不能多于400字', trigger: 'change' }
          ],
        },
        name: '',
        base: API.base,
        permission: [],
        menu: [],
        lists: [],
        showLoad: false,
        object: {
          title: '',
          content: '',
          ids: []
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
      this.$http.get(
        API.teacher
      ).then((res) => {
        this.lists = res.body
        this.showLoad = true
      }, (res) => {
        this.$Notice.error({
          title: '服务器内部错误!'
        })
      })
    },
    methods: {
      goReset () {
        this.$refs.addForm.resetFields()
        this.object.title = ''
        this.object.content = ''
        this.object.ids = []
        this.showLoad = false
        this.$http.get(
          API.teacher
        ).then((res) => {
          this.lists = res.body
          this.showLoad = true
        }, (res) => {
          this.$Notice.error({
            title: '服务器内部错误!'
          })
        })
      },
      goSave () {
        this.$Loading.start()
        this.$Message.info('正在进行保存操作，请稍后...')
        this.$http.get(
          API.save,
          { params: {
            title: this.object.title,
            content: this.object.content,
            ids: this.object.ids
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
              desc: '消息：' + this.object.title + '已保存！'
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
