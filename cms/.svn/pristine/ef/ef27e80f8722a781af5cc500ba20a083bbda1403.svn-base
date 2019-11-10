package xyz.work.base.ctrl;


import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.base.svc.CabinSvc;


@Controller
@RequestMapping(value = "/CabinWS")
public class CabinWS {
    @Autowired
    private CabinSvc cabinSvc;

    @RequestMapping(value = "queryCabinList")
    @ResponseBody
    public Map<String, Object> queryCabinList(int page , int rows , 
                                              String cruise , 
                                              String nameCn ,
                                              BigDecimal volume ,
                                              String type , 
                                              String isOpen) {
        int pageSize = rows;
        int offset = (page - 1) * pageSize;
        return cabinSvc.queryCabinList(offset, pageSize, cruise, nameCn, volume, type, isOpen);
    }

    @RequestMapping(value = "addCabin")
    @ResponseBody
    public Map<String, Object> addCabin(String cruise , String nameCn , String mark ,
                                        BigDecimal volume , BigDecimal maxVolume , int type ,
                                        String remark , String floors , String acreage ,
                                        String survey , String isOpen) {
        return cabinSvc.addCabin(cruise, nameCn, mark, volume, maxVolume, type, remark, floors,
            acreage, survey, isOpen);
    }

    @RequestMapping(value = "editCabin")
    @ResponseBody
    public Map<String, Object> editCabin(String numberCode , String cruise , String nameCn ,
                                         String mark , BigDecimal volume , BigDecimal maxVolume ,
                                         int type , String remark , String floors ,
                                         String acreage , String survey , String isOpen) {
        return cabinSvc.editCabin(numberCode, cruise, nameCn, mark, volume, maxVolume, type,
            remark, floors, acreage, survey, isOpen);
    }

    @RequestMapping(value = "deleteCabin")
    @ResponseBody
    public Map<String, Object> deleteCabin(String numberCodes) {
        return cabinSvc.deleteCabin(numberCodes);
    }

    @RequestMapping(value = "addImages")
    @ResponseBody
    public Map<String, Object> addImages(String numberCode , String urls){
        return cabinSvc.addImages(numberCode, urls);
    }
    
    @RequestMapping(value = "addQuickCabin")
    @ResponseBody
    public Map<String, Object> addQuickCabin(String cruise , String cabinJosnStr) {
        return cabinSvc.addQuickCabin(cruise, cabinJosnStr);
    }
    
    @RequestMapping(value = "addDetail")  
    @ResponseBody
    public Map<String, Object> addDetail(String numberCode , String detail) {
        return cabinSvc.addDetail(numberCode, detail);
    }
    
    @RequestMapping(value = "editIsOpen")  
    @ResponseBody
    public Map<String, Object> editIsOpen(String numberCode){
        return cabinSvc.editIsOpen(numberCode);
    }
    
    @RequestMapping(value = "queryCabinGroupCruise")  
    @ResponseBody
    public Map<String, Object> queryCabinGroupCruise(){
        return cabinSvc.queryCabinGroupCruise();
    }
    
}