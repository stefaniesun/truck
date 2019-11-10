<template>
  <div id="priceMarkup">
    <!-- 头部 start -->
    <header>
      <h2>加价</h2>
      <i @click="goBack" v-if="!pMarkUp" class="back iconfont icon-fanhui"></i>
    </header>

    <!-- 内容 start -->
    <div class="main">
      <div class="content">
        <div class="item-box" v-for="(con,index) in content">
          <div class="cabin-title">
            <span>{{con.cabinType}}</span>
          </div>
          <ul class="cabin-list">
            <li class="clearfloat" v-for="item in con.cabContent">
              <div class="cabin-name">
                <span>{{item.name}}</span>
                <span class="old-price">原价：¥{{item.displayPrice}}</span>
                <div class="rebate-price" v-if="item.rebate">后返{{item.rebate}}/人</div>
              </div>
              <div class="input-price">
                <input type="number" autocomplete="off" v-model="item.inputPrice" @input="item.inputPrice = parseInt(item.inputPrice)" @blur="addPrice(item)">
                <span>加价金额</span>
              </div>
              <div class="new-price">
                <span>加价后价格</span>
                <span>¥{{item.newprice}}</span>
              </div>
            </li>
          </ul>
          <!-- 批量加价 start-->
          <div class="batch-increase" @click="showEject(index)">
            <span>批量加价</span>
            <i class="iconfont icon-jia"></i>
          </div>
        </div>
      </div>
    </div>

    <!-- 确认分享 start -->
    <div class="confirm-share" @click="share = true">
      <button>确认分享</button>
    </div>

    <!-- 弹窗 -->
    <div class="eject-box" @click.self.stop="ejectStatus = false" v-if="ejectStatus">
      <div class="eject-inner">
        <p class="title">批量加价</p>
        <div class="input-box">
          <p>输入加价金额</p>
          <input type="number" autocomplete="off" v-model="inputPrice" @input="inputPrice = parseInt(inputPrice)">
        </div>
        <div class="btn-box">
          <button @click="closeEject()">确认</button>
        </div>
      </div>
    </div>
    <div class="share-content" v-if="share" @click="share = false">
    </div>
    <div class="share-img" v-if="share" @click="share = false">
      <img src="../assets/img/share.png">
    </div>
  </div>
</template>

