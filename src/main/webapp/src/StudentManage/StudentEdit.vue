<template>
  <div class="StudentEdit">
    <mu-appbar title="请核实后输入以下信息">
      <mu-icon-button icon='reply' slot="right" @click="reply"/>
    </mu-appbar>
    <mu-text-field :disabled="edit" v-model="name" label="姓名" icon="comment" :errorColor="nameErrorColor" :errorText="nameErrorText" @input="checkName" fullWidth labelFloat/><br/>
    <mu-text-field :disabled="edit" v-model="number" label="证件号码" icon="comment" :errorColor="numberErrorColor" :errorText="numberErrorText" @input="checkNumber" fullWidth labelFloat maxLength="18"/><br/>
    <mu-text-field :disabled="edit" v-model="code" label="学籍号码" icon="comment" :errorColor="codeErrorColor" :errorText="codeErrorText" @input="checkCode" fullWidth labelFloat maxLength="15"/><br/>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-flat-button :disabled="edit" :label="roomName" @click="openRoom=true" :icon="roomIcon" :backgroundColor="roomBack" color="#FFFFFF"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-flat-button :disabled="edit" :label="teamName" @click="openTeam=true" :icon="teamIcon" :backgroundColor="teamBack" color="#FFFFFF"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="edit" v-if="edit" @click="goEdit" primary/>
        <mu-float-button icon="cancel" v-if="save" @click="goCancel" secondary/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="delete" v-if="deletes"  @click="openDelete" backgroundColor="red"/>
        <mu-float-button icon="compare_arrows" v-if="resave" @click="openResave" backgroundColor="green"/>
        <mu-float-button icon="done" v-if="save" @click="goSave" backgroundColor="green"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-drawer right :open="openRoom" docked="false">
      <mu-appbar title="请选择所属班级" @click.native="closeRoom">
        <mu-icon-button icon='done' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item title="清空" @click.native="room_id=''">
          <mu-icon slot="left" value="send"/>
        </mu-list-item>
        <mu-list-item v-for="room in rooms" :title="room.name">
          <mu-avatar v-if="room.state.toString() === '1'" slot="leftAvatar" :size="40" color="deepOrange300" backgroundColor="purple500">激</mu-avatar>
          <mu-avatar v-if="room.state.toString() === '2'" slot="leftAvatar" :size="40" color="deepOrange300" backgroundColor="purple500">注</mu-avatar>
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
          <mu-icon slot="left" value="send"/>
        </mu-list-item>
        <mu-list-item v-for="team in teams" :title="team.name">
          <mu-avatar v-if="team.state.toString() === '1'" slot="leftAvatar" :size="40" color="deepOrange300" backgroundColor="purple500">冻</mu-avatar>
          <mu-avatar v-if="team.state.toString() === '2'" slot="leftAvatar" :size="40" color="deepOrange300" backgroundColor="purple500">删</mu-avatar>
          <mu-radio v-model="team_id" label="" labelLeft :nativeValue="team.id" uncheckIcon="favorite_border" checkedIcon="favorite" slot="right" />
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-dialog :open="forSave" title="正在保存" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-dialog :open="forDelete" :title="deleteTitle" @close="goClose">
      <mu-flat-button label="取消" @click="goClose" />
      <mu-flat-button label="确定" @click="goDelete" secondary/>
    </mu-dialog>
    <mu-dialog :open="forResave" :title="resaveTitle" @close="goClose">
      <mu-flat-button label="取消" @click="goClose" />
      <mu-flat-button label="确定" @click="goResave" secondary/>
    </mu-dialog>
    <mu-popup position="bottom" :overlay="false" popupClass="popup-bottom" :open="bottomPopup">
      <mu-icon :value="icon" :size="36" :color="color"/>&nbsp;{{ message }}
    </mu-popup>
  </div>
</template>

