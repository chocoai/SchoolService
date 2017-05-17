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
          <Menu-item name="SemesterDesktop" v-if="menu.SemesterDesktop_Page">学期</Menu-item>
          <Menu-item name="CourseDesktop" v-if="menu.CourseDesktop_Page">课程</Menu-item>
          <Menu-item name="RoomDesktop" v-if="menu.RoomDesktop_Page">班级</Menu-item>
          <Menu-item name="TeacherDesktop" v-if="menu.TeacherDesktop_Page">教师</Menu-item>
          <Menu-item name="StudentDesktop" v-if="menu.StudentDesktop_Page">学生</Menu-item>
          <Menu-item name="ParentDesktop" v-if="menu.ParentDesktop_Page">家长</Menu-item>
        </Submenu>
        <Submenu name="relation">
          <template slot="title">
            <Icon type="stats-bars"></Icon>
            关系管理
          </template>
          <Menu-item name="StudentParentIdentity" v-if="menu.StudentParentIdentityDesktop_Page">学生_家长_身份</Menu-item>
          <Menu-item name="RoomStudentDesktop" v-if="menu.RoomStudentDesktop_Page">班级_学生</Menu-item>
          <Menu-item name="CourseRoomTeacher" v-if="menu.CourseRoomTeacherDesktop_Page">课程_班级_教师</Menu-item>
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
    props: ['active', 'name', 'three', 'menu'],
    data () {
      return {
        active: '',
        name: '',
        menu: []
      }
    },
    computed: {
      one: function () {
        switch (this.active) {
          case 'home':
            return '首页'
          case 'SemesterDesktop':
            return '基础管理'
          case 'CourseDesktop':
            return '基础管理'
          case 'RoomDesktop':
            return '基础管理'
          case 'TeacherDesktop':
            return '基础管理'
          case 'StudentDesktop':
            return '基础管理'
          case 'ParentDesktop':
            return '基础管理'
          case 'StudentParentIdentity':
            return '关系管理'
          case 'RoomStudentDesktop':
            return '关系管理'
          case 'CourseRoomTeacher':
            return '关系管理'
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
          case 'SemesterDesktop':
            return '学期'
          case 'CourseDesktop':
            return '课程'
          case 'RoomDesktop':
            return '班级'
          case 'TeacherDesktop':
            return '教师'
          case 'StudentDesktop':
            return '学生'
          case 'ParentDesktop':
            return '家长'
          case 'StudentParentIdentity':
            return '学生_家长_身份'
          case 'RoomStudentDesktop':
            return '班级_学生'
          case 'CourseRoomTeacher':
            return '课程_班级_教师'
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
          case 'SemesterDesktop':
            window.location.href = '/SemesterDesktop/Page'
            break
          case 'CourseDesktop':
            window.location.href = '/CourseDesktop/Page'
            break
          case 'RoomDesktop':
            window.location.href = '/RoomDesktop/Page'
            break
          case 'TeacherDesktop':
            window.location.href = '/TeacherDesktop/Page'
            break
          case 'StudentDesktop':
            window.location.href = '/StudentDesktop/Page'
            break
          case 'ParentDesktop':
            window.location.href = '/ParentDesktop/Page'
            break
          case 'StudentParentIdentity':
            window.location.href = '/StudentParentIdentityDesktop/Page'
            break
          case 'RoomStudentDesktop':
            window.location.href = '/RoomStudentDesktop/Page'
            break
          case 'CourseRoomTeacher':
            window.location.href = '/CourseRoomTeacherDesktop/Page'
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
    color: #887c64;
  }
</style>
