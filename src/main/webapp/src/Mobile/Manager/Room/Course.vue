<template>
  <div>
    <mu-appbar title="班级课程">
      <mu-icon-button icon='reply' slot="right" @click="goReply"/>
    </mu-appbar>
    <mu-text-field label="班级名称" disabled="true" v-model="roomName" fullWidth labelFloat icon="title"/><br/>
    <mu-text-field label="当前学期" disabled="true" v-model="semesterName" fullWidth labelFloat icon="title"/><br/>
    <mu-sub-header>必修课</mu-sub-header>
    <mu-flexbox wrap="wrap">
      <mu-flexbox-item class="flex-demo">
        <mu-checkbox v-for="courseA in courses" :label="courseA.name" :nativeValue="courseA.id" v-model="course" labelClass="a" iconClass="b"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <br/>
    <br/>
    <br/>
    <mu-sub-header>选修课</mu-sub-header>
    <mu-flexbox wrap="wrap">
      <mu-flexbox-item class="flex-demo">
        <mu-checkbox v-for="courseB in courzes" :label="courseB.name" :nativeValue="courseB.id" v-model="courze" labelClass="a" iconClass="b"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <br/>
    <mu-flexbox v-if="Edit_Able && State_Able">
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="edit" @click="goEdit" primary/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-flexbox v-if="Save_Able">
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="cancel" @click="goCancel" secondary/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="done" @click="goSave" backgroundColor="green"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-dialog :open="Reading" title="正在读取" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-dialog :open="Saving" title="正在保存" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-popup position="bottom" :overlay="false" popupClass="popup-bottom" :open="bottomPopup">
      <mu-icon :value="icon" :size="36" :color="color"/>&nbsp;{{ message }}
    </mu-popup>
  </div>
</template>

<script>
import * as API from './API.js'
export default {
  name: 'Edit',
  data () {
    return {
      Edit_Able: true,
      Save_Able: false,
      Active_Able: false,
      State_Able: false,
      Activing: false,
      Saving: false,
      Reading: true,
      bottomPopup: false,
      icon: '',
      color: '',
      message: '',
      roomName: '',
      roomState: '',
      semester: '',
      semesterName: '',
      courseA: '',
      courseB: '',
      courses: '',
      courzes: ''
    }
  },
  created () {
    this.fetchData(this.$route.params.id)
  },
  watch: {
    // 如果路由有变化，会再次执行该方法
    '$route': 'fetchData'
  },
  computed: {
    name: function () {
      return this.year + '级' + this.order + '班'
    }
  },
  methods: {
    goReply () {
      this.$router.push({ path: '/list' })
    },
    openPopup (message, icon, color) {
      this.message = message
      this.icon = icon
      this.color = color
      this.bottomPopup = true
      setTimeout(() => { this.bottomPopup = false }, 1500)
    },
    goEdit () {
      this.Edit_Able = false
      this.Save_Able = true
    },
    goCancel () {
      this.Edit_Able = true
      this.Save_Able = false
      this.fetchData(this.$route.params.id)
    },
    goSave () {
      this.Saving = true
      this.$http.get(
        API.edit,
        { params: {
          id: this.$route.params.id,
          course: this.course,
          courze: this.courze
        } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        this.Saving = false
        if (response.body === 'error') {
          this.openPopup('请重新登录!', 'report_problem', 'orange')
          window.location.href = '/'
        } else if (response.body === 'OK') {
          this.openPopup('修改成功！', 'check_circle', 'green')
          setTimeout(() => { this.$router.push({ path: '/list' }) }, 1000)
        } else {
          this.openPopup(response.body, 'report_problem', 'orange')
        }
      }, (response) => {
        this.Saving = false
        this.openPopup('服务器内部错误!', 'error', 'red')
      })
    },
    fetchData (id) {
      this.$http.get(
        API.get,
        { params: { id: id } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        this.room = response.body
        this.roomName = this.room.name
        this.roomState = this.room.state
        this.$http.get(
          API.getCourseList,
          { params: {state: 1, type: 1} },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          this.courses = response.body
          this.$http.get(
            API.getCourseList,
            { params: {state: 1, type: 2} },
            { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
          ).then((response) => {
            this.courzes = response.body
            this.$http.get(
              API.getSemester,
              { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
            ).then((response) => {
              this.semester = response.body
              this.semesterName = this.semester.name
              this.Reading = false
              if (this.roomState.toString() === '1') {
                this.State_Able = true
              } else {
                this.State_Able =  false
              }
            }, (response) => {
            })
          }, (response) => {
          })
        }, (response) => {
        })
      }, (response) => {
      })
    }
  }
}
</script>
<style lang="css">
  .flex-demo {
    height: 70px;
    text-align: center;
    line-height: 32px;
  }
  .popup-bottom {
    width: 100%;
    opacity: .8;
    height: 48px;
    line-height: 48px;
    display: flex;
    align-items: center;
    justify-content: center;
    max-width: 300px;
  }
  .box{
    display: flex;
    flex-wrap: wrap;
  }
  .a{
    width: 70px;
    text-align: left;
  }
  .b{
    width: 10px;
    text-align: right;
  }
</style>
