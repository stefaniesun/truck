package xyz.work.resources.svc.imp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.ReturnUtil;
import xyz.util.StringTool;
import xyz.util.UUIDUtil;
import xyz.work.resources.model.Joiner;
import xyz.work.resources.model.Provider;
import xyz.work.resources.svc.JoinerSvc;

@Service
public class JoinerSvcImp implements JoinerSvc {
    @Autowired
    private CommonDao commonDao;

    @Override
    public Map<String, Object> queryJoinerList(int offset , int pageSize , String nameCn ,
                                               String phone , String weChat , String provider) {
       String hql = "from Joiner j where 1=1 ";
       if(StringTool.isNotNull(provider)){
           hql += "and j.provider = '"+ provider +"' ";
       }
       if(StringTool.isNotNull(nameCn)){
           hql += "and j.nameCn like '%"+ nameCn +"%' ";
       }
       if(StringTool.isNotNull(phone)){
           hql += "and j.phone like '%"+ phone +"%' ";
       }
       if(StringTool.isNotNull(weChat)){
           hql += "and j.weChat like '%"+ weChat +"%' ";
       }
       hql += "order by j.alterDate ";
       
       Query countQuery = commonDao.getQuery("select count(*) " + hql.toString());
       Number countNum = (Number)countQuery.uniqueResult();
       int count = countNum == null ? 0 : countNum.intValue();

       Query query = commonDao.getQuery(hql.toString());
       query.setMaxResults(pageSize);
       query.setFirstResult(offset);
       @SuppressWarnings("unchecked")
       List<Joiner> list = query.list();
       
       String providerHql = "from Provider ";
       @SuppressWarnings("unchecked")
       List<Provider> providerList = commonDao.queryByHql(providerHql);
       for(Joiner joiner : list){
           for(Provider p : providerList){
               if(joiner.getProvider().equals(p.getNumberCode())){
                   joiner.setProviderNameCn(p.getNameCn());
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
    public Map<String, Object> addJoiner(String nameCn , String phone , String weChat ,
                                         String provider , String qq , String remark) {
        if(StringTool.isEmpty(nameCn)){
            return ReturnUtil.returnMap(0, "对接人姓名不能为空!");
        }
        /*if(StringTool.isEmpty(phone)){
            return ReturnUtil.returnMap(0, "对接人电话不能为空!");
        }
        if(StringTool.isEmpty(weChat)){
            return ReturnUtil.returnMap(0, "对接人微信号不能为空!");
        }
        if(StringTool.isEmpty(qq)){
            return ReturnUtil.returnMap(0, "对接人QQ号不能为空!");
        }*/
        
        Date date = new Date();
        
        Joiner joinerObj = new Joiner();
        joinerObj.setNumberCode(UUIDUtil.getUUIDStringFor32());
        joinerObj.setNameCn(nameCn);
        joinerObj.setPhone(phone);
        joinerObj.setWeChat(weChat);
        joinerObj.setProvider(provider);
        joinerObj.setRemark(remark);
        joinerObj.setQq(qq);
        joinerObj.setAddDate(date);
        joinerObj.setAlterDate(date);
        commonDao.save(joinerObj);
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> editJoiner(String numberCode , String nameCn , String phone ,
                                          String weChat , String provider , String qq , String remark) {
        if(StringTool.isEmpty(nameCn)){
            return ReturnUtil.returnMap(0, "对接人姓名不能为空!");
        }
        /*if(StringTool.isEmpty(phone)){
            return ReturnUtil.returnMap(0, "对接人电话不能为空!");
        }
        if(StringTool.isEmpty(weChat)){
            return ReturnUtil.returnMap(0, "对接人微信号不能为空!");
        }
        if(StringTool.isEmpty(qq)){
            return ReturnUtil.returnMap(0, "对接人QQ号不能为空!");
        }*/
        
        Joiner joinerObj = (Joiner)commonDao.getObjectByUniqueCode("Joiner", "numberCode", numberCode);
        joinerObj.setNameCn(nameCn);
        joinerObj.setPhone(phone);
        joinerObj.setWeChat(weChat);
        joinerObj.setProvider(provider);
        joinerObj.setQq(qq);
        joinerObj.setRemark(remark);
        joinerObj.setAlterDate(new Date());
        commonDao.update(joinerObj);
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> deleteJoiner(String numberCodes) {
        if(StringTool.isEmpty(numberCodes)){
            return ReturnUtil.returnMap(0, "请选择要删除的对接人信息!");
        }
        
        String sql = "DELETE FROM joiner WHERE number_code IN ("+ StringTool.StrToSqlString(numberCodes) +") ";
        commonDao.getSqlQuery(sql).executeUpdate();
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> queryJoinerListByprovider(String provider) {
        if(StringTool.isEmpty(provider)){
            return ReturnUtil.returnMap(0, "供应商选择错误!");
        }
        
        String hql = "from Joiner a where 1=1 ";
        hql += "and a.provider = '"+ provider +"' ";
        hql += "order by a.alterDate ";
        @SuppressWarnings("unchecked")
        List<Joiner> list = commonDao.queryByHql(hql);
        
        Provider providerObj = (Provider)commonDao.getObjectByUniqueCode("Provider", "numberCode", provider);
        for(Joiner joiner : list){
            joiner.setProviderNameCn(providerObj.getNameCn());
        }

        return ReturnUtil.returnMap(1, list);
    }

}