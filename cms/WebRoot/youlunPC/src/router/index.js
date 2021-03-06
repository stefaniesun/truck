/**
 * Created by Administrator on 2017/12/4.
 */
import Vue from 'vue'
import Router from 'vue-router'

Router.prototype.goBack = function () {  //route添加一个goBack方法，用于记录路由的前进后退状态,需要后退时调用：vm.$router.goBack();
  this.isBack = true;
  this.go(-1);
};

Vue.use(Router)

const router = new Router({
  routes: [
    {
      path: '/',
      name: 'login',
      component: resolve => { require(['@/components/login'], resolve); },
      meta: {
        keepAlive: false // 需要被缓存
      }
    },
    {
      path:'/productQuery',
      name:'productQuery',
      component:resolve => { require(['@/components/productQuery'],resolve);},
      meta: {
        keepAlive: false // 不需要被缓存
      }
    },
    {
      path: '/placeOrder',
      name: 'placeOrder',
      component: resolve => { require(['@/components/placeOrder'], resolve); },
      meta: {
        keepAlive: false // 不需要被缓存
      }
    },
    {
      path: '/productDetail',
      name: 'productDetail',
      component: resolve =>{ require(['@/components/ptDetail'], resolve); },
      meta: {
        keepAlive: false // 不需要被缓存
      }
    },
    {
      path: '/orderSuccess',
      name: 'orderSuccess',
      component: resolve =>{ require(['@/components/orderSuccess'], resolve); },
      meta: {
        keepAlive: false // 不需要被缓存
      }
    },
    {
      path: '/orderAll',
      name: 'orderAll',
      component: resolve =>{ require(['@/components/orderAll'], resolve); },
      meta: {
        keepAlive: false // 不需要被缓存
      }
    },
    {
      path: '/moneyAmount',
      name: 'moneyAmount',
      component: resolve =>{ require(['@/components/moneyAmount'], resolve); },
      meta: {
        keepAlive: false // 不需要被缓存
      }
    },
    {
      path: '/customerAmount',
      name: 'customerAmount',
      component: resolve =>{ require(['@/components/customerAmount'], resolve); },
      meta: {
        keepAlive: false // 不需要被缓存
      }
    },
    {
      path: '/distributorApply',
      name: 'distributorApply',
      component: resolve =>{ require(['@/components/distributorApply'], resolve); },
      meta: {
        keepAlive: true // 需要被缓存
      }
    },
    {
      path: '/resetPwd',
      name: 'resetPwd',
      component: resolve =>{ require(['@/components/resetPwd'], resolve); },
      meta: {
        keepAlive: false // 不需要被缓存
      }
    },
    {
      path: '/cruiseIntroduction',
      name: 'cruiseIntroduction',
      component: resolve =>{ require(['@/components/cruiseIntroduction'], resolve); },
      meta: {
        keepAlive: false // 需要被缓存
      }
    },
    {
      path: '/travelInstructions',
      name: 'travelInstructions',
      component: resolve =>{ require(['@/components/travelInstructions'], resolve); },
      meta: {
        keepAlive: true // 需要被缓存
      }
    },
    {
      path: "/bargainPrice",
      name: "bargainPrice",
      component: resolve => {
        require(['@/components/bargainPrice'], resolve);
      },
      meta: {
        keepAlive: false
      }
    },
    {
      path: "/cruiseDetails",
      name: "cruiseDetails",
      component: resolve => {
        require(['@/components/cruiseDetails'], resolve);
      },
      meta: {
        keepAlive: false
      }
    },
    {
      path: "/facilitiesDetails",
      name: "facilitiesDetails",
      component: resolve => {
        require(['@/components/facilitiesDetails'], resolve);
      },
      meta: {
        keepAlive: false
      }
    },
    {
      path: "/shareStatistics",
      name: "shareStatistics",
      component: resolve => {
        require(['@/components/shareStatistics'], resolve);
      },
      meta: {
        keepAlive: false
      }
    },
    {
      path: "/orderDetails",
      name: "orderDetails",
      component: resolve => {
        require(['@/components/orderDetails'], resolve);
      },
      meta: {
        keepAlive: false
      }
    },
    {
      path: "/paymentPage",
      name: "paymentPage",
      component: resolve => {
        require(['@/components/paymentPage'], resolve);
      },
      meta: {
        keepAlive: false
      }
    },
    {
      path: "/noPage",
      name: "noPage",
      component: resolve => {
        require(['@/components/noPage'], resolve);
      },
      meta: {
        keepAlive: false
      }
    },
    {
      path: "/chatPanel",
      name: "chatPanel",
      component: resolve => {
        require(['@/components/chatPanel'], resolve);
      },
      meta: {
        keepAlive: false
      }
    }
  ]
})

router.beforeEach((to, form, next) => {
  if( location.href.indexOf('cms.cloud') > -1 ){
    if(location.href.indexOf('index.html') > -1){
      next();
    }else {
      location.replace('http://cms.cloud.maytek.cn/redirectLogin.xyz');
    }
  }else if(location.port && location.href.indexOf('cms:') > -1){
    if(location.href.indexOf('index.html') > -1){
      next();
    }else {
      location.replace('http://cms:'+location.port+'/xt_cms/redirectLogin.xyz');
    }
  }else {
    next();
  }
});

export default router;
