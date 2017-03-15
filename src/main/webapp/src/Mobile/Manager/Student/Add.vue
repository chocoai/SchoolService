<template>
  <div>
    <mu-appbar title="添加新学生">
      <mu-icon-button icon='reply' slot="right" @click="gorReply"/>
    </mu-appbar>
    <mu-text-field v-model="name" label="姓名" icon="comment" :errorColor="nameErrorColor" :errorText="nameErrorText" @input="checkName" fullWidth labelFloat/><br/>
    <mu-text-field v-model="number" label="证件号码" icon="comment" :errorColor="numberErrorColor" :errorText="numberErrorText" @input="checkNumber" fullWidth labelFloat maxLength="18"/><br/>
    <mu-text-field v-model="code" label="学籍号码" icon="comment" :errorColor="codeErrorColor" :errorText="codeErrorText" @input="checkCode" fullWidth labelFloat maxLength="15"/><br/>
    <mu-text-field v-model="address" label="住址" icon="store_mall_directory" :errorColor="addressErrorColor" :errorText="addressErrorText" @input="checkAddress" fullWidth labelFloat/><br/>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-flat-button :label="roomName" @click="openRoom=true" primary/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-dialog :open="Saving" title="正在保存" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-dialog :open="Reading" title="正在读取" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="cached" @click="goReset" backgroundColor="orange"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button :disabled="Save_Able" icon="save" @click="goSave" backgroundColor="green"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-popup position="bottom" :overlay="false" popupClass="popup-bottom" :open="bottomPopup">
      <mu-icon :value="icon" :size="36" :color="color"/>&nbsp;{{ message }}
    </mu-popup>
    <mu-drawer right :open="openRoom" docked="false">
      <mu-appbar title="请选择所属班级"/>
      <mu-list :value="room_id" @itemClick="roomChange">
        <mu-list-item v-for="room in list" :title="room.name" :value="room.id" :describeText="room.slogan" :afterText="getState(room.state)">
        </mu-list-item>
      </mu-list>
    </mu-drawer>
  </div>
</template>

<script>
import * as API from './API.js'
export default {
  name: 'Add',
  data () {
    return {
      bottomPopup: false,
      Saving: false,
      Reading: true,
      Save_Able: true,
      openRoom: false,
      icon: '',
      color: '',
      name: '',
      number: '',
      code: '',
      address: '',
      message: '',
      nameErrorText: '',
      nameErrorColor: '',
      numberErrorText: '',
      numberErrorColor: '',
      codeErrorText: '',
      codeErrorColor: '',
      addressErrorText: '',
      addressErrorColor: '',
      list: []
    }
  },
  created: function () {
    this.$http.get(
      API.list,
      { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
    ).then((response) => {
      this.list = response.body
      this.Reading = false
    }, (response) => {
      this.openPopup('服务器内部错误！', 'error', 'red')
    })
  },
  computed: {
    Save_Able: function () {
      if (this.nameErrorText.toString() === 'OK' && this.numberErrorText.toString() === 'OK' && this.codeErrorText.toString() === 'OK' && this.addressErrorText.toString() === 'OK') {
        return false
      } else {
        return true
      }
    }
  },
  methods: {
    gorReply () {
      this.$router.push({ path: '/list' })
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
      this.room_id = ''
      this.roomName = '请选择班级'
    },
    roomChange (val) {
      this.room_id = val.value
      this.roomName = val.title
      this.openRoom = false
    },
    getState (state) {
      if (state.toString() === '1') {
        return '可用'
      } else if (state.toString() === '0') {
        return '停用'
      } else {
        return '状态错误'
      }
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
          API.CheckNumberForAdd,
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
          API.CheckCodeForAdd,
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
      this.Saving = true
      this.$http.get(
        API.save,
        { params: {
          name: this.name,
          number: this.number,
          code: this.code,
          address: this.address,
          room_id: this.room_id
        } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        this.Saving = false
        if (response.body === 'error') {
          this.openPopup('请重新登录!', 'report_problem', 'orange')
          window.location.href = '/'
        } else if (response.body === 'OK') {
          this.openPopup('保存成功！', 'check_circle', 'green')
          setTimeout(() => { this.$router.push({ path: '/list' }) }, 1000)
        } else {
          this.openPopup(response.body, 'report_problem', 'orange')
        }
      }, (response) => {
        this.Saving = false
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
