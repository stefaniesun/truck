package xyz.work.taobao.svc.imp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
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
import xyz.work.excel.model.PriceStrategy;
import xyz.work.resources.model.Channel;
import xyz.work.resources.model.Provider;
import xyz.work.resources.model.Stock;
import xyz.work.resources.model.Tkview;
import xyz.work.sell.model.OrderContent;
import xyz.work.taobao.model.TaobaoOrderTbo;
import xyz.work.taobao.model.TaobaoOrderTbt;
import xyz.work.taobao.svc.TaobaoPoolSvc;

@Service
public class TaobaoPoolSvcImp implements TaobaoPoolSvc {

    @Autowired
    CommonDao commonDao;
    
    @Autowired
    OrderContentUtil orderContentUtil;
    
    @Autowired
    PossessorUtil possessorUtil;
    
    @Autowired
    ProductUtil productUtil;
    
    @Override
    public Map<String, Object> queryTaobaoPool(int offset , 
                                                int pageSize,
                                                String tid,
                                                String nickName,
                                                String sort,
                                                String order) {
        
        String possessor = MyRequestUtil.getPossessor();
        
        StringBuffer tbtSql = new StringBuffer("from TaobaoOrderTbt ct where 1=1");
        
        {
            if (StringTool.isNotNull(possessor)) {
                List<String> relates = possessorUtil.getDecideMap(Constant.relate_type_channel).keySet().contains(Constant.relate_type_channel)?possessorUtil.getDecideMap(Constant.relate_type_channel).get(Constant.relate_type_channel):new ArrayList<String>();
                tbtSql.append(" and ct.channel in ("+StringTool.listToSqlString(relates)+")");
            }
        }
        
        if (StringTool.isNotNull(tid)) {
            tbtSql.append(" and tid = '"+tid+"'");
        }
        
        if (StringTool.isNotNull(nickName)) {
            tbtSql.append(" and buyNick like '%"+nickName+"%'");
        }
        
        if (sort != null && !"".equals(sort) && order != null && !"".equals(order)) {
            if (",alterDate,".contains("," + sort+ ",") && ",asc,desc,".contains("," + order + ",")) {
                tbtSql.append(" order by ct." + sort + " " + order);
            }
        }else {
            tbtSql.append(" order by ct.alterDate desc");
        }
        
        Query countQuery = commonDao.getQuery("select count(*) " + tbtSql.toString());
        Number countNum = (Number)countQuery.uniqueResult();
        int count = countNum == null ? 0 : countNum.intValue();

        Query query = commonDao.getQuery(tbtSql.toString());
        query.setMaxResults(pageSize);
        query.setFirstResult(offset);
        @SuppressWarnings("unchecked")
        List<TaobaoOrderTbt> taobaoOrderTbtList = query.list();
        
        List<String> tidList = new ArrayList<String>();
        
        for (TaobaoOrderTbt tbt : taobaoOrderTbtList) {
            tidList.add(tbt.getTid());
        }
        
        String taobaoorderTboSql = "select tid,travel_contact_name,travel_contact_mobile,travel_contact_mail,CONCAT(trip_start_date,'')  from taobao_order_tbo where tid in ("+StringTool.listToSqlString(tidList)+") group by tid";
        
        @SuppressWarnings("unchecked")
        List<Object[]> tboList = commonDao.getSqlQuery(taobaoorderTboSql).list();
        
        for (TaobaoOrderTbt tbt : taobaoOrderTbtList) {
            for (Object[] tbo : tboList) {
                if (tbt.getTid().equals((String)tbo[0])) {
                    tbt.setTravelContactName((String)tbo[1]);
                    tbt.setTravelContactMobile((String)tbo[2]);
                    tbt.setTravelContactMail((String)tbo[3]);
                    if ((String)tbo[4] != null) {
                        tbt.setTripStartDate(DateUtil.stringToDate((String)tbo[4]));
                    }
                    break;
                }
            }
        }

        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", count);
        mapContent.put("rows", taobaoOrderTbtList);

        return ReturnUtil.returnMap(1, mapContent);
    }

