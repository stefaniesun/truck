package xyz.work.meituan.svc.imp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.JSON;
import xyz.filter.ReturnUtil;
import xyz.filter.RmiUtil;
import xyz.util.Constant;
import xyz.util.StringTool;
import xyz.work.meituan.model.BaseInfo;
import xyz.work.meituan.model.SkuInfo;
import xyz.work.meituan.model.SkuStockInfo;
import xyz.work.meituan.svc.QueryTaobaoSvc;
import xyz.work.meituan.svc.Config.MeituanUtil;
import xyz.work.resources.model.Stock;
import xyz.work.taobao.model.TaobaoBaseInfo;
import xyz.work.taobao.model.TaobaoSkuInfo;
import xyz.work.taobao.model.TaobaoSkuInfoDetail;
import xyz.work.taobao.model.TaobaoTravelItem;

@Service
public class QueryTaobaoSvcImp implements QueryTaobaoSvc{
    @Autowired
    private CommonDao commonDao;
    
    @Autowired
    private RmiUtil rmiUtil;

    @Override
    public Map<String, Object> queryTaobaoBaseInfoList() {
        StringBuffer baseInfoSql = new StringBuffer("SELECT t.number_code AS travelItem, ");
        baseInfoSql.append("t.channel AS channel,t.channel_name_cn AS channelNameCn, ");
        baseInfoSql.append("c.cruise_company AS company,c.cruise_line AS cruiseLine, ");
        baseInfoSql.append("c.ship_fee_include AS shipFeeInclude,c.ship_up AS shipUp, ");
        baseInfoSql.append("c.ship_down AS shipDown,b.number_code AS numberCode, ");
        baseInfoSql.append("b.title AS title,b.cruise AS cruise, ");
        baseInfoSql.append("s.name_cn AS cruiseNameCn,b.trip_max_days AS tripMaxDays, ");
        baseInfoSql.append("b.accom_nights AS accomNights,b.from_locations AS fromLocations, ");
        baseInfoSql.append("b.prov AS prov,b.city AS city,b.taobao_desc AS taobaoDesc, ");
        baseInfoSql.append("b.to_locations AS toLocations,b.sub_titles AS subTitles, ");
        baseInfoSql.append("b.pic_urls AS picUrls,b.item_type AS itemType ");
        baseInfoSql.append("FROM taobao_base_info b ");
        baseInfoSql.append("LEFT JOIN taobao_cruise_item_ext c ");
        baseInfoSql.append("ON b.taobao_travel_item = c.taobao_travel_item ");
        baseInfoSql.append("LEFT JOIN taobao_travel_item t ");
        baseInfoSql.append("ON t.number_code = b.taobao_travel_item "); 
        baseInfoSql.append("LEFT JOIN cruise s ");
        baseInfoSql.append("ON b.cruise = s.number_code ");
        SQLQuery query = commonDao.getSqlQuery(baseInfoSql.toString());
        query.addScalar("travelItem").addScalar("channel").addScalar("channelNameCn")
        .addScalar("company").addScalar("cruiseLine").addScalar("shipFeeInclude")
        .addScalar("shipUp").addScalar("shipDown").addScalar("numberCode")
        .addScalar("title").addScalar("cruise").addScalar("cruiseNameCn")
        .addScalar("tripMaxDays").addScalar("accomNights")
        .addScalar("fromLocations").addScalar("prov").addScalar("city")
        .addScalar("taobaoDesc").addScalar("toLocations").addScalar("subTitles")
        .addScalar("picUrls").addScalar("itemType")
        .setResultTransformer(Transformers.aliasToBean(BaseInfo.class));
        @SuppressWarnings("unchecked")
        List<BaseInfo> baseInfoList = query.list();
        
        return ReturnUtil.returnMap(1, baseInfoList);
    }

