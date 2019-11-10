package xyz.work.meituan.svc.Config;

import java.util.HashMap;
import java.util.Map;

import xyz.filter.JSON;
import xyz.filter.RmiUtil;
import xyz.filter.SysPropertyTool;

public class MeituanUtil {
    //确认审批路径1
    private final static String affrimApprolUrl="/MeituanCmsInterfaWS/affrimApprovl.api";
    
    public final static String partnerid="753";//美团项目提供的供应商编号，这里统一用美团分配的供应商id,便于管理
    
    /**
     * 确认审批
     * @param mtOrderid 美团订单编号
     * @param code 审批结果 -1 无库存 1库存充足
     * @return Map<String,Object>
     * @author :huying
     * @date : 2017-6-21上午10:13:58
     */
    @SuppressWarnings("unchecked")
    public static Map<String,Object> affrimApprolSend(String mtOrderid,int code){
        Map<String,Object> data=new HashMap<String,Object>();
        data.put("partnerid", partnerid);
        data.put("mtOrderid", mtOrderid);
        data.put("code", code);
        Map<String,String> accessoryParam=new HashMap<String,String>();
        accessoryParam.put("data",JSON.toJson(data));
        return (Map<String,Object>)new RmiUtil().loadData(SysPropertyTool.getXmlValue("meituanUrl")+affrimApprolUrl, accessoryParam);
    }

}