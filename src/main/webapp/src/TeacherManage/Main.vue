<template>
  <div id="teacherManage">
    <mu-appbar title="">
      <mu-text-field icon="search" class="appbar-search-field" hintText="请输入教师姓名" @input="query"/>
    </mu-appbar>
    <mu-list>
      <mu-list-item v-for="teacher in teachers" :value="teacher.id" :title="teacher.name" :describeText="teacher.phone" @click="look(teacher.id)">
        <mu-avatar :src="teacher.name" slot="leftAvatar" :size="30"/>
        <mu-icon-menu slot="right" icon="keyboard_arrow_right" tooltip="操作">
        </mu-icon-menu>
      </mu-list-item>
    </mu-list>
    <mu-pagination :total="pageTotal" :current="pageCurrent" :pageSize="pageSize" @pageChange="pageChange">
    </mu-pagination>
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
        queryName: '',
        teachers: [],
        pageTotal: '',
        pageSize: '',
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

      this.$http.get(
        AF.TeacherQueryByName,
        { params:
        {
          name: '',
          pageCurrent: '1',
          pageSize: '6'
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
      }, (response) => {
      })

      this.$http.get(
        AF.TeacherTotalByName,
        { params:
        {
          name: ''
        }
        }
      ).then((response) => {
        this.pageTotal = response.body
        this.pageCurrent = '1'
        console.log(this.pageTotal)
      }, (response) => {
      })
    },
    methods: {
      query (value) {
        this.$http.get(
          AF.TeacherQueryByName,
          { params:
          {
            name: value
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
          this.queryName = value
        }, (response) => {
        })
      },
      look (teacherId) {
        window.location.href = '#/look/' + teacherId
      },
      pageChange (newPage) {
        this.$http.get(
          AF.TeacherQueryByName,
          { params:
          {
            name: this.queryName,
            pageCurrent: newPage,
            pageSize: '6'
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
          this.pageCurrent = newPage
        }, (response) => {
        })
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
</style>
