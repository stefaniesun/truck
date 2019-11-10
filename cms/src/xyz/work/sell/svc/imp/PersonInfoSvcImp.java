package xyz.work.sell.svc.imp;


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
import xyz.work.security.model.Possessor;
import xyz.work.sell.model.PersonInfo;
import xyz.work.sell.svc.PersonInfoSvc;


@Service
public class PersonInfoSvcImp implements PersonInfoSvc {
    @Autowired
    CommonDao commonDao;

    @Override
    public Map<String, Object> queryPersonInfoList(String numberCodes) {
        
        String possessor = MyRequestUtil.getPossessor();
        
        String hql = "from PersonInfo p where p.numberCode in (" + StringTool.StrToSqlString(numberCodes) + ") ";
        hql += " and p.possessor = '"+possessor+"'";
        
        @SuppressWarnings("unchecked")
        List<PersonInfo> personInfoList = commonDao.getQuery(hql).list();

        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("rows", personInfoList);
        return ReturnUtil.returnMap(1, mapContent);
    }

    @Override
    public Map<String, Object> addPersonInfo(String realName , String ename , String card ,
                                             String birthday , String card2 , String expireDate ,
                                             String hjd , String sex , String nation ,
                                             String personType) {
        String possessor = MyRequestUtil.getPossessor();
        Possessor possessorObj = (Possessor)commonDao.getObjectByUniqueCode("Possessor", "numberCode", possessor);
        String possessorNameCn = "";
        if(possessorObj != null){
            possessorNameCn = possessorObj.getNameCn();
        }
        String numberCode = UUIDUtil.getUUIDStringFor32();
        Date date = new Date();
        
        PersonInfo personInfo = new PersonInfo();
        personInfo.setNumberCode(numberCode);
        personInfo.setRealName(realName);
        personInfo.setEname(ename);
        personInfo.setCard(card);
        personInfo.setBirthday(birthday);
        personInfo.setPassport(card2);
        personInfo.setExpireDate(expireDate);
        personInfo.setDomicile(hjd);
        personInfo.setSex(sex);
        personInfo.setNation(nation);
        personInfo.setPersonType(personType);
        personInfo.setPossessor(possessor);
        personInfo.setPossessorNameCn(possessorNameCn);
        personInfo.setAddDate(date);
        personInfo.setAlterDate(date);
        commonDao.save(personInfo);

        return ReturnUtil.returnMap(1, numberCode);
    }

    @Override
    public Map<String, Object> editPersonInfo(String numberCode , String ename , String card ,
                                              String birthday , String card2 , String expireDate ,
                                              String hjd , String sex , String nation ,
                                              String personType) {
        if (numberCode == null || "".equals(numberCode)) {
            return ReturnUtil.returnMap(0, "参数错误!");
        }

        PersonInfo personInfo = (PersonInfo)commonDao.getObjectByUniqueCode("PersonInfo",
            "numberCode", numberCode);
        if (personInfo == null) {
            ReturnUtil.returnMap(0, "参数错误!");
        }
        personInfo.setEname(ename);
        personInfo.setCard(card);
        personInfo.setBirthday(birthday);
        personInfo.setPassport(card2);
        personInfo.setExpireDate(expireDate);
        personInfo.setDomicile(hjd);
        personInfo.setSex(sex);
        personInfo.setNation(nation);
        personInfo.setPersonType(personType);
        personInfo.setAlterDate(new Date());
        commonDao.update(personInfo);

        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> getPersonInfoList(String q) {
        
        String possessor = MyRequestUtil.getPossessor();
        
        StringBuffer hql = new StringBuffer();
        hql.append("from PersonInfo t  WHERE 1 = 1");
        hql.append(" and t.possessor = '"+possessor+"'");
        if (q != null && !"".equals(q)) {
            String[] strs = q.split(" ");
            for (int i = 0; i < strs.length; i++ ) {
                if (!"".equals(strs[i].trim())) {
                    hql.append(" AND CONCAT( IFNULL(t.realName, ''),IFNULL(t.ename, ''),IFNULL(t.card, ''), IFNULL(t.card2, '')) LIKE '%"
                               + strs[i] + "%'");
                }
            }
        }

        Query query = commonDao.getQuery(hql.toString());
        query.setFirstResult(0);
        query.setMaxResults(20);

        @SuppressWarnings("unchecked")
        List<PersonInfo> personInfoList = query.list();

        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("rows", personInfoList);

        return ReturnUtil.returnMap(1, mapContent);
    }
}