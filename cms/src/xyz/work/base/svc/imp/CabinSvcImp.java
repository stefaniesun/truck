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
import xyz.work.base.model.Cabin;
import xyz.work.base.model.Cruise;
import xyz.work.base.svc.CabinSvc;
import xyz.work.resources.model.TkviewType;


@Service
public class CabinSvcImp implements CabinSvc {
    @Autowired
    CommonDao commonDao;

    @Override
    public Map<String, Object> queryCabinList(int offset , int pageSize , String cruise ,
                                              String nameCn , BigDecimal volume ,
                                              String type , String isOpen) {
        StringBuffer cabinSql = new StringBuffer("from Cabin c where 1=1 ");
        if (StringTool.isNotNull(cruise)) {
            cabinSql.append("and c.cruise = '" + cruise + "' ");
        }
        if (StringTool.isNotNull(nameCn)) {
            cabinSql.append("and c.nameCn like '%" + nameCn + "%' ");
        }
        if (volume != null && !"".equals(volume)) {
            cabinSql.append("and c.volume <= " + volume +" ");
            cabinSql.append("and c.maxVolume <= " + volume +" ");
        }
        if (StringTool.isNotNull(type)) {
            int cabinType = Integer.parseInt(type);
            cabinSql.append("and c.type =" + cabinType +" ");
        }
        if(StringTool.isNotNull(isOpen)){
            cabinSql.append("and c.isOpen = '" + isOpen + "'");
        }
        cabinSql.append("order by c.alterDate DESC ");

        Query countQuery = commonDao.getQuery("select count(*) " + cabinSql.toString());
        Number countNum = (Number)countQuery.uniqueResult();
        int count = countNum == null ? 0 : countNum.intValue();

        Query query = commonDao.getQuery(cabinSql.toString());
        query.setMaxResults(pageSize);
        query.setFirstResult(offset);
        @SuppressWarnings("unchecked")
        List<Cabin> cabinList = query.list();

        /* 邮轮档案 */
        List<String> cruiseNumber = new ArrayList<String>();
        for (Cabin cabinObj : cabinList) {
            cruiseNumber.add(cabinObj.getCruise());
        }
        String cruiseSql = "SELECT c.number_code,c.name_cn FROM cruise c WHERE c.number_code IN ("
                           + StringTool.listToSqlString(cruiseNumber) + ")";
        @SuppressWarnings("unchecked")
        List<Object[]> cruiseList = commonDao.getSqlQuery(cruiseSql).list();

        if (cruiseList != null && cruiseList.size() > 0) {
            for (Cabin cabinObj : cabinList) {
                for (Object[] cruiseObj : cruiseList) {
                    if (StringTool.isNotNull((String)cruiseObj[1]) && cruiseObj[0].equals(cabinObj.getCruise())) {
                        cabinObj.setCruiseNameCn((String)cruiseObj[1]);
                        break;
                    }
                }
            }
        }

        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", count);
        mapContent.put("rows", cabinList);

        return ReturnUtil.returnMap(1, mapContent);
    }

    @Override
    public Map<String, Object> addCabin(String cruise , String nameCn , String mark ,
                                        BigDecimal volume , BigDecimal maxVolume , int type ,
                                        String remark , String floors , String acreage ,
                                        String survey , String isOpen) {
        
        MyRequestUtil.decidePowerIsAll();
        
        if (StringTool.isEmpty(cruise)) {
            return ReturnUtil.returnMap(0, ConstantMsg.cruise_null);
        }
        if (StringTool.isEmpty(nameCn)) {
            return ReturnUtil.returnMap(0, ConstantMsg.cabin_namne_null);
        }
        if (volume.compareTo(BigDecimal.ZERO) == 0 || volume.compareTo(BigDecimal.ZERO) == -1) {
            return ReturnUtil.returnMap(0, ConstantMsg.cabin_volume_error);
        }
        if (maxVolume.compareTo(volume) == -1) {
            return ReturnUtil.returnMap(0, ConstantMsg.cabin_maxVolume_error);
        }
        if (type > 3 && type < 0) {
            return ReturnUtil.returnMap(0, ConstantMsg.cabin_type_error);
        }

        Cruise cruiseObj = (Cruise)commonDao.getObjectByUniqueCode("Cruise", "numberCode", cruise);
        if (cruiseObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.cruise_null);
        }

