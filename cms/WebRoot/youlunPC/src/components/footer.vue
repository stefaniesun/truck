<template>
  <div id="footer">
    <footer>
      <div class="top-line">
        <p onClick="window.open('http://www.miitbeian.gov.cn/')">版权所有 上海美匣网络科技有限公司 Copyright©2017保留所有权利 <br>
          沪ICP备16025219号-1</p>
      </div>
    </footer>
  </div>
</template>

<style lang="less" scoped>
  @import "../assets/css/common";

  #footer{
    footer{
      width: 100%;
      min-width: 1200px;
      height: 200px;
      background-color: @topBg;
      padding-top: 73px;
      .top-line{
        width: 1000px;
        height: 52px;
        margin:auto;
        background-image: url(../assets/img/line.png);
        background-size: 100% 100%;
        text-align: center;
        color: #fff;
        font-size: 14px;
        p{
          padding-top: 7px;
          cursor: pointer;
        }
      }
    }
  }
</style>

<script>
  export default{
      name:'footer'
  }
</script>
