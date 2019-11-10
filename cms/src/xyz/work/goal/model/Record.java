package xyz.work.goal.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 会议记录
 * 
 * @author 熊玲
 */
@Entity
@Table(name = "record")
public class Record {
    @Id
    @Column(name="iidd", unique=true, nullable=false)
    @GeneratedValue(generator="paymentableGenerator")
    @GenericGenerator(name="paymentableGenerator", strategy="uuid")
    private String iidd;          //主键

    @Column(name="number_code", unique=true, nullable=false)
    private String numberCode;   //编号
    
    @Column(name="content", length=999999999)
    private String content;     //内容
    
    @Column(name="details", length=999999999)
    private String details;     //详情
    
    @Column(name="add_date")
    private Date addDate;   //添加时间

    @Column(name="alter_date")
    private Date alterDate;  //修改时间
    
    @Column(name="remark", length=10000)
    private String remark;   //备注

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    
}