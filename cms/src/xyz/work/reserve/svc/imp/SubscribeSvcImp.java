package xyz.work.reserve.svc.imp;


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
import xyz.util.ConstantMsg;
import xyz.util.StringTool;
import xyz.work.reserve.model.Reserve;
import xyz.work.reserve.svc.SubscribeSvc;
import xyz.work.sell.model.PersonInfo;


@Service
public class SubscribeSvcImp implements SubscribeSvc {
    @Autowired
    private CommonDao commonDao;

    @Override
    public Map<String, Object> queryReserveList(int offset , int pageSize , String  tkviewNameCn,
                                                String linkMan , int flag) {
        
        StringBuffer shipHql = new StringBuffer("from Reserve r where 1=1 ");
        {
            shipHql.append(" and r.possessor = '" + MyRequestUtil.getPossessor() + "'");
        }
        if (StringTool.isNotNull(tkviewNameCn)) {
            shipHql.append(" and r.tkviewNameCn like '%" + tkviewNameCn + "%'");
        }
        if(StringTool.isNotNull(linkMan)){
            shipHql.append(" and r.linkMan like '%" + linkMan +"%'");
        }
        if(flag != -1){
            shipHql.append(" and r.flag = '" + flag +"'");
        }
        shipHql.append(" order by r.alterDate desc");

        Query countQuery = commonDao.getQuery("select count(*) " + shipHql.toString());
        Number countNum = (Number)countQuery.uniqueResult();
        int count = countNum == null ? 0 : countNum.intValue();

        Query query = commonDao.getQuery(shipHql.toString());
        query.setMaxResults(pageSize);
        query.setFirstResult(offset);
        @SuppressWarnings("unchecked")
        List<Reserve> list = query.list();
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", count);
        map.put("rows", list);
        
        return ReturnUtil.returnMap(1, map);
    }

    @Override
    public Map<String, Object> editReserveFlagByNumberCode(String numberCode , int flag , String flagRemark ,
                                           String remark) {
        if(StringTool.isEmpty(numberCode)){
            ReturnUtil.returnMap(0, ConstantMsg.reserve_number_null);
        }
        if(flag < 0){
            ReturnUtil.returnMap(0, ConstantMsg.reserve_flag_error);
        }
        
        Reserve reserveObj = (Reserve)commonDao.getObjectByUniqueCode("Reserve", "numberCode", numberCode);
        reserveObj.setOperator(MyRequestUtil.getUsername());
        reserveObj.setChanges(0);
        reserveObj.setFlag(flag);
        reserveObj.setFlagRemark(flagRemark);
        reserveObj.setRemark(remark);
        reserveObj.setAlterDate(new Date());
        commonDao.update(reserveObj);
        
        return ReturnUtil.returnMap(1,null);
    }

    @Override
    public Map<String, Object> getPersonListByReserve(String reserve) {
        if(StringTool.isEmpty(reserve)){
            ReturnUtil.returnMap(0, ConstantMsg.reserve_number_null);
        }
        Reserve reserveObj = (Reserve)commonDao.getObjectByUniqueCode("Reserve", "numberCode", reserve);
        if(reserveObj == null){
            ReturnUtil.returnMap(0, ConstantMsg.reserve_error);
        }
            
        String hql = "from PersonInfo where numberCode in ("+ StringTool.StrToSqlString(reserveObj.getPersonInfo()) +")";
        @SuppressWarnings("unchecked")
        List<PersonInfo> personList = commonDao.queryByHql(hql);
        
        return ReturnUtil.returnMap(1,personList);
    }

}