package xyz.work.excel.svc;


import java.util.Map;

import org.springframework.stereotype.Service;


@Service
public interface ExcelSvc {

    public Map<String, Object> importExcelDataOper(String excelPath);

    public Map<String, Object> exportExcelDataOper(String provider , String cruise ,
                                                   String shipment , String cabin);

	public Map<String, Object> queryExcelLogList(int offset, int pageSize);
	
	public Map<String, Object> downTemplate(String cruise ,String shipment , String cabin);
	
	public Map<String, Object> downCompareData(String cruise,String shopName,String sourceCode);

	public Map<String, Object> importSkuExcelOper(String excelPath);

	public Map<String, Object> transferDataOper();

}