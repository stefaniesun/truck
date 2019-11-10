// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import axios from 'axios'
import VueAxios from 'vue-axios'
import "babel-polyfill";
import { DatePicker,LoadingBar,Carousel,Dropdown,Page,Icon } from 'iview';
import 'iview/dist/styles/iview.css';

Vue.use(VueAxios, axios)
const DropdownMenu = Dropdown.Menu;
const DropdownItem = Dropdown.Item;
const CarouselItem = Carousel.Item;
Vue.component('DatePicker', DatePicker);
Vue.component('LoadingBar', LoadingBar);
Vue.component('Carousel', Carousel);
Vue.component('CarouselItem', CarouselItem);
Vue.component('Dropdown', Dropdown);
Vue.component('DropdownMenu', DropdownMenu);
Vue.component('DropdownItem', DropdownItem);
Vue.component('Page', Page);
Vue.component('Icon', Icon);

router.beforeEach((to, from, next) => {
  LoadingBar.start();
  if(to.matched.length === 0) {
    next({
      path: '/',
    })
  }else{
    next();
  }
  LoadingBar.finish();
});

router.afterEach(() => {
  window.scrollTo(0, 0);
  LoadingBar.finish();
});

require('@/assets/utils/component');
require('@/assets/utils/axios');
require('@/assets/utils/urlApi');

Vue.config.productionTip = false

process.env.NODE_ENV == 'development'?Vue.prototype.api = 'api/':Vue.prototype.api = '';

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  template: '<App/>',
  components: { App }
})