<style lang="less" scoped>
  @import "../assets/css/base.less";

  .small-span(@fn:26*@basePX;@col:@subTextColor){
    font-size: @fn;
    color: @col;
  }

  #priceMarkup{
    width: 100%;
    height: 100%;
    .main{
      width: 100%;
      height: 100%;
      max-height: 100%;
      min-height: 100%;
      padding:88*@basePX 24*@basePX 154*@basePX;
      background-color: @appBg;
      overflow-x: hidden;
      overflow-y: auto;
      .content{
        .item-box{
          width: 100%;
          background-color: #fff;
          margin-top: 24*@basePX;
          padding: 40*@basePX 24*@basePX 0;
          .borderRadius;
          .cabin-title{
            width: 579*@basePX;
            height: 1px;
            background-color: @topBg;
            margin:auto;
            text-align: center;
            position: relative;
            span{
              display: inline-block;
              padding:0 20*@basePX;
              background-color: #fff;
              position: absolute;
              transform:translate(-42%,-50%);
              color: @topBg;
            }
          }
          .cabin-list{
            width: 100%;
            margin-top: 40*@basePX;
            li{
              width: 100%;
              border-bottom: 1px solid @lineColor;
              padding: 45*@basePX 0;
              text-align: center;
              .cabin-name{
                float: left;
                width: 5rem;
                span{
                  display: block;
                  font-size: 32*@basePX;
                  color: #4c4c4c;
                  text-align: left;
                }
                .old-price{
                  .small-span;
                }
                .rebate-price{
                  width: 2.77333333rem;
                  height: 0.59733333rem;
                  line-height: 0.59733333rem;
                  border-radius: 0.29866667rem;
                  -webkit-border-radius: 0.29866667rem;
                  -moz-border-radius: 0.29866667rem;
                  text-align: center;
                  color: #fff;
                  background: #ff0000;
                  font-size: 0.46933333rem;
                }
              }
              .input-price{
                float: left;
                margin: -4*@basePX auto 0;
                input{
                  width: 220*@basePX;
                  border:0;
                  border-bottom: 2*@basePX solid @clickBtnBg;
                  text-align: center;
                  color: @clickBtnBg;
                  outline: none;
                  font-size: 60*@basePX;
                  margin-top: -8px;
                }
                input::-webkit-input-placeholder{
                  color:@clickBtnBg;
                }
                span{
                  .small-span;
                  display: block;
                  text-align: center;
                  margin-top: 14*@basePX;
                }
              }
              .new-price{
                float: right;
                span{
                  .small-span;
                  display: block;
                  text-align: center;
                }
                span:nth-child(2){
                  .small-span(@col:@clickBtnBg);
                }
              }
            }
          }
          .batch-increase{
            width: 176*@basePX;
            margin: 0 auto;
            text-align: center;
            padding: 30*@basePX 0;
            color:@clickBtnBg;
            span{
              .small-span(@fn:26*@basePX;@col:@clickBtnBg);
            }
          }
        }
      }
    }
    .confirm-share{
      width: 100%;
      background-color: #fafafa;
      position: fixed;
      bottom: 0;
      text-align: center;
      padding: 24*@basePX 0;
      button{
        width:600*@basePX ;
        height: 88*@basePX;
        background-color: @clickBtnBg;
        color:#fff;
        .borderRadius(@radius:50*@basePX);
        font-size: 40*@basePX;
        border:0;
        cursor: pointer;
        outline: none;
      }
    }
    .eject-box{
      width: 100%;
      height: 100%;
      position: fixed;
      top: 0;
      background-color: rgba(0,0,0,0.35);
      z-index: 9;
      .eject-inner{
        width: 600*@basePX;
        height: 800*@basePX;
        left: 50%;
        margin-left: -300*@basePX;
        position: absolute;
        top: 130*@basePX;
        background-color: #fff;
        .borderRadius;
        overflow: hidden;
        .title{
          .small-span(@fn:36*@basePX;@col:#fff);
          height: 140*@basePX;
          padding-top: 30*@basePX;
          text-align: center;
          font-size: 36*@basePX;
          color: #fff;
          background: @topBg url(../assets/img/dialog_head.png) 0 0 no-repeat;
          background-size: 100% 100%;
        }
        .input-box{
          margin:70*@basePX auto 0;
          width: 354*@basePX;
          height: 354*@basePX;
          border:1px solid @topBg;
          .borderRadius(@radius: 50%);
          background-color: #fafafa;
          text-align: center;
          p{
            .small-span(@fn:28*@basePX;@col:#999);
            text-align: center;
            margin-top: 104*@basePX;
          }
          input{
            width: 220*@basePX;
            border:0;
            border-bottom: 2*@basePX solid @clickBtnBg;
            text-align: center;
            color: @clickBtnBg;
            outline: none;
            font-size: 60*@basePX;
            margin: 50*@basePX auto 0;
            background-color: transparent;
          }
          input::-webkit-input-placeholder{
            color:@clickBtnBg;
          }
        }
        .btn-box{
          width: 100%;
          text-align: center;
          margin-top: 90*@basePX;
          button{
            width: 348*@basePX;
            height: 78*@basePX;
            background-color: @topBg;
            border:0;
            .borderRadius(@radius:50*@basePX);
            .small-span(@fn:32*@basePX;@col:#fff);
            outline: none;
          }
        }
        &:after{
          content: ' ';
          height: 10*@basePX;
          width: 100%;
          background-color: @changeBtnBg;
          display: block;
          position: absolute;
          left: 0;
          bottom: 0;
        }
      }
    }
    .share-content{
      position: fixed;
      top:0;
      left: 0;
      width: 100%;
      height: 100%;
      background-color: #000;
      opacity: 0.4;
      z-index: 10;
    }
    .share-img{
      position: fixed;
      top:0;
      left: 14%;
      width: 80%;
      height: 100%;
      z-index: 11;
      img{
      width: 100%;
    }

    }
  }
</style>

<script>

  let vm;
  export default{
    name:'priceMarkup',
    props: ['slcDate','pMarkUp'],
    data(){
      return{
        ejectStatus:false,  //弹窗
        content:[],
        inputPrice:0,
        index:0,
        share : false,
        numberCode : ''
      }
    },
    mounted:function () {
      vm = this;
      vm.getSharePrice(),
      vm.getBasePrice()
    },
    methods:{
    	getSharePrice(){
        let shareParame = {
          voyage : sessionStorage.numberCode,
          distributor : sessionStorage.userCode
        };
        vm.axios.post(vm.api+vm.urlApi.addSharePrice,shareParame).then(function (data) {
          if(data.status){
            vm.numberCode = data.content;
            let voyage = sessionStorage.numberCode;
            let distributor = sessionStorage.userCode;
            let outkey = sessionStorage.outkey;
            let baseUrl = location.href.split('ptDetailToC')[0];
            location.replace(baseUrl + 'ptDetailToC?voyage=' + voyage + '&distributor=' + distributor + '&sharePrice='+ vm.numberCode + '&outkey=' + outkey +'&isQRcode=no');
          }
        })
      },
      getBasePrice(){
        let parame = {
          voyage : sessionStorage.numberCode
        };
        vm.axios.post(vm.api + vm.urlApi.getBasePrice, parame).then(function (data) {
          if (data.status) {
            let list = data.content.cabiList;
            document.title = data.content.voyageNameCn;
            sessionStorage.setItem('documentTitle',data.content.voyageNameCn);
            for (let num in list) {
              let json = list[num];
              let level = sessionStorage.saleLevel;
              if(json.cabinPriceList.length > 0){
                for (let i in json.cabinPriceList) {
                  let price = json.cabinPriceList[i];
                  if (price.level == level) {
                    json.displayPrice = price.price ? json.price - price.price : json.price;
                    json.rebate = price.rebate;
                    break;
                  }
                }
              }else {
                  json.displayPrice = json.price;
              }

              json.inputPrice = 0;
              json.newprice = json.displayPrice;
              switch (json.type.substring(0, 1)) {
                case 'I':
                  let a = vm.lookup('内舱房');
                  if (a == -1) {
                    let arr = {
                      cabinType: '内舱房',
                      cabContent: [json]
                    };
                    vm.content.push(arr);
                  } else {
                    vm.content[a].cabContent.push(json);
                  }
                  break;
                case 'T':
                  let b = vm.lookup('套房');
                  if (b == -1) {
                    let arr = {
                      cabinType: '套房',
                      cabContent: [json]
                    };
                    vm.content.push(arr);
                  } else {
                    vm.content[b].cabContent.push(json);
                  }
                  break;
                case 'S':
                  let c = vm.lookup('海景房');
                  if (c == -1) {
                    let arr = {
                      cabinType: '海景房',
                      cabContent: [json]
                    };
                    vm.content.push(arr);
                  } else {
                    vm.content[c].cabContent.push(json);
                  }
                  break;
                case 'B':
                  let d = vm.lookup('阳台房');
                  if (d == -1) {
                    let arr = {
                      cabinType: '阳台房',
                      cabContent: [json]
                    };
                    vm.content.push(arr);
                  } else {
                    vm.content[d].cabContent.push(json);
                  }
                  break;
              }
            }
          }
        })
      },
      lookup : function(obj){
        let number = -1;
        for(let num in vm.content){
            if(vm.content[num].cabinType == obj){
                number = num;
                break;
            }
        }
        return number;
      },//查找是否存在舱型
      addPrice:function (obj) {
        let data = vm.content;
        let priceJson = [];
        for(let j=0;j<data.length;j++){
          let arr = data[j];
          for(let i in arr.cabContent){
            let json =  arr.cabContent[i];
            if(json.inputPrice){
              let Pjson = {};
              Pjson = {
                cabin : json.numberCode,
                stock : json.stock,
                addPrice : json.inputPrice
              };
              priceJson.push(Pjson);
            }
          }
        }
        if( priceJson.length ){
          let parame = {
            numberCode : vm.numberCode,
            priceJson : JSON.stringify(priceJson)
          };
          vm.axios.post(this.api+this.urlApi.editSharePrice,parame).then(function (data) {
            if(data.status == 1){
              if (!obj.inputPrice || obj.inputPrice < 0) obj.inputPrice = 0;
              obj.newprice = obj.displayPrice + parseInt(obj.inputPrice);
            }
          })
        }
      },//单个加价
      showEject:function (obj) {
        vm.ejectStatus = true;
        vm.inputPrice = 0;
        vm.index = obj;
      },//批量加价按钮点击
      closeEject:function () {
        vm.ejectStatus = !vm.ejectStatus;
        if (!vm.inputPrice || vm.inputPrice < 0) vm.inputPrice = 0;
        let data = vm.content;
        let priceJson = [];
        for(let j=0;j<data.length;j++){
        	let arr = data[j];
        	if( vm.index == j ){
            for(let i in arr.cabContent){
              let json =  arr.cabContent[i];
              let Pjson = {};
              Pjson = {
                cabin : json.numberCode,
                stock : json.stock,
                addPrice : vm.inputPrice
              };
              priceJson.push(Pjson);
            }
          }else {
            for(let i in arr.cabContent){
              let json =  arr.cabContent[i];
              if(json.inputPrice){
                let Pjson = {};
                Pjson = {
                  cabin : json.numberCode,
                  stock : json.stock,
                  addPrice : json.inputPrice
                };
                priceJson.push(Pjson);
              }
            }
          }
        }
        if(priceJson.length){
          let parame = {
            numberCode : vm.numberCode,
            priceJson : JSON.stringify(priceJson)
          };
          vm.axios.post(this.api+this.urlApi.editSharePrice,parame).then(function (res) {
            if(res.status == 1){
              for(let i in data[vm.index].cabContent){
                let json =  data[vm.index].cabContent[i];
                json.inputPrice = parseInt(vm.inputPrice);
                json.newprice = json.displayPrice + parseInt(vm.inputPrice);
              }
            }
          })
        }
      },//多个加价
      goBack(){
        vm.$router.goBack();
      }
    }
  }
</script>
