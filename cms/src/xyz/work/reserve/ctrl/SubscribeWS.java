package xyz.work.reserve.ctrl;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.reserve.svc.SubscribeSvc;


@Controller
@RequestMapping(value = "/SubscribeWS")
public class SubscribeWS {
    @Autowired
    private SubscribeSvc subscribeSvc;

    @RequestMapping(value="queryReserveList")
    @ResponseBody
    public Map<String, Object> queryReserveList(int page , int rows , String tkviewNameCn ,
                                                String linkMan , int flag) {
        int pageSize = rows;
        int offset = (page - 1) * pageSize;
        return subscribeSvc.queryReserveList(offset, pageSize, tkviewNameCn, linkMan, flag);
    }

    @RequestMapping(value="editReserveFlagByNumberCode")
    @ResponseBody
    public Map<String, Object> editReserveFlagByNumberCode(String numberCode , int flag , String flagRemark ,
                                           String remark) {
        return subscribeSvc.editReserveFlagByNumberCode(numberCode, flag, flagRemark, remark);
    }
    
    @RequestMapping(value="getPersonListByReserve")
    @ResponseBody
    public Map<String, Object> getPersonListByReserve(String reserve) {
        return subscribeSvc.getPersonListByReserve(reserve);
    }
}