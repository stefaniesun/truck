package xyz.work.weixin.svc;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface WechatSubscriptionSvc {
    public Map<String, Object> queryWechatSubscriptionList(int offset , 
                                                           int pageSize , 
                                                           String nickName ,
                                                           String wechat);

    public Map<String, Object> addWechatSubscription(String nickName , 
                                                     String wechat , 
                                                     String remark);
    
    public Map<String, Object> editWechatSubscription(String numberCode,
                                                      String nickName, 
                                                      String wechat ,
                                                      String remark);
    
    public Map<String, Object> deleteWechatSubscription(String numberCodes);
    
}