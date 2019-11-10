/**
 * Created by Administrator on 2017/11/1.
 */
import Vue from 'vue'
import Route from '../../router'
import loading from '@/components/loading.vue'
import dialog from '@/components/dialog.vue'
import tooltip from '@/components/tooltip.vue'
const LoadingConstructor = Vue.extend(loading);
const dialogConstructor = Vue.extend(dialog);
const tooltipConstructor = Vue.extend(tooltip);
//加载动画
Vue.prototype.Loading = {
  show(url) {
    let node;
    url.indexOf('SaleVoyageWS/getQueryData.cms')>-1 ?  node = '.content-display' :  node = '#app';
    let mountDom = document.querySelector(node);
    let instance = new LoadingConstructor().$mount();
    if(node == '.content-display'){
      instance.$el.classList.add('lading-wrapper-absolute');
    }else {
      instance.$el.classList.add('lading-wrapper-fixed');
      bodyNoScrolling.open();
    }
    mountDom.appendChild(instance.$el);
  },
  hide(){
    let loading = document.querySelector('.loading-wrapper');
    let loadingType = document.querySelector('.lading-wrapper-fixed');
    loading && loading.parentNode.removeChild(loading);
    if(loadingType){
      bodyNoScrolling.close();
    }
  }
};
//提示弹窗
Vue.prototype.dialog = {
  showDialog(option,callback) {
    let oldVal,newVal;
    if( typeof option === 'string' ){
      if( this.option ) oldVal = this.option;
      newVal = option;
    }else {
      if( this.option ) oldVal = this.option.msg;
      newVal = option.msg;
    }
    if(oldVal === newVal) {
      let dialog = document.querySelector('.dialog-wrapper');
      dialog && dialog.parentNode.removeChild(dialog);
      bodyNoScrolling.close();
    }
    this.option = option;
    let node = '#app';
    let dom = document.querySelector(node);
    let dialog = new dialogConstructor({propsData: {option: option,callback:callback}}).$mount();
    dom.appendChild(dialog.$el);
    bodyNoScrolling.open();
  },
  hideDialog() {
    let dialog = document.querySelector('.dialog-wrapper');
    dialog && dialog.parentNode.removeChild(dialog);
    let msg = ( typeof this.option === 'string' ) ? this.option : this.option.msg;
    if(msg.indexOf('不存在有效登录信息,请重新登录！')>-1 || msg.indexOf('超过时限，请重新登录！')>-1 ){
      sessionStorage.clear();
      Route.push('/');
    }
    bodyNoScrolling.close();
  }
};
//气泡提示
Vue.prototype.tooltip = {
  show(msg,el) {
    this.hide();
    let node = 'body';
    let dom = document.querySelector(node);
    let targer = document.querySelector(el);
    let height = targer.offsetHeight;
    let rec = targer.getBoundingClientRect();
    let tooltip = new tooltipConstructor({propsData: {msg: msg}}).$mount();
    tooltip.$el.style.top = (rec.top - height - 5) + 'px';
    tooltip.$el.style.left = rec.left + 'px';
    dom.appendChild(tooltip.$el);
  },
  hide() {
    let tooltip = document.querySelector('.tooltip-wrap');
    tooltip && tooltip.parentNode.removeChild(tooltip);
  }
};

let bodyNoScrolling = (function(bodyCls) {
  let scrollTop;
  let scrollBarWidth = getScrollWidth();
  let isPaddingRight;
  return {
    open: function() {
      let paddingRight = document.body.style.paddingRight;
      isPaddingRight = paddingRight.split('px')[0] > 0 ? true : false;
      let scrollHeight = document.body.scrollHeight;
      let clientHeight = document.body.clientHeight;
      if(scrollHeight - clientHeight > 0){
        scrollTop = document.documentElement.scrollTop || window.pageYOffset || document.body.scrollTop;
        document.body.style.paddingRight = scrollBarWidth + 'px';
        document.body.classList.add(bodyCls);
      }
    },
    close: function() {
      if( scrollTop !== undefined && !isPaddingRight ){
        document.body.classList.remove(bodyCls);
        if(document.documentElement.scrollTop){
          document.documentElement.scrollTop = scrollTop;
        }else if(document.body.scrollTop){
          document.body.scrollTop = scrollTop;
        }
        document.body.style.paddingRight = '0px';
      }else {
        isPaddingRight = false;
      }
    }
  };
  function getScrollWidth() {
    let noScroll, scroll, oDiv = document.createElement("DIV");
    oDiv.style.cssText = "position:absolute; top:-1000px; width:100px; height:100px; overflow:hidden;";
    noScroll = document.body.appendChild(oDiv).clientWidth;
    oDiv.style.overflowY = "scroll";
    scroll = oDiv.clientWidth;
    document.body.removeChild(oDiv);
    return noScroll-scroll;
  }
})('modal-open');

Vue.prototype.bodyNoScrolling = bodyNoScrolling;

export default new Vue
