package xyz.work.security.ctrl;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.security.svc.ListSvc;

@Controller
@RequestMapping(value="/ListWS")
public class ListWS {
    
    @Autowired
    private ListSvc listSvc;
    
    @RequestMapping(value="getCompanyList")
    @ResponseBody
    public Map<String, Object> getCompanyList(String q, String crusie){
        return listSvc.getCompanyList(q, crusie);
    }
    
    @RequestMapping(value = "getCruiseList")
    @ResponseBody
    public Map<String, Object> getCruiseList(String q) {
        return listSvc.getCruiseList(q);
    }
    
    @RequestMapping(value = "getPortList")
    @ResponseBody
    public Map<String, Object> getPortList(String q,String type) {
        return listSvc.getPortList(q, type);
    }
    
    @RequestMapping(value = "getAreaList")
    @ResponseBody
    public Map<String, Object> getAreaList(String q) {
        return listSvc.getAreaList(q);
    }
    
    @RequestMapping(value = "getRouteList")
    @ResponseBody
    public Map<String,Object> getRouteList(String airway,String q){
        return listSvc.getRouteList(airway, q);
    }
    
    @RequestMapping(value = "getProviderList")
    @ResponseBody
    public Map<String, Object> getProviderList(String q) {
        return listSvc.getProviderList(q);
    }
    
    @RequestMapping(value = "getPossessorList")
    @ResponseBody
    public Map<String, Object> getPossessorList(String q) {
        return listSvc.getPossessorList(q);
    }
    
    @RequestMapping(value = "getChannelList")
    @ResponseBody
    public Map<String, Object> getChannelList(String q, String isMeituan){
        return listSvc.getChannelList(q, isMeituan);
    }
    
    @RequestMapping(value = "getTkviewList")
    @ResponseBody
    public Map<String, Object> getTkviewList(String q){
        return listSvc.getTkviewList(q);
    }
    
    @RequestMapping(value = "getPtviewList")
    @ResponseBody
    public Map<String, Object> getPtviewList(String q,String shipment){
        return listSvc.getPtviewList(q,shipment);
    }
    
    @RequestMapping(value = "getTaobaoProductList")
    @ResponseBody
    public Map<String, Object> getTaobaoProductList(String q){
        return listSvc.getTaobaoProductList(q);
    }
    
    @RequestMapping(value = "getAirwayList")
    @ResponseBody
    public Map<String, Object> getAirwayList(String q,String area) {
        return listSvc.getAirwayList(q,area);
    }
    
    @RequestMapping(value = "getDistributorNameAndPhoneList")
    @ResponseBody
    public Map<String, Object> getDistributorNameAndPhoneList(String q){
        return listSvc.getDistributorNameAndPhoneList(q);
    }
    
    @RequestMapping(value = "getShipmentList")
    @ResponseBody
    public Map<String, Object> getShipmentList(String q , String cruise,BigDecimal isTkview){
        return listSvc.getShipmentList(q, cruise, isTkview);
    }
    
    @RequestMapping(value = "getCabinListByCruise")
    @ResponseBody
    public Map<String, Object> getCabinListByCruise(String q , String cruise){
        return listSvc.getCabinListByCruise(q, cruise);
    }
    
    @RequestMapping(value = "getCabinListByShipment")
    @ResponseBody
    public Map<String, Object> getCabinListByShipment(String q , String shipmnet , String isOpen) {
        return listSvc.getCabinListByShipment(q, shipmnet, isOpen);
    }
    
    @RequestMapping(value = "getTkviewListNotInNumber")
    @ResponseBody
    public Map<String, Object> getTkviewListNotInNumber(String numberCode , String shipment , String q) {
        return listSvc.getTkviewListNotInNumber(numberCode, shipment , q);
    }
    
    @RequestMapping(value = "getCabinList")
    @ResponseBody
    public Map<String, Object> getCabinList(String q,BigDecimal isTkview) {
        return listSvc.getCabinList(q,isTkview);
    }
    
    @RequestMapping(value = "getPtviewShipmentList")
    @ResponseBody
    public Map<String, Object> getPtviewShipmentList(String q) {
        return listSvc.getPtviewShipmentList(q);
    }
    
    @RequestMapping(value = "getTravelItemByChannel")
    @ResponseBody
    public Map<String, Object> getTravelItemByChannel(String travelItem,String channel , String q) {
        return listSvc.getTravelItemByChannel(travelItem,channel, q);
    }
    
    @RequestMapping(value = "getShipmentByTaobaoSku")
    @ResponseBody
    public Map<String, Object> getShipmentByTaobaoSku(String q , String baseInfo,String sku) {
        return listSvc.getShipmentByTaobaoSku(q, baseInfo, sku);
    }
    
    @RequestMapping(value = "getTkviewByShipmentAndCruise")
    @ResponseBody
    public Map<String, Object> getTkviewByShipmentAndCruise(String q , String shipment ,
                                                            String baseInfo , String skuInfo) {
        return listSvc.getTkviewByShipmentAndCruise(q, shipment, baseInfo, skuInfo);
    }
    
    @RequestMapping(value = "getCidByChannel")
    @ResponseBody
    public Map<String, Object> getCidByChannel(String q , String channel) {
        return listSvc.getCidByChannel(q, channel);
    }
    
