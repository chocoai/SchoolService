<template>
  <div>
    <Button type="primary" @click="goEdit" v-if="Edit">修改</Button>
    <Button type="warning" @click="goInactive" v-if="Inactive">注销</Button>
    <Button type="success" @click="goActive" v-if="Active">激活</Button>
    <Button type="error" @click="goDelete" v-if="Delete">删除</Button>
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
        Inactive: false,
        Active: false,
        Delete: false,
        params: ''
      }
    },
    created: function () {
      const states1 = this.params.row.state.toString() === '1'
      const states2 = this.params.row.state.toString() === '0'
      if (getCookie(this.cookieName) === null || getCookie(this.cookieName) === undefined || getCookie(this.cookieName) === '') {
        this.$http.get(
          this.url
        ).then((res) => {
          this.permission = JSON.parse(JSON.parse(getCookie(this.cookieName)))
          this.Edit = this.permission.Edit
          this.Inactive = this.permission.Inactive && states1
          this.Active = this.permission.Active && states2
          this.Delete = this.permission.Delete
        }, (res) => {
          this.$Notice.error({
            title: '服务器内部错误!'
          })
        })
      } else {
        this.permission = JSON.parse(JSON.parse(getCookie(this.cookieName)))
        this.Edit = this.permission.Edit
        this.Inactive = this.permission.Inactive && states1
        this.Active = this.permission.Active && states2
        this.Delete = this.permission.Delete
      }
    },
    methods: {
      goEdit: function () {
        bus.$emit('forEdit', this.params.index)
      },
      goInactive: function () {
        bus.$emit('forInactive', this.params.index)
      },
      goActive: function () {
        bus.$emit('forActive', this.params.index)
      },
      goDelete: function () {
        bus.$emit('forDelete', this.params.index)
      }
    }
  }
</script>
