package xyz.work.r_base.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/**
 * 加勒比--航期
 */
@Entity
@Table(name = "r_shipment")
public class R_Shipment {
    @Id
    @Column(name="iidd",unique=true,nullable=false)
    @GeneratedValue(generator = "paymentableGenerator")       
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    private String iidd;//主键
    
    @Column(name = "cruise")
    private String cruise;         //邮轮编号
    
    @Column(name = "cruise_mark")
    private String cruiseMark;     //邮轮代码(冗余)
    
    @Column(name = "area")
    private String area;           //航区编号
    
    @Column(name = "area_mark")
    private String areaMark;       //航区代码(冗余)
    
    @Column(name = "airway")
    private String airway;         //航线编号
    
    @Column(name = "airway_mark")
    private String airwayMark;     //航线代码(冗余)
    
    @Column(name = "number_code")
    private String numberCode;     //航期编号
    
    @Column(name = "mark")
    private String mark;           //航期代码
    
    @Column(name="travel_date")
    private Date travelDate;       //出发日期
    
    @Column(name = "port")
    private String port;           //出发港口
    
    @Column(name = "port_mark")
    private String portMark;       //出发港口代码
    
    @Column(name = "trip_days")
    private Integer tripDays;      //行程天数
    
    @Column(name = "ship_desc")
    private String shipDesc;       //航期描述
    
    @Transient
    private String cruiseNameCn;   //邮轮名称
    
    @Transient
    private String areaNameCn;     //航区名称
    
    @Transient
    private String airwayNameCn;   //航线名称
    
    @Transient
    private String portNameCn;     //港口名称

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

    public String getCruiseMark() {
        return cruiseMark;
    }

    public void setCruiseMark(String cruiseMark) {
        this.cruiseMark = cruiseMark;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAreaMark() {
        return areaMark;
    }

    public void setAreaMark(String areaMark) {
        this.areaMark = areaMark;
    }

    public String getAirway() {
        return airway;
    }

    public void setAirway(String airway) {
        this.airway = airway;
    }

    public String getAirwayMark() {
        return airwayMark;
    }

    public void setAirwayMark(String airwayMark) {
        this.airwayMark = airwayMark;
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

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPortMark() {
        return portMark;
    }

    public void setPortMark(String portMark) {
        this.portMark = portMark;
    }

    public Integer getTripDays() {
        return tripDays;
    }

    public void setTripDays(Integer tripDays) {
        this.tripDays = tripDays;
    }

    public String getShipDesc() {
        return shipDesc;
    }

    public void setShipDesc(String shipDesc) {
        this.shipDesc = shipDesc;
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

    public String getAirwayNameCn() {
        return airwayNameCn;
    }

    public void setAirwayNameCn(String airwayNameCn) {
        this.airwayNameCn = airwayNameCn;
    }

    public String getPortNameCn() {
        return portNameCn;
    }

    public void setPortNameCn(String portNameCn) {
        this.portNameCn = portNameCn;
    }
    
}