<template>
  <div id="resetPwd">
    <Mheader></Mheader>
    <div class="main">
      <div class="main-inner">
        <p>修改密码</p>
        <ul>
          <li v-for="pwd in pwdCon" ref="pwd">
            <span>{{pwd.title}} <i>*</i></span>
            <input type="password" :placeholder="pwd.placeholder" v-model="pwd.message">
          </li>
        </ul>
        <div class="box-btn">
          <button @click="submit">确认</button>
          <button @click="goBack">取消</button>
        </div>
      </div>
    </div>
    <Mfooter></Mfooter>
  </div>
</template>

<script>
  import Mheader from "@/components/header.vue";
  import Mfooter from "@/components/footer.vue";
  import Vue from 'vue';

  let vm;
  let md5 = require("js-md5");
  export default{
    name:"repwd",
    components:{
      Mheader,
      Mfooter
    },
    data(){
      return{
        pwdCon:[
          {
            title:"旧密码",
            message:"",
            placeholder:"请输入旧密码",
          },
          {
            title:"新密码",
            message:"",
            placeholder:"请输入新密码",
          },
          {
            title:"确认新密码",
            message:"",
            placeholder:"请输入新密码",
          }
        ],
        tipMsg:""
      }
    },
    created:function () {
      vm = this;
    },
    methods:{
        submit:function () {
          let phone = sessionStorage.phone;
          let oldpwd = vm.$refs.pwd[0].querySelector("input");
          oldpwd.className = 'oldpwd';
          let newpwd = vm.$refs.pwd[1].querySelector("input");
          newpwd.className = 'newpwd';
          let repwd = vm.$refs.pwd[2].querySelector("input");
          repwd.className = 'repwd';

          oldpwd = vm.pwdCon[0].message;
          newpwd = vm.pwdCon[1].message;
          repwd = vm.pwdCon[2].message;

          let className = "";
          vm.tipMsg = "";
          if(!oldpwd){
            vm.tipMsg = "旧密码不能为空！";
            className = ".oldpwd";
          }else if(!newpwd){
            vm.tipMsg = "新密码不能为空！";
            className = ".newpwd";
          }else if(!repwd){
            vm.tipMsg = "新密码不能为空！";
            className = ".repwd";
          }else if(oldpwd == newpwd){
            vm.tipMsg = "新密码不能与旧密码相同";
            className = ".newpwd";
          }else if(newpwd != repwd){
            vm.tipMsg = "两次输入的密码不一致！";
            className = ".repwd";
          }

          if(vm.tipMsg){
              vm.tooltip.show(vm.tipMsg,className);
              return;
          }

          let params = {
            oldPassword : md5(oldpwd).substr(8,16),
            newPassword : md5(newpwd,repwd).substr(8,16),
            repassword : md5(repwd).substr(8,16),
            phone: phone,
          };
          vm.axios.post(vm.api + vm.urlApi.resetPwd,params).then(function (data){
            if(data.status == 1){
              vm.dialog.showDialog("密码修改成功,请重新登陆!");
              sessionStorage.clear();
              vm.$router.replace("/login");
            }
          })
        },
        goBack:function () {
          vm.$router.goBack();
        }
    }
  }
</script>

<style lang="less" scoped>
  @import "../assets/css/common";

  #resetPwd{
    .main{
      background-color: #fff;
      width: 1200px;
      height: 600px;
      margin:40px auto;
      .main-inner{
        width: 300px;
        margin:auto;
        p{
          padding-top: 80px;
          font-size: 22px;
          color: #5590d9;
          text-align: center;
        }
        li{
          float: none;
          margin-bottom: 30px;
          span{
            display: block;
            margin-bottom: 10px;
            i{
              color: #f00;
              font-style: normal;
            }
          }
          input{
            width: 100%;
            display: block;
            height: 34px;
            padding:0 15px;
            .borderRadius();
            border:1px solid #ccc;
            outline: none;
          }
          input:focus{
            box-shadow: 0 0 7px rgba(36, 106, 191, 0.38);
          }
        }
        .box-btn{
          width: 100%;
          text-align: left;
          button{
            width: 120px;
            height: 44px;
            color: #fff;
            font-size: 20px;
            .borderRadius(@radius: 40px;);
            border:0;
            margin-right: 10px;
            background-color: #ccc;
            cursor: pointer;
            outline: none;
          }
          button:nth-child(1){
            background-color: @clickBtnBg;
          }
        }
      }
    }
  }
</style>
