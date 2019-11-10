package xyz.work.resources.model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;



/**
 * 单品种类
 */
@Entity
@Table(name = "tkview_type")
public class TkviewType {
    @Id
    @Column(name = "iidd" , unique = true , nullable = false)
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator" , strategy = "uuid")
    private String iidd;// 主键

    @Column(name = "number_code" , unique = true , nullable = false)
    private String numberCode; // 编号

    @Column(name = "cruise")
    private String cruise; // 邮轮

    @Column(name = "cabin")
    private String cabin; // 资源

    @Column(name = "remark" , length = 10000)
    private String remark; // 备注

    @Column(name = "add_date")
    private Date addDate; // 添加时间

    @Column(name = "alter_date")
    private Date alterDate; // 修改时间

    @Transient
    private String cruiseNameCn;// 邮轮名称

    @Transient
    private String cabinNameCn; // 资源名称

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

    public String getCruise() {
        return cruise;
    }

    public void setCruise(String cruise) {
        this.cruise = cruise;
    }

    public String getCabin() {
        return cabin;
    }

    public void setCabin(String cabin) {
        this.cabin = cabin;
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

    public String getCruiseNameCn() {
        return cruiseNameCn;
    }

    public void setCruiseNameCn(String cruiseNameCn) {
        this.cruiseNameCn = cruiseNameCn;
    }

    public String getCabinNameCn() {
        return cabinNameCn;
    }

    public void setCabinNameCn(String cabinNameCn) {
        this.cabinNameCn = cabinNameCn;
    }
    
}