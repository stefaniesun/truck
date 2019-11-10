package xyz.work.menu.ctrl;


import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.menu.svc.QueryResourcesSvc;


@Controller
@RequestMapping(value = "/QueryResourcesWS")
public class QueryResourcesWS {
    @Autowired
    private QueryResourcesSvc queryResourcesSvc;

    @RequestMapping(value="queryTkviewStockList")
    @ResponseBody
    public Map<String, Object> queryTkviewStockList(BigDecimal minPrice , BigDecimal maxPrice ,
                                                    String startDate , String endDate ,
                                                    String startPlace , String toPlace ,
                                                    String cruise , String airway , String cabin ,
                                                    BigDecimal days , BigDecimal nights ,
                                                    String provider) {
        return queryResourcesSvc.queryTkviewStockList(minPrice, maxPrice, startDate, endDate,
            startPlace, toPlace, cruise, airway, cabin, days, nights, provider);
    }

    @RequestMapping(value="clickDateOper")
    @ResponseBody
    public Map<String, Object> clickDateOper(String tkviewJson , String stockJson , String date){
        return queryResourcesSvc.clickDateOper(tkviewJson, stockJson, date);
    }
    
    @RequestMapping(value="getTkviewStockList")
    @ResponseBody
    public Map<String, Object> getTkviewStockList(){
        return queryResourcesSvc.getTkviewStockList();
    }
    
    @RequestMapping(value="getValidateData")
    @ResponseBody
    public Map<String, Object> getValidateData(){
        return queryResourcesSvc.getValidateData();
    }
    
    @RequestMapping(value="getExcel")
    @ResponseBody
    public Map<String, Object> getExcel(){
        return queryResourcesSvc.getExcel();
    }
    
    @RequestMapping(value="updateShipmentDataOper")
    @ResponseBody
    public Map<String, Object> updateShipmentDataOper(){
        return queryResourcesSvc.updateShipmentDataOper();
    }
    
    @RequestMapping(value="queryShipmentData")
    @ResponseBody
    public Map<String, Object> queryShipmentData(){
        return queryResourcesSvc.queryShipmentData();
    }
    
    @RequestMapping(value="getQueryParamData")
    @ResponseBody
    public Map<String, Object> getQueryParamData(){
        return queryResourcesSvc.getQueryParamData();
    }
    
}