package xyz.work.truck.ctrl;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.truck.svc.TruckSvc;
import xyz.work.truck.svc.WebSvc;

@Controller
@RequestMapping(value = "/WebWS")
public class WebWS {

	@Autowired
	WebSvc webSvc;
	
	@RequestMapping(value = "getIndexData")
    @ResponseBody
    public Map<String, Object> getIndexData(int page, int rows){
		int pageSize = rows;
		int offset = (page - 1) * pageSize;
        return webSvc.getIndexData(offset,pageSize);
    }
	
	@RequestMapping(value = "getSearchData")
    @ResponseBody
    public Map<String, Object> getIndexData(int page, int rows,String truckType){
		int pageSize = rows;
		int offset = (page - 1) * pageSize;
        return webSvc.getSearchData(offset,pageSize,truckType);
    }
	
	@RequestMapping(value = "truckDetail")
    @ResponseBody
    public Map<String, Object> truckDetail(String numberCode){
        return webSvc.truckDetail(numberCode);
    }
	
	@RequestMapping(value = "truckViewOper")
    @ResponseBody
    public Map<String, Object> truckViewOper(String truck, String customer) {
        return webSvc.truckViewOper(truck,customer);
    }
	
	@RequestMapping(value = "wechatOper")
    @ResponseBody
    public Map<String, Object> wechatOper(String code) {
        return webSvc.wechatOper(code);
    }
	
	@RequestMapping(value = "xcxWechatOper")
    @ResponseBody
    public Map<String, Object> xcxWechatOper(String code,String nickName,String img,String city,String gender,String province) {
        return webSvc.xcxWechatOper(code,nickName,img,city,gender,province);
    }
	
	@RequestMapping(value = "getToken")
    @ResponseBody
    public Map<String, Object> getToken() {
        return webSvc.getToken();
    }
	
	@RequestMapping(value = "msgReciveOper")
    @ResponseBody
    public Map<String, Object> msgReciveOper(String signature,String timestamp,String nonce,String echostr) {
        return webSvc.msgReciveOper();
    }
	
}
