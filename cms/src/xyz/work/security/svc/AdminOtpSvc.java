package xyz.work.security.svc;

import java.util.Map;

import org.springframework.stereotype.Service;

import xyz.work.security.model.TokenInfo;




@Service
public interface AdminOtpSvc {
	
	public Map<String, Object> queryOtpList(
			int offset,
			int pagesize,
			String flag,
            String tokenNum
	);
	
	public Map<String, Object> addOtp(TokenInfo tokenInfo);
	
	public Map<String, Object> deleteOtp(String otps);
	
}
