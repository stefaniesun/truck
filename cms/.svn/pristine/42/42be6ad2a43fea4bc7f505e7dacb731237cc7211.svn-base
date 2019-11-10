package xyz.work.reserve.ctrl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.reserve.svc.ReserveSvc;

@Controller
@RequestMapping(value = "/ReserveWS")
public class ReserveWS {
    @Autowired
    private ReserveSvc reserveSvc;
    
    @RequestMapping(value="reserveLogin")
    @ResponseBody
    public Map<String, Object> reserveLogin(String tid , String phone) {
        return reserveSvc.reserveLogin(tid, phone);
    }
    
    @RequestMapping(value="queryOrderListByTid")
    @ResponseBody
    public Map<String, Object> queryOrderListByTid(String tid , String notPass) {
        return reserveSvc.queryOrderListByTid(tid , notPass);
    }
    
    @RequestMapping(value="savePersonData")
    @ResponseBody
    public Map<String, Object> savePersonData(String personJson) {
        return reserveSvc.savePersonData(personJson);
    }
    
    @RequestMapping(value="getPersonByNumberCode")
    @ResponseBody
    public Map<String, Object> getPersonByNumberCode(String json) {
        return reserveSvc.getPersonByNumberCode(json);
    }
    
}