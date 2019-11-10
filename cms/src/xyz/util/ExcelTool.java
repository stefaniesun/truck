package xyz.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFConditionalFormattingRule;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFPatternFormatting;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFSheetConditionalFormatting;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import xyz.filter.JSON;

public class ExcelTool{
    public static boolean createExcelForList(List<List<Object>> dataList,Object[][] titleList,String fileName){
    	
        XSSFWorkbook wb = new XSSFWorkbook();
        //设置字体
        XSSFFont font =wb.createFont();
        font.setFontHeightInPoints((short) 10); //字体高度
        font.setColor(XSSFFont.COLOR_NORMAL); //字体颜色
        font.setFontName("微软雅黑"); //字体
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);//粗体显示    
        XSSFSheet sheet = wb.createSheet();

        XSSFRow row = sheet.createRow((int) 0);
        
        CellRangeAddress rang = CellRangeAddress.valueOf("A1:O1");
        sheet.setAutoFilter(rang);
        
        //用于表头样式
        XSSFCellStyle style = wb.createCellStyle();
        //左右居中
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        //上下对齐
        style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        //自动换行
        style.setWrapText(true);    
        style.setFont(font);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        
        //用于数据样式
        XSSFCellStyle style1 = wb.createCellStyle();
        //左右居中
        style1.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        //上下对齐
        style1.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        //自动换行
        style1.setWrapText(true);   
        style1.setFont(font);
        style1.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style1.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        style1.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        
        
        XSSFCellStyle style2 = wb.createCellStyle();
        style2.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        style2.setWrapText(true);   
        style2.setFont(font);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
		style2.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND); 
		style2.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
		
        XSSFCellStyle yellowStyle = wb.createCellStyle();
        yellowStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        yellowStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        yellowStyle.setWrapText(true);   
        yellowStyle.setFont(font);
        yellowStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        yellowStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        yellowStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        yellowStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        yellowStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND); 
        yellowStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        
        XSSFCellStyle redStyle = wb.createCellStyle();
        redStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        redStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        redStyle.setWrapText(true);   
        redStyle.setFont(font);
        redStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        redStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        redStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        redStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        redStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND); 
        redStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
        
        
        //设置EXCEL公式规则
        XSSFSheetConditionalFormatting scf = sheet.getSheetConditionalFormatting();  
        
        //设置Excel行标
        XSSFCell cell = null;
        
        for (int i = 0; i < titleList.length; i++){                 
            cell = row.createCell(i);
            cell.setCellValue(new XSSFRichTextString(JSON.toJson(titleList[i][0])));        
            //设置列宽
            int linWidth = 10;
            if(titleList[i].length > 1){
                linWidth = Integer.parseInt((JSON.toJson(titleList[i][1])));
            }
            if(i>=15){
            	   sheet.setColumnWidth(i,(short)(0));
            }else{
                sheet.setColumnWidth(i,(short)(linWidth*1.3*256));
            }
            cell.setCellStyle(style);    
        }
        XSSFCell cell1 = null;

        for (int i = 0; i < dataList.size(); i++){
            //动态创建行
        	boolean colorFlag=false;
        	String color="";
            row = sheet.createRow((int) i + 1);
            List<Object> tt = dataList.get(i);
            //用于比较列高大小
            int rowCount=1;
            for(int z=0;z<tt.size();z++){
                //这里来创建每个单元格 
                cell1 = row.createCell(z);
                
                String out=JSON.toJson( tt.get(z));
                if(out!=null){
                    int countT = out.split("\r\n").length;
                    if(countT>rowCount){
                        rowCount = countT;
                    }
                }
                if(z==7){
                 	String[] array=out.split("&");
                	out=array[0];
                	if(array.length>1){
                		color= array[1];
                		colorFlag=true;
                	}
                }
                if(z==7||z==15){
                	cell1.setCellType(Cell.CELL_TYPE_NUMERIC);
                	cell1.setCellValue(new BigDecimal(out).floatValue());
                }else if((z==9||z==10||z==16)&&NumberUtil.isNum(out)){
                	cell1.setCellType(Cell.CELL_TYPE_NUMERIC);
                	cell1.setCellValue(new BigDecimal(out).floatValue());
                }else if(z==11){
                	if(NumberUtil.isNum(out)){
                		cell1.setCellType(Cell.CELL_TYPE_NUMERIC);
                    	cell1.setCellValue(new BigDecimal(out).floatValue());
                	}else{
                		cell1.setCellType(Cell.CELL_TYPE_NUMERIC);
                    	cell1.setCellValue(new BigDecimal(0).floatValue());
                	}
                }else  if(z==12){
                   	if(!("现询".equals(tt.get(9).toString())||"售罄".equals(tt.get(9).toString()))){
                   	  cell1.setCellType(XSSFCell.CELL_TYPE_FORMULA);
                   	  cell1.setCellFormula("J"+(i+2)+"-L"+(i+2));
                   	}
                }else if(z==17){
                	cell1.setCellType(XSSFCell.CELL_TYPE_FORMULA);
                    cell1.setCellFormula("IF(OR(H"+(i+2)+"<>P"+(i+2)+",J"+(i+2)+"<>Q"+(i+2)+"),\"update\",\"\")");
              	  	cell1.setCellStyle(style1); 
                }else{
                	  cell1.setCellValue(new XSSFRichTextString(out));
                }
              
                if(colorFlag&&((z==7||z==8||z==15))){
                	if("#FFFFFF00".equals(color)){
                  	   	cell1.setCellStyle(yellowStyle); 
                	}else if("#FFFF0000".equals(color)){
                	  	cell1.setCellStyle(redStyle); 
                	}else{
                	  	cell1.setCellStyle(style2); 
                	}
        
                }else{
                	  cell1.setCellStyle(style1); 
                }
            }
            //设置每个行的高
            row.setHeight((short)(rowCount*300));
            
            int index=i+2;
            //添加修改变色规则
            XSSFConditionalFormattingRule cf_R_rule = scf.createConditionalFormattingRule("AND(H"+index+">P"+index+",LEN(E"+index+")<>0)"); 
            XSSFPatternFormatting cf_R = cf_R_rule.createPatternFormatting();  

            cf_R.setFillPattern(CellStyle.SOLID_FOREGROUND);
            cf_R.setFillBackgroundColor(IndexedColors.RED.index);  
            cf_R.setFillForegroundColor(IndexedColors.RED.index);  

            XSSFConditionalFormattingRule cf_R_rule2 = scf.createConditionalFormattingRule("AND(H"+index+"<P"+index+",LEN(E"+index+")<>0)");  
            XSSFPatternFormatting cf_R2 = cf_R_rule2.createPatternFormatting();  

            cf_R2.setFillPattern(CellStyle.SOLID_FOREGROUND);
            cf_R2.setFillBackgroundColor(IndexedColors.YELLOW.index);  
            cf_R2.setFillForegroundColor(IndexedColors.YELLOW.index);  
            
            XSSFConditionalFormattingRule cf_R_rule5 = scf.createConditionalFormattingRule("LEN(E"+index+")=0");  
            XSSFPatternFormatting cf_R5 = cf_R_rule5.createPatternFormatting();  
            cf_R5.setFillPattern(CellStyle.SOLID_FOREGROUND);
            cf_R5.setFillBackgroundColor(IndexedColors.BRIGHT_GREEN.index);  
            cf_R5.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.index);  
            
            XSSFConditionalFormattingRule cf_R_rule3 = scf.createConditionalFormattingRule("AND(J"+index+"<>Q"+index+",LEN(E"+index+")<>0)");  
            XSSFPatternFormatting cf_R3 = cf_R_rule3.createPatternFormatting();  
            cf_R3.setFillPattern(CellStyle.SOLID_FOREGROUND);
            cf_R3.setFillBackgroundColor(IndexedColors.RED.index);
            cf_R3.setFillForegroundColor(IndexedColors.RED.index);  
            
            XSSFConditionalFormattingRule cf_R_rule4 = scf.createConditionalFormattingRule("AND(J"+index+"=\"售罄\",LEN(E"+index+")<>0)");  
            XSSFPatternFormatting cf_R4 = cf_R_rule4.createPatternFormatting();  
            cf_R4.setFillPattern(CellStyle.SOLID_FOREGROUND);
            cf_R4.setFillBackgroundColor(IndexedColors.BLACK.index);
            cf_R4.setFillForegroundColor(IndexedColors.BLACK.index);  
            
            XSSFConditionalFormattingRule[] cfRules = {cf_R_rule,cf_R_rule2,cf_R_rule5};  
            XSSFConditionalFormattingRule[] cfRules2 = {cf_R_rule4,cf_R_rule3};  
            
          //条件格式应用的单元格范围  
          CellRangeAddress[] regions = {CellRangeAddress.valueOf("F"+index)};  
          CellRangeAddress[] regions2 = {CellRangeAddress.valueOf("J"+index)};  
          
          scf.addConditionalFormatting(regions, cfRules);  
          scf.addConditionalFormatting(regions2, cfRules2);  
          
        }
        
        wb.getCreationHelper()
        .createFormulaEvaluator()
        .evaluateAll();
        
        try{    
            HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
            //获取项目绝对路径
            String file_real_path=request.getSession().getServletContext().getRealPath("tempFile");
            //File file = new File(file_real_path);
            //FileTool.deleteChildFolder(file);
            String path = file_real_path+"\\"+fileName;
            System.out.println(file_real_path);
            FileOutputStream fout = new FileOutputStream(path);//"D:\\test.xls"
            wb.write(fout);
            fout.close();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        
        return true;                
    }
    
    
