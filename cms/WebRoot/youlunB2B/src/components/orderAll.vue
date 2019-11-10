<template>
  <div class="orderAll">
    <mt-loadmore :bottom-method="loadBottom" :bottom-all-loaded="allLoaded" :auto-fill="false" ref="loadmore">
      <section v-for="(item,index) in orderList">
        <div class="number-wrapper panel-top">
          <div class="content-center clearfloat">
            <span class="order-num">订单号：{{item.numberCode}}</span>
            <span @click="showOrderMsg(item.numberCode,index)" class="order-msg">
              消息
              <i v-if="item.msgCount">{{item.msgCount}}</i>
            </span>
          </div>
        </div>
        <div class="info-wrapper ">
          <div class="content-center">
            <div class="order-info clearfloat">
              <div class="img-wrap">
                <img :src="item.image" alt="">
              </div>
              <div class="detail">
                <h3 @click="jumpDetail(item.voyage)">{{item.title}}</h3>
                <span>出发日期：{{item.date.substring(0,10).replace(/-/g,'/')}}</span>
              </div>
              <div class="tips">
                <span class="old-price" >&yen;{{item.amount}}</span>
                <!--<span class="fanxian" v-if="item.rebate">{{'后返' + item.rebate + '/人'}}</span>-->
              </div>
            </div>
            <ul class="info-panel clearfloat">
              <li>联系人：{{item.LinkMan}}
                <span class="iconfont icon-web-icon- item" @click="clickOpen(item)"></span>
              </li>
              <li>电话：<a :href="'tel:'+item.LinkPhone">{{item.LinkPhone}}</a>
                <span class="iconfont icon-web-icon- item" @click="clickOpen(item)"></span>
              </li>
              <li class="cabin-wrap" v-for="orderItem in item.orderList">
                {{orderItem.cabinName + ' * ' + orderItem.count + '(间)'}}
                <span class="cabin-type" :style="{background: orderItem.stockType == 1?'#6aadff':'#ff9b0b'}">{{orderItem.stockType == 1 ? '现询':'实库'}}</span>
              </li>
            </ul>
            <ul class="status-title clearfloat">
              <li>出行人状态</li>
              <li>资源状态</li>
              <li>资金状态</li>
            </ul>
            <ul class="clearfloat status-panel">
              <li v-for="(statusItem,itemIndex) in statusArr[index]" :class="statusItem.status ? 'status-change':''" class="status-item">
                <div class="info">
                  <span>{{statusItem.title}}</span>
                  <div class="clearfloat" v-if="(item.paymentStatus == 'deposit' || item.paymentStatus == 'confirm') && itemIndex == 2">
                    <span v-if="item.deposit" class="tips"><span :class="(item.paymentStatus == 'deposit' || item.paymentStatus == 'confirm')?'':'change'" class="pull-left">定金 {{item.deposit?item.deposit:0}}</span><span :class="(item.paymentStatus == 'deposit' || item.paymentStatus == 'confirm')?'':'change'" class="pull-right">{{(item.paymentStatus == 'deposit' || item.paymentStatus == 'confirm')?'已付':'未付'}}</span></span>
                    <span v-if="(item.paymentStatus == 'confirm' && item.deposit) || item.deposit" class="tips"><span :class="item.paymentStatus == 'confirm'?'':'change'" class="pull-left">{{item.deposit?'尾款 '+(item.amount-item.deposit):'全款 '+item.amount}}</span><span :class="item.paymentStatus == 'confirm'?'':'change'" class="pull-right">{{item.paymentStatus == 'confirm'?'已付':'未付'}}</span></span>
                  </div>
                  <div class="clearfloat" v-if="item.paymentStatus == 'none' && item.resourceStatus !== 'none' && itemIndex == 2">
                    <span @click="payment(item)" class="tips-btn">付款</span>
                  </div>
                  <div class="clearfloat end-date" v-if="(item.paymentStatus == 'deposit'||item.paymentStatus == 'none') && item.resourceStatus == 'lock' && itemIndex == 1 && item.expireDate">
                    倒计时:{{item.expireDate}}
                  </div>
                  <div class="clearfloat" v-if="!statusItem.status && itemIndex == 0">
                    <span @click="showPersonPanel(item)" class="tips-btn">编辑</span>
                  </div>
                </div>
              </li>
            </ul>
          </div>
        </div>
        <div class="number-wrapper panel-bottom">
          <div class="content-center clearfloat">
            <div class="orderInfo">
              <div>下单日期：{{item.addDate.substring(0,10).replace(/-/g,'/')}}&nbsp;&nbsp;{{item.addDate.substring(11,19)}}</div>
              <div v-if="item.type == 'SHARE'">下单人微信：</div>
            </div>
            <div class="orderButton">
              {{item.type == 'SHARE'?'分享下单':item.type == 'DISTRIBUTOR'?'线上订单':item.type == 'PLATFORM'?'线下订单':'C端下单'}}
              <div v-if="item.type == 'SHARE'" @click="isTransformation">确定</div>
            </div>
          </div>
        </div>
      </section>
    </mt-loadmore>
    <div v-if="orderList.length==0" class="blank-page">
      <img src="../assets/img/blank-search.jpg" alt="">
      <span>暂时没有相关内容</span>
    </div>
    <transition  name="custom-classes-transition"
                 enter-active-class=" animated slideInRight"
                 leave-active-class="animated slideOutRight">
      <div class="person-wrap" v-if="showPerson.status">
        <personPanel :order="order" :showPerson="showPerson"></personPanel>
      </div>
      <chatPanel v-if="showMsgPanel.status" :order="order" :showMsgPanel="showMsgPanel"></chatPanel>
    </transition>
    <transition  name="custom-classes-transition"
                 enter-active-class=" animated slideShow"
                 leave-active-class="animated slideHide">
      <Payment v-if="isPayment" :content="paymentObj" @confirm="personConfirm"></Payment>
      <modify v-if="isModify" :link="link" @confirmAll="confirmAll"></modify>
    </transition>
  </div>
