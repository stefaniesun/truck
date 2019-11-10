
package xyz.work.sell.model;

import java.util.Date;

public class R_OrderContent {
	
	private String orderNum;
	
	private String tid;
	
    private Number countClient;
    
    private String clientCode;
    
    private Number countSend;
    
    private Number countRefound;
    
    private Number isPay;
    
    private Number isOver;
    
    private Number isDelete;
    
    private Number flagRemark;
    
    private Date overDate;
	
	private String product;//商品, 内部单是ptview, TB单为 宝贝
	
	private String tkview;
    
    private String stock;//使用库存
    
    private Number flag;//用户自定义状态
    
    private Number orderPrice;
    
    private Number payAmount;
    
    private Number refoundAmount;
    
    private String cruiseNameCn;
    
    private String shipment;
    
    private String shipmentMark;
    
    private Date shipmentTravelDate;
    
    private String cabinNameCn;
    
    private String channelNameCn;
    
    private String productNameCn;
    
    private String tkviewNameCn;
    
    private String providerNameCn;
    
    private String source;//来源 (cms, pc, tb, ...)
    
    private String buyer;
    
    private String linkMan;
    
    private String linkPhone;
    
    private String address;
    
    private String email;
    
    private int isHang;
    
    private String remark;
    
    private String operaterAdd;//录单人
    
    private String operaterAlter;//修改人
    
    private Date addDate;
    
    private Date alterDate;
    
    public int getIsHang() {
        return isHang;
    }

    public void setIsHang(int isHang) {
        this.isHang = isHang;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public Number getCountClient() {
        return countClient;
    }

    public void setCountClient(Number countClient) {
        this.countClient = countClient;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public Number getCountSend() {
        return countSend;
    }

    public void setCountSend(Number countSend) {
        this.countSend = countSend;
    }

    public Number getCountRefound() {
        return countRefound;
    }

    public void setCountRefound(Number countRefound) {
        this.countRefound = countRefound;
    }

    public Number getIsPay() {
        return isPay;
    }

    public void setIsPay(Number isPay) {
        this.isPay = isPay;
    }

    public Number getIsOver() {
        return isOver;
    }

    public void setIsOver(Number isOver) {
        this.isOver = isOver;
    }

    public Number getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Number isDelete) {
        this.isDelete = isDelete;
    }

    public Number getFlagRemark() {
        return flagRemark;
    }

    public void setFlagRemark(Number flagRemark) {
        this.flagRemark = flagRemark;
    }

    public Date getOverDate() {
        return overDate;
    }

    public void setOverDate(Date overDate) {
        this.overDate = overDate;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getTkview() {
        return tkview;
    }

    public void setTkview(String tkview) {
        this.tkview = tkview;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public Number getFlag() {
        return flag;
    }

    public void setFlag(Number flag) {
        this.flag = flag;
    }

    public Number getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(Number orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Number getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Number payAmount) {
        this.payAmount = payAmount;
    }

    public Number getRefoundAmount() {
        return refoundAmount;
    }

    public void setRefoundAmount(Number refoundAmount) {
        this.refoundAmount = refoundAmount;
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

    public String getCabinNameCn() {
        return cabinNameCn;
    }

    public void setCabinNameCn(String cabinNameCn) {
        this.cabinNameCn = cabinNameCn;
    }

    public String getChannelNameCn() {
        return channelNameCn;
    }

    public void setChannelNameCn(String channelNameCn) {
        this.channelNameCn = channelNameCn;
    }

    public String getProductNameCn() {
        return productNameCn;
    }

    public void setProductNameCn(String productNameCn) {
        this.productNameCn = productNameCn;
    }

    public String getTkviewNameCn() {
        return tkviewNameCn;
    }

    public void setTkviewNameCn(String tkviewNameCn) {
        this.tkviewNameCn = tkviewNameCn;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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