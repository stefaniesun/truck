package xyz.work.goal.svc.imp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.MyRequestUtil;
import xyz.filter.ReturnUtil;
import xyz.util.StringTool;
import xyz.util.UUIDUtil;
import xyz.work.core.model.LogWork;
import xyz.work.goal.model.Person;
import xyz.work.goal.model.PlanGoal;
import xyz.work.goal.svc.PlanGoalSvc;

@Service
public class PlanGoalSvcImp implements PlanGoalSvc {
    @Autowired
    private CommonDao commonDao;

    @Override
    public Map<String, Object> queryPlanGoalList(int offset , int pageSize , String goal ,
                                                 String type , String person , String sort ,
                                                 String order) {
         
        String hql = "from PlanGoal p where 1=1 ";
        if(StringTool.isNotNull(goal)){
            hql += "and p.goal like '%"+ goal +"%' ";
        }
        if(StringTool.isNotNull(type)){
            hql += "and p.type = '"+ type +"' ";
        }
        if(StringTool.isNotNull(person)){
            hql += "and p.person like '%"+ person +"%' ";
        }
        if(sort != null && !"".equals(sort) && order != null && !"".equals(order)){
            if(",state,delay,endTime,alterDate,".contains("," + sort+ ",") && ",asc,desc,".contains("," + order + ",")){
                hql += "order by p." + sort + " " + order ;
            } 
        }else{
            hql += "order by p.delay desc,p.state desc,p.alterDate desc ";
        }
        
        Query countQuery = commonDao.getQuery("select count(*) " + hql);
        Number countNum = (Number)countQuery.uniqueResult();
        int count = countNum == null ? 0 : countNum.intValue();

        Query query = commonDao.getQuery(hql.toString());
        query.setMaxResults(pageSize);
        query.setFirstResult(offset);
        @SuppressWarnings("unchecked")
        List<PlanGoal> list = query.list();
        
        @SuppressWarnings("unchecked")
        List<Person> personList = commonDao.queryByHql("from Person");
        for(PlanGoal plan : list){
            for(Person per : personList){
                if(StringTool.isNotNull(plan.getFounder()) && plan.getFounder().equals(per.getNumberCode())){
                    plan.setFounderName(per.getNameCn());
                    break;
                }
            }
            for(Person per : personList){
                if(StringTool.isNotNull(plan.getPerson()) && plan.getPerson().equals(per.getNumberCode())){
                    plan.setPersonName(per.getNameCn());
                    break;
                }
            }
        }
        
        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", count);
        mapContent.put("rows", list);

        return ReturnUtil.returnMap(1, mapContent);
    }

