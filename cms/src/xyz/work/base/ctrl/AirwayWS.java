package xyz.work.base.ctrl;


import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.base.svc.AirwaySvc;


@Controller
@RequestMapping(value = "/AirwayWS")
public class AirwayWS {
    @Autowired
    private AirwaySvc airwaySvc;

    @RequestMapping(value="queryAirwayList")
    @ResponseBody
    public Map<String, Object> queryAirwayList(int page, int rows, String nameCn,
                                               BigDecimal days, BigDecimal nights, String area,
                                               String sort, String order){
        int pageSize = rows;
        int offset = (page - 1) * pageSize;
        return airwaySvc.queryAirwayList(offset, pageSize, nameCn, days, nights, area , sort, order);
    }

    @RequestMapping(value="addAirway")
    @ResponseBody
    public Map<String, Object> addAirway(String area, String nameCn, String mark, BigDecimal days,
                                         BigDecimal nights, String remark){
        return airwaySvc.addAirway(area, nameCn, mark, days, nights, remark);
    }

    @RequestMapping(value="editAirway")
    @ResponseBody
    public Map<String, Object> editAirway(String area, String numberCode, String nameCn,String mark,
                                          BigDecimal days, BigDecimal nights, String remark){
        return airwaySvc.editAirway(area, numberCode, nameCn, mark, days, nights, remark);
    }

    @RequestMapping(value="deleteAirway")
    @ResponseBody
    public Map<String, Object> deleteAirway(String numberCodes){
        return airwaySvc.deleteAirway(numberCodes);
    }

    @RequestMapping(value="getTripByAirway")
    @ResponseBody
    public Map<String, Object> getTripByAirway(String airway){
        return airwaySvc.getTripByAirway(airway);
    }
    
}