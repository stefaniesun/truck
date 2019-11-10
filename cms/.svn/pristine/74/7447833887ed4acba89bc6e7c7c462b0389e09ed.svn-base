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
 * 舱型 
 */
@Entity
@Table(name = "cabin")
public class Cabin implements Comparable<Cabin>{
    @Id
    @Column(name = "iidd", unique = true, nullable = false)
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    private String iidd;// 主键
    
    @Column(name = "cruise")
    private String cruise;  //邮轮编号

    @Column(name = "number_code", unique = true, nullable = false)
    private String numberCode;  //编号

    @Column(name = "name_cn")
    private String nameCn;  //名称

    @Column(name = "volume")
    private BigDecimal volume; //舱型容量
    
    @Column(name = "max_volume")
    private BigDecimal maxVolume; //最大舱型容量
    
    @Column(name = "type")
    private int type;  //舱型标签(类型) 0:内舱   1:海景    2:阳台   3:套房
   
    @Column(name = "mark")
    private String mark;  //代码
    
    @Column(name = "remark",length = 1000)
    private String remark;  //备注

    @Column(name="images",length = 10000)
    private String images; //图片

    @Column(name = "add_date")
    private Date addDate;  //添加时间

    @Column(name = "alter_date")
    private Date alterDate;  //修改时间
    
    @Column(name = "floors")
    private String floors; //所在楼层
    
    @Column(name = "acreage")
    private String acreage; //舱房面积
    
    @Column(name = "survey")
    private String survey; //舱房设施
    
    @Column(name="detail",length=999999999)
    private String detail; //详情
    
    @Column(name="is_open")
    private String isOpen;   //是否开关(开/关,默认开)
    
    @Transient
    private String cruiseNameCn; //邮轮名称

    public String getIidd() {
        return iidd;
    }

    public void setIidd(String iidd) {
        this.iidd = iidd;
    }

    public String getCruise() {
        return cruise;
    }

    public void setCruise(String cruise) {
        this.cruise = cruise;
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

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getMaxVolume() {
        return maxVolume;
    }

    public void setMaxVolume(BigDecimal maxVolume) {
        this.maxVolume = maxVolume;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getFloors() {
        return floors;
    }

    public void setFloors(String floors) {
        this.floors = floors;
    }

    public String getAcreage() {
        return acreage;
    }

    public void setAcreage(String acreage) {
        this.acreage = acreage;
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

    public String getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(String isOpen) {
        this.isOpen = isOpen;
    }

    public String getCruiseNameCn() {
        return cruiseNameCn;
    }

    public void setCruiseNameCn(String cruiseNameCn) {
        this.cruiseNameCn = cruiseNameCn;
    }

	@Override
	public int compareTo(Cabin cabin) {
		return cabin.getNameCn().compareTo(this.nameCn);
	}
    
}