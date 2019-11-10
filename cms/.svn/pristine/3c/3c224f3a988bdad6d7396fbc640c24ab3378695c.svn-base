package xyz.work.goal.ctrl;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.goal.svc.PlanGoalSvc;


@Controller
@RequestMapping(value = "/PlanGoalWS")
public class PlanGoalWS {
    @Autowired
    private PlanGoalSvc planGoalSvc;
    
    @RequestMapping(value="queryPlanGoalList")
    @ResponseBody
    public Map<String, Object> queryPlanGoalList(int page, int rows, String goal , String type ,
                                                 String person , String sort , String order){
        int pageSize = rows;
        int offset = (page - 1) * pageSize;
        return planGoalSvc.queryPlanGoalList(offset, pageSize, goal, type, person, sort, order);
    }

    @RequestMapping(value="addPlanGoal")
    @ResponseBody
    public Map<String, Object> addPlanGoal(String type , String goal , String founder,
                                           String person , Date endTime , int state ,  int delay ,
                                           String remark){
        return planGoalSvc.addPlanGoal(type, goal, founder, person, endTime, state, delay, remark);
    }
    
    @RequestMapping(value="editPlanGoal")
    @ResponseBody
    public Map<String, Object> editPlanGoal(String numberCode , String type , String goal ,
                                            String founder, String person , Date endTime , 
                                            int state ,  int delay , String remark){
        return planGoalSvc.editPlanGoal(numberCode, type, goal, founder, person, endTime, state, delay, remark);
    }
    
    @RequestMapping(value="editPlanGoalByType")
    @ResponseBody
    public Map<String, Object> editPlanGoalByType(String numberCode , String type ){
        return planGoalSvc.editPlanGoalByType(numberCode, type);
    }
    
    @RequestMapping(value="editState")
    @ResponseBody
    public Map<String, Object> editState(String numberCode){
        return planGoalSvc.editState(numberCode);
    }
    
    @RequestMapping(value="editDelay")
    @ResponseBody
    public Map<String, Object> editDelay(String numberCode){
        return planGoalSvc.editDelay(numberCode);
    }
    
    @RequestMapping(value="deletePlanGoal")
    @ResponseBody
    public Map<String, Object> deletePlanGoal(String numberCodes) {
        return planGoalSvc.deletePlanGoal(numberCodes);
    }
    
}