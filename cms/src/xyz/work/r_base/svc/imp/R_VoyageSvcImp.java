package xyz.work.r_base.svc.imp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.ReturnUtil;
import xyz.work.r_base.model.R_Voyage;
import xyz.work.r_base.svc.R_VoyageSvc;

@Service
public class R_VoyageSvcImp implements R_VoyageSvc {
    @Autowired
    private CommonDao commonDao;

    @Override
    public Map<String, Object> queryRVoyageList(int offset , int pageSize ,String shipment) {
        String voyageHql = "from R_Voyage t where 1=1 ";
        voyageHql += "and t.shipment = '"+ shipment +"' ";

        Query countQuery = commonDao.getQuery("select count(*) " + voyageHql);
        Number countNum = (Number)countQuery.uniqueResult();
        int count = countNum == null ? 0 : countNum.intValue();

        Query query = commonDao.getQuery(voyageHql);
        query.setMaxResults(pageSize);
        query.setFirstResult(offset);
        @SuppressWarnings("unchecked")
        List<R_Voyage> list = query.list();
        
        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", count);
        mapContent.put("rows", list);

        return ReturnUtil.returnMap(1, mapContent);
    }
    
}