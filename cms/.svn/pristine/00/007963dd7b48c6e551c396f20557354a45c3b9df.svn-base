package xyz.work.resources.svc.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.ReturnUtil;
import xyz.util.StringTool;
import xyz.work.resources.model.TkviewType;
import xyz.work.resources.svc.TkviewTypeSvc;

@Service
public class TkviewTypeSvcImp implements TkviewTypeSvc {
    @Autowired
    CommonDao commonDao;

    @Override
    public Map<String, Object> queryTkviewTypeList(int offset , int pagesize , String cruise ,
                                                   String cabin) {
        String querySql = "from TkviewType tt where 1=1 ";
        if(StringTool.isNotNull(cruise)){
            querySql += "and tt.cruise = '"+ cruise +"' ";
        }
        if(StringTool.isNotNull(cabin)){
            querySql += "and tt.cabin = '"+ cabin +"' ";
        }
        querySql += "order by tt.cruise ";
        
        Query countQuery = commonDao.getQuery("select count(*) " + querySql);
        Number tempCount = (Number)countQuery.uniqueResult();
        int total = tempCount == null ? 0 : tempCount.intValue();

        Query query = commonDao.getQuery(querySql);
        query.setFirstResult(offset);
        query.setMaxResults(pagesize);
        @SuppressWarnings("unchecked")
        List<TkviewType> tkviewTypeList = query.list();
        
        List<String> cruiseNumber = new ArrayList<String>();
        List<String> cabinNumber = new ArrayList<String>();
        for (TkviewType tkviewType : tkviewTypeList) {
            cruiseNumber.add(tkviewType.getCruise());
            cabinNumber.add(tkviewType.getCabin());
        }
        
        String cruiseSql = "SELECT number_code,name_cn FROM cruise WHERE number_code IN ("+StringTool.listToSqlString(cruiseNumber)+") ";
        @SuppressWarnings("unchecked")
        List<Object[]> cruiseList = commonDao.getSqlQuery(cruiseSql).list();
        
        String cabinSql = "SELECT number_code,name_cn FROM cabin WHERE number_code IN ("+StringTool.listToSqlString(cabinNumber)+") ";
        @SuppressWarnings("unchecked")
        List<Object[]> cabinList = commonDao.getSqlQuery(cabinSql).list();
        
        for (TkviewType tkviewType : tkviewTypeList) {
            for (Object[] c : cruiseList) {
                if (tkviewType.getCruise().equals(c[0])) {
                    tkviewType.setCruiseNameCn((String)c[1]);
                    break;
                }
            }
            for (Object[] c : cabinList) {
                if (tkviewType.getCabin().equals(c[0])) {
                    tkviewType.setCabinNameCn((String)c[1]);
                    break;
                }
            }
        }
        
        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", total);
        mapContent.put("rows", tkviewTypeList);
        
        return ReturnUtil.returnMap(1, mapContent);
    }

}