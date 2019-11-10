
package xyz.work.sell.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="ptview",uniqueConstraints={@UniqueConstraint(columnNames={"cruise","shipment"})})
public class Ptview {
    @Id
    @Column(name="iidd",unique=true,nullable=false)
    @GeneratedValue(generator="paymentableGenerator")       
    @GenericGenerator(name="paymentableGenerator",strategy="uuid")
	private String iidd;
	
	@Column(name="number_code",nullable=false)
	private String numberCode;  //产品编号
	
	@Column(name="name_cn",nullable=false)
	private String nameCn;  //产品名称
    
    @Column(name="company")
    private String company; //所属邮轮公司(根据邮轮获取)
    
    @Column(name="company_name_cn")
    private String companyNameCn; 
    
    @Column(name="cruise")
    private String cruise; //所属邮轮
    
    @Column(name="cruise_name_cn")
    private String cruiseNameCn; //邮轮名称
    
    @Column(name="shipment")
    private String shipment; //所属航期
    
    @Column(name="travel_date")
    private Date travelDate;  //出发日期
    
    @Column(name="ship_mark")
    private String shipMark;  //航期代码
    
    @Column(name="image")
    private String image;//主图
    
    @Column(name="detail_image")
    private String detailImage; //详情图
    
    @Column(name="airway")
    private String airway; //所属航线(根据航期获取)
    
    @Column(name="airway_name_cn")
    private String airwayNameCn; //航线名称

    @Column(name="days")
    private int days;  //几天
    
    @Column(name="base_price")
    private BigDecimal basePrice;  //基础价格
    
    @Column(name="from_place")
    private String fromPlace; //出发地
    
    @Column(name="date_mark")
    private String dateMark; //日期短码
    
    @Column(name="is_open")
    private String isOpen;   //是否开关(开/关,默认开)
    
    @Column(name="remark")
    private String remark;  //备注

    @Column(name="add_date")
    private Date addDate;  //新增时间

    @Column(name="alter_date")
    private Date alterDate;  //修改时间

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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCruise() {
        return cruise;
    }

    public void setCruise(String cruise) {
        this.cruise = cruise;
    }

    public String getShipment() {
        return shipment;
    }

    public void setShipment(String shipment) {
        this.shipment = shipment;
    }

    public Date getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(Date travelDate) {
        this.travelDate = travelDate;
    }

    public String getShipMark() {
        return shipMark;
    }

    public String getCompanyNameCn() {
		return companyNameCn;
	}

	public void setCompanyNameCn(String companyNameCn) {
		this.companyNameCn = companyNameCn;
	}

	public void setShipMark(String shipMark) {
        this.shipMark = shipMark;
    }

    public String getAirway() {
        return airway;
    }

    public void setAirway(String airway) {
        this.airway = airway;
    }

    public String getCruiseNameCn() {
		return cruiseNameCn;
	}

	public void setCruiseNameCn(String cruiseNameCn) {
		this.cruiseNameCn = cruiseNameCn;
	}

	public String getFromPlace() {
        return fromPlace;
    }

    public void setFromPlace(String fromPlace) {
        this.fromPlace = fromPlace;
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

    public String getImage() {
		return image;
	}

	public String getDateMark() {
		return dateMark;
	}

	public void setDateMark(String dateMark) {
		this.dateMark = dateMark;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public BigDecimal getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(BigDecimal basePrice) {
		this.basePrice = basePrice;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
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

    public String getDetailImage() {
        return detailImage;
    }

    public void setDetailImage(String detailImage) {
        this.detailImage = detailImage;
    }

    public String getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(String isOpen) {
        this.isOpen = isOpen;
    }

    public String getAirwayNameCn() {
        return airwayNameCn;
    }

    public void setAirwayNameCn(String airwayNameCn) {
        this.airwayNameCn = airwayNameCn;
    }

}