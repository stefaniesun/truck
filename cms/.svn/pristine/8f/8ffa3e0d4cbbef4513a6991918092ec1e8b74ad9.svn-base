package xyz.work.goal.svc;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface RecordSvc {
    public Map<String, Object> queryRersonList(int offset , int pageSize , String content);
    
    public Map<String, Object> addRerson(String content, String remark);
    
    public Map<String, Object> editRerson(String numberCode, String content, String remark);
    
    public Map<String, Object> deleteRerson(String numberCodes);
    
    public Map<String, Object> addDetails(String numberCode, String details);
    
}