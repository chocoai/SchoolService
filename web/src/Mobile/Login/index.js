import Vue from 'vue'
import Login from './login.vue'
import VueResource from 'vue-resource'

import 'material-design-icons/iconfont/material-icons.css'
import 'muse-components/styles/base.less' // 加载基础的样式
import appBar from 'muse-components/appBar'
import icon from 'muse-components/icon'
import textField from 'muse-components/textField'
import raisedButton from 'muse-components/raisedButton'
import selectField from 'muse-components/selectField'
import popup from 'muse-components/popup'
import { flexboxItem, flexbox } from 'muse-components/flexbox'
import { menuItem, menu } from 'muse-components/menu'

Vue.component(appBar.name, appBar)
Vue.component(icon.name, icon)
Vue.component(textField.name, textField)
Vue.component(selectField.name, selectField)
Vue.component(raisedButton.name, raisedButton)
Vue.component(popup.name, popup)
Vue.component(flexbox.name, flexbox)
Vue.component(flexboxItem.name, flexboxItem)
Vue.component(menu.name, menu)
Vue.component(menuItem.name, menuItem)
Vue.use(VueResource)
/* eslint-disable no-new */
new Vue({
  el: '#app',
  template: '<Login/>',
  components: { Login }
})
