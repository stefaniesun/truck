package xyz.work.goal.svc;


import java.util.Map;

import org.springframework.stereotype.Service;


@Service
public interface PersonSvc {
    public Map<String, Object> queryPersonList(int offset , int pageSize , String nameCn);
    
    public Map<String, Object> addPerson(String nameCn, String sex, int post);
    
    public Map<String, Object> editPerson(String numberCode, String nameCn, String sex, int post);
    
    public Map<String, Object> deletePerson(String numberCodes);
    
}