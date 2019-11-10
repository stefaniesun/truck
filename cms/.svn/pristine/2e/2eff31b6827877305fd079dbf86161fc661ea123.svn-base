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


/**
 * 邮轮
 */
@Entity
@Table(name = "cruise")
public class Cruise {
    @Id
    @Column(name = "iidd" , unique = true , nullable = false)
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator" , strategy = "uuid")
    private String iidd;// 主键

    @Column(name = "number_code" , unique = true , nullable = false)
    private String numberCode; // 编号

    @Column(name = "name_cn")
    private String nameCn; // 名称

    @Column(name = "name_en")
    private String nameEn; // 英文名

    @Column(name = "company")
    private String company; // 邮轮公司编号

    @Column(name = "mark")
    private String mark; // 邮轮代码

    @Column(name = "remark" , length = 10000)
    private String remark; // 备注

    @Column(name = "images" , length = 10000)
    private String images; // 图片
    
    @Column(name = "weixin_small_img" , length = 10000)
    private String weixinSmallImg;  //微信小图
    
    @Column(name = "weixin_large_img" , length = 10000)
    private String weixinLargeImg;  //微信大图

    @Column(name = "add_date")
    private Date addDate; // 添加时间

    @Column(name = "alter_date")
    private Date alterDate; // 修改时间

    @Column(name = "wide")
    private BigDecimal wide; // 邮轮宽度(单位:米)

    @Column(name = "length")
    private BigDecimal length; // 邮轮长度(单位:米)

    @Column(name = "tonnage")
    private BigDecimal tonnage; // 吨位(单位:万吨)

    @Column(name = "floor")
    private BigDecimal floor; // 甲板楼层(单位:层)

    @Column(name = "busload")
    private BigDecimal busload; // 载客量(单位:人)

    @Column(name = "total_cabin")
    private BigDecimal totalCabin; // 船舱总数(单位:间)

    @Column(name = "avg_speed")
    private BigDecimal avgSpeed; // 平均航速(单位:节)

    @Column(name = "voltage_source")
    private String voltageSource; // 电压电源(单位:V)

    @Column(name = "laundromat")
    private int laundromat; // 自助洗衣(0:无 1:有)

    @Column(name = "library")
    private int library; // 图书馆(0:无 1:有)

    @Column(name = "survey" , length = 1000)
    private String survey; // 概况
    
    @Column(name="detail",length=999999999)
    private String detail; //详情
    
    @Column(name="sort_num")
    private int sortNum; //排序序号

    @Transient
    private String companyNameCn; // 邮轮公司名称

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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getWeixinSmallImg() {
        return weixinSmallImg;
    }

    public void setWeixinSmallImg(String weixinSmallImg) {
        this.weixinSmallImg = weixinSmallImg;
    }

    public String getWeixinLargeImg() {
        return weixinLargeImg;
    }

    public void setWeixinLargeImg(String weixinLargeImg) {
        this.weixinLargeImg = weixinLargeImg;
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

    public BigDecimal getWide() {
        return wide;
    }

    public void setWide(BigDecimal wide) {
        this.wide = wide;
    }

    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public BigDecimal getTonnage() {
        return tonnage;
    }

    public void setTonnage(BigDecimal tonnage) {
        this.tonnage = tonnage;
    }

    public BigDecimal getFloor() {
        return floor;
    }

    public void setFloor(BigDecimal floor) {
        this.floor = floor;
    }

    public BigDecimal getBusload() {
        return busload;
    }

    public void setBusload(BigDecimal busload) {
        this.busload = busload;
    }

    public BigDecimal getTotalCabin() {
        return totalCabin;
    }

    public void setTotalCabin(BigDecimal totalCabin) {
        this.totalCabin = totalCabin;
    }

    public BigDecimal getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(BigDecimal avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public String getVoltageSource() {
        return voltageSource;
    }

    public void setVoltageSource(String voltageSource) {
        this.voltageSource = voltageSource;
    }

    public int getLaundromat() {
        return laundromat;
    }

    public void setLaundromat(int laundromat) {
        this.laundromat = laundromat;
    }

    public int getLibrary() {
        return library;
    }

    public void setLibrary(int library) {
        this.library = library;
    }

    public String getSurvey() {
        return survey;
    }

    public void setSurvey(String survey) {
        this.survey = survey;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getSortNum() {
        return sortNum;
    }

    public void setSortNum(int sortNum) {
        this.sortNum = sortNum;
    }

    public String getCompanyNameCn() {
        return companyNameCn;
    }

    public void setCompanyNameCn(String companyNameCn) {
        this.companyNameCn = companyNameCn;
    }

}