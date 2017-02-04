<template>
  <div class="TeamworkAdd">
    <mu-appbar title="输入/选择后发送消息">
      <mu-icon-button icon='reply' slot="right" @click="gorReply"/>
    </mu-appbar>
    <mu-text-field label="内容" underlineShow="false" v-model="content" :errorColor="contentErrorColor" :errorText="contentErrorText" @input="checkContent" fullWidth labelFloat icon="note" multiLine :rows="9" :rowsMax="12" :maxLength="300"/><br/>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-flat-button :label="teamName" @click="openTeam=true" :icon="teamIcon" :backgroundColor="teamBack" color="#FFFFFF"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-flat-button :label="studentName" @click="openStudent=true" :icon="studentIcon" :backgroundColor="studentBack" color="#FFFFFF"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-dialog :open="forSave" title="正在保存并发送" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="cached" @click="goReset" backgroundColor="orange"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="send" @click="forSaved=true" :disabled="saveAble" backgroundColor="green"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-dialog :open="forSaved" title="确定保存并发送消息?" @close="goClose">
      <mu-flat-button label="取消" @click="goClose" />
      <mu-flat-button label="确定" @click="goSave" secondary/>
    </mu-dialog>
    <mu-drawer right :open="openTeam" docked="false">
      <mu-appbar title="请选择所属社团"/>
      <mu-list :value="team_id" @itemClick="teamChange">
        <mu-list-item v-for="team in teams" :title="team.name" :value="team.id">
          <mu-icon slot="left" :size="40" value="account_balance" color="#9c27b0"/>
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-drawer right :open="openStudent" docked="false">
      <mu-appbar title="请选择发送范围" @click.native="closeStudent">
        <mu-icon-button icon='done' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item title="全选" @click.native="student_id=studentAll">
          <mu-icon slot="left" value="supervisor_account" :size="40"/>
        </mu-list-item>
        <mu-list-item title="清空" @click.native="student_id=[]">
          <mu-icon slot="left" value="delete_forever" :size="40"/>
        </mu-list-item>
        <mu-list-item v-for="student in students" :title="student.name" :describeText="student.number">
          <mu-icon v-if="student.sex.toString() === '1'" slot="left" :size="40" color="#00bcd4" value="account_box"/>
          <mu-icon v-if="student.sex.toString() === '2'" slot="left" :size="40" color="#e91e63" value="account_circle" />
          <mu-checkbox v-model="student_id" label="" labelLeft :nativeValue="student.id" slot="right"/>
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-popup position="bottom" :overlay="false" popupClass="popup-bottom" :open="bottomPopup">
      <mu-icon :value="icon" :size="36" :color="color"/>&nbsp;{{ message }}
    </mu-popup>
  </div>
</template>

<script>
import * as API from './TeamworkAPI.js'
export default {
  name: 'TeamworkAdd',
  data () {
    return {
      bottomPopup: false,
      forSave: false,
      forSaved: false,
      icon: '',
      color: '',
      team_id: '',
      student_id: [],
      teams: [],
      students: [],
      studentAll: '',
      teamName: '社团',
      studentName: '发送范围',
      teamAble: false,
      studentAble: true,
      openTeam: false,
      openStudent: false,
      teamIcon: 'looks_one',
      studentIcon: 'looks_two',
      teamBack: '#66CCCC',
      studentBack: '#66CCCC',
      content: '',
      message: '',
      contentErrorText: '',
      contentErrorColor: '',
      saveAble: true
    }
  },
  created: function () {
    this.$http.get(
      API.TeacherTeamList,
      { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
    ).then((response) => {
      this.teams = response.body
      this.$http.get(
        API.TeacherTeamFirst,
        { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
      ).then((response) => {
        this.team_id = response.body
      }, (response) => {
        this.openPopup('服务器内部错误！', 'error', 'red')
      })
    }, (response) => {
      this.openPopup('服务器内部错误！', 'error', 'red')
    })
  },
  computed: {
    saveAble: function () {
      if (this.contentErrorText.toString() === 'OK' && this.student_id.toString !== '') {
        return false
      } else {
        return true
      }
    },
    teamBack: function () {
      if (this.team_id.toString() !== '') {
        return '#9999CC'
      } else {
        return '#66CCCC'
      }
    },
    studentBack: function () {
      if (this.student_id.toString() !== '') {
        return '#9999CC'
      } else {
        return '#66CCCC'
      }
    },
    teamIcon: function () {
      if (this.team_id.toString() !== '') {
        return 'filter_1'
      } else {
        return 'looks_one'
      }
    },
    studentIcon: function () {
      if (this.student_id.toString() !== '') {
        return 'filter_2'
      } else {
        return 'looks_two'
      }
    }
  },
  watch: {
    team_id: function (val) {
      if (val.toString() !== '') {
        this.$http.get(
          API.GetTeamName,
          { params: { id: val } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
        ).then((response) => {
          this.teamName = response.body
        }, (response) => {
          this.openPopup('服务器内部错误！', 'error', 'red')
        })
        this.$http.get(
          API.StudentList,
          { params: { id: val } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
        ).then((response) => {
          this.students = response.body
        }, (response) => {
          this.openPopup('服务器内部错误！', 'error', 'red')
        })
        this.$http.get(
          API.StudentCode,
          { params: { id: val } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
        ).then((response) => {
          this.studentAll = response.body
          this.student_id = this.studentAll
        }, (response) => {
          this.openPopup('服务器内部错误！', 'error', 'red')
        })
        this.student_id = ''
        this.studentName = '发送范围'
      }
    }
  },
  methods: {
    goClose () {
      this.forSaved = false
    },
    gorReply () {
      this.$router.push({ path: '/teamworkList' })
    },
    openPopup (message, icon, color) {
      this.message = message
      this.icon = icon
      this.color = color
      this.bottomPopup = true
      setTimeout(() => { this.bottomPopup = false }, 1500)
    },
    goReset () {
      this.content = ''
      this.saved = false
      this.contentErrorText = ''
      this.contentErrorColor = ''
    },
    teamChange (val) {
      this.team_id = val.value
      this.teamName = val.title
      this.openTeam = false
    },
    closeStudent () {
      this.openStudent = false
      if (this.student_id.toString() !== '') {
        this.studentName = '发送范围已设置'
      } else {
        this.studentName = '发送范围未设置'
      }
    },
    checkContent (value) {
      if (value === null || value === undefined || value === '') {
        this.contentErrorText = '内容为必填项!'
        this.contentErrorColor = 'orange'
      } else if (value.length > 300) {
        this.contentErrorText = '内容不得超过300字符'
        this.contentErrorColor = 'orange'
      } else {
        this.contentErrorText = 'OK'
        this.contentErrorColor = 'green'
      }
    },
    goSave () {
      this.forSave = true
      this.forSaved = false
      this.$http.get(
        API.Save,
        { params: {
          content: this.content,
          team_id: this.team_id,
          student_id: this.student_id
        } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        this.forSave = false
        if (response.body === 'error') {
          this.openPopup('请重新登录!', 'report_problem', 'orange')
          window.location.href = '/'
        } else if (response.body === 'OK') {
          this.openPopup('保存成功！', 'check_circle', 'green')
          setTimeout(() => { this.$router.push({ path: '/teamworkList' }) }, 1000)
        } else {
          this.openPopup(response.body, 'report_problem', 'orange')
        }
        this.forSave = false
      }, (response) => {
        this.forSave = false
        this.openPopup('服务器内部错误!', 'error', 'red')
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
    height: 50px;
    text-align: center;
    line-height: 32px;
  }
</style>
