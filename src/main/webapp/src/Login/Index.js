import Vue from 'vue'
import Login from './Login.vue'

import 'assets/icon.css'
import 'muse-components/styles/base.less' // 加载基础的样式
import appBar from 'muse-components/appBar'
import textField from 'muse-components/textField'
import raisedButton from 'muse-components/raisedButton'
import { flexboxItem, flexbox } from 'muse-components/flexbox'

Vue.component(appBar.name, appBar)
Vue.component(textField.name, textField)
Vue.component(raisedButton.name, raisedButton)
Vue.component(flexbox.name, flexbox)
Vue.component(flexboxItem.name, flexboxItem)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  template: '<Login/>',
  components: { Login }
})
