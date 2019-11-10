package xyz.work.resources.svc;


import java.util.Map;

import org.springframework.stereotype.Service;


@Service
public interface DistributorTagSvc {

    public Map<String, Object> queryDistributorTagList(int offset , int pagesize , String numberCode );

    public Map<String, Object> addDistributorTag(String name , String remark);

    public Map<String, Object> editDistributorTag(String numberCode, String name , String remark);

    public Map<String, Object> deleteDistributorTag(String numberCodes);
    
    public Map<String, Object> queryDistributorTagListByNumberCode(String numberCode);

    public Map<String, Object> editStrategyJsonByNumberCode(String numberCode , String ptrategyJson);
}