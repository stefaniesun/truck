package xyz.work.excel.svc.imp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.JSON;
import xyz.filter.MyRequestUtil;
import xyz.filter.ReturnUtil;
import xyz.util.Constant;
import xyz.util.CrawlerUtil;
import xyz.util.DateUtil;
import xyz.util.ExcelTool;
import xyz.util.NumberUtil;
import xyz.util.PossessorUtil;
import xyz.util.StringTool;
import xyz.util.UUIDUtil;
import xyz.work.base.model.Airway;
import xyz.work.base.model.Cabin;
import xyz.work.base.model.Cruise;
import xyz.work.base.model.Shipment;
import xyz.work.core.model.PlanWork;
import xyz.work.excel.model.ExcelLog;
import xyz.work.excel.model.PriceStrategy;
import xyz.work.excel.svc.ExcelSvc;
import xyz.work.resources.model.Provider;
import xyz.work.resources.model.Stock;
import xyz.work.resources.model.Tkview;
import xyz.work.taobao.model.TaobaoSkuInfo;
import xyz.work.taobao.model.TaobaoSkuInfoDetail;

@Service
public class ExcelSvcImp implements ExcelSvc{
    @Autowired
    CommonDao commonDao;
    
    @Autowired
    PossessorUtil possessorUtil;

