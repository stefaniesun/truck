<template>
  <div id="header">
    <header v-if="isHeader != 1">
      <div class="center clearfloat">
        <div class="logo-wrapper">
          <img src="../assets/img/logo.png" alt="邮轮供应资源售卖系统">
        </div>
        <div v-if="wechatCode" class="contact">
          <div class="wechat-code">
            <img :src="wechatCode" alt="">
            <div class="code-preview">
              <img :src="wechatCode" alt="">
            </div>
          </div>
          <div class="text">
            <span>扫一扫 立即关注</span>
            <span>邮轮分销微信公众号</span>
          </div>
        </div>
      </div>
    </header>
    <nav>
      <div class="center clearfloat">
        <div class="user">
          <svg class="icon">
            <use xlink:href="#icon-zhanghao"></use>
          </svg>
          <span>欢迎您：{{phone}}</span>
          <Dropdown @on-click="jump" class="btn" trigger="click" :transfer="true">
            <a href="javascript:void(0)">
              <Icon type="arrow-down-b"></Icon>
            </a>
            <DropdownMenu slot="list">
              <DropdownItem name="修改密码">修改密码</DropdownItem>
              <DropdownItem name="退出">退出</DropdownItem>
            </DropdownMenu>
          </Dropdown>
        </div>
        <ul class="navigation">
          <router-link :class="currentPage == 1 ? 'current-state' : ''" tag="li" to="productQuery"><span class="link">首页</span></router-link>
          <router-link :class="currentPage == 2 ? 'current-state' : ''" tag="li" to="orderAll"><span class="link">全部订单</span></router-link>
          <router-link :class="currentPage == 3 ? 'current-state' : ''" tag="li" to="moneyAmount"><span class="link">金额统计</span></router-link>
          <router-link :class="currentPage == 4 ? 'current-state' : ''" tag="li" to="bargainPrice"><span class="link">特价尾单</span></router-link>
          <!--<router-link :class="currentPage == 1 ? 'current-state' : ''" tag="li"  class="link" to="customerAmount">游客统计</router-link>-->
          <router-link :class="currentPage == 5 ? 'current-state' : ''" tag="li" to="cruiseIntroduction"><span class="link">邮轮介绍</span></router-link>
          <router-link :class="currentPage == 6 ? 'current-state' : ''" tag="li" to="shareStatistics"><span class="link">分享统计</span></router-link>
        </ul>
      </div>
    </nav>
  </div>
</template>

<script>
  let vm;
  export default {
    name: 'header',
    props : ['currentPage','isHeader'],//currentPage控制当前显示页面状态，1为首页、2为全部订单、3为金额统计、4为游客统计。isHeader控制客服电话一行是否显示
    data(){
    	return{
    		phone: sessionStorage.userName,
        wechatCode: sessionStorage.getItem('qrcode')
      }
    },
    mounted(){
    	vm = this;
    },
    methods: {
      jump(value){
        if (value == '退出') {
          vm.axios.post(this.api + this.urlApi.logOutOper).then(function (respones) {
            if (respones.status) {
              sessionStorage.clear();
              vm.$router.push('/');
            }
          });
        }else if(value == '修改密码'){
          vm.$router.push('/resetPwd');
        }
      }
    }
  }
</script>

<style lang="less" scoped>
  @import "../assets/css/common.less";
  .center{
    width: 1200px;
    margin: 0 auto;
    position: relative;
  }

  header{
    position: relative;
    height: 88px;
    line-height: 88px;
    background-color: #fff;
    .logo-wrapper{
      float: left;
    }
    .contact {
      width: 217px;
      height: 72px;
      margin-top: 8px;
      float: right;
      position: relative;
      .wechat-code{
        width: 72px;
        height: 72px;
        float: left;
        img{
          width: 100%;
          height: 100%;
          display: block;
        }
        .code-preview{
          width: 212px;
          height: 212px;
          position: absolute;
          left: -80px;
          top: 72px;
          z-index: 22;
          display: none;
          border: 1px solid @lineColor;
          .borderRadius;
          overflow: hidden;
        }
      }
      .text{
        position: absolute;
        top: 50%;
        transform: translateY(-50%);
        right: 0;
        line-height: 24px;
        span{
          display: block;
          font-size: 14px;
          &:first-child{
            font-size: 16px;
            padding-left: 3px;
          }
        }
      }
      &:hover{
        .code-preview{
          display: block;
        }
      }
    }
  }

  nav{
    height: 40px;
    line-height: 40px;
    font-size: 14px;
    background-color: @topBg;
    .user{
      float: left;
      color: #f0e83f;
      .icon{
        font-size: 18px;
        color: #fff;
      }
      .btn{
        a{
          color: #f0e83f;
        }
        margin-left: 10px;
        text-align: center;
      }
    }
    .navigation{
      position: absolute;
      right: 0;
      li{
        float: left;
        cursor: pointer;
        &:hover{
          background-color: @tipsColor;
          color: #fff;
          box-shadow: -1px 0px 0px #ff9b0b;
          .link{
            border-right-color: @tipsColor;
          }
        }
        .link{
          padding: 0 20px;
          border-right: 1px solid #fff;
          text-decoration: none;
          color: #fff;
        }
        &:last-child {
          .link{
            border-right: 0;
          }
        }
      }
      .current-state{
        background-color: #ff9b0b;
        box-shadow: -1px 0px 0px #ff9b0b;
        .link{
          border-right-color: @tipsColor;
        }
      }
    }
  }
</style>
