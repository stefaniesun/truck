package xyz.work.weixin.svc.imp;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.ReturnUtil;
import xyz.util.DateUtil;
import xyz.util.ListNumberCode;
import xyz.util.PossessorUtil;
import xyz.util.StringTool;
import xyz.work.base.model.Airway;
import xyz.work.base.model.Cabin;
import xyz.work.base.model.Company;
import xyz.work.base.model.Cruise;
import xyz.work.base.model.Port;
import xyz.work.base.model.Shipment;
import xyz.work.base.model.Voyage;
import xyz.work.resources.model.Tkview;
import xyz.work.sell.model.Ptview;
import xyz.work.sell.model.PtviewSku;
import xyz.work.sell.model.PtviewSkuStock;
import xyz.work.weixin.svc.QueryDataSvc;


@Service
public class QueryDataSvcImp implements QueryDataSvc {
    @Autowired
    private CommonDao commonDao;

    @Autowired
    private PossessorUtil possessorUtil;

    @Override
    public Map<String, Object> queryCruiseList() {
        String ptviewSql = "SELECT p.cruise FROM ptview p";
        @SuppressWarnings("unchecked")
        List<String> numberList = commonDao.getSqlQuery(ptviewSql).list();
        
        String hql = "from Cruise c ";
        hql += "where c.numberCode in ("+ StringTool.listToSqlString(numberList) +") ";
        hql += "order by sortNum desc ";
        @SuppressWarnings("unchecked")
        List<Cruise> cruiseList = commonDao.queryByHql(hql);

        @SuppressWarnings("unchecked")
        List<Company> companyList = commonDao.queryByHql("from Company");
        for (Cruise cruiseObj : cruiseList) {
            for (Company companyObj : companyList) {
                if (cruiseObj.getCompany().equals(companyObj.getNumberCode())) {
                    cruiseObj.setCompanyNameCn(companyObj.getNameCn());
                    break;
                }
            }
        }
        
        return ReturnUtil.returnMap(1, cruiseList);
    }

    @Override
    public Map<String, Object> queryCruiseAndDate(String cruise) {
        if (StringTool.isEmpty(cruise)) {
            return ReturnUtil.returnMap(0, "请选择邮轮!");
        }

        // 邮轮
        Cruise cruiseObj = (Cruise)commonDao.getObjectByUniqueCode("Cruise", "numberCode", cruise);
        Company companyObj = (Company)commonDao.getObjectByUniqueCode("Company", "numberCode", cruiseObj.getCompany());
        cruiseObj.setCompanyNameCn(companyObj.getNameCn());

        // 年-月
        String mothSql = "SELECT DATE_FORMAT(p.travel_date,'%Y-%m') AS travelDate ";
        mothSql += "FROM ptview p WHERE ";
        mothSql += "p.cruise = '" + cruise + "'  ";
        mothSql += "AND p.travel_date >= NOW() ";
        mothSql += "GROUP BY DATE_FORMAT(p.travel_date,'%Y-%m') ";
        @SuppressWarnings("unchecked")
        List<Object> mothList = commonDao.getSqlQuery(mothSql).list();

        Map<String, Object> dateList = new HashMap<String, Object>();
        for(Object obj : mothList){
            String moth = (String)obj;
            
            String ptviewHql = "from Ptview p where p.cruise = '" + cruise + "' ";
            ptviewHql += "and p.travelDate >= NOW() ";
            ptviewHql += "and DATE_FORMAT(p.travelDate,'%Y-%m') = '" + moth + "' ";
            ptviewHql += "group by p.shipment ";
            @SuppressWarnings("unchecked")
            List<Ptview> ptviewList = commonDao.queryByHql(ptviewHql);
            
            List<Map<String, Object>> stockList = new ArrayList<Map<String, Object>>();
            for (Ptview pt : ptviewList) {
                Map<String, Object> stockMap = new HashMap<String, Object>();
                String priceSql = "SELECT MIN(t.price) FROM ptview_sku_stock t ";
                priceSql += "WHERE DATE_FORMAT(t.date,'%Y-%m') = '" + moth + "' ";
                priceSql += "AND t.ptview_sku IN ( ";
                priceSql += " SELECT s.number_code FROM ptview_sku s ";
                priceSql += " WHERE s.ptview = '" + pt.getNumberCode() + "') ";
                @SuppressWarnings("unchecked")
                List<Object> priceList = commonDao.getSqlQuery(priceSql).list();
                if (priceList.size() < 1) {
                    continue;
                }
                BigDecimal minPrice = (BigDecimal)priceList.get(0);
                stockMap.put("travelDate", pt.getTravelDate());
                stockMap.put("minPrice", minPrice);
                stockList.add(stockMap);
            }
            
            dateList.put(moth, stockList);
            
        }
        
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("cruiseObj", cruiseObj);
        resultMap.put("dateList", dateList);
        //resultMap.put("mothList", mothList);

        return ReturnUtil.returnMap(1, resultMap);
    }

