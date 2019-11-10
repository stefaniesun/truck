package xyz.work.resources.svc.imp;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
import xyz.util.PossessorUtil;
import xyz.util.PriceUtil;
import xyz.util.StringTool;
import xyz.util.StringUtil;
import xyz.util.UUIDUtil;
import xyz.work.base.model.Airway;
import xyz.work.base.model.Cabin;
import xyz.work.base.model.Cruise;
import xyz.work.base.model.Shipment;
import xyz.work.core.model.LogWork;
import xyz.work.core.model.PlanWork;
import xyz.work.excel.model.PriceStrategy;
import xyz.work.menu.svc.imp.QueryResourcesSvcImp;
import xyz.work.resources.model.Provider;
import xyz.work.resources.model.RTkview;
import xyz.work.resources.model.ShipmentData;
import xyz.work.resources.model.Stock;
import xyz.work.resources.model.Tkview;
import xyz.work.resources.model.TkviewType;
import xyz.work.resources.svc.TkviewSvc;
import xyz.work.security.model.Possessor;
import xyz.work.taobao.model.TaobaoBaseInfo;
import xyz.work.taobao.model.TaobaoSkuInfo;
import xyz.work.taobao.model.TaobaoSkuInfoDetail;


@Service
public class TkviewSvcImp implements TkviewSvc {

    @Autowired
    CommonDao commonDao;

    @Autowired
    PossessorUtil possessorUtil;

    @Override
    public Map<String, Object> queryTkviewList(int offset , int pagesize , String cruise ,
                                               String shipment , String cabin , String nameCn ,
                                               String mark , String mouth , String sort ,
                                               String order , String mode, String provider) {
        
        StringBuffer sql = new StringBuffer();
        sql.append("FROM tkview t ");
        if(StringTool.isNotNull(provider)){
            sql.append("LEFT JOIN stock s ON s.tkview = t.number_code ");
        }
        sql.append("WHERE 1=1 ");
        if(StringTool.isNotNull(provider)){
            sql.append("AND s.provider IN (" + StringTool.StrToSqlString(provider) + ") ");
        }
        if(!"noValue".equals(cruise)){
            sql.append("AND t.cruise = '"+ cruise +"' ");
        }
        sql.append("AND t.shipment_travel_date >= NOW() ");
        if(StringTool.isNotNull(mode) && mode.equals("packetBoat")){
            sql.append("AND t.name_cn REGEXP '[a-z]+' ");
        }else if(StringTool.isNotNull(mode) && mode.equals("sale")){
             sql.append("AND t.name_cn NOT REGEXP '[a-z]+' ");
        }
        if(StringTool.isNotNull(cabin)) {
            sql.append("AND t.cabin = '"+ cabin + "' ");
        }
        if(StringTool.isNotNull(shipment)) {
            sql.append("AND t.shipment IN ("+ StringTool.StrToSqlString(shipment) + ") ");
        }
        if(StringTool.isNotNull(nameCn)) {
            sql.append("AND t.name_cn LIKE '%"+ nameCn + "%' ");
        }
        if(StringTool.isNotNull(mark)) {
            sql.append("AND t.mark LIKE '%"+ mark + "%' ");
        }
        if(StringTool.isNotNull(mouth)) {
            sql.append("AND DATE_FORMAT(t.shipment_travel_date,'%Y-%m') IN ("+ StringTool.StrToSqlString(mouth) +") ");
        }
        sql.append("ORDER BY t.shipment_travel_date");
        if(StringTool.isNotNull(mode) && mode.equals("sale")){
            sql.append(",t.name_cn=CASE t.name_cn WHEN t.name_cn LIKE '%随机内舱双人间%' THEN 1");
            sql.append("  WHEN t.name_cn LIKE '%随机内舱三人间%' THEN 2");
            sql.append("  WHEN t.name_cn LIKE '%随机内舱四人间%' THEN 3");
            sql.append("  WHEN t.name_cn LIKE '%随机海景双人间%' THEN 4");
            sql.append("  WHEN t.name_cn LIKE '%随机海景三人间%' THEN 5");
            sql.append("  WHEN t.name_cn LIKE '%随机海景四人间%' THEN 6");
            sql.append("  WHEN t.name_cn LIKE '%随机阳台双人间%' THEN 7");
            sql.append("  WHEN t.name_cn LIKE '%随机阳台三人间%' THEN 8");
            sql.append("  WHEN t.name_cn LIKE '%随机阳台四人间%' THEN 9");
            sql.append("  ELSE 10");
            sql.append(" END ");
        }
        
        Query countQuery = commonDao.getSqlQuery("select count(*) " + sql.toString());
        Number tempCount = (Number)countQuery.uniqueResult();
        int total = tempCount == null ? 0 : tempCount.intValue();
        
        String tkSql = "SELECT t.number_code AS numberCode, ";
        tkSql += "t.name_cn AS nameCn,t.mark AS mark, ";
        tkSql += "t.cruise AS cruise,t.cabin AS cabin, ";
        tkSql += "t.volume AS volume,t.shipment AS shipment, ";
        tkSql += "t.shipment_mark AS shipmentMark, ";
        tkSql += "t.shipment_travel_date AS shipmentTravelDate, ";
        tkSql += "t.user_name AS userName, t.release_date AS releaseDate, ";
        tkSql += "t.remark AS remark,t.add_date AS addDate, ";
        tkSql += "t.alter_date AS alterDate,t.iidd AS iidd ";
        SQLQuery sqlTotal = commonDao.getSqlQuery(tkSql + sql.toString());
        sqlTotal.addScalar("numberCode").addScalar("nameCn")
        .addScalar("mark").addScalar("cruise").addScalar("cabin")
        .addScalar("volume").addScalar("shipment").addScalar("shipmentMark")
        .addScalar("shipmentTravelDate").addScalar("userName").addScalar("releaseDate")
        .addScalar("remark").addScalar("addDate").addScalar("alterDate").addScalar("iidd")
        .setResultTransformer(Transformers.aliasToBean(Tkview.class));
        sqlTotal.setFirstResult(offset);
        sqlTotal.setMaxResults(pagesize);
        
        @SuppressWarnings("unchecked")
        List<Tkview> tkviewList = sqlTotal.list();
        
        List<String> cabinNumber = new ArrayList<String>();
        List<String> tkviewNumber = new ArrayList<String>();
        for (Tkview tkviewObj : tkviewList) {
            tkviewNumber.add(tkviewObj.getNumberCode());
            cabinNumber.add(tkviewObj.getCabin());
        }
        
        String stockHql = "from Stock s where s.tkview in ("+ StringTool.listToSqlString(tkviewNumber) +") ";
        stockHql += "order by s.tkview ";
        @SuppressWarnings("unchecked")
        List<Stock> stockList = commonDao.queryByHql(stockHql);
        
        String cruiseSql = "SELECT number_code,name_cn FROM cruise WHERE 1=1 ";
        if(!"noValue".equals(cruise)){
            cruiseSql += "AND number_code in ("+ StringTool.listToSqlString(cabinNumber) + ") ";
        }
        @SuppressWarnings("unchecked")
        List<Object[]> cruiseList = commonDao.getSqlQuery(cruiseSql).list();

        String cabinSql = "SELECT number_code,name_cn FROM cabin WHERE number_code in ("+ StringTool.listToSqlString(cabinNumber) + ") ";
        @SuppressWarnings("unchecked")
        List<Object[]> cabinList = commonDao.getSqlQuery(cabinSql).list();

        for (Tkview tkviewObj : tkviewList) {
            for (Object[] cruiseObj : cruiseList) {
                if (tkviewObj.getCruise().equals(cruiseObj[0])) {
                    tkviewObj.setCruiseNameCn((String)cruiseObj[1]);
                    break;
                }
            }
            for (Object[] cabinObj : cabinList) {
                if (tkviewObj.getCabin().equals(cabinObj[0])) {
                    tkviewObj.setCabinNameCn((String)cabinObj[1]);
                    break;
                }
            }
            
            int stockCount = 0;
            String providerNumber = "";
            for(Stock stockObj : stockList){
                if(tkviewObj.getNumberCode().equals(stockObj.getTkview())){
                    if(StringTool.isEmpty(providerNumber) || (!providerNumber.equals(stockObj.getProvider())) ){
                        providerNumber = stockObj.getProvider();
                        stockCount++;
                    }
                }
            }
            tkviewObj.setStockCount(stockCount);
        }

        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", total);
        mapContent.put("rows", tkviewList);
        
        return ReturnUtil.returnMap(1, mapContent);
    }

