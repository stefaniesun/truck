package xyz.work.taobao.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="taobao_sku_info",uniqueConstraints = {@UniqueConstraint(columnNames={"package_name","taobao_travel_item"})})
public class TaobaoSkuInfo {

    @Id
    @Column(name="iidd",unique=true,nullable=false)
    @GeneratedValue(generator = "paymentableGenerator")       
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    private String iidd;   //主键
    
    @Column(name="number_code",unique=true,nullable=false)
    private String numberCode;  //编号
    
    @Column(name="package_name")
    private String packageName; //套餐名称
    
    @Column(name="taobao_travel_item")
    private String taobaoTravelItem;  //用于关联
    
    @Column(name="add_date")
    private Date addDate;
    
    @Column(name="is_lock")
    private int isLock;      //是否固定SKU 
    
    @Column(name="is_sync")
    private int isSync;     //是否同步SKU(0：同步；1：不同步)
    
    @Column(name="alter_date")
    private Date alterDate;
    
    @Column(name="flag")
    private int flag;       //套餐详情有变(0:；1:)
    
    @Transient
    private int isUpdate;   //(SKU库存的属性)是否更新SKU库存(0：否(默认)，1：是)
    
    @Transient
    private List<TaobaoSkuInfoDetail> detailList = new ArrayList<>();  //SKU日历库存集合

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

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
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

    public int getIsLock() {
        return isLock;
    }

    public void setIsLock(int isLock) {
        this.isLock = isLock;
    }

    public int getIsSync() {
        return isSync;
    }

    public void setIsSync(int isSync) {
        this.isSync = isSync;
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

    public int getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(int isUpdate) {
        this.isUpdate = isUpdate;
    }

    public List<TaobaoSkuInfoDetail> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<TaobaoSkuInfoDetail> detailList) {
        this.detailList = detailList;
    }
    
}