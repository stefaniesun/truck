package xyz.work.base.svc.imp;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
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
import xyz.util.StringTool;
import xyz.util.StringUtil;
import xyz.util.UUIDUtil;
import xyz.work.base.model.Airway;
import xyz.work.base.model.Area;
import xyz.work.base.model.Cabin;
import xyz.work.base.model.Cruise;
import xyz.work.base.model.Port;
import xyz.work.base.model.Shipment;
import xyz.work.base.model.Trip;
import xyz.work.base.model.Voyage;
import xyz.work.base.svc.ShipmentSvc;
import xyz.work.core.model.LogWork;
import xyz.work.core.model.PlanWork;
import xyz.work.resources.model.Tkview;


@Service
public class ShipmentSvcImp implements ShipmentSvc {
    @Autowired
    private CommonDao commonDao;
    
    @Autowired
    PossessorUtil possessorUtil;

    @Override
    public Map<String, Object> queryShipmentList(int offset,
                                                 int pageSize,
                                                 String cruise,
                                                 String airway,
                                                 Date startDate,
                                                 Date endDate,
                                                 String mark,
                                                 String sort,
                                                 String order) {

        StringBuffer shipSql = new StringBuffer("from Shipment s where 1 = 1 ");
        if (StringTool.isNotNull(cruise)) {
            shipSql.append("and s.cruise = '" + cruise + "' ");
        }
        if (StringTool.isNotNull(airway)) {
            shipSql.append("and s.airway = '" + airway + "' ");
        }
        if (startDate != null && !"".equals(startDate)) {
            shipSql.append("and s.travelDate >= '" + DateUtil.dateToShortString(startDate) + "' ");
        }else {
            shipSql.append("and s.travelDate >= '"+DateUtil.dateToShortString(new Date())+"' ");
        }
        
        if (endDate != null && !"".equals(endDate)) {
            shipSql.append("and s.travelDate <= '" + DateUtil.getDateEndForQuery(endDate) + "' ");
        }
        if(StringTool.isNotNull(mark)){
            shipSql.append("and s.mark like '%" + mark + "%' ");
        }
        
        if (StringTool.isNotNull(sort) && StringTool.isNotNull(order)) {
            if (Constant.shipment_content_all_status.contains("," + sort+ ",") && ",asc,desc,".contains("," + order + ",")) {
                shipSql.append("order by s." + sort + " " + order);
            } 
        }else {
            shipSql.append("order by s.travelDate Asc ");
        }
        
        Query countQuery = commonDao.getQuery("select count(*) " + shipSql.toString());
        Number countNum = (Number)countQuery.uniqueResult();
        int count = countNum == null ? 0 : countNum.intValue();

        Query query = commonDao.getQuery(shipSql.toString());
        query.setMaxResults(pageSize);
        query.setFirstResult(offset);
        @SuppressWarnings("unchecked")
        List<Shipment> list = query.list();
       
        List<String> cruiseNumber = new ArrayList<String>();
        List<String> areaNumberCode = new ArrayList<String>();
        List<String> airwayNumebrCodeList = new ArrayList<>();
        for (Shipment s : list) {
            areaNumberCode.add(s.getArea());
            cruiseNumber.add(s.getCruise());
            airwayNumebrCodeList.add(s.getAirway());
        }
        
        /*邮轮档案*/
        String cruiseSql = "SELECT c.number_code,c.name_cn FROM cruise c WHERE c.number_code IN ("+StringTool.listToSqlString(cruiseNumber)+")";
        @SuppressWarnings("unchecked")
        List<Object[]> cruiseList = commonDao.getSqlQuery(cruiseSql).list();
        
        /*航区*/
        String areaSql = "SELECT d.number_code,d.name_cn FROM area d WHERE d.number_code IN ("+StringTool.listToSqlString(areaNumberCode)+")";
        @SuppressWarnings("unchecked")
        List<Object[]> araList = commonDao.getSqlQuery(areaSql).list();
        
        if(cruiseList != null && cruiseList.size() > 0){
            for(Shipment shipObj : list){
                for(Object[] cruiseObj : cruiseList){
                    if(cruiseObj[0].equals(shipObj.getCruise())){
                        shipObj.setCruiseNameCn(cruiseObj[1].toString());
                        break;
                    }
                }
            }
        }
        if(araList != null && araList.size() > 0){
            for(Shipment shipObj : list){
                for(Object[] d : araList){
                    if(d[0].equals(shipObj.getArea())){
                        shipObj.setAreaNameCn(d[1].toString());
                        break;
                    }
                }
            }
        }

        String airwaySql = "select number_code,name_cn from airway where number_code in ("+StringTool.listToSqlString(airwayNumebrCodeList)+")";
        
        @SuppressWarnings("unchecked")
        List<Object[]> airwayList = commonDao.getSqlQuery(airwaySql).list();
        for(Shipment shipObj : list){
            for (Object[] airwayObj : airwayList) {
                if (StringTool.isNotNull(shipObj.getAirway())) {
                    if (shipObj.getAirway().equals((String)airwayObj[0])) {
                        shipObj.setAirwayNameCn((String)airwayObj[1]);
                        break;
                    }
                }
            }
        }
        
        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", count);
        mapContent.put("rows", list);

        return ReturnUtil.returnMap(1, mapContent);
    }

