/**
 * Created by Administrator on 2017/11/29.
 */
//调用 open方法 禁止body滚动,调用 close方法 恢复body滚动
import Vue from 'vue'

let bodyNoScrolling = (function(bodyCls) {
  let scrollTop;
  return {
    open: function() {
      scrollTop = document.documentElement.scrollTop || window.pageYOffset || document.body.scrollTop;
      document.body.style.top = '-' + scrollTop + 'px';
      document.body.classList.add(bodyCls);
    },
    close: function() {
      document.body.classList.remove(bodyCls);
      if(document.documentElement){
        document.documentElement.scrollTop = scrollTop;
      }
      if(document.body){
        document.body.scrollTop = scrollTop;
      }
      // if(window.pageYOffset){
      //   window.pageYOffset = scrollTop;
      // }
      document.body.style.top = '0px';
    }
  };
})('modal-open');

Vue.prototype.bodyNoScrolling = bodyNoScrolling;
