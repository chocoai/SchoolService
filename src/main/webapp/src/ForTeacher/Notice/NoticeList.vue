<template>
  <div class="NoticeList">
    <mu-appbar title="">
      <mu-icon-button icon='menu' slot="left" @click="openMenu"/>
      <mu-text-field icon="search" class="appbar-search-field" hintText="请输入关键词" @input="query" :value="queryString"/>
    </mu-appbar>
    <mu-list>
      <mu-list-item v-for="notice in notices" :value="notice.id" :title="notice.title" :afterText="notice.name" :describeText="notice.time" @click="look(notice.id)">
        <mu-avatar v-if="notice.type.toString() === '1'" slot="leftAvatar" color="cyan50" backgroundColor="teal500">全</mu-avatar>
        <mu-avatar v-if="notice.type.toString() === '2'" slot="leftAvatar" color="cyan50" backgroundColor="teal500">教</mu-avatar>
        <mu-avatar v-if="notice.type.toString() === '3'" slot="leftAvatar" color="cyan50" backgroundColor="teal500">家</mu-avatar>
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
    <menuList :open="open" v-on:closeMenu="closeMenu"></menuList>
    <mu-popup position="bottom" :overlay="false" popupClass="popup-bottom" :open="bottomPopup">
      <mu-icon :value="icon" :size="36" :color="color"/>&nbsp;{{ message }}
    </mu-popup>
  </div>
</template>

<script>
  import * as API from './NoticeAPI.js'
  import MenuList from '../Menu/MenuList'
  export default {
    name: 'NoticeList',
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
        icon: '',
        color: '',
        message: '',
        queryString: '',
        notices: [],
        pageTotal: '',
        pageSize: '7',
        pageCurrent: ''
      }
    },
    created: function () {
      this.queryString = this.$store.state.queryString
      this.pageCurrent = this.$store.state.pageCurrent
      this.noticeQuery(this.queryString, this.pageCurrent, this.pageSize)
      this.noticeTotal(this.queryString, this.pageSize)
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
      },
      noticeQuery (queryString, pageCurrent, pageSize) {
        this.$http.get(
          API.Query,
          { params: {
            queryString: queryString,
            pageCurrent: pageCurrent,
            pageSize: pageSize
          } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
        ).then((response) => {
          this.notices = response.body
          this.pageCurrent = pageCurrent
          this.pageCurrent.toString() === '1' ? this.before = false : this.before = true
        }, (response) => {
          this.openPopup('服务器内部错误!', 'error', 'red')
        })
      },
      noticeTotal (queryString, pageSize) {
        this.$http.get(
          API.Total,
          { params: {
            queryString: queryString,
            pageSize: pageSize
          } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
        ).then((response) => {
          this.pageTotal = response.body
          this.pageTotal === '0' ? this.chip = false : this.chip = true
          this.pageTotal === '1' || this.pageTotal === '0' || this.pageTotal.toString() === this.$store.state.pageCurrent.toString() ? this.next = false : this.next = true
        }, (response) => {
          this.openPopup('服务器内部错误!', 'error', 'red')
        })
      },
      query (value) {
        this.queryString = value
        this.pageCurrent = '1'
        this.$store.commit('save', {
          queryString: this.queryString,
          pageCurrent: this.pageCurrent
        })
        this.noticeQuery(this.queryString, this.pageCurrent, this.pageSize)
        this.noticeTotal(this.queryString, this.pageSize)
        this.before = false
      },
      look (noticeId) {
        this.$router.push({ path: '/noticeLook/' + noticeId })
        this.$store.commit('save', {
          queryString: this.queryString,
          pageCurrent: this.pageCurrent
        })
      },
      pageBefore () {
        this.pageCurrent--
        this.pageCurrent.toString() === '1' ? this.before = false : this.before = true
        this.next = true
        this.noticeQuery(this.queryString, this.pageCurrent, this.pageSize)
      },
      pageNext () {
        this.pageCurrent++
        this.pageCurrent.toString() === this.pageTotal ? this.next = false : this.next = true
        this.before = true
        this.noticeQuery(this.queryString, this.pageCurrent, this.pageSize)
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
</style>
