package xyz.util;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import xyz.dao.CommonDao;
import xyz.filter.MyRequestUtil;
import xyz.work.security.model.Possessor_Relate;

@Component
public final class PossessorUtil {

	@Resource
	private CommonDao commonDao;
	
	private PossessorUtil() {}

	/**
	 * 获取机构的权限
	 * @author Xzavier Sun
	 * @param possessor
	 * @param type
	 * @return
	 */
	public Map<String ,List<String>> getDecideMap(String ...type){
	    String possessor = MyRequestUtil.getPossessor();
	    String sql = "SELECT t.type, GROUP_CONCAT(t.relate) FROM possessor_relate t WHERE t.possessor = '"+possessor+"'";
	    if(type.length > 0){
	        sql += " AND t.type IN ("+StringTool.listToSqlString(Arrays.asList(type))+")";
	    }
	    sql += " GROUP BY t.type";
	    @SuppressWarnings("unchecked")
        List<Object[]> resultList = commonDao.getSqlQuery(sql).list();
        Map<String ,List<String>> decideMap = new HashMap<String, List<String>>();
	    for (Object[] type_relate : resultList) {
	        String typeStr = (String)type_relate[0];
	        String relates = (String)type_relate[1];
	        List<String> relateList = Arrays.asList(relates.split(","));
	        decideMap.put(typeStr, relateList==null?new ArrayList<String>():relateList);
        }
	    
	    for (String tt : type) {
            if(!decideMap.containsKey(tt)){
                decideMap.put(tt, new ArrayList<String>());
            }
        }
	    
	    return decideMap;
	}
	
	public Map<String ,List<String>> getDecideMapByPossessor(String possessor,String ...type){
	    
	    if (StringTool.isEmpty(possessor)) {
            return null;
        }
        String sql = "SELECT t.type, GROUP_CONCAT(t.relate) FROM possessor_relate t WHERE 1=1";
        sql += " AND t.possessor = '"+possessor+"'";
        
        if(type.length > 0){
            sql += " AND t.type IN ("+StringTool.listToSqlString(Arrays.asList(type))+")";
        }
        sql += " GROUP BY t.type";
        @SuppressWarnings("unchecked")
        List<Object[]> resultList = commonDao.getSqlQuery(sql).list();
        Map<String ,List<String>> decideMap = new HashMap<String, List<String>>();
        for (Object[] type_relate : resultList) {
            String typeStr = (String)type_relate[0];
            String relates = (String)type_relate[1];
            
            List<String> relateList = Arrays.asList(relates.split(","));
            decideMap.put(typeStr, relateList==null?new ArrayList<String>():relateList);
        }
        
        return decideMap;
    }
	
	/**
	 * 给机构资源绑定权限
	 * @author Xzavier Sun
	 * @param possessor 机构编号
	 * @param type 表名
	 * @param relate 编号(唯一标识)
	 */
	public void savePossessorRelates(String type ,String relate){
	    
	    String possessor = MyRequestUtil.getPossessor();
	    if(StringTool.isEmpty(possessor)){
	        return;
	    }
	    Possessor_Relate possessorRelate = new Possessor_Relate();
	    possessorRelate.setPossessor(possessor);
	    possessorRelate.setRelate(relate);
	    possessorRelate.setType(type);
	    
	    commonDao.save(possessorRelate);
	}
	
	/**
     * HQL权限查询
     *
     * @param type 表名
     * @param not  not in/in
     * @return String  返回结果
	 */
	public String getRelatesWhereHql(String type,String ...not){
        String possessor = MyRequestUtil.getPossessor();
        if(StringTool.isNotNull(possessor)){
            String notIn = not.length <= 0 ? "" : not[0];
            List<String> relates = getDecideMap(type).keySet().contains(type)?getDecideMap(type).get(type):new ArrayList<String>();
            return " and numberCode "+ notIn +" in ("+ StringTool.listToSqlString(relates) +") ";
        }
        
        return "";
    }
	
	/**
     * SQL权限查询
     *
     * @param type 表名
     * @param alias  别名
     * @return String  返回结果
     */
	public String getRelatesWhereSql(String type,String ...alias){
        String possessor = MyRequestUtil.getPossessor();
        if(StringTool.isNotNull(possessor)){
            List<String> relates = getDecideMap(type).keySet().contains(type)?getDecideMap(type).get(type):new ArrayList<String>();
            
            return " AND "+(alias.length>0?alias[0]+".":"")+"number_code IN ("+StringTool.listToSqlString(relates)+") ";
        }
        
        return " ";
    }
	
}