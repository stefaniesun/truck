package xyz.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.internal.util.StringUtils;
import com.taobao.api.request.AlitripTravelItemBaseAddRequest;
import com.taobao.api.request.AlitripTravelItemBaseAddRequest.CruiseItemExt;
import com.taobao.api.request.AlitripTravelItemBaseAddRequest.PontusTravelBookingRuleInfo;
import com.taobao.api.request.AlitripTravelItemBaseAddRequest.PontusTravelItemBaseInfo;
import com.taobao.api.request.AlitripTravelItemBaseAddRequest.PontusTravelItemSaleInfo;
import com.taobao.api.request.AlitripTravelTradeFullinfoGetRequest;
import com.taobao.api.response.AlitripTravelItemBaseAddResponse;
import com.taobao.api.response.AlitripTravelTradeFullinfoGetResponse;

import xyz.dao.CommonDao;
import xyz.exception.MyExceptionForXyz;
import xyz.filter.JSON;
import xyz.filter.ReturnUtil;
import xyz.work.core.model.PlanWork;
import xyz.work.resources.model.Channel;
import xyz.work.taobao.model.TaobaoOrderTbo;
import xyz.work.taobao.model.TaobaoOrderTbt;
import xyz.work.taobao.model.TaobaoBaseInfo;
import xyz.work.taobao.model.TaobaoBookingRule;
import xyz.work.taobao.model.TaobaoCruiseItemExt;
import xyz.work.taobao.model.TaobaoSaleInfo;
import xyz.work.taobao.model.TaobaoSkuInfoDetail;
import xyz.work.taobao.model.TaobaoTravelItem;
import xyz.util.DateUtil;
import xyz.util.StringTool;

@Component
public class TaobaoUtil{
    
    @Resource
    CommonDao commonDao;

    public Map<String, Object> publishTaobaoItemOper(TaobaoBaseInfo taobaoBaseInfoObj,TaobaoTravelItem taobaoTravelItemObj) {
        
        Channel channelObj = (Channel)commonDao.getObjectByUniqueCode("Channel", "numberCode", taobaoTravelItemObj.getChannel());
        if (channelObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.channel_not_exist_error);
        }
        
        TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", channelObj.getAppKey(), channelObj.getAppSecret());
        AlitripTravelItemBaseAddRequest req = new AlitripTravelItemBaseAddRequest();
        
        //宝贝基础信息
        PontusTravelItemBaseInfo PontusTravelItemBaseInfoObj = new PontusTravelItemBaseInfo();//baseInfo
        PontusTravelItemBaseInfoObj.setTitle(taobaoBaseInfoObj.getTitle());
        List<String> subTitleslist = new ArrayList<String>();
        String[] subTitles = taobaoBaseInfoObj.getSubTitles().split(",");
        for (int i = 0; i < subTitles.length; i++ ) {
            subTitleslist.add(subTitles[i]);
        }
        PontusTravelItemBaseInfoObj.setSubTitles(subTitleslist);
        List<String> picUrlsList = new ArrayList<String>();
        String[] picUrls = null;
        if (StringTool.isNotNull(taobaoBaseInfoObj.getPicUrls())) {
            picUrls = taobaoBaseInfoObj.getPicUrls().split(",");
            for (int i = 0; i < picUrls.length; i++ ) {
                picUrlsList.add(picUrls[i]);
            }
        }
        PontusTravelItemBaseInfoObj.setItemType(Long.valueOf(taobaoBaseInfoObj.getItemType()));
        PontusTravelItemBaseInfoObj.setPicUrls(picUrlsList);
        PontusTravelItemBaseInfoObj.setFromLocations(taobaoBaseInfoObj.getFromLocations());
        PontusTravelItemBaseInfoObj.setToLocations(taobaoBaseInfoObj.getToLocations());
        PontusTravelItemBaseInfoObj.setTripMaxDays(Long.valueOf(taobaoBaseInfoObj.getTripMaxDays()));
        PontusTravelItemBaseInfoObj.setAccomNights(Long.valueOf(taobaoBaseInfoObj.getAccomNights()));
        PontusTravelItemBaseInfoObj.setProv(taobaoBaseInfoObj.getProv());
        PontusTravelItemBaseInfoObj.setCity(taobaoBaseInfoObj.getCity());
        PontusTravelItemBaseInfoObj.setDesc(taobaoBaseInfoObj.getDesc());
        //PontusTravelItemBaseInfoObj.setWapDesc("<wapDesc><shortDesc>标题</shortDesc> <txt>描述</txt> <img>图片路径</img></wapDesc>");
        PontusTravelItemBaseInfoObj.setOutId(taobaoBaseInfoObj.getNumberCode());
        req.setBaseInfo(PontusTravelItemBaseInfoObj);
        
