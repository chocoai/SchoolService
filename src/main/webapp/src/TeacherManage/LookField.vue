<template>
  <div class="LookField">
    <mu-appbar title="请核实后输入以下信息">
      <mu-flat-button color="white" label="返回" slot="right" icon="reply" labelPosition="before" @click="reply"/>
    </mu-appbar>
    <mu-text-field label="教师姓名" :disabled="edit" underlineShow="false" v-model="name" :errorColor="nameErrorColor" :errorText="nameErrorText" @input="checkName" fullWidth labelFloat icon="person"/><br/>
    <mu-text-field label="联系电话" :disabled="edit" underlineShow="false" v-model="phone" :errorColor="phoneErrorColor" :errorText="phoneErrorText" @input="checkPhone"  fullWidth labelFloat icon="phone" maxLength="11"/><br/>
    <mu-text-field label="微信号码" :disabled="edit" underlineShow="false" v-model="weixin" :errorColor="weixinErrorColor" :errorText="weixinErrorText" @input="checkWeixin" fullWidth labelFloat icon="chat"/><br/>
    <mu-text-field label="电子邮箱" :disabled="edit" underlineShow="false" v-model="email"  :errorColor="emailErrorColor" :errorText="emailErrorText" @input="checkEmail" fullWidth labelFloat icon="email"/><br/>
    <mu-text-field label="备注信息" :disabled="edit" underlineShow="false" v-model="remark" fullWidth labelFloat icon="bookmark"/><br/>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-raised-button label="修改" class="demo-raised-button" labelPosition="before" icon="edit" v-if="edit" @click="goEdit" primary/>
        <mu-raised-button label="取消" class="demo-raised-button" labelPosition="before" icon="cancel" v-if="save" @click="goCancel" />
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-raised-button label="删除" class="demo-raised-button" labelPosition="before" icon="delete" v-if="edit" secondary @click="openDelete"/>
        <mu-raised-button label="保存" class="demo-raised-button" labelPosition="before" icon="done" v-if="save" @click="goSave" primary/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-raised-button label="重置" class="demo-raised-button" labelPosition="before" icon="cached" v-if="save" :disabled="edit" backgroundColor="#ffd700" @click="goReset"/>
        <mu-raised-button label="呼叫" class="demo-raised-button" labelPosition="before" icon="dialer_sip" v-if="edit" @click="goCall" backgroundColor="#a4c639"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-toast v-if="toast" :message="message"/>
    <mu-dialog :open="dialog" :title="deleteTitle" @close="goClose">
      <mu-flat-button label="取消" @click="goClose" />
      <mu-flat-button label="确定" @click="goDelete"/>
    </mu-dialog>
  </div>
</template>

