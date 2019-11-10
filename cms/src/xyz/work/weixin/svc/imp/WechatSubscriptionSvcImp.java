package xyz.work.weixin.svc.imp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.ReturnUtil;
import xyz.util.ConstantMsg;
import xyz.util.StringTool;
import xyz.util.UUIDUtil;
import xyz.work.weixin.model.WechatSubscription;
import xyz.work.weixin.svc.WechatSubscriptionSvc;

@Service
public class WechatSubscriptionSvcImp implements WechatSubscriptionSvc {
    @Autowired
    private CommonDao commonDao;

    @Override
    public Map<String, Object> queryWechatSubscriptionList(int offset , int pageSize ,
                                                           String nickName , String wechat) {
        StringBuffer sql = new StringBuffer("from WechatSubscription ws where 1=1 ");
        if (StringTool.isNotNull(nickName)) {
            sql.append(" and ws.nickName like '%" + nickName + "%'");
        }
        if(StringTool.isNotNull(wechat)){
            sql.append(" and ws.wechat like '%" + wechat +"%'");
        }
        sql.append(" order by ws.alterDate desc");

        Query countQuery = commonDao.getQuery("select count(*) " + sql.toString());
        Number countNum = (Number)countQuery.uniqueResult();
        int count = countNum == null ? 0 : countNum.intValue();

        Query query = commonDao.getQuery(sql.toString());
        query.setMaxResults(pageSize);
        query.setFirstResult(offset);
        @SuppressWarnings("unchecked")
        List<WechatSubscription> list = query.list();
        
        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", count);
        mapContent.put("rows", list);

        return ReturnUtil.returnMap(1, mapContent);
    }

    @Override
    public Map<String, Object> addWechatSubscription(String nickName , String wechat ,
                                                     String remark) {
        if(StringTool.isEmpty(nickName)){
            return ReturnUtil.returnMap(0, ConstantMsg.subscription_nickNane_null);
        }
        if(StringTool.isEmpty(wechat)){
            return ReturnUtil.returnMap(0, ConstantMsg.subscription_wechat_null);
        }
        Date addDate = new Date();
        WechatSubscription subscription = new WechatSubscription();
        subscription.setNumberCode(UUIDUtil.getUUIDStringFor32());
        subscription.setNickName(nickName);
        subscription.setWechat(wechat);
        subscription.setRemark(remark);
        subscription.setAddDate(addDate);
        subscription.setAlterDate(addDate);
        commonDao.save(subscription);
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> editWechatSubscription(String numberCode , String nickName ,
                                                      String wechat , String remark) {
        if(StringTool.isEmpty(numberCode)){
            return ReturnUtil.returnMap(0, ConstantMsg.subscription_number_error);
        }
        if(StringTool.isEmpty(nickName)){
            return ReturnUtil.returnMap(0, ConstantMsg.subscription_nickNane_null);
        }
        if(StringTool.isEmpty(wechat)){
            return ReturnUtil.returnMap(0, ConstantMsg.subscription_wechat_null);
        }
        WechatSubscription subscription = (WechatSubscription)commonDao.getObjectByUniqueCode("WechatSubscription", "numberCode", numberCode);
        if(subscription == null){
            return ReturnUtil.returnMap(0, ConstantMsg.subscription_error);
        }
        subscription.setNickName(nickName);
        subscription.setWechat(wechat);
        subscription.setRemark(remark);
        subscription.setAlterDate(new Date());
        commonDao.update(subscription);
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> deleteWechatSubscription(String numberCodes) {
        if (StringTool.isEmpty(numberCodes)) {
            return ReturnUtil.returnMap(0, ConstantMsg.delete_null);
        }
        String sql = "DELETE FROM wechat_subscription WHERE number_code IN ("
                     + StringTool.StrToSqlString(numberCodes) + ")";
        commonDao.getSqlQuery(sql).executeUpdate();
        
        return ReturnUtil.returnMap(1, null);
    }

}