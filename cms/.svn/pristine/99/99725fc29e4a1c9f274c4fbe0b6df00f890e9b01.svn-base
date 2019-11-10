package xyz.work.taobao.model;

/**
 * 淘宝订单池类(子订单)
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
@Table(name="taobao_order_tbo")
public class TaobaoOrderTbo {
	@Id
	@Column(name="iidd",unique=true,nullable=false)
	@GeneratedValue(generator = "paymentableGenerator")       
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	private String iidd; //主键
		
	@Column(name="tid")
	private String tid; //淘宝子订单对应的主订单ID，不是唯一的
	
	@Column(name="oid",unique=true,nullable=false)
	private String oid; //子订单的唯一标识
	
	/* 购买数量 (取值范围:大于零的 整数 )
	 * 对于一个trade对应多个order的时候（一笔主订单，对应多笔子订单），
	 * num=0，num是一个跟商品关联的属性，一笔订单对应多笔子订单的时候，
	 * 主订单上的num无意义。
	 */
	@Column(name="num")
	private int num; //购买数量 (取值范围:大于零的 整数 )
	
	/* 子订单实付金额。(精确到2位小数，单位:元)
	 * 对于多子订单的交易，计算公式如下：
	 *  payment = price * num + adjust_fee(卖家手工调整金额) - discount_fee(优惠金额（免运费、限时打折时为空）) 。
	 * 单子订单交易，payment与主订单的payment一致，对于退款成功的子订单，由于主订单的优惠分摊金额，
	 * 会造成该字段可能不为0.00元。建议使用退款前的实付金额减去退款单中的实际退款金额计算。
	 * 
	 * 如:200.07，表示:200元7分。
	 */
	@Column(name="payment",precision=16,scale=2)
	private BigDecimal payment; //实付金额 (精确到2位小数; 单 位:元。)
	
	@Column(name="price",precision=16,scale=2)
	private BigDecimal price; //商品价格	
	
	/*订单状态.可选 值:
	 * WAIT_BUYER_PAY            等待买家付款,
	 * WAIT_BUYER_CONFIRM_GOODS  卖家已发 货,
	 * WAIT_SELLER_SEND_GOODS    买家已付款,
	 * TRADE_FINISHED            交易成功,
	 * TRADE_CLOSED              交易关 闭,
	 * TRADE_CLOSED_BY_TAOBAO    交易被淘宝关闭,
	 * TRADE_NO_CREATE_PAY       没有创建外部交易(支付宝交 易),
	 * OTHER                     其他状态
	 */
	@Column(name="status")
	private String status;//订单状态
		
	@Transient
	private String statusCn; //订单状态中文名
	
	@Transient
	private String stepTradeStatusCn; //万人团订单状态中文名
	
	@Column(name="total_fee",precision=16,scale=2)
	private BigDecimal totalFee; //商品金额（商品价格乘以数量的总金额）。精确到2位小数;单位:元。
	
	@Column(name="title")
	private String title; //商品标题
	
	@Column(name="num_iid")
	private String numIid; //商品数字ID
	
	@Column(name="sku_id")
	private String skuId; //商品的最小库存单位SKU的id(淘宝)

	@Column(name="out_sku_id")
	private String outSkuId;  //SKU的ID(系统)
	
	@Column(name="sku_properties_name")
	private String skuPropertiesName;//商品详细  (比如- 门票种类:成人票; 门票类型:实体票) SKU的值
	
    /* 退款状态,可选 值:
     * WAIT_SELLER_AGREE         买家已经申请退款,等待卖家同意,
     * WAIT_BUYER_RETURN_GOODS   卖家已经同意退款,等待 买家退货,
     * WAIT_SELLER_CONFIRM_GOODS 买家已经退货,等待卖家确认收货,
     * CLOSED                    退款关闭,
     * SUCCESS                   退 款成功,
     * SELLER_REFUSE_BUYER       卖家拒绝退款,
     * NO_REFUND                 没有退款
     */	
	@Column(name="refund_status")
	private String refundStatus;  //退款状态
	
	@Transient
	private String refundStatusCn; //退款状态中文名
	
	@Column(name="pic_path")
    private String picPath; //商品图片的绝对路径
	
	@Column(name="is_use")
	private int isUse;  //是否使用 (0:未使用   1: 使用)
	
	@Column(name="is_over")
	private int isOver;  //是否关闭 (0:未关闭 1:已关闭)
	
	@Column(name="is_ignore")
	private int isIgnore;  //是否忽略 (0:未忽略  1:普通忽略  2:内部处理)
	
	@Column(name="add_date")
	private Date addDate; //添加日期
	
	@Column(name="alter_date")
	private Date alterDate; //添加日期
	
	@Column(name="seller_nick")
	private String sellerNick;//卖家昵称 就是淘宝账号
	
	@Column(name="buy_nick")
	private String buyNick;  //买家昵称
	
	@Column(name="created")
	private Date created;  //交易创建时间(格式:yyyy-MM-dd HH:mm:ss)
	
	@Column(name="logistics_company")
	private String logisticsCompany;  //子订单发货的快递公司名称
	
	@Column(name="invoice_no")
	private String invoiceNo;  //子订单所在包裹的运单号
	
	@Column(name="is_oversold")
	private int isOversold; //是否超卖(0:否  1:是)
	
	@Column(name="adult_guest_num")
	private int adultGuestNum;  //出行成人人数
	
	@Column(name="child_guest_num")
	private int childGuestNum;  //出行儿童人数
	
	@Column(name="travel_contact_mail")
	private String travelContactMail;  //联系人电子邮箱
	
	@Column(name="travel_contact_mobile")
	private String travelContactMobile;  //联系人电话
	
	@Column(name="travel_contact_name")
	private String travelContactName;  //联系人
	
	@Column(name="trip_start_date")
	private Date tripStartDate;  //出行日期
	
	public int getAdultGuestNum() {
        return adultGuestNum;
    }

    public void setAdultGuestNum(int adultGuestNum) {
        this.adultGuestNum = adultGuestNum;
    }

    public int getChildGuestNum() {
        return childGuestNum;
    }

    public void setChildGuestNum(int childGuestNum) {
        this.childGuestNum = childGuestNum;
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

    public Date getTripStartDate() {
        return tripStartDate;
    }

    public void setTripStartDate(Date tripStartDate) {
        this.tripStartDate = tripStartDate;
    }

    public String getOutSkuId() {
        return outSkuId;
    }

    public void setOutSkuId(String outSkuId) {
        this.outSkuId = outSkuId;
    }

    public int getIsOversold() {
        return isOversold;
    }

    public void setIsOversold(int isOversold) {
        this.isOversold = isOversold;
    }

    public String getIidd() {
		return iidd;
	}

	public void setIidd(String iidd) {
		this.iidd = iidd;
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

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public BigDecimal getPayment() {
		return payment;
	}

	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getIsUse() {
		return isUse;
	}

	public void setIsUse(int isUse) {
		this.isUse = isUse;
	}

	public int getIsOver() {
		return isOver;
	}

	public void setIsOver(int isOver) {
		this.isOver = isOver;
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

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
	
	public int getIsIgnore() {
		return isIgnore;
	}

	public void setIsIgnore(int isIgnore) {
		this.isIgnore = isIgnore;
	}

    public String getStatusCn() {
        return statusCn;
    }

    public void setStatusCn(String statusCn) {
        this.statusCn = statusCn;
    }

    public String getStepTradeStatusCn() {
        return stepTradeStatusCn;
    }

    public void setStepTradeStatusCn(String stepTradeStatusCn) {
        this.stepTradeStatusCn = stepTradeStatusCn;
    }

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    public String getNumIid() {
        return numIid;
    }

    public void setNumIid(String numIid) {
        this.numIid = numIid;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getSkuPropertiesName() {
        return skuPropertiesName;
    }

    public void setSkuPropertiesName(String skuPropertiesName) {
        this.skuPropertiesName = skuPropertiesName;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getRefundStatusCn() {
        return refundStatusCn;
    }

    public void setRefundStatusCn(String refundStatusCn) {
        this.refundStatusCn = refundStatusCn;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
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

    public String getLogisticsCompany() {
        return logisticsCompany;
    }

    public void setLogisticsCompany(String logisticsCompany) {
        this.logisticsCompany = logisticsCompany;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

	
}
