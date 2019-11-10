package xyz.work.resources.ctrl;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.resources.svc.TkviewSvc;

@Controller
@RequestMapping(value = "/TkviewWS")
public class TkviewWS {
    @Autowired
    public TkviewSvc tkviewSvc;

    @RequestMapping(value = "queryTkviewList")
    @ResponseBody
    public Map<String, Object> queryTkviewList(int page , int rows , 
                                               String cruise , String shipment,
                                               String cabin , String nameCn,
                                               String mark , String mouth ,
                                               String sort , String order,
                                               String mode , String provider) {
        int pagesize = rows;
        int offset = (page - 1) * pagesize;
        return tkviewSvc.queryTkviewList(offset, pagesize, cruise, shipment, cabin, nameCn, 
                     mark, mouth, sort, order, mode, provider);
    }

    /**
     * 新增单品
     * @param nameCn 
     * @param mark
     * @param cruise
     * @param shipment
     * @param cabin
     * @param remark
     */
    @RequestMapping(value = "addTkview")
    @ResponseBody
    public Map<String, Object> addTkview(String nameCn,
                                         String mark,
                                         String cruise,
                                         String shipment,
                                         String cabin,
                                         String remark) {
        
        return tkviewSvc.addTkview(nameCn, mark, cruise, shipment, cabin, remark);
    }

    /**
     * 编辑单品
     * @param numberCode
     * @param nameCn
     * @param cruise
     * @param shipment
     * @param cabin
     * @param provider
     * @param remark
     * @return
     */
    @RequestMapping(value = "editTkview")
    @ResponseBody
    public Map<String, Object> editTkview(String numberCode, String nameCn ,String mark , String remark) {
        
        return tkviewSvc.editTkview(numberCode, nameCn, mark, remark);
    }
    
    /**
     * 删除单品
     * @param numberCodes
     * @return
     */
    @RequestMapping(value = "deleteTkviews")
    @ResponseBody
    public Map<String, Object> deleteTkviews(String numberCodes) {
        return tkviewSvc.deleteTkviews(numberCodes);
    }
    
    @RequestMapping(value = "queryPossessorTkviewTrueList")
    @ResponseBody
    public Map<String, Object> queryPossessorTkviewTrueList(String possessor,
                                                            String numberCode,
                                                            String name) {
        return tkviewSvc.queryPossessorTkviewList(true,possessor,numberCode,name);
    }
    
    @RequestMapping(value = "queryPossessorTkviewFalseList")
    @ResponseBody
    public Map<String, Object> queryPossessorTkviewFalseList(String possessor,
                                                             String numberCode,
                                                             String name) {
        return tkviewSvc.queryPossessorTkviewList(false,possessor,numberCode,name);
    }
    
    @RequestMapping(value = "getOverDateByShipment")
    @ResponseBody
    public Map<String, Object> getOverDateByShipment(String shipment){
        return tkviewSvc.getOverDateByShipment(shipment);
    }
    
    @RequestMapping(value = "addQuickTkview")
    @ResponseBody
    public Map<String, Object> addQuickTkview(String tkviewJsonStr,String shipment) {
        return tkviewSvc.addQuickTkview(tkviewJsonStr, shipment);
    }

    @RequestMapping(value = "queryPtviewTkviewTrueList")
    @ResponseBody
    public Map<String, Object> queryPtviewTkviewTrueList(int page , 
                                                         int rows , 
                                                         String ptview,
                                                         String tkview,
                                                         String nameCn) {
        int pagesize = rows;
        int offset = (page - 1) * pagesize;
        return tkviewSvc.queryPtviewTkviewList(offset, pagesize, ptview, true, tkview, nameCn);
    }
    
    @RequestMapping(value = "queryPtviewTkviewFalseList")
    @ResponseBody
    public Map<String, Object> queryPtviewTkviewFalseList(int page , 
                                                          int rows , 
                                                          String ptview,
                                                          String tkview,
                                                          String nameCn) {
        int pagesize = rows;
        int offset = (page - 1) * pagesize;
        return tkviewSvc.queryPtviewTkviewList(offset, pagesize, ptview, false, tkview, nameCn);
    }
    
    @RequestMapping(value = "getTkviewLog")
    @ResponseBody
    public Map<String, Object> getTkviewLog(String tkview) {
        return tkviewSvc.getTkviewLog(tkview);
    }
    
    @RequestMapping(value="getTkviewListByShipment")
    @ResponseBody
    public Map<String, Object> getTkviewListByShipment(String shipment) {
        return tkviewSvc.getTkviewListByShipment(shipment);
    }
    
