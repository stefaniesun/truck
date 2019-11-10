package xyz.work.sell.svc.imp;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.MyRequestUtil;
import xyz.filter.ReturnUtil;
import xyz.util.PossessorUtil;
import xyz.util.StringTool;
import xyz.util.StringUtil;
import xyz.util.UUIDUtil;
import xyz.work.core.model.LogWork;
import xyz.work.sell.model.Ptview;
import xyz.work.sell.model.PtviewSku;
import xyz.work.sell.model.PtviewSku_TkviewType;
import xyz.work.sell.svc.PtviewSkuSvc;

@Service
public class PtviewSkuSvcImp implements PtviewSkuSvc {
    @Autowired
    private CommonDao commonDao;
    
    @Autowired
    private PossessorUtil possessorUtil;
    
    @Override
    public Map<String, Object> queryPtviewSkuList(String ptview) {
        String hql = "from PtviewSku ps where ps.ptview = '"+ ptview +"' ";
        {
            hql += possessorUtil.getRelatesWhereHql("ptview_sku");
        }
        @SuppressWarnings("unchecked")
        List<PtviewSku> list = commonDao.queryByHql(hql);
        
        return ReturnUtil.returnMap(1, list);
    }

    @Override
    public Map<String, Object> addPtviewSku(String ptview , String nameCn , String tkviewType) {
        Ptview ptviewObj = (Ptview)commonDao.getObjectByUniqueCode("Ptview", "numberCode", ptview);
        if(ptviewObj == null){
            return ReturnUtil.returnMap(0, "产品对象已经不存在了!");
        }
        if(StringTool.isEmpty(nameCn)){
            return ReturnUtil.returnMap(0, "产品SKU套餐名称不能为空!");
        }
        if(StringTool.isEmpty(tkviewType)){
            return ReturnUtil.returnMap(0, "请选择关联单品种类!");
        }
        
        String countSql = "SELECT COUNT(*) FROM ptview_sku k ";
        countSql += "WHERE k.ptview = '"+ ptview +"' ";
        countSql += "AND k.name_cn = '"+ nameCn +"' ";
        countSql += possessorUtil.getRelatesWhereSql("ptview_sku","k");
        Query countQuery = commonDao.getSqlQuery(countSql);
        Number countNum = (Number)countQuery.uniqueResult();
        int count = countNum == null ? 0 : countNum.intValue();
        if (count > 0) {
            return ReturnUtil.returnMap(0, "产品SKU套餐【"+ nameCn +"】已存在!");
        }
        
        Date date = new Date();
        PtviewSku skuObj = new PtviewSku();
        skuObj.setPtview(ptview);
        skuObj.setNumberCode(StringUtil.get_new_ptview_sku());
        skuObj.setNameCn(nameCn);
        skuObj.setTkviewType(tkviewType);
        skuObj.setAddDate(date);
        skuObj.setAlterDate(date);
        commonDao.save(skuObj);
        
        possessorUtil.savePossessorRelates("ptview_sku", skuObj.getNumberCode());
        
        LogWork logWork = new LogWork();
        logWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
        logWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
        logWork.setValue(skuObj.getNumberCode());
        logWork.setTableName("ptview_sku");
        logWork.setRemark("【"+ ptviewObj.getNameCn() +"】产品新增SKU套餐【"+ nameCn +"】");
        logWork.setAddDate(date);
        commonDao.save(logWork);
        
        //TODO 删除关联表
        if(StringTool.isNotNull(tkviewType)){
            String[] tkviewTypeArry = tkviewType.split(",");
            for(int t = 0; t < tkviewTypeArry.length; t++){
                String tkviewTypeNumber = tkviewTypeArry[t];
                if(StringTool.isEmpty(tkviewTypeNumber)){
                    continue;
                }
                PtviewSku_TkviewType sku_tkviewTypeObj = new PtviewSku_TkviewType();
                sku_tkviewTypeObj.setNumberCode(UUIDUtil.getUUIDStringFor32());
                sku_tkviewTypeObj.setPtviewSku(skuObj.getNumberCode());
                sku_tkviewTypeObj.setTkviewType(tkviewTypeNumber);
                sku_tkviewTypeObj.setAddDate(date);
                sku_tkviewTypeObj.setAlterDate(date);
                commonDao.save(sku_tkviewTypeObj);
            }
        }
        
        return ReturnUtil.returnMap(1, null);
    }
    
