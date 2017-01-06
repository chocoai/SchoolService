<template>
  <div id="teacherManage">
    <bar></bar>
    <mu-list>
      <mu-list-item v-for="teacher in teachers" :value="teacher.id" :title="teacher.name" :describeText="teacher.phone" @click="look(teacher.id)">
        <mu-avatar :src="teacher.name" slot="leftAvatar" :size="30"/>
        <mu-icon-menu slot="right" icon="more_vert" tooltip="操作" :value="teacher.id" @itemClick="dod(teacher.id)">
          <mu-menu-item title="查看"/>
          <mu-menu-item title="修改"/>
          <mu-menu-item title="删除"/>
        </mu-icon-menu>
      </mu-list-item>
    </mu-list>
    <add iconName="add" hrefLocation="#/add"></add>
  </div>
</template>

<script>
  import Bar from './Bar'
  import Add from './CircleBtn'
  import * as AF from '../Util/AjaxFunction.js'
  export default {
    name: 'teacherManage',
    data () {
      return {
        teachers: []
      }
    },
    components: {
      'bar': Bar,
      'add': Add
    },
    created: function () {
      this.$http.get(AF.TeacherGetName).then((response) => {
        window.document.title = response.data
      }, (response) => {
      })
      this.$http.get(
        AF.TeacherQuery,
        { params:
        {
          name: ''
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
    },
    methods: {
      look (teacherId) {
        window.location.href = '#/look/' + teacherId
      },
      dod (id) {
        console.log(id)
      }
    }
  }
</script>

