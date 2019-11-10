package xyz.work.truck.svc.imp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.catalina.LifecycleListener;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.JSON;
import xyz.filter.MyRequestUtil;
import xyz.filter.ReturnUtil;
import xyz.util.DateUtil;
import xyz.util.StringTool;
import xyz.util.UUIDUtil;
import xyz.work.custom.model.Customer;
import xyz.work.truck.model.Truck;
import xyz.work.truck.svc.TruckSvc;

@Service
public class TruckSvcImp implements TruckSvc {

	@Autowired
	CommonDao commonDao;
	
	@Override
	public Map<String, Object> addTruck(String dataJson) {
		
		 if(StringTool.isEmpty(dataJson)){
	            return ReturnUtil.returnMap(0, "参数为空");
		 }
		 
		 Map<String, Object> map=JSON.toObject(dataJson, Map.class);
		 
		 String title=map.get("title")==null?"":map.get("title").toString();
		 String price=map.get("price")==null?"":map.get("price").toString();
		 String isGuohu=map.get("isGuohu")==null?"":map.get("isGuohu").toString();
		 String cardDate=map.get("cardDate")==null?"":map.get("cardDate").toString();
		 String checkDate=map.get("checkDate")==null?"":map.get("checkDate").toString();
		 String insuranceDate=map.get("insuranceDate")==null?"":map.get("insuranceDate").toString();
		 String mile=map.get("mile")==null?"":map.get("mile").toString();
		 String year=map.get("year")==null?"":map.get("year").toString();
		 String phone=map.get("phone")==null?"":map.get("phone").toString();
		 String type=map.get("type")==null?"":map.get("type").toString();
		 String address=map.get("address")==null?"":map.get("address").toString();
		 String remark=map.get("remark")==null?"":map.get("remark").toString();
		 List<String> images= new ArrayList<String>();
		 if(map.get("images")!=null) {
			 images=(List<String>) map.get("images");
		 }
		 
		 String customer=MyRequestUtil.getBizSecurityLogin().getNumberCode();
		 
		 Customer customerObj=(Customer) commonDao.getObjectByUniqueCode("Customer", "numberCode", customer);
		 if(customerObj==null) {
			 return ReturnUtil.returnMap(0, "用户对象不存在");
		 }
		 if(customerObj.getEnabled()==0) {
			 return ReturnUtil.returnMap(0, "用户帐号已禁用");
		 }
		 
		 if(!StringTool.isNotNull(title)) {
			 return ReturnUtil.returnMap(0, "车辆名称为空");
		 }
		 if(!StringTool.isNotNull(price)) {
			 return ReturnUtil.returnMap(0, "车辆估价为空");
		 }
		 if(images.size()==0) {
			 return ReturnUtil.returnMap(0, "车辆图片为空");
		 }
		 
		 Truck truck=new Truck();
		 
		 truck.setAddDate(new Date());
		 truck.setNumberCode(UUIDUtil.getUUIDStringFor32());
		 truck.setAddress(address);
		 if(StringTool.isNotNull(cardDate)) {
			 truck.setCardDate(DateUtil.shortStringToDate(cardDate));
		 }
		 if(StringTool.isNotNull(checkDate)) {
			 truck.setCheckDate(DateUtil.shortStringToDate(checkDate));
		 }
		 if(StringTool.isNotNull(insuranceDate)) {
			 truck.setInsuranceDate(DateUtil.shortStringToDate(insuranceDate));
		 }
		 truck.setImages(JSON.toJson(images));
		 if(StringTool.isNotNull(isGuohu)) {
			 truck.setIsGuohu(Integer.valueOf(isGuohu));
		 }
		 truck.setMile(mile);
		 truck.setPhone(phone);
		 truck.setPrice(price);
		 truck.setRemark(remark);
		 truck.setTitle(title);
		 truck.setType(type);
		 truck.setYear(year);
		 truck.setCustomer(customer);
		 truck.setCustomerImg(customerObj.getImg());
		 truck.setCustomerName(customerObj.getNameCn());
		 truck.setStatus(Truck.STATUS_SUBMIT);
		 
		 commonDao.save(truck);
		
		  return ReturnUtil.returnMap(1, null);
	}

	@Override
	public Map<String, Object> queryMyTruckList() {
		
		String customer=MyRequestUtil.getBizSecurityLogin().getNumberCode();
		
		if(!StringTool.isNotNull(customer)) {
			 return ReturnUtil.returnMap(0, "登录信息异常");
		}
		
	    List<Truck> truckList=commonDao.queryByHql("from Truck where customer='"+customer+"' order by addDate desc");
		
	    return ReturnUtil.returnMap(1, truckList);
	}


	@Override
	public Map<String, Object> queryTruckList(int offset, int pageSize) {

        StringBuffer sql = new StringBuffer("from Truck  where 1=1 ");
        sql.append(" order by addDate desc");
        
        Query countQuery = commonDao.getQuery("select count(*) " + sql.toString());
        Number countNum = (Number)countQuery.uniqueResult();
        int count = countNum == null ? 0 : countNum.intValue();

        Query query = commonDao.getQuery(sql.toString());
        query.setMaxResults(pageSize);
        query.setFirstResult(offset);
        @SuppressWarnings("unchecked")
        List<Customer> list = query.list();
        
        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", count);
        mapContent.put("rows", list);

        return ReturnUtil.returnMap(1, mapContent);
    
	}

	@Override
	public Map<String, Object> checkOper(String numberCode) {
		
		Truck truck=(Truck) commonDao.getObjectByUniqueCode("Truck", "numberCode", numberCode);
		
		truck.setStatus(Truck.STATUS_CHECK);
		truck.setPassDate(new Date());
		
		commonDao.update(truck);
		
		  return ReturnUtil.returnMap(1, null);
	}

}
