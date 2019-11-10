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
 * 航线 -行程模版
 * 
 * @author 熊玲
 */
@Entity
@Table(name = "trip")
public class Trip {
    @Id
    @Column(name="iidd", unique=true, nullable=false)
    @GeneratedValue(generator="paymentableGenerator")
    @GenericGenerator(name="paymentableGenerator", strategy="uuid")
    private String iidd;// 主键

    @Column(name="number_code", unique=true, nullable=false)
    private String numberCode; // 编号

    @Column(name="priority")
    private int priority;   //排序
    
    @Column(name="airway")
    private String airway;  //航线编号
    
    @Column(name="time")
    private String time;    //时间  第几天(yyyy-MM-dd)
    
    @Column(name="time_type")
    private int timeType;  //时间类型   0:登船日、1:岸上观光日、2:航海日、3:离船日
    
    @Column(name="port")
    private String port;   //港口编号
    
    @Column(name="add_date")
    private Date addDate;   //添加时间

    @Column(name="alter_date")
    private Date alterDate;  //修改时间
    
    @Column(name="images",length=10000)
    private String images;    //图片
    
    @Column(name = "detail" , length=10000)
    private String detail;    //行程规划
    
    @Transient
    private String airwayNameCn; //航线名称
    
    @Transient
    private String portNameCn;   //港口名称
    
    @Transient
    private String portAddress;  //港口地址

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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getAirway() {
        return airway;
    }

    public void setAirway(String airway) {
        this.airway = airway;
    }
    
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getTimeType() {
        return timeType;
    }

    public void setTimeType(int timeType) {
        this.timeType = timeType;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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

    public String getPortAddress() {
        return portAddress;
    }

    public void setPortAddress(String portAddress) {
        this.portAddress = portAddress;
    }
    
}