<template>
  <div class="sumAmount">
    <div class="user-info">
      <div class="top">账户余额  &yen;<span>{{content.sumPrice}}</span></div>
      <ul class="account">
        <li>
          <div>基础账户</div>
          <div>&yen;<span>{{content.normal}}</span></div>
          <div></div>
        </li>
        <li>
          <div>授信账户</div>
          <div>&yen;<span>{{content.credit}}</span></div>
          <div></div>
        </li>
        <li>
          <div>返利账户</div>
          <div>&yen;<span>{{content.rebate}}</span></div>
        </li>
      </ul>
      <div class="clearfloat"></div>
      <ul class="count">
        <li>
          <div>{{content.orderCount}}</div>
          <div>总订单数(单)</div>
          <div></div>
        </li>
        <li>
          <div>{{content.cabinCount}}</div>
          <div>总舱房(间)</div>
          <div></div>
        </li>
        <li>
          <div>{{content.orderPrice}}</div>
          <div>总订单金额(元)</div>
        </li>
      </ul>
      <div class="clearfloat"></div>
    </div>
    <h2>近一月金额统计</h2>
    <div class="container">
      <div class="chart-wrap">

      </div>
    </div>
  </div>
</template>

<script>
  let vm;
  let echarts = require('echarts');
  export default {
    name: 'sumAmount',
    data() {
      return {
        content:{},
      }
    },
    mounted:function(){
      vm = this;
      vm.userCode = sessionStorage.userCode;
      vm.axios.post(this.api + this.urlApi.getDistributorAccountListByNumberCode,{
        UserCode: vm.userCode
      }).then((data) => {
        if(data.status) {
          vm.content = data.content;
        }
      });
      vm.getDistributorAccountListByaddDate();
      vm.myChart = echarts.init(document.querySelector('.chart-wrap'));
    },
    methods: {
      getDistributorAccountListByaddDate : function(){
        let userCode = vm.userCode;
        let addDate = new Date().getFullYear() + '-' + new Date().getMonth() + 1 < 10 ? '0' + (new Date().getMonth() + 1) : new Date().getMonth() + 1;
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
            let year = new Date().getFullYear();
            let month = new Date().getMonth() + 1 < 10 ? '0' + (new Date().getMonth() + 1) : new Date().getMonth() + 1;
            vm.change(year, month);
          }
        });
      },
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
            axisLabel: {
              formatter: function (value, index) {
                // 格式化成月/日，只在第一个刻度显示年份
                var date = new Date(value);
                var texts = [ date.getDate()];
                return texts;
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
            top: 10,
            left: 20,
            right: 20,
            bottom: 90
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
    }
  }

</script>

<style scoped lang="less">
  @import "../assets/css/common.less";
  .sumAmount{
    width: 100%;
    .user-info{
      width: 100%;
      padding: 0 24*@basePX;
        .top{
          width: 100%;
          height: 122*@basePX;
          line-height: 122*@basePX;
          text-align: center;
          font-size: 24*@basePX;
          color:@subTextColor;
          span{
            color:@clickBtnBg;
            font-size: 48*@basePX;
            font-family: Impact;
            margin-left: 10*@basePX;
          }
        }
      .account,.count{
        padding: 20*@basePX 0 15*@basePX 0;
        text-align: center;
        border-top: 1px solid #e7eaf0;
        li{
          font-size: 24*@basePX;
          color:#ccc;
          width: 33.33%;
          float: left;
          position: relative;
          div:nth-of-type(2){
            color:@subTextColor;
            span{
              font-size: 32*@basePX;
              font-family: Impact;
              margin-left: 10*@basePX;
            }
          }
          div:nth-of-type(3){
            width: 1px;
            height: 42*@basePX;
            background-color: #e7eaf0;
            position: absolute;
            right:0;
            top:18*@basePX;
          }
        }
      }
      .count{
        li{
          font-size: 32*@basePX;
          color:@topBg;
          width: 33.33%;
          float: left;
          position: relative;
          div:nth-of-type(1){
            font-family: Impact;
          }
          div:nth-of-type(2){
            font-size: 24*@basePX;
            color:@subTextColor;
          }
        }
      }
    }
    h2{
      font-weight: normal;
      font-size: 28*@basePX;
      line-height: 80*@basePX;
      color: @tipsColor;
      text-align: center;
      border-top: 8*@basePX solid #e9eff2;
      margin-top: 15*@basePX;
    }
    .container{
      width: 100%;
      height: 800*@basePX;
      margin: 0 auto;
      padding-bottom: 50*@basePX;
      .chart-wrap{
        width: 100%;
        height: 100%;
      }
    }
  }

</style>
