<template>
  <div id="chat-panel">
    <div class="panel">
      <div class="top">备注详情</div>
      <div class="main">
        <div ref="msgWrap" class="content">
          <ul ref="msgContent" class="msg-list clearfloat">
            <li v-for="(item,index) in msgList" :key="index" class="item">
              <div v-if="index==0" class="time-line time-line-before">{{startTime}}</div>
              <div :class="item.type === 'operator'?'distributor-msg':'self-msg'" class="msg-panel">
                <span class="name">{{item.username}}</span>
                <div>
                  <span :title="item.addDate">{{item.message}}</span>
                  <i></i>
                </div>
              </div>
              <div v-if="item.isDateTips" class="time-line time-line-after">{{item.isDateTips}}</div>
            </li>
          </ul>
        </div>
        <div class="msg-wrap">
          <div class="msg">
            <textarea v-model="msg" class="msg-content"></textarea>
          </div>
          <ul class="btn-wrap">
            <li @click="msgConfirm(false)">关闭窗口</li>
            <li @click="msgConfirm(true)">提交新增备注</li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  let vm;
  export default{
    name: 'chatPanel',
    data(){
      return {
      	msg: '',
        msgList: new Array,
        userName: sessionStorage.userName,
        startTime: ''
      }
    },
    props: ['order'],
    mounted(){
      vm = this;
      vm.getMsg();
    },
    methods: {
    	getMsg(){
        vm.axios.post(this.api+this.urlApi.getDistributorReadByCruiseOrderOper,{cruiseMainOrder: vm.order}).then(function (response) {
          if(response.status){
            vm.msgList = response.content;
          }
        })
      },
      msgConfirm(flag){
      	if(flag){
          if(vm.msg){
            vm.addMsg();
          }
        }else {
      		if(vm.msg){
      			vm.dialog.showDialog({
              close: true,
              btnText: '确定',
              msg: '有留言未发送，确定需要关闭窗口吗？'
            },function (status) {
              if(status){
                vm.$emit('closeMsgPanel',true);
              }
            })
          }else {
            vm.$emit('closeMsgPanel',true);
          }
        }
      },
      addMsg(){
      	let params ={
          cruiseMainOrder: vm.order,
          message: vm.msg
        }
        vm.axios.post(this.api+this.urlApi.addCruiseOrderMsgByDistributor,params).then(function (response) {
          if(response.status){
            let msgObj = {
            	type: 'distributor',
              username: vm.userName,
              message: vm.msg,
              addDate: new Date().Format('yyyy-MM-dd hh:mm:ss')
            }
            vm.msgList.push(msgObj);
            vm.msg = '';
          }
        })
      },
      setTimeTips(startDay,isInit){
        let type = '';
        if(startDay){
          let type = '';
          let nowDay = startDay;
          if( vm.weekDay < nowDay  ){
            if(isInit){
              vm.startTime = vm.msgList[0].addDate;
            }else {
              type = 'date';
              return type;
            }
            return;
          }else if( vm.weekDay - nowDay >= 1 ) {
            if(nowDay == 1){
              type = '昨天';
            }else {
              switch (vm.weekDay - nowDay) {
                case 1:
                  type = '周一';
                  break;
                case 2:
                  type = '周二';
                  break;
                case 3:
                  type = '周三';
                  break;
                case 4:
                  type = '周四';
                  break;
                case 5:
                  type = '周五';
                  break;
              }
            }
            if(isInit){
              vm.startTime = type + ' ' + vm.msgList[0].addDate.split(' ')[1];;
            }else {
              return type;
            }
          }
        }else {
          if(isInit){
            vm.startTime = vm.msgList[0].addDate.split(' ')[1];
          } else {
            return type;
          }
        }
      }
    },
    watch: {
      msgList(newVal){
      	vm.msgList = newVal;
      	if(vm.msgList.length>0){
          let startDay = parseInt(vm.msgList[0].addDate.split(' ')[0].substring(8,10)) - parseInt(new Date().Format('yyyy-MM-dd').substring(8,10));;
          vm.weekDay = new Date().getDay() ? new Date().getDay() : 7;
          vm.start = new Date(vm.msgList[0].addDate).getTime();
          vm.setTimeTips(Math.abs(startDay),true);
          for(let i=0;i<vm.msgList.length;i++){
            let msgObj = vm.msgList[i];
            if(i>0){
              if( new Date(msgObj.addDate).getTime() - vm.start > (30*60*1000) ){
                let dayCount = parseInt(msgObj.addDate.split(' ')[0].substring(8,10)) - parseInt(new Date().Format('yyyy-MM-dd').substring(8,10));
                let type = vm.setTimeTips(Math.abs(dayCount),false,i);
                if( type == 'date' ){
                  vm.$set(vm.msgList[i-1],'isDateTips',msgObj.addDate);
                }else {
                  vm.$set(vm.msgList[i-1],'isDateTips',type + ' ' + msgObj.addDate.split(' ')[1]);
                }
                vm.start = new Date(msgObj.addDate).getTime();
              }
            }
          }
          vm.$nextTick(function () {
            vm.$nextTick(function () {
              let scrollHeight = vm.$refs['msgContent'].offsetHeight - vm.$refs['msgWrap'].offsetHeight;
              vm.$refs['msgWrap'].scrollTop = scrollHeight + 40;
            })
          })
        }
      }
    },
    destroyed(){

    }
  }