</template>

<script>
  let vm;
  import personPanel from '@/components/personPanel'
  import common from '@/assets/js/common';
  import Payment from "@/components/paymentPage.vue";
  import modify from "@/components/modifyContacts.vue";
  import chatPanel from "@/components/chatPanel.vue";
  export default {
    name: 'orderAll',
    components: {
      personPanel,
      Payment,
      modify,
      chatPanel
    },
    data(){
      return{
        total: 0,
        page: 1,
        allLoaded: false,
        orderList: new Array,
        parame: {
          page: 1,
          rows: 3
        },
        statusArr:new Array,
        showPerson: {
          status: false
        },
        order: '',
        isPayment : false,
        paymentObj:{},
        isModify : false,
        link:{},
        showMsgPanel: {
          status: false
        },
        orderIndex: 0
      }
    },
    mounted(){
      vm = this;
    },
    activated(){
      vm.orderList = new Array;
      if(sessionStorage.userType == 'true'){
        vm.parame.queryParam = '';
        vm.parame.startDate = '';
        vm.parame.endDate = ''
      }
      vm.getData();
    },
    methods: {
      getData(){
        if(sessionStorage.userType == 'false'){
            vm.parame.openId = sessionStorage.openid
        }
        vm.axios.post(this.api+this.urlApi.queryCruiseOrderList,vm.parame).then(function (respones) {
          if(respones.status){
            let content = respones.content;
            vm.total = content.total;
            if (content.rows.length) {
            	vm.orderList.length ?  vm.orderList = vm.orderList.concat(content.rows) :  vm.orderList = content.rows;
            }
            vm.setStatusArr();
          }
        })
      },
      loadBottom() {
        if( vm.page * 3 < vm.total ){
          vm.page++;
          vm.parame.page = vm.page;
          vm.getData();
        }else {
          vm.allLoaded = true;
        }
        this.$refs.loadmore.onBottomLoaded();
      },
      jumpDetail(numberCode){
        sessionStorage.setItem('numberCode',numberCode);
        vm.$router.push('/productDetail');
      },
      setStatusArr(){
        vm.statusArr = new Array;
        for(let i=0;i<vm.orderList.length;i++){
          let obj = vm.orderList[i];
          if(obj.expireDate && (obj.paymentStatus == 'deposit'||obj.paymentStatus == 'none')) obj.expireDate = common.timeFn(obj.expireDate);
          let statusItem = new Array;
          statusItem[0] = new Object;
          statusItem[1] = new Object;
          statusItem[2] = new Object;
          if(obj.personStatus == 'confirm') {
            statusItem[0].title = '已确认出行人';
            statusItem[0].status = true;
          }else {
            statusItem[0].title = '待确认出行人';
            statusItem[0].status = false;
          }
          let resourceTitle,paymentTitle;
          let resourceStatus,paymentStatus;
          switch (obj.resourceStatus)
          {
            case 'none':
              resourceTitle = '资源确认中';
              resourceStatus = false;
              break;
            case 'lock':
              resourceTitle = '已留舱';
              resourceStatus = false;
              break;
            case 'confirm':
              resourceTitle = '已确认';
              resourceStatus = false;
              break;
            case 'submit':
              resourceTitle = '已出票';
              resourceStatus = true;
              break;
            case 'cancel':
              resourceTitle = '已失效';
              resourceStatus = false;
              break;
          }
          statusItem[1].title = resourceTitle;
          statusItem[1].status = resourceStatus;
          switch (obj.paymentStatus)
          {
            case 'none':
              paymentTitle = '待付款';
              paymentStatus = false;
              break;
            case 'deposit':
              paymentTitle = '定金确认';
              paymentStatus = false;
              break;
            case 'confirm':
              paymentTitle = '全款确认';
              paymentStatus = false;
              break;
            case 'ticket':
              paymentTitle = '已开票';
              paymentStatus = true;
              break;
          }
          statusItem[2].title = paymentTitle;
          statusItem[2].status = paymentStatus;
          vm.statusArr.push(statusItem);
        }
      },
      payment(obj){
          vm.paymentObj = obj;
          vm.isPayment = true;
      },
      showPersonPanel(obj){
      	vm.order = obj.numberCode;
        vm.showPerson.status = true;
        vm.bodyNoScrolling.open();
      },
      personConfirm(flag){
        if(flag){
          vm.axios.post(this.api+this.urlApi.orderPayOper,{order:vm.paymentObj.numberCode}).then(function (respones) {
            if(respones.status){
              vm.isPayment = false;
              location.reload();
            }
          })
        }else {
          vm.isPayment = false;
        }
      },
      clickOpen(obj){
          vm.link = obj;
          vm.isModify = true;
      },
      confirmAll(upData){
        vm.isModify = false;
        if(upData.status){
        	let linkObj = upData.linkObj;
        	vm.orderList.forEach(function (item,index) {
            if(item.numberCode === linkObj.numberCode){
              item.LinkMan = linkObj.LinkMan;
              item.LinkPhone = linkObj.LinkPhone;
            }
          })
        }
      },
      isTransformation(){
        vm.dialog.showDialog({
          close: true,
          btnText: '确定',
          msg: '确定要转换订单吗？'
        },function (flag) {
          if( flag ){
            console.log(flag)
          }
        })
      },
      showOrderMsg(numberCode,index){
        vm.showMsgPanel.status = true;
        vm.order = numberCode;
        vm.orderIndex = index;
        vm.bodyNoScrolling.open();
      },
      getMsgPanelStatus(){
        vm.showMsgPanel.status = false;
        vm.bodyNoScrolling.close();
      },

    }
  }

