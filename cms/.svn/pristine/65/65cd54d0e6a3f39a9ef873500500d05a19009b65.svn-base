package xyz.work.taobao.svc.imp;


import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.MyRequestUtil;
import xyz.filter.ReturnUtil;
import xyz.util.DateUtil;
import xyz.util.StringTool;
import xyz.util.StringUtil;
import xyz.util.UUIDUtil;
import xyz.work.base.model.Cruise;
import xyz.work.taobao.model.TaobaoLogWork;
import xyz.work.taobao.model.TaobaoSkuInfo;
import xyz.work.taobao.model.TaobaoSkuInfoDetail;
import xyz.work.taobao.model.TaobaoTravelItem;
import xyz.work.taobao.svc.TaoBaoSkuDetailSvc;


@Service
public class TaoBaoSkuDetailSvcImp implements TaoBaoSkuDetailSvc {
    @Autowired
    private CommonDao commonDao;

    @Override
    public Map<String, Object> addSkuDatilList(String baseInfo , String skuInfo , String dateJson ,
                                               BigDecimal price , int priceType , int isLock ,
                                               int isEdit , String stock) {
        if (StringTool.isEmpty(skuInfo)) {
            return ReturnUtil.returnMap(0, "SKU错误!");
        }
        if(StringTool.isEmpty(dateJson)){
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
        
        TaobaoSkuInfo taobaoSkuInfoObj = (TaobaoSkuInfo)commonDao.getObjectByUniqueCode("TaobaoSkuInfo", "numberCode", skuInfo);
        if(taobaoSkuInfoObj == null){
            return ReturnUtil.returnMap(0, "SKU套餐不存在!");
        }
        
        String repeatSql = "SELECT COUNT(*) FROM taobao_sku_info_detail ";
        repeatSql += "WHERE taobao_sku_info = '"+ taobaoSkuInfoObj.getNumberCode() +"' " ;
        repeatSql += "AND date in ("+ StringTool.StrToSqlString(dateJson) +") ";
        Query countQuery = commonDao.getSqlQuery(repeatSql);
        Number countNum = (Number)countQuery.uniqueResult();
        int count = countNum == null ? 0 : countNum.intValue();
        if (count > 0) {
            return ReturnUtil.returnMap(0, "SKU日历库存重复!");
        }
        
        String[] dateArr = dateJson.split(",");
        for(int d = 0; d < dateArr.length; d++){
            String tempDate = dateArr[d];
            Date addDate = new Date();
            TaobaoSkuInfoDetail taobaoSkuInfoDetailObj = new TaobaoSkuInfoDetail();
            taobaoSkuInfoDetailObj.setTaobaoSkuInfo(taobaoSkuInfoObj.getNumberCode());
            taobaoSkuInfoDetailObj.setNumberCode(StringUtil.get_new_taobao_price_out_num());
            taobaoSkuInfoDetailObj.setDate(DateUtil.shortStringToDate(tempDate));
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
            taobaoSkuInfoDetailObj.setAddDate(addDate);
            taobaoSkuInfoDetailObj.setAlterDate(addDate);
            commonDao.save(taobaoSkuInfoDetailObj);
            
            TaobaoLogWork skuLogWork = new TaobaoLogWork();
            skuLogWork.setAddDate(addDate);
            skuLogWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
            skuLogWork.setValue(baseInfo);
            skuLogWork.setRemark("批量新增SKU套餐【"+taobaoSkuInfoObj.getPackageName()+"】里日期为【"+ tempDate +"】的库存");
            skuLogWork.setTableName("skuTable");
            skuLogWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
            commonDao.save(skuLogWork);
        }
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> editIsLock(String numberCode , int isLock , String cruise) {
        if(StringTool.isEmpty(numberCode)){
            return ReturnUtil.returnMap(0, "");
        }
        
        Cruise cruisefoObj = (Cruise)commonDao.getObjectByUniqueCode("Cruise", "numberCode", cruise);
        
        TaobaoSkuInfoDetail detailObj = (TaobaoSkuInfoDetail)commonDao.getObjectByUniqueCode("TaobaoSkuInfoDetail", "numberCode", numberCode);
        if(detailObj == null){
            return ReturnUtil.returnMap(0, "");
            
        }
        detailObj.setIsLock(isLock);
        commonDao.update(detailObj);
        
        TaobaoSkuInfo skuObj = (TaobaoSkuInfo)commonDao.getObjectByUniqueCode("TaobaoSkuInfo", "numberCode", detailObj.getTaobaoSkuInfo());
        
        TaobaoTravelItem traveObj = (TaobaoTravelItem)commonDao.getObjectByUniqueCode("TaobaoTravelItem", "numberCode", skuObj.getTaobaoTravelItem());
        
        String lock = isLock==0?"解锁":"锁定";
        
        TaobaoLogWork detailLogWork = new TaobaoLogWork();
        detailLogWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
        detailLogWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
        detailLogWork.setValue(detailObj.getNumberCode());
        detailLogWork.setTableName("detailLockTable");
        detailLogWork.setRemark(lock+"【 "+cruisefoObj.getNameCn()+"】邮轮【"+traveObj.getChannelNameCn()+"】渠道下的SKU套餐【"+ skuObj.getPackageName() +"】下的日历库存【"+ DateUtil.dateToShortString(detailObj.getDate()) +"】价格");
        detailLogWork.setAddDate(new Date());
        commonDao.save(detailLogWork);
        
        return ReturnUtil.returnMap(1, null);
    }

}