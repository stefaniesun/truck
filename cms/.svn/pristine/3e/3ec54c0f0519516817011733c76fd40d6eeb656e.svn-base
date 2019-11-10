package xyz.work.sell.svc.imp;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.JSON;
import xyz.filter.MyRequestUtil;
import xyz.filter.ReturnUtil;
import xyz.util.ClazzUtil;
import xyz.util.Constant;
import xyz.util.ConstantMsg;
import xyz.util.DateUtil;
import xyz.util.PossessorUtil;
import xyz.util.StringTool;
import xyz.util.StringUtil;
import xyz.util.UUIDUtil;
import xyz.work.base.model.Airway;
import xyz.work.base.model.Company;
import xyz.work.base.model.Cruise;
import xyz.work.base.model.Port;
import xyz.work.base.model.Shipment;
import xyz.work.base.model.Trip;
import xyz.work.core.model.LogWork;
import xyz.work.resources.model.Stock;
import xyz.work.resources.model.Tkview;
import xyz.work.sell.model.Ptview;
import xyz.work.sell.model.PtviewSku;
import xyz.work.sell.model.PtviewSkuStock;
import xyz.work.sell.model.PtviewSku_TkviewType;
import xyz.work.sell.svc.PtviewSvc;


@Service
public class PtviewSvcImp implements PtviewSvc {

    @Autowired
    private CommonDao commonDao;

    @Autowired
    private PossessorUtil possessorUtil;

    @Override
    public Map<String, Object> queryCruiseList() {
        String sql = "SELECT p.cruise FROM ptview p GROUP BY p.cruise ";
        @SuppressWarnings("unchecked")
        List<String> list = commonDao.getSqlQuery(sql).list();
        
        String hql = "from Cruise c where c.numberCode in ("+ StringTool.listToSqlString(list) +") ";
        @SuppressWarnings("unchecked")
        List<Cruise> cruiseList = commonDao.queryByHql(hql);

        return ReturnUtil.returnMap(1, cruiseList);
    }

