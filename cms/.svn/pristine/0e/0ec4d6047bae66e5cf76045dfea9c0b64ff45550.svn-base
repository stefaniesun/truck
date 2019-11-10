package xyz.work.r_base.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "r_task")
public class R_Task {

	
	public static final String ITINERARY_TXT_PATH="C://";
    public static final String PRICE_TXT_PATH="C://ppf";
	
	@Id
    @Column(name="iidd",unique=true,nullable=false)
    @GeneratedValue(generator = "paymentableGenerator")       
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    private String iidd;            //主键
    
    @Column(name="number_code",unique=true,nullable=false)
    private String numberCode;    
    
    @Column(name="start_time")
    private Date startTime; 
    
    @Column(name="end_time")
    private Date endTime; 
    
    @Column(name="update_time")
    private Date updateTime; 
    
    @Column(name="itinerary_data_count")
    private int itineraryDataCount;
    
    @Column(name="price_data_count")
    private int priceDataCount;
    
    @Column(name="remark")
    private String remark;
    
    @Column(name="auto")
    private int auto;     //1代表自动下行  0代表手动下行
    
    @Column(name="status")
    private int status;   //0代表执行中  1代表执行完成

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

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getItineraryDataCount() {
		return itineraryDataCount;
	}

	public void setItineraryDataCount(int itineraryDataCount) {
		this.itineraryDataCount = itineraryDataCount;
	}

	public int getPriceDataCount() {
		return priceDataCount;
	}

	public void setPriceDataCount(int priceDataCount) {
		this.priceDataCount = priceDataCount;
	}

	public int getAuto() {
		return auto;
	}

	public void setAuto(int auto) {
		this.auto = auto;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
