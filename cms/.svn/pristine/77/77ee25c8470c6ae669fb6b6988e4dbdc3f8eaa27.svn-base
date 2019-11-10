package xyz.work.sell.svc;


import java.math.BigDecimal;
import java.util.Map;

import org.springframework.stereotype.Service;


@Service
public interface PtviewSkuStockSvc {
    /**
     * 根据SKU套餐编号获取库存日期
     * 
     * @param ptviewSku
     * @author : 熊玲
     */
    public Map<String, Object> getPtviewSkuStockDateList(String ptviewSku);

    /**
     * 获取关联的单品种类 (用于展示名称)
     * 
     * @param ptviewSku
     * @author : 熊玲
     */
    public Map<String, Object> getTkviewTypeList(String ptviewSku);

    public Map<String, Object> addPtviewSkuStock(String ptviewSku , String outTkview , String date ,
                                                 BigDecimal bPrice , BigDecimal cPrice ,
                                                 BigDecimal aBPrice , BigDecimal aCPrice ,
                                                 BigDecimal sBPrice , BigDecimal sCPrice ,
                                                 BigDecimal mBPrice , BigDecimal mCPrice ,
                                                 String stock , String outStock);

    public Map<String, Object> editPtviewSkuStock(String numberCode , String outTkview ,
                                                  BigDecimal bPrice , BigDecimal cPrice ,
                                                  BigDecimal aBPrice , BigDecimal aCPrice ,
                                                  BigDecimal sBPrice , BigDecimal sCPrice ,
                                                  BigDecimal mBPrice , BigDecimal mCPrice ,
                                                  String stock);

    public Map<String, Object> deletePtviewSkuStock(String numberCode);

    public Map<String, Object> getPtviewSkuStockListByNumberCode(String numberCode ,
                                                                 String ptviewSku , String date);

    public Map<String, Object> getStockDate(String ptviewSku , String date);
    
    public Map<String, Object> getStockByTkview(String shipment, String ptviewSku);

    public Map<String, Object> getShipmentTravelDate(String shipment);
    
}