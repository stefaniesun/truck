<template>
  <div class="productQuery">
    <header class="search-herder" ref="header">
      <div class="search-input">
        <input @change="startSearch" v-model="keyWord" type="text" placeholder="搜索航线/目的地等关键词"/>
        <div><i class="iconfont icon-sousuo1"></i></div>
      </div>
      <i class="iconfont user icon-gerenzhongxin" @click="goUserCenter()"></i>
    </header>
    <!--条件搜索-->
    <div class="condition-search" ref="searchBtn">
      <p class="title">
        <span @click="openScreenPanel">
          <i class="iconfont icon-screen_icon"></i>
          {{isReset ? '重新筛选':'点击筛选'}}
        </span>
        <span class="change-type" @click="isQuick = !isQuick">切换</span>
      </p>
      <ScreenPanel
        :screen-panel-status="screenPanelStatus"
        :is-request="isRequest"
        @closePanel="closePanel"
        @startScreen="getScreenData"
        @screenKeyArr="getScreenKeyArr">
      </ScreenPanel>
      <transition name="slideRight" enter-active-class="animated slideInRight">
        <div v-show="isReset" class="screen-key-wrap" ref="screenKeyWrap">
          <ul class="key-word">
            <li v-if="item" v-for="(item,index) in screenKeyArr">
              <span>{{item}}</span>
              <i @click="deleteKey(index,item)" class="iconfont icon-cha"></i>
            </li>
          </ul>
          <div class="clearfloat"></div>
        </div>
      </transition>
    </div>
    <!--内容展示-->
    <mt-loadmore :bottom-method="loadBottom" :bottom-all-loaded="allLoaded" :auto-fill="false" ref="loadmore">
      <div v-if="!noData" class="content-display" ref="productList">
        <div @click="goProductDetail(item.numberCode)" v-if="!isQuick" class="single-content" v-for="item in productList">
          <p>{{item.name}}</p>
          <img v-if="item.image" :src="item.image">
          <img v-if="!item.image" src="../assets/img/baseImg.png" alt="">
          <div class="basic-info">
            <div class="money-display">￥{{item.price}}<span>起/人</span></div>
            <div class="clearfloat"></div>
            <div class="time-display"><i class="iconfont icon-date"></i><span>出发日期：{{item.date.substring(0,10)}}</span></div>
            <div class="time-display"><i class="iconfont icon-chengshi"></i><span>出发城市：{{item.startCity}}</span></div>
            <div class="time-display cruise-display"><i class="iconfont icon-youlunyou"></i><span>邮轮号：{{item.cruiseNameCn}}</span></div>
            <div class="clear"></div>
          </div>
        </div>
        <div class="fastBooking" v-if="isQuick">
          <div class="cruise" v-for="num in quickData">
            <div class="title">
              <div class="titleContent">{{num.name}}</div>
              <div class="details" @click="cruiseDetail(num)">详情</div>
              <div class="clearfloat"></div>
            </div>
            <ul>
              <li v-for="cabin in num.list" @click="joinShoppingCart(num,cabin)">
                <div>{{cabin.title}}</div>
                <div class="price">{{cabin.content ? cabin.content : '—'}}</div>
              </li>
            </ul>
            <div class="clearfloat"></div>
          </div>
        </div>
      </div>
      <div class="footer-bottom" v-if="isQuick"></div>

    </mt-loadmore>

    <div v-if="noData" class="blank-page">
      <img src="../assets/img/blank-search.jpg" alt="">
      <span>暂时没有相关内容</span>
    </div>
    <div @click="backTop" v-if="showBackTop" class="back-top">
      <i class="iconfont icon-20pxhuidaodingbu"></i>
    </div>
    <shoppingCart :popupState="popupState" :shoppingCareObj="shoppingCareObj" @confirm="confirm"></shoppingCart>
  </div>
</template>

