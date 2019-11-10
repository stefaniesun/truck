package xyz.work.r_base.ctrl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.r_base.svc.R_VoyageSvc;

@Controller
@RequestMapping(value = "/R_VoyageWS")
public class R_VoyageWS {
    @Autowired
    private R_VoyageSvc r_VoyageSvc;
    
    @RequestMapping(value="queryRVoyageList")
    @ResponseBody
    public Map<String, Object> queryRVoyageList(int page, int rows, String shipment){
        int pageSize = rows;
        int offset = (page-1)*pageSize;
        return r_VoyageSvc.queryRVoyageList(offset, pageSize, shipment);
    }
    
}