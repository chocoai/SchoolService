<template>
  <div>
    <Menu mode="horizontal" theme="dark" :active-name="active" @on-select="onSelect">
      <div class="layout-logo">当前登录人员：{{name}}</div>
      <div class="layout-nav">
        <Menu-item name="home">
          <Icon type="settings"></Icon>
          首页
        </Menu-item>
        <Submenu name="basic">
          <template slot="title">
            <Icon type="stats-bars"></Icon>
            基础管理
          </template>
          <Menu-item name="semester">学期</Menu-item>
          <Menu-item name="course" v-if="permission.CourseDesktop_Page">课程</Menu-item>
          <Menu-item name="room">班级</Menu-item>
        </Submenu>
        <Submenu name="person">
          <template slot="title">
            <Icon type="stats-bars"></Icon>
            人员管理
          </template>
          <Menu-item name="teacher">教师</Menu-item>
          <Menu-item name="student">学生</Menu-item>
          <Menu-item name="parent">家长</Menu-item>
        </Submenu>
        <Submenu name="information">
          <template slot="title">
            <Icon type="stats-bars"></Icon>
            信息管理
          </template>
          <Menu-item name="notice">公告</Menu-item>
          <Menu-item name="attendance">考勤</Menu-item>
          <Menu-item name="vote">投票</Menu-item>
          <Menu-item name="feedback">反馈</Menu-item>
        </Submenu>
        <Submenu name="information">
          <template slot="title">
            <Icon type="stats-bars"></Icon>
            评比管理
          </template>
          <Menu-item name="notice1">评比管理</Menu-item>
        </Submenu>
        <Submenu name="information">
          <template slot="title">
            <Icon type="stats-bars"></Icon>
            统计汇总
          </template>
          <Menu-item name="notice2">统计汇总</Menu-item>
        </Submenu>
        <Menu-item name="exit">
          <Icon type="settings"></Icon>
          退出程序
        </Menu-item>
      </div>
    </Menu>
    <div class="layout-breadcrumb">
      <Breadcrumb>
        <Breadcrumb-item>{{one}}</Breadcrumb-item>
        <Breadcrumb-item>{{two}}</Breadcrumb-item>
        <Breadcrumb-item v-if="three.toString() !== 'blank'">{{three}}</Breadcrumb-item>
      </Breadcrumb>
    </div>
  </div>
</template>
<script>
  export default {
    name: 'menu',
    props: ['active', 'name', 'three', 'permission'],
    data () {
      return {
        active: '',
        name: '',
        permission: []
      }
    },
    computed: {
      one: function () {
        switch (this.active) {
          case 'home':
            return '首页'
          case 'semester':
            return '基础管理'
          case 'course':
            return '基础管理'
          case 'room':
            return '基础管理'
          case 'teacher':
            return '人员管理'
          case 'student':
            return '人员管理'
          case 'parent':
            return '人员管理'
          case 'notice':
            return '信息管理'
          case 'attendance':
            return '信息管理'
          case 'vote':
            return '信息管理'
          case 'feedback':
            return '信息管理'
          case 'notice1':
            return '评比管理'
          case 'notice2':
            return '统计汇总'
          default:
            return ''
        }
      },
      two: function () {
        switch (this.active) {
          case 'home':
            return ''
          case 'semester':
            return '学期'
          case 'course':
            return '课程'
          case 'room':
            return '班级'
          case 'teacher':
            return '教师'
          case 'student':
            return '学生'
          case 'parent':
            return '家长'
          case 'notice':
            return '公告'
          case 'attendance':
            return '考勤'
          case 'vote':
            return '投票'
          case 'feedback':
            return '反馈'
          case 'notice1':
            return '评比管理'
          case 'notice2':
            return '统计汇总'
          default:
            return ''
        }
      }
    },
    methods: {
      onSelect (name) {
        switch (name) {
          case 'home':
            window.location.href = '../MainDesktop/Teacher_Home'
            break
          case 'semester':
            console.log(name)
            break
          case 'course':
            window.location.href = '/CourseDesktop/Page'
            break
          case 'room':
            console.log(name)
            break
          default:
            window.location.href = '/MainDesktop'
        }
      }
    }
  }
</script>
<style scoped>
  .layout-nav{
    width: 1400px;
    margin: 0 auto;
  }
  .layout-breadcrumb{
    padding: 10px 15px 0;
  }
  .layout-logo{
    width: 200px;
    border-radius: 3px;
    float: left;
    position: relative;
    top: 1px;
    left: 20px;
    font-size: 16px;
  }
</style>
