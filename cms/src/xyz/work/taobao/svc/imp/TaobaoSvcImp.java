package xyz.work.taobao.svc.imp;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.exception.MyExceptionForXyz;
import xyz.filter.JSON;
import xyz.filter.MyRequestUtil;
import xyz.filter.ReturnUtil;
import xyz.util.ClazzUtil;
import xyz.util.Constant;
import xyz.util.ConstantMsg;
import xyz.util.DateUtil;
import xyz.util.PossessorUtil;
import xyz.util.StringTool;
import xyz.util.StringUtil;
import xyz.util.TaobaoUtil;
import xyz.util.UUIDUtil;
import xyz.work.base.model.Cabin;
import xyz.work.base.model.Company;
import xyz.work.base.model.Cruise;
import xyz.work.base.model.Shipment;
import xyz.work.excel.model.PriceStrategy;
import xyz.work.resources.model.Channel;
import xyz.work.resources.model.Provider;
import xyz.work.resources.model.Stock;
import xyz.work.resources.model.Tkview;
import xyz.work.resources.model.TkviewType;
import xyz.work.taobao.model.Sku_TkviewType;
import xyz.work.taobao.model.TaobaoBaseInfo;
import xyz.work.taobao.model.TaobaoBookingRule;
import xyz.work.taobao.model.TaobaoCruiseItemExt;
import xyz.work.taobao.model.TaobaoLogWork;
import xyz.work.taobao.model.TaobaoSaleInfo;
import xyz.work.taobao.model.TaobaoSkuInfo;
import xyz.work.taobao.model.TaobaoSkuInfoDetail;
import xyz.work.taobao.model.TaobaoTravelItem;
import xyz.work.taobao.svc.TaobaoSvc;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlitripTravelItemBaseModifyRequest;
import com.taobao.api.request.AlitripTravelItemBaseModifyRequest.CruiseItemExt;
import com.taobao.api.request.AlitripTravelItemBaseModifyRequest.PontusTravelBookingRuleInfo;
import com.taobao.api.request.AlitripTravelItemBaseModifyRequest.PontusTravelItemBaseInfo;
import com.taobao.api.request.AlitripTravelItemShelveRequest;
import com.taobao.api.request.AlitripTravelItemSingleQueryRequest;
import com.taobao.api.request.AlitripTravelItemSkuOverrideRequest;
import com.taobao.api.request.AlitripTravelItemSkuOverrideRequest.PontusTravelItemSkuInfo;
import com.taobao.api.request.AlitripTravelItemSkuOverrideRequest.PontusTravelPrices;
import com.taobao.api.request.AlitripTravelItemSkuPriceModifyRequest;
import com.taobao.api.request.ItemsInventoryGetRequest;
import com.taobao.api.request.ItemsOnsaleGetRequest;
import com.taobao.api.response.AlitripTravelItemBaseModifyResponse;
import com.taobao.api.response.AlitripTravelItemShelveResponse;
import com.taobao.api.response.AlitripTravelItemSingleQueryResponse;
import com.taobao.api.response.AlitripTravelItemSkuOverrideResponse;
import com.taobao.api.response.AlitripTravelItemSkuPriceModifyResponse;
import com.taobao.api.response.ItemsInventoryGetResponse;
import com.taobao.api.response.ItemsOnsaleGetResponse;

@Service
public class TaobaoSvcImp implements TaobaoSvc {
    @Autowired
    CommonDao commonDao;
    
    @Autowired
    TaobaoUtil taobaoUtil;  //淘宝工具
    
    @Autowired
    PossessorUtil possessorUtil; //机构工具
    
    int maxCount = 8; //美团最多SKU套餐个数
    
    /**
     * 宝贝查询
     */
    @Override
    public Map<String, Object> queryTaobaoItem(int offset , int pagesize ,String cruise,
                                               String itemId , String itemTitle ,
                                               String channel , String itemStatus ,
                                               String outId , String itemType ,
                                               String sort , String order) {

        String possessor = MyRequestUtil.getPossessor();
        
        StringBuffer baseInfoHql  = new StringBuffer();
        baseInfoHql.append("from taobao_base_info t left join taobao_travel_item tt ");
        baseInfoHql.append("on t.taobao_travel_item = tt.number_code where 1=1 ");
        if (StringTool.isNotNull(possessor)) {
            List<String> relates = possessorUtil.getDecideMap(Constant.relate_type_channel).keySet().contains(Constant.relate_type_channel)?possessorUtil.getDecideMap(Constant.relate_type_channel).get(Constant.relate_type_channel):new ArrayList<String>();
            baseInfoHql.append(" and tt.channel in ("+StringTool.listToSqlString(relates)+")");
        }
        
        if(StringTool.isNotNull(cruise) && !"noValue".equals(cruise)){
            baseInfoHql.append(" AND t.cruise = '"+ cruise +"' ");
        }
        
        if (StringTool.isNotNull(itemTitle)) {
            baseInfoHql.append(" and t.title like '%"+itemTitle+"%'");
        }
        
        if (StringTool.isNotNull(channel)) {
            baseInfoHql.append(" and tt.channel = '"+channel+"'");
        }
        
        if (StringTool.isNotNull(outId)) {
            baseInfoHql.append(" and t.number_code = '"+outId+"'");
        }
        
        if (StringTool.isNotNull(itemType)) {
            baseInfoHql.append(" and t.item_type = '"+itemType+"'");
        }
        
        if (StringTool.isNotNull(itemStatus)) {
            baseInfoHql.append(" and tt.item_status = '"+itemStatus+"'");
        }
        
        if (StringTool.isNotNull(itemId)) {
            baseInfoHql.append(" and tt.item_id = '"+itemId+"'");
        }
        
        if (sort != null && !"".equals(sort) && order != null && !"".equals(order)) {
            if (",alterDate,addDate,".contains(","+ sort+ ",") && ",asc,desc,".contains("," + order + ",")) {
                if (sort.equals("alterDate")) {
                    sort = "alter_date";
                }else if (sort.equals("addDate")) {
                    sort = "add_date";
                }
                baseInfoHql.append(" order by t." + sort + " " + order);
            }
        }else {
            baseInfoHql.append(" order by t.alter_date desc");
        }

        Query countQuery = commonDao.getSqlQuery("SELECT count(*) " + baseInfoHql.toString());
        Number countNum = (Number)countQuery.uniqueResult();
        int count = countNum == null ? 0 : countNum.intValue();

        StringBuffer selectSb = new StringBuffer();
        selectSb.append("tt.channel AS channel,");
        selectSb.append("tt.channel_name_cn AS channelNameCn,");
        selectSb.append("tt.item_id AS itemId,");
        selectSb.append("t.title AS title,");
        selectSb.append("t.item_type AS itemType,");
        selectSb.append("t.to_locations AS toLocations,");
        selectSb.append("t.from_locations AS fromLocations,");
        selectSb.append("t.trip_max_days AS tripMaxDays,");
        selectSb.append("tt.item_status AS itemStatus,");
        selectSb.append("t.pic_urls AS picUrls,");
        selectSb.append("t.taobao_desc AS 'desc',");
        selectSb.append("t.cruise AS 'cruise',");
        selectSb.append("t.number_code AS numberCode,");
        selectSb.append("t.alter_date AS alterDate,");
        selectSb.append("t.add_date AS addDate,");
        selectSb.append("t.remark AS remark,");
        selectSb.append("t.taobao_travel_item AS taobaoTravelItem,");
        selectSb.append("t.item_url AS itemUrl");
        
        SQLQuery query = commonDao.getSqlQuery("SELECT "+selectSb.toString()+" "+baseInfoHql.toString());
        query.setMaxResults(pagesize);
        query.setFirstResult(offset);
        query.addScalar("channel").addScalar("channelNameCn").addScalar("itemId").addScalar("title").addScalar("itemType")
        .addScalar("toLocations").addScalar("fromLocations").addScalar("tripMaxDays").addScalar("itemStatus")
        .addScalar("picUrls").addScalar("desc").addScalar("numberCode").addScalar("alterDate").addScalar("addDate")
        .addScalar("remark").addScalar("cruise").addScalar("taobaoTravelItem")
        .addScalar("itemUrl").setResultTransformer(Transformers.aliasToBean(TaobaoBaseInfo.class));
        
        @SuppressWarnings("unchecked")
        List<TaobaoBaseInfo> baseInfoList = query.list();

        List<String> cruiseStrList = new ArrayList<>();
        for (TaobaoBaseInfo taobaoBaseInfo : baseInfoList) {
            cruiseStrList.add(taobaoBaseInfo.getCruise());
        }
        /*邮轮信息*/
        String cruiseSql = "SELECT number_code,name_cn FROM cruise WHERE number_code IN ("+StringTool.listToSqlString(cruiseStrList)+")";
        @SuppressWarnings("unchecked")
        List<Object[]> cruiseList = commonDao.getSqlQuery(cruiseSql).list();
        
        for(TaobaoBaseInfo taobaoBaseInfo : baseInfoList) {
            for (Object[] cruiseObj : cruiseList) {
                if (taobaoBaseInfo.getCruise()!=null && taobaoBaseInfo.getCruise().equals((String)cruiseObj[0])) {
                    taobaoBaseInfo.setCruiseNameCn((String)cruiseObj[1]);
                    break;
                }
            }
            
            String countSql = "SELECT s.taobao_travel_item,d.is_update FROM taobao_sku_info s ";
            countSql += "LEFT JOIN taobao_sku_info_detail d ";
            countSql += "ON s.number_code = d.taobao_sku_info ";
            countSql += "WHERE d.is_update = 1 ";
            countSql += "AND s.taobao_travel_item = '"+ taobaoBaseInfo.getTaobaoTravelItem() +"' ";
            countSql += "AND d.date >= DATE_FORMAT(DATE_ADD(NOW(),INTERVAL 2 DAY),'%Y-%m-%d') ";
            @SuppressWarnings("unchecked")
            List<Object[]> countList = commonDao.getSqlQuery(countSql).list();
            
            if(countList.size() < 1){
                taobaoBaseInfo.setIsUpdate(0);
            }else{
                taobaoBaseInfo.setIsUpdate(1);
            }
        }
        
        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", count);
        mapContent.put("rows", baseInfoList);
        
        return ReturnUtil.returnMap(1, mapContent);
    }
    
    /**
     * 宝贝新增、编辑
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> saveTaobaoItem(String baseInfo , String baseJsonStr,
                                              String cruiseInfoJsonStr,
                                              String saleInfoJsonStr,
                                              String rulesJsonStr,
                                              String channel , String cruise) {
        
        TaobaoBaseInfo taobaoBaseInfoObj = null;
        TaobaoCruiseItemExt taobaoCruiseItemExtObj = null;
        TaobaoSaleInfo taobaoSaleInfoObj = null;
        Channel channelObj = null;
        
        if(StringTool.isEmpty(cruise)){
            return ReturnUtil.returnMap(0, ConstantMsg.cruise_error);
        }
        Cruise cruiseObj = (Cruise)commonDao.getObjectByUniqueCode(Constant.cruise, Constant.numberCode, cruise);
        if (cruiseObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.cruise_null);
        }
        
        if(StringTool.isEmpty(channel)){//编辑
            if (StringTool.isEmpty(baseInfo)) {
                return ReturnUtil.returnMap(0, "基础信息不存在!");
            }
            
            taobaoBaseInfoObj = (TaobaoBaseInfo)commonDao.getObjectByUniqueCode("TaobaoBaseInfo", "numberCode", baseInfo);
            if (taobaoBaseInfoObj == null) {
                return ReturnUtil.returnMap(0, "基础信息不存在!");
            }
            
            taobaoCruiseItemExtObj = (TaobaoCruiseItemExt)commonDao.getObjectByUniqueCode("TaobaoCruiseItemExt", "taobaoTravelItem", taobaoBaseInfoObj.getTaobaoTravelItem());
            
            String bookingRuleHql = "from TaobaoBookingRule where taobaoTravelItem = '"+taobaoBaseInfoObj.getTaobaoTravelItem()+"'";
            List<TaobaoBookingRule> taobaoBookingRulesList = commonDao.queryByHql(bookingRuleHql);
            if (taobaoBookingRulesList.size() <= 0) {
                return ReturnUtil.returnMap(0, "规则信息不存在!");
            }
            
            String bookingRuleDeleteSql = "delete from taobao_booking_rule where taobao_travel_item = '"+taobaoBaseInfoObj.getTaobaoTravelItem()+"'";
            commonDao.getSqlQuery(bookingRuleDeleteSql).executeUpdate();
            
        }else{//新增
            if(StringTool.isEmpty(cruise)){
                return ReturnUtil.returnMap(0, ConstantMsg.cruise_error);
            }
            
            channelObj = (Channel)commonDao.getObjectByUniqueCode("Channel", "numberCode", channel);
            if (channelObj == null) {
                return ReturnUtil.returnMap(0, ConstantMsg.channel_not_exist_error);
            }
        }
        
        //宝贝基础信息
        HashMap<String, Object> baseMap = JSON.toObject(baseJsonStr, HashMap.class);
        if (baseMap == null) {
            return ReturnUtil.returnMap(0, "基础信息参数错误!");
        }
        
        //商品规则信息
        List<HashMap<String, Object>> bookIngRuleMap = JSON.toObject(rulesJsonStr, ArrayList.class);
        if (bookIngRuleMap == null) {
            return ReturnUtil.returnMap(0, "规则信息参数错误!");
        }
        
        HashMap<String, Object> cruiseMap = JSON.toObject(cruiseInfoJsonStr, HashMap.class);
        HashMap<String, Object> saleMap = JSON.toObject(saleInfoJsonStr, HashMap.class);
        
        String logRemark = "";
        Date date = new Date();
        //宝贝基础信息
        String taobaoTitle = baseMap.get("taobaoTitle")==null?"":baseMap.get("taobaoTitle").toString();
        int taobaoAccomNights = baseMap.get("taobaoAccomNights")==""?0:Integer.valueOf(baseMap.get("taobaoAccomNights").toString()); 
        int taobaoTripMaxDays = baseMap.get("taobaoTripMaxDays")==""?0:Integer.valueOf(baseMap.get("taobaoTripMaxDays").toString());
        String taobaoProv = baseMap.get("taobaoProv")==null?"":baseMap.get("taobaoProv").toString();
        String taobaoCity = baseMap.get("taobaoCity")==null?"":baseMap.get("taobaoCity").toString();
        String taobaoSubTitles = baseMap.get("taobaoSubTitles")==null?"":baseMap.get("taobaoSubTitles").toString();
        String taobaoFromLocations = baseMap.get("taobaoFromLocations")==null?"":baseMap.get("taobaoFromLocations").toString();
        String taobaoToLocations = baseMap.get("taobaoToLocations")==null?"":baseMap.get("taobaoToLocations").toString();
        
        if(channelObj != null) {//新增
            TaobaoTravelItem taobaoTravelItemObj = new TaobaoTravelItem();
            taobaoTravelItemObj.setNumberCode(StringUtil.get_new_taobao_item_out_num());
            taobaoTravelItemObj.setChannel(channelObj.getNumberCode());
            taobaoTravelItemObj.setChannelNameCn(channelObj.getNameCn());
            taobaoTravelItemObj.setItemType(6);
            taobaoTravelItemObj.setItemStatus(-5);
            taobaoTravelItemObj.setItemId(Constant.nulo);
            taobaoTravelItemObj.setAddDate(date);
            taobaoTravelItemObj.setAlterDate(date);
            commonDao.save(taobaoTravelItemObj);
            
            taobaoBaseInfoObj = new TaobaoBaseInfo();
            taobaoBaseInfoObj.setTaobaoTravelItem(taobaoTravelItemObj.getNumberCode());
            taobaoBaseInfoObj.setNumberCode(StringUtil.get_new_taobao_base_num());
            taobaoBaseInfoObj.setItemUrl("");
            taobaoBaseInfoObj.setChannel(channelObj.getNumberCode());
            taobaoBaseInfoObj.setChannelNameCn(channelObj.getNameCn());
            taobaoBaseInfoObj.setItemType(6);
            taobaoBaseInfoObj.setAddDate(date);
            taobaoBaseInfoObj.setItemId(Constant.nulo);
           
        }else{//编辑
            if (!taobaoBaseInfoObj.getToLocations().equals(taobaoToLocations)) {
                logRemark += "目的地【"+taobaoBaseInfoObj.getToLocations()+"】修改为【"+taobaoToLocations+"】";
            }
            
            if (!taobaoBaseInfoObj.getFromLocations().equals(taobaoFromLocations)) {
                logRemark += "出发地【"+taobaoBaseInfoObj.getFromLocations()+"】修改为【"+taobaoFromLocations+"】";
            }
        }
        taobaoBaseInfoObj.setCruise(cruiseObj.getNumberCode());
        taobaoBaseInfoObj.setTitle(taobaoTitle);
        taobaoBaseInfoObj.setAccomNights(taobaoAccomNights);
        taobaoBaseInfoObj.setTripMaxDays(taobaoTripMaxDays);
        taobaoBaseInfoObj.setProv(taobaoProv);
        taobaoBaseInfoObj.setCity(taobaoCity);
        taobaoBaseInfoObj.setFromLocations(taobaoFromLocations);
        taobaoBaseInfoObj.setToLocations(taobaoToLocations);
        taobaoBaseInfoObj.setSubTitles(taobaoSubTitles);
        taobaoBaseInfoObj.setAlterDate(date);
        
        if (channelObj != null) {
            commonDao.save(taobaoBaseInfoObj);
        }else {
            commonDao.update(taobaoBaseInfoObj);
        }
        
        for (HashMap<String, Object> b : bookIngRuleMap) {
            String taobaoRule = b.get("taobaoRule")==null?"":b.get("taobaoRule").toString();
            String taobaoRuleDesc = b.get("taobaoRuleDesc")==null?"":b.get("taobaoRuleDesc").toString();
            
            TaobaoBookingRule taobaoBookingRuleObj = new TaobaoBookingRule();
            taobaoBookingRuleObj.setTaobaoTravelItem(taobaoBaseInfoObj.getTaobaoTravelItem());
            taobaoBookingRuleObj.setNumberCode(UUIDUtil.getUUIDStringFor32());
            taobaoBookingRuleObj.setRuleType(taobaoRule);
            taobaoBookingRuleObj.setRuleDesc(taobaoRuleDesc);
            taobaoBookingRuleObj.setAddDate(taobaoBaseInfoObj.getAddDate());
            taobaoBookingRuleObj.setAlterDate(date);
            commonDao.save(taobaoBookingRuleObj);
        }
        
        //邮轮信息
        if (cruiseMap != null) {
            String taobaoCruiseCompany = cruiseMap.get("taobaoCruiseCompany")==null?"":cruiseMap.get("taobaoCruiseCompany").toString();
            String taobaoShipName = cruiseMap.get("taobaoShipName")==null?"":cruiseMap.get("taobaoShipName").toString();
            String taobaoCruiseLine = cruiseMap.get("taobaoCruiseLine")==null?"":cruiseMap.get("taobaoCruiseLine").toString();
            String taobaoShipDown = cruiseMap.get("taobaoShipDown")==null?"":cruiseMap.get("taobaoShipDown").toString();
            String taobaoShipUp = cruiseMap.get("taobaoShipUp")==null?"":cruiseMap.get("taobaoShipUp").toString();
            String taobaoShipFeeInclude = cruiseMap.get("taobaoShipFeeInclude")==null?"":cruiseMap.get("taobaoShipFeeInclude").toString();
            
            if (channelObj != null) {//新增
                taobaoCruiseItemExtObj = new TaobaoCruiseItemExt();
                taobaoCruiseItemExtObj.setTaobaoTravelItem(taobaoBaseInfoObj.getTaobaoTravelItem());
                taobaoCruiseItemExtObj.setNumberCode(UUIDUtil.getUUIDStringFor32());
                taobaoCruiseItemExtObj.setAddDate(date);
            }
            taobaoCruiseItemExtObj.setCruiseCompany(taobaoCruiseCompany);
            taobaoCruiseItemExtObj.setShipName(taobaoShipName);
            taobaoCruiseItemExtObj.setCruiseLine(taobaoCruiseLine);
            taobaoCruiseItemExtObj.setShipDown(taobaoShipDown);
            taobaoCruiseItemExtObj.setShipUp(taobaoShipUp);
            taobaoCruiseItemExtObj.setShipFeeInclude(taobaoShipFeeInclude);
            taobaoCruiseItemExtObj.setAlterDate(date);
            if (channelObj != null) {//新增
                commonDao.save(taobaoCruiseItemExtObj);
            }else {
                commonDao.update(taobaoCruiseItemExtObj);
            }
        }
        
        //售卖信息
        if (saleMap != null) {
            int taobaoDuration = saleMap.get("taobaoDuration")==""?0:Integer.valueOf(saleMap.get("taobaoDuration").toString());
            int taobaoSubStock = saleMap.get("taobaoSubStock")==""?1:Integer.valueOf(saleMap.get("taobaoSubStock").toString());
            int taobaoSaleType = saleMap.get("taobaoSaleType")==null?0:Integer.valueOf(saleMap.get("taobaoSaleType").toString());
            Date taobaoEndComboDate = saleMap.get("taobaoEndComboDate")==""?null:DateUtil.stringToDate(saleMap.get("taobaoEndComboDate").toString());
            Date taobaoStartComboDate = saleMap.get("taobaoStartComboDate")==""?null:DateUtil.stringToDate(saleMap.get("taobaoStartComboDate").toString());
            String taobaoHasDiscount = saleMap.get("taobaoHasDiscount")==null?"false":saleMap.get("taobaoHasDiscount").toString().equals("1")?"true":"false";
            String taobaoHasInvoice = saleMap.get("taobaoHasInvoice")==null?"false":saleMap.get("taobaoHasInvoice").toString().equals("1")?"true":"false";
            String taobaoHasShowcase = saleMap.get("taobaoHasShowcase")==null?"false":saleMap.get("taobaoHasShowcase").toString().equals("1")?"true":"false";
            String taobaoSupportOnsaleAutoRefund = saleMap.get("taobaoSupportOnsaleAutoRefund")==null?"fasle":saleMap.get("taobaoSupportOnsaleAutoRefund").toString().equals("1")?"true":"false";
            String taobaoMerchant = saleMap.get("taobaoMerchant")==null?"":saleMap.get("taobaoMerchant").toString();
            String taobaoNetworkId = saleMap.get("taobaoNetworkId")==null?"":saleMap.get("taobaoNetworkId").toString();
            String taobaoSellerCids = saleMap.get("taobaoSellerCids")==null?"":saleMap.get("taobaoSellerCids").toString();

            if (channelObj != null) {//新增
                taobaoSaleInfoObj = new TaobaoSaleInfo();
                taobaoSaleInfoObj.setTaobaoTravelItem(taobaoBaseInfoObj.getTaobaoTravelItem());
                taobaoSaleInfoObj.setNumberCode(UUIDUtil.getUUIDStringFor32());
                taobaoSaleInfoObj.setSaleType(taobaoSaleType);
                taobaoSaleInfoObj.setAddDate(date);
            }
            
            if (channelObj == null) {
                taobaoSaleInfoObj = (TaobaoSaleInfo)commonDao.getObjectByUniqueCode("TaobaoSaleInfo", "taobaoTravelItem", taobaoBaseInfoObj.getTaobaoTravelItem());
                if (taobaoSaleInfoObj.getSaleType() != taobaoSaleType) {
                    String oldType = "";
                    String newType = "";
                    if (taobaoSaleInfoObj.getSaleType() == 0) {
                        oldType = "普通";
                    }else {
                        oldType = "预约";
                    }
                    if (taobaoSaleType == 0) {
                        newType = "普通";
                    }else {
                        newType = "预约";
                    }
                    logRemark += "售卖类型【"+oldType+"】修改为【"+newType+"】";
                }
                
                if (taobaoSaleInfoObj.getSubStock() != taobaoSubStock) {
                    String oldSub = "";
                    String newSub = "";
                    if (taobaoSaleInfoObj.getSubStock() == 0) {
                        oldSub = "拍下";
                    }else {
                        oldSub = "付款";
                    }
                    if (taobaoSubStock == 0) {
                        newSub = "拍下";
                    }else {
                        newSub = "付款";
                    }
                    logRemark += "减库存方式【"+oldSub+"】修改为【"+newSub+"】";
                }
                
                if (taobaoSaleInfoObj.getSaleType() == 1) {
                    if (taobaoStartComboDate == null) {
                        return ReturnUtil.returnMap(0, "请选择开始时间!");
                    }else if (taobaoEndComboDate == null) {
                        return ReturnUtil.returnMap(0, "请选择结束时间 !");
                    }
                }
               
                if (taobaoSaleInfoObj.getStartComboDate() != null) {
                    if (taobaoSaleInfoObj.getStartComboDate().compareTo(taobaoStartComboDate) != 0) {
                        logRemark += "开始时间【"+taobaoSaleInfoObj.getStartComboDate()+"】修改为【"+taobaoStartComboDate+"】";
                    }
                }else {
                    if (taobaoStartComboDate != null) {
                        logRemark += "开始时间【无】修改为【"+taobaoStartComboDate+"】";
                    }
                }
                
                if (taobaoSaleInfoObj.getEndComboDate() != null) {
                    if (taobaoSaleInfoObj.getEndComboDate().compareTo(taobaoEndComboDate) != 0) {
                        logRemark += "结束时间【"+taobaoSaleInfoObj.getEndComboDate()+"】修改为【"+taobaoEndComboDate+"】";
                    }
                }else {
                    if (taobaoEndComboDate != null) {
                        logRemark += "结束时间【无】修改为【"+taobaoEndComboDate+"】";
                    }
                }
               
                String[] newCids = taobaoSaleInfoObj.getSellerCids().split(",");
                for (String c : newCids) {
                    if (!taobaoSellerCids.contains(c)) {
                        logRemark += "结店铺类目编号【"+taobaoSaleInfoObj.getSellerCids()+"】修改为【"+taobaoSellerCids+"】";
                    }
                }
            }
            taobaoSaleInfoObj.setDuration(taobaoDuration);
            taobaoSaleInfoObj.setEndComboDate(taobaoEndComboDate);
            taobaoSaleInfoObj.setHasDiscount(taobaoHasDiscount);
            taobaoSaleInfoObj.setHasInvoice(taobaoHasInvoice);
            taobaoSaleInfoObj.setHasShowcase(taobaoHasShowcase);
            taobaoSaleInfoObj.setMerchant(taobaoMerchant);
            taobaoSaleInfoObj.setNetworkId(taobaoNetworkId);
            taobaoSaleInfoObj.setSaleType(taobaoSaleType);
            taobaoSaleInfoObj.setSellerCids(taobaoSellerCids);
            taobaoSaleInfoObj.setStartComboDate(taobaoStartComboDate);
            taobaoSaleInfoObj.setSubStock(taobaoSubStock);
            taobaoSaleInfoObj.setSupportOnsaleAutoRefund(taobaoSupportOnsaleAutoRefund);
            taobaoSaleInfoObj.setAlterDate(date);
            if (channelObj != null) {//新增
                commonDao.save(taobaoSaleInfoObj);
            }else {
                commonDao.update(taobaoSaleInfoObj);
            }
        }
        
        TaobaoLogWork logWork = new TaobaoLogWork();
        if (channelObj != null) {//新增日志
            logWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
            logWork.setTableName("baseTable");
            logWork.setValue(taobaoBaseInfoObj.getNumberCode());
            logWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
            logWork.setRemark("系统添加宝贝【"+taobaoBaseInfoObj.getTitle()+"】");
            logWork.setAddDate(date);
            commonDao.save(logWork);
        }else{
            logWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
            logWork.setTableName("baseTable");
            logWork.setValue(taobaoBaseInfoObj.getNumberCode());
            logWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
            logWork.setRemark("系统编辑宝贝【"+taobaoBaseInfoObj.getTitle()+"】"+logRemark);
            logWork.setAddDate(date);
            commonDao.save(logWork);
        }
        
        return ReturnUtil.returnMap(1, null);
    }
   
    /**
     * 宝贝图片
     */
    @Override
    public Map<String, Object> addImages(String taobaoBaseInfo , String urls) {
        
        TaobaoBaseInfo taobaoBaseInfoObj = (TaobaoBaseInfo)commonDao.getObjectByUniqueCode("TaobaoBaseInfo", "numberCode", taobaoBaseInfo);
        if (taobaoBaseInfoObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.taobao_base_info_not_exist_error);
        }
        
