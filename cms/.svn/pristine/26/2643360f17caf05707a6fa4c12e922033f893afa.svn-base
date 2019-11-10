package xyz.work.goal.svc.imp;

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
import xyz.work.goal.model.Person;
import xyz.work.goal.svc.PersonSvc;

@Service
public class PersonSvcImp implements PersonSvc {
    @Autowired
    private CommonDao commonDao;

    @Override
    public Map<String, Object> queryPersonList(int offset , int pageSize , String nameCn) {
        
        String hql = "from Person p where 1=1 ";
        if(StringTool.isNotNull(nameCn)){
            hql += "and p.nameCn like '%"+ nameCn +"%' ";
        }
        hql += "order by p.nameCn ";
        
        Query countQuery = commonDao.getQuery("select count(*) " + hql);
        Number countNum = (Number)countQuery.uniqueResult();
        int count = countNum == null ? 0 : countNum.intValue();

        Query query = commonDao.getQuery(hql.toString());
        query.setMaxResults(pageSize);
        query.setFirstResult(offset);
        @SuppressWarnings("unchecked")
        List<Person> list = query.list();
        
        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", count);
        mapContent.put("rows", list);

        return ReturnUtil.returnMap(1, mapContent);
    }

    @Override
    public Map<String, Object> addPerson(String nameCn , String sex , int post) {
        
        if(StringTool.isEmpty(nameCn)){
            return ReturnUtil.returnMap(0, "姓名不能为空!");
        }
        Person personObj = new Person();
        personObj.setNumberCode(UUIDUtil.getUUIDStringFor32());
        personObj.setNameCn(nameCn);
        personObj.setPost(post);
        personObj.setSex(sex);
        commonDao.save(personObj);
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> editPerson(String numberCode , String nameCn , String sex , int post) {
        
        if(StringTool.isEmpty(numberCode)){
            return ReturnUtil.returnMap(0, "员工选择错误!");
        }
        if(StringTool.isEmpty(nameCn)){
            return ReturnUtil.returnMap(0, "姓名不能为空!");
        }
        
        Person personObj = (Person)commonDao.getObjectByUniqueCode("Person", "numberCode", numberCode);
        if(personObj == null){
            return ReturnUtil.returnMap(0, "员工信息不存在!");
        }
        personObj.setNameCn(nameCn);
        personObj.setPost(post);
        personObj.setSex(sex);
        commonDao.update(personObj);
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> deletePerson(String numberCodes) {
        
        if (StringTool.isEmpty(numberCodes)) {
            return ReturnUtil.returnMap(0, "员工信息选择错误!");
        }
        
        String sql = "DELETE FROM person WHERE number_code IN ("+ StringTool.StrToSqlString(numberCodes) +") ";
        commonDao.getSqlQuery(sql).executeUpdate();
        
        return ReturnUtil.returnMap(1, null);
    }

}