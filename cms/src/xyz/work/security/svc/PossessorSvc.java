package xyz.work.security.svc;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface PossessorSvc{
	
	public Map<String ,Object> queryPossessorList(
			int offset,
			int pagesize,
			String nameCn);
	
	public Map<String ,Object> getPossessor(String numberCode);
	
	public Map<String ,Object> addPossessor(
			String nameCn,
			String remark);
	
	public Map<String ,Object> editPossessor(
			String numberCode,
			String nameCn,
			String remark);
	
	public Map<String ,Object> deletePossessor(String numberCodes);
	
	public Map<String ,Object> deletePossessorResource(String possessor, String possessorResources ,boolean isChannel);
	
	public Map<String ,Object> addPossessorResource(
			String possessor,
			String resources,
			String resourceType);
	
	public Map<String ,Object> queryPositionListByInOrNotInUserNumberCodes(
			int offset,
			int pagesize,
			String inPositionNumberCodes,
			String position,
            String nameCn,
			boolean flag);
	
	public Map<String ,Object> queryPossessorResourceList(String possessor ,String type);
	
}
