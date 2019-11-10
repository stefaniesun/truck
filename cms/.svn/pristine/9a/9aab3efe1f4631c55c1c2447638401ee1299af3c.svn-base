package xyz.work.weixin.ctrl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.weixin.svc.WechatSubscriptionSvc;

@Controller
@RequestMapping(value = "/WechatSubscriptionWS")
public class WechatSubscriptionWS {
    @Autowired
    private WechatSubscriptionSvc subscriptionSvc;
    
    @RequestMapping(value="queryWechatSubscriptionList")
    @ResponseBody
    public Map<String, Object> queryWechatSubscriptionList(int page , int rows , 
                                                           String nickName ,
                                                           String wechat) {
        int pageSize = rows;
        int offset = (page-1)*pageSize;
        return subscriptionSvc.queryWechatSubscriptionList(offset, pageSize, nickName, wechat);
    }
    
    @RequestMapping(value="addWechatSubscription")
    @ResponseBody
    public Map<String, Object> addWechatSubscription(String nickName , 
                                                     String wechat , 
                                                     String remark){
        return subscriptionSvc.addWechatSubscription(nickName, wechat, remark);
    }

    @RequestMapping(value="editWechatSubscription")
    @ResponseBody
    public Map<String, Object> editWechatSubscription(String numberCode,
                                                      String nickName, 
                                                      String wechat ,
                                                      String remark){
        return subscriptionSvc.editWechatSubscription(numberCode, nickName, wechat, remark);
    }
    
    @RequestMapping(value="deleteWechatSubscription")
    @ResponseBody
    public Map<String, Object> deleteWechatSubscription(String numberCodes){
        return subscriptionSvc.deleteWechatSubscription(numberCodes);
    }
    
}