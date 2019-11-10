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
public class MyReserveFilter implements Filter {

    @Autowired
    private KeySvc keySvc;

    @Autowired
    private LogSvc logSvc;

    private static String[] publicUrls = new String[] {
        "/ReserveWS/reserveLogin.reserve",
        "/ReserveWS/queryOrderListByTid.reserve",
        "/ReserveWS/savePersonData.reserve",
        "/ReserveWS/getPersonByNumberCode.reserve"
    };
    
    @Override
    public void init(FilterConfig filterConfig)
        throws ServletException {
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
        boolean flag = false;
        for(String url : publicUrls){
            if(path.equals(url)){
                flag = true;
            }
        }
        {
            LogOper logOper = new LogOper();
            logOper.setUsername("myReserveFilter");
            logOper.setInterfacePath(path);
            logOper.setIsWork(1);
            logOper.setRemark("myReserveFilter");
            @SuppressWarnings("rawtypes")
            Map jsonMap = request.getParameterMap();
            String jsonStr = JSON.toJson(jsonMap);
            logOper.setDataContent(jsonStr);
            logOper.setIpInfo(MyRequestUtil.getIp(request));
            logSvc.addLogOper(logOper);
        }
        if(flag){
            chain.doFilter(request1, response1);
        }else{
            throw new MyExceptionForRole("您无权访问!");
        }
    }

    @Override
    public void destroy() {
        ;
    }
}
