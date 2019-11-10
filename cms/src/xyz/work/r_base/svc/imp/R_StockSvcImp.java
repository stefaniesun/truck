package xyz.work.r_base.svc.imp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.ReturnUtil;
import xyz.work.r_base.model.R_Stock;
import xyz.work.r_base.svc.R_StockSvc;

@Service
public class R_StockSvcImp implements R_StockSvc {
    @Autowired
    private CommonDao commonDao;
    
    @Override
    public Map<String, Object> queryRStockList(int offset , int pageSize , String tkview) {
        String stockHql = "from R_Stock s where 1=1 ";
        stockHql += "and s.tkview = '"+ tkview +"' ";
        
        Query countQuery = commonDao.getQuery("select count(*) " + stockHql);
        Number countNum = (Number)countQuery.uniqueResult();
        int count = countNum == null ? 0 : countNum.intValue();

        Query query = commonDao.getQuery(stockHql);
        query.setMaxResults(pageSize);
        query.setFirstResult(offset);
        @SuppressWarnings("unchecked")
        List<R_Stock> list = query.list();
        
        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", count);
        mapContent.put("rows", list);

        return ReturnUtil.returnMap(1, mapContent);
    }

}