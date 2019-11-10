package xyz.work.r_base.ctrl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.r_base.svc.R_ShipmentSvc;


@Controller
@RequestMapping(value = "/R_ShipmentWS")
public class R_ShipmentWS {
    
    @Autowired
    private R_ShipmentSvc r_ShipmentSvc;
    
    @RequestMapping(value="queryRShipmentList")
    @ResponseBody
    public Map<String, Object> queryRShipmentList(int page, int rows ,String cruiseMark , 
                                                  String mark , String areaMark , String airwayMark) {
        int pageSize = rows;
        int offset = (page-1)*pageSize;
        return r_ShipmentSvc.queryRShipmentList(offset, pageSize, cruiseMark, mark, areaMark, airwayMark);
    }
}