<template>
  <div id="app">
    <transition name="fade">
      <div class="app-wrap">
        <keep-alive>
          <router-view v-if="$route.meta.keepAlive" class="child-view" v-cloak></router-view>
        </keep-alive>
        <router-view v-if="!$route.meta.keepAlive" class="child-view" v-cloak></router-view>
      </div>
    </transition>
  </div>
</template>

<script>
export default {
  name: 'app',
  data(){
  	return{

    }
  },
  watch: {
    '$route': function (to, from) {
      if(!(to.name == 'productDetail' || to.name == 'placeOrder')){
        sessionStorage.removeItem("ptOrder");
      }
    }
  }
}
</script>

<style lang="less">
  @import "assets/css/common.less";

  #app {
    width: 100%;
    height: 100%;
    min-height: 100%;
    font-family: Microsoft YaHei;
    color: #2c3e50;
    height: 100%;
    background-color: #eff4f7;
    .app-wrap{
      width: 100%;
      height: 100%;
      background-color: #eff4f7;
    }
  }

  .fade-enter-active, .fade-leave-active {
    transition: opacity .3s cubic-bezier(.55, 0, .1, 1);
  }

  .fade-enter, .fade-leave-active {
    opacity: 0;
  }
</style>
