package xyz.work.base.svc.imp;


import java.math.BigDecimal;
import java.util.ArrayList;
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
import xyz.util.StringUtil;
import xyz.work.base.model.Company;
import xyz.work.base.model.Cruise;
import xyz.work.base.svc.CruiseSvc;


@Service
public class CruiseSvcImp implements CruiseSvc {
    @Autowired
    private CommonDao commonDao;

    @Override
    public Map<String, Object> queryCruiseList(int offset , int pageSize , String nameCn ,
                                               String company , String mark) {
        String hql = "from Cruise c where 1=1 ";
        if (StringTool.isNotNull(nameCn)) {
            hql += "and c.nameCn like '%" + nameCn + "%' ";
        }
        if (StringTool.isNotNull(company)) {
            hql += "and c.company = '" + company + "' ";
        }
        if (StringTool.isNotNull(mark)) {
            hql += "and c.mark like '%" + mark + "%' ";
        }
        hql += "order by c.alterDate desc ";

        String countHql = "select count(*) " + hql;
        Query countQuery = commonDao.getQuery(countHql);
        Number countNum = (Number)countQuery.uniqueResult();
        int count = countNum == null ? 0 : countNum.intValue();

        Query query = commonDao.getQuery(hql.toString());
        query.setMaxResults(pageSize);
        query.setFirstResult(offset);
        @SuppressWarnings("unchecked")
        List<Cruise> list = query.list();

        /* 邮轮公司 */
        List<String> companyNumber = new ArrayList<String>();
        for (Cruise cruise : list) {
            companyNumber.add(cruise.getCompany());
        }
        String companySql = "SELECT c.number_code,c.name_cn FROM company c WHERE c.number_code IN ("
                            + StringTool.listToSqlString(companyNumber) + ")";
        @SuppressWarnings("unchecked")
        List<Object[]> companyList = commonDao.getSqlQuery(companySql).list();

        if (companyList != null && companyList.size() > 0) {
            for (Cruise cruise : list) {
                for (Object[] pany : companyList) {
                    if (pany[0].equals(cruise.getCompany())) {
                        cruise.setCompanyNameCn(pany[1].toString());
                        break;
                    }
                }
            }
        }

        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", count);
        mapContent.put("rows", list);

        return ReturnUtil.returnMap(1, mapContent);
    }