    @SuppressWarnings("static-access")
    @Override
    public Map<String, Object> clickDateOper(String cruise , String date) {
        if(StringTool.isEmpty(cruise)){
            return ReturnUtil.returnMap(0, "邮轮信息错误!");
        }
        if(StringTool.isEmpty(date)){
            return ReturnUtil.returnMap(0, "请选择出发日期!");
        }
        String ptviewHql = "from Ptview p where p.cruise = '" + cruise + "' ";
        ptviewHql += "and p.travelDate = '" + date + "' ";
        @SuppressWarnings("unchecked")
        List<Ptview> ptList = commonDao.queryByHql(ptviewHql);
        if (ptList.size() < 1) {
            return ReturnUtil.returnMap(0, "该产品信息已不存在!");
        }
        Ptview ptviewObj = ptList.get(0);

        Shipment shipObj = (Shipment)commonDao.getObjectByUniqueCode("Shipment", "numberCode", ptviewObj.getShipment());
        if (shipObj == null) {
            return ReturnUtil.returnMap(0, "【" + date + "】出发日期的航期已不存在!");
        }
        // 航线
        Airway airwayObj = (Airway)commonDao.getObjectByUniqueCode("Airway", "numberCode", shipObj.getAirway());
        if (airwayObj == null) {
            return ReturnUtil.returnMap(0, "该航线信息已不存在!");
        }

        // 航程信息
        String voyageHql = "from Voyage v where v.shipment = '"+ shipObj.getNumberCode() +"' ";
        voyageHql += "order by v.priority asc ";
        @SuppressWarnings("unchecked")
        List<Voyage> voyageList = commonDao.queryByHql(voyageHql);

        String portHql = "from Port ";
        @SuppressWarnings("unchecked")
        List<Port> portList = commonDao.queryByHql(portHql);

        Date shipDate = shipObj.getTravelDate();
        int tempDay = 1;
        for (Voyage vo : voyageList) {
            if (tempDay > 1) {
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(shipDate);
                calendar.add(calendar.DATE, 1);
                shipDate = calendar.getTime();
            }
            String dateStr = DateUtil.dateToString(shipDate);
            String[] dateArry = dateStr.split("-");

            String time = dateArry[0] + "月" + dateArry[1] + "日";
            vo.setTime(time);
            for (Port portObj : portList) {
                if (portObj.getNumberCode().equals(vo.getPort())) {
                    vo.setPortNameCn(portObj.getNameCn());
                    vo.setPortAddress(portObj.getAddress());
                    break;
                }
            }

            tempDay++ ;
        }

        Map<String, Object> airwayMap = new HashMap<String, Object>();
        airwayMap.put("airway", airwayObj.getNameCn());
        airwayMap.put("travelDate", ptviewObj.getTravelDate());
        airwayMap.put("fromPlace", ptviewObj.getFromPlace());

        String skuHql = "from PtviewSku s where s.ptview = '"+ ptviewObj.getNumberCode() +"' ";
        @SuppressWarnings("unchecked")
        List<PtviewSku> skuList = commonDao.queryByHql(skuHql);

        List<String> skuNumber = new ArrayList<String>();
        for (PtviewSku sku : skuList) {
            skuNumber.add(sku.getNumberCode());
        }

        String skuStockHql = "from PtviewSkuStock s where s.ptviewSku in ("+ StringTool.listToSqlString(skuNumber) + ") ";
        @SuppressWarnings("unchecked")
        List<PtviewSkuStock> skuStockList = commonDao.queryByHql(skuStockHql);

        List<String> tkviewNumber = new ArrayList<String>();
        for (PtviewSkuStock stock : skuStockList) {
            tkviewNumber.add(stock.getOutTkview());
        }

        String tkviewHql = "from Tkview t where t.numberCode in ("+ StringTool.listToSqlString(tkviewNumber) + ") ";
        @SuppressWarnings("unchecked")
        List<Tkview> tkviewList = commonDao.queryByHql(tkviewHql);

        List<String> cabinNumber = new ArrayList<String>();
        for (Tkview tk : tkviewList) {
            cabinNumber.add(tk.getCabin());
        }

        String cabinHql = "from Cabin c where c.numberCode in ("
                          + StringTool.listToSqlString(cabinNumber) + ") ";
        @SuppressWarnings("unchecked")
        List<Cabin> cabinList = commonDao.queryByHql(cabinHql);

        List<Map<String, Object>> ptviewList = new ArrayList<Map<String, Object>>();
        for (Cabin cabinObj : cabinList) {
            Map<String, Object> cabinMap = new HashMap<String, Object>();
            String nameCn = cabinObj.getNameCn();
            BigDecimal volume = cabinObj.getVolume();
            BigDecimal maxVolume = cabinObj.getMaxVolume();
            String acreage = cabinObj.getAcreage();
            String floors = cabinObj.getFloors();
            int type = cabinObj.getType();
            String images  = cabinObj.getImages();
            String cabinType = "内舱房";
            if (type == 1) {
                cabinType = "海景房";
            }
            else if (type == 2) {
                cabinType = "阳台房";
            }
            else if (type == 3) {
                cabinType = "套房";
            }

            BigDecimal price = new BigDecimal(0);
            int stock = 0;
            for (PtviewSkuStock stockObj : skuStockList) {
                boolean pd = false;
                for (Tkview tk : tkviewList) {
                    if (tk.getNumberCode().equals(stockObj.getOutTkview())
                        && tk.getCabin().equals(cabinObj.getNumberCode())) {
                        //price = stockObj.getPrice();
                        price = stockObj.getcPrice();
                        stock = stockObj.getStock();
                        pd = true;
                        break;
                    }
                }
                if (pd) {
                    break;
                }
            }

            cabinMap.put("nameCn", nameCn);
            cabinMap.put("volume", volume);
            cabinMap.put("maxVolume", maxVolume);
            cabinMap.put("acreage", acreage);
            cabinMap.put("floors", floors);
            cabinMap.put("cabinType", cabinType);
            cabinMap.put("price", price);
            cabinMap.put("stock", stock);
            cabinMap.put("images", images);
            ptviewList.add(cabinMap);

        }

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("airwayMap", airwayMap);
        resultMap.put("ptviewList", ptviewList);
        resultMap.put("voyageList", voyageList);

        return ReturnUtil.returnMap(1, resultMap);
    }

