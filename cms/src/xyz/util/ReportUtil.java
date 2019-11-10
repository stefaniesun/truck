package xyz.util;

import java.io.File;
import java.util.Collection;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

public class ReportUtil {
    private static ReportUtil instance;
    
    public enum FileType{
        PDF,DOC
    }
    
    public static synchronized ReportUtil getInstance(){
        if(instance==null){
            instance = new ReportUtil();
        }
        return instance;
    }
    
    /**
     * 
     * Description  ：得到报表
     *
     * @param req
     * @param res
     * @param type
     * @param modelName
     * @param parameters
     * @param dataColl
     * @return String
     * @exception   ：〈描述可能的异常〉
    
     * @author      ：Yisces(QQ)
     * @date        ：2016-9-5下午4:56:01
     */
    public String getReport(FileType type,String modelName,String pathFile,String pathModel,Map<String, Object> parameters, Collection<?> dataColl){
        JasperPrint jasperPrint = this.init(modelName,pathModel, parameters, dataColl);
        String reportUrl = null;
        switch(type){
            case PDF :
                reportUrl = this.getReportPdfServlet(jasperPrint,pathFile);
                break;
            case DOC :
                reportUrl = this.getReportDocServlet(jasperPrint,pathFile);
                break;
        }
        return reportUrl;
    }
    
    /**
     * 
     * Description  ：模版替换过程
     *
     * @param modelName
     * @param parameters
     * @param dataColl
     * @return JasperPrint
     * @exception   ：〈描述可能的异常〉
    
     * @author      ：Yisces(QQ)
     * @date        ：2016-9-2下午4:10:38
     */
    private JasperPrint init(String modelName,String pathModel, Map<String, Object> parameters, Collection<?> dataColl){
        
        File reportFile = new File(pathModel + "\\"+modelName+".jasper");
        
        JasperPrint jasperPrint = null;
        try {
            JasperReport jasperReport = (JasperReport)JRLoader.loadObject(reportFile);
            
            JRBeanCollectionDataSource jrData = new JRBeanCollectionDataSource(dataColl);
            
            jasperPrint = JasperFillManager.fillReport(jasperReport,parameters, jrData);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return jasperPrint;
    }
    
    /**
     * 
     * Description  ：生成word Servlet请求方式
     *
     * @param req
     * @param res
     * @param jasperPrint
     * @return String
     * @exception   ：〈描述可能的异常〉
    
     * @author      ：Yisces(QQ)
     * @date        ：2016-9-5下午3:13:16
     */
    private String getReportDocServlet(JasperPrint jasperPrint,String path){
        
        String fileName = UUIDUtil.getUUIDStringFor32() + ".doc";
        
        JRDocxExporter exporter = new JRDocxExporter();
        //设置输入项
        ExporterInput exporterInput = new SimpleExporterInput(jasperPrint);
        exporter.setExporterInput(exporterInput);
        
        //设置输出项
        OutputStreamExporterOutput exporterOutput;
        
        try {
            /* 通过流形式返回前台，请打开一下代码 */
            /*
            //请求头部
            res.setContentType("application/msword");
            res.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
            res.setHeader("Pragma", "No-cache");  
            res.setHeader("Cache-Control", "No-cache");  
            res.setDateHeader("Expires", 0);
            exporterOutput = new SimpleOutputStreamExporterOutput(res.getOutputStream());*/
            exporterOutput = new SimpleOutputStreamExporterOutput(path + "\\" + fileName);
            exporter.setExporterOutput(exporterOutput);
            exporter.exportReport();
        }
        catch (JRException e) {
            e.printStackTrace();
        }
        return (fileName);
    }
    
    /**
     * 
     * Description  ：页面直接下载pdf文档
     *
     * @param req
     * @param res
     * @param jasperPrint void
     * @exception   ：〈描述可能的异常〉
    
     * @author      ：Yisces(QQ)
     * @date        ：2016-9-5下午4:54:11
     */
    private String getReportPdfServlet(JasperPrint jasperPrint,String path){
        
        String fileName = UUIDUtil.getUUIDStringFor32() + ".pdf";
        
        try {
            JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\" + fileName);
        }
        catch (JRException e1) {
            e1.printStackTrace();
        }
        
        /* 通过流形式返回前台，请打开一下代码 */
        /*
        JRPdfExporter exporter = new JRPdfExporter();
        //设置输入项
        ExporterInput exporterInput = new SimpleExporterInput(jasperPrint);
        exporter.setExporterInput(exporterInput);
        
        //设置输出项
        OutputStreamExporterOutput exporterOutput;
        
        try {
            res.setContentType("application/pdf;charset=utf-8");
            res.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
            res.setHeader("Pragma", "No-cache");  
            res.setHeader("Cache-Control", "No-cache");  
            res.setDateHeader("Expires", 0);
            exporterOutput = new SimpleOutputStreamExporterOutput(res.getOutputStream());
            exporter.setExporterOutput(exporterOutput);
            exporter.exportReport();
        }
        catch (JRException e) {
            e.printStackTrace();
        }*/
        
        return (fileName);
    }
    
}
