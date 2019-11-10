package xyz.work.sell.svc.imp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.JSON;
import xyz.filter.MyRequestUtil;
import xyz.filter.ReturnUtil;
import xyz.util.Constant;
import xyz.util.ConstantMsg;
import xyz.util.DateUtil;
import xyz.util.OrderContentUtil;
import xyz.util.PossessorUtil;
import xyz.util.ProductUtil;
import xyz.util.StringTool;
import xyz.util.StringUtil;
import xyz.util.UUIDUtil;
import xyz.work.base.model.Cabin;
import xyz.work.base.model.Cruise;
import xyz.work.base.model.Shipment;
import xyz.work.core.model.LogWork;
import xyz.work.resources.model.Provider;
import xyz.work.resources.model.Stock;
import xyz.work.resources.model.Tkview;
import xyz.work.sell.model.LogHold;
import xyz.work.sell.model.OrderContent;
import xyz.work.sell.model.PersonInfo;
import xyz.work.sell.model.Ptview;
import xyz.work.sell.model.R_OrderContent;
import xyz.work.sell.svc.OrderContentSvc;

@Service
public class OrderContentSvcImp implements OrderContentSvc{

    @Autowired
    private CommonDao commonDao;
    
    @Autowired
    private PossessorUtil possessorUtil;
    
    @Autowired
    private ProductUtil productUtil;
    
    @Autowired
    OrderContentUtil orderContentUtil;

    @Override
    public Map<String, Object> queryOrderContentList(int offset,
                                                     int pagesize,
                                                     String queryJson,
                                                     String clientCode,
                                                     String status,
                                                     String dateType,
                                                     Date startDate,
                                                     Date endDate,
                                                     String sort,
                                                     String order) {
        
        String orderNumSql = "select order_num from order_content where 1 = 1 and is_delete = 0";
        {
            if(StringTool.isNotNull(MyRequestUtil.getPossessor())){
                Map<String ,List<String>> decideMap = possessorUtil.getDecideMap(Constant.relate_type_tkview);
                List<String> tkviews = decideMap.keySet().contains(Constant.relate_type_tkview)?decideMap.get(Constant.relate_type_tkview):new ArrayList<String>();
                orderNumSql += " and tkview in ("+StringTool.listToSqlString(tkviews)+")";
            }
        }
        orderNumSql += orderContentUtil.getOrderContentWhereSql(queryJson, clientCode, status, dateType, startDate, endDate);
        orderNumSql += " group by order_num";
        
        if (sort != null && !"".equals(sort) && order != null && !"".equals(order)) {
            if (Constant.q_order_content_all_date_type.contains("," + sort+ ",") && ",asc,desc,".contains("," + order + ",")) {
                orderNumSql += " order by " + sort + " " + order;
            } 
        }else {
            orderNumSql += " ORDER BY alter_date desc";
        }
        
        SQLQuery countQuery = commonDao.getSqlQuery("select count(*) from ("+orderNumSql+") ttt");
        Number tempCount = (Number)countQuery.uniqueResult();
        int total = tempCount==null?0:tempCount.intValue();
        
        SQLQuery query = commonDao.getSqlQuery(orderNumSql);
        query.setFirstResult(offset);
        query.setMaxResults(pagesize);
        
        @SuppressWarnings("unchecked")
        List<String> orderNumList = query.list();
        StringBuffer sql = new StringBuffer();
        sql.append("select o.order_num as orderNum,");
        sql.append(" count(o.client_code) as countClient,");
        sql.append(" o.client_code as clientCode,");
        sql.append(" sum(IF(o.is_send = 1, 1, 0)) AS countSend,");
        sql.append(" sum(IF(o.is_refound_money = 1, 1, 0)) AS countRefound,");
        sql.append(" o.is_pay as isPay,");
        sql.append(" o.is_over as isOver,");
        sql.append(" o.flag as flagRemark,");
        sql.append(" o.remark as remark,");
        sql.append(" o.over_date as overDate,");
        sql.append(" o.product as product,");
        sql.append(" o.tkview as tkview,");
        sql.append(" o.stock as stock,");
        sql.append(" o.flag as flag,");
        sql.append(" o.order_price as orderPrice,");
        sql.append(" o.pay_amount as payAmount,");
        sql.append(" o.refound_amount as refoundAmount,");
        sql.append(" o.cruise_nme_cn as cruiseNameCn,");
        sql.append(" o.shipment as shipment,");
        sql.append(" o.shipment_mark as shipmentMark,");
        sql.append(" o.shipment_travel_date as shipmentTravelDate,");
        sql.append(" o.cabin_name_cn as cabinNameCn,");
        sql.append(" o.channel_name_cn as channelNameCn,");
        sql.append(" o.product_name_cn as productNameCn,");
        sql.append(" o.tkview_name_cn as tkviewNameCn,");
        sql.append(" o.provider_name_cn as providerNameCn,");
        sql.append(" o.source as source,");
        sql.append(" o.buyer as buyer,");
        sql.append(" o.link_man as linkMan,");
        sql.append(" o.link_phone as linkPhone,");
        sql.append(" o.address as address,");
        sql.append(" o.email as email,");
        sql.append(" o.tid as tid,");
        sql.append(" o.operater_add as operaterAdd,");
        sql.append(" o.operater_alter as operaterAlter,");
        sql.append(" o.is_hang as isHang,");
        sql.append(" o.add_date as addDate,");
        sql.append(" o.alter_date as alterDate");
        sql.append(" FROM order_content o WHERE o.is_delete = 0 and o.order_num in("+StringTool.listToSqlString(orderNumList)+") GROUP BY o.order_num order by find_in_set(orderNum,'"+StringTool.listToString(orderNumList, null)+"')");

        SQLQuery query2 = commonDao.getSqlQuery(sql.toString());
        query2.addScalar("orderNum").
        addScalar("countClient").
        addScalar("clientCode").
        addScalar("countSend").
        addScalar("countRefound").
        addScalar("isPay").
        addScalar("isOver").
        addScalar("flagRemark").
        addScalar("remark").
        addScalar("overDate").
        addScalar("product").//商品, 内部单是ptview, TB单为 宝贝
        addScalar("tkview").
        addScalar("stock").//使用库存
        addScalar("flag").//用户自定义状态
        addScalar("orderPrice").
        addScalar("payAmount").
        addScalar("refoundAmount").
        addScalar("cruiseNameCn").
        addScalar("shipment").
        addScalar("shipmentMark").
        addScalar("shipmentTravelDate").
        addScalar("cabinNameCn").
        addScalar("channelNameCn").
        addScalar("productNameCn").
        addScalar("tkviewNameCn").
        addScalar("providerNameCn").
        addScalar("source").//来源 (cms, pc, tb, ...)
        addScalar("buyer").
        addScalar("linkMan").
        addScalar("linkPhone").
        addScalar("address").
        addScalar("isHang").
        addScalar("email").
        addScalar("tid").
        addScalar("operaterAdd").//录单人
        addScalar("operaterAlter").//修改人
        addScalar("addDate").//录单人
        addScalar("alterDate").//修改人
        setResultTransformer(Transformers.aliasToBean(R_OrderContent.class));
        @SuppressWarnings("unchecked")
        List<R_OrderContent> orderCoreList = query2.list();
        
        Map<String,Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total",total);
        mapContent.put("rows",orderCoreList);

        return ReturnUtil.returnMap(1, mapContent);
    }
    
