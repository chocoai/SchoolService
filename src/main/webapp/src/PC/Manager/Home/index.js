import Vue from 'vue'
import iView from 'iview'
import VueResource from 'vue-resource'
import Home from './home.vue'
import 'iview/dist/styles/iview.css'   // 使用 CSS

Vue.use(VueResource)
Vue.use(iView)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  template: '<Home/>',
  components: { Home }
})
