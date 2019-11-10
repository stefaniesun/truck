package xyz.work.menu.ctrl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.menu.svc.QueryShipmentSvc;

@Controller
@RequestMapping(value = "/QueryShipmentWS")
public class QueryShipmentWS {
    @Autowired
    private QueryShipmentSvc queryShipmentSvc;
    
    @RequestMapping(value="queryShipmentList")
    @ResponseBody
    public Map<String, Object> queryShipmentList(int page , int rows , String startDate , String endDate ,
                                                 String startPlace , String port , String mark , String cruise ,
                                                 String sort , String order){
        int pageSize = rows;
        int offset = (page - 1) * pageSize;
        return queryShipmentSvc.queryShipmentList(offset, pageSize, startDate, endDate, startPlace, port, mark, cruise, sort, order);
    }
    
}