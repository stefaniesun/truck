package xyz.work.truck.svc.imp;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.ReturnUtil;
import xyz.util.StringTool;
import xyz.util.UUIDUtil;
import xyz.work.truck.model.Truck;
import xyz.work.truck.model.View;
import xyz.work.truck.svc.WebSvc;

@Service
public class WebSvcImp implements WebSvc {

	@Autowired
	CommonDao commonDao;
	
	@Override
	public Map<String, Object> getIndexData() {
		
		List<Truck> truckList=commonDao.queryByHql("from Truck where status=1 order by addDate");
		
		return ReturnUtil.returnMap(1, truckList);
	}

	@Override
	public Map<String, Object> truckDetail(String numberCode) {
		
		if(!StringTool.isNotNull(numberCode)) {
			return ReturnUtil.returnMap(0, "编号为空");
		}
		
		Truck truck=(Truck) commonDao.getObjectByUniqueCode("Truck", "numberCode", numberCode);
		if(truck==null) {
			return ReturnUtil.returnMap(0, "数据异常");
		}
		
		return ReturnUtil.returnMap(1, truck);
	}

	@Override
	public Map<String, Object> truckViewOper(String truck, String customer) {
		
		if(!StringTool.isNotNull(truck)) {
			return ReturnUtil.returnMap(1, null);
		}
		if(!StringTool.isNotNull(customer)) {
			customer="";
		}
		
		Truck truckObj=(Truck) commonDao.getObjectByUniqueCode("Truck", "numberCode", truck);
		if(truckObj==null) {
			return ReturnUtil.returnMap(1, null);
		}
		
		View view=new View();
		view.setNumberCode(UUIDUtil.getUUIDStringFor32());
		view.setDateInfo(new Date());
		view.setCustomer(customer);
		view.setTruck(truck);
		
		commonDao.save(view);
		
		String sql="select count(*) from view where truck='"+truck+"'";
		Object countObject=commonDao.getSqlQuery(sql).uniqueResult();
		int count=Integer.valueOf(countObject.toString());
		truckObj.setReadCount(count);
		commonDao.update(truckObj);
		
		return ReturnUtil.returnMap(1, null);
	}

}
