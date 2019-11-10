<template>
  <div id="person-wrap">
    <header>
      <h2>出行人信息</h2>
      <i class="back iconfont icon-fanhui" @click="showPerson.status = false;bodyNoScrolling.close()"></i>
    </header>
    <div class="main">
      <div class="container">
        <div class="panel-main">
          <div v-for="(item,index) in personInfo.cabinList" class="item clearfloat">
            <div v-for="(pItem,pIndex) in item.personArr" style="padding-bottom: 10px">
              <p class="title">
                {{item.cabinName}}（房间{{pIndex+1}}）
              </p>
              <div v-for="(subItem,subIndex) in pItem" class="info clearfolat">
                <p class="info-top">
                  <i class="iconfont icon-gerenzhongxinzhuyegerenziliao"></i>
                  <span>出行人{{subIndex+1}}</span>
                </p>
                <ul class="info-input clearfloat">
                  <li>
                    <span>出行人姓名</span>
                    <input v-model="subItem.name" type="text" placeholder="请输入出行人姓名">
                  </li>
                  <li>
                    <span>出行人身份证号</span>
                    <input v-model="subItem.cardNumber" type="text" placeholder="请输入出行人身份证号">
                  </li>
                </ul>
                <ul class="info-pic clearfloat">
                  <li>
                    <span>身份证照片</span>
                    <div class="pic">
                      <xyz-uploader
                        :id="'pic_'+index+'_'+ pIndex+ '_' +subIndex+'_ID'"
                        :urls="subItem.cardImage?subItem.cardImage.split(','):[]"
                        :count="2"
                        :multiple="true"
                        :repeat="true"
                        @complete="complete"
                        @fail="fail"
                        @remove-item="removeItem"
                        @showImage="showImage">
                      </xyz-uploader>
                    </div>
                  </li>
                  <li>
                    <span>护照照片</span>
                    <div class="pic">
                      <xyz-uploader
                        :id="'pic_'+index+'_'+ pIndex+ '_' +subIndex+'_Pass'"
                        :urls="subItem.passportImage?subItem.passportImage.split(','):[]"
                        :count="2"
                        :multiple="true"
                        :repeat="true"
                        @complete="complete"
                        @fail="fail"
                        @remove-item="removeItem"
                        @showImage="showImage">
                      </xyz-uploader>
                    </div>
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="btn-box">
        <button @click="personConfirm">确定</button>
      </div>
    </div>
  </div>
</template>

<script>
  let vm
  import xyzUploader from "@/components/xyzUploader";
  export default{
  	name: 'personPanel',
    components:{
      xyzUploader
    },
    props: {
      showPerson: {
        type: Object
      },
      order: {
        type: String
      },
    },
    data(){
  		return{
        personInfo: new Object
      }
    },
    mounted(){
      vm = this;
      vm.getPersonPanel();
    },
    methods: {
      getPersonPanel(){
        let personInfo = {};
        personInfo.order = vm.order;
        personInfo.cabinList = new Array;
        vm.axios.post(this.api+this.urlApi.getPersonInfo,{order:vm.order}).then(function (respones) {
          if(respones.status){
            let personList = respones.content;
            let cabinObjArr = new Array;
            personList.forEach(function (item,index) {
              if(cabinObjArr.length){
                let onOff = true;
                cabinObjArr.forEach(function(subItem,subIndex){
                  if(subItem.cabinName === item.cabinName){
                    onOff = false;
                    let personArr = subItem.personArr;
                    personArr.forEach(function (pItem,Pindex) {
                      if(pItem.length){
                        let swicth = false;
                        pItem.forEach(function(spItem){
                          if(spItem.cabinNum === item.cabinNum ){
                            swicth = true;
                          }
                        })
                        if(swicth){
                          pItem.push(item);
                        }else {
                          personArr[Pindex+1] = new Array;
                          personArr[Pindex+1].push(item);
                        }

                      }
                    })
                  }
                })
                if(onOff){
                  let cabinObj = {
                    cabinName: item.cabinName,
                    personArr: new Array
                  }
                  cabinObj.personArr[0] = new Array;
                  cabinObj.personArr[0].push(item);
                  cabinObjArr.push(cabinObj)
                }
              }else {
                let cabinObj = {
                  cabinName: item.cabinName,
                  personArr: new Array
                }
                cabinObj.personArr[0] = new Array;
                cabinObj.personArr[0].push(item);
                cabinObjArr.push(cabinObj)
              }
            })
            personInfo.cabinList = cabinObjArr;
            vm.personInfo = personInfo;
          }
        })
      },
      personConfirm(){
        let personJson = new Array;
        for(let i=0;i<vm.personInfo.cabinList.length;i++){
          let personArr = vm.personInfo.cabinList[i];
          for(let j=0;j<personArr.personArr.length;j++){
            let Item = personArr.personArr[j];
            Item.forEach(function(item,index){
              personJson.push(item);
            })
          }
        }
        let parame = {
          order: vm.personInfo.order,
          personJson: JSON.stringify(personJson)
        }
        if(sessionStorage.userType === 'false'){
          parame.userName = sessionStorage.userName;
        }
        vm.axios.post(this.api+this.urlApi.updatePersonInfoOper,parame).then(function (respones) {
          if(respones.status){
            vm.showPerson.status = false;
            vm.bodyNoScrolling.close();
          }
        })
      },
      complete(id,obj){
        if(obj.status == 1){
          let cabinIndex = id.split('_')[1];
          let personIndex = id.split('_')[2];
          let Index = id.split('_')[3];
          let type = id.split('_')[4];
          let cabinObj = vm.personInfo.cabinList;
          for(let i=0;i<cabinObj.length;i++){
            let personArr = vm.personInfo.cabinList[i];
            if (cabinIndex == i) {
              personArr.personArr.forEach(function (item, index) {
                if (personIndex == index) {
                  item.forEach(function (subItem, subIndex) {
                    if (Index == subIndex) {
                      let imgStr;
                      if (type == 'ID') {
                        imgStr = subItem.cardImage;
                        if (subItem.cardImage) {
                          subItem.cardImage += ',' + obj.content.url;
                        } else {
                          subItem.cardImage = obj.content.url;
                        }
                      } else {
                        imgStr = subItem.passportImage;
                        if (subItem.passportImage) {
                          subItem.passportImage += ',' + obj.content.url;
                        } else {
                          subItem.passportImage = obj.content.url;
                        }
                      }
                    }
                  })
                }
              })
            }
          }
        }else {
          vm.dialog.showDialog('图片上传失败');
        }
      },
      fail(id,msg,fileItem){
        vm.dialog.showDialog(msg);
      },
      removeItem(id,url){
        let cabinIndex = id.split('_')[1];
        let personIndex = id.split('_')[2];
        let Index = id.split('_')[3];
        let type = id.split('_')[4];
        let cabinObj = vm.personInfo.cabinList;
        let imageArr
        if(type == 'ID'){
          imageArr = cabinObj[cabinIndex].personArr[personIndex][Index].cardImage.split(',');
        }else {
          imageArr = cabinObj[cabinIndex].personArr[personIndex][Index].passportImage.split(',');
        }
        for(var i=0;i<imageArr.length;i++){
          if(imageArr[i] == url){
            imageArr.splice(i,1);
          }
        }
        if(type == 'ID'){
          cabinObj[cabinIndex].personArr[personIndex][Index].cardImage = imageArr.join(',');
        }else {
          cabinObj[cabinIndex].personArr[personIndex][Index].passportImage = imageArr.join(',');
        }
      },
      showImage(url){
        console.log(url)
      }
    }
  }
