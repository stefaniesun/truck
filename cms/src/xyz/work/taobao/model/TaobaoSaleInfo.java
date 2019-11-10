package xyz.work.taobao.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 商品的销售属性相关信息 
 */
@Entity
@Table(name="taobao_sale_info")
public class TaobaoSaleInfo {
    
    @Id
    @Column(name="iidd",unique=true,nullable=false)
    @GeneratedValue(generator = "paymentableGenerator")       
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    private String iidd;   //主键
    
    @Column(name="number_code",unique=true,nullable=false)
    private String numberCode;  //编号
    
    /**
     * 预约至少提前天数
     * 至少提前天数,最晚成团提前天数,
     * 最小0天,最大为300天;
     * 不传则为0,默认值：0 
     */
    @Column(name="duration")
    private int duration; 
    
    /**
     * 是否支持会员打折:(0:否；1:是) 
     * 可选值:true,false;  默认值:false(不打折)。
     * 不传的话默认为false
     */
    @Column(name="has_discount")
    private String hasDiscount;
    
    /**
     * 是否提供发票 :(0:否；1:是)
     * 默认为false 
     * 仅C商家需要设置该值 
     * B商家强制提供发票
     */
    @Column(name="has_invoice")
    private String hasInvoice; 
    
    /**
     * 是否橱窗推荐:(0:否；1:是)
     * 可选值:true,false;  默认值：false(不推荐)
     */
    @Column(name="has_showcase")
    private String hasShowcase;
    
    /**
     * 旧版电子凭证:
     * 若要支持旧版电子凭证则需填写。
     * 电子凭证码商,格式：码商id:码商nick。 
     * 1、商家必须是电子凭证卖家才能发布电子凭证商品。
     * 参考链接 http://bangpai.taobao.com/group/thread/14051111-283426841.htm?spm=0.0.0.0.TRlt53 
     * 2、 发布电子凭证商品,merchant字段必填。
     * 若为淘宝发码,则merchant设置为 0:淘宝 
     * 3、 network_id联系码商提供
     */
    @Column(name="merchant")
    private String merchant; 
    
    /**
     * 旧版电子凭证:若要支持旧版电子凭证则需填写。电子凭证网点ID
     */
    @Column(name="network_id")
    private String networkId;
    
    /**
     * 接口新增参数：新版电子凭证信息。
     * 如果传递了此参数,则sales_info中旧版电子凭证信息将被忽略,
     * 否则电子凭证信息将以旧版电子凭证参数为准。
     * 如果新、旧版参数都没传,则该商品不支持电子凭证
     */
    
    /**
     * Number confirm_type 可选  资源确认方式。1：即时确认;2：二次确认
     * Number confirm_time 可选  资源确认时长 。1：2个工作小时内确认;2：6个工作小时内确认;3：9个工作小时内确认
     */
    
    /**
     * 商品售卖类型:(默认为0)
     * 0、日历商品; 1、预约商品; 2、非日历商品
     * 注：在调用商品基本信息更新接口 更新商品时,
     *     不允许变更商品的售卖类型
     */
    @Column(name="sale_type")
    private int saleType; 
    
    /**
     * 关联商品与店铺类目 :
     * 结构:"cid1,cid2,...,"。
     * 如何获取卖家店铺类目具体参见：
     *    http://open.taobao.com/doc2/apiDetail.htm?apiId=65
     */
    @Column(name="seller_cids",length=1000)
    private String sellerCids;
    
    /**
     * 预约开始日期:
     * 预约商品必填，普通商品可不填。
     * 可选出发开始日期，格式：yyyy-MM-dd。
     * 对于普通商品，根据日历库存的最早时间来自动计算。
     * 对于预约商品则为必填字段
     */
    @Column(name="start_combo_date")
    private Date startComboDate; 
    
    /**
     * 预约商品必填，普通商品可不填。可选出发结束日期
     * 格式：年-月-日 日期必须是最近300天内的,
     * 最早和最晚日期跨度最大为90天。
     * 对于普通商品,根据日历库存的最晚时间来自动计算;
     * 对于预约商品则为必填字段
     */
    @Column(name="end_combo_date")
    private Date endComboDate;
    
    /**
     * 减库存方式:
     * 0、拍下减库存; 1、付款减库存。
     * 不传默认为0
     */
    @Column(name="sub_stock")
    private int subStock; 
    
    /**
     * 旧版电子凭证(0:否；1:是)
     * 是否支持系统自动退款,true则表示支持
     */
    @Column(name="support_onsale_auto_refund")
    private String supportOnsaleAutoRefund; 
    
    @Column(name="taobao_travel_item")
    private String taobaoTravelItem;  //用于关联

    @Column(name="add_date")
    private Date addDate;
    
    @Column(name="alter_date")
    private Date alterDate;

    public String getIidd() {
        return iidd;
    }

    public void setIidd(String iidd) {
        this.iidd = iidd;
    }

    public String getNumberCode() {
        return numberCode;
    }

    public void setNumberCode(String numberCode) {
        this.numberCode = numberCode;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Date getEndComboDate() {
        return endComboDate;
    }

    public void setEndComboDate(Date endComboDate) {
        this.endComboDate = endComboDate;
    }

    public String getHasDiscount() {
        return hasDiscount;
    }

    public void setHasDiscount(String hasDiscount) {
        this.hasDiscount = hasDiscount;
    }

    public String getHasInvoice() {
        return hasInvoice;
    }

    public void setHasInvoice(String hasInvoice) {
        this.hasInvoice = hasInvoice;
    }

    public String getHasShowcase() {
        return hasShowcase;
    }

    public void setHasShowcase(String hasShowcase) {
        this.hasShowcase = hasShowcase;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getNetworkId() {
        return networkId;
    }

    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }

    public int getSaleType() {
        return saleType;
    }

    public void setSaleType(int saleType) {
        this.saleType = saleType;
    }

    public String getSellerCids() {
        return sellerCids;
    }

    public void setSellerCids(String sellerCids) {
        this.sellerCids = sellerCids;
    }

    public Date getStartComboDate() {
        return startComboDate;
    }

    public void setStartComboDate(Date startComboDate) {
        this.startComboDate = startComboDate;
    }

    public int getSubStock() {
        return subStock;
    }

    public void setSubStock(int subStock) {
        this.subStock = subStock;
    }

    public String getSupportOnsaleAutoRefund() {
        return supportOnsaleAutoRefund;
    }

    public void setSupportOnsaleAutoRefund(String supportOnsaleAutoRefund) {
        this.supportOnsaleAutoRefund = supportOnsaleAutoRefund;
    }

    public String getTaobaoTravelItem() {
        return taobaoTravelItem;
    }

    public void setTaobaoTravelItem(String taobaoTravelItem) {
        this.taobaoTravelItem = taobaoTravelItem;
    }
    
    public Date getAddDate() {
        return addDate;
    }

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }

    public Date getAlterDate() {
        return alterDate;
    }

    public void setAlterDate(Date alterDate) {
        this.alterDate = alterDate;
    }
    
}