    @Override
    public Map<String, Object> queryTaobaoSkuList(String travelItem) {
        if(StringTool.isEmpty(travelItem)){
            return ReturnUtil.returnMap(0, "请选择宝贝信息!");
        }
        
        String skuSql = "SELECT s.number_code AS numberCode,s.package_name AS packageName, ";
        skuSql += "s.taobao_travel_item AS travelItem FROM taobao_sku_info s ";
        skuSql += " WHERE s.taobao_travel_item = '"+ travelItem +"'";
        SQLQuery query = commonDao.getSqlQuery(skuSql);
        query.addScalar("numberCode").addScalar("packageName").addScalar("travelItem")
        .setResultTransformer(Transformers.aliasToBean(SkuInfo.class));
        @SuppressWarnings("unchecked")
        List<SkuInfo> skuInfoList = query.list();
        
        for(SkuInfo skuObj : skuInfoList){
            String skuStockSql = "SELECT d.taobao_sku_info AS skuInfo, ";
            skuStockSql += "d.number_code AS numberCode, ";
            skuStockSql += "d.date AS date, ";
            skuStockSql += "d.stock AS stock , ";
            skuStockSql += "d.price AS price, ";
            skuStockSql += "d.price_type AS priceType ";
            skuStockSql += "FROM taobao_sku_info_detail d  ";
            skuStockSql += "WHERE d.taobao_sku_info = '"+ skuObj.getNumberCode() +"' ";
            SQLQuery stockQuery = commonDao.getSqlQuery(skuStockSql);
            stockQuery.addScalar("skuInfo").addScalar("numberCode").addScalar("date")
            .addScalar("stock").addScalar("price").addScalar("priceType")
            .setResultTransformer(Transformers.aliasToBean(SkuStockInfo.class));
            @SuppressWarnings("unchecked")
            List<SkuStockInfo> skuStockInfoList = stockQuery.list();
            
            skuObj.setStockList(skuStockInfoList);
        }
        
        return ReturnUtil.returnMap(1, skuInfoList);
    }
    
    @Override
    public Map<String, Object> queryTaobaoSkuStockList(String skuInfo) {
        if(StringTool.isEmpty(skuInfo)){
            return ReturnUtil.returnMap(0, "SKU套餐编号不能为空!");
        }
        
        TaobaoSkuInfo skuInfoObj = (TaobaoSkuInfo)commonDao.getObjectByUniqueCode("TaobaoSkuInfo", "numberCode", skuInfo);
        if(skuInfoObj == null){
            return ReturnUtil.returnMap(0, "SKU套餐不存在!");
        }
        
        String stockHql = "from TaobaoSkuInfoDetail where taobaoSkuInfo = '"+ skuInfo +"' ";
        @SuppressWarnings("unchecked")
        List<TaobaoSkuInfoDetail> stockList = commonDao.queryByHql(stockHql);
       
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("skuInfo", skuInfoObj);
        map.put("stockList", stockList);
        
        return ReturnUtil.returnMap(1, map);
    }

    @Override
    public Map<String, Object> editTaobaoSkuStock(String skuInfo, String date, int num) {
        if(StringTool.isEmpty(skuInfo)){
            return ReturnUtil.returnMap(0, "请选择SKU信息!");
        }
        
        TaobaoSkuInfo skuObj = (TaobaoSkuInfo)commonDao.getObjectByUniqueCode("TaobaoSkuInfo", "numberCode", skuInfo);
        if(skuObj == null){
            return ReturnUtil.returnMap(0, "SKU套餐不存在!");
        }
        String baseInfoHql = "from TaobaoBaseInfo b where b.taobaoTravelItem = '"+ skuObj.getTaobaoTravelItem() +"' ";
        @SuppressWarnings("unchecked")
        List<TaobaoBaseInfo> baseList = commonDao.queryByHql(baseInfoHql);
        if(baseList.size() < 1){
            return ReturnUtil.returnMap(0, "淘宝宝贝不存在!");
        }
        TaobaoBaseInfo baseInfoObj = baseList.get(0);
        
        String hql = "from TaobaoSkuInfoDetail d where d.taobaoSkuInfo = '"+ skuInfo +"' "; 
        hql += "and d.date = '"+ date +"' ";
        @SuppressWarnings("unchecked")
        List<TaobaoSkuInfoDetail> detailList = commonDao.queryByHql(hql);
        if(detailList.size() < 1){
            return ReturnUtil.returnMap(0, "该套餐下的【"+ date +"】的库存不存在!");
        }
        TaobaoSkuInfoDetail skuStockObj = detailList.get(0);
        if(skuStockObj.getStock() < num){
            return ReturnUtil.returnMap(0, "SKU下的【"+ date +"】库存不足!");
        }
        
        if(StringTool.isEmpty(baseInfoObj.getCruise())){
            String skuStockUpdate = "UPDATE taobao_sku_info_detail SET ";
            skuStockUpdate += "stock=(stock-"+ num +"),alter_date=NOW() ";
            skuStockUpdate += "WHERE taobao_sku_info = '"+ skuInfo +"' AND date = '"+ date +"' ";
            int updateSku = commonDao.getSqlQuery(skuStockUpdate).executeUpdate();
            return ReturnUtil.returnMap(1, "SKU库存影响"+ updateSku +"行");
        }
        
        String stockSql = "from Stock s where s.tkview = '"+ skuStockObj.getOutId() +"' ";
        @SuppressWarnings("unchecked")
        List<Stock> stockList = commonDao.queryByHql(stockSql);
        if(stockList.size() < 1){
            return ReturnUtil.returnMap(0, "没有单品库存信息!");
        }
        
        Collections.sort(stockList);
        Stock stockObj = stockList.get(0);
        if(stockObj.getStock().compareTo(BigDecimal.ZERO) < 1){
            return ReturnUtil.returnMap(0, "单品库存已售罄!");
        }
        if(stockObj.getStock().compareTo(new BigDecimal(num)) == -1){
            return ReturnUtil.returnMap(0, "单品库存不足!");
        }
        
        String stockUpdate = "UPDATE stock SET stock=(stock-"+ num +"),alter_date=NOW() ";
        stockUpdate += "WHERE number_code = '"+ stockObj.getNumberCode() +"' ";
        int updateStock = commonDao.getSqlQuery(stockUpdate).executeUpdate();
        
        String skuStockUpdate = "UPDATE taobao_sku_info_detail SET ";
        skuStockUpdate += "stock=(stock-"+ num +"),alter_date=NOW() ";
        skuStockUpdate += "WHERE taobao_sku_info = '"+ skuInfo +"' AND date = '"+ date +"' ";
        int updateSku = commonDao.getSqlQuery(skuStockUpdate).executeUpdate();
        
        return ReturnUtil.returnMap(1, "单品库存影响"+ updateStock +"行;SKU库存影响"+ updateSku +"行");
        
    }

