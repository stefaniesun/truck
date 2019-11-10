<template>
  <div class="explain-container" ref="explainContainer">
    <div class="tab clearfloat" ref="tab">
      <div class="tab-item " v-for="(tabItem, index) in infoList" :class="{active: index === jumpIndex}" @click="jump(index)">
        <span class="tab-link">
          {{tabItem.name}}
        </span>
      </div>

    </div>

    <section class="explain-content" v-for="(item, index) in infoList">
      <h3 class="info-title">
        <span>{{item.name}}</span>
      </h3>
      <div class="detail">
        <div class="info-list" v-html="item.content"></div>
      </div>
    </section>

  </div>
</template>

<script>
  let vm;
  export default {
    name: 'infoExplain',
    data() {
      return {
        jumpIndex: 0,
//        infoList: []
      }
    },
    props: ['infoList'],
    created() {
    },

    mounted() {
      vm = this;
      window.addEventListener("scroll", vm.listenScroll);
    },

    destroyed() {
      window.removeEventListener("scroll", vm.listenScroll);
    },
    methods: {
      getScrollTop() {
        return document.documentElement.scrollTop || document.body.scrollTop;
      },

      listenScroll() {
        let mt = document.querySelector('.explain-container').offsetTop;
        let headerH = document.querySelector('header').offsetHeight;
        mt -= headerH;
        if (vm.getScrollTop() >= mt) {
          vm.setPosition(vm.$refs.tab, 'fixed',headerH);
          vm.scrollChange();
        }else {
          vm.setPosition(vm.$refs.tab, 'static', 0)
        }
      },

      setPosition(el, position, top) {
        el.style.position = position;
        if(position === 'fixed') {
          el.style.top = top + 'px';
        }else{
          el.style.marginTop = top + 'px';
        }
      },

      jump(index) {
        vm.jumpIndex = index;

        let distance = vm.getScrollTop();
        let slcItem = document.querySelectorAll('.explain-content');
        let total = slcItem[index].getBoundingClientRect().top ;
        let headerH =  document.querySelector('header').offsetHeight;
        let tabH = document.querySelector('.tab').offsetHeight;

        let  scrollTop = index === 0 ? distance + total -headerH : distance + total - headerH - tabH;
        document.body.scrollTop = scrollTop;
        window.pageYOffset = scrollTop;
        document.documentElement.scrollTop  = scrollTop;
      },

      scrollChange() {
        let sectionItem = document.querySelectorAll('.explain-content');
        for (let i = 0; i < sectionItem.length; i++) {
          let itemTop = sectionItem[i].getBoundingClientRect().top;
          if(itemTop-24 <= document.querySelector('.tab').offsetHeight + document.querySelector('header').offsetHeight) {
            vm.jumpIndex = i;
          }
        }
      }

    }
  }
</script>

<style lang="less" scoped>
  @import "../assets/css/base.less";
  .explain-container{
    padding-top: 0;
    width: 100%;
    margin: 24*@basePX auto auto;
    .explain-content{
      width: 94%;
      .borderRadius;
      margin: 24*@basePX auto auto;
      background-color: #fff;
      padding: 24*@basePX 24*@basePX 40*@basePX;
    }
  }

  .tab{
    width: 100%;
    height: 88*@basePX;
    background-color: #fff;
    z-index: 5;
    .tab-item{
      height: 88*@basePX;
      line-height: 88*@basePX;
      float: left;
      width: 25%;
      text-align: center;
      border-bottom: 3*@basePX solid #fff;
      .tab-link{
        color: #ccc;
        font-size: 32*@basePX;
      }
      &.active{
        border-bottom: 3*@basePX solid @topBg;
        .tab-link{
          color: @topBg;
        }
      }
    }
  }

  .info-title{
    margin: 0 auto;
    color: @topBg;
    font-size: 32*@basePX;
    font-weight: normal;
    text-align: center;
    span{
      display: block;
      position: relative;
      line-height: 37*@basePX;
      &:before,
      &:after{
        position: absolute;
        top: 50%;
        content: '';
        background-color: @topBg;
        height: 2px;
        width: 35%;
      }
      &:before{
        left: 0;
      }
      &:after{
        right: 0;
      }
    }
  }

  .detail{
    .info-list{
      padding: 23*@basePX 12*@basePX ;
      background-color: #eef7ff;
      margin-top: 24*@basePX;
      .borderRadius;
      font-size: 28*@basePX;
    }
  }
</style>
