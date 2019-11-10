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
 * 供应商
 */
@Entity
@Table(name = "provider")
public class Provider {
	@Id
	@Column(name = "iidd", unique = true, nullable = false)
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	private String iidd;// 主键

	@Column(name = "number_code", unique = true, nullable = false)
	private String numberCode;  //编号

	@Column(name = "name_cn")
	private String nameCn; //名称
	
	@Column(name = "mark", unique = true, nullable = false)
	private String mark; //查询代码
	
	@Column(name = "address")
	private String address;// 材料邮寄地址
	
	@Column(name = "we_chat")
	private String weChat; //微信
	
	@Column(name = "talk_group")
	private String talkGroup;  //讨论组
	
	@Column(name = "is_responsible")
	private int isResponsible; //是否负责和客户签订合同 0.否 1.是 2.用自己的合同 3.其它
	
	@Column(name = "sign")
	private String sign; //签订方式
	
	@Column(name = "settlement")
	private String settlement; //结算方式
	
	@Column(name = "policy")
	private String policy; //后返政策

    @Column(name = "status")
	private int status; //合作状态 （0未合作，1合作中）
    
    @Column(name = "remark",length = 10000)
    private String remark;// 备注
    
    @Column(name = "add_date")
	private Date addDate;

    @Column(name = "alter_date")
	private Date alterDate;
    
    @Column(name = "grade")
    private int grade;      //等级 1(等级最高)、2、3、4、5
    
    @Transient
    private String accountSum;
    
    @Transient
    private String joinerSum;
    
    public String getWeChat() {
        return weChat;
    }

    public void setWeChat(String weChat) {
        this.weChat = weChat;
    }
    
    public String getTalkGroup() {
        return talkGroup;
    }

    public void setTalkGroup(String talkGroup) {
        this.talkGroup = talkGroup;
    }

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

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getIsResponsible() {
        return isResponsible;
    }

    public void setIsResponsible(int isResponsible) {
        this.isResponsible = isResponsible;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSettlement() {
        return settlement;
    }

    public void setSettlement(String settlement) {
        this.settlement = settlement;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getAccountSum() {
        return accountSum;
    }

    public void setAccountSum(String accountSum) {
        this.accountSum = accountSum;
    }

    public String getJoinerSum() {
        return joinerSum;
    }

    public void setJoinerSum(String joinerSum) {
        this.joinerSum = joinerSum;
    }
    
}