public static boolean createCompareExcelForList(List<List<Object>> dataList,Object[][] titleList,String fileName){
    	
        XSSFWorkbook wb = new XSSFWorkbook();
        //设置字体
        XSSFFont font =wb.createFont();
        font.setFontHeightInPoints((short) 10); //字体高度
        font.setColor(XSSFFont.COLOR_NORMAL); //字体颜色
        font.setFontName("微软雅黑"); //字体
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);//粗体显示    
        XSSFSheet sheet = wb.createSheet();

        XSSFRow row = sheet.createRow((int) 0);
        
        CellRangeAddress rang = CellRangeAddress.valueOf("A1:O1");
        sheet.setAutoFilter(rang);
        
        //用于表头样式
        XSSFCellStyle style = wb.createCellStyle();
        //左右居中
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        //上下对齐
        style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        //自动换行
        style.setWrapText(true);    
        style.setFont(font);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        
        //用于数据样式
        XSSFCellStyle style1 = wb.createCellStyle();
        //左右居中
        style1.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        //上下对齐
        style1.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        //自动换行
        style1.setWrapText(true);   
        style1.setFont(font);
        style1.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style1.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        style1.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        
        
        XSSFCellStyle style2 = wb.createCellStyle();
        style2.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        style2.setWrapText(true);   
        style2.setFont(font);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
		style2.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND); 
		style2.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
        
        
        //设置Excel行标
        XSSFCell cell = null;
        
        for (int i = 0; i < titleList.length; i++){                 
            cell = row.createCell(i);
            cell.setCellValue(new XSSFRichTextString(JSON.toJson(titleList[i][0])));        
            //设置列宽
            int linWidth = 10;
            if(titleList[i].length > 1){
                linWidth = Integer.parseInt((JSON.toJson(titleList[i][1])));
            }
            if(i>=15){
            	   sheet.setColumnWidth(i,(short)(0));
            }else{
                sheet.setColumnWidth(i,(short)(linWidth*1.3*256));
            }
            cell.setCellStyle(style);    
        }
        XSSFCell cell1 = null;

        for (int i = 0; i < dataList.size(); i++){
            //动态创建行
        	/*boolean colorFlag=false;
        	String color="";*/
            row = sheet.createRow((int) i + 1);
            List<Object> tt = dataList.get(i);
            //用于比较列高大小
            int rowCount=1;
            for(int z=0;z<tt.size();z++){
                //这里来创建每个单元格 
                cell1 = row.createCell(z);
                
                String out=JSON.toJson( tt.get(z));
                if(out!=null){
                    int countT = out.split("\r\n").length;
                    if(countT>rowCount){
                        rowCount = countT;
                    }
                }
                cell1.setCellValue(new XSSFRichTextString(out));
            }
            //设置每个行的高
            row.setHeight((short)(rowCount*300));
        }
        
        wb.getCreationHelper()
        .createFormulaEvaluator()
        .evaluateAll();
        
        try{    
            HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
            //获取项目绝对路径
            String file_real_path=request.getSession().getServletContext().getRealPath("tempFile");
            //File file = new File(file_real_path);
            //FileTool.deleteChildFolder(file);
            String path = file_real_path+"\\"+fileName;
            System.out.println(file_real_path);
            FileOutputStream fout = new FileOutputStream(path);//"D:\\test.xls"
            wb.write(fout);
            fout.close();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        
        return true;                
    }
    
    /**
     * 调用此方法将产生合并单元格的现象，要求第一个单元格必须是合并关键字段
     */
    public static boolean createExcelForJoinForList(List<List<Object>> dataList,Object[][] titleList,short[] fieldCounts,String fileName){
        HSSFWorkbook wb = new HSSFWorkbook();
        //设置字体
        HSSFFont font =wb.createFont();
        font.setFontHeightInPoints((short) 20); //字体高度
        font.setColor(HSSFFont.COLOR_RED); //字体颜色
        font.setFontName("黑体"); //字体
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示    
        font.setItalic(true); //是否使用斜体                    
        HSSFSheet sheet = wb.createSheet();
        HSSFRow row = sheet.createRow((int) 0);
        //用于表头样式
        HSSFCellStyle style = wb.createCellStyle();
        //左右居中
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //上下对齐
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        //自动换行
        style.setWrapText(true);    
        //用于数据样式
        HSSFCellStyle style1 = wb.createCellStyle();
        //左右居中
        style1.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        //上下对齐
        style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        //自动换行
        style1.setWrapText(true);   
        //设置Excel行标
        HSSFCell cell = null;
        for (int i = 0; i < titleList.length; i++){                 
            cell = row.createCell(i);
            cell.setCellValue(new HSSFRichTextString(JSON.toJson(titleList[i][0])));        
            //设置列宽
            int linWidth = Integer.parseInt((JSON.toJson(titleList[i][1])));
            sheet.setColumnWidth(i,(short)(linWidth*1.3*256));
            cell.setCellStyle(style);                     
        }
        Object groupStr = dataList.get(0).get(0);
        int count = 0;
        HSSFCell cell1 = null;
        for (int i = 0; i < dataList.size(); i++){
            //动态创建行
            row = sheet.createRow((int) i + 1);
            List<Object> tt = dataList.get(i);
            {
                if(!tt.get(0).equals(groupStr)){
                    if(count>1){
                        for(short t=0;t<fieldCounts.length;t++){
                            short j = fieldCounts[t];
                            sheet.addMergedRegion(new CellRangeAddress(i+1-count,i,j,j));
                        }
                    }
                    count=1;
                }else{
                    count++;
                }
                groupStr = tt.get(0);
            }
            //用于比较列高大小
            int rowCount=1;
            for(int z=0;z<tt.size();z++){
                //这里来创建每个单元格                
                cell1 = row.createCell(z);
                String out=JSON.toJson( tt.get(z));
                if(out!=null){
                    boolean flag2 = false;
                    for(short ppp : fieldCounts){
                        if(ppp==z){
                            flag2 = true;
                        }
                    }
                    if(!(count>1 && flag2)){
                        int countT = out.split("\r\n").length;
                        if(countT>rowCount){
                            rowCount = countT;
                        }
                    }
                }
                cell1.setCellValue(new HSSFRichTextString(out));
                cell1.setCellStyle(style1); 
            }
            //设置每个行的高
            row.setHeight((short)(rowCount*300));
        }
        if(count>1){
            for(short t=0;t<fieldCounts.length;t++){
                short j = fieldCounts[t];
                sheet.addMergedRegion(new CellRangeAddress(dataList.size()+1-count,dataList.size(),j,j));
            }       
        }            
        try{    
            HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
            //获取项目绝对路径
            String file_real_path=request.getSession().getServletContext().getRealPath("tempFile");
            //File file = new File(file_real_path);
            //FileTool.deleteChildFolder(file);
            String path = file_real_path+"\\"+fileName;
            System.out.println(file_real_path);
            FileOutputStream fout = new FileOutputStream(path);//"D:\\test.xls"
            wb.write(fout);
            fout.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;                
    }
	
	public static Map<String ,Object> getExcelData(String excelPath , String[] excelTitles){
	    Map<String,Object> map = new HashMap<String,Object>();
        
        if(excelPath == null || "".equals(excelPath)){
            map.put("status", 0);
            map.put("msg", "没有上传Excel文件!");
            return map;
        }
        
        if(excelTitles == null){
            map.put("status", 0);
            map.put("msg", "解析EXCEL所需的XML配置有问题！请及时联系管理员处理");
            return map;
        }
        int totalRows = 0;  //总行数
        int totalCells = 0; //总列数
        //保存EXCEL数据
        List<String[]> dataLst = new ArrayList<String[]>();
        //保存EXCEL需要提取的title的下标位置
        List<Integer> titleIndex = new ArrayList<Integer>();
       try {
    	    System.out.println("==="+excelPath);
            URL url = new URL(excelPath); //远程路径
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.connect();
            InputStream input = con.getInputStream();
            
            HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
            String systemPath = request.getSession().getServletContext().getRealPath("tempFile");
            String fileName = excelPath.split("/")[excelPath.split("/").length - 1];
            excelPath = systemPath + File.separator + fileName;
            
            FileOutputStream outPut = new FileOutputStream(excelPath);
            int temp = 0;
            while ((temp = input.read()) != -1) {
                outPut.write(temp);
            }
            outPut.close();
            input.close();
            System.out.println("已将"+excelPath+"\n写入到"+excelPath);
        } catch (IOException ioExceprion) {
            ioExceprion.printStackTrace();
            throw new RuntimeException("IO异常！");
        }
        
        //** 检查文件是否存在 *//*
        File file = new File(excelPath);
        if (file == null || !file.exists()){
            map.put("status", 0);
            map.put("msg", "找不到EXCEL文件！请重新上传");
            return map;
        }
        
        //File file=new File("D:\\excel\\3.xlsx");
        
        /** 根据版本选择创建Workbook的方式 */
        InputStream is = null;
        Workbook wb = null;
        if(excelPath.matches("^.+\\.(?i)(xls)$")){//用2003处理
            try {
                is = new FileInputStream(file);
                wb = new XSSFWorkbook(is);
                wb.setMissingCellPolicy(Row.RETURN_BLANK_AS_NULL);
            } catch (Exception e) {
//              e.printStackTrace();
                map.put("status", 0);
                map.put("msg", "读取文件发生异常！");
                return map;
            }finally{
                if(is!=null){
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        map.put("status", 0);
                        map.put("msg", "读取文件发生异常！");
                        return map;
                    }
                }
            }
        }else if(excelPath.matches("^.+\\.(?i)(xlsx)$")){//用2007处理
            try {
                is = new FileInputStream(file);
                wb = new XSSFWorkbook(is);
                wb.setMissingCellPolicy(Row.RETURN_BLANK_AS_NULL);
            } catch (Exception e) {
                e.printStackTrace();
                map.put("status", 0);
                map.put("msg", "读取文件发生异常！");
                return map;
            }finally{
                if(is!=null){
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        map.put("status", 0);
                        map.put("msg", "读取文件发生异常！");
                        return map;
                    }
                }
            }
        }else{
            map.put("status", 0);
            map.put("msg", "你上传的不是标准的微软Office Excel文件！无法解析！请重新上传标准的微软Office Excel文件！");
            return map;
        }
        
        /** 得到第一个shell */
        Sheet sheet = wb.getSheetAt(0);
        /** 得到Excel的行数 */
        totalRows = sheet.getPhysicalNumberOfRows();
        /** 得到Excel的列数 */
        if (totalRows >= 1 && sheet.getRow(0) != null) {
            totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }
        
