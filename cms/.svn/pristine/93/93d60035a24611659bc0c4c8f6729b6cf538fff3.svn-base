package xyz.work.meituan.ctrl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.work.meituan.svc.MeituanSvc;

@Controller
@RequestMapping(value = "/MeituanWS")
public class MeituanWS {
    @Autowired
    MeituanSvc meituanSvc;
    
    /**
     * 查询确认审批列表
     * @param page
     * @param rows
     * @param mtOrderId
     * @param orderId
     * @return Map<String,Object>
     * @author :huying
     * @date : 2017-6-21上午10:35:44
     */
    @RequestMapping(value="queryConfirmApprovalList")
    @ResponseBody
    public Map<String,Object> queryConfirmApprovalList(
        int page,
        int rows,
        String mtOrderId){
        int pageSize=rows;
        int offset=(page-1)*rows;
        return meituanSvc.queryConfirmApprovalList(pageSize, offset, mtOrderId);
    }
    
    
    /**
     * 接口，供美团项目调用
     * @param data
     * @return Map<String,Object>
     * @author :huying
     * @date : 2017-6-20下午3:34:48
     */
    @RequestMapping(value="addAffirmApproval")
    @ResponseBody
    public Map<String,Object> addAffirmApproval(String data){
        return meituanSvc.addAffrimApprol(data);
    }
    
    
    /**
     * 确认订单审批
     * @param mtOrderid 美团订单编号
     * @param code 审批结果
     * @return Map<String,Object>
     * @author :huying
     * @date : 2017-6-20下午3:35:10
     */
    @RequestMapping(value="affirmApprol")
    @ResponseBody
    public Map<String,Object> affirmApprol(String mtOrderid,int code){
        return meituanSvc.affrimApprolOper(mtOrderid, code);
    }
    
}
