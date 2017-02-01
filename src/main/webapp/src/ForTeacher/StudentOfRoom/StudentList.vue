<template>
  <div class="StudentList">
    <mu-appbar title="">
      <mu-icon-button icon='menu' slot="left" @click="openMenu"/>
      <mu-text-field icon="search" class="appbar-search-field" hintText="请输入关键词" @input="query" :value="queryString"/>
    </mu-appbar>
    <mu-raised-button :label="roomName" icon="cached" fullWidth labelPosition="after" @click="openRoom=true"/>
    <mu-list>
      <mu-list-item v-for="student in students" :value="student.id" :title="student.name" :describeText="student.number" @click="look(student.id)">
        <mu-icon v-if="student.state.toString() === '1' && student.sex.toString() === '1'" slot="left" :size="40" value="account_box" color="Cyan"/>
        <mu-icon v-if="student.state.toString() === '1' && student.sex.toString() === '2'" slot="left" :size="40" value="account_circle" color="pink"/>
        <mu-icon v-if="student.state.toString() === '2' && student.sex.toString() === '1'" slot="left" :size="40" value="account_box" color="#b2ebf2"/>
        <mu-icon v-if="student.state.toString() === '2' && student.sex.toString() === '2'" slot="left" :size="40" value="account_circle" color="#f8bbd0"/>
        <mu-avatar icon="label_outline" slot="rightAvatar" :size="30"/>
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
    <mu-drawer right :open="openRoom" docked="false">
      <mu-appbar title="请选择班级" @click.native="closeRoom">
        <mu-icon-button icon='done' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item v-for="room in rooms" :title="room.name">
          <mu-icon v-if="room.state.toString() === '1'" slot="left" :size="40" value="store" color="#9c27b0"/>
          <mu-icon v-if="room.state.toString() === '2'" slot="left" :size="40" value="store" color="#ce93d8"/>
          <mu-radio v-model="room_id" label="" labelLeft :nativeValue="room.id" slot="right" iconClass="color: #215E21"/>
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
        openRoom: false,
        roomName: '',
        room_id: '',
        rooms: [],
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
        API.TeacherRoomList,
        { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
      ).then((response) => {
        this.rooms = response.body
        this.$http.get(
          API.TeacherRoomFirst,
          { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
        ).then((response) => {
          if (response.body.toString() !== '0') {
            this.room_id = response.body
            this.$http.get(
              API.GetRoomName,
              { params: { id: this.room_id } },
              { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
            ).then((response) => {
              this.roomName = response.body
            }, (response) => {
              this.openPopup('服务器内部错误！', 'error', 'red')
            })
          } else {
            this.roomName = '班级未选择'
          }
          this.queryString = this.$store.state.queryString
          this.pageCurrent = this.$store.state.pageCurrent
          if (this.room_id.toString() !== '') {
            this.studentQuery(this.queryString, this.pageCurrent, this.pageSize, this.room_id)
            this.studentTotal(this.queryString, this.pageSize, this.room_id)
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
      closeRoom () {
        this.openRoom = false
        this.forRead = true
        if (this.room_id.toString() !== '') {
          this.$http.get(
            API.GetRoomName,
            { params: { id: this.room_id } },
            { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
          ).then((response) => {
            this.roomName = response.body
            this.studentQuery(this.queryString, this.pageCurrent, this.pageSize, this.room_id)
            this.studentTotal(this.queryString, this.pageSize, this.room_id)
          }, (response) => {
            this.openPopup('服务器内部错误！', 'error', 'red')
          })
        } else {
          this.roomName = '班级未选择'
          this.forRead = false
        }
      },
      studentQuery (queryString, pageCurrent, pageSize, roomId) {
        this.$http.get(
          API.QueryByName,
          { params:
          {
            queryString: queryString,
            pageCurrent: pageCurrent,
            pageSize: pageSize,
            roomId: roomId
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
      studentTotal (queryString, pageSize, roomId) {
        this.$http.get(
          API.TotalByName,
          { params: {
            queryString: queryString,
            pageSize: pageSize,
            roomId: roomId
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
          room_id: this.room_id
        })
        this.studentQuery(this.queryString, this.pageCurrent, this.pageSize, this.room_id)
        this.studentTotal(this.queryString, this.pageSize, this.room_id)
        this.before = false
      },
      look (studentId) {
        this.$router.push({ path: '/studentLook/' + studentId })
        this.$store.commit('save', {
          queryString: this.queryString,
          pageCurrent: this.pageCurrent,
          room_id: this.room_id
        })
      },
      pageBefore () {
        this.pageCurrent--
        this.pageCurrent.toString() === '1' ? this.before = false : this.before = true
        this.next = true
        this.studentQuery(this.queryString, this.pageCurrent, this.pageSize)
      },
      pageNext () {
        this.pageCurrent++
        this.pageCurrent.toString() === this.pageTotal ? this.next = false : this.next = true
        this.before = true
        this.studentQuery(this.queryString, this.pageCurrent, this.pageSize)
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