//      //取得title行（即第0行）
        Row titleRow = sheet.getRow(0);
//      List<String> titles = new ArrayList<String>();
//      for(int nc = 0; nc < totalCells; nc++){
//          Cell titleCell = titleRow.getCell(nc);
//          String titleCellVal = "";
//          if(titleCell == null){
//              continue;
//          }
//          titleCellVal = titleCell.getStringCellValue();
//          for(String ss : excelTitles){
//              if(ss.equals(titleCellVal)){
//                  titleIndex.add(nc);
////                    titles.add(titleCellVal);
//              }
//          }
//      }
        
        //确定需提取的单元格列下标，排除重复单元格，排除缺失的需提取列
        for(int et = 0; et < excelTitles.length; et++){
            int flag = 0;//排查excel中是否存在title或是否有重复title的标识
            int index = -1;
            for(int nc = 0; nc < totalCells; nc++){
                Cell titleCell = titleRow.getCell(nc);
                if(titleCell == null){
                    continue;//查找下一个单元格
                }
                String titleCellVal = titleCell.getStringCellValue();
                if(excelTitles[et].trim().equals(titleCellVal.trim())){
                    flag++;//找到一个匹配的title则flag+1
                    index = nc;
                }
            }
            //没有找到需要提取的title
            if(flag <= 0){
                map.put("status", 0);
                map.put("msg", "EXCEL中找不到需要提取的列【"+excelTitles[et]+"】！请核查后上传正确的EXCEL");
                return map;
            }
            titleIndex.add(index);//保存需提取列的下标
        }
        
