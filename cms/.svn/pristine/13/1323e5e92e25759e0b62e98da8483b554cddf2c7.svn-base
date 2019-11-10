<template>
  <div class="cruiseIntroduction">
    <Mheader isHeader="0" currentPage="5"></Mheader>
    <div class="main">
      <div class="content-display" v-if="content.length !== 0" v-for="num in content">
        <div class="company-name">
          <img :src="num.image">
          {{num.nameCn}}
        </div>
        <div class="cruise-content" v-for="i in num.cruiseSubjoinList" @click="clickJump(i)">
          <img :src="i.images.split(',')[0]">
          <div class="cruise-name">
            <div>{{i.cru.nameCn}}</div>
            <div>{{i.cru.nameEn}}</div>
          </div>
          <div class="cruise-data">
            <div>总吨位：{{i.cru.tonnage}}吨</div>
            <div>载客量：{{i.cru.busload}}人</div>
            <div>船舱数：{{i.cru.totalCabin}}间</div>
          </div>
          <div class="the-voyage">
            <div>航期</div>
            <img src="../assets/img/cruise.png">
            <div>{{i.voyageNumber}}</div>
          </div>
          <div class="detailed-info clearfloat"><span>详细信息>></span></div>
        </div>
        <div class="clearfloat"></div>
      </div>
      <div v-if="content.length == 0" class="no-content">
        <img src="../assets/img/noContent.png" alt="">
        <div>暂时没有相关内容</div>
      </div>
    </div>
    <Mfooter></Mfooter>
  </div>
</template>
<script>
  let vm;
  import Mheader from "@/components/header.vue";
  import Mfooter from "@/components/footer.vue";
  export default{
    name : 'cruiseIntroduction',
    components :{
      Mheader,
      Mfooter
    },
    data(){
      return {
        content : []
      }
    },
    mounted(){
      vm = this;
      vm.axios.post(vm.api + vm.urlApi.queryIntroductionList).then(function (data){
        if(data.status == 1){
         vm.content = data.content;
        }
      })
    },
    methods : {
      clickJump(obj){
        sessionStorage.cruise = obj.cruise;
        vm.$router.push('/cruiseDetails');
      }
    }
  }
</script>
<style lang="less" scoped>
  @import "../assets/css/common.less";
  .cruiseIntroduction{
    background-color: @appBg;
    .main{
      width: 1200px;
      min-height: 700px;
      background-color: #fff;
      margin:20px auto 40px;
      .borderRadius();
    }
    .content-display{
      width: 1200px;
      margin: 40px auto 0;
      background-color: #fff;
      box-shadow: 0 0 24px rgba(0,0,0,.13);
      padding: 15px 29px 15px 32px;
      .company-name{
        height: 60px;
        line-height: 60px;
        font-size: 24px;
        color:#333;
        border-bottom: 2px solid @topBg;
        img{
          float: left;
          margin-right: 12px;
          height: 41px;
        }
      }
      .cruise-content{
        width: 250px;
        height: 360px;
        margin: 32px 17px 15px 20px;
        padding: 10px;
        float: left;
        cursor: pointer;
        img{
          width: 100%;
          height: 130px;
        }
        .cruise-name{
          width: 218px;
          height: 65px;
          border-bottom: 1px solid #e5e5e5;
          margin-left: 10px;
          text-align: center;
          color:#333;
          padding-top:5px;
          div:nth-of-type(1){
            font-size: 24px;
          }
          div:nth-of-type(2){
            font-size: 14px;
          }
        }
        .cruise-data{
          margin-top: 40px;
          margin-left: 10px;
          float: left;
          border-right: 1px solid #f5f5f5;
          width: 142px;
          div{
            font-size: 14px;
            color:#333;
            line-height: 20px;
          }
        }
        .the-voyage{
          width: 70px;
          float: left;
          margin-top: 40px;
          position: relative;
          div:nth-of-type(1){
            font-size: 14px;
            color:#333;
            line-height: 20px;
            padding-left: 26px;
          }
          img{
            width: 46px;
            height: 40px;
            margin-left: 22px;
          }
          div:nth-of-type(2){
            width: 17px;
            height: 17px;
            position: absolute;
            top:23px;
            right:5px;
            line-height: 17px;
            text-align: center;
            font-size: 12px;
            -webkit-transform:scale(0.7);
            color:#fff;
          }
        }
        .detailed-info{
          width: 100%;
          font-size: 14px;
          color:#868686;
          display: inline-block;
          margin-top: 20px;
          text-align: center;
          span{
            cursor: pointer;
          }
        }
      }
      .cruise-content:nth-of-type(4n+1),.cruise-content:nth-of-type(1){
        margin-left: 10px;
      }
      .cruise-content:hover{
        box-shadow: 0 0 16px rgba(0,0,0,.23);
        .detailed-info{
          color:#ff9600;
        }
      }
    }
  }
</style>
