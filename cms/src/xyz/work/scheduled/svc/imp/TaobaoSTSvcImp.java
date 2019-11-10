package xyz.work.scheduled.svc.imp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlitripTravelTradeCloseRequest;
import com.taobao.api.request.TradesSoldIncrementGetRequest;
import com.taobao.api.response.AlitripTravelTradeCloseResponse;
import com.taobao.api.response.TradesSoldIncrementGetResponse;

import xyz.dao.CommonDao;
import xyz.exception.MyExceptionForXyz;
import xyz.filter.JSON;
import xyz.filter.ReturnUtil;
import xyz.util.Constant;
import xyz.util.ConstantMsg;
import xyz.util.DateUtil;
import xyz.util.PossessorUtil;
import xyz.util.ProductUtil;
import xyz.util.StringTool;
import xyz.util.StringUtil;
import xyz.util.TaobaoUtil;
import xyz.util.UUIDUtil;
import xyz.work.base.model.Cabin;
import xyz.work.base.model.Cruise;
import xyz.work.base.model.Shipment;
import xyz.work.core.model.LogWork;
import xyz.work.core.model.PlanWork;
import xyz.work.scheduled.svc.TaobaoSTSvc;
import xyz.work.sell.model.OrderContent;
import xyz.work.resources.model.Channel;
import xyz.work.resources.model.Provider;
import xyz.work.resources.model.Stock;
import xyz.work.resources.model.Tkview;
import xyz.work.taobao.model.TaobaoOrderTbo;
import xyz.work.taobao.model.TaobaoOrderTbt;

@Service
public class TaobaoSTSvcImp implements TaobaoSTSvc {

    @Autowired
    CommonDao commonDao;
    
    @Autowired
    TaobaoUtil taobaoUtil;
    
    @Autowired
    PossessorUtil possessorUtil;
    
    @Autowired
    ProductUtil productUtil;
    
    private Logger log = LoggerFactory.getLogger(TaobaoSTSvcImp.class);
    
