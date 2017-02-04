<template>
  <div class="NoticeLook">
    <mu-appbar title="信息详情">
      <mu-icon-button icon='reply' slot="right" @click="gorReply"/>
    </mu-appbar>
    <mu-text-field label="标题" disabled underlineShow="false" v-model="title" fullWidth labelFloat icon="title" maxLength="20"/><br/>
    <mu-text-field label="内容" disabled underlineShow="false" v-model="content" fullWidth labelFloat icon="note" multiLine :rows="9" :rowsMax="12" :maxLength="300"/><br/>
    <mu-text-field label="时间" disabled underlineShow="false" v-model="time" fullWidth labelFloat icon="query_builder"/><br/>
    <mu-dialog :open="forRead" title="正在读取" >
      <mu-circular-progress :size="60" :strokeWidth="5"/>请稍后
    </mu-dialog>
    <mu-popup position="bottom" :overlay="false" popupClass="popup-bottom" :open="bottomPopup">
      <mu-icon :value="icon" :size="36" :color="color"/>&nbsp;{{ message }}
    </mu-popup>
  </div>
</template>
<script>
import * as API from './NoticeAPI.js'
export default {
  name: 'NoticeLook',
  data () {
    return {
      bottomPopup: false,
      saved: false,
      forRead: true,
      icon: '',
      color: '',
      title: '',
      content: '',
      time: '',
      message: ''
    }
  },
  created () {
    this.fetchData(this.$route.params.noticeId)
  },
  watch: {
    // 如果路由有变化，会再次执行该方法
    '$route': 'fetchData'
  },
  methods: {
    gorReply () {
      this.$router.push({ path: '/noticeList' })
    },
    openPopup (message, icon, color) {
      this.message = message
      this.icon = icon
      this.color = color
      this.bottomPopup = true
      setTimeout(() => { this.bottomPopup = false }, 1500)
    },
    fetchData (noticeId) {
      this.$http.get(
        API.GetById,
        { params: { id: noticeId } },
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        this.notice = response.body
        this.title = this.notice.title
        this.content = this.notice.content
        this.time = this.notice.time
        this.forRead = false
      }, (response) => {
        this.openPopup('服务器内部错误!', 'error', 'red')
      })
    }
  }
}
</script>
<style lang="css">
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
  .flex-demo {
    height: 70px;
    text-align: center;
    line-height: 32px;
  }
</style>
