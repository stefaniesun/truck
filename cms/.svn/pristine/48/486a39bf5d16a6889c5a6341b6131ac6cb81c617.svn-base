<template>
  <div class="msg-wrap">
    <header>
      <h2>备注详情</h2>
      <i class="back iconfont icon-fanhui" @click="msgConfirm(false)"></i>
    </header>
    <div class="msg-panel-wrap">
      <div class="main" ref="msgWrap">
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
            <div class="clearfloat"></div>
            <div v-if="item.isDateTips" class="time-line time-line-after">{{item.isDateTips}}</div>
          </li>
        </ul>
      </div>
      <div class="new-msg">
        <textarea v-model="msg" class="msg-content"></textarea>
      </div>
      <ul class="btn-wrap">
        <li @click="msgConfirm(false)" class="btn"><span>关闭窗口</span></li>
        <li @click="msgConfirm(true)" class="btn"><span>提交新增备注</span></li>
      </ul>
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
    props: {
      showMsgPanel: {
        type: Object
      },
      order: {
        type: String
      },
    },
    mounted(){
      vm = this;
      vm.getMsg();
    },
    methods: {
      getMsg(){
        let parame = new Object;
        parame.cruiseMainOrder = vm.order;
        vm.axios.post(this.api+this.urlApi.getDistributorReadByCruiseOrderOper,parame).then(function (response) {
          if(response.status){
            vm.msgList = response.content;
          }
        })
      },
      closeMsgPanel(){
        vm.showMsgPanel.status = false;
        vm.bodyNoScrolling.close();
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
                vm.closeMsgPanel();
              }
            })
          }else {
            vm.closeMsgPanel();
          }
        }
      },
      addMsg(){
        let params ={
          cruiseMainOrder: vm.order,
          message: vm.msg,
        }
        if( sessionStorage.userType === 'false' ){
          params.userName = sessionStorage.userName;
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
          let startDay = parseInt(vm.msgList[0].addDate.split(' ')[0].substring(8,10)) - parseInt(new Date().Format('yyyy/MM/dd').substring(8,10));;
          vm.weekDay = new Date().getDay() ? new Date().getDay() : 7;
          vm.start = new Date(vm.msgList[0].addDate.replace(/-/g, "/")).getTime();
          vm.setTimeTips(Math.abs(startDay),true);
          for(let i=0;i<vm.msgList.length;i++){
            let msgObj = vm.msgList[i];
            if(i>0){
            	let addDate = msgObj.addDate.replace(/-/g, "/");
              if( new Date(addDate).getTime() - vm.start > (30*60*1000) ){
                let dayCount = parseInt(msgObj.addDate.split(' ')[0].substring(8,10)) - parseInt(new Date().Format('yyyy/MM/dd').substring(8,10));
                let type = vm.setTimeTips(Math.abs(dayCount),false,i);
                if( type == 'date' ){
                  vm.$set(vm.msgList[i-1],'isDateTips',msgObj.addDate);
                }else {
                  vm.$set(vm.msgList[i-1],'isDateTips',type + ' ' + msgObj.addDate.split(' ')[1]);
                }
                vm.start = new Date(addDate).getTime();
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
  .msg-wrap{
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    max-height: 100%;
    z-index: 15;
    overflow: hidden;
    background-color: #fff;
    .msg-panel-wrap{
      width: 100%;
      overflow: hidden;
      margin-top: 88*@basePX;
      .borderRadius;
      background-color: #fff;
      .main , .new-msg{
        width: 94%;
        overflow-x: hidden;
        overflow-y: auto;
        margin: 20*@basePX 3%;
        border: 1px solid #bfc5cd;
      }
      .main{
        height: 700*@basePX;
        .msg-list{
          width: 100%;
          height: auto;
          min-height: 100%;
          .item{
            width: 100%;
            height: auto;
            min-height: 70*@basePX;
            padding: 0 10*@basePX 40*@basePX;
            position: relative;
            .time-line{
              display: block;
              width: 100%;
              height: 40*@basePX;
              text-align: center;
              color: #ccc;
            }
            .time-line-before{
              margin: 0 auto;
            }
            .time-line-after{
              position: absolute;
              bottom: 0;
              left: 0;
            }
            .msg-panel{
              height: 100%;
              min-height: 62*@basePX;
              width: 80%;
              position: relative;
              padding-bottom: 4px;
              .name{
                margin-top: 8*@basePX;
                font-size: 30*@basePX;
              }
              div{
                .borderRadius(8*@basePX);
                height: auto;
                min-height: 62*@basePX;
                line-height: 32*@basePX;
                padding: 6*@basePX 10*@basePX;
                position: relative;
                font-size: 30*@basePX;
                span{
                  max-width: 200*@basePX;
                  display: block;
                  cursor: default;
                  word-wrap: break-word;
                }
                i{
                  width: 40*@basePX;
                  height: 62*@basePX;
                  display: block;
                  position: absolute;
                  top: 50%;
                  margin-top: -31*@basePX;
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
                box-shadow: 0 0 5*@basePX 0 rgb( 106, 173, 255 );
                margin: 5*@basePX 34*@basePX 0 0;
                i{
                  background: url("../assets/img/msg-right.png") no-repeat 100% 100%;
                  right: -39*@basePX;
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
                box-shadow: 0 0 5*@basePX 0 rgb( 255, 155, 11 );
                margin: 5*@basePX 0 0 34*@basePX;
                i{
                  background: url("../assets/img/msg-left.png") no-repeat 100% 100%;
                  left: -39*@basePX;
                }
              }
            }
          }
        }
      }
      .new-msg{
        height: 150*@basePX;
        padding: 10*@basePX;
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
        width: 460*@basePX;
        height: 70*@basePX;
        margin: 40*@basePX auto 0;
        .btn{
          width: 200*@basePX;
          height: 70*@basePX;
          line-height: 70*@basePX;
          text-align: center;
          .borderRadius(70*@basePX);
          color: #fff;
          background-color: @clickBtnBg;
          &:first-child{
            background-color: @changeBtnBg;
            float: left;
          }
          &:last-child{
            float: right;
          }
        }

      }
    }
  }
</style>
