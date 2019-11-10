<template>
  <div id="bargainPrice">
    <Mheader isHeader="0" currentPage="4"></Mheader>
    <div class="main">
      <ul class="clearfloat" v-if="content.length !== 0" >
        <li class="item clearfloat" v-for="item in content">
          <div class="img-box">
            <img :src="item.name.image" alt="">
          </div>
          <div class="cruise-info-box">
            <h1>{{item.name.name}}</h1>
            <div class="trip-box clearfloat">
              <!-- 航线-->
              <div class="route">
                <ul class="clearfloat">
                  <li v-for="icon in item.airway">
                    <svg class="icon icon-hangxian1" aria-hidden="true" v-if="icon.nums == 0">
                      <use xlink:href="#icon-hangxian1"></use>
                    </svg>
                    <svg class="icon icon-arrow-left" aria-hidden="true" v-if="icon.nums == 1">
                      <use xlink:href="#icon-arrow-left"></use>
                    </svg>
                    <span>{{icon.cityname}}</span>
                  </li>
                </ul>
              </div>
              <!-- 出行日期 -->
              <div class="travel-date">
                <svg class="icon" aria-hidden="true">
                  <use xlink:href="#icon-riqi"></use>
                </svg>
                <span>出行日期：{{item.name.date.substring(0,10)}}</span>
              </div>
            </div>
            <div class="cabin-type">
              <ul class="clearfloat" :class="cabinTypeList ? 'hideList':'showList'">
                <li v-for="type in item.list" class="clearfloat">
                  <span class="cabin-name">{{type.name}}</span>
                  <span class="cabin-price"><i>￥</i>{{type.price - type.dPrice}}<label>/人</label></span>
                  <span class="old-price">{{type.price}}</span>
                  <span class="sale">{{type.dPrice?((type.price - type.dPrice)/type.price).toFixed(2)*10:0}}折</span>
                </li>
              </ul>
              <p style="display: none">
                <svg class="icon" aria-hidden="true" @click="cabinTypeList = !cabinTypeList;">
                  <use xlink:href="#icon-arrow-shrinktop"></use>
                </svg>
              </p>
            </div>
            <div class="btn-box clearfloat">
              <div class="count-down" style="display: none">
                <svg class="icon" aria-hidden="true">
                  <use xlink:href="#icon-daojishi"></use>
                </svg>
                <span ref="timer" >倒计时：</span>
              </div>
              <button @click="goProuductDetail(item)">立即购买</button>
            </div>
          </div>
        </li>
      </ul>
      <div v-if="content.length == 0" class="no-content">
        <img src="../assets/img/noContent.png" alt="">
        <div>暂时没有相关内容</div>
      </div>
    </div>
    <Mfooter></Mfooter>
  </div>
</template>

