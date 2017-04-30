<template>
  <div class="layout">
    <div class="layout-ceiling">
      <div class="layout-ceiling-left">
        <h2>济南市育明小学欢迎您！</h2>
      </div>
      <div class="layout-ceiling-main">
        <a href="http://www.yumingxx.com/">学校官网</a> |
        <a href="http://ymxx.kccs.online:9092/ymxx/">课程管理</a>
      </div>
    </div>
    <div align="center" style="height: 700px">
      <Row>
        <Col span="16">
          <Carousel autoplay v-model="value1">
            <Carousel-item>
              <div align="center"><img :src="pic1" width="1050px" height="700px"/></div>
            </Carousel-item>
            <Carousel-item>
              <div align="center"><img :src="pic2" width="1050px" height="700px"/></div>
            </Carousel-item>
            <Carousel-item>
              <div align="center"><img :src="pic3" width="1050px" height="700px"/></div>
            </Carousel-item>
            <Carousel-item>
              <div align="center"><img :src="pic4" width="1050px" height="700px"/></div>
            </Carousel-item>
          </Carousel>
        </Col>
        <Col span="8">
          <div align="center"><img :src="schoolIcon" width="250px" height="250px"/></div>
          <div align="center"><h1>明德尚美&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</h1></div>
          <div align="center"><h1>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;和雅共进</h1></div>
          <br>
          <Input size="large" type="text" v-model="user" placeholder="请输入用户名" style="width:320px;"><span slot="prepend">用户名</span></Input>
          <br>
          <Input size="large" type="password" v-model="password" placeholder="请输入密码" style="width:320px;"><span slot="prepend">密码</span></Input>
          <br>
          <Input size="large" type="text" v-model="verifyCode" placeholder="请输入验证码" style="width:320px;"><span slot="prepend">验证码</span></Input>
          <br>
          <img :src="verifyPic" @click="getImg" style="width:200px; height:50px"/><br>
          <br>
          <Button type="primary" size="large" @click="goLogin">登录</Button>
          <Button type="ghost" size="large" @click="goReset">重置</Button>
        </Col>
      </Row>
    </div>
    <Copy></Copy>
  </div>
</template>
<script>
  import Copy from '../Common/copy.vue'
  import pic1 from '../../assets/1.jpg'
  import pic2 from '../../assets/2.jpg'
  import pic3 from '../../assets/3.jpg'
  import pic4 from '../../assets/4.jpg'
  import schoolIcon from '../../assets/newIcon.png'
  import * as API from './API.js'
  import { setCookie } from '../../cookieUtil.js'
  export default {
    name: 'login',
    components: { Copy },
    data () {
      return {
        pic1,
        pic2,
        pic3,
        pic4,
        schoolIcon,
        verifyPic: '',
        user: '',
        password: '',
        verifyCode: '',
        value1: 0
      }
    },
    created () {
      this.getImg()
    },
    methods: {
      getImg () {
        this.verifyPic = '/img?' + Math.floor(Math.random() * 100)
      },
      goReset () {
        this.user = ''
        this.password = ''
        this.verifyCode = ''
      },
      goLogin () {
        this.$Loading.start()
        this.$Message.info('正在进行登录操作，请稍后...')
        this.$http.get(
          API.login,
          { params: {
            user: this.user,
            password: this.password,
            verifyCode: this.verifyCode
          } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          if (response.body === 'OK') {
            this.$http.get(
              API.power
            ).then((res) => {
              this.$Loading.finish()
              setCookie('power', res.body, 1)
              window.location.href = '/course/PC_Manager_Course'
            }, (res) => {
              this.$Loading.error()
              this.$Notice.error({
                title: '权限异常，请重新登录!'
              })
            })
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
    position: relative;
    border-radius: 4px;
    overflow: hidden;
  }
  .layout-ceiling{
    background: #464c5b;
    padding: 10px 0;
    overflow: hidden;
  }
  .layout-ceiling-main{
    float: right;
    margin-right: 35px;
  }
  .layout-ceiling-main a{
    color: #9ba7b5;
    font-size: 16px;
  }
  .layout-ceiling-left{
    float: left;
    margin-left: 15px;
  }
  .layout-ceiling-left h2{
    color: #887c64;
  }
</style>