    @Override
    public Map<String, Object> getPtviewSort() {
        //出行日期
        String dateSql = "SELECT DATE_FORMAT(p.travel_date,'%Y-%m-%d') AS `value`,  ";
        dateSql += "DATE_FORMAT(p.travel_date,'%Y-%m-%d') AS text ";
        dateSql += "FROM ptview p ";
        dateSql += "WHERE p.travel_date >= NOW() ";
        dateSql += "GROUP BY DATE_FORMAT(p.travel_date,'%Y-%m-%d') ";
        dateSql += "ORDER BY DATE_FORMAT(p.travel_date,'%Y-%m-%d') ";
        SQLQuery queryDate = commonDao.getSqlQuery(dateSql);
        queryDate.addScalar("value").addScalar("text").setResultTransformer(
            Transformers.aliasToBean(ListNumberCode.class));
        @SuppressWarnings("unchecked")
        List<ListNumberCode> travelDateList = queryDate.list();
        
        //邮轮条件
        String cruiseSql = "SELECT c.number_code AS value,c.name_cn AS text ";
        cruiseSql += "FROM cruise c ";
        cruiseSql += "ORDER BY c.sort_num DESC ";
        SQLQuery queryCruise = commonDao.getSqlQuery(cruiseSql);
        queryCruise.addScalar("value").addScalar("text").setResultTransformer(
            Transformers.aliasToBean(ListNumberCode.class));
        @SuppressWarnings("unchecked")
        List<ListNumberCode> cruiseList = queryCruise.list();
        
        //出港港口
        String portNumSql = "SELECT v.`port` FROM voyage v ";
        portNumSql += "WHERE v.priority=1 ";
        portNumSql += "AND v.shipment IN ( ";
        portNumSql += " SELECT p.shipment FROM ptview p ";
        portNumSql += " WHERE p.travel_date >= NOW() ) ";
        portNumSql += "GROUP BY v.shipment ";
        @SuppressWarnings("unchecked")
        List<String> portNumList = commonDao.getSqlQuery(portNumSql).list();
        
        String portSql = "SELECT p.number_code AS value,p.name_cn AS text FROM `port` p ";
        portSql += "WHERE p.number_code IN ("+ StringTool.listToSqlString(portNumList) +") ";
        SQLQuery queryPort = commonDao.getSqlQuery(portSql);
        queryPort.addScalar("value").addScalar("text").setResultTransformer(
            Transformers.aliasToBean(ListNumberCode.class));
        @SuppressWarnings("unchecked")
        List<ListNumberCode> portList = queryPort.list();
        
        Map<String, Object> resultMap = new HashMap<String, Object>(); 
        resultMap.put("travelDateList", travelDateList);
        resultMap.put("cruiseList", cruiseList);
        resultMap.put("portList", portList);
        
        return ReturnUtil.returnMap(1, resultMap);
    }

