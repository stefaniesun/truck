package xyz.work.base.model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;



/**
 * 港口
 */
@Entity
@Table(name = "port")
public class Port {
    @Id
    @Column(name = "iidd" , unique = true , nullable = false)
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator" , strategy = "uuid")
    private String iidd;// 主键

    @Column(name = "number_code" , unique = true , nullable = false)
    private String numberCode;   //港口编号

    @Column(name = "name_cn")
    private String nameCn;      //港口名称
    
    @Column(name = "country")
    private String country;     //城市

    @Column(name = "address")
    private String address;     //地址
    
    @Column(name = "longitude")
    private String longitude;   //经度

    @Column(name = "latitude")
    private String latitude;     //纬度
    
    @Column(name = "details" , length = 10000)
    private String details;      //港口详情
    
    @Column(name = "remark" , length = 10000)
    private String remark;      //港口备注

    @Column(name = "add_date")
    private Date addDate;       //添加时间

    @Column(name = "alter_date")
    private Date alterDate;     //修改时间
    
    @Column(name="images",length = 10000)
    private String images; //图片

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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
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

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

}