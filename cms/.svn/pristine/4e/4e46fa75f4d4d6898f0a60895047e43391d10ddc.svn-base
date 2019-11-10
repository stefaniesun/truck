package xyz.work.r_base.ctrl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.r_base.svc.R_AirwaySvc;

@Controller
@RequestMapping(value = "/R_AirwayWS")
public class R_AirwayWS {
    @Autowired
    private R_AirwaySvc r_AirwaySvc;
    
    @RequestMapping(value="queryRAirwayList")
    @ResponseBody
    public Map<String, Object> queryRAirwayList(int page, int rows , String areaMark , String mark) {
        int pageSize = rows;
        int offset = (page-1)*pageSize;
        return r_AirwaySvc.queryRAirwayList(offset, pageSize, areaMark, mark);
    }
    
}