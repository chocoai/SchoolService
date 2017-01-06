<template>
  <div class="LookField">
    <form>
      <mu-appbar title="请核实后输入以下信息">
        <mu-flat-button color="white" label="返回" slot="right" icon="reply" labelPosition="before" @click="reply"/>
      </mu-appbar>
      <mu-paper>
        <mu-text-field label="教师姓名" disabled underlineShow="false" v-model="name" fullWidth labelFloat icon="person"/><br/>
        <mu-text-field label="联系电话" disabled underlineShow="false" v-model="phone"  fullWidth labelFloat icon="phone" maxLength="11"/><br/>
        <mu-text-field label="微信号码" disabled underlineShow="false" v-model="weixin" fullWidth labelFloat icon="chat"/><br/>
        <mu-text-field label="电子邮箱" disabled underlineShow="false" v-model="email" fullWidth labelFloat icon="email"/><br/>
        <mu-text-field label="备注信息" disabled underlineShow="false" v-model="remark" fullWidth labelFloat icon="bookmark"/><br/>
      </mu-paper>
    </form>
    <mu-raised-button label="拨打电话" fullWidth @click="call" primary/>
    <mu-toast v-if="toast" :message="message"/>
  </div>
</template>

<script>
import * as AF from '../Util/AjaxFunction.js'
export default {
  name: 'LookField',
  data () {
    return {
      toast: false,
      teacher: '',
      name: '',
      phone: '',
      weixin: '',
      email: '',
      remark: ''
    }
  },
  created () {
    this.fetchData(this.$route.params.teacherId)
  },
  watch: {
    // 如果路由有变化，会再次执行该方法
    '$route': 'fetchData'
  },
  methods: {
    reply () {
      window.location.href = '#/'
    },
    call () {
      window.location.href = 'wtai://wp//mc;' + this.phone
    },
    fetchData (teacherId) {
      this.$http.get(
        AF.TeacherGet,
        { params:
        {
          id: teacherId
        }
        },
        {
          headers:
          {
            'X-Requested-With': 'XMLHttpRequest'
          },
          emulateJSON: true
        }
      ).then((response) => {
        this.teacher = response.body
        this.name = this.teacher.name
        this.phone = this.teacher.phone
        this.weixin = this.teacher.weixin
        this.email = this.teacher.email
        this.remark = this.teacher.remark

      }, (response) => {
      })
    }
  }
}
</script>