    @Override
    public Map<String, Object> addShipment(String cruise,
                                           String area,
                                           String mark,
                                           String airway,
                                           String travelDate,
                                           String finalSaleDate,
                                           String startPlace,
                                           String remark) {
        
        MyRequestUtil.decidePowerIsAll();
        
        if(StringTool.isEmpty(cruise)) {
            return ReturnUtil.returnMap(0, ConstantMsg.cruise_null);
        }
        if(StringTool.isEmpty(area)) {
            return ReturnUtil.returnMap(0, ConstantMsg.area_null);
        }
        if(StringTool.isEmpty(airway)) {
            return ReturnUtil.returnMap(0, ConstantMsg.airway_null);
        }
        if(StringTool.isEmpty(mark)) {
            return ReturnUtil.returnMap(0, ConstantMsg.shipment_mark_null);
        }
        if(StringTool.isEmpty(travelDate)) {
            return ReturnUtil.returnMap(0, ConstantMsg.shipment_travelDate_null);
        }
        if(StringTool.isEmpty(finalSaleDate)) {
            return ReturnUtil.returnMap(0, ConstantMsg.shipment_finalSaleDate_null);
        }
        
        Date travel = DateUtil.shortStringToDate(travelDate);
        Date finalSale = DateUtil.stringToDate(finalSaleDate);
        
        if (finalSale == null && StringTool.isNotNull(finalSaleDate)) {
            finalSale = DateUtil.shortStringToDate(finalSaleDate);
        }
        
        Date date = new Date();
        if (travel.getTime() - date.getTime() <= 0) {
            return ReturnUtil.returnMap(0, ConstantMsg.shipment_travel_after);
        }
        long times = finalSale.getTime() - travel.getTime();
        long days = (times / (1000 * 60 * 60 * 24)) + 1;
        if (days > 20) {
            return ReturnUtil.returnMap(0, ConstantMsg.shipment_finalSale_after);
        }

        String dateSql = "from Shipment s WHERE s.cruise='" + cruise + "'";
        @SuppressWarnings("unchecked")
        List<Shipment> dateList = commonDao.queryByHql(dateSql);
        if (dateList != null && dateList.size() > 0) {
            for (Shipment ship : dateList) {
                String dated = DateUtil.dateToShortString(ship.getTravelDate());
                String dateStr = DateUtil.dateToShortString(travel);
                if (dated.equals(dateStr)) {
                    return ReturnUtil.returnMap(0, ConstantMsg.shipment_travel_equal);
                }
            }
        }
        
        Airway airwayObj = (Airway)commonDao.getObjectByUniqueCode("Airway", Constant.numberCode,airway);
        if (airwayObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.airway_null);
        }
        
        if(airwayObj.getDays()==null){
            return ReturnUtil.returnMap(0, "关联航线天数未设置");
        }
        
        int count = airwayObj.getDays().intValue();
        
        Calendar calendar  = Calendar.getInstance();
        calendar.setTime(travel);
        if(count > 0){
            calendar.add(Calendar.DATE, count-1);
        }
        Date calendarTravelEndDate = calendar.getTime();
        
        Shipment shipment = new Shipment();
        shipment.setCruise(cruise);
        shipment.setArea(area);
        shipment.setNumberCode(StringUtil.get_new_shipment_num());
        shipment.setMark(mark);
        shipment.setAirway(airway);
        shipment.setTravelDate(travel);
        shipment.setTravelEndDate(calendarTravelEndDate);
        shipment.setFinalSaleDate(finalSale);
        shipment.setStartPlace(startPlace);
        shipment.setTripDays(count);
        shipment.setRemark(remark);
        shipment.setAddDate(date);
        shipment.setAlterDate(date);
        commonDao.save(shipment);
        
        String portHql = "from Port p where p.nameCn = '海上巡游' ";
        @SuppressWarnings("unchecked")
        List<Port> portList = commonDao.queryByHql(portHql);
        Port portObj = portList.get(0);
        
