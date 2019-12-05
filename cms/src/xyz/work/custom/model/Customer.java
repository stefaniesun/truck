package xyz.work.custom.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 客户表
 * @author 熊玲
 * @version 2017-1-22上午10:49:47
 */
@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @Column(name = "iidd" , unique = true , nullable = false)
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator" , strategy = "uuid")
    private String iidd;// 主键 
    
    @Column(name = "number_code",unique = true,nullable = false)
    private String numberCode;   //编号
    
    @Column(name = "phone")
    private String phone;  
    
    @Column(name = "name_cn")
    private String nameCn;  
    
    @Column(name = "img")
    private String img;  
    
    @Column(name = "sex")
    private String sex;  
    
    @Column(name = "province")
    private String province;
    
    @Column(name = "country")
    private String country;
    
    @Column(name = "city")
    private String city;
    
    @Column(name = "openid")
    private String openid; 
    
    @Column(name = "flag_register")
    private int flagRegister; 
    
    @Column(name = "enabled")
    private int enabled;  
    
    @Column(name = "remark",length = 10000)
    private String remark;  //备注
    
    @Column(name="add_date")
    private Date addDate;   //添加时间
    
    @Column(name="register_date")
    private Date registerDate;   //添加时间

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}

	public String getNameCn() {
		return nameCn;
	}

	public void setNameCn(String nameCn) {
		this.nameCn = nameCn;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public int getFlagRegister() {
		return flagRegister;
	}

	public void setFlagRegister(int flagRegister) {
		this.flagRegister = flagRegister;
	}
    
}