//      dataLst.add(titles);//实际运行时该行数据不需要添加到数据库
        
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        
        /** 循环Excel的行 ;从第1行开始循环totalRows*/
        for (int r = 1; r < totalRows; r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            String[] rowLst = new String[titleIndex.size()+1];
            /** 循环Excel中我们需要的列 */
            boolean flag=true;
            String operation="";
       
            for(int c =0 ; c<titleIndex.size()&&flag; c++){
            	
                Cell cell = row.getCell(titleIndex.get(c));
                String cellValue="";
                
                if (cell!=null) {
                    // 以下是判断数据的类型
            	   if("".equals(operation)&&(c==15)){
            		     cellValue = String.valueOf(cell.getRichStringCellValue());
              		   if("update".equals(cellValue)){
              			 operation="update";
              		   }
                   }
              
                    switch (cell.getCellType()) {
                    case XSSFCell.CELL_TYPE_NUMERIC: // 数字
                    	
                    	if("yyyy/mm;@".equals(cell.getCellStyle().getDataFormatString()) || "m/d/yy".equals(cell.getCellStyle().getDataFormatString())
                    	        || "yy/m/d".equals(cell.getCellStyle().getDataFormatString()) || "mm/dd/yy".equals(cell.getCellStyle().getDataFormatString())
                    	        || "dd-mmm-yy".equals(cell.getCellStyle().getDataFormatString())|| "yyyy/m/d".equals(cell.getCellStyle().getDataFormatString())){
                    		 cellValue = simpleDateFormat.format(cell.getDateCellValue());
                    	}else{
                        	if(c==2){
                        		if(cell.getDateCellValue().compareTo(new Date())<0){
                        			flag=false;
                            		break;
                        		}
                        	 	cellValue = simpleDateFormat.format(cell.getDateCellValue())+"";
                        	}else if(c==13){
                        		cellValue = simpleDateFormat.format(cell.getDateCellValue())+"";
                        	}else if(c==7){
                        		cellValue = cell.getNumericCellValue()+"";
                        		XSSFCellStyle xs = (XSSFCellStyle) cell.getCellStyle();
                        		if (xs.getFillPattern() == CellStyle.SOLID_FOREGROUND) {  
                        			XSSFColor color=xs.getFillForegroundColorColor();
                        			cellValue+="&"+color.getARGBHex();
                        		}  
                        	}else {
                        		cellValue = cell.getNumericCellValue()+"";
							}
                        }
                        break;
                    case XSSFCell.CELL_TYPE_STRING: // 字符串
                        cellValue = cell.getStringCellValue();
                        if(c==2||c==13){
                        	cellValue=cellValue.replaceAll("/", "-");
                        }else if(c==4&&"".equals(cellValue)){
                    		operation="add";
                    	}else if(c==6){
                    		if(isMergedRegion(sheet,r,c)){
                        		cellValue=getMergedRegionValue(sheet,r,c);
                    		}
                    	}else if(c==7){
                    		XSSFCellStyle xs = (XSSFCellStyle) cell.getCellStyle();
                    		if (xs.getFillPattern() == CellStyle.SOLID_FOREGROUND) {  
                    			XSSFColor color=xs.getFillForegroundColorColor();
                    			cellValue+="&"+color.getARGBHex();
                    		}  
                    	}
                        break;
                    case XSSFCell.CELL_TYPE_BOOLEAN: // Boolean
                        cellValue = cell.getBooleanCellValue() + "";
                        break;
                    case XSSFCell.CELL_TYPE_FORMULA: // 公式
                        try{
                            cellValue = String.valueOf(cell.getNumericCellValue());
                        } catch(IllegalStateException e){
                        }
                        cellValue += "";
                        break;
                    case XSSFCell.CELL_TYPE_BLANK: // 空值
                        cellValue = "";
                        break;
                    case XSSFCell.CELL_TYPE_ERROR: // 故障
                        map.put("status", 0);
                        map.put("msg", "【"+(r+1)+"】行：【"+(titleIndex.get(c)+1)+"】列无法解析！请处理后上传正确的EXCEL");
                        return map;
                    default:
                        //cellValue = "未知类型";
                        map.put("status", 0);
                        map.put("msg", "【"+(r+1)+"】行：【"+(titleIndex.get(c)+1)+"】列无法解析！请处理后上传正确的EXCEL");
                        return map;
                    }
                }else{
                   	if(c==4){
                		operation="add";
                	}else if(c==6){
                		if(isMergedRegion(sheet,r,c)){
                    		cellValue=getMergedRegionValue(sheet,r,c);
                		}
                	}
                   	
                }
                rowLst[c]=cellValue.trim();
            }
            if(flag && !"".equals(operation)){
                rowLst[rowLst.length-1]=(r+1)+"";
                /** 保存第r行的第c列 */
                dataLst.add(rowLst);
            }
        }
        
        //删除这个文件
        File tempFile = new File(excelPath);
        tempFile.delete();
        
        map.put(Constant.result_content, dataLst);
        map.put("status", 1);
        
        return map;
	}
	
	
	public static Map<String ,Object> getSkuExcelData(String excelPath , String[] excelTitles){
	    Map<String,Object> map = new HashMap<String,Object>();
        
        if(excelPath == null || "".equals(excelPath)){
            map.put("status", 0);
            map.put("msg", "没有上传Excel文件!");
            return map;
        }
        
        if(excelTitles == null){
            map.put("status", 0);
            map.put("msg", "解析EXCEL所需的XML配置有问题！请及时联系管理员处理");
            return map;
        }
        int totalRows = 0;  //总行数
        int totalCells = 0; //总列数
        //保存EXCEL数据
        List<String[]> dataLst = new ArrayList<String[]>();
        //保存EXCEL需要提取的title的下标位置
        List<Integer> titleIndex = new ArrayList<Integer>();
       try {
    	    System.out.println("==="+excelPath);
            URL url = new URL(excelPath); //远程路径
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.connect();
            InputStream input = con.getInputStream();
            
            HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
            String systemPath = request.getSession().getServletContext().getRealPath("tempFile");
            String fileName = excelPath.split("/")[excelPath.split("/").length - 1];
            excelPath = systemPath + File.separator + fileName;
            
            FileOutputStream outPut = new FileOutputStream(excelPath);
            int temp = 0;
            while ((temp = input.read()) != -1) {
                outPut.write(temp);
            }
            outPut.close();
            input.close();
            System.out.println("已将"+excelPath+"\n写入到"+excelPath);
        } catch (IOException ioExceprion) {
            ioExceprion.printStackTrace();
            throw new RuntimeException("IO异常！");
        }
        
        //** 检查文件是否存在 *//*
        File file = new File(excelPath);
        if (file == null || !file.exists()){
            map.put("status", 0);
            map.put("msg", "找不到EXCEL文件！请重新上传");
            return map;
        }
        
        //File file=new File("D:\\excel\\3.xlsx");
        
        /** 根据版本选择创建Workbook的方式 */
        InputStream is = null;
        Workbook wb = null;
        if(excelPath.matches("^.+\\.(?i)(xls)$")){//用2003处理
            try {
                is = new FileInputStream(file);
                wb = new XSSFWorkbook(is);
                wb.setMissingCellPolicy(Row.RETURN_BLANK_AS_NULL);
            } catch (Exception e) {
//              e.printStackTrace();
                map.put("status", 0);
                map.put("msg", "读取文件发生异常！");
                return map;
            }finally{
                if(is!=null){
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        map.put("status", 0);
                        map.put("msg", "读取文件发生异常！");
                        return map;
                    }
                }
            }
        }else if(excelPath.matches("^.+\\.(?i)(xlsx)$")){//用2007处理
            try {
                is = new FileInputStream(file);
                wb = new XSSFWorkbook(is);
                wb.setMissingCellPolicy(Row.RETURN_BLANK_AS_NULL);
            } catch (Exception e) {
                e.printStackTrace();
                map.put("status", 0);
                map.put("msg", "读取文件发生异常！");
                return map;
            }finally{
                if(is!=null){
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        map.put("status", 0);
                        map.put("msg", "读取文件发生异常！");
                        return map;
                    }
                }
            }
        }else{
            map.put("status", 0);
            map.put("msg", "你上传的不是标准的微软Office Excel文件！无法解析！请重新上传标准的微软Office Excel文件！");
            return map;
        }
        
        /** 得到第一个shell */
        Sheet sheet = wb.getSheetAt(0);
        /** 得到Excel的行数 */
        totalRows = sheet.getPhysicalNumberOfRows();
        /** 得到Excel的列数 */
        if (totalRows >= 1 && sheet.getRow(0) != null) {
            totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }
        
//      //取得title行（即第0行）
        Row titleRow = sheet.getRow(0);
