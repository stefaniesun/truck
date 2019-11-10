<template>
  <div class="modifyContacts">
    <div class="modifyContent">
      <img src="../assets/img/dialog_head.png"/>
      <p>修改联系人信息</p>
      <div class="contactsName">
        <p>联系人姓名</p>
        <input type="text" placeholder="请输入联系人姓名" v-model="info.LinkMan">
      </div>
      <div class="contactsName">
        <p>联系人电话</p>
        <input type="text" placeholder="请输入联系人电话" v-model="info.LinkPhone">
      </div>
      <div class="button" @click="buttonClick(false)">取消</div>
      <div class="button confirmButton" @click="buttonClick(true)">确认修改</div>
    </div>
  </div>
</template>
<script>
  let vm;
  import common from '@/assets/js/common';
  export default {
    name:'modifyContacts',
    props:['link'],
    data(){
        return{
          info : new Object
        }
    },
    mounted(){
      vm = this;
      vm.info = JSON.parse(JSON.stringify(vm.link));
    },
    methods :{
      buttonClick(flag){
        let link = vm.info;
        if(flag){
          if(!common.xyzIsPhone(link.LinkPhone)){
            vm.dialog.showDialog('手机号格式错误！');
            return;
          }
          let parame = {
            order : link.numberCode,
            username : link.LinkMan,
            phone : link.LinkPhone,
          };
          if( sessionStorage.userType === 'false' ){
            parame.userName = sessionStorage.userName;
          }
          this.axios.post(this.api+this.urlApi.editOrderLinkInfo,parame).then(function (respones) {
            if(respones.status == 1){
            	let upData = {
            		status: flag,
                linkObj: vm.link
              }
              vm.$emit('confirmAll',upData);
            }
          })
        }else {
          let upData = {
            status: flag,
            linkObj: vm.link
          }
          vm.$emit('confirmAll',upData);
        }
      }
    }
  }
</script>
<style lang="less">
  @import "../assets/css/common.less";
  .modifyContacts{
    width: 100%;
    height: 100%;
    background-color: rgba(0,0,0,.35);
    position: fixed;
    top:0;
    left:0;
    z-index: 15;
    .modifyContent{
      width: 600*@basePX;
      height: 675*@basePX;
      position: absolute;
      top:50%;
      left:50%;
      transform: translate(-50%,-50%);
      border-bottom: 14*@basePX solid @changeBtnBg;
      background-color: #fff;
      border-radius: 8*@basePX;
      img{
        width: 100%;
        margin-top: -1px;
        border-radius: 8*@basePX 8*@basePX 0 0;
      }
      >p{
        font-size: 36*@basePX;
        color:#fff;
        text-align: center;
        position: absolute;
        top:20*@basePX;
        left: 0;
        width: 100%;
      }
      .contactsName{
        margin-top: 30*@basePX;
        padding: 0 40*@basePX;
        font-size: 32*@basePX;
        p{
          color:#333;
        }
        input{
          width: 100%;
          height: 73*@basePX;
          border-radius: 8*@basePX;
          border:1px solid #c2d6ee;
          color:#666;
          outline: none;
          margin-top: 15*@basePX;
          padding-left: 22*@basePX;
          font-size: 30*@basePX;
        }
      }
      .button{
        width: 220*@basePX;
        height: 78*@basePX;
        border-radius: 39*@basePX;
        display: inline-block;
        margin: 80*@basePX 37*@basePX 0 37*@basePX;
        background-color: @changeBtnBg;
        text-align: center;
        font-size: 32*@basePX;
        line-height: 78*@basePX;
        color:#fff;
      }
      .confirmButton{
        background-color:@clickBtnBg;
      }
    }
  }
</style>