        Cruise cruiseObj = (Cruise)commonDao.getObjectByUniqueCode("Cruise", "numberCode", cruise);
        
        String tripHql = "from Trip t where t.airway = '"+airway+"' order by t.priority asc";
        @SuppressWarnings("unchecked")
        List<Trip> tripList = commonDao.queryByHql(tripHql);
        for (Trip trip : tripList) {
            Voyage voyage = new Voyage();
            voyage.setNumberCode(UUIDUtil.getUUIDStringFor32());
            voyage.setShipment(shipment.getNumberCode());
            voyage.setPriority(trip.getPriority());
            voyage.setTime(trip.getTime());
            voyage.setTimeType(trip.getTimeType());
            voyage.setPort(trip.getPort());
            if(portObj.getNumberCode().equals(trip.getPort())){
                voyage.setImages(cruiseObj.getImages());
            }else{
                voyage.setImages(trip.getImages());
            }
            voyage.setDetail(trip.getDetail());
            voyage.setAddDate(date);
            voyage.setAlterDate(date);
            commonDao.save(voyage);
        }
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> editShipment(String numberCode,
                                            String finalSaleDate,
                                            String startPlace,
                                            String remark) {
        
        MyRequestUtil.decidePowerIsAll();
        
        if (StringTool.isEmpty(numberCode)) {
            return ReturnUtil.returnMap(0, ConstantMsg.shipment_numberCode_null);
        }
        if (finalSaleDate == null || "".equals(finalSaleDate)) {
            return ReturnUtil.returnMap(0, ConstantMsg.shipment_finalSaleDate_null);
        }

        Shipment shipObj = (Shipment)commonDao.getObjectByUniqueCode("Shipment", "numberCode",numberCode);
        if (shipObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.shipment_null);
        }

        Date travelEnd = shipObj.getTravelDate();
        Date finalSale = DateUtil.stringToDate(finalSaleDate);
        
        if (finalSale == null && StringTool.isNotNull(finalSaleDate)) {
            finalSale = DateUtil.shortStringToDate(finalSaleDate);
        }
        
        Date startDate = shipObj.getTravelDate();
        long times = travelEnd.getTime() - startDate.getTime();
        long days = (times / (1000 * 60 * 60 * 24)) + 1;
        if (days > 20) {
            return ReturnUtil.returnMap(0, ConstantMsg.shipment_travel_after);
        }
        if (StringTool.isNotNull(finalSaleDate)) {
            if (finalSale != null && shipObj.getTravelDate() != null
                && finalSale.compareTo(shipObj.getTravelDate()) > 0) {
                return ReturnUtil.returnMap(0, ConstantMsg.shipment_finalSale_after);
            }
        }
        
        Date date = new Date();
        
        shipObj.setFinalSaleDate(finalSale);
        shipObj.setStartPlace(startPlace);
        shipObj.setRemark(remark);
        shipObj.setAlterDate(date);
        commonDao.update(shipObj);
        
