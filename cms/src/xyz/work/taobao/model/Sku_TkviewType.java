package xyz.work.taobao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="sku_tkview_type",uniqueConstraints = {@UniqueConstraint(columnNames={"sku","tkview_type"})})
public class Sku_TkviewType {
    @Id
    @Column(name="iidd",unique=true,nullable=false)
    @GeneratedValue(generator = "paymentableGenerator")       
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    private String iidd;//主键
    
    @Column(name = "number_code", unique = true, nullable = false)
    private String numberCode;  //编号
    
    @Column(name = "sku")
    private String sku;   //淘宝SKU套餐
    
    @Column(name = "tkview_type")
    private String tkviewType;   //单品种类

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

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getTkviewType() {
        return tkviewType;
    }

    public void setTkviewType(String tkviewType) {
        this.tkviewType = tkviewType;
    }
    
}