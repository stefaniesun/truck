package xyz.work.security.svc.imp;

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
import xyz.work.security.model.SystemContent;
import xyz.work.security.svc.SystemContentSvc;

@Service
public class SystemContentSvcImp  implements SystemContentSvc{
	
	@Autowired
	CommonDao commonDao;
	
	@Override
	public Map<String, Object> querySystemContentList(
			int offset, 
			int pagesize,
			String nameKey,
            String nameCn) {
		
		StringBuffer hql = new StringBuffer(); 
		hql.append("from SystemContent s where 1 = 1");
		if(StringTool.isNotNull(nameKey)){
			hql.append(" and s.nameKey like '%"+nameKey+"%'");
		}
		if(StringTool.isNotNull(nameCn)){
			hql.append(" and s.nameCn like '%"+nameCn+"%'");
		}
		
		String countHql = "select count(*) "+hql.toString();
		Number tempNumber = (Number) commonDao.getQuery(countHql).uniqueResult();
		int count = tempNumber==null?0:tempNumber.intValue();
		
		Query query = commonDao.getQuery(hql.toString());
		query.setMaxResults(pagesize);
		query.setFirstResult(offset);
		
		@SuppressWarnings("unchecked")
		List<SystemContent> systemContentList = query.list();
		
		Map<String ,Object> mapContent = new HashMap<String ,Object>();
		mapContent.put("total", count);
		mapContent.put("rows", systemContentList);
		
		return ReturnUtil.returnMap(1, mapContent);
	}

	@Override
	public Map<String, Object> getSystemContentList() {
		String hql = "from SystemContent s where 1 = 1";
		Query query = commonDao.getQuery(hql);
		
		@SuppressWarnings("unchecked")
		List<SystemContent> systemContentList = query.list();
		
		return ReturnUtil.returnMap(1, systemContentList);
	}

	@Override
	public Map<String, Object> addSystemContent(
			String nameKey, 
			String nameCn,
			String value, 
			String remark) {
		
		if(nameKey==null || "".equals(nameKey)){
			return ReturnUtil.returnMap(0, "英文名称不能为空!");
		}
		if(nameCn==null || "".equals(nameCn)){
			return ReturnUtil.returnMap(0, "中文名称不能为空!");
		}
		
		Date date = new Date();
		
		SystemContent systemContent = new SystemContent();
		systemContent.setNumberCode(UUIDUtil.getUUIDStringFor32());
		systemContent.setNameCn(nameCn);
		systemContent.setNameKey(nameKey);
		systemContent.setValue(value);
		systemContent.setRemark(remark);
		systemContent.setAddDate(date);
		systemContent.setAlterDate(date);
		
		commonDao.save(systemContent);
		
		return ReturnUtil.returnMap(1, null);
	}

	@Override
	public Map<String, Object> editSystemContent(
			String numberCode,
			String nameKey, 
			String nameCn, 
			String value, 
			String remark) {
		
		if(nameKey==null || "".equals(nameKey)){
			return ReturnUtil.returnMap(0, "英文名称不能为空!");
		}
		if(nameCn==null || "".equals(nameCn)){
			return ReturnUtil.returnMap(0, "中文名称不能为空!");
		}
		
		SystemContent systemContent = (SystemContent) commonDao.getObjectByUniqueCode("SystemContent", "numberCode", numberCode);
		if(systemContent == null){
			return ReturnUtil.returnMap(0, "对象不存在!");
		}
		systemContent.setNameKey(nameKey);
		systemContent.setNameCn(nameCn);
		systemContent.setValue(value);
		systemContent.setRemark(remark);
		systemContent.setAlterDate(new Date());
		
		commonDao.update(systemContent);
		
		return ReturnUtil.returnMap(1, null);
	}

	@Override
	public Map<String, Object> deleteSystemContent(String numberCodes) {
		if(numberCodes == null || "".equals(numberCodes)){
			return ReturnUtil.returnMap(0, "请先选中需要删除的对象!");
		}
		
		String sql = "delete from system_content where 1 = 1 and number_code in ("+StringTool.StrToSqlString(numberCodes)+")";
		commonDao.getSqlQuery(sql).executeUpdate();

		return ReturnUtil.returnMap(1, null);
	}
}
