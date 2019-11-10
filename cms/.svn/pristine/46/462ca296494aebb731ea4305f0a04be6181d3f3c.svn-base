package xyz.work.resources.svc.imp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.ReturnUtil;
import xyz.util.StringTool;
import xyz.util.UUIDUtil;
import xyz.work.resources.model.DistributorTag;
import xyz.work.resources.svc.DistributorTagSvc;

@Service
public class DistributorTagSvcImp implements DistributorTagSvc {

    @Autowired
    private CommonDao commonDao;
    
    @Override
    public Map<String, Object> queryDistributorTagList(int offset , int pagesize ,
                                                       String numberCode) {
        StringBuffer hql = new StringBuffer();
        hql.append("from DistributorTag where 1 = 1 ");
        
        if (StringTool.isNotNull(numberCode)) {
            hql.append(" and numberCode = '"+numberCode+"' ");
        }
        
        Query countQuery = commonDao.getQuery("select count(*) " + hql.toString());
        Number tempNumber = (Number)countQuery.uniqueResult();
        int count = tempNumber == null ? 0 : tempNumber.intValue();
        Query query = commonDao.getQuery(hql.toString());
        query.setFirstResult(offset);
        query.setMaxResults(pagesize);

        @SuppressWarnings("unchecked")
        List<DistributorTag> distributorTagList = query.list();

        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", count);
        mapContent.put("rows", distributorTagList);

        return ReturnUtil.returnMap(1, mapContent);
    }

    @Override
    public Map<String, Object> addDistributorTag(String name , String remark) {
        
        if (StringTool.isEmpty(name)) {
            return ReturnUtil.returnMap(0, "分销等级不能为空!");
        }
        Date date = new Date();
        DistributorTag distributorTagobj = new DistributorTag();
        distributorTagobj.setNumberCode(UUIDUtil.getUUIDStringFor32());
        distributorTagobj.setName(name);
        distributorTagobj.setRemark(remark);
        distributorTagobj.setAddDate(date);
        distributorTagobj.setAlertDate(date);
        commonDao.save(distributorTagobj);
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> editDistributorTag(String numberCode , String name , String remark) {
        
        DistributorTag distributorTagObj=(DistributorTag)commonDao.getObjectByUniqueCode("DistributorTag", "numberCode", numberCode);
        if (distributorTagObj == null) {
            return ReturnUtil.returnMap(0, "分销等级对象不能为空");
        }
        distributorTagObj.setName(name);
        distributorTagObj.setRemark(remark);
        distributorTagObj.setAlertDate(new Date());
        commonDao.save(distributorTagObj);
        return ReturnUtil.returnMap(1,null);
    }

    @Override
    public Map<String, Object> deleteDistributorTag(String numberCodes) {
        if (StringTool.isEmpty(numberCodes)) {
            return ReturnUtil.returnMap(0, "请选择要删除的对象!");
        }
        
        String sql = "DELETE FROM distributor_tag WHERE number_code IN ("+StringTool.StrToSqlString(numberCodes)+") ";
        commonDao.getSqlQuery(sql).executeUpdate();
        return ReturnUtil.returnMap(1,null);
    }

    @Override
    public Map<String, Object> queryDistributorTagListByNumberCode(String numberCode) {
        
        DistributorTag distributorTagList=(DistributorTag)commonDao.getObjectByUniqueCode("DistributorTag", "numberCode", numberCode);
        if (distributorTagList == null) {
            return ReturnUtil.returnMap(0, "分销等级对象不能为空");
        }

        return ReturnUtil.returnMap(1,distributorTagList);
    }

    @Override
    public Map<String, Object> editStrategyJsonByNumberCode(String numberCode , String ptrategyJson) {
        
        DistributorTag distributorTagList=(DistributorTag)commonDao.getObjectByUniqueCode("DistributorTag", "numberCode", numberCode);
        if (distributorTagList == null) {
            return ReturnUtil.returnMap(0, "分销等级对象不能为空");
        }
        Date date = new Date();
        distributorTagList.setAlertDate(date);
        distributorTagList.setStrategyJson(ptrategyJson);
        commonDao.save(distributorTagList);
        
        return ReturnUtil.returnMap(1,distributorTagList);
    }

}
