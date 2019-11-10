<template>
  <div id="orderSuccess">
    <Mheader></Mheader>
    <div class="main">
      <!-- 下单成功 -->
      <div class="success-wrapper">
        <div class="struts-box">
          <img src="../assets/img/success.png" alt="">
          <span class="title">恭喜您预定成功！</span>
          <div class="btn">
            马上去
            <router-link tag="span" :to="'orderAll'">查看订单</router-link>
            或
            <router-link tag="span" :to="'productQuery'">返回首页</router-link>
            再次购买
          </div>
        </div>
        <!--<div class="link-box">-->
          <!--<div class="copy-link">-->
            <!--填写出行人信息：-->
            <!--<input type="text" class="link" ref="link" readonly value="http://sis.maytek.cn/loading.html">-->
            <!--<span class="copy-btn" @click="copyLink()">复制链接</span>-->
          <!--</div>-->
          <!--<img :src="QRcode" alt="">-->
          <!--<p>扫描填写出行人信息</p>-->
        <!--</div>-->
      </div>
    </div>
    <Mfooter></Mfooter>
  </div>
</template>

<style lang="less" scoped>
  @import "../assets/css/common";

  #orderSuccess{
    .main{
      color: #999;
      width: 1200px;
      height: 541px;
      margin: 40px auto 40px;
      background-color: #fff;
      position: relative;
      .borderRadius;
      .success-wrapper{
        .struts-box{
          width: 300px;
          text-align: center;
          position: absolute;
          left: 50%;
          top: 50%;
          transform: translate(-50%,-50%);
          img{
            width: 106px;
            height: 106px;
          }
          .title{
            font-size: 24px;
            color: #999;
            display: block;
            margin-top: 20px;
            text-indent: 0.6em;
          }
          .btn{
            padding-top: 16px;
            color: #999;
            font-size: 16px;
            span{
              text-decoration: underline;
              color: @changeBtnBg;
              cursor: pointer;
            }
          }
        }
        .link-box{
          width: 738px;
          height: 242px;
          border:1px solid @lineColor;
          margin:45px auto 0;
          text-align: center;
          .copy-link{
            font-size: 16px;
            margin-top: 30px;
            .link{
              resize: none;
              border: 0;
              color: #999;
              font-size: 16px;
              width: 190px;
            }
            .copy-btn{
              font-size: 18px;
              color: @topBg;
              margin-left: 20px;
              cursor: pointer;
              display: inline-block;
              vertical-align: middle;
            }
          }
          img{
            width: 108px;
            height: 108px;
            margin-top: 14px;
          }
          p{
            margin-top: 6px;
          }
        }
      }
    }
  }
</style>

<script>
  import Mheader from "@/components/header.vue";
  import Mfooter from "@/components/footer.vue";
  import QRCode from 'qrcode'
  let vm
  export default{
    name:'orderSuccess',
    components:{
      Mheader,
      Mfooter
    },
    data(){
      return{
        QRcode: '',       //生成二维码
      }
    },
    mounted:function () {
      vm = this;
//      QRCode.toDataURL('http://sis.maytek.cn/loading.html',{ errorCorrectionLevel:'H' } , function (err, url) {
//        vm.QRcode = url;
//      })
    },
    methods:{
        copyLink:function () {
          let copyUrl=vm.$refs.link;
          copyUrl.select();
          document.execCommand("Copy","false",null);
          vm.dialog.showDialog("复制成功！可粘贴");
        }
    }
  }
</script>
