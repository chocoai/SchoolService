import Vue from 'vue'
import VueRouter from 'vue-router'
import VueResource from 'vue-resource'
import Vuex from 'vuex'

import StudentList from './StudentList.vue'
import StudentLook from './StudentLook.vue'

import 'material-design-icons/iconfont/material-icons.css'
import 'muse-components/styles/base.less' // 加载基础的样式
import appBar from 'muse-components/appBar'
import icon from 'muse-components/icon'
import flatButton from 'muse-components/flatButton'
import iconButton from 'muse-components/iconButton'
import floatButton from 'muse-components/floatButton'
import raisedButton from 'muse-components/raisedButton'
import textField from 'muse-components/textField'
import avatar from 'muse-components/avatar'
import iconMenu from 'muse-components/iconMenu'
import radio from 'muse-components/radio'
import dialog from 'muse-components/dialog'
import chip from 'muse-components/chip'
import popup from 'muse-components/popup'
import drawer from 'muse-components/drawer'
import circularProgress from 'muse-components/circularProgress'
import { listItem, list } from 'muse-components/list'
import { menuItem, menu } from 'muse-components/menu'
import { flexboxItem, flexbox } from 'muse-components/flexbox'

Vue.component(appBar.name, appBar)
Vue.component(icon.name, icon)
Vue.component(flatButton.name, flatButton)
Vue.component(floatButton.name, floatButton)
Vue.component(iconButton.name, iconButton)
Vue.component(raisedButton.name, raisedButton)
Vue.component(textField.name, textField)
Vue.component(avatar.name, avatar)
Vue.component(iconMenu.name, iconMenu)
Vue.component(dialog.name, dialog)
Vue.component(chip.name, chip)
Vue.component(popup.name, popup)
Vue.component(drawer.name, drawer)
Vue.component(radio.name, radio)
Vue.component(circularProgress.name, circularProgress)
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
  { path: '/studentList', component: StudentList },
  { path: '/studentLook/:studentId', component: StudentLook },
  { path: '/', redirect: '/studentList' }
]

const router = new VueRouter({
  routes // （缩写）相当于 routes: routes
})

const store = new Vuex.Store({
  state: {
    room_id: '',
    queryString: '',
    pageCurrent: '1'
  },
  mutations: {
    save (state, page) {
      state.queryString = page.queryString
      state.pageCurrent = page.pageCurrent
      state.room_id = page.room_id
    }
  }
})

/* eslint-disable no-unused-vars  */
const app = new Vue({
  router,
  store,
  template: '<router-view></router-view>'
}).$mount('#app')
