package xyz.work.resources.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
/**
 * 分销商
 */
@Entity
@Table(name="distributor")
public class Distributor {

	public static String DISTRIBUTOR_TYPE_PERSONAL="PERSONAL";//个人
	public static String DISTRIBUTOR_TYPE_PLATFORM="PLATFORM";//平台
	public static String DISTRIBUTOR_TYPE_AGENCY="AGENCY";//旅行社
	public static String DISTRIBUTOR_TYPE_OTHER="OTHER";//其他
	
    @Id
    @Column(name="iidd",unique=true,nullable=false)
    @GeneratedValue(generator = "paymentableGenerator")       
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    private String iidd;//主键
    
    @Column(name="number_code",unique=true,nullable=false)
    private String numberCode;      //分销商编号
    
    @Column(name="name")
    private String name;//分销商名称
    
    @Column(name="type")
    private String type;//分销商类型
    
    @Column(name="link_username")
    private String linkUsername;//联系人
    
    @Column(name="link_phone")
    private String linkPhone;//联系人电话
    
    @Column(name="distributor_tag")
    private String distributorTag;//分销等级
    
    @Column(name="remark")
    private String remark;
    
    @Column(name="is_enable")
    private int isEnable;//是否启用
    
    @Transient
    private String distributorTagNameCn; //分销等级名称
    
    @Column(name="add_date")
    private Date addDate;
    
    @Column(name="alert_date")
    private Date alertDate;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLinkUsername() {
		return linkUsername;
	}

	public void setLinkUsername(String linkUsername) {
		this.linkUsername = linkUsername;
	}

	public String getLinkPhone() {
		return linkPhone;
	}

	public void setLinkPhone(String linkPhone) {
		this.linkPhone = linkPhone;
	}

	public String getDistributorTag() {
		return distributorTag;
	}

	public void setDistributorTag(String distributorTag) {
		this.distributorTag = distributorTag;
	}

	public Date getAddDate() {
		return addDate;
	}

	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}

	public Date getAlertDate() {
		return alertDate;
	}

	public void setAlertDate(Date alertDate) {
		this.alertDate = alertDate;
	}

    public String getDistributorTagNameCn() {
        return distributorTagNameCn;
    }

    public void setDistributorTagNameCn(String distributorTagNameCn) {
        this.distributorTagNameCn = distributorTagNameCn;
    }

    public int getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(int isEnable) {
		this.isEnable = isEnable;
	}

	public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}