<script>
  let vm;
  import common from '@/assets/js/common'
  import ScreenPanel from '@/components/screenPanel'
  import shoppingCart from '@/components/shoppingCart.vue';
  export default{
    name: 'productQuery',
    components: {
      ScreenPanel,
      shoppingCart
    },
    data(){
      return{
        keyWord: '',
        noData: false,
        screenPanelStatus: false,
        productList: [],
        showBackTop: false,
        screenKeyArr: [],
        allLoaded: false,
        total: 0,
        upData: {
          rows: 10,
          area: '',
          date: '',
          price: '',
          cruise: ''
        },
        page: 1,
        isRequest: true,
        isReset: false,
        isQuick : sessionStorage.getItem('isQuick') == 'true' ? true : false,
        quickData:[],
        popupState : false,
        shoppingCareObj : {},
      }
    },
    created(){
      window.addEventListener('scroll',this.scrolling,false);
    },
    beforeDestroy () {
      window.removeEventListener('scroll',this.scrolling,false);
    },
    mounted(){
      vm = this;
      if( vm.isQuick ){
        vm.quickOrderData();
      }else {
        vm.getHotLProList();
      }
    },
    methods: {
      //头部搜索框
      startSearch(){
      	vm.page = 1;
      	if(vm.screenKeyArr){
          for(let i=0;i<vm.screenKeyArr.length;i++){
            vm.$set(vm.screenKeyArr,i,'');
          }
        }
      	let parame = {
      		page: vm.page,
          rows: 10,
          keyword: vm.keyWord
        }
        vm.axios.post(this.api + this.urlApi.queryVoyageListByKeyword,parame).then(function (data) {
        	if(data.status){
            let content = data.content;
            vm.total = content.total;
            vm.productList = content.rows;
          }
        })
      },
      getHotLProList(){
        vm.page = 1;
        let parame = {
          page: vm.page,
          rows: 10
        }
        vm.axios.post(this.api+this.urlApi.queryVoyageList,parame).then(function (data) {
        	if(data.status){
            let content = data.content;
            vm.total = content.total;
            vm.productList = content.rows;
          }
        })
      },
      //监听滚动时执行的方法
      scrolling(){
        vm.scrollTop = document.documentElement.scrollTop || window.pageYOffset || document.body.scrollTop;
        vm.scrollTop >= 300 ? vm.showBackTop = true : vm.showBackTop = false;
      },
      openScreenPanel(){
        vm.screenPanelStatus = true;
      },
      //筛选面板关闭回调
      closePanel(flag){
        vm.screenPanelStatus = flag;
        vm.isRequest = false;
      },
      getScreenKeyArr(data){
        vm.screenKeyArr = data;
      },
      //筛选面板点击搜索回调
      getScreenData(screenData){
      	vm.screenData = screenData;
        vm.keyWord = '';
        vm.sandScreen();
        vm.isRequest = false;
      },
      sandScreen(key){
        vm.page = 1;
        vm.upData.page = vm.page;
        for(let i=0;i<vm.screenData.length;i++){
          let obj = vm.screenData[i];
          if( key == obj.value ){
            if( obj.key == 'areaNameCn' ){
              vm.upData.area = '';
            }else if(obj.key == 'cruiseNameCn'){
              vm.upData.cruise = '';
            }else if(obj.key == 'dateMark') {
              vm.upData.date = '';
            }else if(obj.key == 'basePrice') {
              vm.upData.price = '';
            }
          }else if(key && key.indexOf('-')>-1){
            let strArr = key.split('-');
            strArr[1].length==2 ? vm.upData.date = '' : vm.upData.price = '';
          }else if(!key) {
            switch (obj.key)
            {
              case 'areaNameCn':
                vm.upData.area = obj.id;
                break;
              case 'dateMark':
                vm.upData.date = obj.value;
                break;
              case 'basePrice':
                let price = obj.value >= 0 && obj.value < 3000 ? '0,2999' : obj.value >= 3000 && obj.value <= 5000 ? '3000,5000' : '5001,50000'
                vm.upData.price = price;
                break;
              case 'cruiseNameCn':
                vm.upData.cruise = obj.id;
                break;
              default:
                break;
            }
          }
        }
        vm.axios.post(this.api + this.urlApi.getQueryData,vm.upData).then(function (data) {
          if(data.status){
            let content = data.content;
            vm.total = content.total;
            vm.productList = content.rows;
          }
        })
      },
      //删除关键词
      deleteKey(index,key){
        vm.$set(vm.screenKeyArr,index,'');
        let onOff = false;
        for(let i=0;i<vm.screenKeyArr.length;i++){
          if(vm.screenKeyArr[i]){
            onOff = true;
            break;
          }
        }
        if(onOff){
          vm.sandScreen(key);
        }else {
          vm.getHotLProList();
        }
      },
      //返回顶部
      backTop(){
        vm.showBackTop = false;
      	let timer = setInterval(function () {
          vm.scrollTop -= 100;
          if(vm.scrollTop <= 0){
            vm.scrollTop = 0;
            clearInterval(timer);
          }
          if(document.documentElement.scrollTop){
            document.documentElement.scrollTop = vm.scrollTop;
          }else if(document.body.scrollTop){
            document.body.scrollTop = vm.scrollTop;
          }
        },13)
      },
      loadBottom() {
        if( vm.screenKeyArr.length ){
          if( vm.page * 10 < vm.total ){
            vm.page++;
            vm.upData.page = vm.page;
            vm.axios.post(this.api + this.urlApi.getQueryData,vm.upData).then(function (data) {
            	if(data.status){
                let content = data.content;
                vm.productList = vm.productList.concat(content.rows);
              }
            })
          }else {
            vm.allLoaded = true;
          }
        }else if(vm.keyWord){
          if( vm.page * 10 < vm.total ){
            vm.page++;
            let parame = {
              page: vm.page,
              rows: 10,
              keyword: vm.keyWord
            }
            vm.axios.post(this.api + this.urlApi.queryVoyageListByKeyword,parame).then(function (data) {
              if(data.status){
                let content = data.content;
                vm.total = content.total;
                vm.productList = vm.productList.concat(content.rows);
              }
            })
          }else {
            vm.allLoaded = true;
          }
        }else {
          //页面默认产品列表
          if( vm.page * 10 < vm.total ){
            vm.page++;
            let parame = {
              page: vm.page,
              rows: 10
            }
            vm.axios.post(this.api+this.urlApi.queryVoyageList,parame).then(function (data) {
              if(data.status){
                let content = data.content;
                vm.total = content.total;
                vm.productList = vm.productList.concat(content.rows);
              }
            })
          }else {
            vm.allLoaded = true;
          }
        }
        this.$refs.loadmore.onBottomLoaded();
      },
      //跳转邮轮详情
      goProductDetail(numberCode){
        sessionStorage.setItem('numberCode',numberCode);
        vm.$router.push('/productDetail');
      },
      //跳转个人中心
      goUserCenter() {
        vm.$router.push('/userCenter');
      },
      //快捷订单数据
      quickOrderData(){
        vm.page = 1;
        let parame = {
          page: vm.page,
          rows: 10,
          possessor : sessionStorage.possessor
        }
        vm.axios.post(this.api+this.urlApi.queryVoyageAndStockList,parame).then(function (data) {
          if(data.status){
              vm.quickData = [];
              for(let i=0; i<data.content.length;i++){
                  let json = data.content[i];
                  let obj = {};
                  let list = [];
                  obj.numberCode = json .numberCode;
                  obj.name = json.name;
                  obj.cruise = json.cruiseNameCn;
                  obj.date = json.date;
                  obj.cabinPriceList = json.mapList;
                  obj.image = json.image;
                  list = [
                    {title:'内双',content:json.I2,type:'I2'},
                    {title:'内三',content:json.I3,type:'I3'},
                    {title:'内四',content:json.I4,type:'I4'},
                    {title:'海双',content:json.S2,type:'S2'},
                    {title:'海三',content:json.S3,type:'S3'},
                    {title:'海四',content:json.S4,type:'S4'},
                    {title:'阳双',content:json.B2,type:'B2'},
                    {title:'阳三',content:json.B3,type:'B3'},
                    {title:'阳四',content:json.B4,type:'B4'},
                    {title:'套双',content:json.T2,type:'T2'},
                    {title:'套三',content:json.T3,type:'T3'},
                    {title:'套四',content:json.T4,type:'T4'},
                  ];
                  obj.list = list;
                  vm.quickData.push(obj);
              }
          }
        })
      },
      confirm(falg){
        vm.popupState = falg;
      },
      joinShoppingCart(obj,cur){
        if(!cur.content){
      		return;
        }
        let json = {};
        for(let i=0;i<obj.cabinPriceList.length;i++){
            if(obj.cabinPriceList[i].type == cur.type){
              json = obj.cabinPriceList[i];
              break;
            }
        }
        json.cruiseName = obj.name;
        json.cruiseNumberCode = obj.numberCode;
        json.cruise = obj.cruise;
        let date = obj.date.split(' ')[0];
        json.date = date.split('-')[1] + '/' + date.split('-')[2];
        json.cruiseDate = obj.date;
        vm.$set(json, 'counter', 0);
        let img = obj.image ? obj.image : require('../assets/img/cabinImg.png');
        vm.$set(json, 'cabinImg', img);
        if(json.validCount){
          let realCount = Math.min(json.validCount,json.stockCount);
          vm.$set(json, 'realCount', realCount);
          vm.$set(json, 'holdCount', realCount);
          if(json.maxCount > realCount){
            vm.$set(json, 'maxCount', realCount);
          }
        }else {
          vm.$set(json, 'realCount', json.stockCount);
          vm.$set(json, 'holdCount', json.stockCount);
        }
        vm.shoppingCareObj = json;
        vm.popupState = true;
      },
      cruiseDetail(obj){
        sessionStorage.numberCode = obj.numberCode;
        vm.$router.push('/productDetail');
      }
    },
    watch: {
      screenKeyArr(newVal){
      	let onOff = false;
        for(let i=0;i<newVal.length;i++){
          if(newVal[i]){
            onOff = true;
            break;
          }
        }
        vm.isReset = onOff;
        let fs = document.getElementsByTagName('html')[0].style.fontSize.split('px')[0];
        let headerHeight = vm.$refs['header'].offsetHeight;
        let searchBtnHeight = vm.$refs['searchBtn'].offsetHeight;
        let productList = vm.$refs['productList'];
        if(vm.isReset){
          vm.$nextTick(function () {
            let screenKeyWrapHeight = vm.$refs['screenKeyWrap'].offsetHeight;
            let paddingTop = (headerHeight + searchBtnHeight + screenKeyWrapHeight) / fs;
            productList.style.paddingTop = paddingTop + 'rem';
          })
        }else {
          vm.$nextTick(function () {
            let paddingTop = (headerHeight + searchBtnHeight) / fs;
            productList.style.paddingTop = paddingTop + 'rem';
          })
        }
      },
      productList(newVal){
      	newVal.length == 0 ? vm.noData = true : vm.noData = false;
        if( newVal.length >= 10 ){
          vm.allLoaded = false;
        }
      },
      isQuick(newVal){
      	if( newVal ){
      		vm.quickOrderData();
        }else {
          vm.getHotLProList();
        }
        sessionStorage.setItem('isQuick',newVal);
      }
    }
  }
