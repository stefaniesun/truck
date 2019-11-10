package xyz.work.security.svc;


import java.math.BigDecimal;
import java.util.Map;

import org.springframework.stereotype.Service;


@Service
public interface ListSvc {

    /**
     * 下拉框--获取所有邮轮公司信息
     * 
     * @author : 熊玲
     */
    public Map<String, Object> getCompanyList(String q , String cruise);

    /**
     * 下拉框--获取所有邮轮信息
     * 
     * @author : 熊玲
     */
    public Map<String, Object> getCruiseList(String q);

    /**
     * 下拉框--获取所有港口信息
     * 
     * @author : 熊玲
     */
    public Map<String, Object> getPortList(String q, String type);

    /**
     * 下拉框--获取所有航区信息
     * 
     * @author : 熊玲
     */
    public Map<String, Object> getAreaList(String q);

    /**
     * 下拉框--根据航线编号获取路线信息
     * 
     * @author : 熊玲
     */
    public Map<String, Object> getRouteList(String airway , String q);

    public Map<String, Object> getProviderList(String q);

    public Map<String, Object> getPossessorList(String q);

    public Map<String, Object> getChannelList(String q, String isMeituan);

    public Map<String, Object> getTkviewList(String q);

    public Map<String, Object> getPtviewList(String q , String shipment);

    public Map<String, Object> getTaobaoProductList(String q);

    public Map<String, Object> getAirwayList(String q , String area);

    public Map<String, Object> getDistributorNameAndPhoneList(String q);

    public Map<String, Object> getShipmentList(String q , String cruise , BigDecimal isTkview);

    /**
     * 根据邮轮查询舱型信息
     *
     * @param q
     * @param cruise 邮轮编号(必传)
     */
    public Map<String, Object> getCabinListByCruise(String q , String cruise);

    public Map<String, Object> getCabinListByShipment(String q , String shipmnet , String isOpen);

    public Map<String, Object> getCabinList(String q , BigDecimal isTkview);

    public Map<String, Object> getTkviewListNotInNumber(String numberCode , String shipment ,
                                                        String q);

    public Map<String, Object> getPtviewShipmentList(String q);

    public Map<String, Object> getTravelItemByChannel(String travelItem , String channel , String q);

    public Map<String, Object> getShipmentByTaobaoSku(String q , String baseInfo , String skuNameCn);

    public Map<String, Object> getTkviewByShipmentAndCruise(String q , String shipment ,
                                                            String baseInfo , String skuInfo);

    public Map<String, Object> getCidByChannel(String q , String channel);

    public Map<String, Object> getShipmentAirwayList(String q , String cruise);

    /**
     * 下拉框--获取所有航期邮轮
     * 
     * @author : 熊玲
     */
    public Map<String, Object> getShipmentCruiseList(String q);

    /**
     * 下拉框--获取所有舱型(邮轮参数是不必要的)
     * 
     * @author : 熊玲
     */
    public Map<String, Object> getCabinByCruiseList(String q , String cruise);

    /**
     * 下拉框--获取所有单品种类
     * 
     * @author : 熊玲
     */
    public Map<String, Object> getTkviewTypeList(String q , String cruise);

    /**
     * 下拉框--根据邮轮编号获取航期的月份
     * 
     * @author : 熊玲
     */
    public Map<String, Object> getShipmentMouthList(String q , String cruise);

    /**
     * 下拉框--根据邮轮编号获取航期的出发日期
     * 
     * @author : 熊玲
     */
    public Map<String, Object> getTkviewShipmentList(String q , String cruise , String mouth ,
                                                     BigDecimal isTkview);
    
    /**
     * 下拉框--根据产品SKU编号获取航期
     */
    public Map<String, Object> getShipmentByPtviewSku(String q , String ptviewSku);

    /**
     * 下拉框--根据产品SKU编号获取航期
     */
    public Map<String, Object> getTkviewByShipment(String q , String shipment ,
                                                   String ptviewSku);

    /**
     * 下拉框--获取所有产品信息
     * 
     * @author : 熊玲
     */
    public Map<String, Object> getPtviewAllList(String q, String ptview);

    /**
     * 下拉框--资源查询--根据邮轮编号获取航线
     * 
     * @author : 熊玲
     */
    public Map<String, Object> getAirwayListByCruise(String q , String cruise);
    
    /**
     * 下拉框--资源查询--获取所有航期的出发地
     * 
     * @author : 熊玲
     */
    public Map<String, Object> getStartPlace(String q);
    
    /**
     * 下拉框--资源查询--查询出海上巡游的所有港口
     * 
     * @author : 熊玲
     */
    public Map<String, Object> getToPlace(String q);
    
    /**
     * 下拉框--获取皇家所有的邮轮信息
     * 
     * @author : 熊玲
     */
    public Map<String, Object> getRoyalCruiseList(String q);
    
    /**
     * 下拉框--皇家单品获取月份
     * 
     * @author : 熊玲
     */
    public Map<String, Object> getRoyalShipmentMouthList(String q , String cruise);
    
    /**
     * 下拉框--皇家单品获取出行日期
     * 
     * @author : 熊玲
     */
    public Map<String, Object> getRoyalShipmentList(String q , String cruise , String mouth);
    
    public Map<String, Object> getProviderByShipmentList(String shipment, String cabin);
    
    public Map<String, Object> getProviderByCruiseList(String cruise);
    
    /**
     * 
     * 根据邮轮编号、开启状态 查询舱型信息
     *
     * @param q
     * @param cruise 
     * @param isOpen
     * @author     : 熊玲
     */
    public Map<String, Object> getCabinIsOpenByCruiseList(String q , String cruise , String isOpen);
    
    /**
     * 下拉框--获取全部员工信息
     * 
     * @author : 熊玲
     */
    public Map<String, Object> getPersonList(String q);
    
    /**
     * 
     * Description  ：查询单品下的 舱型
     *
     * @return Map<String,Object>
     * @exception   ：〈描述可能的异常〉
    
     * @author      ：yangyong
     * @date        ：2017-10-26上午10:51:55
     */
    public Map<String,Object> getCabinByTkviewList(String cruise);
    
    /**
     * 
     * Description  ：查询单品下的 航期
     *
     * @param cruise
     * @return Map<String,Object>
     * @exception   ：〈描述可能的异常〉
    
     * @author      ：yangyong
     * @date        ：2017-10-26下午3:50:48
     */
    public Map<String, Object> getShipmentByTkviewList(String cruise);
    
    /**
     * 
     * Description  ：根据单品获取库存供应商
     *
     * @param cruise
     * @param shipment
     * @param cabin
     * @return Map<String,Object>
     * @exception   ：〈描述可能的异常〉
    
     * @author      ：yangyong
     * @date        ：2017-10-27下午12:22:20
     */
    public Map<String, Object> getProviderByTkviewList(String cruise , String shipment , String cabin);
    
    /**
     * 
     * Description  ：查询所有分销等级
     *
     * @return Map<String,Object>
     * @exception   ：〈描述可能的异常〉
    
     * @author      ：yangyong
     * @date        ：2017-11-7下午6:21:41
     */
    public Map<String, Object> getDistributorTagList();
    
}