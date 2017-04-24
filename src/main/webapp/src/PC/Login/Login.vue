<template>
  <div class="layout">
    <div class="layout-ceiling">
      <div class="layout-ceiling-left">
        <h4>济南市育明小学欢迎您！</h4>
      </div>
      <div class="layout-ceiling-main">
        <a href="http://www.yumingxx.com/">学校官网</a> |
        <a href="http://ymxx.kccs.online:9092/ymxx/">课程管理</a> |
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
          <div align="center"><p>育明小学</p></div>
          <br>
          <Input type="text" v-model="user" placeholder="用户名" style="width:320px;"><span slot="prepend">用户名</span></Input>
          <br>
          <Input type="password" v-model="password" placeholder="密码" style="width:320px;"><span slot="prepend">密码</span></Input>
          <br>
          <Input type="text" v-model="verifyCode" placeholder="验证码" style="width:320px;"><span slot="prepend">验证码</span></Input>
          <br>
          <img :src="verifyPic" @click="getImg"/><br>
          <br>
          <Button type="primary">登录</Button>
        </Col>
      </Row>
    </div>
    <div class="layout-copy">
      2016-11-10 15:27:00 &copy; WangYiXin
    </div>
  </div>
</template>
<script>
  import pic1 from '../../assets/1.jpg'
  import pic2 from '../../assets/2.jpg'
  import pic3 from '../../assets/3.jpg'
  import pic4 from '../../assets/4.jpg'
  import schoolIcon from '../../assets/schoolIcon.jpg'
  export default {
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
      this.verifyPic = '/img'
    },
    methods: {
      getImg () {
        this.verifyPic = '/img?' + Math.floor(Math.random() * 100)
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
    .layout-copy{
        text-align: center;
        padding: 10px 0 20px;
        color: #9ea7b4;
    }
    .layout-ceiling{
        background: #464c5b;
        padding: 10px 0;
        overflow: hidden;
    }
    .layout-ceiling-main{
        float: right;
        margin-right: 15px;
    }
    .layout-ceiling-main a{
        color: #9ba7b5;
    }
    .layout-ceiling-left{
        float: left;
        margin-left: 15px;
    }
    .layout-ceiling-left h4{
        color: #9ba7b5;
    }
</style>

