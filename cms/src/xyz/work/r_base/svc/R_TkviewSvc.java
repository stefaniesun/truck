package xyz.work.r_base.svc;


import java.util.Map;

import org.springframework.stereotype.Service;


@Service
public interface R_TkviewSvc {

    public Map<String, Object> readTxtOper();

    public Map<String, Object> queryRTkviewList(int offset , int pageSize , String cruiseMark ,
                                                String cabinMark , String nameCn , String mark ,
                                                String mouth , String shipment);

}