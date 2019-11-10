package xyz.work.base.svc;


import java.util.Map;

import org.springframework.stereotype.Service;


@Service
public interface PortSvc {

    public Map<String, Object> queryPortList(int offset , int pageSize , String nameCn);

    public Map<String, Object> addPort(String nameCn , String country , String address ,
                                       String details , String remark , String images);

    public Map<String, Object> editPort(String numberCode , String nameCn , String country ,
                                        String address , String details , String remark , String images);

    public Map<String, Object> deletePort(String numberCodes);

    /**
     * 设置港口坐标
     * 
     * @param numberCode 编号
     * @param longitude 经度
     * @param latitude 纬度
     * @author : 熊玲
     */
    public Map<String, Object> editPortCoordinate(String numberCode , String longitude ,
                                                  String latitude);

}