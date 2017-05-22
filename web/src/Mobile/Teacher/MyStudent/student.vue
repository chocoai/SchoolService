<template>
  <div>
    <mu-appbar title="学生详情">
      <mu-icon-button icon='reply' slot="right" @click="goReply" v-if="permission.Page"/>
    </mu-appbar>
    <mu-text-field disabled v-model="name" label="姓名" icon="comment" fullWidth labelFloat/><br/>
    <mu-text-field disabled v-model="number" label="证件号码" icon="comment"  fullWidth labelFloat/><br/>
    <mu-text-field disabled v-model="code" label="学籍号码" icon="comment" fullWidth labelFloat/><br/>
    <mu-text-field disabled v-model="address" label="住址" icon="store_mall_directory"  fullWidth labelFloat/><br/>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="cached" v-if="permission.GetStudent" @click="goReset" backgroundColor="orange"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-dialog :open="Reading" title="正在读取" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-popup position="bottom" :overlay="false" popupClass="popup-bottom" :open="bottomPopup">
      <mu-icon :value="icon" :size="36" :color="color"/>&nbsp;{{ message }}
    </mu-popup>
  </div>
</template>

<script>
import * as API from './API.js'
import { getCookie } from '../../../cookieUtil.js'
export default {
  name: 'Student',
  data () {
    return {
      Saving: false,
      Reading: true,
      bottomPopup: false,
      permission: [],
      menu: [],
      icon: '',
      color: '',
      message: '',
      object: [],
      name: '',
      number: '',
      code: '',
      address: '',
      state: ''
    }
  },
  created () {
    this.fetchData(this.$route.params.id)
    if (getCookie('MenuMobile') === null || getCookie('MenuMobile') === undefined || getCookie('MenuMobile') === '' || getCookie(API.base) === null || getCookie(API.base) === undefined || getCookie(API.base) === '') {
      this.$http.get(
        API.menu
      ).then((response) => {
        if (response.body.toString() === 'illegal' || response.body.toString() === 'overdue') {
          this.openPopup('登录过期或非法操作!', 'error', 'red')
        } else {
          this.$http.get(
            API.permission
          ).then((res) => {
            this.permission = JSON.parse(JSON.parse(getCookie(API.base)))
            this.menu = JSON.parse(JSON.parse(getCookie('MenuMobile')))
          }, (res) => {
            this.openPopup('服务器内部错误!', 'error', 'red')
          })
        }
      }, (response) => {
        this.openPopup('服务器内部错误!', 'error', 'red')
      })
    } else {
      this.permission = JSON.parse(JSON.parse(getCookie(API.base)))
      this.menu = JSON.parse(JSON.parse(getCookie('MenuMobile')))
    }
  },
  watch: {
    // 如果路由有变化，会再次执行该方法
    '$route': 'fetchData'
  },
  methods: {
    goReply () {
      this.$router.push({ path: '/list' })
    },
    goReset () {
      this.Reading = true
      this.fetchData(this.$route.params.id)
    },
    openPopup (message, icon, color) {
      this.message = message
      this.icon = icon
      this.color = color
      this.bottomPopup = true
      setTimeout(() => { this.bottomPopup = false }, 1500)
    },
    fetchData (id) {
      this.$http.get(
        API.student,
        { params: { id: id } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        this.object = response.body
        this.name = this.object.name
        this.number = this.object.number
        this.code = this.object.code
        this.address = this.object.address
        this.state = this.object.state
        this.Reading = false
      }, (response) => {
      })
    }
  }
}
</script>
<style lang="css">
  .flex-demo {
    height: 70px;
    text-align: center;
    line-height: 32px;
  }
  .popup-bottom {
    width: 100%;
    opacity: .8;
    height: 48px;
    line-height: 48px;
    display: flex;
    align-items: center;
    justify-content: center;
    max-width: 300px;
  }
</style>
