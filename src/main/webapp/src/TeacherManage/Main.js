import Vue from 'vue'
import Main from './Main.vue'
import { Button, Search } from 'mint-ui'

Vue.component(Search.name, Search)
Vue.component(Button.name, Button)
/* eslint-disable no-new */
new Vue({
  el: '#app',
  template: '<Main/>',
  components: { Main }
})
