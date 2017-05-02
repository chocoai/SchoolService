<template>
  <div class="layout">
    <MenuList active="home" :name="name" three="blank" :permission="permission"></MenuList>
    <div class="layout-content">
      你好!
    </div>
    <Copy></Copy>
  </div>
</template>
<script>
  import Copy from '../../Common/copy.vue'
  import MenuList from '../Menu/menuList.vue'
  import * as API from './API.js'
  import { getCookie } from '../../../cookieUtil.js'
  export default {
    name: 'home',
    components: { Copy, MenuList },
    data () {
      return {
        name: '',
        permission: []
      }
    },
    created: function () {
      if (getCookie('permission') === null || getCookie('permission') === undefined || getCookie('permission') === '') {
        this.$http.get(
          API.permission
        ).then((response) => {
          if (response.body.toString() === 'illegal' || response.body.toString() === 'overdue') {
            this.$Notice.error({
              title: '登录过期或非法操作!'
            })
          } else {
            this.permission = JSON.parse(JSON.parse(getCookie('permission')))
            this.name = decodeURI(getCookie('name')).substring(1, decodeURI(getCookie('name')).length - 1)
          }
        }, (response) => {
          this.$Notice.error({
            title: '服务器内部错误!'
          })
        })
      } else {
        this.permission = JSON.parse(JSON.parse(getCookie('permission')))
        this.name = decodeURI(getCookie('name')).substring(1, decodeURI(getCookie('name')).length - 1)
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
