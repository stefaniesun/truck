package xyz.work.security.ctrl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.filter.SysPropertyTool;
import xyz.work.security.svc.SystemContentSvc;

@Controller
@RequestMapping(value="/SystemContentClientWS")
public class SystemContentClientWS {

	@Autowired
	SystemContentSvc systemContentSvc;
	
	@Autowired
	SysPropertyTool sysPropertyTool;
	
	/**
	 * 管理员--系统配置——client--查询配置列表
	 */
	@RequestMapping(value="querySystemContentList")
	@ResponseBody
	public Map<String, Object> querySystemContentList(
			int page,
			int rows,
			String nameKey,
			String nameCn){
		
		int pagesize = rows;
		int offset = (page-1)*pagesize;
		
		return systemContentSvc.querySystemContentList(offset, pagesize, nameKey,nameCn);
	}
	
	/**
	 * SysPropertyTool load 方法加载配置.
	 */
	@RequestMapping(value="getSystemContentList")
	@ResponseBody
	public Map<String, Object> getSystemContentList(){
		
		return systemContentSvc.getSystemContentList();
	}
	
	/**
	 * 管理员--系统配置——client--新增配置信息
	 */
	@RequestMapping(value="addSystemContent")
	@ResponseBody
	public Map<String, Object> addSystemContent(
			String nameKey,
			String nameCn,
			String value,
			String remark){
		Map<String ,Object> result = systemContentSvc.addSystemContent(nameKey, nameCn, value, remark);
		sysPropertyTool.load();
		return result;
		
	}
	
	/**
	 * 管理员--系统配置——client--修改配置信息
	 */
	@RequestMapping(value="editSystemContent")
	@ResponseBody
	public Map<String, Object> editSystemContent(
			String numberCode,
			String nameKey,
			String nameCn,
			String value,
			String remark){
		Map<String ,Object> result = systemContentSvc.editSystemContent(numberCode, nameKey, nameCn, value, remark);
		sysPropertyTool.load();
		return result;
	}
	
	/**
	 * 管理员--系统配置——client--删除键配置信息
	 */
	@RequestMapping(value="deleteSystemContent")
	@ResponseBody
	public Map<String, Object> deleteSystemContent(String numberCodes){
		Map<String ,Object> result = systemContentSvc.deleteSystemContent(numberCodes);
		sysPropertyTool.load();
		return result;
	}
}
