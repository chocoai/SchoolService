<template>
  <div class="TeamAdd">
    <mu-appbar title="请核实后输入以下信息">
      <mu-icon-button icon='reply' slot="right" @click="goReply"/>
    </mu-appbar>
    <mu-text-field v-model="name" label="社团名称" icon="comment" :errorColor="nameErrorColor" :errorText="nameErrorText" @input="checkName" fullWidth labelFloat/><br/>
    <mu-sub-header>管理教师：</mu-sub-header>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-flat-button :label="teamName" @click="openTeamTeacher=true" :icon="teamIcon" :backgroundColor="teamBack" color="#FFFFFF"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-popup position="bottom" :overlay="false" popupClass="popup-bottom" :open="bottomPopup">
      <mu-icon :value="icon" :size="36" :color="color"/>&nbsp;{{ message }}
    </mu-popup>
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
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="cached" @click="goReset" backgroundColor="orange"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="done" @click="goSave" :disabled="saveAble" backgroundColor="green"/>
      </mu-flexbox-item>
    </mu-flexbox>
  </div>
</template>

<script>
import * as API from './TeamAPI.js'
export default {
  name: 'TeamAdd',
  data () {
    return {
      bottomPopup: false,
      forSave: false,
      icon: '',
      color: '',
      name: '',
      message: '',
      nameErrorText: '',
      nameErrorColor: '',
      teamIcon: 'bookmark_border',
      teamBack: '#66CCCC',
      teamName: '未设置',
      openTeamTeacher: false,
      teamTeacher: [],
      teachers: [],
      saveAble: true
    }
  },
  created: function () {
    this.$http.get(
      API.TeacherList,
      { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
    ).then((response) => {
      this.teachers = response.body
    }, (response) => {
      this.openPopup('服务器内部错误！', 'error', 'red')
    })
  },
  computed: {
    saveAble: function () {
      if (this.nameErrorText.toString() === 'OK') {
        return false
      } else {
        return true
      }
    },
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
  methods: {
    goReply () {
      this.$router.push({ path: '/teamList' })
    },
    openPopup (message, icon, color) {
      this.message = message
      this.icon = icon
      this.color = color
      this.bottomPopup = true
      setTimeout(() => { this.bottomPopup = false }, 1500)
    },
    goReset () {
      this.name = ''
      this.nameErrorColor = ''
      this.teamIcon = 'bookmark_border'
      this.teamBack = '#66CCCC'
      this.teamName = '未设置'
      this.teamTeacher = []
    },
    checkName (value) {
      if (value === null || value === undefined || value === '') {
        this.nameErrorText = '社团名称为必填项!'
        this.nameErrorColor = 'orange'
      } else {
        this.$http.get(
          API.CheckNameForNew,
          { params: { name: value } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          if (response.body === 'error') {
            this.openPopup('请重新登录!', 'report_problem', 'orange')
            window.location.href = '/'
          } else if (response.body === 'OK') {
            this.nameErrorText = 'OK'
            this.nameErrorColor = 'green'
          } else {
            this.nameErrorText = response.body
            this.nameErrorColor = 'red'
          }
        }, (response) => {
          this.openPopup('服务器内部错误！', 'error', 'red')
        })
      }
    },
    goSave () {
      this.forSave = true
      this.$http.get(
        API.Save,
        { params: {
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
          this.openPopup('保存成功！', 'check_circle', 'green')
          setTimeout(() => { this.$router.push({ path: '/teamList' }) }, 1000)
        } else {
          this.openPopup(response.body, 'report_problem', 'orange')
        }
      }, (response) => {
        this.forSave = false
        this.openPopup('服务器内部错误！', 'error', 'red')
      })
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
