package xyz.work.taobao.model;

/**
 * 淘宝订单池类(主订单)
 */
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
@Entity
@Table(name="taobao_order_tbt")
public class TaobaoOrderTbt {
	@Id
	@Column(name="iidd",unique=true,nullable=false)
	@GeneratedValue(generator = "paymentableGenerator")       
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	private String iidd;//主键
		
	@Column(name="number_code")
	private String numberCode;
	
	@Column(name="tid",unique=true,nullable=false)
	private String tid;//淘宝子订单对应的主订单ID，不是唯一的
		
	@Column(name="seller_nick")
	private String sellerNick;//卖家昵称 就是淘宝账号
	
	@Column(name="buy_nick")
	private String buyNick;//买家昵称
	
	@Column(name="available_confirm_fee",precision=16,scale=2)
	private BigDecimal availableConfirmFee; //未收金额
	
	/* 卖家实际收到的支付宝打款金额:
	 * 由于子订单可以部分确认收货，这个金额会随着子订单的确认收货而不断增加，交易成功后等于买家实付款减去退款金额
	 * 精确到2位小数; 如:200.07，表示:200元7分
	 * 单位:元。
	 */
	@Column(name="received_payment",precision=16,scale=2)
	private BigDecimal receivedPayment;//卖家实际收到的支付宝打款金额
	
	/* 订单状态.可选 值:
	 * WAIT_BUYER_PAY           等待买家付款,
	 * WAIT_BUYER_CONFIRM_GOODS 卖家已发 货,
	 * WAIT_SELLER_SEND_GOODS   买家已付款,
	 * TRADE_FINISHED           交易成功,
	 * TRADE_CLOSED             交易关 闭,
	 * TRADE_CLOSED_BY_TAOBAO   交易被淘宝关闭,
	 * TRADE_NO_CREATE_PAY      没有创建外部交易(支付宝交 易),
	 * OTHER                    其他状态
	 */
	@Column(name="status")
	private String status;//订单状态
	
	@Column(name="step_trade_status")
	private String stepTradeStatus; //分阶段付款订单状态（例：万人团）
	
	@Transient
	private String statusCn; //订单状态中文名
	
	@Column(name="type")
	private String type; //交易类型   (比如一口价，万人团)
	
	@Column(name="created")
	private Date created; //交易创建时间(格式:yyyy-MM-dd HH:mm:ss)
	
	@Column(name="end_time")
	private Date end_time; //交易结束时间。交易成功时间(更新交易状态为成功的同时更新)/确认收货时间或者交易关闭时间 。(格式:yyyy-MM-dd HH:mm:ss)
	
	@Column(name="modified")
	private Date modified; //交易修改时间(用户对订单的任何修改都会更新此字段)。格式:yyyy-MM-dd HH:mm:ss
	
	@Column(name="pay_time")
	private Date payTime; //付款时间。订单的付款时间即为物流订单的创建时间。(格式:yyyy-MM-dd HH:mm:ss)
	
	@Column(name="count_oid")
	private int countOid;  //
	
	@Column(name="channel")
    private String channel;  //渠道编号
    
    @Column(name="channel_name_cn")
    private String channelNameCn; //渠道名称
    
	@Column(name="add_date")
	private Date addDate;//添加日期
	
	@Column(name="alter_date")
	private Date alterDate;//修改日期

	//------------------------------收货人基本信息------------------------------------//
	
	@Column(name="receiver_mobile")
	private String receiverMobile; //收货人电话号码(可能被模糊化 如:1898158****)
	
	@Column(name="receiver_name")
	private String receiverName; //收货人姓名(可能被模糊化 如:仲**)
	
	@Column(name="receiver_state")
	private String receiverState;  //客户省份
		
	@Column(name="receiver_district")
	private String receiverDistrict; //客户所在地区
	
	@Column(name="receiver_city")
	private String receiverCity; //客户所在城市
	
	@Column(name="receiver_address")
	private String receiverAddress; //客户地址
	
	@Column(name="receiver_zip")
	private String receiverZip; //客户邮编
	