        Date date = new Date();
        Cabin cabinObj = new Cabin();
        cabinObj.setCruise(cruise);
        cabinObj.setNumberCode(UUIDUtil.getUUIDStringFor32());
        cabinObj.setNameCn(nameCn);
        cabinObj.setMark(mark);
        cabinObj.setVolume(volume);
        cabinObj.setMaxVolume(maxVolume);
        cabinObj.setType(type);
        cabinObj.setRemark(remark);
        cabinObj.setAddDate(date);
        cabinObj.setAlterDate(date);
        cabinObj.setFloors(floors);
        cabinObj.setAcreage(acreage);
        cabinObj.setSurvey(survey);
        cabinObj.setIsOpen(isOpen);
        commonDao.save(cabinObj);
        
        TkviewType tkviewTypeObj = new TkviewType();
        tkviewTypeObj.setNumberCode(UUIDUtil.getUUIDStringFor32());
        tkviewTypeObj.setCruise(cruise);
        tkviewTypeObj.setCabin(cabinObj.getNumberCode());
        tkviewTypeObj.setRemark(remark);
        tkviewTypeObj.setAddDate(date);
        tkviewTypeObj.setAlterDate(date);
        commonDao.save(tkviewTypeObj);

        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> editCabin(String numberCode , String cruise , String nameCn ,
                                         String mark , BigDecimal volume , BigDecimal maxVolume ,
                                         int type , String remark , String floors , String acreage , 
                                         String survey , String isOpen) {
       
        MyRequestUtil.decidePowerIsAll();
        
        if (StringTool.isEmpty(numberCode)) {
            return ReturnUtil.returnMap(0, ConstantMsg.cabin_numberCode_null);
        }
        
        if (StringTool.isEmpty(cruise)) {
            return ReturnUtil.returnMap(0, ConstantMsg.cruise_null);
        }
        Cruise cruiseObj = (Cruise)commonDao.getObjectByUniqueCode("Cruise", "numberCode", cruise);
        if (cruiseObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.cruise_null);
        }
        