        String bookingRuleHql = "from TaobaoBookingRule where taobaoTravelItem = '"+taobaoBaseInfoObj.getTaobaoTravelItem()+"'";
        @SuppressWarnings("unchecked")
        List<TaobaoBookingRule> taobaoBookingRulesList = commonDao.queryByHql(bookingRuleHql);
        
        List<PontusTravelBookingRuleInfo> PontusTravelBookingRuleInfoList = new ArrayList<PontusTravelBookingRuleInfo>();
        if (taobaoBookingRulesList.size() <= 0) {
            return ReturnUtil.returnMap(0, "商品规则不存在!");
        }
        
        for (TaobaoBookingRule rule : taobaoBookingRulesList) {
            PontusTravelBookingRuleInfo PontusTravelBookingRuleInfoObj = new PontusTravelBookingRuleInfo();
            PontusTravelBookingRuleInfoObj.setRuleType(rule.getRuleType());
            PontusTravelBookingRuleInfoObj.setRuleDesc(rule.getRuleDesc());
            PontusTravelBookingRuleInfoList.add(PontusTravelBookingRuleInfoObj);
        }
        req.setBookingRules(PontusTravelBookingRuleInfoList);
        
        TaobaoSaleInfo taobaoSaleInfoObj = (TaobaoSaleInfo)commonDao.getObjectByUniqueCode("TaobaoSaleInfo", "taobaoTravelItem", taobaoBaseInfoObj.getTaobaoTravelItem());
        if (taobaoSaleInfoObj != null) {
            PontusTravelItemSaleInfo PontusTravelItemSaleInfoObj = new PontusTravelItemSaleInfo();//saleInfo
            PontusTravelItemSaleInfoObj.setSubStock(Long.valueOf(taobaoSaleInfoObj.getSubStock()));
            PontusTravelItemSaleInfoObj.setSaleType(Long.valueOf(taobaoSaleInfoObj.getSaleType()));
            if (PontusTravelItemSaleInfoObj.getSaleType() == 1) {
                if (taobaoSaleInfoObj.getStartComboDate() == null) {
                    return ReturnUtil.returnMap(0, "开始日期不能为空!");
                }
                if (taobaoSaleInfoObj.getEndComboDate() == null) {
                    return ReturnUtil.returnMap(0, "结束日期不能为空!");
                }
                PontusTravelItemSaleInfoObj.setBcStartDate(StringUtils.parseDateTime(DateUtil.dateToString(taobaoSaleInfoObj.getStartComboDate())));
                PontusTravelItemSaleInfoObj.setStartComboDate(StringUtils.parseDateTime(DateUtil.dateToString(taobaoSaleInfoObj.getStartComboDate())));
                PontusTravelItemSaleInfoObj.setEndComboDate(StringUtils.parseDateTime(DateUtil.dateToString(taobaoSaleInfoObj.getEndComboDate())));
            }else {
                if (taobaoSaleInfoObj.getStartComboDate() != null) {
                    PontusTravelItemSaleInfoObj.setBcStartDate(StringUtils.parseDateTime(DateUtil.dateToString(taobaoSaleInfoObj.getStartComboDate())));
                    PontusTravelItemSaleInfoObj.setStartComboDate(StringUtils.parseDateTime(DateUtil.dateToString(taobaoSaleInfoObj.getStartComboDate())));
                }
                if (taobaoSaleInfoObj.getEndComboDate() != null) {
                    PontusTravelItemSaleInfoObj.setEndComboDate(StringUtils.parseDateTime(DateUtil.dateToString(taobaoSaleInfoObj.getEndComboDate())));
                }
            }
            PontusTravelItemSaleInfoObj.setDuration(Long.valueOf(taobaoSaleInfoObj.getDuration()));
            PontusTravelItemSaleInfoObj.setHasInvoice(Boolean.valueOf(taobaoSaleInfoObj.getHasInvoice()));
            PontusTravelItemSaleInfoObj.setHasDiscount(Boolean.valueOf(taobaoSaleInfoObj.getHasDiscount()));
            PontusTravelItemSaleInfoObj.setHasShowcase(Boolean.valueOf(taobaoSaleInfoObj.getHasShowcase()));
            
            List<String> sellerCidsList = new ArrayList<String>();
            String[] sellerCdis = taobaoSaleInfoObj.getSellerCids().split(",");
            for (int i = 0; i < sellerCdis.length; i++ ) {
                sellerCidsList.add(sellerCdis[i]);
            }
            PontusTravelItemSaleInfoObj.setSellerCids(sellerCidsList);
            PontusTravelItemSaleInfoObj.setMerchant(taobaoSaleInfoObj.getMerchant());
            PontusTravelItemSaleInfoObj.setNetworkId(taobaoSaleInfoObj.getNetworkId());
            PontusTravelItemSaleInfoObj.setSupportOnsaleAutoRefund(Boolean.valueOf(taobaoSaleInfoObj.getSupportOnsaleAutoRefund()));
            req.setSalesInfo(PontusTravelItemSaleInfoObj);
        }
        
