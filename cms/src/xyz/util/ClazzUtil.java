package xyz.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * 对象比较
 * 
 */
public class ClazzUtil {
    
    private static Logger log = LoggerFactory.getLogger(ClazzUtil.class);
    
	public static boolean contrastObject(String clazzFullName ,Object obj1 ,Object obj2 ,String[] exceptionFields){
		boolean flag = true;
		try{
			List<String> exceptionFieldList = new ArrayList<String>();
			if(exceptionFields != null){
				exceptionFieldList = Arrays.asList(exceptionFields);
			}
			Class<?> clazz = Class.forName(clazzFullName);
			Method[] methods = clazz.getMethods();
			for (int j = 0; j < methods.length; j++) {
				Method method = methods[j];
				String methodName = method.getName();
				if (methodName.startsWith("get")) {
					String tField = methodName.substring(3 ,methodName.length());
					String field = tField.substring(0,1).toLowerCase()+tField.substring(1, tField.length());
					
					if(exceptionFieldList.contains(field)){
						continue;
					}
					
					Object[] params = new Object[]{}; //get 方法不需要参数
					
					Object result1 = method.invoke(obj1, params);
					Object result2 = method.invoke(obj2, params);
					
					String value1 = result1 == null?"":result1.toString().trim();
					String value2 = result2 == null?"":result2.toString().trim();
					
					if(!value1.equals(value2)){
						flag = false;
						break;
					}
				}
			}
		}catch(Exception e){
		    log.error(e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * 克隆对象
	 * @author Xzavier Sun
	 * @param obj 原对象
	 * @param notCopyFields 不复制的字段们
	 * @return 复制的新对象
	 * @throws NoSuchFieldException ,SecurityException ,IllegalAccessException ,InstantiationException
	 */
	public static Object cloneObj(Object obj ,String ...notCopyFields){
	    Object result = null;
	    notCopyFields = notCopyFields == null?new String[]{}:notCopyFields;
        try{
            Class<?> clazz = obj.getClass();
            result = (Object)clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for (Field f : fields) {
                if(Arrays.asList(notCopyFields).contains(f.getName())){
                    continue;
                }
                Field field = result.getClass().getDeclaredField(f.getName());
                field.setAccessible(true);
                f.setAccessible(true);
                field.set(result, f.get(obj));
            }
        }catch(Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}
