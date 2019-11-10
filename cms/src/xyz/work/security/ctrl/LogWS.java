package xyz.work.security.ctrl;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.security.svc.LogSvc;

@Controller
@RequestMapping(value="/LogWS")
public class LogWS{
	
	@Autowired
	private LogSvc logSvc;
	
	@RequestMapping(value="getLogOperList")
	@ResponseBody
	public Map<String, Object> getLogOperList(
			int page,
			int rows,
			String username,
			String otherInfo,
            Date dateStart,
            Date dateEnd){
		int pagesize = rows;
		int offset = (page-1)*pagesize;
		return logSvc.getLogOperList(offset, pagesize, username, otherInfo, dateStart, dateEnd);
	}
	
	@RequestMapping(value="getLogOperListForCheck")
	@ResponseBody
	public Map<String, Object> getLogOperListForCheck(
			int page,
			int rows,
			String username,
            Date dateStart,
            Date dateEnd){
		int pagesize = rows;
		int offset = (page-1)*pagesize;
		return logSvc.getLogOperListForCheck(offset, pagesize, username, dateStart, dateEnd);
	}
	
	@RequestMapping(value="getLogOperListForOther")
	@ResponseBody
	public Map<String, Object> getLogOperListForOther(
            Date dateStart,
            Date dateEnd){
		return logSvc.getLogOperListForOther(dateStart, dateEnd);
	}
}
