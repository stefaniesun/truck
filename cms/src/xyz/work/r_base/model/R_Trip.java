package xyz.work.r_base.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 加勒比--行程模版
 */
@Entity
@Table(name = "r_trip")
public class R_Trip {
    @Id
    @Column(name = "iidd" , unique = true , nullable = false)
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator" , strategy = "uuid")
    private String iidd;            //主键

    @Column(name = "number_code" , unique = true , nullable = false)
    private String numberCode;      //编号

    @Column(name = "priority")
    private int priority;          //排序
    
    @Column(name = "airway")
    private String airway;        //航线编号
    
    @Column(name = "airway_mark")
    private String airwayMark;    //航线代码(冗余)
    
    @Column(name = "time")
    private String time;          //时间  第几天(yyyy-MM-dd)
    
    @Column(name = "time_type")
    private String timeType;        //时间类型   0:起航EMBARK 、1:停靠DOCKED、2:巡航CRUISING、3:结束DEBARK
    
    @Column(name = "port")
    private String port;         //港口编号
    
    @Column(name = "port_mark")
    private String portMark;     //港口代码(冗余)
    
    @Column(name = "port_name_cn")
    private String portNameCn;   //港口名称(冗余)

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

    public String getAirwayMark() {
        return airwayMark;
    }

    public void setAirwayMark(String airwayMark) {
        this.airwayMark = airwayMark;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
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

    public String getPortNameCn() {
        return portNameCn;
    }

    public void setPortNameCn(String portNameCn) {
        this.portNameCn = portNameCn;
    }
    
}