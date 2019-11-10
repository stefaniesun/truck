package xyz.work.resources.svc.imp;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
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
import xyz.util.UUIDUtil;
import xyz.work.base.model.Cabin;
import xyz.work.base.model.Shipment;
import xyz.work.core.model.LogWork;
import xyz.work.core.model.PlanWork;
import xyz.work.excel.model.PriceStrategy;
import xyz.work.resources.model.Provider;
import xyz.work.resources.model.Stock;
import xyz.work.resources.model.Tkview;
import xyz.work.resources.model.TkviewType;
import xyz.work.resources.svc.StockSvc;
import xyz.work.resources.svc.TkviewSvc;
import xyz.work.taobao.model.Sku_TkviewType;
import xyz.work.taobao.model.TaobaoSkuInfoDetail;


@Service
public class StockSvcImp implements StockSvc {

    @Autowired
    CommonDao commonDao;

    @Autowired
    TkviewSvc tkviewSvc;

    @Autowired
    PossessorUtil possessorUtil;

    @Override
    public Map<String, Object> queryStockListByTkview(int offset , int pagesize , String tkview ,
                                                      String numberCode , String nickName ,
                                                      String sort , String order , String provider) {
        StringBuffer hql = new StringBuffer();
        hql.append("from Stock s where tkview = '" + tkview + "' ");
        if (StringTool.isNotNull(numberCode)) {
            hql.append("and s.numberCode = '" + numberCode + "' ");
        }
        if (StringTool.isNotNull(nickName)) {
            hql.append("and s.nickName like '%" + nickName + "%' ");
        }
        if (StringTool.isNotNull(provider)) {
            hql.append("and s.provider in (" + StringTool.StrToSqlString(provider) + ") ");
        }
        if (sort != null && !"".equals(sort) && order != null && !"".equals(order)) {
            if (",providerNameCn,cost,type,stock,useStock,surpass,isValid,priority,overDate,useSurpass,".contains(","+ sort+ ",")
                && ",asc,desc,".contains("," + order + ",")) {
                hql.append("order by s." + sort + " " + order);
            }
        }else {
            hql.append("order by s.grade ");
        }

        Query countQuery = commonDao.getQuery("select count(*) " + hql.toString());
        Number tempCount = (Number)countQuery.uniqueResult();
        int total = tempCount == null ? 0 : tempCount.intValue();

        Query query = commonDao.getQuery(hql.toString());
        query.setFirstResult(offset);
        query.setMaxResults(pagesize);

        @SuppressWarnings("unchecked")
        List<Stock> stockList = query.list();

        List<String> tkviewNumber = new ArrayList<String>();
        List<String> providerNumber = new ArrayList<String>();
        for (Stock stockObj : stockList) {
            providerNumber.add(stockObj.getProvider());
            tkviewNumber.add(stockObj.getTkview());
        }

        String tkviewSql = "SELECT t.number_code,t.name_cn FROM tkview t WHERE t.number_code IN ("
                           + StringTool.listToSqlString(tkviewNumber) + ")";
        @SuppressWarnings("unchecked")
        List<Object[]> tkviewList = commonDao.getSqlQuery(tkviewSql).list();

        String providerSql = "SELECT p.number_code,p.name_cn FROM provider p WHERE p.number_code IN ("
                             + StringTool.listToSqlString(providerNumber) + ")";
        @SuppressWarnings("unchecked")
        List<Object[]> providerList = commonDao.getSqlQuery(providerSql).list();

        for (Stock stockObj : stockList) {
            if (providerList != null && providerList.size() > 0) {
                for (Object[] providerObj : providerList) {
                    if (providerObj[0].equals(stockObj.getProvider())) {
                        stockObj.setProviderNameCn(providerObj[1].toString());
                        break;
                    }
                }
            }
            if (stockObj.getType() == 1 && tkviewList != null && tkviewList.size() > 0) { // 实库
                for (Object[] tkviewObj : tkviewList) {
                    if (tkviewObj[0].equals(stockObj.getTkview())) {
                        stockObj.setTkviewNameCn(tkviewObj[1].toString());
                        break;
                    }
                }
            }
        }
        Collections.sort(stockList);

        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", total);
        mapContent.put("rows", stockList);

        return ReturnUtil.returnMap(1, mapContent);
    }

    @Override
    public Map<String, Object> addStock(String tkview , String nickName , String provider ,
                                        int type , BigDecimal stock , BigDecimal surpass ,
                                        BigDecimal cost , String costRemark , Date overDate ,
                                        int priority , String remark) {
        Tkview tkviewObj = (Tkview)commonDao.getObjectByUniqueCode("Tkview", "numberCode", tkview);
        if (tkviewObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.tkview_not_exist_error);
        }

