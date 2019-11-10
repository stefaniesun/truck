package xyz.work.goal.ctrl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.goal.svc.PersonSvc;

@Controller
@RequestMapping(value = "/PersonWS")
public class PersonWS {
    @Autowired
    private PersonSvc personSvc;
    
    @RequestMapping(value="queryPersonList")
    @ResponseBody
    public Map<String, Object> queryPersonList(int page, int rows , String nameCn){
        int pageSize = rows;
        int offset = (page - 1) * pageSize;
        return personSvc.queryPersonList(offset, pageSize, nameCn);
    }
    
    @RequestMapping(value="addPerson")
    @ResponseBody
    public Map<String, Object> addPerson(String nameCn, String sex, int post){
        return personSvc.addPerson(nameCn, sex, post);
    }
    
    @RequestMapping(value="editPerson")
    @ResponseBody
    public Map<String, Object> editPerson(String numberCode, String nameCn, String sex, int post){
        return personSvc.editPerson(numberCode, nameCn, sex, post);
    }
    
    @RequestMapping(value="deletePerson")
    @ResponseBody
    public Map<String, Object> deletePerson(String numberCodes){
        return personSvc.deletePerson(numberCodes);
    }
    
}