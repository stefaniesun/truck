package xyz.work.security.svc.imp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.SellercatsListGetRequest;
import com.taobao.api.response.SellercatsListGetResponse;

import xyz.dao.CommonDao;
import xyz.exception.MyExceptionForXyz;
import xyz.filter.JSON;
import xyz.filter.MyRequestUtil;
import xyz.filter.ReturnUtil;
import xyz.util.Constant;
import xyz.util.ConstantMsg;
import xyz.util.DateUtil;
import xyz.util.ListNumberCode;
import xyz.util.PossessorUtil;
import xyz.util.StringTool;
import xyz.work.base.model.Cruise;
import xyz.work.base.model.Shipment;
import xyz.work.resources.model.Channel;
import xyz.work.resources.model.TkviewType;
import xyz.work.security.svc.ListSvc;
import xyz.work.sell.model.Ptview;
import xyz.work.sell.model.PtviewSku;
import xyz.work.sell.model.PtviewSku_TkviewType;
import xyz.work.taobao.model.Sku_TkviewType;
import xyz.work.taobao.model.TaobaoBaseInfo;
import xyz.work.taobao.model.TaobaoSkuInfo;

@Service
public class ListSvcImp implements ListSvc{
    @Autowired
    private CommonDao commonDao;
    
    @Autowired
    PossessorUtil possessorUtil;

    @Override
    public Map<String, Object> getCompanyList(String q, String cruise) {
        String numberCode = "";
        if(StringTool.isNotNull(cruise)){
            Cruise cruiseObj = (Cruise)commonDao.getObjectByUniqueCode("Cruise", "numberCode", cruise);
            numberCode = cruiseObj.getCompany();
        }
        
        String sql = "SELECT c.name_cn AS text,c.number_code As value FROM company c WHERE 1=1 ";
        if(StringTool.isNotNull(numberCode)){
            sql += "AND c.number_code = '"+ numberCode +"' ";
        }
        if (StringTool.isNotNull(q)) {
            String[] strs = q.split(" ");
            for (int i = 0; i < strs.length; i++ ) {
                if (!"".equals(strs[i].trim())) {
                    sql += "AND c.name_cn like '%" + strs[i] + "%' ";
                }
            }
        }
        
        SQLQuery query = commonDao.getSqlQuery(sql);
        query.addScalar("value").addScalar("text").setResultTransformer(
            Transformers.aliasToBean(ListNumberCode.class));
        @SuppressWarnings("unchecked")
        List<ListNumberCode> results = query.list();

        return ReturnUtil.returnMap(1, results);
    }

    @Override
    public Map<String, Object> getCruiseList(String q) {
        String sql = "SELECT c.number_code AS value,CONCAT('【',c.mark,'】',c.name_cn) AS text ";
        sql += "FROM cruise c WHERE 1=1 ";
        if (StringTool.isNotNull(q)) {
            String[] strs = q.split(" ");
            for (int i = 0; i < strs.length; i++ ) {
                if (!"".equals(strs[i].trim())) {
                    sql += "AND CONCAT('【',c.mark,'】',c.name_cn) LIKE '%" + strs[i] + "%' ";
                }
            }
        }
        sql += "ORDER BY c.alter_date DESC";

        SQLQuery query = commonDao.getSqlQuery(sql);
        query.addScalar("value").addScalar("text").setResultTransformer(
            Transformers.aliasToBean(ListNumberCode.class));
        @SuppressWarnings("unchecked")
        List<ListNumberCode> results = query.list();

        return ReturnUtil.returnMap(1, results);
    }
    
    @Override
    public Map<String, Object> getPortList(String q, String type) {
        
        String sql = "SELECT p.name_cn AS text,p.number_code As value FROM port p where 1=1 ";
        if(StringTool.isNotNull(type) && "1".equals(type)){//type == 1时，查除了 海上巡游 的所有港口;为其他值的时候或者空表示查所有数据
            sql += "AND p.name_cn <> '海上巡游' ";
        }
        if (StringTool.isNotNull(q)) {
            String[] strs = q.split(" ");
            for (int i = 0; i < strs.length; i++ ) {
                if (!"".equals(strs[i].trim())) {
                    sql += "AND p.name_cn LIKE '%" + strs[i] + "%' ";
                }
            }
        }
        
        SQLQuery query = commonDao.getSqlQuery(sql);
        query.addScalar("value").addScalar("text").setResultTransformer(
            Transformers.aliasToBean(ListNumberCode.class));
        @SuppressWarnings("unchecked")
        List<ListNumberCode> results = query.list();

        return ReturnUtil.returnMap(1, results);
    }
    
    @Override
    public Map<String, Object> getAreaList(String q) {
        String sql = "SELECT a.name_cn AS text,a.number_code As value FROM area a where 1=1";
        if (StringTool.isNotNull(q)) {
            String[] strs = q.split(" ");
            for (int i = 0; i < strs.length; i++ ) {
                if (!"".equals(strs[i].trim())) {
                    sql += " AND a.name_cn LIKE '%" + strs[i] + "%'";
                }
            }
        }
        
        SQLQuery query = commonDao.getSqlQuery(sql);
        query.addScalar("value").addScalar("text").setResultTransformer(
            Transformers.aliasToBean(ListNumberCode.class));
        @SuppressWarnings("unchecked")
        List<ListNumberCode> results = query.list();

        return ReturnUtil.returnMap(1, results);
    }
    
    @Override
    public Map<String,Object> getRouteList(String airway,String q){
        if(StringTool.isEmpty(airway)){
            return ReturnUtil.returnMap(0, ConstantMsg.airway_numberCode_null);
        }
        String sql = "SELECT t.name_cn AS text,t.number_code As value FROM route t where 1=1";
        sql += " AND t.airway = '"+ airway +"'";
        if (StringTool.isNotNull(q)) {
            String[] strs = q.split(" ");
            for (int i = 0; i < strs.length; i++ ) {
                if (!"".equals(strs[i].trim())) {
                    sql += " AND t.name_cn LIKE '%" + strs[i] + "%'";
                }
            }
        }
        
        SQLQuery query = commonDao.getSqlQuery(sql);
        query.addScalar("value").addScalar("text").setResultTransformer(
            Transformers.aliasToBean(ListNumberCode.class));
        @SuppressWarnings("unchecked")
        List<ListNumberCode> results = query.list();
        
        return ReturnUtil.returnMap(1, results);
    }
    
    @Override
    public Map<String, Object> getProviderList(String q) {
        
        String sql = "SELECT p.name_cn AS text,p.number_code AS value FROM provider p WHERE 1=1 ";
        sql += " and status = 1 ";
        sql += possessorUtil.getRelatesWhereSql(Constant.relate_type_provider,"p");
        
        if (q != null && !"".equals(q)) {
            String[] strs = q.split(" ");
            for (int i = 0; i < strs.length; i++ ) {
                if (!"".equals(strs[i].trim())) {
                    sql += " AND p.name_cn LIKE '%" + strs[i] + "%' ";
                }
            }
        }
        sql += " ORDER BY p.alter_date DESC ";

        SQLQuery query = commonDao.getSqlQuery(sql);
        query.addScalar("value").addScalar("text").setResultTransformer(
            Transformers.aliasToBean(ListNumberCode.class));
        @SuppressWarnings("unchecked")
        List<ListNumberCode> results = query.list();

        return ReturnUtil.returnMap(1, results);
    }
    
    @Override
    public Map<String, Object> getPossessorList(String q) {
        MyRequestUtil.decidePowerIsAll();
        String sql = "select name_cn as text,number_code as value from possessor p where 1 = 1 ";
        if (q != null && !"".equals(q)) {
            String[] strs = q.split(" ");
            for (int i = 0; i < strs.length; i++ ) {
                if (!"".equals(strs[i].trim())) {
                    sql += " AND p.name_cn LIKE '%" + strs[i] + "%'";
                }
            }
        }
        sql += " ORDER BY p.alter_date DESC";

        SQLQuery query = commonDao.getSqlQuery(sql);
        query.setMaxResults(Constant.list_max_result);
        query.addScalar("value").addScalar("text").setResultTransformer(
            Transformers.aliasToBean(ListNumberCode.class));
        @SuppressWarnings("unchecked")
        List<ListNumberCode> results = query.list();

        return ReturnUtil.returnMap(1, results);
    }

