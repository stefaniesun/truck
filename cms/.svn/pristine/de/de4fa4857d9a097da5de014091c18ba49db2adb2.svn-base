
package xyz.work.sell.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import xyz.filter.JSON;
import xyz.filter.MyRequestUtil;
import xyz.util.DateUtil;

@Entity
@Table(name="order_content")
public class OrderContent {
	@Id
	@Column(name="iidd",unique=true,nullable=false)
	@GeneratedValue(generator = "paymentableGenerator")       
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	private String iidd;// 
	
	@Column(name="order_num")
	private String orderNum;
	
	@Column(name="client_code")
	private String clientCode;
	
	@Column(name="tid")
	private String tid;  //渠道订单号
	
	@Column(name="oid")
	private String oid;  //渠道票单号
	
	@Column(name="flag")
	private int flag;  //旗子 用户自定义表示含义
	
	@Column(name="product")
	private String product;//商品, 内部单是ptview, TB单为 宝贝
	
	@Column(name="product_name_cn")
	private String productNameCn;
	
	@Column(name="product_price")
	private int productPrice;

    @Column(name="tkview")
	private String tkview;

    @Column(name="tkview_name_cn")
    private String tkviewNameCn;
    
    @Column(name="stock")
    private String stock;//使用库存
    
    @Column(name="count")
    private int count;
    
    @Column(name="order_price")
    private int orderPrice;//订单金额 单位:分
    
    @Column(name="client_price")
    private int clientPrice;//票单金额 单位:分
    
    @Column(name="is_pay")
    private int isPay;  //是否付款
    
    @Column(name="pay_date")
    private Date payDate;
    
    @Column(name="pay_amount")
    private BigDecimal payAmount;//付款金额 单位:分
    
    @Column(name="is_refound_money")
    private int isRefoundMoney;  //是否退款
    
    @Column(name="refound_amount")
    private int refoundAmount;//退款金额 单位:分
    
    @Column(name="refound_money_date")
    private Date refoundMoneyDate;
    
    @Column(name="is_refound_stock")
    private int isRefoundStock;  //是否退库存
    
    @Column(name="refound_stock")
    private int refoundStock;//回退库存数量
    
    @Column(name="refound_stock_date")
    private Date refoundStockDate;
    
    @Column(name="cost")
    private int cost;//成本 单位:分
    
    @Column(name="earnest")
    private int earnest;//定金 单位:分
    
    @Column(name="is_sort")
    private int isSort;   //是否排团
    
    @Column(name="is_over")
    private int isOver;  //订单是否结束  (关闭之后将无法更改)
    
    @Column(name="is_delete")
    private int isDelete;
    
    @Column(name="over_date")
    private Date overDate; //订单结束时间
    
    @Column(name="is_send")
    private int isSend;//是否发货(对应是否扣减库存!)
    
    @Column(name="send_date")
    private Date sendDate; //订单结束时间
    
    @Column(name="is_surpass")
    private int isSurpass;//是否超卖
    
    @Column(name="cruise")
    private String cruise;
    
    @Column(name="cruise_nme_cn")
    private String cruiseNameCn;
    
    @Column(name="shipment")
    private String shipment;
    
    @Column(name="shipment_mark")
    private String shipmentMark;
    
    @Column(name="shipment_travel_date")
    private Date shipmentTravelDate;
    
    @Column(name="cabin")
    private String cabin;
    
    @Column(name="cabin_name_cn")
    private String cabinNameCn;
    
    @Column(name="volume")
    private BigDecimal volume;//容积
    
    @Column(name="provider")
    private String provider;
    
    @Column(name="provider_name_cn")
    private String providerNameCn;
    
    @Column(name="channel")
    private String channel;
    
    @Column(name="channel_name_cn")
    private String channelNameCn;
    
    @Column(name="source")
    private String source;//来源 (cms, pc, tb, ...)
    
    @Column(name="seize")
    private int seize;//0.预定/1.预留/
    
    @Column(name="person_info")
    private String personInfo;//出行人
    
    @Column(name="buyer")
    private String buyer;
    
    @Column(name="link_man")
    private String linkMan;
    
    @Column(name="link_phone")
    private String linkPhone;
    
    @Column(name="address")
    private String address;
    
    @Column(name="email")
    private String email;
    
    @Column(name="operater_add")
    private String operaterAdd;//录单人
    
    @Column(name="operater_alter")
    private String operaterAlter;//修改人
    
    @Column(name="is_hang")
    private int isHang;
    
    @Column(name="remark",length=10000)
    private String remark;//订单备注
    
    @Column(name="add_date")
    private Date addDate;
    
    @Column(name="alter_date")
    private Date alterDate;

    public String getIidd() {
        return iidd;
    }

    public void setIidd(String iidd) {
        this.iidd = iidd;
    }
    
    public int getIsHang() {
        return isHang;
    }

