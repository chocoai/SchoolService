<template>
  <div class="TagList">
    <mu-appbar title="">
      <mu-icon-button icon='menu' slot="left" @click="openMenu"/>
      <mu-text-field icon="search" class="appbar-search-field" hintText="请输入标签名" @input="query" :value="queryString"/>
      <mu-icon-button icon='add' slot="right" @click="goAdd"/>
    </mu-appbar>
    <mu-list>
      <mu-list-item v-for="tag in tags" :value="tag.id" :title="tag.name" :describeText="tag.type" @click="look(tag.id)">
        <mu-avatar :src="tag.type" slot="leftAvatar" :size="30"/>
        <mu-avatar icon="link" slot="rightAvatar" :size="30"/>
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
  </div>
</template>

<script>
  import * as API from './TagAPI.js'
  import MenuList from '../components/MenuList'
  export default {
    name: 'TagList',
    components: {
      'menuList': MenuList
    },
    data () {
      return {
        before: false,
        next: false,
        chip: false,
        open: false,
        queryString: '',
        teachers: [],
        pageTotal: '',
        pageSize: '7',
        pageCurrent: ''
      }
    },
    created: function () {
      this.queryString = this.$store.state.queryString
      this.pageCurrent = this.$store.state.pageCurrent
      this.tagQuery(this.queryString, this.pageCurrent, this.pageSize)
      this.tagTotal(this.queryString, this.pageSize)
    },
    methods: {
      openMenu () {
        this.open = true
      },
      closeMenu () {
        this.open = false
      },
      tagQuery (queryString, pageCurrent, pageSize) {
        this.$http.get(
          API.QueryByName,
          { params:
          {
            queryString: queryString,
            pageCurrent: pageCurrent,
            pageSize: pageSize
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
          this.teachers = response.body
          this.pageCurrent = pageCurrent
          this.pageCurrent.toString() === '1' ? this.before = false : this.before = true
        }, (response) => {
        })
      },
      tagTotal (queryString, pageSize) {
        this.$http.get(
          API.TotalByName,
          { params:
          {
            queryString: queryString,
            pageSize: pageSize
          }
          },
          {
            headers:
            {
              'X-Requested-With': 'XMLHttpRequest'
            }
          }
        ).then((response) => {
          this.pageTotal = response.body
          this.pageTotal === '0' ? this.chip = false : this.chip = true
          this.pageTotal === '1' || this.pageTotal === '0' || this.pageTotal.toString() === this.$store.state.pageCurrent.toString() ? this.next = false : this.next = true
        }, (response) => {
        })
      },
      query (value) {
        this.queryString = value
        this.pageCurrent = '1'
        this.$store.commit('save', {
          queryString: this.queryString,
          pageCurrent: this.pageCurrent
        })
        this.teacherQuery(this.queryString, this.pageCurrent, this.pageSize)
        this.teacherTotal(this.queryString, this.pageSize)
        this.before = false
      },
      look (tagId) {
        this.$router.push({ path: '/tagEdit/' + tagId })
        this.$store.commit('save', {
          queryString: this.queryString,
          pageCurrent: this.pageCurrent
        })
      },
      pageBefore () {
        this.pageCurrent--
        this.pageCurrent.toString() === '1' ? this.before = false : this.before = true
        this.next = true
        this.teacherQuery(this.queryString, this.pageCurrent, this.pageSize)
      },
      pageNext () {
        this.pageCurrent++
        this.pageCurrent.toString() === this.pageTotal ? this.next = false : this.next = true
        this.before = true
        this.teacherQuery(this.queryString, this.pageCurrent, this.pageSize)
      },
      goAdd () {
        this.$router.push({ path: '/tagAdd' })
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
