package xyz.work.security.ctrl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.util.EncryptionUtil;
import xyz.work.security.model.SecurityUser;
import xyz.work.security.svc.AdminUserSvc;

@Controller
@RequestMapping(value="/AdminUserWS")
public class AdminUserWS{
	
	@Autowired
	private AdminUserSvc adminUserSvc;
	
	@RequestMapping(value="queryUserList")
	@ResponseBody
	public Map<String, Object> queryUserList(
			int page,
			int rows,
			String username,
			String nickName,
			String position,
			String possessor,
			String enabled){
		int pagesize = rows;
		int offset = (page-1)*pagesize;
		return adminUserSvc.queryUserList(offset, pagesize, username, nickName, position, possessor, enabled);
	}
	
	@RequestMapping(value="getUser")
	@ResponseBody
	public Map<String, Object> getUser(String username){
		return adminUserSvc.getUser(username);
	}
	
	@RequestMapping(value="addUser")
	@ResponseBody
	public Map<String, Object> addUser(
			String username,
			String nickName){
		SecurityUser securityUser = new SecurityUser();
		securityUser.setUsername(username);
		securityUser.setPassword(EncryptionUtil.md5("49ba59abbe56e057{"+username+"}"));
		securityUser.setNickName(nickName);
		return adminUserSvc.addUser(securityUser);
	}
	
	@RequestMapping(value="editUser")
	@ResponseBody
	public Map<String, Object> editUser(
			String username,
			String nickName){
		return adminUserSvc.editUser(username, nickName);
	}

	
	@RequestMapping(value="deleteUser")
	@ResponseBody
	public Map<String, Object> deleteUser(
			String users){
		return adminUserSvc.deleteUser(users);
	}
	
	@RequestMapping(value="editUserEnabled")
	@ResponseBody
	public Map<String, Object> editUserEnabled(
			String username){
		return adminUserSvc.editUserEnabled(username);
	}
	
	@RequestMapping(value="setUserPosition")
	@ResponseBody
	public Map<String, Object> setUserPosition(
			String username,
			String position){
		return adminUserSvc.setUserPosition(username, position);
	}
	
	@RequestMapping(value="getAllPosition")
	@ResponseBody
	public Map<String, Object> getAllPosition(){
		return adminUserSvc.getAllPosition();
	}
	
	@RequestMapping(value="getSecurityUserList")
	@ResponseBody
	public Map<String, Object> getSecurityUserList(String q){
		return adminUserSvc.getSecurityUserList(q);
	}
	
	@RequestMapping(value="setUserPossessor")
	@ResponseBody
	public Map<String, Object> setUserPossessor(String username,String possessor){
		return adminUserSvc.setUserPossessor(username, possessor);
	}
	
	@RequestMapping(value="editUserPassword")
	@ResponseBody
	public Map<String, Object> editUserPassword(String username,String password){
		return adminUserSvc.editUserPassword(username, password);
	}
	
	/**
	 * 通过用户名获取昵称
	 * @param username
	 * @return Map<String,Object>
	 * @author :huying
	 * @date : 2016-7-25下午4:58:03
	 */
	@RequestMapping(value="getNickNameByUsername")
    @ResponseBody
	public Map<String,Object> getNickNameByUsername(String username){
	    return adminUserSvc.getNickNameByUsername(username);
	}
	
	/*
	 * 管理员 - 用户管理 - 给机构用户设置机构
	 * 
	 * 
	@RequestMapping(value="setUserOrganization")
	@ResponseBody
	public Map<String, Object> setUserOrganization(String username,String organization){
		return adminUserSvc.setUserOrganization(username, organization);
	}
	
	
	 * 管理员 - 用户管理 - 给供应商用户添加供应商
	 * 
	 * 
	@RequestMapping(value="addUserProvider")
	@ResponseBody
	public Map<String, Object> addUserProvider(String username,String providers){
		return adminUserSvc.addUserProvider(username, providers);
	}
	
	
	 * 管理员 - 用户管理 - 从供应商用户删除供应商
	 * 
	 * 
	@RequestMapping(value="deleteUserProvider")
	@ResponseBody
	public Map<String, Object> deleteUserProvider(String username,String providers){
		return adminUserSvc.deleteUserProvider(username, providers);
	}
	
	
	 * 管理员 - 用户管理 - 给供应商用户添加供应商
	 * 
	 * 
	@RequestMapping(value="addUserTkview")
	@ResponseBody
	public Map<String, Object> addUserTkview(String username,String tkviews){
		return adminUserSvc.addUserTkview(username, tkviews);
	}
	
	
	 * 管理员 - 用户管理 - 从供应商用户删除供应商
	 * 
	 * 
	@RequestMapping(value="deleteUserTkview")
	@ResponseBody
	public Map<String, Object> deleteUserTkview(String username,String tkviews){
		return adminUserSvc.deleteUserTkview(username, tkviews);
	}*/
}
