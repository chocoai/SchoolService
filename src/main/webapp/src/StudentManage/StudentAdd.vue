<template>
  <div class="StudentAdd">
    <mu-appbar title="请核实后输入以下信息">
      <mu-icon-button icon='reply' slot="right" @click="reply"/>
    </mu-appbar>
    <mu-text-field v-model="name" label="姓名" icon="comment" :errorColor="nameErrorColor" :errorText="nameErrorText" @input="checkName" fullWidth labelFloat/><br/>
    <mu-text-field v-model="number" label="证件号码" icon="comment" :errorColor="numberErrorColor" :errorText="numberErrorText" @input="checkNumber" fullWidth labelFloat maxLength="18"/><br/>
    <mu-text-field v-model="code" label="学籍号码" icon="comment" :errorColor="codeErrorColor" :errorText="codeErrorText" @input="checkCode" fullWidth labelFloat maxLength="15"/><br/>
    <mu-text-field v-model="address" label="住址" icon="store_mall_directory" fullWidth labelFloat/><br/>
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
    <mu-drawer right :open="openRoom" docked="false">
      <mu-appbar title="请选择所属班级" @click.native="closeRoom">
        <mu-icon-button icon='done' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item title="清空" @click.native="room_id=''">
          <mu-icon slot="left" value="delete_forever" :size="40"/>
        </mu-list-item>
        <mu-list-item v-for="room in rooms" :title="room.name">
          <mu-icon v-if="room.state.toString() === '1'" slot="left" :size="40" value="store" color="#9c27b0"/>
          <mu-icon v-if="room.state.toString() === '2'" slot="left" :size="40" value="store" color="#e1bee7"/>
          <mu-radio v-model="room_id" label="" labelLeft :nativeValue="room.id" uncheckIcon="favorite_border" checkedIcon="favorite" slot="right" iconClass="color: #215E21"/>
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-drawer right :open="openTeam" docked="false">
      <mu-appbar title="请选择所属社团" @click.native="closeTeam">
        <mu-icon-button icon='done' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item title="清空" @click.native="team_id=''">
          <mu-icon slot="left" value="delete_forever" :size="40"/>
        </mu-list-item>
        <mu-list-item v-for="team in teams" :title="team.name">
          <mu-icon v-if="team.state.toString() === '1'" slot="left" :size="40" value="store" color="#673ab7"/>
          <mu-icon v-if="team.state.toString() === '2'" slot="left" :size="40" value="store" color="#d1c4e9"/>
          <mu-radio v-model="team_id" label="" labelLeft :nativeValue="team.id" uncheckIcon="favorite_border" checkedIcon="favorite" slot="right" />
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="cached" @click="reset" backgroundColor="orange"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="done" @click="save" backgroundColor="green"/>
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
      icon: '',
      color: '',
      name: '',
      number: '',
      code: '',
      address: '',
      room_id: '',
      team_id: '',
      roomName: '所属班级',
      teamName: '所属社团',
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
      rooms: [],
      teams: []
    }
  },
  created: function () {
    this.$http.get(
      API.RoomList,
      {
        headers:
        {
          'X-Requested-With': 'XMLHttpRequest'
        },
        emulateJSON: true
      }
    ).then((response) => {
      this.rooms = response.body
    }, (response) => {
      this.openPopup('服务器内部错误！', 'error', 'red')
    })
    this.$http.get(
      API.TeamList,
      {
        headers:
        {
          'X-Requested-With': 'XMLHttpRequest'
        },
        emulateJSON: true
      }
    ).then((response) => {
      this.teams = response.body
    }, (response) => {
      this.openPopup('服务器内部错误！', 'error', 'red')
    })
  },
  computed: {
    roomBack: function () {
      if (this.room_id.toString() !== '') {
        return '#9999CC'
      } else {
        return '#66CCCC'
      }
    },
    teamBack: function () {
      if (this.team_id.toString() !== '') {
        return '#9999CC'
      } else {
        return '#66CCCC'
      }
    },
    roomIcon: function () {
      if (this.room_id.toString() !== '') {
        return 'bookmark'
      } else {
        return 'bookmark_border'
      }
    },
    teamIcon: function () {
      if (this.team_id.toString() !== '') {
        return 'bookmark'
      } else {
        return 'bookmark_border'
      }
    }
  },
  methods: {
    reply () {
      this.$router.push({ path: '/studentList' })
    },
    openPopup (message, icon, color) {
      this.message = message
      this.icon = icon
      this.color = color
      this.bottomPopup = true
      setTimeout(() => { this.bottomPopup = false }, 1500)
    },
    reset () {
      this.name = ''
      this.nameErrorColor = ''
      this.number = ''
      this.numberErrorColor = ''
      this.code = ''
      this.address = ''
      this.codeErrorColor = ''
      this.room_id = ''
      this.team_id = ''
      this.roomName = '所属班级'
      this.teamName = '所属社团'
    },
    closeRoom () {
      this.openRoom = false
      if (this.room_id.toString() !== '') {
        this.$http.get(
          API.GetRoomName,
          { params: {
            id: this.room_id
          }
          },
          {
            headers:
            {
              'X-Requested-With': 'XMLHttpRequest'
            }
          }).then((response) => {
            this.roomName = response.body
          }, (response) => {
            this.openPopup('服务器内部错误！', 'error', 'red')
          })
      } else {
        this.roomName = '所属班级'
      }
    },
    closeTeam () {
      this.openTeam = false
      if (this.team_id.toString() !== '') {
        this.$http.get(
          API.GetTeamName,
          { params: {
            id: this.team_id
          }
          },
          {
            headers:
            {
              'X-Requested-With': 'XMLHttpRequest'
            }
          }).then((response) => {
            this.teamName = response.body
          }, (response) => {
            this.openPopup('服务器内部错误！', 'error', 'red')
          })
      } else {
        this.teamName = '所属社团'
      }
    },
    checkName (value) {
      this.$http.get(
        API.CheckNameForNew,
        { params: {
          name: value
        }
        },
        {
          headers:
          {
            'X-Requested-With': 'XMLHttpRequest'
          }
        }).then((response) => {
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
          this.openPopup('服务器内部错误！', 'error', 'red')
        })
    },
    checkNumber (value) {
      this.$http.get(
        API.CheckNumberForNew,
        { params: {
          number: value
        }
        },
        {
          headers:
          {
            'X-Requested-With': 'XMLHttpRequest'
          }
        }).then((response) => {
          if (response.body === 'error') {
            this.openPopup('请重新登录!', 'report_problem', 'orange')
            window.location.href = '/'
          } else if (response.body === 'OK') {
            this.numberErrorText = ''
            this.numberErrorColor = 'green'
          } else {
            this.numberErrorText = response.body
            this.numberErrorColor = 'red'
          }
        }, (response) => {
          this.openPopup('服务器内部错误！', 'error', 'red')
        })
    },
    checkCode (value) {
      this.$http.get(
        API.CheckCodeForNew,
        { params: {
          code: value
        }
        },
        {
          headers:
          {
            'X-Requested-With': 'XMLHttpRequest'
          }
        }).then((response) => {
          if (response.body === 'error') {
            this.openPopup('请重新登录!', 'report_problem', 'orange')
            window.location.href = '/'
          } else if (response.body === 'OK') {
            this.codeErrorText = ''
            this.codeErrorColor = 'green'
          } else {
            this.codeErrorText = response.body
            this.codeErrorColor = 'red'
          }
        }, (response) => {
          this.openPopup('服务器内部错误！', 'error', 'red')
        })
    },
    save () {
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
        }
        },
        {
          headers:
          {
            'X-Requested-With': 'XMLHttpRequest'
          }
        }).then((response) => {
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
