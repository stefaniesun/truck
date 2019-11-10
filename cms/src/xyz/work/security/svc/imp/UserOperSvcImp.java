package xyz.work.security.svc.imp;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.MyRequestUtil;
import xyz.filter.ReturnUtil;
import xyz.work.security.model.UserOper;
import xyz.work.security.svc.UserOperSvc;

@Service
public class UserOperSvcImp  implements UserOperSvc{
	@Autowired
	CommonDao commonDao;

	@Override
	public Map<String, Object> addUserOper(
			String keyCode,
			String content) {
		String username = MyRequestUtil.getSecurityLogin().getUsername();
		String sql = "delete from user_oper where username = '"+username+"' and key_code = '"+keyCode+"'";
		commonDao.getSqlQuery(sql).executeUpdate();
		commonDao.flush();
		
		if(content!=null && !"".equals(content) && keyCode!=null && !"".equals(keyCode)){
			UserOper userOper = new UserOper();
			userOper.setAddDate(new Date());
			userOper.setContent(content);
			userOper.setKeyCode(keyCode);
			userOper.setUsername(username);
			commonDao.save(userOper);
		}
		return ReturnUtil.returnMap(1,null);
	}
}
