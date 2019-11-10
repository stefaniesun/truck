package xyz.work.meituan.model;

import java.math.BigDecimal;
import java.util.Date;

public class SkuStockInfo {
    private String skuInfo; //SKU编号
    
    private String numberCode; //SKU库存编号
    
    private Date date; //日期
    
    private int stock; //库存
    
    private BigDecimal price; //价格
    
    private int priceType; //价格类型

    public String getSkuInfo() {
        return skuInfo;
    }

    public void setSkuInfo(String skuInfo) {
        this.skuInfo = skuInfo;
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

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getPriceType() {
        return priceType;
    }

    public void setPriceType(int priceType) {
        this.priceType = priceType;
    }
    
}