    @Override
    public Map<String, Object> getChannelList(String q, String isMeituan) {
        
        String sql = "SELECT name_cn AS text,number_code AS value FROM channel c WHERE 1=1 ";
        if(StringTool.isNotNull(isMeituan)){
            sql += "AND c.number_code <> 'meituan' ";
        }
        sql += possessorUtil.getRelatesWhereSql(Constant.relate_type_channel);
        
        if (q != null && !"".equals(q)) {
            String[] strs = q.split(" ");
            for (int i = 0; i < strs.length; i++ ) {
                if (!"".equals(strs[i].trim())) {
                    sql += " AND c.name_cn LIKE '%" + strs[i] + "%'";
                }
            }
        }
        sql += " ORDER BY c.alter_date DESC ";

        SQLQuery query = commonDao.getSqlQuery(sql);
        query.addScalar("value").addScalar("text").setResultTransformer(
            Transformers.aliasToBean(ListNumberCode.class));
        @SuppressWarnings("unchecked")
        List<ListNumberCode> results = query.list();

        return ReturnUtil.returnMap(1, results);
    }

    @Override
    public Map<String, Object> getTkviewList(String q) {
        
        String sql = "select name_cn as text,number_code as value from tkview where 1=1 ";
        
        sql += possessorUtil.getRelatesWhereSql(Constant.relate_type_tkview);
        
        if (q != null && !"".equals(q)) {
            String[] strs = q.split(" ");
            for (int i = 0; i < strs.length; i++ ) {
                if (!"".equals(strs[i].trim())) {
                    sql += " AND name_cn LIKE '%" + strs[i] + "%'";
                }
            }
        }
        sql += " ORDER BY alter_date DESC ";

        SQLQuery query = commonDao.getSqlQuery(sql);
        query.addScalar("value").addScalar("text").setResultTransformer(
            Transformers.aliasToBean(ListNumberCode.class));
        @SuppressWarnings("unchecked")
        List<ListNumberCode> results = query.list();

        return ReturnUtil.returnMap(1, results);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getPtviewList(String q,String shipment) {
        
        String sql = "SELECT name_cn AS text,number_code AS value FROM ptview WHERE 1 = 1 ";
        sql += possessorUtil.getRelatesWhereSql(Constant.relate_type_ptview);
        if(StringTool.isNotNull(shipment)){
            sql += " AND shipment = '"+ shipment +"'";
        }
        
        if (StringTool.isNotNull(q)) {
            String[] strs = q.split(" ");
            for (int i = 0; i < strs.length; i++ ) {
                if (!"".equals(strs[i].trim())) {
                    sql += " AND name_cn LIKE '%" + strs[i] + "%'";
                }
            }
        }
        sql += " ORDER BY alter_date DESC ";

        SQLQuery query = commonDao.getSqlQuery(sql);
        query.addScalar("value").addScalar("text")
             .setResultTransformer(Transformers.aliasToBean(ListNumberCode.class));
        List<ListNumberCode> results = query.list();

        return ReturnUtil.returnMap(1, results);
    }

    @Override
    public Map<String, Object> getTaobaoProductList(String q) {

        String sql = "SELECT title AS text,(SELECT item_id FROM taobao_travel_item tti WHERE tti.number_code = t.taobao_travel_item) AS value ";
        sql += "FROM taobao_base_info t WHERE 1=1 ";
        {
            List<String> channels = possessorUtil.getDecideMap(Constant.relate_type_channel).get(Constant.relate_type_channel);
            String sql1 = "select tt.number_code from taobao_travel_item tt where tt.channel in ("+StringTool.listToSqlString(channels)+")";
            @SuppressWarnings("unchecked")
            List<String> taobaoTravelItemNumberCodes = commonDao.getSqlQuery(sql1).list();
            sql += " AND t.taobao_travel_item IN ("+StringTool.listToSqlString(taobaoTravelItemNumberCodes)+")";
        }
        
        if (q != null && !"".equals(q)) {
            String[] strs = q.split(" ");
            for (int i = 0; i < strs.length; i++ ) {
                if (!"".equals(strs[i].trim())) {
                    sql += " AND t.name_cn LIKE '%" + strs[i] + "%'";
                }
            }
        }
        sql += " ORDER BY t.alter_date DESC";

        SQLQuery query = commonDao.getSqlQuery(sql);
        query.addScalar("value")
             .addScalar("text")
             .setResultTransformer(Transformers.aliasToBean(ListNumberCode.class));
        @SuppressWarnings("unchecked")
        List<ListNumberCode> results = query.list();

        return ReturnUtil.returnMap(1, results);
    }
    
    @Override
    public Map<String, Object> getAirwayList(String q,String area) {

        String sql = "SELECT name_cn AS text,number_code AS value FROM airway a WHERE 1=1 ";
        
        if (q != null && !"".equals(q)) {
            String[] strs = q.split(" ");
            for (int i = 0; i < strs.length; i++ ) {
                if (!"".equals(strs[i].trim())) {
                    sql += " AND a.name_cn LIKE '%" + strs[i] + "%'";
                }
            }
        }
        if (area != null && !"".equals(area)) {
			sql += " AND a.area = '"+area+"'"; 
		}
        sql += " ORDER BY a.alter_date DESC";

        SQLQuery query = commonDao.getSqlQuery(sql);
        query.addScalar("value").addScalar("text").setResultTransformer(
            Transformers.aliasToBean(ListNumberCode.class));
        @SuppressWarnings("unchecked")
        List<ListNumberCode> results = query.list();

        return ReturnUtil.returnMap(1, results);
    }
    
    @Override
    public Map<String, Object> getDistributorNameAndPhoneList(String q) {
        String possessor = MyRequestUtil.getPossessor();
        String sql = "SELECT d.name_cn AS text,d.phone AS value FROM distributor d WHERE 1=1 and is_distributor = 1 and is_valid = 1";
        {
            if(StringTool.isNotNull(possessor)){
                List<String> relates = possessorUtil.getDecideMap(Constant.relate_type_distributor).get(Constant.relate_type_distributor);
                sql += " and username in ("+StringTool.listToSqlString(relates)+")";
            }
        }
        if (q != null && !"".equals(q)) {
            String[] strs = q.split(" ");
            for (int i = 0; i < strs.length; i++ ) {
                if (!"".equals(strs[i].trim())) {
                    sql += " AND d.customer LIKE '%" + strs[i] + "%'";
                }
            }
        }
        SQLQuery query = commonDao.getSqlQuery(sql);
        query.addScalar("value").addScalar("text").setResultTransformer(
            Transformers.aliasToBean(ListNumberCode.class));
        @SuppressWarnings("unchecked")
        List<ListNumberCode> results = query.list();

        return ReturnUtil.returnMap(1, results);
    }
    
    @Override
    public Map<String, Object> getShipmentList(String q, String cruise, BigDecimal isTkview) {
      
        String sql = "SELECT s.number_code AS value,CONCAT('【',s.mark,'】',DATE_FORMAT(s.travel_date,'%Y-%m-%d')) AS text ";
        sql += "FROM shipment s WHERE 1=1 ";
        if (StringTool.isNotNull(cruise)) {
            sql += "AND s.cruise = '" + cruise + "' ";
        }
        sql += "AND s.travel_date >= NOW() ";
        if (isTkview != null && isTkview.intValue() == 1) {
            String tkviewSql = "select shipment from tkview where 1=1 "+possessorUtil.getRelatesWhereSql(Constant.relate_type_tkview)+"";
            @SuppressWarnings("unchecked")
            List<String> tkviewList = commonDao.getSqlQuery(tkviewSql).list();
            
            sql += " AND s.number_code IN ("+StringTool.listToSqlString(tkviewList)+") ";
        }
        if (StringTool.isNotNull(q)) {
            String[] strs = q.split(" ");
            for (int i = 0; i < strs.length; i++ ) {
                if (!"".equals(strs[i].trim())) {
                    sql += "AND CONCAT('【',s.mark,'】',DATE_FORMAT(s.travel_date,'%Y-%m-%d')) LIKE '%" + strs[i] + "%' ";
                }
            }
        }
        sql += "ORDER BY s.travel_date ";
        
        SQLQuery query = commonDao.getSqlQuery(sql);
        query.addScalar("value").addScalar("text").
        setResultTransformer(Transformers.aliasToBean(ListNumberCode.class));
        @SuppressWarnings("unchecked")
        List<ListNumberCode> results = query.list();

        return ReturnUtil.returnMap(1, results);
    }
    
    @Override
    public Map<String, Object> getCabinListByCruise(String q , String cruise) {
        if (StringTool.isEmpty(cruise)) {
            return ReturnUtil.returnMap(0, Constant.list_cruise_not_null_error);
        }
        
        q = StringTool.isNotNull(q)?q:"";
        
        String sql = "SELECT t.number_code AS value,t.name_cn AS text FROM cabin t WHERE 1=1 ";
        if(!"noValue".equals(cruise)){
            sql += "AND t.cruise = '"+ cruise +"' ";
        }
        if(StringTool.isNotNull(q)){
            sql += "AND t.name_cn LIKE '%"+ q +"%'";
        }
        SQLQuery query = commonDao.getSqlQuery(sql);
        query.addScalar("value").addScalar("text").setResultTransformer(
            Transformers.aliasToBean(ListNumberCode.class));
        @SuppressWarnings("unchecked")
        List<ListNumberCode> results = query.list();

        return ReturnUtil.returnMap(1, results);
    }

    @Override
    public Map<String, Object> getCabinListByShipment(String q , String shipmnet , String isOpen) {
        
        if (StringTool.isEmpty(shipmnet)) {
            return ReturnUtil.returnMap(1, ConstantMsg.shipment_null);
        }
        
        String shipmentSql = "select cruise from shipment where number_code = '"+ shipmnet +"'";
        String cruiseCode = (String)commonDao.getSqlQuery(shipmentSql).uniqueResult();
        
        if (StringTool.isNotNull(cruiseCode)) {
            String cabinSql = "select name_cn as text,number_code as value from cabin where cruise = '"+ cruiseCode +"' ";
            if(StringTool.isNotNull(isOpen)){
                cabinSql += "and is_open = '"+ isOpen +"' ";
            }
            cabinSql += "ORDER BY type ";
            if (StringTool.isNotNull(q)) {
                String[] strs = q.split(" ");
                for (int i = 0; i < strs.length; i++ ) {
                    if (!"".equals(strs[i].trim())) {
                        cabinSql += " and name_cn like '%"+strs[i]+"%'";
                    }
                }
            }
            
            SQLQuery query = commonDao.getSqlQuery(cabinSql);
            query.addScalar("value").addScalar("text").setResultTransformer(
                Transformers.aliasToBean(ListNumberCode.class));
            @SuppressWarnings("unchecked")
            List<ListNumberCode> results = query.list();

            return ReturnUtil.returnMap(1, results);
            
        }
        
        return ReturnUtil.returnMap(1, "");
    }

    @Override
    public Map<String, Object> getTkviewListNotInNumber(String numberCode , String shipment ,String q) {
        StringBuilder sbSql = new StringBuilder();

        sbSql.append("SELECT number_code AS value,name_cn AS text FROM tkview WHERE 1 = 1 ");
        sbSql.append(possessorUtil.getRelatesWhereSql(Constant.relate_type_tkview));
        if(StringTool.isNotNull(numberCode)){
            sbSql.append(" AND number_code NOT IN ("+StringTool.StrToSqlString(numberCode)+")");
        }
        if(StringTool.isNotNull(shipment)){
            sbSql.append(" AND shipment ='"+ shipment +"'");
        }
        if (StringTool.isNotNull(q)) {
            String[] strs = q.split(" ");
            for (int i = 0; i < strs.length; i++ ) {
                if (!"".equals(strs[i].trim())) {
                    sbSql.append(" AND name_cn LIKE '%" + strs[i] + "%'");
                }
            }
        }
        SQLQuery query = commonDao.getSqlQuery(sbSql.toString());
        query.addScalar("text").addScalar("value").setResultTransformer(
            Transformers.aliasToBean(ListNumberCode.class));

        @SuppressWarnings("unchecked")
        List<ListNumberCode> results = query.list();

        return ReturnUtil.returnMap(1, results);
    }

    @Override
    public Map<String, Object> getCabinList(String q , BigDecimal isTkview) {
        
        q = StringTool.isNotNull(q)?q:"";
        String sql = "SELECT t.number_code AS value,t.name_cn AS text FROM cabin t WHERE t.name_cn LIKE '%"+q+"%'";
        
        if (isTkview != null && isTkview.intValue() == 1) {
		   	String cabinSql = "SELECT cabin FROM tkview where 1=1 ";
		   	cabinSql += possessorUtil.getRelatesWhereSql(Constant.relate_type_tkview);
		    @SuppressWarnings("unchecked")
			List<String> cabinList = commonDao.getSqlQuery(cabinSql).list();
		    sql += " and number_code in ("+StringTool.listToSqlString(cabinList)+")";
        }
        SQLQuery query = commonDao.getSqlQuery(sql);
        
        query.addScalar("value").addScalar("text").setResultTransformer(
            Transformers.aliasToBean(ListNumberCode.class));
        @SuppressWarnings("unchecked")
        List<ListNumberCode> results = query.list();

        return ReturnUtil.returnMap(1, results);
    }

    @Override
    public Map<String, Object> getPtviewShipmentList(String q) {
        String sql = "SELECT s.number_code AS value,CONCAT('【',s.mark,'】',";
        sql += " DATE_FORMAT(s.travel_date,'%Y-%m-%d')) AS text FROM shipment s WHERE 1=1";
        sql += " AND s.number_code IN (SELECT p.shipment FROM ptview p where 1=1 ";
        sql += possessorUtil.getRelatesWhereSql(Constant.relate_type_ptview, "p");
        sql += " GROUP BY p.shipment )";
        if (StringTool.isNotNull(q)) {
            String[] strs = q.split(" ");
            for (int i = 0; i < strs.length; i++ ) {
                if (!"".equals(strs[i].trim())) {
                    sql += " AND CONCAT('【',s.mark,'】',DATE_FORMAT(s.travel_date,'%Y-%m-%d')) LIKE '%" + strs[i] + "%'";
                }
            }
        }
        
        SQLQuery query = commonDao.getSqlQuery(sql);
        query.addScalar("value").addScalar("text").
        setResultTransformer(Transformers.aliasToBean(ListNumberCode.class));
        @SuppressWarnings("unchecked")
        List<ListNumberCode> results = query.list();

        return ReturnUtil.returnMap(1, results);
    }
    
    @Override
    public Map<String, Object> getTravelItemByChannel(String travelItem,String channel , String q) {
        
        if (StringTool.isEmpty(channel)) {
            return ReturnUtil.returnMap(0, "请选择渠道!");
        }
        
        String sql = "SELECT t.taobao_travel_item AS value,CONCAT(CONCAT('【',tt.channel_name_cn,'】'),IFNULL(t.title,'')) AS text FROM taobao_base_info t ";
        sql += " LEFT JOIN taobao_travel_item tt ON tt.number_code = t.taobao_travel_item WHERE 1=1";
        sql += " AND t.taobao_travel_item <> '"+travelItem+"'";
        sql += " AND tt.channel='"+channel+"'";
        
        if (q != null && !"".equals(q)) {
            String[] strs = q.split(" ");
            for (int i = 0; i < strs.length; i++ ) {
                if (!"".equals(strs[i].trim())) {
                    sql += " AND CONCAT(CONCAT('【',tt.channel_name_cn,'】'),IFNULL(t.title,'')) LIKE '%" + strs[i] + "%'";
                }
            }
        }
        SQLQuery query = commonDao.getSqlQuery(sql);
        query.addScalar("value").addScalar("text")
        .setResultTransformer(Transformers.aliasToBean(ListNumberCode.class));
        @SuppressWarnings("unchecked")
        List<ListNumberCode> results = query.list();

        return ReturnUtil.returnMap(1, results);
    }

    @Override
    public Map<String, Object> getShipmentByTaobaoSku(String q, String baseInfo, String sku) {
        
        TaobaoBaseInfo taobaoBaseInfo = (TaobaoBaseInfo)commonDao.getObjectByUniqueCode(Constant.taobaoBaseInfo, Constant.numberCode, baseInfo);
        if (taobaoBaseInfo == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.taobao_base_info_not_exist_error);
        }
        
        TaobaoSkuInfo taobaoSkuInfo = (TaobaoSkuInfo)commonDao.getObjectByUniqueCode(Constant.taobaoSkuInfo, Constant.numberCode, sku);
        if (taobaoSkuInfo == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.taobao_sku_info_not_exist_error);
        }
        
