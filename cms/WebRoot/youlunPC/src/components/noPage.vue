<template>
  <div class="noPage">
    <Mheader></Mheader>
    <div class="content">
        <div class="pageContent">
          <img src="../assets/img/noPageImg1.png">
          <div class="pageNotFound">
            <div class="fontSize">404
              <div class="english">pageNotFound</div>
            </div>
            <div class="return" @click="goBack">返回上一级>></div>
          </div>
          <img src="../assets/img/noPageImg2.png">
        </div>
    </div>
    <Mfooter></Mfooter>
  </div>
</template>
<script>
  import Mheader from "@/components/header.vue";
  import Mfooter from "@/components/footer.vue";
  export default {
      name :'noPage',
    components:{
      Mheader,
      Mfooter,
    },
    methods :{
          goBack(){
              this.$router.goBack();
          }
    }
  }
</script>
<style lang="less">
  @import "../assets/css/common.less";
  .noPage{
    background-color: @appBg;
    .content{
      width: 1200px;
      height: 600px;
      margin: 40px auto;
      background-color: #fff;
      border-radius: 8px;
      text-align: center;
      .pageContent{
        display: inline-block;
        margin-top: 130px;
        img{
          float: left;
          margin-top: 50px;
        }
        .pageNotFound{
          display: inline-block;
          margin: 0 64px 0 36px;
          color:#fcdabe;
          float: left;
          text-align: center;
          .fontSize{
            font-size: 94px;
            position: relative;
            .english{
              width: 100%;
              font-size: 19px;
              position: absolute;
              left: 0;
              bottom: 8px;
            }
          }
          .return{
            font-size: 19px;
            color:#6aadff;
            cursor: pointer;
          }
        }
      }

    }
  }
</style>
