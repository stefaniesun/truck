package xyz.work.base.svc.imp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.MyRequestUtil;
import xyz.filter.ReturnUtil;
import xyz.util.ConstantMsg;
import xyz.util.StringTool;
import xyz.util.UUIDUtil;
import xyz.work.base.model.Area;
import xyz.work.base.svc.AreaSvc;

@Service
public class AreaSvcImp implements AreaSvc {
    @Autowired
    private CommonDao commonDao;

    @Override
    public Map<String, Object> queryAreaList(int offset , int pageSize , String nameCn) {
        String hql = "from Area a where 1=1 ";
        if (StringTool.isNotNull(nameCn)) {
            hql += " and a.nameCn like '%" + nameCn + "%'";
        }
        hql += " order by a.alterDate desc";
        
        String countHql = "select count(*) " + hql;
        Query countQuery = commonDao.getQuery(countHql);
        Number countNum = (Number)countQuery.uniqueResult();
        int count = countNum == null ? 0 : countNum.intValue();

        Query query = commonDao.getQuery(hql.toString());
        query.setMaxResults(pageSize);
        query.setFirstResult(offset);
        @SuppressWarnings("unchecked")
        List<Area> list = query.list();

        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", count);
        mapContent.put("rows", list);

        return ReturnUtil.returnMap(1, mapContent);
    }

    @Override
    public Map<String, Object> addArea(String nameCn , String remark) {
        
        MyRequestUtil.decidePowerIsAll();
        
        if(StringTool.isEmpty(nameCn)){
            return ReturnUtil.returnMap(0, ConstantMsg.area_name_null);
        }
        
        Date date = new Date();
        Area areaObj = new Area();
        areaObj.setNumberCode(UUIDUtil.getUUIDStringFor32());
        areaObj.setNameCn(nameCn);
        areaObj.setRemark(remark);
        areaObj.setAddDate(date); 
        areaObj.setAlterDate(date);
        commonDao.save(areaObj);

        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> editArea(String numberCode , String nameCn , String remark) {
        
        MyRequestUtil.decidePowerIsAll();
        
        if (StringTool.isEmpty(numberCode)) {
            return ReturnUtil.returnMap(0, ConstantMsg.area_numberCode_null);
        }
        if (StringTool.isEmpty(nameCn)) {
            return ReturnUtil.returnMap(0, ConstantMsg.area_name_null);
        }

        Area areaObj = (Area)commonDao.getObjectByUniqueCode("Area", "numberCode", numberCode);
        if (areaObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.area_null);
        }
        areaObj.setNameCn(nameCn);
        areaObj.setRemark(remark);
        areaObj.setAlterDate(new Date());
        commonDao.update(areaObj);

        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> deleteArea(String numberCodes) {
        
        MyRequestUtil.decidePowerIsAll();
        
        if (StringTool.isEmpty(numberCodes)) {
            return ReturnUtil.returnMap(0, ConstantMsg.delete_null);
        }
        
        String sql = "DELETE FROM area WHERE number_code IN ("
            + StringTool.StrToSqlString(numberCodes) + ")";
        commonDao.getSqlQuery(sql).executeUpdate();
        
        return ReturnUtil.returnMap(1, null);
    }

}