    @Override
    public Map<String, Object> importExcelDataOper(String excelPath) {
        String[] titles = {
		    "供应商",
            "邮轮名",
            "日期",
            "询位代码",
            "库存代码",
            "舱型",
            "备注",
            "结算价",
            "特殊结算价",
            "总库存（间）",
            "预约系统",
            "已卖库存（间）",
            "剩余库存",
            "库存截止时间（材料一般为收客后7天内需到材料，尾单另议）",
            "其余可询供应商",
            "更新标注"
        };
        
        //判断Excel是否符合要求  并获取数据
        Map<String ,Object> checkMap =  ExcelTool.getExcelData(excelPath ,titles);
        if(Integer.parseInt(checkMap.get("status").toString())==0){
            return ReturnUtil.returnMap(0, checkMap.get("msg"));
        }
        
        System.out.println(JSON.toJson(checkMap));
        
        @SuppressWarnings("unchecked")
		List<Cruise> cruises=commonDao.queryByHql("from Cruise");
        Map<String, Object> cruiseMap=new HashMap<String, Object>();
        for(Cruise cruise:cruises){
        	cruiseMap.put(cruise.getNameCn(), cruise.getNumberCode());
        }
        
        @SuppressWarnings("unchecked")
		List<Cabin> cabins=commonDao.queryByHql("from Cabin");
        Map<String, BigDecimal> cabinMap=new HashMap<String, BigDecimal>();
        for(Cabin cabin:cabins){
        	cabinMap.put(cabin.getNumberCode(), cabin.getVolume());
        }
        
        @SuppressWarnings("unchecked")
		List<Provider> providers=commonDao.queryByHql("from Provider");
        Map<String,Object> providerMap=new HashMap<String, Object>();
        for(Provider provider:providers){
        	providerMap.put(provider.getMark(),provider);
        }
        
        @SuppressWarnings("unchecked")
		List<String[]> list = (List<String[]>) checkMap.get("content");
        Date date = new Date();
       //List<String> tkviewList = new ArrayList<String>();
        Map<String, Stock> stockMap=new HashMap<String, Stock>();
        
        Set<String> cruiseSet=new HashSet<String>();
        
        int updateSkuCount=0;
        
        List<String> updateTkviewList=new ArrayList<String>();
        
        for(int rowNum = 0; rowNum < list.size(); rowNum++){
            String[] row = list.get(rowNum);
            
            String providerName = row[0]==null?"":row[0].toString();           //供应商名称
            String cruise = row[1]==null?"":row[1].toString();                 //邮轮名
            String shipment = row[2]==null?"":row[2].toString();               //航期
            String provider=row[3]==null?"":row[3].toString();                 //供应商
            String stockNumberCode = row[4]==null?"":row[4].toString();        //库存numberCode
    	    String cabin = row[5]==null?"":row[5].toString();                  //舱型
      		String remark = row[6]==null?"":row[6].toString();                 //备注
    		String cost = row[7]==null?"":row[7].toString();                   //结算价/成本价
    		if(StringTool.isEmpty(cost)||cost.equals("0")){
    			continue;
    		}
    		String costColor="";            //结算价底色
    		String[] array=cost.split("&");
    		cost=array[0];
    		if(array.length>1){
    			costColor="#"+array[1];
    		}
     		String specialCost = row[8]==null?"":row[8].toString();           //特殊结算价
     		String stock = row[9]==null?"":row[9].toString();                 //库存
     		String yuyue = row[10]==null?"":row[10].toString();               //预约系统
     		String useStock = row[11]==null?"":row[11].toString();            //已卖库存
     		String leftStock = row[12]==null?"":row[12].toString();           //剩余库存
     		String overDate = row[13]==null?"":row[13].toString();            //截止日期
     		String otherProvider = row[14]==null?"":row[14].toString();       //其余可询供应商
     		String rowNumer = row[16]==null?"":row[16].toString();            //其余可询供应商
     		
     		cabin = cabin.replaceAll("內", "内");
     		shipment=shipment.replaceAll("/", "-");
     		
        	Stock s = new Stock();
            if("".equals(provider)){
            	  return ReturnUtil.returnMap(0,"第【"+rowNumer+"】行,询位代码为空");
            } 
         	Provider providerObj=(Provider) providerMap.get(provider);
            if(providerObj==null){
         	   return ReturnUtil.returnMap(0,"第【"+rowNumer+"】行,【"+providerName+"】邮轮资源库没有对应的匹配供应商");
            }
            
            if("".equals(stockNumberCode)){
            	
            	List<String> cruiseList=getMatchCruise(cruiseMap,cruise);
            	if(cruiseList.size()==0){
            		  return ReturnUtil.returnMap(0,"第【"+rowNumer+"】行,【"+cruise+"】邮轮资源库没有对应的匹配邮轮");
            	}else if(cruiseList.size()>1){
            		return ReturnUtil.returnMap(0,"第【"+rowNumer+"】行,【"+cruise+"】请输入精确的邮轮名称");
            	}
            	String cruiseNumberCode=cruiseList.get(0);
            	cruiseSet.add(cruiseNumberCode.toString());
        	    
        	    String tkviewNameCn="["+shipment+"]"+cabin;
        	    
        	    Tkview tkview=(Tkview) commonDao.queryUniqueByHql("from Tkview where cruise='"+cruiseNumberCode+"' and nameCn='"+tkviewNameCn+"'");
        	    
        	    System.out.println("HQL=="+"from Tkview where cruise='"+cruiseNumberCode+"' and nameCn='"+tkviewNameCn+"'");
        	    
        	    
        	    if(tkview==null){
        	    	  return ReturnUtil.returnMap(0,"第【"+rowNumer+"】行,【"+tkviewNameCn+"】单品资源库没有对应的匹配资源");
        	    }else{
        	    	
        	    	String sql="delete from stock where tkview='"+tkview.getNumberCode()+"' and provider='"+providerObj.getNumberCode()+"'";
        	    	commonDao.getSqlQuery(sql).executeUpdate();
        	    	
        	    	s=new Stock();
        	    	
        	    	if(NumberUtil.isNum(cost)){
        	    		s.setCost(new BigDecimal(cost).multiply(new BigDecimal(100)));
        	    	}else{
          	    	  continue;
        	    	}
                	s.setSpecialCost(specialCost);
                  	if("现询".equals(stock)){
                		s.setType(0);
                		s.setStock(cabinMap.get(tkview.getCabin()).multiply(new BigDecimal(5)));
                	}else if("售罄".equals(stock)){
                		s.setType(2);
                		s.setStock(new BigDecimal(0));
                	}else {
                		s.setType(1);
                		if(NumberUtil.isNum(stock)){
                			s.setStock(cabinMap.get(tkview.getCabin()).multiply(new BigDecimal(stock)));
                		}else{
                			  return ReturnUtil.returnMap(0,"第【"+rowNumer+"】行,库存数据异常");
                		}
    				}
                	s.setYuyue(yuyue);
                	if(NumberUtil.isNum(useStock)){
                	 	s.setUseStock(new BigDecimal(useStock));
                	}
                 	if(NumberUtil.isNum(leftStock)){
                    	s.setLeftStock(new BigDecimal(leftStock));
                 	}
                 	if(!"".equals(overDate)){
                 	   	s.setOverDate(DateUtil.shortStringToDate(overDate));
                 	}
                	s.setRemark(remark);
                	s.setOtherProvider(otherProvider);
                	s.setTkview(tkview.getNumberCode());
                	s.setNumberCode(UUIDUtil.getUUIDStringFor32());
                	s.setDate(tkview.getShipmentTravelDate());
                	s.setAddDate(date);
                	s.setAlterDate(date);
                	s.setProvider(providerObj.getNumberCode());
                	s.setRgb(costColor);
                	
                	commonDao.save(s);
                	
                	
                    String hql = "from Stock s where s.tkview = '" + tkview.getNumberCode() + "' ";
                    @SuppressWarnings("unchecked")
                    List<Stock> stocklist = commonDao.queryByHql(hql);
                    
                    Collections.sort(stocklist);
                	
                    updateTkviewList.add(tkview.getNumberCode());
                    
                	updateSkuCount++;
        	    }
            }else{
            	s=(Stock) commonDao.getObjectByUniqueCode("Stock", "numberCode", stockNumberCode);
            	if(s==null){
            		  return ReturnUtil.returnMap(0,"第【"+row[row.length-1]+"】行,【"+stock+"】库存资源库没有对应的匹配库存资源");
            	}
            	
            	Tkview tkview=(Tkview) commonDao.getObjectByUniqueCode("Tkview", "numberCode", s.getTkview());
            	cruiseSet.add(tkview.getCruise());
            	
     	    	if(NumberUtil.isNum(cost)){
     	    			s.setCost(new BigDecimal(cost).multiply(new BigDecimal(100)));
    	    	}else{
      	    	  continue;
    	    	}
     	    	
 	    	  String hql = "from Stock s where s.tkview = '" + tkview.getNumberCode() + "' ";
              @SuppressWarnings("unchecked")
              List<Stock> stocklist = commonDao.queryByHql(hql);
              @SuppressWarnings("unused")
              Stock firstStock = stocklist.get(0);
            	s.setSpecialCost(specialCost);
            	if("现询".equals(stock)){
            		s.setType(0);
            		s.setStock(cabinMap.get(tkview.getCabin()).multiply(new BigDecimal(5)));
            	}else if("售罄".equals(stock)){
            		s.setType(2);
            		s.setStock(new BigDecimal(0));
            	}else {
            		s.setType(1);
            		if(NumberUtil.isNum(stock)){
            			s.setStock(cabinMap.get(tkview.getCabin()).multiply(new BigDecimal(stock)));
            		}else{
            			  return ReturnUtil.returnMap(0,"第【"+rowNumer+"】行,库存数据异常");
            		}
				}
            	s.setYuyue(yuyue);
            	if(NumberUtil.isNum(useStock)){
            	 	s.setUseStock(new BigDecimal(useStock));
            	}
             	if(NumberUtil.isNum(leftStock)){
                	s.setLeftStock(new BigDecimal(leftStock));
             	}
              	if(!"".equals(overDate)){
             	   	s.setOverDate(DateUtil.shortStringToDate(overDate));
             	}
            	s.setRemark(remark);
            	s.setOtherProvider(otherProvider);
            	s.setAlterDate(date);
            	s.setDate(tkview.getShipmentTravelDate());
            	s.setRgb(costColor);
            	commonDao.update(s);
               
        		updateTkviewList.add(tkview.getNumberCode());
            	
            	updateSkuCount++;
            	
            }
            
            if(stockMap.get(s.getTkview())==null){
            	stockMap.put(s.getTkview(), s);
            }else{
            	Stock temp=stockMap.get(s.getTkview());
            	if(temp.compareTo(s)>0){
            		stockMap.put(s.getTkview(), s);
            	}
            }
            
        } 
        
        ExcelLog excelLog=new ExcelLog();
        excelLog.setNumberCode(UUIDUtil.getUUIDStringFor32());
        excelLog.setAddDate(new Date());
        excelLog.setType(ExcelLog.TYPE_UPDATE);
        excelLog.setUsername(MyRequestUtil.getUsername());
        excelLog.setFileUrl(excelPath);
        excelLog.setChangeCount(updateSkuCount);
        commonDao.save(excelLog);
        
        System.out.println("updateSkuCount====="+updateSkuCount);
        
        //自动更新计划任务
        for(String tkview:updateTkviewList){
        	PlanWork planWork=new PlanWork();
            planWork.setNumberCode(UUIDUtil.getUUIDStringFor32());
            planWork.setAddDate(new Date());
            planWork.setContent(tkview);
            planWork.setUsername("system");
            planWork.setWorkType(Constant.PLAN_TYPE_UPDATE_TKVIEW);
            commonDao.save(planWork);
        }
        
        return ReturnUtil.returnMap(1,null);
    }
    
