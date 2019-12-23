package xyz.work.truck.svc;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface WebSvc {

	public Map<String, Object> getIndexData(int offset,int pageSize);

	public Map<String, Object> truckDetail(String numberCode);

	public Map<String, Object> truckViewOper(String truck, String customer);

	public Map<String, Object> wechatOper(String code);

	public Map<String, Object> getToken();

	public Map<String, Object> msgReciveOper();

	public Map<String, Object> xcxWechatOper(String code,String nickName,String img,String city,String gender,String province);

	public Map<String, Object> getSearchData(int offset, int pageSize, String truckType);

}
