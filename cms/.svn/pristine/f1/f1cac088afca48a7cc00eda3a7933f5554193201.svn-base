package xyz.work.taobao.ctrl;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.taobao.svc.TaobaoPoolSvc;


@Controller
@RequestMapping(value = "/TaobaoPoolWS")
public class TaobaoPoolWS {

    @Autowired
    TaobaoPoolSvc taobaoPoolSvc;
    
    @RequestMapping(value = "queryTaobaoPool")
    @ResponseBody
    public Map<String, Object> queryTaobaoPool(int page , int rows,String tid,String nickName,String sort,
        String order){
        
        int pageSize = rows;
        int offset = (page - 1) * pageSize;
        
        return taobaoPoolSvc.queryTaobaoPool(offset, pageSize,tid,nickName,sort,order);
    }
    
    @RequestMapping(value = "queryTaobaoOrderDetail")
    @ResponseBody
    public Map<String, Object> queryTaobaoOrderDetail(String tid) {
        return taobaoPoolSvc.queryTaobaoOrderDetail(tid);
    }
    
    @RequestMapping(value = "addOrderContentByTaobao")
    @ResponseBody
    public Map<String, Object> addOrderContentByTaobao(String tkviewJsonStr,
                                                BigDecimal payment,
                                                String linkMan,
                                                String linkPhone,
                                                String endDate,
                                                String remark,
                                                String tid){
        return taobaoPoolSvc.addOrderContentByTaobao(tkviewJsonStr, payment, linkMan, linkPhone, endDate, remark, tid);
        
    }
    
    @RequestMapping(value = "getStockByTkview")
    @ResponseBody
    public Map<String, Object> getStockByTkview(String tkview) {
        return taobaoPoolSvc.getStockByTkview(tkview);
    }
    
}
