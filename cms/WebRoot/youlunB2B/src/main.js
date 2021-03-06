// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import axios from 'axios'
import VueAxios from 'vue-axios'
import { Loadmore, Swipe, SwipeItem ,Lazyload } from 'mint-ui';

Vue.component(Swipe.name, Swipe);
Vue.component(SwipeItem.name, SwipeItem);
Vue.component(Loadmore.name, Loadmore);
Vue.use(Lazyload);
import 'mint-ui/lib/style.css'

require('@/assets/utils/bodyNoscrolling');
require('@/assets/utils/component');
require('@/assets/utils/axios');
require('@/assets/utils/urlApi');

Vue.use(VueAxios, axios)

process.env.NODE_ENV == 'development'?Vue.prototype.api = 'api/':Vue.prototype.api = '';

Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  template: '<App/>',
  components: { App }
})
