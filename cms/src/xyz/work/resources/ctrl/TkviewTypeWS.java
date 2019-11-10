package xyz.work.resources.ctrl;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.resources.svc.TkviewTypeSvc;


@Controller
@RequestMapping(value = "/TkviewTypeWS")
public class TkviewTypeWS {

    @Autowired
    public TkviewTypeSvc tkviewTypeSvc;
    
    @RequestMapping(value = "queryTkviewTypeList")
    @ResponseBody
    public Map<String, Object> queryTkviewTypeList(int page , 
                                                   int rows , 
                                                   String cruise ,
                                                   String cabin) {
        int pagesize = rows;
        int offset = (page - 1) * pagesize;
        return tkviewTypeSvc.queryTkviewTypeList(offset, pagesize, cruise, cabin);
    }

}