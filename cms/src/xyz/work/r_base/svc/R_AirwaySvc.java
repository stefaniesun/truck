package xyz.work.r_base.svc;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface R_AirwaySvc {
    
    public Map<String, Object> queryRAirwayList(int offset , int pageSize , String areaMark , String mark);

}