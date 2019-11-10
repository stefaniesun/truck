package xyz.work.excel.ctrl;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.excel.svc.PriceStrategySvc;

@Controller
@RequestMapping(value = "/PriceStrategyWS")
public class PriceStrategyWS {
    @Autowired
    PriceStrategySvc priceStrategySvc;
    
    @RequestMapping(value="queryPriceStrategyList")
    @ResponseBody
    public Map<String, Object> queryPriceStrategyList(int page , int rows) {
        int pageSize = rows;
        int offset = (page-1)*pageSize;
        return priceStrategySvc.queryPriceStrategyList(offset, pageSize);
    }
    
    @RequestMapping(value="addPriceStrategy")
    @ResponseBody
    public Map<String, Object> addPriceStrategy(BigDecimal minPrice , BigDecimal maxPrice ,
                                                BigDecimal priceMarkup , String remark){
        return priceStrategySvc.addPriceStrategy(minPrice, maxPrice, priceMarkup, remark);
    }

    @RequestMapping(value="editPriceStrategy")
    @ResponseBody
    public Map<String, Object> editPriceStrategy(String numberCode , BigDecimal minPrice ,
                                                 BigDecimal maxPrice , BigDecimal priceMarkup ,
                                                 String remark){
        return priceStrategySvc.editPriceStrategy(numberCode, minPrice, maxPrice, priceMarkup, remark);
    }

    @RequestMapping(value="deletePriceStrategy")
    @ResponseBody
    public Map<String, Object> deletePriceStrategy(String numberCodes){
        return priceStrategySvc.deletePriceStrategy(numberCodes);
    }
    
}