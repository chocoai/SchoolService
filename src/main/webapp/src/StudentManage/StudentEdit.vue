<template>
  <div class="StudentEdit">
    <mu-appbar title="请核实后输入以下信息">
      <mu-icon-button icon='reply' slot="right" @click="reply"/>
    </mu-appbar>
    <mu-text-field :disabled="save" v-model="name" label="姓名" icon="comment" :errorColor="nameErrorColor" :errorText="nameErrorText" @input="checkName" fullWidth labelFloat/><br/>
    <mu-text-field :disabled="save" v-model="number" label="证件号码" icon="comment" :errorColor="numberErrorColor" :errorText="numberErrorText" @input="checkNumber" fullWidth labelFloat maxLength="18"/><br/>
    <mu-text-field :disabled="save" v-model="code" label="学籍号码" icon="comment" :errorColor="codeErrorColor" :errorText="codeErrorText" @input="checkCode" fullWidth labelFloat maxLength="15"/><br/>
    <mu-select-field :disabled="save" v-model="room_id" label="所属班级" icon="comment" fullWidth :maxHeight="300">
      <mu-menu-item v-for="room in rooms" :value="room.id" :title="room.name"/>
    </mu-select-field>
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
      icon: '',
      color: '',
      name: '',
      number: '',
      code: '',
      room_id: '',
      message: '',
      nameErrorText: '',
      nameErrorColor: '',
      numberErrorText: '',
      numberErrorColor: '',
      codeErrorText: '',
      codeErrorColor: '',
      rooms: []
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
      this.openPopup('服务器内部错误！', 'report_problem', 'orange')
    })
    this.fetchData(this.$route.params.studentId)
  },
  watch: {
    // 如果路由有变化，会再次执行该方法
    '$route': 'fetchData'
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
            this.openPopup('请重新登录!', 'report_problem', 'red')
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
            this.openPopup('删除成功!', 'check_circle', 'green')
            setTimeout(() => { this.$router.push({ path: '/studentList' }) }, 1000)
          } else if (response.body === '要删除的班级不存在!') {
            this.edit = true
            this.save = false
            if (this.state.toString() === '1') {
              this.deletes = true
              this.resave = false
            } else {
              this.deletes = false
              this.resave = true
            }
            this.openPopup(response.body, 'report_problem', 'red')
            setTimeout(() => { this.$router.push({ path: '/studentList' }) }, 1000)
          } else {
            this.openPopup(response.body, 'report_problem', 'red')
            setTimeout(() => { this.$router.push({ path: '/studentList' }) }, 1000)
          }
        }, (response) => {
          this.forSave = false
          this.openPopup('服务器内部错误!', 'report_problem', 'orange')
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
            this.openPopup('请重新登录!', 'report_problem', 'red')
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
            this.openPopup(response.body, 'report_problem', 'red')
            setTimeout(() => { this.$router.push({ path: '/studentList' }) }, 1000)
          } else {
            this.openPopup(response.body, 'report_problem', 'red')
            setTimeout(() => { this.$router.push({ path: '/studentList' }) }, 1000)
          }
        }, (response) => {
          this.forSave = false
          this.openPopup('服务器内部错误!', 'report_problem', 'orange')
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
          room_id: this.room_id
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
            this.openPopup('请重新登录!', 'report_problem', 'red')
            window.location.href = '/'
          } else if (response.body === 'OK') {
            this.openPopup('修改成功！', 'check_circle', 'green')
            setTimeout(() => { this.$router.push({ path: '/studentList' }) }, 1000)
          } else {
            this.openPopup(response.body, 'report_problem', 'orange')
          }
        }, (response) => {
          this.forSave = false
          this.openPopup('服务器内部错误!', 'report_problem', 'orange')
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
        this.room_id = this.student.room_id
        this.state = this.student.state
        if (this.student.state.toString() === '1') {
          this.deletes = true
          this.resave = false
        } else {
          this.deletes = false
          this.resave = true
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
