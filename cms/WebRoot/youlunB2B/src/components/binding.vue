<template>
  <div id="binding">
    <div v-if="status" class="bg"></div>
    <div v-if="status" class="main">
      <ul class="input-panel">
        <li v-for="(item,index) in panelData" class="item">
          <div :class="'icon-'+index" class="icon">
            <i class="iconfont" :class="item.icon"></i>
          </div>
          <span>{{item.title}}</span>
          <input v-model="item.value" :placeholder="item.placeholder" :class="item.className" :type="index ? 'password' : 'text'">
        </li>
      </ul>
      <div @click="submit" class="btn">
        <span>绑定</span>
      </div>
      <div @click="goRegister" class="register">
        分销商注册
      </div>
    </div>
    <img v-if="!status" class="success-img" src="../assets/img/loginSuccess.jpg" alt="">
  </div>
</template>

<script>
  let vm;
  let md5 = require ('js-md5');
  export default{
  	name: 'binding',
    data(){
  		return{
  			panelData: [
          {
            title: '电话',
            icon: 'icon-icon-copy',
            placeholder: '请输入手机号',
            value: '',
            className: 'phone'
          },
          {
            title: '密码',
            icon: 'icon-mima',
            placeholder: '请输入密码',
            value: '',
            className: 'passWord'
          }
        ],
        status: true
      }
    },
    mounted(){
      vm = this;
    },
    methods: {
      submit(){
      	let msg = '';
      	let className = '';
      	if( !vm.panelData[0].value ){
          msg = '请先输入手机号';
          className = '.phone';
        }else if( !vm.panelData[1].value ){
          msg = '请先输入密码';
          className =  '.passWord';
        }
        if(msg){
          vm.tooltip.show(msg,className);
        	return;
        }
        let code = sessionStorage.code;
        let deviceType = code ? 'pc' : 'wechat';
        let openId = sessionStorage.openid;
        let parame = {
          openId: openId,
          phone: vm.panelData[0].value,
          password: md5(vm.panelData[1].value).substr(8,16),
          type: deviceType,
          possessor: sessionStorage.getItem('possessor'),
          outKey: sessionStorage.outkey
        }
        if(code) parame.code = code;
        vm.axios.post(this.api+this.urlApi.bindingWechatOper,parame).then(function (data) {
          if(data.status){
            let obj = data.content;
            sessionStorage.setItem('apikey',obj.apikey);
            sessionStorage.setItem('rebatelevel',obj.rebatelevel);
            sessionStorage.setItem('saleLevel',obj.saleLevel);
            sessionStorage.setItem('userCode',obj.userCode);
            sessionStorage.removeItem('code');
            if(deviceType !== 'pc') {
              let originalPath = sessionStorage.originalPath;
              originalPath ? vm.$router.replace(originalPath) : vm.$router.replace('/productQuery');
            }else {
            	vm.status = false;
            }
          }
        })
      },
      goRegister(){
        vm.$router.replace('/distributorApply');
      }
    }
  }
</script>

<style lang="less" scoped>
  @import "../assets/css/base.less";
  #binding{
    width: 100%;
    height: 100%;
    .success-img{
      width: 100%;
      height: 100%;
      display: block;
    }
  }
  .bg{
    width: 100%;
    height: 320*@basePX;
    background-image: url("../assets/img/bindingBg.jpg");
    background-size: 100% 100%;
  }
  .main{
    width: 600*@basePX;
    height: 594*@basePX;
    margin: 180*@basePX auto 0;
    .input-panel{
      width: 100%;
      .item{
        width: 100%;
        height: 80*@basePX;
        color: @changeBtnBg;
        font-size: 32*@basePX;
        border-bottom: 1px solid @changeBtnBg;
        padding: 16*@basePX 10*@basePX;
        margin-bottom: 40*@basePX;
        .icon{
          float: left;
          margin-right: 10*@basePX;
          i{
            vertical-align: baseline;
            display: inline-block;
            font-size: 32*@basePX;
          }
        }
        .icon-0{
          margin-top: 4*@basePX;
        }
        input{
          width: 438*@basePX;
          height: 20*@basePX;
          padding: 25*@basePX 0;
          background-color: #fff;
          border: none;
          outline: none;
          margin-left: 10*@basePX;
          font-size: 32*@basePX;
        }
      }
    }
    .btn{
      margin-top: 56*@basePX;
      width: 100%;
      height: 88*@basePX;
      line-height: 88*@basePX;
      text-align: center;
      color: #fff;
      .borderRadius(88*@basePX);
      background-color: @clickBtnBg;
      font-size: 32*@basePX;
    }
    .register{
      margin-top: 46*@basePX;
      float: right;
      color: @changeBtnBg;
      font-size: 32*@basePX;
    }
  }
</style>
