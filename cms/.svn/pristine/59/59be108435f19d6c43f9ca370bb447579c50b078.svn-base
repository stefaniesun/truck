<template>
  <div class="dialog-wrapper mask-base">
    <div class="dialog">
      <div class="title">提示信息</div>
      <div class="container">
        <div class="icon-wrapper">
          <i class="warning-icon">
            <svg class="icon" aria-hidden="true">
              <use xlink:href="#icon-icon"></use>
            </svg>
          </i>
        </div>
        <div class="content">
          {{ isObj ? option.msg : option }}
        </div>
        <div class="btn-wrap">
          <button @click="closeDialog(true)" class="btn" type="button">{{ isObj ? option.btnText?option.btnText:'知道了' : '知道了'}}</button>
          <button @click="closeDialog(false)" v-if="option.close"  class="btn" type="button">取消</button>
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
      width: 400px;
      min-height: 240px;
      height: auto !important;
      height: 240px;
      top: 50%;
      left: 50%;
      -webkit-transform: translate(-50%,-50%);
      -moz-transform: translate(-50%,-50%);
      -ms-transform: translate(-50%,-50%);
      -o-transform: translate(-50%,-50%);
      transform: translate(-50%,-50%);
      background: @topBg;
      overflow: hidden;
      .borderRadius;
      .title{
        height: 40px;
        line-height: 40px;
        text-align: center;
        font-size: 22px;
        color: #fff;
        letter-spacing: 10px;
        background: @topBg ;
      }
      .container{
        background-color: #fff;
        min-height: 200px;
        height: auto !important;
        height: 200px;
        position: relative;
        padding-bottom: 20px;
        .icon-wrapper{
          padding-top: 30px;
          text-align: center;
          .warning-icon{
            width: 63px;
            height: 63px;
            margin: 0 auto;
            .borderRadius(50%);
            color: #b0d2fd;
            .icon{
              width: 63px;
              height: 63px;
            }
          }
        }
        .content{
          font-size: 16px;
          width: 90%;
          margin: 0 auto;
          color: #4c4c4c;
          word-wrap: break-word;
          text-align: center;
          line-height: 22px;
        }
        .btn-wrap{
          text-align: center;
          margin-top: 20px;
          .btn{
            width: 120px;
            height: 34px;
            margin: 0 10px;
            text-align: center;
            color: @topBg;
            .borderRadius(17px);
            background: #fff;
            outline: none;
            border: 1px solid @changeBtnBg;
            font-size: 18px;
            cursor: pointer;
            &:hover{
              background-color: @changeBtnBg;
              color: #fff;
            }
          }
        }
      }
    }
  }


</style>
