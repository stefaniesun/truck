package xyz.work.security.ctrl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.security.svc.UserOperSvc;

@Controller
@RequestMapping(value="/UserOperWS")
public class UserOperWS {

	@Autowired
	UserOperSvc userOperSvc;
	/**
	 *  管理员--机构--添加机构
	 */
	@RequestMapping(value="addUserOper")
	@ResponseBody
	public Map<String ,Object> addUserOper(
			String keyCode,
			String content){
		
		return userOperSvc.addUserOper(keyCode, content);
	}
}
