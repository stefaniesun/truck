package xyz.work.resources.svc;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface AccountSvc {
    public Map<String, Object> queryAccountList(int offset , int pageSize , int isEnable , String accountNumber , 
                                                String cashAccount , String bankOfDeposit , String provider);
    
    public Map<String, Object> addAccount(String accountNumber , String cashAccount , String bankOfDeposit,
                                          String provider , int isEnable , String remark);
    
    public Map<String, Object> editAccount(String numberCode ,String accountNumber , String cashAccount , 
                                           String bankOfDeposit , String provider , int isEnable , String remark);
    
    public Map<String, Object> editIsEnable(String numberCode);
    
    public Map<String, Object> deleteAccount(String numberCodes);
    
    public Map<String, Object> queryAccountListByprovider(String provider);
    
}