    @Override
    public Map<String, Object> addIncrementTid() {
        
        String channelHql = "from Channel";
        @SuppressWarnings("unchecked")
        List<Channel> channelList = commonDao.queryByHql(channelHql);
        
        if (channelList.size() <= 0) {
            return ReturnUtil.returnMap(0, ConstantMsg.channel_not_exist_error);
        }
        
        Calendar beforeTime = Calendar.getInstance();
        beforeTime.add(Calendar.MINUTE, -5);
        
        Date beforeDate = beforeTime.getTime();
        Date nowDate = new Date();
        
        for (Channel c : channelList) {
            
            TaobaoClient client = new DefaultTaobaoClient(Constant.taobaoUrl, c.getAppKey(), c.getAppSecret());
            TradesSoldIncrementGetRequest req = new TradesSoldIncrementGetRequest();
            req.setFields(Constant.taobao_tid);
            req.setStartModified(beforeDate);
            req.setEndModified(nowDate);
           
            TradesSoldIncrementGetResponse rsp = null;
            try {
                rsp = client.execute(req, c.getSessionKey());
            }
            catch (ApiException e) {
                throw new MyExceptionForXyz(e.getMessage());
            }
            @SuppressWarnings("unchecked")
            HashMap<String, Object> tradesSoldGetResponseMap = JSON.toObject(rsp.getBody(), HashMap.class);
            
            @SuppressWarnings("unchecked")
            HashMap<String, Object> tradesMap = (HashMap<String, Object>)tradesSoldGetResponseMap.get(Constant.taobao_trades_sold_map_key);
            
            if (tradesMap != null) {
                @SuppressWarnings("unchecked")
                HashMap<String, Object> tradeMap = (HashMap<String, Object>)tradesMap.get(Constant.taobao_trades_map_key);
                
                if (tradeMap == null) {
                    return ReturnUtil.returnMap(1, Constant.nulo);
                }
                
                @SuppressWarnings("unchecked")
                List<HashMap<String, Object>> tradeList = (List<HashMap<String, Object>>)tradeMap.get("trade");
                
                if (tradeList != null) {
                    List<String> tidList = new ArrayList<String>();
                    
                    for (HashMap<String, Object> h : tradeList) {
                        tidList.add(h.get(Constant.taobao_tid).toString());
                        
                        String tid = h.get(Constant.taobao_tid).toString();
                        
                        PlanWork planWork=new PlanWork();
                        planWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
                        planWork.setAddDate(new Date());
                        planWork.setAlterDate(new Date());
                        planWork.setContent(tid);
                        planWork.setRemark(c.getNumberCode());
                        planWork.setWorkType(Constant.taobao_work_type);
                        commonDao.save(planWork);
                        
                    }
                }
                
            }else {
                @SuppressWarnings("unchecked")
                HashMap<String, Object> errorResponseList = (HashMap<String, Object>)tradesSoldGetResponseMap.get(Constant.taobao_error_response);
                log.error(errorResponseList.get("msg")+"+"+errorResponseList.get("sub_msg"));
            }
        }
            
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> addTaobaoOrderDetails(PlanWork planWork) {
        if(planWork==null){
            return ReturnUtil.returnMap(0, null);
        }
        
        Map<String, Object> map = taobaoUtil.getTaobaoOrderDetails(planWork);
        @SuppressWarnings("unchecked")
        Map<String, Object> content = (Map<String, Object>)map.get(Constant.content);
        
        if(content == null){
            
            Map<String, Object> returnMap = new HashMap<String, Object>();
            
            returnMap.put("status", 0);
            returnMap.put("msg", map.get(Constant.msg).toString());
            
            return returnMap;
        }else{
            
            String reamrk = "";
            
            TaobaoOrderTbt orderTbtObj = (TaobaoOrderTbt)content.get(Constant.taobao_order_tbt_content_key);
            @SuppressWarnings("unchecked")
            List<TaobaoOrderTbo> orderTbosList = (List<TaobaoOrderTbo>)content.get(Constant.taobao_order_tbo_content_key);
            
            TaobaoOrderTbt oldOrderTbtObj = (TaobaoOrderTbt)commonDao.getObjectByUniqueCode(Constant.taobaoOrderTbt, Constant.taobao_tid, orderTbtObj.getTid());
            if (oldOrderTbtObj != null) {
                if (orderTbtObj.getTid().equals(oldOrderTbtObj.getTid())) {
                    if (orderTbtObj.getReceivedPayment().intValue() != oldOrderTbtObj.getReceivedPayment().intValue()) {
                        reamrk += "主订单【"+oldOrderTbtObj.getTid()+"】【实收金额】【新值:"+orderTbtObj.getReceivedPayment()+"】【旧值:"+oldOrderTbtObj.getReceivedPayment()+"】";
                        oldOrderTbtObj.setReceivedPayment(orderTbtObj.getReceivedPayment());
                    }
                    
                    oldOrderTbtObj.setModified(orderTbtObj.getModified());
                    
                    if (orderTbtObj.getPayTime() == null && oldOrderTbtObj.getPayTime() != null) {
                        reamrk += "主订单【"+oldOrderTbtObj.getTid()+"】【付款时间】【新值:"+orderTbtObj.getPayTime()+"】";
                        oldOrderTbtObj.setPayTime(orderTbtObj.getPayTime());
                    }else if(orderTbtObj.getPayTime() == null && oldOrderTbtObj.getPayTime() == null){
                        ;
                    }else if(orderTbtObj.getPayTime() != null && oldOrderTbtObj.getPayTime() == null){
                        reamrk += "主订单【"+oldOrderTbtObj.getTid()+"】【付款时间】【新值:"+orderTbtObj.getPayTime()+"】";
                        oldOrderTbtObj.setPayTime(orderTbtObj.getPayTime());
                    }else if (orderTbtObj.getPayTime().compareTo(oldOrderTbtObj.getPayTime()) != 0) {
                        reamrk += "主订单【"+oldOrderTbtObj.getTid()+"】【付款时间】【新值:"+orderTbtObj.getPayTime()+"】【旧值:"+oldOrderTbtObj.getPayTime()+"】";
                        oldOrderTbtObj.setPayTime(orderTbtObj.getPayTime());
                    }
                    
                    if (orderTbtObj.getSellerFlag() != oldOrderTbtObj.getSellerFlag()) {
                        reamrk += "主订单【"+oldOrderTbtObj.getTid()+"】【旗子】【新值:"+orderTbtObj.getSellerFlag()+"】【旧值:"+oldOrderTbtObj.getSellerFlag()+"】";
                        oldOrderTbtObj.setSellerFlag(orderTbtObj.getSellerFlag());
                    }
                    
                    if (!orderTbtObj.getStatus().equals(oldOrderTbtObj.getStatus())) {
                        
                        String oldStatus = oldOrderTbtObj.getStatus();
                        String oldStatusCn = "";
                        String newStatus = orderTbtObj.getStatus();
                        String newStatusCn = "";
                        
                        if (oldStatus.equals(Constant.taobao_order_status_wait_buyer_pay)) {
                            oldStatusCn = "等待买家付款";
                        }else if (oldStatus.equals(Constant.taobao_order_status_wait_buyer_confirm_goods)) {
                            oldStatusCn = "卖家已发货";
                        }else if (oldStatus.equals(Constant.taobao_order_status_wait_seller_send_goods)) {
                            oldStatusCn = "买家已付款";
                        }else if (oldStatus.equals(Constant.taobao_order_status_trade_finished)) {
                            oldStatusCn = "交易成功";
                        }else if (oldStatus.equals(Constant.taobao_order_status_trade_closed)) {
                            oldStatusCn = "交易关闭";
                        }else if (oldStatus.equals(Constant.taobao_order_status_trade_closed_by_taobao)) {
                            oldStatusCn = "交易被淘宝关闭";
                        }else if (oldStatus.equals(Constant.taobao_order_status_trade_no_create_pay)) {
                            oldStatusCn = "没有创建外部交易(支付宝交易)";
                        }else if (oldStatus.equals(Constant.taobao_order_status_other)) {
                            oldStatusCn = "其他状态";
                        }
                        
                        if (newStatus.equals(Constant.taobao_order_status_wait_buyer_pay)) {
                            newStatusCn = "等待买家付款";
                        }else if (newStatus.equals(Constant.taobao_order_status_wait_buyer_confirm_goods)) {
                            newStatusCn = "卖家已发货";
                        }else if (newStatus.equals(Constant.taobao_order_status_wait_seller_send_goods)) {
                            newStatusCn = "买家已付款";
                        }else if (newStatus.equals(Constant.taobao_order_status_trade_finished)) {
                            newStatusCn = "交易成功";
                        }else if (newStatus.equals(Constant.taobao_order_status_trade_closed)) {
                            newStatusCn = "交易关闭";
                        }else if (newStatus.equals(Constant.taobao_order_status_trade_closed_by_taobao)) {
                            newStatusCn = "交易被淘宝关闭";
                        }else if (newStatus.equals(Constant.taobao_order_status_trade_no_create_pay)) {
                            newStatusCn = "没有创建外部交易(支付宝交易)";
                        }else if (newStatus.equals(Constant.taobao_order_status_other)) {
                            newStatusCn = "其他状态";
                        }
                        
                        reamrk += "主订单【"+oldOrderTbtObj.getTid()+"】【状态】【新值:"+newStatusCn+"】【旧值:"+oldStatusCn+"】";
                        
                        oldOrderTbtObj.setStatus(orderTbtObj.getStatus());
                    }
                }
                commonDao.update(oldOrderTbtObj);
            }else {
                commonDao.save(orderTbtObj);
            }
            
            reamrk = "";
            for (TaobaoOrderTbo orderTbo : orderTbosList) {
                                    
                TaobaoOrderTbo oldOrderTbo = (TaobaoOrderTbo)commonDao.getObjectByUniqueCode(Constant.taobaoOrderTbo, Constant.taobao_oid, orderTbo.getOid());
                if (oldOrderTbo != null) {
                    
                    if (orderTbo.getNum() != oldOrderTbo.getNum()) {
                        reamrk += "子订单【"+oldOrderTbo.getOid()+"】【数量】【新值:"+orderTbo.getNum()+"】【旧值:"+oldOrderTbo.getNum()+"】";
                        oldOrderTbo.setNum(orderTbo.getNum());
                    }
                    if (orderTbo.getPayment().intValue() != oldOrderTbo.getPayment().intValue()) {
                        reamrk += "子订单【"+oldOrderTbo.getOid()+"】【实付金额】【新值:"+orderTbo.getPayment()+"】【旧值:"+oldOrderTbo.getPayment()+"】";
                        oldOrderTbo.setPayment(orderTbo.getPayment());
                    }
                    if (orderTbo.getPrice().intValue() != oldOrderTbo.getPrice().intValue()) {
                        reamrk += "子订单【"+oldOrderTbo.getOid()+"】【实付金额】【新值:"+orderTbo.getPayment()+"】【旧值:"+oldOrderTbo.getPayment()+"】";
                        oldOrderTbo.setPrice(orderTbo.getPrice());
                    }
                    if (orderTbo.getTotalFee().intValue() != oldOrderTbo.getTotalFee().intValue()) {
                        reamrk += "子订单【"+oldOrderTbo.getOid()+"】【实付金额】【新值:"+orderTbo.getTotalFee()+"】【旧值:"+oldOrderTbo.getTotalFee()+"】";
                        oldOrderTbo.setTotalFee(orderTbo.getTotalFee());
                    }
                    if (orderTbo.getRefundStatus().equals(oldOrderTbo.getRefundStatus())) {
                        
                        String refundStatus = orderTbo.getStatus();
                        String refundStatusCn = "";
                        String oldRefundStatus = oldOrderTbo.getStatus();
                        String oldRefundStatusCn = "";
                        
                        if (refundStatus.equals(Constant.taobao_order_refund_status_wait_seller_agree)) {
                            refundStatusCn = "买家已经申请退款,等待卖家同意";
                        }else if (refundStatus.equals(Constant.taobao_order_refund_status_wait_buyer_return_goods)) {
                            refundStatusCn = "卖家已经同意退款,等待 买家退货";
                        }else if (refundStatus.equals(Constant.taobao_order_refund_status_wait_seller_confirm_goods)) {
                            refundStatusCn = "买家已经退货,等待卖家确认收货";
                        }else if (refundStatus.equals(Constant.taobao_order_refund_status_closed)) {
                            refundStatusCn = "退款关闭";
                        }else if (refundStatus.equals(Constant.taobao_order_refund_status_success)) {
                            refundStatusCn = "退款成功";
                        }else if (refundStatus.equals(Constant.taobao_order_refund_status_seller_refuse_buyer)) {
                            refundStatusCn = "卖家拒绝退款";
                        }else if (refundStatus.equals(Constant.taobao_order_refund_status_no_refund)) {
                            refundStatusCn = "没有退款";
                        }
                        
                        if (oldRefundStatus.equals(Constant.taobao_order_refund_status_wait_seller_agree)) {
                            oldRefundStatusCn = "买家已经申请退款,等待卖家同意";
                        }else if (oldRefundStatus.equals(Constant.taobao_order_refund_status_wait_buyer_return_goods)) {
                            oldRefundStatusCn = "卖家已经同意退款,等待 买家退货";
                        }else if (oldRefundStatus.equals(Constant.taobao_order_refund_status_wait_seller_confirm_goods)) {
                            oldRefundStatusCn = "买家已经退货,等待卖家确认收货";
                        }else if (oldRefundStatus.equals(Constant.taobao_order_refund_status_closed)) {
                            oldRefundStatusCn = "退款关闭";
                        }else if (oldRefundStatus.equals(Constant.taobao_order_refund_status_success)) {
                            oldRefundStatusCn = "退款成功";
                        }else if (oldRefundStatus.equals(Constant.taobao_order_refund_status_seller_refuse_buyer)) {
                            oldRefundStatusCn = "卖家拒绝退款";
                        }else if (oldRefundStatus.equals(Constant.taobao_order_refund_status_no_refund)) {
                            oldRefundStatusCn = "没有退款";
                        }
                        
                        if (StringTool.isNotNull(refundStatusCn) || StringTool.isNotNull(oldRefundStatusCn)) {
                            reamrk += "主订单【"+oldOrderTbo.getOid()+"】【退款状态】【新值:"+refundStatusCn+"】【旧值:"+oldRefundStatusCn+"】";
                            oldOrderTbo.setRefundStatus(orderTbo.getRefundStatus());
                        }
                    }
                    
                    if (!orderTbo.getStatus().equals(oldOrderTbo.getStatus())) {
                        
                        String oldStatus = oldOrderTbo.getStatus();
                        String oldStatusCn = "";
                        String newStatus = orderTbo.getStatus();
                        String newStatusCn = "";
                        
                        if (oldStatus.equals(Constant.taobao_order_status_wait_buyer_pay)) {
                            oldStatusCn = "等待买家付款";
                        }else if (oldStatus.equals(Constant.taobao_order_status_wait_buyer_confirm_goods)) {
                            oldStatusCn = "卖家已发货";
                        }else if (oldStatus.equals(Constant.taobao_order_status_wait_seller_send_goods)) {
                            oldStatusCn = "买家已付款";
                        }else if (oldStatus.equals(Constant.taobao_order_status_trade_finished)) {
                            oldStatusCn = "交易成功";
                        }else if (oldStatus.equals(Constant.taobao_order_status_trade_closed)) {
                            oldStatusCn = "交易关闭";
                        }else if (oldStatus.equals(Constant.taobao_order_status_trade_closed_by_taobao)) {
                            oldStatusCn = "交易被淘宝关闭";
                        }else if (oldStatus.equals(Constant.taobao_order_status_trade_no_create_pay)) {
                            oldStatusCn = "没有创建外部交易(支付宝交易)";
                        }else if (oldStatus.equals(Constant.taobao_order_status_other)) {
                            oldStatusCn = "其他状态";
                        }
                        
                        if (newStatus.equals(Constant.taobao_order_status_wait_buyer_pay)) {
                            newStatusCn = "等待买家付款";
                        }else if (newStatus.equals(Constant.taobao_order_status_wait_buyer_confirm_goods)) {
                            newStatusCn = "卖家已发货";
                        }else if (newStatus.equals(Constant.taobao_order_status_wait_seller_send_goods)) {
                            newStatusCn = "买家已付款";
                        }else if (newStatus.equals(Constant.taobao_order_status_trade_finished)) {
                            newStatusCn = "交易成功";
                        }else if (newStatus.equals(Constant.taobao_order_status_trade_closed)) {
                            newStatusCn = "交易关闭";
                        }else if (newStatus.equals(Constant.taobao_order_status_trade_closed_by_taobao)) {
                            newStatusCn = "交易被淘宝关闭";
                        }else if (newStatus.equals(Constant.taobao_order_status_trade_no_create_pay)) {
                            newStatusCn = "没有创建外部交易(支付宝交易)";
                        }else if (newStatus.equals(Constant.taobao_order_status_other)) {
                            newStatusCn = "其他状态";
                        }
                        
                        reamrk += "主订单【"+oldOrderTbo.getOid()+"】【状态】【新值:"+newStatusCn+"】【旧值:"+oldStatusCn+"】";
                        
                        oldOrderTbo.setStatus(orderTbo.getStatus());
                        
                    }
                    commonDao.update(oldOrderTbo);
                }else {
                    commonDao.save(orderTbo);
                }
                
            }
            
            Map<Integer, String> returnMap = addOrderContentByTaobao(orderTbtObj.getTid());
            
            if (null != returnMap.get(0)) {
               log.error(returnMap.get(0));
            }
            
            if (StringTool.isNotNull(reamrk)) {
                LogWork logWork = new LogWork();
                logWork.setAddDate(new Date());
                logWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
                logWork.setValue(orderTbtObj.getTid());
                logWork.setTableName("order_taobao");
                logWork.setRemark(reamrk);
                logWork.setUsername(Constant.taobao_oper_name_cn);
                commonDao.save(logWork);
            }
           
        }
        
        return ReturnUtil.returnMap(1, null);
    }
    
    private Map<Integer, String> addOrderContentByTaobao(String tid){
        
        Map<Integer, String> returnMap = new HashMap<>();
        
        TaobaoOrderTbt taobaoOrderTbt = (TaobaoOrderTbt)commonDao.getObjectByUniqueCode(Constant.taobaoOrderTbt, Constant.taobao_tid, tid);
        
        if (taobaoOrderTbt == null) {
            log.error("订单编号【"+tid+"】不存在!");
        }
        
        if (taobaoOrderTbt.getStatus().equals(Constant.taobao_order_status_wait_buyer_pay) || taobaoOrderTbt.getStatus().equals(Constant.taobao_order_status_trade_closed) || taobaoOrderTbt.getStatus().equals(Constant.taobao_order_status_trade_closed_by_taobao) || taobaoOrderTbt.getStatus().equals(Constant.taobao_order_status_trade_no_create_pay)) {
            returnMap.put(1, "订单未付款或已关闭!");
            return returnMap;
        }
        
        String taobaoOrderTboHql = "from TaobaoOrderTbo where tid = '"+taobaoOrderTbt.getTid()+"'";
        
        @SuppressWarnings("unchecked")
        List<TaobaoOrderTbo> taobaoOrderTboList = commonDao.queryByHql(taobaoOrderTboHql);
        
        Date date = new Date();
        String orderNum = StringUtil.get_new_orderNum("TB");
        
        for (TaobaoOrderTbo taobaoOrderTbo : taobaoOrderTboList) {
            
            String outSkuId = taobaoOrderTbo.getOutSkuId();
            Tkview tkview = (Tkview)commonDao.getObjectByUniqueCode(Constant.tkview, Constant.numberCode, outSkuId);
            
            if (tkview == null) {
                log.error("订单编号【"+tid+"】单品编号【"+outSkuId+"】单品不存在!");
                continue;
            }
            
            Map<Integer, String> resultMap = productUtil.substractStock(tkview.getNumberCode(), taobaoOrderTbo.getNum());
            
            Channel channel = (Channel)commonDao.getObjectByUniqueCode(Constant.channel, Constant.numberCode, taobaoOrderTbt.getChannel());
            if (channel == null) {
                returnMap.put(0, ConstantMsg.channel_not_exist_error);
                return returnMap;
            }
            
            //0:库存不足, 1:无超卖, 2:有超卖 
            if (null != resultMap.get(1) || null != resultMap.get(0)) {
                
                OrderContent orderContent = new OrderContent();
                orderContent.setIsHang(0);
                Stock stock = null;
                
                if (null != resultMap.get(0)) {
                    stock = (Stock)commonDao.getObjectByUniqueCode(Constant.stock, Constant.numberCode ,resultMap.get(1));
                    if (stock == null) {
                        returnMap.put(0, ConstantMsg.stock_not_exist_error);
                        return returnMap;
                    }
                    if (stock.getType() == 1) {
                        orderContent.setIsHang(1);
                    }else {
                        TaobaoClient client = new DefaultTaobaoClient(Constant.taobaoUrl, channel.getAppKey(), channel.getAppSecret());
                        AlitripTravelTradeCloseRequest req = new AlitripTravelTradeCloseRequest();
                        req.setTid(Long.valueOf(taobaoOrderTbt.getTid()));
                        req.setCloseReason("缺货");
                        AlitripTravelTradeCloseResponse rsp;
                        try {
                            rsp = client.execute(req, channel.getSessionKey());
                        }
                        catch (ApiException e) {
                            throw new MyExceptionForXyz(e.getMessage());
                        }
                        String jsonStr = rsp.getBody();
                        @SuppressWarnings("unchecked")
                        Map<String, Object> jsonMap = JSON.toObject(jsonStr, HashMap.class);
                        
                        @SuppressWarnings("unchecked")
                        Map<String, Object> alitripTravelTradeCloseResponseMap = (Map<String, Object>)jsonMap.get("alitrip_travel_trade_close_response");
                        
                        @SuppressWarnings("unchecked")
                        List<Map<String, Object>> tradeCloseList = (List<Map<String, Object>>)alitripTravelTradeCloseResponseMap.get("trade_close");
                        
                        for (Map<String, Object> trade : tradeCloseList) {
                            String tradeTid = (String)trade.get("tid");
                            String modified = (String)trade.get("modified");
                            
                            if (taobaoOrderTbt.getTid().equals(tradeTid)) {
                                taobaoOrderTbt.setModified(DateUtil.stringToDate(modified));
                                commonDao.update(taobaoOrderTbt);
                            }
                        }
                        
                        break;
                    }
                }
                
                stock = (Stock)commonDao.getObjectByUniqueCode(Constant.stock, Constant.numberCode ,resultMap.get(1));
                if (stock == null) {
                    returnMap.put(0, ConstantMsg.stock_not_exist_error);
                    return returnMap;
                }
                
                Cabin cabin = (Cabin) commonDao.getObjectByUniqueCode(Constant.cabin, Constant.numberCode, tkview.getCabin());
                if (cabin == null) {
                    returnMap.put(0, ConstantMsg.cabin_null);
                    return returnMap;
                }
                
                Shipment shipment = (Shipment)commonDao.getObjectByUniqueCode(Constant.shipment, Constant.numberCode, tkview.getShipment());
                if (shipment == null) {
                    returnMap.put(0, ConstantMsg.shipment_null);
                    return returnMap;
                }
                
                Cruise cruise = (Cruise)commonDao.getObjectByUniqueCode(Constant.cruise, Constant.numberCode, tkview.getCruise());
                if (cruise == null) {
                    returnMap.put(0, ConstantMsg.cruise_null);
                    return returnMap;
                }
                
                Provider provider = (Provider)commonDao.getObjectByUniqueCode(Constant.provider, Constant.numberCode, stock.getProvider());
                if (provider == null) {
                    returnMap.put(0, ConstantMsg.provider_not_exist_error);
                    return returnMap;
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
                 
                 if (taobaoOrderTbo.getRefundStatus().equals(Constant.taobao_order_refund_status_wait_buyer_return_goods)) {
                    isRefund = 1;
                }else if (taobaoOrderTbo.getRefundStatus().equals(Constant.taobao_order_refund_status_wait_seller_confirm_goods)) {
                    isRefund = 1;
                }else if (taobaoOrderTbo.getRefundStatus().equals(Constant.taobao_order_refund_status_closed)) {
                    isRefund = -1;
                }else if (taobaoOrderTbo.getRefundStatus().equals(Constant.taobao_order_refund_status_success)) {
                    isRefund = 1;
                    isReturnGoods = 1;
                }
                
                orderContent.setAddDate(date);
                orderContent.setAddress(taobaoOrderTbt.getReceiverAddress());
                orderContent.setAlterDate(date);
                orderContent.setBuyer(taobaoOrderTbt.getBuyNick());
                orderContent.setCabin(cabin.getNumberCode());
                orderContent.setCabinNameCn(cabin.getNameCn());
                orderContent.setVolume(cabin.getVolume());
                orderContent.setChannel(channel.getNumberCode());
                orderContent.setChannelNameCn(channel.getNameCn());
                orderContent.setClientCode(StringUtil.get_new_clientCode("TB"));
                orderContent.setCost(stock.getCost().intValue());
                orderContent.setCount(taobaoOrderTbo.getNum());
                orderContent.setCruise(cruise.getNumberCode());
                orderContent.setCruiseNameCn(cruise.getNameCn());
                orderContent.setClientPrice(taobaoOrderTbo.getPrice().intValue()*100);
                orderContent.setEarnest(0);
                orderContent.setEmail(taobaoOrderTbo.getTravelContactMail());
                orderContent.setFlag(Constant.order_flag_gray);
                orderContent.setIsDelete(0);
                orderContent.setIsOver(isOver);
                orderContent.setIsPay(isPay);
                if (isRefund == 1) {
                    orderContent.setIsRefoundMoney(1);
                    orderContent.setRefoundAmount(taobaoOrderTbo.getPayment().intValue());
                    if (isReturnGoods == 1) {
                        Boolean isTrue = productUtil.refundStock(stock.getNumberCode(), taobaoOrderTbo.getNum());
                        if (isTrue) {
                            orderContent.setIsRefoundStock(1);
                            orderContent.setRefoundStock(taobaoOrderTbo.getNum());
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
                orderContent.setLinkMan(taobaoOrderTbo.getTravelContactName());
                orderContent.setLinkPhone(taobaoOrderTbo.getTravelContactMobile());
                orderContent.setOid(taobaoOrderTbo.getOid());
                orderContent.setOperaterAdd(Constant.taobao_oper_name_cn);
                orderContent.setOperaterAlter(Constant.taobao_oper_name_cn);
                orderContent.setOrderNum(orderNum);
                orderContent.setOrderPrice(taobaoOrderTbt.getReceivedPayment().intValue());
                orderContent.setOverDate(taobaoOrderTbt.getEnd_time());
                orderContent.setPayAmount(taobaoOrderTbo.getPayment());
                if (isPay == 1) {
                    orderContent.setPayDate(taobaoOrderTbt.getPayTime());
                }
                orderContent.setPersonInfo("");
                orderContent.setProduct(taobaoOrderTbo.getNumIid());
                orderContent.setProductNameCn(taobaoOrderTbo.getTitle());
                orderContent.setProductPrice(taobaoOrderTbo.getPrice().intValue());
                orderContent.setProvider(provider.getNumberCode());
                orderContent.setProviderNameCn(provider.getNameCn());
                
                orderContent.setRemark(taobaoOrderTbt.getRemark());
                orderContent.setShipment(shipment.getNumberCode());
                orderContent.setShipmentMark(shipment.getMark());
                orderContent.setShipmentTravelDate(shipment.getTravelDate());
                orderContent.setSource("TB");
                orderContent.setStock(stock.getNumberCode());
                orderContent.setTid(taobaoOrderTbt.getTid());
                orderContent.setTkview(tkview.getNumberCode());
                orderContent.setTkviewNameCn(tkview.getNameCn());
                
                commonDao.save(orderContent);
                
                LogWork logWork = new LogWork();
                logWork.setAddDate(new Date());
                logWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
                logWork.setValue(taobaoOrderTbt.getTid());
                logWork.setTableName("order_taobao");
                logWork.setRemark("淘宝池订单添加到订单【"+orderNum+"】");
                logWork.setUsername(Constant.taobao_oper_name_cn);
                commonDao.save(logWork);
            }
        }
        
        commonDao.update(taobaoOrderTbt);
        
        returnMap.put(1, "");
        return returnMap;
    }
    
}
