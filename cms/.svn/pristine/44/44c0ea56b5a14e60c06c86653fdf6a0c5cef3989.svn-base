package xyz.work.r_base.svc.imp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.ReturnUtil;
import xyz.work.base.model.Cabin;
import xyz.work.base.model.Cruise;
import xyz.work.r_base.model.R_Stock;
import xyz.work.r_base.model.R_Tkview;
import xyz.work.r_base.svc.RTkview_ViewSvc;

@Service
public class RTkview_ViewSvcImp implements RTkview_ViewSvc {
    
    @Autowired
    private CommonDao commonDao;

    @Override
    public Map<String, Object> queryRoyalCruiseList() {
        //TODO 邮轮公司是写死的，需要改
        String hql = "from Cruise c where c.company = '54023710b14b4e51adcd1592e0ef839a' ";
        @SuppressWarnings("unchecked")
        List<Cruise> crusieList = commonDao.queryByHql(hql);

        return ReturnUtil.returnMap(1, crusieList);
    }

    @Override
    public Map<String, Object> getRoyalCabinList(String cruise) {
        String hql = "from Cabin c where c.cruise = '"+ cruise +"' ";
        hql += "order by c.type ";
        @SuppressWarnings("unchecked")
        List<Cabin> cabinList = commonDao.queryByHql(hql);

        return ReturnUtil.returnMap(1, cabinList);
    }

    @Override
    public Map<String, Object> getRoyalTkviewDateList(String cruise , String cabin) {
        //月份
        String mothSql = "SELECT DATE_FORMAT(r.shipment_travel_date,'%Y-%m') FROM r_tkview r ";
        mothSql += "WHERE r.shipment_travel_date > NOW() ";
        mothSql += "AND r.cruise = '"+ cruise +"' ";
        mothSql += "AND r.cabin = '"+ cabin +"' ";
        mothSql += "GROUP BY DATE_FORMAT(r.shipment_travel_date,'%Y-%m') ";
        mothSql += "ORDER BY DATE_FORMAT(r.shipment_travel_date,'%Y-%m') ";
        @SuppressWarnings("unchecked")
        List<Object> mothList = commonDao.getSqlQuery(mothSql).list();
        
        //单品
        String hql = "from R_Tkview r where r.cruise = '"+ cruise +"' ";
        hql += "and r.shipmentTravelDate > NOW() ";
        hql += "and r.cabin = '"+ cabin +"' ";
        @SuppressWarnings("unchecked")
        List<R_Tkview> tkviewList = commonDao.queryByHql(hql);
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("tkviewList", tkviewList);
        map.put("mothList", mothList);

        return ReturnUtil.returnMap(1, map);
    }

    @Override
    public Map<String, Object> getRoyalTkviewPriceList(String cruise , String tkview , String date) {
        String hql = "from R_Tkview r where r.cruise = '"+ cruise +"' ";
        hql += "and r.shipmentTravelDate = '"+ date +"' ";
        hql += "and r.numberCode = '"+ tkview +"' ";
        @SuppressWarnings("unchecked")
        List<R_Tkview> tkviewList = commonDao.queryByHql(hql);
        
        /*邮轮档案*/
        String cruiseHql = "from Cruise";
        @SuppressWarnings("unchecked")
        List<Cruise> cruiseList = commonDao.queryByHql(cruiseHql);
        
        /*舱型档案*/
        String cabinHql = "from Cabin ";
        @SuppressWarnings("unchecked")
        List<Cabin> cabinList = commonDao.queryByHql(cabinHql);
        
        for(R_Tkview tk : tkviewList){
            for(Cruise cruiseObj : cruiseList){
                if(tk.getCruise().equals(cruiseObj.getNumberCode()) && tk.getCruiseMark().equals(cruiseObj.getMark()) ){
                    tk.setCruiseNameCn(cruiseObj.getNameCn());
                    break;
                }
            }
            for(Cabin cabinObj : cabinList){
                if(tk.getCabin().equals(cabinObj.getNumberCode()) && tk.getCabinMark().equals(cabinObj.getMark())){
                    tk.setCabinNameCn(cabinObj.getNameCn());
                    tk.setVolume(cabinObj.getVolume());
                    break;
                }
            }
        }
        
        String stockHql = "from R_Stock where tkview='" + tkview + "'";
        @SuppressWarnings("unchecked")
        List<R_Stock> stockList = commonDao.queryByHql(stockHql);
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("tkviewList", tkviewList);
        map.put("stockList", stockList);
        
        return ReturnUtil.returnMap(1, map);
    }

}