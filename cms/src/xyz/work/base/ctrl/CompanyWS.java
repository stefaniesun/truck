package xyz.work.base.ctrl;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.base.svc.CompanySvc;


@Controller
@RequestMapping(value = "/CompanyWS")
public class CompanyWS {
    @Autowired
    private CompanySvc companySvc;

    @RequestMapping(value = "queryCompanyList")
    @ResponseBody
    public Map<String, Object> queryCompanyList(int page , int rows , String nameCn){
        int pageSize = rows;
        int offset = (page - 1) * pageSize;
        return companySvc.queryCompanyList(offset, pageSize, nameCn);
    }

    @RequestMapping(value = "addCompany")
    @ResponseBody
    public Map<String, Object> addCompany(String nameCn , String nameEn , String websiteAddress , String remark){
        return companySvc.addCompany(nameCn, nameEn, websiteAddress, remark);
    }

    @RequestMapping(value = "editCompany")
    @ResponseBody
    public Map<String, Object> editCompany(String numberCode , String nameCn ,
                                                       String nameEn , String websiteAddress , String remark){
        return companySvc.editCompany(numberCode, nameCn, nameEn, websiteAddress, remark);
    }

    @RequestMapping(value = "deleteCompany")
    @ResponseBody
    public Map<String, Object> deleteCompany(String numberCodes){
        return companySvc.deleteCompany(numberCodes);
    }
    
}