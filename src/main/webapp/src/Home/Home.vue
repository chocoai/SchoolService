<template>
  <div class="Home">
    <mu-appbar :title="schoolName">
      <mu-icon-button icon='menu' slot="left" @click="openMenu"/>
    </mu-appbar>
    <mu-flexbox orient="vertical">
      <mu-flexbox-item >
        <img :src="sign" />
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-flexbox orient="vertical">
      <mu-flexbox-item >
        孕育明日希望
      </mu-flexbox-item>
    </mu-flexbox>
    <menuList :open="open" v-on:closeMenu="closeMenu"></menuList>
  </div>
</template>
<script>
  import * as API from './HomeAPI.js'
  import MenuList from '../components/MenuList'
  import sign from 'assets/schoolIcon.jpg'
  export default {
    name: 'Home',
    components: {
      'menuList': MenuList
    },
    data () {
      return {
        sign,
        open: false,
        schoolName: ''
      }
    },
    created: function () {
      this.$http.get(API.GetSchoolName, { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }).then((response) => {
        this.schoolName = response.data
      }, (response) => {
      })
    },
    methods: {
      openMenu () {
        this.open = true
      },
      closeMenu () {
        this.open = false
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
