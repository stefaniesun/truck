package xyz.work.scheduled.svc.imp;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import xyz.dao.CommonDao;
import xyz.filter.JSON;
import xyz.filter.MyRequestUtil;
import xyz.util.Constant;
import xyz.util.DateUtil;
import xyz.util.StringTool;
import xyz.util.UUIDUtil;
import xyz.util.ZipUtil;
import xyz.work.base.model.Airway;
import xyz.work.base.model.Area;
import xyz.work.base.model.Backup;
import xyz.work.base.model.Cabin;
import xyz.work.base.model.Company;
import xyz.work.base.model.Cruise;
import xyz.work.base.model.Port;
import xyz.work.base.model.Shipment;
import xyz.work.base.model.Trip;
import xyz.work.base.model.Voyage;
import xyz.work.core.model.PlanWork;
import xyz.work.core.model.PlanWorkLog;
import xyz.work.excel.model.PriceStrategy;
import xyz.work.resources.model.Channel;
import xyz.work.resources.model.Provider;
import xyz.work.resources.model.Stock;
import xyz.work.resources.model.Tkview;
import xyz.work.resources.model.TkviewType;
import xyz.work.resources.svc.TkviewSvc;
import xyz.work.scheduled.TaobaoST;
import xyz.work.scheduled.svc.PlanWorkSTSvc;
import xyz.work.security.model.Possessor;
import xyz.work.security.model.Possessor_Relate;
import xyz.work.sell.model.Ptview;
import xyz.work.sell.model.PtviewSku;
import xyz.work.sell.model.PtviewSkuStock;
import xyz.work.sell.model.PtviewSku_TkviewType;
import xyz.work.taobao.model.Sku_TkviewType;
import xyz.work.taobao.model.TaobaoBaseInfo;
import xyz.work.taobao.model.TaobaoBookingRule;
import xyz.work.taobao.model.TaobaoCruiseItemExt;
import xyz.work.taobao.model.TaobaoSaleInfo;
import xyz.work.taobao.model.TaobaoSkuInfo;
import xyz.work.taobao.model.TaobaoSkuInfoDetail;
import xyz.work.taobao.model.TaobaoTravelItem;

@Service
public class PlanWorkSTSvcImp implements PlanWorkSTSvc {
	
	@Autowired
	CommonDao commonDao;
	
	@Autowired
	TaobaoST taobaoST;
	
	@Autowired
	TkviewSvc tkviewSvc; 
	
	@Override
	public int autoPlanWorkOper(){
		String hql = "from PlanWork t order by t.alterDate asc";
		PlanWork planWork = (PlanWork)commonDao.queryUniqueByHql(hql);
		
		if(planWork==null){
			return 0;
		}
		
		System.out.println(JSON.toJson(planWork));
		
		if(Constant.PLAN_TYPE_UPDATE_CRUISE.equals(planWork.getWorkType())){
			
			  String  cruise=planWork.getContent();
			  tkviewSvc.autoUpdateByCruiseOper(cruise);
			  
			  Constant.QUERY_DATA_TOKEN=DateUtil.dateToLongString(new Date());
			  
			   commonDao.delete(planWork);
			
			   PlanWorkLog planWorkLog = new PlanWorkLog();
               planWorkLog.setNumberCode(UUIDUtil.getUUIDStringFor32());
               planWorkLog.setWorkType(Constant.PLAN_TYPE_UPDATE_CRUISE);
               planWorkLog.setContent(cruise);
               planWorkLog.setAddDate(new Date());
               planWorkLog.setAlterDate(new Date());
               commonDao.save(planWorkLog);
               return 1;
		}else if(Constant.PLAN_TYPE_UPDATE_TKVIEW.equals(planWork.getWorkType())){
			
			System.out.println("执行单品自动更新");
			
			String  tkview=planWork.getContent();
			tkviewSvc.autoUpdateByTkviewOper(tkview);
			
			Constant.QUERY_DATA_TOKEN=DateUtil.dateToLongString(new Date());
			
			commonDao.delete(planWork);
			
			 PlanWorkLog planWorkLog = new PlanWorkLog();
             planWorkLog.setNumberCode(UUIDUtil.getUUIDStringFor32());
             planWorkLog.setWorkType(Constant.PLAN_TYPE_UPDATE_TKVIEW);
             planWorkLog.setContent(tkview);
             planWorkLog.setAddDate(new Date());
             planWorkLog.setAlterDate(new Date());
             commonDao.save(planWorkLog);
             return 1;
		}else{
			commonDao.delete(planWork);
		}
		
		return 0;
	}

    @Override
    public int stockAutoPlanWorkOper() {
        String stockSql = "SELECT s.number_code FROM stock s WHERE s.type = 2 ";
        stockSql += "AND datediff(NOW(), s.alter_date) > 1 ";
        @SuppressWarnings("unchecked")
        List<String> stockList = commonDao.getSqlQuery(stockSql).list();
        
        if(stockList.size() > 0){
            String deleteSql = "DELETE FROM stock WHERE number_code IN ("+ StringTool.listToSqlString(stockList) +") ";
            int result = commonDao.getSqlQuery(deleteSql).executeUpdate(); 
            
            if(result > 0){
                System.out.println(result);
                Date date = new Date();
                String content = "删除单品库存数据"+result+"条";
                PlanWork planWorkObj = new PlanWork();
                planWorkObj.setNumberCode(UUIDUtil.getUUIDStringFor32());
                //planWorkObj.setUsername(MyRequestUtil.getUsername());
                planWorkObj.setWorkType("stock");
                planWorkObj.setContent(content);
                planWorkObj.setRemark("");
                planWorkObj.setAddDate(date);
                planWorkObj.setAlterDate(date);
                commonDao.save(planWorkObj);
                
                PlanWorkLog planWorkLog = new PlanWorkLog();
                planWorkLog.setNumberCode(UUIDUtil.getUUIDStringFor32());
                planWorkLog.setWorkType("stock");
                planWorkLog.setContent(content);
                planWorkLog.setAddDate(date);
                planWorkLog.setAlterDate(date);
                commonDao.save(planWorkLog);
                return 1;
            }
        }
        
        return 0;
    }
    
    @Override
    public int  tkviewPlanWorkOper(){
        String updateSql = "UPDATE tkview t SET ";
        updateSql += "t.user_name = '', ";
        updateSql += "t.release_date = NULL, ";
        updateSql += "t.alter_date = NOW() ";
        updateSql += "WHERE t.release_date <= NOW() ";
        int result = commonDao.getSqlQuery(updateSql).executeUpdate();
        if(result > 0){
            return 1;
        }
        
        return 0;
    }

	@Override
	public int royalDataPlanWorkOper() {
		
		
		Runtime runtime=Runtime.getRuntime();
		
		try{
			Process process=runtime.exec("C://winSCP//WinSCP.com /ini=nul /script=C://winSCP//task.txt");
			
			process.waitFor();
			
			ZipUtil.upzipFile("C://ppf//CHN_CHN_CNY_cruise_no_air_price.zip","C://ppf");
			
			return 1;
			
		}catch(Exception e){
		    System.out.println("Error!");
		}
		
		return 0;
	}

