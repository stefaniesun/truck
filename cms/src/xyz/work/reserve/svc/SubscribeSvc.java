package xyz.work.reserve.svc;


import java.util.Map;

import org.springframework.stereotype.Service;


/**
 * 预约管理(CMS系统)
 */
@Service
public interface SubscribeSvc {

    public Map<String, Object> queryReserveList(int offset , int pageSize , String tkviewNameCn ,
                                                String linkMan , int flag);

    public Map<String, Object> editReserveFlagByNumberCode(String numberCode , int flag , String flagRemark ,
                                           String remark);
    
    
    public Map<String, Object> getPersonListByReserve(String reserve);
    
}