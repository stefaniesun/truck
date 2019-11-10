package xyz.work.sale.ctrl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.sale.svc.PtviewWechatSvc;

@Controller
@RequestMapping(value = "/PtviewWechatWS")
public class PtviewWechatWS {

	
	@Autowired
	PtviewWechatSvc ptviewWechatSvc;
	
	@RequestMapping(value = "queryPtviewList")
    @ResponseBody
    public Map<String, Object> queryPtviewList(int page,int rows) {
		 int pageSize = rows;
	     int offset = (page - 1) * pageSize;
        return ptviewWechatSvc.queryPtviewList(pageSize,offset);
    }
	
	@RequestMapping(value = "getSelectData")
    @ResponseBody
    public Map<String, Object> getSelectData() {
        return ptviewWechatSvc.getSelectData();
    }
	
	@RequestMapping(value = "getQueryData")
    @ResponseBody
    public Map<String, Object> getQueryData(int page,int rows,String airway,String date,String price,String cruise) {
		 int pageSize = rows;
	     int offset = (page - 1) * pageSize;
		return ptviewWechatSvc.getQueryData(pageSize,offset,airway,date,price,cruise);
    }
	
	@RequestMapping(value = "getPtviewDetail")
    @ResponseBody
    public Map<String, Object> getPtviewDetail(String numberCode) {
		return ptviewWechatSvc.getPtviewDetail(numberCode);
    }
	
}