        TaobaoCruiseItemExt taobaoCruiseItemExtObj = (TaobaoCruiseItemExt)commonDao.getObjectByUniqueCode("TaobaoCruiseItemExt", "taobaoTravelItem", taobaoBaseInfoObj.getTaobaoTravelItem());
        if (taobaoCruiseItemExtObj != null) {
            CruiseItemExt CruiseItemExtObj = new CruiseItemExt();//邮轮
            CruiseItemExtObj.setShipDown(taobaoCruiseItemExtObj.getShipDown());
            CruiseItemExtObj.setCruiseLine(taobaoCruiseItemExtObj.getCruiseLine());
            CruiseItemExtObj.setCruiseCompany(taobaoCruiseItemExtObj.getCruiseCompany());
            CruiseItemExtObj.setShipName(taobaoCruiseItemExtObj.getShipName());
            CruiseItemExtObj.setShipUp(taobaoCruiseItemExtObj.getShipUp());
            
            String[] shipFeesIncludes = taobaoCruiseItemExtObj.getShipFeeInclude().split(",");
            List<String> shipFeesIncludesList = new ArrayList<String>();
            for (String s : shipFeesIncludes) {
                if (s.trim().equals("船票")) {
                    shipFeesIncludesList.add("1");
                }else if (s.trim().equals("港务费、邮轮税费")) {
                    shipFeesIncludesList.add("2");
                }else if (s.trim().equals("岸上观光费")) {
                    shipFeesIncludesList.add("3");
                }else if (s.trim().equals("签证费用")) {
                    shipFeesIncludesList.add("4");
                }else if (s.trim().equals("小费")) {
                    shipFeesIncludesList.add("5");
                }else if (s.trim().equals("领队费")) {
                    shipFeesIncludesList.add("6");
                }else if (s.trim().equals("其他费用")) {
                    shipFeesIncludesList.add("7");
                }
            }
            
            CruiseItemExtObj.setShipFeeInclude(StringTool.listToString(shipFeesIncludesList, ","));
            
            req.setCruiseItemExt(CruiseItemExtObj);
        }
        AlitripTravelItemBaseAddResponse rsp = null;
        try {
            rsp = client.execute(req, channelObj.getSessionKey());
        }
        catch (ApiException e) {
            throw new MyExceptionForXyz(e.getMessage());
        }
        
