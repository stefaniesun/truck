<template>
  <div id="login">
    <div class="login-inner">
      <img src="../assets/img/logo.png" alt="">
      <div class="login-inner-box">
        <div class="login-box-switch clearfloat" ref="switch">
          <!-- 二维码登录 -->
          <div class="erwma-login">
            <p class="login-title">手机扫码，安全登录</p>
            <div id="qrcode">
              <img :src="QRcode" alt="">
            </div>
            <p class="scan-title">
              <svg class="icon" aria-hidden="true">
                <use xlink:href="#icon-saomiao"></use>
              </svg>
              <span>打开微信 扫一扫登录</span>
            </p>
            <div class="login-mode" >
              <span class="login-btn-switch" @click="pwdLogin()">
                <svg class="icon" aria-hidden="true">
                  <use xlink:href="#icon-zhanghao"></use>
                </svg>
                密码登录
              </span>
            </div>
          </div>
          <!-- 密码登录 -->
          <div class="pwd-login">
            <p class="login-title">密码登录</p>
            <div class="input-box">
              <div class="item clearfloat">
                <i class="icon-box">
                  <svg class="icon" aria-hidden="true">
                    <use xlink:href="#icon-zhanghao"></use>
                  </svg>
                </i>
                <input class="phone" type="text" placeholder="请输入账号" v-model="phone" />
              </div>
              <div class="item clearfloat">
                <i class="icon-box">
                  <svg class="icon" aria-hidden="true">
                    <use xlink:href="#icon-mima"></use>
                  </svg>
                </i>
                <input class="password" type="password" placeholder="请输入密码" v-model="password">
              </div>
              <div class="item clearfloat">
                <input type="text" class="yzm-input" placeholder="输入验证码" v-model="code">
                <div class="code" id="checkCode" title="点击更换验证码" @click="createCode()">{{res}}</div>
                <span @click="createCode()">换一张</span>
                <svg v-if="code == res.toLowerCase() || code == res.toUpperCase()" class="icon real-code" aria-hidden="true">
                  <use xlink:href="#icon-gou2"></use>
                </svg>
              </div>
            </div>
            <div class="login-btn">
                <button @click="login()" @keyup.enter="login()">登录</button>
            </div>
            <div class="login-mode clearfloat">
              <span class="login-btn-switch" @click="codeLogin()">
                <svg class="icon" aria-hidden="true">
                  <use xlink:href="#icon-erweima"></use>
                </svg>
                扫码登录
              </span>
              <span class="login-btn-switch" @click="goFingPwd()">
                <svg class="icon" aria-hidden="true">
                  <use xlink:href="#icon-12302"></use>
                </svg>
                忘记密码
              </span>
            </div>

          </div>
          <!-- 忘记密码 -->
          <div class="find-pwd">
            <div class="step-1" v-if="stepStatus == 0">
              <p class="login-title">忘记密码</p>
              <div class="input-box">
                <div class="input-item">
                  <input type="text" placeholder="请输入手机号" v-model="telephone" class="input-phone">
                  <div class="icon-phone">
                    <svg class="icon" aria-hidden="true">
                      <use xlink:href="#icon-shouji"></use>
                    </svg>
                  </div>
                </div>
                <div class="input-item clearfloat">
                  <input type="text" placeholder="输入验证码" class="input-yzm find-code-input" v-model="verificationCode">
                  <div class="code find-code" title="点击更换验证码" @click="createCode()">{{res}}</div>
                  <svg v-if="verificationCode == res.toLowerCase() || verificationCode == res.toUpperCase()" class="icon real-code" aria-hidden="true">
                    <use xlink:href="#icon-gou2"></use>
                  </svg>
                </div>
                <div class="input-item clearfloat">
                  <input type="text" placeholder="输入短信验证码" class="input-yzm" v-model="yanZhengMa">
                  <button class="send-yzm" @click="sendVerificationCode()" ref="button">{{yzmMsg}}</button>
                </div>
              </div>
              <div class="btn-box">
                <button @click="nextStep()">下一步</button>
              </div>
              <div class="login-mode clearfloat">
                <span class="login-btn-switch" @click="codeLogin()">
                <svg class="icon" aria-hidden="true">
                  <use xlink:href="#icon-erweima"></use>
                </svg>
                扫码登录
              </span>
                <span class="login-btn-switch" @click="backLogin()">
                <svg class="icon" aria-hidden="true">
                 <use xlink:href="#icon-zhanghao"></use>
                </svg>
                密码登录
              </span>
              </div>
            </div>
            <div class="step-2" v-if="stepStatus == 1">
              <p class="login-title">忘记密码</p>
              <div class="input-box">
                <div class="input-item">
                  <input type="password" placeholder="请输入新密码" v-model="newPwd" class="new-pwd">
                  <div class="icon-phone">
                    <svg class="icon" aria-hidden="true">
                      <use xlink:href="#icon-mima"></use>
                    </svg>
                  </div>
                </div>
                <div class="input-item">
                  <input type="password" placeholder="请再次输入新密码" v-model="newRePwd" class="new-repwd">
                  <div class="icon-phone">
                    <svg class="icon" aria-hidden="true">
                      <use xlink:href="#icon-mima"></use>
                    </svg>
                  </div>
                </div>
              </div>
              <div class="btn-box">
                <button @click="confirmPwd()">确认</button>
              </div>
              <div class="login-mode clearfloat">
              <span class="login-btn-switch" @click="codeLogin()">
                <svg class="icon" aria-hidden="true">
                  <use xlink:href="#icon-erweima"></use>
                </svg>
                扫码登录
              </span>
                <span class="login-btn-switch" @click="backLogin()">
                <svg class="icon" aria-hidden="true">
                 <use xlink:href="#icon-zhanghao"></use>
                </svg>
                密码登录
              </span>
              </div>
            </div>
            <div class="step-3" v-if="stepStatus == 2">
              <div class="yuan">
                <svg class="icon" aria-hidden="true">
                  <use xlink:href="#icon-gou1"></use>
                </svg>
              </div>
              <p>设置密码成功</p>
              <span @click="backLogin()"> 立即登录</span>
            </div>

          </div>
        </div>
      </div>
      <div class="distributor-apply-entry" @click="goDistributorApplyEntry">分销商申请入口>>></div>
    </div>
    <div class="link-wrap">
      <span onClick="window.open('http://www.miitbeian.gov.cn/')">版权所有 上海美匣网络科技有限公司 Copyright2017保留所有权利   沪ICP备16025219号-1</span>
    </div>
  </div>
