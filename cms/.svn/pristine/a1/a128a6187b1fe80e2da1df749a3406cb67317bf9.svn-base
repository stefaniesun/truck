package xyz.work.r_base.ctrl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.r_base.svc.R_TripSvc;

@Controller
@RequestMapping(value = "/R_TripWS")
public class R_TripWS {
    @Autowired
    private R_TripSvc r_TripSvc;
    
    @RequestMapping(value="importTxtOper")
    @ResponseBody
    public Map<String, Object> importTxtOper() {
        return r_TripSvc.importTxtOper();
    }
    
    @RequestMapping(value="queryRTripList")
    @ResponseBody
    public Map<String, Object> queryRTripList(String airway) {
        return r_TripSvc.queryRTripList(airway);
    }
    
}