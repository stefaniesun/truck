package xyz.work.meituan.svc.imp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.JSON;
import xyz.filter.ReturnUtil;
import xyz.util.Constant;
import xyz.work.meituan.model.ConfirmApproval;
import xyz.work.meituan.svc.MeituanSvc;
import xyz.work.meituan.svc.Config.MeituanUtil;

@Service
public class MeituanSvcImp implements MeituanSvc{
    @Autowired
    CommonDao commonDao;

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> addAffrimApprol(String data) {
        if(data==null){
            return ReturnUtil.returnMap(0, "参数不能为空");
        }
        Map<String,Object> data_map=JSON.toObject(JSON.toJson(data), Map.class);
        Object mtOrderidObj=data_map.get("mtOrderid");
        if(mtOrderidObj==null){
            return ReturnUtil.returnMap(0, "参数为空mtOrderid");
        }
        String mtOrderid =mtOrderidObj.toString();
        
        Object totalPriceObj=data_map.get("totalPrice");
        if(totalPriceObj==null){
            return ReturnUtil.returnMap(0, "参数为空totalPrice");
        }
        BigDecimal totalPrice=new BigDecimal(totalPriceObj.toString()); 
        
        String mtProductId=data_map.get("mtProductId")==null?"":data_map.get("mtProductId").toString();
        
        String product=data_map.get("product")==null?"":data_map.get("product").toString();
        Object skuObj=data_map.get("sku");
        if(skuObj==null){
            return ReturnUtil.returnMap(0, "参数为空sku");
        }
        String sku=skuObj.toString();
        
        Object statusObj=data_map.get("status");
        if(statusObj==null){
            return ReturnUtil.returnMap(0, "参数为空status");
        }
        
        int quantity= data_map.get("quantity")==null?0:Integer.parseInt(data_map.get("quantity").toString());
        String contactName=data_map.get("contactName")==null?"":data_map.get("contactName").toString();
        String contactPhone=data_map.get("contactPhone")==null?"":data_map.get("contactPhone").toString();
        String travelDate=data_map.get("travelDate")==null?"":data_map.get("travelDate").toString();
          
        
        
        int status=Integer.parseInt(statusObj.toString());
        ConfirmApproval approval=new ConfirmApproval();
        approval.setMtOrderid(mtOrderid);
        approval.setMtProductId(mtProductId);
        approval.setAddDate(new Date());
        approval.setAlterDate(new Date());
        approval.setSku(sku);
        approval.setTotalPrice(totalPrice);
        approval.setProduct(product);
        approval.setStatus(status);
        approval.setQuantity(quantity);
        approval.setContactName(contactName);
        approval.setContactPhone(contactPhone);
        approval.setTravelDate(travelDate);
        commonDao.save(approval);
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> affrimApprolOper(String mtOrderid , int code) {
        ConfirmApproval approval=(ConfirmApproval)commonDao.getObjectByUniqueCode("ConfirmApproval", "mtOrderid", mtOrderid);
        if(approval==null){
            return ReturnUtil.returnMap(0, "该确认单不存在");
        }else{
            
            Map<String,Object> response=MeituanUtil.affrimApprolSend(mtOrderid,code);
            int status=Integer.parseInt(response.get(Constant.result_status).toString());
            if(status==1){
                if(code==1){
                    approval.setStatus(1);
                }else{
                    approval.setStatus(-1);
                }
                approval.setAlterDate(new Date());
                commonDao.update(approval);
                return ReturnUtil.returnMap(1,"确认成功");
            }else{
                return ReturnUtil.returnMap(0, response.get(Constant.result_msg).toString());
            }
        }
        
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> queryConfirmApprovalList(int pageSize , int offset ,
                                                        String mtOrderId) {
        String hql="from ConfirmApproval c where 1=1";
        if(mtOrderId!=null&&!"".equals(mtOrderId)){
            hql+=" and c.mtOrderid='"+mtOrderId+"'";
        }
        hql+=" order by c.addDate desc";
        Query query=commonDao.getQuery(hql);
        query.setFirstResult(offset);
        query.setMaxResults(pageSize);
        List<ConfirmApproval> confirmApprovalList=query.list();
        
        String countHql=" select count(iidd) "+hql;
        Number number=(Number)commonDao.queryUniqueByHql(countHql);
        int count=number.intValue();
        
        Map<String,Object> resultMap=new HashMap<String,Object>();
        resultMap.put("total", count);
        resultMap.put("rows", confirmApprovalList);
        return ReturnUtil.returnMap(1, resultMap);
    }

}


