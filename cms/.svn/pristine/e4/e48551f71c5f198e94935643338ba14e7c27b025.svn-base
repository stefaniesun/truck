package xyz.work.sell.ctrl;


import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.sell.svc.PtviewSvc;


@Controller
@RequestMapping(value = "/PtviewWS")
public class PtviewWS {
    @Autowired
    private PtviewSvc ptviewSvc;

    @RequestMapping(value = "queryCruiseList")
    @ResponseBody
    public Map<String, Object> queryCruiseList() {
        return ptviewSvc.queryCruiseList();
    }

    @RequestMapping(value = "queryPtviewListByCruise")
    @ResponseBody
    public Map<String, Object> queryPtviewListByCruise(int page , int rows , String cruise ,
                                                       String nameCn , Date startDate , Date endDate) {
        int pageSize = rows;
        int offset = (page - 1) * pageSize;
        return ptviewSvc.queryPtviewListByCruise(offset, pageSize, cruise, nameCn, startDate,endDate);
    }

    @RequestMapping(value = "addPtview")
    @ResponseBody
    public Map<String, Object> addPtview(String cruise ,String shipmentJson ) {
        return ptviewSvc.addPtview(cruise , shipmentJson);
    }

    @RequestMapping(value = "editPtview")
    @ResponseBody
    public Map<String, Object> editPtview(String numberCode , String nameCn ) {
        return ptviewSvc.editPtview(numberCode, nameCn );
    }

    @RequestMapping(value = "deletePtview")
    @ResponseBody
    public Map<String, Object> deletePtview(String numberCodes) {
        return ptviewSvc.deletePtview(numberCodes);
    }
    
    @RequestMapping(value = "getPtviewSkuByNumberCode")
    @ResponseBody
    public Map<String, Object> getPtviewSkuByNumberCode(String numberCode) {
        return ptviewSvc.getPtviewSkuByNumberCode(numberCode);
    }
    
    @RequestMapping(value = "clonePtviewOper")
    @ResponseBody
    public Map<String, Object> clonePtviewOper(String numberCode , String ptview , String skuJson) {
        return ptviewSvc.clonePtviewOper(numberCode, ptview, skuJson);
    }
    
    @RequestMapping(value = "syncPtviewOper")
    @ResponseBody
    public Map<String, Object> syncPtviewOper(String numberCode, String ptview, String skuJson){
        return ptviewSvc.syncPtviewOper(numberCode, ptview, skuJson);
    }
    
    @RequestMapping(value = "getSortNumCruise")
    @ResponseBody
    public Map<String, Object> getSortNumCruise(int page , int rows) {
        int pageSize = rows;
        int offset = (page - 1) * pageSize;
        return ptviewSvc.getSortNumCruise(offset, pageSize);
    }
    
    @RequestMapping(value = "editSortNumCruise")
    @ResponseBody
    public Map<String, Object> editSortNumCruise(String numberCodes) {
        return ptviewSvc.editSortNumCruise(numberCodes);
    }
    
    @RequestMapping(value = "editIsOpen")
    @ResponseBody
    public Map<String, Object> editIsOpen(String numberCode) {
        return ptviewSvc.editIsOpen(numberCode);
    }
    
    @RequestMapping(value = "editImage")
    @ResponseBody
    public Map<String, Object> editImage(String numberCode,String urls) {
        return ptviewSvc.editImage(numberCode,urls);
    }
    
    @RequestMapping(value = "editDetailImages")
    @ResponseBody
    public Map<String, Object> editDetailImages(String numberCode,String urls) {
        return ptviewSvc.editDetailImages(numberCode,urls);
    }
}