    @Override
    public Map<String, Object> getOrderContentRemark(String orderNum) {
        
        String hql = "from OrderContent t where t.orderNum = '"+orderNum+"'";
        OrderContent orderContent = (OrderContent)commonDao.queryUniqueByHql(hql);
        String remark = "";
        if(orderContent != null){
            remark = orderContent.getRemark();
        }
        
        return ReturnUtil.returnMap(1, remark);
    }

    @Override
    public Map<String, Object> getOrderContentDetail(String orderNum) {
        String hql = "from OrderContent where 1=1";
        if (orderNum != null && !"".equals(orderNum)) {
            hql += " and orderNum = '" + orderNum + "'";
        }

        Query queryDetail = commonDao.getQuery(hql);
        @SuppressWarnings("unchecked")
        List<OrderContent> orderContents = queryDetail.list();
        commonDao.clear();
        
        String logSql = "from LogWork where tableName = 'order_core' and value ='" + orderNum + "' ORDER BY addDate DESC ";
        Query queryLog = commonDao.getQuery(logSql);
        @SuppressWarnings("unchecked")
        List<LogWork> logWorkList = queryLog.list();
        Map<String, Object> contents = new HashMap<String, Object>();
        
        String refundPriceSql = "select sum(refound_amount) from order_content where order_num = '"+orderNum+"'";

        SQLQuery query = commonDao.getSqlQuery(refundPriceSql);
        Number tempRefundPrice = (Number)query.uniqueResult();
        int refundPrice = tempRefundPrice==null?0:tempRefundPrice.intValue();
        
        contents.put("orderContents", orderContents);
        contents.put("logWorkList", logWorkList);
        contents.put("refundPrice", refundPrice);
        
        return ReturnUtil.returnMap(1, contents);
    }