<script>
import * as AF from '../Util/AjaxFunction.js'
export default {
  name: 'LookField',
  data () {
    return {
      toast: false,
      edit: true,
      save: false,
      dialog: false,
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
      window.location.href = '#/'
    },
    openDelete () {
      this.dialog = true
    },
    goCall () {
      window.location.href = 'tel:' + this.phone
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
      this.dialog = false
    },
    goReset () {
      this.fetchData(this.$route.params.teacherId)
    },
    goDelete () {
      this.$http.get(
        AF.TeacherDeleteById,
        { params: {
          id: this.$route.params.teacherId
        }
        }).then((response) => {
          switch (response.body) {
            case '0':
              this.edit = true
              this.save = false
              window.location.href = '#/'
              break
            case '1':
              this.message = '该教师已删除！'
              this.toast = true
              if (this.toastTimer) clearTimeout(this.toastTimer)
              this.toastTimer = setTimeout(() => { this.toast = false; this.message = '' }, 1500)
              window.location.href = '#/'
              break
            default:
          }
        }, (response) => {
          this.message = '服务器内部错误！'
          this.toast = true
          if (this.toastTimer) clearTimeout(this.toastTimer)
          this.toastTimer = setTimeout(() => { this.toast = false; this.message = '' }, 1500)
        })
    },
    goSave () {
      this.$http.get(
        AF.TeacherEdit,
        { params: {
          id: this.$route.params.teacherId,
          name: this.name,
          phone: this.phone,
          weixin: this.weixin,
          email: this.email,
          remark: this.remark
        }
        }).then((response) => {
          switch (response.body) {
            case '0':
              this.edit = true
              this.save = false
              this.$router.go('/')
              window.location.href = '#/'
              break
            case '1':
              this.message = '要修改的教师不存在！'
              this.toast = true
              if (this.toastTimer) clearTimeout(this.toastTimer)
              this.toastTimer = setTimeout(() => { this.toast = false }, 1500)
              break
            case '2':
              this.message = '未找到修改内容！'
              this.toast = true
              if (this.toastTimer) clearTimeout(this.toastTimer)
              this.toastTimer = setTimeout(() => { this.toast = false; this.message = '' }, 1500)
              break
            case '3':
              this.message = '姓名应为两个以上汉字！'
              this.toast = true
              if (this.toastTimer) clearTimeout(this.toastTimer)
              this.toastTimer = setTimeout(() => { this.toast = false; this.message = '' }, 1500)
              break
            case '4':
              this.message = '输入的手机号码应为11位数字！'
              this.toast = true
              if (this.toastTimer) clearTimeout(this.toastTimer)
              this.toastTimer = setTimeout(() => { this.toast = false; this.message = '' }, 1500)
              break
            case '5':
              this.message = '输入的微信号不应包含中文！'
              this.toast = true
              if (this.toastTimer) clearTimeout(this.toastTimer)
              this.toastTimer = setTimeout(() => { this.toast = false; this.message = '' }, 1500)
              break
            case '6':
              this.message = '输入的电子邮箱格式不正确！'
              this.toast = true
              if (this.toastTimer) clearTimeout(this.toastTimer)
              this.toastTimer = setTimeout(() => { this.toast = false; this.message = '' }, 1500)
              break
            case '7':
              this.message = '手机号码和微信号不能同时为空！'
              this.toast = true
              if (this.toastTimer) clearTimeout(this.toastTimer)
              this.toastTimer = setTimeout(() => { this.toast = false; this.message = '' }, 1500)
              break
            case '8':
              this.message = '输入的手机号码已存在！'
              this.toast = true
              if (this.toastTimer) clearTimeout(this.toastTimer)
              this.toastTimer = setTimeout(() => { this.toast = false; this.message = '' }, 1500)
              break
            case '9':
              this.message = '输入的微信号已存在！'
              this.toast = true
              if (this.toastTimer) clearTimeout(this.toastTimer)
              this.toastTimer = setTimeout(() => { this.toast = false; this.message = '' }, 1500)
              break
            case 'A':
              this.message = '输入的电子邮箱已存在！'
              this.toast = true
              if (this.toastTimer) clearTimeout(this.toastTimer)
              this.toastTimer = setTimeout(() => { this.toast = false; this.message = '' }, 1500)
              break
            default:
          }
        }, (response) => {
          this.message = '服务器内部错误！'
          this.toast = true
          if (this.toastTimer) clearTimeout(this.toastTimer)
          this.toastTimer = setTimeout(() => { this.toast = false; this.message = '' }, 1500)
        })
    },
    fetchData (teacherId) {
      this.$http.get(
        AF.TeacherGetById,
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
        AF.TeacherCheckNameForEdit,
        { params: {
          name: value
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
          }
        }, (response) => {
          this.message = '服务器内部错误！'
          this.toast = true
          if (this.toastTimer) clearTimeout(this.toastTimer)
          this.toastTimer = setTimeout(() => { this.toast = false; this.message = '' }, 1500)
        })
    },
    checkPhone (value) {
      this.$http.get(
        AF.TeacherCheckPhoneForEdit,
        { params: {
          phone: value
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
          }
        }, (response) => {
          this.message = '服务器内部错误！'
          this.toast = true
          if (this.toastTimer) clearTimeout(this.toastTimer)
          this.toastTimer = setTimeout(() => { this.toast = false; this.message = '' }, 1500)
        })
    },
    checkWeixin (value) {
      this.$http.get(
        AF.TeacherCheckWeixinForEdit,
        { params: {
          weixin: value
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
          }
        }, (response) => {
          this.message = '服务器内部错误！'
          this.toast = true
          if (this.toastTimer) clearTimeout(this.toastTimer)
          this.toastTimer = setTimeout(() => { this.toast = false; this.message = '' }, 1500)
        })
    },
    checkEmail (value) {
      this.$http.get(
        AF.TeacherCheckEmailForEdit,
        { params: {
          email: value
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
          }
        }, (response) => {
          this.message = '服务器内部错误！'
          this.toast = true
          if (this.toastTimer) clearTimeout(this.toastTimer)
          this.toastTimer = setTimeout(() => { this.toast = false; this.message = '' }, 1500)
        })
    }
  }
}
</script>
<style lang="css">
  .flex-demo {
    height: 32px;
    text-align: center;
    line-height: 32px;
  }
</style>