    @Override
    public Map<String, Object> queryPtviewListByCruise(int offset , int pageSize , String cruise ,
                                                       String nameCn , Date startDate , Date endDate) {
        String hql = "from Ptview p where 1=1 ";
        if(StringTool.isNotNull(cruise) && !"noValue".equals(cruise)){
            hql += " and p.cruise = '" + cruise + "' ";
        }
        {
            hql += possessorUtil.getRelatesWhereHql(Constant.relate_type_ptview);
        }
        if (StringTool.isNotNull(nameCn)) {
            hql += " and p.nameCn like '%" + nameCn + "%' ";
        }
        if (startDate != null && !"".equals(startDate)) {
            hql +=" and p.travelDate >= '" + DateUtil.dateToShortString(startDate) + "' ";
        }
        if (endDate != null && !"".equals(endDate)) {
            hql +=" and p.travelDate <= '" + DateUtil.dateToShortString(endDate) + "' ";
        }
        hql += "order by p.alterDate desc ";
        Query countQuery = commonDao.getQuery("select count(*) " + hql);
        Number countNum = (Number)countQuery.uniqueResult();
        int count = countNum == null ? 0 : countNum.intValue();

        Query query = commonDao.getQuery(hql);
        query.setMaxResults(pageSize);
        query.setFirstResult(offset);
        @SuppressWarnings("unchecked")
        List<Ptview> list = query.list();

        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", count);
        mapContent.put("rows", list);

        return ReturnUtil.returnMap(1, mapContent);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> addPtview(String cruise ,String shipmentJson) {
      
        if(shipmentJson.length()<=0){
            return ReturnUtil.returnMap(0, "请至少选择一个航期!");
        }
        Set<String> shipmentSet=new HashSet<>();
        
        for (String shipment : shipmentJson.split(",")) {
            shipmentSet.add(shipment);
        }
        //判断是否重复添加产品
        String ptviewHql=" from Ptview where 1 = 1 and cruise='"+cruise+"' and shipment in ("+StringTool.listToSqlString(shipmentSet)+") ";
   
        List<Ptview> ptviewList = commonDao.queryByHql(ptviewHql);
        for (Ptview ptview : ptviewList) {
            return ReturnUtil.returnMap(0, "请勿重复添加该邮轮下的"+ DateUtil.dateToShortString(ptview.getTravelDate())+"航期");
        }
        
        for (String shipment : shipmentJson.split(",")) {
            String company="";//邮轮公司编号
            String companyNameCn=""; //邮轮公司名字
            Date travelDate=null;// 出发时间
            String shipmentNumber ="";//航期编号
            String shipMark="";//航期代码
            String airway="";//航线
            String airwayNameCn=""; //航线名称
            int days=0;// 天数
            String fromPlace="";// 出发地
            BigDecimal basePrice=BigDecimal.ZERO;// 基础价格
            String cruiseNameCn="";//邮轮名字
            String image="";//邮轮图片
            
            
            Shipment shipmentObj = (Shipment)commonDao.getObjectByUniqueCode("Shipment", "numberCode", shipment);
            if (shipmentObj == null) {
                return ReturnUtil.returnMap(0, ConstantMsg.shipment_null);
            }

            Cruise cruiseList = (Cruise)commonDao.getObjectByUniqueCode("Cruise", "numberCode", cruise);
            
            cruiseNameCn=cruiseList.getNameCn();
            image=cruiseList.getImages().split(",")[0];
            
            Company companyList = (Company)commonDao.getObjectByUniqueCode("Company", "numberCode", cruiseList.getCompany());
            
            company=companyList.getNumberCode();
            companyNameCn=companyList.getNameCn();
            
            Shipment shipmentList = (Shipment)commonDao.getObjectByUniqueCode("Shipment", "numberCode", shipment);
              
            shipmentNumber=shipmentList.getNumberCode();
            shipMark=shipmentList.getMark();
            days=shipmentList.getTripDays();
            travelDate=shipmentList.getTravelDate();
                
            Airway airwayList = (Airway)commonDao.getObjectByUniqueCode("Airway", "numberCode", shipmentList.getAirway());
            
            airway= airwayList.getNumberCode();
            airwayNameCn=airwayList.getNameCn();
            
            //根据航线查询港口
            String tripHql =" from Trip t where 1 = 1 and  t.airway = '"+shipmentList.getAirway()+"' ";
            tripHql+=" order by t.priority asc";
            List<Trip>  tripList=commonDao.queryByHql(tripHql);
            
            Set<String> portSet= new HashSet<>();
            if(tripList.size()>0){
                portSet.add(tripList.get(0).getPort());
            }
            if(portSet.size()>0){
                //得到港口名称
                String portHql = " from Port p where p.numberCode in ("+StringTool.listToSqlString(portSet)+") ";
                List<Port> portlist = commonDao.queryByHql(portHql);  
                if(portlist.size()>0){
                    fromPlace=portlist.get(0).getNameCn();
                }
            }
            
            String TkviewHql="from Tkview t where 1 = 1 and  t.shipment='"+shipment+"'";
            List<Tkview> tkviewList=commonDao.queryByHql(TkviewHql);
            
            Set<String> tkviewNumberSet =new HashSet<>();
            for (Tkview tkview : tkviewList) {
                tkviewNumberSet.add(tkview.getNumberCode());
            }
            if(tkviewNumberSet.size()>0){
                String stockHql ="from Stock s where 1 = 1 and s.tkview in ("+StringTool.listToSqlString(tkviewNumberSet)+") ";
                stockHql += " and s.stock>0";
                stockHql += " order by s.cost asc ";
                
                List<Stock> stockList= commonDao.queryByHql(stockHql);
                if(stockList.size()>0){
                    basePrice=stockList.get(0).getCost();
                } 
            }
          
            Date date = new Date();
            Ptview ptviewObj = new Ptview();
            ptviewObj.setNumberCode(StringUtil.get_new_ptview());
            ptviewObj.setNameCn("["+cruiseNameCn+"]"+DateUtil.dateToShortString(travelDate));
            ptviewObj.setCompany(company);
            ptviewObj.setCompanyNameCn(companyNameCn);
            ptviewObj.setCruise(cruise);
            ptviewObj.setCruiseNameCn(cruiseNameCn);
            ptviewObj.setShipment(shipmentNumber);
            ptviewObj.setTravelDate(travelDate);
            ptviewObj.setShipMark(shipMark);
            ptviewObj.setAirway(airway);
            ptviewObj.setAirwayNameCn(airwayNameCn);
            ptviewObj.setDays(days);
            ptviewObj.setFromPlace(fromPlace);
            ptviewObj.setDateMark(DateUtil.dateToShortString(travelDate).replaceAll("-", ""));
            ptviewObj.setImage(image);
            ptviewObj.setDetailImage("");
            ptviewObj.setBasePrice(basePrice);
            ptviewObj.setIsOpen("关");
            ptviewObj.setAddDate(date);
            ptviewObj.setAlterDate(date);
            commonDao.save(ptviewObj);

            possessorUtil.savePossessorRelates(Constant.relate_type_ptview, ptviewObj.getNumberCode());
            LogWork logWork = new LogWork();
            logWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
            logWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
            logWork.setValue(ptviewObj.getNumberCode());
            logWork.setTableName("ptview");
            logWork.setRemark("新增产品信息【 ["+cruiseNameCn+"]"+travelDate+" 】");
            logWork.setAddDate(date);
            commonDao.save(logWork);
        }

        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> editPtview(String numberCode , String nameCn ) {
        if (StringTool.isEmpty(nameCn)) {
            return ReturnUtil.returnMap(0, "产品名称不能为空!");
        }
       
       

        Date date = new Date();
        Ptview ptviewObj = (Ptview)commonDao.getObjectByUniqueCode("Ptview", "numberCode", numberCode);
        if (ptviewObj == null) {
            return ReturnUtil.returnMap(0, "产品对象不存在!");
        }

        ptviewObj.setNameCn(nameCn);

        ptviewObj.setAlterDate(date);
        commonDao.save(ptviewObj);

        LogWork logWork = new LogWork();
        logWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
        logWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
        logWork.setValue(ptviewObj.getNumberCode());
        logWork.setTableName("ptview");
        logWork.setRemark("编辑产品名字为"+nameCn);
        logWork.setAddDate(date);
        commonDao.save(logWork);

        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> deletePtview(String numberCodes) {
        if (StringTool.isEmpty(numberCodes)) {
            return ReturnUtil.returnMap(0, "请选择要删除的产品信息!");
        }

        String hql = " from PtviewSku ps ";
        hql += "where ps.ptview in (" + StringTool.StrToSqlString(numberCodes) + ") ";
        hql += possessorUtil.getRelatesWhereHql("ptview_sku");
        @SuppressWarnings("unchecked")
        List<PtviewSku> skuList = commonDao.queryByHql(hql);

        List<String> skuNumberList = new ArrayList<String>();
        for (PtviewSku sku : skuList) {
            skuNumberList.add(sku.getNumberCode());
        }

        /* 删除SKU日历库存 */
        String skuStockSql = "DELETE FROM ptview_sku_stock WHERE ptview_sku ";
        skuStockSql += "IN (" + StringTool.listToSqlString(skuNumberList) + ")";
        skuStockSql += possessorUtil.getRelatesWhereSql("ptview_sku_stock");
        commonDao.getSqlQuery(skuStockSql).executeUpdate();

        /* 删除SKU套餐 */
        String skuSql = "DELETE FROM ptview_sku WHERE ptview ";
        skuSql += "IN (" + StringTool.StrToSqlString(numberCodes) + ")";
        skuSql += possessorUtil.getRelatesWhereSql("ptview_sku");
        commonDao.getSqlQuery(skuSql).executeUpdate();

        //TODO 删除关联表
        /* 删除SKU关联的单品种类 */
        String skuTypeSql = "DELETE FROM ptview_sku_tkview_type WHERE ptview_sku ";
        skuTypeSql += "IN (" + StringTool.listToSqlString(skuNumberList) + ")";
        commonDao.getSqlQuery(skuTypeSql).executeUpdate();

        /* 删除产品 */
        String deleteSql = "DELETE FROM ptview WHERE number_code ";
        deleteSql += "IN (" + StringTool.StrToSqlString(numberCodes) + ") ";
        deleteSql += possessorUtil.getRelatesWhereSql("ptview");
        commonDao.getSqlQuery(deleteSql).executeUpdate();

        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> getPtviewSkuByNumberCode(String numberCode) {
        if(StringTool.isEmpty(numberCode)){
            return ReturnUtil.returnMap(0, "请选择要克隆的产品!");
        }
        
        String skuHql = "from PtviewSku s where s.ptview = '"+ numberCode +"' ";
        skuHql += possessorUtil.getRelatesWhereHql("ptview_sku");
        @SuppressWarnings("unchecked")
        List<PtviewSku> skuList = commonDao.queryByHql(skuHql);
        
        for(PtviewSku skuObj : skuList){
            String stockHql = "from PtviewSkuStock s where s.ptviewSku = '"+ skuObj.getNumberCode() +"' ";
            stockHql += possessorUtil.getRelatesWhereHql("ptview_sku_stock");
            @SuppressWarnings("unchecked")
            List<PtviewSkuStock> stockList = commonDao.queryByHql(stockHql);
            skuObj.setStockList(stockList);
        }
        
        return ReturnUtil.returnMap(1, skuList);
    }

    @Override
    public Map<String, Object> clonePtviewOper(String numberCode , String ptview , String skuJson) {
        if(StringTool.isEmpty(numberCode)){
            return ReturnUtil.returnMap(0, "请选择要克隆的产品!");
        }
        if(StringTool.isEmpty(ptview)){
            return ReturnUtil.returnMap(0, ConstantMsg.ptview_null);
        }
        
        Date date = new Date();
        Ptview oldPtview = (Ptview)commonDao.getObjectByUniqueCode("Ptview", "numberCode", numberCode);
        if(oldPtview == null){
            return ReturnUtil.returnMap(0, "克隆的产品不存在!");
        }
        
        Ptview newPtview = (Ptview)commonDao.getObjectByUniqueCode("Ptview", "numberCode", ptview);
        if(newPtview == null){
            return ReturnUtil.returnMap(0, "克隆的产品不存在!");
        }
        Shipment shipObj = (Shipment)commonDao.getObjectByUniqueCode("Shipment", "numberCode", newPtview.getShipment()); 
        
        String oldSkuHql = "from PtviewSku ps where ps.ptview = '"+ numberCode +"' ";
        oldSkuHql += possessorUtil.getRelatesWhereHql("ptview_sku");
        @SuppressWarnings("unchecked")
        List<PtviewSku> oldSkuList = commonDao.queryByHql(oldSkuHql);
        
        List<String> oldSkuNumber = new ArrayList<>();
        for(PtviewSku sku : oldSkuList){
            oldSkuNumber.add(sku.getNumberCode());
        }
        
        //TODO 删除关联表
        String oldTypeHql = "from PtviewSku_TkviewType where ptviewSku in ("+ StringTool.listToSqlString(oldSkuNumber) +") ";
        @SuppressWarnings("unchecked")
        List<PtviewSku_TkviewType> oldTypeList = commonDao.queryByHql(oldTypeHql);
        
        @SuppressWarnings("unchecked")
        List<Map<String ,String>> skuMap = JSON.toObject(skuJson, ArrayList.class);
        for(Map<String ,String> map : skuMap){
            String skuNumberCode = (String)map.get("skuNumber");
            PtviewSku skuObj = null;
            for(PtviewSku oldSku : oldSkuList){
                if(oldSku.getNumberCode().equals(skuNumberCode)){
                    skuObj = (PtviewSku)ClazzUtil.cloneObj(oldSku, "iidd");
                    break;
                }
            }
            if(skuObj == null){
                continue;
            }
            
            String skuHql = "from PtviewSku s where s.nameCn ='"+ skuObj.getNameCn() +"' ";
            skuHql += "and s.ptview = '"+ ptview +"' ";
            skuHql += possessorUtil.getRelatesWhereHql("ptview_sku");
            Query countQuery = commonDao.getQuery("select count(*) " + skuHql);
            Number countNum = (Number)countQuery.uniqueResult();
            int skuInfocount = countNum == null ? 0 : countNum.intValue();
            if (skuInfocount != 0) {
                @SuppressWarnings("unchecked")
                List<PtviewSku> skuList = commonDao.queryByHql(skuHql);
                skuObj = skuList.get(0);
                commonDao.save(skuObj);
            }else{
                skuObj.setPtview(ptview);
                skuObj.setNumberCode(StringUtil.get_new_ptview_sku());
                skuObj.setAddDate(date);
                skuObj.setAlterDate(date);
                commonDao.save(skuObj);
                
                LogWork logWork = new LogWork();
                logWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
                logWork.setValue(ptview);
                logWork.setTableName("ptview");
                logWork.setRemark("【"+ newPtview.getNameCn() +"】产品克隆SKU套餐【"+ skuObj.getNameCn() +"】");
                logWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
                logWork.setAddDate(date);
                commonDao.save(logWork);
            }
            
            for(PtviewSku_TkviewType type : oldTypeList){
                PtviewSku_TkviewType typeObj = null;
                if(type.getPtviewSku().equals(skuNumberCode)){
                    typeObj = (PtviewSku_TkviewType)ClazzUtil.cloneObj(type, "iidd");
                }
                if(typeObj == null){
                    continue;
                }
                typeObj.setNumberCode(UUIDUtil.getUUIDStringFor32());
                typeObj.setPtviewSku(skuObj.getNumberCode());
                typeObj.setAddDate(date);
                typeObj.setAlterDate(date);
                commonDao.save(typeObj);
            }
            
            String stockStr = (String)map.get("stock");
            String oldStockHql = "from PtviewSkuStock s where s.numberCode in ("+ StringTool.StrToSqlString(stockStr) +") ";
            oldStockHql += possessorUtil.getRelatesWhereHql("ptview_sku_stock");
            @SuppressWarnings("unchecked")
            List<PtviewSkuStock> oldStockList = commonDao.queryByHql(oldStockHql);
            
            PtviewSkuStock stockObj = null;
            for(PtviewSkuStock oldStock : oldStockList){
                if(oldStock.getPtviewSku().equals(skuNumberCode)){
                    stockObj = (PtviewSkuStock)ClazzUtil.cloneObj(oldStock, "iidd");
                    break;
                }
            }
            if(stockObj == null){
                continue;
            }
            stockObj.setPtviewSku(skuObj.getNumberCode());
            stockObj.setNumberCode(StringUtil.get_new_taobao_price_out_num());
            stockObj.setDate(shipObj.getTravelDate());
            stockObj.setAddDate(date);
            stockObj.setAlterDate(date);
            commonDao.save(stockObj);
            
            LogWork log_work = new LogWork();
            log_work.setAddDate(new Date());
            log_work.setNumberCode(UUIDUtil.getUUIDStringFor32());
            log_work.setValue(stockObj.getNumberCode());
            log_work.setRemark("克隆产品SKU【"+skuObj.getNameCn()+"】日历库存：日期为【"+ DateUtil.dateToString(shipObj.getTravelDate()) +"】的库存");
            log_work.setTableName("ptview");
            log_work.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
            commonDao.save(log_work);
        }
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> syncPtviewOper(String numberCode , String ptview , String skuJson) {
        if(StringTool.isEmpty(numberCode)){
            return ReturnUtil.returnMap(0, "请选择要同步的产品!");
        }
        if(StringTool.isEmpty(ptview)){
            return ReturnUtil.returnMap(0, ConstantMsg.ptview_null);
        }
        
        Date date = new Date();
        Ptview oldPtview = (Ptview)commonDao.getObjectByUniqueCode("Ptview", "numberCode", numberCode);
        if(oldPtview == null){
            return ReturnUtil.returnMap(0, "同步产品不存在!");
        }
        String oldSkuHql = "from PtviewSku ps where ps.ptview = '"+ numberCode +"' ";
        @SuppressWarnings("unchecked")
        List<PtviewSku> oldSkuList = commonDao.queryByHql(oldSkuHql);
        
        List<String> oldSkuNumber = new ArrayList<>();
        for(PtviewSku sku : oldSkuList){
            oldSkuNumber.add(sku.getNumberCode());
        }
        
        //TODO 删除关联表
        String oldTypeHql = "from PtviewSku_TkviewType where ptviewSku in ("+ StringTool.listToSqlString(oldSkuNumber) +") ";
        @SuppressWarnings("unchecked")
        List<PtviewSku_TkviewType> oldTypeList = commonDao.queryByHql(oldTypeHql);
       
        Ptview newPtview = (Ptview)commonDao.getObjectByUniqueCode("Ptview", "numberCode", ptview);
        if(newPtview == null){
            return ReturnUtil.returnMap(0, "产品信息不存在!");
        }
        Shipment shipObj = (Shipment)commonDao.getObjectByUniqueCode("Shipment", "numberCode", newPtview.getShipment()); 
        
        String newSkuHql = "from PtviewSku where ptview = '"+ ptview +"' ";
        @SuppressWarnings("unchecked")
        List<PtviewSku> newSkuList = commonDao.queryByHql(newSkuHql);
        List<String> skuNumber = new ArrayList<>();
        for(PtviewSku sku : newSkuList){
            skuNumber.add(sku.getNumberCode());
        }
        
        //删除要同步到的产品SKU库存数据
        String stockDelete = "DELETE FROM ptview_sku_stock WHERE ptview_sku IN ("+ StringTool.listToSqlString(skuNumber) +") ";
        commonDao.getSqlQuery(stockDelete).executeUpdate();
        
        //删除要同步到的产品SKU数据
        String skuDelete = "DELETE FROM ptview_sku WHERE ptview = '"+ ptview +"'";
        commonDao.getSqlQuery(skuDelete).executeUpdate();
       
        @SuppressWarnings("unchecked")
        List<Map<String ,String>> skuMap = JSON.toObject(skuJson, ArrayList.class);
        for(Map<String ,String> map : skuMap){
            String skuNumberCode = (String)map.get("skuNumber");
            PtviewSku skuObj = null;
            for(PtviewSku oldSku : oldSkuList){
                if(oldSku.getNumberCode().equals(skuNumberCode)){
                    skuObj = (PtviewSku)ClazzUtil.cloneObj(oldSku, "iidd");
                    break;
                }
            }
            if(skuObj == null){
                continue;
            }
            
            skuObj.setPtview(ptview);
            skuObj.setNumberCode(StringUtil.get_new_ptview_sku());
            skuObj.setAddDate(date);
            skuObj.setAlterDate(date);
            commonDao.save(skuObj);
            
            LogWork logWork = new LogWork();
            logWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
            logWork.setValue(ptview);
            logWork.setTableName("ptview");
            logWork.setRemark("【"+ newPtview.getNameCn() +"】产品同步SKU套餐【"+ skuObj.getNameCn() +"】");
            logWork.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
            logWork.setAddDate(date);
            commonDao.save(logWork);
            
            for(PtviewSku_TkviewType type : oldTypeList){
                PtviewSku_TkviewType typeObj = null;
                if(type.getPtviewSku().equals(skuNumberCode)){
                    typeObj = (PtviewSku_TkviewType)ClazzUtil.cloneObj(type, "iidd");
                }
                if(typeObj == null){
                    continue;
                }
                typeObj.setNumberCode(UUIDUtil.getUUIDStringFor32());
                typeObj.setPtviewSku(skuObj.getNumberCode());
                typeObj.setAddDate(date);
                typeObj.setAlterDate(date);
                commonDao.save(typeObj);
            }
            
            String stockStr = (String)map.get("stock");
            String oldStockHql = "from PtviewSkuStock s where s.numberCode in ("+ StringTool.StrToSqlString(stockStr) +") ";
            @SuppressWarnings("unchecked")
            List<PtviewSkuStock> oldStockList = commonDao.queryByHql(oldStockHql);
            
            PtviewSkuStock stockObj = null;
            for(PtviewSkuStock oldStock : oldStockList){
                if(oldStock.getPtviewSku().equals(skuNumberCode)){
                    stockObj = (PtviewSkuStock)ClazzUtil.cloneObj(oldStock, "iidd");
                    break;
                }
            }
            if(stockObj == null){
                continue;
            }
            stockObj.setPtviewSku(skuObj.getNumberCode());
            stockObj.setNumberCode(StringUtil.get_new_taobao_price_out_num());
            stockObj.setDate(shipObj.getTravelDate());
            stockObj.setAddDate(date);
            stockObj.setAlterDate(date);
            commonDao.save(stockObj);
            
            LogWork log_work = new LogWork();
            log_work.setAddDate(new Date());
            log_work.setNumberCode(UUIDUtil.getUUIDStringFor32());
            log_work.setValue(stockObj.getNumberCode());
            log_work.setRemark("同步产品SKU【"+skuObj.getNameCn()+"】日历库存：日期为【"+ DateUtil.dateToString(shipObj.getTravelDate()) +"】的库存");
            log_work.setTableName("ptview");
            log_work.setUsername(MyRequestUtil.getSecurityLogin().getUsername());
            commonDao.save(log_work);
        }
        
        return ReturnUtil.returnMap(1, null);
    }
    
    @Override
    public Map<String, Object> getSortNumCruise(int offset , int pageSize) {
        String hql = "from Cruise order by sortNum desc ";

        String countHql = "select count(*) " + hql;
        Query countQuery = commonDao.getQuery(countHql);
        Number countNum = (Number)countQuery.uniqueResult();
        int count = countNum == null ? 0 : countNum.intValue();

        Query query = commonDao.getQuery(hql.toString());
        query.setMaxResults(pageSize);
        query.setFirstResult(offset);
        @SuppressWarnings("unchecked")
        List<Cruise> cruiseList = query.list();

        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", count);
        mapContent.put("rows", cruiseList);

        return ReturnUtil.returnMap(1, mapContent);
    }

    @Override
    public Map<String, Object> editSortNumCruise(String numberCodes) {
        if(StringTool.isEmpty(numberCodes)){
            return ReturnUtil.returnMap(0, "邮轮信息不能为空!");
        }
        
        String[] cruiseArry = numberCodes.split(",");
        int maxNum = cruiseArry.length;
        for (String number : cruiseArry) {
            Cruise cruiseObj = (Cruise)commonDao.getObjectByUniqueCode("Cruise", "numberCode", number);
            if(cruiseObj == null){
                return ReturnUtil.returnMap(0, "编号为【"+number+"】的邮轮对象不存在!");
            }
            cruiseObj.setSortNum(maxNum);
            cruiseObj.setAlterDate(new Date());
            commonDao.update(cruiseObj);
            maxNum--;
        }
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> editIsOpen(String numberCode) {
        
        Ptview ptviewObj = (Ptview)commonDao.getObjectByUniqueCode("Ptview", "numberCode", numberCode);
        String isOpen = ptviewObj.getIsOpen();
        isOpen = "开".equals(isOpen)==true?"关":"开";
        ptviewObj.setIsOpen(isOpen);
        commonDao.update(ptviewObj);
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> editImage(String numberCode , String urls) {

        if (StringTool.isEmpty(numberCode)) {
            return ReturnUtil.returnMap(0, ConstantMsg.ptview_numberCode_null);
        }
        Ptview ptviewObj = (Ptview)commonDao.getObjectByUniqueCode("Ptview", "numberCode", numberCode);
        if (ptviewObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.ptview_null);
        }
        ptviewObj.setImage(urls);
        ptviewObj.setAlterDate(new Date());
        commonDao.update(ptviewObj);

        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> editDetailImages(String numberCode , String urls) {
        if (StringTool.isEmpty(numberCode)) {
            return ReturnUtil.returnMap(0, ConstantMsg.ptview_numberCode_null);
        }
        Ptview ptviewObj = (Ptview)commonDao.getObjectByUniqueCode("Ptview", "numberCode", numberCode);
        if (ptviewObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.ptview_null);
        }
        ptviewObj.setDetailImage(urls);
        ptviewObj.setAlterDate(new Date());
        commonDao.update(ptviewObj);

        return ReturnUtil.returnMap(1, null);
    }

}