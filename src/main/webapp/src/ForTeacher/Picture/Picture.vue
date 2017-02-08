<template>
  <div class="Picture">
    <mu-float-button icon="edit" @click="choosePicture" primary/>
    <img :src="localIds" width="360px" height="360px"/>
  </div>
</template>
<script>
  import wx from 'weixin-js-sdk'
  import * as API from './PictureAPI.js'
  export default {
    name: 'Picture',
    data () {
      return {
        open: false,
        localIds: ''
      }
    },
    created () {
      this.$http.get(
        API.Get,
        { headers: { 'X-Requested-With': 'XMLHttpRequest' } }
      ).then((response) => {
        console.log(response.body)
        console.log(response.body.appId)
        console.log(response.body.timestamp)
        console.log(response.body.nonceStr)
        console.log(response.body.signature)
        wx.config({
          debug: false,
          appId: response.body.appId,
          timestamp: parseInt(response.body.timestamp),
          nonceStr: response.body.nonceStr,
          signature: response.body.signature,
          jsApiList: [
            'chooseImage'
          ]
        })
        wx.ready(function () {
        })
      }, (response) => {
      })
    },
    methods: {
      choosePicture () {
        wx.chooseImage({
          count: 1, // 默认9
          sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
          sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
          success: function (res) {
            console.log('成功调用')
            this.localIds = res.localIds // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
          }
        })
      }
    }
  }
</script>
