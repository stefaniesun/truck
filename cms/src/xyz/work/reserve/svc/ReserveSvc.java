package xyz.work.reserve.svc;

import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * 预约页面
 */
@Service
public interface ReserveSvc {
    /**
     * 预约登录
     */
    public Map<String,Object> reserveLogin(String tid , String phone);
    
    /**
     * 根据tid获取订单信息
     */
    public Map<String,Object> queryOrderListByTid(String tid,String notPass);
    
    /**
     * 预约(保存出行人信息)
     */
    public Map<String,Object> savePersonData(String personJson);
    
    /**
     * 根据出行人编号获取出行人信息
     */
    public Map<String,Object> getPersonByNumberCode(String json);
    
}