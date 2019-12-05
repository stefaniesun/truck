package xyz.work.truck.svc;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface WebSvc {

	public Map<String, Object> getIndexData();

	public Map<String, Object> truckDetail(String numberCode);

	public Map<String, Object> truckViewOper(String truck, String customer);

	public Map<String, Object> wechatOper(String code);

	public Map<String, Object> getToken();

}
