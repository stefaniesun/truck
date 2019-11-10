<template>
  <div @touchmove.prevent  class="dialog-wrapper">
    <div class="dialog">
      <div class="title">提示信息</div>
      <div class="container">
        <div class="content">
          {{ isObj ? option.msg : option }}
        </div>
        <div class="btn-wrap">
          <button @click="closeDialog(true)" :class="option.close?'small':''" class="btn" type="button">{{ isObj ? option.btnText?option.btnText:'知道了' : '知道了'}}</button>
          <button @click="closeDialog(false)" :class="option.close?'small':''" v-if="option.close"  class="btn" type="button">取消</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  let vm;
  export default {
    name: 'dialog',
    props: ['option','callback'],
    data(){
      return{
        isObj: false
      }
    },
    mounted(){
      vm = this;
      if( typeof vm.option === 'object') vm.isObj = true;
    },
    methods: {
      closeDialog(flag){
        if(typeof vm.callback === 'function') vm.callback(flag);
        let key,value;
        if( vm.isObj ) {
          key = 'option.msg';
          value = vm.option.msg;
        }else {
          key = 'option';
          value = vm.option;
        }
        vm.dialog.hideDialog[key] = value;
        vm.dialog.hideDialog();
      }
    }
  }
</script>

<style scoped lang="less">
  @import "../assets/css/base.less";

  .dialog-wrapper{
    width: 100%;
    height: 100%;
    position: fixed;
    top: 0;
    left: 0;
    background: rgba(0,0,0,.3);
    z-index: 22;
    > .dialog{
      position: fixed;
      width: 80%;
      min-height: 380*@basePX;
      top: 50%;
      left: 50%;
      -webkit-transform: translate(-50%,-50%);
      -moz-transform: translate(-50%,-50%);
      -ms-transform: translate(-50%,-50%);
      -o-transform: translate(-50%,-50%);
      transform: translate(-50%,-50%);
      background: @topBg;
      padding-bottom: 10*@basePX;
      overflow: hidden;
      .borderRadius;
      .title{
        height: 140*@basePX;
        padding-top: 30*@basePX;
        text-align: center;
        font-size: 36*@basePX;
        color: #fff;
        background: @topBg url(../assets/img/dialog_head.png) 0 0 no-repeat;
        background-size: 100% 100%;
      }
      .container{
        background-color: #fff;
        font-size: 32*@basePX;
        min-height: 230*@basePX;
        margin-top: -2px;
        .content{
          width: 80%;
          margin: 0 auto;
          padding-top: 30*@basePX;
          color: #4c4c4c;
          min-height: 120*@basePX;
          word-wrap: break-word;
          text-align: center;
          line-height: 40*@basePX;
        }
        .btn-wrap{
          text-align: center;
          padding: 30*@basePX 0;
          .btn{
            width: 348*@basePX;
            height: 78*@basePX;
            text-align: center;
            color: #fff;
            .borderRadius(39*@basePX);
            background: @topBg;
            outline: none;
            border: 0;
            font-size: 30*@basePX;
            margin: 0 10*@basePX;
          }
          .small{
            width: 200*@basePX;
          }
        }
      }
    }
  }


</style>
