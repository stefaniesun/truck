package xyz.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xyz.exception.MyExceptionUtil;
import xyz.util.Constant;

public class MyExceptionFilter implements Filter{
	
	private Logger log = LoggerFactory.getLogger(MyExceptionFilter.class);
	
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
		HttpServletResponse response = (HttpServletResponse)response1;
		try{
			chain.doFilter(request1, response1);
		}catch(Exception ex){
			log.info("目标"+request.getServletPath()+"IP"+MyRequestUtil.getIp(request)+"参数"+JSON.toJson(request.getParameterMap()));
			log.error("过滤器异常", ex);
			Throwable newEx = MyExceptionUtil.handleException(ex);
			String exString = newEx.getClass().getSimpleName();
			Map<String, Object> map = new HashMap<String, Object>();
			String model = "../xyzsecurity/100_exception.html";
			if("MySQLIntegrityConstraintViolationException".equals(exString)){
				map.put(Constant.result_msg, "操作失败：对象已存在!");
			}else if("StaleObjectStateException".equals(exString)){
				map.put(Constant.result_msg, "操作失败：数据正被系统占用，请稍后重试！");
			}else if("MySQLSyntaxErrorException".equals(exString)){
				map.put(Constant.result_msg, "查询失败，可能您输入了特殊字符！如无法解决，请截全页图发给系统运营方。");
			}else if("HttpMessageNotWritableException".equals(exString)){
				;
			}else if(exString.contains("MyExceptionFor")){
				map.put(Constant.result_msg, "操作失败："+newEx.getMessage()+"");
			}else if(exString.contains("SizeLimitExceededException")){
				map.put(Constant.result_msg, "系统异常：文件尺寸超过限制！");
			}else if(exString.contains("NestedServletException")){
			    map.put(Constant.result_msg, "系统异常：模版参数不匹配!");
			}else{
				map.put(Constant.result_msg, "系统异常：请将整个网页截图给系统管理员！【"+newEx.getMessage()+"】");
			}
			
			StringBuffer otherInfo = new StringBuffer();
			otherInfo.append("ip:"+request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"\r\n");
			otherInfo.append("servletPath:"+request.getServletPath()+"\r\n");
			otherInfo.append("param:"+JSON.toJson(request.getParameterMap())+"\r\n");
			otherInfo.append("cookies:"+JSON.toJson(request.getCookies())+"\r\n");
			
			boolean isAjax = false;
			String requestType =(String)request.getHeader("X-Requested-With");
			if(requestType != null && requestType.equals("XMLHttpRequest")){
				isAjax = true;
			}
			if(ServletFileUpload.isMultipartContent(request)){
				isAjax = true;
			}
			if (isAjax) {
				map.put(Constant.result_status, 0);
				PrintWriter pw = null;
				try {
					if(response!=null){
						response.setCharacterEncoding("utf-8");
						response.setContentType("text/json;charset=utf-8");
						pw = response.getWriter();
						if(pw!=null){
							pw.print(JSON.toJson(map));
						}
					}
				} catch (Exception e) {
					log.info(e.getMessage());
				}finally{
					if(pw!=null){
						pw.close();
					}
				}
			}else{
				request.getRequestDispatcher(model).forward(request, response);
			}
		}
	}
	
	@Override
	public void destroy() {
		;
	}
}