//      List<String> titles = new ArrayList<String>();
//      for(int nc = 0; nc < totalCells; nc++){
//          Cell titleCell = titleRow.getCell(nc);
//          String titleCellVal = "";
//          if(titleCell == null){
//              continue;
//          }
//          titleCellVal = titleCell.getStringCellValue();
//          for(String ss : excelTitles){
//              if(ss.equals(titleCellVal)){
//                  titleIndex.add(nc);
////                    titles.add(titleCellVal);
//              }
//          }
//      }
        
        //确定需提取的单元格列下标，排除重复单元格，排除缺失的需提取列
        for(int et = 0; et < excelTitles.length; et++){
            int flag = 0;//排查excel中是否存在title或是否有重复title的标识
            int index = -1;
            for(int nc = 0; nc < totalCells; nc++){
                Cell titleCell = titleRow.getCell(nc);
                if(titleCell == null){
                    continue;//查找下一个单元格
                }
                String titleCellVal = titleCell.getStringCellValue();
                if(excelTitles[et].trim().equals(titleCellVal.trim())){
                    flag++;//找到一个匹配的title则flag+1
                    index = nc;
                }
            }
            //没有找到需要提取的title
            if(flag <= 0){
                map.put("status", 0);
                map.put("msg", "EXCEL中找不到需要提取的列【"+excelTitles[et]+"】！请核查后上传正确的EXCEL");
                return map;
            }
            titleIndex.add(index);//保存需提取列的下标
        }
        
//      dataLst.add(titles);//实际运行时该行数据不需要添加到数据库
        
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        
        /** 循环Excel的行 ;从第1行开始循环totalRows*/
        for (int r = 1; r < totalRows; r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            String[] rowLst = new String[titleIndex.size()+1];
            /** 循环Excel中我们需要的列 */
            boolean flag=true;
            String operation="";
       
            for(int c =0 ; c<titleIndex.size()&&flag; c++){
            	
                Cell cell = row.getCell(titleIndex.get(c));
                String cellValue="";
                
                if (cell!=null) {
                    // 以下是判断数据的类型
            	   if("".equals(operation)&&(c==15)){
            		     cellValue = String.valueOf(cell.getRichStringCellValue());
              		   if("update".equals(cellValue)){
              			 operation="update";
              		   }
                   }
              
                    switch (cell.getCellType()) {
                    case XSSFCell.CELL_TYPE_NUMERIC: // 数字
                    	
                    	if("yyyy/mm;@".equals(cell.getCellStyle().getDataFormatString()) || "m/d/yy".equals(cell.getCellStyle().getDataFormatString())
                    	        || "yy/m/d".equals(cell.getCellStyle().getDataFormatString()) || "mm/dd/yy".equals(cell.getCellStyle().getDataFormatString())
                    	        || "dd-mmm-yy".equals(cell.getCellStyle().getDataFormatString())|| "yyyy/m/d".equals(cell.getCellStyle().getDataFormatString())){
                    		 cellValue = simpleDateFormat.format(cell.getDateCellValue());
                    	}else{
                        	if(c==2){
                        		if(cell.getDateCellValue().compareTo(new Date())<0){
                        			flag=false;
                            		break;
                        		}
                        	 	cellValue = simpleDateFormat.format(cell.getDateCellValue())+"";
                        	}else if(c==13){
                        		cellValue = simpleDateFormat.format(cell.getDateCellValue())+"";
                        	}else if(c==7){
                        		cellValue = cell.getNumericCellValue()+"";
                        		XSSFCellStyle xs = (XSSFCellStyle) cell.getCellStyle();
                        		if (xs.getFillPattern() == CellStyle.SOLID_FOREGROUND) {  
                        			XSSFColor color=xs.getFillForegroundColorColor();
                        			cellValue+="&"+color.getARGBHex();
                        		}  
                        	}else {
                        		cellValue = cell.getNumericCellValue()+"";
							}
                        }
                        break;
                    case XSSFCell.CELL_TYPE_STRING: // 字符串
                        cellValue = cell.getStringCellValue();
                        if(c==2||c==13){
                        	cellValue=cellValue.replaceAll("/", "-");
                        }else if(c==4&&"".equals(cellValue)){
                    		operation="add";
                    	}
                        break;
                    case XSSFCell.CELL_TYPE_BOOLEAN: // Boolean
                        cellValue = cell.getBooleanCellValue() + "";
                        break;
                    case XSSFCell.CELL_TYPE_FORMULA: // 公式
                        try{
                            cellValue = String.valueOf(cell.getNumericCellValue());
                        } catch(IllegalStateException e){
                        }
                        cellValue += "";
                        break;
                    case XSSFCell.CELL_TYPE_BLANK: // 空值
                        cellValue = "";
                        break;
                    case XSSFCell.CELL_TYPE_ERROR: // 故障
                        map.put("status", 0);
                        map.put("msg", "【"+(r+1)+"】行：【"+(titleIndex.get(c)+1)+"】列无法解析！请处理后上传正确的EXCEL");
                        return map;
                    default:
                        //cellValue = "未知类型";
                        map.put("status", 0);
                        map.put("msg", "【"+(r+1)+"】行：【"+(titleIndex.get(c)+1)+"】列无法解析！请处理后上传正确的EXCEL");
                        return map;
                    }
                }else{
                   	if(c==4){
                		operation="add";
                	}
                }
                rowLst[c]=cellValue.trim();
            }
            /*if(flag && !"".equals(operation)){
                rowLst[rowLst.length-1]=(r+1)+"";
                *//** 保存第r行的第c列 *//*
                dataLst.add(rowLst);
            }*/
            dataLst.add(rowLst);
        }
        
        //删除这个文件
        File tempFile = new File(excelPath);
        tempFile.delete();
        
        map.put(Constant.result_content, dataLst);
        map.put("status", 1);
        
        return map;
	}
	
	public static boolean isMergedRegion(Sheet sheet , int row , int column){
		int sheetMergeCount = sheet.getNumMergedRegions();
		
		for(int i = 0 ; i < sheetMergeCount ; i++ ){
			CellRangeAddress ca = sheet.getMergedRegion(i);
			int firstColumn = ca.getFirstColumn();
			int lastColumn = ca.getLastColumn();
			int firstRow = ca.getFirstRow();
			int lastRow = ca.getLastRow();
			
			if(row >= firstRow && row <= lastRow){
				if(column >= firstColumn && column <= lastColumn){
					return true ;
				}
			}
		}
		
		return false ;
	}
	
	public static String getMergedRegionValue(Sheet sheet ,int row , int column){    
	    int sheetMergeCount = sheet.getNumMergedRegions();    
	        
	    for(int i = 0 ; i < sheetMergeCount ; i++){    
	        CellRangeAddress ca = sheet.getMergedRegion(i);    
	        int firstColumn = ca.getFirstColumn();    
	        int lastColumn = ca.getLastColumn();    
	        int firstRow = ca.getFirstRow();    
	        int lastRow = ca.getLastRow();    
	            
	        if(row >= firstRow && row <= lastRow){    
	                
	            if(column >= firstColumn && column <= lastColumn){    
	                Row fRow = sheet.getRow(firstRow);    
	                Cell fCell = fRow.getCell(firstColumn);    
	                return getCellValue(fCell) ;    
	            }    
	        }    
	    }    
	        
	    return null ;    
	}   
	
	public static String getCellValue(Cell cell){    
        
	    if(cell == null) return "";    
	        
	    if(cell.getCellType() == Cell.CELL_TYPE_STRING){    
	            
	        return cell.getStringCellValue();    
	            
	    }else if(cell.getCellType() == Cell.CELL_TYPE_BOOLEAN){    
	            
	        return String.valueOf(cell.getBooleanCellValue());    
	            
	    }else if(cell.getCellType() == Cell.CELL_TYPE_FORMULA){    
	            
	        return cell.getCellFormula() ;    
	            
	    }else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){    
	            
	        return String.valueOf(cell.getNumericCellValue());    
	    }
	    return "";    
	}    
	
