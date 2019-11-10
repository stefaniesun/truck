package xyz.work.scheduled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * 计划任务启动类
 * @author Administrator
 *
 */
@Component
public class JobMain {
	
    @Autowired
    private TaobaoST taobaoST;
    
    @Autowired
    private PlanWorkST planWorkST;
    
	private Logger log = LoggerFactory.getLogger(JobMain.class);
	
    //@Scheduled(fixedDelay=1*60*1000)
	@Scheduled(cron="0 0 * * * ?")
    public void autoGetTaobao1111Data_1(){
        log.info("");
    }
	
	@Scheduled(fixedDelay=1*5*1000)
    public void autoPlanWork(){
        //System.out.print("开始尝试排班任务---"+Thread.currentThread().getName());
        int t = planWorkST.autoPlanWork();
        if(t==1){
            log.info("实施了一项排班任务！");
        }
    }
	
    
    //每天晚上12点，中午12点执行一次
    @Scheduled(cron="0 0 0,12 * * ?")
    public void stockAutoPlanWorkOper(){
        int t = planWorkST.stockAutoPlanWork();
        if(t == 1){
            log.info("执行单品库存计划任务!");
        }
    }
    
    //每天晚上12点，中午12点执行一次  同步皇家邮轮数据
    /*@Scheduled(cron="0 0 0,12 * * ?")
    public void royalDataPlanWorkOper(){
        int t = planWorkST.royalDataPlanWorkOper();
        if(t == 1){
            log.info("执行单品库存计划任务!");
        }
    }*/
    
    
    //单品释放
    //@Scheduled(cron="0 0/10 0 * * ?")
    @Scheduled(fixedDelay=10*60*1000)
    public void tkviewPlanWorkOper(){
        int t = planWorkST.tkviewPlanWorkOper();
        if(t == 1){
            log.info("执行单品计划任务!");
        }
    }
    
    //表备份计划任务  
  /*  @Scheduled(cron="0 0 2 * * ?")
    public void tablePlanWorkOper(){
        int t = planWorkST.tablePlanWorkOper();
        if(t == 1){
            log.info("执行表备份计划任务!");
        }
    }*/
    
}