        String skuTkviewTypeHql = "from Sku_TkviewType st where st.sku = '"+ sku +"' ";
        @SuppressWarnings("unchecked")
        List<Sku_TkviewType> skuTkviewTypeList = commonDao.queryByHql(skuTkviewTypeHql);
        
        List<String> typeNumber = new ArrayList<String>();
        if(skuTkviewTypeList.size() > 0){
            for(Sku_TkviewType st : skuTkviewTypeList){
                typeNumber.add(st.getTkviewType());
            }
        }
        
        String tkviewTypeHql = "from TkviewType t where 1=1 ";
        if(typeNumber.size() > 0){
            tkviewTypeHql += "and t.numberCode in ("+ StringTool.listToSqlString(typeNumber) +") ";
        }
        tkviewTypeHql += "and t.cruise = '"+ taobaoBaseInfo.getCruise() +"' ";
        @SuppressWarnings("unchecked")
        List<TkviewType> tkviewTypeList = commonDao.queryByHql(tkviewTypeHql);
        
        List<String> cabinNumber = new ArrayList<String>();
        for(TkviewType type : tkviewTypeList){
            cabinNumber.add(type.getCabin());
        }
        
        String tkviewSql = "SELECT shipment AS value, ";
        tkviewSql += "DATE_FORMAT(shipment_travel_date,'%Y-%m-%d') AS text FROM tkview ";
        tkviewSql += "WHERE DATE_FORMAT(shipment_travel_date,'%Y-%m-%d') > NOW() ";
        tkviewSql += "AND cruise = '"+ taobaoBaseInfo.getCruise() +"' ";
        tkviewSql += possessorUtil.getRelatesWhereSql("tkview");
        if(cabinNumber.size() > 0){
            tkviewSql += "AND cabin IN ("+ StringTool.listToSqlString(cabinNumber) +") ";
        }
        if (StringTool.isNotNull(q)) {
            String[] strs = q.split(" ");
            for (int i = 0; i < strs.length; i++ ) {
                if (!"".equals(strs[i].trim())) {
                    tkviewSql += " AND DATE_FORMAT(shipment_travel_date,'%Y-%m-%d') LIKE '%" + strs[i] + "%' ";
                }
            }
        }
        tkviewSql += " GROUP BY shipment ";
        tkviewSql += "ORDER BY shipment_travel_date ";
        
