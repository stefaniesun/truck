package xyz.work.truck.svc;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface TruckSvc {

	public Map<String, Object> addTruck(String dataJson);

	public Map<String, Object> queryMyTruckList();

	public Map<String, Object> queryTruckList(int offset, int pageSize);

	public Map<String, Object> checkOper(String numberCode);

	public Map<String, Object> collectionOper(String customer, String truck,String type);

	public Map<String, Object> queryMyViewList();

	public Map<String, Object> queryMyCollectionList();

	public Map<String, Object> getCollectionData(String customer, String truck);


}
