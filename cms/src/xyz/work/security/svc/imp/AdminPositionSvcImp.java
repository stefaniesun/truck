package xyz.work.security.svc.imp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
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
import xyz.util.Constant;
import xyz.util.PossessorUtil;
import xyz.util.StringTool;
import xyz.util.UUIDUtil;
import xyz.work.security.model.Provider;
import xyz.work.security.model.SecurityApi;
import xyz.work.security.model.SecurityFunction;
import xyz.work.security.model.SecurityPosition;
import xyz.work.security.model.SecurityPositionButton;
import xyz.work.security.svc.AdminPositionSvc;

@Service
public class AdminPositionSvcImp  implements AdminPositionSvc{
	
	@Autowired
	CommonDao commonDao;
	
	@Autowired
    PossessorUtil possessorUtil;
	
	@Override
	public Map<String, Object> queryPositionList(int offset,int pagesize,String numberCode,String nameCn) {
		
		StringBuffer hql = new StringBuffer();
		hql.append("from SecurityPosition t where 1 = 1");
		{
		    hql.append(possessorUtil.getRelatesWhereHql(Constant.relate_type_position));
		}
		if(numberCode != null && !"".equals(numberCode)){
            hql.append(" and t.numberCode like '%"+numberCode+"%'");
        }
		if(nameCn != null && !"".equals(nameCn)){
			hql.append(" and t.nameCn like '%"+nameCn+"%'");
		}
		
		Query query = commonDao.getQuery(hql.toString());
		int count = query.list().size();
		query.setMaxResults(pagesize);
		query.setFirstResult(offset);
		@SuppressWarnings("unchecked")
		List<SecurityPosition> positionList = query.list();
		commonDao.clear();
		
		for(SecurityPosition position:positionList){
			String sql = "select count(t.iidd) from security_user t where t.position = '"+position.getNumberCode()+"'";
			Number number = (Number)commonDao.getSqlQuery(sql).uniqueResult();
			int countUser = number==null?0:number.intValue();
			position.setCountUser(countUser);
		}
		
		Map<String,Object> mapContent = new HashMap<String, Object>();
		mapContent.put("total",count);
		mapContent.put("rows",positionList);

		return ReturnUtil.returnMap(1, mapContent);
	}
	
	@Override
	public Map<String, Object> addPosition(SecurityPosition securityPosition) {
		
		Date date = new Date();
		securityPosition.setAddDate(date);
		securityPosition.setAlterDate(date);
		commonDao.save(securityPosition);
		
		possessorUtil.savePossessorRelates(Constant.relate_type_position, securityPosition.getNumberCode());
		
		return ReturnUtil.returnMap(1, null);
	}
	
	@Override
	public Map<String, Object> editPosition(
			String numberCode,
			String nameCn,
			String remark) {
		MyRequestUtil.decidePowerIsAll();
		
		String hql = "from SecurityPosition sp where sp.numberCode = '"+numberCode+"'";
		SecurityPosition securityPosition = (SecurityPosition)commonDao.queryUniqueByHql(hql);
		
		securityPosition.setNameCn(nameCn);
		securityPosition.setRemark(remark);
		securityPosition.setAlterDate(new Date());
		commonDao.update(securityPosition);
		return ReturnUtil.returnMap(1, null);
	}
	