    public static List<String>  getMatchCruise(Map<String, Object> map,String cruise){
    	List<String> result=new ArrayList<String>();
    	for (Map.Entry<String, Object> entry : map.entrySet()) {  
    		if(entry.getKey().indexOf(cruise)!=-1){
    			result.add(entry.getValue().toString());
    		}
    	}  
    	return result;
    }
    
    @Override
    public Map<String, Object> exportExcelDataOper(String provider , String cruise ,
                                                   String shipment , String cabin) {
        StringBuffer stockSql = new StringBuffer("SELECT s.iidd AS iidd, ");
        stockSql.append("s.tkview AS tkview, ");
        stockSql.append("s.provider AS provider, ");
        stockSql.append("s.number_code AS numberCode, ");
        stockSql.append("s.nick_name AS nickName, ");
        stockSql.append("s.cost AS cost, ");
        stockSql.append("s.cost_remark AS costRemark, ");
        stockSql.append("s.stock AS stock, ");
        stockSql.append("s.use_stock AS useStock, ");
        stockSql.append("s.rgb AS rgb, ");
        stockSql.append("s.left_stock AS leftStock, ");
        stockSql.append("s.surpass AS surpass, ");
        stockSql.append("s.use_surpass AS useSurpass, ");
        stockSql.append("s.over_date AS overDate, ");
        stockSql.append("s.yu_yue AS yuyue, ");
        stockSql.append("s.other_provider AS otherProvider, ");
        stockSql.append("s.special_cost AS specialCost, ");
        stockSql.append("s.priority AS priority, ");
        stockSql.append("s.type AS type, ");
        stockSql.append("s.remark AS remark, ");
        stockSql.append("s.add_date AS addDate, ");
        stockSql.append("s.alter_date AS alterDate ");
        stockSql.append("FROM stock s ");
        stockSql.append("LEFT JOIN tkview t ");
        stockSql.append("ON s.tkview = t.number_code ");
        stockSql.append("LEFT JOIN provider p ");
        stockSql.append("ON s.provider = p.number_code ");
        stockSql.append("WHERE 1=1 ");
        if(StringTool.isNotNull(provider)){
            stockSql.append("AND p.number_code = '"+ provider +"' ");
        }
        if(StringTool.isNotNull(cruise)){
            stockSql.append("AND t.cruise = '"+ cruise +"' ");
        }
        if(StringTool.isNotNull(shipment)){
            stockSql.append("AND t.shipment = '"+ shipment +"' ");
        }
        if(StringTool.isNotNull(cabin)){
            stockSql.append("AND t.cabin = '"+ cabin +"' ");
        }
        stockSql.append("ORDER BY p.name_cn,t.shipment_travel_date ");
        
        SQLQuery query = commonDao.getSqlQuery(stockSql.toString());
        query.addScalar("iidd").addScalar("tkview").addScalar("provider")
        .addScalar("numberCode").addScalar("nickName").addScalar("cost")
        .addScalar("costRemark").addScalar("stock").addScalar("useStock")
        .addScalar("rgb").addScalar("leftStock").addScalar("surpass")
        .addScalar("useSurpass").addScalar("overDate").addScalar("yuyue")
        .addScalar("otherProvider").addScalar("specialCost").addScalar("priority")
        .addScalar("type").addScalar("remark").addScalar("addDate").addScalar("alterDate")
        .setResultTransformer(Transformers.aliasToBean(Stock.class));
        @SuppressWarnings("unchecked")
        List<Stock> stockList  = query.list();
        
        if(stockList.size() <= 0){
            return ReturnUtil.returnMap(0, "没有任何数据!");
        }
        
        List<String> providerNumber = new ArrayList<String>();
        List<String> tkviewNumber = new ArrayList<String>();
        for(Stock s : stockList){
            tkviewNumber.add(s.getTkview());
            providerNumber.add(s.getProvider());
        }
        
        String tkviewHql = "from Tkview t where 1 = 1 ";
        tkviewHql += "and t.numberCode in ("+ StringTool.listToSqlString(tkviewNumber) +") ";
        @SuppressWarnings("unchecked")
        List<Tkview> tkviewList = commonDao.getQuery(tkviewHql).list();
        
        String providerSQl = "SELECT p.number_code,p.name_cn,p.mark FROM provider p ";
        providerSQl += "WHERE p.number_code IN ("+ StringTool.listToSqlString(providerNumber) +") ";
        @SuppressWarnings("unchecked")
        List<Object[]> providerList = commonDao.getSqlQuery(providerSQl).list();
       
        List<String> cruiseNumber = new ArrayList<String>();
        List<String> cabinNumber = new ArrayList<String>();
        for(Tkview tk : tkviewList){
            cruiseNumber.add(tk.getCruise());
            cabinNumber.add(tk.getCabin());
        }
        
        String cruiseSql = "SELECT c.number_code,c.name_cn FROM cruise c WHERE 1=1 ";
        if(StringTool.isNotNull(cruise)){
            cruiseSql += "AND c.number_code ='"+ cruise +"' ";
        }else{
            cruiseSql += "AND c.number_code IN ("+ StringTool.listToSqlString(cruiseNumber) +") ";
        }
        @SuppressWarnings("unchecked")
        List<Object[]> cruiseList = commonDao.getSqlQuery(cruiseSql).list();
        
        String cabinSql = "SELECT c.number_code,c.name_cn,volume FROM cabin c WHERE 1=1 ";
        if(StringTool.isNotNull(cabin)){
            cabinSql += "AND c.number_code = '"+ cabin +"' ";
        }else{
            cabinSql += "AND c.number_code IN ("+ StringTool.listToSqlString(cabinNumber) +") ";
        }
        @SuppressWarnings("unchecked")
        List<Object[]> cabinList = commonDao.getSqlQuery(cabinSql).list();
        
        Map<String, BigDecimal> cabinMap=new HashMap<String, BigDecimal>();
        for(Object[] cabinObj : cabinList){
        	cabinMap.put(cabinObj[0].toString(), new BigDecimal(cabinObj[2].toString()));
        }
        
        for(Tkview tk : tkviewList){
            for(Object[] cruiseObj : cruiseList){
                if(tk.getCruise().equals(cruiseObj[0])){
                    tk.setCruiseNameCn((String)cruiseObj[1]);
                    break;
                }
            }
            for(Object[] cabinObj : cabinList){
                if(tk.getCabin().equals(cabinObj[0])){
                    tk.setCabinNameCn((String)cabinObj[1]);
                    break;
                }
            }
        }
        for(Stock sk : stockList){
            for(Object[] providerObj : providerList){
                if(sk.getProvider().equals((String)providerObj[0])){
                    sk.setProviderNameCn((String)providerObj[1]);
                    sk.setProviderMark((String)providerObj[2]);
                    break;
                }
            }
        }
        
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy/MM/dd");
        
        List<List<Object>> excelList = new ArrayList<List<Object>>();
        for (Stock stockObj : stockList) {
            for(Tkview tkviewObj : tkviewList){
                if(!stockObj.getTkview().equals(tkviewObj.getNumberCode())){
                    continue;
                }
                List<Object> tempList = new ArrayList<Object>();
                /*0、供应商,        1、邮轮名,     2、日期,            3、询位代码,
                  4、库存代码 ,      5、舱型 ,      6、结算价,          7、特殊结算价, 
                  8、总库存（间）,   9、预约系统,  10、已卖库存（间）,  11、剩余库存,
                 12、库存截止时间（材料一般为收客后7天内需到材料，尾单另议）,
                 13、备注,  14、其余可询供应商*/
                tempList.add(stockObj.getProviderNameCn());           //供应商
                tempList.add(tkviewObj.getCruiseNameCn());            //邮轮名
                //日期
                tempList.add(simpleDateFormat.format(tkviewObj.getShipmentTravelDate()));                            
                tempList.add(stockObj.getProviderMark());             //询位代码
                tempList.add(stockObj.getNumberCode());                   //库存代码
                tempList.add(tkviewObj.getCabinNameCn());             //舱型
                //备注
                tempList.add(StringTool.isEmpty(stockObj.getRemark())?"":stockObj.getRemark());
                //结算价
                int cost=stockObj.getCost().divide(new BigDecimal(100)).intValue();
                tempList.add(cost+"&"+stockObj.getRgb());
                //特殊结算价
                tempList.add(StringTool.isEmpty(stockObj.getSpecialCost())?"":stockObj.getSpecialCost());               
                if(stockObj.getType()==0){
             	   tempList.add("现询");  
                 }else if(stockObj.getType()==2){
                	  tempList.add("售罄");  
                 }else{
             	   tempList.add(stockObj.getStock().divide(cabinMap.get(tkviewObj.getCabin()),0,RoundingMode.UP).intValue());  
                 }
                tempList.add(StringTool.isEmpty(stockObj.getYuyue())?"":new BigDecimal(stockObj.getYuyue()).intValue());   
                tempList.add(stockObj.getUseStock()==null?"":stockObj.getUseStock().intValue());      
                tempList.add(stockObj.getLeftStock()==null?"":stockObj.getLeftStock().intValue());   
                tempList.add(stockObj.getOverDate()==null?"":simpleDateFormat.format(stockObj.getOverDate()));   
                tempList.add(stockObj.getOtherProvider());        
                tempList.add(cost); 
                if(stockObj.getType()==0){
              	   tempList.add("现询");  
                  }else{
              	   tempList.add(stockObj.getStock().divide(cabinMap.get(tkviewObj.getCabin()),0,RoundingMode.UP).intValue());  
                  }
                tempList.add(""); 
                excelList.add(tempList);
            }
        }
        
        Object[][] titles = {
            new Object[]{"供应商",10},
            new Object[]{"邮轮名",10},
            new Object[]{"日期",12},
            new Object[]{"询位代码",8},
            new Object[]{"库存代码",15},
            new Object[]{"舱型",20},
            new Object[]{"备注",16},
            new Object[]{"结算价",16},
            new Object[]{"特殊结算价",16},
            new Object[]{"总库存（间）",6},
            new Object[]{"预约系统",4},
            new Object[]{"已卖库存（间）",4},
            new Object[]{"剩余库存",8},
            new Object[]{"库存截止时间（材料一般为收客后7天内需到材料，尾单另议）",10},
            new Object[]{"其余可询供应商",10},
            new Object[]{"结算价(忽略)",16},
            new Object[]{"总库存（间）(忽略)",6},
            new Object[]{"更新标注",6}
        };
        
        String filename = "cms-"+DateUtil.dateToLongString(new Date())+".xlsx";
        boolean flag = ExcelTool.createExcelForList(excelList, titles, filename);
        if(!flag){
            return ReturnUtil.returnMap(0, "Excel生成失败!");
        }
        
        return ReturnUtil.returnMap(1, filename);
    }


