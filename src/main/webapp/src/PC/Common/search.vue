<template>
  <Row>
    <Col span="16">
      <Input type="text" v-model="keyword" placeholder="请输入关键词" style="width:320px;"><span slot="prepend">关键词</span></Input>
    </Col>
    <Col span="8"><Button type="primary" @click.native="goQuery">查找</Button></Col>
  </Row>
</template>
<script>
  export default{
    data(){
      return{
        keyword:''
      }
    },
    methods: {
      goQuery () {
        this.$emit(('goQuery',this.keyword)
      },
    }
  }
</script>
<style>

</style>