   @Override
    public Map<String, Object> queryPtviewList(String date , String cruise , String port ,
                                               String sort , String order) {
       
       String sql = "SELECT p.number_code AS ptview, ";  
       sql += "p.name_cn AS nameCn, ";
       sql += "p.travel_date AS travelDate, ";
       sql += "p.from_place AS fromPlace, ";
       sql += "p.cruise AS cruise, ";
       //sql += "MIN(s.c_price),s.a_c_price, s.s_c_Price, s.m_c_price,  ";
       sql += "MIN(s.price) ";
       sql += "FROM ptview_sku_stock s ";
       sql += "LEFT JOIN ptview_sku k ON s.ptview_sku = k.number_code ";
       sql += "LEFT JOIN ptview p ON k.ptview = p.number_code ";
       sql += "WHERE 1=1 ";
       if(StringTool.isNotNull(cruise)){   //邮轮
           sql += "AND p.cruise = '"+ cruise +"' ";
       }
       if(StringTool.isNotNull(date)){    //出发日期
           sql += "AND p.travel_date = '"+ date +"' ";
       }
       if(StringTool.isNotNull(port)){    //港口
           String shipSql = "SELECT v.shipment FROM voyage v WHERE v.`port` = '"+ port +"' ";
           @SuppressWarnings("unchecked")
           List<String> shipNumber = commonDao.getSqlQuery(shipSql).list();
           
           sql += "AND p.shipment IN ("+ StringTool.listToSqlString(shipNumber) +") ";
       }
       sql += "GROUP BY k.ptview ";
       if(StringTool.isNotNull(sort)){
           if(sort.equals("price")){ //DESC 
               sql += "ORDER BY MIN(s.price) "+order;
           }else{
               //TODO 销量最多
           }
       }else{
           sql += "ORDER BY MIN(s.price) ";
       }
       @SuppressWarnings("unchecked")
       List<Object[]> list = commonDao.getSqlQuery(sql).list();
       
       List<String> cruiseNumber = new ArrayList<String>();
       for(Object[] obj : list){
           cruiseNumber.add((String)obj[4]);
       }
       
       String cruiseHql = "from Cruise where numberCode in ("+ StringTool.listToSqlString(cruiseNumber) +") ";
       @SuppressWarnings("unchecked")
       List<Cruise> cruiseList = commonDao.queryByHql(cruiseHql);
       
       List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
       for(Object[] obj : list){
           String ptview = (String)obj[0];
           if(StringTool.isEmpty(ptview)){
               continue;
           }
           String nameCn = (String)obj[1];
           Date travel_date = (Date)obj[2];    //出发日期
           String travelDate = DateUtil.dateToShortString(travel_date);
           String fromPlace = (String)obj[3];
           BigDecimal price = (BigDecimal)obj[5];
           String cruise_number = (String)obj[4];
           String cruiseNameCn = "";
           String weixinSmallImg = "";
           String weixinLargeImg = "";
           
           for(Cruise cruObj : cruiseList){
               if(cruise_number.equals(cruObj.getNumberCode())){
                   cruiseNameCn = cruObj.getNameCn();
                   weixinSmallImg = cruObj.getWeixinSmallImg();
                   weixinLargeImg = cruObj.getWeixinLargeImg();
                   break;
               }
           }
           
           Map<String, Object> map = new HashMap<String, Object>(); 
           map.put("ptview", ptview);                 //产品编号
           map.put("nameCn", nameCn);                 //产品名称
           map.put("travelDate", travelDate);         //出发日期
           map.put("fromPlace", fromPlace);           //出发地
           map.put("cruiseNameCn", cruiseNameCn);     //邮轮名称
           map.put("weixinSmallImg", weixinSmallImg); //小图
           map.put("weixinLargeImg", weixinLargeImg); //大图
           map.put("price", price);                   //价格
           resultList.add(map);
       }
       
       return ReturnUtil.returnMap(1, resultList);
    }

