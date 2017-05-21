import Vue from 'vue'
import iView from 'iview'
import VueRouter from 'vue-router'
import VueResource from 'vue-resource'
import Vuex from 'vuex'
import List from './list.vue'
import Edit from './edit.vue'
import * as API from './API.js'
import { getCookie } from '../../../cookieUtil.js'
import 'iview/dist/styles/iview.css'   // 使用 CSS
Vue.use(VueRouter)
Vue.use(VueResource)
Vue.use(Vuex)
Vue.use(iView)

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

router.beforeEach((to, from, next) => {
  iView.LoadingBar.start()
  next()
})

router.afterEach((to, from, next) => {
  iView.LoadingBar.finish()
})

const store = new Vuex.Store({
  state: {
    keyword: '',
    pageCurrent: '1',
    pageSize: '10'
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
