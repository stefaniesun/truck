package xyz.work.truck.svc;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface WebSvc {

	public Map<String, Object> getIndexData();

	public Map<String, Object> truckDetail(String numberCode);

}