    @RequestMapping(value="getCabinByCruiseList")
    @ResponseBody
    public Map<String, Object> getCabinByCruiseList(String cruise) {
        return tkviewSvc.getCabinByCruiseList(cruise);
    }
    
    @RequestMapping(value="addFastlyTkview")
    @ResponseBody
    public Map<String, Object> addFastlyTkview(String cruise,String shipmentStr, String tkviewJsonStr) {
        return tkviewSvc.addFastlyTkview(cruise,shipmentStr,tkviewJsonStr);
    }
    
    @RequestMapping(value="getTkviewListByTkviewType")
    @ResponseBody
    public Map<String, Object> getTkviewListByTkviewType(String tkviewType) {
        return tkviewSvc.getTkviewListByTkviewType(tkviewType);
    }
    
    @RequestMapping(value="getTkviewViewData")
    @ResponseBody
    public Map<String, Object> getTkviewViewData(String cruise , String numberCode , String date) {
        return tkviewSvc.getTkviewViewData(cruise, numberCode , date);
    }
    
    @RequestMapping(value="getTkviewTabList")
    @ResponseBody
    public Map<String, Object> getTkviewTabList(String cruise) {
        return tkviewSvc.getTkviewTabList(cruise);
    }
    
    @RequestMapping(value="getTkviewListByTkviewGroup")
    @ResponseBody
    public Map<String, Object> getTkviewListByTkviewGroup(String cruise , String tkview) {
        return tkviewSvc.getTkviewListByTkviewGroup(cruise, tkview);
    }
    
    @RequestMapping(value="getTkviewViewStockList")
    @ResponseBody
    public Map<String, Object> getTkviewViewStockList(int page , int rows ,
                                                      String numberCode) {
        int pagesize = rows;
        int offset = (page - 1) * pagesize;
        return tkviewSvc.getTkviewViewStockList(offset, pagesize, numberCode);
    }
    
    /**
     * 根据邮轮分组查询单品信息
     */
    @RequestMapping(value="queryTkviewGroupCruise")
    @ResponseBody
    public Map<String, Object> queryTkviewGroupCruise(){
        return tkviewSvc.queryTkviewGroupCruise();
    }
    
    /**
     * 邮轮查询并根据航期分组查询单品信息
     */
    @RequestMapping(value="queryTkviewGroupShipmentByCruise")
    @ResponseBody
    public Map<String, Object> queryTkviewGroupShipmentByCruise(String cruise) {
        return tkviewSvc.queryTkviewGroupShipmentByCruise(cruise);
    }
    
    /**
     * 修改操作人和释放时间
     * 1、同一个人操作时：
     * 过了释放时间或者释放时间为空时,在当前时间上加10分钟;
     * 未过释放时间在释放时间上加5分钟;
     * 2、不同人操作时：
     * 不能修改信息,会给予提示
     */
    @RequestMapping(value="editOperationData")
    @ResponseBody
    public Map<String, Object> editOperationData(String cruise, String tkviewNumber , String mode){
        return tkviewSvc.editOperationData(cruise, tkviewNumber, mode);
    }
    
    /**
     * 手动释放 
     */
    @RequestMapping(value="editTkviewRelease")
    @ResponseBody
    public Map<String, Object> editTkviewRelease(String mode) {
        return tkviewSvc.editTkviewRelease(mode);
    }
    
    /**
     * 更新单品库存信息到SKU日历库存
     */
    @RequestMapping(value="editStockToSku")
    @ResponseBody
    public Map<String, Object> editStockToSku(String cruise) {
        return tkviewSvc.editStockToSku(cruise);
    }
    
    @RequestMapping(value="deleteTkviewByCabin")
    @ResponseBody
    public Map<String, Object> deleteTkviewByCabin(String cabin , String shipment , String provider) {
        return tkviewSvc.deleteTkviewByCabin(cabin, shipment, provider);
    }
    
    @RequestMapping(value="editTkviewRemark")
    @ResponseBody
    public Map<String, Object> editTkviewRemark(String numbercode , String remark) {
        return tkviewSvc.editTkviewRemark(numbercode, remark);
    }
    
    @RequestMapping(value="getTkviewListByShipmentAndCabin")
    @ResponseBody
    public Map<String, Object> getTkviewListByShipmentAndCabin(String cruise , String shipment , String cabin) {
        return tkviewSvc.getTkviewListByShipmentAndCabin(cruise, shipment, cabin);
    }
    
}