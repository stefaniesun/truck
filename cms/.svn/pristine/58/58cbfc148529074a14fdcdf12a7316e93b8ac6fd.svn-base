package xyz.work.security.ctrl;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.security.model.SecurityPosition;
import xyz.work.security.svc.AdminPositionSvc;

@Controller
@RequestMapping(value="/AdminPositionWS")
public class AdminPositionWS{
	
	@Autowired
	private AdminPositionSvc adminPositionSvc;
	
	/**
	 * 管理--岗位--查询岗位列表
	 */
	@RequestMapping(value="queryPositionList")
	@ResponseBody
	public Map<String, Object> queryPositionList(
			int page,
			int rows,
			String numberCode,
			String nameCn){
		int pagesize = rows;
		int offset = (page-1)*pagesize;
		return adminPositionSvc.queryPositionList(offset, pagesize,numberCode,nameCn);
	}
	
	/**
	 * 管理--岗位--新增岗位
	 */
	@RequestMapping(value="addPosition")
	@ResponseBody
	public Map<String, Object> addPosition(
			String numberCode,
			String nameCn,
			String remark){
		SecurityPosition securityPosition = new SecurityPosition();
		securityPosition.setNumberCode(numberCode);
		securityPosition.setNameCn(nameCn);
		securityPosition.setRemark(remark);
		return adminPositionSvc.addPosition(securityPosition);
	}
	
	/**
	 * 管理--岗位--编辑岗位
	 */
	@RequestMapping(value="editPosition")
	@ResponseBody
	public Map<String, Object> editPosition(
			String numberCode,
			String nameCn,
			String remark){
		return adminPositionSvc.editPosition(numberCode,nameCn, remark);
	}
	
	@RequestMapping(value="deletePosition")
	@ResponseBody
	public Map<String, Object> deletePosition(
			String positions){
		return adminPositionSvc.deletePosition(positions);
	}
	
	@RequestMapping(value="queryPositionButtonTrueList")
	@ResponseBody
	public Map<String, Object> queryPositionButtonTrueList(String nameCn,
                                                		   String position,
                                                		   String functionNameCn ){
		return adminPositionSvc.queryPositionButtonList(true,position,nameCn,functionNameCn,null);
	}
	
	@RequestMapping(value="queryPositionButtonFalseList")
	@ResponseBody
	public Map<String, Object> queryPositionButtonFalseList(String nameCn,
			                                                String position,
			                                                String functionNameCn,
			                                                BigDecimal isPossessor){
		return adminPositionSvc.queryPositionButtonList(false,position,nameCn,functionNameCn,isPossessor);
	}
	
	@RequestMapping(value="queryPositionButtonTrueTree")
	@ResponseBody
	public Map<String, Object> queryPositionButtonTrueTree(
			String position){
		return adminPositionSvc.queryPositionButtonTree(true,position);
	}
	
	@RequestMapping(value="queryPositionButtonFalseTree")
	@ResponseBody
	public Map<String, Object> queryPositionButtonFalseTree(
			String position){
		return adminPositionSvc.queryPositionButtonTree(false,position);
	}
	
	@RequestMapping(value="addPositionButton")
	@ResponseBody
	public Map<String, Object> addPositionButton(
			String position,
			String buttons){
		return adminPositionSvc.addPositionButton(position, buttons);
	}
	
	@RequestMapping(value="deletePositionButton")
	@ResponseBody
	public Map<String, Object> deletePositionButton(
			String position,
			String buttons){
		return adminPositionSvc.deletePositionButton(position, buttons);
	}
	
	@RequestMapping(value = "queryPossessorPositionTrueList")
    @ResponseBody
    public Map<String, Object> queryPossessorPositionTrueList(String possessor,String numberCode,String name) {
        return adminPositionSvc.queryPossessorPositionList(true,possessor,numberCode,name);
    }
    
    @RequestMapping(value = "queryPossessorPositionFalseList")
    @ResponseBody
    public Map<String, Object> queryPossessorPositionFalseList(String possessor,String numberCode,String name) {
        return adminPositionSvc.queryPossessorPositionList(false,possessor,numberCode,name);
    }
	
}
