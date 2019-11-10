package xyz.filter;


import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import xyz.util.Constant;
import xyz.work.security.model.LogOper;
import xyz.work.security.model.SecurityApi;
import xyz.work.security.model.SecurityLogin;
import xyz.work.security.svc.LogSvc;

@Aspect
@Component
public class LogAOP
{
	@Autowired
	private LogSvc logSvc;
	
	private Logger log = LoggerFactory.getLogger(LogAOP.class);
	
	//方法执行的前后调用
	//@AfterReturning(value="@annotation(xyz.filter.LogAOPAnnotation)",returning="map")
	//@AfterReturning(value="@annotation(logAOPAnnotation)",returning="map")
	@AfterReturning(value="execution(* xyz.work.*.ctrl.*WS.*(..))",returning="map")
    public void afterReturnMethod(
    		JoinPoint point,
    		Object map) throws Throwable{
		LogOper logOper = new LogOper();
		{
			SecurityLogin securityLogin = MyRequestUtil.getSecurityLogin();
			if(securityLogin==null){
				return;
			}
			log.info(securityLogin.getUsername());
			logOper.setUsername(securityLogin.getUsername());
		}
		{
			SecurityApi securityApi = MyRequestUtil.getSecurityApi();
			if(securityApi==null){
				return;
			}
			log.info(securityApi.getNameCn());
			log.info(securityApi.getUrl());
			if(securityApi.getIsWork()==0){
				return;
			}
			logOper.setInterfacePath(securityApi.getUrl());
			logOper.setIsWork(securityApi.getIsWork());
			logOper.setRemark(securityApi.getNameCn());
		}
		{
			if(map==null){
				return;
			}
			@SuppressWarnings("rawtypes")
			Map jsonMap = MyRequestUtil.getRequest().getParameterMap();
			String jsonStr = JSON.toJson(jsonMap);
			logOper.setDataContent(jsonStr);
			if(map instanceof Map){
				@SuppressWarnings("unchecked")
				int flagResult = (Integer)((Map<String, Object>)map).get(Constant.result_status);
				logOper.setFlagResult(flagResult);
			}else{
				return;
			}
		}
		logOper.setIpInfo(MyRequestUtil.getIp());
		logSvc.addLogOper(logOper);
		//point.proceed();如果有返回值，可继续进行,只有环绕通知有返回值
    }
}