</script>

<style lang="less" scoped>
  @import "../assets/css/base.less";
  #person-wrap{
    width: 100%;
    height: 100%;
    overflow: hidden;
    .main{
      height: 100%;
      padding-top: 88*@basePX;
      overflow-x: hidden;
      overflow-y: auto;
      .container{
        width: 94%;
        margin: 0 auto;
        height: calc(~ '100% - 2.901rem');
        overflow-y: auto;
        overflow-x: hidden;
        background-color: #fff;
        .panel-main{
          width: 100%;
          .item{
            width: 100%;
            margin-top: 38*@basePX;
            border-bottom: 12px solid @appBg;
            .title{
              color: @changeBtnBg;
              font-size: 38*@basePX;
              border-bottom: 1px solid #d2e6ff;
            }
            .info{
              width: 100%;
              margin-top: 38*@basePX;
              .info-top{
                color: @changeBtnBg;
                font-size: 32*@basePX;
              }
              .info-input{
                width: 100%;
                margin-top: 22*@basePX;
                li{
                  width: 50%;
                  float: left;
                  span{
                    color: #333;
                    font-size: 32*@basePX;
                  }
                  input{
                    width: 324*@basePX;
                    height: 72*@basePX;
                    padding: 20*@basePX 0;
                    line-height: 32*@basePX;
                    font-size: 30*@basePX;
                    text-indent: 18*@basePX;
                    border: 1px solid #c2d6ee;
                    outline: none;
                    .borderRadius;
                    margin-top: 10*@basePX;
                  }
                }
              }
              .info-pic{
                width: 100%;
                margin-top: 22*@basePX;
                li{
                  margin-bottom: 22*@basePX;
                  span{
                    color: #333;
                    font-size: 32*@basePX;
                  }
                  .pic{
                    position: relative;
                  }
                }
              }
            }
          }
        }
      }
      .btn-box{
        width: 100%;
        position: fixed;
        left: 0;
        bottom: 0;
        text-align: center;
        z-index: 9;
        box-shadow: 0px 0px 8px 0px #cebaba;
        button{
          width: 600*@basePX;
          height: 88*@basePX;
          .borderRadius(@radius:50*@basePX );
          border:0;
          background-color: @clickBtnBg;
          color: #fff;
          font-size: 40*@basePX;
          margin: 24*@basePX 0;
          letter-spacing: 2px;
          cursor: pointer;
          outline: none;
        }
      }
    }
  }
</style>
