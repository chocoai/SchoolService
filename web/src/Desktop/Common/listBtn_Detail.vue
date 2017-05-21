<template>
  <div>
    <Button type="primary" @click="goEdit" v-if="Edit">详情</Button>
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
        Edit: false,
        params: ''
      }
    },
    created: function () {
      if (getCookie(this.cookieName) === null || getCookie(this.cookieName) === undefined || getCookie(this.cookieName) === '') {
        this.$http.get(
          this.url
        ).then((res) => {
          this.permission = JSON.parse(JSON.parse(getCookie(this.cookieName)))
          this.Edit = this.permission.Edit
        }, (res) => {
          this.$Notice.error({
            title: '服务器内部错误!'
          })
        })
      } else {
        this.permission = JSON.parse(JSON.parse(getCookie(this.cookieName)))
        this.Edit = this.permission.Edit
      }
    },
    methods: {
      goEdit: function () {
        bus.$emit('forEdit', this.params.index)
      }
    }
  }
</script>