    @RequestMapping(value = "getShipmentAirwayList")
    @ResponseBody
    public Map<String, Object> getShipmentAirwayList(String q , String cruise) {
        return listSvc.getShipmentAirwayList(q, cruise);
    }
    
    @RequestMapping(value = "getShipmentCruiseList")
    @ResponseBody
    public Map<String, Object> getShipmentCruiseList(String q) {
        return listSvc.getShipmentCruiseList(q);
    }
    
    @RequestMapping(value = "getCabinByCruiseList")
    @ResponseBody
    public Map<String, Object> getCabinByCruiseList(String q , String cruise) {
        return listSvc.getCabinByCruiseList(q, cruise);
    }
    
    @RequestMapping(value = "getTkviewTypeList")
    @ResponseBody
    public Map<String, Object> getTkviewTypeList(String q , String cruise) {
        return listSvc.getTkviewTypeList(q, cruise);
    }
    
    @RequestMapping(value = "getShipmentMouthList")
    @ResponseBody
    public Map<String, Object> getShipmentMouthList(String q , String cruise) {
        return listSvc.getShipmentMouthList(q, cruise);
    }
    
    @RequestMapping(value = "getTkviewShipmentList")
    @ResponseBody
    public Map<String, Object> getTkviewShipmentList(String q , String cruise , String mouth ,
                                                     BigDecimal isTkview) {
        return listSvc.getTkviewShipmentList(q, cruise, mouth, isTkview);
    }
    
    /**
     * 下拉框--根据产品SKU编号获取航期
     */
    @RequestMapping(value = "getShipmentByPtviewSku")
    @ResponseBody
    public Map<String, Object> getShipmentByPtviewSku(String q , String ptviewSku){
        return listSvc.getShipmentByPtviewSku(q, ptviewSku);
    }

    /**
     * 下拉框--根据产品SKU编号获取航期
     */
    @RequestMapping(value = "getTkviewByShipment")
    @ResponseBody
    public Map<String, Object> getTkviewByShipment(String q , String shipment , String ptviewSku){
        return listSvc.getTkviewByShipment(q, shipment, ptviewSku);
    }
    
    @RequestMapping(value = "getPtviewAllList")
    @ResponseBody
    public Map<String, Object> getPtviewAllList(String q, String ptview) {
        return listSvc.getPtviewAllList(q, ptview);
    }
    
    @RequestMapping(value = "getAirwayListByCruise")
    @ResponseBody
    public Map<String, Object> getAirwayListByCruise(String q , String cruise) {
        return listSvc.getAirwayListByCruise(q, cruise);
    }
    
    @RequestMapping(value = "getStartPlace")
    @ResponseBody
    public Map<String, Object> getStartPlace(String q){
        return listSvc.getStartPlace(q);
    }
    
    @RequestMapping(value = "getToPlace")
    @ResponseBody
    public Map<String, Object> getToPlace(String q){
        return listSvc.getToPlace(q);
    }
    
    @RequestMapping(value = "getRoyalCruiseList")
    @ResponseBody
    public Map<String, Object> getRoyalCruiseList(String q) {
        return listSvc.getRoyalCruiseList(q);
    }
    
    @RequestMapping(value = "getRoyalShipmentMouthList")
    @ResponseBody
    public Map<String, Object> getRoyalShipmentMouthList(String q , String cruise){
        return listSvc.getRoyalShipmentMouthList(q, cruise);
    }
    
    @RequestMapping(value = "getRoyalShipmentList")
    @ResponseBody
    public Map<String, Object> getRoyalShipmentList(String q , String cruise , String mouth){
        return listSvc.getRoyalShipmentList(q, cruise, mouth);
    }
    
    @RequestMapping(value = "getProviderByShipmentList")
    @ResponseBody
    public Map<String, Object> getProviderByShipmentList(String shipment , String cabin) {
        return listSvc.getProviderByShipmentList(shipment, cabin);
    }
    
    @RequestMapping(value = "getProviderByCruiseList")
    @ResponseBody
    public Map<String, Object> getProviderByCruiseList(String cruise) {
        return listSvc.getProviderByCruiseList(cruise);
    }
    
    @RequestMapping(value = "getCabinIsOpenByCruiseList")
    @ResponseBody
    public Map<String, Object> getCabinIsOpenByCruiseList(String q , String cruise , String isOpen){
        return listSvc.getCabinIsOpenByCruiseList(q, cruise, isOpen);
    }
    
    @RequestMapping(value = "getPersonList")
    @ResponseBody
    public Map<String, Object> getPersonList(String q) {
        return listSvc.getPersonList(q);
    }
    
    @RequestMapping(value = "getCabinByTkviewList")
    @ResponseBody
    public Map<String, Object> getCabinByTkviewList(String cruise) {
        return listSvc.getCabinByTkviewList(cruise);
    }
    
    @RequestMapping(value = "getShipmentByTkviewList")
    @ResponseBody
    public Map<String, Object> getShipmentByTkviewList(String cruise) {
        return listSvc.getShipmentByTkviewList(cruise);
    }
    
    @RequestMapping(value = "getProviderByTkviewList")
    @ResponseBody
    public Map<String, Object> getProviderByTkviewList(String cruise , String shipment , String cabin) {
        return listSvc.getProviderByTkviewList(cruise, shipment, cabin);
    }
    
    @RequestMapping(value = "getDistributorTagList")
    @ResponseBody
    public Map<String, Object> getDistributorTagList() {
        return listSvc.getDistributorTagList();
    }
}