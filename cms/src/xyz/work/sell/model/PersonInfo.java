
package xyz.work.sell.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="person_info")
public class PersonInfo{
	@Id
	@Column(name="iidd",unique=true,nullable=false)
	@GeneratedValue(generator = "paymentableGenerator")       
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	private String iidd;//主键
	
	@Column(name="number_code",unique=true,nullable=false)
	private String numberCode;//主键
	
	@Column(name="real_name")
	private String realName;//姓名
	
	@Column(name="ename")
	private String ename;//英文名
	
	@Column(name="card")
	private String card;//证件(身份证)
	
	@Column(name="birthday")
	private String birthday;//出生日期
	
	@Column(name="passport")
	private String passport;//证件2(护照/签证)
	
	@Column(name="expire_date")
	private String expireDate;//过期日期
	
	@Column(name="domicile")
	private String domicile;//户籍地
	
	@Column(name="sex")
	private String sex;//性别
	
	@Column(name="nation")
	private String nation;//国籍
	
	@Column(name="person_type")
	private String personType;//客户类型 ： 成人、儿童等等
	
	@Column(name="distributor")
	private String distributor;  //所属分销商
	
	@Column(name="possessor")
	private String possessor; //所属机构
	
	@Column(name="add_date")
	private Date addDate;
	
	@Column(name="alter_date")
	private Date alterDate;

    @Transient
    private String possessorNameCn; //所属机构名称

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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getDomicile() {
        return domicile;
    }

    public void setDomicile(String domicile) {
        this.domicile = domicile;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getPersonType() {
        return personType;
    }

    public void setPersonType(String personType) {
        this.personType = personType;
    }

    public String getDistributor() {
        return distributor;
    }

    public void setDistributor(String distributor) {
        this.distributor = distributor;
    }

    public String getPossessor() {
        return possessor;
    }

    public void setPossessor(String possessor) {
        this.possessor = possessor;
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

    public String getPossessorNameCn() {
        return possessorNameCn;
    }

    public void setPossessorNameCn(String possessorNameCn) {
        this.possessorNameCn = possessorNameCn;
    }
	
}