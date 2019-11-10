package xyz.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MyExceptionUtil{
    private static Logger log = LoggerFactory.getLogger(MyExceptionUtil.class);
	public static Throwable handleException(Exception ex){
		Throwable newEx = ex;
		//找出最根本的异常
		while(newEx.getCause()!=null){
			log.info("当前异常的名称是："+newEx.getClass().getSimpleName());
			newEx = newEx.getCause();
		}
		newEx.printStackTrace();
		return newEx;
	}
}
