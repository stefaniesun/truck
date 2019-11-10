package xyz.filter;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import xyz.exception.MyExceptionForLogin;
import xyz.util.Constant;
import xyz.work.custom.model.BizSecurityLogin;
import xyz.work.security.svc.KeySvc;

@Component
public class MyLoginCustomerFilter implements Filter{

	@Autowired
	private KeySvc keySvc;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		;
	}

	@Override
	public void doFilter(
			ServletRequest request, 
			ServletResponse response,
			FilterChain chain) 
					throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		HttpServletResponse httpServletResponse = (HttpServletResponse)response;
		
		//正式服必须关闭跨域访问，注释本行即可
//		httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
		
		String apikey = httpServletRequest.getParameter("TRUCK_LOGIN_KEY");
		if(apikey==null){
			Cookie[] ttt = httpServletRequest.getCookies();
			if(ttt!=null){
				for(Cookie cookie : ttt){
					if("TRUCK_LOGIN_KEY".equals(cookie.getName())){
						apikey = cookie.getValue();
					}
				}
			}
		}
		if(apikey==null){
			apikey = request.getParameter("apikey");
		}
		if(apikey==null){
			throw new MyExceptionForLogin("不存在有效登录信息,请重新登录！");
		}
		BizSecurityLogin bizSecurityLogin = keySvc.getBizSecurityLogin(apikey);
		
		if(bizSecurityLogin==null){
			throw new MyExceptionForLogin("不存在有效登录信息,请重新登录！");
		}else{
			Date date = new Date();
			long temp = bizSecurityLogin.getExpireDate().getTime()-date.getTime();
			if(temp<0){
				throw new MyExceptionForLogin("超过时限，请重新登录！");
			}else if(temp<Constant.sessionTimes){
				Date expireDate = new Date();
				expireDate.setTime(date.getTime()+Constant.sessionTimes);
				bizSecurityLogin.setExpireDate(expireDate);
				keySvc.updateBizSecurityLogin(bizSecurityLogin);
			}
		}
		request.setAttribute("bizSecurityLogin", bizSecurityLogin);
		chain.doFilter(httpServletRequest, httpServletResponse);
	}
	
	@Override
	public void destroy() {
		;
	}
}
