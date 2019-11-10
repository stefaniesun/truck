package xyz.work.taobao.svc.imp;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlitripTravelItemSkuPriceModifyRequest;
import com.taobao.api.response.AlitripTravelItemSkuPriceModifyResponse;

import xyz.dao.CommonDao;
import xyz.exception.MyExceptionForXyz;
import xyz.filter.JSON;
import xyz.filter.MyRequestUtil;
import xyz.filter.ReturnUtil;
import xyz.util.ConstantMsg;
import xyz.util.DateUtil;
import xyz.util.StringTool;
import xyz.util.UUIDUtil;
import xyz.work.resources.model.Channel;
import xyz.work.taobao.model.TaobaoBaseInfo;
import xyz.work.taobao.model.TaobaoLogWork;
import xyz.work.taobao.model.TaobaoSkuInfo;
import xyz.work.taobao.model.TaobaoSkuInfoDetail;
import xyz.work.taobao.model.TaobaoTravelItem;
import xyz.work.taobao.svc.TaoBaoSkuSvc;

@Service
public class TaoBaoSkuSvcImp implements TaoBaoSkuSvc {
    @Autowired
    CommonDao commonDao;

    @Override
    public Map<String, Object> getSkuStockListByBaseInfo(String baseInfo) {
        TaobaoBaseInfo baseObj = (TaobaoBaseInfo)commonDao.getObjectByUniqueCode("TaobaoBaseInfo", "numberCode", baseInfo);
        if(baseObj == null){
            
        }
        
        String skuHql = "from TaobaoSkuInfo s where s.taobaoTravelItem = '"+ baseObj.getTaobaoTravelItem() +"' ";
        @SuppressWarnings("unchecked")
        List<TaobaoSkuInfo> skuList = commonDao.queryByHql(skuHql);
        
        List<String> skuInfos = new ArrayList<>();
        for(TaobaoSkuInfo sku : skuList){
            skuInfos.add(sku.getNumberCode());
        }
        
        Date newDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(newDate);
        cal.add(Calendar.DATE, 2);
        String jia = (new SimpleDateFormat("yyyy-MM-dd")).format(cal.getTime());
        
        String skuStockHql = "from TaobaoSkuInfoDetail d where 1=1 ";
        skuStockHql += "and d.taobaoSkuInfo in ("+ StringTool.listToSqlString(skuInfos) +") ";
        skuStockHql += "and d.date >= '"+ jia +"' ";
        skuStockHql += "order by d.taobaoSkuInfo,d.date desc ";
        @SuppressWarnings("unchecked")
        List<TaobaoSkuInfoDetail> stockList = commonDao.queryByHql(skuStockHql);
        
        for(TaobaoSkuInfoDetail stock : stockList){
            for(TaobaoSkuInfo sku : skuList){
                if(stock.getTaobaoSkuInfo().equals(sku.getNumberCode())){
                    sku.getDetailList().add(stock);
                    break;
                }
            }
        }
        
        return ReturnUtil.returnMap(1, skuList);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> updateSkuStockList(String skuJson) {
        if(StringTool.isEmpty(skuJson)){
            return ReturnUtil.returnMap(0, "参数错误!");
        }
        List<Map<String, Object>> skuList = JSON.toObject(skuJson, ArrayList.class);
        
        String successStr = "";
        for(Map<String, Object> sku : skuList){
            String skuId = (String)sku.get("sku");
            List<Map<String, Object>> stockList = (List<Map<String, Object>>)sku.get("stock");;
            
            TaobaoSkuInfo skuObj = (TaobaoSkuInfo)commonDao.getObjectByUniqueCode("TaobaoSkuInfo", "numberCode", skuId); 
            
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
                    /*if(StringTool.isEmpty(successStr)){
                        successStr = numberCode;
                    }else{
                        successStr = successStr + "," +numberCode;
                    }*/
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
                    return ReturnUtil.returnMap(0, "接口错误,错误码【"+errorResponseMap==null?"":JSON.toJson(errorResponseMap)+"】错误信息【"+errorResponseMap==null?"":JSON.toJson(errorResponseMap)+"】");
                }
            }
            
        }
        
        return ReturnUtil.returnMap(1, successStr);
    }

    @Override
    public Map<String, Object> editDetailByNumberCode(String numberCode, int isLock) {
        TaobaoSkuInfoDetail detailObj = (TaobaoSkuInfoDetail)commonDao.getObjectByUniqueCode("TaobaoSkuInfoDetail", "numberCode", numberCode);
        detailObj.setIsLock(isLock);
        commonDao.update(detailObj);
        
        TaobaoSkuInfo skuObj = (TaobaoSkuInfo)commonDao.getObjectByUniqueCode("TaobaoSkuInfo", "numberCode", detailObj.getTaobaoSkuInfo());
        
        String lock = isLock==0?"解锁":"锁定";
        
        TaobaoLogWork detailLogWork = new TaobaoLogWork();
        detailLogWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
        detailLogWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
        detailLogWork.setValue(detailObj.getNumberCode());
        detailLogWork.setTableName("detailLockTable");
        detailLogWork.setRemark(lock+"SKU套餐【"+ skuObj.getPackageName() +"】下的日历库存【"+ DateUtil.dateToShortString(detailObj.getDate()) +"】价格");
        detailLogWork.setAddDate(new Date());
        commonDao.save(detailLogWork);
        
        return ReturnUtil.returnMap(1, detailLogWork);
    }

}