    @Override
    public Map<String, Object> holdOrderCoreOper(String orderNum) {
        String username = MyRequestUtil.getUsername();
        Date date = new Date();
        String hql = "delete LogHold where username = '"+username+"'";
        commonDao.updateByHql(hql);
        commonDao.flush();
        
        hql = "from LogHold t where t.tableName = 'order_core' and t.value = '"+orderNum+"' order by t.expireDate desc";
        LogHold logHold = (LogHold)commonDao.queryUniqueByHql(hql);
        if(logHold!=null && date.before(logHold.getExpireDate())){
            return ReturnUtil.returnMap(0,"此订单已被"+logHold.getUsername()+"控制");
        }
        
        LogHold hold = new LogHold();
        hold.setNumberCode(UUIDUtil.getUUIDStringFor32());
        hold.setAddDate(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE,10);
        hold.setExpireDate(calendar.getTime());
        hold.setValue(orderNum);
        hold.setTableName("order_core");
        hold.setUsername(username);
        commonDao.save(hold);
        return ReturnUtil.returnMap(1,null);
    }
    
    @Override
    public Map<String, Object> relaxOrderCoreOper(){
        String username = MyRequestUtil.getUsername();
        String hql = "delete LogHold where username = '"+username+"'";
        commonDao.updateByHql(hql);
        return ReturnUtil.returnMap(1,null);
    }

