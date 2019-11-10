<template>
  <div id="moneyAmount" @click="eliminate">
    <Mheader isHeader="0" currentPage="3"></Mheader>
    <div class="main">
      <div class="account-balance common-div">
        <div class="balance-num">账户余额<span>￥{{content.sumPrice}}</span></div>
        <ul>
          <li><div></div>基础账户<span>￥{{content.normal}}</span></li>
          <li><div></div>授信账户<span>￥{{content.credit}}</span></li>
          <li><div></div>返利账户<span>￥{{content.rebate}}</span></li>
        </ul>
      </div>
      <div class="order-num common-div">
        <div>{{content.orderCount}}</div>
        <div>总订单数<span>单</span></div>
      </div>
      <div class="order-num common-div">
        <div>{{content.cabinCount}}</div>
        <div>总舱房<span>间</span></div>
      </div>
      <div class="order-num common-div">
        <div>￥{{content.orderPrice}}</div>
        <div>总订单金额<span>元</span></div>
      </div>
      <div class="date-select clearfloat">
        <div v-for="item in dateList" class="select" >
          <div @click="clickDisplay(item)">
            <span>{{item.value}}</span>
            <i class="icon-box">
              <svg class="icon" aria-hidden="true">
                <use xlink:href="#icon-xiala1"></use>
              </svg>
            </i>
          </div>
          <ul class="list clearfloat" v-if="item.isTrue">
            <li @click="getValue(item,num)" v-for="num in item.list" class="item">
              {{num}}
            </li>
          </ul>
        </div>
      </div>
      <div class="chart-wrap"></div>
    </div>
    <Mfooter></Mfooter>
  </div>
</template>

<script>
  import Mheader from "@/components/header.vue";
  import Mfooter from "@/components/footer.vue";
  let echarts = require('echarts');
  let vm;
  export default{
  	name: 'moneyAmount',
    components:{
      Mheader,
      Mfooter
    },
    data(){
  		return{
  			dateList: [
          {
            list:  ['2018'],
            value: new Date().getFullYear(),
            isTrue : false,
          },
          {
            list:  ['01','02','03','04','05','06','07','08','09','10','11','12'],
            value: new Date().getMonth() + 1 < 10 ? '0' + (new Date().getMonth() + 1) : new Date().getMonth() + 1,
            isTrue : false,
          }
        ],
        content : {},
        list : [],
      }
    },
    mounted(){
      vm = this;
      vm.userCode = sessionStorage.userCode;
      vm.axios.post(this.api + this.urlApi.getDistributorAccountListByNumberCode).then((data) => {
        if(data.status) {
          vm.content = data.content;
        }
      });
      vm.getDistributorAccountListByaddDate();
      vm.myChart = echarts.init(document.querySelector('.chart-wrap'));
    },
    methods: {
    	change(year,month){
        let base = + new Date(year, month-1, 0);
        let oneDay = 24 * 3600 * 1000;
        let data = [];
        let daylength = new Date(year, month, 0).getDate();
        for (let i = 0; i < daylength; i++) {
          let now = new Date(base += oneDay);
          let dayStr = [now.getFullYear(), now.getMonth()+1, now.getDate()].join('-');
          let value = 0;
          for(let j=0;j<vm.list.length;j++){
            if(now.getDate() == vm.list[j].addDate.substring(0,10).split('-')[2]){
              value += vm.list[j].amount;
            }
          }
          data.push([dayStr, value]);
        }
        let option = {
          animation: true,
          tooltip: {
            show: true,
          },
          xAxis: {
            type: 'time',
            axisPointer: {
              value: year+'-'+month+'-01',
              snap: true,
              lineStyle: {
                color: '#004E52',
                opacity: 0.5,
                width: 2
              },
              label: {
                show: true,
                formatter: function (params) {
                  return echarts.format.formatTime('yyyy-MM-dd', params.value);
                },
                backgroundColor: 'red'
              },
              handle: {
                show: true,
                color: '#004E52'
              }
            },
            z: 10,
            splitLine: {
              show: true
            },
            splitArea : {//保留网格区域
              show : true,
              areaStyle: ['#fff','#fff']
            },
            splitNumber: 15,
            minInterval: 3600 * 24 * 1000,
            maxInterval: 3600 * 48 * 1000
          },
          yAxis: {
            type: 'value',
            axisTick: {
              inside: true
            },
            splitLine: {
              show: true
            },
            splitArea : {//保留网格区域
              show : true,
              areaStyle: ['#fff','#fff']
            },
            axisLabel: {
              inside: true,
              formatter: '{value}\n'
            },
            z: 10,
            splitNumber: 10,
            minInterval: 10,
            maxInterval: 10000
          },
          grid: {
            top: 65,
            left: 30,
            right: 30,
            height: 360,
          },
          dataZoom: [{
            type: 'inside',
            disabled: true,
            throttle: 10000
          }],
          series: [
            {
              name:'金额',
              type:'line',
              smooth:true,
              stack: 'a',
              symbol: 'circle',
              symbolSize: 5,
              sampling: 'average',
              itemStyle: {
                normal: {
                  color: '#6aadff'
                }
              },
              lineStyle: {
                normal: {
                  color: '#c9e0fc'
                }
              },
              areaStyle: {
                normal: {
                  color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                    offset: 0,
                    color: '#c9e0fc'
                  }, {
                    offset: 1,
                    color: '#8cbffe'
                  }])
                }
              },
              data: data,
              silent: true
            }
          ]
        };
        vm.myChart.setOption(option);
      },
      getValue(obj,val){
      	obj.value = val;
      	obj.isTrue = false;
      	vm.getDistributorAccountListByaddDate();
      },
      eliminate : function(){
        let myDiv = document.getElementsByClassName('date-select');
        try{
          if(myDiv[0].contains(window.event.srcElement)){
          }else {
            for(let num in vm.dateList){
                vm.dateList[num].isTrue = false;
            }
          }
        }catch (e){}
      },//消除下拉框
      getDistributorAccountListByaddDate : function(){
        let userCode = vm.userCode;
        let addDate = vm.dateList[0].value + '-' + vm.dateList[1].value;
        vm.axios.post(this.api + this.urlApi.getDistributorAccountListByaddDate,{
          UserCode: userCode,
          addDate : addDate
        }).then((data) => {
          if(data.status) {
            vm.list = data.content;
            vm.sum = 0;
            for (let j = 0; j < vm.list.length; j++) {
              if (vm.sum < vm.list[j].amount) vm.sum = vm.list[j].amount;
            }
            let year = vm.dateList[0].value;
            let month = vm.dateList[1].value;
            vm.change(year, month);
          }
        });
      },
      clickDisplay : function(item){
          let isTrue = !item.isTrue;
          for(let num in vm.dateList){
              vm.dateList[num].isTrue = false;
          }
          item.isTrue = isTrue;
      }
    }
  }
