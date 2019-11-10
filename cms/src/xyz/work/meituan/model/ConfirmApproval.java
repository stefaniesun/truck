package xyz.work.meituan.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="confirm_approval")
public class ConfirmApproval {
    @Id
    @Column(name="iidd",unique=true,nullable=false)
    @GeneratedValue(generator = "paymentableGenerator")       
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    private String iidd;//主键
    
    @Column(name="mt_orderid",unique=true,nullable=false)
    private String mtOrderid;//美团订单ID
    
    @Column(name="total_price")
    private BigDecimal totalPrice;//订单总金额
    
    @Column(name="mt_producid")
    private String mtProductId;//美团产品id
    
    @Column(name="product")
    private String product;//我方产品编号
    
    @Column(name="sku")
    private String sku;//我方sku编号
    
    @Column(name="status")
    private int status;//0待确认 1通过确认 -1未通过确认
    
    @Column(name="quantity")
    private int quantity;//购买数量
    
    @Column(name="contact_name")
    private String contactName;//联系人姓名
    
    @Column(name="contact_phone")
    private String contactPhone;//联系电话
    
    @Column(name="travel_date")
    private String travelDate;//出行日期
    
    
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

    public String getMtOrderid() {
        return mtOrderid;
    }

    public void setMtOrderid(String mtOrderid) {
        this.mtOrderid = mtOrderid;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getMtProductId() {
        return mtProductId;
    }

    public void setMtProductId(String mtProductId) {
        this.mtProductId = mtProductId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(String travelDate) {
        this.travelDate = travelDate;
    }

    
    
    
    
    
    
}