	@Override
	public Map<String, Object> queryExcelLogList(int offset, int pageSize) {
        String hql = "from ExcelLog order by addDate desc";
        Query countQuery = commonDao.getQuery("select count(*) " + hql);
        Number countNum = (Number)countQuery.uniqueResult();
        int count = countNum == null ? 0 : countNum.intValue();

        Query query = commonDao.getQuery(hql);
        query.setMaxResults(pageSize);
        query.setFirstResult(offset);
        @SuppressWarnings("unchecked")
        List<PriceStrategy> list = query.list();
        
        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", count);
        mapContent.put("rows", list);

        return ReturnUtil.returnMap(1, mapContent);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> downTemplate(String cruise , String shipment , String cabin) {
        
        String shipmentHql=" from Tkview t where 1 = 1  and  t.shipmentTravelDate >= NOW() ";
        if(StringTool.isNotNull(cruise)){
            shipmentHql+=" and t.cruise IN ("+ StringTool.StrToSqlString(cruise) +") ";
        }
        
        if(StringTool.isNotNull(shipment)){
            shipmentHql+=" and t.shipment IN ("+ StringTool.StrToSqlString(shipment) +") ";
        }
        shipmentHql+=" group by t.shipment  order by t.cruise";
        
        List<Tkview> shipmentList = commonDao.queryByHql(shipmentHql);
        
        String cabinHql=" from Tkview t where 1 = 1 and  t.shipmentTravelDate >= NOW() ";
        if(StringTool.isNotNull(cruise)){
            cabinHql+=" and t.cruise IN ("+ StringTool.StrToSqlString(cruise) +") ";
        }
        if(StringTool.isNotNull(cabin)){
            cabinHql+=" and t.cabin IN ("+ StringTool.StrToSqlString(cabin) +") ";
        }
        cabinHql+=" group by t.cabin ";
        
        List<Tkview> cabinList = commonDao.queryByHql(cabinHql);
        
        Set<String> cruiseNumber= new HashSet<String>();
        Set<String> cabinNumber= new HashSet<String>();
        
        for(Tkview c : cabinList){
            cruiseNumber.add(c.getCruise());
            cabinNumber.add(c.getCabin());
        }
                 
        String cruiseHql = "from Cruise t where t.numberCode in ("+StringTool.listToSqlString(cruiseNumber)+")";
        List<Cruise> cruiseList = commonDao.queryByHql(cruiseHql);
        
        String cabiHql1 = "from Cabin t where t.numberCode in ("+StringTool.listToSqlString(cabinNumber)+")";
        List<Cabin> cabinList1 = commonDao.queryByHql(cabiHql1);
        for (Tkview tkview : shipmentList) {
            for(Cruise cruise1 : cruiseList){
                if(tkview.getCruise().equals(cruise1.getNumberCode())){
                    tkview.setCruiseNameCn(cruise1.getNameCn());
                    break;
                }
            }
        }
        for(Tkview tkview : cabinList){
            for (Cabin cabin1 : cabinList1) {
                if(tkview.getCabin().equals(cabin1.getNumberCode())){
                    tkview.setCabinNameCn(cabin1.getNameCn());
                    break;
                }
            }
        }
        
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy/MM/dd");    
        List<List<Object>> infoList = new ArrayList<List<Object>>();
        
        for (Tkview shipment2 : shipmentList) {
            for (Tkview cabin2 : cabinList) {
                List<Object> tempList = new ArrayList<Object>();
                if(!shipment2.getCruise().equals(cabin2.getCruise())){
                    continue;
                }
                tempList.add(shipment2.getShipment());   //0航期编号
                tempList.add(cabin2.getCabin()); // 1舱型编号
                tempList.add(shipment2.getCruiseNameCn()); //2游轮名字
                tempList.add(simpleDateFormat.format(shipment2.getShipmentTravelDate()));  //3日期
                tempList.add(cabin2.getCabinNameCn()); //4舱型名字
                tempList.add(cabin2.getCruise()); //5游轮
                
                infoList.add(tempList);
            }
        }
        List<List<Object>> resultList = new ArrayList<List<Object>>();
        for (List<Object> list : infoList) {
            List<Object> tempList = new ArrayList<Object>();
           
            String hql=" from Tkview t where 1 = 1  ";
            hql+=" and t.shipment='"+list.get(0)+"' ";
            hql+=" and t.cabin='"+list.get(1)+"' ";
            hql+=" and t.cruise='"+list.get(5)+"' ";
            
            List<Tkview> tkviewList = commonDao.queryByHql(hql);
            
            List<Stock> totalStockList=new ArrayList<Stock>();
            
           for (Tkview tkview : tkviewList) {
               String stockHql="from  Stock s where 1 = 1 and s.tkview in ("+StringTool.StrToSqlString(tkview.getNumberCode())+") ";
               stockHql+=" and s.stock = 0 ";
               List<Stock> stockList = commonDao.queryByHql(stockHql);
               
               for (Stock stock : stockList) {
                 String providerhql=" from Provider p where p.numberCode in ("+StringTool.StrToSqlString(stock.getProvider())+") ";
                 List<Provider> providerList = commonDao.queryByHql(providerhql);
                 for (Provider provider : providerList) {
                   if(stock.getProvider().equals(provider.getNumberCode())){
                       stock.setProviderNameCn(provider.getNameCn());
                       stock.setProviderMark(provider.getMark());
                   }
                 }
               }
               totalStockList.addAll(stockList);
           }
           
           if(totalStockList.size()>0){
               for (Stock stock : totalStockList) {
                   List<Object> stockTempList=new ArrayList<Object>();
                   stockTempList.add(stock.getProviderNameCn());   //0供应商
                   stockTempList.add(list.get(2)); // 1游轮名字
                   stockTempList.add(list.get(3));  //2日期
                   stockTempList.add(stock.getProviderMark()); //33询位代码
                   stockTempList.add(stock.getNumberCode()); //4库存代码
                   stockTempList.add(list.get(4)); //5舱型
                   stockTempList.add(StringTool.isEmpty(stock.getRemark())?"":stock.getRemark()); //6备注
                   int cost=stock.getCost().divide(new BigDecimal(100)).intValue();
                   stockTempList.add(cost+"&"+stock.getRgb()); //7结算价
                   stockTempList.add(StringTool.isEmpty(stock.getSpecialCost())?"":stock.getSpecialCost()); //8特殊结算价
                   stockTempList.add("现询"); //9总库存（间）
                   stockTempList.add(StringTool.isEmpty(stock.getYuyue())?"":new BigDecimal(stock.getYuyue()).intValue()); //10预约系统
                   stockTempList.add(stock.getUseStock()==null?"":stock.getUseStock().intValue()); //11已卖库存（间）
                   stockTempList.add(stock.getLeftStock()==null?"":stock.getLeftStock().intValue()); //12剩余库存
                   stockTempList.add(stock.getOverDate()==null?"":simpleDateFormat.format(stock.getOverDate())); //13库存截止时间
                   stockTempList.add(stock.getOtherProvider()); //14其余可询供应商
                   stockTempList.add(cost); //15结算价(忽略)
                   stockTempList.add("现询"); //16总库存（间）
                   stockTempList.add(""); //17更新标注
                   resultList.add(stockTempList);
               }
           }else{
               tempList.add("");   //0供应商
               tempList.add(list.get(2)); // 1游轮名字
               tempList.add(list.get(3));  //2日期
               tempList.add(""); //3询位代码
               tempList.add(""); //4库存代码
               tempList.add(list.get(4)); //5舱型
               tempList.add(""); //6备注
               tempList.add(""); //7结算价
               tempList.add(""); //8特殊结算价
               tempList.add(""); //9总库存（间）
               tempList.add(""); //10预约系统
               tempList.add(""); //11已卖库存（间）
               tempList.add(""); //12剩余库存
               tempList.add(""); //13库存截止时间
               tempList.add(""); //14其余可询供应商
               tempList.add(""); //15结算价(忽略)
               tempList.add(""); //16总库存（间）
               tempList.add(""); //17更新标注   
               resultList.add(tempList);
           }
        }
        Object[][] titles = {
            new Object[]{"供应商",10},
            new Object[]{"邮轮名",10},
            new Object[]{"日期",12},
            new Object[]{"询位代码",8},
            new Object[]{"库存代码",15},
            new Object[]{"舱型",20},
            new Object[]{"备注",16},
            new Object[]{"结算价",16},
            new Object[]{"特殊结算价",16},
            new Object[]{"总库存（间）",6},
            new Object[]{"预约系统",4},
            new Object[]{"已卖库存（间）",4},
            new Object[]{"剩余库存",8},
            new Object[]{"库存截止时间（材料一般为收客后7天内需到材料，尾单另议）",10},
            new Object[]{"其余可询供应商",10},
            new Object[]{"结算价(忽略)",16},
            new Object[]{"总库存（间）(忽略)",6},
            new Object[]{"更新标注",6}
        };
        
        String filename = "cms-"+DateUtil.dateToLongString(new Date())+".xlsx";
        boolean flag = ExcelTool.createTemplateExcelForList(resultList, titles, filename);
        if(!flag){
            return ReturnUtil.returnMap(0, "Excel生成失败!");
        }
        
        return ReturnUtil.returnMap(1, filename);
    }
    
	@SuppressWarnings("unchecked")
    @Override
	public Map<String, Object> downCompareData(String cruise,String shopName,String sourceCode) {
		Cruise cruiseObj=(Cruise) commonDao.getObjectByUniqueCode("Cruise", "numberCode", cruise);
		if(cruise==null){
			  return ReturnUtil.returnMap(0, "邮轮对象为空!");
		}
		
		String[] shopNameArray=shopName.split("&CMS&");
		String[] sourceCodeArray=sourceCode.split("&CMS&");
		
		Map<String, Map<String, List<String>>> transMap=new HashMap<String, Map<String, List<String>>>();
		List<List<Object>> excelList=new ArrayList<List<Object>>();
		
		for(int i=0;i<sourceCodeArray.length;i++){

			Map<String, List<String>> listMap=CrawlerUtil.getCompareData(sourceCodeArray[i]);
			
			for (Map.Entry<String, List<String>> entry : listMap.entrySet()) {  
				
				String cabin=entry.getKey();
				
			  	if(cabin.indexOf("内舱2人房")!=-1) {
		    		cabin="内舱2人房";
				}else if(cabin.indexOf("内舱3人房")!=-1) {
					cabin="内舱3人房";
				}else if(cabin.indexOf("内舱4人房")!=-1) {
					cabin="内舱4人房";
				}else if(cabin.indexOf("海景2人房")!=-1) {
					cabin="海景2人房";
				}else if(cabin.indexOf("海景3人房")!=-1) {
					cabin="海景3人房";
				}else if(cabin.indexOf("海景4人房")!=-1) {
					cabin="海景4人房";
				}else if(cabin.indexOf("阳台2人房")!=-1) {
					cabin="阳台2人房";
				}else if(cabin.indexOf("阳台3人房")!=-1) {
					cabin="阳台3人房";
				}else if(cabin.indexOf("阳台4人房")!=-1) {
					cabin="阳台4人房";
				}else{
					continue;
				}
				
				List<String> dataList=entry.getValue();
				
				for(String data:dataList){
					String[] array=data.split("&");
					Map<String, List<String>> subMap=transMap.get(array[0]);
					if(subMap==null){
						subMap=new HashMap<String, List<String>>();
					}
					List<String> subList=subMap.get(shopNameArray[i]);
					if(subList==null){
						subList=new ArrayList<String>();
						subList.add(cabin+"&"+array[1]);
						subMap.put(shopNameArray[i], subList);
						transMap.put(array[0],subMap);
					}else{
						
						boolean addFlag=false;
						for(String value:subList) {
				    		String[] arr=value.split("&");
				    		if(arr[0].equals(cabin)&&new BigDecimal(array[1]).compareTo(new BigDecimal(arr[1]))>0) {
				    			value=cabin+"&"+array[1];
				    		}
				    	}
						if(!addFlag) {
							subList.add(cabin+"&"+array[1]);
			    		}
						
					}
				}
			}  
		}
		
	    Object[][] titles=new Object[11+shopNameArray.length][2];
	    titles[0] = new Object[]{"序号",10};
	    titles[1] = new Object[]{"邮轮名",10};
		titles[2] = new Object[]{"航期",12};
	    titles[3] = new Object[]{"舱型",8};
	    titles[4] = new Object[]{"航线",15};
	    titles[5] = new Object[]{"上传Sku价格",12};
	    titles[6] = new Object[]{"结算",10};
	    
	    titles[7] = new Object[]{"最低价",10};
	    titles[8] = new Object[]{"Sku价格",10};
	    titles[9] = new Object[]{"最低价格(零利润)",15};
	    titles[10] = new Object[]{"跟进",10};
	    
	    for(int i=0;i<shopNameArray.length;i++){
	    	   titles[10+(i+1)]=  new Object[]{shopNameArray[i],10};
	    }
	    		
        List<Tkview> tkviewList = commonDao.queryByHql("from Tkview where cruise='"+cruise+"' and shipment_travel_date>now()");
	    
	    List<String> shipmentNumberCodeList=new ArrayList<String>();
	    for(Tkview tkview:tkviewList){
	    	shipmentNumberCodeList.add(tkview.getShipment());
	    }
	    List<Shipment> shipmentList=commonDao.queryByHql("from Shipment where numberCode in("+StringTool.listToSqlString(shipmentNumberCodeList)+")");
	    List<String> airwayNumberCodeList=new ArrayList<String>();
	    for(Shipment shipment:shipmentList){
	    	airwayNumberCodeList.add(shipment.getAirway());
	    }
	    List<Airway> airwayList=commonDao.queryByHql("from Airway where numberCode in("+StringTool.listToSqlString(airwayNumberCodeList)+")");
	    for(Shipment shipment:shipmentList){
	    	for(Airway airway:airwayList){
	    		if(shipment.getAirway().equals(airway.getNumberCode())){
	    			shipment.setAirwayNameCn(airway.getNameCn()+" "+airway.getDays().intValue()+"天"+airway.getNights().intValue()+"晚");
	    			break;
	    		}
	    	}
	    }
	    for(Tkview tkview:tkviewList){
	    	for(Shipment shipment:shipmentList){
	    		if(tkview.getShipment().equals(shipment.getNumberCode())){
	    			tkview.setAirwayNameCn(shipment.getAirwayNameCn());
	    			break;
	    		}
	    	}
	    }
	    
	    Map<String, String> dateAirwayMap=new HashMap<String, String>();
	    
	    String sql="select number_code from taobao_sku_info where taobao_travel_item =(select number_code from taobao_travel_item where number_code in(select taobao_travel_item from taobao_base_info where cruise='"+cruise+"') and channel_name_cn like '%巴士%')";
        List<String> numberCodes=commonDao.getSqlQuery(sql).list();
        
        sql="select cabin from tkview_type where number_code in(select tkview_type from sku_tkview_type where sku in("+StringTool.listToSqlString(numberCodes)+"))";
        List<String> cabins=commonDao.getSqlQuery(sql).list();
        
	    for(Tkview tkview:tkviewList){
	    	
	    	if(!cabins.contains(tkview.getCabin())) {
	    		continue;
	    	}
	    	
	    	String cabin=tkview.getNameCn();
	    	
	    	if(cabin.indexOf("内舱双人间")!=-1||cabin.indexOf("内舱2人房")!=-1) {
	    		cabin="内舱2人房";
			}else if(cabin.indexOf("内舱三人间")!=-1||cabin.indexOf("内舱3人房")!=-1) {
				cabin="内舱3人房";
			}else if(cabin.indexOf("内舱四人间")!=-1||cabin.indexOf("内舱4人房")!=-1) {
				cabin="内舱4人房";
			}else if(cabin.indexOf("海景双人间")!=-1||cabin.indexOf("海景2人房")!=-1) {
				cabin="海景2人房";
			}else if(cabin.indexOf("海景三人间")!=-1||cabin.indexOf("海景3人房")!=-1) {
				cabin="海景3人房";
			}else if(cabin.indexOf("海景四人间")!=-1||cabin.indexOf("海景4人房")!=-1) {
				cabin="海景4人房";
			}else if(cabin.indexOf("阳台双人间")!=-1||cabin.indexOf("阳台2人房")!=-1) {
				cabin="阳台2人房";
			}else if(cabin.indexOf("阳台三人间")!=-1||cabin.indexOf("阳台3人房")!=-1) {
				cabin="阳台3人房";
			}else if(cabin.indexOf("阳台四人间")!=-1||cabin.indexOf("阳台4人房")!=-1) {
				cabin="阳台4人房";
			}else{
				continue;
			}
	    	
            List<Stock> stocks = commonDao.queryByHql("from Stock where tkview='"+tkview.getNumberCode()+"'");
	    	if(stocks.size()==0){
	    		continue;
	    	}
	    	Collections.sort(stocks);
	    	
	    	Stock stock=stocks.get(0);
	    	if(stock.getStock().intValue()==0){
	    		continue;
	    	}
	    	
	    	String date=DateUtil.dateToShortString(tkview.getShipmentTravelDate());
	    	
	    	dateAirwayMap.put(date, tkview.getAirwayNameCn());
	    	
	    	Map<String,List<String>> map=transMap.get(date);
	    	if(map==null){
	    		map=new HashMap<String, List<String>>();
	    		transMap.put(date, map);
	    	}
	    	
	    	List<String> list = map.get("cms");
	    	if(list==null){
	    		list=new ArrayList<String>();
	    		map.put("cms", list);
	    	}
	    	
	    	boolean addFlag=false;
    		for(String value:list) {
	    		String[] arr=value.split("&");
	    		if(arr[0].equals(value)&&new BigDecimal(arr[1]).compareTo(stock.getCost())>0) {
	    			value=arr[0]+"&"+stock.getCost().intValue();
	    			addFlag=true;
	    		}
	    	}
    		if(!addFlag) {
    			list.add(cabin+"&"+stock.getCost().intValue());
    		}
	    	
	    }
	    
	    System.out.println("transMap=222="+transMap);
	    
		int index=1;
		List<String> dateList = new ArrayList<String>();
		for (Map.Entry<String, Map<String,List<String>>> entry : transMap.entrySet()) {  
			dateList.add(entry.getKey());
		}
		Collections.sort(dateList);
		
		List<String> shopList = new ArrayList<>();
    	shopList.add("cms");
    	shopList.addAll(Arrays.asList(shopNameArray));
    	shopNameArray=new String[shopList.size()];
    	for(int i=0;i<shopList.size();i++) {
    		shopNameArray[i]=shopList.get(i);
    	}
        
    	String[] cabinArray={"内舱2人房","内舱3人房","内舱4人房","海景2人房","海景3人房","海景4人房","阳台2人房","阳台3人房","阳台4人房"};

    	
        for (String date : dateList) {  
			
        	Map<String, List<String>> map=transMap.get(date);
        	

        	for(String cabin:cabinArray){
        		
        	    List<Object> subList=new ArrayList<Object>();
        	    subList.add(index++);
        	    subList.add(cruiseObj.getNameCn());
        	    subList.add(date);
        	    subList.add(cabin);
        	    subList.add(StringTool.isEmpty(dateAirwayMap.get(date))?"":dateAirwayMap.get(date));
        	 
        	    BigDecimal minPirce=null;
			
        	    for(int i=0;i<shopNameArray.length;i++){
        	    	
        	        List<String> dataList=map.get(shopNameArray[i]);
        	        if(dataList==null){
        	        	if(i==0){
        	        		 subList.add("");
        	        		 subList.add("");
        	        	}else{
        	        		 subList.add("");
        	        	}
        	        }else{
        	            boolean flag=false;
        	            String price="";
        	            for(String data:dataList){
        	                String[] array=data.split("&");
        	                if(array[0].equals(cabin)){
        	                    price=array[1];
        	                    flag=true;
        	                    break;
        	                }
        	            }
        	       
        	            if(flag){
        	               	if(i==0){
        	               		subList.add("");
        	               		subList.add(new BigDecimal(price).divide(new BigDecimal(100)));
        	               	}else{
        	               		subList.add(new BigDecimal(price).divide(new BigDecimal(100)));
        	               	}
        	                if(i!=0){
        	                	   if(minPirce==null){
                   	            	minPirce=new BigDecimal(price);
                   	            }else{
                   	            	if(minPirce.compareTo(new BigDecimal(price))>0){
                   	            		minPirce=new BigDecimal(price);
                   	            	}
                   	            }
        	                }
        	            }else{
        	              	if(i==0){
           	        		 subList.add("");
           	        		 subList.add("");
	           	        	}else{
	           	        		 subList.add("");
	           	        	}
        	            }
        	        }
        	    }
        	    if(minPirce==null){
        	        subList.add("");
        	    }else{
    	    	   subList.add(minPirce.divide(new BigDecimal(100)));
        	    }
        	    excelList.add(subList);
        	}
			
		}
        
        List<List<Object>> list=new ArrayList<List<Object>>();
        
     
        
        String hql="from TaobaoSkuInfo where numberCode in("+StringTool.listToSqlString(numberCodes)+")";
        
        List<TaobaoSkuInfo> skuInfos=commonDao.queryByHql(hql);
        
        List<String> cabinList=Arrays.asList(cabinArray);
        
        Map<String, Map<String, String>> skuMap=new HashMap<String, Map<String,String>>();
        
        for(TaobaoSkuInfo skuInfo:skuInfos){
        	String packageName=skuInfo.getPackageName();
        	if(cabinList.contains(packageName)){
				List<TaobaoSkuInfoDetail> detailList=commonDao.queryByHql("from TaobaoSkuInfoDetail where taobaoSkuInfo='"+skuInfo.getNumberCode()+"' and date>=now()");
        		Map<String, String> detailMap=new HashMap<String, String>();
        		for(TaobaoSkuInfoDetail detail:detailList){
        			detailMap.put(DateUtil.dateToShortString(detail.getDate()), detail.getPrice().divide(new BigDecimal(100)).intValue()+"");
        		}
        		if(detailList.size()>0){
        			skuMap.put(packageName,detailMap);
        		}
        	}
        }
        
        for(List<Object> listObj:excelList){
        	List<Object> newListObj=new ArrayList<Object>();
        	
        	BigDecimal minPrice=null;
        	if(!StringTool.isEmpty(listObj.get(listObj.size()-1).toString())){
        		minPrice=new BigDecimal(listObj.get(listObj.size()-1).toString());
        	}
        	boolean flag=StringTool.isEmpty(listObj.get(6).toString())?true:false;
        	BigDecimal price=new BigDecimal(0);
        	if(!flag){
        		price=new BigDecimal(listObj.get(6).toString());
        	}
        	
        	newListObj.add(listObj.get(0));
        	newListObj.add(listObj.get(1));
        	String date=listObj.get(2).toString();
        	String packageName=listObj.get(3).toString();
        	newListObj.add(date);
        	newListObj.add(packageName);
        	newListObj.add(listObj.get(4));
        	newListObj.add(listObj.get(5));
    
    		if(!flag){
        		newListObj.add(price);
        	}else{
        		newListObj.add("");
        	}
        
        	if(minPrice!=null){
        		newListObj.add(minPrice);
        	}else{
        		newListObj.add("");
        	}
        	
        	Map<String, String> detailMap=skuMap.get(packageName);
        	if(detailMap!=null){
        		String skuPrice=detailMap.get(date);
        		if(!StringTool.isEmpty(skuPrice)){
        			newListObj.add(skuPrice);
        		}else{
        			newListObj.add("");
        		}
        	}else{
        		newListObj.add("");
        	}

    		newListObj.add("");
    		newListObj.add("");
        	
        	for(int i=7;i<listObj.size()-1;i++){
        		newListObj.add(listObj.get(i));
        	}
        	
        	list.add(newListObj);
        }
        
        String filename = "cms-"+DateUtil.dateToLongString(new Date())+".xlsx";
        boolean flag = ExcelTool.createCompareExcel(list, titles, filename);
        if(!flag){
            return ReturnUtil.returnMap(0, "Excel生成失败!");
        }
        
        return ReturnUtil.returnMap(1, filename);
    }

	@SuppressWarnings("unchecked")
    @Override
	public Map<String, Object> importSkuExcelOper(String excelPath) {
        String[] titles = {
		    "序号",
            "邮轮名",
            "航期",
            "舱型",
            "航线",
            "上传Sku价格",
        };
        
        //判断Excel是否符合要求  并获取数据
        Map<String ,Object> checkMap =  ExcelTool.getSkuExcelData(excelPath ,titles);
        if(Integer.parseInt(checkMap.get("status").toString())==0){
            return ReturnUtil.returnMap(0, checkMap.get("msg"));
        }
        
		List<String[]> list = (List<String[]>) checkMap.get("content");
		
		 String cruise=list.get(0)[1];
		Cruise  cruiseObj=(Cruise) commonDao.queryUniqueByHql("from Cruise where nameCn='"+cruise+"'");
		
		String sql="select taobao_travel_item from taobao_base_info where cruise='"+cruiseObj.getNumberCode()+"'";
		List<String> items = commonDao.getSqlQuery(sql).list();
		
		List<TaobaoSkuInfo> skuList=commonDao.queryByHql("from TaobaoSkuInfo where taobaoTravelItem in("+StringTool.listToSqlString(items)+") and packageName in('内舱2人房','内舱3人房','内舱4人房','海景2人房','海景3人房','海景4人房','阳台2人房','阳台3人房','阳台4人房')");
		
		Map<String, List<TaobaoSkuInfoDetail>> map=new HashMap<String, List<TaobaoSkuInfoDetail>>();
		
       for(String[] array:list){
    	   
    	   if(StringTool.isNotNull(array[5])){
    		   String date=array[2];
    		   String cabin=array[3];
    		   String price=array[5];
    		   
    		   List<TaobaoSkuInfoDetail> details=map.get(cabin);
    		   if(details==null){
    			   List<String> numberCodes=new ArrayList<String>();
    			   for(TaobaoSkuInfo skuInfo:skuList){
    				   if(skuInfo.getPackageName().equals(cabin)){
    					   numberCodes.add(skuInfo.getNumberCode());
    				   }
    			   }
    			   details=commonDao.queryByHql("from TaobaoSkuInfoDetail where date>now() and taobaoSkuInfo in("+StringTool.listToSqlString(numberCodes)+")");
    			   if(details==null){
    				   continue;
    			   }
    			   map.put(cabin, details);
    		   }
    		   for(TaobaoSkuInfoDetail detail:details){
    			   if(date.equals(DateUtil.dateToShortString(detail.getDate()))){
    				   detail.setPrice(new BigDecimal(price).multiply(new BigDecimal(100)));
    				   detail.setIsUpdate(1);
    				   detail.setAlterDate(new Date());
    				   commonDao.update(detail);
    			   }
    		   }
    		   
    	   }
    	   
       }
        
        return ReturnUtil.returnMap(1,null);
    }

	@Override
	public Map<String, Object> transferDataOper() {
		
		/*List<Provider> list=commonDao.queryByHql("from Provider");
		
		return ReturnUtil.returnMap(1,list);*/
		
		
		List<Stock> stockList=commonDao.queryByHql("from Stock where date>=now() and cruise='YVTAI'");
		
		  return ReturnUtil.returnMap(1,stockList);
		
	}
    
}