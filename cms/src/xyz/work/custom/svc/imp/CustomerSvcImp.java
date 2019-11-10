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
	public Map<String, Object> customerRegisterOper(String phone,String nameCn,String img,String openid) {
		
	   if(StringTool.isEmpty(phone)){
            return ReturnUtil.returnMap(0, "手机号为空");
        }
	   
	   Customer customer=(Customer) commonDao.getObjectByUniqueCode("Customer", "phone", phone);
	   if(customer!=null) {
		   return ReturnUtil.returnMap(0, "该手机号已被注册");
	   }
	   
	   Customer customerObj=new Customer();
	   customerObj.setNumberCode(UUIDUtil.getUUIDStringFor32());
	   customerObj.setAddDate(new Date());
	   customerObj.setPhone(phone);
	   customerObj.setEnabled(1);
	   customerObj.setOpenid(openid);
	   customerObj.setImg(img);
	   customerObj.setNameCn(nameCn);
	   
	   commonDao.save(customerObj);
	   
	   
	   BizSecurityLogin securityLogin = new BizSecurityLogin();
       Date currentDate = new Date();
       Date expireDate = new Date(currentDate.getTime()+Constant.sessionTimes);
       String apikey = UUIDUtil.getUUIDStringFor32();
       securityLogin.setAddDate(currentDate);
       securityLogin.setExpireDate(expireDate);
       securityLogin.setNumberCode(customerObj.getNumberCode());
      // securityLogin.setUserCode(orderList.get(0).getOrderNum()); 
       securityLogin.setApikey(apikey);
       securityLogin.setNameCn(nameCn);
       commonDao.save(securityLogin);
		
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