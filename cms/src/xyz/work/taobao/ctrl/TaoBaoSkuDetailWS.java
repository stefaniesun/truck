package xyz.work.taobao.ctrl;


import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.taobao.svc.TaoBaoSkuDetailSvc;


@Controller
@RequestMapping(value = "/TaoBaoSkuDetailWS")
public class TaoBaoSkuDetailWS {
    @Autowired
    TaoBaoSkuDetailSvc detailSvc;

    @RequestMapping(value = "addSkuDatilList")
    @ResponseBody
    public Map<String, Object> addSkuDatilList(String baseInfo , String skuInfo , String dateJson ,
                                               BigDecimal price , int priceType , int isLock ,
                                               int isEdit , String stock) {
        return detailSvc.addSkuDatilList(baseInfo, skuInfo, dateJson, price, priceType, isLock,
            isEdit, stock);
    }

    @RequestMapping(value = "editIsLock")
    @ResponseBody
    public Map<String, Object> editIsLock(String numberCode , int isLock , String cruise) {
        return detailSvc.editIsLock(numberCode, isLock , cruise);
    }
}