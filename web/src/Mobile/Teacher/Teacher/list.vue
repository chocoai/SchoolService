<template>
  <div>
    <mu-appbar title="">
      <mu-icon-button icon='menu' slot="left" @click="openMenu"/>
      <mu-text-field icon="search" class="appbar-search-field" hintText="请输入关键词" @input="goQuery" :value="keyword"/>
      <mu-icon-button icon='person_add' slot="right" @click="goAdd" v-if="permission.Save"/>
    </mu-appbar>
    <mu-list>
      <mu-list-item v-for="object in list" :value="object.id" :title="object.name" :describeText="getType(object.type)" :afterText="getState(object.state)" @click="goSheet(object.id, object.state, object.name, object.mobile)">
        <mu-avatar v-if="object.state.toString() === '1'" :src="object.picUrl" slot="leftAvatar" :size="40"/>
        <mu-icon v-if="object.state.toString() === '2'" slot="left" color="#9e9e9e" value="sentiment_very_dissatisfied" :size="40" />
        <mu-icon v-if="object.state.toString() === '3'" slot="left" color="#8bc34a" value="sentiment_neutral" :size="40" />
        <mu-icon v-if="object.state.toString() === '4'" slot="left" color="#3f51b5" value="sentiment_dissatisfied" :size="40" />
      </mu-list-item>
    </mu-list>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-flat-button v-if="before" label="上一页" icon="navigate_before" @click="pageBefore" />
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <div v-if="chip">
          {{pageCurrent}}/{{pageTotal}}
        </div>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-flat-button v-if="next" label="下一页" labelPosition="before" icon="navigate_next" @click="pageNext" />
      </mu-flexbox-item>
    </mu-flexbox>
    <br/>
    <menuList :open="open" v-on:closeMenu="closeMenu" :menu="menu"></menuList>
    <mu-popup position="bottom" :overlay="false" popupClass="popup-bottom" :open="bottomPopup">
      <mu-icon :value="icon" :size="36" :color="color"/>&nbsp;{{ message }}
    </mu-popup>
    <mu-bottom-sheet :open="bottomSheet" @close="goClose">
      <mu-sub-header>{{name}}</mu-sub-header>
      <mu-raised-button class="raised-button" label="修改" v-if="permission.Edit" @click="goEdit" icon="edit" backgroundColor="blue" fullWidth/>
      <mu-raised-button class="raised-button" label="激活" v-if="permission.Active && !state" @click="showActive" icon="beenhere" backgroundColor="green" fullWidth/>
      <mu-raised-button class="raised-button" label="注销" v-if="permission.Inactive && state" @click="showInactive" icon="block" backgroundColor="amber" fullWidth/>
      <mu-raised-button class="raised-button" label="删除" v-if="permission.Delete" @click="showDelete" icon="delete" backgroundColor="red" fullWidth/>
      <mu-raised-button class="raised-button" label="取消" @click="goClose" icon="undo" backgroundColor="grey" fullWidth/>
    </mu-bottom-sheet>
    <mu-dialog :open="Checking" :title="title" @close="goClose">
      <mu-flat-button label="取消" @click="goClose" />
      <mu-flat-button label="确定" v-if="Activing" @click="goActive" secondary/>
      <mu-flat-button label="确定" v-if="Inactiving" @click="goInactive" secondary/>
      <mu-flat-button label="确定" v-if="Deleting" @click="goDelete" secondary/>
    </mu-dialog>
    <mu-dialog :open="Operating" title="正在操作" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
  </div>
</template>