        @SuppressWarnings("unchecked")
        HashMap<String, Object> alitripTravelItemBaseAddResponseMap = JSON.toObject(rsp.getBody(), HashMap.class);
        
        @SuppressWarnings("unchecked")
        HashMap<String, Object> travelItemMap = (HashMap<String, Object>)alitripTravelItemBaseAddResponseMap.get("alitrip_travel_item_base_add_response");
       
        if (travelItemMap == null) {
            @SuppressWarnings("unchecked")
            Map<String, Object> errorResponseMap = (Map<String, Object>)alitripTravelItemBaseAddResponseMap.get("error_response");
            return ReturnUtil.returnMap(0, "接口错误,错误码【"+errorResponseMap.get("code").toString()+"】错误信息【"+errorResponseMap.get("sub_msg").toString()+"】");
        }
        
        @SuppressWarnings("unchecked")
        HashMap<String, Object> itemMap = (HashMap<String, Object>)travelItemMap.get("travel_item");
    
        String itemId = itemMap.get("item_id")==null?"":itemMap.get("item_id").toString();
        String modified = itemMap.get("modified")==null?"":itemMap.get("modified").toString();
        String created = itemMap.get("created")==null?"":itemMap.get("created").toString();
        taobaoTravelItemObj.setItemId(itemId);
        if (StringTool.isNotNull(modified)) {
            taobaoTravelItemObj.setModified(DateUtil.stringToDate(modified));
        }
        taobaoTravelItemObj.setCreated(DateUtil.stringToDate(created));
        taobaoBaseInfoObj.setItemUrl("https://items.alitrip.com/item.htm?id="+itemId.toString());
        
