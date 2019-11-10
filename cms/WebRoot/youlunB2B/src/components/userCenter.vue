<template>
  <div class="user-center" ref="userCenter">
    <header>
      <h2>个人中心</h2>
      <i class="back iconfont icon-fanhui" @click="goBack()"></i>
      <i class="home iconfont icon-shouye" @click="goHome()"></i>
    </header>

    <div class="avatar-wrap">
      <div class="avatar-img">
        <img :src="userImage" alt="avatar">
      </div>
      <h4 class="username">用户名：{{userName}}</h4>
    </div>
      <div class="container">
        <tab></tab>
      </div>
      <router-view></router-view>
    <footer class="footer"></footer>
  </div>
</template>

<script>
  let vm;
  import Tab from '@/components/tab'
  export default {
    name: 'userCenter',
    components: {
      Tab
    },
    data() {
      return {
        isFixed: 'fixed',
        userName: '',
        userImage: ''
      }
    },
    activated(){

    },
    mounted() {
      vm = this;
      if(process.env.NODE_ENV !== 'development'){
        let parame = {
          outkey: sessionStorage.getItem('outkey'),
          openid: sessionStorage.getItem('openid')
        }
        vm.axios.post(this.api + this.urlApi.getUserInfo,parame).then((response) => {
          if(response.status){
          	let content = response.content;
            vm.userName = content.nickname;
            vm.userImage = content.headimgurl;
          }
        })
      }else {
        vm.userName = '测试用户';
        vm.userImage = require('../assets/img/baseImg.png');
      }
      sessionStorage.userName = vm.userName;
    },

    methods: {
      goBack() {
        vm.$router.goBack();
      },
      goHome() {
        vm.$router.push('/productQuery');
      },
    }
  }
</script>

<style lang="less" scoped>
  @import "../assets/css/common.less";

  .avatar-wrap{
    height: 205*@basePX;
    background: #fff;
    text-align: center;
    border-bottom: 15*@basePX solid @appBg;
    padding-top: 88*@basePX;
    position: relative;
    h4{
      font-weight: normal;
      font-size: 32*@basePX;
      color: #4c4c4c;
      float: left;
      height: 100%;
      line-height: 102*@basePX;
      padding-left: 10*@basePX;
    }
    .avatar-img{
      width: 80*@basePX;
      height: 80*@basePX;
      float: left;
      .borderRadius(50%);
      overflow: hidden;
      margin: 11*@basePX 0 0 22*@basePX;
      img{
        width: 100%;
        height: 100%;
        max-height: 100%;
        max-width: 100%;
      }
    }
  }
  .footer{
    height: 24*@basePX;
    background-color: @topBg;
    bottom: 0;
    width: 100%;
    position: fixed;
  }

    .container{
      width: 92%;
      margin: 0 auto;

    }

</style>