<script>
  import * as API from './API.js'
  import { getCookie } from '../../../cookieUtil.js'
  import MenuList from '../Menu/menuList'
  export default {
    name: 'list',
    components: {
      'menuList': MenuList
    },
    data () {
      return {
        before: false,
        next: false,
        chip: false,
        open: false,
        bottomPopup: false,
        bottomSheet: false,
        Activing: false,
        Inactiving: false,
        Deleting: false,
        Checking: false,
        Operating: false,
        permission: [],
        menu: [],
        icon: '',
        color: '',
        message: '',
        id: '',
        name: '',
        title: '',
        mobile: '',
        state: false,
        keyword: '',
        list: [],
        pageTotal: '',
        pageSize: '',
        pageCurrent: ''
      }
    },
    created: function () {
      this.keyword = this.$store.state.keyword
      this.pageCurrent = this.$store.state.pageCurrent
      this.pageSize = this.$store.state.pageSize
      this.getLists()
      if (getCookie('MenuMobile') === null || getCookie('MenuMobile') === undefined || getCookie('MenuMobile') === '' || getCookie(API.base) === null || getCookie(API.base) === undefined || getCookie(API.base) === '') {
        this.$http.get(
          API.menu
        ).then((response) => {
          if (response.body.toString() === 'illegal' || response.body.toString() === 'overdue') {
            this.openPopup('登录过期或非法操作!', 'error', 'red')
          } else {
            this.$http.get(
              API.permission
            ).then((res) => {
              this.permission = JSON.parse(JSON.parse(getCookie(API.base)))
              this.menu = JSON.parse(JSON.parse(getCookie('MenuMobile')))
            }, (res) => {
              this.openPopup('服务器内部错误!', 'error', 'red')
            })
          }
        }, (response) => {
          this.openPopup('服务器内部错误!', 'error', 'red')
        })
      } else {
        this.permission = JSON.parse(JSON.parse(getCookie(API.base)))
        this.menu = JSON.parse(JSON.parse(getCookie('MenuMobile')))
      }
    },
    methods: {
      goCall () {
        this.mobile.toString() === '' ? this.openPopup('无联系电话!', 'report_problem', 'orange') : window.location.href = 'tel:' + this.mobile
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
      getLists () {
        this.$http.get(
          API.query,
          { params: {
            keyword: this.$store.state.keyword,
            pageCurrent: this.$store.state.pageCurrent,
            pageSize: this.$store.state.pageSize
          } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
        ).then((response) => {
          this.$http.get(
            API.total,
            { params: {
              keyword: this.$store.state.keyword,
              pageSize: this.$store.state.pageSize
            } },
            { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
          ).then((res) => {
            this.list = response.body
            this.$store.state.pageCurrent.toString() === '1' ? this.before = false : this.before = true
            this.pageTotal = res.body
            this.pageTotal === '0' ? this.chip = false : this.chip = true
            this.pageTotal === '1' || this.pageTotal === '0' || this.pageTotal.toString() === this.$store.state.pageCurrent.toString() ? this.next = false : this.next = true
          }, (response) => {
            this.openPopup('服务器内部错误!', 'error', 'red')
          })
        }, (response) => {
          this.openPopup('服务器内部错误!', 'error', 'red')
        })
      },
      goSheet (id, state, name, mobile) {
        this.id = id
        this.name = name
        this.mobile = mobile
        if (state.toString() === '1') {
          this.state = true
        } else {
          this.state = false
        }
        this.bottomSheet = true
      },
      goQuery () {
        this.pageCurrent = '1'
        this.$store.commit('save', {
          keyword: this.keyword,
          pageCurrent: this.pageCurrent,
          pageSize: this.pageSize
        })
        this.getLists()
        this.before = false
      },
      goEdit () {
        this.$router.push({ path: '/edit/' + this.id })
        this.$store.commit('save', {
          keyword: this.keyword,
          pageCurrent: this.pageCurrent,
          pageSize: this.pageSize
        })
      },
      showActive () {
        this.Activing = true
        this.Inactiving = false
        this.Deleting = false
        this.Checking = true
        this.bottomSheet = false
        this.title = '确定要邀请：' + this.name + '关注?'
      },
      showInactive () {
        this.Activing = false
        this.Inactiving = true
        this.Deleting = false
        this.Checking = true
        this.bottomSheet = false
        this.title = '确定要取消：' + this.name + '关注?'
      },
      showDelete () {
        this.Activing = false
        this.Inactiving = false
        this.Deleting = true
        this.Checking = true
        this.bottomSheet = false
        this.title = '确定要删除：' + this.name + '?'
      },
      goClose () {
        this.bottomSheet = false
        this.Checking = false
        this.Activing = false
        this.Inactiving = false
        this.Deleting = false
      },
      goDelete () {
        this.goClose()
        this.Operating = true
        this.$http.get(
          API.del,
          { params: { id: this.id } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          this.Operating = false
          if (response.body.toString() === 'illegal' || response.body.toString() === 'overdue') {
            this.openPopup('请重新登录!', 'report_problem', 'orange')
            window.location.href = '/MainMobile'
          } else if (response.body === 'OK') {
            this.openPopup('保存成功！', 'check_circle', 'green')
            setTimeout(() => { this.getLists() }, 1000)
          } else {
            this.openPopup(response.body, 'report_problem', 'orange')
          }
          this.Operating = false
        }, (response) => {
          this.Operating = false
          this.openPopup('服务器内部错误!', 'error', 'red')
        })
      },
      goActive () {
        this.goClose()
        this.Operating = true
        this.$http.get(
          API.active,
          { params: { id: this.id } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          this.Operating = false
          if (response.body === 'illegal' || response.body.toString() === 'overdue') {
            this.openPopup('请重新登录!', 'report_problem', 'red')
            window.location.href = '/MainMobile'
          } else if (response.body === 'OK') {
            this.openPopup('激活成功!', 'check_circle', 'green')
            setTimeout(() => { this.getLists() }, 1000)
          } else {
            this.openPopup(response.body, 'report_problem', 'red')
            setTimeout(() => { this.getLists() }, 1000)
          }
        }, (response) => {
          this.Operating = false
          this.openPopup('服务器内部错误!', 'error', 'red')
        })
      },
      goInactive () {
        this.goClose()
        this.Operating = true
        this.$http.get(
          API.inactive,
          { params: { id: this.id } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          this.Operating = false
          if (response.body === 'illegal' || response.body.toString() === 'overdue') {
            this.openPopup('请重新登录!', 'report_problem', 'red')
            window.location.href = '/MainMobile'
          } else if (response.body === 'OK') {
            this.openPopup('注销成功!', 'check_circle', 'green')
            setTimeout(() => { this.getLists() }, 1000)
          } else {
            this.openPopup(response.body, 'report_problem', 'red')
            setTimeout(() => { this.getLists() }, 1000)
          }
        }, (response) => {
          this.Operating = false
          this.openPopup('服务器内部错误!', 'error', 'red')
        })
      },
      pageBefore () {
        this.pageCurrent--
        this.$store.commit('save', {
          keyword: this.keyword,
          pageCurrent: this.pageCurrent,
          pageSize: this.pageSize
        })
        this.pageCurrent.toString() === '1' ? this.before = false : this.before = true
        this.next = true
        this.getLists()
      },
      pageNext () {
        this.pageCurrent++
        this.$store.commit('save', {
          keyword: this.keyword,
          pageCurrent: this.pageCurrent,
          pageSize: this.pageSize
        })
        this.pageCurrent.toString() === this.pageTotal ? this.next = false : this.next = true
        this.before = true
        this.getLists()
      },
      goAdd () {
        this.$router.push({ path: '/add' })
      }
    }
  }
</script>
<style lang="less">
  .appbar-search-field{
    color: #FFF;
    margin-bottom: 0;
    &.focus-state {
      color: #FFF;
    }
    .mu-text-field-hint {
      color: fade(#FFF, 54%);
    }
    .mu-text-field-input {
      color: #FFF;
    }
    .mu-text-field-focus-line {
      background-color: #FFF;
    }
  }
  .flex-demo {
    height: 70px;
    text-align: center;
    line-height: 32px;
  }
  .raised-button {
    margin: 3px 1px 3px;
  }
</style>
