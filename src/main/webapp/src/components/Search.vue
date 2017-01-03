<template>
  <div class="Search">
    <mt-search
      v-model="value"
      cancel-text="取消"
      placeholder="搜索教师">
    </mt-search>
  </div>
</template>

<script>
export default {
  name: 'Search'
}
</script>
