<template>
  <div>
    <mu-appbar title="新建消息">
      <mu-icon-button icon='reply' slot="right" @click="gorReply"/>
    </mu-appbar>
    <mu-text-field label="标题" underlineShow="false" v-model="title" :errorColor="titleErrorColor" :errorText="titleErrorText" @input="checkTitle" fullWidth labelFloat icon="title" maxLength="20"/><br/>
    <mu-text-field label="内容" underlineShow="false" v-model="content" :errorColor="contentErrorColor" :errorText="contentErrorText" @input="checkContent" fullWidth labelFloat icon="note" multiLine :rows="5" :rowsMax="8" :maxLength="200"/><br/>
    <mu-select-field hintText="确认回复" icon="settings" v-model="send" fullWidth>
      <mu-menu-item value="0" title="不需要回复确认"/>
      <mu-menu-item value="1" title="需要回复确认"/>
    </mu-select-field>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-raised-button :label="teacherName" @click="openTeacher=true" primary/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-dialog :open="Saving" title="正在保存" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-dialog :open="Sending" title="确定要发送这条消息吗?" @close="goClose">
      <mu-flat-button label="取消" @click="goClose" />
      <mu-flat-button label="确定" @click="goSave" secondary/>
    </mu-dialog>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="cached" @click="goReset" backgroundColor="orange"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button :disabled="Save_Able" icon="send" @click="Sending=true" backgroundColor="green"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-popup position="bottom" :overlay="false" popupClass="popup-bottom" :open="bottomPopup">
      <mu-icon :value="icon" :size="36" :color="color"/>&nbsp;{{ message }}
    </mu-popup>
    <mu-drawer right :open="openTeacher" docked="false">
      <mu-appbar title="请选择发送范围" @click.native="closeTeacher">
        <mu-icon-button icon='done' slot="right"/>
      </mu-appbar>
      <mu-list>
        <mu-list-item title="全选" @click.native="teacher_id=teacherAll">
          <mu-icon slot="left" value="supervisor_account" :size="40"/>
        </mu-list-item>
        <mu-list-item title="清空" @click.native="teacher_id=[]">
          <mu-icon slot="left" value="delete_forever" :size="40"/>
        </mu-list-item>
        <mu-list>
          <mu-list-item v-for="teacher in list" :value="teacher.id" :title="teacher.name" :describeText="getType(teacher.type)" :afterText="getState(teacher.state)">
            <mu-avatar v-if="teacher.state.toString() === '1'" :src="teacher.picUrl" slot="leftAvatar" :size="40"/>
            <mu-icon v-if="teacher.state.toString() === '2'" slot="left" color="#9e9e9e" value="sentiment_very_dissatisfied" :size="40" />
            <mu-icon v-if="teacher.state.toString() === '3'" slot="left" color="#8bc34a" value="sentiment_neutral" :size="40" />
            <mu-icon v-if="teacher.state.toString() === '4'" slot="left" color="#3f51b5" value="sentiment_dissatisfied" :size="40" />
            <mu-checkbox v-model="teacher_id" label="" labelLeft :nativeValue="teacher.id" slot="right"/>
          </mu-list-item>
        </mu-list>
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
      Sending: false,
      openTeacher: false,
      icon: '',
      color: '',
      title: '',
      content: '',
      send: '1',
      message: '',
      teacherName: '请选择要发送的人员',
      list: '',
      teacher_id: [],
      teacherAll: '',
      titleErrorText: '',
      contentErrorText: '',
      titleErrorColor: '',
      contentErrorColor: ''
    }
  },
  created: function () {
    this.$http.get(
      API.list,
      { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
    ).then((response) => {
      this.list = response.body.list
      this.teacherAll = response.body.select
      this.teacher_id = this.teacherAll
    }, (response) => {
      this.openPopup('服务器内部错误！', 'error', 'red')
    })
  },
  computed: {
    Save_Able: function () {
      if (this.titleErrorText.toString() === 'OK' && this.contentErrorText.toString() === 'OK') {
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
    goClose () {
      this.Sending = false
    },
    openPopup (message, icon, color) {
      this.message = message
      this.icon = icon
      this.color = color
      this.bottomPopup = true
      setTimeout(() => { this.bottomPopup = false }, 1500)
    },
    goReset () {
      this.title = ''
      this.content = ''
      this.send = '1'
      this.titleErrorText = ''
      this.contentErrorText = ''
      this.titleErrorColor = ''
      this.contentErrorColor = ''
    },
    closeTeacher () {
      this.openTeacher = false
      if (this.teacher_id.toString() === this.teacherAll.toString()) {
        this.teacherName = '已全选'
      } else if (this.teacher_id.toString() !== '') {
        this.teacherName = '部分选择'
      } else {
        this.teacherName = '发送范围未设置'
      }
    },
    getState (state) {
      if (state.toString() === '1') {
        return '已关注'
      } else if (state.toString() === '2') {
        return '已冻结'
      } else if (state.toString() === '3') {
        return '取消关注'
      } else if (state.toString() === '4') {
        return '未关注'
      } else {
        return '状态错误'
      }
    },
    getType (type) {
      if (type.toString() === '1') {
        return '在编教师'
      } else if (type.toString() === '2') {
        return '聘用教师'
      } else if (type.toString() === '3') {
        return '外校教师'
      } else {
        return '状态错误'
      }
    },
    checkTitle (value) {
      if (value === null || value === undefined || value === '') {
        this.titleErrorText = '标题为必填项!'
        this.titleErrorColor = 'orange'
      } else if (value.length > 20) {
        this.titleErrorText = '标题不得超过20字符'
        this.titleErrorColor = 'orange'
      } else {
        this.titleErrorText = 'OK'
        this.titleErrorColor = 'green'
      }
    },
    checkContent (value) {
      if (value === null || value === undefined || value === '') {
        this.contentErrorText = '内容为必填项!'
        this.contentErrorColor = 'orange'
      } else if (value.length > 200) {
        this.contentErrorText = '内容不得超过200字符'
        this.contentErrorColor = 'orange'
      } else {
        this.contentErrorText = 'OK'
        this.contentErrorColor = 'green'
      }
    },
    goSave () {
      this.Sending = false
      this.Saving = true
      this.$http.get(
        API.save,
        { params: {
          title: this.title,
          content: this.content,
          send: this.send,
          teacher_id: this.teacher_id
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
        this.Saving = false
      }, (response) => {
        this.Saving = false
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
    height: 70px;
    text-align: center;
    line-height: 32px;
  }
</style>
