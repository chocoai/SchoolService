<template>
  <div class="AddField">
    <mu-appbar title="新增教师">
      <mu-flat-button color="white" label="保存" slot="right"/>
    </mu-appbar>
    <mu-text-field label="教师姓名" :errorText="nameErrorText" @input="checkName" fullWidth labelFloat icon="person"/><br/>
    <mu-text-field label="联系电话" :errorText="phoneErrorText" @input="checkPhone" fullWidth labelFloat icon="phone" maxLength="11"/><br/>
    <mu-text-field label="微信号码" :errorText="weixinErrorText" @input="checkWeixin" fullWidth labelFloat icon="chat"/><br/>
    <mu-text-field label="电子邮箱" :errorText="emailErrorText" @input="checkEmail" fullWidth labelFloat icon="email"/><br/>
    <mu-text-field label="备注信息" fullWidth labelFloat icon="bookmark"/><br/>
    <back></back>
  </div>
</template>

<script>
import * as AF from '../Util/AjaxFunction.js'
import Back from './Back'
export default {
  name: 'AddField',
  components: {
    'back': Back
  },
  data () {
    return {
      input: '',
      nameErrorText: '',
      phoneErrorText: '',
      weixinErrorText: '',
      emailErrorText: ''
    }
  },
  methods: {
    checkName (value) {
      this.$http.get(
        AF.TeacherCheckName,
        { params:
        {
          name: value
        }
        },
        { 'headers':
        {
          'X-Requested-With': 'XMLHttpRequest'
        },
          emulateJSON: true,
          emulateHTTP: true
        }).then((response) => {
          if (response.body === 'OK') {
            this.nameErrorText = ''
          } else {
            this.nameErrorText = response.body
          }
        }, (response) => {
        })
    },
    checkPhone (value) {
      this.phoneErrorText = '超过啦！！！！'
    },
    checkWeixin (value) {
      this.phoneErrorText = '超过啦！！！！'
    },
    checkEmail (value) {
      this.phoneErrorText = '超过啦！！！！'
    }
  }
}
</script>
