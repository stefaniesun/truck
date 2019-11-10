package xyz.work.r_base.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 加勒比--价格信息
 */
@Entity
@Table(name = "r_stock")
public class R_Stock {
    @Id
    @Column(name="iidd",unique=true,nullable=false)
    @GeneratedValue(generator = "paymentableGenerator")       
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    private String iidd;             //主键
    
    @Column(name="number_code",unique=true,nullable=false)
    private String numberCode;       //编号
    
    @Column(name="tkview")
    private String tkview;           //所属单品
    
    @Column(name="provider")
    private String provider;         //供应商，皇家加勒比
    
    @Column(name="provider_name_cn")
    private String providerNameCn;   //供应商名称
    
    @Column(name="price_mark")
    private String priceMark;        //价格代码

    @Column(name="price_desc")
    private String priceDesc;        //价格描述
    
    @Column(name="price_eff_date")
    private Date priceEffDate;      //价格生效日
    
    @Column(name="price_end_date")
    private Date priceEndDate;      //价格到期日
    
    @Column(name="single_room")
    private BigDecimal singleRoom;   //单人间均价
    
    @Column(name="double_room")
    private BigDecimal doubleRoom;   //双人间均价

    @Column(name="triple_room")
    private BigDecimal tripleRoom;   //三人间均价
    
    @Column(name="quad_room")
    private BigDecimal quadRoom;     //四人间均价

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

    public String getTkview() {
        return tkview;
    }

    public void setTkview(String tkview) {
        this.tkview = tkview;
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

    public String getPriceMark() {
        return priceMark;
    }

    public void setPriceMark(String priceMark) {
        this.priceMark = priceMark;
    }

    public String getPriceDesc() {
        return priceDesc;
    }

    public void setPriceDesc(String priceDesc) {
        this.priceDesc = priceDesc;
    }

    public Date getPriceEffDate() {
        return priceEffDate;
    }

    public void setPriceEffDate(Date priceEffDate) {
        this.priceEffDate = priceEffDate;
    }

    public Date getPriceEndDate() {
        return priceEndDate;
    }

    public void setPriceEndDate(Date priceEndDate) {
        this.priceEndDate = priceEndDate;
    }

    public BigDecimal getSingleRoom() {
        return singleRoom;
    }

    public void setSingleRoom(BigDecimal singleRoom) {
        this.singleRoom = singleRoom;
    }

    public BigDecimal getDoubleRoom() {
        return doubleRoom;
    }

    public void setDoubleRoom(BigDecimal doubleRoom) {
        this.doubleRoom = doubleRoom;
    }

    public BigDecimal getTripleRoom() {
        return tripleRoom;
    }

    public void setTripleRoom(BigDecimal tripleRoom) {
        this.tripleRoom = tripleRoom;
    }

    public BigDecimal getQuadRoom() {
        return quadRoom;
    }

    public void setQuadRoom(BigDecimal quadRoom) {
        this.quadRoom = quadRoom;
    }
    
}