        SQLQuery query = commonDao.getSqlQuery(tkviewSql);
        query.addScalar("value").addScalar("text")
        .setResultTransformer(Transformers.aliasToBean(ListNumberCode.class));
        @SuppressWarnings("unchecked")
        List<ListNumberCode> results = query.list();

        return ReturnUtil.returnMap(1, results);
    }

    @Override
    public Map<String, Object> getTkviewByShipmentAndCruise(String q , String shipment , String baseInfo , String skuInfo) {
    	
        TaobaoBaseInfo taobaoBaseInfo = (TaobaoBaseInfo)commonDao.getObjectByUniqueCode(Constant.taobaoBaseInfo, Constant.numberCode, baseInfo);
        if (taobaoBaseInfo == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.taobao_base_info_not_exist_error);
        }
        
        String skuTkviewTypeHql = "from Sku_TkviewType st where st.sku = '"+ skuInfo +"' ";
        @SuppressWarnings("unchecked")
        List<Sku_TkviewType> skuTkviewTypeList = commonDao.queryByHql(skuTkviewTypeHql);
        
        List<String> typeNumber = new ArrayList<String>();
        if(skuTkviewTypeList.size() > 0){
            for(Sku_TkviewType st : skuTkviewTypeList){
                typeNumber.add(st.getTkviewType());
            }
        }
        
        String tkviewTypeHql = "from TkviewType t where 1=1 ";
        if(typeNumber.size() > 0){
            tkviewTypeHql += "and t.numberCode in ("+ StringTool.listToSqlString(typeNumber) +") ";
        }
        @SuppressWarnings("unchecked")
        List<TkviewType> tkviewTypeList = commonDao.queryByHql(tkviewTypeHql);
        
        List<String> cabinNumber = new ArrayList<String>();
        for(TkviewType type : tkviewTypeList){
            cabinNumber.add(type.getCabin());
        }
        
        Shipment shipmentObj=(Shipment) commonDao.getObjectByUniqueCode("Shipment", "numberCode", shipment);
        if(shipmentObj==null){
              return ReturnUtil.returnMap(0, ConstantMsg.shipment_null);
        }
        
        String tkviewSql = "SELECT number_code AS value,name_cn AS text FROM tkview WHERE 1=1 ";
        tkviewSql += "AND cruise = '"+ taobaoBaseInfo.getCruise() +"' ";
        if(cabinNumber.size() > 0){
            tkviewSql += "AND cabin IN ("+ StringTool.listToSqlString(cabinNumber) +") ";
        }
        tkviewSql += "AND shipment_travel_date = '"+ DateUtil.dateToShortString(shipmentObj.getTravelDate()) +"' ";
        {
            tkviewSql += possessorUtil.getRelatesWhereSql("tkview");
        }
        if (StringTool.isNotNull(shipment)) {
            tkviewSql += " AND shipment = '"+ shipment +"' ";
        }
        if (StringTool.isNotNull(q)) {
            String[] strs = q.split(" ");
            for (int i = 0; i < strs.length; i++ ) {
                if (!"".equals(strs[i].trim())) {
                    tkviewSql += "AND name_cn LIKE '%" + strs[i] + "%'";
                }
            }
        }
        tkviewSql += possessorUtil.getRelatesWhereSql(Constant.relate_type_tkview);
        
        SQLQuery query = commonDao.getSqlQuery(tkviewSql);
        query.addScalar("value").addScalar("text")
        .setResultTransformer(Transformers.aliasToBean(ListNumberCode.class));
        @SuppressWarnings("unchecked")
        List<ListNumberCode> results = query.list();

        return ReturnUtil.returnMap(1, results);
    }

    @Override
    public Map<String, Object> getCidByChannel(String q , String channel) {
        Channel channelObj = (Channel)commonDao.getObjectByUniqueCode("Channel", "numberCode", channel);
        if (channelObj == null) {
            return ReturnUtil.returnMap(0, "渠道不存在 !");
        }
        
        TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", channelObj.getAppKey(), channelObj.getAppSecret());
        SellercatsListGetRequest req = new SellercatsListGetRequest();
        req.setNick(channelObj.getNameCn());
        SellercatsListGetResponse rsp;
        try {
            rsp = client.execute(req);
        }
        catch (ApiException e) {
            throw new MyExceptionForXyz(e.getMessage());
        }
        
        String jsonStr = rsp.getBody();
        
        @SuppressWarnings("unchecked")
        Map<String, Object> jsonMap = JSON.toObject(jsonStr, HashMap.class);
        
        @SuppressWarnings("unchecked")
        Map<String, Object> sellercatsListGetResponseMap = (Map<String, Object>)jsonMap.get("sellercats_list_get_response");
        
        @SuppressWarnings("unchecked")
        Map<String, Object> sellerCatsMap = (Map<String, Object>)sellercatsListGetResponseMap.get("seller_cats");
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> sellerCatList = (List<Map<String, Object>>)sellerCatsMap.get("seller_cat");
        
        List<ListNumberCode> cidList = new ArrayList<ListNumberCode>();
        
        for (Map<String, Object> cid : sellerCatList) {
            ListNumberCode listNumberCode = new ListNumberCode();
            
            String text = (String)cid.get("name");
            String value = (String)cid.get("cid");

            if (StringTool.isNotNull(text)) {
                listNumberCode.setText(text);
                listNumberCode.setValue(value);
            }
            
        }
        
        return ReturnUtil.returnMap(1, cidList);
    }

    @Override
    public Map<String, Object> getShipmentAirwayList(String q , String cruise) {
        String airwayNum = "SELECT airway FROM shipment WHERE cruise = '"+ cruise +"'";
        airwayNum += " GROUP BY airway";
        
        @SuppressWarnings("unchecked")
        List<String> airwayNumList = commonDao.getSqlQuery(airwayNum).list();
        
        String sql = "SELECT name_cn AS text,number_code AS value FROM airway a WHERE 1=1";
        sql += " AND a.number_code IN (" + StringTool.listToSqlString(airwayNumList) + ")";
        
        if (q != null && !"".equals(q)) {
            String[] strs = q.split(" ");
            for (int i = 0; i < strs.length; i++ ) {
                if (!"".equals(strs[i].trim())) {
                    sql += " AND a.name_cn LIKE '%" + strs[i] + "%'";
                }
            }
        }
        sql += " ORDER BY a.alter_date DESC";

        SQLQuery query = commonDao.getSqlQuery(sql);
        query.addScalar("value").addScalar("text").setResultTransformer(
            Transformers.aliasToBean(ListNumberCode.class));
        @SuppressWarnings("unchecked")
        List<ListNumberCode> results = query.list();

        return ReturnUtil.returnMap(1, results);
    }

    @Override
    public Map<String, Object> getShipmentCruiseList(String q) {
        
        String numberSql = "SELECT cruise FROM shipment GROUP BY cruise";
        @SuppressWarnings("unchecked")
        List<String> numberList = commonDao.getSqlQuery(numberSql).list();
        
        
        String sql = "SELECT c.number_code AS value,CONCAT('【',c.mark,'】',c.name_cn) AS text";
        sql += " FROM cruise c WHERE 1=1";
        sql += " AND c.number_code IN ("+ StringTool.listToSqlString(numberList) +")";
        if (StringTool.isNotNull(q)) {
            String[] strs = q.split(" ");
            for (int i = 0; i < strs.length; i++ ) {
                if (!"".equals(strs[i].trim())) {
                    sql += " AND CONCAT('【',c.mark,'】',c.name_cn) LIKE '%" + strs[i] + "%'";
                }
            }
        }
        sql += " ORDER BY c.alter_date DESC";

        SQLQuery query = commonDao.getSqlQuery(sql);
        query.addScalar("value").addScalar("text").setResultTransformer(
            Transformers.aliasToBean(ListNumberCode.class));
        @SuppressWarnings("unchecked")
        List<ListNumberCode> results = query.list();

        return ReturnUtil.returnMap(1, results);
    }

    @Override
    public Map<String, Object> getCabinByCruiseList(String q , String cruise) {
        String sql = "SELECT t.number_code AS value,t.name_cn AS text FROM ";
        sql += "cabin t WHERE 1=1 ";
        if(StringTool.isNotNull(cruise)){
            sql += "AND t.cruise = '"+ cruise +"' ";
        }
        if(StringTool.isNotNull(q)){
            sql += "AND t.name_cn LIKE '%"+ q +"%' ";
        }
        
        SQLQuery query = commonDao.getSqlQuery(sql);
        query.addScalar("value").addScalar("text").setResultTransformer(
            Transformers.aliasToBean(ListNumberCode.class));
        @SuppressWarnings("unchecked")
        List<ListNumberCode> results = query.list();

        return ReturnUtil.returnMap(1, results);
    }

    @Override
    public Map<String, Object> getTkviewTypeList(String q , String cruise) {
        if(StringTool.isEmpty(cruise)){
            return ReturnUtil.returnMap(0, "请先关联邮轮!");
        }
       
        String sql = "SELECT t.number_code AS value,c.name_cn AS text ";
        sql += "FROM tkview_type t ";
        sql += "LEFT JOIN cabin c ";
        sql += "ON t.cabin = c.number_code ";
        sql += "WHERE t.cruise = '"+ cruise +"' ";
        if(StringTool.isNotNull(q)){
            sql += "AND c.name_cn LIKE '%"+ q +"%' ";
        }
        
        SQLQuery query = commonDao.getSqlQuery(sql);
        query.addScalar("value").addScalar("text").setResultTransformer(
            Transformers.aliasToBean(ListNumberCode.class));
        @SuppressWarnings("unchecked")
        List<ListNumberCode> results = query.list();

        return ReturnUtil.returnMap(1, results);
    }
    
    @Override
    public Map<String, Object> getShipmentMouthList(String q , String cruise) {
        if(StringTool.isEmpty(cruise)){
            return ReturnUtil.returnMap(0, "邮轮选择错误!");
        }
        
        String sql = "SELECT DATE_FORMAT(s.travel_date,'%Y-%m') AS `value`, ";
        sql += "DATE_FORMAT(s.travel_date,'%Y-%m') AS text ";
        sql += "FROM  shipment s WHERE 1=1 ";
        if(!"noValue".equals(cruise)){
            sql += "AND s.cruise = '"+ cruise +"'  ";
        }
        sql += "AND s.travel_date >= NOW() ";
        if (StringTool.isNotNull(q)) {
            String[] strs = q.split(" ");
            for (int i = 0; i < strs.length; i++ ) {
                if (!"".equals(strs[i].trim())) {
                    sql += "AND DATE_FORMAT(s.travel_date,'%Y-%m') LIKE '%" + strs[i] + "%' ";
                }
            }
        }
        sql += "GROUP BY DATE_FORMAT(s.travel_date,'%Y-%m') ";
        
        SQLQuery query = commonDao.getSqlQuery(sql);
        query.addScalar("value").addScalar("text").setResultTransformer(
            Transformers.aliasToBean(ListNumberCode.class));
        @SuppressWarnings("unchecked")
        List<ListNumberCode> results = query.list();

        return ReturnUtil.returnMap(1, results);
    }

    @Override
    public Map<String, Object> getTkviewShipmentList(String q , String cruise , String mouth ,
                                                     BigDecimal isTkview) {
        if(StringTool.isEmpty(cruise)){
            return ReturnUtil.returnMap(0, "邮轮选择错误!");
        }
        
        String sql = "SELECT s.number_code AS value, ";
        sql += "DATE_FORMAT(s.travel_date,'%Y-%m-%d') AS text ";
        sql += "FROM shipment s WHERE 1=1 ";
        if(!cruise.equals("naValue")){
            sql += "AND s.cruise = '" + cruise + "' ";
        }
        sql += "AND s.travel_date >= NOW() ";
        if(StringTool.isNotNull(mouth)){
            sql += "AND DATE_FORMAT(s.travel_date,'%Y-%m') IN ("+ StringTool.StrToSqlString(mouth) +") ";
        }
        if (isTkview != null && isTkview.intValue() == 1) {
            String tkviewSql = "SELECT shipment FROM tkview WHERE 1=1 ";
            tkviewSql += possessorUtil.getRelatesWhereSql(Constant.relate_type_tkview);
            @SuppressWarnings("unchecked")
            List<String> tkviewList = commonDao.getSqlQuery(tkviewSql).list();
            
            sql += "AND number_code IN ("+StringTool.listToSqlString(tkviewList)+") ";
        }
        if (StringTool.isNotNull(q)) {
            String[] strs = q.split(" ");
            for (int i = 0; i < strs.length; i++ ) {
                if (!"".equals(strs[i].trim())) {
                    sql += "AND DATE_FORMAT(s.travel_date,'%Y-%m-%d') LIKE '%" + strs[i] + "%' ";
                }
            }
        }
        sql += "ORDER BY s.travel_date ";
        
        SQLQuery query = commonDao.getSqlQuery(sql);
        query.addScalar("value").addScalar("text").
        setResultTransformer(Transformers.aliasToBean(ListNumberCode.class));
        @SuppressWarnings("unchecked")
        List<ListNumberCode> results = query.list();

        return ReturnUtil.returnMap(1, results);
    }

    @Override
    public Map<String, Object> getShipmentByPtviewSku(String q , String ptviewSku) {
        PtviewSku skuObj = (PtviewSku)commonDao.getObjectByUniqueCode("PtviewSku", "numberCode", ptviewSku);
        if (skuObj == null) {
            return ReturnUtil.returnMap(0, "产品SKU套餐对象不存在!");
        }
        
        Ptview ptviewObj = (Ptview)commonDao.getObjectByUniqueCode("Ptview", "numberCode", skuObj.getPtview());
        if (ptviewObj == null) {
            return ReturnUtil.returnMap(0, "产品对象不存在!");
        }
        //TODO 删除关联表
        String skuTkviewTypeHql = "from PtviewSku_TkviewType pt where pt.ptviewSku = '"+ ptviewSku +"' ";
        @SuppressWarnings("unchecked")
        List<PtviewSku_TkviewType> skuTkviewTypeList = commonDao.queryByHql(skuTkviewTypeHql);
        
        List<String> typeNumber = new ArrayList<String>();
        if(skuTkviewTypeList.size() > 0){
            for(PtviewSku_TkviewType st : skuTkviewTypeList){
                typeNumber.add(st.getTkviewType());
            }
        }
        
        String tkviewTypeHql = "from TkviewType t where 1=1 ";
        if(typeNumber.size() > 0){
            tkviewTypeHql += "and t.numberCode in ("+ StringTool.listToSqlString(typeNumber) +") ";
        }
        tkviewTypeHql += "and t.cruise = '"+ ptviewObj.getCruise() +"' ";
        @SuppressWarnings("unchecked")
        List<TkviewType> tkviewTypeList = commonDao.queryByHql(tkviewTypeHql);
        
        List<String> cabinNumber = new ArrayList<String>();
        for(TkviewType type : tkviewTypeList){
            cabinNumber.add(type.getCabin());
        }
        
        String tkviewSql = "SELECT shipment AS value, ";
        tkviewSql += "DATE_FORMAT(shipment_travel_date,'%Y-%m-%d') AS text FROM tkview ";
        tkviewSql += "WHERE DATE_FORMAT(shipment_travel_date,'%Y-%m-%d') > NOW() ";
        tkviewSql += "AND cruise = '"+ ptviewObj.getCruise() +"' ";
        if(cabinNumber.size() > 0){
            tkviewSql += "AND cabin IN ("+ StringTool.listToSqlString(cabinNumber) +") ";
        }
        {
            tkviewSql += possessorUtil.getRelatesWhereSql("tkview");
        }
        if (StringTool.isNotNull(q)) {
            String[] strs = q.split(" ");
            for (int i = 0; i < strs.length; i++ ) {
                if (!"".equals(strs[i].trim())) {
                    tkviewSql += " AND DATE_FORMAT(shipment_travel_date,'%Y-%m-%d') LIKE '%" + strs[i] + "%' ";
                }
            }
        }
        tkviewSql += " GROUP BY shipment ";
        tkviewSql += "ORDER BY shipment_travel_date ";
        
        SQLQuery query = commonDao.getSqlQuery(tkviewSql);
        query.addScalar("value").addScalar("text")
        .setResultTransformer(Transformers.aliasToBean(ListNumberCode.class));
        @SuppressWarnings("unchecked")
        List<ListNumberCode> results = query.list();

        return ReturnUtil.returnMap(1, results);
    }

    @Override
    public Map<String, Object> getTkviewByShipment(String q , String shipment , String ptviewSku) {
        PtviewSku skuObj = (PtviewSku)commonDao.getObjectByUniqueCode("PtviewSku", "numberCode", ptviewSku);
        if(skuObj == null){
            return ReturnUtil.returnMap(0, "产品SKU套餐对象不存在!");
        }
        
        Ptview ptviewObj = (Ptview)commonDao.getObjectByUniqueCode("Ptview", "numberCode", skuObj.getPtview());
        if(ptviewObj == null){
            return ReturnUtil.returnMap(0, "产品对象不存在!");
        }
        
        String skuTkviewTypeHql = "from PtviewSku_TkviewType pt where pt.ptviewSku = '"+ ptviewSku +"' ";
        @SuppressWarnings("unchecked")
        List<PtviewSku_TkviewType> skuTkviewTypeList = commonDao.queryByHql(skuTkviewTypeHql);
        
        List<String> typeNumber = new ArrayList<String>();
        if(skuTkviewTypeList.size() > 0){
            for(PtviewSku_TkviewType st : skuTkviewTypeList){
                typeNumber.add(st.getTkviewType());
            }
        }
        
        String tkviewTypeHql = "from TkviewType t where 1=1 ";
        if(typeNumber.size() > 0){
            tkviewTypeHql += "and t.numberCode in ("+ StringTool.listToSqlString(typeNumber) +") ";
        }
        @SuppressWarnings("unchecked")
        List<TkviewType> tkviewTypeList = commonDao.queryByHql(tkviewTypeHql);
        
        List<String> cabinNumber = new ArrayList<String>();
        for(TkviewType type : tkviewTypeList){
            cabinNumber.add(type.getCabin());
        }
        
        Shipment shipmentObj=(Shipment) commonDao.getObjectByUniqueCode("Shipment", "numberCode", shipment);
        if(shipmentObj==null){
              return ReturnUtil.returnMap(0, ConstantMsg.shipment_null);
        }
        
        String tkviewSql = "SELECT number_code AS value,name_cn AS text FROM tkview WHERE 1=1 ";
        tkviewSql += "AND cruise = '"+ ptviewObj.getCruise() +"' ";
        if(cabinNumber.size() > 0){
            tkviewSql += "AND cabin IN ("+ StringTool.listToSqlString(cabinNumber) +") ";
        }
        tkviewSql += "AND shipment_travel_date = '"+ DateUtil.dateToShortString(shipmentObj.getTravelDate()) +"' ";
        {
            tkviewSql += possessorUtil.getRelatesWhereSql("tkview");
        }
        if (StringTool.isNotNull(shipment)) {
            tkviewSql += " AND shipment = '"+ shipment +"' ";
        }
        if (StringTool.isNotNull(q)) {
            String[] strs = q.split(" ");
            for (int i = 0; i < strs.length; i++ ) {
                if (!"".equals(strs[i].trim())) {
                    tkviewSql += "AND name_cn LIKE '%" + strs[i] + "%'";
                }
            }
        }
        tkviewSql += possessorUtil.getRelatesWhereSql(Constant.relate_type_tkview);
        
        SQLQuery query = commonDao.getSqlQuery(tkviewSql);
        query.addScalar("value").addScalar("text")
        .setResultTransformer(Transformers.aliasToBean(ListNumberCode.class));
        @SuppressWarnings("unchecked")
        List<ListNumberCode> results = query.list();

        return ReturnUtil.returnMap(1, results);
    }

    @Override
    public Map<String, Object> getPtviewAllList(String q, String ptview) {
        String sql = "SELECT p.name_cn AS text,p.number_code AS value FROM ptview p WHERE 1=1 ";
        sql += "AND p.number_code <> '"+ ptview +"' ";
        sql += possessorUtil.getRelatesWhereSql("ptview");
        if (q != null && !"".equals(q)) {
            sql += " AND p.name_cn LIKE '%" + q + "%'";
        }
        sql += " ORDER BY p.alter_date DESC ";

        SQLQuery query = commonDao.getSqlQuery(sql);
        query.addScalar("value").addScalar("text").setResultTransformer(
            Transformers.aliasToBean(ListNumberCode.class));
        @SuppressWarnings("unchecked")
        List<ListNumberCode> results = query.list();

        return ReturnUtil.returnMap(1, results);
    }

    @Override
    public Map<String, Object> getAirwayListByCruise(String q , String cruise) {
        if(StringTool.isEmpty(cruise)){
            return ReturnUtil.returnMap(0, "请选择邮轮!");
        }
        
        String sql = "SELECT s.airway FROM shipment s WHERE s.cruise = '"+ cruise +"' ";
        @SuppressWarnings("unchecked")
        List<String> list = commonDao.getSqlQuery(sql).list();
        
        String airwaySql = "SELECT name_cn AS text,number_code AS value FROM airway a WHERE 1=1 ";
        airwaySql += "AND number_code IN ("+ StringTool.listToSqlString(list) +") ";
        if (StringTool.isNotNull(q)) {
            airwaySql += "AND a.name_cn LIKE '%" + q + "%' ";
        }
        airwaySql += " ORDER BY a.alter_date DESC ";

        SQLQuery query = commonDao.getSqlQuery(airwaySql);
        query.addScalar("value").addScalar("text").setResultTransformer(
            Transformers.aliasToBean(ListNumberCode.class));
        @SuppressWarnings("unchecked")
        List<ListNumberCode> results = query.list();

        return ReturnUtil.returnMap(1, results);
    }

    @Override
    public Map<String, Object> getStartPlace(String q) {
        String sql = "SELECT s.start_place AS value, s.start_place AS text ";
        sql += "FROM shipment s ";
        if(StringTool.isNotNull(q)){
            sql += "WHERE s.start_place LIKE '%"+ q +"%' ";
        }
        sql += "GROUP BY s.start_place ";
        SQLQuery query = commonDao.getSqlQuery(sql);
        query.addScalar("value").addScalar("text").setResultTransformer(
            Transformers.aliasToBean(ListNumberCode.class));
        @SuppressWarnings("unchecked")
        List<ListNumberCode> results = query.list();

        return ReturnUtil.returnMap(1, results);
    }

    @Override
    public Map<String, Object> getToPlace(String q) {
        String sql = "SELECT p.number_code AS value, p.name_cn AS text ";
        sql += "FROM `port` p ";
        sql += "WHERE p.name_cn <> '海上巡游' ";
        if(StringTool.isNotNull(q)){
            sql += "AND p.name_cn LIKE '%"+ q +"%' ";
        }
        
        SQLQuery query = commonDao.getSqlQuery(sql);
        query.addScalar("value").addScalar("text").setResultTransformer(
            Transformers.aliasToBean(ListNumberCode.class));
        @SuppressWarnings("unchecked")
        List<ListNumberCode> results = query.list();

        return ReturnUtil.returnMap(1, results);
    }

    @Override
    public Map<String, Object> getRoyalCruiseList(String q) {
        String sql = "SELECT c.number_code AS value,CONCAT('【',c.mark,'】',c.name_cn) AS text ";
        sql += "FROM cruise c WHERE 1=1 ";
        sql += "AND c.company = '54023710b14b4e51adcd1592e0ef839a' ";
        if (StringTool.isNotNull(q)) {
            sql += "AND CONCAT('【',c.mark,'】',c.name_cn) LIKE '%"+ q +"%' ";
        }
        sql += "ORDER BY c.alter_date DESC ";

        SQLQuery query = commonDao.getSqlQuery(sql);
        query.addScalar("value").addScalar("text").setResultTransformer(
            Transformers.aliasToBean(ListNumberCode.class));
        @SuppressWarnings("unchecked")
        List<ListNumberCode> results = query.list();

        return ReturnUtil.returnMap(1, results);
    }

    @Override
    public Map<String, Object> getRoyalShipmentMouthList(String q , String cruise) {
        if(StringTool.isEmpty(cruise)){
            return ReturnUtil.returnMap(0, "邮轮选择错误!");
        }
        
        String sql = "SELECT DATE_FORMAT(s.travel_date,'%Y-%m') AS `value`, ";
        sql += "DATE_FORMAT(s.travel_date,'%Y-%m') AS text ";
        sql += "FROM r_shipment s WHERE 1=1 ";
        sql += "AND s.cruise = '"+ cruise +"' ";
        sql += "AND s.travel_date >= NOW() ";
        if (StringTool.isNotNull(q)) {
            sql += "AND DATE_FORMAT(s.travel_date,'%Y-%m') LIKE '%" + q + "%' ";
        }
        sql += "GROUP BY DATE_FORMAT(s.travel_date,'%Y-%m') ";
        sql += "ORDER BY DATE_FORMAT(s.travel_date,'%Y-%m') ";
        
        SQLQuery query = commonDao.getSqlQuery(sql);
        query.addScalar("value").addScalar("text").setResultTransformer(
            Transformers.aliasToBean(ListNumberCode.class));
        @SuppressWarnings("unchecked")
        List<ListNumberCode> results = query.list();

        return ReturnUtil.returnMap(1, results);
    }

    @Override
    public Map<String, Object> getRoyalShipmentList(String q , String cruise , String mouth) {
        if(StringTool.isEmpty(cruise)){
            return ReturnUtil.returnMap(0, "邮轮选择错误!");
        }
        
        String sql = "SELECT s.number_code AS value, ";
        sql += "DATE_FORMAT(s.travel_date,'%Y-%m-%d') AS text ";
        sql += "FROM r_shipment s WHERE 1=1 ";
        sql += "AND s.cruise = '" + cruise + "' ";
        sql += "AND s.travel_date >= NOW() ";
        if(StringTool.isNotNull(mouth)){
            sql += "AND DATE_FORMAT(s.travel_date,'%Y-%m') IN ("+ StringTool.StrToSqlString(mouth) +") ";
        }
        if(StringTool.isNotNull(q)){
            sql += "AND DATE_FORMAT(s.travel_date,'%Y-%m-%d') LIKE '%" + q + "%' ";
        }
        sql += "ORDER BY s.travel_date ";
        
        SQLQuery query = commonDao.getSqlQuery(sql);
        query.addScalar("value").addScalar("text").
        setResultTransformer(Transformers.aliasToBean(ListNumberCode.class));
        @SuppressWarnings("unchecked")
        List<ListNumberCode> results = query.list();

        return ReturnUtil.returnMap(1, results);
    }

    @Override
    public Map<String, Object> getProviderByShipmentList(String shipment, String cabin) {
        if(StringTool.isEmpty(shipment) && StringTool.isEmpty(cabin)){
            return ReturnUtil.returnMap(0, "请选择航期或者舱型!");
        }
        
        String tkviewSql = "SELECT t.number_code FROM tkview t WHERE 1=1 ";
        tkviewSql += possessorUtil.getRelatesWhereSql("tkview","t");
        if(StringTool.isNotNull(cabin)){
            tkviewSql += "AND t.cabin IN ("+ StringTool.StrToSqlString(cabin) +") ";
        }
        if(StringTool.isNotNull(shipment)){
            tkviewSql += "AND t.shipment IN ("+ StringTool.StrToSqlString(shipment) +") "; 
        }
        @SuppressWarnings("unchecked")
        List<String> tkviewList = commonDao.getSqlQuery(tkviewSql).list();
        
        String sql = "SELECT p.number_code AS value,p.name_cn AS text FROM provider p ";
        if(tkviewList.size() > 0){
            sql += "WHERE p.number_code IN ( ";
            sql += " SELECT s.provider FROM stock s where s.tkview IN ("+ StringTool.listToSqlString(tkviewList) +") ";
            sql += ") "; 
        }
        
        SQLQuery query = commonDao.getSqlQuery(sql);
        query.addScalar("value").addScalar("text").
        setResultTransformer(Transformers.aliasToBean(ListNumberCode.class));
        @SuppressWarnings("unchecked")
        List<ListNumberCode> results = query.list();

        return ReturnUtil.returnMap(1, results);
    }

    @Override
    public Map<String, Object> getProviderByCruiseList(String cruise) {
        if(StringTool.isEmpty(cruise)){
            return ReturnUtil.returnMap(0, "邮轮选项卡有错!");
        }
        
        String tkviewSql = "SELECT t.number_code FROM tkview t WHERE 1=1 ";
        tkviewSql += possessorUtil.getRelatesWhereSql("tkview","t");
        tkviewSql += "AND t.cruise = '"+ cruise +"' ";
        @SuppressWarnings("unchecked")
        List<String> tkviewList = commonDao.getSqlQuery(tkviewSql).list();
        
        String sql = "SELECT p.number_code AS value,p.name_cn AS text FROM provider p ";
        if(tkviewList.size() > 0){
            sql += "WHERE p.number_code IN ( ";
            sql += " SELECT s.provider FROM stock s where s.tkview IN ("+ StringTool.listToSqlString(tkviewList) +") ";
            sql += ") "; 
        }
        
        SQLQuery query = commonDao.getSqlQuery(sql);
        query.addScalar("value").addScalar("text").
        setResultTransformer(Transformers.aliasToBean(ListNumberCode.class));
        @SuppressWarnings("unchecked")
        List<ListNumberCode> results = query.list();

        return ReturnUtil.returnMap(1, results);
    }

    @Override
    public Map<String, Object> getCabinIsOpenByCruiseList(String q , String cruise , String isOpen) {
        q = StringTool.isNotNull(q) ? q : "";
        
        String sql = "SELECT c.number_code AS value,c.name_cn AS text FROM cabin c WHERE 1=1 ";
        sql += "AND c.cruise = '"+ cruise +"' ";
        sql += "AND c.is_open = '"+ isOpen +"' ";
        if(StringTool.isNotNull(q)){
            sql += "AND c.name_cn LIKE '%"+ q +"%'";
        }
        sql += "ORDER BY c.type ";
        SQLQuery query = commonDao.getSqlQuery(sql);
        query.addScalar("value").addScalar("text").setResultTransformer(
            Transformers.aliasToBean(ListNumberCode.class));
        @SuppressWarnings("unchecked")
        List<ListNumberCode> results = query.list();

        return ReturnUtil.returnMap(1, results);
    }

    @Override
    public Map<String, Object> getPersonList(String q) {
        q = StringTool.isNotNull(q) ? q : "";
        
        String sql = "SELECT p.number_code AS value,p.name_cn AS text FROM person p WHERE 1=1 ";
        if(StringTool.isNotNull(q)){
            sql += "AND p.name_cn LIKE '%"+ q +"%'";
        }
        sql += "ORDER BY p.name_cn ";
        SQLQuery query = commonDao.getSqlQuery(sql);
        query.addScalar("value").addScalar("text").setResultTransformer(
            Transformers.aliasToBean(ListNumberCode.class));
        @SuppressWarnings("unchecked")
        List<ListNumberCode> results = query.list();

        return ReturnUtil.returnMap(1, results);
    }

    
    
    @Override
    public Map<String, Object> getCabinByTkviewList(String cruise) {
        
        String tkviewtHql="select t.cabin from Tkview t where 1 = 1 and t.cruise = '"+cruise+"'  and  t.shipmentTravelDate >= NOW()";
        tkviewtHql+="GROUP BY t.cabin ";
        
        @SuppressWarnings("unchecked")
        List<String> cabin = commonDao.queryByHql(tkviewtHql);
        
        String sql = "SELECT c.number_code AS value,c.name_cn AS text FROM cabin c WHERE 1=1  and c.number_code in ("+StringTool.listToSqlString(cabin)+")";
        sql += "ORDER BY c.name_cn ";
        
        SQLQuery query = commonDao.getSqlQuery(sql);
        query.addScalar("value").addScalar("text").setResultTransformer(
            Transformers.aliasToBean(ListNumberCode.class));
        @SuppressWarnings("unchecked")
        List<ListNumberCode> results = query.list();

        return ReturnUtil.returnMap(1, results);
    }

    @Override
    public Map<String, Object> getShipmentByTkviewList(String cruise) {

        String tkviewtHql="select t.shipment from Tkview t where 1 = 1   and  t.shipmentTravelDate >= NOW()";
        if(StringTool.isNotNull(cruise)){
            tkviewtHql+=" and t.cruise = '"+cruise+"' ";  
        }
        tkviewtHql+="GROUP BY t.shipment ";
        
        @SuppressWarnings("unchecked")
        List<String> cabin = commonDao.queryByHql(tkviewtHql);
        
        String sql = "SELECT s.number_code AS value,DATE_FORMAT(s.travel_date,'%Y-%m-%d')  AS text FROM shipment s WHERE 1=1  and s.number_code in ("+StringTool.listToSqlString(cabin)+")";
        
        SQLQuery query = commonDao.getSqlQuery(sql);
        query.addScalar("value").addScalar("text").setResultTransformer(
            Transformers.aliasToBean(ListNumberCode.class));
        @SuppressWarnings("unchecked")
        List<ListNumberCode> results = query.list();

        return ReturnUtil.returnMap(1, results);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> getProviderByTkviewList(String cruise , String shipment ,
                                                       String cabin) {
        
        String tkviewtHql="select t.numberCode from Tkview t where 1 = 1 ";
        if(StringTool.isNotNull(cruise)){
            tkviewtHql+=" and t.cruise = '"+cruise+"' ";  
        }
        if(StringTool.isNotNull(shipment)){
            tkviewtHql+=" and t.shipment in ("+StringTool.StrToSqlString(shipment)+") ";  
        }
        if(StringTool.isNotNull(cabin)){
            tkviewtHql+=" and t.cabin in ("+StringTool.StrToSqlString(cabin)+") ";  
        }
        tkviewtHql+="GROUP BY t.numberCode ";
        
        List<String> tkview = commonDao.queryByHql(tkviewtHql);
        
        String stockHql="select s.provider from Stock s where 1 = 1 and s.tkview in ("+StringTool.listToSqlString(tkview)+") ";
        stockHql+="GROUP BY s.provider ";
        
        List<String> provider = commonDao.queryByHql(stockHql);
        
        String sql = "SELECT s.number_code AS value,s.name_cn AS text FROM provider s WHERE 1=1  and s.number_code in ("+StringTool.listToSqlString(provider)+")";
        
        SQLQuery query = commonDao.getSqlQuery(sql);
        query.addScalar("value").addScalar("text").setResultTransformer(
            Transformers.aliasToBean(ListNumberCode.class));
        
        List<ListNumberCode> results = query.list();

        return ReturnUtil.returnMap(1, results);
    }

    @Override
    public Map<String, Object> getDistributorTagList() {
        
        String sql = "SELECT d.number_code AS value,d.name AS text FROM distributor_tag d WHERE 1 = 1  ";
        
        SQLQuery query = commonDao.getSqlQuery(sql);
        query.addScalar("value").addScalar("text").setResultTransformer(
            Transformers.aliasToBean(ListNumberCode.class));
        
        @SuppressWarnings("unchecked")
        List<ListNumberCode> results = query.list();

        return ReturnUtil.returnMap(1, results);
    }
    
}