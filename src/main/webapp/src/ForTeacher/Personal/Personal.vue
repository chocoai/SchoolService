<template>
  <div class="Personal">
    <mu-appbar title="个人中心">
      <mu-icon-button icon='menu' slot="left" @click="openMenu"/>
    </mu-appbar>
    <mu-text-field label="姓名" disabled underlineShow="false" v-model="name" fullWidth labelFloat icon="person"/><br/>
    <mu-text-field label="账号" disabled underlineShow="false" v-model="userId" fullWidth labelFloat icon="assignment"/><br/>
    <mu-text-field label="手机" :disabled="edit" underlineShow="false" v-model="mobile" :errorColor="mobileErrorColor" :errorText="mobileErrorText" @input="checkMobile"  fullWidth labelFloat icon="phone" maxLength="11"/><br/>
    <mu-flexbox v-if="edit">
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="edit" v-if="edit" @click="goEdit" primary/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-flexbox v-if="save">
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="cancel" v-if="save" @click="goCancel" secondary/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="done" v-if="save" :disabled="saveAble" @click="goSave" backgroundColor="green"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <menuList :open="open" v-on:closeMenu="closeMenu"></menuList>
    <mu-dialog :open="forSave" title="正在保存" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
  </div>
</template>
<script>
  import MenuList from '../Menu/MenuList'
  export default {
    name: 'Personal',
    components: {
      'menuList': MenuList
    },
    data () {
      return {
        open: false,
        bottomPopup: false,
        edit: true,
        save: false,
        forSave: false,
        icon: '',
        color: '',
        message: '',
        teacher: '',
        name: '',
        mobile: '',
        userId: '',
        mobileErrorText: '',
        mobileErrorColor: '',
        saveAble: true
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
      saveAble: function () {
        if (this.mobileErrorText.toString() === 'OK') {
          return false
        } else {
          return true
        }
      },
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
      goEdit () {
        this.edit = false
        this.save = true
        this.$http.get(
          API.Update,
          { params: { userId: this.userId } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
        ).then((response) => {
        }, (response) => {
        })
      },
      goCancel () {
        this.edit = true
        this.save = false
        this.fetchData(this.$route.params.teacherId)
      },
      goSave () {
        this.forSave = true
        this.$http.get(
          API.Edit,
          { params: {
            id: this.$route.params.teacherId,
            mobile: this.mobile
          } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          this.forSave = false
          if (response.body === 'error') {
            this.openPopup('请重新登录!', 'report_problem', 'orange')
            window.location.href = '/'
          } else if (response.body === 'OK') {
            this.openPopup('修改成功！', 'check_circle', 'green')
            this.edit = true
          } else {
            this.openPopup(response.body, 'report_problem', 'orange')
          }
        }, (response) => {
          this.forSave = false
          this.openPopup('服务器内部错误!', 'error', 'red')
        })
      },
      fetchData (teacherId) {
        this.$http.get(
          API.GetById,
          { params: { id: teacherId } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          this.teacher = response.body
          this.name = this.teacher.name
          this.mobile = this.teacher.mobile
          this.userId = this.teacher.userId
        }, (response) => {
        })
      },
      checkMobile (value) {
        if (value === null || value === undefined || value === '') {
          this.mobileErrorText = '手机号码为必填项!'
          this.mobileErrorColor = 'orange'
        } else if (!/^1(3|4|5|7|8)\d{9}$/.test(value)) {
          this.mobileErrorText = '手机号码格式错误!'
          this.mobileErrorColor = 'orange'
        } else {
          this.$http.get(
            API.CheckMobileForEdit,
            { params: {
              id: this.$route.params.teacherId,
              mobile: value
            } },
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
      }
    }
  }
</script>
<style>
  .flex-demo {
    height: 180px;
    text-align: center;
    line-height: 32px;
  }
</style>
