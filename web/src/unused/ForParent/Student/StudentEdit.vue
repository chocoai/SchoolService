<template>
  <div class="StudentEdit">
    <mu-appbar title="信息详情">
      <mu-icon-button icon='reply' slot="right" @click="goReply"/>
    </mu-appbar>
    <mu-text-field disabled v-model="name" label="姓名" icon="comment" fullWidth labelFloat/><br/>
    <mu-text-field disabled v-model="number" label="证件号码" icon="comment" fullWidth labelFloat maxLength="18"/><br/>
    <mu-text-field disabled v-model="code" label="学籍号码" icon="comment" fullWidth labelFloat maxLength="15"/><br/>
    <mu-text-field :disabled="edit" v-model="address" label="住址" icon="store_mall_directory" :errorColor="addressErrorColor" :errorText="addressErrorText" @input="checkAddress" fullWidth labelFloat/><br/>
    <mu-flexbox>
      <mu-flexbox-item class="flex-parent">
        <mu-flat-button disabled :label="roomName" :icon="roomIcon" :backgroundColor="roomBack" color="#FFFFFF"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-parent">
        <mu-flat-button disabled :label="teamName" :icon="teamIcon" :backgroundColor="teamBack" color="#FFFFFF"/>
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
        <mu-float-button icon="edit" v-if="edit" @click="goEdit" primary/>
        <mu-float-button icon="cancel" v-if="save" @click="goCancel" secondary/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button v-if="edit" icon="delete" @click="forDelete=true" backgroundColor="red"/>
        <mu-float-button v-if="save" icon="done" @click="goSave" backgroundColor="green"/>
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
    <mu-dialog :open="forSave" title="正在保存" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-dialog :open="forRead" title="正在读取" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
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
import * as API from './StudentAPI.js'
export default {
  name: 'StudentEdit',
  data () {
    return {
      bottomPopup: false,
      edit: true,
      save: false,
      forSave: false,
      forRead: true,
      forDelete: false,
      deleteTitle: '',
      icon: '',
      color: '',
      name: '',
      number: '',
      code: '',
      address: '',
      identity_id: '1',
      room_id: '0',
      team_id: '0',
      roomName: '班级未设置',
      teamName: '社团未设置',
      message: '',
      parents: '',
      parentAble1: false,
      parentAble2: false,
      parentAble3: false,
      parentAble4: false,
      parentAble5: false,
      parentAble6: false,
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
      roomIcon: 'bookmark_border',
      teamIcon: 'bookmark_border',
      parentIcon1: 'account_box',
      parentIcon2: 'account_circle',
      parentIcon3: 'account_box',
      parentIcon4: 'account_circle',
      parentIcon5: 'account_box',
      parentIcon6: 'account_circle',
      roomBack: '#66CCCC',
      teamBack: '#66CCCC',
      parentBack1: '#009688',
      parentBack2: '#ff5722',
      parentBack3: '#4caf50',
      parentBack4: '#ff9800',
      parentBack5: '#8bc34a',
      parentBack6: '#ffc107',
      addressErrorText: '',
      addressErrorColor: '',
      saveAble: true
    }
  },
  created () {
    this.fetchData(this.$route.params.studentId)
    this.getParent(this.$route.params.studentId)
  },
  watch: {
    // 如果路由有变化，会再次执行该方法
    '$route': 'fetchData'
  },
  computed: {
    saveAble: function () {
      if (this.addressErrorText.toString() === 'OK') {
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
    },
    roomBack: function () {
      if (this.room_id.toString() !== '0') {
        return '#9999CC'
      } else {
        return '#66CCCC'
      }
    },
    teamBack: function () {
      if (this.team_id.toString() !== '0') {
        return '#9999CC'
      } else {
        return '#66CCCC'
      }
    },
    roomIcon: function () {
      if (this.room_id.toString() !== '0') {
        return 'bookmark'
      } else {
        return 'bookmark_border'
      }
    },
    teamIcon: function () {
      if (this.team_id.toString() !== '0') {
        return 'bookmark'
      } else {
        return 'bookmark_border'
      }
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
      this.$router.push({ path: '/studentList' })
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
    },
    goCancel () {
      this.edit = true
      this.save = false
      this.fetchData(this.$route.params.studentId)
    },
    goClose () {
      this.forDelete = false
    },
    fetchData (studentId) {
      this.$http.get(
        API.GetById,
        { params: { id: studentId } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
      ).then((response) => {
        this.student = response.body
        this.name = this.student.name
        this.number = this.student.number
        this.code = this.student.code
        this.address = this.student.address
        this.state = this.student.state
        this.deleteTitle = '确认要解除您与' + this.name + '的关系吗?'
        if (this.student.room_id === null || this.student.room_id === undefined || this.student.room_id === '' || this.student.room_id === 'undefined') {
          this.room_id = '0'
          this.roomName = '班级未设置'
        } else {
          this.room_id = this.student.room_id
          this.$http.get(
            API.GetRoomName,
            { params: { id: this.room_id } },
            { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
          ).then((response) => {
            this.roomName = response.body
          }, (response) => {
            this.openPopup('服务器内部错误！', 'error', 'red')
          })
        }
        if (this.student.team_id === null || this.student.team_id === undefined || this.student.team_id === '' || this.student.team_id === 'undefined') {
          this.team_id = '0'
          this.teamName = '社团未设置'
        } else {
          this.team_id = this.student.team_id
          this.$http.get(
            API.GetTeamName,
            { params: { id: this.team_id } },
            { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
          ).then((response) => {
            this.teamName = response.body
          }, (response) => {
            this.openPopup('服务器内部错误！', 'error', 'red')
          })
        }
      }, (response) => {
        this.openPopup('服务器内部错误！', 'error', 'red')
      })
    },
    getParent (studentId) {
      this.$http.get(
        API.GetParent,
        { params: { studentId: studentId } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
      ).then((response) => {
        /* eslint-disable no-eval  */
        this.parents = eval('(' + response.body + ')')
        this.parentName1 = this.parents.parentName1
        this.parentName2 = this.parents.parentName2
        this.parentName3 = this.parents.parentName3
        this.parentName4 = this.parents.parentName4
        this.parentName5 = this.parents.parentName5
        this.parentName6 = this.parents.parentName6
        this.parentUserId1 = this.parents.parentUserId1
        this.parentUserId2 = this.parents.parentUserId2
        this.parentUserId3 = this.parents.parentUserId3
        this.parentUserId4 = this.parents.parentUserId4
        this.parentUserId5 = this.parents.parentUserId5
        this.parentUserId6 = this.parents.parentUserId6
        this.parentMobile1 = this.parents.parentMobile1
        this.parentMobile2 = this.parents.parentMobile2
        this.parentMobile3 = this.parents.parentMobile3
        this.parentMobile4 = this.parents.parentMobile4
        this.parentMobile5 = this.parents.parentMobile5
        this.parentMobile6 = this.parents.parentMobile6
        this.parentAble1 = this.parents.parentAble1
        this.parentAble2 = this.parents.parentAble2
        this.parentAble3 = this.parents.parentAble3
        this.parentAble4 = this.parents.parentAble4
        this.parentAble5 = this.parents.parentAble5
        this.parentAble6 = this.parents.parentAble6
        this.forRead = false
      }, (response) => {
        this.openPopup('服务器内部错误!', 'error', 'red')
      })
    },
    goSave () {
      this.forSave = true
      this.$http.get(
        API.Edit,
        { params: { id: this.$route.params.studentId, address: this.address } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
      ).then((response) => {
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
    goDelete () {
      this.forSave = true
      this.$http.get(
        API.Delete,
        { params: { id: this.$route.params.studentId } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
      ).then((response) => {
        this.forSave = false
        if (response.body === 'error') {
          this.openPopup('请重新登录!', 'report_problem', 'orange')
          window.location.href = '/'
        } else if (response.body === 'OK') {
          this.openPopup('解除成功！', 'check_circle', 'green')
          setTimeout(() => { this.$router.push({ path: '/studentList' }) }, 1000)
        } else {
          this.openPopup(response.body, 'report_problem', 'orange')
        }
      }, (response) => {
        this.forSave = false
        this.openPopup('服务器内部错误!', 'error', 'red')
      })
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
    height: 170px;
    text-align: center;
    line-height: 32px;
  }
  .flex-parent {
    height: 50px;
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