</template>

<script>
  let vm;
  let clock = "";
  let nums = 60;
  import common from '@/assets/js/common.js';
  import QRCode from 'qrcode';
  let md5 = require ('js-md5');
  export default {
    name: 'login',
    data () {
      return {
        phone:"",         //账号
        password:"",      //密码
        code:"",          //输入验证码
        res:"",           //生成验证码
        QRcode: '',       //生成二维码
        expireDate:"",    //有效时间
        tipMsg: '',
        weChatCode: '',   //二维码标识
        stepStatus:0,
        telephone:"",     //忘记密码——手机号
        newPwd:"",        //忘记密码——新密码
        newRePwd:"",
        yanZhengMa:"",    //忘记密码——验证码
        yzmCode:"",       //忘记密码——获取到的验证码
        yzmMsg:"发送验证码",
        stopTime: 1000 * 60 * 5,
        verificationCode: ''
      }
    },
    mounted(){
      vm = this;
      sessionStorage.clear();
      if(!common.isIE10()){
        vm.dialog.showDialog('本系统暂不支持IE10以下浏览器，请使用火狐、谷歌、360、IE10及以上浏览器');
        return;
      }
      vm.getPossessorInfo();
      vm.createCode();
      vm.beforelogin();
      window.onkeydown = function(ev){
        if( ev.keyCode == 13 ){
          vm.login();
        }
      };
    },
    methods:{
      getPossessorInfo(){
      	let parame = {
          domain: location.hostname
        };
        vm.axios.post(this.api + this.urlApi.getPossessorInfo,parame).then(function (data) {
          if(data.status == 1){
          	let obj = data.content;
            vm.outkey = obj.outKey;
            sessionStorage.setItem('possessor',obj.possessor);
            sessionStorage.setItem('outkey',obj.outKey);
          }
        })
      },
      beforelogin:function(){
      	vm.phone = '';
        vm.password = '';
        if(window.timeOut) clearInterval(window.timeOut);
      },
      login:function () {   /* 登录 */
        if(!common.isIE10()){
          vm.dialog.showDialog('本系统暂不支持IE10以下浏览器，请使用火狐、谷歌、360、IE10及以上浏览器');
          return;
        }
        let tel = /^1[34578][0-9]{9}$/;
        let phone = vm.phone;
        let className = '';
        vm.tipMsg = '';
        if(!(vm.phone)){
          vm.tipMsg = "手机号不能为空";
          className = '.phone';
        }else if(!(tel.test(phone))){
          className = '.phone';
          vm.tipMsg ="手机号格式错误";
        } else if(!(vm.password)){
          vm.tipMsg = "密码不能为空";
          className = '.password';
        }else if(!(vm.code)){
          vm.tipMsg = "验证码不能为空";
          className = '.yzm-input';
        } else if(vm.code !== vm.res.toLowerCase() && vm.code !== vm.res.toUpperCase() ){
          vm.tipMsg ="验证码错误";
          className = '.yzm-input';
        }
        if(vm.tipMsg){
          vm.createCode();
          vm.tooltip.show(vm.tipMsg,className);
          return;
        }
        let params = {
          phone: vm.phone,
          password: md5(vm.password).substr(8,16),
          outkey: vm.outkey
        };
        vm.axios.post(this.api + this.urlApi.getLoginData,params).then(function (data) {
            if(data.status == 1){
              let obj = data.content;
              vm.getflag(obj);
            }else {
              vm.createCode();
            }
        })
      },
      createCode:function () {    /* 生成验证码 */
        vm.res = "";
        vm.code = "";
        let codeLength = 5;   ///验证码的长度
        let codeChars = new Array(0, 1, 2, 3, 4, 5, 6, 7, 8, 9,'a','b','c','d','e','f','g','h','j','k','m','n','o','p','q','r','s','t','u','v','w','x','y','z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z');
        for(let i = 0; i < codeLength; i++ ){
          let charNum = Math.floor(Math.random()*52);
          vm.res += codeChars[charNum];
        }
      },
      getflag(obj){
        sessionStorage.setItem('apikey',obj.apikey);
        sessionStorage.setItem('userName',obj.nameCn);
        sessionStorage.setItem('rebatelevel',obj.rebatelevel);
        sessionStorage.setItem('saleLevel',obj.saleLevel);
        sessionStorage.setItem('phone',vm.phone);
        sessionStorage.setItem('userCode',obj.userCode);
        vm.goHome();
      },
      codeLogin:function () {    /*PC扫码登录二维码接口*/
        let loginain = vm.$refs.switch.style;
        loginain.marginLeft = "0px";
        let btn = vm.$refs.button;
        if(btn){
          btn.removeAttribute('disabled');
          btn.style.color = "#6aadff";
        }
        if(!vm.QRcode){
          vm.axios.post(this.api + this.urlApi.getWechatLoginCode).then(function (data) {
            if(data.status == 1){
              vm.weChatCode = data.content.code;
              let baseUrl;
              if( process.env.NODE_ENV == 'development' ){
                baseUrl = 'http://192.168.1.122:8080/#/?code=';
              }else {
                baseUrl = location.origin + '/weChat.html#/?code=';
              }
              let url = baseUrl + vm.weChatCode + '&outkey=' + vm.outkey;
              console.log(url)
              QRCode.toDataURL(url,{ errorCorrectionLevel:'H' } , function (err, url) {
                vm.QRcode = url;
              })
              vm.expireDate = data.content.expireDate;
              vm.resWeCode();
            }
          })
        }else {
          vm.resWeCode();
        }
      },
      resWeCode(){
        let paramsHaomiao = (new Date(vm.expireDate)).getTime(); //有效时间转化为毫秒数
        window.timeOut = setInterval(function () {
          let currentTime = new Date().getTime();  //获取当毫秒数
          vm.stopTime -= 1000;
          if( vm.stopTime <= 0 || paramsHaomiao <= currentTime){
            vm.dialog.showDialog({
              btnText:'立即刷新',
              msg: '验证码已过期,请刷新页面重新获取'
            },function (flag) {
            	flag && location.reload();
            });
            vm.QRcode = '';
            clearInterval(window.timeOut);
            return;
          }
          vm.scanLogin();
        },1000);
      },
      scanLogin:function () {    //扫码登录
        vm.axios.post(this.api + this.urlApi.validateWechatLoginOper,{randomCode:vm.weChatCode}).then(function (data) {
        	if(data.status){
            clearInterval(window.timeOut);
            let obj = data.content;
            vm.getflag(obj);
          }else {
        		if(data.msg) clearInterval(window.timeOut);
          }
        })
      },
      pwdLogin:function () {      /* 密码登录 */
        clearInterval(window.timeOut);
        vm.createCode();
        let loginain = vm.$refs.switch.style;
        loginain.marginLeft = "-360px";
      },
      goHome:function () {
        clearInterval(window.timeOut);
        vm.$router.push('/productQuery');
      },
      goDistributorApplyEntry:function () {    /* 分销商入口 */
        let url = location.href.split('#/')[0];
      	let path = '#/distributorApply';
        window.open(url + path);
      },
      goFingPwd:function () {   /* 忘记密码 */
        clearInterval(clock);
        clearInterval(window.timeOut);
        let btn = vm.$refs.button;
        if(btn){
          btn.removeAttribute('disabled');
          btn.style.color = "#6aadff";
        }
        vm.yzmMsg = "发送验证码";
        let m = vm.$refs.switch;
        m.style.marginLeft = "-720px";
        vm.stepStatus = 0;
        vm.telephone = "";
        vm.yanZhengMa = "";
      },
      backLogin:function () {   /* 返回登录 */
        vm.createCode();
        let btn = vm.$refs.button;
        if(btn){
          btn.removeAttribute('disabled');
          btn.style.color = "#6aadff";
        }
        let m = vm.$refs.switch;
        m.style.marginLeft = "-360px";
        vm.stepStatus = 0;
      },
      sendVerificationCode:function () {     /* 发送验证码 */
        let tel = /^1[34578][0-9]{9}$/;
        vm.tipMsg = "";
        let className = "";
        if(!vm.telephone){
          vm.tipMsg = "手机号不能为空";
          className = ".input-phone";
        }else if(!(tel.test(vm.telephone))){
          vm.tipMsg = "手机号格式错误";
          className = ".input-phone";
        }
        if(vm.tipMsg){
          vm.tooltip.show(vm.tipMsg,className);
          return;
        }
        let parame = {
          phone:vm.telephone
        };
        vm.axios.post(this.api+this.urlApi.sendVerificationCode,parame).then(function (data) {
          if(data.status == 1){
            if(data.content.status == 1){
              vm.yzmCode = data.content.content;
              let btn = vm.$refs.button;
              btn.setAttribute('disabled','disabled');
              btn.style.color = "#ccc";
              vm.yzmMsg = "已发送 "+nums+"秒";
              clock = setInterval(vm.doLoop,1000);
            }else{
              vm.dialog.showDialog(data.content.content);
            }
          }
        })
      },
      doLoop:function(){    /* 验证码倒计时 */
        nums--;
        if(nums > 0){
          vm.yzmMsg = '已发送 '+ nums + '秒';
        }else{
          clearInterval(clock);
          let btn = vm.$refs.button;
          btn.removeAttribute('disabled');
          btn.style.color = "#6aadff";
          vm.yzmMsg = "重新发送验证码";
          nums = 60;
        }
      },
      nextStep:function () {    //验证短信验证码
        vm.stepStatus = 1;
        return
        let tel = /^1[34578][0-9]{9}$/;
        vm.tipMsg = "";
        let className = "";
        if(!vm.telephone){
          vm.tipMsg = "手机号不能为空";
          className = ".input-phone";
        }else if(!vm.yanZhengMa){
          vm.tipMsg = "验证码不能为空";
          className = ".input-yzm";
        }else if(!(tel.test(vm.telephone))){
          vm.tipMsg = "手机号格式错误";
          className = ".input-phone";
        }else if(vm.yanZhengMa != vm.yzmCode){
          vm.tipMsg = "验证码错误";
          className = ".input-yzm";
        }

        if(vm.tipMsg){
          vm.tooltip.show(vm.tipMsg,className);
          return;
        }
        let parame = {
          phone:vm.telephone,
          code:vm.yanZhengMa
        };
        vm.axios.post(this.api+this.urlApi.verificationCode,parame).then(function (data) {
          if(data.status == 1){
            vm.stepStatus = 1;
          }
        });

      },
      confirmPwd:function () {     /* 确认密码 */
        vm.tipMsg = "";
        let className = "";
        if(!vm.newPwd){
          vm.tipMsg = "密码不能为空";
          className = ".new-pwd";
        }else if(!vm.newRePwd){
          vm.tipMsg = "密码不能为空";
          className = ".new-repwd";
        }else if(vm.newPwd != vm.newRePwd){
          vm.tipMsg = "两次输入密码不一致";
          className = ".new-repwd";
        }

        if(vm.tipMsg){
            vm.tooltip.show(vm.tipMsg,className);
            return;
        }
        let parame = {
          phone:vm.telephone,
          newPassword:md5(vm.newPwd).substr(8,16)
        };
        vm.axios.post(this.api+this.urlApi.setPassword,parame).then(function (data) {
          if(data.status == 1){
            vm.stepStatus = 2;
          }
        });
      }
   }
  }
