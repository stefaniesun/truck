/**
 * Created by Administrator on 2017/10/20.
 * 统一管理请求接口。
 */
import Vue from 'vue'
let urlApi = {
  queryVoyageList: {    //首页热门产品列表
    url:'SaleVoyageWS/queryVoyageList.cms',
    isApiKey: true,   // true 表示需要 apikey
    isLoading: true   // true 表示需要 加载动画
  },
  getSelectData: {      //筛选关键词
    url: 'SaleVoyageWS/getSelectData.cms',
    isApiKey: true,
    isLoading: true
  },
  getQueryData: {       //获取筛选数据
    url: 'SaleVoyageWS/getQueryData.cms',
    isApiKey: true,
    isLoading: true
  },
  getVoyageDetail: {     //产品详情
    url: 'SaleVoyageWS/getVoyageDetail.cms',
    isApiKey: true,
    isLoading: true
  },
  queryVoyageListByKeyword: {   //关键词搜索
    url: 'SaleVoyageWS/queryVoyageListByKeyword.cms',
    isApiKey: true,
    isLoading: true
  },
  getLoginData:{        //登录
    url: 'SaleVoyageWS/loginByPhoneOper.open',
    isApiKey: false,
    isLoading: true
  },
  getWechatLoginCode:{      //登录二维码
    url: 'SaleVoyageWS/getWechatLoginCodeOper.open',
    isApiKey: false,
    isLoading: false
  },
  validateWechatLoginOper:{      //二维码轮询
    url: 'SaleVoyageWS/validateWechatLoginOper.open',
    isApiKey: false,
    isLoading: false
  },
  getVoyageDateList: {     //日期选择
    url: 'SaleVoyageWS/getVoyageDateList.cms',
    isApiKey: true,
    isLoading: true
  },
  createCruiseOrderOper:{      //下单
    url: 'SaleVoyageWS/createCruiseOrderOper.cms',
    isApiKey: true,
    isLoading: true
  },
  queryCruiseOrderList:{      //订单查询
    url: 'SaleVoyageWS/queryCruiseOrderList.cms',
    isApiKey: true,
    isLoading: true
  },
  logOutOper:{      //退出
    url: 'SaleVoyageWS/logOutOper.cms',
    isApiKey: true,
    isLoading: true
  },
  getRegisterDistributor:{    //注册
    url:'DistributorAccountWS/registerDistributorOper.open',
    isApiKey:false,
    isLoading:true
  },
  resetPwd:{     //修改密码
    url:"DistributorAccountWS/alterPassword.cms",
    isApiKey:true,
    isLoading:true
  },
  querySystemConfigList:{     //获取协议
    url:"SaleVoyageWS/querySystemConfigList.open",
    isApiKey:false,
    isLoading:false
  },
  sendVerificationCode:{    //获取验证码
    url:"DistributorAccountWS/sendVerificationCodeOper.open",
    isApiKey:false,
    isLoading:false
  },
  verificationCode:{    //验证验证码
    url:"DistributorAccountWS/verificationCode.open",
    isApiKey:false,
    isLoading:false
  },
  setPassword:{   //修改密码
    url:"DistributorAccountWS/setPassword.open",
    isApiKey:false,
    isLoading:false
  },
  getBargainPrice:{   //特价尾单
    url:"SaleVoyageWS/getQuoteList.cms",
    isApiKey:true,
    isLoading:true
  },
  getDistributorAccountListByNumberCode:{   //金额
    url:"PlatformWS/getDistributorAccountListByNumberCode.cms",
    isApiKey:true,
    isLoading:true
  },
  getDistributorAccountListByaddDate:{   //金额日期查询
    url:"PlatformWS/getDistributorAccountListByaddDate.cms",
    isApiKey:true,
    isLoading:true
  },
  queryIntroductionList:{   //邮轮百科列表
    url:"IntroductionWS/queryIntroductionList.cms",
    isApiKey:true,
    isLoading:true
  },
  queryCuriseIntroductionList:{   //游轮详情
    url:"IntroductionWS/queryCuriseIntroductionList.cms",
    isApiKey:true,
    isLoading:true
  },
  updatePersonInfoOper:{   //设置出行人
    url:"SaleVoyageWS/updatePersonInfoOper.cms",
    isApiKey:true,
    isLoading:true
  },
  getPersonInfo:{   //获取出行人
    url:"SaleVoyageWS/getPersonInfo.cms",
    isApiKey:true,
    isLoading:true
  },
  orderPayOper:{   //二次付款
    url:"SaleVoyageWS/orderPayOper.cms",
    isApiKey:true,
    isLoading:true
  },
  querySharePriceList:{   //获取分享统计数据
    url:"ShareWS/querySharePriceList.cms",
    isApiKey:true,
    isLoading:true
  },
  querySharePrice:{   //获取统计数据订单详情
    url:"ShareWS/querySharePrice.cms",
    isApiKey:true,
    isLoading:true
  },
  editOrderLinkInfo:{   //更改联系人
    url:"SaleVoyageWS/editOrderLinkInfo.cms",
    isApiKey:true,
    isLoading:true
  },
  getPossessorInfo:{   //获取机构
    url:"SaleVoyageWS/getPossessorInfo.open",
    isApiKey:false,
    isLoading:false
  },
  createQrcode: {   //获取公众号二维码链接
    url: "WX_WeixinWS/createQrcode.wx",
    isApiKey: false,
    isLoading: false
  },
  addShortCode: {   //获取公众号二维码随机码
    url: "ShortCodeWS/addShortCode.open",
    isApiKey: false,
    isLoading: false
  },
  addCruiseOrderMsgByDistributor: {   //添加消息
    url: "SaleVoyageWS/addCruiseOrderMsgByDistributor.cms",
    isApiKey: true,
    isLoading: true
  },
  getDistributorReadByCruiseOrderOper: {   //查询消息
    url: "SaleVoyageWS/getDistributorReadByCruiseOrderOper.cms",
    isApiKey: true,
    isLoading: true
  }
};
for(let i in urlApi){
  urlApi[i] = JSON.stringify(urlApi[i])
}
Vue.prototype.urlApi = urlApi;

