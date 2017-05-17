<template>
  <div>
    <Button type="error" size="large" @click="goDelete" v-if="Delete">删除</Button>
  </div>
</template>
<script>
  import { getCookie } from '../../cookieUtil.js'
  import { bus } from './bus.js'
  export default {
    props: ['params', 'cookieName', 'url'],
    data () {
      return {
        cookieName: '',
        url: '',
        Delete: false,
        params: ''
      }
    },
    created: function () {
      if (getCookie(this.cookieName) === null || getCookie(this.cookieName) === undefined || getCookie(this.cookieName) === '') {
        this.$http.get(
          this.url
        ).then((res) => {
          this.permission = JSON.parse(JSON.parse(getCookie(this.cookieName)))
          this.Delete = this.permission.Delete
        }, (res) => {
          this.$Notice.error({
            title: '服务器内部错误!'
          })
        })
      } else {
        this.permission = JSON.parse(JSON.parse(getCookie(this.cookieName)))
        this.Delete = this.permission.Delete
      }
    },
    methods: {
      goDelete: function () {
        bus.$emit('forDelete', this.params.index)
      }
    }
  }
</script>