	@Column(name="buyer_message")
	private String buyerMessage; //买家留言
	
	
	@Column(name="order_type")
	private int orderType;  //
	
	@Transient
    private String travelContactMail;  //联系人电子邮箱
    
	@Transient
    private String travelContactMobile;  //联系人电话
    
	@Transient
    private String travelContactName;  //联系人
	
	@Transient
	private Date tripStartDate;  //出行日期
	
	@Column(name="remark",length=5000)
	private String remark;//卖家备注
	
	/* 卖家备注旗帜:
	 * 与淘宝网上订单的卖家备注旗帜对应，只有卖家才能查看该字段
	 * 红、黄、绿、蓝、紫 分别对应 1、2、3、4、5
	 */
	@Column(name="seller_flag")
	private int sellerFlag;//卖家备注旗帜
	
	public String getIidd() {
		return iidd;
	}

	public void setIidd(String iidd) {
		this.iidd = iidd;
	}
	
    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getBuyerMessage() {
        return buyerMessage;
    }

    public void setBuyerMessage(String buyerMessage) {
        this.buyerMessage = buyerMessage;
    }

    public Date getTripStartDate() {
        return tripStartDate;
    }

    public void setTripStartDate(Date tripStartDate) {
        this.tripStartDate = tripStartDate;
    }

    public String getTravelContactMail() {
        return travelContactMail;
    }

    public void setTravelContactMail(String travelContactMail) {
        this.travelContactMail = travelContactMail;
    }

    public String getTravelContactMobile() {
        return travelContactMobile;
    }

    public void setTravelContactMobile(String travelContactMobile) {
        this.travelContactMobile = travelContactMobile;
    }

    public String getTravelContactName() {
        return travelContactName;
    }

    public void setTravelContactName(String travelContactName) {
        this.travelContactName = travelContactName;
    }

    public String getNumberCode() {
        return numberCode;
    }

    public void setNumberCode(String numberCode) {
        this.numberCode = numberCode;
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

    public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Date end_time) {
		this.end_time = end_time;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
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

	public String getReceiverMobile() {
		return receiverMobile;
	}

	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverState() {
		return receiverState;
	}

	public void setReceiverState(String receiverState) {
		this.receiverState = receiverState;
	}

	public String getReceiverDistrict() {
		return receiverDistrict;
	}

	public void setReceiverDistrict(String receiverDistrict) {
		this.receiverDistrict = receiverDistrict;
	}

	public String getReceiverCity() {
		return receiverCity;
	}

	public void setReceiverCity(String receiverCity) {
		this.receiverCity = receiverCity;
	}

	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	public String getReceiverZip() {
		return receiverZip;
	}

	public void setReceiverZip(String receiverZip) {
		this.receiverZip = receiverZip;
	}

	public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getSellerFlag() {
		return sellerFlag;
	}

	public void setSellerFlag(int sellerFlag) {
		this.sellerFlag = sellerFlag;
	}

	public int getCountOid() {
		return countOid;
	}

	public void setCountOid(int countOid) {
		this.countOid = countOid;
	}

    public String getSellerNick() {
        return sellerNick;
    }

    public void setSellerNick(String sellerNick) {
        this.sellerNick = sellerNick;
    }

    public String getBuyNick() {
        return buyNick;
    }

    public void setBuyNick(String buyNick) {
        this.buyNick = buyNick;
    }

    public BigDecimal getAvailableConfirmFee() {
        return availableConfirmFee;
    }

    public void setAvailableConfirmFee(BigDecimal availableConfirmFee) {
        this.availableConfirmFee = availableConfirmFee;
    }

    public BigDecimal getReceivedPayment() {
        return receivedPayment;
    }

    public void setReceivedPayment(BigDecimal receivedPayment) {
        this.receivedPayment = receivedPayment;
    }

    public String getStepTradeStatus() {
        return stepTradeStatus;
    }

    public void setStepTradeStatus(String stepTradeStatus) {
        this.stepTradeStatus = stepTradeStatus;
    }

    public String getStatusCn() {
        return statusCn;
    }

    public void setStatusCn(String statusCn) {
        this.statusCn = statusCn;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

	
}
