package xyz.work.meituan.model;

import java.util.List;

public class SkuInfo {
    
    private String travelItem;   //用于关联 travelItem
    
    private String numberCode; //SKU编号
    
    private String packageName; //SKU名称
    
    private List<SkuStockInfo> stockList; //SKU库存集合

    public String getTravelItem() {
        return travelItem;
    }

    public void setTravelItem(String travelItem) {
        this.travelItem = travelItem;
    }

    public String getNumberCode() {
        return numberCode;
    }

    public void setNumberCode(String numberCode) {
        this.numberCode = numberCode;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<SkuStockInfo> getStockList() {
        return stockList;
    }

    public void setStockList(List<SkuStockInfo> stockList) {
        this.stockList = stockList;
    }
    
}