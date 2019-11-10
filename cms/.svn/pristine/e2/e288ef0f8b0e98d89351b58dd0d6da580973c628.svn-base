package xyz.work.taobao.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="taobao_base_info")
public class TaobaoBaseInfo {
    
    @Id
    @Column(name="iidd",unique=true,nullable=false)
    @GeneratedValue(generator = "paymentableGenerator")       
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    private String iidd;   //主键
    
    @Column(name="number_code",unique=true,nullable=false)
    private String numberCode;  //编号

    @Column(name="accom_nights")
    private int accomNights; //几晚
    
    @Column(name="cruise")
    private String cruise;  //邮轮编号
    
    @Column(name="category_id")
    private String categoryId; //
    
    @Column(name="prov")
    private String prov; //宝贝所在地省份(不填默认设置 浙江)
    
    @Column(name="city")
    private String city; //宝贝所在城市(不填默认设置 杭州)
    
    @Column(name="taobao_desc",length=50000)
    private String desc; //商品描述(不超过50000个字符) 必填
    
    /*
     * 出发地 
     * 跟团游商品必填,自由行商品选填,邮轮商品不填。
     * 【重要提示-此属性会作为搜索/筛选条件】
     */
    @Column(name="from_locations")
    private String fromLocations; 
    
    /*
     * 商品类型: 目前只有 6-国际邮轮
     * 1-国内跟团游         2-国内自由行
     * 3-出境跟团游         4-出境自由行 
     * 5-境外当地玩乐     6-国际邮轮
     * 7-国内当地玩乐     9-国内邮轮 
     * 10-同城活动          14-境外玩乐套餐
     */
    @Column(name="item_type")
    private int itemType;  
    
    /*
     * 商品图片路径：
     * 最多支持5张,第一张为主图 必填,其余四张可选填
     * 多张图片间使用英文逗号分隔
     */
    @Column(name="pic_urls",length = 100000)
    private String picUrls; 
    
    /*
     * 商品亮点:
     * 1、目前最多支持4个亮点,超过4个的亮点描述不会被保存; 
     * 2、每个亮点描述35个字符以内 ;
     * 3、每个亮点间用英文逗号分隔
     */
    @Column(name="sub_titles")
    private String subTitles; 
    
    /**
     * 商品标题: 30个字符以内
     * 【重要-此字段会作为搜索条件】
     */
    @Column(name="title")
    private String title; 
    
    /*
     * 目的地:所有商品必填
     * 填写中文,以英文逗号分隔,最多可填12个
     * 如果国家底下还有城市，则必须精确到城市
     * 【重要提示-此字段会作为搜索/筛选条件】
     */
    @Column(name="to_locations")
    private String toLocations; 
    
    @Column(name="trip_max_days")
    private int tripMaxDays; //旅游日程-天数
    
    @Column(name="taobao_travel_item")
    private String taobaoTravelItem;   //用于关联
    
    @Column(name="item_url",length=10000)
    private String itemUrl;  //宝贝链接
    
    @Column(name="add_date")
    private Date addDate;
    
    @Column(name="alter_date")
    private Date alterDate;
    
    @Column(name="remark")
    private String remark;
    
    @Transient
    private String cruiseNameCn; //邮轮名称
    
    @Transient
    private String channel; //渠道编号

    @Transient
    private String channelNameCn;  //渠道名称
    
    @Transient
    private int itemStatus; //宝贝状态
    
    @Transient
    private String itemId; //宝贝ID
    
    @Transient
    private String skuInfos; //宝贝sku
    
    @Transient
    private int isUpdate;   //是否更新SKU库存(0：否，1：是(默认))

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

    public int getAccomNights() {
        return accomNights;
    }

    public void setAccomNights(int accomNights) {
        this.accomNights = accomNights;
    }

    public String getCruise() {
        return cruise;
    }

    public void setCruise(String cruise) {
        this.cruise = cruise;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getFromLocations() {
        return fromLocations;
    }

    public void setFromLocations(String fromLocations) {
        this.fromLocations = fromLocations;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getPicUrls() {
        return picUrls;
    }

    public void setPicUrls(String picUrls) {
        this.picUrls = picUrls;
    }

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public String getSubTitles() {
        return subTitles;
    }

    public void setSubTitles(String subTitles) {
        this.subTitles = subTitles;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getToLocations() {
        return toLocations;
    }

    public void setToLocations(String toLocations) {
        this.toLocations = toLocations;
    }

    public int getTripMaxDays() {
        return tripMaxDays;
    }

    public void setTripMaxDays(int tripMaxDays) {
        this.tripMaxDays = tripMaxDays;
    }

    public String getTaobaoTravelItem() {
        return taobaoTravelItem;
    }

    public void setTaobaoTravelItem(String taobaoTravelItem) {
        this.taobaoTravelItem = taobaoTravelItem;
    }

    public String getItemUrl() {
        return itemUrl;
    }

    public void setItemUrl(String itemUrl) {
        this.itemUrl = itemUrl;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCruiseNameCn() {
        return cruiseNameCn;
    }

    public void setCruiseNameCn(String cruiseNameCn) {
        this.cruiseNameCn = cruiseNameCn;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getChannelNameCn() {
        return channelNameCn;
    }

    public void setChannelNameCn(String channelNameCn) {
        this.channelNameCn = channelNameCn;
    }

    public int getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(int itemStatus) {
        this.itemStatus = itemStatus;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getSkuInfos() {
        return skuInfos;
    }

    public void setSkuInfos(String skuInfos) {
        this.skuInfos = skuInfos;
    }

    public int getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(int isUpdate) {
        this.isUpdate = isUpdate;
    }
    
}