package xyz.work.r_base.svc;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface R_TripSvc {
    
    public Map<String, Object> importTxtOper();
    
    public Map<String, Object> queryRTripList(String airway);
    
}