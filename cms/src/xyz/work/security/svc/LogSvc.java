package xyz.work.security.svc;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;

import xyz.work.security.model.LogOper;




@Service
public interface LogSvc {
	public Map<String, Object> addLogOper(LogOper logOper);
	public Map<String, Object> getLogOperList(
			int offset,
			int pagesize,
			String username,
			String otherInfo,
            Date dateStart,
            Date dateEnd);
	
	public Map<String, Object> getLogOperListForCheck(
			int offset,
			int pagesize,
			String username,
            Date dateStart,
            Date dateEnd);
	
	public Map<String, Object> getLogOperListForOther(
            Date dateStart,
            Date dateEnd);
}
