package xyz.util;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import xyz.dao.CommonDao;
import xyz.work.base.model.Shipment;
import xyz.work.resources.model.Provider;
import xyz.work.resources.model.Stock;
import xyz.work.resources.model.Tkview;
import xyz.work.sell.model.Ptview;

@Component
public class ProductUtil {

	@Resource
	CommonDao commonDao;
	
	public Tkview getTkviewByNumberCode(String numberCode) {
		Tkview productObject = (Tkview)commonDao.getObjectByUniqueCode("Tkview", "numberCode", numberCode);
		if(productObject == null){
		    return null;
		}
		return productObject;
	}
	
   public Ptview getPtviewByNumberCode(String numberCode) {
       Ptview productObject = (Ptview)commonDao.getObjectByUniqueCode("Ptview", "numberCode", numberCode);
        if(productObject == null){
            return null;
        }
        return productObject;
    }
	
	public Provider getProviderByTkview(String stock){
	    Stock obj = (Stock)commonDao.getObjectByUniqueCode("Stock", "numberCode", stock);
		Provider provider = (Provider) commonDao.getObjectByUniqueCode("Provider", "numberCode", obj.getProvider());
		return provider;
	}
	
	/**
	 * 扣减库存
	 * @author Xzavier Sun
	 * @param tkview
	 * @param count
	 * @param isPass 现询是否直接通过
	 * @return 0:库存不足, 1:无超卖, 2:有超卖 
	 */
	public Map<Integer, String> substractStock(String tkview ,int count){
	    
	    Map<Integer, String> resultMap = new HashMap<Integer, String>(); 
	    
	    Tkview tkviewObj = (Tkview)commonDao.getObjectByUniqueCode("Tkview", "numberCode", tkview);
	    
	    Shipment shipment = (Shipment)commonDao.getObjectByUniqueCode(Constant.shipment, Constant.numberCode, tkviewObj.getShipment());
        if (shipment == null) {
            resultMap.put(0, "");
        }
	    
        Calendar c_shipment = Calendar.getInstance();
	    Calendar c_finalSale = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        c_shipment.setTime(shipment.getTravelDate());
        c_finalSale.setTime(shipment.getFinalSaleDate());
        calendar.setTime(new Date());
        
        if (c_finalSale.compareTo(calendar) == -1) {
            resultMap.put(0, "");
            return resultMap;
        }
        if (c_shipment.compareTo(calendar) == -1) {
            resultMap.put(0, "");
            return resultMap;
        }
	    
        String stockHql = "From Stock where tkview = '"+tkviewObj.getNumberCode()+"'"
                        + " and IFNULL(overDate, NOW()) >= NOW()"
                        + " order by priority";
	    
        @SuppressWarnings("unchecked")
        List<Stock> stockList = commonDao.queryByHql(stockHql);
        
        for (Stock stock : stockList) {
            
            int remainStock = stock.getStock().intValue() - stock.getUseStock().intValue();
            int remainSurpassStock = stock.getSurpass().intValue() - stock.getUseSurpass().intValue();
            if (remainStock + remainSurpassStock < count) {
                continue;
            }
            
            int subtractStock = count>remainStock?remainStock:count;
            int subtractSurpassStock = count>remainStock?count-remainStock:0;
            
            String substractStockSql = "UPDATE stock SET use_stock = use_stock + "+subtractStock+", use_surpass = use_surpass + "+subtractSurpassStock+" WHERE number_code = '"+stock.getNumberCode()+"' AND (stock - use_stock) + (surpass - use_surpass) > "+count+" AND IFNULL(over_date, NOW()) >= NOW()";
            int result = commonDao.getSqlQuery(substractStockSql).executeUpdate();
            if(result > 0){
                commonDao.flush();
                if(subtractSurpassStock == 0){
                    resultMap.put(1, stock.getNumberCode());
                    return resultMap;
                }
                resultMap.put(2, stock.getNumberCode());
                return resultMap;
            }
        }
        resultMap.put(0, "");
	    return resultMap;
	}
	
	/**
	 * 回退库存
	 * @author Xzavier Sun
	 * @param stockNumberCode
	 * @param count
	 * @return 回退库存是否成功
	 */
	public boolean refundStock(String stockNumberCode ,int count){
        
	    Stock stock = (Stock)commonDao.getObjectByUniqueCode("Stock", Constant.numberCode, stockNumberCode);
	    if(stock == null){
	        return false;
	    }
	    int refoundSurpass = stock.getSurpass().intValue() - stock.getUseSurpass().intValue();
	    refoundSurpass = refoundSurpass>count?count:refoundSurpass;
	    int refoundStock = count - refoundSurpass;
	    
	    String sql = "UPDATE stock SET use_stock = use_stock - "+refoundStock+", use_surpass = use_surpass - "+refoundSurpass+" WHERE number_code = '"+stockNumberCode+"'";
	    commonDao.getSqlQuery(sql).executeUpdate();
	    
        return true;
    }
	
	public Map<Integer, String> substractInquiryStock(String tkview,int count){
	    
	    Map<Integer, String> resultMap = new HashMap<Integer, String>();
	    Tkview tkviewObj = (Tkview)commonDao.getObjectByUniqueCode("Tkview", "numberCode", tkview);
        
        String stockHql = "From Stock where tkview = '"+tkviewObj.getNumberCode()+"'"
                        + " and type = 0"
                        + " and IFNULL(overDate, NOW()) >= NOW()"
                        + " order by priority DESC";
        
        @SuppressWarnings("unchecked")
        List<Stock> stockList = commonDao.queryByHql(stockHql);
        
        for (Stock stock : stockList) {
            
            String substractStockSql = "UPDATE stock SET use_stock = use_stock + "+count+" WHERE number_code = '"+stock.getNumberCode()+"' AND type = 0 AND IFNULL(over_date, NOW()) >= NOW()";
            int result = commonDao.getSqlQuery(substractStockSql).executeUpdate();
            if(result > 0){
                commonDao.flush();
                resultMap.put(1, stock.getNumberCode());
                return resultMap;
            }
        }
        resultMap.put(0, "");
        return resultMap;
	}
	
}
