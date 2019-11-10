package xyz.work.resources.ctrl;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.resources.svc.ChannelSvc;



@Controller
@RequestMapping(value = "/ChannelWS")
public class ChannelWS {

    @Autowired
    ChannelSvc channelSvc;

    @RequestMapping(value = "queryChannelList")
    @ResponseBody
    public Map<String, Object> queryChannelList(int page , int rows,String numberCode,String nameCn){
        int pagesize = rows;
        int offset = (page - 1) * pagesize;
        return channelSvc.queryChannelList(offset, pagesize, numberCode, nameCn);
    }

    @RequestMapping(value = "addChannel")
    @ResponseBody
    public Map<String, Object> addChannel(String nameCn,
                                        String code,
                                        String appKey,
                                        String appSecret,
                                        String redirectUri,
                                        String remark){
        
        return channelSvc.addChannel(nameCn,code, appKey, appSecret, redirectUri, remark);
    }
    
    @RequestMapping(value = "editChannel")
    @ResponseBody
    public Map<String, Object> editChannel(String numberCode,
                                        String nameCn,
                                        String remark){
        return channelSvc.editChannel(numberCode, nameCn, remark);
    }

    @RequestMapping(value = "deleteChannel")
    @ResponseBody
    public Map<String, Object> deleteChannel(String numberCodes) {
        return channelSvc.deleteChannel(numberCodes);
    }
    
    @RequestMapping(value = "setAuthorizeOper")
    @ResponseBody
    public Map<String, Object> setAuthorizeOper(String code,String appKey,String url,String numberCode) {
        return channelSvc.setAuthorizeOper(code, appKey, url, numberCode);
    }
    
    @RequestMapping(value = "queryPossessorChannelTrueList")
    @ResponseBody
    public Map<String, Object> queryPossessorChannelTrueList(String possessor,String numberCode,String name) {
        return channelSvc.queryPossessorChannelList(true, possessor,numberCode,name);
    }
    
    @RequestMapping(value = "queryPossessorChannelFalseList")
    @ResponseBody
    public Map<String, Object> queryPossessorChannelFalseList(String possessor,String numberCode,String name) {
        return channelSvc.queryPossessorChannelList(false, possessor,numberCode,name);
    }
    
    @RequestMapping(value = "queryChannlListByNumberCode")
    @ResponseBody
    public Map<String, Object> queryChannlListByNumberCode(String numberCode) {
        return channelSvc.queryChannlListByNumberCode(numberCode);
    }
    
    @RequestMapping(value = "editPriceStrategyByNumberCode")
    @ResponseBody
    public Map<String, Object> editPriceStrategyByNumberCode(String numberCode , String priceStrategy) {
        return channelSvc.editPriceStrategyByNumberCode(numberCode ,priceStrategy);
    }
    
    @RequestMapping(value = "editSystemMemory")
    @ResponseBody
    public Map<String, Object> editSystemMemory() {
        return channelSvc.editSystemMemory();
    }
    
}
