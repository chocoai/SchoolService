<template>
  <div>
    <mu-appbar title="教师详情">
      <mu-icon-button icon='reply' slot="right" @click="goReply"/>
    </mu-appbar>
    <mu-text-field label="姓名" :disabled="Edit_Able" v-model="name" :errorColor="nameErrorColor" :errorText="nameErrorText" @input="checkName" fullWidth labelFloat icon="person"/><br/>
    <mu-text-field label="手机" :disabled="Edit_Able" v-model="mobile" :errorColor="mobileErrorColor" :errorText="mobileErrorText" @input="checkMobile" fullWidth labelFloat icon="phone" maxLength="11"/><br/>
    <mu-text-field label="微信" :disabled="Edit_Able" v-model="weixinId" :errorColor="weixinIdErrorColor" :errorText="weixinIdErrorText" @input="checkWeixinId" fullWidth labelFloat icon="email" /><br/>
    <mu-text-field label="QQ" :disabled="Edit_Able" v-model="qq" :errorColor="qqErrorColor" :errorText="qqErrorText" @input="checkQQ" fullWidth labelFloat icon="email" /><br/>
    <mu-text-field label="电子邮箱" :disabled="Edit_Able" v-model="email" :errorColor="emailErrorColor" :errorText="emailErrorText" @input="checkEmail" fullWidth labelFloat icon="email" /><br/>
    <mu-text-field label="联系地址" :disabled="Edit_Able" v-model="address" fullWidth labelFloat icon="email" /><br/>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-flat-button :label="manager" :disabled="Edit_Able" @click="openManager=true" backgroundColor="#ce93d8" color="#000"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-flat-button :label="types" :disabled="Edit_Able" @click="openType=true" backgroundColor="#ce93d8" color="#000"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="edit" v-if="Edit_Able" @click="goEdit" primary/>
        <mu-float-button icon="cancel" v-if="Save_Able" @click="goCancel" secondary/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="delete" v-if="Edit_Able && Active_Able"  @click="openInactive" backgroundColor="red"/>
        <mu-float-button icon="compare_arrows" v-if="Edit_Able && Inactive_Able" @click="openActive" backgroundColor="green"/>
        <mu-float-button icon="done" v-if="Save_Able" :disabled="saveAble" @click="goSave" backgroundColor="green"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="dialer_sip" v-if="Edit_Able" @click="goCall" backgroundColor="cyan300"/>
        <mu-float-button icon="cached" v-if="Save_Able" @click="goReset" backgroundColor="orange"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-dialog :open="Reading" title="正在读取" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-dialog :open="Saving" title="正在保存" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-dialog :open="Activing" title="确定要激活该教师吗?" @close="goClose">
      <mu-flat-button label="取消" @click="goClose" />
      <mu-flat-button label="确定" @click="goActive" secondary/>
    </mu-dialog>
    <mu-dialog :open="Inactiving" title="确定要注销该教师吗?" @close="goClose">
      <mu-flat-button label="取消" @click="goClose" />
      <mu-flat-button label="确定" @click="goInactive" secondary/>
    </mu-dialog>
    <mu-popup position="bottom" :overlay="false" popupClass="popup-bottom" :open="bottomPopup">
      <mu-icon :value="icon" :size="36" :color="color"/>&nbsp;{{ message }}
    </mu-popup>
    <mu-drawer right :open="openManager" docked="false">
      <mu-appbar title="请选择管理类别" @click.native="openManager=false">
        <mu-icon-button icon='done' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item title="一般教师">
          <mu-radio  v-model="isManager" label="" labelLeft :nativeValue="0" uncheckIcon="favorite_border" checkedIcon="favorite" slot="right" />
        </mu-list-item>
        <mu-list-item title="管理人员">
          <mu-radio  v-model="isManager" label="" labelLeft :nativeValue="1" uncheckIcon="favorite_border" checkedIcon="favorite" slot="right" />
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-drawer right :open="openType" docked="false">
      <mu-appbar title="请选择人员类别" @click.native="openType=false">
        <mu-icon-button icon='done' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item title="在编教师">
          <mu-radio  v-model="type" label="" labelLeft :nativeValue="1" uncheckIcon="favorite_border" checkedIcon="favorite" slot="right" />
        </mu-list-item>
        <mu-list-item title="聘用教师">
          <mu-radio  v-model="type" label="" labelLeft :nativeValue="2" uncheckIcon="favorite_border" checkedIcon="favorite" slot="right" />
        </mu-list-item>
        <mu-list-item title="外校教师">
          <mu-radio  v-model="type" label="" labelLeft :nativeValue="3" uncheckIcon="favorite_border" checkedIcon="favorite" slot="right" />
        </mu-list-item>
      </mu-list>
    </mu-drawer>
  </div>
</template>

