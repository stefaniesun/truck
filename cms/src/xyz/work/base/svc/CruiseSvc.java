package xyz.work.base.svc;


import java.math.BigDecimal;
import java.util.Map;

import org.springframework.stereotype.Service;


@Service
public interface CruiseSvc {
    /**
     * 查询邮轮档案信息
     * 
     * @param offset
     * @param pageSize
     * @param nameCn
     *            邮轮名称
     * @param company
     *            邮轮公司
     * @param mark
     *            邮轮代码
     * @author : 熊玲
     */
    public Map<String, Object> queryCruiseList(int offset , int pageSize , String nameCn ,
                                               String company , String mark);

    /**
     * 新增邮轮档案信息
     * 
     * @param nameCn
     *            邮轮名称
     * @param mark
     *            邮轮代码
     * @param company
     *            邮轮公司
     * @param remark
     *            备注
     * @param nameEn
     *            英文名
     * @param wide
     *            宽度
     * @param length
     *            长度
     * @param tonnage
     *            吨位
     * @param floor
     *            所在楼层
     * @param busload
     *            载客量
     * @param totalCabin
     *            总舱数
     * @param avgSpeed
     *            平均航速
     * @param voltageSource
     *            电压电源
     * @param laundromat
     *            自助洗衣
     * @param library
     *            图书馆
     * @param survey
     *            概况
     * @author : 熊玲
     */
    public Map<String, Object> addCruise(String nameCn , String mark , String company ,
                                         String remark , String nameEn , BigDecimal wide ,
                                         BigDecimal length , BigDecimal tonnage ,
                                         BigDecimal floor , BigDecimal busload ,
                                         BigDecimal totalCabin , BigDecimal avgSpeed ,
                                         String voltageSource , String laundromat ,
                                         String library , String survey);

    /**
     * 编辑邮轮档案信息
     * 
     * @param numberCode
     *            邮轮编号
     * @param nameCn
     *            邮轮名称
     * @param mark
     *            代码
     * @param company
     *            邮轮公司
     * @param remark
     *            备注
     * @param nameEn
     *            英文名
     * @param wide
     *            宽度
     * @param length
     *            长度
     * @param tonnage
     *            吨位
     * @param floor
     *            所在楼层
     * @param busload
     *            载客量
     * @param totalCabin
     *            总舱数
     * @param avgSpeed
     *            平均航速
     * @param voltageSource
     *            电源电压
     * @param laundromat
     *            自助洗衣
     * @param library
     *            图书馆
     * @param survey
     * @author : 熊玲
     */
    public Map<String, Object> editCruise(String numberCode , String nameCn , String mark ,
                                          String company , String remark , String nameEn ,
                                          BigDecimal wide , BigDecimal length ,
                                          BigDecimal tonnage , BigDecimal floor ,
                                          BigDecimal busload , BigDecimal totalCabin ,
                                          BigDecimal avgSpeed , String voltageSource ,
                                          String laundromat , String library , String survey);

    /**
     * 删除邮轮档案信息
     * 
     * @param numberCodes
     *            编号字符串(多个以逗号隔开)
     * @author : 熊玲
     */
    public Map<String, Object> deleteCruise(String numberCodes);

    /**
     * 上传图片
     * 
     * @param numberCode
     *            编号
     * @param urls
     *            图片地址(允许传多张)
     * @author : 熊玲
     */
    public Map<String, Object> addImages(String numberCode , String urls);

    /**
     * 邮轮详情
     * 
     * @param numberCode
     *            编号
     * @param detail
     *            详情
     * @author : 熊玲
     */
    public Map<String, Object> addDetail(String numberCode , String detail);

    /**
     * 上传微信图片
     * 
     * @param numberCode
     *            编号
     * @param smallImg
     *            微信小图片
     * @param largeImg
     *            微信大图片
     * @author : 熊玲
     */
    public Map<String, Object> addWeixinImages(String numberCode , String smallImg ,
                                               String largeImg);

}