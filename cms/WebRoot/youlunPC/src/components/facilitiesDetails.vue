<template>
  <div class="facilitiesDetails">
    <Mheader isHeader="0" currentPage="5"></Mheader>
    <div class="contentDisplay" ref="ptdetilContainer">
      <div class="displayContent" v-for="i in content">
        <div class="contentTitle">
          <svg class="icon wave" aria-hidden="true">
            <use :xlink:href="i.img"></use>
          </svg>
          {{i.name}}
        </div>
        <div class="contentExhibition">
          <div class="classificationDisplay" :class="index%2==1?'classificationDisplayTwo':''"  v-for="(num,index) in i.list">
            <img :src="num.image">
            <div class="currentContent">
              <p>{{num.name}}</p>
              <div class="introduce">{{num.presentation}}</div>
              <div class="explain">
                <div v-for="i in num.detailedInfo">
                  <div>{{i.name}}</div>：
                  <span>{{i.value}}</span>
                </div>
                <div>{{num.proposal}}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="navigationTitle" ref="subNavContainer">
        <ul>
          <li v-for="(num,index) in content" :class="index===number?'currentState':''" @click="jump(index)">
            <div>
              <svg class="icon wave" :style="'font-size:'+num.fontSize + 'px'" aria-hidden="true">
                <use :xlink:href="num.img"></use>
              </svg>
            </div>
            <div>{{num.name}}</div>
          </li>
        </ul>
      </div>
    </div>
    <div class="footer-wrapper">
      <m-footer></m-footer>
    </div>
  </div>
</template>
<script>
  let vm;
  import Mheader from "@/components/header.vue";
  import MFooter from '@/components/footer'
  import Vue from "vue"
  export default{
    name : 'facilitiesDetails',
    components :{
      Mheader,
      MFooter,
    },
    data(){
      return{
        number:0,
        content:[],
        isScroll:true,
        isClick : true,
      }
    },
    mounted(){
      vm = this;
      vm.number = parseInt(sessionStorage.currentSubscript);
      vm.content = JSON.parse(sessionStorage.currentContent);
      for(let i=0;i<vm.content.length;i++){
          let json = vm.content[i];
          if(json.name == '舱房'||json.name == '儿童'||json.name == '餐厅'){
            json.fontSize = 30;
          }
          if(json.name == '购物'){
            json.fontSize = 32;
          }
        if(json.name == '娱乐'){
          json.fontSize = 34;
        }
      }
      vm.$nextTick(function(){
        if (vm.number !== 0) {
          vm.jump(vm.number);
          if (vm.number == vm.content.length - 1) {
            vm.setPostion(vm.$refs.subNavContainer, 'fixed', 115, vm.getConLeft() - 100);
          }
        }
        window.addEventListener("scroll", vm.listenScroll);
      })
    },
    destroyed() {
      window.removeEventListener("scroll", vm.listenScroll);
    },
    methods:{
      jump(index) {
        if(index == vm.content.length - 1){
          vm.isScroll = false;
          let time = setTimeout(function(){
            vm.isScroll = true;
            clearTimeout(time);
          },500);
        }else {
          vm.isScroll = true;
        }
        vm.number = index;
        sessionStorage.currentSubscript = vm.number;
        let distance = vm.getScrollTop();
        let slcItem = document.getElementsByClassName('displayContent');
        let total = slcItem[index].getBoundingClientRect().top;
        let top = distance + total;

        document.body.scrollTop = top;
        document.documentElement.scrollTop = top;
        window.pageYOffset = top;
      },
      getScrollTop() {
        return document.documentElement.scrollTop || document.body.scrollTop;
      },
      getConLeft() {
        return vm.$refs.ptdetilContainer.offsetLeft;
      },
      listenScroll() {
        let mh =document.querySelector('#header').offsetHeight + 40;
        if(vm.getScrollTop() >= mh) {
          vm.setPostion(vm.$refs.subNavContainer, 'fixed', 115, vm.getConLeft() - 100);
          vm.scrollAnchor();
        }else{
          vm.setPostion(vm.$refs.subNavContainer, 'absolute', 115,-100);
        }
        vm.scrollTop = vm.getScrollTop();
        vm.scrollTop >= 600 ? vm.showBackTop = true : vm.showBackTop = false;
      },
      scrollAnchor() {
        if(!vm.isScroll){
          return false;
        }
        let section = document.getElementsByClassName('displayContent');
        for (let i = 0; i < section.length ; i++) {
          let bTop = section[i].getBoundingClientRect().top - 40;
          let offsetHeight = section[i].getBoundingClientRect().height;
          if ( bTop <= 0 && Math.abs(bTop) <= offsetHeight) {
            vm.number = i;
            sessionStorage.currentSubscript = vm.number;
            break;
          }
        }
      },
      setPostion(el, postion, top, left) {
        el.style.position = postion;
        el.style.top = top + 'px';
        el.style.left = left + 'px';
      },
    }
  }
</script>
<style lang="less" scoped>
  @import "../assets/css/common.less";
  .facilitiesDetails{
    background-color: @appBg;
    .contentDisplay{
      width: 1200px;
      margin: 40px auto;
      position: relative;
      background-color: #fff;
      .contentTitle{
        height: 75px;
        line-height: 75px;
        padding-left: 30px;
        font-size: 24px;
        color:#333;
        border-bottom: 2px solid #6aadff;
        .wave{
          font-size: 24px;
          color:#6aadff;
        }
      }
      .contentExhibition{
        padding: 40px 0;
        .classificationDisplay{
          height: 400px;
          position: relative;
          img{
            width: 50%;
            height: 400px;
          }
          .currentContent{
            position: absolute;
            top:50%;
            left:640px;
            width: 520px;
            transform: translateY(-50%);
            p{
              font-size: 20px;
              color:#333;
              text-align: center;
            }
            .introduce{
              font-size: 14px;
              color:#999999;
              margin-top: 10px;
            }
            .explain{
              background-color: #f7f7f7;
              padding: 10px;
              border:1px solid #e5e5e5;
              margin-top: 10px;
              >div{
                font-size: 14px;
                color:#999999;
                >div{
                  display: inline-block;
                  width: 57px;
                  text-align:justify;
                  text-align-last:justify;
                }
                span{
                  color:#333333;
                }
              }
            }
          }
        }
        .classificationDisplayTwo{
          img{
            float: right;
          }
          .currentContent{
            left:40px;
          }
        }
      }
      .navigationTitle{
        width: 80px;
        background-color: #fff;
        box-shadow: 0 0 24px rgba(0,0,0,.13);
        padding: 20px 0;
        text-align: center;
        position: absolute;
        top:115px;
        left:-100px;
        border-radius: 8px;
        ul{
          li{
            float: none;
            margin-top: 30px;
            font-size: 16px;
            color:#999999;
            cursor: pointer;
            div:nth-of-type(1){
              width: 56px;
              height: 56px;
              line-height: 64px;
              display: inline-block;
              border-radius: 28px;
              background-color: #f7f7f7;
              color:#999999;
            }
          }
          li:nth-of-type(1){
            margin-top: 0;
          }
          .currentState{
            color:#ff9b0b;
            div:nth-of-type(1){
              background-color: #ff9b0b;
              color:#fff;
            }
          }
        }
      }
    }
  }
</style>
