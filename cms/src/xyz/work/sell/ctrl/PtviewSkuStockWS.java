package xyz.work.sell.ctrl;


import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.sell.svc.PtviewSkuStockSvc;


@Controller
@RequestMapping(value = "/PtviewSkuStockWS")
public class PtviewSkuStockWS {
    @Autowired
    private PtviewSkuStockSvc ptviewSkuStockSvc;

    @RequestMapping(value = "getPtviewSkuStockDateList")
    @ResponseBody
    public Map<String, Object> getPtviewSkuStockDateList(String ptviewSku) {
        return ptviewSkuStockSvc.getPtviewSkuStockDateList(ptviewSku);
    }

    @RequestMapping(value = "getTkviewTypeList")
    @ResponseBody
    public Map<String, Object> getTkviewTypeList(String ptviewSku) {
        return ptviewSkuStockSvc.getTkviewTypeList(ptviewSku);
    }

    @RequestMapping(value = "addPtviewSkuStock")
    @ResponseBody
    public Map<String, Object> addPtviewSkuStock(String ptviewSku , String outTkview , String date ,
                                                 BigDecimal bPrice , BigDecimal cPrice ,
                                                 BigDecimal aBPrice , BigDecimal aCPrice ,
                                                 BigDecimal sBPrice , BigDecimal sCPrice ,
                                                 BigDecimal mBPrice , BigDecimal mCPrice ,
                                                 String stock , String outStock) {
        return ptviewSkuStockSvc.addPtviewSkuStock(ptviewSku, outTkview, date, bPrice, cPrice, 
           aBPrice, aCPrice, sBPrice, sCPrice, mBPrice, mCPrice, stock, outStock);
    }

    @RequestMapping(value = "editPtviewSkuStock")
    @ResponseBody
    public Map<String, Object> editPtviewSkuStock(String numberCode , String outTkview ,
                                                  BigDecimal bPrice , BigDecimal cPrice ,
                                                  BigDecimal aBPrice , BigDecimal aCPrice ,
                                                  BigDecimal sBPrice , BigDecimal sCPrice ,
                                                  BigDecimal mBPrice , BigDecimal mCPrice ,
                                                  String stock) {
        return ptviewSkuStockSvc.editPtviewSkuStock(numberCode, outTkview, bPrice, cPrice, 
            aBPrice, aCPrice, sBPrice, sCPrice, mBPrice, mCPrice, stock);
    }

    @RequestMapping(value = "deletePtviewSkuStock")
    @ResponseBody
    public Map<String, Object> deletePtviewSkuStock(String numberCode) {
        return ptviewSkuStockSvc.deletePtviewSkuStock(numberCode);
    }

    @RequestMapping(value = "getPtviewSkuStockListByNumberCode")
    @ResponseBody
    public Map<String, Object> getPtviewSkuStockListByNumberCode(String numberCode ,
                                                                 String ptviewSku , String date) {
        return ptviewSkuStockSvc.getPtviewSkuStockListByNumberCode(numberCode, ptviewSku, date);
    }

    @RequestMapping(value = "getStockDate")
    @ResponseBody
    public Map<String, Object> getStockDate(String ptviewSku , String date) {
        return ptviewSkuStockSvc.getStockDate(ptviewSku, date);
    }

    @RequestMapping(value = "getStockByTkview")
    @ResponseBody
    public Map<String, Object> getStockByTkview(String shipment , String ptviewSku) {
        return ptviewSkuStockSvc.getStockByTkview(shipment, ptviewSku);
    }

    @RequestMapping(value = "getShipmentTravelDate")
    @ResponseBody
    public Map<String, Object> getShipmentTravelDate(String shipment) {
        return ptviewSkuStockSvc.getShipmentTravelDate(shipment);
    }

}