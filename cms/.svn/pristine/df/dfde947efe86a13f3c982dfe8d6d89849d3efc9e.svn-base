<template>
  <div class="calendar">
    <header>
      <h2>选择日期</h2>
    </header>

    <div class="container">
      <div class="prev-btn" @click.stop="prevMonth" :class="{disabled: monthIndex == 0 || monthDates.length === 1}">
        <svg class="icon">
          <use xlink:href="#icon-you"></use>
        </svg>
      </div>
      <div class="next-btn" @click.stop="nextMonth" :class="{disabled: monthIndex == monthDates.length-1 || monthDates.length === 1}">
        <svg class="icon">
          <use xlink:href="#icon-ziyuan"></use>
        </svg>
      </div>
      <div class="section-wrapper ">
        <div class="section-group clearfloat" ref="sectionGroup">
          <section class="month" v-for="(monthItem,monthKey) in monthDates">
            <h2>{{monthItem.month}}</h2>
            <div class="week">
              <span  v-for="(value,key) in weekdays" class="weekday">{{value}}</span>
            </div>

            <div class="date-container clearfloat" ref="mouthDetail">
              <div class="date-item " v-for="item in monthItem.firstWeekDay"> </div>
              <div @click="selectDate(monthItem.month, item)" class="date-item trip "
                   v-for="(item,key) in monthItem.dates"
                   :class="{active:item.minPrice && key===date
                   && month===new Date(monthItem.month.replace(/([^\u0000-\u00FF])/gi,'-')+item.date).getMonth()+1
                   && year===new Date(monthItem.month.replace(/([^\u0000-\u00FF])/gi,'-')+item.date).getFullYear(),
                   pointer: item.minPrice }">
                <p class="date" :class="{disabled:!item.minPrice }">{{item.date }}</p>
                <p class="price" v-if="item.minPrice">{{item.minPrice}}</p>
              </div>
              <div class="date-item " v-for="item in monthItem.lastSpace"> </div>
            </div>

          </section>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import index from "@/router/index";

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
        year: 0,
        distance: 0,
        monthIndex: 0
      }
    },
    created() {
    },
    mounted() {
      vm = this;
      vm._setDates();
      vm._initalActive();
      vm.setSectionW();
    },

    methods: {
      _initalActive() {
        vm.date = new Date(vm.travelDate).getDate()-1;
        vm.month = new Date(vm.travelDate).getMonth()+1;
        vm.year = new Date(vm.travelDate).getFullYear();
        let year =  new Date(vm.travelDate).getFullYear();
        let index = 0;
        for(let i in vm.dateList) {
          let m = new Date(vm.dateList[i].month).getMonth() + 1;
          let y = new Date(vm.dateList[i].month).getFullYear();
          if(m === vm.month && y === year)  {
            index = i;
            vm.monthIndex = i;
          }
        }

        vm.distance = -index * 600;
        vm.$refs.sectionGroup.style.transform = "translateX(" + vm.distance + "px)";
      },
      setSectionW() {
        let len = vm.dateList.length;
        vm.$refs.sectionGroup.style.width = len * 600 + 'px';
      },
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
          if(firstWeek>=5 && lastDayOfMonth === 31 || firstWeek === 6
            && lastDayOfMonth === 30 || firstWeek === 1 && lastDayOfMonth === 28){
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
              let date = new Date(vm.dateList[obj].dates[j].travelDate);
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
        vm.monthDates = months;
      },
      selectDate(Month,item){
        vm.date = item.date-1;
        let month = Month.replace(/([^\u0000-\u00FF])/gi,"-");
        let travelDate = month + item.date;
        let upData = {
          travelDate: travelDate,
          numberCode: item.numberCode
        }
        month = new Date(travelDate).getMonth()+1;
        vm.month = month;
        if(item.minPrice){
          vm.$emit('select',upData);
          vm.slcDate.slcBoolean = false;
        }
      },
      prevMonth() {
        if( vm.distance < 0) {
          vm.distance += 600;
          vm.$refs.sectionGroup.style.transform = "translateX(" + vm.distance + "px)";
          vm.monthIndex--;
        }
      },
      nextMonth() {
        let groupWidth = vm.$refs.sectionGroup.offsetWidth;
        if(Math.abs(vm.distance) + 600 < groupWidth) {
          vm.distance -= 600;
          vm.$refs.sectionGroup.style.transform = "translateX("+vm.distance+"px)";
          vm.monthIndex++;
        }
      }
    }
  }
</script>

<style lang="less" scoped>
  @import "../assets/css/common.less";
  .calendar{
    width: 700px;
    height: 598px;
    position: fixed;
    left: 50%;
    top: 159px;
    transform: translateX(-50%);
    background: #fff;
  }
  header{
    top: 0;
    width: 100%;
    height: 40px;
    line-height: 40px;
    background-color: @topBg;
    color: #fff;
  }

  header h2{
    font-size: 22px;
    text-align: center;
    font-weight: normal;
  }

  .container{
    width: 100%;
    height: 631px;
    overflow: hidden;
    position: relative;
    .prev-btn,
    .next-btn{
      position: absolute;
      top: 50%;
      transform: translateY(-50%);
      cursor: pointer;
      .icon{
        color: #bcd4f0;
        height: 66px;
        width: 36px;
      }
      &.disabled {
        cursor: default;
        .icon{
          color: #ccc;
        }
      }
    }
    .prev-btn{
      left: 16px;
    }
    .next-btn{
      right: 8px;
    }
    .section-wrapper{
      width: 600px;
      height: 600px;
      margin: 20px auto;
      overflow: hidden;
      position: relative;
    }
    .section-group{
      position: absolute;
      left: 0;
      transition: all .3s;
    }
    section{
      float: left;
      border: 1px solid #dee7f2;
      width: 600px;
      h2{
        font-weight: normal;
        text-align: center;
        background-color: #dee7f2;
        line-height: 40px;
        font-size: 18px;
        color: #333;
      }
      .week{
        height: 40px;
        line-height: 40px;
        .weekday{
          display: inline-block;
          width: 14.286%;
          text-align: center;
          color: #999;
          font-size: 18px;
        }
      }

      .date-container{
        border-bottom: 1px solid #dee7f2;
        .date-item{
          height: 85px;
          float: left;
          position: relative;
          border-left: 1px solid transparent;
          border-top: 1px solid #dee7f2;
          width: 14.25%;
          text-align: center;
          padding: 18px 0;

          font-size: 18px;
          &+ .date-item{
            border-left: 1px solid #dee7f2;
          }
          &:nth-of-type(7n+1){
            border-left: 1px solid transparent;
          }

          .price{
            color: @tipsColor;
            text-align: center;
          }
          &.pointer{
            cursor: pointer;
            &:hover{
              background-color: @topBg;
              .price,.date{
                color: #fff;
              }
            }
          }
          &.trip{
            .date{
              color: #808080;
              &.disabled{
                color: #ccc;
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
</style>

