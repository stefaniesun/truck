package xyz.work.sell.ctrl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.sell.svc.PtviewSkuSvc;

@Controller
@RequestMapping(value = "/PtviewSkuWS")
public class PtviewSkuWS {
    
    @Autowired
    private PtviewSkuSvc ptviewSkuSvc;
    
    @RequestMapping(value = "queryPtviewSkuList")
    @ResponseBody
    public Map<String, Object> queryPtviewSkuList(String ptview){
        return ptviewSkuSvc.queryPtviewSkuList(ptview);
    }
    
    @RequestMapping(value = "addPtviewSku")
    @ResponseBody
    public Map<String, Object> addPtviewSku(String ptview, String nameCn, String tkviewType){
        return ptviewSkuSvc.addPtviewSku(ptview, nameCn, tkviewType);
    }
    
    @RequestMapping(value = "getRelationTkviewType")
    @ResponseBody
    public Map<String, Object> getRelationTkviewType(String numberCode){
        return ptviewSkuSvc.getRelationTkviewType(numberCode);
    }
    
    @RequestMapping(value = "editPtviewSku")
    @ResponseBody
    public Map<String, Object> editPtviewSku(String numberCode, String nameCn, String tkviewType){
        return ptviewSkuSvc.editPtviewSku(numberCode, nameCn, tkviewType);
    }
    
    @RequestMapping(value = "deletePtviewSku")
    @ResponseBody
    public Map<String, Object> deletePtviewSku(String numberCode){
        return ptviewSkuSvc.deletePtviewSku(numberCode);
        
    }

}