        return ReturnUtil.returnMap(1, taobaoTravelItemObj);
    }
    
    /**
     * 
     *
     * @return Map<String,Object>
     * @exception   ：〈描述可能的异常〉
    
     * @author      叶尼玛
     * @date        ：2016-11-24下午3:07:39
     */
    public Map<String, Object> getTaobaoOrderDetails(PlanWork planWork){
        
        Channel channelObj = (Channel)commonDao.getObjectByUniqueCode("Channel", "numberCode", planWork.getRemark());
        
        if (channelObj == null) {
            Map<String, Object> returnMap = new HashMap<String, Object>();
            
            returnMap.put("status", 0);
            returnMap.put("msg", "渠道不存在!");
            
            return returnMap;
        }
        
        Date date = new Date();
        
        String appKey = channelObj.getAppKey();
        String secret = channelObj.getAppSecret();
        String sessionKey = channelObj.getSessionKey();
        
        if (!StringTool.isNotNull(appKey) || !StringTool.isNotNull(sessionKey) || !StringTool.isNotNull(secret)) {
            Map<String, Object> returnMap = new HashMap<String, Object>();
            
            returnMap.put("status", 0);
            returnMap.put("msg", "接口参数错误!");
            
            return returnMap;
        }
        
        TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", appKey, secret);
        AlitripTravelTradeFullinfoGetRequest req = new AlitripTravelTradeFullinfoGetRequest();
        String rspStr = "";
        req.setTid(Long.valueOf(planWork.getContent()));
        AlitripTravelTradeFullinfoGetResponse rsp = null;
        try {
            rsp = client.execute(req, sessionKey);
        }
        catch (ApiException e) {
            e.printStackTrace();
        }
        rspStr = rsp.getBody();
        
        @SuppressWarnings("unchecked")
        Map<String, Object> jsonMap = JSON.toObject(rspStr, HashMap.class);
       
        @SuppressWarnings("unchecked")
        Map<String, Object> alitripTravelTradeFullinfoGetResponseMap = (Map<String, Object>)jsonMap.get("alitrip_travel_trade_fullinfo_get_response"); 
        
        if (alitripTravelTradeFullinfoGetResponseMap == null) {
            @SuppressWarnings("unchecked")
            Map<String, Object> errorResponse =  (Map<String, Object>)jsonMap.get("error_response");
            
            Map<String, Object> returnMap = new HashMap<String, Object>();
            
            returnMap.put("status", 0);
            returnMap.put("msg", (String)errorResponse.get("sub_msg"));
            
            return returnMap;
            
        }
        
        @SuppressWarnings("unchecked")
        Map<String, Object> tradeFullinfoList = (Map<String, Object>)alitripTravelTradeFullinfoGetResponseMap.get("trade_fullinfo");
        
        String modified = (String)tradeFullinfoList.get("modified");
        BigDecimal received_payment = new BigDecimal((String)tradeFullinfoList.get("received_payment"));
        int seller_flag = (Integer)tradeFullinfoList.get("seller_flag");
        String seller_memo = (String)tradeFullinfoList.get("seller_memo");
        String seller_nick = (String)tradeFullinfoList.get("seller_nick");
        String pay_time = (String)tradeFullinfoList.get("pay_time");
        String created = (String)tradeFullinfoList.get("created");
        String type = (String)tradeFullinfoList.get("type");
        String tid = String.valueOf((Long)tradeFullinfoList.get("tid"));
        String status = (String)tradeFullinfoList.get("status");
        String buyer_nick = (String)tradeFullinfoList.get("buyer_nick");
        String buyer_message = (String)tradeFullinfoList.get("buyer_message");
        
        TaobaoOrderTbt orderTbtObj = new TaobaoOrderTbt();
        
        orderTbtObj.setOrderType(0);
        orderTbtObj.setAddDate(date);
        orderTbtObj.setAlterDate(DateUtil.stringToDate(modified));
        orderTbtObj.setReceivedPayment(received_payment);
        //orderTbtObj.setReceiverMobile(receiver_mobile);
        //orderTbtObj.setReceiverName(receiver_name);
        orderTbtObj.setSellerFlag(Integer.valueOf(seller_flag));
        orderTbtObj.setRemark(seller_memo);
        orderTbtObj.setBuyerMessage(buyer_message);
        orderTbtObj.setSellerNick(seller_nick);
        orderTbtObj.setModified(DateUtil.stringToDate(modified));
        if (StringTool.isNotNull(pay_time)) {
            orderTbtObj.setPayTime(DateUtil.stringToDate(pay_time));
        }
        orderTbtObj.setCreated(DateUtil.stringToDate(created));
        orderTbtObj.setType(type);
        orderTbtObj.setTid(tid);
        orderTbtObj.setChannel(channelObj.getNumberCode());
        orderTbtObj.setChannelNameCn(channelObj.getNameCn());
        orderTbtObj.setStatus(status);
        orderTbtObj.setBuyNick(buyer_nick);
        
        @SuppressWarnings("unchecked")
        Map<String ,Object> orderMap = (Map<String ,Object>)tradeFullinfoList.get("orders");
        @SuppressWarnings("unchecked")
        List<Map<String ,Object>> orders = (List<Map<String, Object>>)orderMap.get("order");
        
        List<TaobaoOrderTbo> orderTboList = new ArrayList<TaobaoOrderTbo>();
        for (Map<String, Object> o : orders) {
            
            @SuppressWarnings("unchecked")
            Map<String, Object> extInfoMap = (Map<String, Object>)o.get("ext_info");
            
            int adult_guest_num = 0;
            int child_guest_num = 0;
            String travel_contact_mail = "";
            String travel_contact_mobile = "";
            String travel_contact_name = "";
            String trip_start_date  = "";
            
            if (extInfoMap != null) {
                if (extInfoMap.get("adult_guest_num") != null) {
                    adult_guest_num = (Integer)extInfoMap.get("adult_guest_num");
                }
                if (extInfoMap.get("child_guest_num") != null) {
                    child_guest_num = (Integer)extInfoMap.get("child_guest_num");
                }
                travel_contact_mail = (String)extInfoMap.get("travel_contact_mail");
                travel_contact_mobile = (String)extInfoMap.get("travel_contact_mobile");
                travel_contact_name = (String)extInfoMap.get("travel_contact_name");
                trip_start_date = (String)extInfoMap.get("trip_start_date");
            }
            
            String refund_status = (String)o.get("refund_status");
            String o_total_fee = (String)o.get("total_fee");
            String o_pic_path = (String)o.get("pic_path"); 
            String o_num_iid = String.valueOf((Long)o.get("num_iid"));
            String o_payment = (String)o.get("payment");
            String o_status = (String)o.get("status");
            String o_price  =(String)o.get("price");
            String o_title = (String)o.get("title");
            int o_num = (Integer)o.get("num");
            String oid = String.valueOf((Long)o.get("oid"));
            String sku_id = (String)o.get("sku_id");
            String sku_properties_name = (String)o.get("sku_properties_name");
            String outer_sku_id = (String)o.get("outer_sku_id");
            String outer_price_id  = "";
            
            String skuDetailHql = "from TaobaoSkuInfoDetail where taobaoSkuInfo = '"+outer_sku_id+"'";
            
            @SuppressWarnings("unchecked")
            List<TaobaoSkuInfoDetail> taobaoSkuInfoDetailList = commonDao.queryByHql(skuDetailHql);
            
            for (TaobaoSkuInfoDetail taobaoSkuInfoDetail : taobaoSkuInfoDetailList) {
                Date detailDate = taobaoSkuInfoDetail.getDate();
                Date orderDate = DateUtil.stringToDate(trip_start_date);
                
                if (detailDate.compareTo(orderDate) == 0) {
                    outer_price_id = taobaoSkuInfoDetail.getOutId();
                    taobaoSkuInfoDetail.setStock(taobaoSkuInfoDetail.getStock() - o_num);
                    break;
                }
            }
            
            TaobaoOrderTbo orderTboObj = new TaobaoOrderTbo();
            
            orderTboObj.setAddDate(date);
            orderTboObj.setAlterDate(date);
            orderTboObj.setRefundStatus(refund_status);
            orderTboObj.setTotalFee(new BigDecimal(o_total_fee));
            orderTboObj.setPicPath(o_pic_path);
            orderTboObj.setNumIid(o_num_iid);
            orderTboObj.setPayment(new BigDecimal(o_payment));
            orderTboObj.setStatus(o_status);
            orderTboObj.setPrice(new BigDecimal(o_price));
            orderTboObj.setTitle(o_title);
            orderTboObj.setNum(Integer.valueOf(o_num));
            orderTboObj.setOid(oid);
            orderTboObj.setSkuId(sku_id);
            orderTboObj.setTid(tid);
            orderTboObj.setSkuPropertiesName(sku_properties_name);
            orderTboObj.setBuyNick(buyer_nick);
            orderTboObj.setSellerNick(seller_nick);
            orderTboObj.setCreated(DateUtil.stringToDate(created));
            orderTboObj.setAdultGuestNum(Integer.valueOf(adult_guest_num));
            orderTboObj.setChildGuestNum(Integer.valueOf(child_guest_num));
            orderTboObj.setTravelContactMail(travel_contact_mail);
            orderTboObj.setTravelContactMobile(travel_contact_mobile);
            orderTboObj.setTravelContactName(travel_contact_name);
            if (StringTool.isNotNull(trip_start_date)) {
                orderTboObj.setTripStartDate(DateUtil.stringToDate(trip_start_date));
            }
            orderTboObj.setOutSkuId(outer_price_id);
            
            orderTboList.add(orderTboObj);
        }
        
        Map<String, Object> tempMap = new HashMap<String, Object>();
        
        tempMap.put("orderTbtObj", orderTbtObj);
        tempMap.put("orderTboList", orderTboList);
            
        return ReturnUtil.returnMap(1, tempMap);
    }
    
}
