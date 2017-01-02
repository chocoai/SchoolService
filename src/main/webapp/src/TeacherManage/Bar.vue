<template>
  <div class="Bar">
    <mu-appbar title="">
      <mu-text-field icon="search" class="appbar-search-field"  slot="right" hintText="请输入教师姓名"/>
      <mu-flat-button color="white" label="搜索" slot="right"/>
    </mu-appbar>
  </div>
</template>

<script>
export default {
  name: 'Bar'
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