</script>



<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="less" scoped>
  @import "../assets/css/common";

  @bdrColor:#e9e9e9;
  @iconColor:#e6e6e6;
  @blueColor:#6aadff;
  #login{
    width: 100%;
    height: 100%;
    background-image: url("../assets/img/loginBg.jpg");
    background-size: 100% 100%;
    min-height: 947px;
    position: relative;
    .login-inner{
      width: 400px;
      left: 50%;
      top: 174px;
      margin-left: -200px;
      position: absolute;
      text-align: center;
      .login-inner-box{
        width: 360px;
        height: 360px;
        background-color: #fff;
        margin:50px auto 0;
        box-shadow: 0 0 30px rgba(58,135,229,0.3);
        border-radius: 2px;
        overflow: hidden;
        .login-box-switch{
          width: 1080px;
          height: 360px;
          background-color: #fff;
          padding-top: 35px;
          transition: all 1s;
          margin-left: -360px;
          .erwma-login,.pwd-login,.find-pwd{
            width: 33.333%;
            float: left;
            .login-title{
              color: @blueColor;
              font-size: 18px;
              text-align: center;
            }
            #qrcode{
              width: 200px;
              height: 200px;
              margin: auto;
              img{
                width: 200px;
                height: 200px;
              }
            }
            .scan-title{
              margin-top:13px;
              .icon{
                font-size: 30px;
                display: inline-block;
                vertical-align: middle;
                margin-right:5px;
              }
              span{
                font-size: 16px;
                color: #666;
                display: inline-block;
                vertical-align: middle;
              }
            }
            .login-mode{
              width:250px;
              margin:20px auto auto;
              .login-btn-switch{
                display:inline-block;
                text-align: center;
                color: #ccc;
                font-size: 16px;
                cursor: pointer;
              }
              .login-btn-switch:hover{
                color: @blueColor;
              }
            }

          }
          .real-code{
            width: 20px;
            height: 20px;
            color: @tipsColor;
            margin-top: 8px;
          }
          .pwd-login{
            .input-box{
              width: 268px;
              margin:30px auto 0;
              position: relative;
              .warn{
                position: absolute;
                color: #f00;
                font-size: 12px;
                left: 2px;
                top: -20px;
              }
              .item{
                width: 100%;
                height: 36px;
                border-radius: 6px;
                border:1px solid @bdrColor;
                margin-bottom: 20px;
                position: relative;
                .real-code{
                  position: absolute;
                  top: -8px;
                  right: 9px;
                }
                .icon-box{
                  float: left;
                  border-right: 1px solid @bdrColor;
                  padding:4px 10px;
                  margin-top: 3px;
                  .icon{
                    color: @iconColor;
                    font-size: 18px;
                  }
                }
                input{
                  float: left;
                  padding: 0 7px;
                  height: 34px;
                  border: 0;
                  outline: none;
                }
                .ivu-poptip{
                  float: left;
                }
              }
              .item:nth-child(3){
                border:0;
                margin-bottom: 0;
                .yzm-input{
                  width: 117px;
                  border:1px solid @bdrColor;
                  border-radius: 6px;
                  text-align: center;
                }
                span{
                  color: @blueColor;
                  font-size: 12px;
                  margin-top: 19px;
                  display: inline-block;
                  float: right;
                  cursor: pointer;
                }
              }
            }
            .login-btn{
              width: 100%;
              text-align: center;
              margin-top:30px;
              button{
                width: 268px;
                height: 40px;
                border-radius: 6px;
                border:0;
                outline: none;
                color: #fff;
                font-size: 18px;
                background-color: @blueColor;
                cursor: pointer;
              }
            }
            .login-mode{
              margin-top: 16px;
              span:first-child{
                float: left;
              }
              span:nth-child(2){
                float: right;
              }
            }
          }
          .find-pwd{
            .btn-box{
              width: 100%;
              text-align: center;
              button{
                width: 268px;
                height: 40px;
                border: 0;
                outline: none;
                color: #fff;
                background-color: @topBg;
                font-size: 16px;
                .borderRadius();
                cursor: pointer;
              }
            }
            .step-1,.step-2{
              width: 100%;
              padding: 0 46px;
              .input-box{
                margin-top: 40px;
              }
              .input-item{
                position: relative;
                .icon-phone{
                  display: inline-block;
                  padding: 0 8px;
                  border-right: 1px solid #e9e9e9;
                  position: absolute;

                }
                .send-yzm{
                  font-size: 15px;
                  line-height: 36px;
                  cursor: pointer;
                  color: @topBg;
                  background-color: #fff;
                  border: 0;
                  outline: none;
                }
                input{
                  width: 100%;
                  height: 36px;
                  .borderRadius();
                  border:1px solid #e9e9e9;
                  margin-bottom: 20px;
                  outline:none ;
                  padding-left: 46px;
                }
                input:focus{
                  box-shadow: 0 0 10px rgba(58, 135, 229, 0.3);
                }
                input::-webkit-input-placeholder{
                  color:#ccc;
                  font-size: 14px;
                }
              }

            }
            .step-1{
              .icon-phone{
                top: 7px;
                left:0;
                .icon{
                  font-size: 22px;
                  color: #ccc;
                }
              }
              input.input-yzm{
                width: 159px;
                float: left;
                padding-left: 10px;
              }
              input.find-code-input{
                width: 120px;
              }
            }
            .step-2{
              .icon-phone{
                top: 9px;
                left: 0;
                .icon{
                  color: #ccc;
                  font-size: 17px;
                }
              }

            }
            .step-3{
              .yuan{
                width: 74px;
                height: 74px;
                background-color: #fff;
                .borderRadius(@radius: 50%);
                box-shadow: 0 0 10px rgba(58, 135, 229, 0.3);
                margin:80px auto auto;
                .icon{
                  color: @topBg;
                  font-size: 47px;
                  margin-top: 15px;
                }

              }
              p{
                text-align: center;
                font-size: 18px;
                color: #666;
                margin: 10px 0;
              }
              span{
                color: @topBg;
                font-size: 18px;
                cursor: pointer;
              }
            }
            .login-mode{
              margin-top: 16px;
              span:first-child{
                float: left;
              }
              span:nth-child(2){
                float: right;
              }
            }
          }
        }
      }
      .code{
        width: 92px;
        height: 34px;
        float: left;
        margin-left: 12px;
        background-color:#eeeded ;
        color: #236789;
        font-size: 21px;
        font-family: Arial;
        font-style: italic;
        line-height: 34px;
        font-weight: bold;
        cursor: pointer;
      }
      .distributor-apply-entry{
        width: 360px;
        height: 40px;
        background-color: #fff;
        margin: 10px auto;
        line-height: 40px;
        color: @topBg;
        font-size: 16px;
        box-shadow: 0 0 30px rgba(58, 135, 229, 0.3);
        .borderRadius;
        cursor: pointer;
      }
    }
    .link-wrap{
      width: 1046px;
      height: 34px;
      line-height: 34px;
      position: absolute;
      left: 50%;
      bottom: 60px;
      margin-left: -523px;
      text-align: center;
      font-size: 14px;
      background: url("../assets/img/linkBg.png") no-repeat;
      background-size: 100% 100%;
      span{
        cursor: pointer;
      }
    }
  }
</style>
