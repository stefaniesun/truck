package xyz.work.r_base.svc.imp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.ReturnUtil;
import xyz.util.DateUtil;
import xyz.util.StringTool;
import xyz.util.UUIDUtil;
import xyz.work.base.model.Cabin;
import xyz.work.base.model.Cruise;
import xyz.work.r_base.model.R_Airway;
import xyz.work.r_base.model.R_Area;
import xyz.work.r_base.model.R_Port;
import xyz.work.r_base.model.R_Shipment;
import xyz.work.r_base.model.R_Tkview;
import xyz.work.r_base.svc.R_TkviewSvc;

@Service
public class R_TkviewSvcImp implements R_TkviewSvc {
    
    @Autowired
    private CommonDao commonDao;

    @Override
    public Map<String, Object> readTxtOper() {
        
        List<String> lineTxtList = new ArrayList<String>();
        String resultStr = "";
        Date eff_date = new Date();
        System.out.print("读取数据开始时间:");
        System.out.println(eff_date);
        try {
            String encoding = "UTF-8"; 
            //CHN_cruise_no_air_price.txt  100条数据
            //CHN_CNY_cruise_no_air_price.txt  5000条数据
            String filePath = "C:\\CHN_CHN_CNY_cruise_no_air_price.txt";
            File file = new File(filePath);
            if(file.isFile() && file.exists()){
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding); 
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    lineTxt = lineTxt.trim();
                    if(StringTool.isNotNull(lineTxt) && lineTxt.length() > 30){
                        lineTxtList.add(lineTxt);
                    }
                }
                read.close();
            }else{
                resultStr = "找不到指定的文件!";
                System.out.println("找不到指定的文件!");
                return ReturnUtil.returnMap(0, resultStr);
            }
        }catch (Exception e) {
            resultStr = "读取文件内容出错!";
            System.out.println("读取文件内容出错!");
            e.printStackTrace();
            return ReturnUtil.returnMap(0, resultStr);
        }
        Date end = new Date();
        System.out.print("读取数据结束时间:");
        System.out.println(end);
        
        List<String> areaMarkList = new ArrayList<String>();   //航区
        List<String> portMarkList = new ArrayList<String>();   //港口
        List<Map<String, String>> airwayMapList = new ArrayList<Map<String, String>>();  //航线
        List<Map<String, String>> shipMapList = new ArrayList<Map<String, String>>();    //航期
        List<Map<String, String>> tkviewMapList = new ArrayList<Map<String, String>>();  //单品及价格
        for(String line : lineTxtList){
            String[] arry = line.split("\\|");
            String packageID = (String)arry[0];        //0：航期代码
            String sailDate = (String)arry[1];         //1：出发日期
            String[] sailArry = sailDate.split("\\/");
            sailDate = sailArry[2]+"-"+sailArry[0]+"-"+sailArry[1];
            String fareCode = (String)arry[2];         //2：价格代码
            String sRoomCate = (String)arry[3];        //3：舱型代码
            String priceEffDate = (String)arry[4];     //4：价格生效日期
            String[] effArry = priceEffDate.split("\\/");
            priceEffDate = effArry[2]+"-"+effArry[0]+"-"+effArry[1];
            String priceEffTime = (String)arry[5];     //5：价格生效时间
            String priceEndDate = (String)arry[6];     //6：价格到期日期
            String[] endArry = priceEndDate.split("\\/");
            priceEndDate = endArry[2]+"-"+endArry[0]+"-"+endArry[1];
            String priceEndTime = (String)arry[7];     //7：价格到期时间
            String shipCode = (String)arry[9];         //9：邮轮代码
            String departPortCode = (String)arry[10];  //10：出发港口
            String subRegionCode = (String)arry[11];   //11：航线二级区域
            String regionCode = (String)arry[12];      //12：航线区域
            String packageDesc = (String)arry[22];     //22：航次描述
            String fareCodeDesc = (String)arry[23];    //23：价格代码描述
            String guest1_Amt = (String)arry[25];      //25：第一人船票价
            String guest2_Amt = (String)arry[26];      //26：第二人船票价
            String guest3_Amt = (String)arry[27];      //27：第三人船票价
            String guest4_Amt = (String)arry[28];      //28：第四人船票价
            String single_Amt = (String)arry[31];      //31：单人船票价
            String guest1NC_FareAmt = (String)arry[35];//35：第一人非佣金
            String guest2NC_FareAmt = (String)arry[36];//36：第二人非佣金
            String guest3NC_FareAmt = (String)arry[37];//37：第三人非佣金
            String guest4NC_FareAmt = (String)arry[38];//38：第四人非佣金
            String singleNC_FareAmt = (String)arry[41];//41：单人非佣金
            String taxFee_Amt = (String)arry[42];      //42：港务税费
            String numOfDays = (String)arry[63];       //63：航次时间
            
            areaMarkList.add(regionCode);
            
            portMarkList.add(departPortCode);
            
            Map<String, String> airwayMap = new HashMap<String, String>();
            airwayMap.put("areaMark", regionCode);
            airwayMap.put("mark", subRegionCode);
            airwayMap.put("nights", numOfDays);
            airwayMapList.add(airwayMap);
            
            Map<String, String> shipMap = new HashMap<String, String>();
            shipMap.put("cruiseMark", shipCode);
            shipMap.put("areaMark", regionCode);
            shipMap.put("airwayMark", subRegionCode);
            shipMap.put("travelDate", sailDate);
            shipMap.put("mark", packageID);
            shipMap.put("portMark", departPortCode);
            shipMap.put("shipDesc", packageDesc );
            shipMap.put("tripDays", numOfDays);
            shipMapList.add(shipMap);
            
            String priceEff = priceEffDate+" "+priceEffTime;  //价格生效日
            String priceEnd = priceEndDate+" "+priceEndTime;  //价格到期日
            Map<String, String> tkviewMap = new HashMap<String, String>();
            tkviewMap.put("cruiseMark", shipCode);
            tkviewMap.put("shipmentMark", packageID);
            tkviewMap.put("travelDate", sailDate);
            tkviewMap.put("cabinMark", sRoomCate);
            
            tkviewMap.put("priceMark", fareCode);
            tkviewMap.put("priceDesc", fareCodeDesc);
            tkviewMap.put("priceEffDate", priceEff);
            tkviewMap.put("priceEndDate", priceEnd);
            tkviewMap.put("guest1_Amt", guest1_Amt);
            tkviewMap.put("guest2_Amt", guest2_Amt);
            tkviewMap.put("guest3_Amt", guest3_Amt);
            tkviewMap.put("guest4_Amt", guest4_Amt);
            tkviewMap.put("single_Amt", single_Amt);
            tkviewMap.put("guest1NC_FareAmt", guest1NC_FareAmt);
            tkviewMap.put("guest2NC_FareAmt", guest2NC_FareAmt);
            tkviewMap.put("guest3NC_FareAmt", guest3NC_FareAmt);
            tkviewMap.put("guest4NC_FareAmt", guest4NC_FareAmt);
            tkviewMap.put("singleNC_FareAmt", singleNC_FareAmt);
            tkviewMap.put("taxFee_Amt", taxFee_Amt);
            tkviewMapList.add(tkviewMap);
        }
        
        //邮轮
        //TODO 邮轮公司是写死的
        String cruiHql = "from Cruise where company = '54023710b14b4e51adcd1592e0ef839a' ";
        @SuppressWarnings("unchecked")
        List<Cruise> listCruise = commonDao.queryByHql(cruiHql);
        
        //舱型
        String cabin_hql = "from Cabin ";
        @SuppressWarnings("unchecked")
        List<Cabin> listCabin = commonDao.queryByHql(cabin_hql); 
        
        //港口
        String portHql = "from R_Port ";
        @SuppressWarnings("unchecked")
        List<R_Port> listPort = commonDao.queryByHql(portHql);
        
        //航线
        String rAirwayHql = "from R_Airway ";
        @SuppressWarnings("unchecked")
        List<R_Airway> rAirwayList = commonDao.queryByHql(rAirwayHql);
        
        //航期
        String shipHql = "from R_Shipment ";
        @SuppressWarnings("unchecked")
        List<R_Shipment> rShipList = commonDao.queryByHql(shipHql);
        
        int areaNum = 0;    //新建航区个数
        //去重
        HashSet<String> areaSet = new HashSet<String>(areaMarkList);
        areaMarkList.clear(); 
        areaMarkList.addAll(areaSet);
        
        String rAreaHql = "from R_Area where mark in ("+ StringTool.listToSqlString(areaMarkList) +") ";
        @SuppressWarnings("unchecked")
        List<R_Area> rAreaList = commonDao.queryByHql(rAreaHql);
        
        for(String areaStr : areaMarkList){
            boolean pd = true;
            for(R_Area areaObj : rAreaList){
                if(areaStr.equals(areaObj.getMark())){
                    pd = false;
                    break;
                }
            }
            if(pd){
                R_Area rAreaObj = new R_Area();
                rAreaObj.setNumberCode(UUIDUtil.getUUIDStringFor32());
                rAreaObj.setMark(areaStr);
                commonDao.save(rAreaObj);
                rAreaList.add(rAreaObj);
                areaNum ++;
            }
        }
        
        int airwayNum = 0;    //新建航线个数
        for(Map<String, String> map : airwayMapList){
            String areaMark = map.get("areaMark");
            String mark = map.get("mark");
            String nights = map.get("nights");
            boolean pd = true;
            for(R_Airway airwayObj : rAirwayList){
                if(areaMark.equals(airwayObj.getAreaMark()) && mark.equals(airwayObj.getMark())){
                    pd = false;
                    break;
                }
            }
            if(pd){
                R_Area rAreaObj = (R_Area)commonDao.getObjectByUniqueCode("R_Area", "mark", areaMark);
                R_Airway rAirwayObj = new R_Airway();
                rAirwayObj.setNumberCode(UUIDUtil.getUUIDStringFor32());
                rAirwayObj.setArea(rAreaObj.getNumberCode());
                rAirwayObj.setAreaMark(areaMark);
                rAirwayObj.setMark(mark);
                rAirwayObj.setNights(new BigDecimal(nights));
                rAirwayObj.setDays(new BigDecimal(nights).add(new BigDecimal(1)));
                commonDao.save(rAirwayObj);
                rAirwayList.add(rAirwayObj);
                airwayNum ++;
            }
        }
        
        int portNum = 0;    //新建港口个数
        //去重
        HashSet<String> portSet = new HashSet<String>(portMarkList);
        portMarkList.clear();
        portMarkList.addAll(portSet);
        
        String rPortHql = "from R_Port where mark in ("+ StringTool.listToSqlString(portMarkList) +") ";
        @SuppressWarnings("unchecked")
        List<R_Port> rPortList = commonDao.queryByHql(rPortHql);
        
        for(String portStr : portMarkList){
            boolean pd = true;
            for(R_Port portObj : rPortList){
                if(portStr.equals(portObj.getMark())){
                    pd = false;
                    break;
                }
            }
            if(pd){
                R_Port rPortObj = new R_Port();
                rPortObj.setNumberCode(UUIDUtil.getUUIDStringFor32());
                rPortObj.setMark(portStr);
                commonDao.save(rPortObj);
                rPortList.add(rPortObj);
                portNum ++;
            }
        }
        
        int shipNum = 0;   //新建航期个数
        for(Map<String, String> map : shipMapList){
            String cruiseMark = map.get("cruiseMark");
            String areaMark = map.get("areaMark");
            String airwayMark = map.get("airwayMark");
            String sailDate = map.get("travelDate");
            String mark = map.get("mark");
            String portMark = map.get("portMark");
            String shipDesc = map.get("shipDesc");
            String tripDays = map.get("tripDays");
            boolean pd = true;
            for(R_Shipment shipObj : rShipList){
                if(cruiseMark.equals(shipObj.getCruiseMark()) && mark.equals(shipObj.getMark()) ){
                    pd = false;
                    break;
                }
            }
            if(pd){
                Cruise cruiseObj = null;
                for(Cruise cr : listCruise){
                    if(cruiseMark.equals(cr.getMark())){
                        cruiseObj = cr;
                        break;
                    }
                }
                if(cruiseObj != null){
                    R_Airway rAirway  = null;
                    for(R_Airway way : rAirwayList){
                        if(areaMark.equals(way.getAreaMark()) && airwayMark.equals(way.getMark())){
                            rAirway = way;
                            break;
                        }
                    }
                    
                    R_Port rPortObj  = null;
                    for(R_Port pt : listPort){
                        if(portMark.equals(pt.getMark())){
                            rPortObj = pt;
                            break;
                        }
                    }
                    
                    R_Shipment rShipment = new R_Shipment();
                    if(cruiseObj != null){
                        rShipment.setCruise(cruiseObj.getNumberCode());
                    }
                    rShipment.setCruiseMark(cruiseMark);
                    rShipment.setArea(rAirway.getArea());
                    rShipment.setAreaMark(areaMark);
                    rShipment.setAirway(rAirway.getNumberCode());
                    rShipment.setAirwayMark(airwayMark);
                    rShipment.setNumberCode(mark.substring(2));
                    rShipment.setMark(mark);
                    rShipment.setTravelDate(DateUtil.shortStringToDate(sailDate));
                    if(rPortObj != null){
                        rShipment.setPort(rPortObj.getNumberCode());
                    }
                    rShipment.setPortMark(portMark);
                    rShipment.setTripDays(Integer.valueOf(tripDays)+1);
                    rShipment.setShipDesc(shipDesc);
                    commonDao.save(rShipment);
                    rShipList.add(rShipment);
                    shipNum ++;
                }
                
            }
        }
        
        int tkviewNum = 0;   //新建单品个数
        int stockNum = 0;    //新建单品价格个数
       
        String deleteTk = "DELETE FROM r_tkview ";
        commonDao.getSqlQuery(deleteTk).executeUpdate();
        String deleteSt = "DELETE FROM r_stock ";
        commonDao.getSqlQuery(deleteSt).executeUpdate();
        for(Map<String, String> map : tkviewMapList){ 
            String cruiseMark = map.get("cruiseMark");
            String shipmentMark = map.get("shipmentMark");
            String travelDate = map.get("travelDate");
            String cabinMark = map.get("cabinMark");
            
            String priceMark = map.get("priceMark");
            String priceDesc = map.get("priceDesc");
            String priceEffDate = map.get("priceEffDate");
            String priceEndDate = map.get("priceEndDate");
            Date travel_date = DateUtil.shortStringToDate(travelDate);
            Date endDate = DateUtil.stringToDate(priceEndDate);
            Date dateNew = new Date();
            if(travel_date.getTime()-dateNew.getTime() <= 0 ){
                continue;
            }
            String guest1_Amt = map.get("guest1_Amt");
            String guest2_Amt = map.get("guest2_Amt");
            String guest3_Amt = map.get("guest3_Amt");
            String guest4_Amt = map.get("guest4_Amt");
            String single_Amt = map.get("single_Amt");
            String guest1NC_FareAmt = map.get("guest1NC_FareAmt");
            String guest2NC_FareAmt = map.get("guest2NC_FareAmt");
            String guest3NC_FareAmt = map.get("guest3NC_FareAmt");
            String guest4NC_FareAmt = map.get("guest4NC_FareAmt");
            String singleNC_FareAmt = map.get("singleNC_FareAmt");
            String taxFee_Amt = map.get("taxFee_Amt");
            
            Cruise cruise_obj = null;
            for(Cruise cr : listCruise){
                if(cruiseMark.equals(cr.getMark())){
                    cruise_obj = cr;
                    break;
                }
            }
            if(cruise_obj != null){
                Cabin cabinObj = null;
                for(Cabin cb : listCabin){
                    if(cruise_obj.getNumberCode().equals(cb.getCruise()) && cabinMark.equals(cb.getMark())){
                        cabinObj = cb;
                        break;
                    }
                }
                String tkivewNumberCode = "";
                if(cabinObj != null){
                    String tkNameCn = "";  //单品名称
                    if(cabinObj != null && StringTool.isNotNull(cabinObj.getNameCn())){
                        tkNameCn = "["+ travelDate +"]"+cabinObj.getNameCn();
                    }
                    tkivewNumberCode = UUIDUtil.getUUIDStringFor32();
                    String cruiseNumber = "";
                    if(cruise_obj != null){
                        cruiseNumber = cruise_obj.getNumberCode();
                    }
                    String cabinNumber = "";
                    if(cabinObj != null){
                        cabinNumber = cabinObj.getNumberCode();
                    }
                    String tkviewSql = "INSERT INTO r_tkview ";
                    tkviewSql += "(iidd,number_code,mark,name_cn,cabin,cabin_mark,cruise,cruise_mark,shipment,shipment_mark,shipment_travel_date) "; 
                    tkviewSql += "VALUES ('"+ tkivewNumberCode +"','"+ tkivewNumberCode +"','"+ ("TK"+cabinMark) +"','"+ tkNameCn +"','"+ cabinNumber +"','"+ cabinMark +"','"+ cruiseNumber +"','"+ cruiseMark +"','"+ shipmentMark.substring(2) +"','"+ shipmentMark +"','"+ travelDate +"') ";
                    SQLQuery tkQuery = commonDao.getSqlQuery(tkviewSql);
                    int result = tkQuery.executeUpdate();
                    /*R_Tkview rTkview = new R_Tkview();
                    if(cabinObj != null){
                        rTkview.setCabin(cabinObj.getNumberCode());
                    }
                    rTkview.setCabinMark(cabinMark);
                    if(cruiseObj != null){
                        rTkview.setCruise(cruiseObj.getNumberCode());
                    }
                    rTkview.setCruiseMark(cruiseMark);
                    rTkview.setMark("TK"+cabinMark);
                    rTkview.setNameCn(tkNameCn);
                    rTkview.setNumberCode(UUIDUtil.getUUIDStringFor32());
                    rTkview.setShipment(shipmentMark.substring(2));
                    rTkview.setShipmentMark(shipmentMark);
                    rTkview.setShipmentTravelDate(DateUtil.shortStringToDate(travelDate));
                    commonDao.save(rTkview);*/
                    if(result > 0){
                        tkviewNum++;
                    }
                }
                
                if(endDate.getTime()-dateNew.getTime() <= 0 || StringTool.isEmpty(tkivewNumberCode)){
                    continue;
                }
                double guest1Amt = Double.valueOf(guest1_Amt);  //第一人船票价
                double guest2Amt = Double.valueOf(guest2_Amt);  //第二人船票价
                double guest3Amt = Double.valueOf(guest3_Amt);  //第三人船票价
                double guest4Amt = Double.valueOf(guest4_Amt);  //第四人船票价
                double singleAmt = Double.valueOf(single_Amt);  //单人船票价
                double guest1NCFareAmt = Double.valueOf(guest1NC_FareAmt);  //第一人非佣金
                double guest2NCFareAmt = Double.valueOf(guest2NC_FareAmt);  //第二人非佣金
                double guest3NCFareAmt = Double.valueOf(guest3NC_FareAmt);  //第三人非佣金
                double guest4NCFareAmt = Double.valueOf(guest4NC_FareAmt);  //第四人非佣金
                double singleNCFareAmt = Double.valueOf(singleNC_FareAmt);  //单人非佣金
                double taxFeeAmt = Double.valueOf(taxFee_Amt);  //港务税费
                
                //单人均价
                double single_room = singleAmt+singleNCFareAmt+taxFeeAmt;
                //双人均价
                double doubleGuestAmt = guest1Amt+guest2Amt;
                double doubleFareAmt = guest1NCFareAmt+guest2NCFareAmt;
                double double_room = (doubleGuestAmt+doubleFareAmt+taxFeeAmt*2)/2;
                //三人均价
                double tripleGuestAmt = guest1Amt+guest2Amt+guest3Amt;
                double tripleFareAmt = guest1NCFareAmt+guest2NCFareAmt+guest3NCFareAmt;
                double triple_room = (tripleGuestAmt+tripleFareAmt+taxFeeAmt*3)/3;
                //四人均价
                double quadGuestAmt = guest1Amt+guest2Amt+guest3Amt+guest4Amt;
                double quadFareAmt = guest1NCFareAmt+guest2NCFareAmt+guest3NCFareAmt+guest4NCFareAmt;
                double quad_room = (quadGuestAmt+quadFareAmt+taxFeeAmt*4)/4;
                
                BigDecimal singleRoom = new BigDecimal(single_room);
                BigDecimal doubleRoom = new BigDecimal(double_room);
                BigDecimal tripleRoom = new BigDecimal(triple_room);
                BigDecimal quadRoom = new BigDecimal(quad_room);
                
                //TODO 供应商是设置死的，需要改
                String stockNumber = UUIDUtil.getUUIDStringFor32();
                String stockSql = "INSERT INTO r_stock ";
                stockSql += "(tkview,provider,provider_name_cn,iidd,number_code,price_mark,price_desc,price_eff_date,price_end_date,single_room,double_room,triple_room,quad_room) ";
                stockSql += "VALUES ('"+ tkivewNumberCode +"','17aa5024f9ac403e896a8783be48b60b','皇家加勒比游轮','"+ stockNumber +"','"+ stockNumber +"','"+ priceMark +"','"+ priceDesc +"','"+ priceEffDate +"','"+ priceEndDate +"',"+ singleRoom +","+ doubleRoom +","+ tripleRoom +","+ quadRoom +") ";            
                SQLQuery stQuery = commonDao.getSqlQuery(stockSql);
                int resultStock = stQuery.executeUpdate();
                
                /*R_Stock rStock = new R_Stock();
                rStock.setNumberCode(UUIDUtil.getUUIDStringFor32());
                rStock.setTkview(rTkview.getNumberCode());
                //TODO 供应商是设置死的，需要改
                rStock.setProvider("17aa5024f9ac403e896a8783be48b60b");
                rStock.setProviderNameCn("皇家加勒比游轮");
                rStock.setPriceMark(priceMark);
                rStock.setPriceDesc(priceDesc);
                rStock.setPriceEffDate(DateUtil.stringToDate(priceEffDate));
                rStock.setPriceEndDate(endDate);
                rStock.setSingleRoom(singleRoom);
                rStock.setDoubleRoom(doubleRoom);
                rStock.setTripleRoom(tripleRoom);
                rStock.setQuadRoom(quadRoom);
                commonDao.save(rStock);*/
                if(resultStock > 0){
                    stockNum++;
                }
                
            }
            
        }
        
        resultStr = "共有"+ lineTxtList.size() +"条数据";
        if(portNum > 0){
            resultStr += "【新建"+ portNum +"条港口信息】";
        }
        if(areaNum > 0){
            resultStr += "【新建"+ areaNum +"条航区信息】";
        }
        if(airwayNum > 0){
            resultStr += "【新建"+ airwayNum +"条航线信息】";
        }
        if(shipNum > 0){
            resultStr += "【新建"+ shipNum +"条航期信息】";
        }
        if(tkviewNum > 0){
            resultStr += "【"+ tkviewNum +"有效单品信息】";
        }
        if(stockNum > 0){
            resultStr += "【"+ stockNum +"有效价格信息】";
        }
        
        Date date = new Date();
        System.out.print("存储数据结束时间:");
        System.out.println(date);
        
        return ReturnUtil.returnMap(1, resultStr);
    }

    @Override
    public Map<String, Object> queryRTkviewList(int offset , int pageSize , String cruiseMark ,
                                                String cabinMark , String nameCn , String mark ,
                                                String mouth , String shipment) {
        String tkviweHql = "from R_Tkview t where 1=1 ";
        if(StringTool.isNotNull(cruiseMark)){
            tkviweHql += "and t.cruise ='"+ cruiseMark +"' ";
        }
        if(StringTool.isNotNull(cabinMark)){
            tkviweHql += "and t.cabinMark = '"+ cabinMark +"' ";
        }
        if(StringTool.isNotNull(nameCn)){
            tkviweHql += "and t.nameCn like '%"+ nameCn +"%' ";
        }
        if(StringTool.isNotNull(mark)){
            tkviweHql += "and t.mark like '%"+ mark +"%' ";
        }
        if(StringTool.isNotNull(mouth)) {
            tkviweHql += "and DATE_FORMAT(t.shipmentTravelDate,'%Y-%m') in ("+ StringTool.StrToSqlString(mouth) +") ";
        }
        if(StringTool.isNotNull(shipment)){
            tkviweHql += "and t.shipment = '"+ shipment +"' ";
        }
        
        Query countQuery = commonDao.getQuery("select count(*) " + tkviweHql);
        Number countNum = (Number)countQuery.uniqueResult();
        int count = countNum == null ? 0 : countNum.intValue();

        Query query = commonDao.getQuery(tkviweHql);
        query.setMaxResults(pageSize);
        query.setFirstResult(offset);
        @SuppressWarnings("unchecked")
        List<R_Tkview> list = query.list();
        
        /*邮轮档案*/
        String cruiseHql = "from Cruise";
        @SuppressWarnings("unchecked")
        List<Cruise> cruiseList = commonDao.queryByHql(cruiseHql);
        
        /*舱型档案*/
        String cabinHql = "from Cabin ";
        @SuppressWarnings("unchecked")
        List<Cabin> cabinList = commonDao.queryByHql(cabinHql);
        
        for(R_Tkview tk : list){
            for(Cruise cruiseObj : cruiseList){
                if(tk.getCruiseMark().equals(cruiseObj.getMark()) ){
                    tk.setCruiseNameCn(cruiseObj.getNameCn());
                    break;
                }
            }
            for(Cabin cabinObj : cabinList){
                if(tk.getCabinMark().equals(cabinObj.getMark())){
                    tk.setCabinNameCn(cabinObj.getNameCn());
                    tk.setVolume(cabinObj.getVolume());
                    break;
                }
            }
        }
        
        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", count);
        mapContent.put("rows", list);

        return ReturnUtil.returnMap(1, mapContent);
    }

}