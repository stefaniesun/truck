package xyz.work.security.svc;

import java.util.Map;

import org.springframework.stereotype.Service;

import xyz.work.security.model.SecurityUser;


@Service
public interface AdminUserSvc {
	
	public Map<String, Object> queryUserList(int offset,
                                		     int pagesize,
                                			 String username,
                                             String nickName,
                                             String position,
                                             String possessor,
                                             String enabled);
	
	public Map<String, Object> getUser(String username);
	
	public Map<String, Object> addUser(SecurityUser securityUser);
	
	public Map<String, Object> editUser(String username , String nickName);
	
	public Map<String, Object> editUserEnabled(String username);
	
	public Map<String, Object> deleteUser(String users);
	
	public Map<String, Object> setUserPosition(String username,String position);
	
	public Map<String, Object> getAllPosition();
	
	public Map<String, Object> getSecurityUserList(String q);

	public Map<String, Object> getSecurityUser(String username);
	
	public Map<String, Object> setUserPossessor(String username , String possessor);
	
	public Map<String, Object> editUserPassword(String username,String password);
	
	public Map<String, Object> getNickNameByUsername(String username);
	
	/*public Map<String, Object> setUserOrganization(
			String username,
			String organization);
	
	public Map<String, Object> addUserProvider(
			String username,
			String providers);
	
	public Map<String, Object> deleteUserProvider(
			String username,
			String providers);
	
	public Map<String, Object> addUserTkview(
			String username,
			String tkviews);
	
	public Map<String, Object> deleteUserTkview(
			String username,
			String tkviews);*/
	
}