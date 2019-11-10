<template>
    <div class="orderDetails">
      <Mheader isHeader="0" currentPage="6"></Mheader>
      <div class="main">
        <div v-if="content" class="orderContent">
          <div class="contentDetails">
            <div class="contentImg">
              <img :src="orderInfo.img">
            </div>
            <div class="contentDisplay">
              <div class="title">{{orderInfo.name}}<span>出发日期：{{orderInfo.date}}</span></div>
              <div class="dataDisplay">
                <ul>
                  <li>
                    <div v-for="item in cruiseCabinList">{{item.name}}</div>
                  </li>
                  <li>
                    <div>原价</div>
                    <div v-for="item in cruiseCabinList">{{item.price}}</div>
                  </li>
                  <li>
                    <div>加价</div>
                    <div v-for="item in cruiseCabinList">{{item.addPrice}}</div>
                  </li>
                  <li>
                    <div>加价后</div>
                    <div v-for="item in cruiseCabinList">{{item.price + item.addPrice}}</div>
                  </li>
                </ul>
              </div>
            </div>
            <div class="clearfloat"></div>
          </div>
          <div class="record">
            <div :class="isDisplay == 1 ? 'current':''" @click="isDisplay = 1">
              <svg class="icon wave" aria-hidden="true">
                <use xlink:href="#icon-liulan"></use>
              </svg>
              浏览记录
            </div>
            <div :class="isDisplay == 2 ? 'current':''" @click="isDisplay = 2">
              <svg class="icon wave" aria-hidden="true">
                <use xlink:href="#icon-goumai"></use>
              </svg>
              下单记录
            </div>
          </div>
          <div class="recordContent" v-if="isDisplay == 1">
            <div class="title">
              <div>用户</div>
              <div>地址</div>
              <div>浏览时间</div>
            </div>
            <ul>
              <li v-for="item in browseHistory">
                <div>
                  <img v-if="!item.image" src="../assets/img/baseImg.png" alt="">
                  <img v-if="item.image" :src="item.image">
                  <div class="describe">
                    <div>{{item.nickName}}</div>
                  </div>
                </div>
                <div>{{item.area}}</div>
                <div>{{item.addDate}}</div>
              </li>
            </ul>
          </div>
          <div class="orderRecord" v-if="isDisplay == 2">
            <div class="title">
              <div>订单编号</div>
              <div>舱型</div>
              <div>单价</div>
              <div>份数</div>
              <div>下单金额</div>
              <div>下单人微信</div>
              <div>下单时间</div>
            </div>
            <ul>
              <li v-for="item in orderHistory">
                <div>{{item.numberCode}}</div>
                <div>{{item.cabinName}}</div>
                <div>{{item.salePrice}}</div>
                <div>{{item.count}}</div>
                <div>{{item.saleAmount}}</div>
                <div class="wechat-inof">
                  <img v-if="!item.headImage" src="../assets/img/baseImg.png" alt="">
                  <img v-if="item.headImage" :src="item.headImage" alt="">
                  <span>{{item.nickName}}</span>
                </div>
                <div>
                <span class="date">
                  <span>{{item.addDate.substring(0,10)}}</span>
                  <span>{{item.addDate.substring(11,19)}}</span>
                </span>
                </div>
              </li>
            </ul>
          </div>
        </div>
        <div class="blank" v-if="!content">
          <div class="no-content">
            <img src="../assets/img/noContent.png">
            <div>暂时没有相关内容</div>
          </div>
        </div>
      </div>
      <m-footer></m-footer>
    </div>
</template>
<script>
  let vm;
  import Mheader from "@/components/header.vue";
  import MFooter from '@/components/footer'
  export default{
    name : 'orderDetails',
    components :{
      Mheader,
      MFooter,
    },
    data(){
        return{
          record:{
            page:1,
            total:1,
            pageSize:100
          },
          order:{
            page:1,
            total:1,
            pageSize:100
          },
          isDisplay:1,
          content:[],
          orderInfo:{},
          cruiseCabinList: [],
          browseHistory: [],
          orderHistory: []
        }
    },
    mounted(){
      vm = this;
      let numberCode = sessionStorage.numberCode;
      vm.axios.post(this.api + this.urlApi.querySharePrice,{
        sharePriceLogPage : vm.record.page,
        sharePriceLogRows : vm.record.pageSize,
        cruiseOrderPage : vm.order.page,
        cruiseOrderRows : vm.order.pageSize,
        numberCode : numberCode
      }).then((data) => {
        if(data.status){
          vm.content = data.content;
          let name = vm.content.sharePrice.cruiseVoyage.name;
          let img = vm.content.sharePrice.cruiseVoyage.image;
          let date = vm.content.sharePrice.cruiseVoyage.date.substring(0,10);
          vm.$set(vm.orderInfo,'name',name);
          vm.$set(vm.orderInfo,'img',img);
          vm.$set(vm.orderInfo,'date',date);
          vm.cruiseCabinList = vm.content.sharePrice.cruiseCabinList;
          vm.orderHistory = vm.content.cruiseOrderMap.rows;
          vm.browseHistory = vm.content.sharePriceLogMap.rows;
        }
      })
    },
    methods:{
      recordPageChange(num){

      },
    }
  }
