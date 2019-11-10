package xyz.work.taobao.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="taobao_travel_item")
public class TaobaoTravelItem {

    @Id
    @Column(name="iidd",unique=true,nullable=false)
    @GeneratedValue(generator = "paymentableGenerator")       
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    private String iidd;   //主键
     
    @Column(name="number_code",unique=true,nullable=false)
    private String numberCode;  //编号
    
    @Column(name="created")
    private Date created;  //创建时间(商品在淘宝上的创建时间)
    
    @Column(name="item_id")
    private String itemId;  //宝贝ID(淘宝上的)
    
    @Column(name="seller_id")
    private String sellerId;  //卖家ID
    
    @Column(name="item_status")
    private int itemStatus;  //宝贝状态(0:正常；-1:用户删除；-2:用户下架；-3:小二下架； -4:小二删除； -5:从未上架)
    
    @Column(name="item_type")
    private int itemType;  //宝贝类型(1-国内跟团游； 2-国内自由行； 3-出境跟团游； 4-出境自由行 ；5-境外当地玩乐； 6-国际邮轮 ；9-国内邮轮)
    
    @Column(name="modified")
    private Date modified;  //修改日期(商品在淘宝上的修改时间)
    
    @Column(name="request_id")
    private String requestId;
    
    @Column(name="channel")
    private String channel;  //渠道编号
    
    @Column(name="channel_name_cn")
    private String channelNameCn;   //渠道名称
    
    @Column(name="add_date")
    private Date addDate;
    
    @Column(name="alter_date")
    private Date alterDate;
    
    public String getChannelNameCn() {
        return channelNameCn;
    }

    public void setChannelNameCn(String channelNameCn) {
        this.channelNameCn = channelNameCn;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(int itemStatus) {
        this.itemStatus = itemStatus;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
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
