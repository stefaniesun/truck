package xyz.work.excel.svc;


import java.math.BigDecimal;
import java.util.Map;

import org.springframework.stereotype.Service;


@Service
public interface PriceStrategySvc {

    public Map<String, Object> queryPriceStrategyList(int offset , int pageSize);

    public Map<String, Object> addPriceStrategy(BigDecimal minPrice , BigDecimal maxPrice ,
                                                BigDecimal priceMarkup , String remark);

    public Map<String, Object> editPriceStrategy(String numberCode , BigDecimal minPrice ,
                                                 BigDecimal maxPrice , BigDecimal priceMarkup ,
                                                 String remark);

    public Map<String, Object> deletePriceStrategy(String numberCodes);

}