    @Override
    public Map<String, Object> addOrderContentRemark(String orderNum , String remark) {
        
        String hql = "from OrderContent t where t.orderNum = '"+orderNum+"'";

        @SuppressWarnings("unchecked")
        List<OrderContent> orderContentList = commonDao.queryByHql(hql);
        
        for (OrderContent orderContent : orderContentList) {
            orderContent.addRemark(remark);
            commonDao.update(orderContent);
        }
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> addOrderContent(String linkMan,
                                               String linkPhone,
                                               String address,
                                               String email,
                                               String productJson) {
        
        
        
        
        
        
        return null;
    }

    @Override
    public Map<String, Object> getOrderRemark(String orderNum) {
        if (orderNum != null && !"".equals(orderNum)) {
            String hql = "from OrderContent t where t.orderNum = '" + orderNum + "'";
            OrderContent orderContent = (OrderContent)commonDao.queryUniqueByHql(hql);
            if (orderContent == null) {
                return ReturnUtil.returnMap(1, null);
            }
            String result = orderContent.getRemark() == null ? "" : orderContent.getRemark();
            Map<String, String> remarkMap = new HashMap<String, String>();
            remarkMap.put("orderRemark", result == null ? "" : result);
            return ReturnUtil.returnMap(1, remarkMap);
        }
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> editOrderContentByPay(String orderNum , BigDecimal money) {
        String hql = "from OrderContent where orderNum = '"+orderNum+"'";
        
        @SuppressWarnings("unchecked")
        List<OrderContent> orderContents = commonDao.queryByHql(hql);
        Date date = new Date();
        for (OrderContent orderContentObj : orderContents) {
            if(orderContentObj.getIsOver() == 1){
                return ReturnUtil.returnMap(0, ConstantMsg.order_is_over);
            }
            if(orderContentObj.getIsDelete() == 1){
                return ReturnUtil.returnMap(0, ConstantMsg.order_is_delete);
            }
            
            orderContentObj.setPayAmount(orderContentObj.getPayAmount().add(money));
            orderContentObj.setIsPay(1);
            orderContentObj.setPayDate(date);
            orderContentObj.setAlterDate(date);
            commonDao.update(orderContentObj);
        }
        
        LogWork logWork = new LogWork();
        logWork.setAddDate(new Date());
        logWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
        logWork.setValue(orderContents.get(0).getOrderNum());
        logWork.setTableName("order_core");
        logWork.setRemark("新增收款【"+money.divide(new BigDecimal(100))+"】元");
        logWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
        commonDao.save(logWork);

        return ReturnUtil.returnMap(1, null);
    }
    
    @Override
    public Map<String, Object> editFlagColor(String clientCode , String flagColor , String tag){
        if (clientCode == null || "".equals(clientCode) || flagColor == null
            || "".equals(flagColor)) {
            return ReturnUtil.returnMap(0, "参数提交错误!");
        }
        OrderContent orderContent = (OrderContent)commonDao.getObjectByUniqueCode("OrderContent","clientCode", clientCode);

        String oldFlagColorTemp = "灰色";
        switch (orderContent.getFlag()) {
            case 1:
                oldFlagColorTemp = "红色";
                break;
            case 2:
                oldFlagColorTemp = "黄色";
                break;
            case 3:
                oldFlagColorTemp = "绿色";
                break;
            case 4:
                oldFlagColorTemp = "蓝色";
                break;
            case 5:
                oldFlagColorTemp = "紫色";
                break;
            case 6:
                oldFlagColorTemp = "黑色";
                break;
        }
        
        if (tag.equals("订单")) {
            String flagSql = "update order_content set flag = '" + flagColor
                             + "' where order_num = '" + orderContent.getOrderNum() + "' ";
            commonDao.getSqlQuery(flagSql).executeUpdate();
            orderContent = (OrderContent)commonDao.getObjectByUniqueCode("OrderContent",
                "clientCode", clientCode);
        }
        else if (tag.equals("票单")) {
            String hql = "from OrderContent where orderNum = '"+orderContent.getOrderNum()+"'";
            @SuppressWarnings("unchecked")
            List<OrderContent> contentList = commonDao.queryByHql(hql);
            for (OrderContent o : contentList) {
                o.setFlag(Integer.valueOf(flagColor));
                o.setAlterDate(new Date());
                o.setOperaterAlter(MyRequestUtil.getSecurityLogin().getUsername());
                commonDao.update(o);
            }
        }
        
        String flagColorTemp = "灰色";
        switch (orderContent.getFlag()) {
            case 1:
                flagColorTemp = "红色";
                break;
            case 2:
                flagColorTemp = "黄色";
                break;
            case 3:
                flagColorTemp = "绿色";
                break;
            case 4:
                flagColorTemp = "蓝色";
                break;
            case 5:
                flagColorTemp = "紫色";
                break;
            case 6:
                flagColorTemp = "黑色";
                break;
        }
        
        LogWork logWork = new LogWork();
        logWork.setAddDate(new Date());
        logWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
        logWork.setValue(orderContent.getOrderNum());
        logWork.setTableName("order_core");
        logWork.setRemark("修改旗子【新值:"+flagColorTemp+"】【旧值:"+oldFlagColorTemp+"】");
        logWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
        commonDao.save(logWork);

        return ReturnUtil.returnMap(1, null);
    }
    
  @Override
    public Map<String, Object> countPrice(String ptview) {
       /* BigDecimal countPrice = BigDecimal.ZERO;
        if(!StringTool.isNotNull(ptview)){
            return ReturnUtil.returnMap(1, countPrice);
        }
        Ptview ptviewObj = (Ptview) commonDao.getObjectByUniqueCode(Constant.ptview, Constant.numberCode, ptview);
        if (ptviewObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.ptview_null);
        }
        
        countPrice = ptviewObj.getB_price();*/
      
        return ReturnUtil.returnMap(1, null);
    }
    
    @Override
    public Map<String, Object> getStockByPtview(String ptview) {

        Ptview ptviewObj = (Ptview) commonDao.getObjectByUniqueCode(Constant.ptview, Constant.numberCode, ptview);
        if (ptviewObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.ptview_null);
        }
        
        /*String ptviewTkviewHql = "from PtviewTkview where ptview = '"+ptviewObj.getNumberCode()+"'";
        
        @SuppressWarnings("unchecked")
        List<PtviewTkview> ptviewTkviewList = commonDao.queryByHql(ptviewTkviewHql);
        
        BigDecimal stockCount = BigDecimal.ZERO;
        
        for (PtviewTkview ptviewTkview : ptviewTkviewList) {
            String stockSql = "select sum(s.stock-s.use_stock) from stock s where tkview = '"+ptviewTkview.getTkview()+"'";
            stockSql += " and IFNULL(over_date, NOW()) >= NOW()";
            BigDecimal stock = (BigDecimal)commonDao.getSqlQuery(stockSql).uniqueResult();
            if (stock != null) {
                stockCount = stockCount.add(stock);
            }
        }*/
        
        return ReturnUtil.returnMap(1, null);
    }
    
    @Override
    public Map<String, Object> addInsideOrderContent(String ptivewJson, 
                                                     BigDecimal earnest ,
                                                     String linkMan ,
                                                     String linkPhone , 
                                                     String endDate,
                                                     String remark) {
        
        String userName = MyRequestUtil.getUsername();
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> ptviewJsonList = JSON.toObject(ptivewJson, ArrayList.class);

        String orderNum = "";
        BigDecimal orderPrice = new BigDecimal(0);
        
        if (!StringTool.isNotNull(endDate)) {
            orderNum = StringUtil.get_new_orderNum("Y");
        }else {
            orderNum = StringUtil.get_new_orderNum("L");
        }
        
        for (int i = 0; i < ptviewJsonList.size(); i++ ) {
            Map<String, Object> map = ptviewJsonList.get(i);
            
            BigDecimal count = new BigDecimal(map.get("count").toString());
            if(BigDecimal.ZERO.compareTo(count) >= 0){
                return ReturnUtil.returnMap(0, "预购数量有误!");
            }
            String ptview = map.get("ptview").toString();
            if(StringTool.isEmpty(ptview)){
                return ReturnUtil.returnMap(0, ConstantMsg.ptview_null);
            }
            
            Ptview ptviewObj = (Ptview)commonDao.getObjectByUniqueCode(Constant.ptview, Constant.numberCode, ptview);
            if (ptviewObj == null) {
                return ReturnUtil.returnMap(0, ConstantMsg.ptview_null);
            }
            Date date = new Date();
            
            OrderContent orderContent = new OrderContent();
            Tkview tkviewObj = null;
            Stock stock = null;
            
            String ptviewTkviewSql = "select tkview from ptview_tkview where ptview = '"+ptviewObj.getNumberCode()+"' order by priority";
            @SuppressWarnings("unchecked")
            List<String> ptviewTkviewList = commonDao.getSqlQuery(ptviewTkviewSql).list();
            
            for (String tkview : ptviewTkviewList) {
                Map<Integer, String> resultMap = productUtil.substractStock(tkview, count.intValue());
                
                //0:库存不足, 1:无超卖, 2:有超卖 
                if (null != resultMap.get(0)) {
                    continue;
                }else if (null != resultMap.get(1)) {
                    tkviewObj = (Tkview)commonDao.getObjectByUniqueCode(Constant.tkview, Constant.numberCode, tkview);
                    orderContent.setTkview(tkviewObj.getNumberCode());
                    orderContent.setTkviewNameCn(tkviewObj.getNameCn());
                    orderContent.setCount(count.intValue());
                    
                    stock = (Stock)commonDao.getObjectByUniqueCode(Constant.stock, Constant.numberCode ,resultMap.get(1));
                    if (stock == null) {
                        return ReturnUtil.returnMap(0, ConstantMsg.stock_not_exist_error);
                    }
                    
                    orderContent.setIsSurpass(0);
                    orderContent.setStock(stock.getNumberCode());
                    orderContent.setCost(stock.getCost().intValue());
                    
                    if (null != resultMap.get(2)) {
                        orderContent.setIsSurpass(1);
                    }
                }
            }
            
            if (tkviewObj == null) {
                return ReturnUtil.returnMap(0, "库存不足!");
            }
            
            if (!StringTool.isNotNull(endDate)) {
                orderContent.setSeize(0);
                orderContent.setClientCode(StringUtil.get_new_clientCode("Y"));
                orderContent.setOrderNum(orderNum);
            }else {
                orderContent.setSeize(1);
                orderContent.setClientCode(StringUtil.get_new_clientCode("L"));
                orderContent.setOrderNum(orderNum);
            }
            
            Cabin cabin = (Cabin) commonDao.getObjectByUniqueCode(Constant.cabin, Constant.numberCode, tkviewObj.getCabin());
            if (cabin == null) {
                return ReturnUtil.returnMap(0, ConstantMsg.cabin_null);
            }
            
            Shipment shipment = (Shipment)commonDao.getObjectByUniqueCode(Constant.shipment, Constant.numberCode, tkviewObj.getShipment());
            if (shipment == null) {
                return ReturnUtil.returnMap(0, ConstantMsg.shipment_null);
            }
            
            Cruise cruise = (Cruise)commonDao.getObjectByUniqueCode(Constant.cruise, Constant.numberCode, tkviewObj.getCruise());
            if (cruise == null) {
                return ReturnUtil.returnMap(0, ConstantMsg.cruise_null);
            }
            
            Provider provider = (Provider)commonDao.getObjectByUniqueCode(Constant.provider, Constant.numberCode, stock.getProvider());
            if (provider == null) {
                return ReturnUtil.returnMap(0, ConstantMsg.provider_not_exist_error);
            }
            orderContent.setAddress("");
            orderContent.setBuyer(linkMan);
            orderContent.setCabin(cabin.getNumberCode());
            orderContent.setCabinNameCn(cabin.getNameCn());
            orderContent.setVolume(cabin.getVolume());
            //orderContent.setClientPrice(ptviewObj.getB_price().intValue()*count.intValue());
            orderContent.setCost(stock.getCost().intValue());
            orderContent.setCount(count.intValue());
            orderContent.setCruise(cruise.getNumberCode());
            orderContent.setCruiseNameCn(cruise.getNameCn());
            orderContent.setEarnest(earnest.intValue());
            orderContent.setFlag(Constant.order_flag_gray);
            orderContent.setIsDelete(0);
            orderContent.setIsOver(0);
            orderContent.setIsPay(1);
            orderContent.setIsRefoundMoney(0);
            orderContent.setIsRefoundStock(0);
            orderContent.setIsSend(1);
            orderContent.setIsSort(0);
            orderContent.setLinkMan(linkMan);
            orderContent.setLinkPhone(linkPhone);
            orderContent.setOperaterAdd(userName);
            orderContent.setOperaterAlter(userName);
            orderContent.setOrderPrice(orderPrice.intValue());
            orderContent.setOverDate(DateUtil.stringToDate(endDate));
            orderContent.setPayAmount(earnest);
            orderContent.setPayDate(date);
            orderContent.setPersonInfo(null);
            orderContent.setProduct(ptviewObj.getNumberCode());
            orderContent.setProductNameCn(ptviewObj.getNameCn());
           // orderContent.setProductPrice(ptviewObj.getB_price().intValue());
            orderContent.setProvider(provider.getNumberCode());
            orderContent.setProviderNameCn(provider.getNameCn());
            orderContent.setRefoundAmount(0);
            orderContent.setRefoundMoneyDate(null);
            orderContent.setRefoundStock(0);
            orderContent.setRefoundStockDate(null);
            orderContent.setRemark(remark);
            orderContent.setSendDate(date);
            orderContent.setShipment(shipment.getNumberCode());
            orderContent.setShipmentMark(shipment.getMark());
            orderContent.setShipmentTravelDate(shipment.getTravelDate());
            orderContent.setSource("CMS");
            orderContent.setStock(stock.getNumberCode());
            orderContent.setTkview(tkviewObj.getNumberCode());
            orderContent.setTkviewNameCn(tkviewObj.getNameCn());
            orderContent.setAddDate(date);
            orderContent.setAlterDate(date);
            commonDao.save(orderContent);
            commonDao.flush();
            
            orderPrice = orderPrice.add(new BigDecimal(orderContent.getClientPrice()));
        }
        
        String sql = "update order_content set order_price = "+orderPrice+" where order_num = '"+orderNum+"'";
        commonDao.getSqlQuery(sql).executeUpdate();
        
        LogWork logWork = new LogWork();
        logWork.setAddDate(new Date());
        logWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
        logWork.setValue(orderNum);
        logWork.setRemark("预订下单!");
        logWork.setTableName("order_core");
        logWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
        commonDao.save(logWork);

        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> updateOrderContentCost(String clientCode , Integer cost) {
        
        if (cost < 0.01) {
            return ReturnUtil.returnMap(0, ConstantMsg.order_cost_not_less);
        }
        
        OrderContent orderContent = (OrderContent)commonDao.getObjectByUniqueCode(Constant.orderContent, "clientCode" , clientCode);
        if (orderContent == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.order_not_exist_error);
        }
        
        BigDecimal oldCost = new BigDecimal(orderContent.getCost()).divide(new BigDecimal(100));
        
        if (orderContent.getIsOver() == Constant.order_status_true
            || orderContent.getIsDelete() == Constant.order_status_true) {
            return ReturnUtil.returnMap(0, ConstantMsg.order_over);
        }
        
        orderContent.setCost(cost);
        
        commonDao.update(orderContent);
        
        LogWork logWork = new LogWork();
        logWork.setAddDate(new Date());
        logWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
        logWork.setValue(orderContent.getOrderNum());
        logWork.setRemark("修改票单【"+orderContent.getClientCode()+"】成本价【新值:"+cost+"】【旧值:"+oldCost+"】");
        logWork.setTableName("order_core");
        logWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
        commonDao.save(logWork);
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> updateOrderContentTkviewAndCount(String clientCode , String tkview ,
                                                                Integer count) {
        
        OrderContent orderContent = (OrderContent)commonDao.getObjectByUniqueCode(Constant.orderContent, "clientCode" , clientCode);
        if (orderContent == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.order_not_exist_error);
        }
        
        if (orderContent.getIsOver() == Constant.order_status_true
            || orderContent.getIsDelete() == Constant.order_status_true) {
            return ReturnUtil.returnMap(0, ConstantMsg.order_over);
        }
        
        Boolean isRefund = productUtil.refundStock(orderContent.getStock(), orderContent.getCount());
        if (!isRefund) {
            return ReturnUtil.returnMap(0, ConstantMsg.order_refound_stock_error);
        }
        
        Tkview tkviewObj = (Tkview)commonDao.getObjectByUniqueCode(Constant.tkview, Constant.numberCode, tkview);
        
        if (tkviewObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.tkview_not_exist_error);
        }
        
        if (orderContent.getTkview().equals(tkviewObj.getNumberCode()) && orderContent.getCount() == count) {
            return ReturnUtil.returnMap(0, "操作无改动!");
        }
        
        Map<Integer, String> resultMap = productUtil.substractStock(tkviewObj.getNumberCode(), count);
        
        String remark = "";
        //0:库存不足, 1:无超卖, 2:有超卖 
        if (null != resultMap.get(0)) {
            return ReturnUtil.returnMap(0, "库存不足!");
        }else if (null != resultMap.get(1)) {
            remark += "单品【新值:"+tkviewObj.getNumberCode()+"】【旧值:"+orderContent.getTkview()+"】数量【新值:"+count+"】【旧值:"+orderContent.getCount()+"】";
            orderContent.setTkview(tkviewObj.getNumberCode());
            orderContent.setTkviewNameCn(tkviewObj.getNameCn());
            orderContent.setCount(count);
            
            Stock stock = (Stock)commonDao.getObjectByUniqueCode(Constant.stock, Constant.numberCode ,resultMap.get(1));
            if (stock == null) {
                return ReturnUtil.returnMap(0, ConstantMsg.stock_not_exist_error);
            }
            
            orderContent.setStock(stock.getNumberCode());
            orderContent.setCost(stock.getCost().intValue());
            
            if (null != resultMap.get(2)) {
                orderContent.setIsSurpass(1);
            }
            
            LogWork logWork = new LogWork();
            logWork.setAddDate(new Date());
            logWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
            logWork.setValue(orderContent.getOrderNum());
            logWork.setRemark("修改票单【"+orderContent.getClientCode()+"】"+remark);
            logWork.setTableName("order_core");
            logWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
            commonDao.save(logWork);
            commonDao.flush();
            
            commonDao.update(orderContent);
        }
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> updateOrderContentSend(String clientCode , Integer isSend) {
        
        if(isSend == null){
            return ReturnUtil.returnMap(0, "参数错误!");
        }
        
        OrderContent orderContent = (OrderContent)commonDao.getObjectByUniqueCode("OrderContent", "clientCode", clientCode);
        if(orderContent == null){
            return ReturnUtil.returnMap(0, "订单不存在!");
        }
        if(orderContent.getIsOver() == Constant.order_status_true
            || orderContent.getIsDelete() == Constant.order_status_true){
            
            return ReturnUtil.returnMap(0, ConstantMsg.order_over);
        }
        if(orderContent.getIsSend() == Constant.order_status_true){
            return ReturnUtil.returnMap(0, "该订单已发货!");
        }
        
        Stock stock = (Stock)commonDao.getObjectByUniqueCode(Constant.stock, Constant.numberCode, orderContent.getStock());
        if (stock == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.stock_not_exist_error);
        }
        
        if (stock.getType() == 0) {
            Map<Integer, String> resultMap = productUtil.substractInquiryStock(orderContent.getTkview(), orderContent.getCount());
            if (resultMap.get(0) != null) {
                return ReturnUtil.returnMap(0, "库存不足!");
            }
        }else {
            Map<Integer, String> resultMap = productUtil.substractStock(orderContent.getTkview(), orderContent.getCount());
            if (resultMap.get(0) != null) {
                return ReturnUtil.returnMap(0, "库存不足!");
            }else if (resultMap.get(1) != null) {
                if (resultMap.get(2) != null) {
                    orderContent.setIsSurpass(1);
                }
            }
        }
        
        Date date = new Date();
        
        orderContent.setIsSend(isSend);
        orderContent.setAlterDate(date);
        commonDao.update(orderContent);
        commonDao.flush();
        
        LogWork logWork = new LogWork();
        logWork.setAddDate(date);
        logWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
        logWork.setTableName("order_core");
        logWork.setUsername(MyRequestUtil.getUsername());
        logWork.setValue(orderContent.getOrderNum());
        logWork.setRemark("订单【"+orderContent.getClientCode()+"】发货!");
        
        commonDao.save(logWork);
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> updateOrderContentRefound(String clientCode,
                                                         Integer money,
                                                         Integer stock) {
        
        if(money == null || stock == null){
            return ReturnUtil.returnMap(0, "参数错误!");
        }
        
        OrderContent orderContent = (OrderContent)commonDao.getObjectByUniqueCode("OrderContent", "clientCode", clientCode);
        if(orderContent == null){
            return ReturnUtil.returnMap(0, "订单不存在!");
        }
        
        int payAmount = orderContent.getPayAmount().intValue();
        int stockCount = orderContent.getCount();
        int refoundAmount = orderContent.getRefoundAmount();
        int refoundStock = orderContent.getRefoundStock();
        
        
        if(orderContent.getIsOver() == Constant.order_status_true
            || orderContent.getIsDelete() == Constant.order_status_true){
            
            return ReturnUtil.returnMap(0, ConstantMsg.order_over);
        }
        
        if(money > (payAmount - refoundAmount)){
            return ReturnUtil.returnMap(0, "该订单仅能退【￥:" + (payAmount - refoundAmount) + "】!");
        }
        if(stock > (stockCount - refoundStock)){
            return ReturnUtil.returnMap(0, "该订单仅能退库存【" + (stockCount - refoundStock) + "】件!");
        }
        
        Date date = new Date();
        
        if(money > 0){
            orderContent.setIsRefoundMoney(1);
            orderContent.setRefoundAmount(orderContent.getRefoundAmount() + money);
        }
        if(stock > 0){
            orderContent.setIsRefoundStock(1);
            orderContent.setRefoundAmount(orderContent.getRefoundAmount() + money);
            Boolean isRefound = productUtil.refundStock(orderContent.getStock(), orderContent.getCount());
            if (!isRefound) {
                return ReturnUtil.returnMap(0, ConstantMsg.order_refound_stock_error);
            }
        }
        
        orderContent.setAlterDate(date);
        commonDao.update(orderContent);
        commonDao.flush();
        
        LogWork logWork = new LogWork();
        logWork.setAddDate(date);
        logWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
        logWork.setTableName("order_core");
        logWork.setUsername(MyRequestUtil.getUsername());
        logWork.setValue(orderContent.getOrderNum());
        logWork.setRemark("退款【￥:"+money/100+"】, 回退库存:【"+stock+"】");
        
        commonDao.save(logWork);
        
        return ReturnUtil.returnMap(1, null);
    }
    
    @Override
    public Map<String, Object> updateOrderContentForLinkman(String orderNum,
                                                            String linkman,
                                                            String linkPhone,
                                                            String email,
                                                            String address) {
        if (orderNum == null && "".equals(orderNum)) {
            return ReturnUtil.returnMap(0, "订单编号为空!");
        }
        Date date = new Date();
        
        String orderHql = "from OrderContent where orderNum = '" + orderNum + "'";
        @SuppressWarnings("unchecked")
        List<OrderContent> contents = commonDao.getQuery(orderHql).list();
        for (OrderContent orderContent : contents) {
            orderContent.setLinkMan(linkman);
            orderContent.setLinkPhone(linkPhone);
            orderContent.setEmail(email);
            orderContent.setAddress(address);
            orderContent.setAlterDate(date);
            commonDao.update(orderContent);
        }
        commonDao.flush();
        
        LogWork logWork = new LogWork();
        logWork.setAddDate(date);
        logWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
        logWork.setTableName("order_core");
        logWork.setUsername(MyRequestUtil.getUsername());
        logWork.setValue(contents.get(0).getOrderNum());
        logWork.setRemark("修改联系人信息!");
        
        commonDao.save(logWork);

        return ReturnUtil.returnMap(1, null);
    }
    
    @Override
    public Map<String, Object> getPersonInfoList(String clientCode) {
        OrderContent orderContent = null;
        Map<String, Object> mapContent = null;
        if (clientCode != null && !"".equals(clientCode)) {

            orderContent = (OrderContent)commonDao.getObjectByUniqueCode("OrderContent","clientCode", clientCode);
            if (orderContent == null) {
                ReturnUtil.returnMap(0, "参数错误!");
            }
            String numberCodes = orderContent.getPersonInfo();

            String hql = "from PersonInfo p where p.numberCode in ("
                         + StringTool.StrToSqlString(numberCodes) + ") ";

            @SuppressWarnings("unchecked")
            List<PersonInfo> personInfoList = commonDao.getQuery(hql).list();
            mapContent = new HashMap<String, Object>();
            mapContent.put("rows", personInfoList);
            return ReturnUtil.returnMap(1, mapContent);
        }else {
            mapContent = new HashMap<String, Object>();
            mapContent.put("rows", null);
            return ReturnUtil.returnMap(1, mapContent);
        }
    }
    
    @Override
    public Map<String, Object> updateOrderContentForPerson(String clientCode , String personInfo ,
                                                           String isImplement) {
        OrderContent orderContent = (OrderContent)commonDao.getObjectByUniqueCode("OrderContent","clientCode", clientCode);
        if (orderContent == null) {
            ReturnUtil.returnMap(0, "参数错误!");
        }

        orderContent.setPersonInfo(personInfo);
        commonDao.update(orderContent);
        
        LogWork logWork = new LogWork();
        logWork.setAddDate(new Date());
        logWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
        logWork.setValue(orderContent.getOrderNum());
        logWork.setRemark("修改出行人信息");
        logWork.setTableName("order_core");
        logWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
        commonDao.save(logWork);

        return ReturnUtil.returnMap(1, null);
    }

	@Override
	public Map<String, Object> deleteOrderContent(String orderNum,
			String clientCode) {
		String hql = "from OrderContent where orderNum = '" + orderNum + "'";
        @SuppressWarnings("unchecked")
        List<OrderContent> orderContents = commonDao.getQuery(hql).list();

        for (OrderContent o : orderContents) {
            if (o.getIsOver() != 1) {
                return ReturnUtil.returnMap(0, "未结束订单，禁止删除!");
            }
            String roomSql = "delete from room where shipment = '" + o.getShipment()
                             + "'";
            roomSql += " and cruise = '" + o.getCruise() + "'";
            Query roomQuery = commonDao.getSqlQuery(roomSql);
            roomQuery.executeUpdate();
            
            Boolean isRefund = productUtil.refundStock(o.getStock(), o.getCount());
            if (!isRefund) {
                return ReturnUtil.returnMap(0, "库存不存在!");
            }

            o.setIsDelete(1);
            commonDao.update(o);
        }
        
        LogWork logWork = new LogWork();
        logWork.setAddDate(new Date());
        logWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
        logWork.setValue(orderNum);
        logWork.setRemark("从cms删除订单!");
        logWork.setTableName("order_core");
        logWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
        commonDao.save(logWork);
        
        return ReturnUtil.returnMap(1, null);
	}

}