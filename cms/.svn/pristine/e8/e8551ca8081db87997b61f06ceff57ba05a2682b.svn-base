package xyz.work.resources.ctrl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.resources.svc.AccountSvc;

@Controller
@RequestMapping(value = "/AccountWS")
public class AccountWS {
    @Autowired
    public AccountSvc accountSvc;
    
    @RequestMapping(value = "queryAccountList")
    @ResponseBody
    public Map<String, Object> queryAccountList(int page , int rows , int isEnable ,  String accountNumber , 
                                                String cashAccount ,String bankOfDeposit , String provider){
        int pageSize = rows;
        int offset = (page - 1) * pageSize;
        return accountSvc.queryAccountList(offset, pageSize, isEnable, accountNumber, cashAccount, bankOfDeposit, provider);
    }

    @RequestMapping(value = "addAccount")
    @ResponseBody
    public Map<String, Object> addAccount(String accountNumber , String cashAccount , String bankOfDeposit,
                                          String provider , int isEnable , String remark){
        return accountSvc.addAccount(accountNumber, cashAccount, bankOfDeposit, provider, isEnable, remark);
    }

    @RequestMapping(value = "editAccount")
    @ResponseBody
    public Map<String, Object> editAccount(String numberCode ,String accountNumber , String cashAccount , 
                                           String bankOfDeposit , String provider , int isEnable , String remark){
        return accountSvc.editAccount(numberCode, accountNumber, cashAccount, bankOfDeposit, provider, isEnable, remark);
    }

    @RequestMapping(value = "editIsEnable")
    @ResponseBody
    public Map<String, Object> editIsEnable(String numberCode){
        return accountSvc.editIsEnable(numberCode);
    }

    @RequestMapping(value = "deleteAccount")
    @ResponseBody
    public Map<String, Object> deleteAccount(String numberCodes){
        return accountSvc.deleteAccount(numberCodes);
    }
    
    @RequestMapping(value = "queryAccountListByprovider")
    @ResponseBody
    public Map<String, Object> queryAccountListByprovider(String provider) {
        return accountSvc.queryAccountListByprovider(provider);
    }
    
}