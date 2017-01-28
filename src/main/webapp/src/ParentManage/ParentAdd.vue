<template>
  <div class="ParentAdd">
    <mu-appbar title="请核实后输入以下信息">
      <mu-icon-button icon='reply' slot="right" @click="goReply"/>
    </mu-appbar>
    <mu-text-field label="姓名" underlineShow="false" v-model="name" :errorColor="nameErrorColor" :errorText="nameErrorText" @input="checkName" fullWidth labelFloat icon="person"/><br/>
    <mu-text-field label="账号" underlineShow="false" v-model="userId" :errorColor="userIdErrorColor" :errorText="userIdErrorText" @input="checkUserId" fullWidth labelFloat icon="assignment" disabled/><br/>
    <mu-text-field label="手机" underlineShow="false" v-model="mobile" :errorColor="mobileErrorColor" :errorText="mobileErrorText" @input="checkMobile" fullWidth labelFloat icon="phone" maxLength="11"/><br/>
    <mu-dialog :open="forSave" title="正在保存" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo1">
        <mu-flat-button v-if="before" icon="navigate_before" @click="goBefore" />
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo1">
        <mu-flat-button :label="studentNumberLabel" class="demo-flat-button" primary disabled/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo1">
        <mu-flat-button v-if="next" labelPosition="before" icon="navigate_next" @click="goNext" />
      </mu-flexbox-item>
    </mu-flexbox>
    <div v-if="studentNumber1">
      <mu-flexbox>
        <mu-flexbox-item class="flex-demo">
          <mu-flat-button :disabled="roomAble1" :label="roomName1" @click="openRoom1=true" :icon="roomIcon1" :backgroundColor="roomBack1" color="#FFFFFF"/>
        </mu-flexbox-item>
      </mu-flexbox>
      <mu-flexbox>
        <mu-flexbox-item class="flex-demo">
          <mu-flat-button :disabled="studentAble1" :label="studentName1" @click="openStudent1=true" :icon="studentIcon1" :backgroundColor="studentBack1" color="#FFFFFF"/>
        </mu-flexbox-item>
      </mu-flexbox>
      <mu-flexbox>
        <mu-flexbox-item class="flex-demo">
          <mu-flat-button :disabled="identityAble1" :label="identityName1" @click="openIdentity1=true" :icon="identityIcon1" :backgroundColor="identityBack1" color="#FFFFFF"/>
        </mu-flexbox-item>
      </mu-flexbox>
    </div>
    <div v-if="studentNumber2">
      <mu-flexbox>
        <mu-flexbox-item class="flex-demo">
          <mu-flat-button :disabled="roomAble2" :label="roomName2" @click="openRoom2=true" :icon="roomIcon2" :backgroundColor="roomBack2" color="#FFFFFF"/>
        </mu-flexbox-item>
      </mu-flexbox>
      <mu-flexbox>
        <mu-flexbox-item class="flex-demo">
          <mu-flat-button :disabled="studentAble2" :label="studentName2" @click="openStudent2=true" :icon="studentIcon2" :backgroundColor="studentBack2" color="#FFFFFF"/>
        </mu-flexbox-item>
      </mu-flexbox>
      <mu-flexbox>
        <mu-flexbox-item class="flex-demo">
          <mu-flat-button :disabled="identityAble2" :label="identityName2" @click="openIdentity2=true" :icon="identityIcon2" :backgroundColor="identityBack2" color="#FFFFFF"/>
        </mu-flexbox-item>
      </mu-flexbox>
    </div>
    <div v-if="studentNumber3">
      <mu-flexbox>
        <mu-flexbox-item class="flex-demo">
          <mu-flat-button :disabled="roomAble3" :label="roomName3" @click="openRoom3=true" :icon="roomIcon3" :backgroundColor="roomBack3" color="#FFFFFF"/>
        </mu-flexbox-item>
      </mu-flexbox>
      <mu-flexbox>
        <mu-flexbox-item class="flex-demo">
          <mu-flat-button :disabled="studentAble3" :label="studentName3" @click="openStudent3=true" :icon="studentIcon3" :backgroundColor="studentBack3" color="#FFFFFF"/>
        </mu-flexbox-item>
      </mu-flexbox>
      <mu-flexbox>
        <mu-flexbox-item class="flex-demo">
          <mu-flat-button :disabled="identityAble3" :label="identityName3" @click="openIdentity3=true" :icon="identityIcon3" :backgroundColor="identityBack3" color="#FFFFFF"/>
        </mu-flexbox-item>
      </mu-flexbox>
    </div>
    <div v-if="studentNumber4">
      <mu-flexbox>
        <mu-flexbox-item class="flex-demo">
          <mu-flat-button :disabled="roomAble4" :label="roomName4" @click="openRoom4=true" :icon="roomIcon4" :backgroundColor="roomBack4" color="#FFFFFF"/>
        </mu-flexbox-item>
      </mu-flexbox>
      <mu-flexbox>
        <mu-flexbox-item class="flex-demo">
          <mu-flat-button :disabled="studentAble4" :label="studentName4" @click="openStudent4=true" :icon="studentIcon4" :backgroundColor="studentBack4" color="#FFFFFF"/>
        </mu-flexbox-item>
      </mu-flexbox>
      <mu-flexbox>
        <mu-flexbox-item class="flex-demo">
          <mu-flat-button :disabled="identityAble4" :label="identityName4" @click="openIdentity4=true" :icon="identityIcon4" :backgroundColor="identityBack4" color="#FFFFFF"/>
        </mu-flexbox-item>
      </mu-flexbox>
    </div>
    <mu-dialog :open="forSave" title="正在保存" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="cached" @click="goReset" backgroundColor="orange"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="done" @click="goSave" backgroundColor="green"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-popup position="bottom" :overlay="false" popupClass="popup-bottom" :open="bottomPopup">
      <mu-icon :value="icon" :size="36" :color="color"/>&nbsp;{{ message }}
    </mu-popup>
    <mu-drawer right :open="openRoom1" docked="false">
      <mu-appbar title="请选择所属班级" @click.native="closeRoom1">
        <mu-icon-button icon='done' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item title="清空" @click.native="room_id1=''">
          <mu-icon slot="left" value="delete_forever" :size="40"/>
        </mu-list-item>
        <mu-list-item v-for="room in rooms" :title="room.name">
          <mu-icon v-if="room.state.toString() === '1'" slot="left" :size="40" value="store" color="#9c27b0"/>
          <mu-icon v-if="room.state.toString() === '2'" slot="left" :size="40" value="store" color="#ce93d8"/>
          <mu-radio v-model="room_id1" label="" labelLeft :nativeValue="room.id" uncheckIcon="favorite_border" checkedIcon="favorite" slot="right" iconClass="color: #215E21"/>
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-drawer right :open="openRoom2" docked="false">
      <mu-appbar title="请选择所属班级" @click.native="closeRoom2">
        <mu-icon-button icon='done' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item title="清空" @click.native="room_id2=''">
          <mu-icon slot="left" value="delete_forever" :size="40"/>
        </mu-list-item>
        <mu-list-item v-for="room in rooms" :title="room.name">
          <mu-icon v-if="room.state.toString() === '1'" slot="left" :size="40" value="store" color="#9c27b0"/>
          <mu-icon v-if="room.state.toString() === '2'" slot="left" :size="40" value="store" color="#ce93d8"/>
          <mu-radio v-model="room_id2" label="" labelLeft :nativeValue="room.id" uncheckIcon="favorite_border" checkedIcon="favorite" slot="right" iconClass="color: #215E21"/>
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-drawer right :open="openRoom3" docked="false">
      <mu-appbar title="请选择所属班级" @click.native="closeRoom3">
        <mu-icon-button icon='done' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item title="清空" @click.native="room_id3=''">
          <mu-icon slot="left" value="delete_forever" :size="40"/>
        </mu-list-item>
        <mu-list-item v-for="room in rooms" :title="room.name">
          <mu-icon v-if="room.state.toString() === '1'" slot="left" :size="40" value="store" color="#9c27b0"/>
          <mu-icon v-if="room.state.toString() === '2'" slot="left" :size="40" value="store" color="#ce93d8"/>
          <mu-radio v-model="room_id3" label="" labelLeft :nativeValue="room.id" uncheckIcon="favorite_border" checkedIcon="favorite" slot="right" iconClass="color: #215E21"/>
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-drawer right :open="openRoom4" docked="false">
      <mu-appbar title="请选择所属班级" @click.native="closeRoom4">
        <mu-icon-button icon='done' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item title="清空" @click.native="room_id4=''">
          <mu-icon slot="left" value="delete_forever" :size="40"/>
        </mu-list-item>
        <mu-list-item v-for="room in rooms" :title="room.name">
          <mu-icon v-if="room.state.toString() === '1'" slot="left" :size="40" value="store" color="#9c27b0"/>
          <mu-icon v-if="room.state.toString() === '2'" slot="left" :size="40" value="store" color="#ce93d8"/>
          <mu-radio v-model="room_id4" label="" labelLeft :nativeValue="room.id" uncheckIcon="favorite_border" checkedIcon="favorite" slot="right" iconClass="color: #215E21"/>
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-drawer right :open="openStudent1" docked="false">
      <mu-appbar title="请选择学生" @click.native="closeStudent1">
        <mu-icon-button icon='done' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item title="清空" @click.native="student_id1=''">
          <mu-icon slot="left" value="delete_forever" :size="40"/>
        </mu-list-item>
        <mu-list-item v-for="student in students1" :title="student.name" :describeText="student.number">
          <mu-icon v-if="student.state.toString() === '1' && student.sex.toString() === '1'" slot="left" :size="40" value="account_box" color="#00bcd4"/>
          <mu-icon v-if="student.state.toString() === '1' && student.sex.toString() === '2'" slot="left" :size="40" value="account_circle" color="#e91e63"/>
          <mu-icon v-if="student.state.toString() === '2' && student.sex.toString() === '1'" slot="left" :size="40" value="account_box" color="#b2ebf2"/>
          <mu-icon v-if="student.state.toString() === '2' && student.sex.toString() === '2'" slot="left" :size="40" value="account_circle" color="#f8bbd0"/>
          <mu-radio v-model="student_id1" label="" labelLeft :nativeValue="student.id" uncheckIcon="favorite_border" checkedIcon="favorite" slot="right"/>
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-drawer right :open="openStudent2" docked="false">
      <mu-appbar title="请选择学生" @click.native="closeStudent2">
        <mu-icon-button icon='done' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item title="清空" @click.native="student_id2=''">
          <mu-icon slot="left" value="delete_forever" :size="40"/>
        </mu-list-item>
        <mu-list-item v-for="student in students2" :title="student.name" :describeText="student.number">
          <mu-icon v-if="student.state.toString() === '1' && student.sex.toString() === '1'" slot="left" :size="40" value="account_box" color="#00bcd4"/>
          <mu-icon v-if="student.state.toString() === '1' && student.sex.toString() === '2'" slot="left" :size="40" value="account_circle" color="#e91e63"/>
          <mu-icon v-if="student.state.toString() === '2' && student.sex.toString() === '1'" slot="left" :size="40" value="account_box" color="#b2ebf2"/>
          <mu-icon v-if="student.state.toString() === '2' && student.sex.toString() === '2'" slot="left" :size="40" value="account_circle" color="#f8bbd0"/>
          <mu-radio v-model="student_id2" label="" labelLeft :nativeValue="student.id" uncheckIcon="favorite_border" checkedIcon="favorite" slot="right"/>
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-drawer right :open="openStudent3" docked="false">
      <mu-appbar title="请选择学生" @click.native="closeStudent3">
        <mu-icon-button icon='done' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item title="清空" @click.native="student_id3=''">
          <mu-icon slot="left" value="delete_forever" :size="40"/>
        </mu-list-item>
        <mu-list-item v-for="student in students3" :title="student.name" :describeText="student.number">
          <mu-icon v-if="student.state.toString() === '1' && student.sex.toString() === '1'" slot="left" :size="40" value="account_box" color="#00bcd4"/>
          <mu-icon v-if="student.state.toString() === '1' && student.sex.toString() === '2'" slot="left" :size="40" value="account_circle" color="#e91e63"/>
          <mu-icon v-if="student.state.toString() === '2' && student.sex.toString() === '1'" slot="left" :size="40" value="account_box" color="#b2ebf2"/>
          <mu-icon v-if="student.state.toString() === '2' && student.sex.toString() === '2'" slot="left" :size="40" value="account_circle" color="#f8bbd0"/>
          <mu-radio v-model="student_id3" label="" labelLeft :nativeValue="student.id" uncheckIcon="favorite_border" checkedIcon="favorite" slot="right"/>
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-drawer right :open="openStudent4" docked="false">
      <mu-appbar title="请选择学生" @click.native="closeStudent4">
        <mu-icon-button icon='done' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item title="清空" @click.native="student_id4=''">
          <mu-icon slot="left" value="delete_forever" :size="40"/>
        </mu-list-item>
        <mu-list-item v-for="student in students4" :title="student.name" :describeText="student.number">
          <mu-icon v-if="student.state.toString() === '1' && student.sex.toString() === '1'" slot="left" :size="40" value="account_box" color="#00bcd4"/>
          <mu-icon v-if="student.state.toString() === '1' && student.sex.toString() === '2'" slot="left" :size="40" value="account_circle" color="#e91e63"/>
          <mu-icon v-if="student.state.toString() === '2' && student.sex.toString() === '1'" slot="left" :size="40" value="account_box" color="#b2ebf2"/>
          <mu-icon v-if="student.state.toString() === '2' && student.sex.toString() === '2'" slot="left" :size="40" value="account_circle" color="#f8bbd0"/>
          <mu-radio v-model="student_id4" label="" labelLeft :nativeValue="student.id" uncheckIcon="favorite_border" checkedIcon="favorite" slot="right"/>
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-drawer right :open="openIdentity1" docked="false">
      <mu-appbar title="请选择身份" @click.native="closeIdentity1">
        <mu-icon-button icon='done' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item title="清空" @click.native="identity_id1=''">
          <mu-icon slot="left" value="delete_forever" :size="40"/>
        </mu-list-item>
        <mu-list-item v-for="identity in identities" :title="identity.name">
          <mu-icon v-if="identity.id.toString() === '1'"  slot="left" :size="40" color="#009688" value="account_box"/>
          <mu-icon v-if="identity.id.toString() === '2'"  slot="left" :size="40" color="#ff5722" value="account_circle"/>
          <mu-icon v-if="identity.id.toString() === '3'"  slot="left" :size="40" color="#4caf50" value="account_box"/>
          <mu-icon v-if="identity.id.toString() === '4'"  slot="left" :size="40" color="#ff9800" value="account_circle"/>
          <mu-icon v-if="identity.id.toString() === '5'"  slot="left" :size="40" color="#8bc34a" value="account_box"/>
          <mu-icon v-if="identity.id.toString() === '6'"  slot="left" :size="40" color="#ffc107" value="account_circle"/>
          <mu-radio v-model="identity_id1" label="" labelLeft :nativeValue="identity.id" uncheckIcon="favorite_border" checkedIcon="favorite" slot="right"/>
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-drawer right :open="openIdentity2" docked="false">
      <mu-appbar title="请选择身份" @click.native="closeIdentity2">
        <mu-icon-button icon='done' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item title="清空" @click.native="identity_id2=''">
          <mu-icon slot="left" value="delete_forever" :size="40"/>
        </mu-list-item>
        <mu-list-item v-for="identity in identities" :title="identity.name">
          <mu-icon v-if="identity.id.toString() === '1'"  slot="left" :size="40" color="#009688" value="account_box"/>
          <mu-icon v-if="identity.id.toString() === '2'"  slot="left" :size="40" color="#ff5722" value="account_circle"/>
          <mu-icon v-if="identity.id.toString() === '3'"  slot="left" :size="40" color="#4caf50" value="account_box"/>
          <mu-icon v-if="identity.id.toString() === '4'"  slot="left" :size="40" color="#ff9800" value="account_circle"/>
          <mu-icon v-if="identity.id.toString() === '5'"  slot="left" :size="40" color="#8bc34a" value="account_box"/>
          <mu-icon v-if="identity.id.toString() === '6'"  slot="left" :size="40" color="#ffc107" value="account_circle"/>
          <mu-radio v-model="identity_id2" label="" labelLeft :nativeValue="identity.id" uncheckIcon="favorite_border" checkedIcon="favorite" slot="right"/>
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-drawer right :open="openIdentity3" docked="false">
      <mu-appbar title="请选择身份" @click.native="closeIdentity3">
        <mu-icon-button icon='done' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item title="清空" @click.native="identity_id3=''">
          <mu-icon slot="left" value="delete_forever" :size="40"/>
        </mu-list-item>
        <mu-list-item v-for="identity in identities" :title="identity.name">
          <mu-icon v-if="identity.id.toString() === '1'"  slot="left" :size="40" color="#009688" value="account_box"/>
          <mu-icon v-if="identity.id.toString() === '2'"  slot="left" :size="40" color="#ff5722" value="account_circle"/>
          <mu-icon v-if="identity.id.toString() === '3'"  slot="left" :size="40" color="#4caf50" value="account_box"/>
          <mu-icon v-if="identity.id.toString() === '4'"  slot="left" :size="40" color="#ff9800" value="account_circle"/>
          <mu-icon v-if="identity.id.toString() === '5'"  slot="left" :size="40" color="#8bc34a" value="account_box"/>
          <mu-icon v-if="identity.id.toString() === '6'"  slot="left" :size="40" color="#ffc107" value="account_circle"/>
          <mu-radio v-model="identity_id3" label="" labelLeft :nativeValue="identity.id" uncheckIcon="favorite_border" checkedIcon="favorite" slot="right"/>
        </mu-list-item>
      </mu-list>
    </mu-drawer>
    <mu-drawer right :open="openIdentity4" docked="false">
      <mu-appbar title="请选择身份" @click.native="closeIdentity4">
        <mu-icon-button icon='done' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item title="清空" @click.native="identity_id4=''">
          <mu-icon slot="left" value="delete_forever" :size="40"/>
        </mu-list-item>
        <mu-list-item v-for="identity in identities" :title="identity.name">
          <mu-icon v-if="identity.id.toString() === '1'"  slot="left" :size="40" color="#009688" value="account_box"/>
          <mu-icon v-if="identity.id.toString() === '2'"  slot="left" :size="40" color="#ff5722" value="account_circle"/>
          <mu-icon v-if="identity.id.toString() === '3'"  slot="left" :size="40" color="#4caf50" value="account_box"/>
          <mu-icon v-if="identity.id.toString() === '4'"  slot="left" :size="40" color="#ff9800" value="account_circle"/>
          <mu-icon v-if="identity.id.toString() === '5'"  slot="left" :size="40" color="#8bc34a" value="account_box"/>
          <mu-icon v-if="identity.id.toString() === '6'"  slot="left" :size="40" color="#ffc107" value="account_circle"/>
          <mu-radio v-model="identity_id4" label="" labelLeft :nativeValue="identity.id" uncheckIcon="favorite_border" checkedIcon="favorite" slot="right"/>
        </mu-list-item>
      </mu-list>
    </mu-drawer>
  </div>
