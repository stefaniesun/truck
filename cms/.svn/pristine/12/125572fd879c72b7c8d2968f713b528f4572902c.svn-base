package xyz.work.r_base.ctrl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.r_base.svc.R_AreaSvc;

@Controller
@RequestMapping(value = "/R_AreaWS")
public class R_AreaWS {
    @Autowired
    private R_AreaSvc r_AreaSvc;
    
    @RequestMapping(value="queryRAreaList")
    @ResponseBody
    public Map<String, Object> queryRAreaList(int page, int rows, String mark) {
        int pageSize = rows;
        int offset = (page-1)*pageSize;
        return r_AreaSvc.queryRAreaList(offset, pageSize, mark);
    }
    
}