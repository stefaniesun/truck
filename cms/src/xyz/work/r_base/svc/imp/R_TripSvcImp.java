package xyz.work.r_base.svc.imp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.dao.CommonDao;
import xyz.filter.ReturnUtil;
import xyz.util.StringTool;
import xyz.util.UUIDUtil;
import xyz.work.r_base.model.R_Port;
import xyz.work.r_base.model.R_Trip;
import xyz.work.r_base.model.R_Voyage;
import xyz.work.r_base.svc.R_TripSvc;

@Service
public class R_TripSvcImp implements R_TripSvc {
    @Autowired
    private CommonDao commonDao;

    @Override
    public Map<String, Object> importTxtOper() {
        
        List<String> lineTxtList = new ArrayList<String>();
        
        String resultStr = "";
        Date eff_date = new Date();
        System.out.print("读取数据开始时间:");
        System.out.println(eff_date);
        try {
            String encoding = "UTF-8"; 
            //CNY_ITINERARY.txt  5000条数据
            //ITINERARY.txt      55774条数据
            String filePath = "C:\\ITINERARY.txt";
            File file = new File(filePath);
            if(file.isFile() && file.exists()){
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding); 
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    lineTxt = lineTxt.trim();
                    if(StringTool.isNotNull(lineTxt) && lineTxt.length() > 30){
                        lineTxtList.add(lineTxt);
                    }
                }
                read.close();
            }else{
                resultStr = "找不到指定的文件!";
                System.out.println("找不到指定的文件!");
                return ReturnUtil.returnMap(0, resultStr);
            }
        }catch (Exception e) {
            resultStr = "读取文件内容出错!";
            System.out.println("读取文件内容出错!");
            e.printStackTrace();
            return ReturnUtil.returnMap(0, resultStr);
        }
        Date end = new Date();
        System.out.print("读取数据结束时间:");
        System.out.println(end);
        
        String deleteSql = "DELETE FROM r_voyage ";
        commonDao.getSqlQuery(deleteSql).executeUpdate();
         
        String rPortHql = "from R_Port ";
        @SuppressWarnings("unchecked")
        List<R_Port> rPortList = commonDao.queryByHql(rPortHql);
        
        String shipNum = "";      //航次Code
        String airwayNameCn = ""; //航线名称
        int priority = 1;         //顺序号
        //int count = 0;            //几天
        for(String line : lineTxtList){
            String[] arry = line.split("\\|");
            String packageID = (String)arry[0];        //0：航期代码
            String actDate = (String)arry[2];          //2：时间
            //String subRegionCode = (String)arry[4];    //4：航线二级区域
            //String regionCode = (String)arry[5];       //5：航线区域
            String itinCode = (String)arry[7];         //7：航次Code
            String locationCode = (String)arry[11];    //11：停靠港口
            String locationName = (String)arry[12];    //12：港口名称
            String activity = (String)arry[13];        //13：描述（启航，巡航，靠港，结束）
            String arrivalTime = (String)arry[14];     //14：到达时间
            String departTime = (String)arry[15];      //15：出发时间
            //String numOfDays = (String)arry[17];       //17：航次持续时间
            //BigDecimal day = new BigDecimal(numOfDays);
            
            if(StringTool.isEmpty(shipNum) || !shipNum.equals(itinCode)){
                shipNum = itinCode;
                //count = Integer.valueOf(numOfDays)+1;
                airwayNameCn = locationName;
                priority = 1;
            }else{
                if(!activity.equals("CRUISING")){//巡航
                    airwayNameCn = airwayNameCn +"-"+ locationName;
                }
                priority++;
            }
            
            R_Port portObj = null;
            for(R_Port port : rPortList){
                if(locationCode.equals(port.getMark())){
                    portObj = port;
                    if(StringTool.isEmpty(portObj.getNameCn())){
                        portObj.setNameCn(locationName);
                        commonDao.update(portObj);
                    }
                    break; 
                }
            }
            
            R_Voyage voyage = new R_Voyage();
            voyage.setNumberCode(UUIDUtil.getUUIDStringFor32());
            voyage.setPriority(priority);
            voyage.setShipment(itinCode);
            voyage.setShipmentMark(packageID);
            if(portObj != null){
                voyage.setPort(portObj.getNumberCode());
            }
            voyage.setPortNameCn(locationName);
            voyage.setPortMark(locationCode);
            //voyage.setTime("第"+ priority +"天");
            voyage.setTime(actDate);
            voyage.setTimeType(activity);
            arrivalTime = StringTool.isEmpty(arrivalTime)==true?"——":arrivalTime;
            departTime = StringTool.isEmpty(departTime)==true?"——":departTime;
            voyage.setArrivalTime(arrivalTime);
            voyage.setLeaveTime(departTime);
            commonDao.save(voyage);    
        }
           
        resultStr = "共有"+ lineTxtList.size() +"条数据";
        Date date = new Date();
        System.out.print("存储数据结束时间:");
        System.out.println(date);
        
        return ReturnUtil.returnMap(1, resultStr);
    }

    @Override
    public Map<String, Object> queryRTripList(String airway) {
        String tripHql = "from R_Trip t where 1=1 ";
        tripHql += "and t.airway = '"+ airway +"' ";
        
        @SuppressWarnings("unchecked")
        List<R_Trip> list = commonDao.queryByHql(tripHql);

        return ReturnUtil.returnMap(1, list);
    }

}