package xyz.work.base.ctrl;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.base.svc.AreaSvc;


@Controller
@RequestMapping(value = "/AreaWS")
public class AreaWS {
    @Autowired
    AreaSvc areaSvc;
    
    @RequestMapping(value = "queryAreaList")
    @ResponseBody
    public Map<String, Object> queryAreaList(int page , int rows , String nameCn){
        int pageSize = rows;
        int offset = (page - 1) * pageSize;
        return areaSvc.queryAreaList(offset, pageSize, nameCn);
    }

    @RequestMapping(value = "addArea")
    @ResponseBody
    public Map<String, Object> addArea(String nameCn , String remark){
        return areaSvc.addArea(nameCn, remark);
    }

    @RequestMapping(value = "editArea")
    @ResponseBody
    public Map<String, Object> editArea(String numberCode , String nameCn , String remark){
        return areaSvc.editArea(numberCode, nameCn, remark);
    }

    @RequestMapping(value = "deleteArea")
    @ResponseBody
    public Map<String, Object> deleteArea(String numberCodes){
        return areaSvc.deleteArea(numberCodes);
    }
    
}