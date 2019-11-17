package xyz.work.truck.ctrl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.truck.svc.TruckSvc;

@Controller
@RequestMapping(value = "/TruckWS")
public class TruckWS {

	@Autowired
	TruckSvc truckSvc;
	
	@RequestMapping(value = "addTruck")
    @ResponseBody
    public Map<String, Object> addTruck(String dataJson){
        return truckSvc.addTruck(dataJson);
    }
	
	@RequestMapping(value = "queryMyTruckList")
    @ResponseBody
    public Map<String, Object> queryMyTruckList(){
        return truckSvc.queryMyTruckList();
    }
	
	@RequestMapping(value = "queryMyViewList")
    @ResponseBody
    public Map<String, Object> queryMyViewList(){
        return truckSvc.queryMyViewList();
    }
	
	@RequestMapping(value = "queryMyCollectionList")
    @ResponseBody
    public Map<String, Object> queryMyCollectionList(){
        return truckSvc.queryMyCollectionList();
    }
	
	@RequestMapping(value = "queryTruckList")
    @ResponseBody
    public Map<String, Object> queryTruckList(int page , int rows){
	       int pageSize = rows;
	        int offset = (page-1)*pageSize;
	        return truckSvc.queryTruckList(offset, pageSize);
    }
	
	@RequestMapping(value = "checkOper")
    @ResponseBody
    public Map<String, Object> checkOper(String numberCode){
        return truckSvc.checkOper(numberCode);
    }
	
	
	@RequestMapping(value = "collectionOper")
    @ResponseBody
    public Map<String, Object> collectionOper(String customer,String truck,String type){
        return truckSvc.collectionOper(customer,truck,type);
    }
	
	@RequestMapping(value = "getCollectionData")
    @ResponseBody
    public Map<String, Object> getCollectionData(String customer,String truck){
        return truckSvc.getCollectionData(customer,truck);
    }
	
	
}
