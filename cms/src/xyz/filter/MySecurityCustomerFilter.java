package xyz.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import xyz.exception.MyExceptionForRole;
import xyz.work.security.model.LogOper;
import xyz.work.security.svc.KeySvc;
import xyz.work.security.svc.LogSvc;

@Component
public class MySecurityCustomerFilter implements Filter{

	@Autowired
	private LogSvc logSvc;
	
	@Autowired
	private KeySvc keySvc;
	
	private static String[] publicUrls = new String[]{
		
		"/Truck/getOrderList.visa",
		"/CustomerWS/getVisaflowList.visa",
		"/VisaHandleWS/getChooseInfo.visa",
		"/VisaHandleWS/getVisaflowFileList.visa",
		
	    "/VisaflowHandleWS/getVisaflowLowerTable.wq",
	    "/VisaflowHandleWS/getExeceptionClause.wq",
	     "/VisaflowHandleWS/getTableDicByDicCode.wq",
        
	};
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		;
	}

	@Override
	public void doFilter(
			ServletRequest request1, 
			ServletResponse response1,
			FilterChain chain)
					throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)request1;
		String path = request.getServletPath();
		boolean flagSuccess = false;
		
		System.out.println("path=="+path);
		for(String url : publicUrls){
			if(path.equals(url)){
				flagSuccess = true;
				break;
			}
		}
		flagSuccess = true;
		if(flagSuccess==false){
			throw new MyExceptionForRole("您无权访问此接口！");
		}
		{
			LogOper logOper = new LogOper();
			logOper.setUsername("mySecurityCustomerFilter");
			logOper.setInterfacePath(path);
			logOper.setIsWork(1);
			logOper.setRemark("mySecurityCustomerFilter");
			@SuppressWarnings("rawtypes")
			Map jsonMap = request.getParameterMap();
			String jsonStr = JSON.toJson(jsonMap);
			logOper.setDataContent(jsonStr);
			logOper.setIpInfo(MyRequestUtil.getIp(request));
			logSvc.addLogOper(logOper);
		}
		chain.doFilter(request1, response1);
	}
	
	@Override
	public void destroy() {
		;
	}
}
