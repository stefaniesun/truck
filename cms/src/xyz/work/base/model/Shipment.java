package xyz.work.base.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/**
 * 航期
 */
@Entity
@Table(name="shipment")
public class Shipment {
    @Id
    @Column(name="iidd",unique=true,nullable=false)
    @GeneratedValue(generator = "paymentableGenerator")       
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    private String iidd;//主键
    
    @Column(name = "cruise")
    private String cruise;  //邮轮编号
    
    @Column(name = "area")
    private String area;  //航区编号
    
    @Column(name = "airway")
    private String airway;  //航线编号
    
    @Column(name="number_code",unique=true,nullable=false)
    private String numberCode;  //航期编号
    
    @Column(name="mark")
    private String mark;//航期代码
    
    @Column(name="travel_date")
    private Date travelDate;  //出发日期
    
    @Column(name="travel_end_date")
    private Date travelEndDate;  //结束日期
    
    @Column(name="final_sale_date")
    private Date finalSaleDate; //最后售卖日期
    
    @Column(name = "start_place")
    private String startPlace; // 出发地点
    
    @Column(name = "trip_days")
    private Integer tripDays;    //行程天数
    
    @Column(name="detail",length=999999999)
    private String detail; //详情
    
    @Column(name="remark",length=10000)
    private String remark; //备注  
    
    @Column(name="add_date")
    private Date addDate; //添加时间

    @Column(name="alter_date")
    private Date alterDate; //修改时间
    
    @Transient
    private String cruiseNameCn; //邮轮名称
    
    @Transient
    private String areaNameCn; //航区名称
    
    @Transient
    private String airwayNameCn;

    public String getIidd() {
        return iidd;
    }

    public void setIidd(String iidd) {
        this.iidd = iidd;
    }
    
    public String getAirwayNameCn() {
        return airwayNameCn;
    }

    public void setAirwayNameCn(String airwayNameCn) {
        this.airwayNameCn = airwayNameCn;
    }

    public String getCruise() {
        return cruise;
    }

    public void setCruise(String cruise) {
        this.cruise = cruise;
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

    public void setNumberCode(String numberCode) {
        this.numberCode = numberCode;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public Date getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(Date travelDate) {
        this.travelDate = travelDate;
    }

    public Date getTravelEndDate() {
        return travelEndDate;
    }

    public void setTravelEndDate(Date travelEndDate) {
        this.travelEndDate = travelEndDate;
    }

    public Date getFinalSaleDate() {
        return finalSaleDate;
    }

    public void setFinalSaleDate(Date finalSaleDate) {
        this.finalSaleDate = finalSaleDate;
    }

    public String getStartPlace() {
        return startPlace;
    }

    public void setStartPlace(String startPlace) {
        this.startPlace = startPlace;
    }

    public Integer getTripDays() {
        return tripDays;
    }

    public void setTripDays(Integer tripDays) {
        this.tripDays = tripDays;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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

    public String getAreaNameCn() {
        return areaNameCn;
    }

    public void setAreaNameCn(String areaNameCn) {
        this.areaNameCn = areaNameCn;
    }
    
    public String getAirway() {
        return airway;
    }

    public void setAirway(String airway) {
        this.airway = airway;
    }
    
}