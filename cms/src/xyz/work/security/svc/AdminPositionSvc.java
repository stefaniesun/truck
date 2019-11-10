package xyz.work.security.svc;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.stereotype.Service;

import xyz.work.security.model.SecurityPosition;


@Service
public interface AdminPositionSvc {
	
	public Map<String, Object> queryPositionList(int offset,int pagesize,String numberCode,String nameCn);
	
	public Map<String, Object> addPosition(SecurityPosition securityPosition);
	
	public Map<String, Object> editPosition(
			String numberCode,
			String nameCn,
			String remark);
	
	public Map<String, Object> deletePosition(String positions);
	
	public Map<String, Object> queryPositionButtonList(boolean isContain,
                                            		   String position, 
                                            		   String nameCn,
                                            		   String functionNameCn,
                                            		   BigDecimal isPossessor);
	
	public Map<String, Object> queryPositionButtonTree(
			boolean isContain,
			String position);
	
	public Map<String, Object> addPositionButton(String position,String buttons);
	
	public Map<String, Object> deletePositionButton(String position,String buttons);
	
	public Map<String, Object> queryPossessorPositionList(Boolean isTrue,
	                                                      String possessor,
	                                                      String numberCode,
	                                                      String name);

}
