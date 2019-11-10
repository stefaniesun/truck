package xyz.work.custom.svc;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface UserTagSvc {
	
	Map<String, Object> queryUserTagList(int pageSize,int offset,String name);
	
	Map<String, Object> addUserTag(String name,String remark);
	
	Map<String, Object> editUserTag(String  numberCode,String name,String remark);
	

}
