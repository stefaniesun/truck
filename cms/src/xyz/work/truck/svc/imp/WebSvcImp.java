package xyz.work.truck.svc.imp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qiniu.util.Auth;

import xyz.dao.CommonDao;
import xyz.filter.JSON;
import xyz.filter.ReturnUtil;
import xyz.filter.RmiUtil;
import xyz.util.Constant;
import xyz.util.StringTool;
import xyz.util.UUIDUtil;
import xyz.work.custom.model.BizSecurityLogin;
import xyz.work.custom.model.Customer;
import xyz.work.truck.model.Truck;
import xyz.work.truck.model.View;
import xyz.work.truck.svc.WebSvc;

@Service
public class WebSvcImp implements WebSvc {

	@Autowired
	CommonDao commonDao;
	
	@Autowired
	RmiUtil rmiUtil;
	
	@Override
	public Map<String, Object> getIndexData() {
		
		List<Truck> truckList=commonDao.queryByHql("from Truck where status=1 order by addDate desc");
		
		return ReturnUtil.returnMap(1, truckList);
	}

	@Override
	public Map<String, Object> truckDetail(String numberCode) {
		
		if(!StringTool.isNotNull(numberCode)) {
			return ReturnUtil.returnMap(0, "编号为空");
		}
		
		Truck truck=(Truck) commonDao.getObjectByUniqueCode("Truck", "numberCode", numberCode);
		if(truck==null) {
			return ReturnUtil.returnMap(0, "数据异常");
		}
		
		return ReturnUtil.returnMap(1, truck);
	}

	@Override
	public Map<String, Object> truckViewOper(String truck, String customer) {
		
		if(!StringTool.isNotNull(truck)) {
			return ReturnUtil.returnMap(1, null);
		}
		if(!StringTool.isNotNull(customer)) {
			customer="";
		}
		
		Truck truckObj=(Truck) commonDao.getObjectByUniqueCode("Truck", "numberCode", truck);
		if(truckObj==null) {
			return ReturnUtil.returnMap(1, null);
		}
		
		View view=new View();
		view.setNumberCode(UUIDUtil.getUUIDStringFor32());
		view.setDateInfo(new Date());
		view.setCustomer(customer);
		view.setTruck(truck);
		
		commonDao.save(view);
		
		String sql="select count(*) from view where truck='"+truck+"'";
		Object countObject=commonDao.getSqlQuery(sql).uniqueResult();
		int count=Integer.valueOf(countObject.toString());
		truckObj.setReadCount(count);
		commonDao.update(truckObj);
		
		return ReturnUtil.returnMap(1, null);
	}

	@Override
	public Map<String, Object> wechatOper(String code) {
		
	   Map<String, String> accessoryParam=new HashMap<>();
		
	   Map<String, Object> resultMap=(Map<String, Object>) rmiUtil.loadData("https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx06833e456d066236&secret=4368103514bfc80244f56695c431bef2&code="+code+"&grant_type=authorization_code",accessoryParam);
	    
	    if(resultMap!=null){
	    	
	    	String openId=resultMap.get("openid")!=null?resultMap.get("openid").toString():"";
	    	String access_token=resultMap.get("access_token")!=null?resultMap.get("access_token").toString():"";
	    	
	    	Customer customer=(Customer) commonDao.getObjectByUniqueCode("Customer", "openid", openId);
	    	if(customer==null) {
	 	 	   resultMap=(Map<String, Object>) rmiUtil.loadData("https://api.weixin.qq.com/sns/userinfo?access_token="+access_token+"&openid="+openId+"&lang=zh_CN",accessoryParam);

		    	System.out.println(JSON.toJson(resultMap));
	 	 	   
	 	 	   String nickname=resultMap.get("nickname")!=null?resultMap.get("nickname").toString():"";
	 	 	   String sex=resultMap.get("sex")!=null?resultMap.get("sex").toString():"";
		 	   String PROVINCE=resultMap.get("province")!=null?resultMap.get("province").toString():"";
		 	   String city=resultMap.get("city")!=null?resultMap.get("city").toString():"";
		 	   String country=resultMap.get("country")!=null?resultMap.get("country").toString():"";
		 	   String headimgurl=resultMap.get("headimgurl")!=null?resultMap.get("headimgurl").toString():"";
		 	   
		 	  customer=new Customer();
		 	   
		 	 customer.setNumberCode(UUIDUtil.getUUIDStringFor32());
		 	customer.setAddDate(new Date());
		 	customer.setEnabled(0);
		 	customer.setImg(headimgurl);
		 	customer.setNameCn(nickname);
		 	customer.setOpenid(openId);
		 	customer.setCountry(country);
		 	customer.setCity(city);
		 	customer.setProvince(PROVINCE);
		 	customer.setSex(sex);
		 	customer.setEnabled(1);
		 	customer.setFlagRegister(0);
		 	
		 	commonDao.save(customer);
		 	
		 	
	    	}
	    	
	    	
	    	BizSecurityLogin securityLogin = new BizSecurityLogin();
		       Date currentDate = new Date();
		       Date expireDate = new Date(currentDate.getTime()+Constant.sessionTimes);
		       String apikey = UUIDUtil.getUUIDStringFor32();
		       securityLogin.setAddDate(currentDate);
		       securityLogin.setExpireDate(expireDate);
		       securityLogin.setNumberCode(customer.getNumberCode());
		       securityLogin.setFlagRegister(customer.getFlagRegister());
		       securityLogin.setApikey(apikey);
		       securityLogin.setImg(customer.getImg());
		       securityLogin.setNameCn(customer.getNameCn());
		       commonDao.save(securityLogin);
				
			   return ReturnUtil.returnMap(1, securityLogin);
	    	
	    }
		
	    return ReturnUtil.returnMap(1, null);
	}

	@Override
	public Map<String, Object> getToken() {
		 	String upToken=getUpToken();
		    Map<String, String> map = new HashMap<String, String>();
		    map.put("upToken", upToken);
		    System.out.println("token======================"+upToken);
		    return ReturnUtil.returnMap(1, map);
	}
	
	/** 基本配置-从七牛管理后台拿到，这两个后台获取
	 * 设置好账号的ACCESS_KEY和SECRET_KEY
	 */
	public String ACCESS_KEY = "7uwiPLPm6-0Hvd_qds5bJiToLuUrKcEZcC3XN774";

	public String SECRET_KEY = "8fx6lc7DzJgvZtFMA33Ooi6Ivq9fAHDD6wnLnBdj";

	// 要上传的空间名--bucketname 也叫存储空间名，在七年里自己设置的
	//对应要上传到七牛上 你的那个路径（自己建文件夹 注意设置公开）
	public String bucketname = "lfhcw";

	public String getUpToken() {
	    // 密钥配置
	    System.out.println("初始化上传变量");
	    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
	    return auth.uploadToken(bucketname);
	}

}
