<template>
  <div id="teacherManage">
    <mu-appbar title="">
      <mu-text-field icon="search" class="appbar-search-field" hintText="请输入教师姓名" @input="query" :value="queryString"/>
    </mu-appbar>
    <mu-list>
      <mu-list-item v-for="teacher in teachers" :value="teacher.id" :title="teacher.name" :describeText="teacher.phone" @click="look(teacher.id)">
        <mu-avatar :src="teacher.name" slot="leftAvatar" :size="30"/>
        <mu-avatar icon="link" slot="rightAvatar" :size="30"/>
      </mu-list-item>
    </mu-list>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-raised-button v-if="before" label="上一页" labelPosition="before" icon="navigate_before" @click="pageBefore" />
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <div v-if="chip">
          {{pageCurrent}}/{{pageTotal}}
        </div>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-raised-button v-if="next" label="下一页" labelPosition="before" icon="navigate_next" @click="pageNext" />
      </mu-flexbox-item>
    </mu-flexbox>
    <add iconName="add" hrefLocation="#/add"></add>
  </div>
</template>

<script>
  import Add from './CircleBtn'
  import * as AF from '../Util/AjaxFunction.js'
  export default {
    name: 'teacherManage',
    data () {
      return {
        before: false,
        next: false,
        chip: false,
        queryString: '',
        teachers: [],
        pageTotal: '',
        pageSize: '6',
        pageCurrent: ''
      }
    },
    components: {
      'add': Add
    },
    created: function () {
      this.$http.get(AF.TeacherGetName).then((response) => {
        window.document.title = response.data
      }, (response) => {
      })
      this.queryString = this.$store.state.queryString
      this.pageCurrent = this.$store.state.pageCurrent
      this.teacherQuery(this.queryString, this.pageCurrent, this.pageSize)
      this.teacherTotal(this.queryString, this.pageSize)
    },
    methods: {
      teacherQuery (queryString, pageCurrent, pageSize) {
        this.$http.get(
          AF.TeacherQueryByName,
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
      teacherTotal (queryString, pageSize) {
        this.$http.get(
          AF.TeacherTotalByName,
          { params:
          {
            queryString: queryString,
            pageSize: pageSize
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
      look (teacherId) {
        window.location.href = '#/look/' + teacherId
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
    height: 32px;
    text-align: center;
    line-height: 32px;
  }
</style>
