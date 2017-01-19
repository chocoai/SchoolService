<template>
  <div class="TeacherEdit">
    <mu-appbar title="请核实后输入以下信息">
      <mu-icon-button icon='reply' slot="right" @click="reply"/>
    </mu-appbar>
    <mu-text-field label="姓名" :disabled="edit" underlineShow="false" v-model="name" :errorColor="nameErrorColor" :errorText="nameErrorText" @input="checkName" fullWidth labelFloat icon="person"/><br/>
    <mu-text-field label="账号" disabled v-model="userId" fullWidth labelFloat icon="assignment"/><br/>
    <mu-text-field label="手机" :disabled="edit" underlineShow="false" v-model="mobile" :errorColor="mobileErrorColor" :errorText="mobileErrorText" @input="checkMobile"  fullWidth labelFloat icon="phone" maxLength="11"/><br/>
    <mu-select-field v-model="isManager" label="权限" icon="comment" fullWidth :maxHeight="300">
      <mu-menu-item value="0" title="一般教师"/>
      <mu-menu-item value="1" title="管理人员"/>
    </mu-select-field>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="edit" v-if="edit" @click="goEdit" primary/>
        <mu-float-button icon="cancel" v-if="save" @click="goCancel" secondary/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="delete" v-if="edit"  @click="openDelete" backgroundColor="red"/>
        <mu-float-button icon="done" v-if="save" @click="goSave" backgroundColor="green"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="cached" v-if="save" @click="goReset" backgroundColor="orange"/>
        <mu-float-button icon="dialer_sip" v-if="edit" @click="goCall" backgroundColor="#6633CC"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-dialog :open="forDelete" :title="deleteTitle" @close="goClose">
      <mu-flat-button label="取消" @click="goClose" />
      <mu-flat-button label="确定" @click="goDelete" secondary/>
    </mu-dialog>
    <mu-popup position="bottom" :overlay="false" popupClass="popup-bottom" :open="bottomPopup">
      <mu-icon :value="icon" :size="36" :color="color"/>&nbsp;{{ message }}
    </mu-popup>
  </div>
</template>

<script>
import * as API from './TeacherAPI.js'
export default {
  name: 'TeacherEdit',
  data () {
    return {
      edit: true,
      save: false,
      forDelete: false,
      bottomPopup: false,
      icon: '',
      color: '',
      message: '',
      deleteTitle: '',
      teacher: '',
      name: '',
      mobile: '',
      userId: '',
      isManager: '',
      nameErrorText: '',
      mobileErrorText: '',
      nameErrorColor: '',
      mobileErrorColor: ''
    }
  },
  created () {
    this.fetchData(this.$route.params.teacherId)
  },
  watch: {
    // 如果路由有变化，会再次执行该方法
    '$route': 'fetchData'
  },
  methods: {
    reply () {
      this.$router.push({ path: '/teacherList' })
    },
    openDelete () {
      this.forDelete = true
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
    },
    goCancel () {
      this.edit = true
      this.save = false
      this.fetchData(this.$route.params.teacherId)
    },
    goClose () {
      this.forDelete = false
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
            this.openPopup('删除成功!', 'check_circle', 'green')
            setTimeout(() => { this.$router.push({ path: '/teacherList' }) }, 1000)
          } else if (response.body === '要删除的教师不存在!') {
            this.edit = true
            this.save = false
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
          userId: this.userId
        }
        },
        {
          headers:
          {
            'X-Requested-With': 'XMLHttpRequest'
          }
        }).then((response) => {
          switch (response.body) {
            case '0':
              this.edit = true
              this.save = false
              this.openPopup('修改成功!', 'check_circle', 'green')
              break
            case '1':
              this.openPopup('要修改的教师不存在!', 'report_problem', 'red')
              setTimeout(() => { this.$router.push({ path: '/teacherList' }) }, 1000)
              break
            case '2':
              this.openPopup('未找到修改内容！', 'report_problem', 'orange')
              break
            case '3':
              this.openPopup('姓名应为两个以上汉字！', 'report_problem', 'orange')
              break
            case '4':
              this.openPopup('输入的手机号码应为11位数字！', 'report_problem', 'orange')
              break
            case '5':
              this.openPopup('输入的微信号不应包含中文！', 'report_problem', 'orange')
              break
            case '6':
              this.openPopup('输入的电子邮箱格式不正确！', 'report_problem', 'orange')
              break
            case '7':
              this.openPopup('手机号码和微信号不能同时为空！', 'report_problem', 'orange')
              break
            case '8':
              this.openPopup('输入的手机号码已存在！', 'report_problem', 'orange')
              break
            case '9':
              this.openPopup('输入的微信号已存在！', 'report_problem', 'orange')
              break
            case 'A':
              this.openPopup('输入的电子邮箱已存在！', 'report_problem', 'orange')
              break
            default:
              window.location.href = '/'
          }
        }, (response) => {
          this.openPopup('服务器内部错误!', 'report_problem', 'orange')
        })
    },
    fetchData (teacherId) {
      this.$http.get(
        API.GetById,
        { params:
        {
          id: teacherId
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
        this.teacher = response.body
        this.name = this.teacher.name
        this.mobile = this.teacher.mobile
        this.userId = this.teacher.userId
        this.isManager = this.teacher.isManager
        this.deleteTitle = '确认要删除' + this.name + '吗?'
      }, (response) => {
      })
    },
    checkName (value) {
      this.$http.get(
        API.CheckNameForEdit,
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
    checkMobile (value) {
      this.$http.get(
        API.CheckMobileForEdit,
        { params: {
          mobile: value
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
            this.mobileErrorText = ''
            this.mobileErrorColor = 'green'
          } else {
            this.mobileErrorText = response.body
            this.mobileErrorColor = 'red'
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
    height: 70px;
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
