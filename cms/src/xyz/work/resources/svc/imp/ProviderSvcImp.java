package xyz.work.resources.svc.imp;


import java.util.ArrayList;
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
import xyz.util.ConstantMsg;
import xyz.util.DateUtil;
import xyz.util.ExcelTool;
import xyz.util.PossessorUtil;
import xyz.util.StringTool;
import xyz.util.StringUtil;
import xyz.util.UUIDUtil;
import xyz.work.resources.model.Account;
import xyz.work.resources.model.Joiner;
import xyz.work.resources.model.Provider;
import xyz.work.resources.svc.ProviderSvc;


@Service
public class ProviderSvcImp implements ProviderSvc {

    @Autowired
    CommonDao commonDao;
    
    @Autowired
    PossessorUtil possessorUtil;

    @Override
    public Map<String, Object> queryProviderList(int offset, int pagesize, String nameCn,
                                                 String mark, String queryJson) {

        StringBuffer hql = new StringBuffer();
        hql.append("from Provider p where 1 = 1 ");
        hql.append(possessorUtil.getRelatesWhereHql(Constant.provider_type));
        if (StringUtil.yqqQueryExists(queryJson, Constant.stock_name_cn)) {
            hql.append(" and p.nameCn like '%" + StringUtil.yqqQueryString(queryJson, Constant.stock_name_cn) + "%'");
        }
        if (StringUtil.yqqQueryExists(queryJson, Constant.nulo)) {
            hql.append(" and p.mark = '" + StringUtil.yqqQueryString(queryJson, Constant.nulo) + "'");
        }
        if (StringUtil.yqqQueryExists(queryJson, Constant.stock_weChat)) {
            hql.append(" and p.weChat like '%"+StringUtil.yqqQueryString(queryJson, Constant.stock_weChat)+"%'");
        }
        if (StringUtil.yqqQueryExists(queryJson, Constant.stock_talkGroup)) {
            hql.append(" and p.talkGroup like '%"+StringUtil.yqqQueryString(queryJson, Constant.stock_talkGroup)+"%'");
        }
        if (StringUtil.yqqQueryExists(queryJson, Constant.stock_joiner)) {
            hql.append(" and p.joiner like '%" + StringUtil.yqqQueryString(queryJson, Constant.stock_joiner) + "%'");
        }
        if (StringUtil.yqqQueryExists(queryJson, Constant.stock_status)) {
            hql.append(" and p.isResponsible in (" + StringUtil.yqqQueryString(queryJson, Constant.stock_status) + ")");
        }
        if (StringTool.isNotNull(mark)) {
            hql.append(" and p.mark = '" + mark + "' ");
        }
        
        hql.append(" order by p.alterDate desc");

        Query countQuery = commonDao.getQuery("select count(*) " + hql.toString());
        Number countNum = (Number)countQuery.uniqueResult();
        int count = countNum == null ? 0 : countNum.intValue();

        Query query = commonDao.getQuery(hql.toString());
        query.setMaxResults(pagesize);
        query.setFirstResult(offset);

        @SuppressWarnings("unchecked")
        List<Provider> list = query.list();
        
        @SuppressWarnings("unchecked")
        List<Account> accountList = commonDao.queryByHql("from Account");
        
        @SuppressWarnings("unchecked")
        List<Joiner> joinerList = commonDao.queryByHql("from Joiner");
        
        for(Provider p : list){
            int accountSum = 0;
            int joinerSum = 0;
            for(Account a : accountList){
                if(p.getNumberCode().equals(a.getProvider())){
                    accountSum++;
                }
            }
            for(Joiner j : joinerList){
                if(p.getNumberCode().equals(j.getProvider())){
                    joinerSum++;
                }
            }
            p.setAccountSum(accountSum+"");
            p.setJoinerSum(joinerSum+"");
        }
 
        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", count);
        mapContent.put("rows", list);

        return ReturnUtil.returnMap(1, mapContent);
    }

