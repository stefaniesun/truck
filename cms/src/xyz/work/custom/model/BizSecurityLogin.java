
package xyz.work.custom.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="biz_security_login")
public class BizSecurityLogin{
	@Id
	@Column(name="iidd",unique=true,nullable=false)
	@GeneratedValue(generator = "paymentableGenerator")       
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	private String iidd;//主键

	@Column(name="apikey",unique=true)
    private String apikey;//密钥
    
	@Column(name="number_code")
    private String numberCode;
	
    @Column(name="name_cn")
    private String nameCn;
    
    @Column(name="img")
    private String img;
    
    @Column(name="flag_register")
    private int flagRegister;
    
    @Column(name="user_code")
    private String userCode;
    
    @Column(name="add_date")
    private Date addDate; //添加时间
    
    @Column(name="expire_date")
    private Date expireDate; //过期时间

    public String getIidd() {
        return iidd;
    }

    public void setIidd(String iidd) {
        this.iidd = iidd;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public Date getAddDate() {
        return addDate;
    }

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

	public String getNumberCode() {
		return numberCode;
	}

	public void setNumberCode(String numberCode) {
		this.numberCode = numberCode;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public int getFlagRegister() {
		return flagRegister;
	}

	public void setFlagRegister(int flagRegister) {
		this.flagRegister = flagRegister;
	}
    
}
