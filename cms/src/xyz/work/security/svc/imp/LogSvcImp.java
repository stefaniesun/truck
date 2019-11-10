package xyz.work.security.svc.imp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.MyRequestUtil;
import xyz.filter.ReturnUtil;
import xyz.util.DateUtil;
import xyz.util.StringTool;
import xyz.work.security.model.LogOper;
import xyz.work.security.model.LogOperCheckForm;
import xyz.work.security.svc.LogSvc;

@Service
public class LogSvcImp implements LogSvc{

	@Autowired
	CommonDao commonDao;
	
	@Override
	public Map<String, Object> addLogOper(LogOper logOper) {
		logOper.setAddDate(new Date());
		if(logOper.getDataContent()!=null && logOper.getDataContent().length()>20000){
			String newStr = logOper.getDataContent().substring(0,20000);
			logOper.setDataContent(newStr);
		}
		commonDao.save(logOper);
		return ReturnUtil.returnMap(1, null);
	}
	
	@Override
	public Map<String, Object> getLogOperList(
			int offset,
			int pagesize,
			String username,
			String otherInfo,
            Date dateStart,
            Date dateEnd) {
		String hql = "from LogOper t where 1=1";
		if(StringTool.isNotNull(username)){
			hql += " and t.username in ("+StringTool.StrToSqlString(username)+")";
		}
		if(dateStart!=null){
			hql += " and t.addDate >= '"+DateUtil.dateToString(dateStart)+"'";
		}
		if(dateEnd!=null){
			hql += " and t.addDate <= '"+DateUtil.getDateEndForQuery(dateEnd)+"'";
		}
		if(otherInfo!=null && !"".equals(otherInfo)){
			hql += " and concat('#',ifnull(t.ipInfo,''),'#',ifnull(t.interfacePath,''),'#',ifnull(t.remark,''),'#',ifnull(t.isWork,''),'#',ifnull(t.dataContent,''),'#') like '%"+otherInfo+"%'";
		}
		{
			Map<String, List<String>> decideMap = MyRequestUtil.getDecideMap();
			if(decideMap!=null){
				List<String> channels = decideMap.get("channels");
				Collections.sort(channels);
				String sqlT = "SELECT t.possessor FROM possessor_relate t WHERE t.type = 'channel' GROUP BY t.possessor HAVING '"+StringTool.listToString(channels, "")+"' LIKE CONCAT('%', group_concat(t.relate ORDER BY t.relate ASC SEPARATOR '%' ),'%')";
				@SuppressWarnings("unchecked")
				List<String> numberCodeList = commonDao.getSqlQuery(sqlT).list();
				sqlT = "select t.username from security_user t where t.possessor in ("+StringTool.listToSqlString(numberCodeList)+")";
				@SuppressWarnings("unchecked")
				List<String> usernameList = commonDao.getSqlQuery(sqlT).list();
				hql += " and t.username in ("+StringTool.listToSqlString(usernameList)+")";
			}
		}
		
		String countHql = "select count(iidd) "+hql;
		Query countQuery = commonDao.getQuery(countHql);
		Number countTemp = (Number)countQuery.uniqueResult();
		int count = countTemp==null?0:countTemp.intValue();
		
		hql+= " order by t.addDate desc";
		
		Query query = commonDao.getQuery(hql);
		query.setMaxResults(pagesize);
		query.setFirstResult(offset);
		
		@SuppressWarnings("unchecked")
		List<LogOper> logOperList = query.list();
		
		Map<String,Object> mapContent = new HashMap<String, Object>();
		mapContent.put("total",count);
		mapContent.put("rows",logOperList);
		return ReturnUtil.returnMap(1, mapContent);
	}
	
