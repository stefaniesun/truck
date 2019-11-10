package xyz.work.goal.ctrl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.goal.svc.RecordSvc;

@Controller
@RequestMapping(value = "/RecordWS")
public class RecordWS {
    @Autowired
    private RecordSvc recordSvc;
    
    @RequestMapping(value="queryRersonList")
    @ResponseBody
    public Map<String, Object> queryRersonList(int page, int rows , String content){
        int pageSize = rows;
        int offset = (page - 1) * pageSize;
        return recordSvc.queryRersonList(offset, pageSize, content);
    }
    
    @RequestMapping(value="addRerson")
    @ResponseBody
    public Map<String, Object> addRerson(String content, String remark){
        return recordSvc.addRerson(content, remark);
    }
    
    @RequestMapping(value="editRerson")
    @ResponseBody
    public Map<String, Object> editRerson(String numberCode, String content, String remark){
        return recordSvc.editRerson(numberCode, content, remark);
    }
    
    @RequestMapping(value="deleteRerson")
    @ResponseBody
    public Map<String, Object> deleteRerson(String numberCodes){
        return recordSvc.deleteRerson(numberCodes);
    }
    
    @RequestMapping(value="addDetails")
    @ResponseBody
    public Map<String, Object> addDetails(String numberCode, String details){
        return recordSvc.addDetails(numberCode, details);
    }
    
}