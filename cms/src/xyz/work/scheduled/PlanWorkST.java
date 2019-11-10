package xyz.work.scheduled;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import xyz.work.scheduled.svc.PlanWorkSTSvc;


/**
 * 计划任务类
 * 
 * @author Administrator
 */
@Component
public class PlanWorkST {

    @Autowired
    private PlanWorkSTSvc planWorkSTSvc;

    /**
     * 排班表自动运行
     */
    public int autoPlanWork() {
        return planWorkSTSvc.autoPlanWorkOper();
    }

    /**
     * 单品库存
     */
    public int stockAutoPlanWork() {
        return planWorkSTSvc.stockAutoPlanWorkOper();
    }
    
    /**
     * 单品释放
     */
    public int tkviewPlanWorkOper(){
        return planWorkSTSvc.tkviewPlanWorkOper();
    }

	public int royalDataPlanWorkOper() {
		 return planWorkSTSvc.royalDataPlanWorkOper();
	}
	
	/**
     * 表备份计划任务
     */
	public int tablePlanWorkOper(){
	    return planWorkSTSvc.tablePlanWorkOper();
	}

}