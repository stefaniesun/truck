package xyz.work.base.svc;


import java.math.BigDecimal;
import java.util.Map;

import org.springframework.stereotype.Service;


@Service
public interface CabinSvc {
    public Map<String, Object> queryCabinList(int offset , int pageSize , String cruise ,
                                              String nameCn , BigDecimal volume ,
                                              String type , String isOpen);

    public Map<String, Object> addCabin(String cruise , String nameCn , String mark ,
                                        BigDecimal volume , BigDecimal maxVolume , int type ,
                                        String remark , String floors , String acreage ,
                                        String survey , String isOpen);

    public Map<String, Object> editCabin(String numberCode , String cruise ,
                                         String nameCn , String mark ,
                                         BigDecimal volume , BigDecimal maxVolume ,
                                         int type , String remark , String floors ,
                                         String acreage , String survey , String isOpen);

    public Map<String, Object> deleteCabin(String numberCodes);
    
    public Map<String, Object> addImages(String numberCode , String urls);
    
    public Map<String, Object> addQuickCabin(String cruise , String cabinJosnStr);
    
    public Map<String, Object> addDetail(String numberCode , String detail);
    
    public Map<String, Object> editIsOpen(String numberCode);
    
    public Map<String, Object> queryCabinGroupCruise();
    
}