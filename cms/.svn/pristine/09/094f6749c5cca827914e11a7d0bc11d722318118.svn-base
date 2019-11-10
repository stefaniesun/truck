package xyz.work.r_base.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 加勒比--航程
 */
@Entity
@Table(name = "r_voyage")
public class R_Voyage {
    @Id
    @Column(name="iidd",unique=true,nullable=false)
    @GeneratedValue(generator = "paymentableGenerator")       
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    private String iidd;             //主键
    
    @Column(name = "number_code" , unique = true , nullable = false)
    private String numberCode;      //编号
    
    @Column(name = "priority")
    private int priority;           //顺序
    
    @Column(name = "shipment")
    private String shipment;        //航期编号
    
    @Column(name = "shipment_mark")
    private String shipmentMark;    //航期代码(冗余)
    
    @Column(name = "port")
    private String port;            //所属港口
    
    @Column(name = "port_mark")
    private String portMark;       //港口代码(冗余)
    
    @Column(name = "port_name_cn")
    private String portNameCn;     //港口名称(冗余)

    @Column(name = "time")
    private String time;           //时间 第几天(yyyy-MM-dd)
    
    @Column(name = "time_type")
    private String timeType;         //时间类型   0:起航EMBARK 、1:停靠DOCKED、2:巡航CRUISING、3:结束DEBARK 
    
    @Column(name = "arrival_time")
    private String arrivalTime;   //到达时间

    @Column(name = "leave_time")
    private String leaveTime;     //出发时间

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

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(String leaveTime) {
        this.leaveTime = leaveTime;
    }
    
}