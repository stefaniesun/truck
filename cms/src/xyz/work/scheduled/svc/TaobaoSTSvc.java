package xyz.work.scheduled.svc;

import java.util.Map;

import org.springframework.stereotype.Service;

import xyz.work.core.model.PlanWork;


@Service
public interface TaobaoSTSvc {

    public Map<String, Object> addIncrementTid();
    
    public Map<String, Object> addTaobaoOrderDetails(PlanWork planWork);
    
}
