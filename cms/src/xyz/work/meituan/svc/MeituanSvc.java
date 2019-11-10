package xyz.work.meituan.svc;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface MeituanSvc {
    
    public Map<String,Object> queryConfirmApprovalList(int pageSize,int offset,String mtOrderId);

    public Map<String,Object> addAffrimApprol(String data);
    
    public Map<String,Object> affrimApprolOper(String mtOrderid,int code);
    
}
