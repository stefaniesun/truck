package xyz.work.security.svc;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface UserOperSvc{
	public Map<String ,Object> addUserOper(
			String keyCode,
			String content);
}
