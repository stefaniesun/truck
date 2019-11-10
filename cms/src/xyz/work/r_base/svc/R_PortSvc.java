package xyz.work.r_base.svc;


import java.util.Map;

import org.springframework.stereotype.Service;


@Service
public interface R_PortSvc {

    public Map<String, Object> importExcelPortOper(String excelPath);

    public Map<String, Object> queryRPortList(int offset , int pageSize , String nameCn ,
                                              String mark , String location);

}