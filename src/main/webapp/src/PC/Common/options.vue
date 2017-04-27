<template>
  <div>
    显示边框 <i-switch v-model="showBorder" style="margin-right: 5px"></i-switch>
    显示斑马纹 <i-switch v-model="showStripe" style="margin-right: 5px"></i-switch>
    表格尺寸
    <Radio-group v-model="tableSize" type="button">
      <Radio label="large">大</Radio>
      <Radio label="default">中</Radio>
      <Radio label="small">小</Radio>
    </Radio-group>
  </div>
</template>
<script>
  export default {
    name: 'options',
    data () {
      return {
        showBorder: false,
        showStripe: false,
        tableSize: 'default'
      }
    },
    watch: {
      showBorder: function (val) {
        this.$emit('showBorder', val)
      },
      showStripe: function (val) {
        this.$emit('showStripe', val)
      },
      tableSize: function (val) {
        this.$emit('tableSize', val)
      }
    }
  }
</script>
