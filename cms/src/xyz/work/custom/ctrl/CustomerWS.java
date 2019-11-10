package xyz.work.custom.ctrl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.custom.svc.CustomerSvc;

@Controller
@RequestMapping(value = "/CustomerWS")
public class CustomerWS {
    @Autowired
    private CustomerSvc customerSvc;
    
    
    @RequestMapping(value="customerRegisterOper")
    @ResponseBody
    public Map<String, Object> customerRegisterOper(String phone,String nameCn,String img,String openid) {
        return customerSvc.customerRegisterOper(phone,nameCn,img,openid);
    }
    
    @RequestMapping(value="queryCustomerList")
    @ResponseBody
    public Map<String, Object> queryCustomerList(int page , int rows , String nickName) {
        int pageSize = rows;
        int offset = (page-1)*pageSize;
        return customerSvc.queryCustomerList(offset, pageSize, nickName);
    }
    
    @RequestMapping(value="editCustomer")
    @ResponseBody
    public Map<String, Object> editCustomer(String numberCode,String nickName,String remark){
        return customerSvc.editCustomer(numberCode, nickName, remark);
    }
    
    @RequestMapping(value="editCustomerEnabled")
    @ResponseBody
    public Map<String, Object> editCustomerEnabled(String numberCode){
        return customerSvc.editCustomerEnabled(numberCode);
    }
    
}