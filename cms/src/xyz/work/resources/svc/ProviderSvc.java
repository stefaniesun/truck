package xyz.work.resources.svc;


import java.util.Map;

import org.springframework.stereotype.Service;


/**
 * 供货方
 */
@Service
public interface ProviderSvc {

    public Map<String, Object> queryProviderList(int offset , int pagesize , String nameCn ,
                                                 String mark , String queryJson);

    public Map<String, Object> addProvider(String mark , String nameCn , String address ,
                                           String weChat , String talkGroup , int isResponsible ,
                                           String sign , String settlement , String policy ,
                                           int status , String remark , int grade);

    public Map<String, Object> editProvider(String numberCode , String mark , String nameCn ,
                                            String address , String weChat , String talkGroup ,
                                            int isResponsible , String sign , String settlement ,
                                            String policy , int status , String remark , int grade);

    public Map<String, Object> deleteProviders(String providers);

    public Map<String, Object> queryPossessorProviderList(Boolean isTrue , String possessor ,
                                                          String numberCode , String name);

    public Map<String, Object> getProviderMarkList(String mark , String numberCode);

    /**
     * 导入Excel
     * 
     * @param excelPath
     * @author : 熊玲
     */
    public Map<String, Object> importExcelOper(String excelPath);

    /**
     * 导出Excel模版
     * 
     * @author : 熊玲
     */
    public Map<String, Object> exportExcelOper();

    /**
     * 编辑供应商账户
     * 
     * @param numberCode
     * @param accountJson
     * @author : 熊玲
     */
    public Map<String, Object> editAccountList(String provider , String accountJson);

    /**
     * 编辑对接人信息
     * 
     * @param provider
     * @param joinerJson
     * @author : 熊玲
     */
    public Map<String, Object> editJoinerList(String provider , String joinerJson);

}