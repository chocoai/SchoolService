import Vue from 'vue'
import VueRouter from 'vue-router'
import VueResource from 'vue-resource'
import Home from './Home.vue'

import 'material-design-icons/iconfont/material-icons.css'
import 'muse-components/styles/base.less' // 加载基础的样式
import 'muse-ui/dist/muse-ui.css'
import 'muse-ui/dist/theme-light.css' // 使用 light 主题
import appBar from 'muse-components/appBar'
import icon from 'muse-components/icon'
import iconButton from 'muse-components/iconButton'
import avatar from 'muse-components/avatar'
import drawer from 'muse-components/drawer'
import { listItem, list } from 'muse-components/list'
import { menuItem, menu } from 'muse-components/menu'
import { flexboxItem, flexbox } from 'muse-components/flexbox'

Vue.component(appBar.name, appBar)
Vue.component(icon.name, icon)
Vue.component(iconButton.name, iconButton)
Vue.component(avatar.name, avatar)
Vue.component(drawer.name, drawer)
Vue.component(listItem.name, listItem)
Vue.component(menuItem.name, menuItem)
Vue.component(flexboxItem.name, flexboxItem)
Vue.component(list.name, list)
Vue.component(flexbox.name, flexbox)
Vue.component(menu.name, menu)

Vue.use(VueRouter)
Vue.use(VueResource)

const routes = [
  { path: '/', component: Home }
]

const router = new VueRouter({
  routes // （缩写）相当于 routes: routes
})

/* eslint-disable no-unused-vars  */
const app = new Vue({
  router,
  template: '<router-view></router-view>'
}).$mount('#app')
