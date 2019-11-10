<template>
  <div class="touristAmount">
    <section class="clearfloat" v-for="item in 4">
      <div class="user">
        <i class="iconfont icon-youke"></i>
        <span class="name">张三</span>
      </div>
      <div class="content">
        <div class="left-side clearfloat">
          <div class="line-text">
            <span class="tag">电话：</span>
            <span class="value">15826464914</span>
          </div>
          <div class="line-text">
            <span class="tag">微信号：</span>
            <span class="value">15826464914</span>
          </div>
        </div>
        <div class="right-side">
          <i class="iconfont icon-dianhua"></i>
        </div>

      </div>
    </section>
  </div>
</template>

<script>
  let vm;
  export default {
    name: 'touristAmount'
  }

</script>

<style scoped lang="less">
  @import "../assets/css/base.less";

  .touristAmount{
    width: 94%;
    margin: 0 auto;
    section {
      padding: 40*@basePX 0;
      border-bottom: 1px solid @lineColor;
      .user{
        float: left;
        width: 20%;
        text-align: center;
        font-size: 28*@basePX;
        i,span{
          display: block;
        }
        i{
          font-size: 55*@basePX;
          color: #c3cfe4;
        }
        span{
          margin-top: -8*@basePX;
        }
      }

      .content{
        float: left;
        width: 75%;
        position: relative;
        font-size: 28*@basePX;
        .left-side{
          float: left;
          line-height: 60*@basePX;
          .tag{
            color: @subTextColor;
          }
          .value{
            color: #333;
          }
        }
        .right-side{
          position: absolute;
          right: 0;
          top: 50%;
          -webkit-transform: translateY(-50% );
          -moz-transform: translateY(-50% );
          -ms-transform: translateY(-50% );
          -o-transform: translateY(-50% );
          transform: translateY(-50% );
          color: @topBg;
          i{
            font-size: 50*@basePX;
          }
        }
      }
    }
  }

</style>
