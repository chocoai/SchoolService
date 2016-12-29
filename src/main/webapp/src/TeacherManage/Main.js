import Vue from 'vue'
import Main from './Main.vue'
import { Button } from 'mint-ui'

Vue.component(Button.name, Button)
/* eslint-disable no-new */
new Vue({
  el: '#app',
  template: '<Main/>',
  components: { Main }
})
