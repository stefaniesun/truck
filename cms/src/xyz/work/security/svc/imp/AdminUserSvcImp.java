package xyz.work.security.svc.imp;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.exception.MyExceptionForRole;
import xyz.filter.MyRequestUtil;
import xyz.filter.ReturnUtil;
import xyz.util.Constant;
import xyz.util.EncryptionUtil;
import xyz.util.ListNumberCode;
import xyz.util.PossessorUtil;
import xyz.util.StringTool;
import xyz.work.security.model.SecurityPosition;
import xyz.work.security.model.SecurityUser;
import xyz.work.security.svc.AdminUserSvc;

@Service
public class AdminUserSvcImp  implements AdminUserSvc{
	
	@Autowired
	CommonDao commonDao;
	
	@Autowired
    PossessorUtil possessorUtil;
	
	@Override
	public Map<String, Object> queryUserList(
			int offset,
			int pagesize,
			String username,
            String nickName,
            String position,
            String possessor,
            String enabled) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT su.add_date AS addDate,");
		sql.append("su.alter_date AS alterDate,");
		sql.append("su.enabled AS enabled,");
		sql.append("su.is_repeat AS isRepeat,");
		sql.append("su.nick_name AS nickName,");
		sql.append("su.token_num AS tokenNum,");
		sql.append("su.username AS username,");
		sql.append("su.position AS position,");
		sql.append("su.possessor AS possessor,");
		sql.append(" (SELECT p.name_cn from possessor p WHERE p.number_code = su.possessor ) as possessorNameCn ,");
		sql.append(" (SELECT sp.name_cn FROM security_position sp WHERE sp.number_code = su.position) AS positionNameCn");
		sql.append(" FROM security_user su WHERE 1=1");
		{
		    String possessorStr = MyRequestUtil.getPossessor();
			if(StringTool.isNotNull(possessorStr)){
				sql.append(" and su.possessor = '"+possessorStr+"'");
			}
		}
		
		if(StringTool.isNotNull(username)){
			sql.append(" and su.username in ("+StringTool.StrToSqlString(username)+")");
		}
		if(StringTool.isNotNull(position)){
			sql.append(" and su.position in ("+StringTool.StrToSqlString(position)+")");
		}
		if(StringTool.isNotNull(nickName)){
			sql.append(" and su.nick_name like '%"+nickName+"%'");
		}
		if(StringTool.isNotNull(possessor)){
			sql.append(" and su.possessor in ("+StringTool.StrToSqlString(possessor)+")");
		}
		if(StringTool.isNotNull(enabled)){
			sql.append(" and su.enabled = " + enabled);
		}
		sql.append(" order by su.alter_date desc");
		
		String countSQL = "select count(ttt.username) from ("+sql.toString()+") ttt";
		SQLQuery countQuery = commonDao.getSqlQuery(countSQL);
		Number countTemp = (Number)countQuery.uniqueResult();
		int count = countTemp==null?0:countTemp.intValue();
		
		SQLQuery query = commonDao.getSqlQuery(sql.toString());
		
		query.setMaxResults(pagesize);
		query.setFirstResult(offset);
		query.addScalar("addDate").
		addScalar("alterDate").
		addScalar("enabled").
		addScalar("isRepeat").
		addScalar("nickName").
		addScalar("tokenNum").
		addScalar("username").
		addScalar("position").
		addScalar("positionNameCn").
		addScalar("possessor").
		addScalar("possessorNameCn").
		setResultTransformer(Transformers.aliasToBean(SecurityUser.class));
		
		@SuppressWarnings("unchecked")
		List<SecurityUser> userList = query.list();
		