<style lang="less" scoped>
  @import "../assets/css/common";

  #bargainPrice{
    background-color: @appBg;
    .main{
      width: 1200px;
      min-height: 700px;
      background-color: #fff;
      margin:20px auto 40px;
      padding:40px 50px;
      .borderRadius();
      .item{
        width: 100%;
        min-height: 328px;
        border:1px solid #ccc;
        .borderRadius();
        margin-bottom: 40px;
        padding:20px 40px;
        .img-box{
          float: left;
          .borderRadius();
          width: 409px;
          height: 286px;
          padding: 1px;
          border:1px solid #ccc;
          img{
            .borderRadius();
            width: 100%;
            height: 100%;
          }
        }
        .cruise-info-box{
          float: left;
          width: 609px;
          padding-left: 40px;
          h1{
            font-size: 24px;
            font-weight: normal;
            width: 100%;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            margin-bottom: 10px;
          }
          .trip-box{
            .route{
              float: left;
              .icon-arrow-left{
                color: @topBg;
              }
              .icon-hangxian1{
                margin-right:6px;
              }
              .icon{
                vertical-align: middle;
                font-size: 18px;
              }
              span{
                vertical-align: middle;
                margin-right:4px;
                font-size: 14px;
              }
            }
            .travel-date{
              margin-left: 22px;
              float: left;
              .icon{
                vertical-align: middle;
                font-size: 14px;
              }
              span{
                vertical-align: middle;
                font-size: 14px;
              }
            }
          }
          .cabin-type{
            margin-top: 10px;
            .showList{
              height: auto;
              transition: height 2s linear;
              -webkit-transition: height 2s linear;
            }
            .hideList{
              height: 135px;
              overflow: hidden;
              transition: height 2s linear;
              -webkit-transition: height 2s linear;
            }

            li{
              background-color: #eef5fe;
              width: 100%;
              height: 45px;
              padding: 0 20px;
              font-size: 14px;
              span{
                line-height: 45px;
                float: left;
              }
              .cabin-name{
                width: 84px;
                overflow: hidden;
                height: 45px;
              }
              .cabin-price{
                margin-left: 70px;
                color: @clickBtnBg;
                font-size: 24px;
                width: 115px;
                label{
                  color: #333;
                  font-size: 14px;
                  font-style: normal;
                }
                i{
                  font-size: 14px;
                  font-style: normal;
                }
              }
              .old-price{
                margin-left:65px;
                text-decoration: line-through;
                color: #666;
              }
              .sale{
                float: right;
                color: #f00;
              }
            }
            li:nth-child(2n){
              background-color: #fff;
            }
            p{
              text-align: center;
              height: 45px;
              position: relative;
              .icon{
                color: @topBg;
                font-size: 25px;
                margin: 10px 0;
                cursor: pointer;
                position: absolute;
                transform: translate(-50%,-50%);
                top: 50%;
                left: 50%;
                animation: myfirst 1.5s infinite linear ;
                -webkit-animation: myfirst 1.5s infinite linear;
              }
              @keyframes myfirst {
                0% {
                  top: 27%;
                }
                50% {
                  top: 46%;
                }
                100% {
                  top:27%;
                }
              }
              @-webkit-keyframes myfirst {
                /* Safari 与 Chrome */
                0% {
                  right: 47px;
                }
                50% {
                  right: 67px;
                }
                100% {
                  right: 47px;
                }
              }
            }
          }
          .btn-box{
            width: 100%;
            margin-top: 15px;
            .count-down{
              float: left;
              color: #999;
              .icon{
                font-size: 19px;
                vertical-align: middle;
              }
              span{
                font-size: 16px;
                vertical-align: middle;
                margin-left: 10px;
              }
            }
            button{
              float: right;
              width: 164px;
              height: 50px;
              background-color: #fff;
              border:1px solid @clickBtnBg;
              color: @clickBtnBg;
              font-size: 24px;
              border-radius: 30px;
              cursor: pointer;
              outline: none;
            }
          }
        }
      }
      li:last-child{
        margin-bottom: 0;
      }
      .show{
        visibility: visible;
      }
      .hide{
        visibility:hidden;
      }
    }
  }
</style>

<script>
  import Mheader from "@/components/header.vue";
  import Mfooter from "@/components/footer.vue";
  let vm;
  export default{
    name:"bargainPrice",
    components:{
      Mheader,
      Mfooter
    },
    data(){
      return{
        cabinTypeList:true,
        content:[],         //尾单列表
      }
    },
    methods: {
      getQuoteList: function () {
        vm.axios.post(this.api + this.urlApi.getBargainPrice).then(function (data) {
          if (data.status == 1) {
            for (let i in data.content) {
              let list = data.content[i];
              let dataList = [];
              let objList = list.CruiseVoyage;
              //截取航线 --- start
              let str = objList.airwayNameCn;
              let city = str.split(" ");
              let cityName = city[0].split("-");
              let airwayNameCn = [];
              for (let a in cityName) {
                let airway = {};
                airway = {
                  nums: 1,
                  cityname: cityName[a],
                };
                airwayNameCn.push(airway);
                airwayNameCn[0].nums = 0;
              }
              //截取航线 --- end
              for (let j in list.CruiseCabinList) {
                let json = list.CruiseCabinList[j];
                let saleLevel = sessionStorage.saleLevel;
                for (let k in json.cabinPriceList) {
                  if (json.cabinPriceList[k].level == saleLevel) {
                    json.dPrice = json.cabinPriceList[k].price;
                  }
                }
                dataList.push(json);
              }
              let OBJ = {
                name: objList,
                list: dataList,
                airway: airwayNameCn,
              };
              vm.content.push(OBJ);
            }
          }
        });
      },
      goProuductDetail: function (obj) {
        sessionStorage.setItem('numberCode', obj.name.numberCode);
        vm.$router.push('/productDetail');
      }
    },
    mounted: function () {
      vm = this;
      vm.getQuoteList();
    }
  }
</script>