        Cabin cabinObj = (Cabin)commonDao.getObjectByUniqueCode("Cabin", "numberCode", numberCode);
        if (cabinObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.cabin_null);
        }
        cabinObj.setCruise(cruise);
        cabinObj.setNameCn(nameCn);
        cabinObj.setMark(mark);
        cabinObj.setVolume(volume);
        cabinObj.setMaxVolume(maxVolume);
        cabinObj.setType(type);
        cabinObj.setRemark(remark);
        cabinObj.setAlterDate(new Date());
        cabinObj.setFloors(floors);
        cabinObj.setAcreage(acreage);
        cabinObj.setSurvey(survey);
        cabinObj.setIsOpen(isOpen);
        commonDao.save(cabinObj);
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> deleteCabin(String numberCodes) {
        
        MyRequestUtil.decidePowerIsAll();
        
        if (StringTool.isEmpty(numberCodes)) {
            return ReturnUtil.returnMap(0, ConstantMsg.delete_null);
        }
        
        String sql = "DELETE FROM cabin WHERE number_code IN ("+StringTool.StrToSqlString(numberCodes)+") ";
        commonDao.getSqlQuery(sql).executeUpdate();
        
        String deleteSql = "DELETE FROM tkview_type WHERE cabin IN ("+ StringTool.StrToSqlString(numberCodes) +") ";
        commonDao.getSqlQuery(deleteSql).executeUpdate();
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> addImages(String numberCode , String urls) {
        
        MyRequestUtil.decidePowerIsAll();
        
        if (StringTool.isEmpty(numberCode)) {
            return ReturnUtil.returnMap(0, ConstantMsg.cabin_numberCode_null);
        }
        Cabin cabinObj = (Cabin)commonDao.getObjectByUniqueCode("Cabin", "numberCode", numberCode);
        if (cabinObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.cabin_null);
        }
        cabinObj.setImages(urls);
        cabinObj.setAlterDate(new Date());
        commonDao.update(cabinObj);

        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> addQuickCabin(String cruise , String cabinJosnStr) {
        MyRequestUtil.decidePowerIsAll();
        
        if (StringTool.isEmpty(cruise)) {
            return ReturnUtil.returnMap(0, ConstantMsg.cruise_null);
        }
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> cabinList = JSON.toObject(cabinJosnStr, ArrayList.class);
        if (cabinList == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.cruise_add_qucik_json_null_error);
        }
        
        Cruise cruiseObj = (Cruise)commonDao.getObjectByUniqueCode(Constant.cruise, Constant.numberCode, cruise);
        if (cruiseObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.cruise_null);
        }
        Date date = new Date();
        for (Map<String, Object> cabin : cabinList) {
         
            int type = cabin.get("type")==null?0:Integer.valueOf(cabin.get("type").toString());
            String nameCn = (String)cabin.get("name");
            BigDecimal minVolume = new BigDecimal(cabin.get("minVolume")==""?"0":cabin.get("minVolume").toString());
            BigDecimal maxVolume = new BigDecimal(cabin.get("maxVolume")==""?"0":cabin.get("maxVolume").toString());
            String mark = (String)cabin.get("mark");
            String floors = (String)cabin.get("floors");
            String acreage = (String)cabin.get("acreage");
            String survey = (String)cabin.get("survey");
            String remark = (String)cabin.get("remark");
            String isOpen = (String)cabin.get("isOpen");
            
            Cabin cabinObj = new Cabin();
            cabinObj.setCruise(cruiseObj.getNumberCode());
            cabinObj.setNumberCode(UUIDUtil.getUUIDStringFor32());
            cabinObj.setType(type);
            cabinObj.setNameCn(nameCn);
            cabinObj.setMark(mark);
            cabinObj.setVolume(minVolume);
            cabinObj.setMaxVolume(maxVolume);
            cabinObj.setFloors(floors);
            cabinObj.setAcreage(acreage);
            cabinObj.setSurvey(survey);
            cabinObj.setRemark(remark);
            cabinObj.setAddDate(date);
            cabinObj.setAlterDate(date);
            cabinObj.setIsOpen(isOpen);
            commonDao.save(cabinObj);
            
            TkviewType tkviewTypeObj = new TkviewType();
            tkviewTypeObj.setNumberCode(UUIDUtil.getUUIDStringFor32());
            tkviewTypeObj.setCruise(cruiseObj.getNumberCode());
            tkviewTypeObj.setCabin(cabinObj.getNumberCode());
            tkviewTypeObj.setRemark(remark);
            tkviewTypeObj.setAddDate(date);
            tkviewTypeObj.setAlterDate(date);
            commonDao.save(tkviewTypeObj);
        }
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> addDetail(String numberCode , String detail) {
        MyRequestUtil.decidePowerIsAll();
        
        if (StringTool.isEmpty(numberCode)) {
            return ReturnUtil.returnMap(0, ConstantMsg.cabin_numberCode_null);
        }
        Cabin cabinObj = (Cabin)commonDao.getObjectByUniqueCode("Cabin", "numberCode",numberCode);
        if (cabinObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.cabin_null);
        }
        cabinObj.setDetail(detail);
        cabinObj.setAlterDate(new Date());
        commonDao.update(cabinObj);

        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> editIsOpen(String numberCode) {
        Cabin cabinObj = (Cabin)commonDao.getObjectByUniqueCode("Cabin", "numberCode", numberCode);
        String isOpen = cabinObj.getIsOpen();
        isOpen = "开".equals(isOpen)==true?"关":"开";
        cabinObj.setIsOpen(isOpen);
        commonDao.update(cabinObj);
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> queryCabinGroupCruise() {
        
        String sql = "SELECT s.cruise,c.name_cn,COUNT(s.iidd) ";
        sql += "FROM cabin s ";
        sql += "LEFT JOIN cruise c ";
        sql += "ON s.cruise = c.number_code ";
        sql += "GROUP BY s.cruise ";
        sql += "order by c.alter_date DESC ";
        @SuppressWarnings("unchecked")
        List<Object[]> list = commonDao.getSqlQuery(sql).list();

        return ReturnUtil.returnMap(1, list);
        
    }

}