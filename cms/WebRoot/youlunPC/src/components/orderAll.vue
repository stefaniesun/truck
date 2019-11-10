<template>
  <div id="orderAll" :onmousewheel="'return ' + !isPayment">
    <Mheader isHeader="0" currentPage="2"></Mheader>
    <div class="main">
      <div class="main-top clearfloat">
        <div class="search">
          <input v-model="orderKey" type="text" placeholder="请输入订单号">
          <div @click="queryKeyWord" class="btn">
            搜索
          </div>
        </div>
        <div class="date-select">
          <DatePicker
            v-model="startDate"
            :options="dateOptions"
            type="date"
            :editable="false"
            placeholder="点击选择开始日期">
          </DatePicker>
          <span>~</span>
          <DatePicker
            v-model="endDate"
            :options="dateOptions"
            type="date"
            placement="bottom-end"
            :editable="false"
            placeholder="点击选择结束日期">
          </DatePicker>
          <div @click="getDateSection" class="date-btn">
            确定
          </div>
        </div>
      </div>
      <div class="clearfloat"></div>
      <div v-if="orderList.length >0" class="content">
        <ul class="nav">
          <li class="width-1">订单信息</li>
          <li class="width-2" style="max-width: 76px">订单类型</li>
          <li class="width-2">出发日期</li>
          <li class="width-3">舱型+份数</li>
          <li class="width-4">状态</li>
          <li class="width-5">金额</li>
          <li class="msg-title">消息</li>
        </ul>
        <ul class="list clearfloat">
          <li v-for="(item,index) in orderList" class="item">
            <div class="top">
              <span>
                订单号：{{item.numberCode}}
              </span>
              <span class="user-info">
                <span class="name">联系人：{{item.LinkMan}}</span>
                <span class="phone"> 联系电话：{{item.LinkPhone}}</span>
                 <svg @click="changeLinkMan(item,index)" v-if="item.resourceStatus !== 'submit' && item.resourceStatus !== 'cancel'" class="icon" aria-hidden="true">
                  <use xlink:href="#icon-web-icon-"></use>
                </svg>
              </span>
              <span class="order-date">
                <span v-if="item.type == 'SHARE'">下单人微信：{{}}</span>
                下单时间：{{item.addDate.replace(/-/g,'/')}}
              </span>
            </div>
            <div ref="orderDeteil" class="detail">
              <ul class="detail-wrap">
                <li @click="jumpDetail(item.voyage)" title="点击可跳转到对应的详情" class="width-1">
                  <img class="pic" :src="item.image" alt="">
                  <span class="name">{{item.title}}</span>
                </li>
                <li class="width-2" style="max-width: 76px">
                  <div class="order-type">
                    <span :style="{background: item.type == 'DISTRIBUTOR'?'#ff9b0b':'#6aadff'}">{{item.type == 'SHARE'?'分享下单':item.type == 'DISTRIBUTOR'?'线上订单':item.type == 'PLATFORM'?'线下订单':'C端下单'}}</span>
                    <div v-if="item.type == 'SHARE'" class="clearfloat">
                      <span @click="changeType" class="tips-btn">确定</span>
                    </div>
                  </div>
                </li>
                <li class="width-2">
                  <span>{{item.date.substring(0,10).replace(/-/g,'/')}}</span>
                </li>
                <li class="width-3">
                  <div ref="canbinName" class="cabin-name">
                    <span class="cabin-type" v-for="orderItem in item.orderList">
                      {{orderItem.cabinName+' *'}}
                      <span class="count">{{orderItem.count + '(间)'}}</span>
                      <span class="type" :style="{background: orderItem.stockType == 1?'#6aadff':'#ff9b0b'}"> {{orderItem.stockType == 1 ? '现询':'实库'}}</span>
                    </span>
                  </div>
                </li>
                <li class="width-4 btn-wrap">
                  <ul class="status-top clearfloat">
                    <li>出行人状态</li>
                    <li>资源状态</li>
                    <li>资金状态</li>
                  </ul>
                  <ul class="status-list">
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
                          <span @click="setPerson(item)" class="tips-btn">编辑</span>
                        </div>
                      </div>
                    </li>
                  </ul>
                </li>
                <li :style="{'marginLeft':item.type !== 'SHARE'?'0px':'20px'}" class="width-5 price">
                  <div class="cabin-price">
                    <span class="salePrice">{{'&yen;' + item.amount}}</span>
                    <!--<span v-if="item.rebate" class="rebatePrice">{{'后返' + item.rebate + '/人'}}</span>-->
                  </div>
                </li>
                <li class="msg">
                  <div @click="showMsgPanel(item.numberCode,index)" :title="'订单留言'">
                    <svg class="icon">
                      <use xlink:href="#icon-beizhu"></use>
                    </svg>
                    <span v-if="item.msgCount">{{item.msgCount}}</span>
                  </div>
                </li>
              </ul>
            </div>
          </li>
        </ul>
      </div>
      <div v-if="orderList.length == 0" class="no-content">
        <img src="../assets/img/noContent.png" alt="">
        <div>暂时没有相关内容</div>
      </div>
    </div>
    <div v-if="orderList.length >0" class="page-wrap">
      <div>
        <Page :total="total" :page-size="3" @on-change="pageChange" className="paging-style"></Page>
      </div>
    </div>
    <Mfooter></Mfooter>
    <transition  name="custom-classes-transition"
                 enter-active-class=" animated slideShow"
                 leave-active-class="animated slideHide">
      <div v-if="isShowPanel" @click.self.stop="clsePanel" class="pael-wrap">
        <div class="panel">
          <div class="top">
            出行人信息
          </div>
          <div class="panel-main">
            <div class="item clearfloat" v-for="(item,index) in personInfo.cabinList">
              <div v-for="(pItem,pIndex) in item.personArr" style="padding-bottom: 10px">
                <p class="title">
                  {{item.cabinName}}（房间{{pIndex+1}}）
                </p>
                <div class="info clearfloat" v-for="(subItem,subIndex) in pItem">
                  <p class="info-top">
                    <svg class="icon">
                      <use xlink:href="#icon-zhanghao"></use>
                    </svg>
                    <span>出行人{{subIndex+1}}</span>
                  </p>
                  <div class="info-panel clearfloat">
                    <ul class="left">
                      <li class="input-panel">
                        <span>姓名</span>
                        <input v-model="subItem.name" placeholder="请输入出行人姓名" type="text">
                      </li>
                      <li class="input-panel">
                        <span>身份证号</span>
                        <input v-model="subItem.cardNumber" placeholder="请输入出行人身份证号" type="text">
                      </li>
                    </ul>
                    <ul class="right">
                      <li>
                        <span class="pic-type">身份证照片</span>
                        <div class="pic">
                          <xyz-uploader
                            :id="'pic_'+index+'_'+ pIndex+ '_' +subIndex+'_ID'"
                            :urls="subItem.cardImage?subItem.cardImage.split(','):[]"
                            :count="200"
                            :multiple="true"
                            :repeat="true"
                            @complete="complete"
                            @fail="fail"
                            @remove-item="removeItem"
                            @showImage="showImage">
                          </xyz-uploader>
                        </div>
                      </li>
                      <li>
                        <span class="pic-type">护照照片</span>
                        <div class="pic">
                          <xyz-uploader
                            :id="'pic_'+index+'_'+ pIndex+ '_' +subIndex+'_Pass'"
                            :urls="subItem.passportImage?subItem.passportImage.split(','):[]"
                            :count="2"
                            :multiple="true"
                            :repeat="true"
                            @complete="complete"
                            @fail="fail"
                            @remove-item="removeItem"
                            @showImage="showImage">
                          </xyz-uploader>
                        </div>
                      </li>
                    </ul>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <ul class="btn-wrap">
            <li @click="clsePanel" class="btn"><span>取消</span></li>
            <li @click="confirm" class="btn"><span>确定</span></li>
          </ul>
        </div>
      </div>
      <div v-if="isShowLinkManPanel" @click.self.stop="clsePanel" class="pael-wrap">
        <div class="link-man-panel">
          <div class="top">修改联系人信息</div>
          <ul class="content">
            <li>
              <span>姓名</span>
              <input v-model="linkMan" placeholder="请输入姓名" type="text">
            </li>
            <li>
              <span>手机号</span>
              <input v-model="linkPhone" placeholder="请输入手机号" type="text" class="link-phone">
            </li>
          </ul>
          <div class="btn-wrap">
            <div @click="clsePanel" class="btn"><span>取消</span></div>
            <div @click="confirmLinkMan" class="btn"><span>确认修改</span></div>
          </div>
        </div>
      </div>
      <Payment v-if="isPayment" :contentValue="contentValue" @confirm="personConfirm"></Payment>
      <chatPanel v-if="isShowMsg" @closeMsgPanel="getMsgPanelStatus" :order="order"></chatPanel>
    </transition>
  </div>
