package xyz.work.reserve.svc.imp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.JSON;
import xyz.filter.ReturnUtil;
import xyz.util.ConstantMsg;
import xyz.util.StringTool;
import xyz.util.UUIDUtil;
import xyz.work.reserve.model.Reserve;
import xyz.work.reserve.svc.ReserveSvc;
import xyz.work.security.model.Possessor_Relate;
import xyz.work.sell.model.OrderContent;
import xyz.work.sell.model.PersonInfo;
import xyz.work.taobao.model.TaobaoOrderTbo;
import xyz.work.taobao.model.TaobaoTravelItem;

@Service
public class ReserveSvcImp implements ReserveSvc {
    @Autowired
    private CommonDao commonDao;
    
    @Override
    public Map<String, Object> reserveLogin(String tid , String phone) {
        if(StringTool.isEmpty(tid)){
            return ReturnUtil.returnMap(0, ConstantMsg.reserve_tid_null);
        }
        if(StringTool.isEmpty(phone)){
            return ReturnUtil.returnMap(0, ConstantMsg.reserve_phone_null);
        }
        
        String hql = " from OrderContent ot where ot.source='TB'";
        hql += " and ot.tid = '"+ tid +"' and ot.linkPhone = '"+ phone +"'";
        Query countQuery = commonDao.getQuery("select count(*) "+hql);
        Number countNum = (Number)countQuery.uniqueResult();
        int count = countNum == null ? 0 : countNum.intValue();
        
        String reserveHql = "from Reserve r where r.tid = '"+ tid +"'";
        Query query = commonDao.getQuery("select count(*) "+reserveHql);
        Number queryCount = (Number)query.uniqueResult();
        int reserveCount = queryCount == null ? 0 : queryCount.intValue();
        
        Query reserveQuery = commonDao.getQuery(reserveHql);
        @SuppressWarnings("unchecked")
        List<Reserve> reserveList = reserveQuery.list();
        
        if(count > 0 && reserveCount == 0){
            return ReturnUtil.returnMap(1, null);
        }else if(count > 0 && reserveCount > 0){
            List<String> review = new ArrayList<String>();
            List<String> fail =  new ArrayList<String>();
            for(Reserve reserve : reserveList){
                int flag = reserve.getFlag();
                if(flag == 0){
                    review.add(reserve.getClientCode());
                }else if(flag == 1){
                    fail.add(reserve.getClientCode());
                }
            }
            
            int sum = review.size() + fail.size();
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("reviewList", review);
            map.put("failList", fail);
            map.put("passCount", sum);
            return ReturnUtil.returnMap(1, map);
        }
        
        return ReturnUtil.returnMap(0, ConstantMsg.reserve_login_null);
    }

    @Override
    public Map<String, Object> queryOrderListByTid(String tid,String notPass) {
        if(StringTool.isEmpty(tid)){
            return ReturnUtil.returnMap(0, ConstantMsg.reserve_number_null);
        }
        
        String taobaoSql = "SELECT t.title,t.sku_properties_name,t.num,t.tid,t.oid FROM taobao_order_tbo t";
        taobaoSql += " WHERE t.tid = '"+ tid +"'";
        @SuppressWarnings("unchecked")
        List<Object[]> taobaoList = commonDao.getSqlQuery(taobaoSql).list();
        
        String tbtSql = "SELECT tb.tid,tb.created FROM taobao_order_tbt tb";
        tbtSql += " WHERE tb.tid = '"+ tid +"'";
        @SuppressWarnings("unchecked")
        List<Object[]> tbtList = commonDao.getSqlQuery(tbtSql).list();
        
        String orderSql = "SELECT ot.shipment_travel_date,ot.volume,ot.tid,ot.oid,ot.person_info,ot.count FROM order_content ot";
        orderSql += " WHERE ot.source='TB' AND ot.tid = '"+ tid +"'";
        if(StringTool.isNotNull(notPass)){
            orderSql += " AND ot.client_code IN ("+ StringTool.StrToSqlString(notPass) +")";
        }
        @SuppressWarnings("unchecked")
        List<Object[]> orderList = commonDao.getSqlQuery(orderSql).list();
         
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("taobaoList", taobaoList);
        map.put("tbtList", tbtList);
        map.put("orderList", orderList);
        
        return ReturnUtil.returnMap(1,map);
    }

