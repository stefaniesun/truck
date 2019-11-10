package xyz.work.taobao.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="taobao_sku_info_detail",uniqueConstraints = {@UniqueConstraint(columnNames={"date","taobao_sku_info"})})
public class TaobaoSkuInfoDetail {

    @Id
    @Column(name="iidd",unique=true,nullable=false)
    @GeneratedValue(generator = "paymentableGenerator")       
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    private String iidd;               //主键
    
    @Column(name="number_code",unique=true,nullable=false)
    private String numberCode;         //编号 
    
    @Column(name="out_id")
    private String outId;              //对应本地tkview

    @Column(name="date")
    private Date date;                 //日期(对于普通商品必填,对于预约商品该字段不填)
    
    @Column(name="price")
    private BigDecimal price;          //价格,以分为单位
    
    @Column(name="price_type")
    private int priceType;             //价格类型(1-成人价;2-儿童价;3-单房差)
    
    @Column(name="stock")
    private int stock;                //库存数
    
    @Column(name="taobao_sku_info")
    private String taobaoSkuInfo;     //关联SKU
    
    @Column(name="airway")
    private String airway;            //航线
    
    @Column(name="add_date")
    private Date addDate;
    
    @Column(name="alter_date")
    private Date alterDate;
    
    @Column(name="flag")
    private int flag;                //标记(是否有改动)0:无；1:有
    
    @Column(name="is_relation")
    private int isRelation;         //是否与单品有关联(0:无；1:有)
    
    @Column(name="is_lock")
    private int isLock;             //是否锁定价格，默认 0(不锁定); 1:锁定价格
    
    @Column(name="is_edit")
    private int isEdit;             //是否允许编辑价格，默认0(允许); 1:不允许编辑
    
    @Column(name="is_sync")
    private int isSync;             //是否同步， 默认0(同步); 1:不同步
    
    @Column(name="is_update")
    private int isUpdate;           //是否更新SKU库存(0：否，1：是(默认))
    
    @Column(name="is_normal")
    private int isNormal;           //是否按正常加价逻辑 默认0是  1不是
    
    @Column(name="out_stock")
    private String outStock;        //关联Stock
    
    @Transient
    private String tkviewNameCn;    //单品名称
    
    @Transient 
    private String airwayNameCn;    //航线名称
    
    @Transient
    private int tkviewCount;       //关联单品个数
    
    @Transient
    private int updateFlag;
    
    @Transient
    private String packageName;       //关联单品个数

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

    public String getOutId() {
        return outId;
    }

    public void setOutId(String outId) {
        this.outId = outId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getPriceType() {
        return priceType;
    }

    public void setPriceType(int priceType) {
        this.priceType = priceType;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getTaobaoSkuInfo() {
        return taobaoSkuInfo;
    }

    public void setTaobaoSkuInfo(String taobaoSkuInfo) {
        this.taobaoSkuInfo = taobaoSkuInfo;
    }

    public String getAirway() {
        return airway;
    }

    public void setAirway(String airway) {
        this.airway = airway;
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

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getIsRelation() {
        return isRelation;
    }

    public void setIsRelation(int isRelation) {
        this.isRelation = isRelation;
    }

	public int getIsLock() {
        return isLock;
    }

    public void setIsLock(int isLock) {
        this.isLock = isLock;
    }

    public int getIsEdit() {
        return isEdit;
    }

    public int getUpdateFlag() {
		return updateFlag;
	}

	public void setUpdateFlag(int updateFlag) {
		this.updateFlag = updateFlag;
	}

	public void setIsEdit(int isEdit) {
        this.isEdit = isEdit;
    }
    
    public int getIsSync() {
        return isSync;
    }

    public void setIsSync(int isSync) {
        this.isSync = isSync;
    }

    public int getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(int isUpdate) {
        this.isUpdate = isUpdate;
    }

    public String getOutStock() {
        return outStock;
    }

    public void setOutStock(String outStock) {
        this.outStock = outStock;
    }

    public String getTkviewNameCn() {
        return tkviewNameCn;
    }

    public void setTkviewNameCn(String tkviewNameCn) {
        this.tkviewNameCn = tkviewNameCn;
    }

    public String getAirwayNameCn() {
        return airwayNameCn;
    }

    public void setAirwayNameCn(String airwayNameCn) {
        this.airwayNameCn = airwayNameCn;
    }

	public int getIsNormal() {
		return isNormal;
	}

	public void setIsNormal(int isNormal) {
		this.isNormal = isNormal;
	}

    public int getTkviewCount() {
        return tkviewCount;
    }

    public void setTkviewCount(int tkviewCount) {
        this.tkviewCount = tkviewCount;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
   
}