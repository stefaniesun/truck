<template>
  <div class="paymentPage">
    <div class="content">
      <div class="title">付款页面</div>
      <div class="contentDisplay">
        <div class="orderInfo">
          <p>订单信息</p>
          <div class="orderContent">
            <div class="cruiseImg">
              <img :src="contentValue.image">
            </div>
            <div class="cruiseInfo">
                <div>{{contentValue.title}}</div>
                <div>出发日期：{{contentValue.date}}</div>
                <div>联系人：{{contentValue.LinkMan}}<span>联系电话：{{contentValue.LinkPhone}}</span></div>
            </div>
            <div class="clearfloat"></div>
            <div class="cruiseOrder">
              <ul>
                <li>
                  <div v-for="num in title">{{num}}</div>
                </li>
                <li v-for="cabinItem in contentValue.orderList">
                  <div><img src="../assets/img/cabinImg.png"></div>
                  <div>{{cabinItem.cabinName}}</div>
                  <div>&yen{{cabinItem.price}}/人</div>
                  <div>{{cabinItem.count}}</div>
                  <div class="totalPrace">&yen{{cabinItem.amount}}</div>
                </li>
              </ul>
            </div>
          </div>
        </div>
        <div class="orderInfo">
          <p>下单金额</p>
          <div class="orderContent">
            <div class="accountBalance pull-left">
              <div class="accountSurplus">账户余额<span>&yen<span>{{currentAccount.sumPrice}}</span></span></div>
              <div class="isAllowablePayment" v-if="!isPayment">
                <svg class="icon iconFont pull-left" aria-hidden="true">
                  <use xlink:href="#icon-tishi"></use>
                </svg>
                <span>余额不足</span>
              </div>
              <div class="accountType">
                <div></div>
                基础账户<span>&yen{{currentAccount.normal}}</span>
              </div>
              <div class="accountType marginLeft">
                <div></div>
                授信账户<span>&yen{{currentAccount.credit}}</span>
              </div>
              <div class="accountType marginLeft">
                <div></div>
                返利账户<span>&yen{{currentAccount.rebate}}</span>
              </div>
            </div>
            <div class="paymentPrice pull-left">付款金额<span>&yen<span>{{contentValue.amount}}</span></span></div>
            <div class="clearfloat"></div>
          </div>
          <div class="buttonDisplay">
            <div class="button" @click="payment(false)">取消</div>
            <div class="button paymentButton" :class="isPayment?'':'noClick'" @click="payment(true)">立即付款</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
  let vm;
  export default {
    name : 'paymentPage',
    data(){
        return{
          title :['展示','舱型','单价','数量','金额'],
          isPayment:true,
          currentAccount:{},
        }
    },
    props : ['contentValue'],
    mounted(){
      vm = this;
      vm.axios.post(this.api + this.urlApi.getDistributorAccountListByNumberCode).then((data) => {
        if(data.status) {
          vm.currentAccount = data.content;
          vm.$nextTick(function(){
            if(vm.contentValue.amount > vm.currentAccount.sumPrice){
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
    background-color: rgba(0,0,0,.3);
    position: fixed;
    top:0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 10;
    .content{
      width: 1100px;
      background-color: #fff;
      border-radius: 8px;
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%,-50%);
      .title{
        height: 40px;
        border-radius: 8px 8px 0 0;
        background-color: #6aadff;
        color:#fff;
        line-height: 40px;
        font-size: 22px;
        text-align: center;
      }
      .contentDisplay{
        padding: 0 20px 40px 20px;
        .orderInfo{
          margin-top: 20px;
          p{
            font-size: 20px;
            color:#6aadff;
            line-height: 30px;
            border-bottom: 1px solid #6aadff;
          }
          .orderContent{
            padding: 22px 30px 0 30px;
            .cruiseImg{
              width: 178px;
              height: 124px;
              border:1px solid #ccc;
              border-radius: 8px;
              padding: 2px;
              float: left;
              img{
                width: 100%;
                height: 100%;
                border-radius: 8px;
              }
            }
            .cruiseInfo{
              width: 820px;
              float: left;
              padding-left: 30px;
              div:nth-of-type(1){
                font-size: 28px;
                color:#333333;
                line-height: 40px;
                margin-top: 10px;
              }
              div{
                font-size: 16px;
                color:#999;
                line-height: 27px;
                span{
                  margin-left: 20px;
                }
              }
            }
            .cruiseOrder{
              margin-top: 20px;
              border:1px solid #e6e6e6;
              ul{
                li{
                  font-size: 18px;
                  color:#999999;
                  line-height: 80px;
                  text-align: center;
                  border-top: 1px solid #f2f2f2;
                  padding:20px 20px 0 48px;
                  float: none;
                  height: 120px;
                  div{
                    float: left;
                  }
                  div:nth-of-type(1){
                    width: 120px;
                    img{
                      width: 120px;
                      height: 80px;
                      border:1px solid #ccc;
                      border-radius: 5px;
                    }
                  }
                  div:nth-of-type(2){
                    width: 230px;
                  }
                  div:nth-of-type(3){
                    width: 200px;
                  }
                  div:nth-of-type(4){
                    width: 180px;
                  }
                  div:nth-of-type(5){
                    width: 200px;
                  }
                  .totalPrace{
                    font-size: 25px;
                    color:#ff9b0b;
                  }
                }
                li:nth-of-type(1){
                  border-top: 0;
                  height: 40px;
                  line-height: 40px;
                  font-size: 20px;
                  color:#333333;
                  padding: 0 0 0 48px;
                }
              }
            }
            .accountBalance{
              width: 560px;
              height: 80px;
              background-color: #f4f8fd;
              border:1px solid #a8c5e6;
              padding-left: 68px;
              position: relative;
              .accountSurplus{
                font-size: 14px;
                color:#666666;
                margin-top: 10px;
                >span{
                  color:#6aadff;
                  margin-left: 12px;
                  span{
                    font-size: 24px;
                  }
                }
              }
            }
            .isAllowablePayment{
              position: absolute;
              top:15px;
              right:0;
              width: 160px;
              color:#ff0000;
              .iconFont{
                font-size: 20px;
                margin-top: 3px;
              }
              span{
                margin-left: 7px;
                font-size: 16px;
              }
            }
            .accountType{
              position: relative;
              float: left;
              font-size: 12px;
              color:#999999;
              margin-top: 5px;
              span{
                color:#333333;
                margin-left: 10px;
              }
              div{
                width: 4px;
                height: 4px;
                background-color: #6aadff;
                border-radius: 2px;
                position: absolute;
                top:8px;
                left:-10px;
              }
            }
            .marginLeft{
              margin-left: 35px;
            }
            .paymentPrice{
              width: 397px;
              height: 80px;
              background-color: #fefaf4;
              border:1px solid #ff9b0b;
              margin-left: 13px;
              text-align: center;
              font-size: 14px;
              color:#666666;
              padding-top: 5px;
              >span{
                font-size: 18px;
                color:#ff9b0b;
                margin-left: 12px;
                span{
                  font-size: 40px;
                  font-family: Impact;
                }
              }
            }
          }
          .buttonDisplay{
            margin-top: 40px;
            text-align: center;
            .button{
              width: 180px;
              height: 50px;
              line-height: 50px;
              border-radius: 25px;
              color:#fff;
              background-color: #6aadff;
              display: inline-block;
              cursor: pointer;
              margin: 0 50px;
              font-size: 20px;
            }
            .paymentButton{
              background-color: #ff9b0b;
            }
            .noClick{
              background-color: #cccccc;
              cursor: auto;
            }
          }
        }
      }
    }
  }
</style>