<script>
import * as API from './API.js'
export default {
  name: 'Edit',
  data () {
    return {
      Edit_Able: true,
      Save_Able: false,
      Active_Able: false,
      Inactive_Able: false,
      Activing: false,
      Inactiving: false,
      Saving: false,
      Reading: true,
      openManager: false,
      openType: false,
      bottomPopup: false,
      icon: '',
      color: '',
      message: '',
      teacher: [],
      name: '',
      mobile: '',
      weixinId: '',
      qq: '',
      email: '',
      isManager: '',
      address: '',
      type: '',
      state: '',
      nameErrorText: '',
      nameErrorColor: '',
      mobileErrorText: '',
      mobileErrorColor: '',
      weixinIdErrorText: '',
      weixinIdErrorColor: '',
      qqErrorText: '',
      qqErrorColor: '',
      emailErrorText: '',
      emailErrorColor: ''
    }
  },
  created () {
    this.fetchData(this.$route.params.id)
  },
  watch: {
    // 如果路由有变化，会再次执行该方法
    '$route': 'fetchData'
  },
  computed: {
    saveAble: function () {
      if (this.nameErrorText.toString() === 'OK' && this.mobileErrorText.toString() === 'OK') {
        return false
      } else {
        return true
      }
    },
    manager: function () {
      if (this.isManager.toString() === '0') {
        return '一般教师'
      } else if (this.isManager.toString() === '1') {
        return '管理人员'
      } else {
        return ''
      }
    },
    types: function () {
      if (this.type.toString() === '1') {
        return '在编教师'
      } else if (this.type.toString() === '2') {
        return '聘用教师'
      } else if (this.type.toString() === '3') {
        return '外校教师'
      } else {
        return ''
      }
    }
  },
  methods: {
    goReply () {
      this.$router.push({ path: '/list' })
    },
    openActive () {
      this.Activing = true
    },
    openInactive () {
      this.Inactiving = true
    },
    openPopup (message, icon, color) {
      this.message = message
      this.icon = icon
      this.color = color
      this.bottomPopup = true
      setTimeout(() => { this.bottomPopup = false }, 1500)
    },
    goEdit () {
      this.Edit_Able = false
      this.Save_Able = true
    },
    goCancel () {
      this.Edit_Able = true
      this.Save_Able = false
      this.fetchData(this.$route.params.id)
    },
    goClose () {
      this.Activing = false
      this.Inactiving = false
    },
    goCall () {
      this.mobile.toString() === '' ? this.openPopup('无联系电话!', 'report_problem', 'orange') : window.location.href = 'tel:' + this.mobile
    },
    goActive () {
      this.$http.get(
        API.active,
        { params: { id: this.$route.params.id } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        if (response.body === 'error') {
          this.openPopup('请重新登录!', 'report_problem', 'red')
          window.location.href = '/'
        } else if (response.body === 'OK') {
          this.Edit_Able = true
          this.Save_Able = false
          this.Active_Able = true
          this.Inactive_Able = false
          this.openPopup('激活成功!', 'check_circle', 'green')
          setTimeout(() => { this.$router.push({ path: '/list' }) }, 1000)
        } else {
          this.Edit_Able = true
          this.Save_Able = false
          this.Active_Able = true
          this.Inactive_Able = false
          this.openPopup(response.body, 'report_problem', 'red')
          setTimeout(() => { this.$router.push({ path: '/list' }) }, 1000)
        }
      }, (response) => {
        this.openPopup('服务器内部错误!', 'error', 'red')
      })
    },
    goInactive () {
      this.$http.get(
        API.inactive,
        { params: { id: this.$route.params.id } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        if (response.body === 'error') {
          this.openPopup('请重新登录!', 'report_problem', 'red')
          window.location.href = '/'
        } else if (response.body === 'OK') {
          this.Edit_Able = true
          this.Save_Able = false
          this.Active_Able = false
          this.Inactive_Able = true
          this.openPopup('注销成功!', 'check_circle', 'green')
          setTimeout(() => { this.$router.push({ path: '/list' }) }, 1000)
        } else {
          this.Edit_Able = true
          this.Save_Able = false
          this.Active_Able = false
          this.Inactive_Able = true
          this.openPopup(response.body, 'report_problem', 'red')
          setTimeout(() => { this.$router.push({ path: '/list' }) }, 1000)
        }
      }, (response) => {
        this.openPopup('服务器内部错误!', 'error', 'red')
      })
    },
    goSave () {
      this.Saving = true
      this.$http.get(
        API.edit,
        { params: {
          id: this.$route.params.id,
          name: this.name,
          mobile: this.mobile,
          email: this.email,
          qq: this.qq,
          weixinId: this.weixinId,
          isManager: this.isManager,
          type: this.type
        } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        this.Saving = false
        if (response.body === 'error') {
          this.openPopup('请重新登录!', 'report_problem', 'orange')
          window.location.href = '/'
        } else if (response.body === 'OK') {
          this.openPopup('修改成功！', 'check_circle', 'green')
          setTimeout(() => { this.$router.push({ path: '/list' }) }, 1000)
        } else {
          this.openPopup(response.body, 'report_problem', 'orange')
        }
      }, (response) => {
        this.Saving = false
        this.openPopup('服务器内部错误!', 'error', 'red')
      })
    },
    fetchData (id) {
      this.$http.get(
        API.get,
        { params: { id: id } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        this.teacher = response.body
        this.name = this.teacher.name
        this.mobile = this.teacher.mobile
        this.email = this.teacher.email
        this.qq = this.teacher.qq
        this.weixinId = this.teacher.weixinId
        this.isManager = this.teacher.isManager
        this.type = this.teacher.type
        this.state = this.teacher.state
        this.address = this.teacher.address
        if (this.teacher.state.toString() === '0') {
          this.Active_Able = false
          this.Inactive_Able = true
        } else {
          this.Active_Able = true
          this.Inactive_Able = false
        }
        this.Reading = false
      }, (response) => {
      })
    },
    checkName (value) {
      if (value === null || value === undefined || value === '') {
        this.nameErrorText = '姓名为必填项!'
        this.nameErrorColor = 'orange'
      } else if (value.length > 20) {
        this.nameErrorText = '姓名不得超过20字符'
        this.nameErrorColor = 'orange'
      } else {
        this.$http.get(
          API.checkName,
          { params: { name: value } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          if (response.body === 'error') {
            this.openPopup('请重新登录!', 'report_problem', 'orange')
            window.location.href = '/'
          } else if (response.body === 'OK') {
            this.nameErrorText = 'OK'
            this.nameErrorColor = 'green'
          } else {
            this.nameErrorText = response.body
            this.nameErrorColor = 'red'
          }
        }, (response) => {
          this.openPopup('服务器内部错误!', 'error', 'red')
        })
      }
    },
    checkMobile (value) {
      if (value === null || value === undefined || value === '') {
        this.mobileErrorText = '手机号码为必填项!'
        this.mobileErrorColor = 'orange'
      } else if (!value.match(/^1(3|4|5|7|8)\d{9}$/)) {
        this.mobileErrorText = '手机号码格式错误!'
        this.mobileErrorColor = 'orange'
      } else {
        this.$http.get(
          API.checkMobileForEdit,
          { params: { id: this.$route.params.id, mobile: value } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          if (response.body === 'error') {
            this.openPopup('请重新登录!', 'report_problem', 'orange')
            window.location.href = '/'
          } else if (response.body === 'OK') {
            this.mobileErrorText = 'OK'
            this.mobileErrorColor = 'green'
          } else {
            this.mobileErrorText = response.body
            this.mobileErrorColor = 'red'
          }
        }, (response) => {
          this.openPopup('服务器内部错误!', 'error', 'red')
        })
      }
    },
    checkEmail (value) {
      if (value === null || value === undefined || value === '') {
        this.emailErrorText = ''
        this.emailErrorColor = ''
      } else if (!value.match(/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$/)) {
        this.emailErrorText = '电子邮箱格式错误!'
        this.emailErrorColor = 'orange'
      } else {
        this.$http.get(
          API.checkEmailForEdit,
          { params: { id: this.$route.params.id, email: value } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          if (response.body === 'error') {
            this.openPopup('请重新登录!', 'report_problem', 'orange')
            window.location.href = '/'
          } else if (response.body === 'OK') {
            this.emailErrorText = 'OK'
            this.emailErrorColor = 'green'
          } else {
            this.emailErrorText = response.body
            this.emailErrorColor = 'red'
          }
        }, (response) => {
          this.openPopup('服务器内部错误!', 'error', 'red')
        })
      }
    },
    checkQQ (value) {
      if (value === null || value === undefined || value === '') {
        this.qqErrorText = ''
        this.qqErrorColor = ''
      } else if (!value.match(/^\d{5,12}$/)) {
        this.qqErrorText = 'QQ格式错误!'
        this.qqErrorColor = 'orange'
      } else {
        this.$http.get(
          API.checkQQForEdit,
          { params: { id: this.$route.params.id, qq: value } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          if (response.body === 'error') {
            this.openPopup('请重新登录!', 'report_problem', 'orange')
            window.location.href = '/'
          } else if (response.body === 'OK') {
            this.qqErrorText = 'OK'
            this.qqErrorColor = 'green'
          } else {
            this.qqErrorText = response.body
            this.qqErrorColor = 'red'
          }
        }, (response) => {
          this.openPopup('服务器内部错误!', 'error', 'red')
        })
      }
    },
    checkWeixinId (value) {
      if (value === null || value === undefined || value === '') {
        this.weixinIdErrorText = ''
        this.weixinIdErrorColor = ''
      } else if (!value.match(/^[a-zA-Z]{1}[-_a-zA-Z0-9]{5,19}$/)) {
        this.weixinIdErrorText = '微信格式错误!'
        this.weixinIdErrorColor = 'orange'
      } else {
        this.$http.get(
          API.checkWeixinIdForEdit,
          { params: { id: this.$route.params.id, weixinId: value } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          if (response.body === 'error') {
            this.openPopup('请重新登录!', 'report_problem', 'orange')
            window.location.href = '/'
          } else if (response.body === 'OK') {
            this.weixinIdErrorText = 'OK'
            this.weixinIdErrorColor = 'green'
          } else {
            this.weixinIdErrorText = response.body
            this.vErrorColor = 'red'
          }
        }, (response) => {
          this.openPopup('服务器内部错误!', 'error', 'red')
        })
      }
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