    @Override
    public Map<String, Object> addProduct(String channel , String travelItem) {
        
        String hql = "from TaobaoTravelItem t where t.channel='"+ channel +"' "; 
        if(StringTool.isNotNull(travelItem)){
            hql += "and t.numberCode in ("+ StringTool.StrToSqlString(travelItem) +") ";
        }
        @SuppressWarnings("unchecked")
        List<TaobaoTravelItem> travelItemList = commonDao.queryByHql(hql);
       
        List<String> travelItemNumber = new ArrayList<String>();
        for(TaobaoTravelItem t : travelItemList){
            travelItemNumber.add(t.getNumberCode());
        }
        
        String baseInfoHql = "from TaobaoBaseInfo b where b.taobaoTravelItem in ("+ StringTool.listToSqlString(travelItemNumber) +") ";
        @SuppressWarnings("unchecked")
        List<TaobaoBaseInfo> baseInfoList = commonDao.queryByHql(baseInfoHql);
        
        String skuHql = "from TaobaoSkuInfo s where s.taobaoTravelItem in ("+ StringTool.listToSqlString(travelItemNumber) +") ";
        @SuppressWarnings("unchecked")
        List<TaobaoSkuInfo> skuList = commonDao.queryByHql(skuHql);
        
        List<String> skuNumber = new ArrayList<>();
        for(TaobaoSkuInfo sku : skuList){
            skuNumber.add(sku.getNumberCode());
        }
        String stockHql = "from TaobaoSkuInfoDetail d where d.taobaoSkuInfo in ("+ StringTool.listToSqlString(skuNumber) +") ";
        stockHql += "and d.date > NOW() ";
        @SuppressWarnings("unchecked")
        List<TaobaoSkuInfoDetail> stocklist = commonDao.queryByHql(stockHql);
        
        List<Map<String, Object>> productMapList = new ArrayList<Map<String, Object>>();    
        for(TaobaoBaseInfo baseObj : baseInfoList){
            Map<String, Object> productMap = new HashMap<String, Object>();
            String productCode = baseObj.getNumberCode(); //产品编号
            String nameCn = baseObj.getTitle();           //产品名称
            
            if("mafengwo".equals(channel)){
                productMap.put("partnerid", "10103");
            }
            /*else{
                productMap.put("partnerid", MeituanUtil.partnerid);
            }*/
            productMap.put("productCode", productCode);
            productMap.put("nameCn", nameCn);
            
            List<Map<String, Object>> skus = new ArrayList<Map<String, Object>>(); //Sku集合
            for(TaobaoSkuInfo skuObj : skuList){
                if(baseObj.getTaobaoTravelItem().equals(skuObj.getTaobaoTravelItem())){
                    Map<String, Object> skuMap = new HashMap<>();
                    String skuCode = skuObj.getNumberCode();    //Sku编号
                    String skuNameCn = skuObj.getPackageName(); //Sku中文名
                    skuMap.put("skuCode", skuCode);
                    skuMap.put("skuNameCn", skuNameCn);
                    
                    int volume = 2;
                    if("mafengwo".equals(channel)){
                        skuCode += "-"+baseObj.getTripMaxDays();
                        if(skuNameCn.indexOf("单") != -1){
                            volume = 1;
                        }else if(skuNameCn.indexOf("二") != -1 || skuNameCn.indexOf("双") != -1){
                            volume = 2;
                        }else if(skuNameCn.indexOf("三") != -1){
                            volume = 3;
                        }
                    }
                    
                    List<Map<String, Object>> skuDetails = new ArrayList<Map<String, Object>>(); //库存集合
                    for(TaobaoSkuInfoDetail stockObj : stocklist){
                        if(skuObj.getNumberCode().equals(stockObj.getTaobaoSkuInfo())){
                            Map<String, Object> stockMap = new HashMap<>();
                            String detailCode = stockObj.getNumberCode();     //库存编号
                            String dateInfo = stockObj.getDate().toString();  //日期 格式:yyyy-MM-dd
                            int stockCount = stockObj.getStock();             //库存数量
                            BigDecimal price = stockObj.getPrice();           //价格
                            
                            stockMap.put("detailCode", detailCode);
                            stockMap.put("dateInfo", dateInfo);
                            stockMap.put("stockCount", stockCount);
                            
                            if("mafengwo".equals(channel)){
                                stockMap.put("volume", volume);
                            }
                            stockMap.put("price", price);
                            skuDetails.add(stockMap);
                        }
                    }
                    skuMap.put("skuDetails", skuDetails);
                    skus.add(skuMap);
                }
            }
            productMap.put("skus", skus);
            productMapList.add(productMap);
        }
        
        if(productMapList.size() < 1){
            String channelMess = "meituan".equals(channel)==true?"美团":"马蜂窝";
            return ReturnUtil.returnMap(0, channelMess+"渠道下没有宝贝数据!");
        }
        
        String productList = JSON.toJson(productMapList);
        System.out.println(productList);
        
        Map<String, Object> map = new HashMap<>();
        map.put("productList", productList);
        
        String url = "";
        if("meituan".equals(channel)){  //美团
            //http://meituan.api.maytek.cn/xt_meituan/MeituanCmsInterfaWS/pushProduct.api //文档接口
            url = "http://meituan.api.maytek.cn/xt_meituan/MeituanCmsInterfaWS/pushProduct.api";
        }else{  //马蜂窝
            //url = "http://192.168.1.123:8080/xt_mafengwo/MafengwoCmsInterfaWS/pushProduct.api";
            url="http://mafengwo.api.maytek.cn/xt_mafengwo/MafengwoCmsInterfaWS/pushProduct.api";
        }
        
        @SuppressWarnings("unchecked")
        Map<String, Object> resultMap = (Map<String, Object>)rmiUtil.loadData(url, map);
        
        System.out.println(resultMap);
        
        if((Integer)resultMap.get(Constant.result_status) != 1){
            return resultMap;
        }
            
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> pushProductOper(String channel , String baseInfo, String skuJson) {
        if(StringTool.isEmpty(skuJson)){
            return ReturnUtil.returnMap(0, "请选择SKU套餐!");
        }
        
        TaobaoBaseInfo baseObj = (TaobaoBaseInfo)commonDao.getObjectByUniqueCode("TaobaoBaseInfo", "numberCode", baseInfo);
        if(baseObj == null){
            return ReturnUtil.returnMap(0, "宝贝基础信息对象不存在!");
        }
        
        @SuppressWarnings("unchecked")
        List<String> numberList = JSON.toObject(skuJson, ArrayList.class);
        
        String skuHql = "from TaobaoSkuInfo s where s.taobaoTravelItem = '"+ baseObj.getTaobaoTravelItem() +"' ";
        skuHql += "and s.numberCode in ("+ StringTool.listToSqlString(numberList) +") ";
        @SuppressWarnings("unchecked")
        List<TaobaoSkuInfo> skuList = commonDao.queryByHql(skuHql);
        
        if(skuList.size() < 1){
            return ReturnUtil.returnMap(0, "SKU套餐信息不存在!");
        }
        
        List<String> skuNumber = new ArrayList<>();
        for(TaobaoSkuInfo sku : skuList){
            skuNumber.add(sku.getNumberCode());
        }
        String stockHql = "from TaobaoSkuInfoDetail d where d.taobaoSkuInfo in ("+ StringTool.listToSqlString(skuNumber) +") ";
        stockHql += "and d.date > NOW() ";
        @SuppressWarnings("unchecked")
        List<TaobaoSkuInfoDetail> stocklist = commonDao.queryByHql(stockHql);
        
        List<Map<String, Object>> productMapList = new ArrayList<Map<String, Object>>();    
        Map<String, Object> productMap = new HashMap<String, Object>();
        String productCode = baseObj.getNumberCode(); //产品编号
        String nameCn = baseObj.getTitle(); //产品名称
        String remark = baseObj.getRemark();  //备注
        
        if("mafengwo".equals(channel)){
            productMap.put("partnerid", "10509");
        }else{
            productMap.put("partnerid", MeituanUtil.partnerid);
        }
        productMap.put("productCode", productCode);
        productMap.put("nameCn", nameCn);
        productMap.put("remark", remark);
        
        List<Map<String, Object>> skus = new ArrayList<Map<String, Object>>(); //Sku集合
        for(TaobaoSkuInfo skuObj : skuList){
            if(baseObj.getTaobaoTravelItem().equals(skuObj.getTaobaoTravelItem())){
                Map<String, Object> skuMap = new HashMap<>();
                String skuCode = skuObj.getNumberCode();    //Sku编号
                String skuNameCn = skuObj.getPackageName(); //Sku中文名
                
                if("mafengwo".equals(channel)){
                    skuCode += "-"+baseObj.getTripMaxDays();
                }
                skuMap.put("skuCode", skuCode);
                skuMap.put("skuNameCn", skuNameCn);
                
                int volume = 2;
                if("mafengwo".equals(channel)){
                    if(skuNameCn.indexOf("单") != -1){
                        volume = 1;
                    }else if(skuNameCn.indexOf("二") != -1 || skuNameCn.indexOf("双") != -1){
                        volume = 2;
                    }else if(skuNameCn.indexOf("三") != -1){
                        volume = 3;
                    }else if(skuNameCn.indexOf("四") != -1){
                        volume = 4;
                    }
                }
                
                List<Map<String, Object>> skuDetails = new ArrayList<Map<String, Object>>(); //库存集合
                for(TaobaoSkuInfoDetail stockObj : stocklist){
                    if(skuObj.getNumberCode().equals(stockObj.getTaobaoSkuInfo())){
                        Map<String, Object> stockMap = new HashMap<>();
                        String detailCode = stockObj.getNumberCode(); //库存编号
                        String dateInfo = stockObj.getDate().toString();  //日期 格式:yyyy-MM-dd
                        int stockCount = stockObj.getStock();  //库存数量
                        
                        BigDecimal price = stockObj.getPrice();  //价格
                        stockMap.put("detailCode", detailCode);
                        stockMap.put("dateInfo", dateInfo);
                        stockMap.put("stockCount", stockCount);
                        if("mafengwo".equals(channel)){
                            stockMap.put("volume", volume);
                        }
                        stockMap.put("price", price);
                        skuDetails.add(stockMap);
                    }
                }
                skuMap.put("skuDetails", skuDetails);
                skus.add(skuMap);
            }
        }
        productMap.put("skus", skus);
        productMapList.add(productMap);
        
        String productList = JSON.toJson(productMapList);
        System.out.println(productList);
        Map<String, Object> map = new HashMap<>();
        map.put("productList", productList);
       
        String url = "";
        if("meituan".equals(channel)){ //美团
            //http://meituan.api.maytek.cn/xt_meituan/MeituanCmsInterfaWS/pushProduct.api //文档接口
            //http://192.168.1.31:8080/xt_meituan/MeituanCmsInterfaWS/pushProduct.api //接口
            url = "http://meituan.api.maytek.cn/xt_meituan/MeituanCmsInterfaWS/pushProduct.api";
        }else{ //马蜂窝
            //http://192.168.1.121:8080/xt_mafengwo/MafengwoCmsInterfaWS/pushProduct.api
            //http://mafengwo.api.maytek.cn/xt_mafengwo/MafengwoCmsInterfaWS/pushProduct.api
            url="http://192.168.1.121:8086/xt_mafengwo/MafengwoCmsInterfaWS/pushProduct.api";
        }
       
        @SuppressWarnings("unchecked")
        Map<String, Object> resultMap = (Map<String, Object>)rmiUtil.loadData(url, map);
        
        System.out.println(resultMap);
        
        if((Integer)resultMap.get(Constant.result_status) != 1){
            return resultMap;
        }
            
        return ReturnUtil.returnMap(1, null);
        
    }

}