	@Override
	public Map<String, Object> deletePosition(String positions) {
		MyRequestUtil.decidePowerIsAll();
		String [] positionStrs = positions.split(",");
		for(int i = 0;i<positionStrs.length;i++) {
			String numberCode = positionStrs[i];
			String hql = "from SecurityPosition sp where sp.numberCode = '"+numberCode+"'";
			SecurityPosition securityPosition = (SecurityPosition)commonDao.queryUniqueByHql(hql);
			
			commonDao.delete(securityPosition);
			hql = "delete from SecurityPositionButton s where s.position = '"+numberCode+"'";
			commonDao.updateByHql(hql);
			hql = "delete from SecurityLogin s where s.position = '"+numberCode+"'";
			commonDao.updateByHql(hql);
		}
		return ReturnUtil.returnMap(1, null);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryPositionButtonList(boolean isContain,
                                            		   String position,
                                            		   String nameCn,
                                            		   String functionNameCn,
                                            		   BigDecimal isPossessor){
		String currentUserPosition = MyRequestUtil.getSecurityLogin().getPosition();
		
		String hql = "select t.button from SecurityPositionButton t where t.position = '"+currentUserPosition+"'";
		List<String> currentUserButtons = commonDao.queryByHql(hql);
		
		hql = "select t.button from SecurityPositionButton t where t.position = '"+position+"'";
		List<String> currentPositionButtons = commonDao.queryByHql(hql);
		
		Set<String> buttons = new HashSet<String>();
		if(isContain){
			for(String f1 : currentUserButtons){
				for(String f2 : currentPositionButtons){
					if(f1.equals(f2)){
						buttons.add(f1);
						break;
					}
				}
			}
		}else{
			for(String f1 : currentUserButtons){
				boolean flag = true;
				for(String f2 : currentPositionButtons){
					if(f1.equals(f2)){
						flag = false;
						break;
					}
				}
				if(flag){
					buttons.add(f1);
				}
			}
		}
		
		StringBuffer sql = new StringBuffer();
		sql.append("select");
		sql.append(" t1.number_code AS numberCode,");
		sql.append(" t2.group_cn AS groupNameCn,");
		sql.append(" t2.name_cn AS functionNameCn,");
		sql.append(" GROUP_CONCAT(t1.name_cn) AS nameCn");
		sql.append(" FROM security_api t1");
		sql.append(" LEFT JOIN security_function t2 ON t1.`function` = t2.number_code");
		sql.append(" WHERE t1.is_decide = 1");
		sql.append(" and t1.number_code in ("+StringTool.listToSqlString(buttons)+")");
		if (isPossessor != null && isPossessor.intValue() == 1) {
            sql.append(" and t1.number_code not in ("+StringTool.StrToSqlString(StringTool.arrToString(Constant.possessorNotSeeButtonCodes, ","))+")");
        }
		if (StringTool.isNotNull(nameCn)) {
			sql.append(" and t1.name_cn like '%"+nameCn+"%'");
		}
		if (StringTool.isNotNull(functionNameCn)) {
            sql.append(" and t2.group_cn like '%"+functionNameCn+"%'");
        }
		sql.append(" GROUP BY t1.number_code order by functionNameCn");
		SQLQuery query = commonDao.getSqlQuery(sql.toString());
		query.addScalar("numberCode").
		addScalar("functionNameCn").
		addScalar("groupNameCn").
		addScalar("nameCn").
		setResultTransformer(Transformers.aliasToBean(SecurityApi.class));
		
		List<SecurityApi> securityApiList = query.list();
		
		Map<String, Object> mapContent = new HashMap<String, Object>();
		mapContent.put("total", securityApiList.size());
		mapContent.put("rows", securityApiList);
		return ReturnUtil.returnMap(1, mapContent);
	}
	
	@Override
	public Map<String, Object> addPositionButton(String position,String buttons) {
		if(buttons==null || "".equals(buttons)){
			return ReturnUtil.returnMap(1, "您未选中任何操作!");
		}
		String currentUserPosition = MyRequestUtil.getSecurityLogin().getPosition();
		
		String sql = "select t.number_code from security_api t where t.number_code in (select t2.button from security_position_button t2 where t2.position = '"+currentUserPosition+"' and not (t2.position='' and t2.button in ("+StringTool.StrToSqlString(buttons)+"))) and t.number_code in ("+StringTool.StrToSqlString(buttons)+") group by t.number_code";
		
		@SuppressWarnings("unchecked")
		List<String> buttonList = commonDao.getSqlQuery(sql).list();
		
		for(String button : buttonList){
			SecurityPositionButton securityPositionButton = new SecurityPositionButton();
			securityPositionButton.setPosition(position);
			securityPositionButton.setButton(button);
			commonDao.save(securityPositionButton);
		}
		return ReturnUtil.returnMap(1, null);
	}
	
	@Override
	public Map<String, Object> deletePositionButton(String position,String buttons) {
		String hql = "delete from SecurityPositionButton t where t.position = '"+position+"' and t.button in("+StringTool.StrToSqlString(buttons)+")";
		commonDao.updateByHql(hql);
		return ReturnUtil.returnMap(1, null);
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryPositionButtonTree(
			boolean isContain,
			String position){
		String currentUserPosition = MyRequestUtil.getSecurityLogin().getPosition();
		
		String hql = "select t.button from SecurityPositionButton t where t.position = '"+currentUserPosition+"'";
		List<String> currentUserButtons = commonDao.queryByHql(hql);
		
		hql = "select t.button from SecurityPositionButton t where t.position = '"+position+"'";
		List<String> currentPositionButtons = commonDao.queryByHql(hql);
		
		Set<String> buttons = new HashSet<String>();
		if(isContain){
			for(String f1 : currentUserButtons){
				for(String f2 : currentPositionButtons){
					if(f1.equals(f2)){
						buttons.add(f1);
						break;
					}
				}
			}
		}else{
			for(String f1 : currentUserButtons){
				boolean flag = true;
				for(String f2 : currentPositionButtons){
					if(f1.equals(f2)){
						flag = false;
						break;
					}
				}
				if(flag){
					buttons.add(f1);
				}
			}
		}
		
		StringBuffer sql = new StringBuffer();
		sql.append("select");
		sql.append(" t1.function,");
		sql.append(" t1.number_code AS numberCode,");
		sql.append(" GROUP_CONCAT(t1.name_cn) AS nameCn");
		sql.append(" FROM security_api t1");
		sql.append(" WHERE t1.is_decide = 1");
		sql.append(" and t1.number_code in ("+StringTool.listToSqlString(buttons)+")");
		sql.append(" GROUP BY t1.number_code order by t1.iidd");
		SQLQuery query = commonDao.getSqlQuery(sql.toString());
		query.addScalar("numberCode").
		addScalar("nameCn").
		addScalar("function").
		setResultTransformer(Transformers.aliasToBean(SecurityApi.class));
		
		List<SecurityApi> securityApiList = query.list();
		
		Set<String> functionSet = new HashSet<String>();
		for(SecurityApi securityApi : securityApiList){
			functionSet.add(securityApi.getFunction());
		}
		
		hql = "from SecurityFunction t where t.numberCode in ("+StringTool.listToSqlString(functionSet)+") order by t.iidd";
		List<SecurityFunction> securityFunctionList = commonDao.queryByHql(hql);
		
		Set<String> groupSet = new HashSet<String>();
		for(SecurityFunction securityFunction : securityFunctionList){
			groupSet.add(securityFunction.getGroupCn());
		}
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		for(String group : groupSet){
			Map<String, Object> gMap = new LinkedHashMap<String, Object>();
			gMap.put("id", UUIDUtil.getUUIDStringFor32());
			gMap.put("text",group);
			gMap.put("state","closed");
			List<Map<String, Object>> fList = new ArrayList<Map<String,Object>>();
			for(SecurityFunction securityFunction : securityFunctionList){
				if(group.equals(securityFunction.getGroupCn())){
					Map<String, Object> fMap = new HashMap<String, Object>();
					fMap.put("state","open");
					fMap.put("text", securityFunction.getNameCn());
					fMap.put("id", securityFunction.getNumberCode());
					List<Map<String, Object>> aList = new ArrayList<Map<String, Object>>();
					for(SecurityApi securityApi : securityApiList){
						if(securityFunction.getNumberCode().equals(securityApi.getFunction())){
							Map<String, Object> aMap = new HashMap<String, Object>();
							aMap.put("state","open");
							aMap.put("text", securityApi.getNameCn());
							aMap.put("id", securityApi.getNumberCode());
							aList.add(aMap);
						}
					}
					if(aList.size()>0){
						fMap.put("children",aList);
					}
					fList.add(fMap);
				}
			}
			if(fList.size()>0){
				gMap.put("children",fList);
			}
			resultList.add(gMap);
		}
		return ReturnUtil.returnMap(1, resultList);
	}

    @Override
    public Map<String, Object> queryPossessorPositionList(Boolean isTrue , String possessor ,
                                                          String numberCode , String name) {

        StringBuffer hqlSb = new StringBuffer();
        
        hqlSb.append("from SecurityPosition where 1=1");
        List<String> relates = possessorUtil.getDecideMapByPossessor(possessor,Constant.relate_type_position).keySet().contains(Constant.relate_type_position)?possessorUtil.getDecideMapByPossessor(possessor,Constant.relate_type_position).get(Constant.relate_type_position):new ArrayList<String>();
        
        if (isTrue) {
            hqlSb.append(" and numberCode in ("+StringTool.listToSqlString(relates)+")");
        }else {
            hqlSb.append(" and numberCode not in ("+StringTool.listToSqlString(relates)+")");
        }
        
        if (StringTool.isNotNull(numberCode)) {
            hqlSb.append(" and numberCode = '"+numberCode+"'");
        }

        if (StringTool.isNotNull(name)) {
            hqlSb.append(" and nameCn like '%"+name+"%'");
        }
        
        @SuppressWarnings("unchecked")
        List<Provider> providerList = commonDao.queryByHql(hqlSb.toString());
        
        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", providerList.size());
        mapContent.put("rows", providerList);
        
        return ReturnUtil.returnMap(1, mapContent);
        
    }
}
