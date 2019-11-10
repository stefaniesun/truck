package xyz.work.r_base.svc.imp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.ReturnUtil;
import xyz.util.ExcelTool;
import xyz.util.StringTool;
import xyz.util.UUIDUtil;
import xyz.work.r_base.model.R_Port;
import xyz.work.r_base.svc.R_PortSvc;

@Service
public class R_PortSvcImp implements R_PortSvc {
    @Autowired
    private CommonDao commonDao;

    @Override
    public Map<String, Object> importExcelPortOper(String excelPath) {
        if(StringTool.isEmpty(excelPath)){
            return ReturnUtil.returnMap(0, "excel文件路径错误!");
        }
        
        String[] titles = {
            "港口所在地区",
            "港口名称",
            "港口标识"
        };
        //判断Excel是否符合要求  并获取数据
        Map<String ,Object> checkMap =  ExcelTool.getExcelData(excelPath, titles);
        if(Integer.parseInt(checkMap.get("status").toString()) == 0){
            return ReturnUtil.returnMap(0, checkMap.get("msg"));
        }
        @SuppressWarnings("unchecked")
        List<String[]> excelList = (List<String[]>) checkMap.get("content");
        
        String hql = "from R_Port ";
        @SuppressWarnings("unchecked")
        List<R_Port> portList = commonDao.queryByHql(hql);
        
        String temp = "";
        for(int rowNum = 0; rowNum < excelList.size(); rowNum++){
            String[] row = excelList.get(rowNum);
            String location = row[0]==null?"":row[0].toString();    //港口所在地区
            String nameCn = row[1]==null?"":row[1].toString();      //港口名称
            String mark = row[2]==null?"":row[2].toString();        //港口标识
            if(rowNum == 0 || !location.equals(temp)){
                temp = location;
            }
            
            R_Port rPort = null;
            for(R_Port portObj : portList){
                if(nameCn.equals(portObj.getNameCn()) && mark.equals(portObj.getMark())){
                    rPort = portObj;
                    break;
                }
            }
            if(rPort == null){
                rPort = new R_Port();
                rPort.setNumberCode(UUIDUtil.getUUIDStringFor32());
                rPort.setLocation(location);
                rPort.setNameCn(nameCn);
                rPort.setMark(mark);
                commonDao.save(rPort);
                portList.add(rPort);
            }else{
                rPort.setLocation(location);
                commonDao.update(rPort);
            }
            
        } 
        
        return ReturnUtil.returnMap(1,null);
    }

    @Override
    public Map<String, Object> queryRPortList(int offset , int pageSize ,String nameCn, String mark, String location) {
        String hql = "from R_Port p where 1=1 ";
        if(StringTool.isNotNull(nameCn)){
            hql += "and p.nameCn like '%"+ nameCn +"%' ";
        }
        if(StringTool.isNotNull(mark)){
            hql += "and p.mark like '%"+ mark +"%' ";
        }
        if(StringTool.isNotNull(location)){
            hql += "and p.location like '%"+ location +"%' ";
        }
        Query countQuery = commonDao.getQuery("select count(*) " + hql);
        Number countNum = (Number)countQuery.uniqueResult();
        int count = countNum == null ? 0 : countNum.intValue();

        Query query = commonDao.getQuery(hql);
        query.setMaxResults(pageSize);
        query.setFirstResult(offset);
        @SuppressWarnings("unchecked")
        List<R_Port> list = query.list();
        
        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", count);
        mapContent.put("rows", list);

        return ReturnUtil.returnMap(1, mapContent);
    }

}