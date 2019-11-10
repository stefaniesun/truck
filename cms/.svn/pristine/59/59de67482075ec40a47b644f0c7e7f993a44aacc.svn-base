<template>
  <div id="beforLogin">
    <img v-if="status" class="success-img" src="../assets/img/loginSuccess.jpg" alt="">
  </div>
</template>

<script>
  let vm;
  import common from '@/assets/js/common'
  export default{
  	name: 'beforLogin',
    data(){
  		return{
        status: false,
        data: {}
      }
    },
    mounted(){
  		vm = this;
      let parame = common.getUrlParameters();
      let outkey = parame.outkey;
      sessionStorage.setItem('outkey',outkey);
      vm.code = sessionStorage.code;
      vm.data = {
      	openid: process.env.NODE_ENV !== 'development' ? parame.openid : 'wudong888',
        outkey: parame.outkey
      };
      if(vm.code) vm.data.randomCode = vm.code;
      if(sessionStorage.urlJson){
        let urlJson = JSON.parse(sessionStorage.urlJson);
        sessionStorage.setItem('userCode',urlJson.distributor);
        sessionStorage.setItem('numberCode',urlJson.voyage);
        sessionStorage.setItem('isQRcode',urlJson.isQRcode);
        sessionStorage.setItem('originalPath','/ptDetailToC');
      }
      vm.bindingOper();
    },
    methods:{
      bindingOper(){
        vm.axios.post(this.api+this.urlApi.validateWechatBindingOper,vm.data).then(function (rep) {
          if(rep.status){
            sessionStorage.setItem('possessor',rep.content.possessor);
          	if(vm.code){
              if(rep.content.isDistributor){
                vm.status = true;
              }else {
                sessionStorage.setItem('openid',vm.data.openid);
                vm.$router.replace('/binding');
              }
            }else {
              if(rep.content.isDistributor){
                sessionStorage.setItem('userType',true);
                let securityLogin = rep.content.securityLogin;
                vm.wechatLogin(securityLogin);
              }else {
                sessionStorage.setItem('openid',vm.data.openid);
              	if(sessionStorage.urlJson){
                  sessionStorage.setItem('userType',true);
                  vm.$router.replace('/binding');
                }else {
                  sessionStorage.setItem('userType',false);
                  vm.$router.replace('/productQuery');
                }
              }
            }
          }
        })
      },
      wechatLogin(obj){
        sessionStorage.setItem('apikey',obj.apikey);
        sessionStorage.setItem('saleLevel',obj.saleLevel);
        sessionStorage.setItem('rebatelevel',obj.rebatelevel);
        sessionStorage.setItem('userCode',obj.userCode);
        sessionStorage.setItem('openid',vm.data.openid);
        let originalPath = sessionStorage.originalPath;
        originalPath ? vm.$router.replace(originalPath) : vm.$router.replace('/productQuery');
      }
    }
  }
</script>

<style lang="less" scoped>
  @import "../assets/css/base.less";
  #beforLogin{
    width: 100%;
    height: 100%;
    position: relative;
    .success-img{
      width: 100%;
      height: 100%;
      display: block;
    }
  }
</style>
