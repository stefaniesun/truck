package xyz.work.sale.svc.imp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.JSON;
import xyz.filter.ReturnUtil;
import xyz.util.StringTool;
import xyz.util.StringUtil;
import xyz.work.base.model.Airway;
import xyz.work.base.model.Cruise;
import xyz.work.sale.svc.PtviewWechatSvc;
import xyz.work.sell.model.Ptview;

@Service
public class PtviewWechatSvcImp implements PtviewWechatSvc {

	@Autowired
	CommonDao commonDao;

	@Override
	public Map<String, Object> queryPtviewList(int pageSize, int offset) {
        String hql = "from Ptview p where p.travelDate>now() ";
        
        hql += "order by p.alterDate desc ";
        Query countQuery = commonDao.getQuery("select count(*) " + hql);
        Number countNum = (Number)countQuery.uniqueResult();
        int count = countNum == null ? 0 : countNum.intValue();

        Query query = commonDao.getQuery(hql);
        query.setMaxResults(pageSize);
        query.setFirstResult(offset);
        @SuppressWarnings("unchecked")
        List<Ptview> list = query.list();

        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", count);
        mapContent.put("rows", list);
        
        System.out.println(JSON.toJson(mapContent));

        return ReturnUtil.returnMap(1, mapContent);
    }

	@Override
	public Map<String, Object> getSelectData() {
		
		String sql="select airway,airway_name_cn,date_mark,base_price,cruise,cruise_name_cn,company_name_cn from ptview where travel_date > now()";
		
		@SuppressWarnings("unchecked")
		List<Object[]> list=commonDao.getSqlQuery(sql).list();
		
		List<Map<String, String>> result=new ArrayList<Map<String,String>>();
		
		for(Object[] objects:list){
			Map<String, String> map=new HashMap<String, String>();
			map.put("airway", objects[0].toString());
			map.put("airwayNameCn", objects[1].toString());
			map.put("dateMark", objects[2].toString());
			map.put("basePrice", objects[3].toString());
			map.put("cruise", objects[4].toString());
			map.put("cruiseNameCn", objects[5].toString());
			map.put("companyNameCn", objects[6].toString());
			result.add(map);
		}
		
		return ReturnUtil.returnMap(1, result);
	}

	@Override
	public Map<String, Object> getQueryData(int pageSize, int offset,
			String airway, String date, String price, String cruise) {
		
        String hql = "from Ptview p where p.travelDate>now() ";
        
    	if(!StringTool.isEmpty(airway)){
			hql+=" and airway='"+airway+"'";
		}
		if(!StringTool.isEmpty(date)){
			hql+=" and dateMark='"+date+"'";
		}
		if(!StringTool.isEmpty(price)){
			String[] priceArray=price.split(",");
			hql+=" and basePrice>='"+priceArray[0]+"' and basePrice<='"+priceArray[1]+"'";
		}
		if(!StringTool.isEmpty(cruise)){
			hql+=" and cruise='"+cruise+"'";
		}
        
        hql += "order by p.alterDate desc ";
        Query countQuery = commonDao.getQuery("select count(*) " + hql);
        Number countNum = (Number)countQuery.uniqueResult();
        int count = countNum == null ? 0 : countNum.intValue();

        Query query = commonDao.getQuery(hql);
        query.setMaxResults(pageSize);
        query.setFirstResult(offset);
        @SuppressWarnings("unchecked")
        List<Ptview> list = query.list();

        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", count);
        mapContent.put("rows", list);

        return ReturnUtil.returnMap(1, mapContent);
    
	}

	@Override
	public Map<String, Object> getPtviewDetail(String numberCode) {
		
		if(	StringTool.isEmpty(numberCode)){
			  return ReturnUtil.returnMap(0, "参数异常");
		}
		
		Ptview ptview=(Ptview) commonDao.getObjectByUniqueCode("Ptview", "numberCode", numberCode);
		
		if(ptview==null){
			 return ReturnUtil.returnMap(0, "对象为空");
		}
		
		Cruise cruise=(Cruise) commonDao.getObjectByUniqueCode("Cruise", "numberCode", ptview.getCruise());
		
	    Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("ptview", ptview);
        mapContent.put("cruise", cruise);
		
        return ReturnUtil.returnMap(1, mapContent);
	}
	
	
	
	
}
