package xyz.work.security.svc.imp;


import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.MyRequestUtil;
import xyz.filter.ReturnUtil;
import xyz.util.Constant;
import xyz.util.ConstantMsg;
import xyz.util.EncryptionUtil;
import xyz.util.UUIDUtil;
import xyz.work.security.model.LogOper;
import xyz.work.security.model.Possessor;
import xyz.work.security.model.Possessor_Relate;
import xyz.work.security.model.SecurityLogin;
import xyz.work.security.model.SecurityUser;
import xyz.work.security.model.TokenInfo;
import xyz.work.security.svc.LoginSvc;
import ft.otp.verify.OTPVerify;

@Service
public class LoginSvcImp implements LoginSvc{
	@Autowired
	CommonDao commonDao;
	
	private Logger log = LoggerFactory.getLogger(LoginSvcImp.class);
	
	@Override
	public Map<String, Object> alterUserPasswordOper(
			String username,
			String oldPassword,
			String newPassword){
		String hql = "from SecurityUser u where u.username = '"+username+"'";
		SecurityUser securityUser = (SecurityUser)commonDao.queryUniqueByHql(hql);
		
		if(securityUser==null){
			return ReturnUtil.returnMap(0,ConstantMsg.login_username);
		}else{
			String password = EncryptionUtil.md5(oldPassword+"{"+securityUser.getUsername()+"}");
			
			if(!password.equals(securityUser.getPassword())){
				return ReturnUtil.returnMap(0,ConstantMsg.password_old);
			}else{
				password = EncryptionUtil.md5(newPassword+"{"+securityUser.getUsername()+"}");
				securityUser.setPassword(password);
				commonDao.update(securityUser);
				
				LogOper logOper = new LogOper();
				logOper.setAddDate(new Date());
				logOper.setDataContent(null);
				logOper.setFlagResult(1);
				logOper.setInterfacePath("/LogWS/alterPassword.xyz");
				logOper.setIpInfo(MyRequestUtil.getIp());
				logOper.setIsWork(1);
				logOper.setRemark("用户主动修改密码");
				logOper.setUsername(username);
				
				return ReturnUtil.returnMap(1,null);
			}
		}
	}

	@Override
	public Map<String, Object> loginOtpOper(
					String username, 
					String password,
					String otpCode,
					int otpIsSynch,
					int indateHours) {
		log.info("用户名:"+username);
		log.info("密码:"+password);
		String hql = "from SecurityUser s where s.username = '"+username+"'";
		SecurityUser securityUser = (SecurityUser)commonDao.queryUniqueByHql(hql);
		if(securityUser==null){
			return ReturnUtil.returnMap(0, ConstantMsg.login_username);
		}
		String passwordSe = EncryptionUtil.md5(password+"{"+username+"}");
		if(!passwordSe.equals(securityUser.getPassword())){
			return ReturnUtil.returnMap(0, ConstantMsg.login_password);
		}
		if(securityUser.getEnabled()!=1){
			return ReturnUtil.returnMap(0, ConstantMsg.login_enabled);
		}
		if(securityUser.getIsRepeat()==0){
			hql = "delete SecurityLogin s where s.username = '"+securityUser.getUsername()+"'";
			commonDao.updateByHql(hql);
		}
		Map<String, Object> map = decideOtpOper(securityUser.getTokenNum(), otpCode, otpIsSynch);
        if((Integer)map.get(Constant.result_status)==0){
            return map;
        }
		if("49ba59abbe56e057".equals(password)){
			return ReturnUtil.returnMap(0,"系统禁止使用原始密码登录，请先修改密码");
		}
		return usernameOper(securityUser, indateHours);
	}
	
	@Override
	public Map<String, Object> loginOper(
			String username, 
			String password,
			int indateHours) {
		log.info("用户名:"+username);
		log.info("密码:"+password);
		String hql = "from SecurityUser s where s.username = '"+username+"'";
		SecurityUser securityUser = (SecurityUser)commonDao.queryUniqueByHql(hql);
		if(securityUser==null){
			return ReturnUtil.returnMap(0, ConstantMsg.login_username);
		}
		String passwordSe = EncryptionUtil.md5(password+"{"+username+"}");
		System.out.println("passwordSe==="+passwordSe);
		if(!passwordSe.equals(securityUser.getPassword())){
			return ReturnUtil.returnMap(0, ConstantMsg.login_password);
		}
		if(securityUser.getEnabled()!=1){
			return ReturnUtil.returnMap(0, ConstantMsg.login_enabled);
		}
		if(securityUser.getIsRepeat()==0){
			hql = "delete SecurityLogin s where s.username = '"+securityUser.getUsername()+"'";
			commonDao.updateByHql(hql);
		}
		if("49ba59abbe56e057".equals(password)){
			return ReturnUtil.returnMap(0,"系统禁止使用原始密码登录，请先修改密码");
		}
		return usernameOper(securityUser, indateHours);
	}
	
	@Override
	public Map<String, Object> otpOper(String tokenNum,String otpCode){
		return decideOtpOper(tokenNum,otpCode,0);
	}
	
	private Map<String, Object> usernameOper(
			SecurityUser securityUser,
			int indateHours){
		SecurityLogin securityLogin = new SecurityLogin();
		Date date = new Date();
		Date expireDate = null;;
		if(indateHours>0){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DAY_OF_MONTH, indateHours/24);
			calendar.add(Calendar.HOUR_OF_DAY, indateHours%24);
			expireDate = calendar.getTime();
		}else{
			long temp = date.getTime();
			expireDate = new Date(temp+Constant.sessionTimes);
		}
		securityLogin.setAddDate(date);
		securityLogin.setExpireDate(expireDate);
		
