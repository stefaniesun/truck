package xyz.work.base.model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


/**
 * 邮轮公司
 */
@Entity
@Table(name = "company")
public class Company {
    @Id
    @Column(name = "iidd" , unique = true , nullable = false)
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator" , strategy = "uuid")
    private String iidd;// 主键

    @Column(name = "number_code" , unique = true , nullable = false)
    private String numberCode; // 编号

    @Column(name = "name_cn" ,unique = true , nullable = false)
    private String nameCn; // 名称
  
    @Column(name = "name_en")
    private String nameEn; // 英文名

    @Column(name = " website_address" )
    private String websiteAddress; // 官网地址
    
    @Column(name = "remark" , length = 10000)
    private String remark; // 备注

    @Column(name = "add_date")
    private Date addDate; // 添加时间

    @Column(name = "alter_date")
    private Date alterDate; // 修改时间

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

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
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

    public String getWebsiteAddress() {
        return websiteAddress;
    }

    public void setWebsiteAddress(String websiteAddress) {
        this.websiteAddress = websiteAddress;
    }
    

}