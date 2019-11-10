package xyz.work.security.svc;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface SystemContentSvc{
	
	public Map<String ,Object> querySystemContentList(
			int offset,
			int pagesize,
			String nameKey,
            String nameCn);
	
	public Map<String ,Object> getSystemContentList();
	
	public Map<String ,Object> addSystemContent(
			String nameKey,
			String nameCn,
			String value,
			String remark);
	
	public Map<String ,Object> editSystemContent(
			String numberCode,
			String nameKey,
			String nameCn,
			String value,
			String remark);
	
	public Map<String ,Object> deleteSystemContent(String numberCodes);
}
