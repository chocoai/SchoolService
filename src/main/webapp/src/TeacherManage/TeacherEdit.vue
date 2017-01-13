<template>
  <div class="TeacherEdit">
    <mu-appbar title="请核实后输入以下信息">
      <mu-icon-button icon='reply' slot="right" @click="reply"/>
    </mu-appbar>
    <mu-text-field label="教师姓名" :disabled="edit" underlineShow="false" v-model="name" :errorColor="nameErrorColor" :errorText="nameErrorText" @input="checkName" fullWidth labelFloat icon="person"/><br/>
    <mu-text-field label="联系电话" :disabled="edit" underlineShow="false" v-model="phone" :errorColor="phoneErrorColor" :errorText="phoneErrorText" @input="checkPhone"  fullWidth labelFloat icon="phone" maxLength="11"/><br/>
    <mu-text-field label="微信号码" :disabled="edit" underlineShow="false" v-model="weixin" :errorColor="weixinErrorColor" :errorText="weixinErrorText" @input="checkWeixin" fullWidth labelFloat icon="chat"/><br/>
    <mu-text-field label="电子邮箱" :disabled="edit" underlineShow="false" v-model="email"  :errorColor="emailErrorColor" :errorText="emailErrorText" @input="checkEmail" fullWidth labelFloat icon="email"/><br/>
    <mu-text-field label="备注信息" :disabled="edit" underlineShow="false" v-model="remark" fullWidth labelFloat icon="bookmark"/><br/>
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
      phone: '',
      weixin: '',
      email: '',
      remark: '',
      nameErrorText: '',
      phoneErrorText: '',
      weixinErrorText: '',
      emailErrorText: '',
      nameErrorColor: '',
      phoneErrorColor: '',
      weixinErrorColor: '',
      emailErrorColor: ''
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
      this.phone.toString() === '' ? this.openPopup('无联系电话！', 'report_problem', 'orange') : window.location.href = 'tel:' + this.phone
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
          switch (response.body) {
            case '0':
              this.edit = true
              this.save = false
              this.openPopup('删除成功！', 'check_circle', 'green')
              setTimeout(() => { this.$router.push({ path: '/teacherList' }) }, 1000)
              break
            case '1':
              this.openPopup('要删除的教师不存在！', 'report_problem', 'red')
              setTimeout(() => { this.$router.push({ path: '/teacherList' }) }, 1000)
              break
            default:
              window.location.href = '/'
          }
        }, (response) => {
          this.openPopup('服务器内部错误！', 'report_problem', 'orange')
        })
    },
    goSave () {
      this.$http.get(
        API.Edit,
        { params: {
          id: this.$route.params.teacherId,
          name: this.name,
          phone: this.phone,
          weixin: this.weixin,
          email: this.email,
          remark: this.remark
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
              this.openPopup('修改成功！', 'check_circle', 'green')
              break
            case '1':
              this.openPopup('要修改的教师不存在！', 'report_problem', 'red')
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
          this.openPopup('服务器内部错误！', 'report_problem', 'orange')
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
        this.phone = this.teacher.phone
        this.weixin = this.teacher.weixinId
        this.email = this.teacher.email
        this.remark = this.teacher.remark
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
          switch (response.body) {
            case '0':
              this.nameErrorText = ''
              this.nameErrorColor = 'green'
              break
            case '1':
              this.nameErrorText = '请输入两个以上汉字！'
              this.nameErrorColor = 'red'
              break
            case '2':
              this.nameErrorText = '该姓名存在重名情况！'
              this.nameErrorColor = 'orange'
              break
            default:
              this.nameErrorText = ''
              this.nameErrorColor = 'blue'
              window.location.href = '/'
          }
        }, (response) => {
          this.openPopup('服务器内部错误！', 'report_problem', 'orange')
        })
    },
    checkPhone (value) {
      this.$http.get(
        API.CheckPhoneForEdit,
        { params: {
          phone: value
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
              this.phoneErrorText = '此处为绑定微信的手机号码！'
              this.phoneErrorColor = 'green'
              break
            case '1':
              this.phoneErrorText = '请输入11位数字！'
              this.phoneErrorColor = 'red'
              break
            case '2':
              this.phoneErrorText = '该手机号码已使用！'
              this.phoneErrorColor = 'orange'
              break
            default:
              this.phoneErrorText = ''
              this.phoneErrorColor = 'blue'
              window.location.href = '/'
          }
        }, (response) => {
          this.openPopup('服务器内部错误！', 'report_problem', 'orange')
        })
    },
    checkWeixin (value) {
      this.$http.get(
        API.CheckWeixinForEdit,
        { params: {
          weixin: value
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
              this.weixinErrorText = '此处为微信号，不是微信昵称！'
              this.weixinErrorColor = 'green'
              break
            case '1':
              this.weixinErrorText = '微信号不应包含中文！'
              this.weixinErrorColor = 'red'
              break
            case '2':
              this.weixinErrorText = '该微信号已使用！'
              this.weixinErrorColor = 'orange'
              break
            default:
              this.weixinErrorText = ''
              this.weixinErrorColor = 'blue'
              window.location.href = '/'
          }
        }, (response) => {
          this.openPopup('服务器内部错误！', 'report_problem', 'orange')
        })
    },
    checkEmail (value) {
      this.$http.get(
        API.CheckEmailForEdit,
        { params: {
          email: value
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
              this.emailErrorText = ''
              this.emailErrorColor = 'green'
              break
            case '1':
              this.emailErrorText = '电子邮箱格式错误！'
              this.emailErrorColor = 'red'
              break
            case '2':
              this.emailErrorText = '该电子邮箱已使用！'
              this.emailErrorColor = 'orange'
              break
            default:
              this.emailErrorText = ''
              this.emailErrorColor = 'blue'
              window.location.href = '/'
          }
        }, (response) => {
          this.openPopup('服务器内部错误！', 'report_problem', 'orange')
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
