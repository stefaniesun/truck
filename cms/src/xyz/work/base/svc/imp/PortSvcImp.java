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
import xyz.work.base.model.Port;
import xyz.work.base.svc.PortSvc;

@Service
public class PortSvcImp implements PortSvc{
    @Autowired
    CommonDao commonDao;

    @Override
    public Map<String, Object> queryPortList(int offset , int pageSize , String nameCn) {
        String hql = "from Port p where 1=1 ";
        if (StringTool.isNotNull(nameCn)) {
            hql += " and p.nameCn like '%" + nameCn + "%'";
        } 
        hql += " order by p.alterDate desc";

        String countHql = "select count(*) " + hql;
        Query countQuery = commonDao.getQuery(countHql);
        Number countNum = (Number)countQuery.uniqueResult();
        int count = countNum == null ? 0 : countNum.intValue();

        Query query = commonDao.getQuery(hql.toString());
        query.setMaxResults(pageSize);
        query.setFirstResult(offset);
        @SuppressWarnings("unchecked")
        List<Port> list = query.list();

        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", count);
        mapContent.put("rows", list);

        return ReturnUtil.returnMap(1, mapContent);
    }

    @Override
    public Map<String, Object> addPort(String nameCn ,String country , String address , String details , 
                                       String remark , String images) {
        
        MyRequestUtil.decidePowerIsAll();
        
        if (StringTool.isEmpty(nameCn)) {
            return ReturnUtil.returnMap(0, ConstantMsg.port_name_null);
        }
        address = StringTool.isEmpty(address)==true?"":address;

        Date date = new Date();
        Port portObj = new Port();
        portObj.setNumberCode(UUIDUtil.getUUIDStringFor32());
        portObj.setNameCn(nameCn);
        portObj.setCountry(country);
        portObj.setAddress(address);
        portObj.setRemark(remark);
        portObj.setImages(images);
        portObj.setAddDate(date);
        portObj.setAlterDate(date);
        commonDao.save(portObj);

        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> editPort(String numberCode , String  nameCn , String country , String address ,
                                        String details , String remark , String images) {
        
        MyRequestUtil.decidePowerIsAll();
        
        if (StringTool.isEmpty(numberCode)) {
            return ReturnUtil.returnMap(0, ConstantMsg.port_numberCode_null);
        }
        if (StringTool.isEmpty(nameCn)) {
            return ReturnUtil.returnMap(0, ConstantMsg.port_name_null);
        }
        address = StringTool.isEmpty(address)==true?"":address;
        
        Port portObj = (Port)commonDao.getObjectByUniqueCode("Port", "numberCode", numberCode);
        if (portObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.port_null);
        }
        portObj.setNameCn(nameCn);
        portObj.setCountry(country);
        portObj.setAddress(address);
        portObj.setRemark(remark);
        portObj.setImages(images);
        portObj.setAlterDate(new Date());
        commonDao.update(portObj);
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> deletePort(String numberCodes) {
        
        MyRequestUtil.decidePowerIsAll();
        
        if (StringTool.isEmpty(numberCodes)) {
            return ReturnUtil.returnMap(0, ConstantMsg.delete_null);
        }

        String sql = "delete from port where number_code in ("
                     + StringTool.StrToSqlString(numberCodes) + ")";
        commonDao.getSqlQuery(sql).executeUpdate();

        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> editPortCoordinate(String numberCode , String longitude ,
                                                  String latitude) {
        MyRequestUtil.decidePowerIsAll();
        
        if (StringTool.isEmpty(numberCode)) {
            return ReturnUtil.returnMap(0, ConstantMsg.port_numberCode_null);
        }
        /*if (StringTool.isEmpty(longitude)) {
            return ReturnUtil.returnMap(0, "经度不能为空!");
        }
        if (StringTool.isEmpty(latitude)) {
            return ReturnUtil.returnMap(0, "纬度不能为空!");
        }*/
        
        
        Port portObj = (Port)commonDao.getObjectByUniqueCode("Port", "numberCode", numberCode);
        if(portObj == null){
            return ReturnUtil.returnMap(0, ConstantMsg.port_null);
        }
        portObj.setLongitude(longitude);
        portObj.setLatitude(latitude);
        commonDao.update(portObj);
        
        return ReturnUtil.returnMap(1, null);
    }

}