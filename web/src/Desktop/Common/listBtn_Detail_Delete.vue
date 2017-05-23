<template>
  <div>
    <Button type="primary" @click="goDetail" v-if="Detail">详情</Button>
    <Button type="warning" @click="goDelete" v-if="Delete">删除</Button>
  </div>
</template>
<script>
  import { getCookie } from '../../cookieUtil.js'
  import { bus } from './bus.js'
  import * as API from './API.js'
  export default {
    props: ['params', 'cookieName', 'url'],
    data () {
      return {
        cookieName: '',
        url: '',
        Detail: false,
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
          this.Detail = this.permission.Detail
          this.Delete = this.permission.Delete
        }, (res) => {
          this.$Notice.error({
            title: '服务器内部错误!'
          })
        })
      } else {
        this.permission = JSON.parse(JSON.parse(getCookie(this.cookieName)))
        this.Detail = this.permission.Detail
        this.Delete = this.permission.Delete
      }
    },
    methods: {
      goDetail: function () {
        bus.$emit('forDetail', this.params.index)
      },
      goDelete: function () {
        bus.$emit('forDelete', this.params.index)
      }
    }
  }
</script>