    @Override
    public Map<String, Object> addCruise(String nameCn , String mark , String company ,
                                         String remark , String nameEn , BigDecimal wide ,
                                         BigDecimal length , BigDecimal tonnage ,
                                         BigDecimal floor , BigDecimal busload ,
                                         BigDecimal totalCabin , BigDecimal avgSpeed ,
                                         String voltageSource , String laundromat ,
                                         String library , String survey) {
        
        MyRequestUtil.decidePowerIsAll();
        
        if (StringTool.isEmpty(company)) {
            return ReturnUtil.returnMap(0, ConstantMsg.company_null);
        }
        if (StringTool.isEmpty(nameCn)) {
            return ReturnUtil.returnMap(0, ConstantMsg.cruise_name_null);
        }
        if (StringTool.isEmpty(mark)) {
            return ReturnUtil.returnMap(0, ConstantMsg.cruise_mark_null);
        }

        Company companyObj = (Company)commonDao.getObjectByUniqueCode("Company", "numberCode",company);
        if (companyObj == null) {
            return ReturnUtil.returnMap(0, "邮轮公司对象不存在!");
        }

        String sql = "select count(*) from cruise where name_cn = '"+nameCn+"' and company = '"+companyObj.getNumberCode()+"'";
        Query countQuery = commonDao.getSqlQuery(sql);
        Number countNum = (Number)countQuery.uniqueResult();
        int count = countNum == null ? 0 : countNum.intValue();
        if (count > 0) {
            return ReturnUtil.returnMap(0, "同一邮轮公司,邮轮已存在!");
        }
        
        Date date = new Date();
        Cruise cruiseObj = new Cruise();
        cruiseObj.setNumberCode(StringUtil.get_new_criuse_num());
        cruiseObj.setCompany(companyObj.getNumberCode());
        cruiseObj.setNameCn(nameCn);
        cruiseObj.setNameEn(nameEn);
        cruiseObj.setMark(mark);
        cruiseObj.setRemark(remark);
        cruiseObj.setAddDate(date);
        cruiseObj.setAlterDate(date);
        cruiseObj.setWide(wide);
        cruiseObj.setLength(length);
        cruiseObj.setTonnage(tonnage);
        cruiseObj.setFloor(floor);
        cruiseObj.setBusload(busload);
        cruiseObj.setTotalCabin(totalCabin);
        cruiseObj.setAvgSpeed(avgSpeed);
        cruiseObj.setVoltageSource(voltageSource);
        cruiseObj.setLaundromat(Integer.parseInt(laundromat));
        cruiseObj.setLibrary(Integer.parseInt(library));
        cruiseObj.setSurvey(survey);
        
        String hql = "from Cruise ";
        String countHql = "select count(*) " + hql;
        Query query = commonDao.getQuery(countHql);
        Number num = (Number)query.uniqueResult();
        int sum = num == null ? 0 : num.intValue();
        cruiseObj.setSortNum(sum+1);
        
        commonDao.save(cruiseObj);

        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> editCruise(String numberCode , String nameCn , String mark ,
                                          String company , String remark , String nameEn ,
                                          BigDecimal wide , BigDecimal length ,
                                          BigDecimal tonnage , BigDecimal floor ,
                                          BigDecimal busload , BigDecimal totalCabin ,
                                          BigDecimal avgSpeed , String voltageSource ,
                                          String laundromat , String library , String survey) {
        MyRequestUtil.decidePowerIsAll();
        
        if (StringTool.isEmpty(numberCode)) {
            return ReturnUtil.returnMap(0, ConstantMsg.cruise_numberCode_null);
        }
        if (StringTool.isEmpty(company)) {
            return ReturnUtil.returnMap(0, ConstantMsg.company_null);
        }
        if (StringTool.isEmpty(nameCn)) {
            return ReturnUtil.returnMap(0, ConstantMsg.cruise_name_null);
        }
        if (StringTool.isEmpty(mark)) {
            return ReturnUtil.returnMap(0, ConstantMsg.cruise_mark_null);
        }

        Company companyObj = (Company)commonDao.getObjectByUniqueCode("Company", "numberCode",company);
        if(companyObj == null){
            return ReturnUtil.returnMap(0, ConstantMsg.company_null);
        }
        Cruise cruiseObj = (Cruise)commonDao.getObjectByUniqueCode("Cruise", "numberCode", numberCode);
        if (cruiseObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.cruise_null);
        }
        
        cruiseObj.setCompany(companyObj.getNumberCode());
        cruiseObj.setNameCn(nameCn);
        cruiseObj.setNameEn(nameEn);
        cruiseObj.setMark(mark);
        cruiseObj.setRemark(remark);
        cruiseObj.setAlterDate(new Date());
        cruiseObj.setWide(wide);
        cruiseObj.setLength(length);
        cruiseObj.setTonnage(tonnage);
        cruiseObj.setFloor(floor);
        cruiseObj.setBusload(busload);
        cruiseObj.setTotalCabin(totalCabin);
        cruiseObj.setAvgSpeed(avgSpeed);
        cruiseObj.setVoltageSource(voltageSource);
        cruiseObj.setLaundromat(Integer.parseInt(laundromat));
        cruiseObj.setLibrary(Integer.parseInt(library));
        cruiseObj.setSurvey(survey);
        commonDao.update(cruiseObj);
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> deleteCruise(String numberCodes) {
        
        MyRequestUtil.decidePowerIsAll();
        
        if (StringTool.isEmpty(numberCodes)) {
            return ReturnUtil.returnMap(0, ConstantMsg.delete_null);
        }
        
        String sql = "DELETE FROM cruise WHERE number_code in ("
            + StringTool.StrToSqlString(numberCodes) + ")";
        commonDao.getSqlQuery(sql).executeUpdate();
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> addImages(String numberCode , String urls) {
        
        MyRequestUtil.decidePowerIsAll();
        
        if (StringTool.isEmpty(numberCode)) {
            return ReturnUtil.returnMap(0, ConstantMsg.cruise_numberCode_null);
        }
        
        Cruise cruiseObj = (Cruise)commonDao.getObjectByUniqueCode("Cruise", "numberCode", numberCode);
        if (cruiseObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.cruise_null);
        }
        cruiseObj.setImages(urls);
        cruiseObj.setAlterDate(new Date());
        commonDao.update(cruiseObj);
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> addDetail(String numberCode , String detail) {
        MyRequestUtil.decidePowerIsAll();
        
        if (StringTool.isEmpty(numberCode)) {
            return ReturnUtil.returnMap(0, ConstantMsg.cruise_numberCode_null);
        }
        Cruise cruiseObj = (Cruise)commonDao.getObjectByUniqueCode("Cruise", "numberCode",numberCode);
        if (cruiseObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.cruise_null);
        }
        cruiseObj.setDetail(detail);
        commonDao.update(cruiseObj);

        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> addWeixinImages(String numberCode , String smallImg ,
                                               String largeImg) {
        MyRequestUtil.decidePowerIsAll();
        
        if (StringTool.isEmpty(numberCode)) {
            return ReturnUtil.returnMap(0, ConstantMsg.cruise_numberCode_null);
        }
        
        Cruise cruiseObj = (Cruise)commonDao.getObjectByUniqueCode("Cruise", "numberCode", numberCode);
        if (cruiseObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.cruise_null);
        }
        cruiseObj.setWeixinSmallImg(smallImg);
        cruiseObj.setWeixinLargeImg(largeImg);
        cruiseObj.setAlterDate(new Date());
        commonDao.update(cruiseObj);
        
        return ReturnUtil.returnMap(1, null); 
    }
   
}