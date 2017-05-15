<template>
  <div>
    <Button type="primary" size="large" @click="goEdit" v-if="Edit">修改</Button>
    <Button type="warning" size="large" @click="goInactive" v-if="Inactive">注销</Button>
    <Button type="success" size="large" @click="goActive" v-if="Active">激活</Button>
    <Button type="error" size="large" @click="goDelete" v-if="Delete">删除</Button>
  </div>
</template>
<script>
  import { getCookie } from '../../../cookieUtil.js'
  import { bus } from '../../Common/bus.js'
  import * as API from './API.js'
  export default {
    props: ['params', 'cookieName'],
    data () {
      return {
        cookieName: '',
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
          API.permission
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