    @Override
    public Map<String, Object> savePersonData(String personJson) {
        if(StringTool.isEmpty(personJson)){
            return ReturnUtil.returnMap(0, "参数错误!");
        }
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> personList = JSON.toObject(personJson, ArrayList.class);
        
        String personStr = "";  //出行人字符串
        String oitStr = "";     //淘宝票单号字符串
        String possessor = "";  //机构
        for(int g = 0; g < personList.size(); g++){
            Map<String, Object> personMap = personList.get(g);
            String oid = personMap.get("oid")==null?"":personMap.get("oid").toString();
            String numberCode = personMap.get("numberCode")==null?"":personMap.get("numberCode").toString();
            String realName = personMap.get("realName")==null?"":personMap.get("realName").toString();
            String ename = personMap.get("ename")==null?"":personMap.get("ename").toString();
            String sex = personMap.get("sex")==null?"":personMap.get("sex").toString();
            String type = personMap.get("type")==null?"":personMap.get("type").toString();
            String passport = personMap.get("passport")==null?"":personMap.get("passport").toString();
            String nation = personMap.get("nation")==null?"":personMap.get("nation").toString();
            String expireDate = personMap.get("expireDate")==null?"":personMap.get("expireDate").toString();
            String card = personMap.get("card")==null?"":personMap.get("card").toString();
            String domicile = personMap.get("domicile")==null?"":personMap.get("domicile").toString();
            String birthday = personMap.get("birthday")==null?"":personMap.get("birthday").toString();
            if(g==0){
                String possessorSql = "SELECT * FROM possessor_relate pr WHERE pr.type='tkview'"; 
                possessorSql += " AND pr.relate = (SELECT ot.tkview FROM order_content ot WHERE ot.oid='"+ oid +"') ";
                
                SQLQuery query = commonDao.getSqlQuery(possessorSql);
                query.addScalar("iidd").addScalar("possessor").addScalar("type").addScalar("relate")
                .setResultTransformer(Transformers.aliasToBean(Possessor_Relate.class));
                @SuppressWarnings("unchecked")
                List<Possessor_Relate> possessorList = query.list();
                
                if(possessorList != null && possessorList.size() > 0){
                    possessor = possessorList.get(0).getPossessor();
                }
            }
            
            Date date = new Date();
            PersonInfo personObj = null;
            if(StringTool.isEmpty(numberCode)){
                personObj = new PersonInfo();
                personObj.setNumberCode(UUIDUtil.getUUIDStringFor32());
                personObj.setRealName(realName);
                personObj.setEname(ename);
                personObj.setSex(sex);
                personObj.setPersonType(type);
                personObj.setPassport(passport);
                personObj.setNation(nation);
                personObj.setExpireDate(expireDate);
                personObj.setCard(card);
                personObj.setDomicile(domicile);
                personObj.setBirthday(birthday);
                personObj.setPossessor(possessor);
                personObj.setAddDate(date);
                personObj.setAlterDate(date);
                commonDao.save(personObj);
            }else{
                personObj = (PersonInfo)commonDao.getObjectByUniqueCode("PersonInfo", "numberCode", numberCode);
                personObj.setRealName(realName);
                personObj.setEname(ename);
                personObj.setSex(sex);
                personObj.setPersonType(type);
                personObj.setPassport(passport);
                personObj.setNation(nation);
                personObj.setExpireDate(expireDate);
                personObj.setCard(card);
                personObj.setDomicile(domicile);
                personObj.setBirthday(birthday);
                personObj.setPossessor(possessor);
                personObj.setAlterDate(date);
                commonDao.update(personObj);
                
                Reserve reserve = (Reserve)commonDao.getObjectByUniqueCode("Reserve", "oid", oid);
                reserve.setChanges(1);
                reserve.setChangesRemark("出行人信息有改动,请审核!");
                commonDao.update(reserve);
            }
            
            if(StringTool.isEmpty(numberCode) && g==0){
                oitStr = oid;
                personStr = personStr + oid +":"+personObj.getNumberCode();
            }else if(numberCode == "" && g > 0){
                if(oitStr.equals(oid)){
                    personStr = personStr + "," + personObj.getNumberCode();
                }else{
                    oitStr = oid;
                    personStr = personStr + ";" + oid +":" + personObj.getNumberCode();
                }
            }
            
        }
        
        if(StringTool.isNotNull(personStr)){
            String[] personArr = personStr.split(";");
            for(int k = 0; k < personArr.length; k++){
                String personNumStr = personArr[k];
                String[] personNum = personNumStr.split(":");
                if(personNum != null && personNum.length > 0){
                    OrderContent orderObj = (OrderContent)commonDao.getObjectByUniqueCode("OrderContent", "oid", personNum[0]);
                    orderObj.setPersonInfo(personNum[1]);
                    commonDao.update(orderObj);
                    
                    Date addDate = new Date();
                    TaobaoTravelItem itemObj = (TaobaoTravelItem)commonDao.getObjectByUniqueCode("TaobaoTravelItem", "itemId", orderObj.getProduct());
                    TaobaoOrderTbo tboObj = (TaobaoOrderTbo)commonDao.getObjectByUniqueCode("TaobaoOrderTbo", "oid", orderObj.getOid());
                    
                    Reserve reserve = new Reserve();
                    reserve.setNumberCode(UUIDUtil.getUUIDStringFor32());
                    reserve.setOperator("");
                    reserve.setChannel(orderObj.getChannel());
                    reserve.setChannelNameCn(orderObj.getChannelNameCn());
                    reserve.setOrderNum(orderObj.getOrderNum());
                    reserve.setClientCode(orderObj.getClientCode());
                    reserve.setTid(orderObj.getTid());
                    reserve.setOid(orderObj.getOid());
                    reserve.setTkview(orderObj.getTkview());
                    reserve.setTkviewNameCn(orderObj.getTkviewNameCn());
                    reserve.setPossessor(possessor);
                    reserve.setAddDate(addDate);
                    reserve.setAlterDate(addDate);
                    reserve.setRemark("");
                    reserve.setItemId(itemObj.getItemId());
                    reserve.setItemNameCn(tboObj.getTitle());
                    reserve.setItemCount(tboObj.getNum());
                    reserve.setPersonInfo(orderObj.getPersonInfo());
                    String[] arry = personNum[1].split(",");
                    reserve.setPersonCount(arry.length);
                    reserve.setLinkMan(orderObj.getLinkMan());
                    reserve.setLinkPhone(orderObj.getLinkPhone());
                    reserve.setLinkEmail(orderObj.getEmail());
                    reserve.setFlag(0);
                    reserve.setFlagRemark("等待审核...");
                    reserve.setChanges(0);
                    commonDao.save(reserve);
                }
            }
        }
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> getPersonByNumberCode(String json) {
        if(StringTool.isEmpty(json)){
            return ReturnUtil.returnMap(0, "参数错误!");
        }
        
        @SuppressWarnings("unchecked")
        List<Map<String,Object>> list = JSON.toObject(json, ArrayList.class);
        String str = "";
        for(int p = 0; p < list.size(); p++){
            Map<String,Object> map = list.get(p);
            str += map.get("person").toString();
        }
        
        String personHql = "from PersonInfo p where p.numberCode in ("+ StringTool.StrToSqlString(str) +")";
        @SuppressWarnings("unchecked")
        List<PersonInfo> personList = commonDao.queryByHql(personHql);
        
        return ReturnUtil.returnMap(1,personList);
    }

}