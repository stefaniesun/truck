package xyz.work.taobao.svc;


import java.math.BigDecimal;
import java.util.Map;

import org.springframework.stereotype.Service;


@Service
public interface TaoBaoSkuDetailSvc {
    /**
     * 批量新增SKU日历库存
     */
    public Map<String, Object> addSkuDatilList(String baseInfo , String skuInfo , String dateJson ,
                                               BigDecimal price , int priceType , int isLock ,
                                               int isEdit , String stock);
    
    /**
     * 编辑是否锁定状态
     */
    public Map<String, Object> editIsLock(String numberCode , int isLock , String cruise);

}