public static boolean createExcelStyle(Object[][] titleList, String fileName){
    
        XSSFWorkbook wb = new XSSFWorkbook();
        
        /*设置字体*/
        XSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 10);      //字体高度
        font.setColor(XSSFFont.COLOR_NORMAL);        //字体颜色
        font.setFontName("微软雅黑");                  //字体
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);//粗体显示    
        XSSFSheet sheet = wb.createSheet();
        
        /*用于表头样式*/
        XSSFCellStyle titleStyle = wb.createCellStyle();  
        titleStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);  //左右居中
        titleStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);  //上下对齐
        titleStyle.setWrapText(true);     //自动换行
        titleStyle.setFont(font);
        titleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        titleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);   //左边框
        titleStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);    //上边框
        titleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);  //右边框
        
        XSSFRow row = sheet.createRow((int) 0);
        /*设置Excel行标*/
        XSSFCell cell = null;
        for (int i = 0; i < titleList.length; i++){                 
            cell = row.createCell(i);
            cell.setCellValue(new XSSFRichTextString(JSON.toJson(titleList[i][0])));        
            int linWidth = 10; //设置列宽
            if(titleList[i].length > 1){
                linWidth = Integer.parseInt((JSON.toJson(titleList[i][1])));
            }
            sheet.setColumnWidth(i,(short)(linWidth*1.3*256));
            cell.setCellStyle(titleStyle);    
        }
        
        wb.getCreationHelper()
        .createFormulaEvaluator()
        .evaluateAll();
        
        try{    
            HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
            //获取项目绝对路径
            String file_real_path=request.getSession().getServletContext().getRealPath("tempFile");
            String path = file_real_path+"\\"+fileName;
            System.out.println(file_real_path);
            FileOutputStream fout = new FileOutputStream(path);//"D:\\test.xls"
            wb.write(fout);
            fout.close();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        
        return true;                
    }

    public static Map<String ,Object> getExcel(String excelPath , String[] excelTitles){
        Map<String,Object> map = new HashMap<String,Object>();
        
        if(excelPath == null || "".equals(excelPath)){
            map.put("status", 0);
            map.put("msg", "没有上传Excel文件!");
            return map;
        }
        
        if(excelTitles == null){
            map.put("status", 0);
            map.put("msg", "解析EXCEL所需的XML配置有问题,请及时联系管理员处理!");
            return map;
        }
        int totalRows = 0;   //总行数
        int totalCells = 0;  //总列数
        List<String[]> dataLst = new ArrayList<String[]>();  //保存EXCEL数据
        List<Integer> titleIndex = new ArrayList<Integer>(); //保存EXCEL需要提取的title的下标位置
        try{
            URL url = new URL(excelPath); //远程路径
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.connect();
            InputStream input = con.getInputStream();
            
            HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
            String systemPath = request.getSession().getServletContext().getRealPath("tempFile");
            String fileName = excelPath.split("/")[excelPath.split("/").length - 1];
            excelPath = systemPath + File.separator + fileName;
            
            FileOutputStream outPut = new FileOutputStream(excelPath);
            int temp = 0;
            while((temp = input.read()) != -1){
                outPut.write(temp);
            }
            outPut.close();
            input.close();
            System.out.println("已将"+excelPath+"\n写入到"+excelPath);
        }catch(IOException ioExceprion){
            ioExceprion.printStackTrace();
            throw new RuntimeException("IO异常!");
        }
        
        //检查文件是否存在
        File file = new File(excelPath);
        if(file == null || !file.exists()){
            map.put("status", 0);
            map.put("msg", "找不到EXCEL文件,请重新上传!");
            return map;
        }
        
        //根据版本选择创建Workbook的方式 
        InputStream is = null;
        Workbook wb = null;
        if(excelPath.matches("^.+\\.(?i)(xls)$")){//用2003处理
            try{
                is = new FileInputStream(file);
                wb = new HSSFWorkbook(is);
                wb.setMissingCellPolicy(Row.RETURN_BLANK_AS_NULL);
            }catch(Exception e){
                map.put("status", 0);
                map.put("msg", "读取文件发生异常!");
                return map;
            }finally{
                if(is!=null){
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        map.put("status", 0);
                        map.put("msg", "读取文件发生异常!");
                        return map;
                    }
                }
            }
        }else if(excelPath.matches("^.+\\.(?i)(xlsx)$")){//用2007处理
            try{
                is = new FileInputStream(file);
                wb = new XSSFWorkbook(is);
                wb.setMissingCellPolicy(Row.RETURN_BLANK_AS_NULL);
            }catch (Exception e){
                e.printStackTrace();
                map.put("status", 0);
                map.put("msg", "读取文件发生异常!");
                return map;
            }finally{
                if(is != null){
                    try{
                        is.close();
                    }catch(IOException e){
                        e.printStackTrace();
                        map.put("status", 0);
                        map.put("msg", "读取文件发生异常!");
                        return map;
                    }
                }
            }
        }else{
            map.put("status", 0);
            map.put("msg", "你上传的不是标准的微软Office Excel文件,无法解析。请重新上传标准的微软Office Excel文件!");
            return map;
        }
        
        Sheet sheet = wb.getSheetAt(0);  //得到第一个shell
        totalRows = sheet.getPhysicalNumberOfRows();  //得到Excel的行数 
        //得到Excel的列数
        if(totalRows >= 1 && sheet.getRow(0) != null){
            totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }
        
        Row titleRow = sheet.getRow(0);  //取得title行（即第0行）
        
        //确定需提取的单元格列下标，排除重复单元格，排除缺失的需提取列
        for(int et = 0; et < excelTitles.length; et++){
            int flag = 0;   //排查excel中是否存在title或是否有重复title的标识
            int index = -1;
            for(int nc = 0; nc < totalCells; nc++){
                Cell titleCell = titleRow.getCell(nc);
                if(titleCell == null){
                    continue;  //查找下一个单元格
                }
                String titleCellVal = titleCell.getStringCellValue();
                if(titleCellVal == null || "".equals(titleCellVal)){
                    continue;  //查找下一个单元格
                }
                if(excelTitles[et].equals(titleCellVal.trim())){
                    flag++;   //找到一个匹配的title则flag+1
                    index = nc;
                }
            }
            //没有找到需要提取的title
            if(flag <= 0){
                map.put("status", 0);
                map.put("msg", "EXCEL中找不到需要提取的列【"+excelTitles[et]+"】!请核查后上传正确的Excel");
                return map;
            }
            //有重复的title出现
            if(flag > 1){
                map.put("status", 0);
                map.put("msg", "EXCEL中找到重复列【"+excelTitles[et]+"】!请处理后上传正确的Excel");
                return map;
            }
            titleIndex.add(index);//保存需提取列的下标
        }
        
        //循环Excel的行; 从第1行开始循环
        for (int r = 1; r < totalRows; r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            String[] rowLst = new String[titleIndex.size()];
            //循环Excel中我们需要的列 
            for(int c =0 ; c<titleIndex.size(); c++){
                Cell cell = row.getCell(titleIndex.get(c));
                String cellValue = "";
                if (null != cell) {
                    //以下是判断数据的类型
                    switch (cell.getCellType()) {
                        case XSSFCell.CELL_TYPE_NUMERIC: // 数字
                            if(org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)){//判断为日期【判断为日期的前提条件是先判断XSSFCell.CELL_TYPE_NUMERIC】
                                cellValue = xyz.util.DateUtil.dateToShortString(cell.getDateCellValue());
                                break;
                            }else{
                                cellValue = cell.getNumericCellValue() + "";
                            }
                            break;
                        case XSSFCell.CELL_TYPE_STRING: // 字符串
                            cellValue = cell.getStringCellValue();
                            break;
                        case XSSFCell.CELL_TYPE_BOOLEAN: // Boolean
                            cellValue = cell.getBooleanCellValue() + "";
                            break;
                        case XSSFCell.CELL_TYPE_FORMULA: // 公式
                            try{
                                cellValue = String.valueOf(cell.getNumericCellValue());
                            } catch(IllegalStateException e){
                                cellValue = String.valueOf(cell.getRichStringCellValue());
                            }
                            cellValue += "";
                            break;
                        case XSSFCell.CELL_TYPE_BLANK: // 空值
                            cellValue = "";
                            break;
                        case XSSFCell.CELL_TYPE_ERROR: // 故障
                            map.put("status", 0);
                            map.put("msg", "【"+(r+1)+"】行:【"+(titleIndex.get(c)+1)+"】列无法解析!请处理后上传正确的Excel");
                            return map;
                        default:
                            map.put("status", 0);
                            map.put("msg", "【"+(r+1)+"】行:【"+(titleIndex.get(c)+1)+"】列无法解析!请处理后上传正确的Excel");
                            return map;
                    }
                }
                rowLst[c]=cellValue.trim();
            }
            //保存第r行的第c列 
            dataLst.add(rowLst);
        }
        
        //删除这个文件
        File tempFile = new File(excelPath);
        tempFile.delete();
        
        map.put(Constant.result_content, dataLst);
        map.put("status", 1);
        
        return map;
    }
