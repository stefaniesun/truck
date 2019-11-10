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
import xyz.work.r_base.model.R_Airway;
import xyz.work.r_base.svc.R_AirwaySvc;

@Service
public class R_AirwaySvcImp implements R_AirwaySvc {
    @Autowired
    private CommonDao commonDao;

    @Override
    public Map<String, Object> queryRAirwayList(int offset , int pageSize , String areaMark , String mark) {
        String hql = "from R_Airway a where 1=1 ";
        if(StringTool.isNotNull(areaMark)){
            hql += "and a.areaMark like '%"+ areaMark +"%' ";
        }
        if(StringTool.isNotNull(mark)){
            hql += "and a.mark like '%"+ mark +"%' ";
        }

        Query countQuery = commonDao.getQuery("select count(*) " + hql);
        Number countNum = (Number)countQuery.uniqueResult();
        int count = countNum == null ? 0 : countNum.intValue();

        Query query = commonDao.getQuery(hql);
        query.setMaxResults(pageSize);
        query.setFirstResult(offset);
        @SuppressWarnings("unchecked")
        List<R_Airway> list = query.list();
        
        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", count);
        mapContent.put("rows", list);

        return ReturnUtil.returnMap(1, mapContent);
    }

}