package xyz.work.r_base.ctrl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.r_base.svc.RTkview_ViewSvc;

@Controller
@RequestMapping(value = "/RTkview_ViewWS")
public class RTkview_ViewWS {
    @Autowired
    private RTkview_ViewSvc rTkview_ViewSvc;
    
    @RequestMapping(value="queryRoyalCruiseList")
    @ResponseBody
    public Map<String, Object> queryRoyalCruiseList(){
        return rTkview_ViewSvc.queryRoyalCruiseList();
    }
    
    @RequestMapping(value="getRoyalCabinList")
    @ResponseBody
    public Map<String, Object> getRoyalCabinList(String cruise){
        return rTkview_ViewSvc.getRoyalCabinList(cruise);
    }
    
    @RequestMapping(value="getRoyalTkviewDateList")
    @ResponseBody
    public Map<String, Object> getRoyalTkviewDateList(String cruise , String cabin){
        return rTkview_ViewSvc.getRoyalTkviewDateList(cruise, cabin);
    }
    
    @RequestMapping(value="getRoyalTkviewPriceList")
    @ResponseBody
    public Map<String, Object> getRoyalTkviewPriceList(String cruise , String tkview , String date) {
        return rTkview_ViewSvc.getRoyalTkviewPriceList(cruise, tkview, date);
    }

}