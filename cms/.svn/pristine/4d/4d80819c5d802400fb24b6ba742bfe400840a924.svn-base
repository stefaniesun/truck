package xyz.work.resources.ctrl;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.resources.svc.ProviderSvc;


@Controller
@RequestMapping(value = "/ProviderWS")
public class ProviderWS {
    @Autowired
    public ProviderSvc providerSvc;

    @RequestMapping(value = "queryProviderList")
    @ResponseBody
    public Map<String, Object> queryProviderList(int page , int rows , String mark ,
                                                 String nameCn , String queryJson) {

        int pageSize = rows;
        int offset = (page - 1) * pageSize;

        return providerSvc.queryProviderList(offset, pageSize, nameCn, mark, queryJson);
    }

    @RequestMapping(value = "addProvider")
    @ResponseBody
    public Map<String, Object> addProvider(String mark , String nameCn , String address ,
                                           String weChat , String talkGroup , int isResponsible , 
                                           String sign , String settlement , String policy , 
                                           int status , String remark , int grade) {

        return providerSvc.addProvider(mark, nameCn, address, weChat, talkGroup,
            isResponsible, sign, settlement, policy, status, remark, grade);
    }

    @RequestMapping(value = "editProvider")
    @ResponseBody
    public Map<String, Object> editProvider(String numberCode , String nameCn , String mark ,
                                            String address , String weChat , String talkGroup , 
                                            int isResponsible , String sign , String settlement ,
                                            String policy , int status , String remark , int grade) {

        return providerSvc.editProvider(numberCode, mark, nameCn, address, weChat,
            talkGroup, isResponsible, sign, settlement, policy, status, remark, grade);
    }

    @RequestMapping(value = "deleteProviders")
    @ResponseBody
    public Map<String, Object> deleteProviders(String providers) {
        return providerSvc.deleteProviders(providers);
    }

    @RequestMapping(value = "queryPossessorProviderTrueList")
    @ResponseBody
    public Map<String, Object> queryPossessorProviderTrueList(String possessor ,
                                                              String numberCode , String name) {
        return providerSvc.queryPossessorProviderList(true, possessor, numberCode, name);
    }

    @RequestMapping(value = "queryPossessorProviderFalseList")
    @ResponseBody
    public Map<String, Object> queryPossessorProviderFalseList(String possessor ,
                                                               String numberCode , String name) {
        return providerSvc.queryPossessorProviderList(false, possessor, numberCode, name);
    }
    
    @RequestMapping(value = "getProviderMarkList")
    @ResponseBody
    public Map<String, Object> getProviderMarkList(String mark , String numberCode) {
        return providerSvc.getProviderMarkList(mark, numberCode);
    }

    @RequestMapping(value = "importExcelOper")
    @ResponseBody
    public Map<String, Object> importExcelOper(String excelPath){
        return providerSvc.importExcelOper(excelPath);
    }

    @RequestMapping(value = "exportExcelOper")
    @ResponseBody
    public Map<String, Object> exportExcelOper(){
        return providerSvc.exportExcelOper();
    }
    
    @RequestMapping(value = "editAccountList")
    @ResponseBody
    public Map<String, Object> editAccountList(String provider , String accountJson) {
        return providerSvc.editAccountList(provider, accountJson);
    }
    
    @RequestMapping(value = "editJoinerList")
    @ResponseBody
    public Map<String, Object> editJoinerList(String provider , String joinerJson) {
        return providerSvc.editJoinerList(provider, joinerJson);
    }
    
}