    @Override
    public Map<String, Object> queryTaobaoOrderDetail(String tid) {
        
        String hql = "from TaobaoOrderTbo where tid = '"+tid+"'";

        Query queryDetail = commonDao.getQuery(hql);
        @SuppressWarnings("unchecked")
        List<OrderContent> OrderContents = queryDetail.list();
        commonDao.clear();
        String logSql = "from LogWork where value='" + tid + "' ORDER BY addDate DESC ";
        Query queryLog = commonDao.getQuery(logSql);
        @SuppressWarnings("unchecked")
        List<LogWork> logWorkList = queryLog.list();
        Map<String, Object> contents = new HashMap<String, Object>();

        contents.put("OrderContents", OrderContents);
        contents.put("logWorkList", logWorkList);
        return ReturnUtil.returnMap(1, contents);
        
    }

    @Override
    public Map<String, Object> addOrderContentByTaobao(String tkviewJsonStr , BigDecimal payment ,
                                              String linkMan , String linkPhone , String endDate ,
                                              String remark , String tid) {
        
        String username = MyRequestUtil.getSecurityLogin().getUsername();
        
        Date date = new Date();
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> tkviewJsonList = JSON.toObject(tkviewJsonStr, ArrayList.class);

        String orderNum = "";
        BigDecimal orderPrice = new BigDecimal(0);
        
        TaobaoOrderTbt taobaoOrderTbt = null;
        
        orderNum = StringUtil.get_new_orderNum(null);
        
        TaobaoOrderTbo taobaoOrderTboObj = null;
        
        for (int i = 0; i < tkviewJsonList.size(); i++ ) {
            Map<String, Object> map = tkviewJsonList.get(i);
            
            BigDecimal count = new BigDecimal(map.get("count")==null?"0":map.get("count").toString());
            String tkview = map.get("tkview")==null?"":map.get("tkview").toString();
            String oid = map.get("oid")==null?"":map.get("oid").toString();
            
            if (!oid.equals("")) {
                taobaoOrderTboObj = (TaobaoOrderTbo)commonDao.getObjectByUniqueCode(Constant.taobaoOrderTbo, Constant.taobao_oid, oid);
                if (taobaoOrderTboObj == null) {
                    return ReturnUtil.returnMap(0, "淘宝订单不存在!");
                }
                String orderContentIsTidSql = "select count(*) from order_content where tid = '"+taobaoOrderTboObj.getTid()+"'";
                
                Query countQuery = commonDao.getSqlQuery(orderContentIsTidSql);
                
                Number countNum = (Number)countQuery.uniqueResult();
                int tidCount = countNum == null ? 0 : countNum.intValue();
                if (tidCount > 0 ) {
                    return ReturnUtil.returnMap(0, "请勿重复添加订单!");
                }
            }
            
            if (taobaoOrderTbt == null) {
                taobaoOrderTbt = (TaobaoOrderTbt)commonDao.getObjectByUniqueCode(Constant.taobaoOrderTbt, Constant.taobao_tid, taobaoOrderTboObj.getTid());
                if (taobaoOrderTbt == null) {
                    return ReturnUtil.returnMap(0, "淘宝订单不存在!");
                }
            }
            
            Tkview tkviewObj = (Tkview)commonDao.getObjectByUniqueCode(Constant.tkview, Constant.numberCode, tkview);
            
            if (tkviewObj == null) {
                return ReturnUtil.returnMap(0, ConstantMsg.tkview_not_exist_error);
            }
            
            OrderContent orderContent = new OrderContent();
            
            Map<Integer, String> resultMap = productUtil.substractStock(tkviewObj.getNumberCode(), count.intValue());
            
            //0:库存不足, 1:无超卖, 2:有超卖 
            if (null != resultMap.get(0)) {
                return ReturnUtil.returnMap(0, "库存不足!");
            }else if (null != resultMap.get(1)) {
                
                Channel channel = (Channel)commonDao.getObjectByUniqueCode(Constant.channel, Constant.numberCode, taobaoOrderTbt.getChannel());
                if (channel == null) {
                    return ReturnUtil.returnMap(0, ConstantMsg.channel_not_exist_error);
                }
                
                Stock stock = (Stock)commonDao.getObjectByUniqueCode(Constant.stock, Constant.numberCode ,resultMap.get(1));
                if (stock == null) {
                    return ReturnUtil.returnMap(0, ConstantMsg.stock_not_exist_error);
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
                
                int isPay = 0;
                int isSend = 0;
                int isOver = 0;
                int isRefund = 0;
                int isReturnGoods = 0;
                
                 if (taobaoOrderTbt.getStatus().equals(Constant.taobao_order_status_wait_buyer_confirm_goods)) {
                     isSend = 1;
                     isPay = 1;
                 }else if (taobaoOrderTbt.getStatus().equals(Constant.taobao_order_status_wait_seller_send_goods)) {
                     isPay = 1;
                 }else if (taobaoOrderTbt.getStatus().equals(Constant.taobao_order_status_trade_finished)) {
                     isPay = 1;
                     isSend = 1;
                 }else if (taobaoOrderTbt.getStatus().equals(Constant.taobao_order_status_trade_closed)) {
                     isOver = 1;
                 }else if (taobaoOrderTbt.getStatus().equals(Constant.taobao_order_status_trade_closed_by_taobao)) {
                     isOver = 1;
                 }
                 String refundStatus = taobaoOrderTboObj.getRefundStatus()==null||taobaoOrderTboObj.getRefundStatus().equals("")?"":taobaoOrderTboObj.getRefundStatus();
                 if (refundStatus.equals(Constant.taobao_order_refund_status_wait_buyer_return_goods)) {
                     isRefund = 1;
                 }else if (taobaoOrderTboObj.getRefundStatus().equals(Constant.taobao_order_refund_status_wait_seller_confirm_goods)) {
                     isRefund = 1;
                 }else if (taobaoOrderTboObj.getRefundStatus().equals(Constant.taobao_order_refund_status_closed)) {
                     isRefund = -1;
                 }else if (taobaoOrderTboObj.getRefundStatus().equals(Constant.taobao_order_refund_status_success)) {
                     isRefund = 1;
                     isReturnGoods = 1;
                 }
                
                orderContent.setAddDate(date);
                orderContent.setAddress(taobaoOrderTbt.getReceiverAddress());
                orderContent.setAlterDate(date);
                orderContent.setOperaterAdd(username);
                orderContent.setOperaterAlter(username);
                orderContent.setBuyer(taobaoOrderTbt.getBuyNick());
                orderContent.setCabin(cabin.getNumberCode());
                orderContent.setCabinNameCn(cabin.getNameCn());
                orderContent.setVolume(cabin.getVolume());
                orderContent.setChannel(channel.getNumberCode());
                orderContent.setChannelNameCn(channel.getNameCn());
                orderContent.setClientCode(StringUtil.get_new_clientCode(null));
                orderContent.setCost(stock.getCost().intValue());
                orderContent.setCount(count.intValue());
                orderContent.setCruise(cruise.getNumberCode());
                orderContent.setChannel(channel.getNumberCode());
                orderContent.setChannelNameCn(channel.getNameCn());
                orderContent.setCruiseNameCn(cruise.getNameCn());
                orderContent.setEarnest(0);
                orderContent.setEmail(taobaoOrderTboObj.getTravelContactMail());
                orderContent.setFlag(Constant.order_flag_gray);
                orderContent.setIsDelete(0);
                orderContent.setIsOver(isOver);
                orderContent.setIsPay(isPay);
                if (isRefund == 1) {
                    orderContent.setIsRefoundMoney(1);
                    orderContent.setRefoundAmount(taobaoOrderTboObj.getPayment().intValue());
                    if (isReturnGoods == 1) {
                        Boolean isTrue = productUtil.refundStock(stock.getNumberCode(), taobaoOrderTboObj.getNum());
                        if (isTrue) {
                            orderContent.setIsRefoundStock(1);
                            orderContent.setRefoundStock(taobaoOrderTboObj.getNum());
                        }else {
                            orderContent.setIsRefoundStock(0);
                        }
                    }
                }
                
                orderContent.setIsSend(isSend);
                orderContent.setIsSort(0);
                if (resultMap.get(2) != null) {
                    orderContent.setIsSurpass(1);
                }
                orderContent.setLinkMan(taobaoOrderTboObj.getTravelContactName());
                orderContent.setLinkPhone(taobaoOrderTboObj.getTravelContactMobile());
                orderContent.setOid(taobaoOrderTboObj.getOid());
                orderContent.setOperaterAdd(Constant.taobao_oper_name_cn);
                orderContent.setOperaterAlter(Constant.taobao_oper_name_cn);
                orderContent.setOrderNum(orderNum);
                orderContent.setOrderPrice(taobaoOrderTbt.getReceivedPayment().intValue());
                orderContent.setOverDate(taobaoOrderTbt.getEnd_time());
                orderContent.setPayAmount(taobaoOrderTboObj.getPayment());
                if (isPay == 1) {
                    orderContent.setPayDate(taobaoOrderTbt.getPayTime());
                }
                orderContent.setPersonInfo("");
                orderContent.setProduct(taobaoOrderTboObj.getNumIid());
                orderContent.setProductNameCn(taobaoOrderTboObj.getTitle());
                orderContent.setProductPrice(taobaoOrderTboObj.getPrice().intValue());
                orderContent.setProvider(provider.getNumberCode());
                orderContent.setProviderNameCn(provider.getNameCn());
                
                orderContent.setRemark(taobaoOrderTbt.getRemark());
                orderContent.setShipment(shipment.getNumberCode());
                orderContent.setShipmentMark(shipment.getMark());
                orderContent.setShipmentTravelDate(shipment.getTravelDate());
                orderContent.setSource("TB");
                orderContent.setStock(stock.getNumberCode());
                orderContent.setTid(taobaoOrderTbt.getTid());
                orderContent.setTkview(tkviewObj.getNumberCode());
                orderContent.setTkviewNameCn(tkviewObj.getNameCn());
                orderContent.setLinkMan(taobaoOrderTbt.getReceiverName());
                orderContent.setLinkPhone(taobaoOrderTbt.getReceiverMobile());
                
                orderPrice.add(taobaoOrderTboObj.getPrice());
            }
            
            commonDao.save(orderContent);
            commonDao.flush();
            
        }
        
        commonDao.update(taobaoOrderTbt);
        commonDao.flush();
        
        String sql = "update order_content set order_price = "+orderPrice+" where order_num = '"+orderNum+"'";
        commonDao.getSqlQuery(sql).executeUpdate();
        
        LogWork logWork = new LogWork();
        logWork.setAddDate(new Date());
        logWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
        logWork.setValue(orderNum);
        logWork.setRemark("淘宝池【"+tid+"】下单!");
        logWork.setTableName("order_core");
        logWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
        commonDao.save(logWork);
        
        LogWork logWorkForTaobao = new LogWork();
        logWorkForTaobao.setAddDate(new Date());
        logWorkForTaobao.setNumberCode(UUIDUtil.getUUIDStringFor32());
        logWorkForTaobao.setValue(tid);
        logWorkForTaobao.setRemark("淘宝池订单添加到订单【"+orderNum+"】");
        logWorkForTaobao.setTableName("order_core_taobao");
        logWorkForTaobao.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
        commonDao.save(logWorkForTaobao);

        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> getStockByTkview(String tkview) {
        if(StringTool.isEmpty(tkview)){
            return ReturnUtil.returnMap(0, "单品选择错误!");
        }
        
        Tkview tkviewObj = (Tkview)commonDao.getObjectByUniqueCode("Tkview", "numberCode", tkview);
        
        Cabin cabinObj = (Cabin)commonDao.getObjectByUniqueCode("Cabin", "numberCode", tkviewObj.getCabin());
        
        String hql = "from Stock s where 1=1 ";
        hql += "and s.tkview = '"+ tkview +"' ";
        hql += "order by s.tkview";
        @SuppressWarnings("unchecked")
        List<Stock> stockList = commonDao.queryByHql(hql);
        
        List<String> providerNumber = new ArrayList<String>();
        for(Stock s : stockList){
            providerNumber.add(s.getProvider());
            s.setStock(s.getStock().multiply(cabinObj.getMaxVolume()));
        }
        
        String providerHql = "from Provider p where p.numberCode in ("+ StringTool.listToSqlString(providerNumber) +") ";
        @SuppressWarnings("unchecked")
        List<Provider> providerList = commonDao.queryByHql(providerHql);
        
        for(Stock s : stockList){
            s.setTkviewNameCn(tkviewObj.getNameCn());
            tkviewObj.getStocks().add(s);
            
            for(Provider p : providerList){
                if(s.getProvider().equals(p.getNumberCode())){
                    s.setProviderNameCn(p.getNameCn());
                    s.setProviderMark(p.getMark());
                    break;
                }
            }
        }
        
        if(tkviewObj.getStocks().size()>0){
            Collections.sort(tkviewObj.getStocks());
            tkviewObj.setCost(tkviewObj.getStocks().get(0).getCost());
            tkviewObj.setStock(tkviewObj.getStocks().get(0).getStock());
        }
        
        List<Stock> resultStockList=new ArrayList<Stock>();
        resultStockList.addAll(tkviewObj.getStocks());
        
        String priceHql = "from PriceStrategy";
        @SuppressWarnings("unchecked")
        List<PriceStrategy> priceList = commonDao.queryByHql(priceHql);
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("stockList", resultStockList);
        map.put("priceList", priceList);
        
        return ReturnUtil.returnMap(1, map);
    }
    
}