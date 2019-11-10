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
import xyz.work.resources.model.Account;
import xyz.work.resources.model.Provider;
import xyz.work.resources.svc.AccountSvc;

@Service
public class AccountSvcImp implements AccountSvc {
    @Autowired
    private CommonDao commonDao;

    @Override
    public Map<String, Object> queryAccountList(int offset , int pageSize , int isEnable ,
                                                String accountNumber , String cashAccount ,
                                                String bankOfDeposit , String provider) {
        String hql = "from Account a where 1=1 ";
        if(StringTool.isNotNull(provider)){
            hql += "and a.provider = '"+ provider +"' ";
        }
        if(StringTool.isNotNull(cashAccount)){
            hql += "and a.cashAccount like '%"+ cashAccount +"%' ";
        }
        if(StringTool.isNotNull(bankOfDeposit)){
            hql += "and a.bankOfDeposit like '%"+ bankOfDeposit +"%' ";
        }
        if(isEnable == 0 || isEnable == 1){
            hql += "and a.isEnable = "+ isEnable +" ";
        }
        hql += "order by a.alterDate ";
        
        Query countQuery = commonDao.getQuery("select count(*) " + hql.toString());
        Number countNum = (Number)countQuery.uniqueResult();
        int count = countNum == null ? 0 : countNum.intValue();

        Query query = commonDao.getQuery(hql.toString());
        query.setMaxResults(pageSize);
        query.setFirstResult(offset);
        @SuppressWarnings("unchecked")
        List<Account> list = query.list();
        
        String providerHql = "from Provider ";
        @SuppressWarnings("unchecked")
        List<Provider> providerList = commonDao.queryByHql(providerHql);
        for(Account acc : list){
            for(Provider p : providerList){
                if(acc.getProvider().equals(p.getNumberCode())){
                    acc.setProviderNameCn(p.getNameCn());
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
    public Map<String, Object> addAccount(String accountNumber , String cashAccount ,
                                          String bankOfDeposit , String provider , int isEnable ,
                                          String remark) {
        if(StringTool.isEmpty(accountNumber)){
            return ReturnUtil.returnMap(0, "对公帐号不能为空!");
        }
        if(StringTool.isEmpty(cashAccount)){
            return ReturnUtil.returnMap(0, "收款户名不能为空!");
        }
        if(StringTool.isEmpty(bankOfDeposit)){
            return ReturnUtil.returnMap(0, "开户银行不能为空!");
        }
        
        Date date = new Date();
        Account accountObj = new Account();
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
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> editAccount(String numberCode , String accountNumber ,
                                           String cashAccount , String bankOfDeposit ,
                                           String provider , int isEnable , String remark) {
        if(StringTool.isEmpty(numberCode)){
            return ReturnUtil.returnMap(0, "供应商账户选择错误!");
        }
        if(StringTool.isEmpty(accountNumber)){
            return ReturnUtil.returnMap(0, "对公帐号不能为空!");
        }
        if(StringTool.isEmpty(cashAccount)){
            return ReturnUtil.returnMap(0, "收款户名不能为空!");
        }
        if(StringTool.isEmpty(bankOfDeposit)){
            return ReturnUtil.returnMap(0, "开户银行不能为空!");
        }
        
        Account accountObj = (Account)commonDao.getObjectByUniqueCode("Account", "numberCode", numberCode);
        accountObj.setAccountNumber(accountNumber);
        accountObj.setCashAccount(cashAccount);
        accountObj.setBankOfDeposit(bankOfDeposit);
        accountObj.setProvider(provider);
        accountObj.setIsEnable(isEnable);
        accountObj.setRemark(remark);
        accountObj.setAlterDate(new Date());
        commonDao.update(accountObj);
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> editIsEnable(String numberCode) {
        if(StringTool.isEmpty(numberCode)){
            return ReturnUtil.returnMap(0, "供应商账户选择错误!");
        }
        
        Account accountObj = (Account)commonDao.getObjectByUniqueCode("Account", "numberCode", numberCode);
        
        int isEnable = accountObj.getIsEnable();
        isEnable = isEnable==0?1:0;
        
        accountObj.setIsEnable(isEnable);
        commonDao.update(accountObj);
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> deleteAccount(String numberCodes) {
        if(StringTool.isEmpty(numberCodes)){
            return ReturnUtil.returnMap(0, "请选择要删除的供应商账户信息!");
        }
        
        String sql = "DELETE FROM account WHERE number_code IN ("+ StringTool.StrToSqlString(numberCodes) +") ";
        commonDao.getSqlQuery(sql).executeUpdate();
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> queryAccountListByprovider(String provider) {
        if(StringTool.isEmpty(provider)){
            return ReturnUtil.returnMap(0, "供应商选择错误!");
        }
        
        String hql = "from Account a where 1=1 ";
        hql += "and a.provider = '"+ provider +"' ";
        hql += "order by a.alterDate ";
        @SuppressWarnings("unchecked")
        List<Account> list = commonDao.queryByHql(hql);
        
        Provider providerObj = (Provider)commonDao.getObjectByUniqueCode("Provider", "numberCode", provider);
        for(Account acc : list){
            acc.setProviderNameCn(providerObj.getNameCn());
        }

        return ReturnUtil.returnMap(1, list);
    }

}
