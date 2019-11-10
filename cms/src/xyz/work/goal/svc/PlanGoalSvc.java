package xyz.work.goal.svc;


import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;


@Service
public interface PlanGoalSvc {
    /**
     * 查询计划目标
     * 
     * @param offset
     * @param pageSize
     * @param goal
     * @param type
     * @param person
     * @param sort
     * @param order
     * @author : 熊玲
     */
    public Map<String, Object> queryPlanGoalList(int offset , int pageSize , String goal ,
                                                 String type , String person , String sort ,
                                                 String order);

    /**
     * 新增计划目标
     * 
     * @param type
     * @param goal
     * @param founder
     * @param person
     * @param endTime
     * @param state
     * @param delay
     * @param remark
     * @author : 熊玲
     */
    public Map<String, Object> addPlanGoal(String type , String goal , String founder, String person , 
                                           Date endTime , int state , int delay , String remark);

    /**
     * 编辑计划目标
     * 
     * @param numberCode
     * @param type
     * @param goal
     * @param founder
     * @param person
     * @param endTime
     * @param state
     * @param delay
     * @param remark
     * @author : 熊玲
     */
    public Map<String, Object> editPlanGoal(String numberCode , String type , String goal , String founder,
                                            String person , Date endTime , int state , int delay ,
                                            String remark);

    /**
     * 编辑类型
     * 
     * @param numberCode
     * @param type
     * @author : 熊玲
     */
    public Map<String, Object> editPlanGoalByType(String numberCode , String type);

    /**
     * 编辑状态
     * 
     * @param numberCode
     * @author : 熊玲
     */
    public Map<String, Object> editState(String numberCode);

    /**
     * 编辑是否延迟状态
     * 
     * @param numberCode
     * @author : 熊玲
     */
    public Map<String, Object> editDelay(String numberCode);
    
    /**
     * 删除计划目标
     * 
     * @param numberCodes
     * @author : 熊玲
     */
    public Map<String, Object> deletePlanGoal(String numberCodes);

}