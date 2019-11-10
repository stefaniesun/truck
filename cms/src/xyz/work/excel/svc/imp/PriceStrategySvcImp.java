package xyz.work.excel.svc.imp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.ReturnUtil;
import xyz.util.ConstantMsg;
import xyz.util.StringTool;
import xyz.util.UUIDUtil;
import xyz.work.excel.model.PriceStrategy;
import xyz.work.excel.svc.PriceStrategySvc;

@Service
public class PriceStrategySvcImp implements PriceStrategySvc {
    @Autowired
    CommonDao commonDao;

    @Override
    public Map<String, Object> queryPriceStrategyList(int offset , int pageSize) {
        String hql = "from PriceStrategy p";
        Query countQuery = commonDao.getQuery("select count(*) " + hql);
        Number countNum = (Number)countQuery.uniqueResult();
        int count = countNum == null ? 0 : countNum.intValue();

        Query query = commonDao.getQuery(hql);
        query.setMaxResults(pageSize);
        query.setFirstResult(offset);
        @SuppressWarnings("unchecked")
        List<PriceStrategy> list = query.list();
        
        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", count);
        mapContent.put("rows", list);

        return ReturnUtil.returnMap(1, mapContent);
    }

    @Override
    public Map<String, Object> addPriceStrategy(BigDecimal minPrice , BigDecimal maxPrice ,
                                                BigDecimal priceMarkup , String remark) {
        if(StringTool.isEmpty(minPrice.toString())){
            return ReturnUtil.returnMap(0, "区间最小值不能为空!");
        }
        if(StringTool.isEmpty(maxPrice.toString())){
            return ReturnUtil.returnMap(0, "区间最大值不能为空!");
        }
        if(StringTool.isEmpty(priceMarkup.toString())){
            return ReturnUtil.returnMap(0, "加价值不能为空!");
        }
        if(minPrice.compareTo(maxPrice) == 1){
            return ReturnUtil.returnMap(0, "区间最小值不能大于区间最大值!");
        }
        
        String minPriceSql = "SELECT * FROM price_strategy ps WHERE ";
        minPriceSql += "ps.min_price <= "+ minPrice +" ";
        minPriceSql += "AND ps.max_price >= "+ minPrice +" ";
        @SuppressWarnings("unchecked")
        List<PriceStrategy> minPriceList = commonDao.getSqlQuery(minPriceSql).list();
        if(minPriceList.size() > 0){
            return ReturnUtil.returnMap(0, "区间最小值"+minPrice+"已经存在!");
        }
        
        String maxPriceSql = "SELECT * FROM price_strategy ps WHERE ";
        maxPriceSql += "ps.min_price <= "+ maxPrice +" ";
        maxPriceSql += "AND ps.max_price >= "+ maxPrice +" ";
        @SuppressWarnings("unchecked")
        List<PriceStrategy> maxPriceList = commonDao.getSqlQuery(maxPriceSql).list();
        if(maxPriceList.size() > 0){
            return ReturnUtil.returnMap(0, "区间最大值"+maxPrice+"已经存在!");
        }

        Date addDate = new Date();
        PriceStrategy priceObj = new PriceStrategy();
        priceObj.setNumberCode(UUIDUtil.getUUIDStringFor32());
        priceObj.setMinPrice(minPrice);
        priceObj.setMaxPrice(maxPrice);
        priceObj.setPriceMarkup(priceMarkup);
        priceObj.setRemark(remark);
        priceObj.setAddDate(addDate);
        priceObj.setAlterDate(addDate);
        commonDao.save(priceObj);
        
        return ReturnUtil.returnMap(1,null);
    }

    @Override
    public Map<String, Object> editPriceStrategy(String numberCode , BigDecimal minPrice ,
                                                 BigDecimal maxPrice , BigDecimal priceMarkup ,
                                                 String remark) {
        if(StringTool.isEmpty(numberCode)){
            return ReturnUtil.returnMap(0, "请选择要编辑的对象!");
        }
        if(StringTool.isEmpty(minPrice.toString())){
            return ReturnUtil.returnMap(0, "区间最小值不能为空!");
        }
        if(StringTool.isEmpty(maxPrice.toString())){
            return ReturnUtil.returnMap(0, "区间最大值不能为空!");
        }
        if(StringTool.isEmpty(priceMarkup.toString())){
            return ReturnUtil.returnMap(0, "加价值不能为空!");
        }
        if(minPrice.compareTo(maxPrice) == 1){
            return ReturnUtil.returnMap(0, "区间最小值不能大于区间最大值!");
        }
        
        String minPriceSql = "SELECT * FROM price_strategy ps WHERE ";
        minPriceSql += "ps.number_code <> '"+ numberCode +"'";
        minPriceSql += "AND ( ps.min_price = '"+ minPrice +"' ";
        minPriceSql += "OR ps.max_price = '"+ minPrice +"') ";
        @SuppressWarnings("unchecked")
        List<PriceStrategy> minPriceList = commonDao.getSqlQuery(minPriceSql).list();
        if(minPriceList.size() > 0){
            return ReturnUtil.returnMap(0, "区间最小值"+minPrice+"已经存在!");
        }
        
        String maxPriceSql = "SELECT * FROM price_strategy ps WHERE ";
        maxPriceSql += "ps.number_code <> '"+ numberCode +"' ";
        maxPriceSql += "AND (ps.min_price = '"+ maxPrice +"' ";
        maxPriceSql += "OR ps.max_price = '"+ maxPrice +"') ";
        @SuppressWarnings("unchecked")
        List<PriceStrategy> maxPriceList = commonDao.getSqlQuery(maxPriceSql).list();
        if(maxPriceList.size() > 0){
            return ReturnUtil.returnMap(0, "区间最大值"+maxPrice+"已经存在!");
        }
        
        PriceStrategy priceObj = (PriceStrategy)commonDao.getObjectByUniqueCode("PriceStrategy", "numberCode", numberCode);
        priceObj.setMinPrice(minPrice);
        priceObj.setMaxPrice(maxPrice);
        priceObj.setPriceMarkup(priceMarkup);
        priceObj.setRemark(remark);
        priceObj.setAlterDate(new Date());
        commonDao.update(priceObj);
        
        return ReturnUtil.returnMap(1,null);
    }

    @Override
    public Map<String, Object> deletePriceStrategy(String numberCodes) {
        if (StringTool.isEmpty(numberCodes)) {
            return ReturnUtil.returnMap(0, ConstantMsg.delete_null);
        }
        
        String sql = "delete from price_strategy where number_code in ("
                     + StringTool.StrToSqlString(numberCodes) + ")";
        commonDao.getSqlQuery(sql).executeUpdate();

        return ReturnUtil.returnMap(1, null);
    }

}