package xyz.work.sell.svc;


import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;


@Service
public interface PtviewSvc {

    public Map<String, Object> queryCruiseList();

    public Map<String, Object> queryPtviewListByCruise(int offset , int pageSize , String cruise ,
                                                       String nameCn,Date startDate , Date endDate);

    public Map<String, Object> addPtview(String cruise ,String shipmentJson );

    public Map<String, Object> editPtview(String numberCode , String nameCn );

    public Map<String, Object> deletePtview(String numberCodes);
    
    public Map<String, Object> getPtviewSkuByNumberCode(String numberCode);
    
    public Map<String, Object> clonePtviewOper(String numberCode, String ptview, String skuJson);
    
    public Map<String, Object> syncPtviewOper(String numberCode, String ptview, String skuJson);

    public Map<String, Object> getSortNumCruise(int offset , int pageSize);
    
    public Map<String, Object> editSortNumCruise(String numberCodes);
    
    public Map<String, Object> editIsOpen(String numberCode);
    
    public Map<String, Object> editImage(String numberCode,String urls);
    
    public Map<String, Object> editDetailImages(String numberCode,String urls);
    
}