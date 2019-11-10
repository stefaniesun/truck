package xyz.work.r_base.model;

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
 * 加勒比--单品
 */
@Entity
@Table(name = "r_tkview")
public class R_Tkview {
    @Id
    @Column(name="iidd",unique=true,nullable=false)
    @GeneratedValue(generator = "paymentableGenerator")       
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    private String iidd;            //主键
    
    @Column(name="number_code",unique=true,nullable=false)
    private String numberCode;      //单品编号
    
    @Column(name="name_cn")
    private String nameCn;          //单品名称
    
    @Column(name="mark")
    private String mark;            //单品代码
    
    @Column(name="cruise")
    private String cruise;          //邮轮编号
    
    @Column(name="cruise_mark")
    private String cruiseMark;      //邮轮代码(冗余)
    
    @Column(name="shipment")
    private String shipment;        //航期编号
    
    @Column(name="shipment_mark")
    private String shipmentMark;    //航期代码(冗余)
    
    @Column(name="shipment_travel_date")
    private Date shipmentTravelDate; //出发日期(冗余)
    
    @Column(name="cabin")
    private String cabin;            //舱型编号
    
    @Column(name="cabin_mark")
    private String cabinMark;        //舱型代码(冗余)
    
    @Transient
    private String cruiseNameCn;     //邮轮名称
    
    @Transient
    private String cabinNameCn;      //舱型名称
    
    @Transient
    private BigDecimal volume;      //舱型容量

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

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
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

    public String getShipment() {
        return shipment;
    }

    public void setShipment(String shipment) {
        this.shipment = shipment;
    }

    public String getShipmentMark() {
        return shipmentMark;
    }

    public void setShipmentMark(String shipmentMark) {
        this.shipmentMark = shipmentMark;
    }

    public Date getShipmentTravelDate() {
        return shipmentTravelDate;
    }

    public void setShipmentTravelDate(Date shipmentTravelDate) {
        this.shipmentTravelDate = shipmentTravelDate;
    }

    public String getCabin() {
        return cabin;
    }

    public void setCabin(String cabin) {
        this.cabin = cabin;
    }

    public String getCabinMark() {
        return cabinMark;
    }

    public void setCabinMark(String cabinMark) {
        this.cabinMark = cabinMark;
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

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

}