public static boolean createTemplateExcelForList(List<List<Object>> dataList,Object[][] titleList,String fileName){
    
        XSSFWorkbook wb = new XSSFWorkbook();
        //设置字体
        XSSFFont font =wb.createFont();
        font.setFontHeightInPoints((short) 10); //字体高度
        font.setColor(XSSFFont.COLOR_NORMAL); //字体颜色
        font.setFontName("微软雅黑"); //字体
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);//粗体显示    
        XSSFSheet sheet = wb.createSheet();
    
        XSSFRow row = sheet.createRow((int) 0);
        
        CellRangeAddress rang = CellRangeAddress.valueOf("A1:O1");
        sheet.setAutoFilter(rang);
        
        //用于表头样式
        XSSFCellStyle style = wb.createCellStyle();
        //左右居中
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        //上下对齐
        style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        //自动换行
        style.setWrapText(true);    
        style.setFont(font);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        
        //用于数据样式
        XSSFCellStyle style1 = wb.createCellStyle();
        //左右居中
        style1.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        //上下对齐
        style1.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        //自动换行
        style1.setWrapText(true);   
        style1.setFont(font);
        style1.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style1.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        style1.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        
        
        XSSFCellStyle style2 = wb.createCellStyle();
        style2.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        style2.setWrapText(true);   
        style2.setFont(font);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        style2.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND); 
        style2.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
        
        XSSFCellStyle yellowStyle = wb.createCellStyle();
        yellowStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        yellowStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        yellowStyle.setWrapText(true);   
        yellowStyle.setFont(font);
        yellowStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        yellowStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        yellowStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        yellowStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        yellowStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND); 
        yellowStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        
        XSSFCellStyle redStyle = wb.createCellStyle();
        redStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        redStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        redStyle.setWrapText(true);   
        redStyle.setFont(font);
        redStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        redStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        redStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        redStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        redStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND); 
        redStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
        
        
        //设置EXCEL公式规则
        XSSFSheetConditionalFormatting scf = sheet.getSheetConditionalFormatting();  
        
        //设置Excel行标
        XSSFCell cell = null;
        
        for (int i = 0; i < titleList.length; i++){                 
            cell = row.createCell(i);
            cell.setCellValue(new XSSFRichTextString(JSON.toJson(titleList[i][0])));        
            //设置列宽
            int linWidth = 10;
            if(titleList[i].length > 1){
                linWidth = Integer.parseInt((JSON.toJson(titleList[i][1])));
            }
            if(i>=15){
                   sheet.setColumnWidth(i,(short)(0));
            }else{
                sheet.setColumnWidth(i,(short)(linWidth*1.3*256));
            }
            cell.setCellStyle(style);    
        }
        XSSFCell cell1 = null;
    
        for (int i = 0; i < dataList.size(); i++){
            //动态创建行
            boolean colorFlag=false;
            String color="";
            row = sheet.createRow((int) i + 1);
            List<Object> tt = dataList.get(i);
            //用于比较列高大小
            int rowCount=1;
            for(int z=0;z<tt.size();z++){
                //这里来创建每个单元格 
                cell1 = row.createCell(z);
                
                String out=JSON.toJson( tt.get(z));
                if(out!=null){
                    int countT = out.split("\r\n").length;
                    if(countT>rowCount){
                        rowCount = countT;
                    }
                }
                if(z==7){
                    String[] array=out.split("&");
                    out=array[0];
                    if(array.length>1){
                        color= array[1];
                        colorFlag=true;
                    }
                }
                if(z==7||z==15){
                    cell1.setCellType(Cell.CELL_TYPE_NUMERIC);
                }else if((z==9||z==10||z==16)&&NumberUtil.isNum(out)){
                    cell1.setCellType(Cell.CELL_TYPE_NUMERIC);
                }else if(z==11){
                    if(NumberUtil.isNum(out)){
                        cell1.setCellType(Cell.CELL_TYPE_NUMERIC);
                    }else{
                        cell1.setCellType(Cell.CELL_TYPE_NUMERIC);
                    }
                }else  if(z==12){
                    if(!("现询".equals(tt.get(9).toString())||"售罄".equals(tt.get(9).toString()))){
                      cell1.setCellType(XSSFCell.CELL_TYPE_FORMULA);
                      cell1.setCellFormula("J"+(i+2)+"-L"+(i+2));
                    }
                }else if(z==17){
                    cell1.setCellType(XSSFCell.CELL_TYPE_FORMULA);
                    cell1.setCellFormula("IF(OR(H"+(i+2)+"<>P"+(i+2)+",J"+(i+2)+"<>Q"+(i+2)+"),\"update\",\"\")");
                    cell1.setCellStyle(style1); 
                }else{
                      cell1.setCellValue(new XSSFRichTextString(out));
                }
              
                if(colorFlag&&((z==7||z==8||z==15))){
                    if("#FFFFFF00".equals(color)){
                        cell1.setCellStyle(yellowStyle); 
                    }else if("#FFFF0000".equals(color)){
                        cell1.setCellStyle(redStyle); 
                    }else{
                        cell1.setCellStyle(style2); 
                    }
        
                }else{
                      cell1.setCellStyle(style1); 
                }
            }
            //设置每个行的高
            row.setHeight((short)(rowCount*300));
            
            int index=i+2;
            //添加修改变色规则
            XSSFConditionalFormattingRule cf_R_rule = scf.createConditionalFormattingRule("AND(H"+index+">P"+index+",LEN(E"+index+")<>0)"); 
            XSSFPatternFormatting cf_R = cf_R_rule.createPatternFormatting();  
    
            cf_R.setFillPattern(CellStyle.SOLID_FOREGROUND);
            cf_R.setFillBackgroundColor(IndexedColors.RED.index);  
            cf_R.setFillForegroundColor(IndexedColors.RED.index);  
    
            XSSFConditionalFormattingRule cf_R_rule2 = scf.createConditionalFormattingRule("AND(H"+index+"<P"+index+",LEN(E"+index+")<>0)");  
            XSSFPatternFormatting cf_R2 = cf_R_rule2.createPatternFormatting();  
    
            cf_R2.setFillPattern(CellStyle.SOLID_FOREGROUND);
            cf_R2.setFillBackgroundColor(IndexedColors.YELLOW.index);  
            cf_R2.setFillForegroundColor(IndexedColors.YELLOW.index);  
            
            XSSFConditionalFormattingRule cf_R_rule5 = scf.createConditionalFormattingRule("LEN(E"+index+")=0");  
            XSSFPatternFormatting cf_R5 = cf_R_rule5.createPatternFormatting();  
            cf_R5.setFillPattern(CellStyle.SOLID_FOREGROUND);
            cf_R5.setFillBackgroundColor(IndexedColors.BRIGHT_GREEN.index);  
            cf_R5.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.index);  
            
            XSSFConditionalFormattingRule cf_R_rule3 = scf.createConditionalFormattingRule("AND(J"+index+"<>Q"+index+",LEN(E"+index+")<>0)");  
            XSSFPatternFormatting cf_R3 = cf_R_rule3.createPatternFormatting();  
            cf_R3.setFillPattern(CellStyle.SOLID_FOREGROUND);
            cf_R3.setFillBackgroundColor(IndexedColors.RED.index);
            cf_R3.setFillForegroundColor(IndexedColors.RED.index);  
            
            XSSFConditionalFormattingRule cf_R_rule4 = scf.createConditionalFormattingRule("AND(J"+index+"=\"售罄\",LEN(E"+index+")<>0)");  
            XSSFPatternFormatting cf_R4 = cf_R_rule4.createPatternFormatting();  
            cf_R4.setFillPattern(CellStyle.SOLID_FOREGROUND);
            cf_R4.setFillBackgroundColor(IndexedColors.BLACK.index);
            cf_R4.setFillForegroundColor(IndexedColors.BLACK.index);  
            
            XSSFConditionalFormattingRule[] cfRules = {cf_R_rule,cf_R_rule2,cf_R_rule5};  
            XSSFConditionalFormattingRule[] cfRules2 = {cf_R_rule4,cf_R_rule3};  
            
          //条件格式应用的单元格范围  
          CellRangeAddress[] regions = {CellRangeAddress.valueOf("F"+index)};  
          CellRangeAddress[] regions2 = {CellRangeAddress.valueOf("J"+index)};  
          
          scf.addConditionalFormatting(regions, cfRules);  
          scf.addConditionalFormatting(regions2, cfRules2);  
          
        }
        
        wb.getCreationHelper()
        .createFormulaEvaluator()
        .evaluateAll();
        
        try{    
            HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
            //获取项目绝对路径
            String file_real_path=request.getSession().getServletContext().getRealPath("tempFile");
            //File file = new File(file_real_path);
            //FileTool.deleteChildFolder(file);
            String path = file_real_path+"\\"+fileName;
            System.out.println(file_real_path);
            FileOutputStream fout = new FileOutputStream(path);//"D:\\test.xls"
            wb.write(fout);
            fout.close();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        
        return true;
    }


