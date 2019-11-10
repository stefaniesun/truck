package xyz.work.taobao.ctrl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.taobao.svc.TaoBaoSkuSvc;

@Controller
@RequestMapping(value = "/TaoBaoSkuWS")
public class TaoBaoSkuWS {
    @Autowired
    TaoBaoSkuSvc skuSvc;
    
    @RequestMapping(value = "getSkuStockListByBaseInfo")
    @ResponseBody
    public Map<String, Object> getSkuStockListByBaseInfo(String baseInfo){
        return skuSvc.getSkuStockListByBaseInfo(baseInfo);
    }
    
    @RequestMapping(value = "updateSkuStockList")
    @ResponseBody
    public Map<String, Object> updateSkuStockList(String skuJson) {
        return skuSvc.updateSkuStockList(skuJson);
    }
    
    @RequestMapping(value = "editDetailByNumberCode")
    @ResponseBody
    public Map<String, Object> editDetailByNumberCode(String numberCode, int isLock) {
        return skuSvc.editDetailByNumberCode(numberCode, isLock);
    }
    
}