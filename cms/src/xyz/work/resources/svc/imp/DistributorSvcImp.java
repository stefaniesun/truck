package xyz.work.resources.svc.imp;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.ReturnUtil;
import xyz.util.StringTool;
import xyz.util.UUIDUtil;
import xyz.work.resources.model.Distributor;
import xyz.work.resources.model.DistributorTag;
import xyz.work.resources.svc.DistributorSvc;


@Service
public class DistributorSvcImp implements DistributorSvc {

    @Autowired
    private CommonDao commonDao;
    
    @Override
    public Map<String, Object> queryDistributorList(int offset , int pagesize , String numberCode ,
                                                    String name) {
        StringBuffer hql = new StringBuffer();
        hql.append("from Distributor where 1 = 1 ");
        
        if (StringTool.isNotNull(numberCode)) {
            hql.append(" and numberCode = '"+numberCode+"' ");
        }
        if (StringTool.isNotNull(name)) {
            hql.append(" and name like '%"+name+"%' ");
        }
        
        Query countQuery = commonDao.getQuery("select count(*) " + hql.toString());
        Number tempNumber = (Number)countQuery.uniqueResult();
        int count = tempNumber == null ? 0 : tempNumber.intValue();
        Query query = commonDao.getQuery(hql.toString());
        query.setFirstResult(offset);
        query.setMaxResults(pagesize);

        @SuppressWarnings("unchecked")
        List<Distributor> distributorList = query.list();

        Set<String> distributorTagNumberSet = new HashSet<String>();
        for (Distributor distributor : distributorList) {
            distributorTagNumberSet.add(distributor.getDistributorTag());
        }
        String distributorTagHql="from DistributorTag d where 1 = 1 and d.numberCode in  ("+ StringTool.listToSqlString(distributorTagNumberSet) + ")";
        @SuppressWarnings("unchecked")
        List<DistributorTag> distributorTagsList=commonDao.queryByHql(distributorTagHql);
        for (Distributor distributor : distributorList) {
            for (DistributorTag distributorTag : distributorTagsList) {
                if(distributor.getDistributorTag().equals(distributorTag.getNumberCode())){
                    distributor.setDistributorTagNameCn(distributorTag.getName());
                }
            }
        }
        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", count);
        mapContent.put("rows", distributorList);

        return ReturnUtil.returnMap(1, mapContent);
    }

    @Override
    public Map<String, Object> addDistributor(String name , String type , String linkUsername ,
                                              String linkPhone , String distributorTag , String remark) {
        
        if (StringTool.isEmpty(name)) {
            return ReturnUtil.returnMap(0, "分销商名称不能为空!");
        }
        if (StringTool.isEmpty(type)) {
            return ReturnUtil.returnMap(0, "分销商类型不能为空!");
        }
        if (StringTool.isEmpty(linkUsername)) {
            return ReturnUtil.returnMap(0, "联系人名字不能为空!");
        }
        if (StringTool.isEmpty(linkPhone)) {
            return ReturnUtil.returnMap(0, "联系人电话不能为空!");
        }
        if (StringTool.isEmpty(distributorTag)) {
            return ReturnUtil.returnMap(0, "分销等级不能为空!");
        }
        Date date = new Date();
        Distributor distributorObj = new Distributor();
        distributorObj.setNumberCode(UUIDUtil.getUUIDStringFor32());
        distributorObj.setName(name);
        distributorObj.setType(type);
        distributorObj.setLinkUsername(linkUsername);
        distributorObj.setLinkPhone(linkPhone);
        distributorObj.setDistributorTag(distributorTag);
        distributorObj.setRemark(remark);
        distributorObj.setAddDate(date);
        distributorObj.setAlertDate(date);
        commonDao.save(distributorObj);
        return ReturnUtil.returnMap(1,null);
    }

    @Override
    public Map<String, Object> editDistributor(String numberCode , String name , String type ,
                                               String linkUsername , String linkPhone ,
                                               String distributorTag , String remark) {

        Distributor distributorObj=(Distributor)commonDao.getObjectByUniqueCode("Distributor", "numberCode", numberCode);
        if (distributorObj == null) {
            return ReturnUtil.returnMap(0, "分销商对象不能为空");
        }
        distributorObj.setName(name);
        distributorObj.setType(type);
        distributorObj.setLinkUsername(linkUsername);
        distributorObj.setLinkPhone(linkPhone);
        distributorObj.setDistributorTag(distributorTag);
        distributorObj.setRemark(remark);
        distributorObj.setAlertDate(new Date());
        commonDao.save(distributorObj);
        return ReturnUtil.returnMap(1,null);
    }

    @Override
    public Map<String, Object> deleteDistributor(String numberCodes) {
        
        if (StringTool.isEmpty(numberCodes)) {
            return ReturnUtil.returnMap(0, "请选择要删除的对象!");
        }
        
        String sql = "DELETE FROM distributor WHERE number_code IN ("+StringTool.StrToSqlString(numberCodes)+") ";
        commonDao.getSqlQuery(sql).executeUpdate();
        return ReturnUtil.returnMap(1,null);
    }

    @Override
    public Map<String, Object> editIsEnable(String numberCode) {

        Distributor distributorObj=(Distributor)commonDao.getObjectByUniqueCode("Distributor", "numberCode", numberCode);
        int isEnable = distributorObj.getIsEnable();
        isEnable = isEnable==1?0:1;
        distributorObj.setIsEnable(isEnable);
        commonDao.update(distributorObj);
        
        return ReturnUtil.returnMap(1, null);
    }

}
