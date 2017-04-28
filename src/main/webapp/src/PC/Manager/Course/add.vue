<template>
  <div class="layout">
    <Row>
      <Col><MenuList active="course" :name="name" three="新增"></MenuList></Col>
    </Row>
    <Row>
      <Col span="8"></Col>
      <Col span="8">
        <Form :label-width="100" :rules="course_validate" ref="addForm" :model="course">
          <Form-item label="课程名称" prop="name" required>
            <Input v-model="course.name" placeholder="请输入课程名称" style="width: 400px"></Input>
          </Form-item>
          <Form-item label="课程描述" prop="detail" required>
            <Input v-model="course.detail" type="textarea" :autosize="{minRows: 3,maxRows: 5}" placeholder="请输入对该课程的描述" style="width: 400px"></Input>
          </Form-item>
          <Form-item label="课程类型" required>
            <Radio-group v-model="course.type" type="button">
              <Radio label="1">必修课</Radio>
              <Radio label="2">选修课</Radio>
            </Radio-group>
          </Form-item>
          <Form-item label="课程状态" required>
            <Radio-group v-model="course.state" type="button">
              <Radio label="1">激活</Radio>
              <Radio label="0">注销</Radio>
            </Radio-group>
          </Form-item>
          <Form-item>
            <Button type="primary" @click="goSave">提交</Button>
            <Button type="ghost" style="margin-left: 8px" @click="goReset">重置</Button>
            <Button style="margin-left: 8px">返回</Button>
          </Form-item>
        </Form>
      </Col>
      <Col span="8"></Col>
    </Row>
    <Row><Col><Copy></Copy></Col></Row>
  </div>
</template>
<script>
  import Copy from '../../Common/copy.vue'
  import MenuList from '../Menu/menuList.vue'
  export default {
    name: 'add',
    components: { Copy, MenuList },
    data () {
      return {
        course_validate: {
          name: [
            { required: true, message: '课程名称不能为空', trigger: 'change' }
          ],
          detail: [
            { required: true, message: '课程详情不能为空', trigger: 'change' }
          ]
        },
        name: 'xxx',
        showLoad: true,
        course: {
          name: '',
          detail: '',
          type: '1',
          state: '1'
        }
      }
    },
    methods: {
      goReset () {
        this.$refs.addForm.resetFields()
        this.name = ''
        this.detail = ''
        this.type = '1'
        this.state = '1'
      },
      goSave () {
        this.$refs.addForm.validate((valid) => {
          if (valid) {
            this.$Message.success('提交成功!')
          } else {
            this.$Message.error('表单验证失败!')
          }
        })
      }
    }
  }
</script>
<style scoped>
  .layout{
    border: 1px solid #d7dde4;
    background: #f5f7f9;
  }
</style>
