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
 * 对接人信息
 */
@Entity
@Table(name = "joiner")
public class Joiner {
    @Id
    @Column(name = "iidd", unique = true, nullable = false)
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    private String iidd;// 主键

    @Column(name = "number_code", unique = true, nullable = false)
    private String numberCode;  //编号
    
    @Column(name = "name_cn")
    private String nameCn; //姓名
    
    @Column(name = "phone")
    private String phone;  //电话
 
    @Column(name = "we_chat")
    private String weChat; //微信
    
    @Column(name = "provider")
    private String provider;  //供应商
    
    @Column(name="qq")
    private String qq;//QQ号
    
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

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWeChat() {
        return weChat;
    }

    public void setWeChat(String weChat) {
        this.weChat = weChat;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
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