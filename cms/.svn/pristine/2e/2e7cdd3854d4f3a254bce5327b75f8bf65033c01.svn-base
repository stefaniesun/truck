package xyz.work.resources.ctrl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.resources.svc.DistributorSvc;

@Controller
@RequestMapping(value = "/DistributorWS")
public class DistributorWS {
    
    @Autowired
    public DistributorSvc distributorSvc;
    
    @RequestMapping(value = "queryDistributorList")
    @ResponseBody
    public Map<String, Object> queryDistributorList(int page , int rows , String numberCode ,  String name ){
        int pageSize = rows;
        int offset = (page - 1) * pageSize;
        return distributorSvc.queryDistributorList(offset, pageSize, numberCode, name);
    }

    @RequestMapping(value = "addDistributor")
    @ResponseBody
    public Map<String, Object> addDistributor(String name , String type , String linkUsername , String linkPhone ,
                                              String distributorTag , String remark){
        
        
        return distributorSvc.addDistributor(name, type, linkUsername, linkPhone, distributorTag ,remark);
    }
    
    @RequestMapping(value = "editDistributor")
    @ResponseBody
    public Map<String, Object> editDistributor(String numberCode , String name , String type , String linkUsername , String linkPhone ,
                                              String distributorTag , String remark){
        
        
        return distributorSvc.editDistributor(numberCode, name, type, linkUsername, linkPhone, distributorTag , remark);
    }
    
    @RequestMapping(value = "deleteDistributor")
    @ResponseBody
    public Map<String, Object> deleteDistributor(String numberCodes){
        
        return distributorSvc.deleteDistributor(numberCodes);
    }
    
    @RequestMapping(value = "editIsEnable")
    @ResponseBody
    public Map<String, Object> editIsEnable(String numberCode){
        
        return distributorSvc.editIsEnable(numberCode);
    }
    
}
