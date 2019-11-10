package xyz.work.taobao.svc;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface TaoBaoSkuSvc {
    
    /**
     * 根据baseInfo编号获取SKU及库存信息
     */
    public Map<String, Object> getSkuStockListByBaseInfo(String baseInfo);
    
    
    /**
     * 批量更新SKU
     */
    public Map<String, Object> updateSkuStockList(String skuJson);
    
    /**
     * 锁定日志--编辑价格是否锁定
     */
    public Map<String, Object> editDetailByNumberCode(String numberCode, int isLock);
    
}