    @Override
    public Map<String, Object> addTkview(String nameCn , String mark , String cruise ,
                                         String shipment , String cabin , String remark) {

        if (StringTool.isEmpty(cruise)) {
            return ReturnUtil.returnMap(0, ConstantMsg.cruise_null);
        }
        if (StringTool.isEmpty(shipment)) {
            return ReturnUtil.returnMap(0, ConstantMsg.shipment_null);
        }
        if (StringTool.isEmpty(cabin)) {
            return ReturnUtil.returnMap(0, ConstantMsg.cabin_null);
        }

        Cruise cruiseObj = (Cruise)commonDao.getObjectByUniqueCode(Constant.cruise, Constant.numberCode, cruise);
        Date date = new Date();
        if (cruiseObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.cruise_null);
        }
        Shipment shipmentObj = (Shipment)commonDao.getObjectByUniqueCode(Constant.shipment, Constant.numberCode, shipment);
        if (shipmentObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.shipment_null);
        }

        String existSql = "select name_cn from tkview where name_cn = '" + nameCn + "' ";
        existSql += " and shipment = '" + shipment + "'";
        existSql += possessorUtil.getRelatesWhereSql(Constant.relate_type_tkview);

        @SuppressWarnings("unchecked")
        List<String> exitstList = commonDao.getSqlQuery(existSql).list();
        if (exitstList.size() >= 1) {
            return ReturnUtil.returnMap(0, "同一航期,单品名称不能重复!");
        }

        Tkview tkview = new Tkview();
        tkview.setNumberCode(StringUtil.get_new_tkview());
        tkview.setAddDate(date);
        tkview.setAlterDate(date);
        tkview.setCabin(cabin);
        Cabin cabinObj = (Cabin)commonDao.getObjectByUniqueCode(Constant.cabin,Constant.numberCode, cabin);
        if (cabin == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.cabin_null);
        }
        tkview.setVolume(cabinObj.getVolume());
        tkview.setCruise(cruise);
        tkview.setNameCn(nameCn);
        tkview.setMark(mark);
        tkview.setRemark(remark);
        tkview.setShipment(shipment);
        tkview.setShipmentMark(shipmentObj.getMark());
        tkview.setShipmentTravelDate(shipmentObj.getTravelDate());
        commonDao.save(tkview);
        
        possessorUtil.savePossessorRelates(Constant.relate_type_tkview, tkview.getNumberCode());
        LogWork logWork = new LogWork();
        logWork.setAddDate(new Date());
        logWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
        logWork.setValue(tkview.getNumberCode());
        logWork.setTableName("tkview");
        logWork.setRemark("添加单品【" + tkview.getNameCn() + "】");
        logWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
        commonDao.save(logWork);
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> editTkview(String numberCode , String nameCn , String mark ,
                                          String remark) {
        Date date = new Date();
        Tkview tkviewObj = (Tkview)commonDao.getObjectByUniqueCode(Constant.tkview, Constant.numberCode, numberCode);
        if (tkviewObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.tkview_not_exist_error);
        }

        String logRemark = "";
        if (!tkviewObj.getNameCn().equals(nameCn)) {
            logRemark += "名称【新值:" + nameCn + "】【旧值:" + tkviewObj.getNameCn() + "】";
        }

        mark = mark == null ? "" : mark;
        if (!mark.equals(tkviewObj.getMark())) {
            logRemark += "代码【新值:" + mark + "】【旧值:" + tkviewObj.getMark() == null ? "" : tkviewObj.getMark();
        }
        String existSql = "SELECT t.name_cn FROM tkview t WHERE t.name_cn = '" + nameCn + "' ";
        existSql += "AND t.shipment = '" + tkviewObj.getShipment() + "' ";
        existSql += "AND t.number_code <> '"+ numberCode +"' ";
        existSql += possessorUtil.getRelatesWhereSql(Constant.relate_type_tkview);
        @SuppressWarnings("unchecked")
        List<String> exitstList = commonDao.getSqlQuery(existSql).list();
        if (!nameCn.equals(tkviewObj.getNameCn())) {
            if (exitstList.size() >= 1) {
                return ReturnUtil.returnMap(0, "同一航期,单品名称不能重复!");
            }
        }
        
        tkviewObj.setAlterDate(date);
        tkviewObj.setNameCn(nameCn);
        tkviewObj.setMark(mark);
        tkviewObj.setRemark(remark);
        commonDao.update(tkviewObj);

        LogWork logWork = new LogWork();
        logWork.setAddDate(date);
        logWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
        logWork.setValue(tkviewObj.getNumberCode());
        logWork.setTableName("tkview");
        logWork.setRemark("修改单品【" + tkviewObj.getNumberCode() + "】" + logRemark);
        logWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
        commonDao.save(logWork);

        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> deleteTkviews(String numberCodes) {

        if (numberCodes == null || "".equals(numberCodes)) {
            return ReturnUtil.returnMap(0, ConstantMsg.tkview_numberCode_error);
        }

        String sql = "delete from tkview where number_code in ("+ StringTool.StrToSqlString(numberCodes) + ") ";
        commonDao.getSqlQuery(sql).executeUpdate();

        String stockSql = "delete from stock where tkview in ("+ StringTool.StrToSqlString(numberCodes) + ")";
        commonDao.getSqlQuery(stockSql).executeUpdate();

        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> queryPossessorTkviewList(Boolean isTrue , String possessor ,
                                                        String numberCode , String name) {

        StringBuffer hqlSb = new StringBuffer();

        hqlSb.append("from Tkview where 1=1");
        List<String> relates = possessorUtil.getDecideMapByPossessor(possessor,Constant.relate_type_tkview).keySet()
           .contains(Constant.relate_type_tkview) ? possessorUtil.getDecideMapByPossessor(possessor, 
           Constant.relate_type_tkview).get(Constant.relate_type_tkview) : new ArrayList<String>();

        if (relates == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.possessor_numberCode_error);
        }
        if (isTrue) {
            hqlSb.append(" and numberCode in (" + StringTool.listToSqlString(relates) + ")");
        }else {
            hqlSb.append(" and numberCode not in (" + StringTool.listToSqlString(relates) + ")");
        }
        if (StringTool.isNotNull(numberCode)) {
            hqlSb.append(" and numberCode = '" + numberCode + "'");
        }
        if (StringTool.isNotNull(name)) {
            hqlSb.append(" and nameCn like '%" + name + "%'");
        }

        @SuppressWarnings("unchecked")
        List<Tkview> tkviewList = commonDao.queryByHql(hqlSb.toString());

        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", tkviewList.size());
        mapContent.put("rows", tkviewList);

        return ReturnUtil.returnMap(1, mapContent);
    }

    @Override
    public Map<String, Object> getOverDateByShipment(String shipment) {

        Shipment shipmentObj = (Shipment)commonDao.getObjectByUniqueCode("Shipment", "numberCode", shipment);
        if (shipmentObj == null) {
            return ReturnUtil.returnMap(0, "航期不存在!");
        }
        String overDate = shipmentObj.getFinalSaleDate() == null ? "" : DateUtil.dateToString(shipmentObj.getFinalSaleDate());

        return ReturnUtil.returnMap(1, overDate);
    }

    @Override
    public Map<String, Object> addQuickTkview(String tkviewJsonStr , String shipment) {

        @SuppressWarnings("unchecked")
        List<HashMap<String, Object>> tkviewList = JSON.toObject(tkviewJsonStr, ArrayList.class);

        Shipment shipmentObj = (Shipment)commonDao.getObjectByUniqueCode(Constant.shipment, Constant.numberCode, shipment);
        if (shipmentObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.shipment_null);
        }

        Cruise cruise = (Cruise)commonDao.getObjectByUniqueCode(Constant.cruise, Constant.numberCode, shipmentObj.getCruise());
        if (cruise == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.cruise_null);
        }

        Date date = new Date();
        for (HashMap<String, Object> tkview : tkviewList) {
            String cabin = (String)tkview.get("cabin");
            String tkviewNameCn = (String)tkview.get("tkviewNameCn");
            String tkviewMark = (String)tkview.get("tkviewMark");
            String provider = (String)tkview.get("provider");
            String cost = (String)tkview.get("cost");
            String stock = (String)tkview.get("stock");
            String surpass = (String)tkview.get("surpass");

            String existSql = "select name_cn from tkview where name_cn = '" + tkviewNameCn + "' ";
            existSql += " and shipment = '" + shipmentObj.getNumberCode() + "'";
            existSql += possessorUtil.getRelatesWhereSql(Constant.relate_type_tkview);
            @SuppressWarnings("unchecked")
            List<String> exitstList = commonDao.getSqlQuery(existSql).list();
            if (exitstList.size() >= 1) {
                return ReturnUtil.returnMap(0, "同一航期,单品名称【" + tkviewNameCn + "】不能重复!");
            }

            Cabin cabinObj = (Cabin)commonDao.getObjectByUniqueCode(Constant.cabin,
                Constant.numberCode, cabin);
            if (cabinObj == null) {
                return ReturnUtil.returnMap(0, ConstantMsg.cabin_null);
            }

            Tkview tkviewObj = new Tkview();
            tkviewObj.setAddDate(date);
            tkviewObj.setAlterDate(date);
            tkviewObj.setCabin(cabin);
            tkviewObj.setVolume(cabinObj.getVolume());
            tkviewObj.setCruise(cruise.getNumberCode());
            tkviewObj.setMark(tkviewMark);
            tkviewObj.setNameCn(tkviewNameCn);
            tkviewObj.setNumberCode(StringUtil.get_new_tkview());
            tkviewObj.setShipment(shipment);
            tkviewObj.setShipmentMark(shipmentObj.getMark());
            tkviewObj.setShipmentTravelDate(shipmentObj.getTravelDate());
            commonDao.save(tkviewObj);
            possessorUtil.savePossessorRelates(Constant.relate_type_tkview, tkviewObj.getNumberCode());

            LogWork logWork = new LogWork();
            logWork.setAddDate(new Date());
            logWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
            logWork.setValue(tkviewObj.getNumberCode());
            logWork.setTableName("tkview");
            logWork.setRemark("添加单品【" + tkviewObj.getNumberCode() + "】");
            logWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
            commonDao.save(logWork);
            
            Provider providerObj = (Provider)commonDao.getObjectByUniqueCode(Constant.provider, Constant.numberCode, provider);
            if (providerObj == null) {
                return ReturnUtil.returnMap(0, ConstantMsg.provider_not_exist_error);
            }

            Stock stockObj = new Stock();
            stockObj.setTkview(tkviewObj.getNumberCode());
            stockObj.setNumberCode(UUIDUtil.getUUIDStringFor32());
            stockObj.setProvider(provider);
            stockObj.setOverDate(shipmentObj.getFinalSaleDate());
            stockObj.setCost(new BigDecimal(cost));
            stockObj.setStock(new BigDecimal(stock));
            stockObj.setSurpass(new BigDecimal(surpass));
            stockObj.setUseStock(BigDecimal.ZERO);
            stockObj.setUseSurpass(BigDecimal.ZERO);
            stockObj.setType(1);
            stockObj.setPriority(5);
            stockObj.setDate(shipmentObj.getTravelDate());
            stockObj.setAddDate(date);
            stockObj.setAlterDate(date);
            commonDao.save(stockObj);

            String type = "";
            if (stockObj.getType() == 0) {
                type = "现询";
            }else if (stockObj.getType() == 1) {
                type = "实库";
            }

            LogWork stockLogWork = new LogWork();
            stockLogWork.setAddDate(date);
            stockLogWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
            stockLogWork.setValue(tkviewObj.getNumberCode());
            stockLogWork.setTableName("stock");
            stockLogWork.setRemark("添加库存【供应商:"+ providerObj.getNameCn() +"】【类型:" + type
                                   +"】【总库存:"+ stockObj.getStock() +"】");
            stockLogWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
            commonDao.save(stockLogWork);
        }

        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> queryPtviewTkviewList(int offset , int pagesize , String ptview ,
                                                     boolean flag , String tkview , String nameCn) {

        String hql = "select pt.tkview from PtviewTkview pt where pt.ptview = '" + ptview + "'";
        @SuppressWarnings("unchecked")
        List<String> tkviewNumberCodeList = commonDao.queryByHql(hql);

        String tkviewHql = "from Tkview t where t.numberCode " + (flag ? "" : "not") + " in ("
                           + StringTool.listToSqlString(tkviewNumberCodeList) + ")";
        tkviewHql += possessorUtil.getRelatesWhereHql("tkview");
        if (StringTool.isNotNull(tkview)) {
            tkviewHql += " and t.numberCode = '" + tkview + "'";
        }
        if (StringTool.isNotNull(nameCn)) {
            tkviewHql += " and t.nameCn like '%" + nameCn + "%'";
        }

        Query countQuery = commonDao.getQuery("select count(*) " + tkviewHql);
        Number tempCount = (Number)countQuery.uniqueResult();
        int total = tempCount == null ? 0 : tempCount.intValue();

        Query query = commonDao.getQuery(tkviewHql);
        query.setFirstResult(offset);
        query.setMaxResults(pagesize);

        @SuppressWarnings("unchecked")
        List<Tkview> tkviewList = query.list();

        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", total);
        mapContent.put("rows", tkviewList);

        return ReturnUtil.returnMap(1, mapContent);
    }

    @Override
    public Map<String, Object> getTkviewLog(String tkview) {

        String logSql = "from LogWork where value='" + tkview + "' ORDER BY addDate ";
        Query queryLog = commonDao.getQuery(logSql);
        @SuppressWarnings("unchecked")
        List<LogWork> logWorkList = queryLog.list();

        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", logWorkList.size());
        mapContent.put("rows", logWorkList);

        return ReturnUtil.returnMap(1, mapContent);
    }

    @Override
    public Map<String, Object> getTkviewListByShipment(String shipment) {

        if (StringTool.isEmpty(shipment)) {
            return ReturnUtil.returnMap(0, ConstantMsg.shipment_null);
        }

        String sql = "SELECT t.number_code AS numberCode,t.name_cn AS nameCn FROM tkview t WHERE 1=1 ";
        sql += possessorUtil.getRelatesWhereSql(Constant.relate_type_tkview);
        sql += " AND t.shipment = '" + shipment + "' ";
        SQLQuery query = commonDao.getSqlQuery(sql);
        query.addScalar("numberCode").addScalar("nameCn")
        .setResultTransformer(Transformers.aliasToBean(RTkview.class));
        @SuppressWarnings("unchecked")
        List<RTkview> ptviewList = query.list();

        List<String> tkviewCodeList = new ArrayList<>();
        for (RTkview tkview : ptviewList) {
            tkviewCodeList.add(tkview.getNumberCode());
        }

        String stockSql = "select s.tkview,sum(s.stock) from stock s where tkview in ("+ StringTool.listToSqlString(tkviewCodeList) +")";
        @SuppressWarnings("unchecked")
        List<Object[]> stockList = commonDao.getSqlQuery(stockSql).list();

        for (Object[] stock : stockList) {
            for (RTkview tkview : ptviewList) {
                if (tkview.getNumberCode().equals((String)stock[0])) {
                    tkview.setCountStock(new BigDecimal(stock[1].toString()));
                }
            }
        }

        return ReturnUtil.returnMap(1, ptviewList);
    }

    @Override
    public Map<String, Object> getCabinByCruiseList(String cruise) {
        if (StringTool.isEmpty(cruise)) {
            return ReturnUtil.returnMap(0, ConstantMsg.cruise_null);
        }
        String cabinHql = "from Cabin where cruise = '" + cruise + "' ";
        cabinHql += "and isOpen = '开' ";
        cabinHql += "order by type ";
        @SuppressWarnings("unchecked")
        List<Cabin> cabinList = commonDao.getQuery(cabinHql).list();
        
        String shipmentHql = "from Shipment where cruise = '" + cruise + "' AND travelDate >= NOW() ORDER BY travelDate";
        @SuppressWarnings("unchecked")
        List<Shipment> shipmentList = commonDao.getQuery(shipmentHql).list();

        Map<String, Object> asoidjMap = new HashMap<String, Object>();
        asoidjMap.put("cabinList", cabinList);
        asoidjMap.put("shipmentList", shipmentList);

        return ReturnUtil.returnMap(1, asoidjMap);
    }

    @Override
    public Map<String, Object> addFastlyTkview(String cruise , String shipments ,
                                               String tkviewJsonStr) {
        @SuppressWarnings("unchecked")
        List<HashMap<String, Object>> tkviewList = JSON.toObject(tkviewJsonStr, ArrayList.class);

        for (String shipment : shipments.split(",")) {
            if (StringTool.isEmpty(cruise)) {
                return ReturnUtil.returnMap(0, ConstantMsg.cruise_null);
            }
            if (StringTool.isEmpty(shipment)) {
                return ReturnUtil.returnMap(0, ConstantMsg.shipment_null);
            }

            Shipment shipmentObj = (Shipment)commonDao.getObjectByUniqueCode("Shipment", "numberCode", shipment);
            if (shipmentObj == null) {
                return ReturnUtil.returnMap(0, ConstantMsg.shipment_null);
            }
            String tkviewHql = "from Tkview where shipment = '" + shipment + "'";
            @SuppressWarnings("unchecked")
            List<Tkview> tkviewObjList = commonDao.queryByHql(tkviewHql);
            Date date = new Date();
            for (HashMap<String, Object> tkview : tkviewList) {
                String nameCn = (String)tkview.get("nameCn");
                String mark = (String)tkview.get("mark");
                String remark = (String)tkview.get("remark");
                String cabin = (String)tkview.get("cabin");
                nameCn = "["+ DateUtil.dateToShortString(shipmentObj.getTravelDate()) +"]"+nameCn;
                for (Tkview tkview2 : tkviewObjList) {
                    String oldNameCn = tkview2.getNameCn();
                    if(oldNameCn.equals(nameCn)){
                        return ReturnUtil.returnMap(0, "重复创建单品【"+ nameCn +"】!");
                    }
                }
                Cabin c = (Cabin)commonDao.getObjectByUniqueCode("Cabin", "numberCode", cabin);
                Tkview tkviewObj = new Tkview();
                tkviewObj.setNumberCode(StringUtil.get_new_tkview());
                tkviewObj.setAddDate(date);
                tkviewObj.setAlterDate(date);
                tkviewObj.setCabin(cabin);
                tkviewObj.setVolume(c.getVolume());
                tkviewObj.setCruise(cruise);
                tkviewObj.setShipment(shipment);
                tkviewObj.setMark(mark);
                tkviewObj.setNameCn(nameCn);
                tkviewObj.setRemark(remark);
                tkviewObj.setShipmentMark(shipmentObj.getMark());
                tkviewObj.setShipmentTravelDate(shipmentObj.getTravelDate());
                commonDao.save(tkviewObj);

                possessorUtil.savePossessorRelates(Constant.relate_type_tkview, tkviewObj.getNumberCode());
            
                LogWork logWork = new LogWork();
                logWork.setAddDate(new Date());
                logWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
                logWork.setValue(tkviewObj.getNumberCode());
                logWork.setTableName("tkview");
                logWork.setRemark("快捷新增单品【" + tkviewObj.getNumberCode() + "】");
                logWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
                commonDao.save(logWork);
            }
        }
        

        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> getTkviewListByTkviewType(String tkviewType) {

        TkviewType t = (TkviewType)commonDao.getObjectByUniqueCode("TkviewType", "numberCode", tkviewType);
        if (t == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.tkview_type_not_exist_error);
        }
        
        String hql = "from Tkview where cruise='"+ t.getCruise() +"' and cabin='" + t.getCabin() +"' order by shipmentTravelDate";
        @SuppressWarnings("unchecked")
        List<Tkview> tkviewList = commonDao.queryByHql(hql);

        Map<String, Date> dateMap = new HashMap<String, Date>();
        Calendar cal = Calendar.getInstance();
        for (Tkview tkview : tkviewList) {
            cal.setTime(tkview.getShipmentTravelDate());
            dateMap.put(cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1),
                tkview.getShipmentTravelDate());
        }
        List<Date> dateList = new ArrayList<Date>();
        for (Map.Entry<String, Date> entry : dateMap.entrySet()) {
            dateList.add(entry.getValue()
                );
        }
        Collections.sort(dateList);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-d");
        List<String> dateStringList = new ArrayList<String>();
        for (Date date : dateList) {
            dateStringList.add(simpleDateFormat.format(date));
        }

        Map<String, Object> map = new HashMap<String, Object>();

        map.put("tkviewList", tkviewList);
        map.put("dateList", dateStringList);

        return ReturnUtil.returnMap(1, map);
    }

    @Override
    public Map<String, Object> getTkviewViewData(String cruise , String numberCode , String date) {
        
        String tkviewSql = "SELECT t.number_code AS numberCode, t.name_cn AS nameCn, ";
        tkviewSql += "t.mark AS mark,t.cruise AS cruise,t.cabin AS cabin, ";
        tkviewSql += "t.volume AS volume,t.shipment AS shipment,t.shipment_mark AS shipmentMark, ";
        tkviewSql += "t.shipment_travel_date AS shipmentTravelDate,t.user_name AS userName, ";
        tkviewSql += "t.release_date AS releaseDate,t.remark AS remark, ";
        tkviewSql += "t.add_date AS addDate,t.alter_date AS alterDate,t.iidd AS iidd ";
        tkviewSql += "FROM tkview t WHERE t.cruise = '"+ cruise +"' ";
        tkviewSql += possessorUtil.getRelatesWhereSql("tkview","t");
        tkviewSql += "AND SUBSTR(t.name_cn,13) = ( ";
        tkviewSql += " SELECT SUBSTR(name_cn,13) FROM tkview WHERE number_code = '"+ numberCode +"' ) ";
        tkviewSql += "AND t.shipment_travel_date = '"+ date +"' ";
        SQLQuery query = commonDao.getSqlQuery(tkviewSql);
        query.addScalar("numberCode").addScalar("nameCn").addScalar("mark")
        .addScalar("cruise").addScalar("cabin").addScalar("volume")
        .addScalar("shipment").addScalar("shipmentMark").addScalar("shipmentTravelDate")
        .addScalar("userName").addScalar("releaseDate").addScalar("remark")
        .addScalar("addDate").addScalar("alterDate").addScalar("iidd")
        .setResultTransformer(Transformers.aliasToBean(Tkview.class));
        @SuppressWarnings("unchecked")
        List<Tkview> tkviewList = query.list();
        
        if(tkviewList.size() < 0){
            return ReturnUtil.returnMap(1, null);
        }
        Tkview tkviewObj = tkviewList.get(0);
        Cruise cruiseObj = (Cruise)commonDao.getObjectByUniqueCode("Cruise", "numberCode", tkviewObj.getCruise());
        if(cruiseObj == null){
            return ReturnUtil.returnMap(0, "该单品的邮轮已经不存在了!");
        }
        
        Cabin cabinObj = (Cabin)commonDao.getObjectByUniqueCode("Cabin", "numberCode", tkviewObj.getCabin());
        if(cabinObj == null){
            return ReturnUtil.returnMap(0, "该单品的舱型已经不存在了!");
        }
        
        Shipment shipmentObj = (Shipment)commonDao.getObjectByUniqueCode("Shipment", "numberCode", tkviewObj.getShipment());
        if(shipmentObj == null){
            return ReturnUtil.returnMap(0, "该单品的航期已经不存在了!");
        }

        Airway airwayObj = (Airway)commonDao.getObjectByUniqueCode("Airway", "numberCode", shipmentObj.getAirway());
        if(airwayObj == null){
            return ReturnUtil.returnMap(0, "该单品的航线已经不存在了!");
        }
        
        String stockHql = "from Stock where tkview='" + tkviewObj.getNumberCode() + "'";
        @SuppressWarnings("unchecked")
        List<Stock> stockList = commonDao.queryByHql(stockHql);
        Collections.sort(stockList);

        List<String> providerNumber = new ArrayList<String>();
        for (Stock s : stockList) {
            providerNumber.add(s.getProvider());
        }
        String providerHql = "from Provider p where 1=1 ";
        providerHql += "and p.numberCode in (" + StringTool.listToSqlString(providerNumber) + ") ";
        @SuppressWarnings("unchecked")
        List<Provider> providerList = commonDao.queryByHql(providerHql);

        for (Stock s : stockList) {
            for (Provider p : providerList) {
                if (s.getProvider().equals(p.getNumberCode())) {
                    s.setProviderNameCn(p.getNameCn());
                    s.setProviderMark(p.getMark());
                    break;
                }
            }
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("tkview", tkviewObj);
        map.put("cabin", cabinObj);
        map.put("shipment", shipmentObj);
        map.put("cruise", cruiseObj);
        map.put("airway", airwayObj);
        map.put("stockList", stockList);

        return ReturnUtil.returnMap(1, map);
    }

    @Override
    public Map<String, Object> getTkviewTabList(String cruise) {

        String hql = "from Tkview where 1=1 ";
        hql += possessorUtil.getRelatesWhereHql(Constant.relate_type_tkview);
        hql += "and cruise='" + cruise + "' ";
        //hql += "and shipmentTravelDate >= NOW() ";
        hql += "group by substring(nameCn,13)";
        
        @SuppressWarnings("unchecked")
        List<Tkview> tkviews = commonDao.queryByHql(hql);

        return ReturnUtil.returnMap(1, tkviews);
    }

    @Override
    public Map<String, Object> getTkviewListByTkviewGroup(String cruise , String tkview) {

        String tkviewSql = "SELECT t.number_code AS numberCode, t.name_cn AS nameCn, ";
        tkviewSql += "t.mark AS mark,t.cruise AS cruise,t.cabin AS cabin, ";
        tkviewSql += "t.volume AS volume,t.shipment AS shipment,t.shipment_mark AS shipmentMark, ";
        tkviewSql += "t.shipment_travel_date AS shipmentTravelDate,t.user_name AS userName, ";
        tkviewSql += "t.release_date AS releaseDate,t.remark AS remark, ";
        tkviewSql += "t.add_date AS addDate,t.alter_date AS alterDate,t.iidd AS iidd ";
        tkviewSql += "FROM tkview t WHERE t.cruise = '"+ cruise +"' ";
        tkviewSql += possessorUtil.getRelatesWhereSql("tkview", "t");
        tkviewSql += " AND SUBSTR(t.name_cn,13) = ( ";
        tkviewSql += "SELECT SUBSTR(name_cn,13) FROM tkview WHERE number_code = '"+ tkview +"') ";
        SQLQuery query = commonDao.getSqlQuery(tkviewSql);
        query.addScalar("numberCode").addScalar("nameCn").addScalar("mark")
        .addScalar("cruise").addScalar("cabin").addScalar("volume")
        .addScalar("shipment").addScalar("shipmentMark").addScalar("shipmentTravelDate")
        .addScalar("userName").addScalar("releaseDate").addScalar("remark")
        .addScalar("addDate").addScalar("alterDate").addScalar("iidd")
        .setResultTransformer(Transformers.aliasToBean(Tkview.class));
        @SuppressWarnings("unchecked")
        List<Tkview> tkviewList = query.list();
        
        String mothSql = "SELECT DATE_FORMAT(t.shipment_travel_date,'%Y-%m') FROM ";
        mothSql += "tkview t WHERE t.cruise = '"+ cruise +"' ";
        mothSql += "AND SUBSTR(t.name_cn,13) = ( ";
        mothSql += " SELECT SUBSTR(name_cn,13) FROM tkview WHERE number_code = '"+ tkview +"') ";
        mothSql += "GROUP BY DATE_FORMAT(t.shipment_travel_date,'%Y-%m') ";
        mothSql += "ORDER BY t.shipment_travel_date ";
        @SuppressWarnings("unchecked")
        List<Object> mothList = commonDao.getSqlQuery(mothSql).list();
       
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("tkviewList", tkviewList);
        map.put("dateList", mothList);

        return ReturnUtil.returnMap(1, map);
    }

    @Override
    public Map<String, Object> getTkviewViewStockList(int offset , int pagesize ,String numberCode) {
        Tkview tkviewObj = (Tkview)commonDao.getObjectByUniqueCode("Tkview", "numberCode", numberCode);
        if(tkviewObj == null){
            return ReturnUtil.returnMap(0, ConstantMsg.tkview_not_exist_error);
        }
        
        int index = tkviewObj.getNameCn().indexOf("]")+1;
        String nameCn = tkviewObj.getNameCn().substring(index);
        
        String tkviewHql = "from Tkview where 1=1 ";
        tkviewHql += possessorUtil.getRelatesWhereHql(Constant.relate_type_tkview);
        tkviewHql += "and cruise = '" + tkviewObj.getCruise()+ "' ";
        tkviewHql += "and nameCn like '%" + nameCn + "%' ";
        @SuppressWarnings("unchecked")
        List<Tkview> tkviewList = commonDao.queryByHql(tkviewHql);
        List<String> numberList = new ArrayList<>();
        for(Tkview t : tkviewList){
            numberList.add(t.getNumberCode());
        }
        
        String hql = "from Stock where tkview in (" + StringTool.listToSqlString(numberList) + ") ";
        Query countQuery = commonDao.getQuery("select count(*) " + hql.toString());
        Number tempCount = (Number)countQuery.uniqueResult();
        int total = tempCount == null ? 0 : tempCount.intValue();

        Query query = commonDao.getQuery(hql.toString());
        query.setFirstResult(offset);
        query.setMaxResults(pagesize);
        @SuppressWarnings("unchecked")
        List<Stock> stockList  = query.list();
        Collections.sort(stockList);
        
        List<String> providerNumber = new ArrayList<String>();
        for (Stock s : stockList) {
            providerNumber.add(s.getProvider());
        }
        String providerHql = "from Provider p where 1=1 ";
        providerHql += "and p.numberCode in (" + StringTool.listToSqlString(providerNumber) + ") ";
        @SuppressWarnings("unchecked")
        List<Provider> providerList = commonDao.queryByHql(providerHql);

        for (Stock s : stockList) {
            for (Provider p : providerList) {
                if (s.getProvider().equals(p.getNumberCode())) {
                    s.setProviderNameCn(p.getNameCn());
                    s.setProviderMark(p.getMark());
                    break;
                }
            }
        }
        
        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", total);
        mapContent.put("rows", stockList);
            
        return ReturnUtil.returnMap(1, mapContent);
    }

    @Override
    public Map<String, Object> queryTkviewGroupCruise() {
        
        String sql = "SELECT t.cruise,c.name_cn,COUNT(t.iidd) ";
        sql += "FROM tkview t ";
        sql += "LEFT JOIN cruise c ON t.cruise = c.number_code ";
        sql += "WHERE t.shipment_travel_date >= NOW() ";
        sql += "GROUP BY t.cruise ";
        sql += "ORDER BY c.sort_num DESC ";
        @SuppressWarnings("unchecked")
        List<Object[]> crusieList = commonDao.getSqlQuery(sql).list();

        return ReturnUtil.returnMap(1, crusieList);
    }

    @Override 
    public Map<String, Object> queryTkviewGroupShipmentByCruise(String cruise) {
        StringBuffer tkviewHql = new StringBuffer("from Tkview t where 1=1 ");
        tkviewHql.append("and t.cruise = '" + cruise + "' ");
        tkviewHql.append("and t.shipmentTravelDate >= NOW() ");
        tkviewHql.append(possessorUtil.getRelatesWhereHql(Constant.relate_type_tkview));
        tkviewHql.append("group by t.shipment ");
        tkviewHql.append("order by t.shipmentTravelDate ");

        @SuppressWarnings("unchecked")
        List<Tkview> tkviewList = commonDao.queryByHql(tkviewHql.toString());

        /* 所属机构 */
        String possessor = MyRequestUtil.getPossessor();
        Possessor possessorObj = new Possessor();
        if (StringTool.isNotNull(possessor)) {
            possessorObj = (Possessor)commonDao.getObjectByUniqueCode(Constant.possessor,
                Constant.numberCode, possessor);
        }

        List<String> cruiseNumber = new ArrayList<String>();
        for (Tkview tkview : tkviewList) {
            tkview.setPossessorNameCn(possessorObj.getNameCn());
            cruiseNumber.add(tkview.getCruise());
        }

        String cruiseSql = "SELECT number_code,name_cn FROM cruise WHERE number_code IN ("+ StringTool.listToSqlString(cruiseNumber) +") ";
        @SuppressWarnings("unchecked")
        List<Object[]> cruiseList = commonDao.getSqlQuery(cruiseSql).list();

        for (Tkview tkview : tkviewList) {
            for (Object[] c : cruiseList) {
                if (tkview.getCruise().equals(c[0])) {
                    tkview.setCruiseNameCn((String)c[1]);
                    break;
                }
            }
        }

        return ReturnUtil.returnMap(1, tkviewList);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> editOperationData(String cruise , String tkviewNumber , String mode) {
        String userName = MyRequestUtil.getUsername();
        String str = "";
        
        Tkview tkviewObj = (Tkview)commonDao.getObjectByUniqueCode("Tkview", "numberCode", tkviewNumber);
        if(tkviewObj == null){
            return ReturnUtil.returnMap(1, str);
        }
        
        if(StringTool.isEmpty(cruise) || "noValue".equals(cruise)){
            cruise = tkviewObj.getCruise();
        }
        
        Cruise cruiseObj = (Cruise)commonDao.getObjectByUniqueCode("Cruise", "numberCode", cruise);
        String countHql = "from Tkview t where t.cruise = '"+ cruise +"' ";
        countHql += "and t.shipment = '"+ tkviewObj.getShipment() +"' ";
        List<Tkview> list = commonDao.queryByHql(countHql);
        
        if(list.size() < 1){
            str += "【"+ cruiseObj.getNameCn() +"】邮轮";
            str += "【"+DateUtil.dateToShortString(tkviewObj.getShipmentTravelDate())  + "】航期 ";
            str += "没有任何单品信息!";
        }else{
            String user_name = list.get(0).getUserName();
            if(StringTool.isNotNull(user_name) && !user_name.equals(userName)){
                str += "【"+ cruiseObj.getNameCn() +"】邮轮";
                str += "【"+DateUtil.dateToShortString(tkviewObj.getShipmentTravelDate())  + "】航期下的";
                str += "单品被【"+ user_name +"】控制住了!";
            }else if(StringTool.isNotNull(user_name) && user_name.equals(userName)){
                /*1、释放时间未过*/
                String beforeSql = countHql ;
                beforeSql += "and t.userName = '"+ userName +"' ";
                beforeSql += "and t.releaseDate > NOW() ";
                List<Tkview> beforeList = commonDao.queryByHql(beforeSql);
                
                /*2、释放时间已过，但是还没清空时*/
                String afterSql = countHql;
                afterSql += "and t.userName = '"+ userName +"' ";
                afterSql += "and t.releaseDate <= NOW() ";
                List<Tkview> afterList = commonDao.queryByHql(afterSql);
                
                if(beforeList.size() > 0){
                    String updateBeforeSql = "UPDATE tkview t SET ";
                    updateBeforeSql += "t.release_date = DATE_ADD(t.release_date,INTERVAL 5 MINUTE), ";
                    updateBeforeSql += "t.alter_date = NOW() ";
                    updateBeforeSql += "WHERE t.cruise = '"+ cruise +"' ";
                    updateBeforeSql += "AND t.shipment = '"+ tkviewObj.getShipment() +"' ";
                    commonDao.getSqlQuery(updateBeforeSql).executeUpdate();
                }else if(afterList.size() > 0){
                    String updateAfterSql = "UPDATE tkview t SET ";
                    updateAfterSql += "t.release_date = DATE_ADD(NOW(),INTERVAL 10 MINUTE), ";
                    updateAfterSql += "t.alter_date = NOW() ";
                    updateAfterSql += "WHERE t.cruise = '"+ cruise +"' ";
                    updateAfterSql += "AND t.shipment = '"+ tkviewObj.getShipment() +"' ";
                    commonDao.getSqlQuery(updateAfterSql).executeUpdate();
                }
                
            }else if(StringTool.isEmpty(user_name)){
                /*3、释放时间为空时*/
                String updateSql = "UPDATE tkview t SET ";
                updateSql += "t.user_name = '"+ userName +"', ";
                updateSql += "t.release_date = DATE_ADD(NOW(),INTERVAL 10 MINUTE), ";
                updateSql += "t.alter_date = NOW() ";
                updateSql += "WHERE t.cruise = '"+ cruise +"' ";
                updateSql += "AND t.shipment = '"+ tkviewObj.getShipment() +"' ";
                commonDao.getSqlQuery(updateSql).executeUpdate();
            }
        }
        
        String updSql = "UPDATE tkview t SET ";
        updSql += "t.user_name = '', ";
        updSql += "t.release_date = NULL, ";
        updSql += "t.alter_date = NOW() ";
        updSql += "WHERE t.user_name = '"+ MyRequestUtil.getUsername() +"' ";
        updSql += "AND t.cruise = '"+ cruise +"' ";
        updSql += "AND t.shipment <> '"+ tkviewObj.getShipment() +"' ";
        commonDao.getSqlQuery(updSql).executeUpdate();
        
        return ReturnUtil.returnMap(1, str);
    }

    @Override
    public Map<String, Object> editTkviewRelease(String mode) {
        String updateSql = "UPDATE tkview t SET ";
        updateSql += "t.user_name = '', ";
        updateSql += "t.release_date = NULL, ";
        updateSql += "t.alter_date = NOW() ";
        updateSql += "WHERE t.user_name = '"+ MyRequestUtil.getUsername() +"' ";
        commonDao.getSqlQuery(updateSql).executeUpdate();

        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> editStockToSku(String cruise) {
    	
        //自动更新计划任务
        PlanWork planWork=new PlanWork();
        
        planWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
        planWork.setAddDate(new Date());
        planWork.setContent(cruise);
        planWork.setUsername("system");
        planWork.setWorkType(Constant.PLAN_TYPE_UPDATE_CRUISE);
        
        commonDao.save(planWork);
    	
    	return ReturnUtil.returnMap(1, null);
    }
    
    public Map<String, Object> addStockObjToSku(String cruise , String sku , 
                                                List<String> tkviewTypeNumber , 
                                                List<PriceStrategy> priceList,
                                                List<String> dateList) {
        
        TaobaoSkuInfo taobaoSkuInfoObj = (TaobaoSkuInfo)commonDao.getObjectByUniqueCode("TaobaoSkuInfo", "numberCode", sku);
        if(taobaoSkuInfoObj == null){
            return ReturnUtil.returnMap(0, "SKU套餐不存在!");
        }
        
        TaobaoBaseInfo baseInfo = (TaobaoBaseInfo)commonDao.getObjectByUniqueCode("TaobaoBaseInfo", "taobaoTravelItem", sku);
        
        //获取当前淘宝SKU关联的单品种类
        String typeHql = "from TkviewType t where t.numberCode in ("+ StringTool.listToSqlString(tkviewTypeNumber) +") ";
        typeHql += "and t.cruise = '"+ cruise +"'";
        @SuppressWarnings("unchecked")
        List<TkviewType> queryTypeList = commonDao.queryByHql(typeHql);
        List<String> cabinNum = new ArrayList<>();
        for(TkviewType type : queryTypeList){
            cabinNum.add(type.getCabin());
        }
        
        //获取当前淘宝SKU管理的单品信息
        String tkviewHql = "from Tkview t where t.cruise = '" + cruise + "' ";
        tkviewHql += "and t.cabin in ("+ StringTool.listToSqlString(cabinNum) +") ";
        if(dateList != null){
            tkviewHql += "and t.shipmentTravelDate not in ("+ StringTool.listToSqlString(dateList) +")";
        }
        @SuppressWarnings("unchecked")
        List<Tkview> tkviewList = commonDao.queryByHql(tkviewHql);
        List<String> tkviewNum = new ArrayList<String>();
        for(Tkview t : tkviewList){
            tkviewNum.add(t.getNumberCode());
        }
        
        //获取当前淘宝SKU关联的单品库存信息
        String hql = "from Stock s where 1=1 ";
        hql += "and s.tkview in (" + StringTool.listToSqlString(tkviewNum) + ") ";
        hql += "and s.isOnline = 0 ";
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
            
            for(Tkview tk : tkviewList){
                List<Stock> resultStockList = tk.getStocks();
                Collections.sort(resultStockList);
                
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
                
                Date addDate = new Date();
                TaobaoSkuInfoDetail taobaoSkuInfoDetailObj = new TaobaoSkuInfoDetail();
                taobaoSkuInfoDetailObj.setTaobaoSkuInfo(sku);
                taobaoSkuInfoDetailObj.setNumberCode(StringUtil.get_new_taobao_price_out_num());
                taobaoSkuInfoDetailObj.setOutId(stockObj.getTkview());
                taobaoSkuInfoDetailObj.setAirway(shipObj.getAirway());
                taobaoSkuInfoDetailObj.setDate(shipObj.getTravelDate());
                taobaoSkuInfoDetailObj.setPriceType(1);  //成人价
                taobaoSkuInfoDetailObj.setPrice(price);
                if(stockObj.getType() == 1){ //实库
                    taobaoSkuInfoDetailObj.setStock(stockObj.getStock().multiply(cabinObj.getMaxVolume()).intValue());
                }
                taobaoSkuInfoDetailObj.setIsLock(0);
                taobaoSkuInfoDetailObj.setIsEdit(0);
                taobaoSkuInfoDetailObj.setFlag(0);
                taobaoSkuInfoDetailObj.setIsUpdate(1);
                taobaoSkuInfoDetailObj.setIsRelation(1);
                taobaoSkuInfoDetailObj.setOutStock(stockObj.getNumberCode());
                taobaoSkuInfoDetailObj.setAddDate(addDate);
                taobaoSkuInfoDetailObj.setAlterDate(addDate);
                commonDao.save(taobaoSkuInfoDetailObj);
                
                LogWork logWork = new LogWork();
                logWork.setAddDate(addDate);
                logWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
                logWork.setValue(baseInfo.getNumberCode());
                logWork.setRemark("添加套餐【"+taobaoSkuInfoObj.getPackageName()+"】里日期为【"+ DateUtil.dateToShortString(shipObj.getTravelDate()) +"】的库存");
                logWork.setTableName("skuTable");
                logWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
                commonDao.save(logWork);
            }
           
        }
            
        return ReturnUtil.returnMap(1, null);
    }
    
    /**
     * 更新库存到淘宝SKU
     * 
     * @param tkview
     *            单品编号
     * @author : 熊玲
     */
    public Map<String, Object> editStockObjToSku(String cruise , String sku , Date travelDate , 
                                                 List<String> tkviewTypeNumber , 
                                                 List<PriceStrategy> priceList) {
        
        //获取当前淘宝SKU关联的单品种类
        String typeHql = "from TkviewType t where t.numberCode in ("+ StringTool.listToSqlString(tkviewTypeNumber) +") ";
        typeHql += "and t.cruise = '"+ cruise +"'";
        @SuppressWarnings("unchecked")
        List<TkviewType> queryTypeList = commonDao.queryByHql(typeHql);
        List<String> cabinNum = new ArrayList<>();
        for(TkviewType type : queryTypeList){
            cabinNum.add(type.getCabin());
        }
        
        //获取当前淘宝SKU管理的单品信息
        String tkviewHql = "from Tkview t where t.cruise = '" + cruise + "' ";
        tkviewHql += "and t.cabin in ("+ StringTool.listToSqlString(cabinNum) +") ";
        tkviewHql += "and t.shipmentTravelDate = '"+ DateUtil.dateToShortString(travelDate) +"' ";
        @SuppressWarnings("unchecked")
        List<Tkview> tkviewList = commonDao.queryByHql(tkviewHql);
        List<String> tkviewNum = new ArrayList<String>();
        for(Tkview t : tkviewList){
            tkviewNum.add(t.getNumberCode());
        }
        
        //获取当前淘宝SKU关联的单品库存信息
        String hql = "from Stock s where 1=1 ";
        hql += "and s.tkview in (" + StringTool.listToSqlString(tkviewNum) + ") ";
        hql += "and s.isOnline = 0 ";
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
            updateSkuStockSql += "WHERE d.taobao_sku_info = '"+ sku +"' ";
            updateSkuStockSql += "AND d.date = '"+ DateUtil.dateToShortString(travelDate) +"' ";
            updateSkuStockSql += "AND d.is_lock = 0 "; //价格不锁定
            commonDao.getSqlQuery(updateSkuStockSql).executeUpdate();
        }
        
        return ReturnUtil.returnMap(1, null);
        
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> deleteTkviewByCabin(String cabin , String shipment , String provider) {
        if (StringTool.isEmpty(cabin) && StringTool.isEmpty(shipment) && StringTool.isEmpty(provider)) {
            return ReturnUtil.returnMap(0, "请选择舱型、航期或者供应商!");
        }
        
        List<String> stockList = null;
        if(StringTool.isNotNull(provider)){
            String stoclSql = "SELECT s.provider FROM stock s ";
            stoclSql += "WHERE s.tkview IN ( ";
            stoclSql += " SELECT * FROM tkview t WHERE 1=1 ";
            stoclSql += possessorUtil.getRelatesWhereSql("tkview","t");
            if(StringTool.isNotNull(cabin)){
                stoclSql += "AND t.cabin IN  ("+ StringTool.StrToSqlString(cabin) +") ";
            }
            if(StringTool.isNotNull(shipment)){
                stoclSql += " AND t.shipment IN  ("+ StringTool.StrToSqlString(shipment) +") ) ";
            }
            stoclSql += "AND s.provider IN ("+ StringTool.StrToSqlString(provider) +") ";
            stockList = commonDao.getSqlQuery(stoclSql).list();
        }
        
        String hql = "from Tkview t where 1=1 ";
        hql += possessorUtil.getRelatesWhereHql("tkview");
        if(StringTool.isNotNull(cabin)){
            hql += "and t.cabin in ("+ StringTool.StrToSqlString(cabin) +") ";
        }
        if(StringTool.isNotNull(shipment)){
            hql += "and t.shipment in ("+ StringTool.StrToSqlString(shipment) +") ";
        }
        if(stockList != null && stockList.size() > 0){
            hql += "and t.numberCode in ("+ StringTool.listToSqlString(stockList) +") ";
        }
        List<Tkview> tkviewList = commonDao.queryByHql(hql);
        if(tkviewList.size() < 1){
            return ReturnUtil.returnMap(0, "没有相关联的的单品信息!");
        }
        
        List<String> strList = new ArrayList<>();
        for(Tkview tk : tkviewList){
            strList.add(tk.getNumberCode());
        }
        String sql = "delete from tkview where number_code in ("+ StringTool.listToSqlString(strList) + ") ";
        commonDao.getSqlQuery(sql).executeUpdate();

        String stockSql = "delete from stock where tkview in ("+ StringTool.listToSqlString(strList) + ")";
        commonDao.getSqlQuery(stockSql).executeUpdate();
        
        
        for(String tkview:strList){
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
    public Map<String, Object> editTkviewRemark(String numbercode , String remark) {
        if(StringTool.isEmpty(numbercode)){
            return ReturnUtil.returnMap(0, "请选择单品信息!");
        }
        if(StringTool.isEmpty(remark)){
            return ReturnUtil.returnMap(0, "请填写单品备注!");
        }
        
        String hql = "from Tkview t where t.numberCode in ("+ StringTool.StrToSqlString(numbercode) +") ";
        @SuppressWarnings("unchecked")
        List<Tkview> tkviewList = commonDao.queryByHql(hql);
        
        for(Tkview tk : tkviewList){
            tk.setRemark(remark);
            tk.setAlterDate(new Date());
            commonDao.update(tk);
        }
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> getTkviewListByShipmentAndCabin(String cruise , String shipment , String cabin) {
        if(StringTool.isEmpty(shipment) && StringTool.isEmpty(cabin)){
            return ReturnUtil.returnMap(0, "请选择航期或者舱型!");
        }
        
        String hql = "from Tkview t where 1=1 ";
        hql += "and t.cruise = '"+ cruise +"' ";
        hql += possessorUtil.getRelatesWhereHql("tkview");
        hql += "and t.shipment in ("+ StringTool.StrToSqlString(shipment) +") ";
        hql += "and t.cabin in ("+ StringTool.StrToSqlString(cabin) +") ";
        hql += "order by t.shipment,t.cabin ";
        @SuppressWarnings("unchecked")
        List<Tkview> tkviewList = commonDao.queryByHql(hql);
        
        String cabinHql = "from Cabin c where c.cruise = '"+ cruise +"' ";
        @SuppressWarnings("unchecked")
        List<Cabin> cabinList = commonDao.queryByHql(cabinHql);
        
        for(Tkview tk : tkviewList){
            for(Cabin cabinObj : cabinList){
                if(tk.getCabin().equals(cabinObj.getNumberCode())){
                    tk.setCabinNameCn(cabinObj.getNameCn());
                    break;
                }
            }
        }
        
        return ReturnUtil.returnMap(1, tkviewList);
    }

	@Override
	public void autoUpdateByTkviewOper(String tkview) {
	
		Tkview tkviewObj = (Tkview) commonDao.getObjectByUniqueCode("Tkview", "numberCode", tkview);
		
		if(tkviewObj==null){
			return;
		}
		
		TkviewType tkviewType = (TkviewType) commonDao.queryUniqueByHql("from TkviewType where cabin='"+tkviewObj.getCabin()+"'");
    	
    	String sql = "select sku from sku_tkview_type where tkview_type='"+tkviewType.getNumberCode()+"'";
    	@SuppressWarnings("unchecked")
        List<String> skuList = commonDao.getSqlQuery(sql).list();
    	
    	String hql = "from TaobaoSkuInfo where numberCode in("+StringTool.listToSqlString(skuList)+")";
    	@SuppressWarnings("unchecked")
        List<TaobaoSkuInfo> skuInfos = commonDao.queryByHql(hql);
    	
    	hql = "from Stock where tkview ='"+tkview+"' and stock>0 and date >=now()";
		@SuppressWarnings("unchecked")
        List<Stock> stocks = commonDao.queryByHql(hql);
		if(stocks.isEmpty()){
	    	for(TaobaoSkuInfo skuInfo:skuInfos){
	       		hql="from TaobaoSkuInfoDetail where taobaoSkuInfo ='"+skuInfo.getNumberCode()+"' and  date='"+DateUtil.dateToShortString(tkviewObj.getShipmentTravelDate())+"'";
	    		TaobaoSkuInfoDetail detail=(TaobaoSkuInfoDetail) commonDao.queryUniqueByHql(hql);
	    		if(detail!=null){
	    			commonDao.delete(detail);
	    		}
	    	}
	    	return;
		}
		Collections.sort(stocks);
		Stock stock=stocks.get(0);
		
		
		sql="select number_code,channel from taobao_travel_item where number_code in("+StringTool.listToSqlString(skuList)+")";
    	@SuppressWarnings("unchecked")
		List<Object[]> channelList=commonDao.getSqlQuery(sql).list();
    	Map<String, String> itemMap=new HashMap<String, String>();
    	for(Object[] objects:channelList){
    		itemMap.put(objects[0].toString(), objects[1].toString());
    	}
    	
    	sql="select number_code from taobao_travel_item where number_code in("+StringTool.listToSqlString(skuList)+") and channel_name_cn like '%美团%'";
    	@SuppressWarnings("unchecked")
        List<String> noUpdateItemList=commonDao.getSqlQuery(sql).list();
    	
		Cabin cabin=(Cabin) commonDao.getObjectByUniqueCode("Cabin", "numberCode", tkviewObj.getCabin());
		if(cabin==null){
			return;
		}
    	
    	for(TaobaoSkuInfo skuInfo:skuInfos){
    		
     		boolean updateFlag=true;
    		if(noUpdateItemList.contains(skuInfo.getTaobaoTravelItem())){
    			updateFlag=false;
    		}
    		
    		hql="from TaobaoSkuInfoDetail where taobaoSkuInfo ='"+skuInfo.getNumberCode()+"' and  date='"+DateUtil.dateToShortString(tkviewObj.getShipmentTravelDate())+"'";
    		TaobaoSkuInfoDetail detail=(TaobaoSkuInfoDetail) commonDao.queryUniqueByHql(hql);
    		
    		if(detail==null){
    			detail=new TaobaoSkuInfoDetail();
				detail.setAddDate(new Date());
				detail.setAlterDate(new Date());
				detail.setDate(tkviewObj.getShipmentTravelDate());
				detail.setIsLock(0);
				detail.setFlag(1);
				if(updateFlag){
					detail.setIsUpdate(1);
				}
				detail.setNumberCode(StringUtil.get_new_taobao_price_out_num());
				detail.setPrice(PriceUtil.getSalePrice(itemMap.get(skuInfo.getTaobaoTravelItem()),stock.getCost()));
				detail.setStock(stock.getStock().intValue()*cabin.getMaxVolume().intValue());
				detail.setPriceType(1);
				detail.setTaobaoSkuInfo(skuInfo.getNumberCode());
				commonDao.save(detail);
    		}else{
    			if(detail.getIsLock()!=1){
    				
    				detail.setStock(stock.getStock().intValue()*cabin.getMaxVolume().intValue());
    				detail.setPrice(PriceUtil.getSalePrice(itemMap.get(skuInfo.getTaobaoTravelItem()),stock.getCost()));
    				detail.setPriceType(1);
    				detail.setOutStock(stock.getNumberCode());
    				if(!detail.getPrice().equals(stock.getCost())||detail.getStock()!=stock.getStock().intValue()){
    					detail.setFlag(1);
    					if(updateFlag){
    						detail.setIsUpdate(1);
    					}
    				}
    			}
    		}
			
    	}
    	
    	SimpleDateFormat format=new SimpleDateFormat("yyMMdd");
    	
    	
    	String type="";
    	if(tkviewObj.getNameCn().indexOf("内舱双人间")!=-1){
			type="N2";
		}else if(tkviewObj.getNameCn().indexOf("内舱三人间")!=-1){
			type="N3";
		}else if(tkviewObj.getNameCn().indexOf("内舱四人间")!=-1){
			type="N4";
		}else if(tkviewObj.getNameCn().indexOf("海景双人间")!=-1){
			type="H2";
		}else if(tkviewObj.getNameCn().indexOf("海景三人间")!=-1){
			type="H3";
		}else if(tkviewObj.getNameCn().indexOf("海景四人间")!=-1){
			type="H4";
		}else if(tkviewObj.getNameCn().indexOf("阳台双人间")!=-1){
			type="Y2";
		}else if(tkviewObj.getNameCn().indexOf("阳台三人间")!=-1){
			type="Y3";
		}else if(tkviewObj.getNameCn().indexOf("阳台四人间")!=-1){
			type="Y4";
		}else if(tkviewObj.getNameCn().indexOf("套房双人间")!=-1){
			type="T2";
		}else if(tkviewObj.getNameCn().indexOf("套房三人间")!=-1){
			type="T3";
		}else if(tkviewObj.getNameCn().indexOf("套房四人间")!=-1){
			type="T4";
		}else{
			return;
		}
    	
    	String numberCode=tkviewObj.getCruise()+format.format(tkviewObj.getShipmentTravelDate());
    	ShipmentData shipmentData=QueryResourcesSvcImp.DATA_MAP.get(numberCode);
    	if(shipmentData==null){
    		shipmentData=new ShipmentData();
    		shipmentData.setNumberCode(numberCode);
    	}
    	
    	int cost=stock.getCost().intValue()/100;
    	
		if(type.equals("N2")){
			if(shipmentData.getN2()==0||cost<shipmentData.getN2()){
				shipmentData.setN2(cost);
			}
		}else if(type.equals("N3")){
			if(shipmentData.getN3()==0||cost<shipmentData.getN3()){
				shipmentData.setN3(cost);
			}
		}else if(type.equals("N4")){
			if(shipmentData.getN4()==0||cost<shipmentData.getN4()){
				shipmentData.setN4(cost);
			}
		}else if(type.equals("H2")){
			if(shipmentData.getH2()==0||cost<shipmentData.getH2()){
				shipmentData.setH2(cost);
			}
		}else if(type.equals("H3")){
			if(shipmentData.getH3()==0||cost<shipmentData.getH3()){
				shipmentData.setH3(cost);
			}
		}else if(type.equals("H4")){
			if(shipmentData.getH4()==0||cost<shipmentData.getH4()){
				shipmentData.setH4(cost);
			}
		}else if(type.equals("Y2")){
			if(shipmentData.getY2()==0||cost<shipmentData.getY2()){
				shipmentData.setY2(cost);
			}
		}else if(type.equals("Y3")){
			if(shipmentData.getY3()==0||cost<shipmentData.getY3()){
				shipmentData.setY3(cost);
			}
		}else if(type.equals("Y4")){
			if(shipmentData.getY4()==0||cost<shipmentData.getY4()){
				shipmentData.setY4(cost);
			}
		}else if(type.equals("T2")){
			if(shipmentData.getT2()==0||cost<shipmentData.getT2()){
				shipmentData.setT2(cost);
			}
		}else if(type.equals("T3")){
			if(shipmentData.getT3()==0||cost<shipmentData.getT3()){
				shipmentData.setT3(cost);
			}
		}else if(type.equals("T4")){
			if(shipmentData.getT4()==0||cost<shipmentData.getT4()){
				shipmentData.setT4(cost);
			}
		}
    	
	}

	@Override
	public void autoUpdateByCruiseOper(String cruise) {
    	
    	
    	String sql="select taobao_travel_item from taobao_base_info where cruise='"+cruise+"'";
    	
    	@SuppressWarnings("unchecked")
        List<String> itemList=commonDao.getSqlQuery(sql).list();
    	
    	String hql="from TaobaoSkuInfo where taobaoTravelItem in("+StringTool.listToSqlString(itemList)+")";
    	
    	@SuppressWarnings("unchecked")
        List<TaobaoSkuInfo> skuInfos=commonDao.queryByHql(hql);
    	
    	sql="select number_code,channel from taobao_travel_item where number_code in("+StringTool.listToSqlString(itemList)+")";
    	@SuppressWarnings("unchecked")
		List<Object[]> channelList=commonDao.getSqlQuery(sql).list();
    	Map<String, String> itemMap=new HashMap<String, String>();
    	for(Object[] objects:channelList){
    		itemMap.put(objects[0].toString(), objects[1].toString());
    	}
    	
    	sql="select number_code from taobao_travel_item where  channel_name_cn like '%美团%'";
    	@SuppressWarnings("unchecked")
        List<String> noUpdateItemList=commonDao.getSqlQuery(sql).list();
    	
    	for(TaobaoSkuInfo skuInfo:skuInfos){
    		
    		boolean updateFlag=true;
    		if(noUpdateItemList.contains(skuInfo.getTaobaoTravelItem())){
    			updateFlag=false;
    		}
    		
    		hql="from TaobaoSkuInfoDetail where taobaoSkuInfo ='"+skuInfo.getNumberCode()+"' and  date >=now()";
    	  	@SuppressWarnings("unchecked")
            List<TaobaoSkuInfoDetail> details=commonDao.queryByHql(hql);
    	  	
    		sql="select cabin from tkview_type where number_code in (select tkview_type from sku_tkview_type where sku='"+skuInfo.getNumberCode()+"')";
    		@SuppressWarnings("unchecked")
            List<String> cabins=commonDao.getSqlQuery(sql).list();
    		if(cabins.isEmpty()){
    			continue;
    		}
    		sql="select number_code from tkview where cruise='"+cruise+"' and cabin in("+StringTool.listToSqlString(cabins)+")";
    		@SuppressWarnings("unchecked")
            List<String> numberCodes=commonDao.getSqlQuery(sql).list();
    		if(numberCodes.isEmpty()){
    			continue;
    		}
    		hql="from Stock where tkview in("+StringTool.listToSqlString(numberCodes)+") and stock>0 and date >=now()";
    		@SuppressWarnings("unchecked")
            List<Stock> stocks=commonDao.queryByHql(hql);
    		
    		Map<String, List<Stock>> map=new HashMap<>();
    		for(Stock stock:stocks){
    			String date=DateUtil.dateToShortString(stock.getDate());
    			if(map.get(date)==null){
    				List<Stock> list=new ArrayList<>();
    				list.add(stock);
    				map.put(date, list);
    			}else{
    				List<Stock> list=map.get(date);
    				list.add(stock);
    				map.put(date, list);
    			}
    		}
    		
    	
    		
    		
    		for (Map.Entry<String, List<Stock>> entry : map.entrySet()) {  
    			boolean flag=false;
    			List<Stock> list= entry.getValue();
    			 Collections.sort(list);
    			Stock stock=list.get(0);
    			
    			Tkview tkview=(Tkview) commonDao.getObjectByUniqueCode("Tkview", "numberCode", stock.getTkview());
    			Cabin cabin=(Cabin) commonDao.getObjectByUniqueCode("Cabin", "numberCode", tkview.getCabin());
    			
    			for(TaobaoSkuInfoDetail detail:details){
    				String date=DateUtil.dateToShortString(detail.getDate());
    				if(date.equals(entry.getKey())){
    					flag=true;
    					if(detail.getIsLock()!=1){
        					detail.setStock(stock.getStock().intValue()*cabin.getMaxVolume().intValue());
        					detail.setPrice(PriceUtil.getSalePrice(itemMap.get(skuInfo.getTaobaoTravelItem()),stock.getCost()));
        					detail.setPriceType(1);
        					detail.setOutStock(stock.getNumberCode());
        					if(!detail.getPrice().equals(stock.getCost())||detail.getStock()!=stock.getStock().intValue()){
        						detail.setFlag(1);
        						if(updateFlag){
        							detail.setIsUpdate(1);
        						}
        					}
        				}
						detail.setUpdateFlag(1);
    				}
    			}
    			
    			if(!flag){
    				TaobaoSkuInfoDetail detail=new TaobaoSkuInfoDetail();
    				detail.setAddDate(new Date());
    				detail.setAlterDate(new Date());
    				detail.setDate(DateUtil.shortStringToDate(entry.getKey()));
    				detail.setIsLock(0);
    				detail.setFlag(1);
    				if(updateFlag){
    					detail.setIsUpdate(1);
    				}
    				detail.setNumberCode(StringUtil.get_new_taobao_price_out_num());
					detail.setPrice(PriceUtil.getSalePrice(itemMap.get(skuInfo.getTaobaoTravelItem()),stock.getCost()));
    				detail.setStock(stock.getStock().intValue()*cabin.getMaxVolume().intValue());
    				detail.setPriceType(1);
    				detail.setTaobaoSkuInfo(skuInfo.getNumberCode());
    				commonDao.save(detail);
    			}
			}
    		
    		for(TaobaoSkuInfoDetail detail:details){
				if(detail.getUpdateFlag()==0){
					commonDao.delete(detail);
				}
			}
    		
    	}
    	
    }
    
}