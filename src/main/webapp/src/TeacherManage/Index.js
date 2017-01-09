import Vue from 'vue'
import Main from './Main.vue'
import Add from './AddField.vue'
import Look from './LookField.vue'
import VueRouter from 'vue-router'
import VueResource from 'vue-resource'
import Vuex from 'vuex'
// import 'muse-ui/dist/muse-ui.css'
// import MuseUI from 'muse-ui'
//
// Vue.use(MuseUI)

import 'muse-components/styles/base.less' // 加载基础的样式
import appBar from 'muse-components/appBar'
import flatButton from 'muse-components/flatButton'
import floatButton from 'muse-components/floatButton'
import raisedButton from 'muse-components/raisedButton'
import textField from 'muse-components/textField'
import divider from 'muse-components/divider'
import paper from 'muse-components/paper'
import toast from 'muse-components/toast'
import avatar from 'muse-components/avatar'
import iconMenu from 'muse-components/iconMenu'
import dialog from 'muse-components/dialog'
import pagination from 'muse-components/pagination'
import { listItem, list } from 'muse-components/list'
import { menuItem, menu } from 'muse-components/menu'
import { flexboxItem, flexbox } from 'muse-components/flexbox'

Vue.component(appBar.name, appBar)
Vue.component(flatButton.name, flatButton)
Vue.component(floatButton.name, floatButton)
Vue.component(raisedButton.name, raisedButton)
Vue.component(textField.name, textField)
Vue.component(divider.name, divider)
Vue.component(paper.name, paper)
Vue.component(toast.name, toast)
Vue.component(avatar.name, avatar)
Vue.component(iconMenu.name, iconMenu)
Vue.component(dialog.name, dialog)
Vue.component(pagination.name, pagination)
Vue.component(listItem.name, listItem)
Vue.component(menuItem.name, menuItem)
Vue.component(flexboxItem.name, flexboxItem)
Vue.component(list.name, list)
Vue.component(menu.name, menu)
Vue.component(flexbox.name, flexbox)

Vue.use(VueRouter)
Vue.use(VueResource)
Vue.use(Vuex)

const routes = [
  { path: '/add', component: Add },
  { path: '/look/:teacherId', component: Look },
  { path: '/', component: Main }
]

const router = new VueRouter({
  routes // （缩写）相当于 routes: routes
})

const store = new Vuex.Store({
  state: {
    count: 0
  },
  mutations: {
    increment (state) {
      state.count++
    }
  }
})

/* eslint-disable no-unused-vars  */
const app = new Vue({
  router,
  store,
  template: '<router-view></router-view>'
}).$mount('#app')
