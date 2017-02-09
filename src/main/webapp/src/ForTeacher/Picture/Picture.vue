<template>
  <div class="Picture">
    <mu-flat-button label="选择" v-on:click.native="chooseImage" primary/>
    <img :src="localIds"/>
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
      }, (response) => {
      })
    },
    methods: {
      chooseImage () {
        wx.chooseImage({
          count: 1, // 默认9
          sizeType: ['original'],
          sourceType: ['camera'],
          success: function (res) {
            // 运行不报错，但手机调试时就是不出现相册
            console.log(res)
            this.localIds = res.localIds
          }
        })
      }
    }
  }
</script>