</template>

<script>
  import Mheader from "@/components/header.vue";
  import Mfooter from "@/components/footer.vue";
  import xyzUploader from "@/components/xyzUploader";
  import common from '@/assets/js/common';
  import Payment from "@/components/paymentPage.vue";
  import chatPanel from "@/components/chatPanel.vue";
  let vm
  export default{
  	name: 'orderAll',
    components:{
      Mheader,
      Mfooter,
      xyzUploader,
      Payment,
      chatPanel
    },
    data(){
  		return{
        total: 0,
        page: 1,
        orderList: new Array,
        orderKey: '',
        startDate: '',
        endDate: '',
        isDateSection: false,
        dateOptions: {
          disabledDate (date) {
            return date && date.valueOf() < Date.now() - 86400000;
          }
        },
        parame: {
          page: 1,
          rows: 3,
          queryParam: '',
          startDate: '',
          endDate: ''
        },
        statusArr:new Array,
        isShowPanel: false,
        isShowLinkManPanel: false,
        personInfo: new Object,
        linkMan: '',
        linkPhone: '',
        order: '',
        orderIndex: 0,
        isPayment:false,
        contentValue:{},
        isShowMsg: false
      }
    },
    mounted(){
      vm = this;
      vm.getData();
    },
    methods: {
    	getData(){
        vm.axios.post(this.api+this.urlApi.queryCruiseOrderList,vm.parame).then(function (respones) {
          if(respones.status){
          	let content = respones.content;
            vm.total = content.total;
            vm.orderList = content.rows;
            if(vm.orderList.length){
              vm.$nextTick(function(){
                let prev = document.querySelector('.ivu-page-prev');
                prev.innerHTML = '上一页';
                let next = document.querySelector('.ivu-page-next');
                next.innerHTML = '下一页';
              });
            }
            vm.setStatusArr();
          }
        })
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
      queryData(){
        vm.axios.post(this.api+this.urlApi.queryCruiseOrderList,vm.parame).then(function (respones) {
          if(respones.status){
            let content = respones.content;
            vm.total = content.total;
            vm.orderList = content.rows;
            vm.setStatusArr();
          }
        })
      },
      pageChange(page){
        vm.page = page;
        vm.parame.page = vm.page;
        vm.queryData();
      },
      queryKeyWord(){
        vm.page = 1;
        vm.parame.page = vm.page;
        vm.parame.queryParam = vm.orderKey;
        vm.queryData();
      },
      getDateSection(){
        let startDate,endDate;
        if( vm.startDate && vm.endDate && vm.startDate > vm.endDate ){
          vm.dialog.showDialog('开始日期不能大于结束日期');
          return
        }else if(!vm.startDate && !vm.endDate){
          if(vm.isDateSection){
            vm.isDateSection = false;
          }else {
            vm.dialog.showDialog('请先选择日期');
            return
          }
        }else {
          startDate = vm.startDate ? new Date(vm.startDate).Format("yyyy-MM-dd") : '';
          endDate = vm.endDate ? new Date(vm.endDate).Format("yyyy-MM-dd") : '';
          vm.isDateSection = true;
          vm.page = 1;
          vm.parame.page = vm.page;
          vm.parame.startDate = startDate;
          vm.parame.endDate = endDate;
          vm.queryData();
        }
      },
      jumpDetail(numberCode){
        sessionStorage.setItem('numberCode',numberCode);
        vm.$router.push('/productDetail');
      },
      changeType(){

      },
      payment(obj){
        vm.contentValue = obj;
        vm.isPayment = true;
        vm.bodyNoScrolling.open();
      },
      setPerson(obj){
        let personInfo = {};
        personInfo.order = obj.numberCode;
        personInfo.cabinList = new Array;
        vm.axios.post(this.api+this.urlApi.getPersonInfo,{order:obj.numberCode}).then(function (respones) {
          if(respones.status){
          	let personList = respones.content;
          	let cabinObjArr = new Array;
            personList.forEach(function (item,index) {
            	if(cabinObjArr.length){
            		let onOff = true;
                cabinObjArr.forEach(function(subItem,subIndex){
                	if(subItem.cabinName === item.cabinName){
                    onOff = false;
                    let personArr = subItem.personArr;
                    personArr.forEach(function (pItem,Pindex) {
                      if(pItem.length){
                      	let swicth = false;
                        pItem.forEach(function(spItem){
                        	if(spItem.cabinNum === item.cabinNum ){
                            swicth = true;
                          }
                        })
                        if(swicth){
                          pItem.push(item);
                        }else {
                          personArr[Pindex+1] = new Array;
                          personArr[Pindex+1].push(item);
                        }

                      }
                    })
                  }
                })
                if(onOff){
                  let cabinObj = {
                    cabinName: item.cabinName,
                    personArr: new Array
                  }
                  cabinObj.personArr[0] = new Array;
                  cabinObj.personArr[0].push(item);
                  cabinObjArr.push(cabinObj)
                }
              }else {
                let cabinObj = {
                  cabinName: item.cabinName,
                  personArr: new Array
                }
                cabinObj.personArr[0] = new Array;
                cabinObj.personArr[0].push(item);
                cabinObjArr.push(cabinObj)
              }
            })
            personInfo.cabinList = cabinObjArr;

            vm.personInfo = personInfo;
            vm.isShowPanel = true;
            vm.bodyNoScrolling.open();
          }
        })
      },
      clsePanel(){
        vm.isShowPanel = false;
        vm.isShowLinkManPanel = false;
        vm.bodyNoScrolling.close();
      },
      confirm(){
      	let personJson = new Array;
      	for(let i=0;i<vm.personInfo.cabinList.length;i++){
      		let personArr = vm.personInfo.cabinList[i];
          for(let j=0;j<personArr.personArr.length;j++){
            let Item = personArr.personArr[j];
            Item.forEach(function(item,index){
              personJson.push(item);
            })
          }
        }
        let parame = {
          order: vm.personInfo.order,
          personJson: JSON.stringify(personJson)
        }
        vm.axios.post(this.api+this.urlApi.updatePersonInfoOper,parame).then(function (respones) {
          if(respones.status){
            vm.isShowPanel = false;
            vm.bodyNoScrolling.close();
          }
        })
      },
      complete(id,obj){
        if(obj.status == 1){
          let cabinIndex = id.split('_')[1];
          let personIndex = id.split('_')[2];
          let Index = id.split('_')[3];
          let type = id.split('_')[4];
          let cabinObj = vm.personInfo.cabinList;
          for(let i=0;i<cabinObj.length;i++){
          	let personArr = vm.personInfo.cabinList[i];
            if (cabinIndex == i) {
              personArr.personArr.forEach(function (item, index) {
                if (personIndex == index) {
                  item.forEach(function (subItem, subIndex) {
                    if (Index == subIndex) {
                      let imgStr;
                      if (type == 'ID') {
                        imgStr = subItem.cardImage;
                        if (subItem.cardImage) {
                          subItem.cardImage += ',' + obj.content.url;
                        } else {
                          subItem.cardImage = obj.content.url;
                        }
                      } else {
                        imgStr = subItem.passportImage;
                        if (subItem.passportImage) {
                          subItem.passportImage += ',' + obj.content.url;
                        } else {
                          subItem.passportImage = obj.content.url;
                        }
                      }
                    }
                  })
                }
              })
            }
          }
        }else {
        	vm.dialog.showDialog('图片上传失败');
        }
      },
      fail(id,msg,fileItem){
        vm.dialog.showDialog(msg);
      },
      removeItem(id,url){
        let cabinIndex = id.split('_')[1];
        let personIndex = id.split('_')[2];
        let Index = id.split('_')[3];
        let type = id.split('_')[4];
        let cabinObj = vm.personInfo.cabinList;
        let imageArr
        if(type == 'ID'){
          imageArr = cabinObj[cabinIndex].personArr[personIndex][Index].cardImage.split(',');
        }else {
          imageArr = cabinObj[cabinIndex].personArr[personIndex][Index].passportImage.split(',');
        }
        for(var i=0;i<imageArr.length;i++){
          if(imageArr[i] == url){
            imageArr.splice(i,1);
          }
        }
        if(type == 'ID'){
          cabinObj[cabinIndex].personArr[personIndex][Index].cardImage = imageArr.join(',');
        }else {
          cabinObj[cabinIndex].personArr[personIndex][Index].passportImage = imageArr.join(',');
        }
      },
      showImage(url){
        console.log(url)
      },
      changeLinkMan(obj,index){
        vm.order = obj.numberCode;
      	vm.orderIndex = index;
        vm.linkMan = obj.LinkMan;
        vm.linkPhone = obj.LinkPhone;
        vm.isShowLinkManPanel = true;
        vm.bodyNoScrolling.open();
      },
      confirmLinkMan(){
        if(!common.xyzIsPhone(vm.linkPhone)){
            vm.tooltip.show('手机号格式错误!','.link-phone');
          return;
        }
      	let parame = {
      		order: vm.order,
          username: vm.linkMan,
          phone: vm.linkPhone
        }
        vm.axios.post(this.api+this.urlApi.editOrderLinkInfo,parame).then(function (respones) {
          if(respones.status){
          	vm.orderList[vm.orderIndex].LinkMan = vm.linkMan;
            vm.orderList[vm.orderIndex].LinkPhone = vm.linkPhone;
            vm.isShowLinkManPanel = false;
            vm.bodyNoScrolling.close();
          }
        })
      },
      personConfirm(flag){
        if(flag){
          vm.axios.post(this.api+this.urlApi.orderPayOper,{order:vm.contentValue.numberCode}).then(function (respones) {
              if(respones.status){
                vm.isPayment = false;
                vm.bodyNoScrolling.close();
                location.reload();
              }
            })
        }else {
          vm.isPayment = false;
        }
      },
      showMsgPanel(numberCode,index){
      	vm.isShowMsg = true;
      	vm.order = numberCode;
      	vm.orderIndex = index;
        vm.bodyNoScrolling.open();
      },
      getMsgPanelStatus(flag){
        if(flag){
          vm.isShowMsg = false;
          vm.orderList[vm.orderIndex].msgCount = 0;
          vm.bodyNoScrolling.close();
        }
      }
    },
    watch: {
      orderList(newVal){
      	if( newVal.length ){
          vm.$nextTick(function () {
            let canbinName = vm.$refs['canbinName'];
            let orderDeteil = vm.$refs['orderDeteil'];
            canbinName.forEach(function (item,index) {
              let height = item.offsetHeight;
              if( height > 109 ){
                orderDeteil[index].style.height = height;
              }
            })
          })
        }
      }
    }
  }
</script>

<style lang="less" scoped>
  @import "../assets/css/common.less";
  #orderAll{
    background-color: @appBg;
    .main{
      width: 1200px;
      min-height: 700px;
      margin: 40px auto;
      padding: 18px 19px;
      .borderRadius;
      background-color: #fff;
      position: relative;
    }
    .main-top{
      width: 100%;
      height: 26px;
      position: relative;
      .search{
        width: 320px;
        height: 26px;
        line-height: 26px;
        .borderRadius(26px);
        overflow: hidden;
        position: absolute;
        right: 0;
        input{
          width: 240px;
          height: 26px;
          text-indent: 16px;
          float: left;
          font-size: 14px;
          border: 1px solid @lineColor;
          outline: none;
          transition: all .3s;
          border-bottom-left-radius: 26px;
          border-top-left-radius: 26px;
          &:focus{
            border-color: @changeBtnBg;
          }
        }
        .btn{
          width: 80px;
          height: 100%;
          background-color: @changeBtnBg;
          color: #fff;
          text-align: center;
          cursor: pointer;
          float: left;
        }
      }
      .date-select{
        width: 416px;
        height: 100%;
        position: absolute;
        right: 350px;
        top: 0;
        .date-btn{
          width: 50px;
          height: 26px;
          line-height: 26px;
          text-align: center;
          float: right;
          border-radius: 4px;
          background: #ff9b0b;
          color: #fff;
          font-size: 14px;
          cursor: pointer;
        }
      }
    }
    .content{
      margin: 20px auto 10px;
      .nav{
        width: 100%;
        height: 42px;
        line-height: 42px;
        text-align: center;
        border: 1px solid @lineColor;
        background-color: #fafafb;
        margin-bottom: 10px;
        .msg-title{
          margin-left: 5px;
        }
        li{
          color: #333;
        }
      }
      .list{
        .item{
          width: 100%;
          margin-bottom: 20px;
          border: 1px solid @lineColor;
          .top{
            width: 100%;
            height: 40px;
            line-height: 40px;
            padding: 0 22px;
            background-color: #fafafb;
            border-bottom: 1px solid @lineColor;
            letter-spacing: 1px;
            .user-info{
              padding-left: 130px;
              .name{
                display: inline-block;
                min-width: 115px;
              }
              .icon{
                color: @changeBtnBg;
                font-size: 22px;
                cursor: pointer;
                transform: translateY(4px);
              }
            }
            .order-date{
              float: right;
              span{
                padding-right: 28px;
              }
            }
          }
          .detail{
            width: 100%;
            height: 109px;
            line-height: 109px;
            min-height: 109px;
            text-align: center;
            font-size: 14px;
            position: relative;
            .detail-wrap{
              width: 100%;
              height: 100%;
              >li{
                height: 100%;
                border-right: 1px solid @lineColor;
                position: relative;
                span{
                  word-wrap: break-word;
                }
              }
              >li:first-child{
                cursor: pointer;
                overflow: hidden;
                &:hover{
                  color: #6aadff;
                }
              }
              >li:last-child{
                border-right: none;
              }
              .order-type{
                width: 100%;
                text-align: center;
                position: absolute;
                left: 0;
                top: 50%;
                transform: translateY(-50%);
                line-height: 24px;
                span{
                  display: inline-block;
                  height: 20px;
                  line-height: 20px;
                  border-radius: 4px;
                  -webkit-border-radius: 4px;
                  -moz-border-radius: 4px;
                  color: #fff;
                  text-align: center;
                  margin: 4px 0;
                  padding: 0 4px;
                }
                .tips-btn{
                  background: @changeBtnBg;
                  display: block;
                  width: 70px;
                  height: 24px;
                  line-height: 24px;
                  margin: 4px auto 0;
                  .borderRadius(26px);
                  cursor: pointer;
                  color: #fff;
                }
              }
              li.price{
                font-size: 24px;
                position: relative;
                .cabin-price{
                  width: 100%;
                  line-height: 32px;
                  text-align: center;
                  position: absolute;
                  left: 50%;
                  top: 50%;
                  transform: translate(-50%,-50%);
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
              li.msg{
                width: 36px;
                height: 100%;
                color: @changeBtnBg;
                font-size: 22px;
                position: relative;
                div{
                  position: absolute;
                  left: 50%;
                  top: 50%;
                  transform: translate(-50%,-50%);
                  height: 22px;
                  line-height: 22px;
                  .icon{
                    cursor: pointer;
                  }
                  span{
                    width: 18px;
                    height: 18px;
                    line-height: 18px;
                    text-align: center;
                    background-color: red;
                    color: #fff;
                    .borderRadius(50%);
                    display: block;
                    position: absolute;
                    right: -7px;
                    top: -10px;
                    font-size: 12px;
                    cursor: pointer;
                  }
                }

              }
              li.btn-wrap{
                .status-top{
                  width: 100%;
                  li{
                    height: 28px;
                    line-height: 28px;
                    width: 108px;
                    background: #fafafb;
                    border-right: 1px solid #fff;
                    border-bottom: 1px solid @lineColor;
                    &:last-child {
                      width: 98px;
                      border-right: none;
                    }
                    &:first-child{
                      width: 98px;
                    }
                  }
                }
                .status-list{
                  width: 100%;
                  height: calc(~ '100% - 29px');
                  .status-item{
                    width: 108px;
                    height: 100%;
                    position: relative;
                    background-color: #eff4f7;
                    border-right: 1px solid #fff;
                    &:last-child {
                      width: 98px;
                      border-right: none;
                    }
                    &:first-child{
                      width: 98px;
                    }
                    .info{
                      width: 100%;
                      text-align: center;
                      color: @tipsColor;
                      position: absolute;
                      left: 0;
                      top: 50%;
                      transform: translateY(-50%);
                      line-height: 24px;
                      .tips{
                        display: block;
                        font-size: 12px;
                        color: #0ab012;
                        .pull-left{
                          padding-left: 6px;
                        }
                        .pull-right{
                          padding-right: 6px;
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
                        width: 70px;
                        height: 24px;
                        line-height: 24px;
                        margin: 4px auto 0;
                        .borderRadius(26px);
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
              .cabin-name{
                width: 100%;
                line-height: 20px;
                position: absolute;
                top: 50%;
                left: 50%;
                transform: translate(-50%,-50%);
                .cabin-type{
                  display: block;
                  span.count{
                    display: inline-block;
                    .borderRadius;
                    background-color: #fafafb;
                    padding: 2px 4px;
                  }
                  span.type{
                    display: inline-block;
                    width: 40px;
                    height: 20px;
                    line-height: 20px;
                    .borderRadius;
                    color: #fff;
                    text-align: center;
                    margin: 4px 0;
                  }
                }
              }
              .pic{
                width: 120px;
                height: 72px;
                display: block;
                .borderRadius;
                position: absolute;
                top: 50%;
                left: 10px;
                transform: translateY(-50%);
                z-index: 4;
              }
              .name{
                width: 256px;
                line-height: 32px;
                max-width: 256px;
                position: absolute;
                top: 50%;
                left: 140px;
                text-align: left;
                text-indent: -7px;
                transform: translateY(-50%);
                z-index: 4;
              }
            }
          }
        }
      }
    }
    li.width-1{
      width: 364px;
      max-width: 364px;
    }
    li.width-2{
      width: 92px;
      max-width: 92px;
    }
    li.width-3{
      width: 180px;
      max-width: 180px;
    }
    li.width-4{
      width: 305px;
      max-width: 305px;
    }
    li.width-5{
      width: 106px;
      max-width: 106px;
    }
    .page-wrap{
      width: 1200px;
      height: 50px;
      margin: -30px auto 40px;
      background-color: #fff;
      .borderRadius;
      position: relative;
      div{
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%,-50%);
      }
    }
    .pael-wrap{
      width: 100%;
      height: 100%;
      position: fixed;
      top: 0;
      left: 0;
      background: rgba(0,0,0,.3);
      z-index: 18;
      .panel{
        width: 1100px;
        height: 720px;
        position: absolute;
        left: 50%;
        top: 50%;
        transform: translate(-50%,-50%);
        overflow: hidden;
        background: #fff;
        .borderRadius;
        position: relative;
        .top{
          width: 100%;
          height: 40px;
          line-height: 40px;
          text-align: center;
          color: #fff;
          font-size: 18px;
          background: @changeBtnBg;
        }
        .panel-main{
          background-color: #fff;
          width: 100%;
          height: 584px;
          padding: 0 18px;
          overflow-x: hidden;
          overflow-y: auto;
          .item{
            width: 100%;
            padding-top: 16px;
            .title{
              width: 100%;
              color: @changeBtnBg;
              font-size: 18px;
              border-bottom: 1px solid #d2e6ff;
            }
            .info{
              width: 100%;
              height: 260px;
              padding: 18px 28px 0;
              .info-top{
                width: 100%;
                text-align: left;
                color: @changeBtnBg;
                font-size: 16px;
              }
              .info-panel{
                width: 100%;
                height: 200px;
                margin: 18px auto;
                background-color: #f0f7ff;
                padding: 20px 26px;
                .left,.right{
                  float: left;
                }
                .left{
                  width: 240px;
                  height: 100%;
                  li{
                    width: 100%;
                    height: 68px;
                    span{
                      color: #333;
                      font-size: 16px;
                    }
                    input{
                      width: 238px;
                      height: 40px;
                      padding: 7px;
                      line-height: 36px;
                      text-indent: 4px;
                      .borderRadius;
                      border: 1px solid @lineColor;
                      outline: none;
                      &:focus{
                        box-shadow: 0 0 7px rgba(36, 106, 191, 0.38);
                      }
                    }
                    &:first-child{
                      margin-bottom: 24px;
                    }
                  }
                }
                .right{
                  width: 694px;
                  height: 100%;
                  padding-left: 50px;
                  li{
                    width: 50%;
                    height: 100%;
                    float: left;
                    span{
                      color: #333;
                      font-size: 16px;
                    }
                    .pic{
                      max-width: 300px;
                    }
                  }
                }
              }
            }
          }
        }
        .btn-wrap{
          width: 320px;
          height: 40px;
          margin: 20px auto 0;
          .btn{
            float: left;
            width: 140px;
            height: 40px;
            line-height: 40px;
            text-align: center;
            background: @clickBtnBg;
            color: #fff;
            .borderRadius(50px);
            margin: 0 10px;
            font-size: 18px;
            cursor: pointer;
            span{
              letter-spacing: 14px;
              margin-left: 14px;
            }
            &:first-child{
              background-color: @changeBtnBg;
            }
          }
        }
      }
      .link-man-panel{
        width: 500px;
        height: 300px;
        position: absolute;
        left: 50%;
        top: 50%;
        transform: translate(-50%,-50%);
        overflow: hidden;
        background: #fff;
        .borderRadius;
        text-align: center;
        .top{
          text-align: center;
          width: 100%;
          height: 40px;
          line-height: 40px;
          color: #fff;
          font-size: 16px;
          background-color: @changeBtnBg;
        }
        .content{
          width: 345px;
          height: 110px;
          margin: 40px auto 33px;
          li{
            width: 100%;
            height: 38px;
            line-height: 38px;
            font-size: 14px;
            span{
              display: inline-block;
              min-width: 54px;
              text-align: right;
            }
            input{
              width: 280px;
              height: 100%;
              border: 1px solid @lineColor;
              outline: none;
              background-color: #fff;
              margin-left: 6px;
              text-indent: 8px;
              .borderRadius;
            }
            &:first-child{
              margin-bottom: 32px;
            }
          }
        }
        .btn-wrap{
          width: 360px;
          height: 48px;
          line-height: 48px;
          margin: 0 auto;
          text-align: center;
          .btn{
            color: #fff;
            width: 152px;
            height: 100%;
            .borderRadius(48px);
            cursor: pointer;
            font-size: 18px;
            span{
              letter-spacing: 6px;
              margin-left: 14px;
            }
            &:first-child{
              float: left;
              background-color: @changeBtnBg;
            }
            &:last-child{
              background-color: @clickBtnBg;
              float: right;
            }
          }
        }
      }
    }
    .animated {
      animation-duration: .3s;
      animation-fill-mode: both;
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