</script>

<style scoped lang="less">
  @import "../assets/css/base.less";
  #chat-panel{
    width: 100%;
    height: 100%;
    position: fixed;
    top: 0;
    left: 0;
    background: rgba(0,0,0,.3);
    z-index: 18;
    .panel{
      width: 1100px;
      height: 720px;
      position: absolute;
      left: 50%;
      top: 50%;
      transform: translate(-50%,-50%);
      overflow: hidden;
      background: #fff;
      .borderRadius;
      position: relative;
      .top{
        width: 100%;
        height: 40px;
        line-height: 40px;
        text-align: center;
        color: #fff;
        font-size: 18px;
        background: @changeBtnBg;
      }
      .main{
        width: 100%;
        padding: 20px;
        .content,.msg-wrap{
          border: 1px solid #bfc5cd;
        }
        .content{
          width: 100%;
          height: 508px;
          overflow-y: auto;
          overflow-x: hidden;
          padding: 18px;
          .msg-list{
            width: 100%;
            height: auto;
            min-height: 100%;
            .item{
              width: 100%;
              height: auto;
              min-height: 44px;
              padding-bottom: 28px;
              position: relative;
              .time-line{
                display: block;
                width: 100%;
                height: 22px;
                text-align: center;
                color: #ccc;
              }
              .time-line-before{
                margin: 0 auto;
              }
              .time-line-after{
                position: absolute;
                bottom: 0;
              }
              .msg-panel{
                height: 100%;
                min-height: 44px;
                width: 50%;
                position: relative;
                .name{
                  margin-top: 8px;
                  font-size: 16px;
                }
                div{
                  .borderRadius(8px);
                  height: auto;
                  min-height: 34px;
                  line-height: 22px;
                  padding: 6px 10px;
                  position: relative;
                  font-size: 14px;
                  span{
                    max-width: 366px;
                    display: block;
                    cursor: default;
                    word-wrap: break-word;
                  }
                  i{
                    width: 20px;
                    height: 42px;
                    display: block;
                    position: absolute;
                    top: 50%;
                    margin-top: -21px;
                    background: url("../assets/img/msg-left.png") no-repeat 100% 100%;
                  }
                }
              }
              .self-msg{
                float: right;
                .name{
                  float: right;
                }
                div{
                  float: right;
                  text-align: right;
                  box-shadow: 0px 0px 5px 0px rgb( 106, 173, 255 );
                  margin: 5px 24px 0 0;
                  i{
                    background: url("../assets/img/msg-right.png") no-repeat 100% 100%;
                    right: -19px;
                  }
                }
              }
              .distributor-msg{
                float: left;
                .name{
                  float: left;
                }
                div{
                  float: left;
                  text-align: left;
                  box-shadow: 0px 0px 5px 0px rgb( 255, 155, 11 );
                  margin: 5px 0 0 24px;
                  i{
                    background: url("../assets/img/msg-left.png") no-repeat 100% 100%;
                    left: -19px;
                  }
                }
              }
            }
          }
        }
        .msg-wrap{
          width: 100%;
          height: 120px;
          margin-top: 10px;
          position: relative;
          .msg{
            width: 100%;
            height: 60px;
            overflow-y: auto;
            overflow-x: hidden;
            padding: 10px 10px 0 10px;
            .msg-content{
              width: 100%;
              height: auto;
              min-height: 100%;
              outline: none;
              font-size: 14px;
              resize: none;
              display: block;
              border: none;
            }
          }
          .btn-wrap{
            width: 270px;
            height: 40px;
            line-height: 40px;
            position: absolute;
            right: 10px;
            bottom: 10px;
            li{
              color: #fff;
              width: 124px;
              height: 100%;
              .borderRadius(40px);
              text-align: center;
              letter-spacing: 3px;
              cursor: pointer;
              font-size: 14px;
              &:first-child{
                background-color: @changeBtnBg;
              }
              &:last-child{
                background-color: @clickBtnBg;
                float: right;
              }
            }
          }
        }
      }
    }
  }
</style>
