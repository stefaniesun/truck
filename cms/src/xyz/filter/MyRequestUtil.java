package xyz.filter;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import xyz.exception.MyExceptionForRole;
import xyz.util.ConstantMsg;
import xyz.work.custom.model.BizSecurityLogin;
import xyz.work.security.model.SecurityApi;
import xyz.work.security.model.SecurityLogin;

public class MyRequestUtil{
	
	public static HttpServletRequest getRequest(){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		return request;
	}
	
	public static SecurityLogin getSecurityLogin(){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		SecurityLogin securityLogin = (SecurityLogin)request.getAttribute("securityLogin");
		return securityLogin;
	}
	
	public static String getUsername(){
	    return getSecurityLogin().getUsername();
	}
	
	public static SecurityApi getSecurityApi(){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		SecurityApi securityApi = (SecurityApi)request.getAttribute("securityApi");
		return securityApi;
	}
	
	public static BizSecurityLogin getBizSecurityLogin(){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		BizSecurityLogin bizSecurityLogin = (BizSecurityLogin)request.getAttribute("bizSecurityLogin");
		return bizSecurityLogin;
	}
	
	public static Map<String,List<String>> getDecideMap(){
		String decideStr = getSecurityLogin().getPossessor();
		if(decideStr==null||"".equals(decideStr)){
			return null;
		}else{
			@SuppressWarnings("unchecked")
			Map<String, List<String>> t = JSON.toObject(decideStr, Map.class);
			return t;
		}
	}
	
	public static void decidePowerIsAll(){
		String decideStr = getSecurityLogin().getPossessor();
		if(decideStr==null||"".equals(decideStr)){
			return;
		}
		throw new MyExceptionForRole(ConstantMsg.prohibit_possessor_do);
	}
	
	public static String getIp(){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		String ip = request.getHeader("X-Real-IP");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
			ip = request.getHeader("X-Forwarded-For");   
		}  
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
			ip = request.getHeader("Proxy-Client-IP");   
		}  
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
			ip = request.getHeader("WL-Proxy-Client-IP");   
		}   
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
			ip = request.getHeader("HTTP_CLIENT_IP");
		}  
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");   
		} 
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
			ip = request.getRemoteAddr();   
		}   
		//对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割   
	     if(ip!=null && ip.length()>15){ 
	         if(ip.indexOf(",")>0){   
	        	 ip = ip.substring(0,ip.indexOf(","));   
	         }   
	     }   
		return ip;
	}
	
	public static String getIp(HttpServletRequest request){
		String ip = request.getHeader("X-Real-IP");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
			ip = request.getHeader("X-Forwarded-For");   
		}  
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
			ip = request.getHeader("Proxy-Client-IP");   
		}  
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
			ip = request.getHeader("WL-Proxy-Client-IP");   
		}   
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
			ip = request.getHeader("HTTP_CLIENT_IP");
		}  
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");   
		} 
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
			ip = request.getRemoteAddr();   
		}   
		//对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割   
	     if(ip!=null && ip.length()>15){ 
	         if(ip.indexOf(",")>0){   
	        	 ip = ip.substring(0,ip.indexOf(","));   
	         }   
	     }   
		return ip;
	}
	
	public static String getPossessor(){
        String possessor = getSecurityLogin().getPossessor();
        if(possessor==null||"".equals(possessor)){
            return null;
        }
        return possessor;
    }
}