    @SuppressWarnings("static-access")
    @Override
    public Map<String, Object> queryPtviewByNumber(String ptview) {
        Ptview ptviewObj = (Ptview)commonDao.getObjectByUniqueCode("Ptview", "numberCode", ptview);
        if(ptviewObj == null){
            return ReturnUtil.returnMap(0, "该产品信息不存在!");
        }
        
        Shipment shipObj = (Shipment)commonDao.getObjectByUniqueCode("Shipment", "numberCode", ptviewObj.getShipment());
        if(shipObj == null){
            return ReturnUtil.returnMap(0, "航期信息不存在!");
        }
        
        Cruise cruiseObj = (Cruise)commonDao.getObjectByUniqueCode("Cruise", "numberCode", ptviewObj.getCruise());
        if(cruiseObj == null){
            return ReturnUtil.returnMap(0, "邮轮信息不存在!");
        }
        
        Airway airwayObj = (Airway)commonDao.getObjectByUniqueCode("Airway", "numberCode", shipObj.getAirway()); 
        if(airwayObj == null){
            return ReturnUtil.returnMap(0, "航线信息不存在!");
        }
        
        // 航程信息
        String voyageHql = "from Voyage v where v.shipment = '" + shipObj.getNumberCode() + "' ";
        voyageHql += "order by v.priority asc ";
        @SuppressWarnings("unchecked")
        List<Voyage> voyageList = commonDao.queryByHql(voyageHql);

        String portHql = "from Port ";
        @SuppressWarnings("unchecked")
        List<Port> portList = commonDao.queryByHql(portHql);

        Date shipDate = shipObj.getTravelDate();
        int tempDay = 1;
        for (Voyage vo : voyageList) {
            if (tempDay > 1) {
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(shipDate);
                calendar.add(calendar.DATE, 1);
                shipDate = calendar.getTime();
            }
            String dateStr = DateUtil.dateToString(shipDate);
            String[] dateArry = dateStr.split("-");

            String time = dateArry[0] + "月" + dateArry[1] + "日";
            vo.setTime(time);
            for (Port portObj : portList) {
                if (portObj.getNumberCode().equals(vo.getPort())) {
                    vo.setPortNameCn(portObj.getNameCn());
                    vo.setPortAddress(portObj.getAddress());
                    break;
                }
            }

            tempDay++ ;
        }
        
        //售卖产品
        String skuHql = "from PtviewSku s where s.ptview = '"+ ptviewObj.getNumberCode() +"' ";
        @SuppressWarnings("unchecked")
        List<PtviewSku> skuList = commonDao.queryByHql(skuHql);
        
        List<String> skuNumber = new ArrayList<String>();
        for(PtviewSku sku : skuList){
            skuNumber.add(sku.getNumberCode());
        }
        
        String skuStockHql = "from PtviewSkuStock s where s.ptviewSku in ("+ StringTool.listToSqlString(skuNumber) +") ";
        @SuppressWarnings("unchecked")
        List<PtviewSkuStock> skuStockList = commonDao.queryByHql(skuStockHql);
        
        List<String> tkviewNumber = new ArrayList<String>();
        for (PtviewSkuStock stock : skuStockList) {
            tkviewNumber.add(stock.getOutTkview());
        }
        
        String tkviewHql = "from Tkview t where t.numberCode in ("+ StringTool.listToSqlString(tkviewNumber) + ") ";
        @SuppressWarnings("unchecked")
        List<Tkview> tkviewList = commonDao.queryByHql(tkviewHql);
        
        List<String> cabinNumber = new ArrayList<String>();
        for (Tkview tk : tkviewList) {
            cabinNumber.add(tk.getCabin());
        }
            
        String cabinHql = "from Cabin c where c.numberCode in ("+ StringTool.listToSqlString(cabinNumber) + ") ";
        @SuppressWarnings("unchecked")
        List<Cabin> cabinList = commonDao.queryByHql(cabinHql);
            
        List<Map<String, Object>> ptviewList = new ArrayList<Map<String, Object>>();
        for (Cabin cabinObj : cabinList) {
            Map<String, Object> cabinMap = new HashMap<String, Object>();
            String nameCn = cabinObj.getNameCn();
            BigDecimal volume = cabinObj.getVolume();
            BigDecimal maxVolume = cabinObj.getMaxVolume();
            String acreage = cabinObj.getAcreage();
            String floors = cabinObj.getFloors();
            int type = cabinObj.getType();
            String images  = cabinObj.getImages();
            String cabinType = "内舱房";
            if (type == 1) {
                cabinType = "海景房";
            }else if (type == 2) {
                cabinType = "阳台房";
            }else if (type == 3) {
                cabinType = "套房";
            }
            cabinMap.put("nameCn", nameCn);
            cabinMap.put("volume", volume);
            cabinMap.put("maxVolume", maxVolume);
            cabinMap.put("acreage", acreage);
            cabinMap.put("floors", floors);
            cabinMap.put("cabinType", cabinType);
            cabinMap.put("images", images);
            
            BigDecimal price = new BigDecimal(0);
            int stock = 0;
            for (PtviewSkuStock stockObj : skuStockList) {
                boolean pd = false;
                for (Tkview tk : tkviewList) {
                   if (tk.getNumberCode().equals(stockObj.getOutTkview())
                       && tk.getCabin().equals(cabinObj.getNumberCode())) {
                       //price = stockObj.getPrice();
                       price = stockObj.getcPrice();
                       stock = stockObj.getStock();
                       pd = true;
                       break;
                   }
                }
                if (pd) {
                   break;
                }
            }
            cabinMap.put("price", price);
            cabinMap.put("stock", stock);
            ptviewList.add(cabinMap);
        }
        
        Map<String, Object> resultMap = new HashMap<String, Object>(); 
        resultMap.put("cruiseObj", cruiseObj);                    //邮轮对象
        resultMap.put("airwayNameCn", airwayObj.getNameCn());     //航线
        resultMap.put("travelDate", ptviewObj.getTravelDate());   //出发时间
        resultMap.put("fromPlace", ptviewObj.getFromPlace());     //出发地
        resultMap.put("ptviewList", ptviewList);                  //售卖产品
        resultMap.put("voyageList", voyageList);                  //行程介绍
        
        return ReturnUtil.returnMap(1, resultMap);
    }

}