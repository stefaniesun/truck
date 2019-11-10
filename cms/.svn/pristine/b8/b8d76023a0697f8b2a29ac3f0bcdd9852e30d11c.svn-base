package xyz.work.r_base.svc.imp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.ReturnUtil;
import xyz.util.StringTool;
import xyz.work.r_base.model.R_Shipment;
import xyz.work.r_base.svc.R_ShipmentSvc;

@Service
public class R_ShipmentSvcImp implements R_ShipmentSvc {
    @Autowired
    private CommonDao commonDao;

    @Override
    public Map<String, Object> queryRShipmentList(int offset , int pageSize ,String cruiseMark , 
                                                  String mark , String areaMark , String airwayMark) {
        String shipHql = "from R_Shipment s where 1=1 ";
        /*if(StringTool.isNotNull(cruiseMark)){
            shipHql += "and s.cruiseMark like '%"+ cruiseMark +"%' ";  
        }*/
        if(StringTool.isNotNull(cruiseMark)){
            shipHql += "and s.cruise = '"+ cruiseMark +"' ";  
        }
        if(StringTool.isNotNull(mark)){
            shipHql += "and s.mark like '%"+ mark +"%' ";  
        }
        if(StringTool.isNotNull(areaMark)){
            shipHql += "and s.areaMark like '%"+ areaMark +"%' ";  
        }
        if(StringTool.isNotNull(airwayMark)){
            shipHql += "and s.mark like '%"+ airwayMark +"%' ";  
        }
        Query countQuery = commonDao.getQuery("select count(*) " + shipHql);
        Number countNum = (Number)countQuery.uniqueResult();
        int count = countNum == null ? 0 : countNum.intValue();

        Query query = commonDao.getQuery(shipHql);
        query.setMaxResults(pageSize);
        query.setFirstResult(offset);
        @SuppressWarnings("unchecked")
        List<R_Shipment> shipList = query.list();
        
        /*邮轮档案*/
        String cruiseSql = "SELECT c.number_code,c.name_cn,c.mark FROM cruise c ";
        @SuppressWarnings("unchecked")
        List<Object[]> cruiseList = commonDao.getSqlQuery(cruiseSql).list();
        
        /*航线*/
        String airwySql = "SELECT a.number_code,a.name_cn,a.mark FROM r_airway a ";
        @SuppressWarnings("unchecked")
        List<Object[]> airwayList = commonDao.getSqlQuery(airwySql).list();
        
        /*港口*/
        String portSql = "SELECT p.number_code,p.name_cn,p.mark FROM r_port p ";
        @SuppressWarnings("unchecked")
        List<Object[]> portList = commonDao.getSqlQuery(portSql).list();
        
        for(R_Shipment ship : shipList){
            for(Object[] cruise : cruiseList){
                //String cruiseNum = (String)cruise[0];
                String cruiseNameCn = (String)cruise[1];
                String cruise_mark = (String)cruise[2];
                if(ship.getCruiseMark().equals(cruise_mark)){
                    ship.setCruiseNameCn(cruiseNameCn);
                    break;
                }
            }
            for(Object[] airway : airwayList){
                //String airwayNum = (String)airway[0];
                String airwayNameCn = (String)airway[1];
                String airway_mark = (String)airway[2];
                if(ship.getAirwayMark().equals(airway_mark)){
                    ship.setAirwayNameCn(airwayNameCn);
                    break;
                }
            }
            for(Object[] port : portList){
                //String portNum = (String)port[0];
                String portNameCn = (String)port[1];
                String port_mark = (String)port[2];
                if(ship.getPortMark().equals(port_mark)){
                    ship.setPortNameCn(portNameCn);
                    break;
                }
            }
        }
        
        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", count);
        mapContent.put("rows", shipList);

        return ReturnUtil.returnMap(1, mapContent);
    }

}