package xyz.work.weixin.ctrl;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.weixin.svc.QueryDataSvc;


@Controller
@RequestMapping(value = "/QueryDataWS")
public class QueryDataWS {
    @Autowired
    private QueryDataSvc queryDataSvc;

    @RequestMapping(value = "queryCruiseList")
    @ResponseBody
    public Map<String, Object> queryCruiseList() {
        return queryDataSvc.queryCruiseList();
    }

    @RequestMapping(value = "queryCruiseAndDate")
    @ResponseBody
    public Map<String, Object> queryCruiseAndDate(String cruise) {
        return queryDataSvc.queryCruiseAndDate(cruise);
    }

    @RequestMapping(value = "clickDateOper")
    @ResponseBody
    public Map<String, Object> clickDateOper(String cruise , String date) {
        return queryDataSvc.clickDateOper(cruise, date);
    }

   @RequestMapping(value = "getPtviewSort")
    @ResponseBody
    public Map<String, Object> getPtviewSort() {
        return queryDataSvc.getPtviewSort();
    }

    @RequestMapping(value = "queryPtviewList")
    @ResponseBody
    public Map<String, Object> queryPtviewList(String date , String cruise , String port ,
                                               String sort , String order) {
        return queryDataSvc.queryPtviewList(date, cruise, port, sort, order);
    }
    
    @RequestMapping(value = "queryPtviewByNumber")
    @ResponseBody
    public Map<String, Object> queryPtviewByNumber(String ptview){
        return queryDataSvc.queryPtviewByNumber(ptview);
    }

}