    @Override
    public Map<String, Object> getRelationTkviewType(String numberCode) {
        PtviewSku skuObj = (PtviewSku)commonDao.getObjectByUniqueCode("PtviewSku", "numberCode", numberCode);
        if(skuObj == null){
            return ReturnUtil.returnMap(0, "产品SKU套餐对象已经不存在了!");
        }
        
        //TODO 删除关联表
        String queryHql = "from PtviewSku_TkviewType st where st.ptviewSku = '"+ numberCode +"' ";
        @SuppressWarnings("unchecked")
        List<PtviewSku_TkviewType> list = commonDao.queryByHql(queryHql);
        
        String tkviewStr = "";
        for(PtviewSku_TkviewType st : list){
            if(StringTool.isEmpty(tkviewStr)){
                tkviewStr = st.getTkviewType();
                continue;
            }
            tkviewStr = tkviewStr + "," + st.getTkviewType();
        }
        
        return ReturnUtil.returnMap(1, tkviewStr);
    }

    @Override
    public Map<String, Object> editPtviewSku(String numberCode , String nameCn , String tkviewType) {
        PtviewSku skuObj = (PtviewSku)commonDao.getObjectByUniqueCode("PtviewSku", "numberCode", numberCode);
        if(skuObj == null){
            return ReturnUtil.returnMap(0, "产品SKU套餐对象已经不存在了!");
        }
        Ptview ptviewObj = (Ptview)commonDao.getObjectByUniqueCode("Ptview", "numberCode", skuObj.getPtview());
        if(ptviewObj == null){
            return ReturnUtil.returnMap(0, "产品对象已经不存在了!");
        }
        if(StringTool.isEmpty(nameCn)){
            return ReturnUtil.returnMap(0, "产品SKU套餐名称不能为空!");
        }
        if(StringTool.isEmpty(tkviewType)){
            return ReturnUtil.returnMap(0, "请选择关联单品种类!");
        }
        
        String countSql = "SELECT COUNT(*) FROM ptview_sku ";
        countSql += "WHERE ptview = '"+ skuObj.getPtview() +"' ";
        countSql += possessorUtil.getRelatesWhereSql("ptview_sku");
        countSql += "AND name_cn = '"+ nameCn +"' ";
        countSql += "AND number_code <> '"+ numberCode +"' ";
        Query countQuery = commonDao.getSqlQuery(countSql);
        Number countNum = (Number)countQuery.uniqueResult();
        int count = countNum == null ? 0 : countNum.intValue();
        if (count > 0) {
            return ReturnUtil.returnMap(0, "产品SKU套餐【"+ nameCn +"】已存在!");
        }
        
        String oldNameCn = skuObj.getNameCn();
        
        Date date = new Date();
        skuObj.setNameCn(nameCn);
        skuObj.setTkviewType(tkviewType);
        skuObj.setAlterDate(date);
        commonDao.update(skuObj);
        
        String str = "产品【"+ ptviewObj.getNameCn() +"】编辑SKU套餐【"+ nameCn +"】";
        if(!oldNameCn.equals(nameCn)){
            str = "产品【"+ ptviewObj.getNameCn() +"】SKU套餐【"+ oldNameCn +"】改为【"+ nameCn +"】";
        }
        
        LogWork logWork = new LogWork();
        logWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
        logWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
        logWork.setValue(skuObj.getNumberCode());
        logWork.setTableName("ptview_sku");
        logWork.setRemark(str);
        logWork.setAddDate(date);
        commonDao.save(logWork);
        
        //TODO 删除关联表
        String deleteSql = "DELETE FROM ptview_sku_tkview_type WHERE ptview_sku = '"+ numberCode +"' ";
        commonDao.getSqlQuery(deleteSql).executeUpdate();
        if(StringTool.isNotNull(tkviewType)){
            String[] tkviewTypeArry = tkviewType.split(",");
            for(int t = 0; t < tkviewTypeArry.length; t++){
                String tkviewTypeNumber = tkviewTypeArry[t];
                if(StringTool.isEmpty(tkviewTypeNumber)){
                    continue;
                }
                PtviewSku_TkviewType sku_tkviewTypeObj = new PtviewSku_TkviewType();
                sku_tkviewTypeObj.setNumberCode(UUIDUtil.getUUIDStringFor32());
                sku_tkviewTypeObj.setPtviewSku(skuObj.getNumberCode());
                sku_tkviewTypeObj.setTkviewType(tkviewTypeNumber);
                sku_tkviewTypeObj.setAddDate(date);
                sku_tkviewTypeObj.setAlterDate(date);
                commonDao.save(sku_tkviewTypeObj);
            }
        }
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> deletePtviewSku(String numberCode) {
        if(StringTool.isEmpty(numberCode)){
            return ReturnUtil.returnMap(0, "请选择要删除的SKU套餐!");
        }
        
        //TODO  删除关联表
        String sql = "DELETE FROM ptview_sku_tkview_type WHERE ptview_sku = '"+ numberCode +"' ";
        commonDao.getSqlQuery(sql).executeUpdate();
        
        String deleteSql = "DELETE FROM ptview_sku WHERE number_code = '"+ numberCode +"' ";
        commonDao.getSqlQuery(deleteSql).executeUpdate();
        
        return ReturnUtil.returnMap(1, null);
    }

}