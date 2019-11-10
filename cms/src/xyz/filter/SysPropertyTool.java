package xyz.filter;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import xyz.work.security.model.SystemContent;
import xyz.work.security.svc.SystemContentSvc;

@Component
public class SysPropertyTool {
	private static Properties p = new Properties();
	private static Properties xml = new Properties();
	private static String path = "config/sys.properties";
	
	@Autowired
	private SystemContentSvc systemContentSvc;

	@PostConstruct
	public void load() {
		Map<String ,Object> resultData = systemContentSvc.getSystemContentList();
		if("1".equals(resultData.get("status").toString())){
			p = new Properties();
			@SuppressWarnings("unchecked")
			List<SystemContent> systemContentList = (List<SystemContent>)resultData.get("content");
			for (SystemContent systemContent : systemContentList){
				p.setProperty(systemContent.getNameKey(), systemContent.getValue());
			}
		}
	}
	
	@PostConstruct
	public void loadX() {
		try {
			InputStream  is = SysPropertyTool.class.getClassLoader().getResourceAsStream(path);
			xml.load(is);
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//此方法通过在配置文件里靠键取值
	public static String getValue(String key){
		return p.getProperty(key);
	}
	
	public static String getXmlValue(String key){
		return xml.getProperty(key);
	}
}
