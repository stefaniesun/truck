package xyz.work.r_base.ctrl;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.r_base.svc.R_StockSvc;


@Controller
@RequestMapping(value = "/R_StockWS")
public class R_StockWS {
    @Autowired
    private R_StockSvc r_StockSvc;

    @RequestMapping(value="queryRStockList")
    @ResponseBody
    public Map<String, Object> queryRStockList(int page, int rows ,String tkview) {
        int pageSize = rows;
        int offset = (page-1)*pageSize;
        return r_StockSvc.queryRStockList(offset, pageSize, tkview);
    }

}