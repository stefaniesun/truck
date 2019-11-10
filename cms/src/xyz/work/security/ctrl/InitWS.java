package xyz.work.security.ctrl;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import xyz.exception.MyExceptionForRole;
import xyz.filter.XmlUtil;
import xyz.util.Constant;
import xyz.work.security.model.SecurityUser;
import xyz.work.security.svc.InitSvc;

@Controller
@RequestMapping(value="/InitWS")
public class InitWS {
	
	@Autowired
	private InitSvc initSvc;
	
	private Logger log = LoggerFactory.getLogger(InitWS.class);
	
	@PostConstruct
	@RequestMapping(value="init_1239127awdasd_api")
	public String init_1239127awdasd_api(){		
		Map<String, Object> t = XmlUtil.XmlUtilFunction("role/sysFunction.xml");
		Map<String, Object> map = initSvc.initApiOper(t);
		if((Integer)map.get(Constant.result_status)!=1){
			log.info(map.get(Constant.result_msg)+"");
			throw new MyExceptionForRole("初始化失败");
		}
		map = initSvc.initAdminPositionOper();
		if((Integer)map.get(Constant.result_status)!=1){
			log.info(map.get(Constant.result_msg)+"");
			throw new MyExceptionForRole("初始化失败");
		}
		SecurityUser securityUser = XmlUtil.XmlUtilAdmin("role/sysAdmin.xml");
		map = initSvc.initAdminOper(securityUser);
		if((Integer)map.get(Constant.result_status)!=1){
			log.info(map.get(Constant.result_msg)+"");
			throw new MyExceptionForRole("初始化失败");
		}
		return "forward:../xyzsecurity/200_init.html";
	}
}
