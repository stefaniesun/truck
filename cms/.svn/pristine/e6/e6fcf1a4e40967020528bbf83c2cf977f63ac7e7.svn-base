package xyz.work.core.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * @author yys
 *
 */
@Entity
@Table(name="room")
public class Room {

	@Id
	@Column(name="iidd",unique=true,nullable=false)
	@GeneratedValue(generator = "paymentableGenerator")       
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	private String iidd;//主键
	
	@Column(name="room")
	private String room;
	
	@Column(name="real_name")
	private String realName;
	
	@Column(name="e_name")
	private String eName;
	
	@Column(name="birthday")
	private Date birthday;
	
	@Column(name="volume")
	private BigDecimal volume;
	
	@Column(name="cabin_type_name_cn")
	private String cabinTypeNameCn;
	
	@Column(name="cabin_type_number_code")
	private String cabinTypeNumberCode;
	
	@Column(name="shipment_number_code")
	private String shipmentNumberCode;
	
	@Column(name="cruise_number_code")
	private String cruiseNumberCode;
	
	@Column(name="person_number_code")
	private String personNumberCode;
	
	@Column(name="number_code",unique=true,nullable=false)
	private String numberCode;
	
	@Column(name="remark",length=10000)
	private String remark;
	
	@Column(name="stateColor")
	private int stateColor;
	
	@Column(name="creat_date")
	private Date creatDate;
	
	@Column(name="alter_date")
	private Date alterDate;
	
	public Date getCreatDate() {
		return creatDate;
	}

	public void setCreatDate(Date creatDate) {
		this.creatDate = creatDate;
	}

	public Date getAlterDate() {
		return alterDate;
	}

	public void setAlterDate(Date alterDate) {
		this.alterDate = alterDate;
	}

	public String geteName() {
		return eName;
	}

	public void seteName(String eName) {
		this.eName = eName;
	}
	
	public String getCabinTypeNumberCode() {
		return cabinTypeNumberCode;
	}

	public void setCabinTypeNumberCode(String cabinTypeNumberCode) {
		this.cabinTypeNumberCode = cabinTypeNumberCode;
	}
	
	public int getStateColor() {
		return stateColor;
	}

	public void setStateColor(int stateColor) {
		this.stateColor = stateColor;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPersonNumberCode() {
		return personNumberCode;
	}

	public void setPersonNumberCode(String personNumberCode) {
		this.personNumberCode = personNumberCode;
	}
	
	public String getNumberCode() {
		return numberCode;
	}

	public void setNumberCode(String numberCode) {
		this.numberCode = numberCode;
	}

	public String getShipmentNumberCode() {
		return shipmentNumberCode;
	}

	public void setShipmentNumberCode(String shipmentNumberCode) {
		this.shipmentNumberCode = shipmentNumberCode;
	}

	public String getCruiseNumberCode() {
		return cruiseNumberCode;
	}

	public void setCruiseNumberCode(String cruiseNumberCode) {
		this.cruiseNumberCode = cruiseNumberCode;
	}

	public String getIidd() {
		return iidd;
	}

	public void setIidd(String iidd) {
		this.iidd = iidd;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

	public String getCabinTypeNameCn() {
		return cabinTypeNameCn;
	}

	public void setCabinTypeNameCn(String cabinTypeNameCn) {
		this.cabinTypeNameCn = cabinTypeNameCn;
	}
}
