package xyz.work.truck.svc.imp;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.ReturnUtil;
import xyz.util.StringTool;
import xyz.work.truck.model.Truck;
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

}