public static boolean createCompareExcel(List<List<Object>> dataList,
		Object[][] titleList, String fileName) {
	
    XSSFWorkbook wb = new XSSFWorkbook();
    //设置字体
    XSSFFont font =wb.createFont();
    font.setFontHeightInPoints((short) 10); //字体高度
    font.setColor(XSSFFont.COLOR_NORMAL); //字体颜色
    font.setFontName("微软雅黑"); //字体
    font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);//粗体显示    
    XSSFSheet sheet = wb.createSheet();

    XSSFRow row = sheet.createRow((int) 0);
    
    CellRangeAddress rang = CellRangeAddress.valueOf("A1:O1");
    sheet.setAutoFilter(rang);
    
    //用于表头样式
    XSSFCellStyle style = wb.createCellStyle();
    //左右居中
    style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
    //上下对齐
    style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
    //自动换行
    style.setWrapText(true);    
    style.setFont(font);
    style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
    style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
    style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
    style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
    
    //用于数据样式
    XSSFCellStyle style1 = wb.createCellStyle();
    //左右居中
    style1.setAlignment(XSSFCellStyle.ALIGN_CENTER);
    //上下对齐
    style1.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
    //自动换行
    style1.setWrapText(true);   
    style1.setFont(font);
    style1.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
    style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
    style1.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
    style1.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框

    //对低价绿色样式
    XSSFCellStyle style2 = wb.createCellStyle();
    style2.setAlignment(XSSFCellStyle.ALIGN_CENTER);
    style2.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
    style2.setWrapText(true);   
    style2.setFont(font);
    style2.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
    style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
    style2.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
    style2.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
    style2.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND); 
    style2.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
    
    //设置EXCEL公式规则
    XSSFSheetConditionalFormatting scf = sheet.getSheetConditionalFormatting();  
    
    //设置Excel行标
    XSSFCell cell = null;
    
    for (int i = 0; i < titleList.length; i++){                 
        cell = row.createCell(i);
        cell.setCellValue(new XSSFRichTextString(JSON.toJson(titleList[i][0])));        
        //设置列宽
        int linWidth = 10;
        if(titleList[i].length > 1){
            linWidth = Integer.parseInt((JSON.toJson(titleList[i][1])));
        }
      
        sheet.setColumnWidth(i,(short)(linWidth*1.3*256));
        
        cell.setCellStyle(style);    
    }
    XSSFCell cell1 = null;

    for (int i = 0; i < dataList.size(); i++){
        //动态创建行
        row = sheet.createRow((int) i + 1);
        List<Object> tt = dataList.get(i);
        //用于比较列高大小
        int rowCount=1;
        
        String minPrice=JSON.toJson( tt.get(7));
        
        boolean flag=false;
        String price=JSON.toJson( tt.get(6));
        if(!StringTool.isEmpty(price)){
        	flag=true;
        }
        
        for(int z=0;z<tt.size();z++){
            //这里来创建每个单元格 
            cell1 = row.createCell(z);
            
            String out=JSON.toJson( tt.get(z));
            if(out!=null){
                int countT = out.split("\r\n").length;
                if(countT>rowCount){
                    rowCount = countT;
                }
            }
            int index=i+2;
          
            if(z==6||z==7){
            	  cell1.setCellType(Cell.CELL_TYPE_NUMERIC);
            	  cell1.setCellValue(out);
            }else if(flag&&z==9){
                cell1.setCellType(XSSFCell.CELL_TYPE_FORMULA);
                cell1.setCellFormula("INT(G"+index+"/0.967)+1");
                cell1.setCellStyle(style1); 
            }else if(flag&&z==10){
                cell1.setCellType(XSSFCell.CELL_TYPE_FORMULA);
                cell1.setCellFormula("IF(H"+index+"=\"\",\"\",IF(J"+index+">=H"+index+",\"\",IF(H"+index+"-J"+index+">=200,\"加价\",\"跟进\")))");
                cell1.setCellStyle(style1);
            }
            else{
            	  cell1.setCellValue(new XSSFRichTextString(out));
            }
            
            cell1.setCellStyle(style1); 
            
            if(!StringTool.isEmpty(minPrice)&&z>10&&z<tt.size()){
            	if(out.equals(minPrice)){
            		 cell1.setCellStyle(style2); 
            	}
            }
          
        }
        //设置每个行的高
        row.setHeight((short)(rowCount*300));
        
    }
    
    
    wb.getCreationHelper()
    .createFormulaEvaluator()
    .evaluateAll();
    
    try{    
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        //获取项目绝对路径
        String file_real_path=request.getSession().getServletContext().getRealPath("tempFile");
        //File file = new File(file_real_path);
        //FileTool.deleteChildFolder(file);
        String path = file_real_path+"\\"+fileName;
        System.out.println(file_real_path);
        FileOutputStream fout = new FileOutputStream(path);//"D:\\test.xls"
        wb.write(fout);
        fout.close();
    }catch (Exception e){
        e.printStackTrace();
        return false;
    }
    
    return true;                
}
    
}