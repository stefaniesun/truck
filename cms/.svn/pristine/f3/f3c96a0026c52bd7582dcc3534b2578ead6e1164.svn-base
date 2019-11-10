package xyz.work.custom.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 客户标签表
 * @author 张伯东
 * @version 下午 15:03:00
 */
@Entity
@Table(name = "user_tag")
public class UserTag {
	
	@Id
    @Column(name = "iidd" , unique = true , nullable = false)
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator" , strategy = "uuid")
    private String iidd;// 主键
	
	@Column(name = "number_code" , unique = true , nullable = false)
	private String numberCode;//编号
	
	@Column(name = "name")
	private String name;//客户名称
	
	@Column(name = "remark")
    private String remark;//备注
	
	@Column(name = "add_date")
	private Date addDate;//添加时间
	
	@Column(name = "alter_date")
	private Date alterDate;//修改时间

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
	
}