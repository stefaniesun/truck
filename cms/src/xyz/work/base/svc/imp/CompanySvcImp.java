package xyz.work.base.svc.imp;

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
import xyz.util.UUIDUtil;
import xyz.work.base.model.Company;
import xyz.work.base.svc.CompanySvc;

@Service
public class CompanySvcImp implements CompanySvc{
    @Autowired
    private CommonDao commonDao;

    @Override
    public Map<String, Object> queryCompanyList(int offset , int pageSize , String nameCn) {
        String hql = "from Company c where 1=1 ";
        if (StringTool.isNotNull(nameCn)) {
            hql += "and c.nameCn like '%" + nameCn + "%' ";
        }
        hql += "order by c.alterDate desc ";

        String countHql = "select count(*) " + hql;
        Query countQuery = commonDao.getQuery(countHql);
        Number countNum = (Number)countQuery.uniqueResult();
        int count = countNum == null ? 0 : countNum.intValue();

        Query query = commonDao.getQuery(hql.toString()) ;
        query.setMaxResults(pageSize);
        query.setFirstResult(offset);
        @SuppressWarnings("unchecked")
        List<Company> list = query.list();

        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", count);
        mapContent.put("rows", list);

        return ReturnUtil.returnMap(1, mapContent);
    }

    @Override
    public Map<String, Object> addCompany(String nameCn , String nameEn , String websiteAddress , String remark) {
        
        MyRequestUtil.decidePowerIsAll();
        
        if (StringTool.isEmpty(nameCn)) {
            return ReturnUtil.returnMap(0, ConstantMsg.company_name_null);
        }
        
        Date date = new Date();
        Company companyObj = new Company();
        companyObj.setNumberCode(UUIDUtil.getUUIDStringFor32());
        companyObj.setNameCn(nameCn);
        companyObj.setNameEn(nameEn);
        companyObj.setWebsiteAddress(websiteAddress);
        companyObj.setRemark(remark);
        companyObj.setAddDate(date); 
        companyObj.setAlterDate(date);
        commonDao.save(companyObj);

        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> editCompany(String numberCode , String nameCn ,
                                                       String nameEn , String websiteAddress , String remark) {
        
        MyRequestUtil.decidePowerIsAll();
        
        if (StringTool.isEmpty(numberCode)) {
            return ReturnUtil.returnMap(0, ConstantMsg.company_numberCode_null);
        }
        if (StringTool.isEmpty(nameCn)) {
            return ReturnUtil.returnMap(0, ConstantMsg.company_name_null);
        }

        Company companyObj = (Company)commonDao.getObjectByUniqueCode("Company", "numberCode", numberCode);
        if (companyObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.company_null);
        }
        companyObj.setNameCn(nameCn);
        companyObj.setNameEn(nameEn);
        companyObj.setWebsiteAddress(websiteAddress);
        companyObj.setRemark(remark);
        companyObj.setAlterDate(new Date());
        commonDao.update(companyObj);

        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> deleteCompany(String numberCodes) {
        
        MyRequestUtil.decidePowerIsAll();
        
        if (StringTool.isEmpty(numberCodes)) {
            return ReturnUtil.returnMap(0, ConstantMsg.delete_null);
        }
        
        String sql = "DELETE FROM company WHERE number_code IN ("
            + StringTool.StrToSqlString(numberCodes) + ")";
        commonDao.getSqlQuery(sql).executeUpdate();
        
        return ReturnUtil.returnMap(1, null);
    }

}