package xyz.work.resources.ctrl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.resources.svc.JoinerSvc;

@Controller
@RequestMapping(value = "/JoinerWS")
public class JoinerWS {
    @Autowired
    public JoinerSvc joinerSvc;
    
    @RequestMapping(value = "queryJoinerList")
    @ResponseBody
    public Map<String, Object> queryJoinerList(int page , int rows , String nameCn ,  String phone ,
                                                String weChat , String provider){
        int pageSize = rows;
        int offset = (page - 1) * pageSize;
        return joinerSvc.queryJoinerList(offset, pageSize, nameCn, phone, weChat, provider);
    }
    
    @RequestMapping(value = "addJoiner")
    @ResponseBody
    public Map<String, Object> addJoiner(String nameCn , String phone , String weChat,
                                         String provider , String qq , String remark){
        return joinerSvc.addJoiner(nameCn, phone, weChat, provider, qq, remark);
    }
    
    @RequestMapping(value = "editJoiner")
    @ResponseBody
    public Map<String, Object> editJoiner(String numberCode ,String nameCn , String phone , 
                                          String weChat , String provider , String qq , String remark){
        return joinerSvc.editJoiner(numberCode, nameCn, phone, weChat, provider, qq, remark);
    }
    
    @RequestMapping(value = "deleteJoiner")
    @ResponseBody
    public Map<String, Object> deleteJoiner(String numberCodes){
        return joinerSvc.deleteJoiner(numberCodes);
    }
    
    @RequestMapping(value = "queryJoinerListByprovider")
    @ResponseBody
    public Map<String, Object> queryJoinerListByprovider(String provider){
        return joinerSvc.queryJoinerListByprovider(provider);
    }
    
}