<template>
  <div id="teacherManage">
    <bar></bar>
    <add></add>
  </div>
</template>

<script>
  import Bar from './Bar'
  import Add from './Add'
  export default {
    name: 'teacherManage',
    components: {
      'bar': Bar,
      'add': Add
    },
    methods: {
      incrementTotal: function () {
        console.log('1111111')
      }
    }
  }
</script>