        return ReturnUtil.returnMap(1, null);
    }
    
    public Map<String ,Object> setShipmentAirway(String numberCode ,String airway){
        
        MyRequestUtil.decidePowerIsAll();
        
        if(StringTool.isEmpty(numberCode)){
            return ReturnUtil.returnMap(0, ConstantMsg.shipment_numberCode_null);
        }
        if(StringTool.isEmpty(airway)){
            return ReturnUtil.returnMap(0, ConstantMsg.airway_null);
        }
        
        Shipment shipment = (Shipment)commonDao.getObjectByUniqueCode(Constant.shipment, Constant.numberCode, numberCode);
        if (shipment == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.shipment_null);
        }
        
        String deleteVoyageSql = "delete from voyage where shipment = '"+numberCode+"' ";
        commonDao.getSqlQuery(deleteVoyageSql).executeUpdate();
        
        Airway airwayObj = (Airway)commonDao.getObjectByUniqueCode(Constant.airway, Constant.numberCode, airway);
        if (airwayObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.airway_null);
        }
        
        String portHql = "from Port p where p.nameCn = '海上巡游' ";
        @SuppressWarnings("unchecked")
        List<Port> portList = commonDao.queryByHql(portHql);
        Port portObj = portList.get(0);
        
        Cruise cruiseObj = (Cruise)commonDao.getObjectByUniqueCode("Cruise", "numberCode", shipment.getCruise());
        
        Date date = new Date();
        
        String tripHql = "from Trip t where t.airway = '"+airway+"' order by t.priority asc";
        @SuppressWarnings("unchecked")
        List<Trip> tripList = commonDao.queryByHql(tripHql);
        for (Trip trip : tripList) {
            Voyage voyage = new Voyage();
            voyage.setNumberCode(UUIDUtil.getUUIDStringFor32());
            voyage.setShipment(numberCode);
            voyage.setTime(trip.getTime());
            voyage.setPriority(trip.getPriority());
            voyage.setTimeType(trip.getTimeType());
            voyage.setPort(trip.getPort());
            if(portObj.getNumberCode().equals(trip.getPort())){
                voyage.setImages(cruiseObj.getImages());
            }else{
                voyage.setImages(trip.getImages());
            }
            voyage.setDetail(trip.getDetail());
            voyage.setAddDate(date);
            voyage.setAlterDate(date);
            commonDao.save(voyage);
        }
        
        Calendar calendar  = Calendar.getInstance();
        calendar.setTime(shipment.getTravelDate());
        calendar.add(Calendar.DATE, tripList.size()-1);
        Date calendarTravelEndDate = calendar.getTime();

        shipment.setAlterDate(date);
        shipment.setAirway(airway);
        shipment.setTravelEndDate(calendarTravelEndDate);
        if(airwayObj.getDays()==null){
       	   return ReturnUtil.returnMap(0, "对应航线天数未设置!");
        }
        shipment.setTripDays(airwayObj.getDays().intValue());
        shipment.setArea(airwayObj.getArea());
        commonDao.update(shipment);
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> deleteShipment(String numberCodes) {
        
        MyRequestUtil.decidePowerIsAll();
        
        if (StringTool.isEmpty(numberCodes)) {
            return ReturnUtil.returnMap(0, ConstantMsg.delete_null);
        }
        
        String numberCode=numberCodes.split(",")[0];
        Shipment shipment=(Shipment) commonDao.getObjectByUniqueCode("Shipment", "numberCode", numberCode);

        String shipSql = "DELETE FROM shipment WHERE number_code IN ("+ StringTool.StrToSqlString(numberCodes) +")";
        commonDao.getSqlQuery(shipSql).executeUpdate();
        
        String deleteVoyageSql = "DELETE FROM voyage WHERE shipment IN ("+ StringTool.StrToSqlString(numberCodes) +")";
        commonDao.getSqlQuery(deleteVoyageSql).executeUpdate();
        
        String tkviewSql = "SELECT t.number_code FROM tkview t WHERE t.shipment IN ("+ StringTool.StrToSqlString(numberCodes) +") ";
        @SuppressWarnings("unchecked")
        List<String> tkviewList = commonDao.getSqlQuery(tkviewSql).list();
        if(tkviewList.size() > 0){
            String sql = "DELETE FROM tkview WHERE number_code IN ("+ StringTool.listToSqlString(tkviewList) + ") ";
            commonDao.getSqlQuery(sql).executeUpdate();

            String stockSql = "DELETE FROM stock WHERE tkview IN ("+ StringTool.listToSqlString(tkviewList) + ")";
            commonDao.getSqlQuery(stockSql).executeUpdate();

        }
        
        //自动更新计划任务
        PlanWork planWork=new PlanWork();
        
        planWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
        planWork.setAddDate(new Date());
        planWork.setContent(shipment.getCruise());
        planWork.setUsername("system");
        planWork.setWorkType(Constant.PLAN_TYPE_UPDATE_CRUISE);
        
        commonDao.save(planWork);
        
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> addDetail(String shipment , String detail) {
        
        MyRequestUtil.decidePowerIsAll();
        
        if (StringTool.isEmpty(shipment)) {
            return ReturnUtil.returnMap(0, ConstantMsg.shipment_numberCode_null);
        }
        Shipment shipObj = (Shipment)commonDao.getObjectByUniqueCode("Shipment", "numberCode",shipment);
        if (shipObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.shipment_null);
        }
        shipObj.setDetail(detail);
        shipObj.setAlterDate(new Date());
        commonDao.update(shipObj);

        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> queryVoyageListByShipment(String shipment) {
        
        if(StringTool.isEmpty(shipment)){
            return ReturnUtil.returnMap(0, ConstantMsg.param_error);
        }
        
        String hql = "from Voyage v where v.shipment='"+ shipment +"' order by v.priority asc";
        Query countQuery = commonDao.getQuery("select count(*) " + hql);
        Number tempCount = (Number)countQuery.uniqueResult();
        int total = tempCount==null?0:tempCount.intValue();
        
        @SuppressWarnings("unchecked")
        List<Voyage> voyageList = commonDao.queryByHql(hql);
        
        List<String> portCodeList = new ArrayList<>();
        
        for (Voyage voyage : voyageList) {
            portCodeList.add(voyage.getPort());
        }
        
        String portHql = "from Port where numberCode in ("+StringTool.listToSqlString(portCodeList)+")";
        
        @SuppressWarnings("unchecked")
        List<Port> portList = commonDao.queryByHql(portHql);
        for (Voyage voyage : voyageList) {
            for (Port port : portList) {
                if (port.getNumberCode().equals(voyage.getPort())) {
                    String portNameCn = port.getNameCn();
                    if(StringTool.isNotNull(port.getCountry())){
                        portNameCn += "("+port.getCountry()+")";
                    }
                    voyage.setPortNameCn(portNameCn);
                    voyage.setPortAddress(port.getAddress());
                    break;
                }
            }
        }
        
        Map<String ,Object> mapContent = new HashMap<String ,Object>();
        mapContent.put("total", total);
        mapContent.put("rows", voyageList);
        
        return ReturnUtil.returnMap(1, mapContent);
    }
    
    public Map<String ,Object> editShipmentVoyage(String numberCode,
                                                  String time,
                                                  int timeType,
                                                  String arrivalTime,
                                                  String leaveTime,
                                                  String detail,
                                                  String remark){
        
        MyRequestUtil.decidePowerIsAll();
        if(StringTool.isEmpty(time)){
            return ReturnUtil.returnMap(0, ConstantMsg.voyage_time_null);
        }
        if(timeType < 0 || timeType > 3){
            return ReturnUtil.returnMap(0, ConstantMsg.voyage_timeType_error);
        }
        
        arrivalTime = StringTool.isEmpty(arrivalTime)==true?"——":arrivalTime;
        leaveTime = StringTool.isEmpty(leaveTime)==true?"——":leaveTime;
        
        Voyage voyage = (Voyage)commonDao.getObjectByUniqueCode("Voyage", Constant.numberCode, numberCode);
        if(voyage == null){
            return ReturnUtil.returnMap(0, ConstantMsg.voyage_null);
        }
        voyage.setTime(time);
        voyage.setTimeType(timeType);
        voyage.setArrivalTime(arrivalTime);
        voyage.setLeaveTime(leaveTime);
        voyage.setDetail(detail);
        voyage.setRemark(remark);
        voyage.setAlterDate(new Date());
        commonDao.update(voyage);
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> getVoyageByShipment(String shipment) {
        if(StringTool.isEmpty(shipment)){
            return ReturnUtil.returnMap(0, ConstantMsg.shipment_null);
        }
        
        String hql = "from Voyage where shipment = '"+ shipment +"' order by priority ";
        @SuppressWarnings("unchecked")
        List<Voyage> voyageList = commonDao.queryByHql(hql);
        
        List<String> portCodeList = new ArrayList<String>();
        for (Voyage voyage : voyageList) {
            portCodeList.add(voyage.getPort());
        }
        
        String portHql = "from Port where numberCode in ("+ StringTool.listToSqlString(portCodeList) +")";
        @SuppressWarnings("unchecked")
        List<Port> portList = commonDao.queryByHql(portHql);
        for (Voyage voyage : voyageList) {
            for (Port port : portList) {
                if (port.getNumberCode().equals(voyage.getPort())) {
                    voyage.setPortNameCn(port.getNameCn());
                    voyage.setPortAddress(port.getAddress());
                    break;
                }
            }
        }
        
        return ReturnUtil.returnMap(1, voyageList);
    }

    @Override
    public Map<String, Object> editQuickVoyage(String voyageJson) {
        
        MyRequestUtil.decidePowerIsAll();
        
        if(StringTool.isEmpty(voyageJson)){
            return ReturnUtil.returnMap(0, ConstantMsg.param_error);
        }
        
        @SuppressWarnings("unchecked")
        List<Map<String , Object>> voyageList = JSON.toObject(voyageJson, ArrayList.class);
        Date alterDate = new Date();
        for(int k = 0; k < voyageList.size(); k++){
            Map<String, Object> map = voyageList.get(k);
            if(StringTool.isEmpty(map.get("numberCode").toString())){
                return ReturnUtil.returnMap(0, ConstantMsg.voyage_numberCode_null);
            }
            if(StringTool.isEmpty(map.get("time").toString())){
                return ReturnUtil.returnMap(0, ConstantMsg.voyage_time_null);
            }
            String numberCode = map.get("numberCode").toString();
            String time = map.get("time").toString();
            if(map.get("timeType")==null||StringTool.isEmpty(map.get("timeType").toString())){
                return ReturnUtil.returnMap(0, "航程时间类型不能为空!");
            }
            int timeType = Integer.parseInt(map.get("timeType").toString());
            if(timeType < 0 || timeType > 3){
                return ReturnUtil.returnMap(0, ConstantMsg.voyage_timeType_error);
            }
            if(StringTool.isEmpty(time)){
                return ReturnUtil.returnMap(0, ConstantMsg.voyage_time_null);
            }
            
            Voyage voyageObj = (Voyage)commonDao.getObjectByUniqueCode("Voyage", "numberCode", numberCode);
            if(voyageObj == null){
                return ReturnUtil.returnMap(0, ConstantMsg.voyage_null);
            }
            voyageObj.setTime(time);
            voyageObj.setTimeType(timeType);
            String arrivalTime = "——";
            String leaveTime = "——";
            String detail = map.get("detail").toString();
            String remark = map.get("remark").toString();
            if(StringTool.isNotNull(map.get("arrivalTime").toString())){
                arrivalTime = map.get("arrivalTime").toString();
            }
            if(StringTool.isNotNull(map.get("leaveTime").toString())){
                leaveTime = map.get("leaveTime").toString();
            }
            voyageObj.setArrivalTime(arrivalTime);
            voyageObj.setLeaveTime(leaveTime);
            voyageObj.setDetail(detail);
            voyageObj.setPriority((k+1));
            voyageObj.setRemark(remark);
            voyageObj.setAlterDate(alterDate);
            commonDao.update(voyageObj);
        }
         
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> cloneShipmentByNumberCodeOper(String numberCode ,
                                                         String travelDate ,
                                                         String mark , 
                                                         String finalSaleDate , 
                                                         String startPlace, 
                                                         String remark) {
        
        MyRequestUtil.decidePowerIsAll();
        
        if(StringTool.isEmpty(numberCode)){
            return ReturnUtil.returnMap(0, ConstantMsg.shipment_null);
        }
        if(StringTool.isEmpty(travelDate)){
            return ReturnUtil.returnMap(0, ConstantMsg.shipment_travelDate_null);
        }
        if(StringTool.isEmpty(mark)) {
            return ReturnUtil.returnMap(0, ConstantMsg.shipment_mark_null);
        }
        if(StringTool.isEmpty(finalSaleDate)) {
            return ReturnUtil.returnMap(0, ConstantMsg.shipment_finalSaleDate_null);
        }
        
        Shipment shipmentObj = (Shipment)commonDao.getObjectByUniqueCode("Shipment", "numberCode", numberCode);
        if (shipmentObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.shipment_null);
        }
        Airway airwayObj = (Airway)commonDao.getObjectByUniqueCode("Airway", Constant.numberCode,shipmentObj.getAirway());
        if (airwayObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.airway_null);
        }
        Date travel = DateUtil.shortStringToDate(travelDate);
        Date finalSale = DateUtil.stringToDate(finalSaleDate);
        
        Date date = new Date();
        if (travel.getTime() - date.getTime() <= 0) {
            return ReturnUtil.returnMap(0, ConstantMsg.shipment_travel_after);
        }
        long times = finalSale.getTime() - travel.getTime();
        long days = (times / (1000 * 60 * 60 * 24)) + 1;
        if (days > 20) {
            return ReturnUtil.returnMap(0, ConstantMsg.shipment_finalSale_after);
        }
        
        String dateSql = "from Shipment s WHERE s.cruise='" + shipmentObj.getCruise() + "'";
        @SuppressWarnings("unchecked")
        List<Shipment> dateList = commonDao.queryByHql(dateSql);
        if (dateList != null && dateList.size() > 0) {
            for (Shipment ship : dateList) {
                String dated = DateUtil.dateToShortString(ship.getTravelDate());
                String dateStr = DateUtil.dateToShortString(travel);
                if (dated.equals(dateStr)) {
                    return ReturnUtil.returnMap(0, ConstantMsg.shipment_travel_equal);
                }
            }
        }
        
        Calendar calendar  = Calendar.getInstance();
        calendar.setTime(travel);
        calendar.add(Calendar.DATE, shipmentObj.getTripDays());
        Date calendarTravelEndDate = calendar.getTime();
        
        /*克隆航期*/
        Shipment shipment = new Shipment();
        shipment.setNumberCode(StringUtil.get_new_shipment_num());
        shipment.setCruise(shipmentObj.getCruise());
        shipment.setArea(shipmentObj.getArea());
        shipment.setMark(mark);
        shipment.setAirway(shipmentObj.getAirway());
        shipment.setTravelDate(travel);
        shipment.setTravelEndDate(calendarTravelEndDate);
        shipment.setFinalSaleDate(finalSale);
        shipment.setStartPlace(startPlace);
        shipment.setTripDays(shipmentObj.getTripDays());
        shipment.setRemark(remark);
        shipment.setAddDate(date);
        shipment.setAlterDate(date);
        commonDao.save(shipment);
        
        /*克隆行程(标题、抵达时间、起航时间未克隆)*/
        String voyageHql = "from Voyage v where v.shipment = '"+ numberCode +"' order by v.priority";
        @SuppressWarnings("unchecked")
        List<Voyage> voyageList = commonDao.queryByHql(voyageHql);
        int tempDay = 1;
        for (Voyage voyage : voyageList) {
            Voyage voyageObj = new Voyage();
            voyageObj.setNumberCode(UUIDUtil.getUUIDStringFor32());
            voyageObj.setShipment(shipment.getNumberCode());
            voyageObj.setPriority(voyage.getPriority());
            voyageObj.setTime("第"+ tempDay +"天");
            voyageObj.setTimeType(voyage.getTimeType());
            voyageObj.setPort(voyage.getPort());
            //voyageObj.setDetail(voyage.getDetail());
            //voyageObj.setRemark(voyage.getRemark());
            voyageObj.setAddDate(date);
            voyageObj.setAlterDate(date);
            commonDao.save(voyageObj);
            tempDay++;
        }
        
        return ReturnUtil.returnMap(1, null);
    }
    
    @Override
	public Map<String, Object> quickCreateTkviewOper(String numberCode) {
		
		Shipment shipment=(Shipment) commonDao.getObjectByUniqueCode("Shipment", "numberCode", numberCode);
		if(shipment==null){
			  return ReturnUtil.returnMap(0, ConstantMsg.shipment_null);
		}
		
		String cruise = shipment.getCruise();
		if(cruise==null||"".equals(cruise)){
			  return ReturnUtil.returnMap(0, ConstantMsg.cruise_null);
		}
		
	   String hql="from Cabin where cruise='"+ cruise +"' ";
	   @SuppressWarnings("unchecked")
	   List<Cabin> cabins=commonDao.getQuery(hql).list();
	   
	   Date date=new Date();
	   for(Cabin cabin:cabins){
		   	Tkview tkview = new Tkview();
	        tkview.setNumberCode(StringUtil.get_new_tkview());
	        tkview.setAddDate(date);
	        tkview.setAlterDate(date);
	        tkview.setCabin(cabin.getNumberCode());
	        tkview.setVolume(cabin.getVolume());
	        tkview.setCruise(cruise);
	        tkview.setNameCn('['+DateUtil.dateToShortString(shipment.getTravelDate())+']'+cabin.getNameCn());
	        tkview.setMark("");
	        tkview.setRemark("");
	        tkview.setShipment(shipment.getNumberCode());
	        tkview.setShipmentMark(shipment.getMark());
	        tkview.setShipmentTravelDate(shipment.getTravelDate());
	        commonDao.save(tkview);
	        
	        possessorUtil.savePossessorRelates(Constant.relate_type_tkview, tkview.getNumberCode());

	        LogWork logWork = new LogWork();
	        logWork.setAddDate(new Date());
	        logWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
	        logWork.setValue(tkview.getNumberCode());
	        logWork.setTableName("tkview");
	        logWork.setRemark("快捷添加单品【"+tkview.getNameCn()+"】");
	        logWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
	        commonDao.save(logWork);
	        
	   }
		
	   return ReturnUtil.returnMap(1, null);
	}

	@Override
	public Map<String, Object> addMoreShipment(String cruise,
											   String area,
											   String airway,
											   String startPlace,
											   String shipmentJsonStr) {
		Cruise cruiseObj = (Cruise)commonDao.getObjectByUniqueCode("Cruise", "numberCode", cruise);
		if (cruiseObj == null) {
			return ReturnUtil.returnMap(0, ConstantMsg.cruise_null);
		}
		
		Area areaObj = (Area)commonDao.getObjectByUniqueCode("Area", "numberCode", area);
		if (areaObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.area_null);			
		}
		
		Airway airwayObj = (Airway)commonDao.getObjectByUniqueCode("Airway", "numberCode", airway);
		if (airwayObj == null) {
		    return ReturnUtil.returnMap(0, ConstantMsg.airway_null);			
		}
		
		String tripHql = "from Trip t where t.airway = '"+airway+"' order by t.priority asc";
        @SuppressWarnings("unchecked")
        List<Trip> tripList = commonDao.queryByHql(tripHql);
        
		@SuppressWarnings("unchecked")
		List<HashMap<String, Object>> shipmentList = JSON.toObject(shipmentJsonStr, ArrayList.class);
		for (HashMap<String, Object> shipment : shipmentList) {
			String mark = (String)shipment.get("mark");
			String travelDate = (String)shipment.get("travelDate");
			String finalSaleDate =(String)shipment.get("finalSaleDate");
			String remark = (String)shipment.get("remark");
			Date travel = DateUtil.shortStringToDate(travelDate);
			Date finalSale = DateUtil.stringToDate(finalSaleDate);
			if (finalSale == null && StringTool.isNotNull(finalSaleDate)) {
		         finalSale = DateUtil.shortStringToDate(finalSaleDate);
		    }
			Date date = new Date();
			if (travel.getTime() - date.getTime() <= 0) {
		         return ReturnUtil.returnMap(0, ConstantMsg.shipment_travel_after);
		    }
			long times = finalSale.getTime() - travel.getTime();
		    long days = (times / (1000 * 60 * 60 * 24)) + 1;
		    if (days > 20) {
		         return ReturnUtil.returnMap(0, ConstantMsg.shipment_finalSale_after);
		    }
		    String dateSql = "from Shipment s WHERE s.cruise='" + cruise + "'";
		    @SuppressWarnings("unchecked")
		    List<Shipment> dateList = commonDao.queryByHql(dateSql);
		    if (dateList != null && dateList.size() > 0) {
		        for (Shipment ship : dateList) {
		           String dated = DateUtil.dateToShortString(ship.getTravelDate());
		           String dateStr = DateUtil.dateToShortString(travel);
		           if (dated.equals(dateStr)) {
		             return ReturnUtil.returnMap(0, ConstantMsg.shipment_travel_equal);
		           }
		        }
		    }
		    String tripSql = "select count(*) from trip where airway = '"+ airwayObj.getNumberCode() +"'";
		    Number countNum = (Number)commonDao.getSqlQuery(tripSql).uniqueResult();
		    int count = countNum == null ? 0 : countNum.intValue();
		        
		    Calendar calendar  = Calendar.getInstance();
		    calendar.setTime(travel);
		    calendar.add(Calendar.DATE, count-1);
		        
	        Date calendarTravelEndDate = calendar.getTime();
			Shipment shipmentObj = new Shipment();
			shipmentObj.setAddDate(date);
			shipmentObj.setAirway(airway);
			shipmentObj.setAlterDate(date);
			shipmentObj.setArea(area);
			shipmentObj.setCruise(cruise);
			shipmentObj.setFinalSaleDate(finalSale);
			shipmentObj.setMark(mark);
			shipmentObj.setNumberCode(StringUtil.get_new_shipment_num());
			shipmentObj.setRemark(remark);
			shipmentObj.setStartPlace(startPlace);
			shipmentObj.setTravelDate(travel);
			shipmentObj.setTravelEndDate(calendarTravelEndDate);
			shipmentObj.setTripDays(count);
			commonDao.save(shipmentObj);
			
	        for (Trip trip : tripList) {
	            Voyage voyage = new Voyage();
	            voyage.setNumberCode(UUIDUtil.getUUIDStringFor32());
	            voyage.setShipment(shipmentObj.getNumberCode());
	            voyage.setTime(trip.getTime());
	            voyage.setTimeType(trip.getTimeType());
	            voyage.setPort(trip.getPort());
	            voyage.setAddDate(date);
	            voyage.setAlterDate(date);
	            commonDao.save(voyage);
	        }
		 }
		
		 return ReturnUtil.returnMap(1, null);
	}

    @Override
    public Map<String, Object> getTkiewAndStocCount(String shipments) {
        String tkviewSql = "SELECT t.number_code FROM tkview t WHERE t.shipment IN ("+ StringTool.StrToSqlString(shipments) +") ";
        @SuppressWarnings("unchecked")
        List<String> tkviewList = commonDao.getSqlQuery(tkviewSql).list();
        
        String stockSql = "SELECT s.number_code FROM stock s WHERE s.tkview IN ("+ StringTool.listToSqlString(tkviewList) +") ";
        @SuppressWarnings("unchecked")
        List<String> stockList = commonDao.getSqlQuery(stockSql).list();
        
        Map<String, Integer> map = new HashMap<>();
        map.put("tkview", tkviewList.size());
        map.put("stock", stockList.size());
        
        return ReturnUtil.returnMap(1, map);
    }
	
    @Override
    public Map<String, Object> queryShipmentGroupCruise(Date startDate) {
        
        String sql = "SELECT s.cruise,c.name_cn,COUNT(s.iidd) ";
        sql += "FROM shipment s ";
        sql += "LEFT JOIN cruise c ";
        sql += "ON s.cruise = c.number_code ";
        sql += "WHERE s.travel_date >= NOW() ";
        sql += "GROUP BY s.cruise ";
        @SuppressWarnings("unchecked")
        List<Object[]> list = commonDao.getSqlQuery(sql).list();

        return ReturnUtil.returnMap(1, list);
    }

}