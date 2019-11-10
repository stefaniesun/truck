package xyz.work.scheduled;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import xyz.work.core.model.PlanWork;
import xyz.work.scheduled.svc.TaobaoSTSvc;


@Component
public class TaobaoST {

    @Autowired
    TaobaoSTSvc taobaoSTSvc;
    
    public String addIncrementTid(){
        Map<String ,Object> resultMap = taobaoSTSvc.addIncrementTid();
        if(Integer.parseInt(resultMap.get("status").toString()) != 1){
            return resultMap.get("msg").toString();
        }
        return null;
    }
    
    public String addTaobaoOrderDetails(PlanWork planWork){
        Map<String ,Object> resultMap = taobaoSTSvc.addTaobaoOrderDetails(planWork);
        if(Integer.parseInt(resultMap.get("status").toString()) != 1){
            return resultMap.get("msg").toString();
        }
        return null;
    }
    
}