<script>
import * as API from './StudentAPI.js'
export default {
  name: 'StudentEdit',
  data () {
    return {
      bottomPopup: false,
      edit: true,
      save: false,
      deletes: false,
      resave: false,
      forSave: false,
      forDelete: false,
      forResave: false,
      resaveTitle: '',
      deleteTitle: '',
      icon: '',
      color: '',
      name: '',
      number: '',
      code: '',
      room_id: '',
      team_id: '',
      room: '',
      team: '',
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
  created () {
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
    this.fetchData(this.$route.params.studentId)
  },
  watch: {
    // 如果路由有变化，会再次执行该方法
    '$route': 'fetchData'
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
    openDelete () {
      this.forDelete = true
    },
    openResave () {
      this.forResave = true
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
    goEdit () {
      this.edit = false
      this.save = true
      this.deletes = false
      this.resave = false
    },
    goCancel () {
      this.edit = true
      this.save = false
      this.fetchData(this.$route.params.studentId)
    },
    goClose () {
      this.forDelete = false
      this.forResave = false
    },
    goDelete () {
      this.forSave = true
      this.$http.get(
        API.DeleteById,
        { params: {
          id: this.$route.params.studentId
        }
        },
        {
          headers:
          {
            'X-Requested-With': 'XMLHttpRequest'
          }
        }
        ).then((response) => {
          this.forSave = false
          if (response.body === 'error') {
            this.openPopup('请重新登录!', 'report_problem', 'orange')
            window.location.href = '/'
          } else if (response.body === 'OK') {
            this.edit = true
            this.save = false
            if (this.state.toString() === '1') {
              this.deletes = true
              this.resave = false
            } else {
              this.deletes = false
              this.resave = true
            }
            this.openPopup('注销成功!', 'check_circle', 'green')
            setTimeout(() => { this.$router.push({ path: '/studentList' }) }, 1000)
          } else if (response.body === '要注销的学生不存在!') {
            this.edit = true
            this.save = false
            if (this.state.toString() === '1') {
              this.deletes = true
              this.resave = false
            } else {
              this.deletes = false
              this.resave = true
            }
            this.openPopup(response.body, 'report_problem', 'orange')
            setTimeout(() => { this.$router.push({ path: '/studentList' }) }, 1000)
          } else {
            this.openPopup(response.body, 'report_problem', 'orange')
            setTimeout(() => { this.$router.push({ path: '/studentList' }) }, 1000)
          }
        }, (response) => {
          this.forSave = false
          this.openPopup('服务器内部错误!', 'error', 'red')
        })
    },
    goResave () {
      this.forSave = true
      this.$http.get(
        API.ResaveById,
        { params: {
          id: this.$route.params.studentId
        }
        },
        {
          headers:
          {
            'X-Requested-With': 'XMLHttpRequest'
          }
        }
        ).then((response) => {
          this.forSave = false
          if (response.body === 'error') {
            this.openPopup('请重新登录!', 'report_problem', 'orange')
            window.location.href = '/'
          } else if (response.body === 'OK') {
            this.edit = true
            this.save = false
            if (this.state.toString() === '1') {
              this.deletes = true
              this.resave = false
            } else {
              this.deletes = false
              this.resave = true
            }
            this.openPopup('激活成功!', 'check_circle', 'green')
            setTimeout(() => { this.$router.push({ path: '/studentList' }) }, 1000)
          } else if (response.body === '要激活的学生不存在!') {
            this.edit = true
            this.save = false
            if (this.state.toString() === '1') {
              this.deletes = true
              this.resave = false
            } else {
              this.deletes = false
              this.resave = true
            }
            this.openPopup(response.body, 'report_problem', 'orange')
            setTimeout(() => { this.$router.push({ path: '/studentList' }) }, 1000)
          } else {
            this.openPopup(response.body, 'report_problem', 'orange')
            setTimeout(() => { this.$router.push({ path: '/studentList' }) }, 1000)
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
          id: this.$route.params.studentId,
          name: this.name,
          number: this.number,
          code: this.code,
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
            this.openPopup('修改成功！', 'check_circle', 'green')
            setTimeout(() => { this.$router.push({ path: '/studentList' }) }, 1000)
          } else {
            this.openPopup(response.body, 'report_problem', 'orange')
          }
        }, (response) => {
          this.forSave = false
          this.openPopup('服务器内部错误!', 'error', 'red')
        })
    },
    fetchData (studentId) {
      this.$http.get(
        API.GetById,
        { params:
        {
          id: studentId
        }
        },
        {
          headers:
          {
            'X-Requested-With': 'XMLHttpRequest'
          },
          emulateJSON: true
        }
      ).then((response) => {
        this.student = response.body
        this.name = this.student.name
        this.number = this.student.number
        this.code = this.student.code
        this.state = this.student.state
        if (this.student.state.toString() === '1') {
          this.deletes = true
          this.resave = false
        } else {
          this.deletes = false
          this.resave = true
        }
        this.deleteTitle = '确认要注销' + this.name + '吗?'
        this.resaveTitle = '确认要激活' + this.name + '吗?'
        if (this.student.room_id !== null) {
          this.room_id = this.student.room_id
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
        if (this.student.team_id !== null) {
          this.team_id = this.student.team_id
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
      }, (response) => {
      })
    },
    checkName (value) {
      this.$http.get(
        API.CheckNameForEdit,
        { params: {
          id: this.$route.params.studentId,
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
            this.openPopup('请重新登录!', 'report_problem', 'red')
            window.location.href = '/'
          } else if (response.body === 'OK') {
            this.nameErrorText = ''
            this.nameErrorColor = 'green'
          } else {
            this.nameErrorText = response.body
            this.nameErrorColor = 'red'
          }
        }, (response) => {
          this.openPopup('服务器内部错误!', 'report_problem', 'orange')
        })
    },
    checkNumber (value) {
      this.$http.get(
        API.CheckNumberForEdit,
        { params: {
          id: this.$route.params.studentId,
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
            this.openPopup('请重新登录!', 'report_problem', 'red')
            window.location.href = '/'
          } else if (response.body === 'OK') {
            this.numberErrorText = ''
            this.numberErrorColor = 'green'
          } else {
            this.numberErrorText = response.body
            this.numberErrorColor = 'red'
          }
        }, (response) => {
          this.openPopup('服务器内部错误!', 'report_problem', 'orange')
        })
    },
    checkCode (value) {
      this.$http.get(
        API.CheckCodeForEdit,
        { params: {
          id: this.$route.params.studentId,
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
            this.openPopup('请重新登录!', 'report_problem', 'red')
            window.location.href = '/'
          } else if (response.body === 'OK') {
            this.codeErrorText = ''
            this.codeErrorColor = 'green'
          } else {
            this.codeErrorText = response.body
            this.codeErrorColor = 'red'
          }
        }, (response) => {
          this.openPopup('服务器内部错误!', 'report_problem', 'orange')
        })
    }
  }
}
</script>
<style lang="css">
  .flex-demo {
    height: 170px;
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
</style>
