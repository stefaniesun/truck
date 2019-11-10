package xyz.work.reserve.model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;


/**
 * 预约表
 * 
 * @author 熊玲
 * @version 2016-12-22下午4:30:53
 */
@Entity
@Table(name = "reserve")
public class Reserve {
    @Id
    @Column(name = "iidd" , unique = true , nullable = false)
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator" , strategy = "uuid")
    private String iidd;// 主键
    
    @Column(name="number_code",unique=true,nullable=false)
    private String numberCode;  //编号
    
    @Column(name="operator")
    private String operator;  //操作人
    
    @Column(name="channel")
    private String channel; //渠道编号
    
    @Column(name="channel_name_cn")
    private String channelNameCn; //渠道名称
    
    @Column(name="order_num")
    private String orderNum; //订单编号(本地的)
    
    @Column(name="client_code")
    private String clientCode; //订单票单号(本地的)
    
    @Column(name="tid")
    private String tid; 
    
    @Column(name="oid")
    private String oid;
    
    @Column(name="tkview")
    private String tkview; //单品编号(本地的)
    
    @Column(name="tkview_name_cn")
    private String tkviewNameCn; //单品名称(本地的)
    
    @Column(name="possessor")
    private String possessor; //所属机构
    
    @Column(name="add_date")
    private Date addDate; //添加时间
    
    @Column(name="alter_date")
    private Date alterDate; //修改时间
    
    @Column(name="remark",length = 10000)
    private String remark; //备注
    
    @Column(name="item_id")
    private String itemId; //预约宝贝编号
    
    @Column(name="item_name_cn")
    private String itemNameCn; //预约宝贝名称
    
    @Column(name="item_count")
    private int itemCount; //预约宝贝数量
    
    @Column(name="person_info")
    private String personInfo; //出行人信息(JSON字符串)
    
    @Column(name="person_count")
    private int personCount; //出行总人数
    
    @Column(name="link_man")
    private String linkMan; //联系人
    
    @Column(name="link_phone")
    private String linkPhone; //联系人电话
    
    @Column(name="link_email")
    private String linkEmail; //联系人邮箱
    
    @Column(name="flag")
    private int flag; //预约状态    0:审核  1:失败  2:成功
    
    @Column(name="flag_remark",length = 10000)
    private String flagRemark; //预约状态备注
    
    @Column(name="changes")
    private int changes; //0：无改动   1：有改动
    
    @Column(name="changes_remark",length = 10000)
    private String changesRemark; //改动备注
    
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

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getChannelNameCn() {
        return channelNameCn;
    }

    public void setChannelNameCn(String channelNameCn) {
        this.channelNameCn = channelNameCn;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getTkview() {
        return tkview;
    }

    public void setTkview(String tkview) {
        this.tkview = tkview;
    }

    public String getTkviewNameCn() {
        return tkviewNameCn;
    }

    public void setTkviewNameCn(String tkviewNameCn) {
        this.tkviewNameCn = tkviewNameCn;
    }

    public String getPossessor() {
        return possessor;
    }

    public void setPossessor(String possessor) {
        this.possessor = possessor;
    }

    public String getPossessorNameCn() {
        return possessorNameCn;
    }

    public void setPossessorNameCn(String possessorNameCn) {
        this.possessorNameCn = possessorNameCn;
    }

    public int getChanges() {
        return changes;
    }

    public void setChanges(int changes) {
        this.changes = changes;
    }

    public String getChangesRemark() {
        return changesRemark;
    }

    public void setChangesRemark(String changesRemark) {
        this.changesRemark = changesRemark;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemNameCn() {
        return itemNameCn;
    }

    public void setItemNameCn(String itemNameCn) {
        this.itemNameCn = itemNameCn;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public String getPersonInfo() {
        return personInfo;
    }

    public void setPersonInfo(String personInfo) {
        this.personInfo = personInfo;
    }

    public int getPersonCount() {
        return personCount;
    }

    public void setPersonCount(int personCount) {
        this.personCount = personCount;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public String getLinkPhone() {
        return linkPhone;
    }

    public void setLinkPhone(String linkPhone) {
        this.linkPhone = linkPhone;
    }
    
    public String getLinkEmail() {
        return linkEmail;
    }

    public void setLinkEmail(String linkEmail) {
        this.linkEmail = linkEmail;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getFlagRemark() {
        return flagRemark;
    }

    public void setFlagRemark(String flagRemark) {
        this.flagRemark = flagRemark;
    }
    
}