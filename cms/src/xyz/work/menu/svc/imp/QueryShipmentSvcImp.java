package xyz.work.menu.svc.imp;

import java.util.ArrayList;
import java.util.Date;
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
import xyz.util.StringTool;
import xyz.work.base.model.Shipment;
import xyz.work.menu.svc.QueryShipmentSvc;

@Service
public class QueryShipmentSvcImp implements QueryShipmentSvc {
    @Autowired
    private CommonDao commonDao;

    @Override
    public Map<String, Object> queryShipmentList(int offset , int pageSize , String startDate , String endDate , 
                                                 String startPlace ,  String port , String mark ,
                                                 String cruise , String sort , String order) {
        String sql = "FROM shipment s ";
        sql += "WHERE 1=1 ";
        if(StringTool.isNotNull(cruise)){
            sql += "AND s.cruise = '"+ cruise +"' ";
        }
        if(StringTool.isNotNull(startPlace)){
            sql += "AND s.start_place LIKE '%"+ startPlace +"%' ";
        }
        if(StringTool.isNotNull(startDate)){
            sql += "AND s.travel_date >= '"+ startDate +"' ";
        }else{
            sql += "AND s.travel_date >= '"+ DateUtil.dateToShortString(new Date()) +"' ";
        }
        if(StringTool.isNotNull(endDate)){
            sql += "AND s.travel_date <= '"+ endDate +"' ";
        }
        if(StringTool.isNotNull(mark)){
            sql += "AND s.mark LIKE '%"+ mark +"%' ";
        }
        if(StringTool.isNotNull(port)){
            sql += "AND s.number_code IN (SELECT v.shipment FROM voyage v ";
            sql += "WHERE v.`port` IN (SELECT p.number_code FROM `port` p WHERE p.name_cn LIKE '%"+ port +"%') ";
            sql += "AND v.priority > 0 ";
            sql += "GROUP BY v.shipment) ";
        }
        if(StringTool.isNotNull(sort) && StringTool.isNotNull(order)){
            if("travelDate".contains("," + sort+ ",") && ",asc,desc,".contains("," + order + ",")){
                sql += "ORDER BY s." + sort + " " + order;
            } 
        }else{
            sql += "ORDER BY s.travel_date DESC ";
            
        }
        
        SQLQuery countQuery = commonDao.getSqlQuery("select count(*) " + sql);
        Number countNum = (Number)countQuery.uniqueResult();
        int count = countNum==null?0:countNum.intValue();
        
        String querSql = "SELECT s.iidd AS iidd,s.cruise AS cruise,s.number_code AS numberCode,";
        querSql += "s.travel_date AS travelDate,s.mark AS mark,s.travel_end_date AS travelEndDate,";
        querSql += "s.final_sale_date AS finalSaleDate,s.area AS area,s.airway AS airway,";
        querSql += "s.start_place AS startPlace,s.trip_days AS tripDays,s.detail AS detail,";
        querSql += "s.remark AS remark,s.add_date AS addDate,s.alter_date AS alterDate ";
        
        SQLQuery query = commonDao.getSqlQuery(querSql + sql);
        query.setMaxResults(pageSize);
        query.setFirstResult(offset);
        query.addScalar("iidd").addScalar("cruise").addScalar("numberCode")
        .addScalar("travelDate").addScalar("mark").addScalar("travelEndDate")
        .addScalar("finalSaleDate").addScalar("area").addScalar("airway")
        .addScalar("startPlace").addScalar("tripDays").addScalar("detail")
        .addScalar("remark").addScalar("addDate").addScalar("alterDate")
        .setResultTransformer(Transformers.aliasToBean(Shipment.class));
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

}