package xyz.work.sell.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "ptview_sku")
public class PtviewSku {
    @Id
    @Column(name="iidd",unique=true,nullable=false)
    @GeneratedValue(generator="paymentableGenerator")       
    @GenericGenerator(name="paymentableGenerator",strategy="uuid")
    private String iidd;
    
    @Column(name="ptview",nullable=false)
    private String ptview;  //所属产品
    
    @Column(name="number_code",nullable=false)
    private String numberCode;  //产品SKU套餐编号
    
    @Column(name="name_cn")
    private String nameCn;   //套餐名称
    
    @Column(name="tkview_type")
    private String tkviewType;  //单品种类

    @Column(name="add_date")
    private Date addDate;  //新增时间

    @Column(name="alter_date")
    private Date alterDate;  //修改时间
    
    @Transient
    private List<PtviewSkuStock> stockList;
    
    public String getIidd() {
        return iidd;
    }

    public void setIidd(String iidd) {
        this.iidd = iidd;
    }

    public String getPtview() {
        return ptview;
    }

    public void setPtview(String ptview) {
        this.ptview = ptview;
    }

    public String getNumberCode() {
        return numberCode;
    }

    public void setNumberCode(String numberCode) {
        this.numberCode = numberCode;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
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

    public List<PtviewSkuStock> getStockList() {
        return stockList;
    }

    public void setStockList(List<PtviewSkuStock> stockList) {
        this.stockList = stockList;
    }
    
}