    public void setIsHang(int isHang) {
        this.isHang = isHang;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public int getSeize() {
        return seize;
    }

    public void setSeize(int seize) {
        this.seize = seize;
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

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getProductNameCn() {
        return productNameCn;
    }

    public void setProductNameCn(String productNameCn) {
        this.productNameCn = productNameCn;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
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

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(int orderPrice) {
        this.orderPrice = orderPrice;
    }

    public int getClientPrice() {
        return clientPrice;
    }

    public void setClientPrice(int clientPrice) {
        this.clientPrice = clientPrice;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public int getRefoundAmount() {
        return refoundAmount;
    }

    public void setRefoundAmount(int refoundAmount) {
        this.refoundAmount = refoundAmount;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getEarnest() {
        return earnest;
    }

    public void setEarnest(int earnest) {
        this.earnest = earnest;
    }

    public int getRefoundStock() {
        return refoundStock;
    }

    public void setRefoundStock(int refoundStock) {
        this.refoundStock = refoundStock;
    }

    public int getIsOver() {
        return isOver;
    }

    public void setIsOver(int isOver) {
        this.isOver = isOver;
    }

    public int getIsSend() {
        return isSend;
    }

    public void setIsSend(int isSend) {
        this.isSend = isSend;
    }

    public int getIsPay() {
        return isPay;
    }

    public void setIsPay(int isPay) {
        this.isPay = isPay;
    }

    public int getIsRefoundMoney() {
        return isRefoundMoney;
    }

    public void setIsRefoundMoney(int isRefoundMoney) {
        this.isRefoundMoney = isRefoundMoney;
    }

    public int getIsRefoundStock() {
        return isRefoundStock;
    }

    public void setIsRefoundStock(int isRefoundStock) {
        this.isRefoundStock = isRefoundStock;
    }

    public int getIsSort() {
        return isSort;
    }

    public void setIsSort(int isSort) {
        this.isSort = isSort;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public Date getRefoundMoneyDate() {
        return refoundMoneyDate;
    }

    public void setRefoundMoneyDate(Date refoundMoneyDate) {
        this.refoundMoneyDate = refoundMoneyDate;
    }

    public Date getRefoundStockDate() {
        return refoundStockDate;
    }

    public void setRefoundStockDate(Date refoundStockDate) {
        this.refoundStockDate = refoundStockDate;
    }

    public Date getOverDate() {
        return overDate;
    }

    public void setOverDate(Date overDate) {
        this.overDate = overDate;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public int getIsSurpass() {
        return isSurpass;
    }

    public void setIsSurpass(int isSurpass) {
        this.isSurpass = isSurpass;
    }

    public String getCruise() {
        return cruise;
    }

    public void setCruise(String cruise) {
        this.cruise = cruise;
    }

    public String getCruiseNameCn() {
        return cruiseNameCn;
    }

    public void setCruiseNameCn(String cruiseNameCn) {
        this.cruiseNameCn = cruiseNameCn;
    }

    public String getShipment() {
        return shipment;
    }

    public void setShipment(String shipment) {
        this.shipment = shipment;
    }

    public String getShipmentMark() {
        return shipmentMark;
    }

    public void setShipmentMark(String shipmentMark) {
        this.shipmentMark = shipmentMark;
    }

    public Date getShipmentTravelDate() {
        return shipmentTravelDate;
    }

    public void setShipmentTravelDate(Date shipmentTravelDate) {
        this.shipmentTravelDate = shipmentTravelDate;
    }

    public String getCabin() {
        return cabin;
    }

    public void setCabin(String cabin) {
        this.cabin = cabin;
    }

    public String getCabinNameCn() {
        return cabinNameCn;
    }

    public void setCabinNameCn(String cabinNameCn) {
        this.cabinNameCn = cabinNameCn;
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

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getProviderNameCn() {
        return providerNameCn;
    }

    public void setProviderNameCn(String providerNameCn) {
        this.providerNameCn = providerNameCn;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPersonInfo() {
        return personInfo;
    }

    public void setPersonInfo(String personInfo) {
        this.personInfo = personInfo;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOperaterAdd() {
        return operaterAdd;
    }

    public void setOperaterAdd(String operaterAdd) {
        this.operaterAdd = operaterAdd;
    }

    public String getOperaterAlter() {
        return operaterAlter;
    }

    public void setOperaterAlter(String operaterAlter) {
        this.operaterAlter = operaterAlter;
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
    
    public void addRemark(String remark) {//TODO订单添加备注
        String username = MyRequestUtil.getSecurityLogin()==null?"system":MyRequestUtil.getSecurityLogin().getUsername();
        
        String oldRemark = this.getRemark();
        if(oldRemark == null || "".equals(oldRemark)){
            oldRemark = "[]";
        }
        @SuppressWarnings("unchecked")
        List<Map<String ,Object>> remarkList = JSON.toObject(oldRemark, ArrayList.class);
        Map<String ,Object> remarkMap = new HashMap<String, Object>();
        remarkMap.put("alterDate", DateUtil.dateToString(new Date()));
        remarkMap.put("remark", remark);
        remarkMap.put("alterOperator", username);
        remarkList.add(remarkMap);
        
        this.remark = JSON.toJson(remarkList);
    }
}
