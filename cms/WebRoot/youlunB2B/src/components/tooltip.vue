<template>
  <div class="tooltip-wrap">
    <div class="arrow"></div>
    <div class="inner">
        {{msg}}
    </div>
  </div>
</template>

<script>
  let vm;
  export default{
  	name: 'tooltip',
    props: ['msg'],
    mounted(){
  		vm = this;
      setTimeout(function () {
        vm.tooltip.hide();
      },1000)
    }
  }
</script>

<style lang="less" scoped>
  @import "../assets/css/base.less";
  .tooltip-wrap{
    height: 34px;
    line-height: 34px;
    text-align: center;
    position: absolute;
    display: block;
    visibility: visible;
    font-size: 14px;
    z-index: 9;
    .borderRadius;
    .arrow{
      width: 0;
      height: 0;
      border-left: 5px solid transparent;
      border-top: 5px solid rgba(70,76,91,.9);
      border-right: 5px solid transparent;
      border-bottom: none;
      position: absolute;
      left: 50%;
      bottom: -5px;
      transform: translateX(-50%);
    }
    .inner{
      padding: 0 10px;
      border-radius: 4px;
      background-color: rgba(70,76,91,.9);
      color: #fff;
    }
  }
</style>