        taobaoBaseInfoObj.setPicUrls(urls);
        taobaoBaseInfoObj.setAlterDate(new Date());
        commonDao.update(taobaoBaseInfoObj);
        
        return ReturnUtil.returnMap(1, null);
    }

    /**
     * 宝贝详情
     */
    @Override
    public Map<String, Object> addDetail(String taobaoBaseInfo , String detail) {
       
        TaobaoBaseInfo taobaoBaseInfoObj = (TaobaoBaseInfo)commonDao.getObjectByUniqueCode("TaobaoBaseInfo", "numberCode", taobaoBaseInfo);
        if (taobaoBaseInfoObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.taobao_base_info_not_exist_error);
        }
        
        taobaoBaseInfoObj.setDesc(detail);
        taobaoBaseInfoObj.setAlterDate(new Date());
        commonDao.update(taobaoBaseInfoObj);
        
        return ReturnUtil.returnMap(1, null);
    }
    
    /**
     * 根据travalItem获取宝贝SKU以及SKU的库存
     */
    @Override
    public Map<String, Object> getTaobaoItemByNumberCode(String numberCode){
        if(!StringTool.isNotNull(numberCode)){
            return ReturnUtil.returnMap(0, "请选择淘宝宝贝!");
        }
        
        String skuHql = "from TaobaoSkuInfo s where s.taobaoTravelItem = '"+ numberCode +"'";
        @SuppressWarnings("unchecked")
        List<TaobaoSkuInfo> skuList = commonDao.queryByHql(skuHql);
        
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("skuList", skuList);
        
        Map<String,List<TaobaoSkuInfoDetail>> skuMap = new HashMap<String,List<TaobaoSkuInfoDetail>>();
        for(TaobaoSkuInfo s : skuList){
            String stockHql = "from TaobaoSkuInfoDetail d where d.taobaoSkuInfo = '"+ s.getNumberCode() +"'";
            @SuppressWarnings("unchecked")
            List<TaobaoSkuInfoDetail> stockList = commonDao.queryByHql(stockHql);
            
            skuMap.put(s.getNumberCode(), stockList);
        }
        map.put("stockList", skuMap);
        
        return ReturnUtil.returnMap(1, map);
    }
    
    /**
     * SKU同步
     */
    @Override
    public Map<String, Object> syncTaobaoTravelItemOper(String syncNumberCode , String skuJson , String travelItems,int type ) {
    	
    	  for(String travelItem:travelItems.split(",")){
    		 if (StringTool.isEmpty(travelItem)) {
				return ReturnUtil.returnMap(0, "请选择要同步的宝贝!");
			 }
    		 
    		 List<String> skuPackages=new ArrayList<String>();
    		  
    		if(type==0){
    		    //删除要同步到的宝贝SKU库存数据
    	        String skuDetailDelete = "DELETE FROM taobao_sku_info_detail WHERE taobao_sku_info IN (";
    	        skuDetailDelete += " SELECT sk.number_code FROM taobao_sku_info sk WHERE sk.taobao_travel_item = '"+ travelItem +"'";
    	        skuDetailDelete += ") ";
    	        commonDao.getSqlQuery(skuDetailDelete).executeUpdate();
    	        
    		}
    		  
	        Date date = new Date();
	        TaobaoTravelItem travelItemSync = (TaobaoTravelItem)commonDao.getObjectByUniqueCode("TaobaoTravelItem", "numberCode", syncNumberCode);
	        if(travelItemSync == null){
	            return ReturnUtil.returnMap(0, "同步商品信息不存在!");
	        }
	        
	        TaobaoTravelItem travelItemObj = (TaobaoTravelItem)commonDao.getObjectByUniqueCode("TaobaoTravelItem", "numberCode", travelItem);
	        if(travelItemObj == null){
	            return ReturnUtil.returnMap(0, "商品信息不存在!");
	        }
	        travelItemObj.setItemStatus(travelItemSync.getItemStatus());
	        travelItemObj.setItemType(travelItemSync.getItemType());
	        travelItemObj.setSellerId(travelItemSync.getSellerId());
	        travelItemObj.setCreated(travelItemSync.getCreated());
	        travelItemObj.setModified(travelItemSync.getModified());
	        travelItemObj.setAddDate(date);
	        travelItemObj.setAlterDate(date);
	        commonDao.update(travelItemObj);
	        
	        TaobaoBaseInfo baseInfoObj = (TaobaoBaseInfo)commonDao.getObjectByUniqueCode("TaobaoBaseInfo", "taobaoTravelItem", travelItem);
	        
	        TaobaoLogWork logWork = new TaobaoLogWork();
	        logWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
	        logWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
	        logWork.setTableName("baseTable");
	        logWork.setValue(baseInfoObj.getNumberCode());
	        if(type==0){
		        logWork.setRemark("【覆盖模式】同步宝贝【"+travelItemSync.getChannelNameCn()+"】【"+baseInfoObj.getTitle()+"】");
	        }else{
		        logWork.setRemark("【更新模式】同步宝贝【"+travelItemSync.getChannelNameCn()+"】【"+baseInfoObj.getTitle()+"】");
	        }
	        logWork.setAddDate(date);
	        commonDao.save(logWork);
	        
	        @SuppressWarnings("unchecked")
	        List<Map<String,Object>> skuSyncList = JSON.toObject(skuJson, ArrayList.class);
	        //int skuCount = 0;
	        if(skuSyncList != null && skuSyncList.size() > 0){
	            for(Map<String,Object> skuMap : skuSyncList){
	                String sycnSkuNumber = (String)skuMap.get("numberCode");
	                String isSycnStr = (String)skuMap.get("isSycn");
	                int isSync = Integer.parseInt(isSycnStr);
	                TaobaoSkuInfo skuSyncObj = (TaobaoSkuInfo)commonDao.getObjectByUniqueCode("TaobaoSkuInfo", "numberCode", sycnSkuNumber);
	                skuSyncObj.setIsSync(isSync);
	                commonDao.update(skuSyncObj);
	                
	                skuPackages.add(skuSyncObj.getPackageName());
	                boolean pd = false;
	                if(isSync == 0){//同步开始
	                	TaobaoSkuInfo  skuInfoObj = new TaobaoSkuInfo();
	             
	                	String hql="from TaobaoSkuInfo where taobaoTravelItem='"+travelItem+"' and packageName='"+skuSyncObj.getPackageName()+"'";
	                	skuInfoObj=(TaobaoSkuInfo) commonDao.queryUniqueByHql(hql);
	                	 TaobaoLogWork skuLogWork = new TaobaoLogWork();
	                	if(skuInfoObj!=null){
	                	    skuInfoObj.setIsSync(isSync);
	                	    skuInfoObj.setFlag(skuSyncObj.getFlag());
	                	    skuInfoObj.setIsLock(skuSyncObj.getIsLock());
	                	    skuInfoObj.setAlterDate(date);
	                	    commonDao.update(skuInfoObj);
	                	    
	                	    skuLogWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
	                	    skuLogWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
	                	    skuLogWork.setTableName("skuTable");
	                	    skuLogWork.setValue(skuInfoObj.getNumberCode());
	                	    skuLogWork.setRemark("编辑同步SKU套餐【"+ skuSyncObj.getPackageName() +"】");
	                	    skuLogWork.setAddDate(date);
	                	    commonDao.save(skuLogWork);
	                        
	                	}else{
	                	    skuInfoObj = new TaobaoSkuInfo();
	                	    skuInfoObj.setTaobaoTravelItem(travelItem);
    	                    skuInfoObj.setNumberCode(StringUtil.get_new_taobao_sku_out_num());
    	                    skuInfoObj.setPackageName(skuSyncObj.getPackageName());
    	                    skuInfoObj.setIsSync(isSync);
    	                    skuInfoObj.setFlag(skuSyncObj.getFlag());
    	                    skuInfoObj.setIsLock(skuSyncObj.getIsLock());
    	                    skuInfoObj.setAddDate(date);
    	                    skuInfoObj.setAlterDate(date);
    	                    commonDao.save(skuInfoObj);
    	                    
    	                    skuLogWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
                            skuLogWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
                            skuLogWork.setTableName("skuTable");
                            skuLogWork.setValue(skuInfoObj.getNumberCode());
                            skuLogWork.setRemark("新增同步SKU套餐【"+ skuSyncObj.getPackageName() +"】");
                            skuLogWork.setAddDate(date);
                            commonDao.save(skuLogWork);
    	        		}
                    	
	                    @SuppressWarnings("unchecked")
	                    List<Map<String,String>> stcokList = (List<Map<String,String>>) skuMap.get("stock");
	                    for(Map<String,String> stockMap : stcokList){
	                        String stockNumber = (String)stockMap.get("stock");
	                        String isSycnStockStr = (String)stockMap.get("isSycn");
	                        int isSycnStock = Integer.parseInt(isSycnStockStr);
	                        
	                        TaobaoSkuInfoDetail stockSync = (TaobaoSkuInfoDetail)commonDao.getObjectByUniqueCode("TaobaoSkuInfoDetail", "numberCode", stockNumber);
	                        stockSync.setIsSync(isSycnStock);
	                        stockSync.setAlterDate(date);
	                        commonDao.update(stockSync);
	                        
	                        if(isSycnStock == 0){//同步
	                        	TaobaoSkuInfoDetail stockObj;
	                        	if(type==1){
	                        		hql="from TaobaoSkuInfoDetail where taobaoSkuInfo='"+skuInfoObj.getNumberCode()+"' and date='"+DateUtil.dateToShortString(stockSync.getDate())+"'";
	                        		stockObj=(TaobaoSkuInfoDetail) commonDao.queryUniqueByHql(hql);
	                        		if(stockObj!=null){
	                        			commonDao.delete(stockObj);
	                        		}
	                        	}
	                        	stockObj = new TaobaoSkuInfoDetail();
	                            stockObj.setNumberCode(StringUtil.get_new_taobao_price_out_num());
	                            stockObj.setTaobaoSkuInfo(skuInfoObj.getNumberCode());
	                            stockObj.setOutId(stockSync.getOutId());
	                            stockObj.setAirway(stockSync.getAirway());
	                            stockObj.setTkviewNameCn(stockSync.getTkviewNameCn());
	                            stockObj.setPriceType(stockSync.getPriceType());
	                            stockObj.setPrice(stockSync.getPrice());
	                            stockObj.setStock(stockSync.getStock());
	                            stockObj.setIsSync(isSycnStock);
	                            stockObj.setIsLock(stockSync.getIsLock());
	                            stockObj.setIsEdit(stockSync.getIsEdit());
	                            stockObj.setFlag(stockSync.getFlag());
	                            stockObj.setDate(stockSync.getDate());
	                            stockObj.setAddDate(date);
	                            stockObj.setAlterDate(date);
	                            commonDao.save(stockObj);
	                            
	                            TaobaoLogWork detailLogWork = new TaobaoLogWork();
	                            detailLogWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
	                            detailLogWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
	                            detailLogWork.setTableName("skuDetailTable");
	                            detailLogWork.setValue(stockObj.getNumberCode());
	                            detailLogWork.setRemark("同步SKU套餐【"+ skuSyncObj.getPackageName() +"】下的日历库存【"+ DateUtil.dateToShortString(stockSync.getDate()) +"】");
	                            detailLogWork.setAddDate(date);
	                            commonDao.save(detailLogWork);
	                        }
	                    }
	                    
	                }//同步结束
	                
	                if(pd){
	                    break;
	                }
	            }
	        }
	        
	        //删除要同步到的宝贝SKU数据
	        if(type==0){
	        	String skuDelete = "DELETE FROM taobao_sku_info WHERE taobao_travel_item = '"+ travelItem +"' and package_name not in("+StringTool.listToSqlString(skuPackages)+")";
		        commonDao.getSqlQuery(skuDelete).executeUpdate();
	        }
	  }
    	  
        return ReturnUtil.returnMap(1, null);
    }
    
    /**
     * 宝贝克隆
     */
    @Override
    public Map<String, Object> copyTaobaoTravelItemOper(String numberCode , String channel ,
                                                        String skuJson , String stockJson){
        
        if(StringTool.isEmpty(numberCode)){
            return ReturnUtil.returnMap(0, "请选择要克隆的宝贝!");
        }
        if(StringTool.isEmpty(channel)){
            return ReturnUtil.returnMap(0, ConstantMsg.channel_error);
        }
        
        Date date = new Date();
        TaobaoTravelItem taobaoTravelItem = (TaobaoTravelItem)commonDao.getObjectByUniqueCode("TaobaoTravelItem", "numberCode", numberCode);
        Channel channelObj = (Channel)commonDao.getObjectByUniqueCode("Channel", "numberCode", channel);
        
        String copyTravelItemNumberCode = StringUtil.get_new_taobao_item_out_num();
        TaobaoTravelItem copyTravelItem = (TaobaoTravelItem)ClazzUtil.cloneObj(taobaoTravelItem, "iidd","numberCode","itemId");
        copyTravelItem.setNumberCode(copyTravelItemNumberCode);
        copyTravelItem.setItemStatus(-5);
        copyTravelItem.setChannel(channel);
        copyTravelItem.setChannelNameCn(channelObj.getNameCn());
        copyTravelItem.setAddDate(date);
        copyTravelItem.setAlterDate(date);
        commonDao.save(copyTravelItem);
        
        //TaobaoBaseInfo
        String baseInfoHql = "from TaobaoBaseInfo bi where bi.taobaoTravelItem = '"+taobaoTravelItem.getNumberCode()+"'";
        @SuppressWarnings("unchecked")
        List<TaobaoBaseInfo> baseInfoList = commonDao.getQuery(baseInfoHql).list();
        if(baseInfoList.size() == 0){
            return ReturnUtil.returnMap(0, "001:商品有误!");
        }
        
        TaobaoBaseInfo copyBaseInfo = (TaobaoBaseInfo)ClazzUtil.cloneObj(baseInfoList.get(0), "iidd","numberCode");
        String oldChannel = copyBaseInfo.getChannel();
        copyBaseInfo.setNumberCode(StringUtil.get_new_taobao_base_num());
        copyBaseInfo.setChannel(channel);
        copyBaseInfo.setChannelNameCn(channelObj.getNameCn());
        copyBaseInfo.setAddDate(date);
        copyBaseInfo.setAlterDate(date);
        copyBaseInfo.setTaobaoTravelItem(copyTravelItemNumberCode);
        commonDao.save(copyBaseInfo);
        
        TaobaoLogWork logWork = new TaobaoLogWork();
        logWork.setAddDate(new Date());
        logWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
        logWork.setValue(copyBaseInfo.getNumberCode());
        logWork.setRemark("将渠道【"+ oldChannel +"】下的宝贝【"+baseInfoList.get(0).getTitle()+"】复制到渠道【"+copyBaseInfo.getChannel()+"】下");
        logWork.setTableName("baseTable");
        logWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
        commonDao.save(logWork);
        
        //taobaoBookingRule
        String bookingRuleHql = "from TaobaoBookingRule br where br.taobaoTravelItem = '"+taobaoTravelItem.getNumberCode()+"'";
        @SuppressWarnings("unchecked")
        List<TaobaoBookingRule> bookingRuleList = commonDao.getQuery(bookingRuleHql).list();
        for (TaobaoBookingRule bookingRule : bookingRuleList) {
            TaobaoBookingRule bookingRuleClone = (TaobaoBookingRule)ClazzUtil.cloneObj(bookingRule, "iidd","numberCode");
            bookingRuleClone.setNumberCode(UUIDUtil.getUUIDStringFor32());
            bookingRuleClone.setTaobaoTravelItem(copyTravelItemNumberCode);
            bookingRuleClone.setAddDate(date);
            bookingRuleClone.setAlterDate(date);
            commonDao.save(bookingRuleClone);
            
        }
        
        //taobaoCruiseItemExt
        String cruiseItemExtHql = "from TaobaoCruiseItemExt cie where cie.taobaoTravelItem = '"+taobaoTravelItem.getNumberCode()+"'";
        @SuppressWarnings("unchecked")
        List<TaobaoCruiseItemExt> cruiseItemList = commonDao.getQuery(cruiseItemExtHql).list();
        if(cruiseItemList.size() == 0){
            return ReturnUtil.returnMap(0, "002:商品有误!");
        }
        TaobaoCruiseItemExt copyCruiseItemExt = (TaobaoCruiseItemExt)ClazzUtil.cloneObj(cruiseItemList.get(0), "iidd","numberCode");
        copyCruiseItemExt.setNumberCode(UUIDUtil.getUUIDStringFor32());
        copyCruiseItemExt.setTaobaoTravelItem(copyTravelItemNumberCode);
        copyCruiseItemExt.setAddDate(date);
        copyCruiseItemExt.setAlterDate(date);
        commonDao.save(copyCruiseItemExt);
        
        //taobaoSaleInfo
        String taobaoSaleInfoHql = "from TaobaoSaleInfo si where si.taobaoTravelItem = '"+taobaoTravelItem.getNumberCode()+"'";
        @SuppressWarnings("unchecked")
        List<TaobaoSaleInfo> taobaoSaleInfoHqlList = commonDao.getQuery(taobaoSaleInfoHql).list();
        if(cruiseItemList.size() == 0){
            return ReturnUtil.returnMap(0, "003:商品有误!");
        }
        TaobaoSaleInfo copySaleInfo = (TaobaoSaleInfo)ClazzUtil.cloneObj(taobaoSaleInfoHqlList.get(0), "iidd","numberCode");
        copySaleInfo.setNumberCode(UUIDUtil.getUUIDStringFor32());
        copySaleInfo.setTaobaoTravelItem(copyTravelItemNumberCode);
        copySaleInfo.setAddDate(date);
        copySaleInfo.setAlterDate(date);
        commonDao.save(copySaleInfo);
        
        //taobaoSkuInfo
        String taobaoSkuInfoHql = "from TaobaoSkuInfo si where si.taobaoTravelItem = '"+taobaoTravelItem.getNumberCode()+"'";
        @SuppressWarnings("unchecked")
        List<TaobaoSkuInfo> taobaoSkuInfoList = commonDao.getQuery(taobaoSkuInfoHql).list();
        
        @SuppressWarnings("unchecked")
        Map<String ,String> skuMap = JSON.toObject(skuJson ,HashMap.class);
        
        @SuppressWarnings("unchecked")
        List<Map<String,Object>> stockList = JSON.toObject(stockJson, ArrayList.class);
        
        String taobaoSkuDetailInfoHql = "from TaobaoSkuInfoDetail sdi where sdi.taobaoSkuInfo in ("+StringTool.listToSqlString(skuMap.keySet())+")";
        @SuppressWarnings("unchecked")
        List<TaobaoSkuInfoDetail> taobaoSkuInfoDetailList = commonDao.getQuery(taobaoSkuDetailInfoHql).list();
        
        for (String skuNumberCode : skuMap.keySet()) {
            TaobaoSkuInfo copySkuInfo = null;
            for (TaobaoSkuInfo skuInfo : taobaoSkuInfoList) {
                if(skuInfo.getNumberCode().equals(skuNumberCode)){
                    copySkuInfo = (TaobaoSkuInfo)ClazzUtil.cloneObj(skuInfo, "iidd");
                    break;
                }
            }
            if(copySkuInfo == null){
                continue;
            }
            
            String skuInfoNumberCode = StringUtil.get_new_taobao_sku_out_num();
            for(Map<String,Object> stockMap : stockList){
                String skuNumber = (String)stockMap.get("skuNumber");
                if(!skuNumberCode.equals(skuNumber)){
                    continue;
                }
                String skuIsLock = (String)stockMap.get("isLock");
                
                @SuppressWarnings("unchecked")
                List<Map<String,String>> skuStcokList = (List<Map<String,String>>)stockMap.get("stock");
                for(Map<String,String> lockMap : skuStcokList){
                    String stockNumber = lockMap.get("numberCode").toString();
                    String isLock = lockMap.get("isLock").toString();
                    
                    for (TaobaoSkuInfoDetail skuDetail : taobaoSkuInfoDetailList) {
                        if(skuMap.get(copySkuInfo.getNumberCode()).contains(skuDetail.getNumberCode()) && stockNumber.equals(skuDetail.getNumberCode())){
                            TaobaoSkuInfoDetail skuDetailClone = (TaobaoSkuInfoDetail)ClazzUtil.cloneObj(skuDetail, "iidd","numberCode");
                            skuDetailClone.setNumberCode(StringUtil.get_new_taobao_price_out_num());
                            skuDetailClone.setTaobaoSkuInfo(skuInfoNumberCode);
                            skuDetailClone.setIsLock(Integer.parseInt(isLock));
                            skuDetailClone.setIsEdit(Integer.parseInt(isLock));
                            skuDetailClone.setIsUpdate(0);
                            skuDetailClone.setAddDate(date);
                            skuDetailClone.setAlterDate(date);
                            commonDao.save(skuDetailClone);
                            
                            TaobaoLogWork detailLogWork = new TaobaoLogWork();
                            detailLogWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
                            detailLogWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
                            detailLogWork.setTableName("skuDetailTable");
                            detailLogWork.setValue(skuDetailClone.getNumberCode());
                            detailLogWork.setRemark("复制SKU套餐【"+ copySkuInfo.getPackageName() +"】下的日历库存【"+ DateUtil.dateToShortString(skuDetailClone.getDate()) +"】");
                            detailLogWork.setAddDate(date);
                            commonDao.save(detailLogWork);
                            break;
                        }
                    }
                }
                copySkuInfo.setNumberCode(skuInfoNumberCode);
                copySkuInfo.setTaobaoTravelItem(copyTravelItemNumberCode);
                copySkuInfo.setIsLock(Integer.parseInt(skuIsLock));
                copySkuInfo.setAddDate(date);
                copySkuInfo.setAlterDate(date);
                commonDao.save(copySkuInfo);
                
                TaobaoLogWork skuLogWork = new TaobaoLogWork();
                skuLogWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
                skuLogWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
                skuLogWork.setTableName("skuTable");
                skuLogWork.setValue(copySkuInfo.getNumberCode());
                skuLogWork.setRemark("复制SKU套餐【"+ copySkuInfo.getPackageName() +"】");
                skuLogWork.setAddDate(date);
                commonDao.save(skuLogWork);
            }
            
        }
        return ReturnUtil.returnMap(1, null);
    }
   
    /**
     * 发布宝贝(未发布是上传;如果是批量上传,不会上传已发布的宝贝)
     */
    @Override
    public Map<String, Object> publishTaobaoItemOper(String baseInfo) {

        String baseInfoHql = "from TaobaoBaseInfo where numberCode in ("+StringTool.StrToSqlString(baseInfo)+")";
        
        @SuppressWarnings("unchecked")
        List<TaobaoBaseInfo> taobaoBaseInfoList = commonDao.queryByHql(baseInfoHql);
        if (taobaoBaseInfoList.size() <= 0) {
            return ReturnUtil.returnMap(0, "宝贝基础信息不存在!");
        }
        for (TaobaoBaseInfo taobaoBaseInfoObj : taobaoBaseInfoList) {
            if (StringTool.isEmpty(taobaoBaseInfoObj.getDesc())) {
                return ReturnUtil.returnMap(0, "请添加宝贝详情!");
            }
            if (StringTool.isEmpty(taobaoBaseInfoObj.getPicUrls())) {
                return ReturnUtil.returnMap(0, "请添加宝贝图片!");
            }
            
            TaobaoTravelItem taobaoTravelItemObj = (TaobaoTravelItem)commonDao.getObjectByUniqueCode("TaobaoTravelItem", "numberCode", taobaoBaseInfoObj.getTaobaoTravelItem());
            if (taobaoTravelItemObj == null) {
                return ReturnUtil.returnMap(0, ConstantMsg.taobao_base_info_not_exist_error);
            }
            
            //已发布的宝贝,上传更新(批量上传)
            if (StringTool.isNotNull(taobaoTravelItemObj.getItemId())) {
                updateTaobaoItem(taobaoBaseInfoObj.getNumberCode());
                continue;
            }
            
            Map<String, Object> map = taobaoUtil.publishTaobaoItemOper(taobaoBaseInfoObj, taobaoTravelItemObj);
            if (map.get("status").toString().equals("0")) {
                return ReturnUtil.returnMap(0, map.get("msg").toString());
            }else {
                taobaoTravelItemObj = (TaobaoTravelItem)map.get("content");
                commonDao.update(taobaoTravelItemObj);
            }
            
            TaobaoLogWork baseLogWork = new TaobaoLogWork();
            baseLogWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
            baseLogWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
            baseLogWork.setValue(taobaoBaseInfoObj.getNumberCode());
            baseLogWork.setTableName("baseTable");
            baseLogWork.setRemark("发布宝贝【"+taobaoBaseInfoObj.getTitle()+"】");
            baseLogWork.setAddDate(new Date());
            commonDao.save(baseLogWork);
            
        }
        
        return ReturnUtil.returnMap(1, null);
    }

    /**
     * 已发布宝贝，更新到淘宝上去
     */
    @Override
    public Map<String, Object> updateTaobaoItem(String baseInfo) {
        
        TaobaoBaseInfo taobaoBaseInfoObj = (TaobaoBaseInfo)commonDao.getObjectByUniqueCode("TaobaoBaseInfo", "numberCode", baseInfo);
        if (taobaoBaseInfoObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.taobao_base_info_not_exist_error);
        }
        
        TaobaoTravelItem taobaoTravelItemObj = (TaobaoTravelItem)commonDao.getObjectByUniqueCode("TaobaoTravelItem", "numberCode", taobaoBaseInfoObj.getTaobaoTravelItem());
        if (taobaoTravelItemObj == null) {
            return ReturnUtil.returnMap(0, "商品信息不存在!");
        }
        
        Channel channelObj = (Channel)commonDao.getObjectByUniqueCode("Channel", "numberCode", taobaoTravelItemObj.getChannel());
        if (channelObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.channel_not_exist_error);
        }
        
        TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", channelObj.getAppKey(), channelObj.getAppSecret());
        AlitripTravelItemBaseModifyRequest req = new AlitripTravelItemBaseModifyRequest();
        
        /*宝贝基础信息*/
        PontusTravelItemBaseInfo pontusTravelItemBaseInfoObj = new PontusTravelItemBaseInfo();//baseInfo
        pontusTravelItemBaseInfoObj.setTitle(taobaoBaseInfoObj.getTitle());
        
        //商品亮点
        List<String> subTitleslist = new ArrayList<String>();
        String[] subTitles = taobaoBaseInfoObj.getSubTitles().split(",");
        for (int i = 0; i < subTitles.length; i++ ) {
            subTitleslist.add(subTitles[i]);
        }
        pontusTravelItemBaseInfoObj.setSubTitles(subTitleslist);
        
        //宝贝图片信息
        List<String> picUrlsList = new ArrayList<String>();
        String[] picUrls = null;
        if (StringTool.isNotNull(taobaoBaseInfoObj.getPicUrls())) {
            picUrls = taobaoBaseInfoObj.getPicUrls().split(",");
            for (int i = 0; i < picUrls.length; i++ ) {
                picUrlsList.add(picUrls[i]);
            }
        }
        pontusTravelItemBaseInfoObj.setPicUrls(picUrlsList);
        pontusTravelItemBaseInfoObj.setItemType(Long.valueOf(taobaoBaseInfoObj.getItemType()));
        pontusTravelItemBaseInfoObj.setTripMaxDays(Long.valueOf(taobaoBaseInfoObj.getTripMaxDays()));
        pontusTravelItemBaseInfoObj.setAccomNights(Long.valueOf(taobaoBaseInfoObj.getAccomNights()));
        pontusTravelItemBaseInfoObj.setFromLocations(taobaoBaseInfoObj.getFromLocations());
        pontusTravelItemBaseInfoObj.setToLocations(taobaoBaseInfoObj.getToLocations());
        pontusTravelItemBaseInfoObj.setProv(taobaoBaseInfoObj.getProv());
        pontusTravelItemBaseInfoObj.setCity(taobaoBaseInfoObj.getCity());
        pontusTravelItemBaseInfoObj.setDesc(taobaoBaseInfoObj.getDesc());
        pontusTravelItemBaseInfoObj.setOutId(taobaoBaseInfoObj.getNumberCode());
        //手机端详情
        //pontusTravelItemBaseInfoObj.setWapDesc("<wapDesc><shortDesc>标题</shortDesc> <txt>描述</txt> <img>图片路径</img></wapDesc>");
        req.setBaseInfo(pontusTravelItemBaseInfoObj);
        
        /*商品规则信息*/
        String bookingRuleHql = "from TaobaoBookingRule where taobaoTravelItem = '"+taobaoBaseInfoObj.getTaobaoTravelItem()+"'";
        @SuppressWarnings("unchecked")
        List<TaobaoBookingRule> taobaoBookingRulesList = commonDao.queryByHql(bookingRuleHql);
        
        List<PontusTravelBookingRuleInfo> PontusTravelBookingRuleInfoList = new ArrayList<PontusTravelBookingRuleInfo>();
        if (taobaoBookingRulesList.size() <= 0) {
            return ReturnUtil.returnMap(0, "规则信息不存在!");
        }
        
        for (TaobaoBookingRule rule : taobaoBookingRulesList) {
            PontusTravelBookingRuleInfo PontusTravelBookingRuleInfoObj = new PontusTravelBookingRuleInfo();
            PontusTravelBookingRuleInfoObj.setRuleType(rule.getRuleType());
            PontusTravelBookingRuleInfoObj.setRuleDesc(rule.getRuleDesc());
            PontusTravelBookingRuleInfoList.add(PontusTravelBookingRuleInfoObj);
        }
        req.setBookingRules(PontusTravelBookingRuleInfoList);
        
        //TODO 修改商品售卖属性，淘宝不允许，看后期如何调整
        //TaobaoSaleInfo taobaoSaleInfoObj = (TaobaoSaleInfo)commonDao.getObjectByUniqueCode("TaobaoSaleInfo", "taobaoTravelItem", taobaoBaseInfoObj.getTaobaoTravelItem());
        
        /*宝贝邮轮信息*/
        TaobaoCruiseItemExt taobaoCruiseItemExtObj = (TaobaoCruiseItemExt)commonDao.getObjectByUniqueCode("TaobaoCruiseItemExt", "taobaoTravelItem", taobaoBaseInfoObj.getTaobaoTravelItem());
        if (taobaoCruiseItemExtObj != null) {
            CruiseItemExt CruiseItemExtObj = new CruiseItemExt();//邮轮
            CruiseItemExtObj.setCruiseCompany(taobaoCruiseItemExtObj.getCruiseCompany());
            CruiseItemExtObj.setShipName(taobaoCruiseItemExtObj.getShipName());
            CruiseItemExtObj.setCruiseLine(taobaoCruiseItemExtObj.getCruiseLine());
            CruiseItemExtObj.setShipDown(taobaoCruiseItemExtObj.getShipDown());
            CruiseItemExtObj.setShipUp(taobaoCruiseItemExtObj.getShipUp());
            
            //邮轮费用包含
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
        req.setItemId(Long.valueOf(taobaoTravelItemObj.getItemId()));
        
        AlitripTravelItemBaseModifyResponse rsp = null;
        try {
            rsp = client.execute(req, channelObj.getSessionKey());
        }catch (ApiException e) {
            throw new MyExceptionForXyz(e.getMessage());
        }
        
        @SuppressWarnings("unchecked")
        HashMap<String, Object> alitripTravelItemBaseAddResponseMap = JSON.toObject(rsp.getBody(), HashMap.class);
        
        @SuppressWarnings("unchecked")
        HashMap<String, Object> travelItemMap = (HashMap<String, Object>)alitripTravelItemBaseAddResponseMap.get("alitrip_travel_item_base_modify_response");
       
        if (travelItemMap == null) {
            @SuppressWarnings("unchecked")
            Map<String, Object> errorResponseMap = (Map<String, Object>)alitripTravelItemBaseAddResponseMap.get("error_response");
            return ReturnUtil.returnMap(0, "接口错误,错误码【"+errorResponseMap.get("msg").toString()+"】错误信息【"+errorResponseMap.get("sub_msg").toString()+"】");
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
        if (StringTool.isNotNull(created)) {
        	taobaoTravelItemObj.setCreated(DateUtil.stringToDate(created));
        }
        taobaoBaseInfoObj.setItemUrl("https://items.alitrip.com/item.htm?id="+itemId.toString());//宝贝ID
        commonDao.update(taobaoTravelItemObj);
        
        TaobaoLogWork baseLogWork = new TaobaoLogWork();
        baseLogWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
        baseLogWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
        baseLogWork.setValue(taobaoBaseInfoObj.getNumberCode());
        baseLogWork.setTableName("baseTable");
        baseLogWork.setRemark("发布宝贝【"+taobaoBaseInfoObj.getTitle()+"】");
        baseLogWork.setAddDate(new Date());
        commonDao.save(baseLogWork);
        
        String updateSql = "UPDATE taobao_sku_info_detail SET is_update = 0 ";
        updateSql += "WHERE taobao_sku_info IN ( ";
        updateSql += " SELECT s.number_code FROM taobao_sku_info s WHERE s.taobao_travel_item = '"+ taobaoBaseInfoObj.getTaobaoTravelItem() +"' ";
        updateSql += ") ";
        commonDao.getSqlQuery(updateSql).executeUpdate();
        
        return ReturnUtil.returnMap(1, null);
    }
    
    /**
     * 获取/跟新宝贝
     */
    @Override
    public Map<String, Object> getTaobaoItemByIdOper(Long id,String channel,String remark) {
        
        int addAllCount = 0;
        int addCount = 0;
        
        Channel channelObj = (Channel)commonDao.getObjectByUniqueCode("Channel", "numberCode", channel);
        
        String travelItemIsOnlySql = "select item_id from taobao_travel_item where channel = '"+channelObj.getNumberCode()+"'";
     
        @SuppressWarnings("unchecked")
        List<String> travelItemIsOnlyList = commonDao.getSqlQuery(travelItemIsOnlySql).list();
        //本地没有的Itemid就添加这个宝贝
        if (!travelItemIsOnlyList.contains(id.toString())) {
            Map<String ,Object> resultmap = taobaoItemByIdOper(id, channelObj, remark,"获取单个宝贝");
            if (resultmap.get("status").toString().equals("0")) {
                return ReturnUtil.returnMap(0, resultmap.get("msg").toString());
            }
        }else{
        	//否则进行更新操作
        	Map<String ,Object> resultmap = taobaoItemUpdateByIdOper(id, channelObj, remark,"更新单个宝贝");
            if (resultmap.get("status").toString().equals("0")) {
                return ReturnUtil.returnMap(0, resultmap.get("msg").toString());
            }
        }
        
        addCount++;
        addAllCount = 1;
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("addAllCount", addAllCount);
        map.put("addCount", addCount);
        map.put("rest", addAllCount-addCount);
        
        return ReturnUtil.returnMap(1, map);
    }
    
    @Override
    public Map<String, Object> addAllTaobaoIdOper(String channel,String remark) {
        
        int addAllCount = 0;
        int addCount = 0;
        
        Channel channelObj = (Channel)commonDao.getObjectByUniqueCode(Constant.channel, Constant.numberCode, channel);
        if (channelObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.channel_not_exist_error);
        }
        
        String appKey = channelObj.getAppKey();
        if (StringTool.isEmpty(appKey)) {
            return ReturnUtil.returnMap(0, ConstantMsg.channel_appKey_not_null);
        }
        String appSecret = channelObj.getAppSecret();
        if (StringTool.isEmpty(appSecret)) {
            return ReturnUtil.returnMap(0, ConstantMsg.channel_app_secret_not_null);
        }
        
        TaobaoClient client = new DefaultTaobaoClient(Constant.taobaoUrl, appKey, appSecret);
        ItemsOnsaleGetRequest req = new ItemsOnsaleGetRequest();
        req.setFields("num_iid,title,price");
        ItemsOnsaleGetResponse rsp = null;
        try {
            rsp = client.execute(req, channelObj.getSessionKey());
        }
        catch (ApiException e) {
            throw new MyExceptionForXyz(e.getMessage());
        }
        String jsonStr = rsp.getBody(); 
        
        @SuppressWarnings("unchecked")
        HashMap<String, Object> responseMap = JSON.toObject(jsonStr, HashMap.class);
        if (responseMap.get("items_onsale_get_response") == null) {
            @SuppressWarnings("unchecked")
            Map<String, Object> errorResponseMap = (Map<String, Object>)responseMap.get("error_response");
            return ReturnUtil.returnMap(0, "接口错误,错误码【"+errorResponseMap.get("msg").toString()+"】错误信息【"+errorResponseMap.get("sub_msg").toString()+"】");
        }
        
        String travelItemIsOnlySql = "select item_id from taobao_travel_item where channel = '"+channelObj.getNumberCode()+"'";
        //本地已有的ITEMID
        @SuppressWarnings("unchecked")
        List<String> travelItemIsOnlyList = commonDao.getSqlQuery(travelItemIsOnlySql).list();
        
        @SuppressWarnings("unchecked")
        HashMap<String, Object> itemsMap = (HashMap<String, Object>)responseMap.get("items_onsale_get_response");
        @SuppressWarnings("unchecked")
        HashMap<String, Object> itemMap = (HashMap<String, Object>)itemsMap.get("items");
        @SuppressWarnings("unchecked")
        List<HashMap<String, Object>> idList = (List<HashMap<String, Object>>)itemMap.get("item");
        addAllCount += idList.size();
        for (HashMap<String, Object> m : idList) {
            Long id = m.get("num_iid")==null?Long.valueOf("0"):Long.valueOf(m.get("num_iid").toString());
            //如果本地有了ITEMID就跳过这个宝贝
            if (travelItemIsOnlyList.contains(id.toString())) {
                continue;
            }
            Map<String, Object> map = taobaoItemByIdOper(id, channelObj, remark,"获取所有宝贝");
            if (map.get("status").toString().equals("0")) {
                return ReturnUtil.returnMap(0, map.get("msg").toString());
            }
            addCount++;
        }
        
        TaobaoClient inventoryClient = new DefaultTaobaoClient(Constant.taobaoUrl, appKey, appSecret);
        ItemsInventoryGetRequest inventoryReq = new ItemsInventoryGetRequest();
        inventoryReq.setFields("pic_url,num,num_iid,title,props,valid_thru");
        ItemsInventoryGetResponse inventoryRsp = null;
        try {
            inventoryRsp = inventoryClient.execute(inventoryReq, channelObj.getSessionKey());
        }
        catch (ApiException e) {
            throw new MyExceptionForXyz(e.getMessage());
        }
        String inventoryRspJsonStr = inventoryRsp.getBody();
        @SuppressWarnings("unchecked")
        HashMap<String, Object> inventoryResponseMap = JSON.toObject(inventoryRspJsonStr, HashMap.class);
        @SuppressWarnings("unchecked")
        HashMap<String, Object> inventoryItemsMap = (HashMap<String, Object>)inventoryResponseMap.get("items_inventory_get_response");
        
        if (inventoryItemsMap == null) {
            @SuppressWarnings("unchecked")
            Map<String, Object> errorResponseMap = (Map<String, Object>)inventoryResponseMap.get("error_response");
            return ReturnUtil.returnMap(0, "接口错误,错误码【"+errorResponseMap.get("msg").toString()+"】错误信息【"+errorResponseMap.get("sub_msg").toString()+"】");
        }
        
        @SuppressWarnings("unchecked")
        HashMap<String, Object> inventoryItemMap = (HashMap<String, Object>)inventoryItemsMap.get("items");
        @SuppressWarnings("unchecked")
        List<HashMap<String, Object>> inventoryIdList = (List<HashMap<String, Object>>)inventoryItemMap.get("item");
        addAllCount += inventoryIdList.size();
        for (HashMap<String, Object> m : inventoryIdList) {
            Long id = m.get("num_iid")==null?Long.valueOf("0"):Long.valueOf(m.get("num_iid").toString());
            if (travelItemIsOnlyList.contains(id.toString())) {
                continue;
            }
            Map<String, Object> map = taobaoItemByIdOper(id, channelObj, remark,"获取所有宝贝");
            if (map.get("status").toString().equals("0")) {
                return ReturnUtil.returnMap(0, map.get("msg").toString());
            }
            addCount++;
        }
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("addAllCount", addAllCount);
        map.put("addCount", addCount);
        map.put("rest", addAllCount-addCount);
        
        return ReturnUtil.returnMap(1, map);
    }
    
    /**
     *〈添加宝贝详细信息〉上面2个方法获取了ID之后调用的方法 (新增)
     *
     * @param id
     * @param channel
     * @param remark
     * @author      叶尼玛
     * @date        ：2016-11-10下午3:36:56
     */
    private Map<String, Object> taobaoItemByIdOper(Long id,Channel channel,String remark,String logRemark) {
        
        String appKey = channel.getAppKey();
        if (!StringTool.isNotNull(appKey)) {
            return ReturnUtil.returnMap(0, ConstantMsg.channel_appKey_not_null);
        }
        String appSecret = channel.getAppSecret();
        if (!StringTool.isNotNull(appSecret)) {
            return ReturnUtil.returnMap(0, ConstantMsg.channel_app_secret_not_null);
        }
        
        TaobaoClient client = new DefaultTaobaoClient(Constant.taobaoUrl, appKey, appSecret);
        AlitripTravelItemSingleQueryRequest req = new AlitripTravelItemSingleQueryRequest();
        req.setItemId(id);
        AlitripTravelItemSingleQueryResponse rsp = null;
        try {
            rsp = client.execute(req, channel.getSessionKey());
        }
        catch (ApiException e) {
            throw new MyExceptionForXyz(e.getMessage());
        }
        String taobaoJsonStr = rsp.getBody();
        @SuppressWarnings("unchecked")
        Map<String, List<HashMap<String, List<HashMap<String, HashMap<String, Object>>>>>> taobaoMap = JSON.toObject(taobaoJsonStr, HashMap.class);
        
        if (taobaoMap.get("error_response") != null) {
            @SuppressWarnings("unchecked")
            Map<String, Object> errorResponseMap = (Map<String, Object>)taobaoMap.get("error_response");
            return ReturnUtil.returnMap(0, "接口错误,错误码【"+errorResponseMap.get("msg")!=null?errorResponseMap.get("msg").toString():""+"】错误信息【"+errorResponseMap.get("sub_msg")!=null?errorResponseMap.get("sub_msg").toString():""+"】");
        }
        
        @SuppressWarnings("unchecked")
        HashMap<String, Object> alitripTravelItemSingleQueryResponseMap = (HashMap<String, Object>)taobaoMap.get("alitrip_travel_item_single_query_response");
    
        @SuppressWarnings("unchecked")
        HashMap<String, Object> travelItemMap = (HashMap<String, Object>)alitripTravelItemSingleQueryResponseMap.get("travel_item");
            
        @SuppressWarnings("unchecked")
        HashMap<String, Object> baseInfoMap = (HashMap<String, Object>)travelItemMap.get("base_info");
        
        @SuppressWarnings("unchecked")
        HashMap<String, List<HashMap<String, Object>>> bookingRulesMap = (HashMap<String, List<HashMap<String, Object>>>)travelItemMap.get("booking_rules");
        
        @SuppressWarnings("unchecked")
        HashMap<String, Object> cruiseItemExt = (HashMap<String, Object>)travelItemMap.get("cruise_item_ext");
        
        @SuppressWarnings("unchecked")
        HashMap<String, Object> saleInfoMap = (HashMap<String, Object>)travelItemMap.get("sale_info");
        
        @SuppressWarnings("unchecked")
        HashMap<String, List<HashMap<String, Object>>> skuInfos = (HashMap<String, List<HashMap<String, Object>>>)travelItemMap.get("sku_infos");
        Date date = new Date();
        
        String created = travelItemMap.get("created")==null?"":travelItemMap.get("created").toString();
        String itemId = travelItemMap.get("item_id")==null?"":travelItemMap.get("item_id").toString();
        String itemStatus = travelItemMap.get("item_status")==null?"":travelItemMap.get("item_status").toString();
        String travelItemType = travelItemMap.get("item_type")==null?"":travelItemMap.get("item_type").toString();
        String modified = travelItemMap.get("modified")==null?"":travelItemMap.get("modified").toString();
        String sellerId = travelItemMap.get("seller_id")==null?"":travelItemMap.get("seller_id").toString();
        String requestId = travelItemMap.get("request_id")==null?"":travelItemMap.get("request_id").toString();
        
        Map<String, Object> itemsMap = new HashMap<String, Object>();
        itemsMap.put("created", created);
        itemsMap.put("item_id", itemId);
        itemsMap.put("item_status", itemStatus);
        itemsMap.put("item_type", travelItemType);
        itemsMap.put("modified", modified);
        itemsMap.put("seller_id", sellerId);
        itemsMap.put("request_id", requestId);
        
        TaobaoTravelItem taobaoTravelItemObj = new TaobaoTravelItem();
        taobaoTravelItemObj.setAddDate(date);
        taobaoTravelItemObj.setAlterDate(date);
        
        taobaoTravelItemObj.setNumberCode(StringUtil.get_new_taobao_item_out_num());
        if (StringTool.isNotNull(created)) {
            taobaoTravelItemObj.setCreated(DateUtil.stringToDate(created));
        }
        taobaoTravelItemObj.setItemId(itemId);
        taobaoTravelItemObj.setItemStatus(Integer.valueOf(itemStatus));
        taobaoTravelItemObj.setItemType(Integer.valueOf(travelItemType));
        if (StringTool.isNotNull(modified)) {
            taobaoTravelItemObj.setModified(DateUtil.stringToDate(modified));
        }
        taobaoTravelItemObj.setSellerId(sellerId);
        taobaoTravelItemObj.setRequestId(requestId);
        taobaoTravelItemObj.setChannel(channel.getNumberCode());
        taobaoTravelItemObj.setChannelNameCn(channel.getNameCn());
        commonDao.save(taobaoTravelItemObj);
        
        //宝贝基础信息
        String accomNights = baseInfoMap.get("accom_nights")==null?"":baseInfoMap.get("accom_nights").toString();
        String categoryId = baseInfoMap.get("category_id")==null?"":baseInfoMap.get("category_id").toString();
        String city = baseInfoMap.get("city")==null?"":baseInfoMap.get("city").toString();
        String desc = baseInfoMap.get("desc")==null?"":baseInfoMap.get("desc").toString();
        String fromLocations = baseInfoMap.get("from_locations")==null?"":baseInfoMap.get("from_locations").toString();
        String itemType = baseInfoMap.get("item_type")==null?"":baseInfoMap.get("item_type").toString();
        String outId = baseInfoMap.get("out_id")==null?"":baseInfoMap.get("out_id").toString();
        String prov = baseInfoMap.get("prov")==null?"":baseInfoMap.get("prov").toString();
        String title = baseInfoMap.get("title")==null?"":baseInfoMap.get("title").toString();
        String toLocations = baseInfoMap.get("to_locations")==null?"":baseInfoMap.get("to_locations").toString();
        String tripMaxDays = baseInfoMap.get("trip_max_days")==null?"":baseInfoMap.get("trip_max_days").toString();
        
        @SuppressWarnings("unchecked")
        HashMap<String, List<String>> picUrlsMap = (HashMap<String, List<String>>)baseInfoMap.get("pic_urls");
        List<String> imagesList = picUrlsMap.get("string");
        
        @SuppressWarnings("unchecked")
        HashMap<String, List<String>> subTitlesMap = (HashMap<String, List<String>>)baseInfoMap.get("sub_titles");
        List<String> subTitlesList = subTitlesMap.get("string");
        
        TaobaoBaseInfo taobaoBaseInfoObj = new TaobaoBaseInfo();
        
        if (!StringTool.isNotNull(outId)) {
            outId = StringUtil.get_new_taobao_base_num();
        }
        taobaoBaseInfoObj.setRemark(remark);
        if (StringTool.isNotNull(accomNights)) {
            taobaoBaseInfoObj.setAccomNights(Integer.valueOf(accomNights));
        }
        taobaoBaseInfoObj.setAddDate(date);
        taobaoBaseInfoObj.setAlterDate(date);
        taobaoBaseInfoObj.setNumberCode(outId);
        taobaoBaseInfoObj.setCategoryId(categoryId);
        taobaoBaseInfoObj.setCity(city);
        taobaoBaseInfoObj.setDesc(desc);
        taobaoBaseInfoObj.setFromLocations(fromLocations);
        taobaoBaseInfoObj.setItemType(Integer.valueOf(itemType));
        taobaoBaseInfoObj.setNumberCode(StringUtil.get_new_taobao_item_out_num());
        taobaoBaseInfoObj.setPicUrls(StringTool.listToString(imagesList, ","));
        taobaoBaseInfoObj.setProv(prov);
        taobaoBaseInfoObj.setItemUrl("https://items.alitrip.com/item.htm?id="+id.toString());
        String subTitiles = "";
        if (subTitlesList != null && subTitlesList.size() > 0) {
            subTitiles = StringTool.listToString(subTitlesList, ",");
        }
        taobaoBaseInfoObj.setSubTitles(subTitiles);
        taobaoBaseInfoObj.setTitle(title);
        taobaoBaseInfoObj.setTaobaoTravelItem(taobaoTravelItemObj.getNumberCode());
        taobaoBaseInfoObj.setToLocations(toLocations);
        if (StringTool.isNotNull(tripMaxDays)) {
            taobaoBaseInfoObj.setTripMaxDays(Integer.valueOf(tripMaxDays));
        }
        commonDao.save(taobaoBaseInfoObj);
        
        //规则
        List<HashMap<String, Object>> pontusRravelBookingRuleInfoMap = bookingRulesMap.get("pontus_travel_booking_rule_info");
        if (pontusRravelBookingRuleInfoMap != null) {
            for (HashMap<String, Object> rule : pontusRravelBookingRuleInfoMap) {
                String ruleDesc = rule.get("rule_desc")==null?"":rule.get("rule_desc").toString();
                String ruleType = rule.get("rule_type")==null?"":rule.get("rule_type").toString();
                
                TaobaoBookingRule taobaoBookingRulesObj = new TaobaoBookingRule();
                taobaoBookingRulesObj.setNumberCode(UUIDUtil.getUUIDStringFor32());
                taobaoBookingRulesObj.setAddDate(date);
                taobaoBookingRulesObj.setAlterDate(date);
                taobaoBookingRulesObj.setRuleDesc(ruleDesc);
                taobaoBookingRulesObj.setRuleType(ruleType);
                taobaoBookingRulesObj.setTaobaoTravelItem(taobaoTravelItemObj.getNumberCode());
                commonDao.save(taobaoBookingRulesObj);
                
            }
        }
        
        if (cruiseItemExt != null) {
          //邮轮基础信息
            String cruiseCompany = cruiseItemExt.get("cruise_company")==null?"":cruiseItemExt.get("cruise_company").toString();
            String cruiseLine = cruiseItemExt.get("cruise_line")==null?"":cruiseItemExt.get("cruise_line").toString();
            String shipDown = cruiseItemExt.get("ship_down")==null?"":cruiseItemExt.get("ship_down").toString();
            String shipFeeInclude = cruiseItemExt.get("ship_fee_include")==null?"":cruiseItemExt.get("ship_fee_include").toString();
            String shipName = cruiseItemExt.get("ship_name")==null?"":cruiseItemExt.get("ship_name").toString();
            String shipUp = cruiseItemExt.get("ship_up")==null?"":cruiseItemExt.get("ship_up").toString();
            
            TaobaoCruiseItemExt taobaoCruiseItemExtObj = new TaobaoCruiseItemExt();
            taobaoCruiseItemExtObj.setAddDate(date);
            taobaoCruiseItemExtObj.setAlterDate(date);
            taobaoCruiseItemExtObj.setCruiseCompany(cruiseCompany);
            taobaoCruiseItemExtObj.setCruiseLine(cruiseLine);
            taobaoCruiseItemExtObj.setNumberCode(UUIDUtil.getUUIDStringFor32());
            taobaoCruiseItemExtObj.setShipDown(shipDown);
            taobaoCruiseItemExtObj.setShipFeeInclude(shipFeeInclude);
            taobaoCruiseItemExtObj.setShipName(shipName);
            taobaoCruiseItemExtObj.setShipUp(shipUp);
            taobaoCruiseItemExtObj.setTaobaoTravelItem(taobaoTravelItemObj.getNumberCode());
            commonDao.save(taobaoCruiseItemExtObj);
            
        }
        
        //销售相关
        String duration = saleInfoMap.get("duration")==null?"":saleInfoMap.get("duration").toString();
        String endComboDate = saleInfoMap.get("end_combo_date")==null?"":saleInfoMap.get("end_combo_date").toString();
        String hasDiscount = saleInfoMap.get("has_discount")==null?"":saleInfoMap.get("has_discount").toString();
        String hasInvoice = saleInfoMap.get("has_invoice")==null?"":saleInfoMap.get("has_invoice").toString();
        String hasShowcase = saleInfoMap.get("has_showcase")==null?"":saleInfoMap.get("has_showcase").toString();
        String merchant = saleInfoMap.get("merchant")==null?"":saleInfoMap.get("merchant").toString();
        String networkId = saleInfoMap.get("network_id")==null?"":saleInfoMap.get("network_id").toString();
        String saleType = saleInfoMap.get("sale_type")==null?"":saleInfoMap.get("sale_type").toString();
        String startComboDate = saleInfoMap.get("start_combo_date")==null?"":saleInfoMap.get("start_combo_date").toString();
        String subStock = saleInfoMap.get("sub_stock")==null?"":saleInfoMap.get("sub_stock").toString();
        String supportOnsaleAutoRefund = saleInfoMap.get("support_onsale_auto_refund")==null?"":saleInfoMap.get("support_onsale_auto_refund").toString();
        
        @SuppressWarnings("unchecked")
        HashMap<String, List<String>> sellerCidsMap = (HashMap<String, List<String>>)saleInfoMap.get("seller_cids");
        List<String> sellerCidsList = sellerCidsMap.get("string");
        
        TaobaoSaleInfo taobaoSaleInfoObj = new TaobaoSaleInfo();
        taobaoSaleInfoObj.setAddDate(date);
        taobaoSaleInfoObj.setAlterDate(date);
        taobaoSaleInfoObj.setDuration(Integer.valueOf(duration));
        if (StringTool.isNotNull(endComboDate)) {
            taobaoSaleInfoObj.setEndComboDate(DateUtil.stringToDate(endComboDate));
        }
        taobaoSaleInfoObj.setHasDiscount(hasDiscount);
        taobaoSaleInfoObj.setHasInvoice(hasInvoice);
        taobaoSaleInfoObj.setHasShowcase(hasShowcase);
        taobaoSaleInfoObj.setMerchant(merchant);
        taobaoSaleInfoObj.setNetworkId(networkId);
        taobaoSaleInfoObj.setNumberCode(UUIDUtil.getUUIDStringFor32());
        taobaoSaleInfoObj.setSaleType(Integer.valueOf(saleType));
        taobaoSaleInfoObj.setTaobaoTravelItem(taobaoTravelItemObj.getNumberCode());
        if (sellerCidsList != null) {
            taobaoSaleInfoObj.setSellerCids(StringTool.listToString(sellerCidsList, ","));
        }
        if (StringTool.isNotNull(startComboDate)) {
            taobaoSaleInfoObj.setStartComboDate(DateUtil.stringToDate(startComboDate));
        }
        taobaoSaleInfoObj.setSubStock(Integer.valueOf(subStock));
        taobaoSaleInfoObj.setSupportOnsaleAutoRefund(supportOnsaleAutoRefund);
        commonDao.save(taobaoSaleInfoObj);
        
        //SKU
        List<HashMap<String, Object>> pontusTravelItemSkuInfoMap = skuInfos.get("pontus_travel_item_sku_info");
        if (pontusTravelItemSkuInfoMap != null) {
            for (HashMap<String, Object> skuInfo : pontusTravelItemSkuInfoMap) {
                String outerSkuId = skuInfo.get("outer_sku_id")==null?"":skuInfo.get("outer_sku_id").toString();
                String packageName = skuInfo.get("package_name")==null?"":skuInfo.get("package_name").toString();
                @SuppressWarnings("unchecked")
                HashMap<String, List<HashMap<String, Object>>> pricesMap = (HashMap<String, List<HashMap<String, Object>>>)skuInfo.get("prices");
                List<HashMap<String, Object>> pontusList = pricesMap.get("pontus_travel_prices");
                
                TaobaoSkuInfo taobaoSkuInfoObj = new TaobaoSkuInfo();
                taobaoSkuInfoObj.setAddDate(date);
                taobaoSkuInfoObj.setAlterDate(date);
                if (!StringTool.isNotNull(outerSkuId)) {
                    outerSkuId = StringUtil.get_new_taobao_sku_out_num();
                }
                taobaoSkuInfoObj.setNumberCode(outerSkuId);
                taobaoSkuInfoObj.setPackageName(packageName);
                taobaoSkuInfoObj.setTaobaoTravelItem(taobaoTravelItemObj.getNumberCode());
                taobaoSkuInfoObj.setIsLock(1);   //是否允许修改套餐名称(0:允许)
                commonDao.save(taobaoSkuInfoObj);
                
                //删除之前的关联脏数据
                @SuppressWarnings("unchecked")
				List<TaobaoSkuInfoDetail> details=commonDao.queryByHql("from TaobaoSkuInfoDetail where taobaoSkuInfo='"+outerSkuId+"'");
                for(TaobaoSkuInfoDetail detail:details){
                	commonDao.delete(detail);
                }
                
                for (HashMap<String, Object> p : pontusList) {
                    BigDecimal price = p.get("price") == null ? BigDecimal.ZERO : new BigDecimal(p.get("price").toString());
                    
                    String DetailsDate = p.get("date")==null?"":p.get("date").toString();
                    String DetailsPriceType = p.get("price_type")==null?"":p.get("price_type").toString();
                    String DetailsStock = p.get("stock")==null?"":p.get("stock").toString();
                    
                    TaobaoSkuInfoDetail taobaoSkuInfoDetailObj = new TaobaoSkuInfoDetail();
                    taobaoSkuInfoDetailObj.setAddDate(date);
                    taobaoSkuInfoDetailObj.setAlterDate(date);
                    if (StringTool.isNotNull(DetailsDate)) {
                        taobaoSkuInfoDetailObj.setDate(DateUtil.stringToDate(DetailsDate));
                    }
                    taobaoSkuInfoDetailObj.setFlag(0);
                    taobaoSkuInfoDetailObj.setNumberCode(StringUtil.get_new_taobao_price_out_num());
                    taobaoSkuInfoDetailObj.setPrice(price);
                    taobaoSkuInfoDetailObj.setPriceType(Integer.valueOf(DetailsPriceType));
                    if (StringTool.isNotNull(DetailsStock)) {
                        taobaoSkuInfoDetailObj.setStock(Integer.valueOf(DetailsStock));
                    }
                    taobaoSkuInfoDetailObj.setTaobaoSkuInfo(taobaoSkuInfoObj.getNumberCode());
                    commonDao.save(taobaoSkuInfoDetailObj);
                }
            }
        }
        
        TaobaoLogWork baseLogWork = new TaobaoLogWork();
        baseLogWork.setAddDate(new Date());
        baseLogWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
        baseLogWork.setValue(taobaoBaseInfoObj.getNumberCode());
        baseLogWork.setRemark("【"+logRemark+"】添加宝贝【"+taobaoBaseInfoObj.getTitle()+"】");
        baseLogWork.setTableName("baseTable");
        baseLogWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
        commonDao.save(baseLogWork);
        
        return ReturnUtil.returnMap(1, null);
    }
    
    /**
     * 获取/更新宝贝--更新
     */
    private Map<String, Object> taobaoItemUpdateByIdOper(Long id,Channel channel,String remark,String logRemark) {
        
        String appKey = channel.getAppKey();
        if (!StringTool.isNotNull(appKey)) {
            return ReturnUtil.returnMap(0, ConstantMsg.channel_appKey_not_null);
        }
        String appSecret = channel.getAppSecret();
        if (!StringTool.isNotNull(appSecret)) {
            return ReturnUtil.returnMap(0, ConstantMsg.channel_app_secret_not_null);
        }
        
        TaobaoClient client = new DefaultTaobaoClient(Constant.taobaoUrl, appKey, appSecret);
        AlitripTravelItemSingleQueryRequest req = new AlitripTravelItemSingleQueryRequest();
        req.setItemId(id);
        AlitripTravelItemSingleQueryResponse rsp = null;
        try {
            rsp = client.execute(req, channel.getSessionKey());
        }catch (ApiException e) {
            throw new MyExceptionForXyz(e.getMessage());
        }
        String taobaoJsonStr = rsp.getBody();
        System.out.println("=="+taobaoJsonStr);
        @SuppressWarnings("unchecked")
        Map<String, List<HashMap<String, List<HashMap<String, HashMap<String, Object>>>>>> taobaoMap = JSON.toObject(taobaoJsonStr, HashMap.class);
        
        if (taobaoMap.get("error_response") != null) {
            @SuppressWarnings("unchecked")
            Map<String, Object> errorResponseMap = (Map<String, Object>)taobaoMap.get("error_response");
            return ReturnUtil.returnMap(0, "接口错误,错误码【"+errorResponseMap.get("msg")!=null?errorResponseMap.get("msg").toString():""+"】错误信息【"+errorResponseMap.get("sub_msg")!=null?errorResponseMap.get("sub_msg").toString():""+"】");
        }
        
        @SuppressWarnings("unchecked")
        HashMap<String, Object> alitripTravelItemSingleQueryResponseMap = (HashMap<String, Object>)taobaoMap.get("alitrip_travel_item_single_query_response");
    
        @SuppressWarnings("unchecked")
        HashMap<String, Object> travelItemMap = (HashMap<String, Object>)alitripTravelItemSingleQueryResponseMap.get("travel_item");
            
        @SuppressWarnings("unchecked")
        HashMap<String, Object> baseInfoMap = (HashMap<String, Object>)travelItemMap.get("base_info");
        
        @SuppressWarnings("unchecked")
        HashMap<String, List<HashMap<String, Object>>> bookingRulesMap = (HashMap<String, List<HashMap<String, Object>>>)travelItemMap.get("booking_rules");
        
        @SuppressWarnings("unchecked")
        HashMap<String, Object> cruiseItemExt = (HashMap<String, Object>)travelItemMap.get("cruise_item_ext");
        
        @SuppressWarnings("unchecked")
        HashMap<String, Object> saleInfoMap = (HashMap<String, Object>)travelItemMap.get("sale_info");
        
        @SuppressWarnings("unchecked")
        HashMap<String, List<HashMap<String, Object>>> skuInfos = (HashMap<String, List<HashMap<String, Object>>>)travelItemMap.get("sku_infos");
        Date date = new Date();
        
        String created = travelItemMap.get("created")==null?"":travelItemMap.get("created").toString();
        String itemId = travelItemMap.get("item_id")==null?"":travelItemMap.get("item_id").toString();
        String itemStatus = travelItemMap.get("item_status")==null?"":travelItemMap.get("item_status").toString();
        String travelItemType = travelItemMap.get("item_type")==null?"":travelItemMap.get("item_type").toString();
        String modified = travelItemMap.get("modified")==null?"":travelItemMap.get("modified").toString();
        String sellerId = travelItemMap.get("seller_id")==null?"":travelItemMap.get("seller_id").toString();
        String requestId = travelItemMap.get("request_id")==null?"":travelItemMap.get("request_id").toString();
        
        Map<String, Object> itemsMap = new HashMap<String, Object>();
        itemsMap.put("created", created);
        itemsMap.put("item_id", itemId);
        itemsMap.put("item_status", itemStatus);
        itemsMap.put("item_type", travelItemType);
        itemsMap.put("modified", modified);
        itemsMap.put("seller_id", sellerId);
        itemsMap.put("request_id", requestId);
        
        String hql="from TaobaoTravelItem where channel='"+channel.getNumberCode()+"' and itemId='"+itemId+"'";
        
        TaobaoTravelItem taobaoTravelItemObj = (TaobaoTravelItem) commonDao.queryUniqueByHql(hql);
        if(taobaoTravelItemObj==null){
        	  return ReturnUtil.returnMap(0, ConstantMsg.taobao_travel_item_not_exist_error);
        }

        taobaoTravelItemObj.setAlterDate(date);
        if (StringTool.isNotNull(created)) {
            taobaoTravelItemObj.setCreated(DateUtil.stringToDate(created));
        }
        taobaoTravelItemObj.setItemId(itemId);
        taobaoTravelItemObj.setItemStatus(Integer.valueOf(itemStatus));
        taobaoTravelItemObj.setItemType(Integer.valueOf(travelItemType));
        if (StringTool.isNotNull(modified)) {
            taobaoTravelItemObj.setModified(DateUtil.stringToDate(modified));
        }
        taobaoTravelItemObj.setSellerId(sellerId);
        taobaoTravelItemObj.setRequestId(requestId);
        taobaoTravelItemObj.setChannel(channel.getNumberCode());
        taobaoTravelItemObj.setChannelNameCn(channel.getNameCn());
        commonDao.update(taobaoTravelItemObj);
        
        //宝贝基础信息
        String accomNights = baseInfoMap.get("accom_nights")==null?"":baseInfoMap.get("accom_nights").toString();
        String categoryId = baseInfoMap.get("category_id")==null?"":baseInfoMap.get("category_id").toString();
        String city = baseInfoMap.get("city")==null?"":baseInfoMap.get("city").toString();
        String desc = baseInfoMap.get("desc")==null?"":baseInfoMap.get("desc").toString();
        String fromLocations = baseInfoMap.get("from_locations")==null?"":baseInfoMap.get("from_locations").toString();
        String itemType = baseInfoMap.get("item_type")==null?"":baseInfoMap.get("item_type").toString();
        String outId = baseInfoMap.get("out_id")==null?"":baseInfoMap.get("out_id").toString();
        String prov = baseInfoMap.get("prov")==null?"":baseInfoMap.get("prov").toString();
        String title = baseInfoMap.get("title")==null?"":baseInfoMap.get("title").toString();
        String toLocations = baseInfoMap.get("to_locations")==null?"":baseInfoMap.get("to_locations").toString();
        String tripMaxDays = baseInfoMap.get("trip_max_days")==null?"":baseInfoMap.get("trip_max_days").toString();
        
        @SuppressWarnings("unchecked")
        HashMap<String, List<String>> picUrlsMap = (HashMap<String, List<String>>)baseInfoMap.get("pic_urls");
        List<String> imagesList = picUrlsMap.get("string");
        
        @SuppressWarnings("unchecked")
        HashMap<String, List<String>> subTitlesMap = (HashMap<String, List<String>>)baseInfoMap.get("sub_titles");
        List<String> subTitlesList = subTitlesMap.get("string");
        
        TaobaoBaseInfo taobaoBaseInfoObj =  (TaobaoBaseInfo) commonDao.getObjectByUniqueCode("TaobaoBaseInfo", "taobaoTravelItem", taobaoTravelItemObj.getNumberCode());
        if(taobaoBaseInfoObj==null){
        	taobaoBaseInfoObj=new TaobaoBaseInfo();
        }
        
        if (!StringTool.isNotNull(outId)) {
            outId = StringUtil.get_new_taobao_base_num();
        }
        taobaoBaseInfoObj.setRemark(remark);
        if (StringTool.isNotNull(accomNights)) {
            taobaoBaseInfoObj.setAccomNights(Integer.valueOf(accomNights));
        }
        taobaoBaseInfoObj.setAlterDate(date);
        taobaoBaseInfoObj.setNumberCode(outId);
        taobaoBaseInfoObj.setCategoryId(categoryId);
        taobaoBaseInfoObj.setCity(city);
        taobaoBaseInfoObj.setDesc(desc);
        taobaoBaseInfoObj.setFromLocations(fromLocations);
        taobaoBaseInfoObj.setItemType(Integer.valueOf(itemType));
        taobaoBaseInfoObj.setNumberCode(StringUtil.get_new_taobao_item_out_num());
        taobaoBaseInfoObj.setPicUrls(StringTool.listToString(imagesList, ","));
        taobaoBaseInfoObj.setProv(prov);
        taobaoBaseInfoObj.setItemUrl("https://items.alitrip.com/item.htm?id="+id.toString());
        String subTitiles = "";
        if (subTitlesList != null && subTitlesList.size() > 0) {
            subTitiles = StringTool.listToString(subTitlesList, ",");
        }
        taobaoBaseInfoObj.setSubTitles(subTitiles);
        taobaoBaseInfoObj.setTitle(title);
        taobaoBaseInfoObj.setTaobaoTravelItem(taobaoTravelItemObj.getNumberCode());
        taobaoBaseInfoObj.setToLocations(toLocations);
        if (StringTool.isNotNull(tripMaxDays)) {
            taobaoBaseInfoObj.setTripMaxDays(Integer.valueOf(tripMaxDays));
        }
        commonDao.update(taobaoBaseInfoObj);
        
        //规则
        List<HashMap<String, Object>> pontusRravelBookingRuleInfoMap = bookingRulesMap.get("pontus_travel_booking_rule_info");
        
        if (pontusRravelBookingRuleInfoMap != null) {
        	// 清除之前的数据
            String ruleDelete = "DELETE FROM taobao_booking_rule WHERE taobao_travel_item = '"+ taobaoTravelItemObj.getNumberCode() +"'";
            commonDao.getSqlQuery(ruleDelete).executeUpdate();
        	
            for (HashMap<String, Object> rule : pontusRravelBookingRuleInfoMap) {
                String ruleDesc = rule.get("rule_desc")==null?"":rule.get("rule_desc").toString();
                String ruleType = rule.get("rule_type")==null?"":rule.get("rule_type").toString();
               
                TaobaoBookingRule taobaoBookingRulesObj = new TaobaoBookingRule();
                taobaoBookingRulesObj.setNumberCode(UUIDUtil.getUUIDStringFor32());
                taobaoBookingRulesObj.setAddDate(date);
                taobaoBookingRulesObj.setAlterDate(date);
                taobaoBookingRulesObj.setRuleDesc(ruleDesc);
                taobaoBookingRulesObj.setRuleType(ruleType);
                taobaoBookingRulesObj.setTaobaoTravelItem(taobaoTravelItemObj.getNumberCode());
                commonDao.save(taobaoBookingRulesObj);
                
            }
        }
        
        if (cruiseItemExt != null) {
          //邮轮基础信息
            String cruiseCompany = cruiseItemExt.get("cruise_company")==null?"":cruiseItemExt.get("cruise_company").toString();
            String cruiseLine = cruiseItemExt.get("cruise_line")==null?"":cruiseItemExt.get("cruise_line").toString();
            String shipDown = cruiseItemExt.get("ship_down")==null?"":cruiseItemExt.get("ship_down").toString();
            String shipFeeInclude = cruiseItemExt.get("ship_fee_include")==null?"":cruiseItemExt.get("ship_fee_include").toString();
            String shipName = cruiseItemExt.get("ship_name")==null?"":cruiseItemExt.get("ship_name").toString();
            String shipUp = cruiseItemExt.get("ship_up")==null?"":cruiseItemExt.get("ship_up").toString();
            
            TaobaoCruiseItemExt taobaoCruiseItemExtObj =  (TaobaoCruiseItemExt) commonDao.getObjectByUniqueCode("TaobaoCruiseItemExt", "taobaoTravelItem", taobaoTravelItemObj.getNumberCode());
            if(taobaoCruiseItemExtObj==null){
            	taobaoCruiseItemExtObj = new TaobaoCruiseItemExt();
            }
            taobaoCruiseItemExtObj.setAddDate(date);
            taobaoCruiseItemExtObj.setAlterDate(date);
            taobaoCruiseItemExtObj.setCruiseCompany(cruiseCompany);
            taobaoCruiseItemExtObj.setCruiseLine(cruiseLine);
            taobaoCruiseItemExtObj.setShipDown(shipDown);
            taobaoCruiseItemExtObj.setShipFeeInclude(shipFeeInclude);
            taobaoCruiseItemExtObj.setShipName(shipName);
            taobaoCruiseItemExtObj.setShipUp(shipUp);
            taobaoCruiseItemExtObj.setTaobaoTravelItem(taobaoTravelItemObj.getNumberCode());
            commonDao.update(taobaoCruiseItemExtObj);
            
        }
        
        //销售相关
        String duration = saleInfoMap.get("duration")==null?"":saleInfoMap.get("duration").toString();
        String endComboDate = saleInfoMap.get("end_combo_date")==null?"":saleInfoMap.get("end_combo_date").toString();
        String hasDiscount = saleInfoMap.get("has_discount")==null?"":saleInfoMap.get("has_discount").toString();
        String hasInvoice = saleInfoMap.get("has_invoice")==null?"":saleInfoMap.get("has_invoice").toString();
        String hasShowcase = saleInfoMap.get("has_showcase")==null?"":saleInfoMap.get("has_showcase").toString();
        String merchant = saleInfoMap.get("merchant")==null?"":saleInfoMap.get("merchant").toString();
        String networkId = saleInfoMap.get("network_id")==null?"":saleInfoMap.get("network_id").toString();
        String saleType = saleInfoMap.get("sale_type")==null?"":saleInfoMap.get("sale_type").toString();
        String startComboDate = saleInfoMap.get("start_combo_date")==null?"":saleInfoMap.get("start_combo_date").toString();
        String subStock = saleInfoMap.get("sub_stock")==null?"":saleInfoMap.get("sub_stock").toString();
        String supportOnsaleAutoRefund = saleInfoMap.get("support_onsale_auto_refund")==null?"":saleInfoMap.get("support_onsale_auto_refund").toString();
        
        @SuppressWarnings("unchecked")
        HashMap<String, List<String>> sellerCidsMap = (HashMap<String, List<String>>)saleInfoMap.get("seller_cids");
        List<String> sellerCidsList = sellerCidsMap.get("string");
        
        TaobaoSaleInfo taobaoSaleInfoObj =  (TaobaoSaleInfo) commonDao.getObjectByUniqueCode("TaobaoSaleInfo", "taobaoTravelItem", taobaoTravelItemObj.getNumberCode());
        if(taobaoSaleInfoObj==null){
            taobaoSaleInfoObj = new TaobaoSaleInfo();
        }
        taobaoSaleInfoObj.setAlterDate(date);
        taobaoSaleInfoObj.setDuration(Integer.valueOf(duration));
        if (StringTool.isNotNull(endComboDate)) {
            taobaoSaleInfoObj.setEndComboDate(DateUtil.stringToDate(endComboDate));
        }
        taobaoSaleInfoObj.setHasDiscount(hasDiscount);
        taobaoSaleInfoObj.setHasInvoice(hasInvoice);
        taobaoSaleInfoObj.setHasShowcase(hasShowcase);
        taobaoSaleInfoObj.setMerchant(merchant);
        taobaoSaleInfoObj.setNetworkId(networkId);
        taobaoSaleInfoObj.setSaleType(Integer.valueOf(saleType));
        taobaoSaleInfoObj.setTaobaoTravelItem(taobaoTravelItemObj.getNumberCode());
        if (sellerCidsList != null) {
            taobaoSaleInfoObj.setSellerCids(StringTool.listToString(sellerCidsList, ","));
        }
        if (StringTool.isNotNull(startComboDate)) {
            taobaoSaleInfoObj.setStartComboDate(DateUtil.stringToDate(startComboDate));
        }
        taobaoSaleInfoObj.setSubStock(Integer.valueOf(subStock));
        taobaoSaleInfoObj.setSupportOnsaleAutoRefund(supportOnsaleAutoRefund);
        commonDao.update(taobaoSaleInfoObj);
        
        //SKU
        List<HashMap<String, Object>> pontusTravelItemSkuInfoMap = skuInfos.get("pontus_travel_item_sku_info");
        hql = "from TaobaoSkuInfo where taobaoTravelItem='"+taobaoTravelItemObj.getNumberCode()+"'";
        @SuppressWarnings("unchecked")
		List<TaobaoSkuInfo> skuInfosList=commonDao.queryByHql(hql);
        
        if (pontusTravelItemSkuInfoMap != null) {
            for (HashMap<String, Object> skuInfo : pontusTravelItemSkuInfoMap) {
                String outerSkuId = skuInfo.get("outer_sku_id")==null?"":skuInfo.get("outer_sku_id").toString();
                String packageName = skuInfo.get("package_name")==null?"":skuInfo.get("package_name").toString();
                @SuppressWarnings("unchecked")
                HashMap<String, List<HashMap<String, Object>>> pricesMap = (HashMap<String, List<HashMap<String, Object>>>)skuInfo.get("prices");
                List<HashMap<String, Object>> pontusList = pricesMap.get("pontus_travel_prices");
                
                boolean flag=false;
                for(TaobaoSkuInfo taobaoSkuInfoObj:skuInfosList){
                	if(StringTool.isNotNull(packageName)&&taobaoSkuInfoObj.getPackageName().equals(packageName)){
                		//更新价格信息
                		if(taobaoSkuInfoObj.getPackageName().equals(packageName)){
                			taobaoSkuInfoObj.setAlterDate(date);
                    		taobaoSkuInfoObj.setIsLock(1);  //是否固定SKU
                    		commonDao.update(taobaoSkuInfoObj);
                			flag=true;
                            hql = "from TaobaoSkuInfoDetail where taobaoSkuInfo='"+taobaoSkuInfoObj.getNumberCode()+"'";
                            @SuppressWarnings("unchecked")
                    		List<TaobaoSkuInfoDetail> details=commonDao.queryByHql(hql);
                            
                            for (HashMap<String, Object> p : pontusList) {
                                BigDecimal price = p.get("price") == null ? BigDecimal.ZERO : new BigDecimal(p.get("price").toString());
                                String DetailsDate = p.get("date")==null?"":p.get("date").toString();
                                String DetailsPriceType = p.get("price_type")==null?"":p.get("price_type").toString();
                                String DetailsStock = p.get("stock")==null?"":p.get("stock").toString();
                                
                                boolean detailFlag=false;
                                for(TaobaoSkuInfoDetail detail:details){
                                	 if (StringTool.isNotNull(DetailsDate)&&DetailsDate.substring(0, 10).equals(DateUtil.dateToShortString(detail.getDate()))) {
                                		 detail.setAlterDate(date);
                                		 detail.setPrice(price);
                                		 detail.setPriceType(Integer.valueOf(DetailsPriceType));
                            	         if (StringTool.isNotNull(DetailsStock)) {
                            	        	 detail.setStock(Integer.valueOf(DetailsStock));
                                         }
                            	         commonDao.update(detail);
                            	         detailFlag=true;
                            	         break;
                                	 }
                                }
                                
                                if(!detailFlag){
                                	TaobaoSkuInfoDetail taobaoSkuInfoDetailObj = new TaobaoSkuInfoDetail();
                                    taobaoSkuInfoDetailObj.setAddDate(date);
                                    taobaoSkuInfoDetailObj.setAlterDate(date);
                                    if (StringTool.isNotNull(DetailsDate)) {
                                        taobaoSkuInfoDetailObj.setDate(DateUtil.stringToDate(DetailsDate));
                                    }
                                    taobaoSkuInfoDetailObj.setFlag(0);
                                    taobaoSkuInfoDetailObj.setNumberCode(StringUtil.get_new_taobao_price_out_num());
                                    taobaoSkuInfoDetailObj.setPrice(price);
                                    taobaoSkuInfoDetailObj.setPriceType(Integer.valueOf(DetailsPriceType));
                                    if (StringTool.isNotNull(DetailsStock)) {
                                        taobaoSkuInfoDetailObj.setStock(Integer.valueOf(DetailsStock));
                                    }
                                    taobaoSkuInfoDetailObj.setTaobaoSkuInfo(taobaoSkuInfoObj.getNumberCode());
                                    commonDao.save(taobaoSkuInfoDetailObj);
                                    
                                    TaobaoLogWork detailLogWork = new TaobaoLogWork();
                                    detailLogWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
                                    detailLogWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
                                    detailLogWork.setTableName("skuDetailTable");
                                    detailLogWork.setValue(taobaoSkuInfoDetailObj.getNumberCode());
                                    detailLogWork.setRemark("更新SKU套餐【"+ packageName +"】下的日历库存【"+ taobaoSkuInfoDetailObj.getDate() +"】");
                                    detailLogWork.setAddDate(date);
                                    commonDao.save(detailLogWork);
                                }
                                
                            }
                            
                            //清楚多余下架的SKU
                            for(TaobaoSkuInfoDetail detail:details){
                            	boolean deleteFlag=false;
                            	  for (HashMap<String, Object> p : pontusList) {
                                     String DetailsDate = p.get("date")==null?"":p.get("date").toString();
                                 	 if (StringTool.isNotNull(DetailsDate)&&DetailsDate.substring(0, 10).equals(DateUtil.dateToShortString(detail.getDate()))) {
                                 		deleteFlag=true;
                                 		break;
                                 	 }
                            	  }
                            	  if(!deleteFlag){
                            		  commonDao.delete(detail);
                            	  }
                           }
                            
                		}
                		break;
                	}
                }
                if(!flag){
                	//新增SKU数据
         			TaobaoSkuInfo taobaoSkuInfoObj = new TaobaoSkuInfo();
                    taobaoSkuInfoObj.setAddDate(date);
                    taobaoSkuInfoObj.setAlterDate(date);
                    if (!StringTool.isNotNull(outerSkuId)) {
                          outerSkuId = StringUtil.get_new_taobao_sku_out_num();
                    }
                    taobaoSkuInfoObj.setNumberCode(outerSkuId);
                    taobaoSkuInfoObj.setPackageName(packageName);
                    taobaoSkuInfoObj.setTaobaoTravelItem(taobaoTravelItemObj.getNumberCode());
                    taobaoSkuInfoObj.setIsLock(0);
                    commonDao.save(taobaoSkuInfoObj);
                    
                    TaobaoLogWork skuLogWork = new TaobaoLogWork();
                    skuLogWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
                    skuLogWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
                    skuLogWork.setTableName("skuTable");
                    skuLogWork.setValue(taobaoSkuInfoObj.getNumberCode());
                    skuLogWork.setRemark("获取【"+ taobaoSkuInfoObj.getPackageName() +"】SKU套餐");
                    skuLogWork.setAddDate(date);
                    commonDao.save(skuLogWork);
                      
                  for (HashMap<String, Object> p : pontusList) {
                      BigDecimal price = p.get("price") == null ? BigDecimal.ZERO : new BigDecimal(p.get("price").toString());
                      String DetailsDate = p.get("date")==null?"":p.get("date").toString();
                      String DetailsPriceType = p.get("price_type")==null?"":p.get("price_type").toString();
                      String DetailsStock = p.get("stock")==null?"":p.get("stock").toString();
                      
                      TaobaoSkuInfoDetail taobaoSkuInfoDetailObj = new TaobaoSkuInfoDetail();
                      taobaoSkuInfoDetailObj.setAddDate(date);
                      taobaoSkuInfoDetailObj.setAlterDate(date);
                      if (StringTool.isNotNull(DetailsDate)) {
                          taobaoSkuInfoDetailObj.setDate(DateUtil.stringToDate(DetailsDate));
                      }
                      taobaoSkuInfoDetailObj.setFlag(0);
                      taobaoSkuInfoDetailObj.setNumberCode(StringUtil.get_new_taobao_price_out_num());
                      taobaoSkuInfoDetailObj.setPrice(price);
                      taobaoSkuInfoDetailObj.setPriceType(Integer.valueOf(DetailsPriceType));
                      if (StringTool.isNotNull(DetailsStock)) {
                          taobaoSkuInfoDetailObj.setStock(Integer.valueOf(DetailsStock));
                      }
                      taobaoSkuInfoDetailObj.setTaobaoSkuInfo(taobaoSkuInfoObj.getNumberCode());
                      commonDao.save(taobaoSkuInfoDetailObj);
                      
                      TaobaoLogWork detailLogWork = new TaobaoLogWork();
                      detailLogWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
                      detailLogWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
                      detailLogWork.setTableName("detailTable");
                      detailLogWork.setValue(taobaoSkuInfoObj.getNumberCode());
                      detailLogWork.setRemark("获取SKU套餐【"+ taobaoSkuInfoObj.getPackageName() +"】下的日历库存【"+ taobaoSkuInfoDetailObj.getDate() +"】");
                      detailLogWork.setAddDate(date);
                      commonDao.save(detailLogWork);
                  }
               }
            	
            }
            
            //清除本地多余SKU数据
            for(TaobaoSkuInfo taobaoSkuInfoObj:skuInfosList){
            	boolean flag=false;
            	for (HashMap<String, Object> skuInfo : pontusTravelItemSkuInfoMap) {
                    String packageName = skuInfo.get("package_name")==null?"":skuInfo.get("package_name").toString();
                	if(StringTool.isNotNull(packageName)&&taobaoSkuInfoObj.getPackageName().equals(packageName)){
                		flag=true;
                		break;
                	}
            	}
            	if(!flag){
            		@SuppressWarnings("unchecked")
					List<TaobaoSkuInfoDetail> details=commonDao.queryByHql("from TaobaoSkuInfoDetail where taobaoSkuInfo='"+taobaoSkuInfoObj.getNumberCode()+"'");
            		for(TaobaoSkuInfoDetail detail:details){
            			commonDao.delete(detail);
            		}
            		commonDao.delete(taobaoSkuInfoObj);
            	}
            }
        }
        
        TaobaoLogWork baseLogWork = new TaobaoLogWork();
        baseLogWork.setAddDate(new Date());
        baseLogWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
        baseLogWork.setValue(taobaoBaseInfoObj.getNumberCode());
        baseLogWork.setRemark("【"+logRemark+"】更新SKU【"+taobaoBaseInfoObj.getTitle()+"】");
        baseLogWork.setTableName("baseTable");
        baseLogWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
        commonDao.save(baseLogWork);
        
        return ReturnUtil.returnMap(1, null);
    }
    
    @Override
    public Map<String, Object> deleteTaobao(String numberCodes) {
        
        String bookingRulesSql = "DELETE FROM taobao_booking_rule where taobao_travel_item in ("+ StringTool.StrToSqlString(numberCodes) +")";
        
        String saleInfoSql = "DELETE FROM taobao_sale_info where taobao_travel_item in ("+ StringTool.StrToSqlString(numberCodes) +")";
        
        String hqlSku = "from TaobaoSkuInfo s where s.taobaoTravelItem in ("+ StringTool.StrToSqlString(numberCodes) +")";
        @SuppressWarnings("unchecked")
        List<TaobaoSkuInfo> skuList = commonDao.queryByHql(hqlSku);
        List<String> skuInfos = new ArrayList<>();
        for(TaobaoSkuInfo sku : skuList){
            skuInfos.add(sku.getNumberCode());
        }
        if(skuInfos.size() > 0){
            String skuTkviewType = "DELETE FROM sku_tkview_type WHERE sku IN ("+ StringTool.listToSqlString(skuInfos) +")";
            commonDao.getSqlQuery(skuTkviewType).executeUpdate();
        }
        
        String skuInfoSql = "DELETE FROM taobao_sku_info where taobao_travel_item in ("+ StringTool.StrToSqlString(numberCodes) +")";
        
        String cruiseItemSql = "DELETE FROM taobao_cruise_item_ext where taobao_travel_item in ("+ StringTool.StrToSqlString(numberCodes) +")";
        
        commonDao.getSqlQuery(bookingRulesSql).executeUpdate();
        commonDao.getSqlQuery(saleInfoSql).executeUpdate();
        commonDao.getSqlQuery(skuInfoSql).executeUpdate();
        commonDao.getSqlQuery(cruiseItemSql).executeUpdate();
        
        String baseInfoSql = "DELETE FROM taobao_base_info where taobao_travel_item in ("+StringTool.StrToSqlString(numberCodes)+")";
        commonDao.getSqlQuery(baseInfoSql).executeUpdate();
        
        String travelItemSql = "DELETE FROM taobao_travel_item where number_code in ("+StringTool.StrToSqlString(numberCodes)+")";
        commonDao.getSqlQuery(travelItemSql).executeUpdate();
        
        return ReturnUtil.returnMap(1, null);
    }
    
    @Override
    public Map<String, Object> getTaobaoAllInfo(String baseInfo) {
        
        TaobaoBaseInfo taobaoBaseInfoObj = (TaobaoBaseInfo)commonDao.getObjectByUniqueCode("TaobaoBaseInfo", "numberCode", baseInfo);
        if (taobaoBaseInfoObj == null) {
            return ReturnUtil.returnMap(0, "宝贝基础数据错误!");
        }
        
        TaobaoTravelItem taobaoTravelItemObj = (TaobaoTravelItem)commonDao.getObjectByUniqueCode("TaobaoTravelItem", "numberCode", taobaoBaseInfoObj.getTaobaoTravelItem());
        if (taobaoTravelItemObj == null) {
            return ReturnUtil.returnMap(0, "【1】数据错误!");
        }
        
        TaobaoCruiseItemExt taobaoCruiseItemExtObj = (TaobaoCruiseItemExt)commonDao.getObjectByUniqueCode("TaobaoCruiseItemExt", "taobaoTravelItem", taobaoBaseInfoObj.getTaobaoTravelItem());
        if (taobaoCruiseItemExtObj == null) {
            return ReturnUtil.returnMap(0, "【2】数据错误!");
        }
        
        TaobaoSaleInfo taobaoSaleInfoObj = (TaobaoSaleInfo)commonDao.getObjectByUniqueCode("TaobaoSaleInfo", "taobaoTravelItem", taobaoBaseInfoObj.getTaobaoTravelItem());
        if (taobaoSaleInfoObj == null) {
            return ReturnUtil.returnMap(0, "【3】数据错误!");
        }
        
        String skuHql = "from TaobaoSkuInfo where taobaoTravelItem = '"+taobaoTravelItemObj.getNumberCode()+"'";
        @SuppressWarnings("unchecked")
        List<TaobaoSkuInfo> taobaoSkuInfoList = commonDao.queryByHql(skuHql);
        if (taobaoSkuInfoList.size() < 0) {
            return ReturnUtil.returnMap(0, "SKU套餐数据错误!");
        }
        
        String bookingRulesHql = "from TaobaoBookingRule where taobaoTravelItem = '"+taobaoTravelItemObj.getNumberCode()+"'";
        @SuppressWarnings("unchecked")
        List<TaobaoBookingRule> taobaoBookingRulesList = commonDao.queryByHql(bookingRulesHql);
        if (taobaoBookingRulesList.size() < 0) {
            return ReturnUtil.returnMap(0, "【4】数据错误!");
        }
        
        Map<String, Object> taobaoMap = new HashMap<String, Object>();
        taobaoMap.put("taobaoBaseInfoObj", taobaoBaseInfoObj);
        taobaoMap.put("taobaoTravelItemObj", taobaoTravelItemObj);
        taobaoMap.put("taobaoBookingRulesList", taobaoBookingRulesList);
        taobaoMap.put("taobaoCruiseItemExtObj", taobaoCruiseItemExtObj);
        taobaoMap.put("taobaoSaleInfoObj", taobaoSaleInfoObj);
        taobaoMap.put("taobaoSkuInfoList", taobaoSkuInfoList);
        
        return ReturnUtil.returnMap(1, taobaoMap);
    }

    @Override
    public Map<String, Object> getTaobaoSkuOper(String baseInfo) {
        
        TaobaoBaseInfo taobaoBaseInfoObj = (TaobaoBaseInfo)commonDao.getObjectByUniqueCode("TaobaoBaseInfo", "numberCode", baseInfo);
        if (taobaoBaseInfoObj == null) {
            return ReturnUtil.returnMap(0, "基础信息不存在!");
        }
        
        TaobaoTravelItem taobaoTravelItemObj = (TaobaoTravelItem)commonDao.getObjectByUniqueCode("TaobaoTravelItem", "numberCode", taobaoBaseInfoObj.getTaobaoTravelItem());
        if (taobaoTravelItemObj == null) {
            return ReturnUtil.returnMap(0, "宝贝不存在!");
        }
        
        String hql = "from TaobaoSkuInfo t where t.taobaoTravelItem = '" + taobaoTravelItemObj.getNumberCode() + "' ";
        hql += "order by t.addDate ";
        Query countQuery = commonDao.getQuery("select count(*) " + hql.toString());
        Number countNum = (Number)countQuery.uniqueResult();
        int count = countNum == null ? 0 : countNum.intValue();

        Query query = commonDao.getQuery(hql.toString());
        @SuppressWarnings("unchecked")
        List<TaobaoSkuInfo> baseInfoList = query.list();
        
        Date newDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(newDate);
        cal.add(Calendar.DATE, 2);
        String jia = (new SimpleDateFormat("yyyy-MM-dd")).format(cal.getTime());
        //boolean flag = true;
        for(TaobaoSkuInfo sku : baseInfoList){
            String skuStockHql = "from TaobaoSkuInfoDetail d where 1=1 ";
            skuStockHql += "and d.isUpdate = 1 ";
            skuStockHql += "and d.taobaoSkuInfo = '"+ sku.getNumberCode() +"'";
            skuStockHql += "and d.date > '"+jia+"' ";
            @SuppressWarnings("unchecked")
            List<TaobaoSkuInfoDetail> stockList = commonDao.queryByHql(skuStockHql);
            if(stockList.size() > 0){
                sku.setIsUpdate(1);
                continue;
            }
            sku.setIsUpdate(0);
            /*if(flag && sku.getPackageName().equals("其他套餐")){
                flag = false;
            }*/
        }
        
       /* if(flag){
            TaobaoSkuInfo skuInfo=new TaobaoSkuInfo();
            skuInfo.setNumberCode(UUIDUtil.getUUIDStringFor32());
            skuInfo.setAddDate(new Date());
            skuInfo.setPackageName("其他套餐");
            skuInfo.setTaobaoTravelItem(taobaoTravelItemObj.getNumberCode());
            skuInfo.setIsLock(1);
            commonDao.save(skuInfo);
            baseInfoList.add(skuInfo);
        }*/
        
        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", count);
        mapContent.put("rows", baseInfoList);
        
        return ReturnUtil.returnMap(1, mapContent);
    }

    @Override
    public Map<String, Object> editTaobaoDetails(String numberCode , String tkview,
                                                 String minTkview , BigDecimal price,
                                                 int priceType , int isLock, int isEdit , 
                                                 String stock , String stockNumberCode) {
        if(StringTool.isEmpty(tkview)){
            return ReturnUtil.returnMap(0, "请选择单品信息!");
        }
        boolean pd = false;
        if(StringTool.isEmpty(stock)){
            return ReturnUtil.returnMap(0, "库存不能为空!");
        }else{
            if(!"现询".equals(stock)){
                Pattern pattern = Pattern.compile("[0-9]*"); 
                Matcher isNum = pattern.matcher(stock);
                if(!isNum.matches()){
                    return ReturnUtil.returnMap(0, "库存只能填0-9的数字或现询!");
                }
                pd = true;
            }
           
        }
        
        String deleteSkuTkview = "DELETE FROM sku_tkview WHERE ";
        deleteSkuTkview += "sku_info_detail = '"+ numberCode +"' ";
        commonDao.getSqlQuery(deleteSkuTkview).executeUpdate();
        commonDao.flush();
        
        TaobaoSkuInfoDetail taobaoSkuInfoDetail = (TaobaoSkuInfoDetail)commonDao.getObjectByUniqueCode(Constant.taobaoSkuInfoDetail, Constant.numberCode, numberCode);
        if (taobaoSkuInfoDetail == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.taobao_sku_info_detail_not_exist_error);
        }
        
        Tkview tkviewObj = (Tkview)commonDao.getObjectByUniqueCode(Constant.tkview, Constant.numberCode, minTkview);
        if (tkviewObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.tkview_not_exist_error);
        }
        
        Shipment shipmentObj = (Shipment)commonDao.getObjectByUniqueCode("Shipment", "numberCode", tkviewObj.getShipment());
       
        taobaoSkuInfoDetail.setAirway(shipmentObj.getAirway());
        taobaoSkuInfoDetail.setOutId(minTkview);
        taobaoSkuInfoDetail.setDate(tkviewObj.getShipmentTravelDate());
        taobaoSkuInfoDetail.setPriceType(priceType);
        taobaoSkuInfoDetail.setPrice(price);
        taobaoSkuInfoDetail.setFlag(1);
        taobaoSkuInfoDetail.setIsLock(isLock);  //是否锁定价格
        taobaoSkuInfoDetail.setIsEdit(isEdit);
        taobaoSkuInfoDetail.setIsUpdate(1);
        if(pd){
            taobaoSkuInfoDetail.setStock(new BigDecimal(stock).intValue());
        }
        if(taobaoSkuInfoDetail.getIsNormal()==1&&taobaoSkuInfoDetail.getOutStock()!=null&&!taobaoSkuInfoDetail.getOutStock().equals(stockNumberCode)){
        	taobaoSkuInfoDetail.setIsNormal(0);
        }
        taobaoSkuInfoDetail.setOutStock(stockNumberCode);
        taobaoSkuInfoDetail.setAlterDate(new Date());
        commonDao.update(taobaoSkuInfoDetail);
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> overrideTaobaoSkuOper(String taobaoBaseInfo) {
        
        TaobaoBaseInfo taobaoBaseInfoObj = (TaobaoBaseInfo)commonDao.getObjectByUniqueCode("TaobaoBaseInfo", "numberCode", taobaoBaseInfo);
        
        TaobaoTravelItem taobaoTravelItemObj = (TaobaoTravelItem)commonDao.getObjectByUniqueCode("TaobaoTravelItem", "numberCode", taobaoBaseInfoObj.getTaobaoTravelItem());
        if (taobaoTravelItemObj == null) {
            return ReturnUtil.returnMap(0, "宝贝不存在!");
        }
        
        Channel channelObj = (Channel)commonDao.getObjectByUniqueCode("Channel", "numberCode", taobaoTravelItemObj.getChannel());
        if (channelObj == null) {
            return ReturnUtil.returnMap(0, "渠道不存在!");
        }
        
        String appKey = channelObj.getAppKey();
        if (!StringTool.isNotNull(appKey)) {
            return ReturnUtil.returnMap(0, "appKey错误!");
        }
        String appSecret = channelObj.getAppSecret(); 
        if (!StringTool.isNotNull(appSecret)) {
            return ReturnUtil.returnMap(0, "appSecret错误!");
        }
        
        String skuHql = "from TaobaoSkuInfo where taobaoTravelItem = '"+taobaoTravelItemObj.getNumberCode()+"'";
        @SuppressWarnings("unchecked")
        List<TaobaoSkuInfo> taobaoSkuInfoList = commonDao.queryByHql(skuHql);
        
        TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", appKey, appSecret);
        
        AlitripTravelItemSkuOverrideRequest req = new AlitripTravelItemSkuOverrideRequest();
        if (StringTool.isEmpty(taobaoTravelItemObj.getItemId())) {
            return ReturnUtil.returnMap(0, "宝贝未上传!");
        }
        req.setItemId(Long.valueOf(taobaoTravelItemObj.getItemId()));
        List<PontusTravelItemSkuInfo> PontusTravelItemSkuInfoList = new ArrayList<PontusTravelItemSkuInfo>();
        
        for (TaobaoSkuInfo skus : taobaoSkuInfoList) {
        	if("其他套餐".equals(skus.getPackageName())){
        		continue;
        	}
        	
            PontusTravelItemSkuInfo packageObj = new PontusTravelItemSkuInfo();
            packageObj.setPackageName(skus.getPackageName());
            packageObj.setOuterSkuId(skus.getNumberCode());
            
            List<PontusTravelPrices> PontusTravelPricesList = new ArrayList<PontusTravelPrices>();
            
            //上传的宝贝不能超过334天
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DATE, 334);
            String maxDate = (new SimpleDateFormat("yyyy-MM-dd")).format(cal.getTime());
            String detailsHql = "from TaobaoSkuInfoDetail where taobaoSkuInfo = '"+skus.getNumberCode()+"' ";
            detailsHql += "and date <= '"+ maxDate +"' ";
            @SuppressWarnings("unchecked")
            List<TaobaoSkuInfoDetail> taobaoSkuInfoDetailObj = commonDao.queryByHql(detailsHql);
            
            for (TaobaoSkuInfoDetail t : taobaoSkuInfoDetailObj) {
                PontusTravelPrices PontusTravelPricesObj = new PontusTravelPrices();
                PontusTravelPricesObj.setPriceType(Long.valueOf(t.getPriceType()));
                PontusTravelPricesObj.setStock(Long.valueOf(t.getStock()));
                PontusTravelPricesObj.setPrice(t.getPrice().longValue());
                PontusTravelPricesObj.setDate(t.getDate());
                PontusTravelPricesObj.setOuterPriceId(t.getOutId());
                PontusTravelPricesList.add(PontusTravelPricesObj);
                
                t.setFlag(0);
                t.setAlterDate(new Date());
                commonDao.update(t);
            }
            
           // packageObj.setPackageDesc("套餐描述");
            packageObj.setPrices(PontusTravelPricesList);
            PontusTravelItemSkuInfoList.add(packageObj);
        }
        
        req.setSkus(PontusTravelItemSkuInfoList);
        
        AlitripTravelItemSkuOverrideResponse rsp = null;
        try {
            rsp = client.execute(req, channelObj.getSessionKey());
        }catch (ApiException e) {
            throw new MyExceptionForXyz(e.getMessage());
        }
        
        @SuppressWarnings("unchecked")
        HashMap<String, Object> alitripTravelItemSkuResponseMap = JSON.toObject(rsp.getBody(), HashMap.class);
        
        @SuppressWarnings("unchecked")
        HashMap<String, Object> travelItemMap = (HashMap<String, Object>)alitripTravelItemSkuResponseMap.get("alitrip_travel_item_sku_override_response");
       
        if (travelItemMap == null) {
            @SuppressWarnings("unchecked")
            Map<String, Object> errorResponseMap = (Map<String, Object>)alitripTravelItemSkuResponseMap.get("error_response");
            return ReturnUtil.returnMap(0, "接口错误,错误码【"+errorResponseMap.get("msg").toString()+"】错误信息【"+errorResponseMap.get("sub_msg").toString()+"】");
        }
        
 /*       for(TaobaoSkuInfo s : taobaoSkuInfoList) {
            commonDao.update(s);
        }*/
        
        commonDao.flush();
        String updateSql=" update taobao_sku_info_detail set  is_update=0 where taobao_sku_info in(select number_code from taobao_sku_info where taobao_travel_item='"+  taobaoTravelItemObj.getNumberCode()+"')";
        commonDao.getSqlQuery(updateSql).executeUpdate();
        
        return ReturnUtil.returnMap(1, null);
    }

    /**
     * 单个商品上/下架
     *
     * @param taobaoBaseInfo
     * @param isUp
     */
    @Override
    public Map<String, Object> editOnlineStatus(String taobaoBaseInfo,Boolean isUp) {
        
        String baseHql = "from TaobaoBaseInfo where numberCode in ("+StringTool.StrToSqlString(taobaoBaseInfo)+")";
        @SuppressWarnings("unchecked")
        List<TaobaoBaseInfo> taobaoBaseInfoList = commonDao.queryByHql(baseHql);
        
        for (TaobaoBaseInfo taobaoBaseInfoObj : taobaoBaseInfoList) {
            TaobaoTravelItem taobaoTravelItemObj = (TaobaoTravelItem)commonDao.getObjectByUniqueCode("TaobaoTravelItem", "numberCode", taobaoBaseInfoObj.getTaobaoTravelItem());
            if (taobaoTravelItemObj == null) {
                return ReturnUtil.returnMap(0, "宝贝对象不存在!");
            }
            
            Channel channelObj = (Channel)commonDao.getObjectByUniqueCode("Channel", "numberCode", taobaoTravelItemObj.getChannel());
            if (channelObj == null) {
                return ReturnUtil.returnMap(0, ConstantMsg.channel_not_exist_error);
            }
            String appKey = channelObj.getAppKey();
            if (StringTool.isEmpty(appKey)) {
                return ReturnUtil.returnMap(0, "渠道appKey错误!");
            }
            String appSecret = channelObj.getAppSecret();
            if (StringTool.isEmpty(appSecret)) {
                return ReturnUtil.returnMap(0, "渠道appSecret错误!");
            }
            
            TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", appKey, appSecret);
            AlitripTravelItemShelveRequest req = new AlitripTravelItemShelveRequest();
            req.setItemId(Long.valueOf(taobaoTravelItemObj.getItemId()));
            int tempItemStatus = taobaoTravelItemObj.getItemStatus();
            Long itemStatus = 0L;
            String status = "";
            
            if (isUp != null) {
                if (isUp) {
                    itemStatus = 1L;
                    taobaoTravelItemObj.setItemStatus(0);
                    status = "上架";
                }else if(!isUp){
                    taobaoTravelItemObj.setItemStatus(-2);
                    status = "下架";
                }
            }else {
                if(tempItemStatus == 0 || tempItemStatus == 1) {
                    taobaoTravelItemObj.setItemStatus(-2);
                    status = "下架";
                }else if (tempItemStatus == -2) {
                    itemStatus = 1L;
                    taobaoTravelItemObj.setItemStatus(0);
                    status = "上架";
                }else if (tempItemStatus == -5) {
                    itemStatus = 1L;
                    taobaoTravelItemObj.setItemStatus(0);
                    status = "上架";
                }
            }
            
            req.setItemStatus(itemStatus);
            //定时上架
            //req.setOnlineTime(StringUtils.parseDateTime("2015-10-10 10:10:10"));

            AlitripTravelItemShelveResponse rsp = null;
            try {
                rsp = client.execute(req, channelObj.getSessionKey());
            }catch (ApiException e) {
                throw new MyExceptionForXyz(e.getMessage());
            }
            
            taobaoBaseInfoObj.setAlterDate(new Date());
            commonDao.update(taobaoBaseInfoObj);
            
            @SuppressWarnings("unchecked")
            HashMap<String, Object> alitripTravelItemStatusResponseMap = JSON.toObject(rsp.getBody(), HashMap.class);
            
            @SuppressWarnings("unchecked")
            HashMap<String, Object> travelItemMap = (HashMap<String, Object>)alitripTravelItemStatusResponseMap.get("alitrip_travel_item_shelve_response");
           
            if (travelItemMap == null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> errorResponseMap = (Map<String, Object>)alitripTravelItemStatusResponseMap.get("error_response");
                return ReturnUtil.returnMap(0, "接口错误,错误码【"+errorResponseMap.get("msg").toString()+"】错误信息【"+errorResponseMap.get("sub_msg").toString()+"】");
            }
            
            TaobaoLogWork logWork = new TaobaoLogWork();
            logWork.setAddDate(new Date());
            logWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
            logWork.setValue(taobaoBaseInfoObj.getNumberCode());
            logWork.setRemark("【"+taobaoBaseInfoObj.getTitle()+"】【"+status+"】");
            logWork.setTableName("baseTable");
            logWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
            commonDao.save(logWork);
            
        }
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> getSkuDetails(String skuInfo) {
        
        TaobaoSkuInfo taobaoSkuInfoObj = (TaobaoSkuInfo)commonDao.getObjectByUniqueCode("TaobaoSkuInfo", "numberCode", skuInfo);
        if (taobaoSkuInfoObj == null) {
            return ReturnUtil.returnMap(0, "SKU不存在!");
        }
        TaobaoBaseInfo taobaoBaseInfoObj = (TaobaoBaseInfo)commonDao.getObjectByUniqueCode("TaobaoBaseInfo", "taobaoTravelItem", taobaoSkuInfoObj.getTaobaoTravelItem());
        Cruise cruiseObj = (Cruise)commonDao.getObjectByUniqueCode("Cruise", "numberCode", taobaoBaseInfoObj.getCruise());
        if (cruiseObj == null) {
			return ReturnUtil.returnMap(0, "请先关联邮轮!");
		}
        
        Date newDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(newDate);
        cal.add(Calendar.DATE, 2);
        String jia = (new SimpleDateFormat("yyyy-MM-dd")).format(cal.getTime());
        
        String detailsHql = "from TaobaoSkuInfoDetail t where t.taobaoSkuInfo = '"+ taobaoSkuInfoObj.getNumberCode() +"' ";
        detailsHql += "and t.date >= '"+ jia +"' ";
        detailsHql += "order by t.date ";
        @SuppressWarnings("unchecked")
        List<TaobaoSkuInfoDetail> detailList= commonDao.queryByHql(detailsHql);
        
        String sql = "SELECT DATE_FORMAT(d.date,'%Y-%m') AS date, ";
        sql += "d.is_update AS isUpdate ";
        sql += "FROM taobao_sku_info_detail d WHERE 1=1 ";
        sql += "AND d.date >= '"+ jia +"' ";
        sql += "AND d.taobao_sku_info = '"+ taobaoSkuInfoObj.getNumberCode() +"' ";
        sql += "GROUP BY DATE_FORMAT(d.date,'%Y-%m') ";
        sql += "ORDER BY DATE_FORMAT(d.date,'%Y-%m') ";
        @SuppressWarnings("unchecked")
        List<Object[]> dateList = commonDao.getSqlQuery(sql).list();
        
        List<String[]> dateStringList = new ArrayList<String[]>();
        for(Object[] obj : dateList){
            String tempDate = (String)obj[0];
            int sum = 0;  //有更新的个数
            for(TaobaoSkuInfoDetail detail : detailList){
                String strDate = DateUtil.dateToShortString(detail.getDate());
                strDate = strDate.substring(0, strDate.length()-3);
                if(detail.getIsUpdate() == 1 && tempDate.equals(strDate) ){
                    sum++;
                }
            }
            String isUpdate = ((Integer)obj[1]).toString();
            if(sum > 0){//更新
                isUpdate = "1";  
            }
            String[] strArry = {tempDate, isUpdate};
            dateStringList.add(strArry);
        }
        
        Map<String, Object> mapContent = new HashMap<String, Object>();
        //mapContent.put("total", count);
        mapContent.put("rows", detailList);
        mapContent.put("dateList", dateStringList);
        
        return ReturnUtil.returnMap(1, mapContent);
    }

    @Override
    public Map<String, Object> addSkuDetails(String baseInfo , String skuInfo , String date,
                                             String minTkview, BigDecimal price , int priceType ,
                                             int isLock , int isEdit , String stock ,
                                             String stockNumberCode) {
        if (StringTool.isEmpty(skuInfo)) {
            return ReturnUtil.returnMap(0, "SKU错误!");
        }
        if(StringTool.isEmpty(date)){
            return ReturnUtil.returnMap(0, "请选择出行日期!");
        } 
        
        boolean pd = false;
        if(StringTool.isEmpty(stock)){
            return ReturnUtil.returnMap(0, "库存不能为空!");
        }else{
            if(!"现询".equals(stock)){
                Pattern pattern = Pattern.compile("[0-9]*"); 
                Matcher isNum = pattern.matcher(stock);
                if(!isNum.matches()){
                    return ReturnUtil.returnMap(0, "库存只能填0-9的数字或现询!");
                }
                pd = true;
            }
           
        }
        
        Tkview tkviewObj = null;
        String airway = "";
        if(StringTool.isNotNull(minTkview)){
            tkviewObj = (Tkview)commonDao.getObjectByUniqueCode(Constant.tkview, Constant.numberCode, minTkview);
            if (tkviewObj == null) {
                return ReturnUtil.returnMap(0, ConstantMsg.tkview_not_exist_error);
            }
            Shipment shipmentObj = (Shipment)commonDao.getObjectByUniqueCode("Shipment", "numberCode", tkviewObj.getShipment());
            airway = shipmentObj.getAirway();
        }
        
        TaobaoSkuInfo taobaoSkuInfoObj = (TaobaoSkuInfo)commonDao.getObjectByUniqueCode("TaobaoSkuInfo", "numberCode", skuInfo);
        if(taobaoSkuInfoObj == null){
            return ReturnUtil.returnMap(0, "SKU套餐不存在!");
        }
        
        String repeatSql = "SELECT COUNT(*) FROM taobao_sku_info_detail ";
        repeatSql += "WHERE taobao_sku_info = '"+ taobaoSkuInfoObj.getNumberCode() +"' " ;
        repeatSql += "AND date = '"+ date +"' ";
        if(tkviewObj != null){
            repeatSql += "AND out_id = '"+ tkviewObj.getNumberCode() +"' ";
        }
        Query countQuery = commonDao.getSqlQuery(repeatSql);
        Number countNum = (Number)countQuery.uniqueResult();
        int count = countNum == null ? 0 : countNum.intValue();
        if (count > 0) {
            return ReturnUtil.returnMap(0, "SKU日历库存重复!");
        }
        
        Date addDate = new Date();
        TaobaoSkuInfoDetail taobaoSkuInfoDetailObj = new TaobaoSkuInfoDetail();
        taobaoSkuInfoDetailObj.setTaobaoSkuInfo(taobaoSkuInfoObj.getNumberCode());
        taobaoSkuInfoDetailObj.setNumberCode(StringUtil.get_new_taobao_price_out_num());
        taobaoSkuInfoDetailObj.setOutId(minTkview);
        taobaoSkuInfoDetailObj.setAirway(airway);
        taobaoSkuInfoDetailObj.setDate(DateUtil.shortStringToDate(date));
        taobaoSkuInfoDetailObj.setPriceType(priceType);
        taobaoSkuInfoDetailObj.setPrice(price);
        if(pd){
            taobaoSkuInfoDetailObj.setStock(new BigDecimal(stock).intValue());
        }
        taobaoSkuInfoDetailObj.setIsLock(isLock);
        taobaoSkuInfoDetailObj.setIsEdit(isEdit);
        taobaoSkuInfoDetailObj.setFlag(0);
        taobaoSkuInfoDetailObj.setIsUpdate(1);
        taobaoSkuInfoDetailObj.setIsRelation(1);
        taobaoSkuInfoDetailObj.setOutStock(stockNumberCode);
        taobaoSkuInfoDetailObj.setAddDate(addDate);
        taobaoSkuInfoDetailObj.setAlterDate(addDate);
        commonDao.save(taobaoSkuInfoDetailObj);
        
        TaobaoLogWork skuLogWork = new TaobaoLogWork();
        skuLogWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
        skuLogWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
        skuLogWork.setTableName("detailTable");
        skuLogWork.setValue(taobaoSkuInfoDetailObj.getNumberCode());
        skuLogWork.setRemark("添加SKU套餐【"+taobaoSkuInfoObj.getPackageName()+"】里日期为【"+ date +"】的库存");
        skuLogWork.setAddDate(addDate);
        commonDao.save(skuLogWork);

        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> deleteSkuDetails(String skuInfos,String numberCodes) {
        
        TaobaoSkuInfo taobaoSkuInfoObj = (TaobaoSkuInfo)commonDao.getObjectByUniqueCode("TaobaoSkuInfo", "numberCode", skuInfos);
        if (taobaoSkuInfoObj == null) {
            return ReturnUtil.returnMap(0, "套餐不存在!");
        }
        
        TaobaoBaseInfo taobaoBaseInfoObj = (TaobaoBaseInfo)commonDao.getObjectByUniqueCode("TaobaoBaseInfo", "taobaoTravelItem", taobaoSkuInfoObj.getTaobaoTravelItem());
        if (taobaoBaseInfoObj == null) {
            return ReturnUtil.returnMap(0, "基础信息不存在!");
        }
        
        String hql = "from TaobaoSkuInfoDetail where numberCode in ("+StringTool.StrToSqlString(numberCodes)+")";
        @SuppressWarnings("unchecked")
        List<TaobaoSkuInfoDetail> taobaoSkuInfoDetailsList = commonDao.queryByHql(hql);
        
        for (TaobaoSkuInfoDetail t : taobaoSkuInfoDetailsList) {
            TaobaoLogWork detailLogWork = new TaobaoLogWork();
            detailLogWork.setAddDate(new Date());
            detailLogWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
            detailLogWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
            detailLogWork.setValue(t.getNumberCode());
            detailLogWork.setTableName("detailTable");
            detailLogWork.setRemark("删除套餐【"+taobaoSkuInfoObj.getPackageName()+"】里日期为【"+DateUtil.dateToShortString(t.getDate()).substring(0,10)+"】的库存");
            commonDao.save(detailLogWork);
        }
        
        String sql = "delete from taobao_sku_info_detail where number_code in ("+StringTool.StrToSqlString(numberCodes)+")";
        commonDao.getSqlQuery(sql).executeUpdate();
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> addTaobaoSku(String baseInfo,String skuNameCn , String tkviewType) {
        if(StringTool.isEmpty(skuNameCn)){
            return ReturnUtil.returnMap(0, "SKU套餐名称不能为空!");
        }
        
        TaobaoBaseInfo taobaoBaseInfoObj = (TaobaoBaseInfo)commonDao.getObjectByUniqueCode("TaobaoBaseInfo", "numberCode", baseInfo);
        if (taobaoBaseInfoObj == null) {
            return ReturnUtil.returnMap(0, "基础信息不存在!");
        }
        
        TaobaoTravelItem taobaoTravelItemObj = (TaobaoTravelItem)commonDao.getObjectByUniqueCode("TaobaoTravelItem", "numberCode", taobaoBaseInfoObj.getTaobaoTravelItem());
        if (taobaoTravelItemObj == null) {
            return ReturnUtil.returnMap(0, "商品对象不存在!");
        }
        
        String skuCountSql = "SELECT COUNT(*) FROM taobao_sku_info s ";
        skuCountSql += "LEFT JOIN taobao_travel_item t ";
        skuCountSql += "ON s.taobao_travel_item = t.number_code ";
        skuCountSql += "WHERE s.taobao_travel_item = '"+ taobaoTravelItemObj.getNumberCode() +"' ";
        skuCountSql += "AND s.package_name = '"+skuNameCn+"' ";
        
        Query skuInfoQuery = commonDao.getSqlQuery(skuCountSql);
        Number skuInfoCountNum = (Number)skuInfoQuery.uniqueResult();
        int skuInfocount = skuInfoCountNum == null ? 0 : skuInfoCountNum.intValue();
        if (skuInfocount != 0) {
            return ReturnUtil.returnMap(0, "请不要添加相同套餐!");
        }

        String countSql = "SELECT COUNT(*) FROM taobao_sku_info s ";
        countSql += "LEFT JOIN taobao_travel_item t ";
        countSql += "ON s.taobao_travel_item = t.number_code ";
        countSql += "WHERE s.taobao_travel_item = '"+ taobaoTravelItemObj.getNumberCode() +"' ";
        if(taobaoTravelItemObj.getChannel().equals("meituan")){
            countSql += "AND t.channel = 'meituan' ";
            Query countQuery = commonDao.getSqlQuery(countSql);
            Number countNum = (Number)countQuery.uniqueResult();
            int count = countNum == null ? 0 : countNum.intValue();
            if (count >= maxCount) {
                return ReturnUtil.returnMap(0, "套餐最多"+ maxCount +"个!");
            }
        }else{
            countSql += "AND t.channel <> 'meituan' ";
            Query countQuery = commonDao.getSqlQuery(countSql);
            Number countNum = (Number)countQuery.uniqueResult();
            int count = countNum == null ? 0 : countNum.intValue();
            if (count >= 12) {
                return ReturnUtil.returnMap(0, "套餐最多12个!");
            }
        }
        
        Date date = new Date();
        TaobaoSkuInfo taobaoSkuInfoObj = new TaobaoSkuInfo();
        taobaoSkuInfoObj.setNumberCode(StringUtil.get_new_taobao_sku_out_num());
        taobaoSkuInfoObj.setPackageName(skuNameCn);
        taobaoSkuInfoObj.setTaobaoTravelItem(taobaoTravelItemObj.getNumberCode());
        taobaoSkuInfoObj.setFlag(0);
        taobaoSkuInfoObj.setAddDate(date);
        taobaoSkuInfoObj.setAlterDate(date);
        commonDao.save(taobaoSkuInfoObj);
        
        TaobaoLogWork skuLogWork = new TaobaoLogWork();
        skuLogWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
        skuLogWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
        skuLogWork.setValue(taobaoSkuInfoObj.getNumberCode());
        skuLogWork.setTableName("skuTable");
        skuLogWork.setRemark("新增SKU套餐【"+taobaoSkuInfoObj.getPackageName()+"】");
        skuLogWork.setAddDate(new Date());
        commonDao.save(skuLogWork);
        
        if(StringTool.isNotNull(tkviewType)){
            String[] tkviewTypeArry = tkviewType.split(",");
            for(int t = 0; t < tkviewTypeArry.length; t++){
                String tkviewTypeNumber = tkviewTypeArry[t];
                Sku_TkviewType sku_tkviewTypeObj = new Sku_TkviewType();
                sku_tkviewTypeObj.setNumberCode(UUIDUtil.getUUIDStringFor32());
                sku_tkviewTypeObj.setSku(taobaoSkuInfoObj.getNumberCode());
                sku_tkviewTypeObj.setTkviewType(tkviewTypeNumber);
                commonDao.save(sku_tkviewTypeObj);
            }
        }
        
        return ReturnUtil.returnMap(1, taobaoSkuInfoObj.getNumberCode());
    }

    @Override
    public Map<String, Object> editTaobaoSku(String baseInfo , String skuInfo , 
                                             String skuNameCn , String tkviewType) {
        if(StringTool.isEmpty(skuNameCn)){
            return ReturnUtil.returnMap(0, "SKU套餐名称不能为空!");
        }
        TaobaoBaseInfo taobaoBaseInfoObj = (TaobaoBaseInfo)commonDao.getObjectByUniqueCode("TaobaoBaseInfo", "numberCode", baseInfo);
        if (taobaoBaseInfoObj == null) {
            return ReturnUtil.returnMap(0, "基础信息不存在!");
        }
        
        TaobaoTravelItem taobaoTravelItemObj = (TaobaoTravelItem)commonDao.getObjectByUniqueCode("TaobaoTravelItem", "numberCode", taobaoBaseInfoObj.getTaobaoTravelItem());
        if (taobaoTravelItemObj == null) {
            return ReturnUtil.returnMap(0, "商品对象不存在!");
        }
        
        TaobaoSkuInfo taobaoSkuInfoObj = (TaobaoSkuInfo)commonDao.getObjectByUniqueCode("TaobaoSkuInfo", "numberCode", skuInfo);
        if (taobaoSkuInfoObj == null) {
            return ReturnUtil.returnMap(0, "SKU套餐对象不存在!");
        }
       /* if (taobaoSkuInfoObj.getIsLock() == 1 && !taobaoSkuInfoObj.getPackageName().equals(skuNameCn)) {
            return ReturnUtil.returnMap(0, "SKU套餐名称不能修改!"); 
        }*/
        if (!(taobaoSkuInfoObj.getPackageName()).equals(skuNameCn)) {
        	String taobaoSkuInfoCountSql =" select COUNT(*) from taobao_sku_info where taobao_travel_item = '"+ taobaoTravelItemObj.getNumberCode() +"'";
            taobaoSkuInfoCountSql +=" and package_name = '"+ skuNameCn +"'";
            Query taobaoSkuInfoQuery = commonDao.getSqlQuery(taobaoSkuInfoCountSql);
            Number taobaoSkuInfoCountNum = (Number)taobaoSkuInfoQuery.uniqueResult();
            int taobaoSkuInfocount = taobaoSkuInfoCountNum == null ? 0 : taobaoSkuInfoCountNum.intValue();
            if (taobaoSkuInfocount != 0) {
            	return ReturnUtil.returnMap(0, "该【"+ skuNameCn +"】套餐已经存在!");
    		}
		}
        Date alterDate = new Date();
        taobaoSkuInfoObj.setPackageName(skuNameCn);
        if (!skuNameCn.equals(taobaoSkuInfoObj.getPackageName())) {
            taobaoSkuInfoObj.setFlag(1);
        }
        taobaoSkuInfoObj.setAlterDate(alterDate);
        commonDao.update(taobaoSkuInfoObj);
        
        TaobaoLogWork skuLogWork = new TaobaoLogWork();
        skuLogWork.setAddDate(alterDate);
        skuLogWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
        skuLogWork.setValue(baseInfo);
        skuLogWork.setRemark("修改SKU套餐名称【"+taobaoSkuInfoObj.getPackageName()+"】");
        skuLogWork.setTableName("skuTable");
        skuLogWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
        commonDao.save(skuLogWork);
        
        String deleteSql = "delete from sku_tkview_type where sku = '"+ skuInfo +"'";
        commonDao.getSqlQuery(deleteSql).executeUpdate();
        
        if(StringTool.isNotNull(tkviewType)){
              String[] tkviewTypeArry = tkviewType.split(",");
              for(int t = 0; t < tkviewTypeArry.length; t++){
                  String tkviewTypeNumber = tkviewTypeArry[t];
                  Sku_TkviewType sku_tkviewTypeObj = new Sku_TkviewType();
                  sku_tkviewTypeObj.setNumberCode(UUIDUtil.getUUIDStringFor32());
                  sku_tkviewTypeObj.setSku(taobaoSkuInfoObj.getNumberCode());
                  sku_tkviewTypeObj.setTkviewType(tkviewTypeNumber);
                  commonDao.save(sku_tkviewTypeObj);
              }
        }
        
        String tkviewTypeHql = "from TkviewType t where t.numberCode in ("+ StringTool.StrToSqlString(tkviewType) +")";
        @SuppressWarnings("unchecked")
        List<TkviewType> typeList = commonDao.queryByHql(tkviewTypeHql);
        
        if(typeList.size() > 0){
            String cruise = typeList.get(0).getCruise();
            List<String> cabinNumber = new ArrayList<>();
            for(TkviewType typeObj : typeList){
                cabinNumber.add(typeObj.getCabin());
            }
            
            String tkviewHql = "from Tkview t where t.cruise = '"+ cruise +"' ";
            tkviewHql += "and t.cabin in ("+ StringTool.listToSqlString(cabinNumber) +") ";
            @SuppressWarnings("unchecked")
            List<Tkview> tkviewList = commonDao.queryByHql(tkviewHql);
            
            List<String> tkviewNumber = new ArrayList<String>();
            for(Tkview t : tkviewList){
                tkviewNumber.add(t.getNumberCode());
            }
            
            String hql = "from Stock s where 1=1 ";
            hql += "and s.tkview in ("+ StringTool.listToSqlString(tkviewNumber) +") ";
            hql += "order by s.tkview ";
            @SuppressWarnings("unchecked")
            List<Stock> stockList = commonDao.queryByHql(hql);
            for(Stock s : stockList){
                for(Tkview tkviewObj : tkviewList){
                    if(s.getTkview().equals(tkviewObj.getNumberCode())){
                        tkviewObj.getStocks().add(s);
                        break;
                    }
                }
            }
            
            List<Tkview> tkviews=new ArrayList<Tkview>();
            for(Tkview t:tkviewList){
                if(t.getStocks().size()>0){
                    Collections.sort(t.getStocks());
                    t.setCost(t.getStocks().get(0).getCost());
                    t.setStock(t.getStocks().get(0).getStock());
                    tkviews.add(t);
                }
            }
            Collections.sort(tkviews);
            
            String priceHql = "from PriceStrategy";
            @SuppressWarnings("unchecked")
            List<PriceStrategy> priceList = commonDao.queryByHql(priceHql);
            
            String skuStcokHql = "from TaobaoSkuInfoDetail d where d.taobaoSkuInfo = '"+ skuInfo +"'";
            @SuppressWarnings("unchecked")
            List<TaobaoSkuInfoDetail> skuStockList = commonDao.queryByHql(skuStcokHql);
            
            for(TaobaoSkuInfoDetail skuStockObj : skuStockList){
                for(Tkview tkviewObj : tkviewList){
                    String stockDate = DateUtil.dateToShortString(skuStockObj.getDate());
                    String travelDate = DateUtil.dateToShortString(tkviewObj.getShipmentTravelDate());
                    if(stockDate.equals(travelDate)){
                        if(tkviewObj.getStocks().size()>0){
                            Shipment shipObj = (Shipment)commonDao.getObjectByUniqueCode("Shipment", "numberCode", tkviewObj.getShipment());
                            if(shipObj == null){
                                return ReturnUtil.returnMap(0, "航期不存在!");
                            }
                            Stock stockObj = tkviewObj.getStocks().get(0);
                            BigDecimal cost=stockObj.getCost().divide(new BigDecimal(100));
                           for(PriceStrategy priceObj : priceList){
                                if(priceObj.getMinPrice().compareTo(cost) <= 0 &&  
                                   priceObj.getMaxPrice().compareTo(cost) >= 0){
                                    skuStockObj.setPrice(new BigDecimal(cost.add(priceObj.getPriceMarkup()).divide(new BigDecimal(100)).intValue()*100+99).multiply(new BigDecimal(100)));
                                    skuStockObj.setStock(stockObj.getStock().intValue());
                                    skuStockObj.setOutId(tkviewObj.getNumberCode());
                                    skuStockObj.setOutStock(stockObj.getNumberCode());
                                    skuStockObj.setAirway(shipObj.getAirway());
                                    skuStockObj.setIsUpdate(1);
                                    skuStockObj.setAlterDate(alterDate);
                                    commonDao.update(skuStockObj);
                                    break;
                                }
                            }
                        }
                    }
                    
                }
            }
            
        } 
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> deleteTaobaoSku(String baseInfo,String skuInfos) {
        
        String hql = "from TaobaoSkuInfo where numberCode in ("+ StringTool.StrToSqlString(skuInfos) +")";
        
        @SuppressWarnings("unchecked")
        List<TaobaoSkuInfo> taobaoSkuInfoList = commonDao.queryByHql(hql);
        
        for (TaobaoSkuInfo t : taobaoSkuInfoList) {
            if (t.getIsLock() == 1) {
                return ReturnUtil.returnMap(0, "套餐不能删除!");
            }
            
            TaobaoLogWork skuLogWork = new TaobaoLogWork();
            skuLogWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
            skuLogWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
            skuLogWork.setValue(t.getNumberCode());
            skuLogWork.setTableName("skuTable");
            skuLogWork.setRemark("删除SKU套餐【"+t.getPackageName()+"】");
            skuLogWork.setAddDate(new Date());
            commonDao.save(skuLogWork);
            
        }
        
        String skuDetailSql = "DELETE FROM taobao_sku_info_detail WHERE taobao_sku_info = '"+skuInfos+"'";
        commonDao.getSqlQuery(skuDetailSql).executeUpdate();
        
        String sql = "DELETE FROM taobao_sku_info WHERE number_code IN ("+ StringTool.StrToSqlString(skuInfos) +")";
        commonDao.getSqlQuery(sql).executeUpdate();
        
        String skuTkviewType = "DELETE FROM sku_tkview_type WHERE sku IN ("+ StringTool.StrToSqlString(skuInfos) +")";
        commonDao.getSqlQuery(skuTkviewType).executeUpdate();
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> getTaobaoLog(String numberCode) {
        String logSql = "from TaobaoLogWork where value='" + numberCode + "' and tableName='baseTable' ORDER BY addDate DESC ";
        Query queryLog = commonDao.getQuery(logSql);
        @SuppressWarnings("unchecked")
        List<TaobaoLogWork> logWorkList = queryLog.list();

        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", logWorkList.size());
        mapContent.put("rows", logWorkList);

        return ReturnUtil.returnMap(1, mapContent);
    }
   
    @Override
    public Map<String, Object> getCruiseData(String cruise) {
        
        Map<String, Object> map = new HashMap<String, Object>();
        String dateSql = "SELECT concat(MIN(travel_date),''),concat(MAX(travel_end_date),'') FROM shipment WHERE 1=1";
        if(StringTool.isEmpty(cruise)){
            return ReturnUtil.returnMap(0, ConstantMsg.cruise_error);
        }
        dateSql += " AND cruise ='"+ cruise +"'";
        Cruise cruiseObj = (Cruise)commonDao.getObjectByUniqueCode("Cruise", "numberCode", cruise);
        if (cruiseObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.cruise_null);
        }
        Company company = (Company)commonDao.getObjectByUniqueCode(Constant.company, Constant.numberCode, cruiseObj.getCompany());
        if (company == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.company_null);
        }
        cruiseObj.setCompanyNameCn(company.getNameCn());
        map.put("cruise", cruiseObj);
       
        @SuppressWarnings("unchecked")

        List<Object[]> dateList = commonDao.getSqlQuery(dateSql).list();
        
        String travel = (String)dateList.get(0)[0];
        String end = (String)dateList.get(0)[1];
        map.put("travel", travel);
        map.put("end", end);
        
        return ReturnUtil.returnMap(1, map);
    }
    
    @Override
    public Map<String, Object> getTkviewByTaobaoCruise(String baseInfo) {
        
        TaobaoBaseInfo taobaoBaseInfo = (TaobaoBaseInfo)commonDao.getObjectByUniqueCode(Constant.taobaoBaseInfo, Constant.numberCode, baseInfo);
        if (taobaoBaseInfo == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.taobao_base_info_not_exist_error);
        }
            
        Cruise cruise = (Cruise)commonDao.getObjectByUniqueCode(Constant.cruise, Constant.numberCode, taobaoBaseInfo.getCruise());
        if (cruise == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.cruise_null);
        }
        
        String shipmentNumberCodeSql = "SELECT number_code FROM shipment WHERE cruise = '"+taobaoBaseInfo.getCruise()+"'";
        @SuppressWarnings("unchecked")
        List<String> shipmnetNumberCodeList = commonDao.getSqlQuery(shipmentNumberCodeSql).list();
        
        String tkviewHql = "from Tkview where cruise = '"+cruise.getNumberCode()+"' and shipment in ("+StringTool.listToSqlString(shipmnetNumberCodeList)+")";
        tkviewHql += " "+possessorUtil.getRelatesWhereHql(Constant.relate_type_tkview)+" ORDER BY name_cn";
        
        @SuppressWarnings("unchecked")
        List<Tkview> tkviewList = commonDao.queryByHql(tkviewHql);
        
        List<String> tkviewNumberCodeList = new ArrayList<>();
        for (Tkview tkview : tkviewList) {
            tkviewNumberCodeList.add(tkview.getNumberCode());
        }

        String stockSql = "SELECT tkview,stock FROM stock WHERE tkview IN ("+StringTool.listToSqlString(tkviewNumberCodeList)+")";
        @SuppressWarnings("unchecked")
        List<Object[]> stockList = commonDao.getSqlQuery(stockSql).list();
        
        for (Tkview tkview : tkviewList) {
            for (Object[] stock : stockList) {
                if (tkview.getNumberCode().equals((String)stock[0])) {
                    tkview.setStock((BigDecimal)stock[1]);
                    break;
                }
            }
        }
        
        Map<String, Object> tkviewMap = new HashMap<String, Object>();
        List<Tkview> tempTkviewList = new ArrayList<>();
        
        if (tkviewList.size() > 0 ) {
            String tempTkviewNameCn = tkviewList.get(0).getNameCn();
            for (int i = 0; i < tkviewList.size(); i++ ) {
                if (tempTkviewNameCn.equals(tkviewList.get(i).getNameCn().trim())) {
                    tempTkviewList.add(tkviewList.get(i));
                }else {
                    tkviewMap.put(tempTkviewNameCn, tempTkviewList);
                    tempTkviewNameCn = tkviewList.get(i).getNameCn().trim();
                    tempTkviewList = new ArrayList<>();
                    tempTkviewList.add(tkviewList.get(i));
                    if ((i+1) == tkviewList.size()) {
                        tkviewMap.put(tempTkviewNameCn, tempTkviewList);
                    }
                }
            }
        }
        
        return ReturnUtil.returnMap(1, tkviewMap);
    }
    
    @Override
	public Map<String, Object> getExistSkuByBaseInfo(String baseInfo) {
    	
    	TaobaoBaseInfo taobaoBaseInfoObj = (TaobaoBaseInfo)commonDao.getObjectByUniqueCode("TaobaoBaseInfo", "numberCode", baseInfo);
    	
    	if (taobaoBaseInfoObj == null) {
			return ReturnUtil.returnMap(0, "信息不存在");
		}
    	
    	String taobaoSkuInfoHql ="from TaobaoSkuInfo where taobaoTravelItem  ='"+taobaoBaseInfoObj.getTaobaoTravelItem()+"'";
    	@SuppressWarnings("unchecked")
		List<TaobaoSkuInfo> taobaoSkuInfoList = commonDao.getQuery(taobaoSkuInfoHql).list();
    	List<String> taobaoSkuInfoNumberCode = new ArrayList<>();
    	for (TaobaoSkuInfo taobaoSkuInfo : taobaoSkuInfoList) {
    		taobaoSkuInfoNumberCode.add(taobaoSkuInfo.getNumberCode());
		}
    	
    	String taobaoSkuInfoDetailHql = "from TaobaoSkuInfoDetail where taobaoSkuInfo in ("+StringTool.listToSqlString(taobaoSkuInfoNumberCode)+")";
		@SuppressWarnings("unchecked")
		List<TaobaoSkuInfoDetail> taobaoSkuInfoDetailList = commonDao.getQuery(taobaoSkuInfoDetailHql).list();
		List<String> taobaoSkuInfoDetailTkview = new ArrayList<>();
		for (TaobaoSkuInfoDetail taobaoSkuInfoDetail : taobaoSkuInfoDetailList) {
			taobaoSkuInfoDetailTkview.add(taobaoSkuInfoDetail.getOutId());
		}
		
    	return ReturnUtil.returnMap(1,taobaoSkuInfoDetailTkview);
	}

    @Override
    public Map<String, Object> addRelationSkuAndStock(String skuJsonStr, String baseInfo) {
        
        TaobaoBaseInfo taobaoBaseInfo = (TaobaoBaseInfo)commonDao.getObjectByUniqueCode(Constant.taobaoBaseInfo, Constant.numberCode, baseInfo);
        if (taobaoBaseInfo == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.taobao_base_info_not_exist_error);
        }
        
        Date date = new Date();
        
        @SuppressWarnings("unchecked")
        List<Object> skuJsonList = JSON.toObject(skuJsonStr, ArrayList.class);
        
        @SuppressWarnings("unchecked")
        Map<String, List<Map<String, Object>>> jsonMap = (Map<String, List<Map<String, Object>>>)skuJsonList.get(0);
        
        for (String key : jsonMap.keySet()) {
            String taobaoskuHql = "from TaobaoSkuInfo where taobaoTravelItem = '"+taobaoBaseInfo.getTaobaoTravelItem()+"' and packageName = '"+key.trim()+"'";
            TaobaoSkuInfo taobaoSkuInfoObj = (TaobaoSkuInfo)commonDao.getQuery(taobaoskuHql).uniqueResult();
            
            TaobaoSkuInfo taobaoSkuInfo = null;
            if (taobaoSkuInfoObj == null) {
                taobaoSkuInfo = new TaobaoSkuInfo();
                taobaoSkuInfo.setNumberCode(StringUtil.get_new_taobao_sku_out_num());
                taobaoSkuInfo.setAddDate(date);
                taobaoSkuInfo.setFlag(0);
            }else {
                taobaoSkuInfo = taobaoSkuInfoObj;
                taobaoSkuInfo.setFlag(1);
            }
            taobaoSkuInfo.setAlterDate(date);
            taobaoSkuInfo.setIsLock(0);
            taobaoSkuInfo.setPackageName(key.trim());
            taobaoSkuInfo.setTaobaoTravelItem(taobaoBaseInfo.getTaobaoTravelItem());
            commonDao.save(taobaoSkuInfo);
            
            List<Map<String, Object>> JsonList = jsonMap.get(key);
            for (Map<String, Object> skuMap : JsonList) {
                Date shipmentTravelDate = DateUtil.shortStringToDate((String)skuMap.get("date"));
                int type = Integer.valueOf((String)skuMap.get("type"));
                BigDecimal price = new BigDecimal((String)skuMap.get("price"));
                int stock = new BigDecimal((String)skuMap.get("stock")).intValue();
                String tkview = (String)skuMap.get("tkview");
                
                Tkview tkviewObj = (Tkview)commonDao.getObjectByUniqueCode(Constant.tkview, Constant.numberCode, tkview);
                if (tkviewObj == null) {
                    return ReturnUtil.returnMap(0, ConstantMsg.tkview_not_exist_error);
                }
                
                if (taobaoSkuInfoObj != null) {
                    String repeatSql = "select count(*) from taobao_sku_info_detail where taobao_sku_info = '"+taobaoSkuInfoObj.getNumberCode()+"'" ;
                    repeatSql += " and date = '"+tkviewObj.getShipmentTravelDate()+"' and out_id = '"+tkviewObj.getNumberCode()+"' ";
                    
                    Query countQuery = commonDao.getSqlQuery(repeatSql);
                    Number countNum = (Number)countQuery.uniqueResult();
                    int count = countNum == null ? 0 : countNum.intValue();
                    
                    if (count > 0) {
                        return ReturnUtil.returnMap(0, "库存重复!");
                    }
                }
                
                TaobaoSkuInfoDetail taobaoSkuInfoDetail = new TaobaoSkuInfoDetail();
                taobaoSkuInfoDetail.setAddDate(date);
                taobaoSkuInfoDetail.setAlterDate(date);
                taobaoSkuInfoDetail.setDate(shipmentTravelDate);
                taobaoSkuInfoDetail.setFlag(0);
                taobaoSkuInfoDetail.setIsRelation(1);
                taobaoSkuInfoDetail.setNumberCode(StringUtil.get_new_taobao_price_out_num());
                taobaoSkuInfoDetail.setOutId(tkview);
                taobaoSkuInfoDetail.setPrice(price);
                taobaoSkuInfoDetail.setPriceType(type);
                taobaoSkuInfoDetail.setStock(stock);
                taobaoSkuInfoDetail.setTaobaoSkuInfo(taobaoSkuInfo.getNumberCode());
                taobaoSkuInfoDetail.setTkviewNameCn(tkviewObj.getNameCn());
                commonDao.save(taobaoSkuInfoDetail);
            }
        }
        
        return ReturnUtil.returnMap(1, null);
    }


	@Override
	public Map<String, Object> getRelationTkview(String taobaoSkuDetail) {
	    
		/*TaobaoSkuInfoDetail detail=(TaobaoSkuInfoDetail) commonDao.getObjectByUniqueCode("TaobaoSkuInfoDetail", "numberCode", taobaoSkuDetail);
		
		
		Shipment shipment=(Shipment) commonDao.queryUniqueByHql("from Shipment where airway='"+detail.getAirway()+"' and travelDate='"+DateUtil.dateToShortString(detail.getDate())+"'");
		
	    String skuTkviewHql = "from Sku_Tkview st where 1=1 ";
	    skuTkviewHql += "and st.skuInfoDetail = '"+ taobaoSkuDetail +"'";
	    @SuppressWarnings("unchecked")
        List<Sku_Tkview> list = commonDao.queryByHql(skuTkviewHql);
        
        String tkviewStr = "";
        String tkviewHql = "from Tkview t where t.numberCode in ("+ StringTool.listToSqlString(tkviewNumber) +") ";
        @SuppressWarnings("unchecked")
        List<Tkview> tkviewList = commonDao.queryByHql(tkviewHql);
        
        String stockHql = "from Stock s where s.tkview in ("+ StringTool.listToSqlString(tkviewNumber) +") ";
		@SuppressWarnings("unchecked")
        List<Stock> stockList = commonDao.queryByHql(stockHql); 
		
		List<String> providerNumber = new ArrayList<String>();
		for(Stock s : stockList){
		    providerNumber.add(s.getProvider());
		}
		
		String providerHql = "from Provider p where p.numberCode in ("+ StringTool.listToSqlString(providerNumber) +") ";
		@SuppressWarnings("unchecked")
        List<Provider> providerList = commonDao.queryByHql(providerHql);
	
		for(Stock s : stockList){
		    for(Tkview t : tkviewList){
		        if(s.getTkview().equals(t.getNumberCode())){
		            s.setTkviewNameCn(t.getNameCn());
		            t.getStocks().add(s);
		            break;
		        }
		    }
		    for(Provider p : providerList){
		        if(s.getProvider().equals(p.getNumberCode())){
		            s.setProviderNameCn(p.getNameCn());
		            s.setProviderMark(p.getMark());
		            break;
		        }
		    }
		}
		
		List<Tkview> tkviews=new ArrayList<Tkview>();
        for(Tkview t:tkviewList){
            if(t.getStocks().size()>0){
                Collections.sort(t.getStocks());
                t.setCost(t.getStocks().get(0).getCost());
                t.setStock(t.getStocks().get(0).getStock());
                tkviews.add(t);
            }
        }
        Collections.sort(tkviews);
        
        List<Stock> resultStockList=new ArrayList<Stock>();
        for(Tkview t : tkviews){
            resultStockList.addAll(t.getStocks());
        }
		
	   String priceHql = "from PriceStrategy";
       @SuppressWarnings("unchecked")
       List<PriceStrategy> priceList = commonDao.queryByHql(priceHql);
	
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("shipment", shipment.getNumberCode());
		map.put("stockList", resultStockList);
		map.put("tkview", tkviewStr);
		map.put("priceList", priceList);
		map.put("detail", detail);*/
		
		return ReturnUtil.returnMap(1, null);
	}


	@Override
	public Map<String, Object> queryTaobaoGroupCruise() {
		String taobaoHql = "from TaobaoBaseInfo b group by b.cruise ";
		taobaoHql += "order by b.alterDate";
		@SuppressWarnings("unchecked")
        List<TaobaoBaseInfo> taobaoList = commonDao.queryByHql(taobaoHql);
		
		List<String> cruiseNumber = new ArrayList<String>();
		for(TaobaoBaseInfo base : taobaoList){
		    if(StringTool.isNotNull(base.getCruise())){
		        cruiseNumber.add(base.getCruise());
		    }
		}
		
		String cruiseSql = "SELECT number_code,name_cn FROM cruise WHERE number_code IN ("+StringTool.listToSqlString(cruiseNumber)+")";
        @SuppressWarnings("unchecked")
        List<Object[]> cruiseList = commonDao.getSqlQuery(cruiseSql).list();
        
        for (TaobaoBaseInfo base : taobaoList) {
            for (Object[] cruiseObj : cruiseList) {
                if(StringTool.isNotNull(base.getCruise()) && base.getCruise().equals((String)cruiseObj[0])) {
                    base.setCruiseNameCn((String)cruiseObj[1]);
                    break;
                }
            }
        }
        
        Date newDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(newDate);
        cal.add(Calendar.DATE, 2);
        String jia = (new SimpleDateFormat("yyyy-MM-dd")).format(cal.getTime());
        
        String countSql = "SELECT COUNT(*) AS count,b.cruise FROM taobao_base_info b ";
        countSql += "WHERE b.taobao_travel_item IN ( ";
        countSql += " SELECT s.taobao_travel_item FROM taobao_sku_info s ";
        countSql += " WHERE s.number_code IN ( ";
        countSql += "  SELECT d.taobao_sku_info FROM taobao_sku_info_detail d ";
        countSql += "  WHERE d.is_update = 1 and d.date > '"+ jia +"' ) ";
        countSql += ")  ";
        countSql += "GROUP BY b.cruise ORDER BY b.alter_date ";
        @SuppressWarnings("unchecked")
        List<Object[]> countList = commonDao.getSqlQuery(countSql).list();
        
        Map<String, Object> map = new HashMap<>();
        map.put("taobaoList", taobaoList);
        map.put("countList", countList);
		
		return ReturnUtil.returnMap(1, map);
	}

	@Override
    public Map<String, Object> getStockDate(String sku , String date) {
        if(StringTool.isEmpty(sku)){
            return ReturnUtil.returnMap(0, "套餐对象不存在!");
        }
        if(StringTool.isEmpty(date)){
            return ReturnUtil.returnMap(0, "请选日期!");
        }
        
        String stockSql = "SELECT * FROM taobao_sku_info_detail d ";
        stockSql += "WHERE d.taobao_sku_info = '"+ sku +"' ";
        stockSql += "AND d.date = '"+ date +"'";
        
        @SuppressWarnings("unchecked")
        List<TaobaoSkuInfoDetail> list = commonDao.getSqlQuery(stockSql).list();
        int count = list.size();
        
        return ReturnUtil.returnMap(1, count);
    }

    @Override
    public Map<String, Object> editStockByNumberCodes(String numberCodes) {
        if(StringTool.isEmpty(numberCodes)){
            return ReturnUtil.returnMap(0, "请选需要清空的对象!");
        }
        
        String editSql = "UPDATE taobao_sku_info_detail SET stock=0 ";
        editSql += "WHERE number_code IN ("+ StringTool.StrToSqlString(numberCodes) +") ";
        commonDao.getSqlQuery(editSql).executeUpdate();
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> queryStockByNumberCodes(String sku) {
        Date newDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(newDate);
        cal.add(Calendar.DATE, 2);
        String jia = (new SimpleDateFormat("yyyy-MM-dd")).format(cal.getTime());
        
       String skuStockHql = "from TaobaoSkuInfoDetail d where 1=1 ";
       skuStockHql += "and d.taobaoSkuInfo = '"+ sku +"' ";
       skuStockHql += "and d.date >= '"+ jia +"' ";
       skuStockHql += "order by d.date ";
       @SuppressWarnings("unchecked")
       List<TaobaoSkuInfoDetail> stockList = commonDao.queryByHql(skuStockHql);
        
        return ReturnUtil.returnMap(1, stockList);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> skuPriceModifyOper(String sku , String stockJson) {
        List<Map<String, Object>> stockList = JSON.toObject(stockJson, ArrayList.class);
        if (stockList == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.cruise_add_qucik_json_null_error);
        }
        
        TaobaoSkuInfo skuObj = (TaobaoSkuInfo)commonDao.getObjectByUniqueCode("TaobaoSkuInfo", "numberCode", sku); 
      
        TaobaoTravelItem taobaoTravelItemObj = (TaobaoTravelItem)commonDao.getObjectByUniqueCode("TaobaoTravelItem", "numberCode", skuObj.getTaobaoTravelItem());
       
        Channel channelObj = (Channel)commonDao.getObjectByUniqueCode("Channel", "numberCode", taobaoTravelItemObj.getChannel());
        if (channelObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.channel_not_exist_error);
        }
        if (StringTool.isEmpty(channelObj.getAppKey())) {
            return ReturnUtil.returnMap(0, "appKey错误!");
        }
        if (StringTool.isEmpty(channelObj.getAppSecret())) {
            return ReturnUtil.returnMap(0, "appSecret错误!");
        }
        
        List<com.taobao.api.request.AlitripTravelItemSkuPriceModifyRequest.PontusTravelItemSkuInfo> PontusTravelItemSkuInfoList = new ArrayList<com.taobao.api.request.AlitripTravelItemSkuPriceModifyRequest.PontusTravelItemSkuInfo>();
        List<com.taobao.api.request.AlitripTravelItemSkuPriceModifyRequest.PontusTravelPrices> PontusTravelPricesList = new ArrayList<com.taobao.api.request.AlitripTravelItemSkuPriceModifyRequest.PontusTravelPrices>();
        TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", channelObj.getAppKey(), channelObj.getAppSecret());
        
        AlitripTravelItemSkuPriceModifyRequest req = new AlitripTravelItemSkuPriceModifyRequest();
        if (StringTool.isEmpty(taobaoTravelItemObj.getItemId())) {
            return ReturnUtil.returnMap(0, "宝贝未上传!");
        }
        req.setItemId(Long.valueOf(taobaoTravelItemObj.getItemId()));
        
        List<List<Map<String, Object>>> stockListMap = new ArrayList<List<Map<String, Object>>>();
        List<Map<String, Object>> stockMap = null;
        for(int s = 0; s < stockList.size(); s++){
            if(stockMap == null){
                stockMap = new ArrayList<Map<String, Object>>();
            }
            if((s+1)%8==0 || s == (stockList.size()-1)){
                stockMap.add(stockList.get(s));
                stockListMap.add(stockMap);
                stockMap = null;
            }else{
                stockMap.add(stockList.get(s));
            }
        }
        
        String successStr = "";
        for(List<Map<String, Object>> mapList: stockListMap){
            for(Map<String, Object> map : mapList){
                String numberCode = (String)map.get("numberCode");
                String priceStr = (String)map.get("price");
                BigDecimal price = new BigDecimal(priceStr).multiply(new BigDecimal(100));
                String isNormalStr = (String)map.get("isNormal");
                int isNormal = Integer.parseInt(isNormalStr);
                
                TaobaoSkuInfoDetail detailObj = (TaobaoSkuInfoDetail)commonDao.getObjectByUniqueCode("TaobaoSkuInfoDetail","numberCode", numberCode);
                
                com.taobao.api.request.AlitripTravelItemSkuPriceModifyRequest.PontusTravelItemSkuInfo packageObj = new com.taobao.api.request.AlitripTravelItemSkuPriceModifyRequest.PontusTravelItemSkuInfo();
                packageObj.setOuterSkuId(skuObj.getNumberCode());
                //packageObj.setPackageName(skuObj.getPackageName());
                
                com.taobao.api.request.AlitripTravelItemSkuPriceModifyRequest.PontusTravelPrices PontusTravelPricesObj = new com.taobao.api.request.AlitripTravelItemSkuPriceModifyRequest.PontusTravelPrices();
                //覆盖更新
                PontusTravelPricesObj.setOperation(3L);
                PontusTravelPricesObj.setOuterPriceId(detailObj.getOutId());
                PontusTravelPricesObj.setDate(detailObj.getDate());
                PontusTravelPricesObj.setPrice(price.longValue());
                PontusTravelPricesObj.setPriceType(Long.valueOf(detailObj.getPriceType()));
                PontusTravelPricesObj.setStock(Long.valueOf(detailObj.getStock()));
                PontusTravelPricesList.add(PontusTravelPricesObj);
                
                detailObj.setPrice(price);
                detailObj.setIsUpdate(0);
                detailObj.setIsNormal(isNormal);
                detailObj.setFlag(0);
                detailObj.setAlterDate(new Date());
                commonDao.update(detailObj);
                    
                //packageObj.setPackageDesc("套餐描述");
                packageObj.setPrices(PontusTravelPricesList);
                PontusTravelItemSkuInfoList.add(packageObj);
                if(StringTool.isEmpty(successStr)){
                    successStr = numberCode;
                }else{
                    successStr = successStr + "," +numberCode;
                }
            }
            //req.setSkus(PontusTravelItemSkuInfoList);
            req.setSkus(PontusTravelItemSkuInfoList);
            
            System.out.println(JSON.toJson(PontusTravelItemSkuInfoList));
            
            AlitripTravelItemSkuPriceModifyResponse  rsp = null;
            try {
                rsp = client.execute(req, channelObj.getSessionKey());
            }catch (ApiException e) {
                throw new MyExceptionForXyz(e.getMessage());
            }
            
            HashMap<String, Object> alitripTravelItemSkuResponseMap = new HashMap<String, Object>();
            if(rsp.getBody()!=null){
                alitripTravelItemSkuResponseMap = JSON.toObject(rsp.getBody(), HashMap.class);
            }
            HashMap<String, Object> travelItemMap = (HashMap<String, Object>)alitripTravelItemSkuResponseMap.get("alitrip_travel_item_sku_price_modify_response");
            if (travelItemMap == null) {
                Map<String, Object> errorResponseMap = (Map<String, Object>)alitripTravelItemSkuResponseMap.get("error_response");
                return ReturnUtil.returnMap(0, successStr+";接口错误,错误码【"+errorResponseMap==null?"":JSON.toJson(errorResponseMap)+"】错误信息【"+errorResponseMap==null?"":JSON.toJson(errorResponseMap)+"】");
            }
        }
        
        return ReturnUtil.returnMap(1, successStr);
    }

    @Override
    public Map<String, Object> editIsNormalByNumberCode(String numberCode , int isNormal) {
        TaobaoSkuInfoDetail detailObj = (TaobaoSkuInfoDetail)commonDao.getObjectByUniqueCode("TaobaoSkuInfoDetail","numberCode", numberCode);
        detailObj.setIsNormal(isNormal);
        commonDao.update(detailObj);
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> getRelationTkviewType(String taobaoSku) {
        String queryHql = "from Sku_TkviewType st where st.sku = '"+ taobaoSku +"'";
        @SuppressWarnings("unchecked")
        List<Sku_TkviewType> list = commonDao.queryByHql(queryHql);
        String tkviewStr = "";
        for(Sku_TkviewType st : list){
            if(StringTool.isEmpty(tkviewStr)){
                tkviewStr = st.getTkviewType();
                continue;
            }
            tkviewStr =tkviewStr + "," + st.getTkviewType();
        }
        
        return ReturnUtil.returnMap(1, tkviewStr);
    }

    @Override
    public Map<String, Object> queryTkviewTypeList(String taobaoSku) {
        String skuTkviewTypeHql = "from Sku_TkviewType st where ";
        skuTkviewTypeHql += "st.sku = '"+ taobaoSku +"' ";
        @SuppressWarnings("unchecked")
        List<Sku_TkviewType> list = commonDao.queryByHql(skuTkviewTypeHql);
        
        List<String> tkviewTypeNumber = new ArrayList<>();
        for(Sku_TkviewType sku : list){
            tkviewTypeNumber.add(sku.getTkviewType());
        }
        
        String tkviewTypeHql = "from TkviewType t where ";
        tkviewTypeHql += "t.numberCode in ("+ StringTool.listToSqlString(tkviewTypeNumber) +") ";
        @SuppressWarnings("unchecked")
        List<TkviewType> tkview_typeList = commonDao.queryByHql(tkviewTypeHql);
        
        List<String> cabinNumber = new ArrayList<>();
        for(TkviewType type : tkview_typeList){
            cabinNumber.add(type.getCabin());
        }
        
        String cabinHql = "from Cabin c where c.numberCode in ("+ StringTool.listToSqlString(cabinNumber) +")";
        @SuppressWarnings("unchecked")
        List<Cabin> cabinList = commonDao.queryByHql(cabinHql);
        
        for(TkviewType type : tkview_typeList){
            for(Cabin cabin : cabinList){
                if(type.getCabin().equals(cabin.getNumberCode())){
                    type.setCabinNameCn(cabin.getNameCn());
                    break;
                }
            }
        }
        
        return ReturnUtil.returnMap(1, tkview_typeList);
    }

    @Override
    public Map<String, Object> getTkviewScotkList(String numberCode , String taobaoSku , String date) {
        TaobaoSkuInfo skuInfo=(TaobaoSkuInfo) commonDao.getObjectByUniqueCode("TaobaoSkuInfo", "numberCode", taobaoSku);
        if(skuInfo == null){
            return ReturnUtil.returnMap(0, "SKU套餐对象不存在!");
        }
        
        TaobaoBaseInfo baseInfo=(TaobaoBaseInfo) commonDao.getObjectByUniqueCode("TaobaoBaseInfo", "taobaoTravelItem", skuInfo.getTaobaoTravelItem());
        if(baseInfo == null){
            return ReturnUtil.returnMap(0, "宝贝基础信息不存在!");
        }
        
    	TaobaoSkuInfoDetail detail=(TaobaoSkuInfoDetail) commonDao.getObjectByUniqueCode("TaobaoSkuInfoDetail", "numberCode", numberCode);
    	if(detail == null){
    	    return ReturnUtil.returnMap(0, date+"的SKU库存对象不存在!");
    	}
    	
        String skuTkviewTypeHql = "from Sku_TkviewType st where st.sku = '"+ taobaoSku +"' ";
        @SuppressWarnings("unchecked")
        List<Sku_TkviewType> skuTkviewTypeList = commonDao.queryByHql(skuTkviewTypeHql);
        
        List<String> tkviewTypeNumber = new ArrayList<>();
        for(Sku_TkviewType sku : skuTkviewTypeList){
            tkviewTypeNumber.add(sku.getTkviewType());
        }
        
        String tkviewTypeHql = "from TkviewType t where ";
        tkviewTypeHql += "t.numberCode in ("+ StringTool.listToSqlString(tkviewTypeNumber) +") ";
        @SuppressWarnings("unchecked")
        List<TkviewType> tkviewTypeList = commonDao.queryByHql(tkviewTypeHql);
        
        List<String> cabinNumber = new ArrayList<>();
        for(TkviewType type : tkviewTypeList){
            cabinNumber.add(type.getCabin());
        }
        
        String tkviewHql = "from Tkview t where t.cruise = '"+ baseInfo.getCruise() +"' ";
        tkviewHql += "and t.shipmentTravelDate = '"+ date +"' ";
        tkviewHql += "and t.cabin in ("+ StringTool.listToSqlString(cabinNumber) +") ";
        @SuppressWarnings("unchecked")
        List<Tkview> tkviewList = commonDao.queryByHql(tkviewHql);
        
        List<String> tkviewNumber = new ArrayList<String>();
        for(Tkview t : tkviewList){
            tkviewNumber.add(t.getNumberCode());
        }
        
        String hql = "from Stock s where 1=1 ";
        hql += "and s.tkview in ("+ StringTool.listToSqlString(tkviewNumber) +") ";
        hql += "order by s.tkview ";
        @SuppressWarnings("unchecked")
        List<Stock> stockList = commonDao.queryByHql(hql);
        
        List<String> providerNumber = new ArrayList<String>();
        for(Stock s : stockList){
            providerNumber.add(s.getProvider());
        }
        String providerHql = "from Provider p where 1=1 ";
        providerHql += "and p.numberCode in ("+ StringTool.listToSqlString(providerNumber) +") ";
        @SuppressWarnings("unchecked")
        List<Provider> providerList = commonDao.queryByHql(providerHql);
        
        for(Stock s : stockList){
            for(Tkview tkviewObj : tkviewList){
                if(s.getTkview().equals(tkviewObj.getNumberCode())){
                    s.setTkviewNameCn(tkviewObj.getNameCn());
                    tkviewObj.getStocks().add(s);
                    break;
                }
            }
            for(Provider p : providerList){
                if(s.getProvider().equals(p.getNumberCode())){
                    s.setProviderNameCn(p.getNameCn());
                    s.setProviderMark(p.getMark());
                    break;
                }
            }
        }
        
        for(Tkview t : tkviewList){
            Cabin cabinObj = (Cabin)commonDao.getObjectByUniqueCode("Cabin", "numberCode", t.getCabin());
            for(Stock st : t.getStocks()){
                if(st.getTkview().equals(t.getNumberCode())){
                    st.setStock(st.getStock().multiply(cabinObj.getMaxVolume()));
                }
            }
        }
        
        List<Tkview> tkviews=new ArrayList<Tkview>();
        for(Tkview t:tkviewList){
            if(t.getStocks().size()>0){
                Collections.sort(t.getStocks());
                t.setCost(t.getStocks().get(0).getCost());
                t.setStock(t.getStocks().get(0).getStock());
                tkviews.add(t);
            }
        }
        Collections.sort(tkviews);
        
        List<Stock> resultStockList=new ArrayList<Stock>();
        for(Tkview t:tkviews){
            resultStockList.addAll(t.getStocks());
        }
        
        String priceHql = "from PriceStrategy";
        @SuppressWarnings("unchecked")
        List<PriceStrategy> priceList = commonDao.queryByHql(priceHql);
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("stockList", resultStockList);
        map.put("priceList", priceList);
        map.put("detail", detail);
        
        return ReturnUtil.returnMap(1, map);
    }

    @Override
    public Map<String, Object> submitSkuDetailsOper(String numberCode , String outId , BigDecimal price ,
                                                    int priceType , String stock) {
        
        boolean pd = false;
        if(StringTool.isEmpty(stock)){
            return ReturnUtil.returnMap(0, "库存不能为空!");
        }else{
            if(!"现询".equals(stock)){
                Pattern pattern = Pattern.compile("[0-9]*"); 
                Matcher isNum = pattern.matcher(stock);
                if(!isNum.matches()){
                    return ReturnUtil.returnMap(0, "库存只能填0-9的数字或现询!");
                }
                pd = true;
            }
        }
        
        if(StringTool.isEmpty(numberCode)){
            return ReturnUtil.returnMap(0, "SKU日历库存不存在!");
        }
        
        TaobaoSkuInfoDetail skuStockObj = (TaobaoSkuInfoDetail)commonDao.getObjectByUniqueCode("TaobaoSkuInfoDetail", "numberCode", numberCode);
        skuStockObj.setOutId(outId);
        skuStockObj.setPriceType(priceType);
        skuStockObj.setPrice(price);
        if(pd){
            skuStockObj.setStock(new BigDecimal(stock).intValue());
        }
        skuStockObj.setIsUpdate(1);
        skuStockObj.setAlterDate(new Date());
        commonDao.update(skuStockObj);
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> getSkuInfo(String baseInfo) {
       TaobaoBaseInfo baseObj = (TaobaoBaseInfo)commonDao.getObjectByUniqueCode("TaobaoBaseInfo", "numberCode", baseInfo);
       
       String skuHql = "from TaobaoSkuInfo b where b.taobaoTravelItem = '"+ baseObj.getTaobaoTravelItem() +"' ";
       @SuppressWarnings("unchecked")
       List<TaobaoSkuInfo> skuList = commonDao.queryByHql(skuHql);
       
       return ReturnUtil.returnMap(1, skuList);
    }

    @Override
    public Map<String, Object> queryTaobaoLogByCruiseWorkList(String cruise) {
        
        Cruise cruisefoObj = (Cruise)commonDao.getObjectByUniqueCode("Cruise", "numberCode", cruise);
        
        String taobaoBaseInfoHql="from TaobaoBaseInfo t where 1 = 1 and t.cruise='"+cruise+"'";
        
        @SuppressWarnings("unchecked")
        List<TaobaoBaseInfo> taobaoBaseInfoList = commonDao.queryByHql(taobaoBaseInfoHql);
        
        Set<String> taobaoTravelItemSet= new HashSet<String>();
        for (TaobaoBaseInfo taobaoBaseInfo : taobaoBaseInfoList) {
            taobaoTravelItemSet.add(taobaoBaseInfo.getTaobaoTravelItem());
        }
        
        String taobaoTravelItemHql="from TaobaoTravelItem t where 1 = 1 and t.numberCode in ("+ StringTool.listToSqlString(taobaoTravelItemSet) +") ";
        @SuppressWarnings("unchecked")
        List<TaobaoTravelItem> taobaoTravelItemList = commonDao.queryByHql(taobaoTravelItemHql);
        
        Set<String> channelSet= new HashSet<String>();
        Set<String> taobaoTravelItemNumberCodeSet= new HashSet<String>();
        
        for (TaobaoTravelItem taobaoTravelItem : taobaoTravelItemList) {
            channelSet.add(taobaoTravelItem.getChannel());
            taobaoTravelItemNumberCodeSet.add(taobaoTravelItem.getNumberCode());
        }
        String channelHql="from Channel c where 1 = 1 and c.numberCode in ("+ StringTool.listToSqlString(channelSet) +") ";
        
        Map<String, Object> taobaoTravelItemMap = new HashMap<String, Object>();
        @SuppressWarnings("unchecked")
        List<Channel> channelList= commonDao.queryByHql(channelHql);
        for (TaobaoTravelItem taobaoTravelItem : taobaoTravelItemList) {
            for (Channel channel : channelList) {
                if(taobaoTravelItem.getChannel().equals(channel.getNumberCode())){
                    taobaoTravelItem.setChannelNameCn(channel.getNameCn());
                    break;
                }
            }
            taobaoTravelItemMap.put(taobaoTravelItem.getNumberCode(), taobaoTravelItem.getChannelNameCn());
        }
        
        List<Map<String, Object>> dataList =new ArrayList<Map<String, Object>>();
        List<String> numberCodelList = new ArrayList<>();
        for (Map.Entry<String, Object> entry : taobaoTravelItemMap.entrySet()) { 
          
            String skuHql = "from TaobaoSkuInfo t where t.taobaoTravelItem = '"+entry.getKey()+"' ";
            @SuppressWarnings("unchecked")
            List<TaobaoSkuInfo> skuList = commonDao.queryByHql(skuHql);
            
            List<String> skuNumber = new ArrayList<>();
            for(TaobaoSkuInfo s : skuList){
                skuNumber.add(s.getNumberCode());
            }
            
            /* 查询价格被锁定的SKU日历库存 */
            String detailHql = "from TaobaoSkuInfoDetail d WHERE d.taobaoSkuInfo in ("+ StringTool.listToSqlString(skuNumber) +") ";
            detailHql += "and d.isLock = 1 ";
            detailHql += "order by d.taobaoSkuInfo ";
            @SuppressWarnings("unchecked")
            List<TaobaoSkuInfoDetail> detailLiist = commonDao.queryByHql(detailHql);
            
            for(TaobaoSkuInfoDetail d : detailLiist){
                numberCodelList.add(d.getNumberCode());
                for(TaobaoSkuInfo s : skuList){
                   if(d.getTaobaoSkuInfo().equals(s.getNumberCode())){
                       d.setPackageName(s.getPackageName());
                       break;
                   }
                }
            }
            for (TaobaoSkuInfoDetail taobaoSkuInfoDetail : detailLiist) {
                Map<String, Object> taobaoSkuInfoDetailMap = new HashMap<String, Object>();
                taobaoSkuInfoDetailMap.put("numberCode", taobaoSkuInfoDetail.getNumberCode());
                taobaoSkuInfoDetailMap.put("date", taobaoSkuInfoDetail.getDate());
                taobaoSkuInfoDetailMap.put("price", taobaoSkuInfoDetail.getPrice());
                taobaoSkuInfoDetailMap.put("stock", taobaoSkuInfoDetail.getStock());
                taobaoSkuInfoDetailMap.put("isLock", taobaoSkuInfoDetail.getIsLock());
                taobaoSkuInfoDetailMap.put("packageName", taobaoSkuInfoDetail.getPackageName());
                taobaoSkuInfoDetailMap.put("channelNameCn", entry.getValue());
                taobaoSkuInfoDetailMap.put("cruiseNameCn", cruisefoObj.getNameCn());
                dataList.add(taobaoSkuInfoDetailMap);
            }
          }

        String logSql = "SELECT w.iidd AS iidd,w.number_code AS numberCode,w.table_name AS tableName,";
        logSql += "w.username AS username,w.`value` AS value,w.remark AS remark,w.add_date AS addDate ";
        logSql += "FROM (SELECT l.iidd,l.number_code,l.table_name,l.username,l.`value`,l.remark,l.add_date ";
        logSql += "FROM taobao_log_work l ORDER BY l.add_date DESC) w ";
        logSql += "WHERE w.table_name = 'detailLockTable' ";
        logSql += "AND w.`value` IN ("+ StringTool.listToSqlString(numberCodelList) +") ";
        logSql += "GROUP BY w.`value` ";
        logSql += "ORDER BY w.add_date DESC ";
        SQLQuery query = commonDao.getSqlQuery(logSql);
        query.addScalar("iidd").addScalar("numberCode").addScalar("tableName")
        .addScalar("username").addScalar("value").addScalar("remark")
        .addScalar("addDate")
        .setResultTransformer(Transformers.aliasToBean(TaobaoLogWork.class));
        @SuppressWarnings("unchecked")
        List<TaobaoLogWork> logWorkList = query.list();
        
        Map<String, Object> map = new HashMap<>();
        map.put("logWorkList", logWorkList);
        map.put("dataList", dataList);
        
        return ReturnUtil.returnMap(1, map);
    }

    @Override
    public Map<String, Object> batchUploadSkuOper(String cruise) {
        String baseHql = "from TaobaoBaseInfo where cruise = '"+ cruise +"' ";
        @SuppressWarnings("unchecked")
        List<TaobaoBaseInfo> baseList = commonDao.queryByHql(baseHql);
        
        List<String> travelItemList = new ArrayList<>();
        for(TaobaoBaseInfo b : baseList){
            travelItemList.add(b.getTaobaoTravelItem());
        }
        
        String itemHql = "from TaobaoTravelItem where numberCode in ("+ StringTool.listToSqlString(travelItemList) +") ";
        @SuppressWarnings("unchecked")
        List<TaobaoTravelItem> itemList = commonDao.queryByHql(itemHql);
        
        for(TaobaoTravelItem taobaoTravelItemObj : itemList){
            Channel channelObj = (Channel)commonDao.getObjectByUniqueCode("Channel", "numberCode", taobaoTravelItemObj.getChannel());
            if (channelObj == null) {
                return ReturnUtil.returnMap(0, "渠道不存在!");
            }
            
            if(channelObj.getNumberCode().equals("meituan")){
               continue; 
            }
            
            String appKey = channelObj.getAppKey();
            if (!StringTool.isNotNull(appKey)) {
                return ReturnUtil.returnMap(0, "appKey错误!");
            }
            String appSecret = channelObj.getAppSecret(); 
            if (!StringTool.isNotNull(appSecret)) {
                return ReturnUtil.returnMap(0, "appSecret错误!");
            }
            
            String skuHql = "from TaobaoSkuInfo where taobaoTravelItem = '"+taobaoTravelItemObj.getNumberCode()+"' ";
            @SuppressWarnings("unchecked")
            List<TaobaoSkuInfo> taobaoSkuInfoList = commonDao.queryByHql(skuHql);
            
            TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", appKey, appSecret);
            
            AlitripTravelItemSkuOverrideRequest req = new AlitripTravelItemSkuOverrideRequest();
            if (StringTool.isEmpty(taobaoTravelItemObj.getItemId())) {
                return ReturnUtil.returnMap(0, "宝贝未上传!");
            }
            req.setItemId(Long.valueOf(taobaoTravelItemObj.getItemId()));
            List<PontusTravelItemSkuInfo> PontusTravelItemSkuInfoList = new ArrayList<PontusTravelItemSkuInfo>();
            
            for (TaobaoSkuInfo skus : taobaoSkuInfoList) {
                if("其他套餐".equals(skus.getPackageName())){
                    continue;
                }
                
                PontusTravelItemSkuInfo packageObj = new PontusTravelItemSkuInfo();
                packageObj.setPackageName(skus.getPackageName());
                packageObj.setOuterSkuId(skus.getNumberCode());
                
                List<PontusTravelPrices> PontusTravelPricesList = new ArrayList<PontusTravelPrices>();
                
                //上传的宝贝不能超过334天
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                cal.add(Calendar.DATE, 334);
                String maxDate = (new SimpleDateFormat("yyyy-MM-dd")).format(cal.getTime());
                String detailsHql = "from TaobaoSkuInfoDetail where taobaoSkuInfo = '"+skus.getNumberCode()+"' ";
                detailsHql += "and date <= '"+ maxDate +"' ";
                @SuppressWarnings("unchecked")
                List<TaobaoSkuInfoDetail> taobaoSkuInfoDetailObj = commonDao.queryByHql(detailsHql);
                for (TaobaoSkuInfoDetail t : taobaoSkuInfoDetailObj) {
                    PontusTravelPrices PontusTravelPricesObj = new PontusTravelPrices();
                    PontusTravelPricesObj.setPriceType(Long.valueOf(t.getPriceType()));
                    PontusTravelPricesObj.setStock(Long.valueOf(t.getStock()));
                    PontusTravelPricesObj.setPrice(t.getPrice().longValue());
                    PontusTravelPricesObj.setDate(t.getDate());
                    PontusTravelPricesObj.setOuterPriceId(t.getOutId());
                    PontusTravelPricesList.add(PontusTravelPricesObj);
                    
                    t.setFlag(0);
                    t.setAlterDate(new Date());
                    commonDao.update(t);
                }
                //packageObj.setPackageDesc("套餐描述");
                packageObj.setPrices(PontusTravelPricesList);
                PontusTravelItemSkuInfoList.add(packageObj);
            }
            
            req.setSkus(PontusTravelItemSkuInfoList);
            
            AlitripTravelItemSkuOverrideResponse rsp = null;
            try{
                rsp = client.execute(req, channelObj.getSessionKey());
            }catch (ApiException e) {
                throw new MyExceptionForXyz(e.getMessage());
            }
            
            @SuppressWarnings("unchecked")
            HashMap<String, Object> alitripTravelItemSkuResponseMap = JSON.toObject(rsp.getBody(), HashMap.class);
            
            @SuppressWarnings("unchecked")
            HashMap<String, Object> travelItemMap = (HashMap<String, Object>)alitripTravelItemSkuResponseMap.get("alitrip_travel_item_sku_override_response");
           
            if (travelItemMap == null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> errorResponseMap = (Map<String, Object>)alitripTravelItemSkuResponseMap.get("error_response");
                return ReturnUtil.returnMap(0, "接口错误,错误码【"+errorResponseMap.get("msg").toString()+"】错误信息【"+errorResponseMap.get("sub_msg").toString()+"】");
            }
            commonDao.flush();
            
            String updateSql = "UPDATE taobao_sku_info_detail SET is_update=0 WHERE taobao_sku_info IN (SELECT number_code FROM taobao_sku_info WHERE taobao_travel_item = '"+ taobaoTravelItemObj.getNumberCode() +"') ";
            commonDao.getSqlQuery(updateSql).executeUpdate();
        }
        
        return ReturnUtil.returnMap(1, null);
    }

}