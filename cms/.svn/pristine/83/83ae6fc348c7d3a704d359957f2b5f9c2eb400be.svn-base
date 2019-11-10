package xyz.work.resources.ctrl;


import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.resources.svc.StockSvc;


@Controller
@RequestMapping(value = "/StockWS")
public class StockWS {

    @Autowired
    StockSvc stockSvc;

    @RequestMapping(value = "queryStockListByTkview")
    @ResponseBody
    public Map<String, Object> queryStockListByTkview(int page , int rows , String tkview ,
                                                      String numberCode , String nickName ,
                                                      String sort , String order, String provider) {
        int pagesize = rows;
        int offset = (page - 1) * pagesize;
        return stockSvc.queryStockListByTkview(offset, pagesize, tkview, numberCode, nickName,
            sort, order, provider);
    }

    @RequestMapping(value = "addStock")
    @ResponseBody
    public Map<String, Object> addStock(String tkview , String nickName , String provider ,
                                        int type , BigDecimal stock , BigDecimal surpass ,
                                        BigDecimal cost , String costRemark , Date overDate ,
                                        int priority , String remark) {
        return stockSvc.addStock(tkview, nickName, provider, type, stock, surpass, cost,
            costRemark, overDate, priority, remark);
    }

    @RequestMapping(value = "editStockDetails")
    @ResponseBody
    public Map<String, Object> editStockDetails(String numberCode , String nickName ,
                                                String provider , BigDecimal cost ,
                                                String costRemark , BigDecimal stock ,
                                                BigDecimal surpass , String overDate ,
                                                int priority , String remark , int stockType) {
        return stockSvc.editStockDetails(numberCode, nickName, provider, cost, costRemark, stock,
            surpass, overDate, priority, remark, stockType);
    }

    @RequestMapping(value = "deleteStock")
    @ResponseBody
    public Map<String, Object> deleteStock(String numberCode) {
        return stockSvc.deleteStock(numberCode);
    }

    @RequestMapping(value = "setPriority")
    @ResponseBody
    public Map<String, Object> setPriority(String numberCode , int isUp) {
        return stockSvc.setPriority(numberCode, isUp);
    }

    @RequestMapping(value = "getCabinByCruise")
    @ResponseBody
    public Map<String, Object> getCabinByCruise(String cruise) {
        return stockSvc.getCabinByCruise(cruise);
    }

    @RequestMapping(value = "addStockList")
    @ResponseBody
    public Map<String, Object> addStockList(String cabinStr ,String shipStr, String nickName , String provider ,
                                            int type , BigDecimal stock , BigDecimal surpass ,
                                            BigDecimal cost , String costRemark ,
                                            int priority , String remark) {
        return stockSvc.addStockList(cabinStr, shipStr, nickName, provider, type, stock, surpass, cost,
            costRemark, priority, remark);
    }
    
    @RequestMapping(value = "getShipmentByCruise")
    @ResponseBody
    public Map<String, Object> getShipmentByCruise(String cruise) {
        return stockSvc.getShipmentByCruise(cruise);
    }
    
    @RequestMapping(value = "getStockListByCruise")
    @ResponseBody
    public Map<String, Object> getStockListByCruise(String cruise, String cabin, String shipment) {
        return stockSvc.getStockListByCruise(cruise, cabin, shipment);
    }

    @RequestMapping(value = "getCabinByTkiew")
    @ResponseBody
    public Map<String, Object> getCabinByTkiew(String cruise) {
        return stockSvc.getCabinByTkiew(cruise);
    }
    
    @RequestMapping(value = "getShipmentByTkiew")
    @ResponseBody
    public Map<String, Object> getShipmentByTkiew(String cruise) {
        return stockSvc.getShipmentByTkiew(cruise);
    }
    
    @RequestMapping(value = "getStockByTkview")
    @ResponseBody
    public Map<String, Object> getStockByTkview(String cruise) {
        return stockSvc.getStockByTkview(cruise);
    }
    
    @RequestMapping(value = "editStockByCabinAndShipmentAndProvider")
    @ResponseBody
    public Map<String, Object> editStockByCabinAndShipmentAndProvider(String cabin , String shipment, String provider) {
        return stockSvc.editStockByCabinAndShipmentAndProvider(cabin, shipment, provider);
    }
}