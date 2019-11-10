<template>
  <div class="calendar">
    <header>
      <h2>选择日期</h2>
      <i class="back iconfont icon-fanhui" @click="slcDate.slcBoolean = false;bodyNoScrolling.close()"></i>
    </header>
    <div class="main">
      <div class="container">
        <section class="month" v-for="(monthItem,monthKey) in monthDates">
          <h2>{{monthItem.month}}</h2>
          <div class="week">
            <span  v-for="(value,key) in weekdays" class="weekday">{{value}}</span>
          </div>

          <div class="date-container clearfloat" ref="mouthDetail">
            <div class="date-item trip" v-for="item in monthItem.firstWeekDay"> </div>
            <div @click="selectDate(monthItem.month, item)" class="date-item trip" v-for="(item,key) in monthItem.dates"
                 :class="{active:item.minPrice && key===date
               && month===translateTravelDate(monthItem.month.replace(/([^\u0000-\u00FF])/gi,'-') + item.date).getMonth()+1
               && year===translateTravelDate(monthItem.month.replace(/([^\u0000-\u00FF])/gi,'-') + item.date).getFullYear()}">
              <p class="date" :class="{disabled:!item.minPrice }">{{item.date }}</p>
              <p class="price" v-if="item.minPrice">{{item.minPrice}}</p>
            </div>
            <div class="date-item trip" v-for="item in monthItem.lastSpace"> </div>
          </div>

        </section>
      </div>
    </div>
  </div>
</template>

<script>
  let vm;
  import common from '@/assets/js/common'

  export default {
    name: 'calendar',
    props: {
      slcDate: {
        type: Object
      },
      dateList: {
        type: Array
      },
      travelDate: {
        type: String
      }
    },
    data() {
      return {
        weekdays: ['日','一','二','三','四','五','六'],
        monthDates: [],
        date: 0,
        month: 0,
        year: 0
      }
    },
    created() {
      vm = this;
      vm._setDates();
      vm.date = vm.translateTravelDate(vm.travelDate).getDate()-1;
      vm.month = vm.translateTravelDate(vm.travelDate).getMonth()+1;
      vm.year = vm.translateTravelDate(vm.travelDate).getFullYear();
    },

    methods: {

      _setDates() {
        let months = [];
        for(let key in  vm.dateList ){
          let obj = key;

          let monthObj = {
            month: 0,
            dates: [],
            firstWeekDay:0,
            lastSpace: 0
          };

          let chinaDate = common.chinaDate(vm.dateList[obj].month);
          monthObj.month = chinaDate.substr(0, chinaDate.length-4 );

          let month = new Date(vm.dateList[obj].month.split('-')[0],vm.dateList[obj].month.split('-')[1],1).getMonth();
          let fullYear = new Date(vm.dateList[obj].month.split('-')[0],vm.dateList[obj].month.split('-')[1],1).getFullYear();
          let lastDayOfMonth = new Date(fullYear, month, 0).getDate();
          let firstWeek = new Date(fullYear, month-1, 1).getDay();
          monthObj.firstWeekDay = firstWeek;
          monthObj.lastSpace = 35-lastDayOfMonth-firstWeek;
          if(firstWeek>=5 && lastDayOfMonth === 31 || firstWeek === 6 && lastDayOfMonth === 30 || firstWeek === 1 && lastDayOfMonth === 28){
            monthObj.lastSpace = 42-lastDayOfMonth-firstWeek;
          }

          for (let i = 0; i < lastDayOfMonth; i++) {
            let date = {
              date: i+1,
              minPrice: 0
            };
            vm.$set(monthObj.dates,monthObj.dates.length,date)
          }

          for (let j = 0; j < vm.dateList[obj].dates.length; j++) {
            for (let i = 0; i < monthObj.dates.length; i++) {
              let price = 0;
              let numberCode = '';
              let date = vm.translateTravelDate(vm.dateList[obj].dates[j].travelDate);
              let travelDate = date.getDate();
              if (travelDate === i + 1) {
                price = vm.dateList[obj].dates[j].minPrice;
                numberCode = vm.dateList[obj].dates[j].numberCode;
                monthObj.dates[i].minPrice = price;
                monthObj.dates[i].numberCode = numberCode;
                break;
              }
            }
          }
          vm.$set(months, months.length, monthObj)
        }
        vm.monthDates = months
      },
      translateTravelDate(date) {
        date = date.split(/[- : \/]/);
        date = new Date(date[0], date[1]-1, date[2]);
        return date;
      },
      selectDate(Month,item){
        vm.date = item.date-1;
        let month = Month.replace(/([^\u0000-\u00FF])/gi,"-");
        let travelDate = month + item.date;
        let upData = {
          travelDate: travelDate,
          numberCode: item.numberCode
        }
        month = vm.translateTravelDate(travelDate).getMonth()+1;
        vm.month = month;
        if(item.minPrice){
          vm.$emit('select',upData);
          vm.slcDate.slcBoolean = false;
          vm.bodyNoScrolling.close();
        }
      }
    }
  }
</script>

<style lang="less" scoped>
  @import "../assets/css/common.less";
  .calendar{
    width: 100%;
    height: 100%;
    overflow: hidden;
  }
  .main{
    height: 100%;
    padding-top: 88*@basePX;
    overflow-x: hidden;
    overflow-y: auto;
    .container{
      background-color: #fff;
      section{
        &:not(:first-of-type){
          margin-top: 16*@basePX;
        }
        h2{
          font-weight: normal;
          text-align: center;
          background-color: @panleBg;
          line-height: 70*@basePX;
          font-size: 32*@basePX;
          color: #333;
        }
        .week{
          height: 70*@basePX;
          line-height: 70*@basePX;
          .weekday{
            display: inline-block;
            width: 14.286%;
            text-align: center;
            color: @subTextColor;
            font-size: 32*@basePX;
          }
        }

        .date-container{
          border-bottom: 1px solid @lineColor;
          .date-item{
            height: 104*@basePX;
            float: left;
            position: relative;
            border-left: 1px solid transparent;
            border-top: 1px solid @lineColor;
            width: 14.25%;
            text-align: right;
            padding: 0.1rem 0.3rem 0.1rem 0;

            font-size: 32*@basePX;
            &+ .date-item{
              border-left: 1px solid @lineColor;
            }
            &:nth-of-type(7n+1){
              border-left: 1px solid transparent;
            }

            .price{
              color: @tipsColor;
              text-align: right;
              margin-top: 0.1rem;
            }
            &.trip{
              .date{
                color: #333333;
                &.disabled{
                  color: @subTextColor;
                }
              }
            }

            &.active{
              background-color: @topBg;
              .price,.date{
                color: #fff;
              }
            }
          }
        }
      }
    }
  }
</style>
