<template>
  <div class="paymentPage">
    <div class="paymentContent">
      <img src="../assets/img/dialog_head.png" class="dialog">
      <div class="orderInfo">
        <div class="title">
          <img src="../assets/img/product.jpg">
          <div :class="content.image">
            <p>{{content.title}}</p>
            <div>出发日期：{{content.date.split(' ')[0]}}</div>
          </div>
          <div class="clearfloat"></div>
        </div>
        <ul class="orderDetails clearfloat" v-for="cabinItem in content.orderList">
          <li>
            <p>舱型</p>
            <div>{{cabinItem.cabinName}}</div>
          </li>
          <li>
            <p>单价</p>
            <div>&yen{{cabinItem.price}}/人</div>
          </li>
          <li>
            <p>数量</p>
            <div>{{cabinItem.count}}</div>
          </li>
          <li>
            <p>金额</p>
            <div>&yen{{cabinItem.amount}}</div>
          </li>
        </ul>
        <div class="clearfloat"></div>
      </div>
      <div class="accountInfo">
        <div class="accountBalance">账户余额<span class="money"> &yen <span>{{currentAccount.sumPrice}}</span></span></div>
        <div class="noMoney" v-if="!isPayment">余额不足！</div>
        <div class="accountType">
          <p>基础账户</p>
          <div>&yen  <span>{{currentAccount.normal}}</span></div>
        </div>
        <div class="segmentingLine"></div>
        <div class="accountType">
          <p>授信账户</p>
          <div>&yen  <span>{{currentAccount.credit}}</span></div>
        </div>
        <div class="segmentingLine"></div>
        <div class="accountType">
          <p>返利账户</p>
          <div>&yen  <span>{{currentAccount.rebate}}</span></div>
        </div>
        <div class="clearfloat"></div>
      </div>
      <div class="button">
          <div @click="payment(false)">取消</div>
          <div class="paymentButton" :class="isPayment ? '':'noClick'" @click="payment(true)">立即付款&yen{{content.amount}}</div>
      </div>
    </div>
  </div>
</template>
<script>
  let vm;
  export default{
      name :'paymentPage',
      data(){
          return{
            isPayment : true,
            currentAccount : {},
          }
      },
    props : ['content'],
    mounted(){
      vm = this;
      vm.axios.post(this.api + this.urlApi.getDistributorAccountListByNumberCode).then((data) => {
        if(data.status) {
          vm.currentAccount = data.content;
          vm.$nextTick(function(){
            if(vm.content.amount > vm.currentAccount.sumPrice){
              vm.isPayment = false;
            }
          })
        }
      });
    },
    methods:{
      payment(flag){
        if(flag){
          if(!vm.isPayment){
            return;
          }
        }
        vm.$emit('confirm',flag);
      },
    }
  }
</script>
<style lang="less">
  @import "../assets/css/common.less";
  .paymentPage{
    position: fixed;
    top:0;
    left:0;
    width: 100%;
    height: 100%;
    z-index: 10;
    background-color: rgba(0,0,0,.35);
    .paymentContent{
      width: 600*@basePX;
      position: absolute;
      top:50%;
      left:50%;
      transform: translate(-50%,-50%);
      background-color: #fff;
      border-radius: 8*@basePX;
      border-bottom: 10*@basePX solid @topBg;
      .dialog{
        width: 100%;
        height: 140*@basePX;
        border-radius: 8*@basePX 8*@basePX 0 0 ;
      }
      .orderInfo{
        padding: 0 24*@basePX 40*@basePX 24*@basePX;
        .title{
          img{
            float: left;
            width: 180*@basePX;
            height: 108*@basePX;
            border-radius: 8*@basePX;
            margin-right: 15*@basePX;
          }
          .cruiseInfo{
            float: left;
            width: 350*@basePX;
            font-size: 24*@basePX;
            color:@subTextColor;
            p{
              font-size: 28*@basePX;
              color:#4c4c4c;
            }
          }
        }
        .orderDetails{
          margin-top: 24*@basePX;
          li{
            float: left;
            width: 50%;
            text-align: center;
            p{
              background-color: #93c0f8;
              height: 40*@basePX;
              font-size: 26*@basePX;
              line-height: 40*@basePX;
              color:#fff;
            }
            div{
              font-size: 28*@basePX;
              color:#4c4c4c;
              height: 56*@basePX;
              line-height: 56*@basePX;
              background-color: #f3f8fe;
            }
          }
        }
      }
      .accountInfo{
        border-top: 8*@basePX solid #e7eaf0;
        padding: 0 24*@basePX;
        position: relative;
        .accountBalance{
          font-size: 24*@basePX;
          color:#8d95a3;
          padding: 30*@basePX 0 20*@basePX 0 ;
          border-bottom: 1px solid #e7eaf0;
          .money{
            color:@topBg;
            span{
              font-size: 48*@basePX;
              font-family: Impact;
            }
          }
        }
        .noMoney{
          font-size: 24*@basePX;
          color:red;
          position: absolute;
          top:50*@basePX;
          right:20*@basePX;
        }
        .accountType{
          padding: 20*@basePX 0 40*@basePX 0;
          float: left;
          font-size: 24*@basePX;
          width: 182*@basePX;
          text-align: center;
          p{
            color:#333333;
          }
          div{
            color:@subTextColor;
            span{
              font-size: 32*@basePX;
              font-family: Impact;
            }
          }
        }
        .segmentingLine{
          width: 1px;
          height: 40*@basePX;
          background-color: #e7eaf0;
          float: left;
          margin-top: 36*@basePX;
        }
      }
      .button{
        border-top: 8*@basePX solid #e7eaf0;
        padding: 40*@basePX 0 20*@basePX 0;
        text-align: center;
        div{
          display: inline-block;
          height: 78*@basePX;
          line-height: 78*@basePX;
          border-radius: 39*@basePX;
          padding: 0 38*@basePX;
          min-width: 216*@basePX;
          font-size: 32*@basePX;
          color:#fff;
          margin: 0 12*@basePX;
          background-color: @topBg;
        }
        .paymentButton{
          background-color: @clickBtnBg;
        }
        .noClick{
          background-color: #cbcbcb;
        }
      }
    }
  }
</style>
