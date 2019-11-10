<template>
  <div class="shareStatistics">
    <Mheader isHeader="0" currentPage="6"></Mheader>
    <div class="main">
      <div v-if="content.length" class="content">
        <div class="contentTitle">
          <ul>
            <li>分享产品</li>
            <li>分享时间</li>
            <li>浏览次数</li>
            <li>下单次数</li>
            <li>状态</li>
            <li>操作</li>
          </ul>
        </div>
        <div class="contentDisplay">
          <ul>
            <li v-for="num in content">
              <div class="productDisplay pull-left">
                <img :src="num.cruiseVoyage.image">
                <div>{{num.cruiseVoyage.name}}</div>
              </div>
              <div class="dateDisplay pull-left">{{num.addDate.replace(/\-/g,'/')}}</div>
              <div class="numDisplay pull-left">{{num.sharePriceLogNumber}}</div>
              <div class="orderNum pull-left">{{num.cruiseOrderNumber}}</div>
              <div class="stateDisplay pull-left" >
                <span v-if="!num.isTakeEffect">生效中</span>
                <span :class="num.isTakeEffect ? 'fontColor':''" v-if="num.isTakeEffect">已失效</span>
              </div>
              <div class="operation pull-left"><div @click="seeDetails(num)">查看详情</div></div>
              <div class="clearfloat"></div>
            </li>
          </ul>
        </div>
      </div>
      <div v-if="content.length == 0" class="no-content">
        <img src="../assets/img/noContent.png" alt="">
        <div>暂时没有相关内容</div>
      </div>
    </div>
    <div v-if="content.length >0" class="paging-display">
      <div>
        <Page show-elevator className="paging-style" :current="page" :page-size="5" :total="total" @on-change="pageChange"></Page>
      </div>
    </div>
    <m-footer></m-footer>
  </div>
</template>
<script>
  let vm;
  import Mheader from "@/components/header.vue";
  import MFooter from '@/components/footer'
  export default{
    name : 'shareStatistics',
    components :{
      Mheader,
      MFooter,
    },
    data(){
        return{
          content:[],
          page:1,
          total :1
        }
    },
    mounted(){
      vm = this;
      vm.axios.post(this.api + this.urlApi.querySharePriceList,{
        page : vm.page,
        rows : 5
      }).then((data) => {
          if(data.status){
            vm.total = data.content.total;
            for(let i=0;i<data.content.rows.length; i++){
                let json = data.content.rows[i];
                let time1 = new Date().getTime();
                let time2 = new Date(json.expireDate).getTime();
                let isTakeEffect = time1 > time2 ? true : false;
                vm.$set(json,'isTakeEffect',isTakeEffect);
                vm.content.push(json)
            }
            if(vm.content.length>0){
              vm.$nextTick(function(){
                let prev = document.querySelector('.ivu-page-prev');
                prev.innerHTML = '上一页';
                let next = document.querySelector('.ivu-page-next');
                next.innerHTML = '下一页';
                let dom = document.querySelector('.ivu-page-options-elevator input');
                dom.addEventListener('blur',function () {
                  let value = parseInt(dom.value);
                  let isNum = isNaN(value);
                  if(isNum){
                    dom.value = 1;
                    vm.pageChange(1);
                  }else {
                    if(value <1){
                      dom.value = 1;
                      vm.pageChange(1);
                    }else if(Math.ceil(vm.total/5) < value){
                      dom.value = Math.ceil(vm.total/5);
                      vm.pageChange(Math.ceil(vm.total/5) );
                    }else {
                      vm.pageChange(value);
                    }
                  }
                })
              });
            }
          }
        });

    },
    methods:{
      pageChange(num){
        vm.page = num;
        vm.axios.post(this.api + this.urlApi.querySharePriceList,{
          page : vm.page,
          rows : 5
        }).then((data) => {
          if(data.status){
            vm.total = data.content.total;
            vm.content = data.content.rows;
          }
        });
      },
      seeDetails(obj){
          sessionStorage.numberCode = obj.numberCode;
          vm.$router.push('/orderDetails');
      }
    }
  }
</script>
<style lang="less" scoped>
  @import "../assets/css/common.less";
  .shareStatistics{
    background-color: @appBg;
    .main{
      width: 1200px;
      min-height: 700px;
      background-color: #fff;
      margin:20px auto 40px;
      .borderRadius();
    }
    .content{
      width: 1200px;
      margin:40px auto;
      background-color: #fff;
      border-radius: 8px;
      padding: 20px;
      .contentTitle{
        height: 40px;
        line-height: 40px;
        background-color: #e7f3ff;
        font-size: 16px;
        color:#333333;
        padding-left: 10px;
        ul{
          li:nth-of-type(1){
            width: 380px;
          }
          li:nth-of-type(2){
            margin-left: 40px;
            width: 175px;
          }
          li:nth-of-type(3){
            width: 135px;
          }
          li:nth-of-type(4){
            width: 155px;
          }
          li:nth-of-type(5){
            width: 135px;
          }
          li:nth-of-type(6){
            width: 130px;
          }
        }
      }
      .contentDisplay{
        padding: 0 10px;
        ul{
          li{
            float: none;
            padding: 20px 0;
            border-bottom: 1px solid #e7e7e7;
            font-size: 14px;
            color:#333;
            line-height: 72px;
            .productDisplay{
              width: 380px;
              position: relative;
              font-size: 16px;
              img{
                width: 120px;
                height: 72px;
                .borderRadius;
              }
              div{
                width: 237px;
                position: absolute;
                line-height: 25px;
                top:50%;
                right: 0;
                transform: translateY(-50%);
              }
            }
            .dateDisplay{
              margin-left: 40px;
              width: 175px;
            }
            .numDisplay{
              width: 135px;
            }
            .orderNum{
              width: 155px;
            }
            .stateDisplay{
              width: 135px;
              color:#36ad3b;
            }
            .fontColor{
              color:#999999
            }
            .operation{
              width: 120px;
              div{
                width: 80px;
                height: 28px;
                border-radius: 14px;
                background-color: #ff9b0b;
                color:#fff;
                line-height: 28px;
                text-align: center;
                margin-top: 20px;
                cursor: pointer;
              }
            }
          }
        }
      }
    }
    .paging-display{
      width: 1200px;
      height: 50px;
      margin: -30px auto 40px;
      background-color: #fff;
      text-align: center;
      div{
        display: inline-block;
        margin-top: 10px;
      }
    }
  }
</style>
