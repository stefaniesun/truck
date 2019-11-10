package xyz.work.sell.svc;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface OrderContentSvc {
    
    public Map<String ,Object> queryOrderContentList(int offset,
                                                     int pagesize,
                                                     String queryJson,
                                                     String clientCode,
                                                     String status,
                                                     String dateType,
                                                     Date startDate,
                                                     Date endDate,
                                                     String sort,
                                                     String order);
    
    public Map<String ,Object> getOrderContentRemark(String orderNum);
    
    public Map<String ,Object> getOrderContentDetail(String orderNum);
    
    public Map<String ,Object> holdOrderCoreOper(String orderNum);
    
    public Map<String ,Object> relaxOrderCoreOper();
    
    public Map<String ,Object> addOrderContentRemark(String orderNum ,String remark);
    
    public Map<String ,Object> addOrderContent(String linkMan,
                                               String linkPhone,
                                               String address,
                                               String email,
                                               String productJson);
    
    public Map<String, Object> getOrderRemark(String orderNum);
    
    public Map<String, Object> editOrderContentByPay(String orderNum , BigDecimal money);
    
    public Map<String, Object> editFlagColor(String clientCode , String flagColor , String tag);
    
    public Map<String, Object> countPrice(String ptview);
    
    public Map<String, Object> getStockByPtview(String ptview);
    
    public Map<String, Object> addInsideOrderContent(String ptivewJson, 
                                                     BigDecimal earnest ,
                                                     String linkMan ,
                                                     String linkPhone , 
                                                     String endDate,
                                                     String remark);
    
    public Map<String ,Object> updateOrderContentSend(String clientCode ,Integer isSend);
    
    public Map<String ,Object> updateOrderContentRefound(String clientCode ,Integer money ,Integer stock);
    
    public Map<String ,Object> updateOrderContentCost(String clientCode ,Integer cost);
    
    public Map<String ,Object> updateOrderContentTkviewAndCount(String clientCode ,
                                                                String tkview ,
                                                                Integer count);
    
    public Map<String ,Object> updateOrderContentForLinkman(String orderNum,
                                                            String linkman,
                                                            String linkPhone,
                                                            String email,
                                                            String address);
    
    public Map<String, Object> updateOrderContentForPerson(String clientCode , 
                                                           String personInfo ,
                                                           String isImplement);
   
    public Map<String, Object> getPersonInfoList(String clientCode);

	public Map<String, Object> deleteOrderContent(String orderNum,
			String clientCode);
    
}