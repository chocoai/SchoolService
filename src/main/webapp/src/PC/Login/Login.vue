<template>
  <div class="layout">
    <div class="layout-ceiling">
      <div class="layout-ceiling-main">
        <a href="#">注册登录</a> |
        <a href="#">帮助中心</a> |
        <a href="#">安全中心</a> |
        <a href="#">服务大厅</a>
      </div>
    </div>
    <div class="layout-header">
      <div class="layout-logo"></div>
    </div>
    <div style="height: 700px">
      <Carousel autoplay v-model="value1">
        <Carousel-item>
          <div align="center"><img :src="pic1" width="1400px" height="700px"/></div>
        </Carousel-item>
        <Carousel-item>
          <div align="center"><img :src="pic2" width="1400px" height="700px"/></div>
        </Carousel-item>
        <Carousel-item>
          <div align="center"><img :src="pic3" width="1400px" height="700px"/></div>
        </Carousel-item>
        <Carousel-item>
          <div align="center"><img :src="pic4" width="1400px" height="700px"/></div>
        </Carousel-item>
      </Carousel>
    </div>
    <div class="layout-copy">
      2016-2017 &copy; NelsonNick
    </div>
  </div>
</template>
<script>
  import pic1 from '../../assets/1.jpg'
  import pic2 from '../../assets/2.jpg'
  import pic3 from '../../assets/3.jpg'
  import pic4 from '../../assets/4.jpg'
  export default {
    data () {
      return {
        pic1,
        pic2,
        pic3,
        pic4,
        value1: 0
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
    .layout-logo{
        width: 100px;
        height: 30px;
        background: #5b6270;
        border-radius: 3px;
        float: left;
        position: relative;
        top: 15px;
        left: 20px;
    }
    .layout-header{
        height: 60px;
        background: #fff;
        box-shadow: 0 1px 1px rgba(0,0,0,.1);
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
</style>

