package xyz.work.base.svc.imp;


import java.math.BigDecimal;
import java.util.ArrayList;
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
import xyz.util.StringTool;
import xyz.util.UUIDUtil;
import xyz.work.base.model.Airway;
import xyz.work.base.model.Port;
import xyz.work.base.model.Trip;
import xyz.work.base.svc.AirwaySvc;


@Service
public class AirwaySvcImp implements AirwaySvc {

    @Autowired
    private CommonDao commonDao;

    @Override
    public Map<String, Object> queryAirwayList(int offset , int pageSize , String nameCn ,
                                               BigDecimal days , BigDecimal nights , String area ,
                                               String sort , String order) {
        StringBuffer sql = new StringBuffer("from Airway t where 1=1 ");
        if(StringTool.isNotNull(nameCn)){
            sql.append("and t.nameCn like '%" + nameCn + "%' ");
        }
        if(StringTool.isNotNull(area)){
            sql.append("and t.area = '" + area + "' ");
        }
        if(sort != null && !"".equals(sort) && order != null && !"".equals(order)){
            if(Constant.airway_content_all_status.contains("," + sort+ ",") && ",asc,desc,".contains("," + order + ",")){
                sql.append("order by t." + sort + " " + order );
            } 
        }else{
            sql.append("order by t.addDate desc ");
        }

        Query countQuery = commonDao.getQuery("select count(*) " + sql.toString());
        Number countNum = (Number)countQuery.uniqueResult();
        int count = countNum == null ? 0 : countNum.intValue();

        Query query = commonDao.getQuery(sql.toString());
        query.setMaxResults(pageSize);
        query.setFirstResult(offset);
        @SuppressWarnings("unchecked")
        List<Airway> list = query.list();

        List<String> areaNumber = new ArrayList<String>();
        for(Airway w : list){
            areaNumber.add(w.getArea());
        }

        String areaSql = "SELECT a.number_code,a.name_cn FROM area a WHERE a.number_code IN ("
                         + StringTool.listToSqlString(areaNumber) + ")";
        @SuppressWarnings("unchecked")
        List<Object[]> areaList = commonDao.getSqlQuery(areaSql).list();
        if(areaList != null && areaList.size() > 0){
            for(Airway w : list){
                for(Object[] obj : areaList){
                    if(obj[0].equals(w.getArea())){
                        w.setAreaNameCn(obj[1].toString());
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
    public Map<String, Object> addAirway(String area , String nameCn , String mark , BigDecimal days ,
                                         BigDecimal nights , String remark) {

        MyRequestUtil.decidePowerIsAll();

        if(StringTool.isEmpty(area)){
            return ReturnUtil.returnMap(0, ConstantMsg.area_null);
        }
       /* if(StringTool.isEmpty(mark)){
            return ReturnUtil.returnMap(0, "航线代码不能为空!");
        }*/
        if(StringTool.isEmpty(nameCn)){
            return ReturnUtil.returnMap(0, ConstantMsg.airway_name_null);
        }

        Date date = new Date();
        Airway airwayObj = new Airway();
        airwayObj.setArea(area);
        airwayObj.setNumberCode(UUIDUtil.getUUIDStringFor32());
        airwayObj.setNameCn(nameCn);
        airwayObj.setMark(mark);
        airwayObj.setDays(days);
        airwayObj.setNights(nights);
        airwayObj.setRemark(remark);
        airwayObj.setAddDate(date);
        airwayObj.setAlterDate(date);
        commonDao.save(airwayObj);
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> editAirway(String area , String numberCode , String nameCn , String mark ,
                                          BigDecimal days , BigDecimal nights , String remark) {

        MyRequestUtil.decidePowerIsAll();

        if(StringTool.isEmpty(area)){
            return ReturnUtil.returnMap(0, ConstantMsg.area_null);
        }
        if(StringTool.isEmpty(numberCode)){
            return ReturnUtil.returnMap(0, ConstantMsg.airway_null);
        }
        /*if(StringTool.isEmpty(mark)){
            return ReturnUtil.returnMap(0, "航线代码不能为空!");
        }*/
        if(StringTool.isEmpty(nameCn)){
            return ReturnUtil.returnMap(0, ConstantMsg.airway_name_null);
        }

        Airway airwayObj = (Airway)commonDao.getObjectByUniqueCode("Airway", "numberCode", numberCode);
        if (airwayObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.airway_null);
        }
        airwayObj.setArea(area);
        airwayObj.setNameCn(nameCn);
        airwayObj.setMark(mark);
        airwayObj.setDays(days);
        airwayObj.setNights(nights);
        airwayObj.setRemark(remark);
        airwayObj.setAlterDate(new Date());
        commonDao.save(airwayObj);

        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> deleteAirway(String numberCodes) {

        MyRequestUtil.decidePowerIsAll();

        if(StringTool.isEmpty(numberCodes)){
            return ReturnUtil.returnMap(0, ConstantMsg.delete_null);
        }
        String sql = "delete from airway where number_code in ("+ StringTool.StrToSqlString(numberCodes) +")";
        commonDao.getSqlQuery(sql).executeUpdate();

        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> getTripByAirway(String airway) {
        if(StringTool.isEmpty(airway)){
            return ReturnUtil.returnMap(0, ConstantMsg.airway_null);
        }
        
        String hql = "from Trip where airway = '"+ airway +"' order by priority ";
        @SuppressWarnings("unchecked")
        List<Trip> tripList = commonDao.queryByHql(hql);
        
        List<String> portCodeList = new ArrayList<String>();
        for (Trip voyage : tripList) {
            portCodeList.add(voyage.getPort());
        }
        
        String portHql = "from Port where numberCode in ("+ StringTool.listToSqlString(portCodeList) +")";
        @SuppressWarnings("unchecked")
        List<Port> portList = commonDao.queryByHql(portHql);
        for (Trip trip : tripList) {
            for (Port port : portList) {
                if (port.getNumberCode().equals(trip.getPort())) {
                    trip.setPortNameCn(port.getNameCn());
                    trip.setPortAddress(port.getAddress());
                    break;
                }
            }
        }
        
        return ReturnUtil.returnMap(1, tripList);
    }

}