package xyz.work.sell.svc.imp;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.MyRequestUtil;
import xyz.filter.ReturnUtil;
import xyz.util.Constant;
import xyz.util.ConstantMsg;
import xyz.util.DateUtil;
import xyz.util.PossessorUtil;
import xyz.util.StringTool;
import xyz.util.StringUtil;
import xyz.util.UUIDUtil;
import xyz.work.base.model.Cabin;
import xyz.work.base.model.Shipment;
import xyz.work.core.model.LogWork;
import xyz.work.excel.model.PriceStrategy;
import xyz.work.resources.model.Provider;
import xyz.work.resources.model.Stock;
import xyz.work.resources.model.Tkview;
import xyz.work.resources.model.TkviewType;
import xyz.work.sell.model.Ptview;
import xyz.work.sell.model.PtviewSku;
import xyz.work.sell.model.PtviewSkuStock;
import xyz.work.sell.model.PtviewSku_TkviewType;
import xyz.work.sell.svc.PtviewSkuStockSvc;

@Service
public class PtviewSkuStockSvcImp implements PtviewSkuStockSvc {
    @Autowired
    private CommonDao commonDao;
    
    @Autowired
    private PossessorUtil possessorUtil;

    @Override
    public Map<String, Object> getPtviewSkuStockDateList(String ptviewSku) {
        PtviewSku ptviewSkuObj = (PtviewSku)commonDao.getObjectByUniqueCode("PtviewSku", "numberCode", ptviewSku);
        if (ptviewSkuObj == null) {
            return ReturnUtil.returnMap(0, "产品SKU套餐不存在!");
        }
        
        Ptview ptviewObj = (Ptview)commonDao.getObjectByUniqueCode("Ptview", "numberCode", ptviewSkuObj.getPtview());
        if(ptviewObj == null){
            return ReturnUtil.returnMap(0, "产品对象不存在!");
        }
        
        String skuStockHql = "from PtviewSkuStock s where s.ptviewSku = '"+ ptviewSku +"' ";
        skuStockHql += possessorUtil.getRelatesWhereHql("ptview_sku_stock");
        skuStockHql += "order by s.date ";
        @SuppressWarnings("unchecked")
        List<PtviewSkuStock> ptviewSkuStockList = commonDao.queryByHql(skuStockHql);
        
        for (PtviewSkuStock stockObj : ptviewSkuStockList) {
            //TODO 删除关联表
            String ptviewSku_tkviewTypeHql = "from PtviewSku_TkviewType pt where pt.ptviewSku = '"+ ptviewSku +"' ";
            @SuppressWarnings("unchecked")
            List<PtviewSku_TkviewType> ptviewSkuTkviewTypeList = commonDao.queryByHql(ptviewSku_tkviewTypeHql);
            
            List<String> typeNumber = new ArrayList<String>();
            for(PtviewSku_TkviewType skuType : ptviewSkuTkviewTypeList){
                typeNumber.add(skuType.getTkviewType());
            }
            
            String tkviewTypeHql = "from TkviewType t where t.numberCode in ("+ StringTool.listToSqlString(typeNumber) +") ";
            @SuppressWarnings("unchecked")
            List<TkviewType> typeList = commonDao.queryByHql(tkviewTypeHql);
            
            List<String> cabinNumber = new ArrayList<String>();
            for(TkviewType ty : typeList){
                cabinNumber.add(ty.getCabin());
            }
            
            String tkviewHql = "from Tkview t where ";
            tkviewHql += "t.cabin in ("+ StringTool.listToSqlString(cabinNumber) +") ";
            @SuppressWarnings("unchecked")
            List<Tkview> tkviewList = commonDao.queryByHql(tkviewHql);
            
            stockObj.setTkviewCount(tkviewList.size());
            
            if(tkviewList.size() > 0){
                String nameCnStr = "";
                for(Tkview tkviewObj : tkviewList){
                    if(StringTool.isEmpty(nameCnStr)){
                        nameCnStr = tkviewObj.getNameCn();
                    }else{
                        nameCnStr = nameCnStr + "," + tkviewObj.getNameCn();
                    }
                }
                stockObj.setTkviewNameCn(nameCnStr);
            }
            
        }
        
        Map<String, Date> dateMap = new HashMap<String, Date>();
        Calendar cal = Calendar.getInstance();
        for (PtviewSkuStock stockObj : ptviewSkuStockList) {
            cal.setTime(stockObj.getDate());
            dateMap.put(cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1), stockObj.getDate());
        }
        List<Date> dateList=new ArrayList<Date>();
        for (Map.Entry<String, Date> entry : dateMap.entrySet()) {  
              dateList.add(entry.getValue());
        }  
        Collections.sort(dateList);
        
