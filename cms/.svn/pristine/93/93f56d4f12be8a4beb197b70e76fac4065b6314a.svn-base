package xyz.work.excel.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

/**
 * 加价区间
 */
@Entity
@Table(name = "price_strategy",uniqueConstraints = {@UniqueConstraint(columnNames={"min_price","max_price"})})
public class PriceStrategy {
    
	
    @Id
    @Column(name = "iidd" , unique = true , nullable = false)
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator" , strategy = "uuid")
    private String iidd;// 主键
    
    @Column(name = "number_code", unique = true , nullable = false)
    private String numberCode; 
    
    @Column(name = "min_price", nullable = false)
    private BigDecimal minPrice;       //区间最小值
    
    @Column(name = "max_price", nullable = false)
    private BigDecimal maxPrice;      //区间最大值
    
    @Column(name = "price_markup", nullable = false)
    private BigDecimal priceMarkup;   //加价值
    
    @Column(name = "remark",length = 10000)
    private String remark;            //备注
    
    @Column(name="add_date")
    private Date addDate;             //添加时间

    @Column(name="alter_date")
    private Date alterDate;           //修改时间

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

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public BigDecimal getPriceMarkup() {
        return priceMarkup;
    }

    public void setPriceMarkup(BigDecimal priceMarkup) {
        this.priceMarkup = priceMarkup;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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