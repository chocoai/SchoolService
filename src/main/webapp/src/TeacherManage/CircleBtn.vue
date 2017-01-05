<template>
  <div class="CircleBtn">
    <mu-float-button class="demo-float-button" :icon="iconName" :href="hrefLocation"/>
  </div>
</template>
<script>
  export default {
    name: 'CircleBtn',
    props: {
      'iconName': { type: String, required: true, default: '' },
      'hrefLocation': { type: String, required: true, default: '' }
    }
  }
</script>
<style>
  .CircleBtn {
    margin-right: 12px;
    position: absolute;
    right: 1px;
    bottom: 1px;
   }
</style>
