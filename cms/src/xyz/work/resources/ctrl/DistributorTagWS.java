package xyz.work.resources.ctrl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.resources.svc.DistributorTagSvc;

@Controller
@RequestMapping(value = "/DistributorTagWS")
public class DistributorTagWS {
    
    @Autowired
    public DistributorTagSvc distributorTagSvc;
    
    @RequestMapping(value = "queryDistributorTagList")
    @ResponseBody
    public Map<String, Object> queryDistributorTagList(int page , int rows , String numberCode ){
        int pageSize = rows;
        int offset = (page - 1) * pageSize;
        return distributorTagSvc.queryDistributorTagList(offset, pageSize, numberCode);
    }

    @RequestMapping(value = "addDistributorTag")
    @ResponseBody
    public Map<String, Object> addDistributorTag(String name ,  String remark){
        
        return distributorTagSvc.addDistributorTag(name, remark);
    }
    
    @RequestMapping(value = "editDistributorTag")
    @ResponseBody
    public Map<String, Object> editDistributorTag(String numberCode , String name , String remark){
        
        return distributorTagSvc.editDistributorTag(numberCode, name, remark);
    }
    
    @RequestMapping(value = "deleteDistributorTag")
    @ResponseBody
    public Map<String, Object> deleteDistributorTag(String numberCodes){
        
        return distributorTagSvc.deleteDistributorTag(numberCodes);
    }
    
    @RequestMapping(value = "queryDistributorTagListByNumberCode")
    @ResponseBody
    public Map<String, Object> queryDistributorTagListByNumberCode(String numberCode){
        
        return distributorTagSvc.queryDistributorTagListByNumberCode(numberCode);
    }
    
    @RequestMapping(value = "editStrategyJsonByNumberCode")
    @ResponseBody
    public Map<String, Object> editStrategyJsonByNumberCode(String numberCode , String ptrategyJson){
        
        return distributorTagSvc.editStrategyJsonByNumberCode(numberCode ,ptrategyJson);
    }
}
