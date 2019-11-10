package xyz.work.r_base.svc;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface R_AreaSvc {
    
    public Map<String, Object> queryRAreaList(int offset , int pageSize , String mark);
    
}