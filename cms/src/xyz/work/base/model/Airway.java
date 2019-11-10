package xyz.work.base.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "airway")
public class Airway {
    @Id
    @Column(name = "iidd" , unique = true , nullable = false)
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator" , strategy = "uuid")
    private String iidd;// 主键
    
    @Column(name = "area")
    private String area;//航区编号

    @Column(name = "number_code" , unique = true , nullable = false)
    private String numberCode; // 航线编号

    @Column(name = "name_cn")
    private String nameCn;  //航线名称
    
    @Column(name = "mark")
    private String mark;  //航线代码
    
    @Column(name = "days")
    private BigDecimal days;  //几天
    
    @Column(name = "nights")
    private BigDecimal nights;  //几晚
    
    @Column(name = "remark",length = 10000)
    private String remark;  //航线备注
    
    @Column(name="add_date")
    private Date addDate;   //添加时间

    @Column(name="alter_date")
    private Date alterDate;  //修改时间
    
    @Transient
    private  String  areaNameCn;  //航区名称

    public String getIidd() {
        return iidd;
    }

    public void setIidd(String iidd) {
        this.iidd = iidd;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getNumberCode() {
        return numberCode;
    }

    public String index() {
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

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
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

    public String getAreaNameCn() {
        return areaNameCn;
    }

    public void setAreaNameCn(String areaNameCn) {
        this.areaNameCn = areaNameCn;
    }

}