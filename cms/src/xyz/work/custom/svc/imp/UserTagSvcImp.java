package xyz.work.custom.svc.imp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.ReturnUtil;
import xyz.util.ConstantMsg;
import xyz.util.StringTool;
import xyz.util.UUIDUtil;
import xyz.work.custom.model.UserTag;
import xyz.work.custom.svc.UserTagSvc;

@Service
public class UserTagSvcImp implements UserTagSvc {
	
	@Autowired
	CommonDao commonDao;

	@Override
	public Map<String, Object> queryUserTagList(int pageSize, int offset,
			String name) {
		StringBuffer sql = new StringBuffer("from UserTag c where 1=1 ");
        if(StringTool.isNotNull(name)){
            sql.append(" and c.name like '%"+ name +"%'");
        }
        sql.append(" order by c.alterDate desc");
        
        Query countQuery = commonDao.getQuery("select count(*) " + sql.toString());
        Number countNum = (Number)countQuery.uniqueResult();
        int count = countNum == null ? 0 : countNum.intValue();

        Query query = commonDao.getQuery(sql.toString());
        query.setMaxResults(pageSize);
        query.setFirstResult(offset);
        @SuppressWarnings("unchecked")
        List<UserTag> list = query.list();
        
        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", count);
        mapContent.put("rows", list);

        return ReturnUtil.returnMap(1, mapContent);
	}

	@Override
	public Map<String, Object> addUserTag(String name, String remark) {
		
		UserTag userTag = new UserTag();
		Date date= new Date();
		userTag.setAddDate(date);
		userTag.setAlterDate(date);
		userTag.setName(name);
		userTag.setNumberCode(UUIDUtil.getUUIDStringFor32());
		userTag.setRemark(remark);
		
		commonDao.save(userTag);
		return ReturnUtil.returnMap(1, null);
	}

	@Override
	public Map<String, Object> editUserTag(String numberCode, String name,
			String remark) {
		
		if (StringTool.isEmpty(numberCode)) {
			return ReturnUtil.returnMap(0, ConstantMsg.user_tag_numberCode_null);
		}
		
		UserTag userTag = (UserTag)commonDao.getObjectByUniqueCode("UserTag", "numberCode", numberCode);
		if (userTag == null) {
			return ReturnUtil.returnMap(0, ConstantMsg.user_tag_numberCode_null);
		}
		
		userTag.setAlterDate(new Date());
		userTag.setName(name);
		userTag.setRemark(remark);
		commonDao.update(userTag);
		return ReturnUtil.returnMap(1, null);
	}
}
