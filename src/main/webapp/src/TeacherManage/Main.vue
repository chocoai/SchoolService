<template>
  <div id="teacherManage">
    <addBtn></addBtn>
    <search></search>
  </div>
</template>

<script>
  import AddBtn from './AddBtn'
  import Search from './Search'
  export default {
    name: 'teacherManage',
    components: {
      AddBtn,
      Search
    }
  }
</script>