</script>

<style scoped lang="less">
  @import "../assets/css/base.less";

  .orderAll{
    width: 100%;
    padding-top: 17*@basePX;
    &:after{
      content: ' ';
      display: block;
      width: 100%;
      height: 20*@basePX;
      background-color: #fff;
    }
    section{
      background: #fff;
      &:not(:first-child){
        margin-top: 17*@basePX;
      }
      .content-center{
        width: 92%;
        margin: 0 auto;
        .order-msg{
          float: right;
          position: relative;
          margin-right: 10*@basePX;
          i{
            width: 30*@basePX;
            height: 30*@basePX;
            line-height: 30*@basePX;
            text-align: center;
            font-size: 22*@basePX;
            .borderRadius(50%);
            background: red;
            position: absolute;
            top: 0;
            right: -30*@basePX;
            font-style: normal
          }
        }
      }
      .number-wrapper{
        line-height: 51*@basePX;
        font-size: 26*@basePX;
        position: relative;
        .orderInfo{
          float: left;
        }
        .orderButton{
          float: right;
          font-size: 26*@basePX;
          color:@changeBtnBg;
          position: absolute;
          top:50%;
          right:4%;
          transform: translateY(-50%);
          div{
            width: 98*@basePX;
            float: right;
            height: 40*@basePX;
            line-height: 40*@basePX;
            font-size: 26*@basePX;
            color: #fff;
            background-color: @changeBtnBg;
            border-radius: 20*@basePX;
            text-align: center;
            margin:5*@basePX 0 0 10*@basePX;
          }
        }
      }
      .panel-top{
        background-color: @changeBtnBg;
        color: #fff;
      }
      .panel-bottom{
        background-color: @appBg;
      }
      .info-wrapper{
        background-color: #fff;
      }
      .order-info{
        &:first-child{
          border-bottom: 1px solid @lineColor;
        }
        padding: 16*@basePX 0;
        position: relative;
        .info{
          float: left;
          font-size: 26*@basePX;
          &:not(:first-child) {
            margin-left: 5%;
          }
        }
        .img-wrap{
          float: left;
          width: 25%;
          height: 110*@basePX;
          .borderRadius(4*@basePX);
          overflow: hidden;
          img{
            width: 100%;
            height: 100%;
            max-width: 100%;
            max-height: 100%;
          }
        }
        .detail{
          float: left;
          width: 50%;
          margin-left: 4%;
          overflow: hidden;
          h3{
            font-size: 28*@basePX;
            color: #4c4c4c;
          }
          span{
            font-size: 28*@basePX;
            color: @subTextColor;
          }
        }
        .tips{
          right: 0;
          width: 20%;
          display: table;
          position: absolute;
          top: 50%;
          transform: translateY(-50%);
          .old-price,
          .fanxian{
            display: table-cell;
            vertical-align: middle;
            text-align: center;
            float: right;
            width: 100%;
          }

          .old-price{
            font-size: 38*@basePX;
            color: @tipsColor;
          }
          /*.fanxian{
            display: block;
            height: 32*@basePX;
            width: 130*@basePX;
            background: #ff0000;
            color: #fff;
            text-align: center;
            line-height: 32*@basePX;
            font-size: 24*@basePX;
            .borderRadius(14*@basePX)
          }*/
        }
      }
      .info-panel{
        width: 100%;
        border-bottom: 1px solid @lineColor;
        padding-bottom: 10*@basePX;
        li{
          width: 50%;
          color: @subTextColor;
          padding: 10*@basePX 0 0;
          float: left;
          a{
            text-decoration: none;
            color: @changeBtnBg;
          }
          .item{
            font-size: 34*@basePX;
            color:@changeBtnBg;
          }
          .cabin-type{
            display: inline-block;
            padding: 0 8*@basePX;
            .borderRadius;
            color: #fff;
            margin-left: 3*@basePX;
          }
        }
      }
      .status-title{
        width: 100%;
        margin-top: 10*@basePX;
        li{
          float: left;
          width: 33.3333333%;
          text-align: center;
          color: #333;
        }
      }
      .status-panel{
        width: 100%;
        margin-bottom: 24*@basePX;
        .status-item{
          width: 222*@basePX;
          height: 100*@basePX;
          margin-right: 0.23866667rem;
          float: left;
          text-align: center;
          .borderRadius(6px);
          background-color: #e1eeff;
          position: relative;
          &:last-child{
            margin-right: 0;
          }
          .info{
            width: 100%;
            text-align: center;
            color: @tipsColor;
            position: absolute;
            left: 0;
            top: 50%;
            transform: translateY(-50%);
            line-height: 32*@basePX;
            .tips{
              display: block;
              font-size: 12px;
              color: #0ab012;
              .pull-left{
                padding-left: 12*@basePX;
              }
              .pull-right{
                padding-right: 12*@basePX;
              }
              .change{
                color: #2c3e50;
              }
            }
            .end-date{
              font-size: 12px;
            }
            .tips-btn{
              background: @changeBtnBg;
              display: block;
              width: 120*@basePX;
              height: 38*@basePX;
              line-height: 38*@basePX;
              margin: 8*@basePX auto 0;
              .borderRadius(38*@basePX);
              cursor: pointer;
              color: #fff;
            }
          }
        }
        .status-change{
          background-color: #eafeeb;
        }
      }
    }
    .blank-page{
      width: 100%;
      padding: 32*@basePX 22*@basePX 25*@basePX;
      overflow-x: hidden;
      text-align: center;
      img{
        width: 508*@basePX;
        height: 404*@basePX;
        display: block;
        margin: 28*@basePX auto 0;
        transform: translateX(20*@basePX);
      }
      span{
        display: block;
        padding-top: 50*@basePX;
        color: @subTextColor;
        font-size: 32*@basePX;
      }
    }
    .person-wrap{
      position: fixed;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      max-height: 100%;
      z-index: 15;
      overflow: hidden;
      background-color: #fff;
    }
    .animated {
      animation-duration: .5s;
      animation-fill-mode: both;
    }
    @keyframes slideInRight {
      from {
        transform: translate3d(100%, 0, 0);
        visibility: visible;
      }

      to {
        transform: translate3d(0, 0, 0);
      }
    }

    .slideInRight {
      animation-name: slideInRight;
    }

    @keyframes slideOutRight {
      from {
        transform: translate3d(0, 0, 0);
      }

      to {
        visibility: hidden;
        transform: translate3d(100%, 0, 0);
      }
    }

    .slideOutRight {
      animation-name: slideOutRight;
    }

    @keyframes slideShow {
      from {
        transform: scaleX(0);
        visibility: visible;
      }

      to {
        transform: scaleX(1);
      }
    }

    .slideShow {
      animation-name: slideShow;
    }

    @keyframes slideHide {
      from {
        transform: scaleX(1);
      }

      to {
        transform: scaleX(0);
        visibility: visible;
      }
    }

    .slideHide {
      animation-name: slideHide;
    }
  }

</style>