</script>

<style lang="less" scoped>
  @import "../assets/css/base.less";
  .productQuery{
    width: 100%;
    height: 100%;
    .search-herder{
      .search-input{
        width: 592*@basePX;
        height: 64*@basePX;
        line-height: 64*@basePX;
        background-color: #fff;
        border-radius: 32*@basePX;
        padding-left: 25*@basePX;
        position: absolute;
        top: 50%;
        left: 22*@basePX;
        transform: translateY(-50%);
        overflow: hidden;
        input{
          width: 460*@basePX;
          height: 64*@basePX;
          border:0;
          outline: none;
          font-size: 32*@basePX;
          padding-left: 18*@basePX;
        }
        div{
          width: 90*@basePX;
          height: 64*@basePX;
          border-radius: 0 32*@basePX 32*@basePX 0;
          float: right;
          padding-right: 22*@basePX;
          .icon-sousuo1{
            color: #b3b3b3;
            font-size: 38*@basePX;
          }
        }
      }
    }
    .condition-search{
      width: 100%;
      height: 72*@basePX;
      text-align: center;
      background-color: #dee9f8;
      position: fixed;
      left: 0;
      top: 88*@basePX;
      z-index: 10;
      .title{
        line-height: 72*@basePX;
        color: #333;
        font-size: 32*@basePX;
        .icon-screen_icon{
          margin-right: 10*@basePX;
          font-size: 32*@basePX;
        }
        .change-type{
          float: right;
          padding-right: 22*@basePX;
          color: @changeBtnBg;
        }
      }
      .screen-key-wrap{
        width: 100%;
        padding: 3*@basePX 22*@basePX 6*@basePX;
        margin-top: -1px;
        border-top: 1px solid @lineColor;
        border-bottom: 1px solid @lineColor;
        background: #fff;
        .key-word{
          height: auto;
          li{
            width: auto;
            height: 36*@basePX;
            line-height: 36*@basePX;
            background: #dee9f8;
            margin: 6*@basePX 10*@basePX 3*@basePX 0;
            float: left;
            color: #333;
            .borderRadius;
            font-size: 12px;
            padding: 0 6*@basePX;
            .icon-cha{
              font-size: 12px;
            }
          }
        }
      }
      .animated {
        animation-duration: .5s;
        animation-fill-mode: both;
      }
      @keyframes slideInRight {
        from {
          transform: translate3d(100%, 0, 0);
        }
        to {
          visibility: visible;
          transform: translate3d(0, 0, 0);
        }
      }
      .slideInRight {
        animation-name: slideInRight;
      }
    }
    .content-display{
      width: 100%;
      height: auto;
      padding: 160*@basePX 22*@basePX 25*@basePX;
      overflow-x: auto;
      background-color: @appBg;
      transition: all .5s;
      .single-content{
        margin-top: 25*@basePX;
        background-color: #fff;
        border-radius: 8*@basePX;
        padding: 20*@basePX 24*@basePX 30*@basePX 0;
        p{
          font-size: 32*@basePX;
          color:#4c4c4c;
          padding-left: 8*@basePX;
        }
        >img{
          width: 298*@basePX;
          height: 206*@basePX;
          display: inline-block;
          margin: 20*@basePX 25*@basePX 0 24*@basePX;
          .borderRadius;
        }
        .basic-info{
          width: 330*@basePX;
          float: right;
          .money-display{
            font-size: 38*@basePX;
            color:#ff9b0b;
            margin-left: -8*@basePX;
            margin-top: 20*@basePX;
            span{
              font-size: 22*@basePX;
            }
            span{
              color: @subTextColor;
              padding-left: 10*@basePX;
              text-decoration: line-through;
            }
          }
          .time-display{
            height: 28*@basePX;
            font-size: 26*@basePX;
            color:#8d95a3;
            margin-top: 16*@basePX;
            span,i{
              display: inline-block;
              vertical-align: middle;
            }
            .icon-date{
              font-size: 28*@basePX;
              margin-right: 12*@basePX;
            }
            .icon-chengshi{
              width: 32*@basePX;
              height: 34*@basePX;
              font-size: 28*@basePX;
              margin: -10*@basePX 8*@basePX 0 -2*@basePX;
            }
          }
          .cruise-display{
            margin-top: 10*@basePX;
            .icon-youlunyou{
              font-size: 38*@basePX;
              margin-right: 7*@basePX;
              margin-left: -5*@basePX;
            }
          }
        }
      }
      .fastBooking{
        height: 100%;
        background-color: #f2f2f2;
        .cruise{
          background-color: #fff;
          border-radius: 8*@basePX;
          padding: 0 12*@basePX;
          margin-top: 16*@basePX;
          .title{
            padding: 20*@basePX 0;
            border-bottom: 1px solid #c7d2e0;
            font-size: 28*@basePX;
            color:#4c4c4c;
            .titleContent{
              float: left;
              max-width: 550*@basePX;
              word-wrap : break-word;
            }
            .details{
              width: 82*@basePX;
              height: 34*@basePX;
              border-radius: 17*@basePX;
              float: right;
              background-color: @clickBtnBg;
              line-height: 34*@basePX;
              color:#fff;
              font-size: 24*@basePX;
              text-align: center;
              margin-right: 12*@basePX;
            }
          }
          ul{
            width: 100%;
            li{
              width: 105*@basePX;
              text-align: center;
              height: 116*@basePX;
              line-height: 58*@basePX;
              color:#8d95a3;
              font-size: 26*@basePX;
              float: left;
              .price{
                color:#275ea2;
              }
            }
            li:nth-of-type(3n){
              margin-right: 26*@basePX;
            }
          }
        }
      }
    }
    .footer-bottom{
      width: 100%;
      height: 2.688rem;
      background: #fff;
    }
    .blank-page{
      width: 100%;
      padding: 72*@basePX 22*@basePX 25*@basePX;
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
    .back-top{
      width: 92*@basePX;
      height: 92*@basePX;
      .borderRadius(50%);
      background: #a6cefe;
      position: fixed;
      right: 55*@basePX;
      bottom: 158*@basePX;
      z-index: 18;
      color: #fff;
      text-align: center;
      .icon-20pxhuidaodingbu{
        display: block;
        font-size: 48*@basePX;
      }
      .icon-20pxhuidaodingbu:after{
        content:"TOP";
        clear:both;
        display:block;
        font-size: 22*@basePX;
        margin-top: -16*@basePX;
      }
    }
  }
</style>
