<template>
  <div class="TeacherAdd">
    <mu-appbar title="请核实后输入以下信息">
      <mu-icon-button icon='reply' slot="right" @click="reply"/>
    </mu-appbar>
    <mu-text-field label="标签名称" underlineShow="false" v-model="name" :errorColor="nameErrorColor" :errorText="nameErrorText" @input="checkName" fullWidth labelFloat icon="book"/><br/>
    <mu-select-field label="标签类型" v-model="type" icon="toc" :errorText="typeErrorText">
      <mu-menu-item value="1" title="教师"/>
      <mu-menu-item value="2" title="家长"/>
      <mu-menu-item value="3" title="学生"/>
    </mu-select-field>
    <mu-select-field label="应用类别" v-model="category" icon="toc" :errorText="categoryErrorText"　@change="changeCategory">
      <mu-menu-item value="1" title="企业号"/>
      <mu-menu-item value="2" title="订阅号"/>
      <mu-menu-item value="3" title="服务号"/>
    </mu-select-field>
    <mu-select-field label="应用名称" v-model="application" icon="toc" :errorText="applicationErrorText" :maxHeight="300">
      <mu-menu-item v-for="id, name in apps" :title="name" :value="id"/>
    </mu-select-field>
    <mu-flexbox>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="cached" @click="reset" backgroundColor="orange"/>
      </mu-flexbox-item>
      <mu-flexbox-item class="flex-demo">
        <mu-float-button icon="done" @click="save" backgroundColor="green"/>
      </mu-flexbox-item>
    </mu-flexbox>
    <mu-popup position="bottom" :overlay="false" popupClass="popup-bottom" :open="bottomPopup">
      <mu-icon :value="icon" :size="36" :color="color"/>{{ message }}
    </mu-popup>
  </div>
</template>

<script>
import * as API from './TagAPI.js'
export default {
  name: 'TeacherAdd',
  data () {
    return {
      bottomPopup: false,
      icon: '',
      color: '',
      message: '',
      name: '',
      type: '',
      category: '',
      application: '',
      apps: []
    }
  },
  computed: {
    typeErrorText () {
      return this.type ? '' : '这是必选项'
    },
    categoryErrorText () {
      return this.type ? '' : '这是必选项'
    },
    applicationErrorText () {
      return this.type ? '' : '这是必选项'
    }
  },
  created: function () {
    this.type = '1'
    this.category = '1'
    this.changeCategory(this.category)
  },
  methods: {
    changeCategory (value) {
      this.$http.get(
        API.GetApp,
        { params:
        {
          category: value
        }
        }).then((response) => {
          this.apps = response.body
        }, (response) => {
          this.openPopup('服务器内部错误！', 'report_problem', 'orange')
        })
    },
    reply () {
      this.$router.push({ path: '/tagList' })
    },
    openPopup (message, icon, color) {
      this.message = message
      this.icon = icon
      this.color = color
      this.bottomPopup = true
      setTimeout(() => { this.bottomPopup = false }, 1500)
    },
    reset () {
      this.name = ''
      this.type = '1'
      this.category = '1'
      this.changeCategory(this.category)
    },
    checkName (value) {
      this.$http.get(
        API.CheckNameForNew,
        { params: {
          name: value
        }
        }).then((response) => {
          switch (response.body) {
            case '0':
              this.nameErrorText = ''
              this.nameErrorColor = 'green'
              break
            case '1':
              this.nameErrorText = '请输入两个以上汉字！'
              this.nameErrorColor = 'red'
              break
            case '2':
              this.nameErrorText = '该姓名存在重名情况！'
              this.nameErrorColor = 'orange'
              break
            default:
              this.nameErrorText = ''
              this.nameErrorColor = 'blue'
          }
        }, (response) => {
          this.openPopup('服务器内部错误！', 'report_problem', 'orange')
        })
    },
    save () {
      this.$http.get(
        API.Save,
        { params: {
          name: this.name,
          phone: this.phone,
          weixin: this.weixin,
          email: this.email,
          remark: this.remark
        }
        }).then((response) => {
          switch (response.body) {
            case '0':
              this.openPopup('保存成功！', 'check_circle', 'green')
              setTimeout(() => { this.$router.push({ path: '/tagList' }) }, 1000)
              break
            case '1':
              this.openPopup('姓名为必填项！', 'report_problem', 'orange')
              break
            case '2':
              this.openPopup('输入的姓名应该为两个以上汉字！', 'report_problem', 'orange')
              break
            case '3':
              this.openPopup('手机号码与微信号必须填写其中一项！', 'report_problem', 'orange')
              break
            case '4':
              this.openPopup('输入的手机号码应为11位数字！', 'report_problem', 'orange')
              break
            case '5':
              this.openPopup('输入的手机号码已存在！', 'report_problem', 'orange')
              break
            case '6':
              this.openPopup('输入的微信号不应包含中文！', 'report_problem', 'orange')
              break
            case '7':
              this.openPopup('输入的微信号已存在！', 'report_problem', 'orange')
              break
            case '8':
              this.openPopup('输入的电子邮箱格式不正确！', 'report_problem', 'orange')
              break
            case '9':
              this.openPopup('输入的电子邮箱已存在！', 'report_problem', 'orange')
              break
            default:
          }
        }, (response) => {
          this.openPopup('服务器内部错误！', 'report_problem', 'orange')
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
