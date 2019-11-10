package xyz.work.r_base.ctrl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.r_base.svc.R_TaskSvc;

@Controller
@RequestMapping(value = "/R_TaskWS")
public class R_TaskWS {
    @Autowired
    private R_TaskSvc rTaskSvc;
    
    @RequestMapping(value="queryTaskList")
    @ResponseBody
    public Map<String, Object> queryTaskList(int page, int rows){
        int pageSize = rows;
        int offset = (page-1)*pageSize;
        return rTaskSvc.queryTaskList(offset, pageSize);
    }
    
    
    @RequestMapping(value="manualUpdateDataOper")
    @ResponseBody
    public Map<String, Object> manualUpdateDataOper(){
        return rTaskSvc.manualUpdateDataOper();
    }
    
    
    
}