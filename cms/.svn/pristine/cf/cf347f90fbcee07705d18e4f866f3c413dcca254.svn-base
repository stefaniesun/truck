package xyz.work.base.ctrl;


import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.base.svc.ShipmentSvc;


@Controller
@RequestMapping(value = "/ShipmentWS")
public class ShipmentWS {
    @Autowired
    public ShipmentSvc shipmentSvc;

    @RequestMapping(value = "queryShipmentList")
    @ResponseBody
    public Map<String, Object> queryShipmentList(int page , 
                                                 int rows , 
                                                 String cruise ,
                                                 String airway , 
                                                 Date startDate , 
                                                 Date endDate ,
                                                 String mark , 
                                                 String sort , 
                                                 String order) {
        int pageSize = rows;
        int offset = (page - 1) * pageSize;
        return shipmentSvc.queryShipmentList(offset, pageSize, cruise, airway, startDate, endDate,
            mark, sort, order);
    }

    @RequestMapping(value = "addShipment")
    @ResponseBody
    public Map<String, Object> addShipment(String cruise , 
                                           String area , 
                                           String mark ,
                                           String airway , 
                                           String travelDate ,
                                           String finalSaleDate , 
                                           String startPlace ,
                                           String remark) {

        return shipmentSvc.addShipment(cruise, area, mark, airway, travelDate, finalSaleDate,
            startPlace, remark);
    }
    
    @RequestMapping(value = "addMoreShipment")
    @ResponseBody
    public Map<String, Object> addmoreShipment(String cruise , 
                                               String area , 
                                               String airway ,
                                               String startPlace,
                                               String shipmentJsonStr
                                          ) {

        return shipmentSvc.addMoreShipment(cruise, area, airway,startPlace,shipmentJsonStr);
    }

    @RequestMapping(value = "editShipment")
    @ResponseBody
    public Map<String, Object> editShipment(String numberCode , 
                                            String finalSaleDate , 
                                            String startPlace ,
                                            String remark) {

        return shipmentSvc.editShipment(numberCode, finalSaleDate, startPlace,remark);
    }

    @RequestMapping(value = "deleteShipment")
    @ResponseBody
    public Map<String, Object> deleteShipment(String numberCodes) {
        return shipmentSvc.deleteShipment(numberCodes);
    }

    @RequestMapping(value = "addDetail")
    @ResponseBody
    public Map<String, Object> addDetail(String shipment , String detail) {
        return shipmentSvc.addDetail(shipment, detail);
    }

    @RequestMapping(value = "queryVoyageListByShipment")
    @ResponseBody
    public Map<String, Object> queryVoyageListByShipment(String shipment) {
        return shipmentSvc.queryVoyageListByShipment(shipment);
    }

    @RequestMapping(value = "editShipmentVoyage")
    @ResponseBody
    public Map<String, Object> editShipmentVoyage(String numberCode , 
                                                  String time ,
                                                  int timeType,
                                                  String arrivalTime , 
                                                  String leaveTime ,
                                                  String detail ,
                                                  String remark) {

        return shipmentSvc.editShipmentVoyage(numberCode, time, timeType, arrivalTime, 
                                              leaveTime, detail,remark);
    }

    @RequestMapping(value = "setShipmentAirway")
    @ResponseBody
    public Map<String, Object> setShipmentAirway(String numberCode , String airway) {
        return shipmentSvc.setShipmentAirway(numberCode, airway);
    }

    @RequestMapping(value = "getVoyageByShipment")
    @ResponseBody
    public Map<String, Object> getVoyageByShipment(String shipment) {
        return shipmentSvc.getVoyageByShipment(shipment);
    }

    @RequestMapping(value = "editQuickVoyage")
    @ResponseBody
    public Map<String, Object> editQuickVoyage(String voyageJson) {
        return shipmentSvc.editQuickVoyage(voyageJson);
    }

    @RequestMapping(value = "cloneShipmentByNumberCodeOper")
    @ResponseBody
    public Map<String, Object> cloneShipmentByNumberCodeOper(String numberCode , 
                                                         String travelDate ,
                                                         String mark , 
                                                         String finalSaleDate ,
                                                         String startPlace , 
                                                         String remark) {
        return shipmentSvc.cloneShipmentByNumberCodeOper(numberCode, travelDate, mark, finalSaleDate,
            startPlace, remark);
    }
    
    @RequestMapping(value = "quickCreateTkviewOper")
    @ResponseBody
    public Map<String, Object> quickCreateTkviewOper(String numberCode) {
        return shipmentSvc.quickCreateTkviewOper(numberCode);
    }
    
    @RequestMapping(value = "getTkiewAndStocCount")
    @ResponseBody
    public Map<String, Object> getTkiewAndStocCount(String shipments){
        return shipmentSvc.getTkiewAndStocCount(shipments);
    }
    
    @RequestMapping(value = "queryShipmentGroupCruise")
    @ResponseBody
    public Map<String, Object> queryShipmentGroupCruise(Date startDate){
        return shipmentSvc.queryShipmentGroupCruise(startDate);
    }
    
}