<template>
  <div class="StudentList">
    <mu-appbar title="">
      <mu-icon-button icon='menu' slot="left" @click="openMenu"/>
      <mu-text-field icon="search" class="appbar-search-field" hintText="请输入关键词" @input="query" :value="queryString"/>
    </mu-appbar>
    <mu-raised-button :label="teamName" icon="cached" fullWidth labelPosition="after" @click="openTeam=true"/>
    <mu-list>
      <mu-list-item v-for="student in students" :value="student.id" :title="student.name" :describeText="student.number" :afterText="student.code" @click="look(student.id)">
        <mu-icon v-if="student.sex.toString() === '1'" slot="left" :size="40" value="account_box" color="Cyan"/>
        <mu-icon v-if="student.sex.toString() === '2'" slot="left" :size="40" value="account_circle" color="pink"/>
      </mu-list-item>
    </mu-list>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-flat-button v-if="before" label="上一页" icon="navigate_before" @click="pageBefore" />
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <div v-if="chip">
          {{pageCurrent}}/{{pageTotal}}
        </div>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-flat-button v-if="next" label="下一页" labelPosition="before" icon="navigate_next" @click="pageNext" />
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-drawer right :open="openTeam" docked="false">
      <mu-appbar title="请选择社团" @click.native="openTeam=false">
        <mu-icon-button icon='close' slot="right"/>
      </mu-appbar>
      <mu-list :value="room_id" @itemClick="teamChange">
        <mu-list-item v-for="team in teams" :title="team.name">
          <mu-icon slot="left" :size="40" value="store" color="#9c27b0"/>
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-dialog :open="forRead" title="正在读取" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <menuList :open="open" v-on:closeMenu="closeMenu"></menuList>
    <mu-popup position="bottom" :overlay="false" popupClass="popup-bottom" :open="bottomPopup">
      <mu-icon :value="icon" :size="36" :color="color"/>&nbsp;{{ message }}
    </mu-popup>
  </div>
</template>

<script>
  import * as API from './StudentAPI.js'
  import MenuList from '../Menu/MenuList'
  export default {
    name: 'StudentList',
    components: {
      'menuList': MenuList
    },
    data () {
      return {
        before: false,
        next: false,
        chip: false,
        open: false,
        forRead: true,
        bottomPopup: false,
        openTeam: false,
        teamName: '',
        team_id: '0',
        teams: [],
        icon: '',
        color: '',
        message: '',
        queryString: '',
        students: [],
        pageTotal: '',
        pageSize: '7',
        pageCurrent: ''
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
          if (response.body.toString() !== '0') {
            this.team_id = response.body
            this.$http.get(
              API.GetTeamName,
              { params: { id: this.team_id } },
              { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
            ).then((response) => {
              this.teamName = response.body
            }, (response) => {
              this.openPopup('服务器内部错误！', 'error', 'red')
            })
          } else {
            this.teamName = '无可选社团'
          }
          this.queryString = this.$store.state.queryString
          this.pageCurrent = this.$store.state.pageCurrent
          if (this.team_id.toString() !== '0') {
            this.studentQuery(this.queryString, this.pageCurrent, this.pageSize, this.team_id)
            this.studentTotal(this.queryString, this.pageSize, this.team_id)
          } else {
            this.forRead = false
          }
        }, (response) => {
          this.openPopup('服务器内部错误！', 'error', 'red')
        })
      }, (response) => {
        this.openPopup('服务器内部错误！', 'error', 'red')
      })
    },
    methods: {
      openMenu () {
        this.open = true
      },
      closeMenu () {
        this.open = false
      },
      openPopup (message, icon, color) {
        this.message = message
        this.icon = icon
        this.color = color
        this.bottomPopup = true
        setTimeout(() => { this.bottomPopup = false }, 1500)
      },
      teamChange (val) {
        this.team_id = val.value
        this.teamName = val.title
        this.openTeam = false
        this.forRead = true
        this.studentQuery(this.queryString, this.pageCurrent, this.pageSize, this.team_id)
        this.studentTotal(this.queryString, this.pageSize, this.team_id)
      },
      studentQuery (queryString, pageCurrent, pageSize, teamId) {
        this.$http.get(
          API.QueryByName,
          { params:
          {
            queryString: queryString,
            pageCurrent: pageCurrent,
            pageSize: pageSize,
            teamId: teamId
          } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
        ).then((response) => {
          this.students = response.body
          this.pageCurrent = pageCurrent
          this.pageCurrent.toString() === '1' ? this.before = false : this.before = true
          this.forRead = false
        }, (response) => {
          this.openPopup('服务器内部错误！', 'error', 'red')
        })
      },
      studentTotal (queryString, pageSize, teamId) {
        this.$http.get(
          API.TotalByName,
          { params: {
            queryString: queryString,
            pageSize: pageSize,
            teamId: teamId
          } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          this.pageTotal = response.body
          this.pageTotal === '0' ? this.chip = false : this.chip = true
          this.pageTotal === '1' || this.pageTotal === '0' || this.pageTotal.toString() === this.$store.state.pageCurrent.toString() ? this.next = false : this.next = true
        }, (response) => {
          this.openPopup('服务器内部错误！', 'error', 'red')
        })
      },
      query (value) {
        this.queryString = value
        this.pageCurrent = '1'
        this.$store.commit('save', {
          queryString: this.queryString,
          pageCurrent: this.pageCurrent,
          team_id: this.team_id
        })
        this.studentQuery(this.queryString, this.pageCurrent, this.pageSize, this.team_id)
        this.studentTotal(this.queryString, this.pageSize, this.team_id)
        this.before = false
      },
      look (studentId) {
        this.$router.push({ path: '/studentLook/' + studentId })
        this.$store.commit('save', {
          queryString: this.queryString,
          pageCurrent: this.pageCurrent,
          team_id: this.team_id
        })
      },
      pageBefore () {
        this.pageCurrent--
        this.pageCurrent.toString() === '1' ? this.before = false : this.before = true
        this.next = true
        this.studentQuery(this.queryString, this.pageCurrent, this.pageSize, this.team_id)
      },
      pageNext () {
        this.pageCurrent++
        this.pageCurrent.toString() === this.pageTotal ? this.next = false : this.next = true
        this.before = true
        this.studentQuery(this.queryString, this.pageCurrent, this.pageSize, this.team_id)
      }
    }
  }
</script>
<style lang="less">
  .appbar-search-field{
    color: #FFF;
    margin-bottom: 0;
    &.focus-state {
      color: #FFF;
    }
    .mu-text-field-hint {
      color: fade(#FFF, 54%);
    }
    .mu-text-field-input {
      color: #FFF;
    }
    .mu-text-field-focus-line {
      background-color: #FFF;
    }
  }
  .flex-demo {
    height: 70px;
    text-align: center;
    line-height: 32px;
  }
</style>