		Map<String,Object> mapContent = new HashMap<String, Object>();
		mapContent.put("total",count);
		mapContent.put("rows",userList);
		return ReturnUtil.returnMap(1,mapContent);
	}
	
	@Override
	public Map<String, Object> getUser(String username) {
		
		SecurityUser user = (SecurityUser) commonDao.getObjectByUniqueCode("SecurityUser", "username", username);
		
		if(user == null){
			return ReturnUtil.returnMap(0, "用户有误!");
		}
		
		return ReturnUtil.returnMap(1, user);
	}

	@Override
	public Map<String, Object> addUser(SecurityUser securityUser) {
		Date date = new Date();
		securityUser.setEnabled(0);
		securityUser.setIsRepeat(0);
		securityUser.setTokenNum(null);
		securityUser.setPosition(null);
		securityUser.setAddDate(date);
		securityUser.setAlterDate(date);
		commonDao.save(securityUser);
		{
			String possessor = MyRequestUtil.getPossessor();
			securityUser.setPossessor(possessor);
		}
		return ReturnUtil.returnMap(1,null);
	}
	
	@Override
	public Map<String, Object> editUser(
			String username,
			String nickName) {
		String hql = "from SecurityUser su where su.username = '"+username+"'";
		SecurityUser securityUser = (SecurityUser)commonDao.queryUniqueByHql(hql);
		securityUser.setNickName(nickName);
		securityUser.setAlterDate(new Date());
		commonDao.update(securityUser);
		return ReturnUtil.returnMap(1,null);
	}

	@Override
	public Map<String, Object> editUserEnabled(String username) {
		String hql = "from SecurityUser su where su.username = '"+username+"'";
		SecurityUser securityUser = (SecurityUser)commonDao.queryUniqueByHql(hql);
		if(securityUser.getEnabled()==0){
			securityUser.setEnabled(1);
		}else{
			securityUser.setEnabled(0);
		}
		securityUser.setAlterDate(new Date());
		commonDao.update(securityUser);
		return ReturnUtil.returnMap(1,null);
	}
	
	@Override
	public Map<String, Object> deleteUser(String users) {
		String [] userStrs = users.split(",");
		for(int i = 0;i<userStrs.length;i++) {
			String username = userStrs[i];
			String hql = "from SecurityUser su where su.username = '"+username+"'";
			SecurityUser securityUser = (SecurityUser)commonDao.queryUniqueByHql(hql);
			commonDao.delete(securityUser);
			hql = "delete from SecurityLogin s where s.username = '"+securityUser.getUsername()+"'";
			commonDao.updateByHql(hql);
		}
		return ReturnUtil.returnMap(1,null);
	}
	
	@Override
	public Map<String, Object> setUserPosition(String username,String position) {
	    if(StringTool.isEmpty(position)){
            return ReturnUtil.returnMap(0,"岗位对象不存在!");
        }
	    
		SecurityUser securityUser = (SecurityUser)commonDao.getObjectByUniqueCode("SecurityUser", "username", username);
		if(securityUser==null){
			return ReturnUtil.returnMap(0,"用户有误!");
		}
		
		String currentUsername = MyRequestUtil.getSecurityLogin().getUsername();
		
		if (currentUsername.equals(securityUser.getUsername())) {
            return ReturnUtil.returnMap(0, "不能修改自己的岗位!");
        }
		
		SecurityPosition securityPosition = (SecurityPosition)commonDao.getObjectByUniqueCode("SecurityPosition", "numberCode", position);
		if(securityPosition==null){
			return ReturnUtil.returnMap(0,"岗位有误!");
		}
		
		SecurityPosition securityPositionC = (SecurityPosition)commonDao.getObjectByUniqueCode("SecurityPosition", "numberCode",MyRequestUtil.getSecurityLogin().getPosition());
		if(securityPositionC==null){
			return ReturnUtil.returnMap(0,"岗位有误!");
		}
		
		securityUser.setPosition(position);
		securityUser.setAlterDate(new Date());
		commonDao.update(securityUser);
		String hql = "delete from SecurityLogin s where s.username = '"+securityUser.getUsername()+"'";
		commonDao.updateByHql(hql);
		return ReturnUtil.returnMap(1,null);
	}
	
	@Override
	public Map<String, Object> getAllPosition() {
		String cPosition = MyRequestUtil.getSecurityLogin().getPosition();
		SecurityPosition securityPosition = (SecurityPosition)commonDao.getObjectByUniqueCode("SecurityPosition","numberCode",cPosition);
		if(securityPosition==null){
			throw new MyExceptionForRole("岗位不存在，请联系系统管理员!");
		}
		
		StringBuffer hql = new StringBuffer();
		hql.append("from SecurityPosition t where 1=1");
		{
		    String possessor = MyRequestUtil.getPossessor();
		    if(StringTool.isNotNull(possessor)){
		        hql.append(possessorUtil.getRelatesWhereHql(Constant.relate_type_position));
		    }
		}
		
		@SuppressWarnings("unchecked")
		List<SecurityPosition> positionList = commonDao.queryByHql(hql.toString());
		return ReturnUtil.returnMap(1,positionList);
	}
	
	@Override
	public Map<String, Object> getSecurityUserList(String q) {
		String sql = "SELECT p.username AS value,p.nick_name AS text FROM security_user p where 1=1"; 
		
		if(q != null && !"".equals(q)){
			sql += " and p.nick_name like '%"+q+"%'";
		}
		
		{
			Map<String, List<String>> decideMap = MyRequestUtil.getDecideMap();
			if(decideMap!=null){
				List<String> channels = decideMap.get("channels");
				Collections.sort(channels);
				String sqlT = "SELECT t.possessor FROM possessor_relate t WHERE t.type = 'channel' GROUP BY t.possessor HAVING '"+StringTool.listToString(channels, "")+"' LIKE CONCAT('%', group_concat(t.relate ORDER BY t.relate ASC SEPARATOR '%' ),'%')";
				@SuppressWarnings("unchecked")
				List<String> numberCodeList = commonDao.getSqlQuery(sqlT).list();
				sql += " and p.possessor in ("+StringTool.listToSqlString(numberCodeList)+")";
			}
		}
		
		SQLQuery query = commonDao.getSqlQuery(sql);
		query.addScalar("value").
		addScalar("text").
		setResultTransformer(Transformers.aliasToBean(ListNumberCode.class));
		
		@SuppressWarnings("unchecked")
		List<ListNumberCode> results = query.list();
		return ReturnUtil.returnMap(1, results);
	}
	
	@Override
	public Map<String, Object> getSecurityUser(String username) {
		SecurityUser securityUser = (SecurityUser)commonDao.getObjectByUniqueCode("SecurityUser", "username", username);
		return ReturnUtil.returnMap(1, securityUser);
	}

	@Override
	public Map<String, Object> setUserPossessor(String username, String possessor) {
	    
		SecurityUser securityUser = (SecurityUser)commonDao.getObjectByUniqueCode("SecurityUser", "username", username);
		if(securityUser==null){
			return ReturnUtil.returnMap(0,"用户有误!");
		}
		if(possessor==null || "".equals(possessor)){
			if(MyRequestUtil.getDecideMap()!=null){
				return ReturnUtil.returnMap(0,"您不能取消该用户的机构!");
			}
		}

		securityUser.setPossessor(possessor);
		securityUser.setAlterDate(new Date());
		commonDao.update(securityUser);
		
		return ReturnUtil.returnMap(1,null);
	}

	@Override
	public Map<String, Object> editUserPassword(String username, String password) {
		
		SecurityUser user = (SecurityUser) commonDao.getObjectByUniqueCode("SecurityUser", "username", username);
		user.setPassword(EncryptionUtil.md5(password+"{"+username+"}"));
		user.setAlterDate(new Date());
		
		commonDao.update(user);
		
		return ReturnUtil.returnMap(1,null);
	}

    @Override
    public Map<String, Object> getNickNameByUsername(String username) {
        SecurityUser user = (SecurityUser) commonDao.getObjectByUniqueCode("SecurityUser", "username", username);
        return ReturnUtil.returnMap(1,user.getNickName());
    }
    
}