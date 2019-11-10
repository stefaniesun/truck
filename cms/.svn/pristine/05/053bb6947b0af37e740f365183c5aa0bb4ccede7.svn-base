package xyz.work.system.ctrl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.filter.ReturnUtil;
import xyz.filter.RmiUtil;
import xyz.filter.SysPropertyTool;
import xyz.work.security.model.SecurityApi;
import xyz.work.security.model.SecurityLogin;
import xyz.work.security.svc.KeySvc;

@Controller
public class RmiWS{
	
	@Autowired
	KeySvc keySvc;
	
	@Autowired
	RmiUtil rmiUtil;
	
	private Logger log = LoggerFactory.getLogger(RmiWS.class);
	
	@RequestMapping(value="/{ta}/{tb}")
	@ResponseBody
	public Object loadData(
			HttpServletRequest request){
		SecurityLogin securityLogin = (SecurityLogin)request.getAttribute("securityLogin");
		SecurityApi securityApi = (SecurityApi)request.getAttribute("securityApi");
		
		StringBuffer urlString = new StringBuffer();
		if(securityApi.getFlagServer()==1){
			String serverUrl = SysPropertyTool.getValue("server_orderUrl");
			if(serverUrl==null || "".equals(serverUrl)){
				return ReturnUtil.returnMap(0,"数据分布异常，请联系系统管理员！【"+securityApi.getUrl()+"】");
			}
			urlString.append(serverUrl);
		}else if(securityApi.getFlagServer()==2){
			String serverUrl = SysPropertyTool.getValue("server_productUrl");
			if(serverUrl==null || "".equals(serverUrl)){
				return ReturnUtil.returnMap(0,"数据分布异常，请联系系统管理员！【"+securityApi.getUrl()+"】");
			}
			urlString.append(serverUrl);
		}else if(securityApi.getFlagServer()==3){
			String serverUrl = SysPropertyTool.getValue("server_b2bUrl");
			if(serverUrl==null || "".equals(serverUrl)){
				return ReturnUtil.returnMap(0,"数据分布异常，请联系系统管理员！【"+securityApi.getUrl()+"】");
			}
			urlString.append(serverUrl);
		}else{
			return ReturnUtil.returnMap(0,"接口未注册，请联系系统管理员！【"+securityApi.getUrl()+"】");
		}
		
		urlString.append(request.getServletPath());
		
		
		String urlEnd = urlString.toString();
		if(securityApi.getFlagServer()==3){
			urlEnd = urlEnd.replaceAll("\\.do", "\\.rmi");
		}
		log.info(urlEnd);
		
		Map<String, String> accessoryParam = new HashMap<String, String>();
		accessoryParam.put("currentUsername",securityLogin.getUsername());
		
		return rmiUtil.loadData(request,urlEnd, accessoryParam);
	}
}
