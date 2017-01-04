<template>
  <div class="Bar">
    <mu-appbar v-bind:title="userName">
      <mu-text-field icon="search" class="appbar-search-field" slot="right" hintText="请输入教师姓名"/>
      <mu-flat-button color="white" label="搜索" slot="right"/>
    </mu-appbar>
  </div>
</template>

<script>
import * as AF from '../Util/AjaxFunction.js'
export default {
  name: 'Bar',
  data () {
    return {
      userName: '未知'
    }
  },
  created: function () {
    this.$http.get(AF.TeacherGetName).then((response) => {
      this.userName = response.data
      console.log(response)
    }, (response) => {
    })
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
