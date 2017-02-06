<template>
  <div class="StudentAdd">
    <mu-appbar title="请核实后输入以下信息">
      <mu-icon-button icon='reply' slot="right" @click="goReply"/>
    </mu-appbar>
    <mu-text-field v-model="name" label="姓名" icon="comment" :errorColor="nameErrorColor" :errorText="nameErrorText" @input="checkName" fullWidth labelFloat/><br/>
    <mu-text-field v-model="number" label="证件号码" icon="comment" :errorColor="numberErrorColor" :errorText="numberErrorText" @input="checkNumber" fullWidth labelFloat maxLength="18"/><br/>
    <mu-text-field v-model="code" label="学籍号码" icon="comment" :errorColor="codeErrorColor" :errorText="codeErrorText" @input="checkCode" fullWidth labelFloat maxLength="15"/><br/>
    <mu-text-field v-model="address" label="住址" icon="store_mall_directory" :errorColor="addressErrorColor" :errorText="addressErrorText" @input="checkAddress" fullWidth labelFloat/><br/>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-flat-button :label="roomName" @click="openRoom=true" :icon="roomIcon" :backgroundColor="roomBack" color="#FFFFFF"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-flat-button :label="teamName" @click="openTeam=true" :icon="teamIcon" :backgroundColor="teamBack" color="#FFFFFF"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-popup position="bottom" :overlay="false" popupClass="popup-bottom" :open="bottomPopup">
      <mu-icon :value="icon" :size="36" :color="color"/>&nbsp;{{ message }}
    </mu-popup>
    <mu-dialog :open="forSave" title="正在保存" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-dialog :open="forRead" title="正在读取" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-drawer right :open="openRoom" docked="false">
      <mu-appbar title="请选择所属班级"/>
      <mu-list :value="room_id" @itemClick="roomChange">
        <mu-list-item title="班级" value="0">
          <mu-icon slot="left" value="store" :size="40"/>
        </mu-list-item>
        <mu-list-item v-for="room in rooms" :title="room.name" :value="room.id">
          <mu-icon slot="left" :size="40" value="store" color="#9c27b0"/>
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-drawer right :open="openTeam" docked="false">
      <mu-appbar title="请选择所属社团" />
      <mu-list :value="team_id" @itemClick="teamChange">
        <mu-list-item title="社团" value="0">
          <mu-icon slot="left" value="account_balance" :size="40"/>
        </mu-list-item>
        <mu-list-item v-for="team in teams" :title="team.name" :value="team.id">
          <mu-icon slot="left" :size="40" value="account_balance" color="#673ab7"/>
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
import * as API from './StudentAPI.js'
export default {
  name: 'StudentAdd',
  data () {
    return {
      bottomPopup: false,
      forSave: false,
      forRead: true,
      icon: '',
      color: '',
      name: '',
      number: '',
      code: '',
      address: '',
      room_id: '0',
      team_id: '0',
      roomName: '班级',
      teamName: '社团',
      message: '',
      openRoom: false,
      openTeam: false,
      roomIcon: 'bookmark_border',
      teamIcon: 'bookmark_border',
      roomBack: '#66CCCC',
      teamBack: '#66CCCC',
      nameErrorText: '',
      nameErrorColor: '',
      numberErrorText: '',
      numberErrorColor: '',
      codeErrorText: '',
      codeErrorColor: '',
      addressErrorText: '',
      addressErrorColor: '',
      rooms: [],
      teams: [],
      saveAble: true
    }
  },
  created: function () {
    this.$http.get(
      API.RoomList,
      { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
    ).then((response) => {
      this.rooms = response.body
    }, (response) => {
      this.openPopup('服务器内部错误！', 'error', 'red')
    })
    this.$http.get(
      API.TeamList,
      { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
    ).then((response) => {
      this.teams = response.body
      this.forRead = false
    }, (response) => {
      this.openPopup('服务器内部错误！', 'error', 'red')
    })
  },
  computed: {
    saveAble: function () {
      if (this.nameErrorText.toString() === 'OK' && this.numberErrorText.toString() === 'OK' && this.codeErrorText.toString() === 'OK' && this.addressErrorText.toString() === 'OK') {
        return false
      } else {
        return true
      }
    },
    roomBack: function () {
      if (this.room_id.toString() !== '0') {
        return '#9999CC'
      } else {
        return '#66CCCC'
      }
    },
    teamBack: function () {
      if (this.team_id.toString() !== '0') {
        return '#9999CC'
      } else {
        return '#66CCCC'
      }
    },
    roomIcon: function () {
      if (this.room_id.toString() !== '0') {
        return 'bookmark'
      } else {
        return 'bookmark_border'
      }
    },
    teamIcon: function () {
      if (this.team_id.toString() !== '0') {
        return 'bookmark'
      } else {
        return 'bookmark_border'
      }
    }
  },
  methods: {
    goReply () {
      this.$router.push({ path: '/studentList' })
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
      this.number = ''
      this.numberErrorColor = ''
      this.code = ''
      this.codeErrorColor = ''
      this.address = ''
      this.addressErrorColor = ''
      this.room_id = '0'
      this.team_id = '0'
      this.roomName = '班级'
      this.teamName = '社团'
    },
    roomChange (val) {
      this.room_id = val.value
      this.roomName = val.title
      this.openRoom = false
    },
    teamChange (val) {
      this.team_id = val.value
      this.teamName = val.title
      this.openTeam = false
    },
    checkName (value) {
      if (value === null || value === undefined || value === '') {
        this.nameErrorText = '姓名为必填项!'
        this.nameErrorColor = 'orange'
      } else if (!/^[\u4e00-\u9fa5]{2,}$/.test(value)) {
        this.nameErrorText = '姓名应为2个以上汉字'
        this.nameErrorColor = 'orange'
      } else {
        this.nameErrorText = 'OK'
        this.nameErrorColor = 'green'
      }
    },
    checkNumber (value) {
      if (value === null || value === undefined || value === '') {
        this.nameErrorText = '证件号码为必填项!'
        this.nameErrorColor = 'orange'
      } else if (!/\d{17}[0-9,X]/.test(value)) {
        this.numberErrorText = '证件号码应为18位字符'
        this.numberErrorColor = 'orange'
      } else {
        this.$http.get(
          API.CheckNumberForNew,
          { params: { number: value } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          if (response.body === 'error') {
            this.openPopup('请重新登录!', 'report_problem', 'orange')
            window.location.href = '/'
          } else if (response.body === 'OK') {
            this.numberErrorText = 'OK'
            this.numberErrorColor = 'green'
          } else {
            this.numberErrorText = response.body
            this.numberErrorColor = 'red'
          }
        }, (response) => {
          this.openPopup('服务器内部错误！', 'error', 'red')
        })
      }
    },
    checkCode (value) {
      if (value === null || value === undefined || value === '') {
        this.codeErrorText = '学籍号码为必填项!'
        this.codeErrorColor = 'orange'
      } else {
        this.$http.get(
          API.CheckCodeForNew,
          { params: { code: value } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          if (response.body === 'error') {
            this.openPopup('请重新登录!', 'report_problem', 'orange')
            window.location.href = '/'
          } else if (response.body === 'OK') {
            this.codeErrorText = 'OK'
            this.codeErrorColor = 'green'
          } else {
            this.codeErrorText = response.body
            this.codeErrorColor = 'red'
          }
        }, (response) => {
          this.openPopup('服务器内部错误！', 'error', 'red')
        })
      }
    },
    checkAddress (value) {
      if (value === null || value === undefined || value === '') {
        this.addressErrorText = '住址为必填项!'
        this.addressErrorColor = 'orange'
      } else {
        this.addressErrorText = 'OK'
        this.addressErrorColor = 'green'
      }
    },
    goSave () {
      this.forSave = true
      this.$http.get(
        API.Save,
        { params: {
          name: this.name,
          number: this.number,
          code: this.code,
          address: this.address,
          room_id: this.room_id,
          team_id: this.team_id
        } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        this.forSave = false
        if (response.body === 'error') {
          this.openPopup('请重新登录!', 'report_problem', 'orange')
          window.location.href = '/'
        } else if (response.body === 'OK') {
          this.openPopup('保存成功！', 'check_circle', 'green')
          setTimeout(() => { this.$router.push({ path: '/studentList' }) }, 1000)
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
    height: 70px;
    text-align: center;
    line-height: 32px;
  }
</style>
