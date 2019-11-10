package xyz.work.menu.svc;


import java.math.BigDecimal;
import java.util.Map;

import org.springframework.stereotype.Service;


/**
 * 资源查询
 * 
 * @author 熊玲
 */
@Service
public interface QueryResourcesSvc {

    public Map<String, Object> queryTkviewStockList(BigDecimal minPrice , BigDecimal maxPrice ,
                                                    String startDate , String endDate ,
                                                    String startPlace , String toPlace ,
                                                    String cruise , String airway , String cabin ,
                                                    BigDecimal days , BigDecimal nights , String provider);
    
    public Map<String, Object> clickDateOper(String tkviewJson , String stockJson , String date);

	public Map<String, Object> getTkviewStockList();

	public Map<String, Object> getExcel();

	public Map<String, Object> getValidateData();

	public Map<String, Object> updateShipmentDataOper();

	public Map<String, Object> queryShipmentData();

	public Map<String, Object> getQueryParamData();

}