package xyz.work.resources.svc;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface JoinerSvc {
    public Map<String, Object> queryJoinerList(int offset , int pageSize , String nameCn , 
                                               String phone , String weChat , String provider);

    public Map<String, Object> addJoiner(String nameCn , String phone , String weChat,
                                         String provider , String qq , String remark);
    
    public Map<String, Object> editJoiner(String numberCode ,String nameCn , String phone , 
                                          String weChat , String provider , String qq , String remark);
    
    public Map<String, Object> deleteJoiner(String numberCodes);
    
    public Map<String, Object> queryJoinerListByprovider(String provider);
    
}