package xyz.work.r_base.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 加勒比--航区
 */
@Entity
@Table(name = "r_area")
public class R_Area {
    @Id
    @Column(name="iidd",unique=true,nullable=false)
    @GeneratedValue(generator = "paymentableGenerator")       
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    private String iidd;             //主键
    
    @Column(name="number_code",unique=true,nullable=false)
    private String numberCode;       //航区编号
    
    @Column(name = "mark" , unique = true , nullable = false)
    private String mark;            //航区代码

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

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
    
}