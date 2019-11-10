package xyz.work.r_base.ctrl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.r_base.svc.R_PortSvc;

@Controller
@RequestMapping(value = "/R_PortWS")
public class R_PortWS {
    @Autowired
    private R_PortSvc r_PortSvc;
    
    @RequestMapping(value="importExcelPortOper")
    @ResponseBody
    public Map<String, Object> importExcelPortOper(String excelPath) {
        return r_PortSvc.importExcelPortOper(excelPath);
    }
    
    @RequestMapping(value="queryRPortList")
    @ResponseBody
    public Map<String, Object> queryRPortList(int page, int rows ,String nameCn, String mark, String location) {
        int pageSize = rows;
        int offset = (page-1)*pageSize;
        return r_PortSvc.queryRPortList(offset, pageSize, nameCn, mark, location);
    }
    
}