</script>
<style lang="less">
  @import "../assets/css/common.less";
  .orderDetails{
    background-color: @appBg;
    .main{
      width: 1200px;
      min-height: 700px;
      background-color: #fff;
      margin:20px auto 40px;
      .borderRadius();
    }
    .orderContent{
      width:1200px;
      background-color: #fff;
      border-radius: 8px;
      padding: 40px 40px 20px 40px;
      margin: 40px auto;
      .contentDetails{
        margin-top: 10px;
        .contentImg{
          width: 190px;
          height: 129px;
          border:1px solid #cccccc;
          .borderRadius;
          padding: 3px;
          margin: 24px 15px 0 5px;
          float: left;
          img{
            width: 100%;
            height: 100%;
            .borderRadius;
          }
        }
        .contentDisplay{
          float: left;
          width: 910px;
          margin-top: 5px;
          .title{
            font-size: 28px;
            color:#333333;
            span{
              font-size: 16px;
              color:#999999;
              margin-left: 37px;
            }
          }
          .dataDisplay{
            ul{
              li{
                float: none;
                line-height: 30px;
                height: 30px;
                font-size: 14px;
                color:#333333;
                div{
                  padding-left: 13px;
                  float: left;
                  width: 100px;
                }
              }
              li:nth-of-type(1){
                height: 25px;
                line-height: 25px;
                background-color: #e7f3ff;
                padding-left: 70px;
              }
              li:nth-of-type(2),li:nth-of-type(3),li:nth-of-type(4){
                color:#999999;
                border: 1px solid #cbe3ff;
                border-top: 0;
                border-right: 0;
                div:nth-of-type(1){
                  color:#333333;
                  width: 69px;
                }
                div{
                  border-right: 1px solid #cbe3ff;
                }
              }
              li:nth-of-type(3){
                color:#ff9b0b;
              }
              li:nth-of-type(4){
                color:#275ea2;
              }
            }
          }
        }
      }
      .record{
        margin-top: 20px;
        height: 40px;
        border:1px solid #ccc;
        font-size: 20px;
        color:#cccccc;
        line-height: 38px;
        text-align: center;
        div{
          width: 50%;
          float: left;
          cursor: pointer;
          .wave{
            font-size: 20px;
            color:#999;
          }
        }
        .current{
          height: 40px;
          background-color: #6aadff;
          margin-top: -1px;
          color:#fff;
          .wave{
            color:#fff;
          }
        }
      }
      .recordContent,.orderRecord{
        margin-top: 10px;
        .title{
          height: 42px;
          line-height: 42px;
          font-size: 16px;
          color:#333;
          background-color: #e7f3ff;
          padding-left: 60px;
          div{
            float: left;
          }
          div:nth-of-type(1){
            width: 420px;
          }
          div:nth-of-type(2){
            width: 360px;
          }
          div:nth-of-type(3){
            width: 280px;
          }
        }
        >ul{
          min-height: 400px;
          li{
            float: none;
            padding: 11px 0 11px 54px;
            font-size: 16px;
            height: 82px;
            color:#333333;
            line-height: 60px;
            border-bottom: 1px solid #e7e7e7;
            >div{
              float: left;
              position: relative;
              height: 100%;
              .date{
                width: 100px;
                height: 48px;
                position: absolute;
                left: 0;
                top: 50%;
                transform: translateY(-50%);
                line-height: 22px;
                span{
                  display: block;
                }
              }
            }
            >div:nth-of-type(1){
              width: 426px;
              img{
                width: 60px;
                height: 60px;
                border:1px solid #cccccc;
                border-radius: 30px;
                float: left;
              }
              .describe{
                float: left;
                margin-left: 20px;
                height: 100%;
                line-height: 60px;
              }
            }
            >div:nth-of-type(2){
              width: 360px;
            }
            >div:nth-of-type(3){
              width: 280px;
            }
          }
        }
      }
      .orderRecord{
        .title, >ul li{
          padding-left: 40px;
          div:nth-of-type(1){
              width: 203px;
          }
          div:nth-of-type(2){
            width: 168px;
          }
          div:nth-of-type(3){
            width: 134px;
          }
          div:nth-of-type(4){
            width: 96px;
          }
          div:nth-of-type(5){
            width: 142px;
          }
          div:nth-of-type(6){
            width: 180px;
          }
          div:nth-of-type(7){
            width: 150px;
          }
          .wechat-inof{
            line-height: 53px;
            position: relative;
            img{
              height: 100%;
              width: 53px;
              display: block;
              .borderRadius(50%);
            }
            span{
              position: absolute;
              left: 62px;
              top: 50%;
              transform: translateY(-50%);
            }
          }
        }
        >ul{
          li{
            height: 76px;
            line-height: 53px;
            border-bottom: 1px solid #e7e7e7;
            font-size: 16px;
            color:#333;
          }
        }
      }
    }
    .paging-display{
      text-align: center;
      margin-top: 20px;
      height: 50px;
      background-color: #e7f3ff;
      border-radius: 8px;
      >div{
        display: inline-block;
        margin-top: 8px;
      }
      li:nth-of-type(1),.ivu-page-next{
        padding: 0 12px;
      }
    }
  }
</style>
