<template>
  <div>
    <Button type="primary" @click="goDetail" v-if="Detail">详情</Button>
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
        Detail: false,
        params: ''
      }
    },
    created: function () {
      if (getCookie(this.cookieName) === null || getCookie(this.cookieName) === undefined || getCookie(this.cookieName) === '') {
        this.$http.get(
          this.url
        ).then((res) => {
          this.permission = JSON.parse(JSON.parse(getCookie(this.cookieName)))
          this.Detail = this.permission.Detail
        }, (res) => {
          this.$Notice.error({
            title: '服务器内部错误!'
          })
        })
      } else {
        this.permission = JSON.parse(JSON.parse(getCookie(this.cookieName)))
        this.Detail = this.permission.Detail
      }
    },
    methods: {
      goDetail: function () {
        bus.$emit('forDetail', this.params.index)
      }
    }
  }
</script>
