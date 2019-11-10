<template>
  <div id="quotation">
    <header>
      <h2>报价单</h2>
    </header>
    <div class="container">
      <ul class="list">
        <li v-for="item in orderList" class="clearfloat">
          <div class="title clearfloat">
            <h3>歌诗达邮轮&nbsp12月7日</h3>
            <span class="text">舱型：阳台三人间</span>
          </div>
          <div class="info clearfloat">
            <span class="text">目的地：长崎+福冈</span>
            <span class="price">￥2400/人</span>
            <div @click="goDetail" class="btn">详情</div>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
  export default{
  	name: 'quotation',
    data(){
  		return{
        orderList: 6
      }
    },
    methods: {
      goDetail: function(){
        console.log(321)
      }
    }
  }
</script>

<style lang="less" scoped>
  @import "../assets/css/base.less";
  #quotation {
    width: 100%;
    height: 100%;
    background-color: @appBg;
    color: #4d4d4d;
  }
  .container {
    width: 100%;
    margin: 72*@basePX auto 0;
    padding: 24*@basePX 0;
    .list {
      width: 94%;
      margin: 24*@basePX auto auto;
      padding-bottom: 20*@basePX;
      background-color: #fff;
      .borderRadius;
      li {
        width: 96%;
        margin: 0 auto;
        padding: 0 17*@basePX 20*@basePX;
        border-bottom: 1px solid @lineColor;
        .title, .info {
          padding-top: 20*@basePX;
        }
        .text {
          color: @subTextColor;
          font-size: 24*@basePX;
        }
        h3 {
          font-size: 32*@basePX;
          font-weight: 500;
          float: left;
        }
        .title {
          .text {
            padding-top: 6*@basePX;
            float: right;
          }
        }
        .info {
          position: relative;
          .text {
            padding-top: 6*@basePX;
            float: left;
          }
          .price {
            color: @tipsColor;
            font-size: 32*@basePX;
            position: absolute;
            top: 50%;
            right: 16%;
            transform: translateY(-50%);
            padding-top: 22*@basePX;
          }
          .btn {
            width: 86*@basePX;
            height: 40*@basePX;
            line-height: 40*@basePX;
            text-align: center;
            color: #fff;
            font-size: 24*@basePX;
            .borderRadius(34*@basePX);
            background: @changeBtnBg;
            float: right;
          }
        }
      }
    }
  }
</style>