</template>

<script>
import * as API from './ParentAPI.js'
export default {
  name: 'ParentAdd',
  data () {
    return {
      bottomPopup: false,
      forSave: false,
      icon: '',
      color: '',
      name: '',
      userId: '',
      mobile: '',
      message: '',
      nameErrorText: '',
      userIdErrorText: '',
      mobileErrorText: '',
      nameErrorColor: '',
      userIdErrorColor: '',
      mobileErrorColor: '',
      studentNumber: '1',
      studentNumberLabel: '学生1',
      before: false,
      next: true,
      studentNumber1: true,
      studentNumber2: false,
      studentNumber3: false,
      studentNumber4: false,
      roomAble1: false,
      roomAble2: true,
      roomAble3: true,
      roomAble4: true,
      studentAble1: true,
      studentAble2: true,
      studentAble3: true,
      studentAble4: true,
      identityAble1: true,
      identityAble2: true,
      identityAble3: true,
      identityAble4: true,
      roomName1: '班级',
      roomName2: '班级',
      roomName3: '班级',
      roomName4: '班级',
      studentName1: '姓名',
      studentName2: '姓名',
      studentName3: '姓名',
      studentName4: '姓名',
      identityName1: '身份',
      identityName2: '身份',
      identityName3: '身份',
      identityName4: '身份',
      openRoom1: false,
      openRoom2: false,
      openRoom3: false,
      openRoom4: false,
      openStudent1: false,
      openStudent2: false,
      openStudent3: false,
      openStudent4: false,
      openIdentity1: false,
      openIdentity2: false,
      openIdentity3: false,
      openIdentity4: false,
      roomIcon1: 'looks_one',
      roomIcon2: 'looks_one',
      roomIcon3: 'looks_one',
      roomIcon4: 'looks_one',
      studentIcon1: 'looks_two',
      studentIcon2: 'looks_two',
      studentIcon3: 'looks_two',
      studentIcon4: 'looks_two',
      identityIcon1: 'looks_3',
      identityIcon2: 'looks_3',
      identityIcon3: 'looks_3',
      identityIcon4: 'looks_3',
      roomBack1: '#66CCCC',
      roomBack2: '#66CCCC',
      roomBack3: '#66CCCC',
      roomBack4: '#66CCCC',
      studentBack1: '#66CCCC',
      studentBack2: '#66CCCC',
      studentBack3: '#66CCCC',
      studentBack4: '#66CCCC',
      identityBack1: '#66CCCC',
      identityBack2: '#66CCCC',
      identityBack3: '#66CCCC',
      identityBack4: '#66CCCC',
      rooms: [],
      identities: [],
      students1: [],
      students2: [],
      students3: [],
      students4: [],
      room_id1: '',
      room_id2: '',
      room_id3: '',
      room_id4: '',
      student_id1: '',
      student_id2: '',
      student_id3: '',
      student_id4: '',
      identity_id1: '',
      identity_id2: '',
      identity_id3: '',
      identity_id4: ''
    }
  },
  created: function () {
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
      this.openPopup('服务器内部错误！', 'error', 'red')
    })
    this.$http.get(
      API.IdentityList,
      {
        headers:
        {
          'X-Requested-With': 'XMLHttpRequest'
        },
        emulateJSON: true
      }
    ).then((response) => {
      this.identities = response.body
    }, (response) => {
      this.openPopup('服务器内部错误！', 'error', 'red')
    })
  },
  watch: {
    room_id1: function (val) {
      if (val.toString() !== '') {
        this.$http.get(
          API.StudentList,
          { params:
          {
            id: val
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
          this.students1 = response.body
        }, (response) => {
          this.openPopup('服务器内部错误！', 'error', 'red')
        })
      } else {
        this.students1 = []
      }
    },
    room_id2: function (val) {
      if (val.toString() !== '') {
        this.$http.get(
          API.StudentList,
          { params:
          {
            id: val
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
          this.students2 = response.body
        }, (response) => {
          this.openPopup('服务器内部错误！', 'error', 'red')
        })
      } else {
        this.students2 = []
      }
    },
    room_id3: function (val) {
      if (val.toString() !== '') {
        this.$http.get(
          API.StudentList,
          { params:
          {
            id: val
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
          this.students3 = response.body
        }, (response) => {
          this.openPopup('服务器内部错误！', 'error', 'red')
        })
      } else {
        this.students3 = []
      }
    },
    room_id4: function (val) {
      if (val.toString() !== '') {
        this.$http.get(
          API.StudentList,
          { params:
          {
            id: val
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
          this.students4 = response.body
        }, (response) => {
          this.openPopup('服务器内部错误！', 'error', 'red')
        })
      } else {
        this.students4 = []
      }
    }
  },
  computed: {
    studentNumberLabel: function () {
      return '学生' + this.studentNumber
    },
    roomBack1: function () {
      if (this.room_id1.toString() !== '') {
        return '#9999CC'
      } else {
        return '#66CCCC'
      }
    },
    roomBack2: function () {
      if (this.room_id2.toString() !== '') {
        return '#9999CC'
      } else {
        return '#66CCCC'
      }
    },
    roomBack3: function () {
      if (this.room_id3.toString() !== '') {
        return '#9999CC'
      } else {
        return '#66CCCC'
      }
    },
    roomBack4: function () {
      if (this.room_id4.toString() !== '') {
        return '#9999CC'
      } else {
        return '#66CCCC'
      }
    },
    studentBack1: function () {
      if (this.student_id1.toString() !== '') {
        return '#9999CC'
      } else {
        return '#66CCCC'
      }
    },
    studentBack2: function () {
      if (this.student_id2.toString() !== '') {
        return '#9999CC'
      } else {
        return '#66CCCC'
      }
    },
    studentBack3: function () {
      if (this.student_id3.toString() !== '') {
        return '#9999CC'
      } else {
        return '#66CCCC'
      }
    },
    studentBack4: function () {
      if (this.student_id4.toString() !== '') {
        return '#9999CC'
      } else {
        return '#66CCCC'
      }
    },
    identityBack1: function () {
      if (this.identity_id1.toString() !== '') {
        return '#9999CC'
      } else {
        return '#66CCCC'
      }
    },
    identityBack2: function () {
      if (this.identity_id2.toString() !== '') {
        return '#9999CC'
      } else {
        return '#66CCCC'
      }
    },
    identityBack3: function () {
      if (this.identity_id3.toString() !== '') {
        return '#9999CC'
      } else {
        return '#66CCCC'
      }
    },
    identityBack4: function () {
      if (this.identity_id4.toString() !== '') {
        return '#9999CC'
      } else {
        return '#66CCCC'
      }
    },
    roomIcon1: function () {
      if (this.room_id1.toString() !== '') {
        return 'filter_1'
      } else {
        return 'looks_one'
      }
    },
    roomIcon2: function () {
      if (this.room_id2.toString() !== '') {
        return 'filter_1'
      } else {
        return 'looks_one'
      }
    },
    roomIcon3: function () {
      if (this.room_id3.toString() !== '') {
        return 'filter_1'
      } else {
        return 'looks_one'
      }
    },
    roomIcon4: function () {
      if (this.room_id4.toString() !== '') {
        return 'filter_1'
      } else {
        return 'looks_one'
      }
    },
    studentIcon1: function () {
      if (this.student_id1.toString() !== '') {
        return 'filter_2'
      } else {
        return 'looks_two'
      }
    },
    studentIcon2: function () {
      if (this.student_id2.toString() !== '') {
        return 'filter_2'
      } else {
        return 'looks_two'
      }
    },
    studentIcon3: function () {
      if (this.student_id3.toString() !== '') {
        return 'filter_2'
      } else {
        return 'looks_two'
      }
    },
    studentIcon4: function () {
      if (this.student_id4.toString() !== '') {
        return 'filter_2'
      } else {
        return 'looks_two'
      }
    },
    identityIcon1: function () {
      if (this.identity_id1.toString() !== '') {
        return 'filter_3'
      } else {
        return 'looks_3'
      }
    },
    identityIcon2: function () {
      if (this.identity_id2.toString() !== '') {
        return 'filter_3'
      } else {
        return 'looks_3'
      }
    },
    identityIcon3: function () {
      if (this.identity_id3.toString() !== '') {
        return 'filter_3'
      } else {
        return 'looks_3'
      }
    },
    identityIcon4: function () {
      if (this.identity_id4.toString() !== '') {
        return 'filter_3'
      } else {
        return 'looks_3'
      }
    },
    identityAble4: function () {
      if (this.student_id4.toString() !== '') {
        return false
      } else {
        return true
      }
    },
    studentAble4: function () {
      if (this.room_id4.toString() !== '') {
        return false
      } else {
        return true
      }
    },
    roomAble4: function () {
      if (this.identity_id3.toString() !== '') {
        return false
      } else {
        return true
      }
    },
    identityAble3: function () {
      if (this.student_id3.toString() !== '') {
        return false
      } else {
        return true
      }
    },
    studentAble3: function () {
      if (this.room_id3.toString() !== '') {
        return false
      } else {
        return true
      }
    },
    roomAble3: function () {
      if (this.identity_id2.toString() !== '') {
        return false
      } else {
        return true
      }
    },
    identityAble2: function () {
      if (this.student_id2.toString() !== '') {
        return false
      } else {
        return true
      }
    },
    studentAble2: function () {
      if (this.room_id2.toString() !== '') {
        return false
      } else {
        return true
      }
    },
    roomAble2: function () {
      if (this.identity_id1.toString() !== '') {
        return false
      } else {
        return true
      }
    },
    identityAble1: function () {
      if (this.student_id1.toString() !== '') {
        return false
      } else {
        return true
      }
    },
    studentAble1: function () {
      if (this.room_id1.toString() !== '') {
        return false
      } else {
        return true
      }
    }
  },
  methods: {
    goReply () {
      this.$router.push({ path: '/parentList' })
    },
    openPopup (message, icon, color) {
      this.message = message
      this.icon = icon
      this.color = color
      this.bottomPopup = true
      setTimeout(() => { this.bottomPopup = false }, 1500)
    },
    goBefore () {
      this.studentNumber--
      this.studentNumber.toString() === '1' ? this.before = false : this.before = true
      this.next = true
      switch (this.studentNumber) {
        case 1:
          this.studentNumber1 = true
          this.studentNumber2 = false
          this.studentNumber3 = false
          this.studentNumber4 = false
          break
        case 2:
          this.studentNumber1 = false
          this.studentNumber2 = true
          this.studentNumber3 = false
          this.studentNumber4 = false
          break
        case 3:
          this.studentNumber1 = false
          this.studentNumber2 = false
          this.studentNumber3 = true
          this.studentNumber4 = false
          break
        case 4:
          this.studentNumber1 = false
          this.studentNumber2 = false
          this.studentNumber3 = false
          this.studentNumber4 = true
          break
        default:
          this.studentNumber1 = false
          this.studentNumber2 = false
          this.studentNumber3 = false
          this.studentNumber4 = false
      }
    },
    goNext () {
      this.studentNumber++
      this.studentNumber.toString() === '4' ? this.next = false : this.next = true
      this.before = true
      switch (this.studentNumber) {
        case 1:
          this.studentNumber1 = true
          this.studentNumber2 = false
          this.studentNumber3 = false
          this.studentNumber4 = false
          break
        case 2:
          this.studentNumber1 = false
          this.studentNumber2 = true
          this.studentNumber3 = false
          this.studentNumber4 = false
          break
        case 3:
          this.studentNumber1 = false
          this.studentNumber2 = false
          this.studentNumber3 = true
          this.studentNumber4 = false
          break
        case 4:
          this.studentNumber1 = false
          this.studentNumber2 = false
          this.studentNumber3 = false
          this.studentNumber4 = true
          break
        default:
          this.studentNumber1 = false
          this.studentNumber2 = false
          this.studentNumber3 = false
          this.studentNumber4 = false
      }
    },
    goReset () {
      this.name = ''
      this.userId = ''
      this.mobile = ''
      this.nameErrorText = ''
      this.userIdErrorText = ''
      this.mobileErrorText = ''
      this.nameErrorColor = ''
      this.userIdErrorColor = ''
      this.mobileErrorColor = ''
      this.studentNumber = '1'
      this.before = false
      this.next = true
      this.studentNumber1 = true
      this.studentNumber2 = false
      this.studentNumber3 = false
      this.studentNumber4 = false
      this.roomAble1 = false
      this.roomAble2 = true
      this.roomAble3 = true
      this.roomAble4 = true
      this.studentAble1 = true
      this.studentAble2 = true
      this.studentAble3 = true
      this.studentAble4 = true
      this.identityAble1 = true
      this.identityAble2 = true
      this.identityAble3 = true
      this.identityAble4 = true
      this.roomName1 = '班级'
      this.roomName2 = '班级'
      this.roomName3 = '班级'
      this.roomName4 = '班级'
      this.studentName1 = '姓名'
      this.studentName2 = '姓名'
      this.studentName3 = '姓名'
      this.studentName4 = '姓名'
      this.identityName1 = '身份'
      this.identityName2 = '身份'
      this.identityName3 = '身份'
      this.identityName4 = '身份'
      this.openRoom1 = false
      this.openRoom2 = false
      this.openRoom3 = false
      this.openRoom4 = false
      this.openStudent1 = false
      this.openStudent2 = false
      this.openStudent3 = false
      this.openStudent4 = false
      this.openIdentity1 = false
      this.openIdentity2 = false
      this.openIdentity3 = false
      this.openIdentity4 = false
      this.roomIcon1 = 'looks_one'
      this.roomIcon2 = 'looks_one'
      this.roomIcon3 = 'looks_one'
      this.roomIcon4 = 'looks_one'
      this.studentIcon1 = 'looks_two'
      this.studentIcon2 = 'looks_two'
      this.studentIcon3 = 'looks_two'
      this.studentIcon4 = 'looks_two'
      this.identityIcon1 = 'looks_3'
      this.identityIcon2 = 'looks_3'
      this.identityIcon3 = 'looks_3'
      this.identityIcon4 = 'looks_3'
      this.roomBack1 = '#66CCCC'
      this.roomBack2 = '#66CCCC'
      this.roomBack3 = '#66CCCC'
      this.roomBack4 = '#66CCCC'
      this.studentBack1 = '#66CCCC'
      this.studentBack2 = '#66CCCC'
      this.studentBack3 = '#66CCCC'
      this.studentBack4 = '#66CCCC'
      this.identityBack1 = '#66CCCC'
      this.identityBack2 = '#66CCCC'
      this.identityBack3 = '#66CCCC'
      this.identityBack4 = '#66CCCC'
      this.students1 = []
      this.students2 = []
      this.students3 = []
      this.students4 = []
      this.room_id1 = ''
      this.room_id2 = ''
      this.room_id3 = ''
      this.room_id4 = ''
      this.student_id1 = ''
      this.student_id2 = ''
      this.student_id3 = ''
      this.student_id4 = ''
      this.identity_id1 = ''
      this.identity_id2 = ''
      this.identity_id3 = ''
      this.identity_id4 = ''
    },
    closeRoom1 () {
      this.openRoom1 = false
      if (this.room_id1.toString() !== '') {
        this.$http.get(
          API.GetRoomName,
          { params: {
            id: this.room_id1
          }
          },
          {
            headers:
            {
              'X-Requested-With': 'XMLHttpRequest'
            }
          }).then((response) => {
            this.roomName1 = response.body
          }, (response) => {
            this.openPopup('服务器内部错误！', 'error', 'red')
          })
      } else {
        this.roomName1 = '班级'
        this.studentName1 = '姓名'
        this.identityName1 = '身份'
        this.students1 = []
        this.student_id1 = ''
        this.identity_id1 = ''
      }
    },
    closeRoom2 () {
      this.openRoom2 = false
      if (this.room_id2.toString() !== '') {
        this.$http.get(
          API.GetRoomName,
          { params: {
            id: this.room_id2
          }
          },
          {
            headers:
            {
              'X-Requested-With': 'XMLHttpRequest'
            }
          }).then((response) => {
            this.roomName2 = response.body
          }, (response) => {
            this.openPopup('服务器内部错误！', 'error', 'red')
          })
      } else {
        this.roomName2 = '班级'
        this.studentName2 = '姓名'
        this.identityName2 = '身份'
        this.students2 = []
        this.student_id2 = ''
        this.identity_id2 = ''
      }
    },
    closeRoom3 () {
      this.openRoom3 = false
      if (this.room_id3.toString() !== '') {
        this.$http.get(
          API.GetRoomName,
          { params:
          {
            id: this.room_id3
          }
          },
          {
            headers:
            {
              'X-Requested-With': 'XMLHttpRequest'
            }
          }).then((response) => {
            this.roomName3 = response.body
          }, (response) => {
            this.openPopup('服务器内部错误！', 'error', 'red')
          })
      } else {
        this.roomName3 = '班级'
        this.studentName3 = '姓名'
        this.identityName3 = '身份'
        this.students3 = []
        this.student_id3 = ''
        this.identity_id3 = ''
      }
    },
    closeRoom4 () {
      this.openRoom4 = false
      if (this.room_id4.toString() !== '') {
        this.$http.get(
          API.GetRoomName,
          { params: {
            id: this.room_id4
          }
          },
          {
            headers:
            {
              'X-Requested-With': 'XMLHttpRequest'
            }
          }).then((response) => {
            this.roomName4 = response.body
          }, (response) => {
            this.openPopup('服务器内部错误！', 'error', 'red')
          })
      } else {
        this.roomName4 = '班级'
        this.studentName4 = '姓名'
        this.identityName4 = '身份'
        this.students4 = []
        this.student_id4 = ''
        this.identity_id4 = ''
      }
    },
    closeStudent1 () {
      if ((this.student_id1.toString() === this.student_id2.toString() && this.student_id2.toString() !== '') || (this.student_id1.toString() === this.student_id3.toString() && this.student_id3.toString() !== '') || (this.student_id1.toString() === this.student_id4.toString() && this.student_id4.toString() !== '')) {
        this.openPopup('该学生重复选择!', 'report_problem', 'orange')
      } else {
        this.openStudent1 = false
        if (this.student_id1.toString() !== '') {
          this.$http.get(
            API.GetStudentName,
            { params: {
              id: this.student_id1
            }
            },
            {
              headers:
              {
                'X-Requested-With': 'XMLHttpRequest'
              }
            }).then((response) => {
              this.studentName1 = response.body
            }, (response) => {
              this.openPopup('服务器内部错误！', 'error', 'red')
            })
        } else {
          this.studentName1 = '姓名'
          this.identityName1 = '身份'
          this.student_id1 = ''
          this.identity_id1 = ''
        }
      }
    },
    closeStudent2 () {
      if ((this.student_id2.toString() === this.student_id1.toString() && this.student_id1.toString() !== '') || (this.student_id2.toString() === this.student_id3.toString() && this.student_id3.toString() !== '') || (this.student_id2.toString() === this.student_id4.toString() && this.student_id4.toString() !== '')) {
        this.openPopup('该学生重复选择!', 'report_problem', 'orange')
      } else {
        this.openStudent2 = false
        if (this.student_id2.toString() !== '') {
          this.$http.get(
            API.GetStudentName,
            { params: {
              id: this.student_id2
            }
            },
            {
              headers:
              {
                'X-Requested-With': 'XMLHttpRequest'
              }
            }).then((response) => {
              this.studentName2 = response.body
            }, (response) => {
              this.openPopup('服务器内部错误!', 'error', 'red')
            })
        } else {
          this.studentName2 = '姓名'
          this.identityName2 = '身份'
          this.student_id2 = ''
          this.identity_id2 = ''
        }
      }
    },
    closeStudent3 () {
      if ((this.student_id3.toString() === this.student_id1.toString() && this.student_id1.toString() !== '') || (this.student_id3.toString() === this.student_id2.toString() && this.student_id2.toString() !== '') || (this.student_id3.toString() === this.student_id4.toString() && this.student_id4.toString() !== '')) {
        this.openPopup('该学生重复选择!', 'report_problem', 'orange')
      } else {
        this.openStudent3 = false
        if (this.student_id3.toString() !== '') {
          this.$http.get(
            API.GetStudentName,
            { params: {
              id: this.student_id3
            }
            },
            {
              headers:
              {
                'X-Requested-With': 'XMLHttpRequest'
              }
            }).then((response) => {
              this.studentName3 = response.body
            }, (response) => {
              this.openPopup('服务器内部错误!', 'error', 'red')
            })
        } else {
          this.studentName3 = '姓名'
          this.identityName3 = '身份'
          this.student_id3 = ''
          this.identity_id3 = ''
        }
      }
    },
    closeStudent4 () {
      if ((this.student_id4.toString() === this.student_id1.toString() && this.student_id1.toString() !== '') || (this.student_id4.toString() === this.student_id2.toString() && this.student_id2.toString() !== '') || (this.student_id4.toString() === this.student_id4.toString() && this.student_id4.toString() !== '')) {
        this.openPopup('该学生重复选择!', 'report_problem', 'orange')
      } else {
        this.openStudent4 = false
        if (this.student_id4.toString() !== '') {
          this.$http.get(
            API.GetStudentName,
            { params: {
              id: this.student_id4
            }
            },
            {
              headers:
              {
                'X-Requested-With': 'XMLHttpRequest'
              }
            }).then((response) => {
              this.studentName4 = response.body
            }, (response) => {
              this.openPopup('服务器内部错误!', 'error', 'red')
            })
        } else {
          this.studentName4 = '姓名'
          this.identityName4 = '身份'
          this.student_id4 = ''
          this.identity_id4 = ''
        }
      }
    },
    closeIdentity1 () {
      this.openIdentity1 = false
      if (this.identity_id1.toString() !== '') {
        this.$http.get(
          API.GetIdentityName,
          { params: {
            id: this.identity_id1
          }
          },
          {
            headers:
            {
              'X-Requested-With': 'XMLHttpRequest'
            }
          }).then((response) => {
            this.identityName1 = response.body
          }, (response) => {
            this.openPopup('服务器内部错误!', 'error', 'red')
          })
      } else {
        this.identityName1 = '身份'
        this.identity_id1 = ''
      }
    },
    closeIdentity2 () {
      this.openIdentity2 = false
      if (this.identity_id2.toString() !== '') {
        this.$http.get(
          API.GetIdentityName,
          { params: {
            id: this.identity_id2
          }
          },
          {
            headers:
            {
              'X-Requested-With': 'XMLHttpRequest'
            }
          }).then((response) => {
            this.identityName2 = response.body
          }, (response) => {
            this.openPopup('服务器内部错误!', 'error', 'red')
          })
      } else {
        this.identityName2 = '身份'
        this.identity_id2 = ''
      }
    },
    closeIdentity3 () {
      this.openIdentity3 = false
      if (this.identity_id3.toString() !== '') {
        this.$http.get(
          API.GetIdentityName,
          { params: {
            id: this.identity_id3
          }
          },
          {
            headers:
            {
              'X-Requested-With': 'XMLHttpRequest'
            }
          }).then((response) => {
            this.identityName3 = response.body
          }, (response) => {
            this.openPopup('服务器内部错误!', 'error', 'red')
          })
      } else {
        this.identityName3 = '身份'
        this.identity_id3 = ''
      }
    },
    closeIdentity4 () {
      this.openIdentity4 = false
      if (this.identity_id4.toString() !== '') {
        this.$http.get(
          API.GetIdentityName,
          { params: {
            id: this.identity_id4
          }
          },
          {
            headers:
            {
              'X-Requested-With': 'XMLHttpRequest'
            }
          }).then((response) => {
            this.identityName4 = response.body
          }, (response) => {
            this.openPopup('服务器内部错误!', 'error', 'red')
          })
      } else {
        this.identityName4 = '身份'
        this.identity_id4 = ''
      }
    },
    checkName (value) {
      this.$http.get(
        API.CheckName,
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
            this.openPopup('请重新登录!', 'report_problem', 'orange')
            window.location.href = '/'
          } else if (response.body === 'OK') {
            this.nameErrorText = ''
            this.nameErrorColor = 'green'
          } else {
            this.nameErrorText = response.body
            this.nameErrorColor = 'red'
          }
        }, (response) => {
          this.openPopup('服务器内部错误!', 'error', 'red')
        })
      this.$http.get(
        API.GetUserId,
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
          if (value === '') {
            this.userId = ''
          } else {
            this.userId = response.body
          }
        }, (response) => {
          this.openPopup('服务器内部错误!', 'error', 'red')
        })
    },
    checkUserId (value) {
      this.$http.get(
        API.CheckUserIdForNew,
        { params: {
          userId: value
        }
        },
        {
          headers:
          {
            'X-Requested-With': 'XMLHttpRequest'
          }
        }).then((response) => {
          if (response.body === 'error') {
            this.openPopup('请重新登录!', 'report_problem', 'orange')
            window.location.href = '/'
          } else if (response.body === 'OK') {
            this.userIdErrorText = ''
            this.userIdErrorColor = 'green'
          } else {
            this.userIdErrorText = response.body
            this.userIdErrorColor = 'red'
          }
        }, (response) => {
          this.openPopup('服务器内部错误!', 'error', 'red')
        })
    },
    checkMobile (value) {
      this.$http.get(
        API.CheckMobileForNew,
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
            this.openPopup('请重新登录!', 'report_problem', 'orange')
            window.location.href = '/'
          } else if (response.body === 'OK') {
            this.mobileErrorText = '此处应填写微信绑定的手机号码.'
            this.mobileErrorColor = 'green'
          } else {
            this.mobileErrorText = response.body
            this.mobileErrorColor = 'red'
          }
        }, (response) => {
          this.openPopup('服务器内部错误!', 'error', 'red')
        })
    },
    goSave () {
      this.forSave = true
      this.$http.get(
        API.Save,
        { params: {
          name: this.name,
          userId: this.userId,
          mobile: this.mobile,
          room_id1: this.room_id1,
          room_id2: this.room_id2,
          room_id3: this.room_id3,
          room_id4: this.room_id4,
          student_id1: this.student_id1,
          student_id2: this.student_id2,
          student_id3: this.student_id3,
          student_id4: this.student_id4,
          identity_id1: this.identity_id1,
          identity_id2: this.identity_id2,
          identity_id3: this.identity_id3,
          identity_id4: this.identity_id4
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
            this.openPopup('请重新登录!', 'report_problem', 'orange')
            window.location.href = '/'
          } else if (response.body === 'OK') {
            this.openPopup('保存成功！', 'check_circle', 'green')
            setTimeout(() => { this.$router.push({ path: '/parentList' }) }, 1000)
          } else {
            this.openPopup(response.body, 'report_problem', 'orange')
          }
          this.forSave = false
        }, (response) => {
          this.forSave = false
          this.openPopup('服务器内部错误!', 'error', 'red')
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
    height: 60px;
    text-align: center;
    line-height: 32px;
  }
  .flex-demo1 {
    height: 40px;
    text-align: center;
    line-height: 32px;
  }
  .demo-flat-button {
    margin: 12px;
  }
</style>
