import Vue from 'vue'
import VueRouter from 'vue-router'
import VueResource from 'vue-resource'
import Vuex from 'vuex'

import TeacherList from './TeacherList.vue'
import TeacherAdd from './TeacherAdd.vue'
import TeacherEdit from './TeacherEdit.vue'

// import 'muse-ui/dist/muse-ui.css'
// import MuseUI from 'muse-ui'
//
// Vue.use(MuseUI)
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
import dialog from 'muse-components/dialog'
import chip from 'muse-components/chip'
import popup from 'muse-components/popup'
import drawer from 'muse-components/drawer'
import circularProgress from 'muse-components/circularProgress'
import selectField from 'muse-components/selectField'
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
Vue.component(circularProgress.name, circularProgress)
Vue.component(selectField.name, selectField)
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
  { path: '/teacherList', component: TeacherList },
  { path: '/teacherAdd', component: TeacherAdd },
  { path: '/teacherEdit/:teacherId', component: TeacherEdit },
  { path: '/', redirect: '/teacherList' }
]

const router = new VueRouter({
  routes // （缩写）相当于 routes: routes
})

const store = new Vuex.Store({
  state: {
    queryString: '',
    pageCurrent: '1'
  },
  mutations: {
    save (state, page) {
      state.queryString = page.queryString
      state.pageCurrent = page.pageCurrent
    }
  }
})

/* eslint-disable no-unused-vars  */
const app = new Vue({
  router,
  store,
  template: '<router-view></router-view>'
}).$mount('#app')
