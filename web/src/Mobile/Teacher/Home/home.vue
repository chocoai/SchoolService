<template>
  <div class="Home">
    <mu-appbar :title="schoolName">
      <mu-icon-button icon='menu' slot="left" @click="openMenu"/>
    </mu-appbar>
    <mu-flexbox orient="vertical" justify="center">
      <mu-flexbox-item >
        <img :src="sign1" width="360px" height="360px"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-flexbox orient="vertical" justify="center">
      <mu-flexbox-item >
        <img :src="sign2" width="95%" />
      </mu-flexbox-item>
    </mu-flexbox>
    <menuList :open="open" v-on:closeMenu="closeMenu" :menu="menu"></menuList>
  </div>
</template>
<script>
  import MenuList from '../Menu/menuList'
  import * as API from './API.js'
  import { getCookie } from '../../../cookieUtil.js'
  import sign1 from '../../../assets/schoolIcon.jpg'
  import sign2 from '../../../assets/biaoyu.png'
  export default {
    name: 'Home',
    components: {
      'menuList': MenuList
    },
    data () {
      return {
        sign1,
        sign2,
        open: false,
        bottomPopup: false,
        permission: [],
        menu: [],
        icon: '',
        color: '',
        message: '',
        schoolName: '济南市育明小学'
      }
    },
    created: function () {
      if (getCookie('MenuMobile') === null || getCookie('MenuMobile') === undefined || getCookie('MenuMobile') === '' || getCookie(API.base) === null || getCookie(API.base) === undefined || getCookie(API.base) === '') {
        this.$http.get(
          API.menu
        ).then((response) => {
          if (response.body.toString() === 'illegal' || response.body.toString() === 'overdue') {
            this.openPopup('登录过期或非法操作!', 'error', 'red')
          } else {
            this.menu = JSON.parse(JSON.parse(getCookie('MenuMobile')))
          }
        }, (response) => {
          this.openPopup('服务器内部错误!', 'error', 'red')
        })
      } else {
        this.menu = JSON.parse(JSON.parse(getCookie('MenuMobile')))
      }
    },
    methods: {
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
