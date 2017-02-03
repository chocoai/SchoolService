<template>
  <div class="RoomEdit">
    <mu-appbar title="请核实后输入以下信息">
      <mu-icon-button icon='reply' slot="right" @click="goReply"/>
    </mu-appbar>
    <mu-text-field v-model="name" label="社团名称" :disabled="edit" icon="comment" :errorColor="nameErrorColor" :errorText="nameErrorText" @input="checkName" fullWidth labelFloat/><br/>
    <mu-sub-header>管理教师：</mu-sub-header>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-flat-button :label="teamName" :disabled="edit" @click="openTeamTeacher=true" :icon="teamIcon" :backgroundColor="teamBack" color="#FFFFFF"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="edit" v-if="edit" @click="goEdit" primary/>
        <mu-float-button icon="cancel" v-if="save" @click="goCancel" secondary/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="delete" v-if="inactive"  @click="openInactive" backgroundColor="red"/>
        <mu-float-button icon="compare_arrows" v-if="active" @click="openActive" backgroundColor="green"/>
        <mu-float-button icon="done" v-if="save" @click="goSave" backgroundColor="green"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-dialog :open="forSave" title="正在保存" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-drawer right :open="openTeamTeacher" docked="false">
      <mu-appbar title="请选择管理老师" @click.native="openTeamTeacher=false">
        <mu-icon-button icon='done' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item title="清空" @click.native="teamTeacher=''">
          <mu-icon slot="left" value="delete_forever" :size="40"/>
        </mu-list-item>
        <mu-list-item v-for="teacher in teachers" :title="teacher.name">
          <mu-avatar v-if="teacher.state.toString() === '1'" :src="teacher.picUrl" slot="leftAvatar" :size="40"/>
          <mu-icon v-if="teacher.state.toString() === '2'" slot="left" color="#9e9e9e" value="sentiment_very_dissatisfied" :size="40" />
          <mu-icon v-if="teacher.state.toString() === '3'" slot="left" color="#8bc34a" value="sentiment_neutral" :size="40" />
          <mu-icon v-if="teacher.state.toString() === '4'" slot="left" color="#3f51b5" value="sentiment_dissatisfied" :size="40" />
          <mu-checkbox v-model="teamTeacher" :nativeValue="teacher.id" uncheckIcon="favorite_border" checkedIcon="favorite" slot="right"/>
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-dialog :open="forInactive" :title="inactiveTitle" @close="goClose">
      <mu-flat-button label="取消" @click="goClose" />
      <mu-flat-button label="确定" @click="goInactive" secondary/>
    </mu-dialog>
    <mu-dialog :open="forActive" :title="activeTitle" @close="goClose">
      <mu-flat-button label="取消" @click="goClose" />
      <mu-flat-button label="确定" @click="goActive" secondary/>
    </mu-dialog>
    <mu-popup position="bottom" :overlay="false" popupClass="popup-bottom" :open="bottomPopup">
      <mu-icon :value="icon" :size="36" :color="color"/>&nbsp;{{ message }}
    </mu-popup>
  </div>
</template>