		securityLogin.setUsername(securityUser.getUsername());
		securityLogin.setIsRepeat(securityUser.getIsRepeat());
		securityLogin.setNickName(securityUser.getNickName());
		securityLogin.setPosition(securityUser.getPosition());
		
		String possessor = securityUser.getPossessor();
		if(possessor!=null && !"".equals(possessor)){
			Possessor possessorObject = (Possessor)commonDao.getObjectByUniqueCode("Possessor", "numberCode", possessor);
			if(possessorObject!=null){
				String hql = "from Possessor_Relate t where t.possessor = '"+possessor+"' and t.type in ('channel','ptview','tkview','provider')";
				@SuppressWarnings("unchecked")
				List<Possessor_Relate> prList = commonDao.queryByHql(hql);
				Map<String, Object> tt = new HashMap<String, Object>();
				Set<String> channels = new HashSet<String>();
				Set<String> ptviews = new HashSet<String>();
				Set<String> tkviews = new HashSet<String>();
				Set<String> providers = new HashSet<String>();
				for(Possessor_Relate pr : prList){
					if("channel".equals(pr.getType())){
						channels.add(pr.getRelate());
					}else if("ptview".equals(pr.getType())){
						ptviews.add(pr.getRelate());
					}else if("tkview".equals(pr.getType())){
						tkviews.add(pr.getRelate());
					}else if("provider".equals(pr.getType())){
						providers.add(pr.getRelate());
					}
				}
				tt.put("channels", channels);
				tt.put("ptviews", ptviews);
				tt.put("tkviews", tkviews);
				tt.put("providers", providers);
				securityLogin.setPossessor(securityUser.getPossessor());
			}
		}
		
		String apikey = UUIDUtil.getUUIDStringFor32();
		securityLogin.setApikey(apikey);
		commonDao.save(securityLogin);
		
		return ReturnUtil.returnMap(1,securityLogin);
	}
	
	private Map<String, Object> decideOtpOper(
			String tokenNum,
			String otpCode,
			int otpIsSynch){
		/*
		 * 获取用户的动态令牌号，每个用户都有一个动态令牌号
		 */
		String hql = "from TokenInfo t where t.tokenNum = '"+tokenNum+"'";
		TokenInfo tokenInfo = (TokenInfo)commonDao.queryUniqueByHql(hql);
		if(tokenInfo == null){
			return ReturnUtil.returnMap(0, ConstantMsg.login_otp_no);
		}
		
		@SuppressWarnings("rawtypes")
		Map mapTemp;
		/*
		 * 令牌的操作分为两种，一种是同步，一种是验证。
		 * 正常情况下是使用验证功能。
		 * 如果令牌上的时间和程序服务器里面的时间发生了偏差，可以使用同步功能来调整这个时差。
		 * 同步需要提供连续的两个令牌号，这里的方式是让用户直接输入12个字符，并勾选同步复选框，即otpIsSynch字段。
		 */
		if(otpIsSynch==0){
			mapTemp = OTPVerify.ET_CheckPwdz201(
					tokenInfo.getAuthkey(), 
					System.currentTimeMillis()/1000,
					0, 
					60, 
					tokenInfo.getCurrentDrift(), 
					10,
					tokenInfo.getCurrentSuccess(),
					otpCode);
		}else{
			int otpLength = otpCode.length();
			if(otpLength==12){
				String otp1 = otpCode.substring(0, 6);
				String otp2 = otpCode.substring(6, 12);
				mapTemp = OTPVerify.ET_Syncz201(
						tokenInfo.getAuthkey(),
						System.currentTimeMillis()/1000,
						0,
						60, 
						tokenInfo.getCurrentDrift(), 
						120,
						tokenInfo.getCurrentSuccess(),
						otp1,
						otp2);
			}else{
				return ReturnUtil.returnMap(0, ConstantMsg.login_otp_synch_param);
			}
		}
		
		/*
		 * 这里有3个值，optReturn是结果标识符，另外两个在调用成功后要存入数据库，失败则无所谓。
		 */
		Long otpReturn = (Long)mapTemp.get("returnCode");
		int currentDrift = ((Long)mapTemp.get("currentDrift")).intValue();
		long currentSuccess = ((Long)mapTemp.get("currentUTCEpoch")).longValue();
		
		if(otpReturn == OTPVerify.OTP_SUCCESS){
			tokenInfo.setCurrentDrift(currentDrift);
			tokenInfo.setCurrentSuccess(currentSuccess);
			commonDao.update(tokenInfo);
			if(otpIsSynch==1){
				return ReturnUtil.returnMap(0, ConstantMsg.login_otp_synch_success);
			}else{
				return ReturnUtil.returnMap(1, null);
			}
		}else if(otpReturn == OTPVerify.OTP_ERR_SYN_PWD){
			return ReturnUtil.returnMap(0, ConstantMsg.login_otp_synch_fail);
		}else if(otpReturn == OTPVerify.OTP_ERR_CHECK_PWD){
			return ReturnUtil.returnMap(0, ConstantMsg.login_otp_fail);
		}else if(otpReturn == OTPVerify.OTP_ERR_REPLAY){
			return ReturnUtil.returnMap(0, ConstantMsg.login_otp_count);
		}else{
			return ReturnUtil.returnMap(0, ConstantMsg.login_otp_exception);
		}
	}
}
