// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'

// import 'mint-ui/lib/style.css'
// import Mint from 'mint-ui/lib/index'
// Vue.use(Mint)
import { Button } from 'mint-ui'

Vue.component(Button.name, Button)
/* eslint-disable no-new */
new Vue({
  el: '#app',
  template: '<App/>',
  components: { App }
})
