package xyz.work.r_base.svc;


import java.util.Map;

import org.springframework.stereotype.Service;


@Service
public interface R_StockSvc {
    
    public Map<String, Object> queryRStockList(int offset , int pageSize ,String tkview);
}