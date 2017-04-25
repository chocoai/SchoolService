<template>
  <div class="layout">
    <MenuList active="home" :name="name" three="blank"></MenuList>
    <div class="layout-content">
      内容区域
    </div>
    <Copy></Copy>
  </div>
</template>
<script>
  import Copy from '../../Common/copy.vue'
  import MenuList from '../Menu/menuList.vue'
  export default {
    name: 'home',
    components: { Copy, MenuList },
    data () {
      return {
        name: 'xxx'
      }
    }
  }
</script>
<style scoped>
  .layout{
    border: 1px solid #d7dde4;
    background: #f5f7f9;
  }

  .layout-assistant{
    width: 300px;
    margin: 0 auto;
    height: inherit;
  }
  .layout-content{
    min-height: 200px;
    margin: 15px;
    overflow: hidden;
    background: #fff;
    border-radius: 4px;
  }
  .layout-content-main{
    padding: 10px;
  }
</style>
