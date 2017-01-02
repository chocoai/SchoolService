<template>
  <div class="AddField">
    <mt-header title="行政教师">
      <router-link to="/" slot="left">
        <mt-button icon="back">返回</mt-button>
      </router-link>
    </mt-header>
    <mt-field label="姓名" placeholder="请输入教师姓名"></mt-field>
    <mt-field label="手机号" placeholder="请输入手机号" type="tel"></mt-field>
    <mt-field label="微信号" placeholder="请输入微信号" type="password"></mt-field>
    <mt-field label="邮箱" placeholder="请输入邮箱" type="email"></mt-field>
    <mt-field label="备注" placeholder="请输入备注信息" type="textarea" rows="4"></mt-field>
    <mt-button type="primary" size="large" v-on:click.native="doTheThing">
      新增
    </mt-button>
  </div>
</template>

<script>
export default {
  name: 'AddField'
}
</script>
