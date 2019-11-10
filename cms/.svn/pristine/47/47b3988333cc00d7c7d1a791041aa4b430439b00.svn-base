<template>
  <div class="counter">
    <div class="content">
      <span class="sub iconfont icon-tooldel" @click="subtraction()"></span>
      <span class="num">{{product.counter}}</span>
      <span class="add iconfont icon-tooladd" @click="addition()"></span>
    </div>
  </div>
</template>

<script>

  let vm;
  export default {
    name: 'counter',
    props:{
      product: {
        type: Object
      }
    },
    data() {
      return {

      }
    },
    mounted() {
      vm = this;
    },
    methods: {
      subtraction(){
        if(this.product.counter > 0){
          this.product.counter--;
          this.product.realCount++;
          this.$emit('confirm',this.product);
        }
      },
      addition() {
        if(!this.product.maxCount){
          if(this.product.realCount > 0){
            this.product.counter++;
            this.product.realCount--;
            this.$emit('confirm',this.product);
          }
        }else {
          if(this.product.counter < this.product.maxCount){
            if(this.product.realCount > 0){
              this.product.counter++;
              this.product.realCount--;
              this.$emit('confirm',this.product);
            }else {
              this.dialog.showDialog('该舱型已售罄');
            }
          }else {
            this.dialog.showDialog('单次购买最多只能买'+this.product.maxCount+'间');
          }
        }
      },
    }
  }
</script>

<style lang="less" scoped>
  @import "../assets/css/common.less";
  .animated {
    animation-duration: 1s;
    animation-fill-mode: both;
  }
  @keyframes slideInRight {
    from {
      transform: translate3d(100%, 0, 0);
      visibility: visible;
    }

    to {
      transform: translate3d(0, 0, 0);
    }
  }

  .slideInRight {
    animation-name: slideInRight;
  }

  .counter{
    background-color: @clickBtnBg;
  }
  .counter,
  .count-wrap{
    width: 100%;
    height: 100%;
    font-size: 28*@basePX;
  }
  .content{
    .sub,.add{
      float: left;
      font-size: 40*@basePX;
      color:@topBg;
      margin-top: -5*@basePX;
    }
    .num{
      float: left;
      font-size: 32*@basePX;
      color:#8d95a3;
      padding: 0 10*@basePX;
    }
  }
</style>
