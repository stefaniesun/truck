package xyz.work.truck.ctrl;

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
    public Map<String, Object> getIndexData(){
        return webSvc.getIndexData();
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
	
}