    @Override
    public Map<String, Object> addProvider(String mark , String nameCn , String address , 
                                           String weChat , String talkGroup , int isResponsible ,
                                           String sign , String settlement , String policy ,
                                           int status , String remark , int grade) {

        String existSql = "select name_cn from provider where name_cn = '"+nameCn+"' ";
        existSql += possessorUtil.getRelatesWhereSql(Constant.relate_type_provider);
        
        @SuppressWarnings("unchecked")
        List<String> existList = commonDao.getSqlQuery(existSql).list();
        if (existList.size() >= 1) {
            return ReturnUtil.returnMap(0, "供应商名称重复!");
        }
        
        Date date = new Date();
        Provider provider = new Provider();
        provider.setAddDate(date);
        provider.setAddress(address);
        provider.setAlterDate(date);
        provider.setTalkGroup(talkGroup);
        provider.setIsResponsible(isResponsible);
        provider.setWeChat(weChat);
        provider.setNumberCode(UUIDUtil.getUUIDStringFor32());
        provider.setNameCn(nameCn);
        provider.setPolicy(policy);
        provider.setMark(mark);
        provider.setSettlement(settlement);
        provider.setSign(sign);
        provider.setGrade(grade);
        provider.setStatus(status);
        provider.setRemark(remark);
        commonDao.save(provider);
        
        possessorUtil.savePossessorRelates(Constant.provider_type, provider.getNumberCode());

        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> editProvider(String numberCode , String mark , String nameCn ,
                                            String address , String weChat , String talkGroup ,
                                            int isResponsible , String sign , String settlement ,
                                            String policy , int status , String remark , int grade) {
        Date date = new Date();
        Provider provider = (Provider)commonDao.getObjectByUniqueCode(Constant.provider, Constant.numberCode, numberCode);
        if (provider == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.provider_not_exist_error);
        }
        
        String existSql = "select name_cn from provider where name_cn = '"+nameCn+"'";
        existSql += possessorUtil.getRelatesWhereSql(Constant.relate_type_provider);
        
        @SuppressWarnings("unchecked")
        List<String> existList = commonDao.getSqlQuery(existSql).list();
        if (!nameCn.equals(provider.getNameCn())) {
        	if (existList.size() >= 1) {
                return ReturnUtil.returnMap(0, "供应商名称重复!");
            }
		}
        
        provider.setAddress(address);
        provider.setTalkGroup(talkGroup);
        provider.setIsResponsible(isResponsible);
        provider.setWeChat(weChat);
        provider.setNameCn(nameCn);
        provider.setPolicy(policy);
        provider.setMark(mark);
        provider.setSettlement(settlement);
        provider.setSign(sign);
        provider.setStatus(status);
        provider.setGrade(grade);
        provider.setRemark(remark);
        provider.setAlterDate(date);
        commonDao.update(provider);
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> deleteProviders(String providers) {

        String deleteProviderSql = "delete from provider where number_code in ("
            + StringTool.StrToSqlString(providers) + ")";
        commonDao.getSqlQuery(deleteProviderSql).executeUpdate();
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> queryPossessorProviderList(Boolean isTrue,String possessor,String numberCode,String name) {
        
        StringBuffer hqlSb = new StringBuffer();
        
        hqlSb.append("from Provider where 1=1 ");
        List<String> relates = possessorUtil.getDecideMapByPossessor(possessor,Constant.provider_type).keySet().contains(Constant.provider_type)?possessorUtil.getDecideMapByPossessor(possessor,Constant.provider_type).get(Constant.provider_type):new ArrayList<String>();
        
        if (relates == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.possessor_numberCode_error);
        }
        
        if (isTrue) {
            hqlSb.append(" and numberCode in ("+StringTool.listToSqlString(relates)+")");
        }else {
            hqlSb.append(" and numberCode not in ("+StringTool.listToSqlString(relates)+")");
        }
        
        if (StringTool.isNotNull(numberCode)) {
            hqlSb.append(" and numberCode = '"+numberCode+"'");
        }

        if (StringTool.isNotNull(name)) {
            hqlSb.append(" and nameCn like '%"+name+"%'");
        }
        
        @SuppressWarnings("unchecked")
        List<Provider> providerList = commonDao.queryByHql(hqlSb.toString());
        
        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", providerList.size());
        mapContent.put("rows", providerList);
        
        return ReturnUtil.returnMap(1, mapContent);
    }

    @Override
    public Map<String, Object> getProviderMarkList(String mark , String numberCode) {
        String hql = "from Provider p where p.mark = '"+ mark +"' and p.numberCode <> '"+ numberCode +"' ";
        @SuppressWarnings("unchecked")
        List<Provider> list = commonDao.queryByHql(hql);
        
        return ReturnUtil.returnMap(1, list);
    }

    @Override
    public Map<String, Object> importExcelOper(String excelPath) {
        String[] titles = {
            "等级(1-5,数值越高等级越高)",
            "供应商名称",
            "查询编号",
            "负责和客户签订合同(0.否/1.是/2.用自己的合同/3.其它)",
            "合作状态(0.否/1.是)",
            "材料寄到地址",
            "微信号",
            "讨论组",
            "与客户签订方式",
            "结算方式",
            "后返政策",
            "备注"
        };
        
        //判断Excel是否符合要求  并获取数据
        Map<String, Object> checkMap = ExcelTool.getExcel(excelPath, titles);
        if(Integer.parseInt(checkMap.get("status").toString())==0){
            return ReturnUtil.returnMap(0, checkMap.get("msg"));
        }
        
        @SuppressWarnings("unchecked")
        List<String[]> list = (List<String[]>)checkMap.get("content");
        
        List<Map<String,String>> excelList = new ArrayList<>();
        for(String[] row : list){
            String grade = row[0]==null?"":row[0].toString();         //等级(1-5,数值越高等级越高)
            String nameCn = row[1]==null?"":row[1].toString();        //供应商名称
            String mark = row[2]==null?"":row[2].toString();          //查询编号
            String isResponsible = row[3]==null?"":row[3].toString(); //负责和客户签订合同(0.否/1.是/2.用自己的合同/3.其它)
            String status = row[4]==null?"":row[4].toString();        //合作状态(0.否/1.是)
            String address = row[5]==null?"":row[5].toString();       //材料寄到地址
            String weChat = row[6]==null?"":row[6].toString();        //微信号
            String talkGroup = row[7]==null?"":row[7].toString();     //讨论组
            String sign = row[8]==null?"":row[8].toString();          //与客户签订方式
            String settlement = row[9]==null?"":row[9].toString();    //结算方式
            String policy = row[10]==null?"":row[10].toString();      //后返政策
            String remark = row[11]==null?"":row[11].toString();      //备注
            
            Map<String,String> map = new HashMap<>();
            map.put("grade", grade);
            map.put("nameCn", nameCn);
            map.put("mark", mark);
            map.put("isResponsible", isResponsible);
            map.put("status", status);
            map.put("address", address);
            map.put("weChat", weChat);
            map.put("talkGroup", talkGroup);
            map.put("sign", sign);
            map.put("settlement", settlement);
            map.put("policy", policy);
            map.put("remark", remark);
            excelList.add(map);
        } 
        if(excelList.size() > 0){
            @SuppressWarnings("unchecked")
            List<Provider> providerList = commonDao.queryByHql("from Provider");
            Map<String,Object> providerMap=new HashMap<String, Object>();
            for(Provider provider : providerList){
                providerMap.put(provider.getMark(),provider);
            }
            
            Date date = new Date();
            int rowNum = 1;
            for(Map<String,String> map : excelList){
                String gradeStr = map.get("grade");
                String nameCn = map.get("nameCn");
                String mark = map.get("mark");
                String isResponsible = map.get("isResponsible");
                String statusStr = map.get("status");
                if(StringTool.isEmpty(gradeStr)){
                    return ReturnUtil.returnMap(0, "第"+ rowNum +"行【供应商等级】为空!");
                }
                int grade = Integer.valueOf(gradeStr);
                if(grade < 1 || grade > 5){
                    return ReturnUtil.returnMap(0, "第"+ rowNum +"行【供应商等级】错误!");
                }
                if(StringTool.isEmpty(nameCn)){
                    return ReturnUtil.returnMap(0, "第"+ rowNum +"行【供应商名称】为空!");
                }
                if(StringTool.isEmpty(mark)){
                    return ReturnUtil.returnMap(0, "第"+ rowNum +"行【查选代码】为空!");  
                }
                if(StringTool.isEmpty(isResponsible)){
                    return ReturnUtil.returnMap(0, "第"+ rowNum +"行【负责和客户签订合同】为空!");  
                }
                if(StringTool.isEmpty(statusStr)){
                    return ReturnUtil.returnMap(0, "第"+ rowNum +"行【合作状态】为空!");  
                }
                
                Provider providerObj = (Provider) providerMap.get(mark);
                boolean pd = false;
                if(providerObj == null){
                    providerObj = new Provider();
                    providerObj.setNumberCode(UUIDUtil.getUUIDStringFor32());
                    providerObj.setAddDate(date);
                    providerObj.setMark(mark);
                    pd = true;
                }
                providerObj.setNameCn(nameCn);
                providerObj.setGrade(grade);
                int responsible = 3;   //其它
                if(isResponsible.indexOf("否") > -1 || isResponsible.indexOf("0") > -1){
                    responsible = 0;
                }else if(isResponsible.indexOf("是") > -1 || isResponsible.indexOf("1") > -1){
                    responsible = 1;
                }else if(isResponsible.indexOf("用自己的合同") > -1 || isResponsible.indexOf("2") > -1){
                    responsible = 2;
                }
                providerObj.setIsResponsible(responsible);
                int status = 0; //否
                if(statusStr.indexOf("是") > -1 || statusStr.indexOf("1") > -1 ){
                    status = 1;
                }
                providerObj.setStatus(status);
                String address = map.get("address");
                String weChat = map.get("weChat");
                String talkGroup = map.get("talkGroup");
                String sign = map.get("sign");
                String settlement = map.get("settlement");
                String policy = map.get("policy");
                String remark = map.get("remark");
                providerObj.setAddress(address);
                providerObj.setWeChat(weChat);
                providerObj.setTalkGroup(talkGroup);
                providerObj.setSign(sign);
                providerObj.setSettlement(settlement);
                providerObj.setPolicy(policy);
                providerObj.setRemark(remark);
                providerObj.setAlterDate(date);
                if(pd){
                    commonDao.save(providerObj);
                    possessorUtil.savePossessorRelates(Constant.provider_type, providerObj.getNumberCode());
                }else{
                    commonDao.update(providerObj);
                }
                rowNum++;
            }
        }
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> exportExcelOper() {
        Object[][] titles = {
            new Object[]{"等级(1-5,数值越高等级越高)",10},
            new Object[]{"供应商名称",10},
            new Object[]{"查询编号",8},
            new Object[]{"负责和客户签订合同(0.否/1.是/2.用自己的合同/3.其它)",12},  //0:否  1:是  2:用自己的合同  3:其它
            new Object[]{"合作状态(0.否/1.是)",8},     //0:否    1:是  
            new Object[]{"材料寄到地址",20},
            new Object[]{"微信号",15},
            new Object[]{"讨论组",15},
            new Object[]{"与客户签订方式",20},
            new Object[]{"结算方式",10},
            new Object[]{"后返政策",8},
            new Object[]{"备注",10}
        };
        
        String filename = "provider-"+DateUtil.dateToLongString(new Date())+".xlsx";
        boolean flag = ExcelTool.createExcelStyle(titles, filename);
        if(!flag){
            return ReturnUtil.returnMap(0, "Excel模版生成失败!");
        }
        
        return ReturnUtil.returnMap(1, filename);
    }

    @Override
    public Map<String, Object> editAccountList(String provider , String accountJson) {
        if(StringTool.isEmpty(provider)){
            return ReturnUtil.returnMap(0, "供应商选择错误!");
        }
        if(StringTool.isEmpty(accountJson)){
            return ReturnUtil.returnMap(0, "请填写供应账户信息!");
        }
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> accountList = JSON.toObject(accountJson, ArrayList.class);
        
        Date date = new Date();
        Account accountObj = null;
        List<String> existList = new ArrayList<>();
        for(Map<String, Object> account : accountList) {
            String numberCode = (String)account.get("numberCode");
            String accountNumber = (String)account.get("accountNumber");
            String cashAccount = (String)account.get("cashAccount");
            String bankOfDeposit = (String)account.get("bankOfDeposit");
            int isEnable = account.get("isEnable")==null?1:Integer.valueOf(account.get("isEnable").toString());
            String remark = (String)account.get("remark");
            
            if(StringTool.isEmpty(accountNumber)){
                return ReturnUtil.returnMap(0, "对公帐号不能为空!");
            }
            if(StringTool.isEmpty(cashAccount)){
                return ReturnUtil.returnMap(0, "收款户名不能为空!");
            }
            if(StringTool.isEmpty(bankOfDeposit)){
                return ReturnUtil.returnMap(0, "开户银行不能为空!");
            }
            
            if(StringTool.isNotNull(numberCode)){
                existList.add(numberCode);
                accountObj = (Account)commonDao.getObjectByUniqueCode("Account", "numberCode", numberCode);
                accountObj.setAccountNumber(accountNumber);
                accountObj.setCashAccount(cashAccount);
                accountObj.setBankOfDeposit(bankOfDeposit);
                accountObj.setProvider(provider);
                accountObj.setIsEnable(isEnable);
                accountObj.setRemark(remark);
                accountObj.setAlterDate(date);
                commonDao.update(accountObj);
            }else{
                accountObj = new Account();
                accountObj.setNumberCode(UUIDUtil.getUUIDStringFor32());
                accountObj.setAccountNumber(accountNumber);
                accountObj.setCashAccount(cashAccount);
                accountObj.setBankOfDeposit(bankOfDeposit);
                accountObj.setProvider(provider);
                accountObj.setIsEnable(isEnable);
                accountObj.setRemark(remark);
                accountObj.setAddDate(date);
                accountObj.setAlterDate(date);
                commonDao.save(accountObj);
            }
        }
        
        if(existList.size() > 0){
            String sql = "DELETE FROM account WHERE number_code NOT IN ("+StringTool.listToSqlString(existList)+") ";
            sql += "and provider = '"+ provider +"' ";
            commonDao.getSqlQuery(sql).executeUpdate();
        }
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> editJoinerList(String provider , String joinerJson) {
        if(StringTool.isEmpty(provider)){
            return ReturnUtil.returnMap(0, "供应商选择错误!");
        }
        if(StringTool.isEmpty(joinerJson)){
            return ReturnUtil.returnMap(0, "请填写对接人信息!");
        }
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> joinerList = JSON.toObject(joinerJson, ArrayList.class);
        
        Date date = new Date();
        Joiner joinerObj = null;
        List<String> existList = new ArrayList<>();
        for(Map<String, Object> joiner : joinerList) {
            String numberCode = (String)joiner.get("numberCode");
            String nameCn = (String)joiner.get("nameCn");
            String phone = (String)joiner.get("phone");
            String weChat = (String)joiner.get("weChat");
            String qq = (String)joiner.get("qq");
            String remark = (String)joiner.get("remark");
            
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
            
            if(StringTool.isNotNull(numberCode)){
                existList.add(numberCode);
                joinerObj = (Joiner)commonDao.getObjectByUniqueCode("Joiner", "numberCode", numberCode);
                joinerObj.setNameCn(nameCn);
                joinerObj.setPhone(phone);
                joinerObj.setWeChat(weChat);
                joinerObj.setProvider(provider);
                joinerObj.setQq(qq);
                joinerObj.setRemark(remark);
                joinerObj.setAlterDate(date);
                commonDao.update(joinerObj);
            }else{
                joinerObj = new Joiner();
                joinerObj.setNumberCode(UUIDUtil.getUUIDStringFor32());
                joinerObj.setNameCn(nameCn);
                joinerObj.setPhone(phone);
                joinerObj.setWeChat(weChat);
                joinerObj.setProvider(provider);
                joinerObj.setQq(qq);
                joinerObj.setRemark(remark);
                joinerObj.setAddDate(date);
                joinerObj.setAlterDate(date);
                commonDao.save(joinerObj);
            }
        }
        if(existList.size() > 0){
            String sql = "DELETE FROM joiner WHERE number_code NOT IN ("+StringTool.listToSqlString(existList)+") ";
            sql += "and provider = '"+ provider +"' ";
            commonDao.getSqlQuery(sql).executeUpdate();
        }
        
        return ReturnUtil.returnMap(1, null);
    }

}