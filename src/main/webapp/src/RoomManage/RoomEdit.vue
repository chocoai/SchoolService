<template>
  <div class="RoomEdit">
    <mu-appbar title="请核实后输入以下信息">
      <mu-icon-button icon='reply' slot="right" @click="reply"/>
    </mu-appbar>
    <mu-text-field v-model="name" label="班级名称" icon="comment" :errorColor="nameErrorColor" :errorText="nameErrorText" @input="checkName" fullWidth labelFloat/><br/>
    <mu-select-field v-model="course1" label="班主任" icon="comment" fullWidth :maxHeight="300">
      <mu-menu-item v-for="teacher in teachers"  :value="teacher.id" :title="teacher.name" multiple/>
    </mu-select-field>
    <mu-select-field v-model="course2" label="语文教师" icon="comment" fullWidth :maxHeight="300">
      <mu-menu-item v-for="teacher in teachers" :value="teacher.id" :title="teacher.name" multiple/>
    </mu-select-field>
    <mu-select-field v-model="course3" label="数学教师" icon="comment" fullWidth :maxHeight="300">
      <mu-menu-item v-for="teacher in teachers" :value="teacher.id" :title="teacher.name" multiple/>
    </mu-select-field>
    <mu-select-field v-model="course4" label="英语教师" icon="comment" fullWidth :maxHeight="300">
      <mu-menu-item v-for="teacher in teachers" :value="teacher.id" :title="teacher.name" multiple/>
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
import * as API from './RoomAPI.js'
export default {
  name: 'RoomEdit',
  data () {
    return {
      bottomPopup: false,
      icon: '',
      color: '',
      name: '',
      message: '',
      nameErrorText: '',
      nameErrorColor: '',
      course1: [],
      course2: [],
      course3: [],
      course4: [],
      teachers: []
    }
  },
  created () {
    this.fetchData(this.$route.params.roomId)
  },
  watch: {
    // 如果路由有变化，会再次执行该方法
    '$route': 'fetchData'
  },
  methods: {
    reply () {
      this.$router.push({ path: '/roomList' })
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
    goCall () {
      this.mobile.toString() === '' ? this.openPopup('无联系电话!', 'report_problem', 'orange') : window.location.href = 'tel:' + this.mobile
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
      this.fetchData(this.$route.params.teacherId)
    },
    goClose () {
      this.forDelete = false
      this.forResave = false
    },
    goReset () {
      this.fetchData(this.$route.params.teacherId)
    },
    goDelete () {
      this.$http.get(
        API.DeleteById,
        { params: {
          id: this.$route.params.teacherId
        }
        },
        {
          headers:
          {
            'X-Requested-With': 'XMLHttpRequest'
          }
        }
        ).then((response) => {
          if (response.body === 'error') {
            window.location.href = '/'
          } else if (response.body === 'OK') {
            this.edit = true
            this.save = false
            if (this.state.toString() === '1' || this.state.toString() === '2') {
              this.deletes = true
              this.resave = false
            } else {
              this.deletes = false
              this.resave = true
            }
            this.openPopup('取消关注成功!', 'check_circle', 'green')
            setTimeout(() => { this.$router.push({ path: '/teacherList' }) }, 1000)
          } else if (response.body === '要取消关注的教师不存在!') {
            this.edit = true
            this.save = false
            if (this.state.toString() === '1' || this.state.toString() === '2') {
              this.deletes = true
              this.resave = false
            } else {
              this.deletes = false
              this.resave = true
            }
            this.openPopup(response.body, 'report_problem', 'red')
            setTimeout(() => { this.$router.push({ path: '/teacherList' }) }, 1000)
          } else {
            this.openPopup(response.body, 'report_problem', 'red')
            setTimeout(() => { this.$router.push({ path: '/teacherList' }) }, 1000)
          }
        }, (response) => {
          this.openPopup('服务器内部错误!', 'report_problem', 'orange')
        })
    },
    goResave () {
      this.$http.get(
        API.ResaveById,
        { params: {
          id: this.$route.params.teacherId
        }
        },
        {
          headers:
          {
            'X-Requested-With': 'XMLHttpRequest'
          }
        }
        ).then((response) => {
          if (response.body === 'error') {
            window.location.href = '/'
          } else if (response.body === 'OK') {
            this.edit = true
            this.save = false
            if (this.state.toString() === '1' || this.state.toString() === '2') {
              this.deletes = true
              this.resave = false
            } else {
              this.deletes = false
              this.resave = true
            }
            this.openPopup('重新邀请关注成功!', 'check_circle', 'green')
            setTimeout(() => { this.$router.push({ path: '/teacherList' }) }, 1000)
          } else if (response.body === '要重新邀请关注的教师不存在!') {
            this.edit = true
            this.save = false
            if (this.state.toString() === '1' || this.state.toString() === '2') {
              this.deletes = true
              this.resave = false
            } else {
              this.deletes = false
              this.resave = true
            }
            this.openPopup(response.body, 'report_problem', 'red')
            setTimeout(() => { this.$router.push({ path: '/teacherList' }) }, 1000)
          } else {
            this.openPopup(response.body, 'report_problem', 'red')
            setTimeout(() => { this.$router.push({ path: '/teacherList' }) }, 1000)
          }
        }, (response) => {
          this.openPopup('服务器内部错误!', 'report_problem', 'orange')
        })
    },
    goSave () {
      this.$http.get(
        API.Edit,
        { params: {
          id: this.$route.params.teacherId,
          name: this.name,
          mobile: this.mobile,
          isManager: this.isManager
        }
        },
        {
          headers:
          {
            'X-Requested-With': 'XMLHttpRequest'
          }
        }).then((response) => {
          if (response.body === 'error') {
            window.location.href = '/'
          } else if (response.body === 'OK') {
            this.openPopup('修改成功！', 'check_circle', 'green')
            setTimeout(() => { this.$router.push({ path: '/teacherList' }) }, 1000)
          } else {
            this.openPopup(response.body, 'report_problem', 'orange')
          }
        }, (response) => {
          this.openPopup('服务器内部错误!', 'report_problem', 'orange')
        })
    },
    fetchData (roomId) {
      this.$http.get(
        API.GetById,
        { params:
        {
          id: roomId
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
        this.room = response.body
        this.name = this.room.name
        this.mobile = this.teacher.mobile
        this.userId = this.teacher.userId
        this.isManager = this.teacher.isManager.toString()
        this.state = this.teacher.state
        if (this.room.state.toString() === '1') {
          this.deletes = true
          this.resave = false
        } else {
          this.deletes = false
          this.resave = true
        }
        this.deleteTitle = '确认要取消' + this.name + '的关注吗?'
        this.resaveTitle = '确认要邀请' + this.name + '再次关注吗?'
      }, (response) => {
      })
    },
    checkName (value) {
      this.$http.get(
        API.CheckNameForEdit,
        { params: {
          id: this.$route.params.teacherId,
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
