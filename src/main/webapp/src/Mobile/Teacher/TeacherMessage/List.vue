<template>
  <div>
    <mu-appbar title="">
      <mu-icon-button icon='menu' slot="left" @click="openMenu"/>
      <mu-text-field icon="search" class="appbar-search-field" hintText="请输入关键词" @input="goQuery" :value="queryString"/>
    </mu-appbar>
    <mu-list>
      <mu-list-item v-for="teacherMessage in list" :value="teacherMessage.id" :title="teacherMessage.title" :describeText="getString(teacherMessage.tname, teacherMessage.ttime, teacherMessage.treply)" :afterText="getState(teacherMessage.tstate)" @click="goEdit(teacherMessage.id)">
        <mu-icon v-if="teacherMessage.state.toString() === '0'" slot="left" color="lime300" value="assignment_late" :size="40" />
        <mu-icon v-if="teacherMessage.state.toString() === '1'" slot="left" color="teal300" value="assignment_turned_in" :size="40" />
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
  import * as API from './API.js'
  import MenuList from '../Menu/MenuList'
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
        icon: '',
        color: '',
        message: '',
        queryString: '',
        list: [],
        pageTotal: '',
        pageSize: '7',
        pageCurrent: ''
      }
    },
    created: function () {
      this.queryString = this.$store.state.queryString
      this.pageCurrent = this.$store.state.pageCurrent
      this.query(this.queryString, this.pageCurrent, this.pageSize)
      this.total(this.queryString, this.pageSize)
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
      query (queryString, pageCurrent, pageSize) {
        this.$http.get(
          API.query,
          { params: {
            queryString: queryString,
            pageCurrent: pageCurrent,
            pageSize: pageSize
          } },
          { headers: { 'X-Requested-With': 'XMLHttpRequest' }, emulateJSON: true }
        ).then((response) => {
          this.list = response.body
          this.pageCurrent = pageCurrent
          this.pageCurrent.toString() === '1' ? this.before = false : this.before = true
        }, (response) => {
          this.openPopup('服务器内部错误!', 'error', 'red')
        })
      },
      total (queryString, pageSize) {
        this.$http.get(
          API.total,
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
      goQuery (value) {
        this.queryString = value
        this.pageCurrent = '1'
        this.$store.commit('save', {
          queryString: this.queryString,
          pageCurrent: this.pageCurrent
        })
        this.query(this.queryString, this.pageCurrent, this.pageSize)
        this.total(this.queryString, this.pageSize)
        this.before = false
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
      getString (name, time, reply) {
        if (reply.toString() === '1') {
          return name.toString() + '于' + time.toString() + '发布,并要求确认!'
        } else if (reply.toString() === '0') {
          return name.toString() + '于' + time.toString() + '发布!'
        } else {
          return '状态错误'
        }
      },
      goEdit (id) {
        this.$router.push({ path: '/edit/' + id })
        this.$store.commit('save', {
          queryString: this.queryString,
          pageCurrent: this.pageCurrent
        })
      },
      pageBefore () {
        this.pageCurrent--
        this.pageCurrent.toString() === '1' ? this.before = false : this.before = true
        this.next = true
        this.query(this.queryString, this.pageCurrent, this.pageSize)
      },
      pageNext () {
        this.pageCurrent++
        this.pageCurrent.toString() === this.pageTotal ? this.next = false : this.next = true
        this.before = true
        this.query(this.queryString, this.pageCurrent, this.pageSize)
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
