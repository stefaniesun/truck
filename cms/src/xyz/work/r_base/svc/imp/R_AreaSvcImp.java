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
import xyz.work.r_base.model.R_Area;
import xyz.work.r_base.svc.R_AreaSvc;

@Service
public class R_AreaSvcImp implements R_AreaSvc {    
    @Autowired
    private CommonDao commonDao;

    @Override
    public Map<String, Object> queryRAreaList(int offset , int pageSize , String mark) {
        String areaHql = "from R_Area a where 1=1 ";
        if(StringTool.isNotNull(mark)){
            areaHql += "and a.mark like '%"+ mark +"%' ";
        }
        
        Query countQuery = commonDao.getQuery("select count(*) " + areaHql);
        Number countNum = (Number)countQuery.uniqueResult();
        int count = countNum == null ? 0 : countNum.intValue();

        Query query = commonDao.getQuery(areaHql);
        query.setMaxResults(pageSize);
        query.setFirstResult(offset);
        @SuppressWarnings("unchecked")
        List<R_Area> list = query.list();
        
        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", count);
        mapContent.put("rows", list);
        
        return ReturnUtil.returnMap(1, mapContent);
    }

}