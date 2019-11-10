package xyz.work.taobao.svc;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface TaobaoPoolSvc {

    public Map<String, Object> queryTaobaoPool(int offset , int pageSize,String tid,String nickName,String sort,
        String order);
    
    public Map<String, Object> queryTaobaoOrderDetail(String tid);
    
    public Map<String, Object> addOrderContentByTaobao(String tkviewJsonStr,BigDecimal payment,String linkMan,String linkPhone,String endDate,String remark,String tid);
    
    public Map<String, Object> getStockByTkview(String tkview);
}
