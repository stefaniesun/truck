package xyz.work.r_base.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 加勒比--港口
 */
@Entity
@Table(name = "r_port")
public class R_Port {
    @Id
    @Column(name="iidd",unique=true,nullable=false)
    @GeneratedValue(generator = "paymentableGenerator")       
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    private String iidd;//主键
    
    @Column(name = "number_code",unique=true,nullable=false)
    private String numberCode;    //编号
    
    @Column(name = "location")
    private String location;      //所在地区
    
    @Column(name = "name_cn")
    private String nameCn;        //港口名称
    
    @Column(name = "mark")
    private String mark;         //港口标识

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
    
}