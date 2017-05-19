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
    <menuList :open="open" v-on:closeMenu="closeMenu"></menuList>
  </div>
</template>
<script>
  import MenuList from '../Menu/MenuList'
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
        icon: '',
        color: '',
        message: '',
        schoolName: '济南市育明小学'
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
