<template>
  <div>
    <mu-appbar title="学生详情">
      <mu-icon-button icon='reply' slot="right" @click="goReply"/>
    </mu-appbar>
    <mu-text-field :disabled="Edit_Able" v-model="name" label="姓名" icon="comment" :errorColor="nameErrorColor" :errorText="nameErrorText" @input="checkName" fullWidth labelFloat/><br/>
    <mu-text-field :disabled="Edit_Able" v-model="number" label="证件号码" icon="comment" :errorColor="numberErrorColor" :errorText="numberErrorText" @input="checkNumber" fullWidth labelFloat maxLength="18"/><br/>
    <mu-text-field :disabled="Edit_Able" v-model="code" label="学籍号码" icon="comment" :errorColor="codeErrorColor" :errorText="codeErrorText" @input="checkCode" fullWidth labelFloat maxLength="15"/><br/>
    <mu-text-field :disabled="Edit_Able" v-model="address" label="住址" icon="store_mall_directory" :errorColor="addressErrorColor" :errorText="addressErrorText" @input="checkAddress" fullWidth labelFloat/><br/>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-raised-button :disabled="Edit_Able" :label="roomName" @click="openRoom=true" primary/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-flexbox>
      <mu-flexbox-item class="flex-parent">
        <mu-flat-button v-if="parentAble1" label="爸爸" @click="openParent1=true" :icon="parentIcon1" :backgroundColor="parentBack1" color="#FFFFFF"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-parent">
        <mu-flat-button v-if="parentAble3" label="爷爷" @click="openParent3=true" :icon="parentIcon3" :backgroundColor="parentBack3" color="#FFFFFF"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-parent">
        <mu-flat-button v-if="parentAble5" label="外公" @click="openParent5=true" :icon="parentIcon5" :backgroundColor="parentBack5" color="#FFFFFF"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-flexbox>
      <mu-flexbox-item class="flex-parent">
        <mu-flat-button v-if="parentAble2" label="妈妈" @click="openParent2=true" :icon="parentIcon2" :backgroundColor="parentBack2" color="#FFFFFF"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-parent">
        <mu-flat-button v-if="parentAble4" label="奶奶" @click="openParent4=true" :icon="parentIcon4" :backgroundColor="parentBack4" color="#FFFFFF"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-parent">
        <mu-flat-button v-if="parentAble6" label="外婆" @click="openParent6=true" :icon="parentIcon6" :backgroundColor="parentBack6" color="#FFFFFF"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="edit" v-if="Edit_Able" @click="goEdit" primary/>
        <mu-float-button icon="cancel" v-if="Save_Able" @click="goCancel" secondary/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="delete" v-if="Inactive_Able && Edit_Able"  @click="openInactive" backgroundColor="red"/>
        <mu-float-button icon="compare_arrows" v-if="Active_Able && Edit_Able" @click="openActive" backgroundColor="green"/>
        <mu-float-button icon="done" v-if="Save_Able" :disabled="saveAble" @click="goSave" backgroundColor="green"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-drawer right :open="openParent1" docked="false">
      <mu-appbar :title="parentTitle1" @click.native="closeParent">
        <mu-icon-button icon='close' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item :title="parentName1">
          <mu-icon slot="left" value="account_box" color="#009688"/>
        </mu-list-item>
        <mu-list-item :title="parentUserId1">
          <mu-icon slot="left" value="assignment" color="#009688"/>
        </mu-list-item>
        <mu-list-item :title="parentMobile1" @click.native="callParent1">
          <mu-icon slot="left" value="phone" color="#009688"/>
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-drawer right :open="openParent2" docked="false">
      <mu-appbar :title="parentTitle2" @click.native="closeParent">
        <mu-icon-button icon='close' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item :title="parentName2">
          <mu-icon slot="left" value="account_circle" color="#ff5722"/>
        </mu-list-item>
        <mu-list-item :title="parentUserId2">
          <mu-icon slot="left" value="assignment" color="#ff5722"/>
        </mu-list-item>
        <mu-list-item :title="parentMobile2" @click.native="callParent2">
          <mu-icon slot="left" value="phone" color="#ff5722"/>
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-drawer right :open="openParent3" docked="false">
      <mu-appbar :title="parentTitle3" @click.native="closeParent">
        <mu-icon-button icon='close' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item :title="parentName3">
          <mu-icon slot="left" value="account_box" color="#4caf50"/>
        </mu-list-item>
        <mu-list-item :title="parentUserId3">
          <mu-icon slot="left" value="assignment" color="#4caf50"/>
        </mu-list-item>
        <mu-list-item :title="parentMobile3" @click.native="callParent3">
          <mu-icon slot="left" value="phone" color="#4caf50"/>
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-drawer right :open="openParent4" docked="false">
      <mu-appbar :title="parentTitle4" @click.native="closeParent">
        <mu-icon-button icon='close' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item :title="parentName4">
          <mu-icon slot="left" value="account_circle" color="#ff9800"/>
        </mu-list-item>
        <mu-list-item :title="parentUserId4">
          <mu-icon slot="left" value="assignment" color="#ff9800"/>
        </mu-list-item>
        <mu-list-item :title="parentMobile4" @click.native="callParent4">
          <mu-icon slot="left" value="phone" color="#ff9800"/>
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-drawer right :open="openParent5" docked="false">
      <mu-appbar :title="parentTitle5" @click.native="closeParent">
        <mu-icon-button icon='close' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item :title="parentName5">
          <mu-icon slot="left" value="account_box" color="#8bc34a"/>
        </mu-list-item>
        <mu-list-item :title="parentUserId5">
          <mu-icon slot="left" value="assignment" color="#8bc34a"/>
        </mu-list-item>
        <mu-list-item :title="parentMobile5" @click.native="callParent5">
          <mu-icon slot="left" value="phone" color="#8bc34a"/>
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-drawer right :open="openParent6" docked="false" >
      <mu-appbar :title="parentTitle6" @click.native="closeParent">
        <mu-icon-button icon='close' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item :title="parentName6">
          <mu-icon slot="left" value="account_circle" color="#ffc107"/>
        </mu-list-item>
        <mu-list-item :title="parentUserId6">
          <mu-icon slot="left" value="assignment" color="#ffc107"/>
        </mu-list-item>
        <mu-list-item :title="parentMobile6" @click.native="callParent6">
          <mu-icon slot="left" value="phone" color="#ffc107"/>
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-dialog :open="Reading" title="正在读取" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-dialog :open="Saving" title="正在保存" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-dialog :open="Inactiving" title="确定要注销该学生吗?" @close="goClose">
      <mu-flat-button label="取消" @click="goClose" />
      <mu-flat-button label="确定" @click="goInactive" secondary/>
    </mu-dialog>
    <mu-dialog :open="Activing" title="确定要激活该学生吗?" @close="goClose">
      <mu-flat-button label="取消" @click="goClose" />
      <mu-flat-button label="确定" @click="goActive" secondary/>
    </mu-dialog>
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
  name: 'Edit',
  data () {
    return {
      Edit_Able: true,
      Save_Able: false,
      Active_Able: false,
      Activing: false,
      Inactiving: false,
      Saving: false,
      Reading: true,
      bottomPopup: false,
      icon: '',
      color: '',
      message: '',
      name: '',
      number: '',
      code: '',
      address: '',
      state: '',
      roomName: '',
      parentAble1: false,
      parentAble2: false,
      parentAble3: false,
      parentAble4: false,
      parentAble5: false,
      parentAble6: false,
      openRoom: false,
      openParent1: false,
      openParent2: false,
      openParent3: false,
      openParent4: false,
      openParent5: false,
      openParent6: false,
      parentTitle1: '',
      parentTitle2: '',
      parentTitle3: '',
      parentTitle4: '',
      parentTitle5: '',
      parentTitle6: '',
      parentName1: '',
      parentName2: '',
      parentName3: '',
      parentName4: '',
      parentName5: '',
      parentName6: '',
      parentUserId1: '',
      parentUserId2: '',
      parentUserId3: '',
      parentUserId4: '',
      parentUserId5: '',
      parentUserId6: '',
      parentMobile1: '',
      parentMobile2: '',
      parentMobile3: '',
      parentMobile4: '',
      parentMobile5: '',
      parentMobile6: '',
      parentIcon1: 'account_box',
      parentIcon2: 'account_circle',
      parentIcon3: 'account_box',
      parentIcon4: 'account_circle',
      parentIcon5: 'account_box',
      parentIcon6: 'account_circle',
      parentBack1: '#009688',
      parentBack2: '#ff5722',
      parentBack3: '#4caf50',
      parentBack4: '#ff9800',
      parentBack5: '#8bc34a',
      parentBack6: '#ffc107',
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
  created () {
    this.fetchData(this.$route.params.id)
  },
  watch: {
    // 如果路由有变化，会再次执行该方法
    '$route': 'fetchData'
  },
  computed: {
    saveAble: function () {
      if (this.nameErrorText.toString() === 'OK' && this.numberErrorText.toString() === 'OK' && this.codeErrorText.toString() === 'OK' && this.addressErrorText.toString() === 'OK' && this.roomName.toString() !== '请选择班级') {
        return false
      } else {
        return true
      }
    },
    parentTitle1: function () {
      return this.name + '的爸爸'
    },
    parentTitle2: function () {
      return this.name + '的妈妈'
    },
    parentTitle3: function () {
      return this.name + '的爷爷'
    },
    parentTitle4: function () {
      return this.name + '的奶奶'
    },
    parentTitle5: function () {
      return this.name + '的外公'
    },
    parentTitle6: function () {
      return this.name + '的外婆'
    }
  },
  methods: {
    closeParent () {
      this.openParent1 = false
      this.openParent2 = false
      this.openParent3 = false
      this.openParent4 = false
      this.openParent5 = false
      this.openParent6 = false
    },
    callParent1 () {
      this.parentMobile1.toString() === '' ? this.openPopup('无联系电话!', 'report_problem', 'orange') : window.location.href = 'tel:' + this.parentMobile1
    },
    callParent2 () {
      this.parentMobile2.toString() === '' ? this.openPopup('无联系电话!', 'report_problem', 'orange') : window.location.href = 'tel:' + this.parentMobile2
    },
    callParent3 () {
      this.parentMobile3.toString() === '' ? this.openPopup('无联系电话!', 'report_problem', 'orange') : window.location.href = 'tel:' + this.parentMobile3
    },
    callParent4 () {
      this.parentMobile4.toString() === '' ? this.openPopup('无联系电话!', 'report_problem', 'orange') : window.location.href = 'tel:' + this.parentMobile4
    },
    callParent5 () {
      this.parentMobile5.toString() === '' ? this.openPopup('无联系电话!', 'report_problem', 'orange') : window.location.href = 'tel:' + this.parentMobile5
    },
    callParent6 () {
      this.parentMobile6.toString() === '' ? this.openPopup('无联系电话!', 'report_problem', 'orange') : window.location.href = 'tel:' + this.parentMobile6
    },
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
          this.openPopup('激活成功!', 'check_circle', 'green')
          setTimeout(() => { this.$router.push({ path: '/list' }) }, 1000)
        } else if (response.body === '要激活的学生不存在!') {
          this.Edit_Able = true
          this.Save_Able = false
          this.Active_Able = true
          this.openPopup(response.body, 'report_problem', 'red')
          setTimeout(() => { this.$router.push({ path: '/list' }) }, 1000)
        } else {
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
          this.Inactive_Able = true
          this.openPopup('注销成功!', 'check_circle', 'green')
          setTimeout(() => { this.$router.push({ path: '/list' }) }, 1000)
        } else if (response.body === '要注销的学生不存在!') {
          this.Edit_Able = true
          this.Save_Able = false
          this.Inactive_Able = true
          this.openPopup(response.body, 'report_problem', 'red')
          setTimeout(() => { this.$router.push({ path: '/list' }) }, 1000)
        } else {
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
        this.name = response.body.student[0].name
        this.number = response.body.student[0].number
        this.code = response.body.student[0].code
        this.address = response.body.student[0].address
        this.state = response.body.student[0].state
        this.roomName = response.body.student[0].roomName
        this.list = response.body.rooms
        this.parentName1 = response.body.parent[0].parentName
        this.parentName2 = response.body.parent[1].parentName
        this.parentName3 = response.body.parent[2].parentName
        this.parentName4 = response.body.parent[3].parentName
        this.parentName5 = response.body.parent[4].parentName
        this.parentName6 = response.body.parent[5].parentName
        this.parentUserId1 = response.body.parent[0].parentUserId
        this.parentUserId2 = response.body.parent[1].parentUserId
        this.parentUserId3 = response.body.parent[2].parentUserId
        this.parentUserId4 = response.body.parent[3].parentUserId
        this.parentUserId5 = response.body.parent[4].parentUserId
        this.parentUserId6 = response.body.parent[5].parentUserId
        this.parentMobile1 = response.body.parent[0].parentMobile
        this.parentMobile2 = response.body.parent[1].parentMobile
        this.parentMobile3 = response.body.parent[2].parentMobile
        this.parentMobile4 = response.body.parent[3].parentMobile
        this.parentMobile5 = response.body.parent[4].parentMobile
        this.parentMobile6 = response.body.parent[5].parentMobile
        this.parentAble1 = response.body.parent[0].parentAble
        this.parentAble2 = response.body.parent[1].parentAble
        this.parentAble3 = response.body.parent[2].parentAble
        this.parentAble4 = response.body.parent[3].parentAble
        this.parentAble5 = response.body.parent[4].parentAble
        this.parentAble6 = response.body.parent[5].parentAble
        if (response.body.student[0].state.toString() === '1') {
          this.Inactive_Able = true
          this.Active_Able = false
        } else {
          this.Inactive_Able = false
          this.Active_Able = true
        }
        this.Reading = false
      }, (response) => {
      })
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
        this.numberErrorText = '证件号码为必填项!'
        this.numberErrorColor = 'orange'
      } else if (!/\d{17}[0-9,X]/.test(value)) {
        this.numberErrorText = '证件号码应为18位字符'
        this.numberErrorColor = 'orange'
      } else {
        this.$http.get(
          API.checkNumberForEdit,
          { params: { id: this.$route.params.id, number: value } },
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
          API.checkCodeForEdit,
          { params: { id: this.$route.params.id, code: value } },
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
