<template>
  <div class="orderSuccess">
    <!-- 头部 start -->
    <header>
      <h2>下单成功</h2>
      <i @click="goBack" class="iconfont back icon-fanhui"></i>
      <i v-if="isShowTips" @click="goUserCenter" class="iconfont user icon-gerenzhongxin"></i>
    </header>
    <!-- 内容 start -->
    <div class="main">
      <div class="order-struts">
        <div class="circular-box">
          <i class="iconfont icon-gou" ></i>
          <h6>成功</h6>
        </div>
        <p>恭喜您预定成功！</p>
      </div>
      <p v-if="isShowTips" class="tips">马上去 <span @click="goUserCenter">查看订单</span> 或 <span @click="goBack">返回首页</span> 再次购买</p>
      <!-- 下单成功 -->
      <!--<div class="success" v-if="orderStrtus == false">-->
        <!--<p>扫描填写出行人信息</p>-->
        <!--<img src="" alt="">-->
        <!--<div class="btn-box">-->
          <!--<button>分享二维码发送给微信好友</button>-->
        <!--</div>-->
      <!--</div>-->
      <!-- 底部蓝色背景条 -->
      <div class="bottom-color-bar"></div>
    </div>
  </div>
</template>

<script>
  let vm;
  export default{
    name: 'orderSuccess',
    data(){
      return {
        orderStrtus:true,
        isShowTips: sessionStorage.urlJson ? false : true
      }
    },
    mounted:function(){
      vm = this;
    },
    methods:{
      goBack:function () {
      	if(sessionStorage.urlJson){
          vm.$router.goBack();
        }else {
          vm.$router.replace('/productQuery');
        }
      },
      goUserCenter:function () {
        vm.$router.replace('/userCenter');
      }
    }
  }
</script>

<style lang="less" scoped>
  @import "../assets/css/base.less";

  @font-color:#8d95a3;
  .orderSuccess {
    width: 100%;
    height: 100%;
    background-color: #fff;
    .main{
      width: 100%;
      height: 100%;
      padding:88*@basePX 0 0 0;
      .order-struts{
        width: 100%;
        height: 410*@basePX;
        background-image: url("../assets/img/blueBg.png");
        background-size: 100% 100%;
        text-align: center;
        padding-top: 36*@basePX;
        .circular-box{
          width: 232*@basePX;
          height: 232*@basePX;
          background: url("../assets/img/circular.png");
          background-size: 100% 100%;
          margin: auto;
          position: relative;
            i{
              color: #fff;
              display: inline-block;
              text-shadow:0 0 10px rgba(7,58,119,0.55);
            }
          .icon-cha{
            font-size: 70*@basePX;
            margin-top: 53*@basePX;
          }
          .icon-gou{
            font-size:88*@basePX;
            margin-top:40*@basePX;
          }
          h6{
            color: #fff;
            font-weight: normal;
            font-size: 24*@basePX;
            position: absolute;
            left:90*@basePX;
            bottom: 45*@basePX;
          }
        }
        p{
          display:block;
          text-align: center;
          color: #fff;
          margin-top: 32*@basePX;
          font-size: 38*@basePX;
          text-indent: 1em;
        }
      }
      .tips{
        margin-top: 100*@basePX;
        font-size: 32*@basePX;
        text-align: center;
        span{
          color: @changeBtnBg;
          text-decoration: underline;
        }
      }
      /*.btn-box{
        width: 100%;
        text-align: center;
        margin-top: 39*@basePX;
        button{
          background-color: @clickBtnBg;
          color: #fff;
          cursor: pointer;
          border: 0;
          height: 78*@basePX;
          .borderRadius(@radius: 50*@basePX);
          outline:0;
          font-size: 34*@basePX;
        }
      }
      .success{
        text-align: center;
        p{
          font-size: 32*@basePX;
          color: @font-color;
          text-align: center;
          margin-top: 60*@basePX;
        }
        img{
          width: 340*@basePX;
          height: 340*@basePX;
          margin-top: 27*@basePX;
        }
        button{
          width: 500*@basePX;
        }
      }*/
      .bottom-color-bar{
        width:100% ;
        height: 24*@basePX;
        background-color: @topBg;
        position: absolute;
        bottom: 0;
      }
    }
  }
</style>
