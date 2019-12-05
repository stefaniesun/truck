package xyz.work.custom.svc.imp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.ReturnUtil;
import xyz.util.Constant;
import xyz.util.ConstantMsg;
import xyz.util.StringTool;
import xyz.util.UUIDUtil;
import xyz.work.custom.model.BizSecurityLogin;
import xyz.work.custom.model.Customer;
import xyz.work.custom.svc.CustomerSvc;

@Service
public class CustomerSvcImp implements CustomerSvc {
    @Autowired
    private CommonDao commonDao;
        
    @Override
    public Map<String, Object> queryCustomerList(int offset , int pageSize , String nickName) {
        StringBuffer sql = new StringBuffer("from Customer c where 1=1 ");
        if(StringTool.isNotNull(nickName)){
            sql.append(" and c.nickName like '%"+ nickName +"%'");
        }
        sql.append(" order by c.alterDate desc");
        
        Query countQuery = commonDao.getQuery("select count(*) " + sql.toString());
        Number countNum = (Number)countQuery.uniqueResult();
        int count = countNum == null ? 0 : countNum.intValue();

        Query query = commonDao.getQuery(sql.toString());
        query.setMaxResults(pageSize);
        query.setFirstResult(offset);
        @SuppressWarnings("unchecked")
        List<Customer> list = query.list();
        
        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", count);
        mapContent.put("rows", list);

        return ReturnUtil.returnMap(1, mapContent);
    }

    @Override
    public Map<String, Object> editCustomer(String numberCode,String nickName , String remark) {
        if(StringTool.isEmpty(numberCode)){
            return ReturnUtil.returnMap(0, ConstantMsg.customer_numberCode_null);
        }
        if(StringTool.isEmpty(nickName)){
            return ReturnUtil.returnMap(0, ConstantMsg.customer_nickName_null);
        }
        Customer customer = (Customer)commonDao.getObjectByUniqueCode("Customer", "numberCode", numberCode);
        if(customer == null){
            return ReturnUtil.returnMap(0, ConstantMsg.customer_error);
        }
        customer.setRemark(remark);
        customer.setAlterDate(new Date());
        commonDao.update(customer);
        
        return ReturnUtil.returnMap(1, null);
    }

	@Override
	public Map<String, Object> customerRegisterOper(String phone,String customer,String img,String openid) {
		
	   if(StringTool.isEmpty(phone)){
            return ReturnUtil.returnMap(0, "手机号为空");
        }
	   
	   if(StringTool.isEmpty(customer)){
           return ReturnUtil.returnMap(0, "用户信息为空");
       }
	   
	   Customer customerObjTemp=(Customer) commonDao.getObjectByUniqueCode("Customer", "phone", phone);
	   if(customerObjTemp!=null) {
		   return ReturnUtil.returnMap(0, "该手机号已被注册");
	   }
	   
	   Customer customerObj=(Customer) commonDao.getObjectByUniqueCode("Customer", "numberCode", customer);
	   if(customer==null) {
		   return ReturnUtil.returnMap(0, "用户不存在");
	   }
	   
	   customerObj.setPhone(phone);
	   customerObj.setFlagRegister(1);
	   customerObj.setRegisterDate(new Date());
	   
	   commonDao.update(customerObj);
	   
	   
	   List<BizSecurityLogin> securityLoginList=commonDao.queryByHql("from BizSecurityLogin where numberCode='"+customer+"' order by addDate desc");
	   
		 if(securityLoginList.size()==0) {
			   return ReturnUtil.returnMap(0, "登陆用户不存在");
		   }
		 
		 BizSecurityLogin securityLogin=securityLoginList.get(0);

		
		securityLogin.setFlagRegister(1);
	   
	   return ReturnUtil.returnMap(1, securityLogin);
	}

	@Override
	public Map<String, Object> editCustomerEnabled(String numberCode) {
		
		 if(StringTool.isEmpty(numberCode)){
	            return ReturnUtil.returnMap(0, "参数为空");
        }
		 
		 Customer customer=(Customer) commonDao.getObjectByUniqueCode("Customer", "numberCode", numberCode);
		if(customer==null) {
			 return ReturnUtil.returnMap(0, "用户为空");
		}
		 
		if(customer.getEnabled()==0) {
			customer.setEnabled(1);
		}else {
			customer.setEnabled(0);
		}
		 
		 return ReturnUtil.returnMap(1, null);
	}

}