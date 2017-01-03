import Vue from 'vue'
import Main from './Main.vue'
import Add from './AddField.vue'
import VueRouter from 'vue-router'
import MuseUI from 'muse-ui'
import VueResource from 'vue-resource'
import 'muse-ui/dist/muse-ui.css'

Vue.use(MuseUI)
Vue.use(VueRouter)
Vue.use(VueResource)

const routes = [
  { path: '/add', component: Add },
  { path: '/', component: Main }
]

const router = new VueRouter({
  routes // （缩写）相当于 routes: routes
})

/* eslint-disable no-unused-vars  */
const app = new Vue({
  router,
  template: '<router-view></router-view>'
}).$mount('#app')
