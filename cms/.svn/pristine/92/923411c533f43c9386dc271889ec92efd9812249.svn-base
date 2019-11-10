package xyz.work.r_base.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 加勒比--航线
 */
@Entity
@Table(name = "r_airway")
public class R_Airway {
    @Id
    @Column(name="iidd",unique=true,nullable=false)
    @GeneratedValue(generator = "paymentableGenerator")       
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    private String iidd;//主键
    
    @Column(name="number_code",unique=true,nullable=false)
    private String numberCode;       //编号
    
    @Column(name = "area")
    private String area;           //航区编号
    
    @Column(name = "area_mark")
    private String areaMark;       //航区代码(冗余)
    
    @Column(name = "mark")
    private String mark;     //航线代码
    
    @Column(name = "name_cn")
    private String nameCn;  //航线名称
    
    @Column(name = "days")
    private BigDecimal days;  //几天
    
    @Column(name = "nights")
    private BigDecimal nights;  //几晚

    public String getIidd() {
        return iidd;
    }

    public void setIidd(String iidd) {
        this.iidd = iidd;
    }

    public String getArea() {
        return area;
    }

    public String getAreaMark() {
        return areaMark;
    }

    public void setAreaMark(String areaMark) {
        this.areaMark = areaMark;
    }

    public String getNumberCode() {
        return numberCode;
    }

    public void setNumberCode(String numberCode) {
        this.numberCode = numberCode;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public BigDecimal getDays() {
        return days;
    }

    public void setDays(BigDecimal days) {
        this.days = days;
    }

    public BigDecimal getNights() {
        return nights;
    }

    public void setNights(BigDecimal nights) {
        this.nights = nights;
    }
    
}