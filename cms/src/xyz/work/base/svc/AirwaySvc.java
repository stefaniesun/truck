package xyz.work.base.svc;


import java.math.BigDecimal;
import java.util.Map;

import org.springframework.stereotype.Service;


@Service
public interface AirwaySvc {
    public Map<String, Object> queryAirwayList(int offset , int pageSize , String nameCn ,
                                               BigDecimal days , BigDecimal nights , String area ,
                                               String sort , String order);

    public Map<String, Object> addAirway(String area , String nameCn , String mark ,
                                         BigDecimal days , BigDecimal nights , String remark);

    public Map<String, Object> editAirway(String area , String numberCode , String nameCn ,
                                          String mark , BigDecimal days , BigDecimal nights ,
                                          String remark);

    public Map<String, Object> deleteAirway(String numberCodes);

    public Map<String, Object> getTripByAirway(String airway);

}