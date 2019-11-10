package xyz.work.sell.ctrl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.sell.svc.OrderContentSvc;

@Controller
@RequestMapping(value="/OrderContentWS")
public class OrderContentWS{
	
	@Autowired
	private OrderContentSvc orderContentSvc;

	/**
	 * 销售管理 - 订单处理 - 查询订单列表
	 */
	@RequestMapping(value="queryOrderContentList")
	@ResponseBody
	public Map<String, Object> queryOrderContentList(int page,
                                        		     int rows,
                                        		     String queryJson,
                                                     String clientCode,
                                                     String status,
                                                     String dateType,
                                                     Date startDate,
                                                     Date endDate,
                                                     String sort,
                                                     String order){
		int pagesize = rows;
		int offset = (page-1)*pagesize;
		
		return orderContentSvc.queryOrderContentList(offset, pagesize, queryJson, clientCode, status, dateType, startDate, endDate ,sort, order);
	}
	
	/**
     * 销售管理 - 订单处理 - 获取订单备注
     */
    @RequestMapping(value="getOrderContentRemark")
    @ResponseBody
    public Map<String, Object> getOrderContentRemark(String orderNum){
        
        return orderContentSvc.getOrderContentRemark(orderNum);
    }
    
    /**
     * 销售管理 - 订单处理 - 获取订单备注
     */
    @RequestMapping(value="getOrderContentDetail")
    @ResponseBody
    public Map<String, Object> getOrderContentDetail(String orderNum){
        
        return orderContentSvc.getOrderContentDetail(orderNum);
    }
    
    /**
     * 销售管理 - 订单处理 - hold注订单
     */
    @RequestMapping(value="holdOrderCoreOper")
    @ResponseBody
    public Map<String, Object> holdOrderCoreOper(String orderNum){
        
        return orderContentSvc.holdOrderCoreOper(orderNum);
    }
    
    /**
     * 销售管理 - 订单处理 - 释放订单
     */
    @RequestMapping(value="relaxOrderCoreOper")
    @ResponseBody
    public Map<String, Object> relaxOrderCoreOper(){
        
        return orderContentSvc.relaxOrderCoreOper();
    }
    
    @RequestMapping(value = "deleteOrderContent")
    @ResponseBody
    public Map<String, Object> deleteOrderContent(String orderNum , String clientCode) {
        return orderContentSvc.deleteOrderContent(orderNum, clientCode);
    }
    
    /**
     * 销售管理 - 订单处理 - 获取订单备注
     */
    @RequestMapping(value="addOrderContentRemark")
    @ResponseBody
    public Map<String, Object> addOrderContentRemark(String orderNum ,String remark){
        
        return orderContentSvc.addOrderContentRemark(orderNum, remark);
    }
    
    @RequestMapping(value="getOrderRemark")
    @ResponseBody
    public Map<String, Object> getOrderRemark(String orderNum){
        return orderContentSvc.getOrderRemark(orderNum);
    }
	
    @RequestMapping(value="editOrderContentByPay")
    @ResponseBody
    public Map<String, Object> editOrderContentByPay(String orderNum , BigDecimal money) {
        return orderContentSvc.editOrderContentByPay(orderNum, money);
    }
    
    @RequestMapping(value="editFlagColor")
    @ResponseBody
    public Map<String, Object> editFlagColor(String clientCode , String flagColor , String tag){
        return orderContentSvc.editFlagColor(clientCode, flagColor, tag);
    }
    
    @RequestMapping(value="countPrice")
    @ResponseBody
    public Map<String, Object> countPrice(String ptview) {
        return orderContentSvc.countPrice(ptview);
    }
    
    @RequestMapping(value="getStockByPtview")
    @ResponseBody
    public Map<String, Object> getStockByPtview(String ptview) {
        return orderContentSvc.getStockByPtview(ptview);
    }
    
    @RequestMapping(value="updateOrderContentSend")
    @ResponseBody
    public Map<String, Object> updateOrderContentSend(String clientCode ,Integer isSend) {
        return orderContentSvc.updateOrderContentSend(clientCode, isSend);
    }
    
    @RequestMapping(value="updateOrderContentRefound")
    @ResponseBody
    public Map<String, Object> updateOrderContentRefound(String clientCode ,Integer money ,Integer stock) {
        return orderContentSvc.updateOrderContentRefound(clientCode, money, stock);
    }
    
    @RequestMapping(value="updateOrderContentCost")
    @ResponseBody
    public Map<String, Object> updateOrderContentCost(String clientCode ,int cost) {
        return orderContentSvc.updateOrderContentCost(clientCode, cost);
    }
    
    @RequestMapping(value="updateOrderContentTkviewAndCount")
    @ResponseBody
    public Map<String, Object> updateOrderContentTkviewAndCount(String clientCode ,String tkview ,Integer count) {
        return orderContentSvc.updateOrderContentTkviewAndCount(clientCode, tkview, count);
    }
    
    @RequestMapping(value = "updateOrderContentForLinkman")
    @ResponseBody
    public Map<String, Object> updateOrderContentForLinkman(String orderNum,
                                                            String linkman,
                                                            String linkPhone,
                                                            String email,
                                                            String address) {
        return orderContentSvc.updateOrderContentForLinkman(orderNum, linkman, linkPhone, email,address);
    }
    
    @RequestMapping(value = "addInsideOrderContent")
    @ResponseBody
    public Map<String, Object> addInsideOrderContent(String ptivewJson, 
                                                     BigDecimal earnest ,
                                                     String linkMan ,
                                                     String linkPhone , 
                                                     String endDate,
                                                     String remark) {
        return orderContentSvc.addInsideOrderContent(ptivewJson, earnest, linkMan, linkPhone, endDate, remark);
    }
    
    @RequestMapping(value = "getPersonInfoList")
    @ResponseBody
    public Map<String, Object> getPersonInfoList(String clientCode) {
        return orderContentSvc.getPersonInfoList(clientCode);
    }
    
    @RequestMapping(value = "updateOrderContentForPerson")
    @ResponseBody
    public Map<String, Object> updateOrderContentForPerson(String clientCode , String personInfo ,
                                                           String isImplement) {

        return orderContentSvc.updateOrderContentForPerson(clientCode, personInfo, isImplement);
    }
    
}