        if (StringTool.isEmpty(provider)) {
            return ReturnUtil.returnMap(0, ConstantMsg.provider_not_exist_error);
        }
        Provider providerObj = (Provider)commonDao.getObjectByUniqueCode("Provider", "numberCode", provider);
        if (providerObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.provider_not_exist_error);
        }

        if (overDate == null) {
            Date travelDate = DateUtil.addDay(tkviewObj.getShipmentTravelDate(), -3);
            String travelDateStr = DateUtil.dateToShortString(travelDate);
            overDate = DateUtil.shortStringToDate(travelDateStr);
        }

        String stockHql = "from Stock s where s.tkview = '"+ tkview +"' and s.provider = '"+ provider +"' ";
        @SuppressWarnings("unchecked")
        List<Stock> stockList = commonDao.queryByHql(stockHql);
        Date date = new Date();
        Stock stockObj = null;
        
        /*覆盖*/
        if(stockList.size() > 0){
            stockObj = stockList.get(0);
            String logRemark = "";
            nickName = nickName == null ? "" : nickName;
            if (!nickName.equals(stockObj.getNickName())) {
                logRemark += "别名【新值:" + nickName + "】【旧值:" + stockObj.getNickName() + "】";
            }
            if (stockObj.getCost().intValue() != cost.intValue()) {
                logRemark += "成本价【新值:"+ cost.intValue()/100 +"】【旧值:"+ stockObj.getCost().intValue()/100 +"】";
            }
            if(type!=stockObj.getType()){
                logRemark += "库存类型【新值:"+ (type==1?"实库":"现询") +"】【旧值:"+ (stockObj.getType()==1?"实库":"现询") +"】";
            }
            stockObj.setNickName(nickName);
            stockObj.setProvider(provider);
            stockObj.setGrade(providerObj.getGrade());
            stockObj.setCost(cost);
            stockObj.setCostRemark(costRemark);
            stockObj.setOverDate(overDate);
            stockObj.setPriority(priority);
            stockObj.setType(type);
            if(type == 1){// 实库
                stockObj.setStock(stock);
                stockObj.setSurpass(surpass);
            }else{
                Cabin cabinObj = (Cabin)commonDao.getObjectByUniqueCode("Cabin", "numberCode", tkviewObj.getCabin());
                stockObj.setStock(cabinObj.getVolume().multiply(new BigDecimal(5)));
                stockObj.setSurpass(BigDecimal.ZERO);
            }
            stockObj.setUseSurpass(BigDecimal.ZERO);
            stockObj.setUseStock(BigDecimal.ZERO);
            stockObj.setDate(tkviewObj.getShipmentTravelDate());
            stockObj.setAlterDate(date);
            stockObj.setRemark(remark);
            commonDao.update(stockObj);

       /*     // 更新库存到淘宝SKU
            Map<String, Object> result = editStockObjToSku(stockObj.getTkview());
            if(Integer.valueOf(result.get(Constant.result_status).toString()) == 0){
                return ReturnUtil.returnMap(0, result.get(Constant.result_msg));
            }*/

            LogWork stockLogWork = new LogWork();
            stockLogWork.setAddDate(new Date());
            stockLogWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
            stockLogWork.setValue(tkviewObj.getNumberCode());
            stockLogWork.setTableName("stock");
            stockLogWork.setRemark("修改库存 " + logRemark);
            stockLogWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
            commonDao.save(stockLogWork);

            String hql = "from Stock s where s.tkview = '" + stockObj.getTkview() + "' ";
            @SuppressWarnings("unchecked")
            List<Stock> list = commonDao.queryByHql(hql);
            Collections.sort(list);

            Stock stockObjet = null;
            for (int h = 0; h < list.size(); h++ ) {
                stockObjet = list.get(h);
                stockObjet.setPriority(h);
                commonDao.update(stockObjet);
            }
            
            if((list.size()==1||list.get(0).getNumberCode().equals(stockObj.getNumberCode()))&&list.get(0).getStock().intValue()!=0){
                //自动更新计划任务
                PlanWork planWork=new PlanWork();
                planWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
                planWork.setAddDate(new Date());
                planWork.setContent(tkviewObj.getNumberCode());
                planWork.setUsername("system");
                planWork.setWorkType(Constant.PLAN_TYPE_UPDATE_TKVIEW);
                commonDao.save(planWork);
            }
            
            return ReturnUtil.returnMap(1, null);
        }
        
        stockObj = new Stock();
        stockObj.setNumberCode(UUIDUtil.getUUIDStringFor32());
        stockObj.setTkview(tkviewObj.getNumberCode());
        stockObj.setNickName(nickName);
        stockObj.setProvider(provider);
        stockObj.setGrade(providerObj.getGrade());
        stockObj.setCost(cost);
        stockObj.setCostRemark(costRemark);
        stockObj.setOverDate(overDate);
        stockObj.setPriority(priority);
        stockObj.setType(type);
        if(type == 1){// 实库
            stockObj.setStock(stock);
            stockObj.setSurpass(surpass);
        }else{
            Cabin cabinObj = (Cabin)commonDao.getObjectByUniqueCode("Cabin", "numberCode", tkviewObj.getCabin());
            stockObj.setStock(cabinObj.getVolume().multiply(new BigDecimal(5)));
            stockObj.setSurpass(BigDecimal.ZERO);
        }
        stockObj.setUseSurpass(BigDecimal.ZERO);
        stockObj.setUseStock(BigDecimal.ZERO);
        stockObj.setDate(tkviewObj.getShipmentTravelDate());
        stockObj.setAddDate(date);
        stockObj.setAlterDate(date);
        stockObj.setRemark(remark);
        commonDao.save(stockObj);

        // 更新库存到淘宝SKU
   /*     Map<String, Object> result = editStockObjToSku(tkview);
        if (Integer.valueOf(result.get(Constant.result_status).toString()) == 0) {
            return ReturnUtil.returnMap(0, result.get(Constant.result_msg));
        }*/

        String tempType = stockObj.getType() == 0 ? "现询" : "实库";
        LogWork stockLogWork = new LogWork();
        stockLogWork.setAddDate(date);
        stockLogWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
        stockLogWork.setValue(tkviewObj.getNumberCode());
        stockLogWork.setTableName("stock");
        stockLogWork.setRemark("添加库存【供应商:"+ providerObj.getNameCn() +"】【类型:"+ tempType +"】【总库存:"+ stockObj.getStock() +"】");
        stockLogWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
        commonDao.save(stockLogWork);

        String hql = "from Stock s where s.tkview = '" + stockObj.getTkview() + "' ";
        @SuppressWarnings("unchecked")
        List<Stock> list = commonDao.queryByHql(hql);
        
        Collections.sort(list);
        
        boolean updateFlag=false;
        
        if((list.size()==1||list.get(0).getNumberCode().equals(stockObj.getNumberCode()))&&list.get(0).getStock().intValue()!=0){
        	updateFlag=true;
        }
        
        Stock stockObjet = null;
        for (int h = 0; h < list.size(); h++ ) {
            stockObjet = list.get(h);
            stockObjet.setPriority(h);
            commonDao.update(stockObjet);
        }
        
        if(updateFlag){
        	 //自动更新计划任务
            PlanWork planWork=new PlanWork();
            planWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
            planWork.setAddDate(new Date());
            planWork.setContent(tkview);
            planWork.setUsername("system");
            planWork.setWorkType(Constant.PLAN_TYPE_UPDATE_TKVIEW);
            commonDao.save(planWork);
        }

        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> editStockDetails(String numberCode , String nickName ,
                                                String provider , BigDecimal cost ,
                                                String costRemark , BigDecimal stock ,
                                                BigDecimal surpass , String overDate ,
                                                int priority , String remark , int stockType) {

        if (StringTool.isEmpty(numberCode)) {
            return ReturnUtil.returnMap(0, ConstantMsg.stock_not_exist_error);
        }
        if (StringTool.isEmpty(provider)) {
            return ReturnUtil.returnMap(0, ConstantMsg.provider_not_exist_error);
        }

        Stock stockObj = (Stock)commonDao.getObjectByUniqueCode("Stock", "numberCode", numberCode);
        if (stockObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.stock_not_exist_error);
        }
        Tkview tkviewObj = (Tkview)commonDao.getObjectByUniqueCode("Tkview", "numberCode", stockObj.getTkview());
        if (tkviewObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.tkview_not_exist_error);
        }
        
        
        String hql = "from Stock s where s.tkview = '" + stockObj.getTkview() + "' ";
        @SuppressWarnings("unchecked")
        List<Stock> list = commonDao.queryByHql(hql);
        Collections.sort(list);
        Stock firstStock=list.get(0);

        String logRemark = "";
        nickName = nickName == null ? "" : nickName;
        if (!nickName.equals(stockObj.getNickName())) {
            logRemark += "别名【新值:" + nickName + "】【旧值:" + stockObj.getNickName() + "】";
        }
        if (stockObj.getCost().intValue() != cost.intValue()) {
            logRemark += "成本价【新值:"+ cost.intValue()/100 +"】【旧值:"+ stockObj.getCost().intValue()/100 +"】";
        }

        Provider providerObj = (Provider)commonDao.getObjectByUniqueCode(Constant.provider, Constant.numberCode, provider);
        if (providerObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.provider_not_exist_error);
        }

        Provider oldProviderObj = (Provider)commonDao.getObjectByUniqueCode(Constant.provider, Constant.numberCode, stockObj.getProvider());
        if (!provider.equals(stockObj.getProvider())) {
            logRemark += "供应商【新值:" + providerObj.getNameCn() + "】";
            if (oldProviderObj != null) {
                logRemark += "【旧值:" + oldProviderObj.getNameCn() + "】";
            }
        }
        stockObj.setNickName(nickName);
        stockObj.setProvider(providerObj.getNumberCode());
        stockObj.setGrade(providerObj.getGrade());
        stockObj.setCost(cost);
        stockObj.setCostRemark(costRemark);
        stockObj.setType(stockType);
        if (stockType == 1) {
            stock = stock == null ? BigDecimal.ZERO : stock;
            stockObj.setStock(stock);
        }else {
            Cabin cabin = (Cabin)commonDao.getObjectByUniqueCode("Cabin", "numberCode", tkviewObj.getCabin());
            stockObj.setStock(cabin.getVolume().multiply(new BigDecimal(5)));
        }
        surpass = surpass == null ? BigDecimal.ZERO : surpass;
        stockObj.setSurpass(surpass);
        if(StringTool.isNotNull(overDate)){
            stockObj.setOverDate(DateUtil.stringToDate(overDate));
        }else{
            stockObj.setOverDate(null);
        }
        stockObj.setRemark(remark);
        stockObj.setDate(tkviewObj.getShipmentTravelDate());
        stockObj.setAlterDate(new Date());
        commonDao.update(stockObj);

       /* // 更新库存到淘宝SKU
        Map<String, Object> result = editStockObjToSku(stockObj.getTkview());
        if(Integer.valueOf(result.get(Constant.result_status).toString()) == 0){
            return ReturnUtil.returnMap(0, result.get(Constant.result_msg));
        }*/

        LogWork stockLogWork = new LogWork();
        stockLogWork.setAddDate(new Date());
        stockLogWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
        stockLogWork.setValue(tkviewObj.getNumberCode());
        stockLogWork.setTableName("stock");
        stockLogWork.setRemark("修改库存 " + logRemark);
        stockLogWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
        commonDao.save(stockLogWork);

        hql = "from Stock s where s.tkview = '" + stockObj.getTkview() + "' ";
        list = commonDao.queryByHql(hql);
        Collections.sort(list);

        Stock stockObjet = null;
        for (int h = 0; h < list.size(); h++ ) {
            stockObjet = list.get(h);
            stockObjet.setPriority(h);
            commonDao.update(stockObjet);
        }
        
        if(stockObj.getNumberCode().equals(firstStock.getNumberCode())||stockObj.compareTo(firstStock)<0){
      	  //自动更新计划任务
            PlanWork planWork=new PlanWork();
            planWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
            planWork.setAddDate(new Date());
            planWork.setContent(tkviewObj.getNumberCode());
            planWork.setUsername("system");
            planWork.setWorkType(Constant.PLAN_TYPE_UPDATE_TKVIEW);
            commonDao.save(planWork);
        }
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> deleteStock(String numberCode) {
    	
        Stock stockObj = (Stock)commonDao.getObjectByUniqueCode("Stock", "numberCode", numberCode);
        Tkview tkviewObj = (Tkview)commonDao.getObjectByUniqueCode("Tkview", "numberCode", stockObj.getTkview());
        if (tkviewObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.tkview_not_exist_error);
        }
        
    	String sql="select number_code from stock where tkview ='"+tkviewObj.getNumberCode()+"' ORDER BY priority";
    	@SuppressWarnings("unchecked")
		List<String> firstList=commonDao.getSqlQuery(sql).list();
        
        String stockSql = "delete from stock where number_code ='" + numberCode + "' ";
        commonDao.getSqlQuery(stockSql).executeUpdate();

        String hql = "from Stock s where s.tkview = '" + stockObj.getTkview() + "' ";
        @SuppressWarnings("unchecked")
        List<Stock> list = commonDao.queryByHql(hql);
        Collections.sort(list);

        Stock stockObjet = null;
        for(int h = 0; h < list.size(); h++ ) {
            stockObjet = list.get(h);
            stockObjet.setPriority(h);
            commonDao.update(stockObjet);
        }
        
        if(list.size()==0||firstList.get(0).equals(numberCode)){
        	  //自动更新计划任务
            PlanWork planWork=new PlanWork();
            planWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
            planWork.setAddDate(new Date());
            planWork.setContent(tkviewObj.getNumberCode());
            planWork.setUsername("system");
            planWork.setWorkType(Constant.PLAN_TYPE_UPDATE_TKVIEW);
            commonDao.save(planWork);
        }

       /* // 更新库存到淘宝SKU
        Map<String, Object> result = editStockObjToSku(stockObj.getTkview());
        if (Integer.valueOf(result.get(Constant.result_status).toString()) == 0) {
            return ReturnUtil.returnMap(0, result.get(Constant.result_msg));
        }*/

        return ReturnUtil.returnMap(1, null);
    }

    /**
     * 更新库存到淘宝SKU
     * 
     * @param tkview
     *            单品编号
     * @author : 熊玲
     */
    public Map<String, Object> editStockObjToSku(String tkview) {
        Tkview queryTkviewObj = (Tkview)commonDao.getObjectByUniqueCode("Tkview", "numberCode", tkview);
        if(queryTkviewObj == null){
            return ReturnUtil.returnMap(0, ConstantMsg.tkview_not_exist_error);
        }

        //根据 单品的邮轮和舱型编号 查询单品种类
        String tkviewTypeSql = "from TkviewType t where 1=1 ";
        tkviewTypeSql += "and t.cruise = '" + queryTkviewObj.getCruise() + "' ";
        tkviewTypeSql += "and t.cabin = '" + queryTkviewObj.getCabin() + "' ";
        @SuppressWarnings("unchecked")
        List<TkviewType> tkviewTypeList = commonDao.queryByHql(tkviewTypeSql);

        //根据单品种类编号获取关联的淘宝关联表信息
        String skuTkviewTypeSql = "SELECT st.iidd,st.number_code AS numberCode, ";
        skuTkviewTypeSql += "st.sku,st.tkview_type AS tkviewType ";
        skuTkviewTypeSql += "FROM sku_tkview_type st ";
        skuTkviewTypeSql += "WHERE st.tkview_type = '" + tkviewTypeList.get(0).getNumberCode() + "' ";
        skuTkviewTypeSql += "ORDER BY st.sku ";
        SQLQuery query = commonDao.getSqlQuery(skuTkviewTypeSql);
        query.addScalar("iidd").addScalar("numberCode").addScalar("sku").addScalar("tkviewType")
        .setResultTransformer(Transformers.aliasToBean(Sku_TkviewType.class));
        @SuppressWarnings("unchecked")
        List<Sku_TkviewType> skuTkviewTypeList = query.list();
        
        //将淘宝与单品种类的关联表转换成 Map集合（SKU编号：单品种类集合List）
        Map<String, List<String>> skuMap = new HashMap<String, List<String>>();
        String sku = "";
        List<String> skuMapList = null;
        for(Sku_TkviewType st : skuTkviewTypeList){
            if(StringTool.isEmpty(sku)){
                sku = st.getSku();
                skuMapList = new ArrayList<String>();
                skuMapList.add(st.getTkviewType());
            }else if(sku.equals(st.getSku())){
                skuMapList.add(st.getTkviewType());
            }else if(!sku.equals(st.getSku())){
                skuMap.put(sku, skuMapList);

                sku = st.getSku();
                skuMapList = new ArrayList<String>();
                skuMapList.add(st.getTkviewType());
            }
        }
        skuMap.put(sku, skuMapList);
        
        //获取加价逻辑信息
        String priceHql = "from PriceStrategy";
        @SuppressWarnings("unchecked")
        List<PriceStrategy> priceList = commonDao.queryByHql(priceHql);
        
        for(String key : skuMap.keySet()){
            List<String> tkviewTypeNumber = skuMap.get(key);  //关联的单品种类
            
            //查询当前淘宝SKU库存有没有符合日期的库存
            String skuStockHql = "from TaobaoSkuInfoDetail d where d.taobaoSkuInfo = '"+ key +"' ";
            skuStockHql += "and d.date = '"+ queryTkviewObj.getShipmentTravelDate() +"' ";
            skuStockHql += "and d.isLock = 0 ";
            @SuppressWarnings("unchecked")
            List<TaobaoSkuInfoDetail> detailList = commonDao.queryByHql(skuStockHql);
            if(detailList.size() < 1){
                continue;
            }
            
            //获取当前淘宝SKU关联的单品种类
            String typeHql = "from TkviewType t where t.numberCode in ("+ StringTool.listToSqlString(tkviewTypeNumber) +") ";
            typeHql += "and t.cruise = '"+ queryTkviewObj.getCruise() +"'";
            @SuppressWarnings("unchecked")
            List<TkviewType> queryTypeList = commonDao.queryByHql(typeHql);
            List<String> cabinNum = new ArrayList<>();
            for(TkviewType type : queryTypeList){
                cabinNum.add(type.getCabin());
            }
            
            //获取当前淘宝SKU管理的单品信息
            String tkviewHql = "from Tkview t where t.cruise = '" + queryTkviewObj.getCruise()+ "' ";
            tkviewHql += "and t.cabin in ("+ StringTool.listToSqlString(cabinNum) +") ";
            tkviewHql += "and t.shipmentTravelDate = '"+ DateUtil.dateToShortString(queryTkviewObj.getShipmentTravelDate()) +"' ";
            @SuppressWarnings("unchecked")
            List<Tkview> tkviewList = commonDao.queryByHql(tkviewHql);
            List<String> tkviewNum = new ArrayList<String>();
            for(Tkview t : tkviewList){
                tkviewNum.add(t.getNumberCode());
            }
            
            //获取当前淘宝SKU关联的单品库存信息
            String hql = "from Stock s where 1=1 ";
            hql += "and s.tkview in (" + StringTool.listToSqlString(tkviewNum) + ") ";
            hql += "order by s.tkview ";
            @SuppressWarnings("unchecked")
            List<Stock> stockList = commonDao.queryByHql(hql);
            if(stockList.size() > 0){
                for(Stock s : stockList){
                    for(Tkview tkviewObj : tkviewList){
                        if(s.getTkview().equals(tkviewObj.getNumberCode())) {
                            s.setTkviewNameCn(tkviewObj.getNameCn());
                            tkviewObj.getStocks().add(s);
                            break;
                        }
                    }
                }

                //给单品库存信息排序
                List<Tkview> tkviews = new ArrayList<Tkview>();
                for(Tkview t : tkviewList){
                    if(t.getStocks().size() > 0){
                        Collections.sort(t.getStocks());
                        t.setCost(t.getStocks().get(0).getCost());
                        t.setStock(t.getStocks().get(0).getStock());
                        tkviews.add(t);
                    }
                }
                Collections.sort(tkviews);
                
                //将所有拍好序的单品库存 存入到一个集合中
                List<Stock> resultStockList = new ArrayList<Stock>();
                for(Tkview tk : tkviews){
                    resultStockList.addAll(tk.getStocks());
                }
                
                //按加价逻辑给成本价加价
                Stock stockObj = resultStockList.get(0);
                BigDecimal cost = stockObj.getCost().divide(new BigDecimal(100));  //转换成真实数据的成本价
                BigDecimal price = stockObj.getCost();  //未转换的成本价
                for(PriceStrategy priceObj : priceList){
                    BigDecimal minPrice = priceObj.getMinPrice();
                    BigDecimal maxPrice = priceObj.getMaxPrice();
                    BigDecimal addPrice = priceObj.getPriceMarkup(); 
                    if(minPrice.compareTo(cost) <= 0 && maxPrice.compareTo(cost) >= 0){
                        price = cost.add(addPrice);
                        //价格(换成真实的格式)最后两位换成99
                        String priceStr = price.toString();
                        priceStr = priceStr.substring(0, priceStr.length() - 5) + "99";
                        
                        price = new BigDecimal(priceStr).multiply(new BigDecimal(100));
                        break;
                    }
                }
               
                Tkview tkviewTempObj = null;
                for(Tkview t : tkviewList){
                    if(stockObj.getTkview().equals(t.getNumberCode())){
                        tkviewTempObj = t;
                    }
                }
                Cabin cabinObj = (Cabin)commonDao.getObjectByUniqueCode("Cabin", "numberCode", tkviewTempObj.getCabin());
                if(cabinObj == null){
                    return ReturnUtil.returnMap(0, "单品舱型信息已经不存在了!");
                }
                
                Shipment shipObj = (Shipment)commonDao.getObjectByUniqueCode("Shipment","numberCode", tkviewTempObj.getShipment());
                if(shipObj == null){
                    return ReturnUtil.returnMap(0, "单品航期信息已经不存在了!");
                }

                //修改SKU库存信息
                String updateSkuStockSql = "UPDATE taobao_sku_info_detail d SET ";
                updateSkuStockSql += "d.out_id='"+ stockObj.getTkview() +"', ";
                updateSkuStockSql += "d.price="+ price +",  ";
                updateSkuStockSql += "d.stock="+ stockObj.getStock().multiply(cabinObj.getMaxVolume()) +", ";
                updateSkuStockSql += "d.airway='"+ shipObj.getAirway() +"', ";
                updateSkuStockSql += "d.is_update=1, ";
                updateSkuStockSql += "d.out_stock='"+ stockObj.getNumberCode() +"',d.alter_date=NOW() ";
                updateSkuStockSql += "WHERE d.taobao_sku_info = '"+ key +"' ";
                updateSkuStockSql += "AND d.date = '"+ DateUtil.dateToShortString(queryTkviewObj.getShipmentTravelDate()) +"' ";
                updateSkuStockSql += "AND d.is_lock = 0 "; //价格不锁定
                commonDao.getSqlQuery(updateSkuStockSql).executeUpdate();
            }
            
        }
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> setPriority(String numberCode , int isUp) {
        Stock stockAfter = (Stock)commonDao.getObjectByUniqueCode("Stock", "numberCode", numberCode);
        if (stockAfter == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.stock_not_exist_error);
        }

        int tempPriority = stockAfter.getPriority();
        Date date = new Date();

        String hql = "from Stock s where s.tkview = '" + stockAfter.getTkview() + "' ";
        @SuppressWarnings("unchecked")
        List<Stock> list = commonDao.queryByHql(hql);
        Collections.sort(list);

        Stock stockObjet = null;
        for (int h = 0; h < list.size(); h++ ) {
            stockObjet = list.get(h);
            if(stockObjet.getNumberCode().equals(numberCode)){
                if(h == (list.size() - 1) && isUp == 1){
                    return ReturnUtil.returnMap(0, "已是最后一个!");
                }
                if(h == 0 && isUp == 0){
                    return ReturnUtil.returnMap(0, "已是最初一个!");
                }

                int temp = h;
                if(isUp == 1){// 向下交换
                    temp = temp + 1;
                }else{
                    temp = temp - 1;
                }
                Stock stockObj = list.get(temp);
                stockAfter.setPriority(stockObj.getPriority());
                stockAfter.setAlterDate(date);
                commonDao.update(stockAfter);

                stockObj.setPriority(tempPriority);
                stockObj.setAlterDate(date);
                commonDao.update(stockObj);
            }
        }

        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> getCabinByCruise(String cruise) {
        String cabinHql = "from Cabin c where c.cruise = '" + cruise + "' and c.isOpen='开' ";
        cabinHql += "order by c.type ";
        @SuppressWarnings("unchecked")
        List<Cabin> list = commonDao.queryByHql(cabinHql);

        return ReturnUtil.returnMap(1, list);
    }

    @Override
    public Map<String, Object> addStockList(String cabinStr , String shipStr , String nickName ,
                                            String provider , int type , BigDecimal stock ,
                                            BigDecimal surpass , BigDecimal cost ,
                                            String costRemark , int priority , String remark) {
        if(StringTool.isEmpty(cabinStr) && StringTool.isEmpty(shipStr)){
            return ReturnUtil.returnMap(0, "请勾选条件信息!");
        }
        if(StringTool.isEmpty(provider)){
            return ReturnUtil.returnMap(0, ConstantMsg.provider_not_exist_error);
        }

        Provider providerObj = (Provider)commonDao.getObjectByUniqueCode("Provider", "numberCode", provider);
        if(providerObj == null){
            return ReturnUtil.returnMap(0, ConstantMsg.provider_not_exist_error);
        }

        //根据舱型、航期获取单品信息
        String tkviewHql = "from Tkview where 1=1 ";
        //tkviewHql += possessorUtil.getRelatesWhereHql("tkview");
        if(StringTool.isNotNull(cabinStr)){
            tkviewHql += "and cabin in (" + StringTool.StrToSqlString(cabinStr) + ") ";
        }
        if (StringTool.isNotNull(shipStr)){
            tkviewHql += "and shipment in (" + StringTool.StrToSqlString(shipStr) + ") ";
        }
        tkviewHql += "order by numberCode ";
        @SuppressWarnings("unchecked")
        List<Tkview> tkviewList = commonDao.queryByHql(tkviewHql);
        //单品编号集合
        List<String> numberList = new ArrayList<>();
        for (Tkview tkObj : tkviewList) {
            numberList.add(tkObj.getNumberCode());
        }

        //获取单品库存
        String stock_hql = "from Stock s where s.provider = '" + provider + "' ";
        stock_hql += "and s.tkview in (" + StringTool.listToSqlString(numberList) + ") ";
        stock_hql += "and s.type = " + type +" ";
        stock_hql += "order by s.tkview ";
        @SuppressWarnings("unchecked")
        List<Stock> stockList = commonDao.queryByHql(stock_hql);

        List<Tkview> addList = new ArrayList<Tkview>();    //新增库存的单品集合
        List<Stock> editList = new ArrayList<Stock>();     //修改库存的单品库存集合
        List<Tkview> editTkList = new ArrayList<Tkview>(); //修改库存的单品集合
        for(Tkview tkObj : tkviewList){
            boolean ps = true;
            for(Stock stockObj : stockList){
                if(tkObj.getNumberCode().equals(stockObj.getTkview()) && provider.equals(stockObj.getProvider())){
                    editList.add(stockObj);
                    editTkList.add(tkObj);
                    ps = false;
                    break;
                }
            }
            if(ps){
                addList.add(tkObj);
            }
        }

        Date date = new Date();
        Stock stockObj = null;
        /* 新增库存 */
        for(Tkview tkviewObj : addList){
            Date finalDate = DateUtil.addDay(tkviewObj.getShipmentTravelDate(), -3);
            String finalStr = DateUtil.dateToShortString(finalDate);
            Date overDate = DateUtil.shortStringToDate(finalStr);

            stockObj = new Stock();
            stockObj.setNumberCode(UUIDUtil.getUUIDStringFor32());
            stockObj.setTkview(tkviewObj.getNumberCode());
            stockObj.setNickName(nickName);
            stockObj.setProvider(provider);
            stockObj.setGrade(providerObj.getGrade());
            stockObj.setCost(cost);
            stockObj.setCostRemark(costRemark);
            stockObj.setOverDate(overDate);
            stockObj.setPriority(priority);
            stockObj.setType(type);
            if(type == 1){// 实库
                stockObj.setStock(stock);
                stockObj.setSurpass(surpass);
            }else{
                Cabin cabinObj = (Cabin)commonDao.getObjectByUniqueCode("Cabin", "numberCode", tkviewObj.getCabin());
                stockObj.setStock(cabinObj.getVolume().multiply(new BigDecimal(5)));
                stockObj.setSurpass(BigDecimal.ZERO);
            }
            stockObj.setUseSurpass(BigDecimal.ZERO);
            stockObj.setUseStock(BigDecimal.ZERO);
            stockObj.setDate(tkviewObj.getShipmentTravelDate());
            stockObj.setAddDate(date);
            stockObj.setAlterDate(date);
            stockObj.setRemark(remark);
            commonDao.save(stockObj);

    /*        //跟新单品库存到淘宝SKU库存
            Map<String, Object> result = editStockObjToSku(stockObj.getTkview());
            if(Integer.valueOf(result.get(Constant.result_status).toString()) == 0) {
                return ReturnUtil.returnMap(0, result.get(Constant.result_msg));
            }*/

            String tempType = stockObj.getType() == 0 ? "现询" : "实库";
            LogWork stockLogWork = new LogWork();
            stockLogWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
            stockLogWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
            stockLogWork.setValue(tkviewObj.getNumberCode());
            stockLogWork.setTableName("stock");
            stockLogWork.setRemark("添加单品【" + tkviewObj.getNameCn() + "】库存【供应商:"
                         + providerObj.getNameCn() + "】【类型:" + tempType + "】【总库存:"
                         + stockObj.getStock() + "】");
            stockLogWork.setAddDate(date);
            commonDao.save(stockLogWork);

            //给单品库存重新排序
            String stockHql = "from Stock s where s.tkview = '" + stockObj.getTkview() + "' ";
            @SuppressWarnings("unchecked")
            List<Stock> list = commonDao.queryByHql(stockHql);
            Collections.sort(list);
            Stock stockObjet = null;
            for (int h = 0; h < list.size(); h++ ) {
                stockObjet = list.get(h);
                stockObjet.setPriority(h);
                commonDao.update(stockObjet);
            }
            
            if((list.size()==1||list.get(0).getNumberCode().equals(stockObj.getNumberCode()))&&list.get(0).getStock().intValue()!=0){
                //自动更新计划任务
                PlanWork planWork=new PlanWork();
                planWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
                planWork.setAddDate(new Date());
                planWork.setContent(tkviewObj.getNumberCode());
                planWork.setUsername("system");
                planWork.setWorkType(Constant.PLAN_TYPE_UPDATE_TKVIEW);
                commonDao.save(planWork);
            }
            
        }

        /* 编辑库存 */
        for(Stock skObj : editList){
            Tkview tkviewObj = null;
            for(Tkview tkObj : editTkList){
                if(skObj.getTkview().equals(tkObj.getNumberCode())){
                    tkviewObj = tkObj;
                    break;
                }
            }
            //有效期：设为出发日期的前3天
            Date finalDate = DateUtil.addDay(tkviewObj.getShipmentTravelDate(), -3);
            String finalStr = DateUtil.dateToShortString(finalDate);
            Date overDate = DateUtil.shortStringToDate(finalStr);

            stockObj = (Stock)commonDao.getObjectByUniqueCode("Stock", "numberCode", skObj.getNumberCode());
            if(stockObj == null){
                return ReturnUtil.returnMap(0, ConstantMsg.stock_not_exist_error);
            }

            String logRemark = "";
            nickName = nickName == null ? "" : nickName;
            if(!nickName.equals(stockObj.getNickName())){
                logRemark += "别名【新值:"+ nickName +"】【旧值:"+ skObj.getNickName() +"】";
            }
            if(skObj.getCost().intValue() != cost.intValue()){
                logRemark += "成本价【新值:"+ cost.intValue()/100 +"】【旧值:"+ skObj.getCost().intValue()/100 +"】";
            }
            if(type != skObj.getType()){
                logRemark += "库存类型【新值:"+ (type==1?"实库":"现询") +"】【旧值:"+ (skObj.getType()==1?"实库":"现询") +"】";
            }
            BigDecimal oldStock = stockObj.getStock();

            
         	String stockHql = "from Stock s where s.tkview = '" + stockObj.getTkview() + "' ";
            @SuppressWarnings("unchecked")
            List<Stock> list = commonDao.queryByHql(stockHql);
        	Stock firstStock=list.get(0);
            
            if(type == 1 && stock.compareTo(BigDecimal.ZERO) != 0){
                stockObj.setStock(stock);
                stockObj.setSurpass(BigDecimal.ZERO);
            }else{
                stockObj.setCost(cost);
                stockObj.setCostRemark(costRemark);
                stockObj.setType(type);
                if(type == 1){
                    stock = stock == null ? BigDecimal.ZERO : stock;
                    stockObj.setStock(stock);
                }else{
                    Cabin cabin = (Cabin)commonDao.getObjectByUniqueCode("Cabin", "numberCode", tkviewObj.getCabin());
                    stock = cabin.getVolume().multiply(new BigDecimal(5));
                    stockObj.setStock(stock);
                }
                surpass = surpass == null ? BigDecimal.ZERO : surpass;
                stockObj.setSurpass(surpass);
            }
            stockObj.setNickName(nickName);
            stockObj.setProvider(providerObj.getNumberCode());
            stockObj.setGrade(providerObj.getGrade());
            stockObj.setOverDate(overDate);
            stockObj.setDate(tkviewObj.getShipmentTravelDate());
            stockObj.setRemark(remark);
            stockObj.setAlterDate(date);
            commonDao.update(stockObj);

        /*    //更新单品库存到淘宝SKU库存
            Map<String, Object> result = editStockObjToSku(stockObj.getTkview());
            if (Integer.valueOf(result.get(Constant.result_status).toString()) == 0) {
                return ReturnUtil.returnMap(0, result.get(Constant.result_msg));
            }*/

            if(stock.compareTo(oldStock) != 0){
               logRemark += "库存【新值:" + stock + "】【旧值:" + oldStock + "】";
            }
            LogWork stockLogWork = new LogWork();
            stockLogWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
            stockLogWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
            stockLogWork.setValue(tkviewObj.getNumberCode());
            stockLogWork.setTableName("stock");
            stockLogWork.setRemark("编辑库存:" + logRemark);
            stockLogWork.setAddDate(date);
            commonDao.save(stockLogWork);

            //给单品库存重新排序
            stockHql = "from Stock s where s.tkview = '" + stockObj.getTkview() + "' ";
            list = commonDao.queryByHql(stockHql);
            Collections.sort(list);
            Stock stockObjet = null;
            for(int h = 0; h < list.size(); h++ ){
                stockObjet = list.get(h);
                stockObjet.setPriority(h);
                commonDao.update(stockObjet);
            }
            
            if(stockObj.getNumberCode().equals(firstStock.getNumberCode())||stockObj.compareTo(firstStock)<0){
            	  //自动更新计划任务
                  PlanWork planWork=new PlanWork();
                  planWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
                  planWork.setAddDate(new Date());
                  planWork.setContent(tkviewObj.getNumberCode());
                  planWork.setUsername("system");
                  planWork.setWorkType(Constant.PLAN_TYPE_UPDATE_TKVIEW);
                  commonDao.save(planWork);
              }
            
        }
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> getShipmentByCruise(String cruise) {
        String shipHql = "from Shipment s where s.cruise = '" + cruise + "' ";
        shipHql += "and s.travelDate >= NOW() ";
        shipHql += "order by s.travelDate ";
        @SuppressWarnings("unchecked")
        List<Shipment> list = commonDao.queryByHql(shipHql);

        return ReturnUtil.returnMap(1, list);
    }

    @Override
    public Map<String, Object> getStockListByCruise(String cruise , String cabin , String shipment) {
        if(StringTool.isEmpty(cabin) && StringTool.isEmpty(shipment)){
            return ReturnUtil.returnMap(0, "请选择条件!");
        }

        String tkviewHql = "from Tkview where cruise = '" + cruise + "' ";
        tkviewHql += possessorUtil.getRelatesWhereHql("tkview");
        if(StringTool.isNotNull(cabin)){
            tkviewHql += "and cabin in (" + StringTool.StrToSqlString(cabin) + ") ";
        }else if(StringTool.isNotNull(shipment)){
            tkviewHql += "and shipment in (" + StringTool.StrToSqlString(shipment) + ") ";
        }
        tkviewHql += "and shipmentTravelDate >= NOW() ";
        tkviewHql += "order by cabin,shipment ";
        @SuppressWarnings("unchecked")
        List<Tkview> list = commonDao.queryByHql(tkviewHql);

        List<String> numberList = new ArrayList<String>();    //单品编号集合
        List<String> cabinNumber = new ArrayList<String>();   //舱型编号集合
        for(Tkview t : list){
            numberList.add(t.getNumberCode());
            cabinNumber.add(t.getCabin());
        }

        //获取舱型信息
        String cabinHql = "from Cabin c where c.numberCode in ("+ StringTool.listToSqlString(cabinNumber) +") ";
        @SuppressWarnings("unchecked")
        List<Cabin> cabinList = commonDao.queryByHql(cabinHql);

        //获取单品下的库存信息
        String stockHql = "from Stock s where s.tkview in ("+ StringTool.listToSqlString(numberList) +") ";
        //stockHql += "and s.overDate > NOW() ";
        @SuppressWarnings("unchecked")
        List<Stock> stockList = commonDao.queryByHql(stockHql);

        //给单品设置 对应的舱型名称
        for(Tkview tkviewObj : list){
            for(Cabin cabinObj : cabinList){
                if (tkviewObj.getCabin().equals(cabinObj.getNumberCode())) {
                    tkviewObj.setCabinNameCn(cabinObj.getNameCn());
                }
            }
        }

        List<Tkview> tkviewList = new ArrayList<Tkview>();
        if(stockList.size() > 0){
            List<String> strList = new ArrayList<>();
            for(Stock s : stockList){
               strList.add(s.getProvider());
            }

            String providerHql = "from Provider p where p.numberCode in ("+ StringTool.listToSqlString(strList) +") ";
            @SuppressWarnings("unchecked")
            List<Provider> providerList = commonDao.queryByHql(providerHql);

            //给单品库存设置 对应的供应商的代码和名称
            for(Stock s : stockList){
                for(Provider p : providerList) {
                    if(s.getProvider().equals(p.getNumberCode())){
                        s.setProviderNameCn(p.getNameCn());
                        s.setProviderMark(p.getMark());
                        break;
                    }
                }
            }

            //将单品库存 存入到相应的单品中
            for(Stock s : stockList){
                for(Tkview tkviewObj : list) {
                    if(s.getTkview().equals(tkviewObj.getNumberCode())){
                        s.setTkviewNameCn(tkviewObj.getNameCn());
                        tkviewObj.getStocks().add(s);
                        break;
                    }
                }
            }
            //给单品库存排序
            for(Tkview t : list){
                if(t.getStocks().size() > 0){
                    Collections.sort(t.getStocks());
                    t.setCost(t.getStocks().get(0).getCost());
                    t.setStock(t.getStocks().get(0).getStock());
                    tkviewList.add(t);
                }
            }
            Collections.sort(tkviewList);

        }

        return ReturnUtil.returnMap(1, tkviewList);
    }

    @Override
    public Map<String, Object> getCabinByTkiew(String cruise) {
        
        String tkviewtHql="from Tkview s where 1 = 1 and  s.cruise = '"+cruise+"' and  s.shipmentTravelDate >= NOW()";
        tkviewtHql+="GROUP BY s.cabin ";
        @SuppressWarnings("unchecked")
        List<Tkview> tkviewList = commonDao.queryByHql(tkviewtHql);
        
        Set<String> cabinSet = new HashSet<String>();
        for(Tkview t : tkviewList){
            cabinSet.add(t.getCabin());
        }
        
        String cabineHql = "from Cabin t where t.numberCode in ("+StringTool.listToSqlString(cabinSet)+")";
        @SuppressWarnings("unchecked")
        List<Cabin> cabinList = commonDao.queryByHql(cabineHql);
        
        for(Tkview tkview : tkviewList){
            for(Cabin cabin : cabinList){
                if(tkview.getCabin().equals(cabin.getNumberCode())){
                    tkview.setCabinNameCn(cabin.getNameCn());
                    break;
                }
            }
        }
        return ReturnUtil.returnMap(1, tkviewList);
    }
    
    @Override
    public Map<String, Object> getShipmentByTkiew(String cruise) {
        
        String tkviewtHql="from Tkview s where 1 = 1 and  s.cruise = '"+cruise+"' and  s.shipmentTravelDate >= NOW()";
        tkviewtHql+="GROUP BY s.shipment ";
        
        @SuppressWarnings("unchecked")
        List<Tkview> tkviewList = commonDao.queryByHql(tkviewtHql);
        
        return ReturnUtil.returnMap(1, tkviewList);

    }
    
    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> getStockByTkview(String cruise) {
        
        String tkviewtHql="select s.numberCode from Tkview s where 1 = 1 and  s.cruise = '"+cruise+"' and  s.shipmentTravelDate >= NOW()";
        tkviewtHql+="GROUP BY s.cabin ";
        
        List<String> tkviews = commonDao.queryByHql(tkviewtHql);
        
        String stocktHql="from Stock s where 1 = 1 and  s.tkview IN ("+StringTool.listToSqlString(tkviews)+")  ";
        stocktHql+="GROUP BY s.provider ";
       
        List<Stock> stockList = commonDao.queryByHql(stocktHql);
        
        Set<String> providerSet = new HashSet<String>();
        for(Stock s : stockList){
            providerSet.add(s.getProvider());
        }
        
        String providerHql = "from Provider t where t.numberCode in ("+StringTool.listToSqlString(providerSet)+")";
        List<Provider> providerList = commonDao.queryByHql(providerHql);
        
        for(Stock stock : stockList){
            for(Provider provider : providerList){
                if(stock.getProvider().equals(provider.getNumberCode())){
                    stock.setProviderNameCn(provider.getNameCn());
                    break;
                }
            }
        }
        return ReturnUtil.returnMap(1, stockList);
    }
    
    @Override
    public Map<String, Object> editStockByCabinAndShipmentAndProvider(String cabin ,
                                                                      String shipment ,
                                                                      String provider) {

        //根据舱型、航期查询单品信息
        String tkviewSql = "SELECT t.number_code FROM tkview t WHERE 1=1  ";
        tkviewSql += possessorUtil.getRelatesWhereSql("tkview", "t");
        if(StringTool.isNotNull(cabin)){
            tkviewSql += "AND t.cabin IN (" + StringTool.StrToSqlString(cabin) + ") ";
        }
        if(StringTool.isNotNull(shipment)){
            tkviewSql += "AND t.shipment IN (" + StringTool.StrToSqlString(shipment) + ") ";
        }
        @SuppressWarnings("unchecked")
        List<String> tkviewList = commonDao.getSqlQuery(tkviewSql).list();
        if(tkviewList.size() < 1){
            return ReturnUtil.returnMap(0, "没有单品数据!");
        }
        
        String slq = "UPDATE Stock s SET s.stock = 0 WHERE  1 = 1  and s.tkview IN (" + StringTool.listToSqlString(tkviewList) + ")";
        if(StringTool.isNotNull(provider)){
            slq+=" and s.provider IN (" + StringTool.StrToSqlString(provider) + ") ";
        }
        commonDao.getSqlQuery(slq).executeUpdate();
        
        for(String tkview:tkviewList){
        	 //自动更新计划任务
            PlanWork planWork=new PlanWork();
            planWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
            planWork.setAddDate(new Date());
            planWork.setContent(tkview);
            planWork.setUsername("system");
            planWork.setWorkType(Constant.PLAN_TYPE_UPDATE_TKVIEW);
            commonDao.save(planWork);
        }
        
        return ReturnUtil.returnMap(1, null);
    }

   

}