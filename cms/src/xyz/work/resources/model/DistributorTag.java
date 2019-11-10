package xyz.work.resources.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="distributor_tag")
public class DistributorTag {

    @Id
    @Column(name="iidd",unique=true,nullable=false)
    @GeneratedValue(generator = "paymentableGenerator")       
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    private String iidd;//主键
    
    @Column(name="number_code",unique=true,nullable=false)
    private String numberCode;      //分销商编号
    
    @Column(name="name")
    private String name;//等级名称
    
    //JSON格式
    //示例
    //[{"minPrice":"1",
    //"maxPrice":"1000",
    //"price":"100",
    //"rebate":"50%"}]
    @Column(name="strategy_json")
    private String strategyJson;//减免价格Json
    
    @Column(name="remark")
    private String remark;
    
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

	public String getStrategyJson() {
		return strategyJson;
	}

	public void setStrategyJson(String strategyJson) {
		this.strategyJson = strategyJson;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