<script>
import * as API from './TeamAPI.js'
export default {
  name: 'TeamEdit',
  data () {
    return {
      bottomPopup: false,
      edit: true,
      save: false,
      inactive: false,
      active: false,
      forSave: false,
      forInactive: false,
      forActive: false,
      activeTitle: '',
      inactiveTitle: '',
      icon: '',
      color: '',
      name: '',
      message: '',
      nameErrorText: '',
      nameErrorColor: '',
      team: '',
      teamIcon: 'bookmark_border',
      teamBack: '#66CCCC',
      teamName: '未设置',
      openTeamTeacher: false,
      teamTeacher: [],
      teachers: []
    }
  },
  created () {
    this.$http.get(
      API.TeacherList,
      { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
    ).then((response) => {
      this.teachers = response.body
    }, (response) => {
      this.openPopup('服务器内部错误！', 'error', 'red')
    })
    this.fetchData(this.$route.params.teamId)
    this.getTeamTeacher(this.$route.params.teamId)
  },
  computed: {
    teamBack: function () {
      if (this.teamTeacher.length > 0) {
        return '#9999CC'
      } else {
        return '#66CCCC'
      }
    },
    teamIcon: function () {
      if (this.teamTeacher.length > 0) {
        return 'bookmark'
      } else {
        return 'bookmark_border'
      }
    },
    teamName: function () {
      if (this.teamTeacher.length > 0) {
        return '已设置'
      } else {
        return '未设置'
      }
    }
  },
  watch: {
    // 如果路由有变化，会再次执行该方法
    '$route': 'fetchData'
  },
  methods: {
    goReply () {
      this.$router.push({ path: '/teamList' })
    },
    openInactive () {
      this.forInactive = true
    },
    openActive () {
      this.forActive = true
    },
    openPopup (message, icon, color) {
      this.message = message
      this.icon = icon
      this.color = color
      this.bottomPopup = true
      setTimeout(() => { this.bottomPopup = false }, 1500)
    },
    goEdit () {
      this.edit = false
      this.save = true
      this.inactive = false
      this.active = false
    },
    goCancel () {
      this.edit = true
      this.save = false
      this.fetchData(this.$route.params.roomId)
    },
    goClose () {
      this.forInactive = false
      this.forActive = false
    },
    goInactive () {
      this.forSave = true
      this.$http.get(
        API.InactiveById,
        { params: { id: this.$route.params.teamId } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        this.forSave = false
        if (response.body === 'error') {
          this.openPopup('请重新登录!', 'report_problem', 'orange')
          window.location.href = '/'
        } else if (response.body === 'OK') {
          this.edit = true
          this.save = false
          if (this.state.toString() === '1') {
            this.inactive = true
            this.active = false
          } else {
            this.inactive = false
            this.active = true
          }
          this.openPopup('删除成功!', 'check_circle', 'green')
          setTimeout(() => { this.$router.push({ path: '/teamList' }) }, 1000)
        } else if (response.body === '要删除的班级不存在!') {
          this.edit = true
          this.save = false
          if (this.state.toString() === '1') {
            this.inactive = true
            this.active = false
          } else {
            this.inactive = false
            this.active = true
          }
          this.openPopup(response.body, 'report_problem', 'orange')
          setTimeout(() => { this.$router.push({ path: '/teamList' }) }, 1000)
        } else {
          this.openPopup(response.body, 'report_problem', 'orange')
          setTimeout(() => { this.$router.push({ path: '/teamList' }) }, 1000)
        }
      }, (response) => {
        this.forSave = false
        this.openPopup('服务器内部错误!', 'error', 'red')
      })
    },
    goActive () {
      this.forSave = true
      this.$http.get(
        API.ActiveById,
        { params: { id: this.$route.params.teamId } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        this.forSave = false
        if (response.body === 'error') {
          this.openPopup('请重新登录!', 'report_problem', 'orange')
          window.location.href = '/'
        } else if (response.body === 'OK') {
          this.edit = true
          this.save = false
          if (this.state.toString() === '1') {
            this.inactive = true
            this.active = false
          } else {
            this.inactive = false
            this.active = true
          }
          this.openPopup('激活成功!', 'check_circle', 'green')
          setTimeout(() => { this.$router.push({ path: '/teamList' }) }, 1000)
        } else if (response.body === '要激活的社团不存在!') {
          this.edit = true
          this.save = false
          if (this.state.toString() === '1') {
            this.inactive = true
            this.active = false
          } else {
            this.inactive = false
            this.active = true
          }
          this.openPopup(response.body, 'report_problem', 'orange')
          setTimeout(() => { this.$router.push({ path: '/teamList' }) }, 1000)
        } else {
          this.openPopup(response.body, 'report_problem', 'orange')
          setTimeout(() => { this.$router.push({ path: '/teamList' }) }, 1000)
        }
      }, (response) => {
        this.forSave = false
        this.openPopup('服务器内部错误!', 'error', 'red')
      })
    },
    goSave () {
      this.forSave = true
      this.$http.get(
        API.Edit,
        { params: {
          id: this.$route.params.teamId,
          name: this.name,
          teamTeacher: this.teamTeacher
        } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        this.forSave = false
        if (response.body === 'error') {
          this.openPopup('请重新登录!', 'report_problem', 'orange')
          window.location.href = '/'
        } else if (response.body === 'OK') {
          this.openPopup('修改成功！', 'check_circle', 'green')
          setTimeout(() => { this.$router.push({ path: '/teamList' }) }, 1000)
        } else {
          this.openPopup(response.body, 'report_problem', 'orange')
        }
      }, (response) => {
        this.forSave = false
        this.openPopup('服务器内部错误!', 'error', 'red')
      })
    },
    fetchData (teamId) {
      this.$http.get(
        API.GetById,
        { params: { id: teamId } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
      ).then((response) => {
        this.team = response.body
        this.name = this.team.name
        this.state = this.team.state
        if (this.team.state.toString() === '1') {
          this.inactive = true
          this.active = false
        } else {
          this.inactive = false
          this.active = true
        }
        this.deleteTitle = '确认要注销' + this.name + '吗?'
        this.resaveTitle = '确认要激活' + this.name + '吗?'
      }, (response) => {
      })
    },
    getTeamTeacher (teamId) {
      this.$http.get(
        API.GetTeamTeacher,
        { params: { team: teamId } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
      ).then((response) => {
        /* eslint-disable no-eval  */
        this.teamTeacher = eval('(' + response.body + ')').teamTeachers
      }, (response) => {
      })
    },
    checkName (value) {
      if (value === null || value === undefined || value === '') {
        this.nameErrorText = '社团名称为必填项!'
        this.nameErrorColor = 'orange'
      } else {
        this.$http.get(
          API.CheckNameForEdit,
          { params: {
            id: this.$route.params.teamId,
            name: value
          } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          if (response.body === 'error') {
            this.openPopup('请重新登录!', 'report_problem', 'orange')
            window.location.href = '/'
          } else if (response.body === 'OK') {
            this.nameErrorText = ''
            this.nameErrorColor = 'green'
          } else {
            this.nameErrorText = response.body
            this.nameErrorColor = 'red'
          }
        }, (response) => {
          this.openPopup('服务器内部错误!', 'error', 'red')
        })
      }
    }
  }
}
</script>
<style lang="css">
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
  .flex-demo {
    height: 170px;
    text-align: center;
    line-height: 32px;
  }
</style>
