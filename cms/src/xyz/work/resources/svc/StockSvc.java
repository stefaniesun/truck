package xyz.work.resources.svc;


import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;


@Service
public interface StockSvc {

    public Map<String, Object> queryStockListByTkview(int offset , int pagesize , String tkview ,
                                                      String numberCode , String nickName ,
                                                      String sort , String order, String provider);

    public Map<String, Object> addStock(String tkview , String nickName , String provider ,
                                        int type , BigDecimal stock , BigDecimal surpass ,
                                        BigDecimal cost , String costRemark , Date overDate ,
                                        int priority , String remark);

    public Map<String, Object> editStockDetails(String numberCode , String nickName ,
                                                String provider , BigDecimal cost ,
                                                String costRemark , BigDecimal stock ,
                                                BigDecimal surpass , String overDate ,
                                                int priority , String remark , int stockType);

    public Map<String, Object> deleteStock(String numberCode);

    public Map<String, Object> setPriority(String numberCode , int isUp);

    public Map<String, Object> getCabinByCruise(String cruise);

    /**
     * 批量新增/编辑库存
     * 
     * @param cabinStr
     *            舱型编号(多个)
     * @param nickName
     * @param provider
     *            供应商
     * @param type
     *            类型
     * @param stock
     *            库存
     * @param surpass
     *            超卖
     * @param cost
     *            价格
     * @param costRemark
     *            价格说明
     * @param priority
     *            优先级
     * @param remark
     *            备注
     * @author : 熊玲
     */
    public Map<String, Object> addStockList(String cabinStr , String shipStr , String nickName ,
                                            String provider , int type , BigDecimal stock ,
                                            BigDecimal surpass , BigDecimal cost ,
                                            String costRemark , int priority ,
                                            String remark);

    public Map<String, Object> getShipmentByCruise(String cruise);

    public Map<String, Object> getStockListByCruise(String cruise , String cabin , String shipment);
    
    public Map<String, Object> getCabinByTkiew(String cruise);
    
    public Map<String, Object> getShipmentByTkiew(String cruise);
    
    public Map<String, Object> getStockByTkview(String cruise);
    
    public Map<String, Object> editStockByCabinAndShipmentAndProvider(String cabin, String shipment, String provider);
}