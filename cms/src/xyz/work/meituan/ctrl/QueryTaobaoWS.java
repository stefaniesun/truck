package xyz.work.meituan.ctrl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.meituan.svc.QueryTaobaoSvc;

@Controller
@RequestMapping(value = "/QueryTaobaoWS")
public class QueryTaobaoWS {
    
    @Autowired
    private QueryTaobaoSvc queryTaobaoSvc;
    
    @RequestMapping(value="queryTaobaoBaseInfoList")
    @ResponseBody
    public Map<String, Object> queryTaobaoBaseInfoList(){
        return queryTaobaoSvc.queryTaobaoBaseInfoList();
    }
    
    @RequestMapping(value="queryTaobaoSkuList")
    @ResponseBody
    public Map<String, Object> queryTaobaoSkuList(String travelItem){
        return queryTaobaoSvc.queryTaobaoSkuList(travelItem);
    }
   
    @RequestMapping(value="queryTaobaoSkuStockList")
    @ResponseBody
    public Map<String, Object> queryTaobaoSkuStockList(String skuInfo){
        return queryTaobaoSvc.queryTaobaoSkuStockList(skuInfo);
    }
    
    @RequestMapping(value="editTaobaoSkuStock")
    @ResponseBody
    public Map<String, Object> editTaobaoSkuStock(String skuInfo, String date, int num){
        return queryTaobaoSvc.editTaobaoSkuStock(skuInfo, date, num);
    }
    
    @RequestMapping(value="addProduct")
    @ResponseBody
    public Map<String, Object> addProduct(String channel , String travelItem) {
        return queryTaobaoSvc.addProduct(channel, travelItem);
    }
    
    @RequestMapping(value="pushProductOper")
    @ResponseBody
    public Map<String, Object> pushProductOper(String channel , String baseInfo, String skuJson){
        return queryTaobaoSvc.pushProductOper(channel, baseInfo, skuJson);
    }
    
}