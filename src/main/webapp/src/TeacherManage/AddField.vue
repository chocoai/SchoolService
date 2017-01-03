<template>
  <div class="AddField">
    <mu-appbar title="新增教师">
      <mu-flat-button color="white" label="保存" slot="right"/>
    </mu-appbar>
    <mu-text-field label="教师姓名" fullWidth labelFloat icon="person"/><br/>
    <mu-text-field label="联系电话" fullWidth labelFloat icon="phone" maxLength="11"/><br/>
    <mu-text-field label="微信号码" fullWidth labelFloat icon="chat"/><br/>
    <mu-text-field label="电子邮箱" fullWidth labelFloat icon="email"/><br/>
    <mu-text-field label="备注信息" fullWidth labelFloat icon="bookmark"/><br/>
    <back></back>
  </div>
</template>

<script>
import Back from './Back'
export default {
  name: 'AddField',
  components: {
    'back': Back
  }
}
</script>
