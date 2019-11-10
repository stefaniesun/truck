package xyz.work.r_base.svc.imp;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;

import xyz.filter.ReturnUtil;
import xyz.util.UUIDUtil;
import xyz.work.core.model.PlanWork;
import xyz.work.r_base.model.R_Task;
import xyz.work.r_base.svc.R_TaskSvc;
import xyz.work.r_base.svc.R_TkviewSvc;

@Service
public class R_TaskSvcImp implements R_TaskSvc {
	
	@Autowired
	CommonDao commonDao;
	
    @Autowired
    R_TkviewSvc r_TkviewSvc;

    @Override
    public Map<String, Object> queryTaskList(int offset , int pageSize ) {
        String hql = "from R_Task order by startTime desc";
        Query countQuery = commonDao.getQuery("select count(*) " + hql);
        Number countNum = (Number)countQuery.uniqueResult();
        int count = countNum == null ? 0 : countNum.intValue();

        Query query = commonDao.getQuery(hql);
        query.setMaxResults(pageSize);
        query.setFirstResult(offset);
        @SuppressWarnings("unchecked")
        List<R_Task> list = query.list();
        
        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", count);
        mapContent.put("rows", list);

        return ReturnUtil.returnMap(1, mapContent);
    }

	@Override
	public Map<String, Object> manualUpdateDataOper() {
		
		R_Task task=new R_Task();
		
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		
		String numberCode="T"+format.format(new Date());
		
		task.setNumberCode(numberCode);
		task.setStartTime(new Date());
		task.setAuto(0);
		
		commonDao.save(task);
		
		PlanWork planWork=new PlanWork();
		
		planWork.setContent(numberCode);
		planWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
		planWork.setWorkType("ppf");
		planWork.setAddDate(new Date());
		planWork.setAlterDate(new Date());
		
		commonDao.save(planWork);
		
		 return ReturnUtil.returnMap(1, null);
	}

}