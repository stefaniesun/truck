package xyz.work.custom.svc;


import java.util.Map;

import org.springframework.stereotype.Service;


@Service
public interface CustomerSvc {
    public Map<String, Object> queryCustomerList(int offset , int pageSize , String nickName);

    public Map<String, Object> editCustomer(String numberCode,String nickName,String remark);

	public Map<String, Object> customerRegisterOper(String phone,String nameCn,String img,String openid);

	public Map<String, Object> editCustomerEnabled(String numberCode);
    
}