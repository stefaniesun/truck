<template>
  <div id="customerAmount">
    <Mheader isHeader="0" currentPage="4"></Mheader>
    <div class="main">
      <div class="title">
        <span>游客统计</span>
      </div>
      <div v-if="customerArr.length > 0" class="top">
        <div v-for="item in 2" :class="item==1?'left':'right'">
          <svg class="customerAll" xmlns="http://www.w3.org/2000/svg" version="1.1" viewbox="0 0 116 116" width="116" height="116" >
            <circle cx="58" cy="58" r="54" stroke-width="5" stroke="#f1f0ed" fill="none"></circle>
            <path class="bCircle" :stroke="item==1?'#ffbe07':'#6aadff'" stroke-linecap="round" stroke-width="8" fill="none" ></path>
            <path class="sCircle" stroke="#c1c5c9" stroke-width="5" stroke-linecap="round" fill="none" transform="rotate(60, 58 ,58 )"></path>
            <path class="sCircle"  stroke="#dee2df" stroke-width="5" stroke-dasharray="3,3" transform="rotate(-150, 58 ,58 )" fill="none"></path>
          </svg>
          <div class="info">
            <span>{{item==1?'总用户':'新用户'}}</span>
            <span>{{item==1? numAll + '人': numNew + '人'}}</span>
          </div>
        </div>
        <svg class="customerAll" xmlns="http://www.w3.org/2000/svg" version="1.1" viewbox="0 0 116 116" width="116" height="116" >
          <path  stroke="#c1c5c9" stroke-width="2"  fill="none" d="path://M1705.06,1318.313v-89.254l-319.9-221.799l0.073-208.063c0.521-84.662-26.629-121.796-63.961-121.491c-37.332-0.305-64.482,36.829-63.961,121.491l0.073,208.063l-319.9,221.799v89.254l330.343-157.288l12.238,241.308l-134.449,92.931l0.531,42.034l175.125-42.917l175.125,42.917l0.531-42.034l-134.449-92.931l12.238-241.308L1705.06,1318.313z'>"></path>

        </svg>

      </div>
      <div v-if="customerArr.length >0" class="contnent">
        <table class="list">
          <tr>
            <td class="name">姓名</td>
            <td class="phone">手机号</td>
            <td class="wx">微信号</td>
          </tr>
          <tr v-for="item in 10">
            <td class="name">
              张三
            </td>
            <td class="phone">
              17312344579
            </td>
            <td class="wx">
              asdasd1234123421k42klj3h
            </td>
          </tr>
        </table>
      </div>
      <div v-if="customerArr.length == 0" class="no-content">
        <img src="../assets/img/noContent.png" alt="">
        <div>暂时没有相关内容</div>
      </div>
    </div>
    <div v-if="customerArr.length >0" class="page-wrap">
      <div>
        <Page :total="total" :page-size="3" @on-change="pageChange" className="paging-style"></Page>
      </div>
    </div>
    <Mfooter></Mfooter>
  </div>
</template>

<script>
  import Mheader from "@/components/header.vue";
  import Mfooter from "@/components/footer.vue";
  let vm
  export default{
    name: 'orderAll',
    components:{
      Mheader,
      Mfooter
    },
    data(){
      return{
        total: 0,
        page: 1,
//        customerArr: new Array,
        customerArr: [''],
        numAll: 0,
        numNew: 0
      }
    },
    mounted(){
      vm = this;
      vm.getData(vm.page);
      let bCircle = document.querySelectorAll('.bCircle');
      let sCircle = document.querySelectorAll('.sCircle');
      vm.setPath(bCircle[0],58,4,54,210);
      vm.setPath(bCircle[1],58,4,54,150);
      vm.setPath(sCircle[0],58,24,34,60);
      vm.setPath(sCircle[1],58,24,34,180);
      vm.setPath(sCircle[2],58,24,34,60);
      vm.setPath(sCircle[3],58,24,34,180);
      vm.$nextTick(function(){
        let prev = document.querySelector('.ivu-page-prev');
        prev.innerHTML = '上一页';
        let next = document.querySelector('.ivu-page-next');
        next.innerHTML = '下一页';
      });
    },
    methods: {
    	setPath(el,startX,startY,r,deg){
    		let degTemp = 0;
    		let timer = setInterval(function () {
          degTemp += deg/(2000/20);
          if(degTemp >= deg){
            clearInterval(timer);
            return;
          }
          if(deg == 210){
            let aa = degTemp/deg;
            vm.numAll = Math.ceil(78625 * aa);
            vm.numNew = Math.ceil(214 * aa);
          }
          let arcType = degTemp > 180 ? '1' : '0';
          let x = startX + r * Math.sin(degTemp*Math.PI/180);
          let y = startY + r - r * Math.cos(degTemp*Math.PI/180);
          let path = 'M' + startX + ' ' + startY + 'A' + r + ' ' + r + ' ' + '0' + ' ' + arcType + ' ' + '1' + ' ' + x + ' ' + y;
          el.setAttribute("d", path) ;
        },20)
      },
      getData(page){
        let parame = {
          page: page,
          rows: 10
        }
//        vm.axios.post(this.api+this.urlApi.queryCruiseOrderList,parame).then(function (respones) {
//          if(respones.status){
//            let content = respones.content;
//            vm.total = content.total;
//            vm.customerArr = content.rows;
//            vm.$nextTick(function(){
//              let prev = document.querySelector('.ivu-page-prev');
//              prev.innerHTML = '上一页';
//              let next = document.querySelector('.ivu-page-next');
//              next.innerHTML = '下一页';
//            });
//          }
//        })
      },
      pageChange(){

      }
    }
  }
</script>

<style lang="less" scoped>
  @import "../assets/css/common.less";
  #customerAmount{
    .main{
      width: 1200px;
      min-height: 700px;
      margin: 40px auto;
      padding: 20px 60px;
      .borderRadius;
      background-color: #fff;
      position: relative;
      .title{
        width: 120px;
        height: 40px;
        line-height: 40px;
        text-align: center;
        color: #fff;
        background-color: @tipsColor;
        font-size: 16px;
        &:before{
          content: '';
          border-top: 20px solid transparent;
          border-left: 18px solid #fff;
          border-bottom: 20px solid transparent;
          float: left;
        }
        span{
          margin-left: -9px;
        }
      }
      .top{
        width: 100%;
        height: 116px;
        margin: 22px auto;
        .left,.right{
          min-width: 190px;
          height: 100%;
          position: relative;
          .info{
            width: 200px;
            position: absolute;
            top: 50%;
            left: 132px;
            transform: translateY(-50%);
            font-weight: 600;
            font-size: 16px;
            span{
              display: block;
            }
          }
        }
        .left{
          float: left;
          margin-left: 168px;
        }
        .right{
          float: right;
          margin-right: 168px;
        }
      }
      .contnent{
        .list{
          width: 100%;
          text-align: center;
          font-size: 14px;
          border-collapse: collapse;
          tr{
            height: 40px;
            line-height: 40px;
            border-bottom: 1px solid @lineColor;
          }
          tr:first-child{
            background-color: #e7f3ff;
            border: none;
          }
          .name,.phone,.wx{
            width: 33.3%;
            min-width: 33.3%;
          }
          .name{
            color: #333;
          }
          .phone{
            color: @changeBtnBg;
          }
          .wx{
            color: @tipsColor;
          }
        }
      }
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
  }
</style>
