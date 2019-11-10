package xyz.work.r_base.ctrl;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.r_base.svc.R_TkviewSvc;


@Controller
@RequestMapping(value = "/R_TkviewWS")
public class R_TkviewWS {
    @Autowired
    private R_TkviewSvc r_TkviewSvc;

    @RequestMapping(value = "readTxtOper")
    @ResponseBody
    public Map<String, Object> readTxtOper() {
        return r_TkviewSvc.readTxtOper();
    }

    @RequestMapping(value = "queryRTkviewList")
    @ResponseBody
    public Map<String, Object> queryRTkviewList(int page, int rows , String cruiseMark ,
                                                String cabinMark , String nameCn , String mark ,
                                                String mouth , String shipment) {
        int pageSize = rows;
        int offset = (page-1)*pageSize;
        return r_TkviewSvc.queryRTkviewList(offset, pageSize, cruiseMark, cabinMark, nameCn, mark, mouth, shipment);
    }

}