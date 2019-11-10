package xyz.work.sell.ctrl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.sell.svc.PersonInfoSvc;


@Controller
@RequestMapping(value="/PersonInfoWS")
public class PersonInfoWS{
	
	@Autowired
	PersonInfoSvc personInfoSvc;
	
	/**
	 * 应用--作业--订单处理--查询出行人
	 */
	@RequestMapping(value="queryPersonInfoList")
	@ResponseBody
	public Map<String, Object> queryPersonInfoList(String numberCodes){
		
		return personInfoSvc.queryPersonInfoList(numberCodes);
	}
	
	/**
	 * 应用--作业--订单处理--添加出行人
	 */
	@RequestMapping(value="addPersonInfo")
	@ResponseBody
	public Map<String, Object> addPersonInfo(
			String realName,
			String ename,
			String card,
			String birthday,
			String card2,
			String expireDate,
			String hjd,
			String sex,
			String nation,
			String personType){
		
		return personInfoSvc.addPersonInfo(realName, ename, card, birthday, card2, expireDate, hjd, sex, nation, personType);
	}
	
	/**
	 * 应用--作业--订单处理--修改出行人
	 */
	@RequestMapping(value="editPersonInfo")
	@ResponseBody
	public Map<String, Object> editPersonInfo(
			String numberCode,
			String ename,
			String card,
			String birthday,
			String card2,
			String expireDate,
			String hjd,
			String sex,
			String nation,
			String personType){
		
		return personInfoSvc.editPersonInfo(numberCode,ename, card, birthday, card2, expireDate, hjd, sex, nation, personType);
	}
	
	/**
	 * 下拉框 -- combogrid 拉取出行人信息
	 */
	@RequestMapping(value="getPersonInfoList")
	@ResponseBody
	public Map<String, Object> getPersonInfoList(String q){
		
		return personInfoSvc.getPersonInfoList(q);
	}
	
}