	@Override
	public Map<String, Object> getLogOperListForCheck(
			int offset,
			int pagesize,
			String username,
            Date dateStart,
            Date dateEnd) {
		String hql = "select t.username,group_concat(distinct t.ip_info) as ipInfo," +
				"min(t.add_date) as dateStart,max(t.add_date) as dateEnd,count(t.iidd) as count from log_oper t where 1=1";
		if(StringTool.isNotNull(username)){
			hql += " and t.username in ("+StringTool.StrToSqlString(username)+")";
		}else{
			{
				Map<String, List<String>> decideMap = MyRequestUtil.getDecideMap();
				if(decideMap!=null){
					List<String> channels = decideMap.get("channels");
					Collections.sort(channels);
					String sqlT = "SELECT t.possessor FROM possessor_relate t WHERE t.type = 'channel' GROUP BY t.possessor HAVING '"+StringTool.listToString(channels, "")+"' LIKE CONCAT('%', group_concat(t.relate ORDER BY t.relate ASC SEPARATOR '%' ),'%')";
					@SuppressWarnings("unchecked")
					List<String> numberCodeList = commonDao.getSqlQuery(sqlT).list();
					sqlT = "select t.username from security_user t where t.possessor in ("+StringTool.listToSqlString(numberCodeList)+")";
					@SuppressWarnings("unchecked")
					List<String> usernameList = commonDao.getSqlQuery(sqlT).list();
					hql += " and t.username in ("+StringTool.listToSqlString(usernameList)+")";
				}
			}
		}
		if(dateStart!=null){
			hql += " and t.add_date >= '"+DateUtil.dateToString(dateStart)+"'";
		}
		if(dateEnd!=null){
			hql += " and t.add_date <= '"+DateUtil.dateToString(dateEnd)+"'";
		}
		hql += " group by t.username order by dateStart asc";
		
		SQLQuery query = commonDao.getSqlQuery(hql);
		query.
		addScalar("username").
		addScalar("ipInfo").
		addScalar("dateStart").
		addScalar("dateEnd").
		addScalar("count").
		setResultTransformer(Transformers.aliasToBean(LogOperCheckForm.class));
		
		@SuppressWarnings("unchecked")
		List<LogOperCheckForm> logOperCheckFormList = query.list();
		
		
		
		
		int count = logOperCheckFormList.size();
		int rows = (offset+pagesize)<count?(offset+pagesize):count;
		List<LogOperCheckForm> newList = offset<count?logOperCheckFormList.subList(offset,rows):new ArrayList<LogOperCheckForm>();
		
		Set<String> ips = new HashSet<String>();
		for(LogOperCheckForm lo : newList){
			String pp = lo.getIpInfo();
			if(StringTool.isNotNull(pp)){
				String [] ps =pp.split(",");
				for(String p : ps){
					ips.add(p);
				}
			}
		}
		String sql = "SELECT t.ip_info,COUNT(DISTINCT t.username) FROM log_oper t where 1=1";
		if(dateStart!=null){
			sql += " and t.add_date >= '"+DateUtil.dateToString(dateStart)+"'";
		}
		if(dateEnd!=null){
			sql += " and t.add_date <= '"+DateUtil.dateToString(dateEnd)+"'";
		}
		sql += " GROUP BY t.ip_info";
		@SuppressWarnings("unchecked")
		List<Object[]> ttList = commonDao.getSqlQuery(sql).list();
		for(LogOperCheckForm lo : newList){
			String pp = lo.getIpInfo();
			if(StringTool.isNotNull(pp)){
				String ipStr = "";
				String [] ps =pp.split(",");
				for(String p : ps){
					for(Object[] obj : ttList){
						if(p.equals(obj[0])){
							ipStr += "【"+p+"——"+obj[1].toString()+"人】";
						}
					}
				}
				lo.setIpInfo(ipStr);
			}
		}
		
		
		Map<String,Object> mapContent = new HashMap<String, Object>();
		mapContent.put("total",count);
		mapContent.put("rows",newList);
		return ReturnUtil.returnMap(1, mapContent);
	}
	
	@Override
	public Map<String, Object> getLogOperListForOther(
            Date dateStart,
            Date dateEnd) {
		String hql = "select t.username from log_oper t where 1=1";
		if(dateStart!=null){
			hql += " and t.add_date >= '"+DateUtil.dateToString(dateStart)+"'";
		}
		if(dateEnd!=null){
			hql += " and t.add_date <= '"+DateUtil.dateToString(dateEnd)+"'";
		}
		hql += " group by t.username";
		
		SQLQuery query = commonDao.getSqlQuery(hql);
		@SuppressWarnings("unchecked")
		List<String> usernameList2 = query.list();
		
		String sql = "select group_concat(t.username order by length(t.nick_name)),group_concat(t.nick_name order by length(t.nick_name)) from security_user t where 1=1 and t.enabled = 1";
		{
			Map<String, List<String>> decideMap = MyRequestUtil.getDecideMap();
			if(decideMap!=null){
				List<String> channels = decideMap.get("channels");
				Collections.sort(channels);
				String sqlT = "SELECT t.possessor FROM possessor_relate t WHERE t.type = 'channel' GROUP BY t.possessor HAVING '"+StringTool.listToString(channels, "")+"' LIKE CONCAT('%', group_concat(t.relate ORDER BY t.relate ASC SEPARATOR '%' ),'%')";
				@SuppressWarnings("unchecked")
				List<String> numberCodeList = commonDao.getSqlQuery(sqlT).list();
				sqlT = "select t.username from security_user t where t.possessor in ("+StringTool.listToSqlString(numberCodeList)+")";
				@SuppressWarnings("unchecked")
				List<String> usernameList = commonDao.getSqlQuery(sqlT).list();
				sql += " and t.username in ("+StringTool.listToSqlString(usernameList)+")";
			}
		}
		if(usernameList2.size()>0){
			sql += " and t.username not in ("+StringTool.listToSqlString(usernameList2)+")";
		}
		
		@SuppressWarnings("unchecked")
		List<Object[]> ttList = commonDao.getSqlQuery(sql).list();
		Map<String, Object> tt = new HashMap<String, Object>();
		if(ttList.size()>0){
			tt.put("username", ttList.get(0)[0]);
			tt.put("nickName", ttList.get(0)[1]);
		}
		return ReturnUtil.returnMap(1, tt);
	}
}