        List<String> dateStringList=new ArrayList<String>();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-M-d");
        for(Date date:dateList){
            dateStringList.add(format.format(date));
        }
        
        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("ptviewSkuStockList", ptviewSkuStockList);
        mapContent.put("dateList", dateStringList);
        
        return ReturnUtil.returnMap(1, mapContent);
    }
    
    @Override
    public Map<String, Object> getTkviewTypeList(String ptviewSku) {
        PtviewSku skuObj = (PtviewSku)commonDao.getObjectByUniqueCode("PtviewSku", "numberCode", ptviewSku);
        if(skuObj == null){
            return ReturnUtil.returnMap(0, "产品SKU套餐对象不存在!");
        }
        
        //TODO 删除关联表
        String hql = "from PtviewSku_TkviewType pt where 1=1 ";
        hql += "and pt.ptviewSku = '"+ ptviewSku +"' ";
        @SuppressWarnings("unchecked")
        List<PtviewSku_TkviewType> list = commonDao.queryByHql(hql);
        List<String> tkviewTypeNumber = new ArrayList<>();
        for(PtviewSku_TkviewType skuType : list){
            tkviewTypeNumber.add(skuType.getTkviewType());
        }
        
        String tkviewTypeHql = "from TkviewType t where ";
        tkviewTypeHql += "t.numberCode in ("+ StringTool.listToSqlString(tkviewTypeNumber) +") ";
        @SuppressWarnings("unchecked")
        List<TkviewType> tkviewTypeList = commonDao.queryByHql(tkviewTypeHql);
        
        List<String> cabinNumber = new ArrayList<>();
        for(TkviewType type : tkviewTypeList){
            cabinNumber.add(type.getCabin());
        }
        
        String cabinHql = "from Cabin c where c.numberCode in ("+ StringTool.listToSqlString(cabinNumber) +")";
        @SuppressWarnings("unchecked")
        List<Cabin> cabinList = commonDao.queryByHql(cabinHql);
        
        for(TkviewType type : tkviewTypeList){
            for(Cabin cabin : cabinList){
                if(type.getCabin().equals(cabin.getNumberCode())){
                    type.setCabinNameCn(cabin.getNameCn());
                    break;
                }
            }
        }
        
        return ReturnUtil.returnMap(1, tkviewTypeList);
    }

    @Override
    public Map<String, Object> addPtviewSkuStock(String ptviewSku , String outTkview , String date , 
                                                 BigDecimal bPrice , BigDecimal cPrice ,
                                                 BigDecimal aBPrice , BigDecimal aCPrice ,
                                                 BigDecimal sBPrice , BigDecimal sCPrice ,
                                                 BigDecimal mBPrice , BigDecimal mCPrice ,
                                                 String stock , String outStock) {
        if (StringTool.isEmpty(ptviewSku)) {
            return ReturnUtil.returnMap(0, "产品SKU套餐不对!");
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
        
        PtviewSku skuObj = (PtviewSku)commonDao.getObjectByUniqueCode("PtviewSku", "numberCode", ptviewSku);
        if(skuObj == null){
            return ReturnUtil.returnMap(0, "SKU套餐对象不存在!");
        }
        
        Tkview tkviewObj = (Tkview)commonDao.getObjectByUniqueCode(Constant.tkview, Constant.numberCode, outTkview);
        if (tkviewObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.tkview_not_exist_error);
        }
        
        Shipment shipmentObj = (Shipment)commonDao.getObjectByUniqueCode("Shipment", "numberCode", tkviewObj.getShipment());
        
        String repeatSql = "SELECT COUNT(*) FROM ptview_sku_stock ";
        repeatSql += "WHERE ptview_sku = '"+ ptviewSku +"' " ;
        repeatSql += "AND date = '"+ date +"' ";
        repeatSql += possessorUtil.getRelatesWhereSql("ptview_sku_stock");
        //repeatSql += "AND out_tkview = '"+ outTkview +"' ";
        Query countQuery = commonDao.getSqlQuery(repeatSql);
        Number countNum = (Number)countQuery.uniqueResult();
        int count = countNum == null ? 0 : countNum.intValue();
        if (count > 0) {
            return ReturnUtil.returnMap(0, "产品SKU日历库存【"+ date +"】重复!");
        }
        
        Date addDate = new Date();
        PtviewSkuStock stockObj = new PtviewSkuStock();
        stockObj.setPtviewSku(ptviewSku);
        stockObj.setNumberCode(StringUtil.get_new_taobao_price_out_num());
        stockObj.setOutTkview(outTkview);
        stockObj.setDate(shipmentObj.getTravelDate());
        //stockObj.setPriceType(priceType);
        //stockObj.setPrice(price);
        //stockObj.setPrice(bPrice);
        stockObj.setbPrice(bPrice);
        stockObj.setcPrice(cPrice);
        stockObj.setaBPrice(aBPrice);
        stockObj.setaCPrice(aCPrice);
        stockObj.setsBPrice(sBPrice);
        stockObj.setsCPrice(sCPrice);
        stockObj.setmBPrice(mBPrice);
        stockObj.setmCPrice(mCPrice);
        if(pd){
            stockObj.setStock(new BigDecimal(stock).intValue());
        }
        stockObj.setOutStock(outStock);
        stockObj.setAddDate(addDate);
        stockObj.setAlterDate(addDate);
        commonDao.save(stockObj);
        
        possessorUtil.savePossessorRelates("ptview_sku_stock", stockObj.getNumberCode());
        
        LogWork logWork = new LogWork();
        logWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
        logWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
        logWork.setValue(stockObj.getNumberCode());
        logWork.setRemark("新增产品SKU【"+skuObj.getNameCn()+"】日历库存：日期为【"+ DateUtil.dateToString(shipmentObj.getTravelDate()) +"】的库存");
        logWork.setTableName("ptview_sku_stock");
        logWork.setAddDate(addDate);
        commonDao.save(logWork);

        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> editPtviewSkuStock(String numberCode , String outTkview ,
                                                  BigDecimal bPrice , BigDecimal cPrice ,
                                                  BigDecimal aBPrice , BigDecimal aCPrice ,
                                                  BigDecimal sBPrice , BigDecimal sCPrice ,
                                                  BigDecimal mBPrice , BigDecimal mCPrice ,
                                                  String stock) {
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
        
        PtviewSkuStock stockObj = (PtviewSkuStock)commonDao.getObjectByUniqueCode("PtviewSkuStock", "numberCode", numberCode);
        if (stockObj == null) {
            return ReturnUtil.returnMap(0, "产品SKU日历库存对象不存在!");
        }
        
        Tkview tkviewObj = (Tkview)commonDao.getObjectByUniqueCode(Constant.tkview, Constant.numberCode, outTkview);
        if (tkviewObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.tkview_not_exist_error);
        }
        stockObj.setOutTkview(outTkview);
        //stockObj.setPriceType(priceType);
        //stockObj.setPrice(price);
        //stockObj.setPrice(bPrice);
        stockObj.setbPrice(bPrice);
        stockObj.setcPrice(cPrice);
        stockObj.setaBPrice(aBPrice);
        stockObj.setaCPrice(aCPrice);
        stockObj.setsBPrice(sBPrice);
        stockObj.setsCPrice(sCPrice);
        stockObj.setmBPrice(mBPrice);
        stockObj.setmCPrice(mCPrice);
        if(pd){
            stockObj.setStock(new BigDecimal(stock).intValue());
        }
        stockObj.setAlterDate(new Date());
        commonDao.update(stockObj);
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> deletePtviewSkuStock(String numberCode) {
        PtviewSkuStock stockObj = (PtviewSkuStock)commonDao.getObjectByUniqueCode("PtviewSkuStock", "numberCode", numberCode);
        if (stockObj == null) {
            return ReturnUtil.returnMap(0, "产品SKU库存对象不存在!");
        }
        
        PtviewSku ptviewSkuObj = (PtviewSku)commonDao.getObjectByUniqueCode("PtviewSku", "numberCode", stockObj.getPtviewSku());
        if (ptviewSkuObj == null) {
            return ReturnUtil.returnMap(0, "产品SKU套餐对象不存在!");
        }
        
        //TODO 删除关联表
        String sql = "DELETE FROM ptview_sku_stock WHERE number_code = '"+ numberCode +"' ";
        commonDao.getSqlQuery(sql).executeUpdate();
        
        LogWork logWork = new LogWork();
        logWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
        logWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
        logWork.setValue(numberCode);
        logWork.setRemark("删除产品SKU套餐【"+ptviewSkuObj.getNameCn()+"】里日期为【"+ DateUtil.dateToString(stockObj.getDate()).substring(0,10)+"】的库存");
        logWork.setTableName("ptview_sku_stock");
        logWork.setAddDate(new Date());
        commonDao.save(logWork);
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> getPtviewSkuStockListByNumberCode(String numberCode , String ptviewSku , String date) {
        PtviewSkuStock stockObj = (PtviewSkuStock) commonDao.getObjectByUniqueCode("PtviewSkuStock", "numberCode", numberCode);
        if(stockObj == null){
            return ReturnUtil.returnMap(0, date+"的SKU库存对象不存在!");
        }
        
        PtviewSku skuObj = (PtviewSku) commonDao.getObjectByUniqueCode("PtviewSku", "numberCode", ptviewSku);
        if(skuObj == null){
            return ReturnUtil.returnMap(0, "产品SKU套餐对象不存在!");
        }
        
        Ptview ptviewObj = (Ptview) commonDao.getObjectByUniqueCode("Ptview", "numberCode", skuObj.getPtview());
        if(ptviewObj == null){
            return ReturnUtil.returnMap(0, "产品信息对象不存在!");
        }
        
        //TODO 删除关联表
        String skuTkviewTypeHql = "from PtviewSku_TkviewType pt where pt.ptviewSku = '"+ ptviewSku +"' ";
        @SuppressWarnings("unchecked")
        List<PtviewSku_TkviewType> skuTkviewTypeList = commonDao.queryByHql(skuTkviewTypeHql);
        List<String> tkviewTypeNumber = new ArrayList<>();
        for(PtviewSku_TkviewType typeObj : skuTkviewTypeList){
            tkviewTypeNumber.add(typeObj.getTkviewType());
        }
        
        String tkviewTypeHql = "from TkviewType t where ";
        tkviewTypeHql += "t.numberCode in ("+ StringTool.listToSqlString(tkviewTypeNumber) +") ";
        @SuppressWarnings("unchecked")
        List<TkviewType> tkviewTypeList = commonDao.queryByHql(tkviewTypeHql);
        
        List<String> cabinNumber = new ArrayList<>();
        for(TkviewType type : tkviewTypeList){
            cabinNumber.add(type.getCabin());
        }
        
        String tkviewHql = "from Tkview t where t.cruise = '"+ ptviewObj.getCruise() +"' ";
        tkviewHql += possessorUtil.getRelatesWhereHql("tkview");
        tkviewHql += " and t.shipmentTravelDate = '"+ date +"' ";
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
        map.put("skuStockObj", stockObj);
        
        return ReturnUtil.returnMap(1, map);
    }

    @Override
    public Map<String, Object> getStockDate(String ptviewSku , String date) {
        PtviewSku skuObj = (PtviewSku)commonDao.getObjectByUniqueCode("PtviewSku", "numberCode", ptviewSku);
        if(skuObj == null){
            return ReturnUtil.returnMap(0, "SKU套餐对象不存在!");
        }
        if(date == null){
            return ReturnUtil.returnMap(0, "请选日期!");
        }
        
        String stockSql = "SELECT * FROM ptview_sku_stock d ";
        stockSql += "WHERE d.ptview_sku = '"+ ptviewSku +"' ";
        stockSql += "AND d.date = '"+ date +"'";
        stockSql += possessorUtil.getRelatesWhereSql("ptview_sku_stock");
        @SuppressWarnings("unchecked")
        List<PtviewSkuStock> list = commonDao.getSqlQuery(stockSql).list();
        int count = list.size();
        
        return ReturnUtil.returnMap(1, count);
    }

    @Override
    public Map<String, Object> getStockByTkview(String shipment, String ptviewSku) {
        PtviewSku skuObj = (PtviewSku)commonDao.getObjectByUniqueCode("PtviewSku", "numberCode", ptviewSku);
        if(skuObj == null){
            return ReturnUtil.returnMap(0, "SKU套餐对象不存在!");
        }
        
        //TODO 删除关联表
        String typeHql = "from PtviewSku_TkviewType p where p.ptviewSku = '"+ ptviewSku +"' ";
        @SuppressWarnings("unchecked")
        List<PtviewSku_TkviewType> list = commonDao.queryByHql(typeHql);
        List<String> numberList = new ArrayList<String>();
        for(PtviewSku_TkviewType pt : list){
            numberList.add(pt.getTkviewType());
        }
        
        String tkviewTypeHql = "from TkviewType t where t.numberCode in ("+ StringTool.listToSqlString(numberList) +") ";
        @SuppressWarnings("unchecked")
        List<TkviewType> tkviewTypeList = commonDao.queryByHql(tkviewTypeHql);
        
        List<String> cabinList = new ArrayList<String>();
        for(TkviewType t : tkviewTypeList){
            cabinList.add(t.getCabin());
        }
        
        String tkviewHql = "from Tkview t where 1=1 ";
        tkviewHql += possessorUtil.getRelatesWhereHql("tkview");
        tkviewHql += "and t.cabin in ("+ StringTool.listToSqlString(cabinList) +") ";
        tkviewHql += "and t.shipment = '"+ shipment +"' ";
        tkviewHql += "order by numberCode ";
        @SuppressWarnings("unchecked")
        List<Tkview> tkviewList = commonDao.queryByHql(tkviewHql);
        
        List<String> tkviewNumber = new ArrayList<>();
        for(Tkview tk : tkviewList){
            tkviewNumber.add(tk.getNumberCode());
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
        
        String providerHql = "from Provider p where p.numberCode in ("+ StringTool.listToSqlString(providerNumber) +") ";
        @SuppressWarnings("unchecked")
        List<Provider> providerList = commonDao.queryByHql(providerHql);
        
        for(Stock s : stockList){
            for(Tkview tkviewObj : tkviewList){
                if(s.getTkview().equals(tkviewObj.getNumberCode())){
                    tkviewObj.getStocks().add(s);
                    s.setTkviewNameCn(tkviewObj.getNameCn());
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
        
        List<Tkview> tkviews = new ArrayList<Tkview>();
        for(Tkview t : tkviewList){
            if(t.getStocks().size()>0){
                Collections.sort(t.getStocks());
                t.setCost(t.getStocks().get(0).getCost());
                t.setStock(t.getStocks().get(0).getStock());
                tkviews.add(t);
            }
        }
        Collections.sort(tkviews);
        
        List<Stock> resultStockList = new ArrayList<Stock>();
        for(Tkview tkviewObj : tkviewList){
            resultStockList.addAll(tkviewObj.getStocks());
        }
        Collections.sort(resultStockList);
        
        String priceHql = "from PriceStrategy";
        @SuppressWarnings("unchecked")
        List<PriceStrategy> priceList = commonDao.queryByHql(priceHql);
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("stockList", resultStockList);
        map.put("priceList", priceList);
        
        return ReturnUtil.returnMap(1, map);
    }

    @Override
    public Map<String, Object> getShipmentTravelDate(String shipment) {
        Shipment shipObj = (Shipment)commonDao.getObjectByUniqueCode("Shipment", "numberCode", shipment);
        if(shipObj == null){
            return ReturnUtil.returnMap(0, "航期不存在!");
        }
        
        return ReturnUtil.returnMap(1, shipObj);
    }
    
}