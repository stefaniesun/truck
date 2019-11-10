<template>
  <div class="cabin-wrapper">
    <div class="cabin-container">
      <div class="group-wrapper clearfloat">
        <div class="cabin-group" :key="index" v-for="(typeItem, index) in cabinList">
          <div class="title">
            <div class="titleContent">
              <div></div>
              <span>{{typeItem.cabinType}}</span>
              <div></div>
            </div>
          </div>
          <div class="cabin-list  animated" :class="transitionName"  v-for="cabin in typeItem.list">
            <div class="isStock" :class="cabin.stockType && cabin.holdCount ? 'virtualLibrary':''">
              <span v-if="!cabin.stockType && cabin.holdCount">实 库</span>
              <span v-if="cabin.stockType && cabin.holdCount">现 询</span>
            </div>
            <div class="img-wrapper">
              <img :src="cabin.cabinImg" alt="">
            </div>
            <div class="introduce">
              <div class="cabin-name">
                {{cabin.name}}
              </div>
              <div class="base-info clearfloat">
                <div class="item">
                  容量：{{cabin.volume}}人
                </div>
                <div v-if="!cabin.stockType && ((cabin.realCount < cabin.sufficientCount) || !cabin.sufficientCount) && cabin.holdCount" class="item">
                   仅剩<span>{{cabin.realCount}}</span>间
                </div>
                <div v-if="!cabin.stockType && (cabin.realCount >= cabin.sufficientCount) && cabin.sufficientCount && cabin.holdCount" class="item">
                  <span>库存充足</span>
                </div>
                <div v-if="cabin.stockType && cabin.holdCount" class="item">
                  <span>库存紧张</span>
                </div>
                <div v-if="!cabin.holdCount" class="item">
                  <span>已售罄</span>
                </div>
              </div>
            </div>
            <div class="counter-container">
              <div class="wrap">
                <div class="tips" v-if="cabin.maxCount && cabin.holdCount">
                  单次购买最多只能买{{cabin.maxCount}}间
                </div>
                <label v-if="cabin.holdCount">数量</label>
                <div v-if="cabin.holdCount" class="counter-wrapper">
                  <counter :product="cabin" :order="order" @confirm="confirm"></counter>
                </div>
                <div class="clearfloat"></div>

              </div>
            </div>
            <div class="cabin-price">
              <span class="salePrice">{{'&yen;' + cabin.price + '/人'}}</span>
              <!--<span v-if="cabin.rebate" class="rebatePrice">{{cabin.rebate ? '后返' + cabin.rebate + '/人' : ''}}</span>-->
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import Counter from '@/components/counter'
  let vm;
  export default {
    name: 'cabinType',
    props: ['cabinList','height','order'],
    components: {
      Counter
    },
    data() {
      return {
        transitionName: 'fadeInRight',
      }
    },
    mounted() {
      vm = this;
    },
    methods: {
      goPtOrder() {
        vm.$router.push('placeOrder')
      },
      confirm(flag){
        this.$emit('confirm-all',flag);
      }
    }
  }
</script>

<style lang="less" scoped>
  @import "../assets/css/common.less";
  .animated {
    animation-duration: .3s;
    animation-fill-mode: both;
  }

  @keyframes fadeInRight {
    from {
      opacity: 0;
      transform: translate3d(100%, 0, 0);
    }

    to {
      opacity: 1;
      transform: none;
    }
  }

  .fadeInRight {
    animation-name: fadeInRight;
  }
  @keyframes fadeInLeft {
    from {
      opacity: 0;
      transform: translate3d(-100%, 0, 0);
    }

    to {
      opacity: 1;
      transform: none;
    }
  }
  .fadeInLeft {
    animation-name: fadeInLeft;
  }

  .cabin-wrapper{
    width: 100%;
    .btn-order{
      float: left;
      background-color: @clickBtnBg;
      color: #fff;
      width: 225px;
      height: 57px;
      .borderRadius(28.5px);
      border: 0;
      outline: none;
      font-size: 26px;
      letter-spacing: 5px;
      cursor: pointer;
      line-height: 57px;
      &:hover{
        background-color: darken(@clickBtnBg, 5%);
      }

    }
    .cabin-container{
      background: @appBg;
      padding: 0 0 10px 10px;
      margin-top: 14px;
      width: 1120px;
      .group-wrapper {
        overflow: hidden;
      }

      .cabin-group{
        .title{
          text-align: center;
          border-bottom: 0;
          .titleContent{
            display: inline-block;
            height: 60px;
            line-height: 60px;
            font-size: 20px;
            color:#ff9b0b;
            div{
              margin-top: 31px;
              width: 320px;
              height: 2px;
              background-color: #ff9b0b;
              float: left;
            }
            span{
              padding: 0 35px;
              float: left;
            }
          }
        }
      }

      .cabin-list{
        background-color: #fff;
        width: 1100px;
        height: 100px;
        padding: 10px 11px;
        border: 1px solid @appBg;
        position: relative;
        .isStock{
          font-size: 16px;
          color:#fff;
          width: 36px;
          height: 58px;
          position: absolute;
          top:-6px;
          right:30px;
          background: url("../assets/img/realLibrary.png");
          background-size: 100% 100%;
          padding-top: 8px;
          text-align: center;
          line-height: 20px;
        }
        .virtualLibrary{
          background: url("../assets/img/virtualLibrary.png");
        }
        .img-wrapper{
          float: left;
          width: 120px;
          height: 80px;
          border: 1px solid #cccccc;
          .borderRadius(2px);
          overflow: hidden;
          text-align: center;
          img{
            height: 100%;
            width: 100%;
            max-height: 100%;
            max-width: 100%;
            .borderRadius(2px);
          }
        }
        .introduce{
          float: left;
          margin-left: 20px;
          padding-top: 12px;
          min-width: 340px;
          .cabin-name{
            color: #333333;
            font-size: 20px;
          }
          .base-info{
            font-size: 18px;
            color: #999;
            .item{
              float: left;
              span{
                color: @tipsColor;
                min-width: 20px;
                display: inline-block;
                text-align: center;
              }
              &:not(:first-child){
                margin-left: 25px;
              }
            }
          }
        }
        .counter-container{
          float: left;
          width: 250px;
          height: 100%;
          line-height: 32px;
          margin-left: 116px;
          transform: translateY(13px);
          position: relative;
          .wrap{
            width: 100%;
            position: absolute;
            left: 0;
            top: 38%;
            transform: translateY(-50%);
            line-height: 32px;
            .tips{
              color: #999;
              font-size: 14px;
              margin-bottom: -10px;
            }
          }
          label{
            font-size: 20px;
            color: #333;
            float: left;
          }
          .counter-wrapper{
            height: 32px;
            float: left;
            margin-left: 10px;
            width: 85px;
          }
        }
        .cabin-price{
          float: left;
          text-align: center;
          position: absolute;
          right: 110px;
          top: 50%;
          transform: translateY(-50%);
          span{
            display: block;
          }
          .salePrice{
            color: @tipsColor;
            font-size: 25px;
          }
          /*.rebatePrice{
            width: 92px;
            height: 18px;
            line-height: 18px;
            margin: 0 auto;
            color: #fff;
            font-size: 14px;
            background-color: @tipsColor;
          }*/
        }
      }

    }
  }

</style>
