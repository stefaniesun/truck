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
 * 供应商账户
 */
@Entity
@Table(name = "account")
public class Account {
    @Id
    @Column(name = "iidd", unique = true, nullable = false)
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    private String iidd;// 主键

    @Column(name = "number_code", unique = true, nullable = false)
    private String numberCode;  //编号
    
    @Column(name = "account_number")
    private String accountNumber; //对公帐号
    
    @Column(name = "cash_account")
    private String cashAccount;  //收款户名（抬头）
 
    @Column(name = "bank_of_deposit")
    private String bankOfDeposit;  //开户银行
    
    @Column(name = "provider")
    private String provider;  //供应商
    
    @Column(name = "is_enable")
    private int isEnable;  //是否启用：0-否    1-是(默认)
    
    @Column(name = "remark",length=10000)
    private String remark;   // 备注
    
    @Column(name = "add_date")
    private Date addDate;

    @Column(name = "alter_date")
    private Date alterDate;
    
    @Transient
    private String providerNameCn;  //供应商名称

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

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCashAccount() {
        return cashAccount;
    }

    public void setCashAccount(String cashAccount) {
        this.cashAccount = cashAccount;
    }

    public String getBankOfDeposit() {
        return bankOfDeposit;
    }

    public void setBankOfDeposit(String bankOfDeposit) {
        this.bankOfDeposit = bankOfDeposit;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
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

    public String getProviderNameCn() {
        return providerNameCn;
    }

    public void setProviderNameCn(String providerNameCn) {
        this.providerNameCn = providerNameCn;
    }
    
}