</script>

<style lang="less" scoped>
  @import "../assets/css/common.less";
  #moneyAmount{
    background-color: @appBg;
    .main{
      width: 1200px;
      min-height: 750px;
      margin: 40px auto;
      padding: 20px 57px;
      .borderRadius;
      background-color: #fff;
      position: relative;
      .account-balance{
        width: 425px;
        .balance-num{
          height: 44px;
          border-bottom: 1px solid #e6e6e6;
          font-size: 14px;
          color:#999999;
          padding-left: 10px;
          span{
            font-size: 30px;
            color:#ff9b0b;
            margin-left: 20px;
            font-weight: bold;
          }
        }
        ul{
          li{
            width: 137px;
            margin-top: 10px;
            font-size: 12px;
            color:#999999;
            div{
              width: 4px;
              height: 4px;
              border-radius: 50%;
              background-color: #6aadff;
              float: left;
              margin-top: 8px;
              margin-right: 6px;
            }
            span{
              margin-left: 10px;
              font-size: 12px;
              color:#333333;
            }
          }
          li:last-child{
            width: 130px;
            float: right;
            span{
              margin-left: 9px;
            }
          }
        }
      }
      .order-num{
        width: 200px;
        margin-left: 20px;
        div:nth-of-type(1){
          height: 55px;
          line-height: 55px;
          font-size: 26px;
          color:#6aadff;
          padding-left: 10px;
          border-bottom: 1px solid #e6e6e6;
        }
        div:nth-of-type(2){
          padding: 5px 10px 0 10px;
          font-size: 14px;
          color:#999999;
          span{
            float: right;
          }
        }
      }
      .common-div{
        height: 100px;
        border-radius: 8px;
        background-color: #fff;
        box-shadow: 0 0 7px rgba(34,54,79,.15);
        padding: 10px;
        float: left;
      }
      .date-select{
        width: 240px;
        height: 34px;
        line-height: 34px;
        position: absolute;
        top: 169px;
        right: 187px;
        .select{
          width: 110px;
          height: 100%;
          .borderRadius(34px);
          background-color: #fafafa;
          color: @changeBtnBg;
          font-size: 16px;
          float: left;
          text-align: center;
          cursor: pointer;
          position: relative;
          span:after{
            content: ' ';
            height: 100%;
            border-right: 1px solid @lineColor;
            padding-left: 4px;
            margin-right: 10px;
          }
          &:first-child{
            margin-right: 20px;
          }
          .list{
            width: 96%;
            height: 150px;
            max-height: 150px;
            overflow-y: auto;
            overflow-x: hidden;
            background-color: #fff;
            border: 1px solid @lineColor;
            position: absolute;
            left: 2%;
            top: 36px;
            .borderRadius;
            z-index: 99999;
            .item{
              width: 100%;
              height: 26px;
              line-height: 26px;
              border-bottom: 1px solid @lineColor;
              &:last-child{
                border-bottom: none;
              }
              &:hover{
                background-color: #fafafa;
              }
            }
          }
        }
      }
      .chart-wrap{
        width: 1108px;
        height: 500px;
        position: absolute;
        left: 50%;
        bottom: 40px;
        transform: translateX(-50%);
      }
    }
  }
</style>
