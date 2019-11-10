<template>
  <div class="noPage">
    <header>
      <h2>邮轮详情</h2>
      <i class="back iconfont icon-fanhui" @click="goBack()"></i>
    </header>
    <div class="content">
      <div class="noPageContent">
        <div class="noPageImg">
          <span>4</span><img src="../assets/img/noPageImp.png"><span>4</span>
          <div class="english">Page Not Found</div>
        </div>
        <div class="return" @click="returnIndex">返回首页>></div>
      </div>
    </div>
  </div>
</template>
<script>
  export default{
    name : 'noPage',
    methods :{
      returnIndex(){
          this.$router.push('/productQuery');
      },
      goBack(){
        this.$router.goBack();
      }
    }
  }
</script>
<style lang="less">
  @import "../assets/css/common.less";
  .noPage{
    background-color: #f2f2f2;
    height: 100%;
    .content{
      padding: 112*@basePX 24*@basePX 24*@basePX 24*@basePX;
      height: 100%;
      .noPageContent{
        height: 100%;
        background-color: #fff;
        text-align: center;
        padding-top: 248*@basePX;
        .noPageImg{
          font-size: 175*@basePX;
          color:#bed9f4;
          display: inline-block;
          position: relative;
          span{
            float: left;
          }
          img{
            float: left;
            width: 158*@basePX;
            height: 157*@basePX;
            margin: 55*@basePX 36*@basePX;
          }
          .english{
            font-size: 54*@basePX;
            color:#e2eaf3;
            position: absolute;
            bottom: -20*@basePX;
          }
        }
        .return{
          font-size: 32*@basePX;
          color:#6aadff;
          margin-top: 46*@basePX;
        }
      }
    }
  }
</style>
