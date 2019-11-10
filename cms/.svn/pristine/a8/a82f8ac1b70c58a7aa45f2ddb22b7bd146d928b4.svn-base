package xyz.work.resources.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="shipment_data")
public class ShipmentData {

	@Id
	@Column(name="iidd",unique=true,nullable=false)
	@GeneratedValue(generator = "paymentableGenerator")       
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	private String iidd;//主键
	
	@Column(name="number_code",unique=true,nullable=false)
    private String numberCode;  //编号
	
	@Column(name="name")
	private String name;
	
	@Column(name="cruise")
	private String cruise;
	
	@Column(name="start_city")
	private String startCity;
	
	@Column(name="airway_name")
	private String airwayName;
	
	@Column(name="days")
	private int days;
	
	@Column(name="date")
	private String date;
	
	@Column(name="cruise_name_cn")
	private String cruiseNameCn;
	
	@Column(name="n2")
	private int N2;
	
	@Column(name="n3")
	private int N3;
	
	@Column(name="n4")
	private int N4;
	
	@Column(name="h2")
	private int H2;
	
	@Column(name="h3")
	private int H3;
	
	@Column(name="h4")
	private int H4;
	
	@Column(name="y2")
	private int Y2;
	
	@Column(name="y3")
	private int Y3;
	
	@Column(name="y4")
	private int Y4;
	
	@Column(name="t2")
	private int T2;
	
	@Column(name="t3")
	private int T3;
	
	@Column(name="t4")
	private int T4;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCruise() {
		return cruise;
	}

	public void setCruise(String cruise) {
		this.cruise = cruise;
	}

	public int getN2() {
		return N2;
	}

	public void setN2(int n2) {
		N2 = n2;
	}

	public int getN3() {
		return N3;
	}

	public void setN3(int n3) {
		N3 = n3;
	}

	public int getN4() {
		return N4;
	}

	public String getStartCity() {
		return startCity;
	}

	public void setStartCity(String startCity) {
		this.startCity = startCity;
	}

	public void setN4(int n4) {
		N4 = n4;
	}

	public int getH2() {
		return H2;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public String getAirwayName() {
		return airwayName;
	}

	public void setAirwayName(String airwayName) {
		this.airwayName = airwayName;
	}

	public void setH2(int h2) {
		H2 = h2;
	}

	public int getH3() {
		return H3;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCruiseNameCn() {
		return cruiseNameCn;
	}

	public void setCruiseNameCn(String cruiseNameCn) {
		this.cruiseNameCn = cruiseNameCn;
	}

	public void setH3(int h3) {
		H3 = h3;
	}

	public int getH4() {
		return H4;
	}

	public void setH4(int h4) {
		H4 = h4;
	}

	public int getY2() {
		return Y2;
	}

	public void setY2(int y2) {
		Y2 = y2;
	}

	public int getY3() {
		return Y3;
	}

	public void setY3(int y3) {
		Y3 = y3;
	}

	public int getY4() {
		return Y4;
	}

	public void setY4(int y4) {
		Y4 = y4;
	}

	public int getT2() {
		return T2;
	}

	public void setT2(int t2) {
		T2 = t2;
	}

	public int getT3() {
		return T3;
	}

	public void setT3(int t3) {
		T3 = t3;
	}

	public int getT4() {
		return T4;
	}

	public void setT4(int t4) {
		T4 = t4;
	}
}