    @Override
    public int tablePlanWorkOper() {
        int count = 0;
        /* 邮轮公司 */
        String companyHql = "from Company";
        @SuppressWarnings("unchecked")
        List<Company> companyList = commonDao.queryByHql(companyHql);
        String companyStr = "";
        for(Company companyObj : companyList){
            String nameEn = StringTool.isEmpty(companyObj.getNameEn())==true?null:("'"+companyObj.getNameEn()+"'");
            String remark = StringTool.isEmpty(companyObj.getRemark())==true?null:("'"+companyObj.getRemark()+"'");
            companyStr += "INSERT INTO `company` (iidd,number_code,name_cn,name_en,remark,add_date,alter_date) VALUES (";
            companyStr += "'"+ companyObj.getIidd() +"', '"+ companyObj.getNumberCode() +"', ";
            companyStr += "'"+ companyObj.getNameCn() +"', '"+ nameEn +"', "+ remark+", ";
            companyStr += "'"+ DateUtil.dateToString(companyObj.getAddDate()) +"', '"+ DateUtil.dateToString(companyObj.getAlterDate()) +"'); ";
            companyStr += " \r\n ";
        }
        count += companyList.size();
        
        /* 邮轮 */
        String cruiseHql = "from Cruise";
        @SuppressWarnings("unchecked")
        List<Cruise> cruiseList = commonDao.queryByHql(cruiseHql);
        String cruiseStr = "";
        for(Cruise cruiseObj : cruiseList){
            String nameEn = StringTool.isEmpty(cruiseObj.getNameEn())==true?null:("'"+cruiseObj.getNameEn()+"'");
            String remark = StringTool.isEmpty(cruiseObj.getRemark())==true?null:("'"+cruiseObj.getRemark()+"'");
            String images = StringTool.isEmpty(cruiseObj.getImages())==true?null:("'"+cruiseObj.getImages()+"'");
            String detail = StringTool.isEmpty(cruiseObj.getDetail())==true?null:("'"+cruiseObj.getDetail()+"'");
            String weixinLargeImg = StringTool.isEmpty(cruiseObj.getWeixinLargeImg())==true?null:("'"+cruiseObj.getWeixinLargeImg()+"'");
            String weixinSmallImg = StringTool.isEmpty(cruiseObj.getWeixinSmallImg())==true?null:("'"+cruiseObj.getWeixinSmallImg()+"'");
            cruiseStr += "INSERT INTO `cruise` (company,number_code,name_cn,name_en,mark,sort_num,wide,length,tonnage,busload,";
            cruiseStr += "floor,total_cabin,voltage_source,avg_speed,laundromat,library,survey,remark,images,add_date,";
            cruiseStr += "alter_date,iidd,detail,weixin_large_img,weixin_small_img) VALUES (";
            cruiseStr += "'"+ cruiseObj.getCompany() +"', '"+ cruiseObj.getNumberCode() +"', '"+ cruiseObj.getNameCn() +"', ";
            cruiseStr += "'"+ nameEn +"', '"+ cruiseObj.getMark() +"', '"+ cruiseObj.getSortNum() +"', ";
            cruiseStr += "'"+ cruiseObj.getWide() +"', '"+cruiseObj.getLength() +"', '"+ cruiseObj.getTonnage() +"', ";
            cruiseStr += "'"+ cruiseObj.getBusload() +"', '"+ cruiseObj.getFloor() +"', '"+ cruiseObj.getTotalCabin() +"', ";
            cruiseStr += "'"+ cruiseObj.getVoltageSource() +"', '"+ cruiseObj.getAvgSpeed() +"', '"+ cruiseObj.getLaundromat() +"', ";
            cruiseStr += "'"+ cruiseObj.getLibrary() +"', '"+ cruiseObj.getSurvey() +"', "+remark+", ";
            cruiseStr += images+", '"+ DateUtil.dateToString(cruiseObj.getAddDate()) +"', '"+ DateUtil.dateToString(cruiseObj.getAlterDate()) +"', ";
            cruiseStr += "'"+ cruiseObj.getIidd() +"', "+ detail +", "+ weixinLargeImg +", "+ weixinSmallImg +"); ";
            cruiseStr += " \r\n ";
        }
        count += cruiseList.size();
        
        /* 舱型 */
        String cabinHql = "from Cabin";
        @SuppressWarnings("unchecked")
        List<Cabin> cabinList = commonDao.queryByHql(cabinHql);
        String cabinStr = "";
        for(Cabin cabinObj : cabinList){
            String mark = StringTool.isEmpty(cabinObj.getMark())==true?null:("'"+cabinObj.getMark()+"'");
            String floors = StringTool.isEmpty(cabinObj.getFloors())==true?null:("'"+cabinObj.getFloors()+"'");
            String acreage = StringTool.isEmpty(cabinObj.getAcreage())==true?null:("'"+cabinObj.getAcreage()+"'");
            String survey = StringTool.isEmpty(cabinObj.getSurvey())==true?null:("'"+cabinObj.getSurvey()+"'");
            String remark = StringTool.isEmpty(cabinObj.getRemark())==true?null:("'"+cabinObj.getRemark()+"'");
            String detail = StringTool.isEmpty(cabinObj.getDetail())==true?null:("'"+cabinObj.getDetail()+"'");
            String images = StringTool.isEmpty(cabinObj.getImages())==true?null:("'"+cabinObj.getImages()+"'");
            cabinStr += "INSERT INTO `cabin` (iidd,number_code,cruise,type,name_cn,mark,volume,max_volume,floors,acreage,survey,is_open,remark,detail,images,add_date,alter_date) VALUES (";
            cabinStr += "'"+ cabinObj.getIidd() +"', '"+ cabinObj.getNumberCode() +"', '"+ cabinObj.getCruise() +"', ";
            cabinStr += "'"+ cabinObj.getType() +"', '"+ cabinObj.getNameCn() +"', '"+ mark +"'";
            cabinStr += "'"+ cabinObj.getVolume() +"', '"+ cabinObj.getMaxVolume() +"', '"+ floors +"', ";
            cabinStr += "'"+ acreage +"', '"+ survey +"', '"+ cabinObj.getIsOpen() +"', ";
            cabinStr += "'"+ remark +"', '"+ detail +"', '"+ images +"', ";
            cabinStr += "'"+ DateUtil.dateToString(cabinObj.getAddDate()) +"', '"+ DateUtil.dateToString(cabinObj.getAlterDate()) +"'); ";
            cabinStr += " \r\n ";
        }
        count += cabinList.size();
        
        /* 单品种类 */
        String tkviewTypeHql = "from TkviewType";
        @SuppressWarnings("unchecked")
        List<TkviewType> tkviewTypeList = commonDao.queryByHql(tkviewTypeHql);
        String tkviewTypeStr = "";
        for(TkviewType typeObj : tkviewTypeList){
            String remark = StringTool.isEmpty(typeObj.getRemark())==true?null:("'"+typeObj.getRemark()+"'");
            tkviewTypeStr += "INSERT INTO `tkview_type` (iidd,number_code,cruise,cabin,remark,add_date,alter_date) VALUES (";
            tkviewTypeStr += "'"+ typeObj.getIidd() +"', '"+ typeObj.getNumberCode() +"', ";
            tkviewTypeStr += "'"+ typeObj.getCruise() +"', '"+ typeObj.getCabin() +"', "+ remark +", ";
            tkviewTypeStr += "'"+ DateUtil.dateToString(typeObj.getAddDate()) +"', '"+ DateUtil.dateToString(typeObj.getAlterDate()) +"'); ";
            tkviewTypeStr += " \r\n ";
        }
        count += tkviewTypeList.size();
        
        /* 航区 */
        String areaHql = "from Area ";
        @SuppressWarnings("unchecked")
        List<Area> areaList = commonDao.queryByHql(areaHql);
        String areaStr = "";
        for(Area areaObj : areaList){
            areaStr += "INSERT INTO `area` (iidd,number_code,name_cn,remark,add_date,alter_date) VALUES (";
            areaStr += "'"+ areaObj.getIidd() +"', '"+ areaObj.getNumberCode() +"', ";
            String remark = StringTool.isEmpty(areaObj.getRemark())==true?null:("'"+areaObj.getRemark()+"'");
            areaStr += "'"+ areaObj.getNameCn() +"', '"+ remark +"', ";
            areaStr += "'"+ DateUtil.dateToString(areaObj.getAddDate()) +"', '"+ DateUtil.dateToString(areaObj.getAlterDate()) +"'); ";
            areaStr += " \r\n ";
        }
        count += areaList.size();
        
        /* 港口 */
        String portHql = "from Port";
        @SuppressWarnings("unchecked")
        List<Port> portList = commonDao.queryByHql(portHql);
        String portStr = "";
        for(Port portObj : portList){
            String country = StringTool.isEmpty(portObj.getCountry())==true?null:("'"+ portObj.getCountry() +"'");
            String address = StringTool.isEmpty(portObj.getAddress())==true?null:("'"+ portObj.getAddress() +"'");
            String remark = StringTool.isEmpty(portObj.getRemark())==true?null:("'"+ portObj.getRemark() +"'");
            portStr += "INSERT INTO `port` (iidd,number_code,name_cn,country,address,remark,add_date,alter_date) VALUES (";
            portStr += "'"+ portObj.getIidd() +"', '"+ portObj.getNumberCode() +"', '"+ portObj.getNameCn() +"', ";
            portStr += "'"+ country +"', '"+ address +"', "+remark+", ";
            portStr += "'"+ DateUtil.dateToString(portObj.getAddDate()) +"', '"+ DateUtil.dateToString(portObj.getAlterDate()) +"'); ";
            portStr += " \r\n ";
        }
        count += portList.size();
        
        /* 航线 */
        String airwayHql = "from Airway ";
        @SuppressWarnings("unchecked")
        List<Airway> airwayList = commonDao.queryByHql(airwayHql);
        String airwayStr = "";
        for(Airway airwayObj : airwayList){
            String remark = StringTool.isEmpty(airwayObj.getRemark())==true?null:("'"+airwayObj.getRemark()+"'");
            airwayStr += "INSERT INTO `airway` (area,number_code,name_cn,days,nights,iidd,remark,add_date,alter_date) VALUES (";
            airwayStr += "'"+ airwayObj.getArea() +"', '"+ airwayObj.getNumberCode() +"', ";
            airwayStr += "'"+ airwayObj.getNameCn() +"', '"+ airwayObj.getDays() +"', '"+ airwayObj.getNights() +"', ";
            airwayStr += "'"+ airwayObj.getIidd() +"', "+ remark +", ";
            airwayStr += "'"+ DateUtil.dateToString(airwayObj.getAddDate()) +"', '"+ DateUtil.dateToString(airwayObj.getAlterDate()) +"'); ";
            airwayStr += " \r\n ";
        }
        count += airwayList.size();
        
       String tripHql = "from Trip";
       @SuppressWarnings("unchecked")
       List<Trip> tripList = commonDao.queryByHql(tripHql);
       String tripStr = "";
       for(Trip tripObj : tripList){
           String port = StringTool.isEmpty(tripObj.getPort())==true?null:("'"+tripObj.getPort()+"'");
           tripStr += "INSERT INTO `trip` (iidd,airway,number_code,port,time_type,time,priority,add_date,alter_date) VALUES (";
           tripStr += "'"+ tripObj.getIidd() +"', '"+ tripObj.getAirway() +"', '"+ tripObj.getNumberCode() +"', ";
           tripStr += "'"+ port +"', '"+ tripObj.getTimeType() +"', '"+ tripObj.getTime() +"', ";
           tripStr += "'"+ tripObj.getPriority() +"', ";
           tripStr += "'"+ DateUtil.dateToString(tripObj.getAddDate()) +"', '"+ DateUtil.dateToString(tripObj.getAlterDate()) +"'); ";
           tripStr += " \r\n ";
       }
       count += tripList.size();
       
       String shipmentHql = "from Shipment";
       @SuppressWarnings("unchecked")
       List<Shipment> shipmentList = commonDao.queryByHql(shipmentHql);
       String shipmentStr = "";
       for(Shipment shipObj : shipmentList){
           String detail = StringTool.isEmpty(shipObj.getDetail())==true?null:("'"+shipObj.getDetail()+"'");
           String remark = StringTool.isEmpty(shipObj.getRemark())==true?null:("'"+shipObj.getRemark()+"'");
           shipmentStr += "INSERT INTO `shipment` (iidd,cruise,number_code,travel_date,mark,travel_end_date,";
           shipmentStr += "final_sale_date,area,airway,start_place,trip_days,detail,remark,";
           shipmentStr += "add_date,alter_date) VALUES (";
           shipmentStr += "'"+ shipObj.getIidd() +"', '"+ shipObj.getCruise() +"', '"+ shipObj.getNumberCode() +"', ";
           shipmentStr += "'"+ DateUtil.dateToShortString(shipObj.getTravelDate()) +"', '"+ shipObj.getMark() +"', ";
           shipmentStr += "'"+ DateUtil.dateToShortString(shipObj.getTravelEndDate()) +"', ";
           shipmentStr += "'"+ DateUtil.dateToString(shipObj.getFinalSaleDate()) +"', '"+ shipObj.getArea() +"', ";
           shipmentStr += "'"+ shipObj.getAirway() +"', '"+ shipObj.getStartPlace() +"', '"+ shipObj.getTripDays() +"', ";
           shipmentStr += detail+", "+ remark +", '"+ DateUtil.dateToString(shipObj.getAddDate()) +"',  ";
           shipmentStr += "'"+ DateUtil.dateToString(shipObj.getAlterDate()) +"'); ";
           shipmentStr += "";
           shipmentStr += " \r\n ";
       }
       count += shipmentList.size();
       
       String voyageHql = "from Voyage";
       @SuppressWarnings("unchecked")
       List<Voyage> voyageList = commonDao.queryByHql(voyageHql);
       String voyageStr = "";
       for(Voyage voyageObj : voyageList){
           String arrivalTime = StringTool.isEmpty(voyageObj.getArrivalTime())==true?null:("'"+ voyageObj.getArrivalTime() +"'");
           String detail = StringTool.isEmpty(voyageObj.getDetail())==true?null:("'"+ voyageObj.getDetail() +"'");
           String leaveTime = StringTool.isEmpty(voyageObj.getLeaveTime())==true?null:("'"+ voyageObj.getLeaveTime() +"'");
           String remark = StringTool.isEmpty(voyageObj.getRemark())==true?null:("'"+voyageObj.getRemark()+"'");
           voyageStr += "INSERT INTO `voyage` (iidd,add_date,alter_date,arrival_time,detail,leave_time,";
           voyageStr += "number_code,port,priority,remark,shipment,time,time_type) VALUES (";
           voyageStr += "'"+ voyageObj.getIidd() +"', '"+ DateUtil.dateToString(voyageObj.getAddDate()) +"', ";
           voyageStr += "'"+ DateUtil.dateToString(voyageObj.getAlterDate()) +"', "+ arrivalTime +", ";
           voyageStr += detail+", "+ leaveTime +", '"+ voyageObj.getNumberCode() +"', ";
           voyageStr += "'"+ voyageObj.getPort() +"', '"+ voyageObj.getPriority() +"', ";
           voyageStr += remark+", '"+ voyageObj.getShipment() +"', ";
           voyageStr += "'"+ voyageObj.getTime() +"', '"+ voyageObj.getTimeType() +"'); ";
           voyageStr += " \r\n ";
       }
       count += voyageList.size();
       
       /* 供应商 */
       String providerHql = "from Provider";
       @SuppressWarnings("unchecked")
       List<Provider> providerList = commonDao.queryByHql(providerHql);
       String providerStr = "";
       for(Provider proObj :providerList){
           String address = StringTool.isEmpty(proObj.getAddress())==true?null:("'"+proObj.getAddress()+"'");
           String sign = StringTool.isEmpty(proObj.getSign())==true?null:("'"+proObj.getSign()+"'");
           String weChat = StringTool.isEmpty(proObj.getWeChat())==true?null:("'"+proObj.getWeChat()+"'");
           String talkGroup = StringTool.isEmpty(proObj.getTalkGroup())==true?null:("'"+proObj.getTalkGroup()+"'");
           String policy = StringTool.isEmpty(proObj.getPolicy())==true?null:("'"+proObj.getPolicy()+"'"); 
           String settlement = StringTool.isEmpty(proObj.getSettlement())==true?null:("'"+proObj.getSettlement()+"'");
           String remark = StringTool.isEmpty(proObj.getRemark())==true?null:("'"+proObj.getRemark()+"'");
           providerStr += "INSERT INTO `provider` (iidd,number_code,grade,name_cn,mark,status,is_responsible,";
           providerStr += "address,sign,we_chat,talk_group,policy,settlement,remark,";
           providerStr += "add_date,alter_date) VALUES (";
           providerStr += "'"+ proObj.getIidd() +"', '"+ proObj.getGrade() +"', '"+ proObj.getNameCn() +"', ";
           providerStr += "'"+ proObj.getMark() +"', '"+ proObj.getStatus() +"', '"+ proObj.getIsResponsible() +"', ";
           providerStr += address +", "+ sign +", "+ weChat +", "+ talkGroup +", ";
           providerStr += policy+", "+ settlement +", "+ remark +",";
           providerStr += "'"+ DateUtil.dateToString(proObj.getAddDate()) +"', '"+ DateUtil.dateToString(proObj.getAlterDate()) +"'); ";
           providerStr += " \r\n ";
       }
       count += providerList.size();
       
       String priceHql = "from PriceStrategy";
       @SuppressWarnings("unchecked")
       List<PriceStrategy> priceList = commonDao.queryByHql(priceHql);
       String priceStr = "";
       for(PriceStrategy priceObj : priceList){
           String remark = StringTool.isEmpty(priceObj.getRemark())==true?null:("'"+priceObj.getRemark()+"'");
           priceStr += "INSERT INTO `price_strategy` (iidd,number_code,min_price,max_price,price_markup,remark,add_date,alter_date) VALUES (";
           priceStr += "'"+ priceObj.getIidd() +"', '"+ priceObj.getNumberCode() +"', ";
           priceStr += "'"+ priceObj.getMinPrice() +"', '"+ priceObj.getMaxPrice() +"', ";
           priceStr += "'"+ priceObj.getPriceMarkup() +"', "+ remark +", ";
           priceStr += "'"+ DateUtil.dateToString(priceObj.getAddDate()) +"', '"+ DateUtil.dateToString(priceObj.getAlterDate()) +"'); ";
           priceStr += " \r\n ";
       }
       count += priceList.size();
       
       String tkviewHql = "from Tkview";
       @SuppressWarnings("unchecked")
       List<Tkview> tkivewList = commonDao.queryByHql(tkviewHql);
       String tkviewStr = "";
       for(Tkview tkviewObj : tkivewList){
           String mark = StringTool.isEmpty(tkviewObj.getMark())==true?null:("'"+tkviewObj.getMark()+"'");
           String release = null;
           if(tkviewObj.getReleaseDate()!=null){
               release = DateUtil.dateToString(tkviewObj.getReleaseDate());
           }
           String releaseDate = StringTool.isEmpty(release)==true?null:("'"+ release +"'");
           String userName = StringTool.isEmpty(tkviewObj.getUserName())==true?null:("'"+tkviewObj.getUserName()+"'");
           String remark = StringTool.isEmpty(tkviewObj.getRemark())==true?null:("'"+tkviewObj.getRemark()+"'");
           tkviewStr += "INSERT INTO `tkview` (iidd,cruise,cabin,volume,shipment,shipment_mark,shipment_travel_date,";
           tkviewStr += "number_code,name_cn,mark,release_date,user_name,remark,add_date,alter_date) VALUES (";
           tkviewStr += "'"+ tkviewObj.getIidd() +"', '"+ tkviewObj.getCruise() +"', ";
           tkviewStr += "'"+ tkviewObj.getCabin() +"', '"+ tkviewObj.getVolume() +"',";
           tkviewStr += "'"+ tkviewObj.getShipment() +"', '"+ tkviewObj.getShipmentMark() +"', ";
           tkviewStr += "'"+ DateUtil.dateToShortString(tkviewObj.getShipmentTravelDate()) +"', ";
           tkviewStr += "'"+ tkviewObj.getNumberCode() +"', '"+ tkviewObj.getNameCn() +"', ";
           tkviewStr += mark+", "+ releaseDate +", "+ userName +", "+ remark +", ";
           tkviewStr += "'"+ DateUtil.dateToString(tkviewObj.getAddDate()) +"', '"+ DateUtil.dateToString(tkviewObj.getAlterDate()) +"'); ";
           tkviewStr += " \r\n ";
       }
       count += tkivewList.size();
       
       String stockHql = "from Stock";
       @SuppressWarnings("unchecked")
       List<Stock> stockList = commonDao.queryByHql(stockHql);
       String stockStr = "";
       for(Stock stockObj : stockList){
           String nickName = StringTool.isEmpty(stockObj.getNickName())==true?null:("'"+stockObj.getNickName()+"'");
           String costRemark = StringTool.isEmpty(stockObj.getCostRemark())==true?null:("'"+stockObj.getCostRemark()+"'");
           String otherProvider = StringTool.isEmpty(stockObj.getOtherProvider())==true?null:("'"+stockObj.getOtherProvider()+"'");
           String remark = StringTool.isEmpty(stockObj.getRemark())==true?null:("'"+stockObj.getRemark()+"'");
           String specialCost = StringTool.isEmpty(stockObj.getSpecialCost())==true?null:("'"+stockObj.getSpecialCost()+"'");
           String yuYue = StringTool.isEmpty(stockObj.getYuyue())==true?null:("'"+stockObj.getYuyue()+"'");
           String rgb = StringTool.isEmpty(stockObj.getRgb())==true?null:("'"+stockObj.getRgb()+"'");
           
           String date = null;
           if(stockObj.getDate() != null){
               date = DateUtil.dateToShortString(stockObj.getDate());
           }
           String overDate = null;
           if(stockObj.getOverDate() != null){
               overDate = DateUtil.dateToString(stockObj.getOverDate());
           }
           
           stockStr += "INSERT INTO `stock` (iidd,tkview,date,provider,grade,number_code,nick_name,stock,cost,";
           stockStr += "cost_remark,use_stock,surpass,use_surpass,type,over_date,other_provider,priority,remark,";
           stockStr += "special_cost,yu_yue,left_stock,rgb,add_date,alter_date) VALUES (";
           stockStr += "'"+ stockObj.getIidd() +"', '"+ stockObj.getTkview() +"', ";
           stockStr += "'"+ date +"', '"+ stockObj.getProvider() +"', ";
           stockStr += "'"+ stockObj.getGrade() +"', '"+ stockObj.getNumberCode() +"', "+ nickName +", ";
           stockStr += "'"+ stockObj.getStock() +"', '"+ stockObj.getCost() +"', "+ costRemark +",";
           stockStr += "'"+ stockObj.getUseStock() +"', '"+ stockObj.getSurpass() +"', '"+ stockObj.getUseSurpass() +"',";
           stockStr += "'"+ stockObj.getType() +"', '"+ overDate +"', "+ otherProvider +",";
           stockStr += "'"+ stockObj.getPriority() +"', "+ remark +", "+ specialCost +", "+ yuYue +", ";
           stockStr += "'"+ stockObj.getLeftStock() +"', "+ rgb +", ";
           stockStr += "'"+ DateUtil.dateToString(stockObj.getAddDate()) +"', '"+ DateUtil.dateToString(stockObj.getAlterDate()) +"'); ";
           stockStr += " \r\n ";
       }
       count += stockList.size();
       
        /* 渠道 */
        String channelHql = "from Channel";
        @SuppressWarnings("unchecked")
        List<Channel> channelList = commonDao.queryByHql(channelHql);
        String channelStr = "";
        for(Channel channelObj : channelList){
            String code = StringTool.isEmpty(channelObj.getCode())==true?null:("'"+channelObj.getCode()+"'");
            String appKey = StringTool.isEmpty(channelObj.getAppKey())==true?null:("'"+channelObj.getAppKey()+"'");
            String appSecret = StringTool.isEmpty(channelObj.getAppSecret())==true?null:("'"+channelObj.getAppSecret()+"'");
            String redirectUri = StringTool.isEmpty(channelObj.getRedirectUri())==true?null:("'"+channelObj.getRedirectUri()+"'");
            String sessionKey = StringTool.isEmpty(channelObj.getSessionKey())==true?null:("'"+channelObj.getSessionKey()+"'");
            String remark = StringTool.isEmpty(channelObj.getRemark())==true?null:("'"+channelObj.getRemark()+"'");
            
            channelStr += "INSERT INTO `channel` (iidd,number_code,name_cn,is_authorize,code,app_key,";
            channelStr += "app_secret,redirect_uri,session_key,remark,add_date,alter_date) VALUES (";
            channelStr += "'"+ channelObj.getIidd() +"', '"+ channelObj.getNumberCode() +"', ";
            channelStr += "'"+ channelObj.getNameCn() +"', '"+ channelObj.getIsAuthorize() +"', "+ code +", "+ appKey +", ";
            channelStr += appSecret+", "+ redirectUri +", "+ sessionKey +", "+ remark +", ";
            channelStr += "'"+ DateUtil.dateToString(channelObj.getAddDate()) +"', '"+ DateUtil.dateToString(channelObj.getAlterDate()) +"'); ";
            channelStr += " \r\n ";
        }
        count += channelList.size();
        
        /* 淘宝travelItem */
        String travelItemHql = "from TaobaoTravelItem";
        @SuppressWarnings("unchecked")
        List<TaobaoTravelItem> travelItemList = commonDao.queryByHql(travelItemHql);
        String travelItemStr = "";
        for(TaobaoTravelItem traObj : travelItemList){
            String created = null;
            if(traObj.getCreated() != null){
                created = DateUtil.dateToString(traObj.getCreated());
            }
            String itemId = StringTool.isEmpty(traObj.getItemId())==true?null:("'"+ traObj.getItemId() +"'");
            String sellerId = StringTool.isEmpty(traObj.getSellerId())==true?null:("'"+ traObj.getSellerId() +"'");
            String modified = null;
            if(traObj.getModified()!=null){
                modified = DateUtil.dateToString(traObj.getModified());
            }
            String requestId = StringTool.isEmpty(traObj.getRequestId())==true?null:("'"+traObj.getRequestId()+"'");
            
            travelItemStr += "INSERT INTO `taobao_travel_item` (iidd,channel,channel_name_cn,number_code,created,item_id,";
            travelItemStr += "seller_id,item_status,item_type,modified,request_id,add_date,alter_date) VALUES (";
            travelItemStr += "'"+ traObj.getIidd() +"', '"+ traObj.getChannel() +"', '"+ traObj.getChannelNameCn() +"', ";
            travelItemStr += "'"+ traObj.getNumberCode() +"',"+created+", "+ itemId +", "+ sellerId +", ";
            travelItemStr += "'"+ traObj.getItemStatus() +"', '"+ traObj.getItemType() +"', "+modified+", "+requestId+", ";
            travelItemStr += "'"+ DateUtil.dateToString(traObj.getAddDate()) +"', '"+ DateUtil.dateToString(traObj.getAlterDate()) +"'); ";
            travelItemStr += " \r\n ";
        }
        count += travelItemList.size();
        
        /* 淘宝baseInfo */
        String baseInfoHql = "from TaobaoBaseInfo";
        @SuppressWarnings("unchecked")
        List<TaobaoBaseInfo> baseInfoList = commonDao.queryByHql(baseInfoHql);
        String baseInfoStr = "";
        for(TaobaoBaseInfo baseObj : baseInfoList){
            String categoryId = StringTool.isEmpty(baseObj.getCategoryId())==true?null:("'"+baseObj.getCategoryId()+"'");
            String desc = StringTool.isEmpty(baseObj.getDesc())==true?null:("'"+baseObj.getDesc()+"'");
            String picUrls = StringTool.isEmpty(baseObj.getPicUrls())==true?null:("'"+baseObj.getPicUrls()+"'");
            String itemUrl = StringTool.isEmpty(baseObj.getItemUrl())==true?null:("'"+baseObj.getItemUrl()+"'");
            String remark = StringTool.isEmpty(baseObj.getRemark())==true?null:("'"+baseObj.getRemark()+"'");
            
            baseInfoStr += "INSERT INTO `taobao_base_info` (iidd,taobao_travel_item,category_id,cruise,number_code,title,prov,city,";
            baseInfoStr += "trip_max_days,accom_nights,to_locations,from_locations,taobao_desc,item_type,pic_urls,item_url,";
            baseInfoStr += "sub_titles,remark,add_date,alter_date) VALUES (";
            baseInfoStr += "'"+ baseObj.getIidd() +"', '"+ baseObj.getTaobaoTravelItem() +"', "+ categoryId +",";
            baseInfoStr += "'"+ baseObj.getCruise() +"', '"+ baseObj.getNumberCode() +"', '"+ baseObj.getTitle() +"', ";
            baseInfoStr += "'"+ baseObj.getProv() +"', '"+ baseObj.getCity() +"', '"+ baseObj.getTripMaxDays() +"', ";
            baseInfoStr += "'"+ baseObj.getAccomNights() +"', '"+ baseObj.getToLocations() +"', ";
            baseInfoStr += "'"+ baseObj.getFromLocations() +"', "+ desc +", '"+ baseObj.getItemType() +"', "+ picUrls +",";
            baseInfoStr += itemUrl +", '"+ baseObj.getSubTitles() +"', "+ remark +", ";
            baseInfoStr += "'"+ DateUtil.dateToString(baseObj.getAddDate()) +"', '"+ DateUtil.dateToString(baseObj.getAlterDate()) +"'); ";
            baseInfoStr += " \r\n ";
        }
        count += baseInfoList.size();
        
        /* 淘宝cruiseItemExt */
        String cruiseItemExtHql = "from TaobaoCruiseItemExt";
        @SuppressWarnings("unchecked")
        List<TaobaoCruiseItemExt> cruiseItemExtList = commonDao.queryByHql(cruiseItemExtHql);
        String cruiseItemExtStr = "";
        for(TaobaoCruiseItemExt extObj : cruiseItemExtList){
            cruiseItemExtStr += "INSERT INTO `taobao_cruise_item_ext` (iidd,taobao_travel_item,number_code,";
            cruiseItemExtStr += "cruise_company,ship_name,cruise_line,ship_up,ship_down,ship_fee_include,";
            cruiseItemExtStr += "add_date,alter_date) VALUES (";
            cruiseItemExtStr += "'"+ extObj.getIidd() +"', '"+ extObj.getTaobaoTravelItem() +"', ";
            cruiseItemExtStr += "'"+ extObj.getNumberCode() +"', '"+ extObj.getCruiseCompany() +"', ";
            cruiseItemExtStr += "'"+ extObj.getShipName() +"', '"+ extObj.getCruiseLine() +"', ";
            cruiseItemExtStr += "'"+ extObj.getShipUp() +"', '"+ extObj.getShipDown() +"', '"+ extObj.getShipFeeInclude() +"', ";
            cruiseItemExtStr += "'"+ DateUtil.dateToString(extObj.getAddDate()) +"', '"+ DateUtil.dateToString(extObj.getAlterDate()) +"'); ";
            cruiseItemExtStr += " \r\n ";
        }
        count += cruiseItemExtList.size();
        
        /* 淘宝bookingRule */
        String ruleHql = "from TaobaoBookingRule";
        @SuppressWarnings("unchecked")
        List<TaobaoBookingRule> ruleList = commonDao.queryByHql(ruleHql);
        String ruleStr = "";
        for(TaobaoBookingRule ruleObj : ruleList){
            ruleStr += "INSERT INTO `taobao_booking_rule` (iidd,taobao_travel_item,number_code,rule_type,";
            ruleStr += "rule_desc,add_date,alter_date) VALUES (";
            ruleStr += "'"+ ruleObj.getIidd() +"', '"+ ruleObj.getTaobaoTravelItem() +"', ";
            ruleStr += "'"+ ruleObj.getNumberCode() +"', '"+ ruleObj.getRuleType() +"', ";
            ruleStr += "'"+ ruleObj.getRuleDesc() +"', ";
            ruleStr += "'"+ DateUtil.dateToString(ruleObj.getAddDate()) +"', '"+ DateUtil.dateToString(ruleObj.getAlterDate()) +"'); ";
            ruleStr += " \r\n ";
        }
        count += ruleList.size();
        
        /* 淘宝saleInfo */
        String saleHql = "from TaobaoSaleInfo";
        @SuppressWarnings("unchecked")
        List<TaobaoSaleInfo> saleList = commonDao.queryByHql(saleHql);
        String saleStr = "";
        for(TaobaoSaleInfo saleObj : saleList){
            saleStr += "INSERT INTO `taobao_sale_info` (iidd,taobao_travel_item,number_code,duration,sale_type,seller_cids,";
            saleStr += "sub_stock,has_discount,has_invoice,has_showcase,support_onsale_auto_refund,merchant,network_id,";
            saleStr += "start_combo_date,end_combo_date,add_date,alter_date) VALUES (";
            saleStr += "'"+ saleObj.getIidd() +"', '"+ saleObj.getTaobaoTravelItem() +"', '"+ saleObj.getNumberCode() +"', ";
            saleStr += "'"+ saleObj.getDuration() +"', '"+ saleObj.getSaleType() +"', '"+ saleObj.getSellerCids() +"', ";
            saleStr += "'"+ saleObj.getSubStock() +"', '"+ saleObj.getHasDiscount() +"', '"+ saleObj.getHasInvoice() +"', ";
            saleStr += "'"+ saleObj.getHasShowcase() +"', '"+ saleObj.getSupportOnsaleAutoRefund() +"', ";
            saleStr += "'"+ saleObj.getMerchant() +"', '"+ saleObj.getNetworkId() +"', ";
            saleStr += " '"+ DateUtil.dateToString(saleObj.getStartComboDate()) +"', '"+ DateUtil.dateToString(saleObj.getEndComboDate()) +"', ";
            saleStr += "'"+ DateUtil.dateToString(saleObj.getAddDate()) +"', '"+ DateUtil.dateToString(saleObj.getAlterDate()) +"'); ";
            saleStr += " \r\n ";
        }
        count += saleList.size();
        
        /* 淘宝skuInfo */
        String skuInfoHql = "from TaobaoSkuInfo";
        @SuppressWarnings("unchecked")
        List<TaobaoSkuInfo> skuInfoList = commonDao.queryByHql(skuInfoHql);
        String skuInfoStr = "";
        for(TaobaoSkuInfo skuObj : skuInfoList){

            skuInfoStr += "INSERT INTO `taobao_sku_info` (iidd,taobao_travel_item,number_code,package_name,";
            skuInfoStr += "flag,is_lock,is_sync,add_date,alter_date) VALUES (";
            skuInfoStr += "'"+ skuObj.getIidd() +"', '"+ skuObj.getTaobaoTravelItem() +"', ";
            skuInfoStr += "'"+ skuObj.getNumberCode() +"', '"+ skuObj.getPackageName() +"', ";
            skuInfoStr += "'"+ skuObj.getFlag() +"', '"+ skuObj.getIsLock() +"', '"+ skuObj.getIsSync() +"', ";
            skuInfoStr += "'"+ DateUtil.dateToString(skuObj.getAddDate()) +"', '"+ DateUtil.dateToString(skuObj.getAlterDate()) +"'); ";
            skuInfoStr += " \r\n ";
        }
        count += skuInfoList.size();
        
        /* 淘宝sku_tkview_type */
        String skuTkviewHql = "from Sku_TkviewType";
        @SuppressWarnings("unchecked")
        List<Sku_TkviewType> skuTkviewList = commonDao.queryByHql(skuTkviewHql);
        String skuTkviewStr = "";
        for(Sku_TkviewType sTypeObj : skuTkviewList){
            skuTkviewStr += "INSERT INTO `sku_tkview_type` (iidd,number_code,sku,tkview_type) VALUES (";
            skuTkviewStr += "'"+ sTypeObj.getIidd() +"', '"+ sTypeObj.getNumberCode()+"', ";
            skuTkviewStr += "'"+ sTypeObj.getSku() +"', '"+ sTypeObj.getTkviewType() +"');";
            skuTkviewStr += " \r\n ";
        }
        count += skuTkviewList.size();
        
        /* 淘宝skuInfoDetail */
        String detailHql = "from TaobaoSkuInfoDetail";
        @SuppressWarnings("unchecked")
        List<TaobaoSkuInfoDetail> detailList = commonDao.queryByHql(detailHql);
        String detailStr = "";
        for(TaobaoSkuInfoDetail detailObj : detailList){
            String outId = StringTool.isEmpty(detailObj.getOutId())==true?null:("'"+detailObj.getOutId()+"'");
            String outstock = StringTool.isEmpty(detailObj.getOutStock())==true?null:("'"+detailObj.getOutStock()+"'");
            String airway = StringTool.isEmpty(detailObj.getAirway())==true?null:("'"+detailObj.getAirway()+"'");
            
            detailStr += "INSERT INTO `taobao_sku_info_detail` (iidd,taobao_sku_info,number_code,date,stock,price_type,price,";
            detailStr += "out_id,out_stock,airway,flag,is_edit,is_lock,is_normal,is_relation,is_sync,is_update,";
            detailStr += "add_date,alter_date) VALUES (";
            detailStr += "'"+ detailObj.getIidd() +"', '"+ detailObj.getTaobaoSkuInfo() +"', ";
            detailStr += "'"+ detailObj.getNumberCode() +"', '"+ DateUtil.dateToString(detailObj.getDate()) +"', ";
            detailStr += "'"+ detailObj.getStock() +"', '"+ detailObj.getPriceType() +"', '"+ detailObj.getPrice() +"', ";
            detailStr += outId +", "+ outstock +", "+ airway +", '"+ detailObj.getFlag() +"', ";
            detailStr += "'"+ detailObj.getIsEdit() +"', '"+ detailObj.getIsLock() +"', '"+ detailObj.getIsNormal() +"', ";
            detailStr += "'"+ detailObj.getIsRelation() +"', '"+ detailObj.getIsSync() +"', '"+ detailObj.getIsUpdate() +"', ";
            detailStr += "'"+ DateUtil.dateToString(detailObj.getAddDate()) +"', ";
            detailStr += "'"+ DateUtil.dateToString(detailObj.getAlterDate()) +"'); ";
            detailStr += " \r\n ";
        }
        count += detailList.size();
        
        /* 产品 */
        String ptviewHql = "from Ptview";
        @SuppressWarnings("unchecked")
        List<Ptview> ptviewList = commonDao.queryByHql(ptviewHql);
        String ptviewStr = "";
        for(Ptview ptviewObj : ptviewList){
            ptviewStr += "INSERT INTO `ptview` (iidd,number_code,name_cn,company,cruise,airway,trip_days,trip_nights,";
            ptviewStr += "from_place,rule_type,ptview_desc,remark,add_date,alter_date,to_place,ship_down,";
            ptviewStr += "ship_up,province,city,related_tips,type,shipment,ship_mark,travel_date,sales) VALUES (";
            ptviewStr += "'"+ ptviewObj.getIidd() +"', '"+ ptviewObj.getNumberCode() +"', '"+ ptviewObj.getNameCn() +"', ";
            ptviewStr += "'"+ ptviewObj.getCompany() +"', '"+ ptviewObj.getCruise() +"', '"+ ptviewObj.getAirway() +"', ";
            ptviewStr += "'"+ DateUtil.dateToString(ptviewObj.getAddDate()) +"', '"+ DateUtil.dateToString(ptviewObj.getAlterDate()) +"', ";
            ptviewStr += " \r\n ";
        }
        count += ptviewList.size();
        
        /**/
        String ptviewSkuHql = "from PtviewSku";
        @SuppressWarnings("unchecked")
        List<PtviewSku> ptciewSkuList = commonDao.queryByHql(ptviewSkuHql);
        String ptviewSkuStr = "";
        for(PtviewSku skuObj : ptciewSkuList){
            String type = StringTool.isEmpty(skuObj.getTkviewType())==true?null:("'"+ skuObj.getTkviewType() +"'");
            
            ptviewSkuStr += "INSERT INTO `ptview_sku` (iidd,ptview,number_code,name_cn,tkview_type,add_date,alter_date) VALUES (";
            ptviewSkuStr += "'"+ skuObj.getIidd() +"', '"+ skuObj.getPtview() +"', ";
            ptviewSkuStr += "'"+ skuObj.getNumberCode() +"', '"+ skuObj.getNameCn() +"', "+ type +", ";
            ptviewSkuStr += "'"+ DateUtil.dateToString(skuObj.getAddDate()) +"', '"+ DateUtil.dateToString(skuObj.getAlterDate()) +"');";
            ptviewSkuStr += " \r\n ";
        }
        count += ptciewSkuList.size();
        
        /**/
        String ptviewSkuTypeHql = "from PtviewSku_TkviewType";
        @SuppressWarnings("unchecked")
        List<PtviewSku_TkviewType> ptviewTypeList = commonDao.queryByHql(ptviewSkuTypeHql);
        String ptviewSkuTypeStr = "";
        for(PtviewSku_TkviewType typeObj : ptviewTypeList){
            ptviewSkuTypeStr += "INSERT INTO `ptview_sku_tkview_type` (iidd,number_code,ptview_sku,";
            ptviewSkuTypeStr += "tkview_type,add_date,alter_date) VALUES (";
            ptviewSkuTypeStr += "'"+ typeObj.getIidd() +"', '"+ typeObj.getNumberCode() +"', ";
            ptviewSkuTypeStr += "'"+ typeObj.getPtviewSku() +"', '"+ typeObj.getTkviewType() +"', ";
            ptviewSkuTypeStr += "'"+ DateUtil.dateToString(typeObj.getAddDate()) +"', ";
            ptviewSkuTypeStr += "'"+ DateUtil.dateToString(typeObj.getAlterDate()) +"');";
            ptviewSkuTypeStr += " \r\n ";
        }
        count += ptviewTypeList.size();
        
        
        /**/
        String ptviewStockHql = "from PtviewSkuStock";
        @SuppressWarnings("unchecked")
        List<PtviewSkuStock> skuStockList = commonDao.queryByHql(ptviewStockHql);
        String ptviewStockStr = "";
        for(PtviewSkuStock skuStockObj : skuStockList){
            ptviewStockStr += "INSERT INTO `ptview_sku_stock` (ptview_sku,number_code,date,stock,";
            ptviewStockStr += "out_tkview,out_stock,b_price,c_price,a_b_Price,a_c_price,s_b_Price,s_c_Price,m_b_Price,";
            ptviewStockStr += "m_c_price,iidd,add_date,alter_date) VALUES (";
            ptviewStockStr += "'"+ skuStockObj.getPtviewSku() +"', '"+ skuStockObj.getNumberCode() +"', ";
            ptviewStockStr += "'"+ DateUtil.dateToString(skuStockObj.getDate()) +"', '"+ skuStockObj.getStock() +"', ";
            ptviewStockStr += "'"+ skuStockObj.getOutTkview() +"', ";
            ptviewStockStr += "'"+ skuStockObj.getOutStock() +"', ";
            ptviewStockStr += "'"+ skuStockObj.getbPrice() +"', '"+ skuStockObj.getcPrice() +"', ";
            ptviewStockStr += "'"+ skuStockObj.getaBPrice() +"', '"+ skuStockObj.getaCPrice() +"', ";
            ptviewStockStr += "'"+ skuStockObj.getsBPrice() +"', '"+ skuStockObj.getsCPrice() +"', ";
            ptviewStockStr += "'"+ skuStockObj.getmBPrice() +"', "+ skuStockObj.getmCPrice() +", ";
            ptviewStockStr += "'"+ skuStockObj.getIidd() +"', ";
            ptviewStockStr += "'"+ DateUtil.dateToString(skuStockObj.getAddDate()) +"', '"+ DateUtil.dateToString(skuStockObj.getAlterDate()) +"');";
            ptviewStockStr += " \r\n ";
        }
        count += skuStockList.size();
        
        /* 机构 */
        String possessorHql = "from Possessor";
        @SuppressWarnings("unchecked")
        List<Possessor> possessorList = commonDao.queryByHql(possessorHql);
        String possessorStr = "";
        for(Possessor possessorObj : possessorList){
            String remark = StringTool.isEmpty(possessorObj.getRemark())==true?null:("'"+ possessorObj.getRemark() +"'");

            possessorStr += "INSERT INTO `possessor` (iidd,number_code,name_cn,remark,add_date,alter_date) VALUES (";
            possessorStr += "'"+ possessorObj.getIidd() +"', '"+ possessorObj.getNumberCode() +"', ";
            possessorStr += "'"+ possessorObj.getNameCn() +"', "+ remark +", ";
            possessorStr += "'"+ DateUtil.dateToString(possessorObj.getAddDate()) +"', '"+ DateUtil.dateToString(possessorObj.getAlterDate()) +"');";
            possessorStr += "";
            possessorStr += " \r\n ";
        }
        count += possessorList.size();
        
        /* 机构关联表 */
        String relateHql = "from Possessor_Relate";
        @SuppressWarnings("unchecked")
        List<Possessor_Relate> relateList = commonDao.queryByHql(relateHql);
        String relateStr = "";
        for(Possessor_Relate relateObj : relateList){
            relateStr += "INSERT INTO `possessor_relate` (iidd,possessor,relate,type) VALUES (";
            relateStr += "'"+ relateObj.getIidd() +"', '"+ relateObj.getPossessor() +"', ";
            relateStr += "'"+ relateObj.getRelate() +"', '"+ relateObj.getType() +"');";
            relateStr += " \r\n ";
        }
        count += relateList.size();
        
        try{ 
            Date date = new Date();
            String fileUrl = DateUtil.dateToLongString(date) +".txt";
            HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
            String file_real_path=request.getSession().getServletContext().getRealPath("backup");
            String path = file_real_path+"\\"+fileUrl;
            System.out.println(path);
            File writeFile = new File(path);  
            //判断文件是否存在,如果存在就删除
            if(writeFile.isFile()){
                writeFile.delete();
            }
            writeFile.createNewFile(); // 创建新文件  
            BufferedWriter out = new BufferedWriter(new FileWriter(writeFile));  
            out.write("/*\t Date："+ DateUtil.dateToString(date)+" \r\n */ \r\n"); 
            /* 邮轮档案 */
            out.write(companyStr+" \r\n ");   //邮轮公司
            out.write(cruiseStr+" \r\n ");    //邮轮
            out.write(cabinStr+" \r\n ");     //舱型
            out.write(tkviewTypeStr+" \r\n ");//单品种类
            out.write(areaStr+" \r\n ");      //航区
            out.write(portStr+" \r\n ");      //港口
            out.write(airwayStr+" \r\n ");    //航线
            out.write(tripStr+" \r\n ");      //航线--行程模版
            out.write(shipmentStr+" \r\n ");  //航期
            out.write(voyageStr+" \r\n ");    //航程
            
            /* 单品管理 */
            out.write(providerStr+" \r\n ");     //供应商
            out.write(priceStr+" \r\n ");        //加价区间
            out.write(tkviewStr+" \r\n ");       //单品
            out.write(stockStr+" \r\n ");        //单品库存
            out.write(channelStr+" \r\n ");      //渠道
            
            /* 淘宝宝贝 */
            out.write(travelItemStr+" \r\n ");     //淘宝travelItem
            out.write(baseInfoStr+" \r\n ");       //淘宝baseInfo
            out.write(cruiseItemExtStr+" \r\n ");  //淘宝cruiseItemExt
            out.write(ruleStr+" \r\n ");           //淘宝bookingRule
            out.write(saleStr+" \r\n ");           //淘宝saleInfo
            out.write(skuInfoStr+" \r\n ");        //淘宝skuInfo
            out.write(skuTkviewStr+" \r\n ");      //淘宝sku_tkview_type
            out.write(detailStr+" \r\n ");         //淘宝skuInfoDetail
            
            /* 产品 */
            out.write(ptviewStr+" \r\n ");           //产品
            out.write(ptviewSkuStr+" \r\n ");        //产品SKu
            out.write(ptviewSkuTypeStr+" \r\n ");    //产品SKu关联表
            out.write(ptviewStockStr+" \r\n ");      //产品SKu库存
            
            out.write(possessorStr+" \r\n ");    //机构
            out.write(relateStr+" \r\n ");       //机构关联表
            
            out.flush(); // 把缓存区内容压入文件  
            out.close(); // 最后记得关闭文件  
            
            long fileS = writeFile.length();
            DecimalFormat df = new DecimalFormat("#.00"); 
            String size = df.format((double) fileS / 1024);
            
            Backup backup = new Backup();
            backup.setNumberCode(UUIDUtil.getUUIDStringFor32());
            backup.setFileUrl(fileUrl);
            backup.setCount(count);
            backup.setFileSize(Double.parseDouble(size));    //备份文件大小(KB为单位)
            backup.setAddDate(date);
            commonDao.save(backup);
            
            return 1;
        }catch(Exception e){  
            e.printStackTrace();  
            System.out.println("Error!");
        }  
        
        return 0;
    }
    
}