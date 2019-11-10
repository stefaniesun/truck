package xyz.work.base.ctrl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.base.svc.PortSvc;

@Controller
@RequestMapping(value = "/PortWS")
public class PortWS {
    @Autowired
    public PortSvc portSvc;
    
    @RequestMapping(value = "queryPortList")
    @ResponseBody
    public Map<String, Object> queryPortList(int page , int rows , String nameCn){
        int pageSize = rows;
        int offset = (page - 1) * pageSize;
        return portSvc.queryPortList(offset, pageSize, nameCn);
    }

    @RequestMapping(value = "addPort")
    @ResponseBody
    public Map<String, Object> addPort(String nameCn , String country , String address , String details , 
                                       String remark , String images){
        return portSvc.addPort(nameCn, country, address, details, remark , images);
    }

    @RequestMapping(value = "editPort")
    @ResponseBody
    public Map<String, Object> editPort(String numberCode , String nameCn , String country , String address ,
                                        String details , String remark , String images){
        return portSvc.editPort(numberCode, nameCn, country, address, details, remark , images);
    }

    @RequestMapping(value = "deletePort")
    @ResponseBody
    public Map<String, Object> deletePort(String numberCodes){
        return portSvc.deletePort(numberCodes);
    }
    
    @RequestMapping(value = "editPortCoordinate")
    @ResponseBody
    public Map<String, Object> editPortCoordinate(String numberCode , String longitude ,
                                                  String latitude){
        return portSvc.editPortCoordinate(numberCode, longitude, latitude);
    }
    
}