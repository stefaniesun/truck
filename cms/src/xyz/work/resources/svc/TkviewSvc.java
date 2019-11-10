package xyz.work.resources.svc;


import java.util.Map;

import org.springframework.stereotype.Service;


@Service
public interface TkviewSvc {

    public Map<String, Object> queryTkviewList(int offset , int pagesize , String cruise ,
                                               String shipment , String cabin , String nameCn ,
                                               String mark , String mouth , String sort ,
                                               String order , String mode , String provider);

    public Map<String, Object> addTkview(String nameCn , String mark , String cruise ,
                                         String shipment , String cabin , String remark);

    public Map<String, Object> editTkview(String numberCode , String nameCn , String mark ,
                                          String remark);

    public Map<String, Object> deleteTkviews(String numberCodes);

    public Map<String, Object> queryPossessorTkviewList(Boolean isTrue , String possessor ,
                                                        String numberCode , String name);

    public Map<String, Object> getOverDateByShipment(String shipment);

    public Map<String, Object> addQuickTkview(String tkviewJsonStr , String shipment);

    public Map<String, Object> queryPtviewTkviewList(int offset , int pagesize , String ptview ,
                                                     boolean flag , String tkview , String nameCn);

    public Map<String, Object> getTkviewLog(String tkview);

    public Map<String, Object> getTkviewListByShipment(String shipment);

    /**
     * 快捷新增单品--根据邮轮获取航期以及舱型信息
     */
    public Map<String, Object> getCabinByCruiseList(String cruise);

    /**
     * 快捷新增单品
     */
    public Map<String, Object> addFastlyTkview(String cruise , String shipmentStr ,
                                               String tkviewJsonStr);

    public Map<String, Object> getTkviewListByTkviewType(String tkviewType);

    /**
     * 单品视图-根据单品名称模糊查询 和时间 获取单品信息
     */
    public Map<String, Object> getTkviewViewData(String cruise , String numberCode , String date);

    /**
     * 单品视图-单品名称选项卡
     */
    public Map<String, Object> getTkviewTabList(String cruise);

    /**
     * 单品视图-日历数据
     */
    public Map<String, Object> getTkviewListByTkviewGroup(String cruise , String tkview);

    /**
     * 单品视图-库存列表信息
     */
    public Map<String, Object> getTkviewViewStockList(int offset , int pagesize , String numberCode);

    /**
     * 根据邮轮分组查询单品信息
     */
    public Map<String, Object> queryTkviewGroupCruise();

    /**
     * 邮轮查询并根据航期分组查询单品信息
     */
    public Map<String, Object> queryTkviewGroupShipmentByCruise(String cruise);

    /**
     * 修改操作人和释放时间 
     * 1、同一个人操作时： 过了释放时间或者释放时间为空时,在当前时间上加10分钟; 
     *   未过释放时间在释放时间上加5分钟; 
     * 2、不同人操作时： 不能修改信息,会给予提示
     */
    public Map<String, Object> editOperationData(String cruise , String tkviewNumber , String mode);

    /**
     * 手动释放单品
     */
    public Map<String, Object> editTkviewRelease(String mode);

    /**
     * 自动同步更新
     */
    public Map<String, Object> editStockToSku(String cruise);

    /**
     * 删除单品信息
     * 
     * @param cabin
     * @param shipment
     * @param provider
     * @author : 熊玲
     */
    public Map<String, Object> deleteTkviewByCabin(String cabin , String shipment , String provider);

    /**
     * 批量备注
     * 
     * @param numbercode
     *            单品编号(多个)
     * @param remark
     *            备注
     * @author : 熊玲
     */
    public Map<String, Object> editTkviewRemark(String numbercode , String remark);

    /**
     * 根据航期舱型查询单品
     * 
     * @param shipment
     * @param cabin
     * @author : 熊玲
     */
    public Map<String, Object> getTkviewListByShipmentAndCabin(String cruise , String shipment , String cabin);

	public void autoUpdateByTkviewOper(String tkview);

	public void autoUpdateByCruiseOper(String cruise);

}