    @Override
    public Map<String, Object> addPlanGoal(String type , String goal , String founder,
                                           String person , Date endTime , int state, int delay ,
                                           String remark) {
        
        if(StringTool.isEmpty(goal)){
            return ReturnUtil.returnMap(0, "计划目标不能为空!");
        }
        if(StringTool.isEmpty(person)){
            return ReturnUtil.returnMap(0, "请指定计划目标负责人!");
        }
        
        Date addDate = new Date();
        PlanGoal goalObj = new PlanGoal();
        goalObj.setNumberCode(UUIDUtil.getUUIDStringFor32());
        goalObj.setType(type);
        goalObj.setState(state);
        goalObj.setGoal(goal);
        goalObj.setFounder(founder);
        goalObj.setPerson(person);
        goalObj.setEndTime(endTime);
        goalObj.setDelay(delay);
        goalObj.setAddDate(addDate);
        goalObj.setAlterDate(addDate);
        goalObj.setRemark(remark);
        commonDao.save(goalObj);
        
        LogWork logWork = new LogWork();
        logWork.setAddDate(new Date());
        logWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
        logWork.setValue(goalObj.getNumberCode());
        logWork.setTableName("goal");
        logWork.setRemark("新增计划目标【"+ goal +"】");
        logWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
        commonDao.save(logWork);
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> editPlanGoal(String numberCode , String type , String goal , String founder, String person ,
                                            Date endTime , int state, int delay , String remark) {
        
        if(StringTool.isEmpty(numberCode)){
            return ReturnUtil.returnMap(0, "计划目标选择错误!");
        }
        if(StringTool.isEmpty(goal)){
            return ReturnUtil.returnMap(0, "计划目标不能为空!");
        }
        if(StringTool.isEmpty(person)){
            return ReturnUtil.returnMap(0, "请指定计划目标负责人!");
        }
        
        PlanGoal goalObj = (PlanGoal)commonDao.getObjectByUniqueCode("PlanGoal", "numberCode", numberCode);
        if(goalObj == null){
            return ReturnUtil.returnMap(0, "计划目标不存在!");
        }
        goalObj.setType(type);
        goalObj.setState(state);
        goalObj.setGoal(goal);
        goalObj.setFounder(founder);
        goalObj.setPerson(person);
        goalObj.setEndTime(endTime);
        goalObj.setDelay(delay);
        goalObj.setAlterDate(new Date());
        goalObj.setRemark(remark);
        commonDao.update(goalObj);
        
        LogWork logWork = new LogWork();
        logWork.setAddDate(new Date());
        logWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
        logWork.setValue(goalObj.getNumberCode());
        logWork.setTableName("goal");
        logWork.setRemark("编辑计划目标【"+ goal +"】");
        logWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
        commonDao.save(logWork);
        
        return ReturnUtil.returnMap(1, null);
    }
    
    @Override
    public Map<String, Object> editPlanGoalByType(String numberCode , String type) {
        
        if(StringTool.isEmpty(numberCode)){
            return ReturnUtil.returnMap(0, "计划目标选择错误!");
        }
        
        PlanGoal goalObj = (PlanGoal)commonDao.getObjectByUniqueCode("PlanGoal", "numberCode", numberCode);
        if(goalObj == null){
            return ReturnUtil.returnMap(0, "计划目标不存在!");
        }
        goalObj.setType(type);
        commonDao.update(goalObj);
        
        LogWork logWork = new LogWork();
        logWork.setAddDate(new Date());
        logWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
        logWork.setValue(goalObj.getNumberCode());
        logWork.setTableName("goal");
        logWork.setRemark("编辑计划目标状态");
        logWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
        commonDao.save(logWork);
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> editState(String numberCode){
        
        if (StringTool.isEmpty(numberCode)) {
            return ReturnUtil.returnMap(0, "计划目标选择错误!");
        }
        
        PlanGoal goalObj = (PlanGoal)commonDao.getObjectByUniqueCode("PlanGoal", "numberCode", numberCode);
        if (goalObj == null) {
            return ReturnUtil.returnMap(0, "该计划目标已经不存在了!");
        }
        int state = goalObj.getState();
        state = state==0?1:0;
        goalObj.setState(state);
        commonDao.update(goalObj);

        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> editDelay(String numberCode) {
        
        if (StringTool.isEmpty(numberCode)) {
            return ReturnUtil.returnMap(0, "计划目标选择错误!");
        }
        
        PlanGoal goalObj = (PlanGoal)commonDao.getObjectByUniqueCode("PlanGoal", "numberCode", numberCode);
        if (goalObj == null) {
            return ReturnUtil.returnMap(0, "该计划目标已经不存在了!");
        }
        int delay = goalObj.getDelay();
        delay = delay==0?1:0;
        goalObj.setDelay(delay);
        commonDao.update(goalObj);

        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> deletePlanGoal(String numberCodes) {
        
        if(StringTool.isEmpty(numberCodes)) {
            return ReturnUtil.returnMap(0, "计划目标选择错误!");
        }
        
        String sql = "DELETE FROM plan_goal WHERE number_code IN ("+ StringTool.StrToSqlString(numberCodes) + ")";
        commonDao.getSqlQuery(sql).executeUpdate();
        
        return ReturnUtil.returnMap(1, null);
    }

}