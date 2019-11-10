package xyz.work.base.svc;


import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;


@Service
public interface ShipmentSvc {
    public Map<String, Object> queryShipmentList(int offset , int pageSize , String cruise ,
                                                 String airway , Date startDate , Date endDate ,
                                                 String mark , String sort , String order);

    public Map<String, Object> addShipment(String cruise , String area , String mark ,
                                           String airway , String travelDate ,
                                           String finalSaleDate , String startPlace , String remark);

    public Map<String, Object> editShipment(String numberCode , String finalSaleDate ,
                                            String startPlace , String remark);

    public Map<String, Object> deleteShipment(String numberCodes);

    public Map<String, Object> addDetail(String shipment , String detail);

    public Map<String, Object> queryVoyageListByShipment(String shipment);

    public Map<String, Object> editShipmentVoyage(String numberCode , String time , int timeType ,
                                                  String arrivalTime , String leaveTime ,
                                                  String detail , String remark);

    public Map<String, Object> setShipmentAirway(String numberCode , String airway);

    public Map<String, Object> getVoyageByShipment(String shipment);

    public Map<String, Object> editQuickVoyage(String voyageJson);

    public Map<String, Object> cloneShipmentByNumberCodeOper(String numberCode ,
                                                             String travelDate , String mark ,
                                                             String finalSaleDate ,
                                                             String startPlace , String remark);

    public Map<String, Object> quickCreateTkviewOper(String numberCode);

    public Map<String, Object> addMoreShipment(String cruise , String area , String airway ,
                                               String startPlace , String shipmentJsonStr);
    
    public Map<String, Object> getTkiewAndStocCount(String shipments);
    
    public Map<String, Object> queryShipmentGroupCruise(Date startDate);
    
}