package xyz.work.excel.ctrl;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.excel.svc.ExcelSvc;


@Controller
@RequestMapping(value = "/ExcelWS")
public class ExcelWS {
    @Autowired
    ExcelSvc excelSvc;

    @RequestMapping(value = "importExcelDataOper")
    @ResponseBody
    public Map<String, Object> importExcelDataOper(String excelPath) {
        return excelSvc.importExcelDataOper(excelPath);
    }

    @RequestMapping(value = "exportExcelDataOper")
    @ResponseBody
    public Map<String, Object> exportExcelDataOper(String provider , String cruise ,
                                                   String shipment , String cabin) {
        return excelSvc.exportExcelDataOper(provider, cruise, shipment, cabin);
    }
    
    @RequestMapping(value="queryExcelLogList")
    @ResponseBody
    public Map<String, Object> queryExcelLogList(int page , int rows) {
        int pageSize = rows;
        int offset = (page-1)*pageSize;
        return excelSvc.queryExcelLogList(offset, pageSize);
    }
    
    @RequestMapping(value = "downTemplate")
    @ResponseBody
    public Map<String, Object> downTemplate(String cruise ,String shipment , String cabin) {
        return excelSvc.downTemplate(cruise, shipment, cabin);
    }
    
    @RequestMapping(value = "downCompareData")
    @ResponseBody
    public Map<String, Object> downCompareData(String cruise,String shopName,String sourceCode) {
        return excelSvc.downCompareData(cruise,shopName,sourceCode);
    }
    
    @RequestMapping(value = "importSkuExcelOper")
    @ResponseBody
    public Map<String, Object> importSkuExcelOper(String excelPath) {
        return excelSvc.importSkuExcelOper(excelPath);
    }
    
    @RequestMapping(value = "transferDataOper")
    @ResponseBody
    public Map<String, Object> transferDataOper() {
        return excelSvc.transferDataOper();
    }
    
    
}