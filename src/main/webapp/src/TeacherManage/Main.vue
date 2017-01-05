<template>
  <div id="teacherManage">
    <bar></bar>
    <add iconName="add" hrefLocation="#/add"></add>
  </div>
</template>

<script>
  import Bar from './Bar'
  import Add from './CircleBtn'
  import * as AF from '../Util/AjaxFunction.js'
  export default {
    name: 'teacherManage',
    components: {
      'bar': Bar,
      'add': Add
    },
    created: function () {
      this.$http.get(AF.TeacherGetName).then((response) => {
        window.document.title = response.data
      }, (response) => {
      })
    },
    methods: {
      incrementTotal: function () {
        console.log('1111111')
      }
    }
  }
</script>

