import Vue from 'vue'
import VueRouter from 'vue-router'
import VueResource from 'vue-resource'
import Vuex from 'vuex'

import List from './list.vue'
import Add from './add.vue'
import Edit from './edit.vue'
import * as API from './API.js'
import { getCookie } from '../../../cookieUtil.js'

import 'material-design-icons/iconfont/material-icons.css'
import 'muse-components/styles/base.less' // 加载基础的样式
import appBar from 'muse-components/appBar'
import icon from 'muse-components/icon'
import flatButton from 'muse-components/flatButton'
import iconButton from 'muse-components/iconButton'
import floatButton from 'muse-components/floatButton'
import raisedButton from 'muse-components/raisedButton'
import bottomSheet from 'muse-components/bottomSheet'
import textField from 'muse-components/textField'
import avatar from 'muse-components/avatar'
import iconMenu from 'muse-components/iconMenu'
import dialog from 'muse-components/dialog'
import drawer from 'muse-components/drawer'
import chip from 'muse-components/chip'
import popup from 'muse-components/popup'
import selectField from 'muse-components/selectField'
import datePicker from 'muse-components/datePicker'
import circularProgress from 'muse-components/circularProgress'
import subHeader from 'muse-components/subHeader'
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
Vue.component(bottomSheet.name, bottomSheet)
Vue.component(dialog.name, dialog)
Vue.component(drawer.name, drawer)
Vue.component(chip.name, chip)
Vue.component(popup.name, popup)
Vue.component(selectField.name, selectField)
Vue.component(datePicker.name, datePicker)
Vue.component(circularProgress.name, circularProgress)
Vue.component(subHeader.name, subHeader)
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
  { path: '/list',
    component: List,
    beforeEnter: (to, from, next) => {
      if (JSON.parse(JSON.parse(getCookie(API.base))).Page) {
        next()
      } else {
        next({ path: '/' })
      }
    }
  },
  { path: '/add',
    component: Add,
    beforeEnter: (to, from, next) => {
      if (JSON.parse(JSON.parse(getCookie(API.base))).Save) {
        next()
      } else {
        next({ path: '/' })
      }
    }
  },
  { path: '/edit/:id',
    component: Edit,
    beforeEnter: (to, from, next) => {
      if (JSON.parse(JSON.parse(getCookie(API.base))).Edit) {
        next()
      } else {
        next({ path: '/' })
      }
    }
  },
  { path: '/', redirect: '/list' }
]

const router = new VueRouter({
  routes // （缩写）相当于 routes: routes
})

const store = new Vuex.Store({
  state: {
    keyword: '',
    pageCurrent: '1',
    pageSize: '8'
  },
  mutations: {
    save (state, page) {
      state.keyword = page.keyword
      state.pageCurrent = page.pageCurrent
      state.pageSize = page.pageSize
    }
  }
})

/* eslint-disable no-unused-vars  */
const app = new Vue({
  router,
  store,
  template: '<router-view></router-view>'
}).$mount('#app')
