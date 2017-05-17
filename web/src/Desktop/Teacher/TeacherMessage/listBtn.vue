<template>
  <div>
    <Button type="primary" @click="goEdit" v-if="Edit">修改</Button>
    <Button type="warning" @click="goInactive" v-if="Inactive">注销</Button>
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
        Edit: false,
        Inactive: false,
        params: ''
      }
    },
    created: function () {
      if (getCookie(this.cookieName) === null || getCookie(this.cookieName) === undefined || getCookie(this.cookieName) === '') {
        this.$http.get(
          API.base
        ).then((res) => {
          this.$http.get(
            this.url,
            { params: {
              id: this.params.row.id
          } },
          ).then((r) => {
            this.Edit = r.teacher_id
            this.Inactive = this.permission.Inactive
          }, (res) => {
            this.$Notice.error({
              title: '服务器内部错误!'
            })
          })
          this.permission = JSON.parse(JSON.parse(getCookie(this.cookieName)))
          this.Edit = this.permission.Edit
          this.Inactive = this.permission.Inactive
        }, (res) => {
          this.$Notice.error({
            title: '服务器内部错误!'
          })
        })
      } else {
        this.permission = JSON.parse(JSON.parse(getCookie(this.cookieName)))
        this.Edit = this.permission.Edit
        this.Inactive = this.permission.Inactive
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
