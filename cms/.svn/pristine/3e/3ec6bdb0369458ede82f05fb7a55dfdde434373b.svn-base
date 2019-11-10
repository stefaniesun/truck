package xyz.work.goal.svc.imp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.ReturnUtil;
import xyz.util.ConstantMsg;
import xyz.util.StringTool;
import xyz.util.UUIDUtil;
import xyz.work.goal.model.Record;
import xyz.work.goal.svc.RecordSvc;

@Service
public class RecordSvcImp implements RecordSvc {
    @Autowired
    private CommonDao commonDao;

    @Override
    public Map<String, Object> queryRersonList(int offset , int pageSize , String content) {
        String hql = "from Record r where 1=1 ";
        if(StringTool.isNotNull(content)){
            hql += "and r.content like '%"+ content +"%' ";
        }
        hql += "order by r.alterDate ";
        
        Query countQuery = commonDao.getQuery("select count(*) " + hql);
        Number countNum = (Number)countQuery.uniqueResult();
        int count = countNum == null ? 0 : countNum.intValue();

        Query query = commonDao.getQuery(hql.toString());
        query.setMaxResults(pageSize);
        query.setFirstResult(offset);
        @SuppressWarnings("unchecked")
        List<Record> list = query.list();
        
        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", count);
        mapContent.put("rows", list);

        return ReturnUtil.returnMap(1, mapContent);
    }

    @Override
    public Map<String, Object> addRerson(String content, String remark) {
        if(StringTool.isEmpty(content)){
            return ReturnUtil.returnMap(0, "会议内容不能为空!");
        }
        
        Date addDate = new Date();
        Record recordObj = new Record();
        recordObj.setNumberCode(UUIDUtil.getUUIDStringFor32());
        recordObj.setContent(content);
        recordObj.setRemark(remark);
        recordObj.setAddDate(addDate);
        recordObj.setAlterDate(addDate);
        commonDao.save(recordObj);
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> editRerson(String numberCode , String content, String remark) {
        if(StringTool.isEmpty(numberCode)){
            return ReturnUtil.returnMap(0, "会议对象选择错误!");
        }
        if(StringTool.isEmpty(content)){
            return ReturnUtil.returnMap(0, "会议内容不能为空!");
        }
        
        Record recordObj = (Record)commonDao.getObjectByUniqueCode("Record", "numberCode", numberCode);
        if (recordObj == null) {
            return ReturnUtil.returnMap(0, "会议记录选择错误!");
        }
        recordObj.setContent(content);
        recordObj.setRemark(remark);
        recordObj.setAlterDate(new Date());
        commonDao.update(recordObj);
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> deleteRerson(String numberCodes) {
        
        if(StringTool.isEmpty(numberCodes)){
            return ReturnUtil.returnMap(0, "会议对象选择错误!");
        }
        
        String sql = "DELETE FROM record WHERE number_code IN ("+ StringTool.StrToSqlString(numberCodes) +") ";
        commonDao.getSqlQuery(sql).executeUpdate();
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> addDetails(String numberCode , String details) {
        if (StringTool.isEmpty(numberCode)) {
            return ReturnUtil.returnMap(0, ConstantMsg.cruise_numberCode_null);
        }
        Record recordObj = (Record)commonDao.getObjectByUniqueCode("Record", "numberCode", numberCode);
        if (recordObj == null) {
            return ReturnUtil.returnMap(0, "会议记录选择错误!");
        }
        recordObj.setDetails(details);
        commonDao.update(recordObj);

        return ReturnUtil.returnMap(1, null);
    }

}
