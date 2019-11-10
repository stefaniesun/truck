package xyz.work.meituan.svc;


import java.util.Map;

import org.springframework.stereotype.Service;


@Service
public interface QueryTaobaoSvc {

    /**
     * 获取淘宝宝贝集合
     * 
     * @author : 熊玲
     * @date : 2017-5-8下午4:55:01
     */
    public Map<String, Object> queryTaobaoBaseInfoList();

    /**
     * 获取宝贝SKU套餐集合、SKU库存集合
     * 
     * @author : 熊玲
     * @date : 2017-5-8下午4:56:01
     */
    public Map<String, Object> queryTaobaoSkuList(String travelItem);

    /**
     * 获取SKU套餐及下面的所有库存
     * 
     * @param skuInfo
     * @author : 熊玲
     */
    public Map<String, Object> queryTaobaoSkuStockList(String skuInfo);

    /**
     * 人工确认--减库存
     * 
     * @param sku
     *            sku编号
     * @param date
     *            日期
     * @param num
     *            下单数量
     * @author : 熊玲
     * @date : 2017-5-9上午9:53:57
     */
    public Map<String, Object> editTaobaoSkuStock(String skuInfo , String date , int num);

    /**
     * 推送美团渠道下面的淘宝数据给美团
     * 
     * @author : 熊玲
     */
    public Map<String, Object> addProduct(String channel , String travelItem);
    
    /**
     * 推送美团/蚂蜂窝渠道下面的SKU套餐
     * 
     * @author : 熊玲
     */
    public Map<String, Object> pushProductOper(String channel , String baseInfo, String skuJson);
    
}