package xyz.work.resources.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="channel")
public class Channel {

    @Id
    @Column(name="iidd",unique=true,nullable=false)
    @GeneratedValue(generator = "paymentableGenerator")       
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    private String iidd;   //主键
    
    @Column(name="number_code",unique=true,nullable=false)
    private String numberCode;      //编号
    
    @Column(name="name_cn")
    private String nameCn;  //渠道名称
    
    @Column(name="session_key")
    private String sessionKey;
    
    @Column(name="code",length=100000)
    private String code;
    
    @Column(name="app_key",length=100000)
    private String appKey;
    
    @Column(name="app_secret",length=100000)
    private String appSecret;
    
    @Column(name="redirect_uri",length=100000)
    private String redirectUri;
    
    @Column(name="is_authorize")
    private int isAuthorize;  //是否授权(0:否   1:是)
    
    //JSON格式
    //示例
    //[{"minPrice":"1",
    //"maxPrice":"1000",
    //"price":"100"}]
    @Column(name="price_strategy",length=5000)
    private String priceStrategy;//加价规则
    
    @Column(name="remark",length = 1000)
    private String remark;  
    
    @Column(name="add_date")
    private Date addDate;
    
    @Column(name="alter_date")
    private Date alterDate;

    @Transient
    private String possessorNameCn; //所属机构
    
    public int getIsAuthorize() {
        return isAuthorize;
    }

    public void setIsAuthorize(int isAuthorize) {
        this.isAuthorize = isAuthorize;
    }

    public String getPossessorNameCn() {
        return possessorNameCn;
    }

    public void setPossessorNameCn(String possessorNameCn) {
        this.possessorNameCn = possessorNameCn;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
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

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
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

    public String getPriceStrategy() {
        return priceStrategy;
    }

    public void setPriceStrategy(String priceStrategy) {
        this.priceStrategy = priceStrategy;
    }
    
    
}
