<template>
  <div class="counter">

      <span class="sub no-select" @click="subtraction()">
        <svg class="icon">
          <use xlink:href="#icon-tooldel"></use>
        </svg>
      </span>
      <span class="num">{{product.counter}}</span>
      <span class="add no-select" @click="addition()">
         <svg class="icon">
          <use xlink:href="#icon-tooladd"></use>
        </svg>
      </span>

  </div>
</template>

<script>
  let vm;
  export default {
    name: 'counter',
    props:{
      product: {
        type: Object,
      },
      order: {
        type: Object
      }
    },
    data() {
      return {
        isOrder:false,
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
          this.order.total--;
          this.order.totalPrice -= this.product.price * this.product.volume;
          this.createOrder('reduce', this.product);
        }
      },
      addition() {
        if(!this.product.maxCount){
          if(this.product.realCount > 0){
            this.product.counter++;
            this.product.realCount--;
            this.order.total++;
            this.order.totalPrice += this.product.price * this.product.volume;
            this.createOrder('add', this.product)
          }
        }else {
        	if(this.product.counter < this.product.maxCount){
            if(this.product.realCount > 0){
              this.product.counter++;
              this.product.realCount--;
              this.order.total++;
              this.order.totalPrice += this.product.price * this.product.volume;
              this.createOrder('add', this.product)
            }
          }
        }
      },
      createOrder(compute,ptObj) {
        let cabinArr = vm.order.cabinArr;
        switch (compute){
          case 'reduce': {
            for(let i = 0; i < cabinArr.length; i++) {
              if(cabinArr[i].numberCode === ptObj.numberCode) {
                vm.order.cabinArr[i].roomNum--;
                if(vm.order.cabinArr[i].roomNum === 0)  {
                  vm.order.cabinArr.splice(i,1)
                }else{
                  vm.$set(vm.order.cabinArr[i], 'realCount', ptObj.realCount);
                }
              }
            }
          }; break;
          case 'add': {
            if(cabinArr.length === 0 || JSON.stringify(cabinArr).indexOf(JSON.stringify(ptObj.numberCode)) === -1) {
              let cabinObj = {
                cabinType: ptObj.name,
                roomNum: 1,
                price: ptObj.price,
                totalPrice:ptObj.price*ptObj.volume*ptObj.counter,
                rebate: ptObj.rebate,
                stockCount: ptObj.stockCount,
                realCount: ptObj.realCount,
                maxCount: ptObj.maxCount,
                stockType: ptObj.stockType,
                sufficientCount: ptObj.sufficientCount,
                cabinImg: ptObj.cabinImg,
                numberCode: ptObj.numberCode,
                stock: ptObj.stock,
                cruiseVoyage: ptObj.cruiseVoyage,
                volume : ptObj.volume,
              };
              vm.$set(vm.order.cabinArr, vm.order.cabinArr.length,cabinObj);
            }else{
              for(let i = 0; i < cabinArr.length; i++) {
                if(cabinArr[i].numberCode === ptObj.numberCode){
                  vm.order.cabinArr[i].roomNum++;
                  vm.$set(vm.order.cabinArr[i], 'realCount', ptObj.realCount);
                }
              }
            }
          }; break;
        }
        let isShow = false;
        for(let num=0;num<cabinArr.length;num++){
          let json = cabinArr[num];
          if(json.roomNum > 0){
            isShow = true;
            break;
          }
        }
        this.$emit('confirm',isShow);

        sessionStorage.ptOrder = JSON.stringify(vm.order);
      }
    }
  }
</script>

<style lang="less" scoped>
  @import "../assets/css/common.less";

  .counter{
    width: 100%;
    height: 100%;
    position: relative;
    text-align: center;
    .sub,.add{
      position: absolute;
      color: @changeBtnBg;
      font-size: 32px;
      cursor: pointer;
    }
    .num{
      -webkit-transform: translateX(-50%);
      -moz-transform: translateX(-50%);
      -ms-transform: translateX(-50%);
      -o-transform: translateX(-50%);
      transform: translateX(-50%);
      color: @subTextColor;
      font-size: 21px;
      cursor: inherit;
    }
    .sub{
      left: 0;
    }
    .add{
      right:0;
    }
  }

</style>
