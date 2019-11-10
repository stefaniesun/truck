package xyz.work.sell.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "ptview_sku_tkview_type")
public class PtviewSku_TkviewType {
    @Id
    @Column(name="iidd",unique=true,nullable=false)
    @GeneratedValue(generator="paymentableGenerator")       
    @GenericGenerator(name="paymentableGenerator",strategy="uuid")
    private String iidd;
    
    @Column(name="number_code",nullable=false)
    private String numberCode;  //编号
    
    @Column(name="ptview_sku",nullable=false)
    private String ptviewSku;  //产品SKU套餐
   
    @Column(name="tkview_type",nullable=false)
    private String tkviewType;  //单品种类
    
    @Column(name="add_date")
    private Date addDate;  //新增时间

    @Column(name="alter_date")
    private Date alterDate;  //修改时间

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

    public String getPtviewSku() {
        return ptviewSku;
    }

    public void setPtviewSku(String ptviewSku) {
        this.ptviewSku = ptviewSku;
    }

    public String getTkviewType() {
        return tkviewType;
    }

    public void setTkviewType(String tkviewType) {
        this.tkviewType = tkviewType;
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