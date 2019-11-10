package xyz.work.custom.ctrl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.custom.svc.UserTagSvc;

@Controller
@RequestMapping(value = "UserTagWS")
public class UserTagWS {
	
	@Autowired
	UserTagSvc userTagSvc;
	
	@RequestMapping(value = "queryUserTagList")
	@ResponseBody
	public 	Map<String, Object> queryUserTagList(int page,int rows,String name){
		int pageSize = rows;
		int offset =  (page -1)*pageSize;
		return userTagSvc.queryUserTagList(pageSize, offset, name);
	}
	
	@RequestMapping(value = "addUserTag")
	@ResponseBody
	public 	Map<String, Object> addUserTag(String name,String remark){
		return userTagSvc.addUserTag(name, remark);
	}
	
	@RequestMapping(value = "editUserTag")
	@ResponseBody
	public 	Map<String, Object> editUserTag(String numberCode,String name,String remark){
		return userTagSvc.editUserTag(numberCode,name, remark);
	}
}
