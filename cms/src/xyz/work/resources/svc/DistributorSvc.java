package xyz.work.resources.svc;


import java.util.Map;

import org.springframework.stereotype.Service;


@Service
public interface DistributorSvc {

    public Map<String, Object> queryDistributorList(int offset , int pagesize , String numberCode ,
                                                    String name );

    public Map<String, Object> addDistributor(String name , String type , String linkUsername , String linkPhone ,
                                              String distributorTag , String remark);

    public Map<String, Object> editDistributor(String numberCode, String name , String type , String linkUsername , 
                                               String linkPhone ,String distributorTag , String remark);

    public Map<String, Object> deleteDistributor(String numberCodes);
    
    public Map<String, Object> editIsEnable(String numberCode);
    

}