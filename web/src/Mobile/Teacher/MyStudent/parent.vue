<template>
  <div>
    <mu-appbar title="家长详情">
      <mu-icon-button icon='reply' slot="right" @click="goReply" v-if="permission.Page"/></mu-appbar>
    <mu-list>
      <mu-list-item v-for="object in list" :value="object.id" :title="object.name" :describeText="getDescribe(object.mobile, object.iname)" :afterText="getState(object.state)" @click="goSheet(object.id, object.state, object.name, object.mobile)">
        <mu-avatar v-if="object.state.toString() === '1'" :src="object.picUrl" slot="leftAvatar" :size="40"/>
        <mu-icon v-if="object.state.toString() === '2'" slot="left" color="#9e9e9e" value="sentiment_very_dissatisfied" :size="40" />
        <mu-icon v-if="object.state.toString() === '3'" slot="left" color="#8bc34a" value="sentiment_neutral" :size="40" />
        <mu-icon v-if="object.state.toString() === '4'" slot="left" color="#3f51b5" value="sentiment_dissatisfied" :size="40" />
      </mu-list-item>
    </mu-list>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="cached" v-if="permission.GetParent" @click="goReset" backgroundColor="orange"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <menuList :open="open" v-on:closeMenu="closeMenu" :menu="menu"></menuList>
    <mu-popup position="bottom" :overlay="false" popupClass="popup-bottom" :open="bottomPopup">
      <mu-icon :value="icon" :size="36" :color="color"/>&nbsp;{{ message }}
    </mu-popup>
    <mu-bottom-sheet :open="bottomSheet" @close="goClose">
      <mu-sub-header>{{name}}</mu-sub-header>
      <mu-raised-button class="raised-button" label="呼叫" @click="goCall" icon="call" backgroundColor="blue" fullWidth/>
      <mu-raised-button class="raised-button" label="取消" @click="goClose" icon="undo" backgroundColor="grey" fullWidth/>
    </mu-bottom-sheet>
    <mu-dialog :open="Reading" title="正在读取" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
  </div>
</template>

<script>
  import * as API from './API.js'
  import { getCookie } from '../../../cookieUtil.js'
  import MenuList from '../Menu/menuList'
  export default {
    name: 'parent',
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
        Reading: true,
        permission: [],
        menu: [],
        icon: '',
        color: '',
        message: '',
        id: '',
        name: '',
        mobile: '',
        state: false,
        keyword: '',
        list: []
      }
    },
    created: function () {
      this.fetchData(this.$route.params.id)
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
    watch: {
      // 如果路由有变化，会再次执行该方法
      '$route': 'fetchData'
    },
    methods: {
      goReply () {
        this.$router.push({ path: '/list' })
      },
      goReset () {
        this.Reading = true
        this.fetchData(this.$route.params.id)
      },
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
      getDescribe (mobile, iname) {
        return iname.toString() + '----' + mobile.toString()
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
      fetchData (id) {
        this.$http.get(
          API.parent,
          { params: { id: id } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
        ).then((response) => {
          this.list = response.body
          this.Reading = false
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
      goClose () {
        this.bottomSheet = false
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
