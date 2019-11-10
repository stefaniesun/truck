package xyz.work.sell.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name="ptview_sku_stock",uniqueConstraints = {@UniqueConstraint(columnNames={"date","ptview_sku"})})
public class PtviewSkuStock {

    @Id
    @Column(name="iidd",unique=true,nullable=false)
    @GeneratedValue(generator = "paymentableGenerator")       
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    private String iidd;   //主键
    
    @Column(name="ptview_sku")
    private String ptviewSku;   //关联产品SKU套餐
    
    @Column(name="number_code",unique=true,nullable=false)
    private String numberCode;  //编号 

    @Column(name="date",nullable=false)
    private Date date;     //日期
    
    @Column(name="b_price",scale=2)
    private BigDecimal bPrice;           //分销价
    
    @Column(name="c_price",scale=2)
    private BigDecimal cPrice;           //散客价
    
    @Column(name="a_b_Price",scale=2)
    private BigDecimal aBPrice;          //单人B价   averageBPrice
    
    @Column(name="a_c_price",scale=2)
    private BigDecimal aCPrice;          //单人C价   averageCPrice
    
    @Column(name="s_b_Price",scale=2)
    private BigDecimal sBPrice;          //自定义 1/2人B价  singleBPrice
    
    @Column(name="s_c_Price",scale=2)
    private BigDecimal sCPrice;          //自定义 1/2人C价  singleCPrice
    
    @Column(name="m_b_Price",scale=2)
    private BigDecimal mBPrice;          //自定义 3/4人B价  manyBPrice
    
    @Column(name="m_c_price",scale=2)
    private BigDecimal mCPrice;          //自定义 3/4人C价  manyCPrice
    
    @Column(name="stock")
    private int stock; //库存数
    
    @Column(name="out_tkview")
    private String outTkview; //对应tkview
    
    @Column(name="out_stock")
    private String outStock;  //对应Stock
    
    @Column(name="add_date")
    private Date addDate;    //新增时间
    
    @Column(name="alter_date")
    private Date alterDate;  //编辑时间
    
    @Transient
    private String tkviewNameCn; //单品名称
    
    @Transient
    private int tkviewCount;     //关联单品个数

    public String getIidd() {
        return iidd;
    }

    public void setIidd(String iidd) {
        this.iidd = iidd;
    }

    public String getPtviewSku() {
        return ptviewSku;
    }

    public void setPtviewSku(String ptviewSku) {
        this.ptviewSku = ptviewSku;
    }

    public String getNumberCode() {
        return numberCode;
    }

    public void setNumberCode(String numberCode) {
        this.numberCode = numberCode;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getbPrice() {
        return bPrice;
    }

    public void setbPrice(BigDecimal bPrice) {
        this.bPrice = bPrice;
    }

    public BigDecimal getcPrice() {
        return cPrice;
    }

    public void setcPrice(BigDecimal cPrice) {
        this.cPrice = cPrice;
    }

    public BigDecimal getaBPrice() {
        return aBPrice;
    }

    public void setaBPrice(BigDecimal aBPrice) {
        this.aBPrice = aBPrice;
    }

    public BigDecimal getaCPrice() {
        return aCPrice;
    }

    public void setaCPrice(BigDecimal aCPrice) {
        this.aCPrice = aCPrice;
    }

    public BigDecimal getsBPrice() {
        return sBPrice;
    }

    public void setsBPrice(BigDecimal sBPrice) {
        this.sBPrice = sBPrice;
    }

    public BigDecimal getsCPrice() {
        return sCPrice;
    }

    public void setsCPrice(BigDecimal sCPrice) {
        this.sCPrice = sCPrice;
    }

    public BigDecimal getmBPrice() {
        return mBPrice;
    }

    public void setmBPrice(BigDecimal mBPrice) {
        this.mBPrice = mBPrice;
    }

    public BigDecimal getmCPrice() {
        return mCPrice;
    }

    public void setmCPrice(BigDecimal mCPrice) {
        this.mCPrice = mCPrice;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getOutTkview() {
        return outTkview;
    }

    public void setOutTkview(String outTkview) {
        this.outTkview = outTkview;
    }

    public String getOutStock() {
        return outStock;
    }

    public void setOutStock(String outStock) {
        this.outStock = outStock;
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

    public String getTkviewNameCn() {
        return tkviewNameCn;
    }

    public void setTkviewNameCn(String tkviewNameCn) {
        this.tkviewNameCn = tkviewNameCn;
    }

    public int getTkviewCount() {
        return tkviewCount;
    }

    public void setTkviewCount(int tkviewCount) {
        this.tkviewCount = tkviewCount;
    }
    
}