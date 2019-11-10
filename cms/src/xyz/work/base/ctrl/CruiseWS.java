package xyz.work.base.ctrl;


import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.base.svc.CruiseSvc;


@Controller
@RequestMapping(value = "/CruiseWS")
public class CruiseWS {
    @Autowired
    private CruiseSvc cruiseSvc;

    @RequestMapping(value = "queryCruiseList")
    @ResponseBody
    public Map<String, Object> queryCruiseList(int page , int rows , String nameCn ,
                                               String company , String mark) {
        int pageSize = rows;
        int offset = (page - 1) * pageSize;
        return cruiseSvc.queryCruiseList(offset, pageSize, nameCn, company, mark);
    }

    @RequestMapping(value = "addCruise")
    @ResponseBody
    public Map<String, Object> addCruise(String nameCn , String mark , String company ,
                                         String remark , String nameEn , BigDecimal wide ,
                                         BigDecimal length , BigDecimal tonnage ,
                                         BigDecimal floor , BigDecimal busload ,
                                         BigDecimal totalCabin , BigDecimal avgSpeed ,
                                         String voltageSource , String laundromat ,
                                         String library , String survey) {
        return cruiseSvc.addCruise(nameCn, mark, company, remark, nameEn, wide, length, tonnage,
            floor, busload, totalCabin, avgSpeed, voltageSource, laundromat, library, survey);
    }

    @RequestMapping(value = "editCruise")
    @ResponseBody
    public Map<String, Object> editCruise(String numberCode , String nameCn , String mark ,
                                          String company , String remark , String nameEn ,
                                          BigDecimal wide , BigDecimal length ,
                                          BigDecimal tonnage , BigDecimal floor ,
                                          BigDecimal busload , BigDecimal totalCabin ,
                                          BigDecimal avgSpeed , String voltageSource ,
                                          String laundromat , String library , String survey) {
        return cruiseSvc.editCruise(numberCode, nameCn, mark, company, remark, nameEn, wide,
            length, tonnage, floor, busload, totalCabin, avgSpeed, voltageSource, laundromat,
            library, survey);
    }

    @RequestMapping(value = "deleteCruise")
    @ResponseBody
    public Map<String, Object> deleteCruise(String numberCodes) {
        return cruiseSvc.deleteCruise(numberCodes);
    }

    @RequestMapping(value = "addImages")
    @ResponseBody
    public Map<String, Object> addImages(String numberCode , String urls) {
        return cruiseSvc.addImages(numberCode, urls);
    }

    @RequestMapping(value = "addDetail")
    @ResponseBody
    public Map<String, Object> addDetail(String numberCode , String detail) {
        return cruiseSvc.addDetail(numberCode, detail);
    }

    @RequestMapping(value = "addWeixinImages")
    @ResponseBody
    public Map<String, Object> addWeixinImages(String numberCode , String smallImg ,
                                               String largeImg) {
        return cruiseSvc.addWeixinImages(numberCode, smallImg, largeImg);
    }

}