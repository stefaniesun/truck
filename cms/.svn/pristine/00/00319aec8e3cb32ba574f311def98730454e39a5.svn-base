package xyz.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreeMarkerUtil {

    private static Configuration cfg; //freemarker工厂
    
    private static FreeMarkerUtil instance; //当前工具类对象
    
    private String tempPath = WebRootPath.getInstance().getPath()+"temp"; //模版所在文件夹
    
    private String outPath = WebRootPath.getInstance().getPath()+"view"; //文件输出路径
    
    @SuppressWarnings("unused")
    private static long file_num = System.currentTimeMillis();
    
    private FreeMarkerUtil(){}
    
    public static synchronized FreeMarkerUtil getInstance(){
        if(instance == null){
            instance = new FreeMarkerUtil();
        }
        return instance;
    }
    
    private static synchronized Configuration getConfiguration(){
        if(cfg == null){
            cfg = new Configuration();
        }
        return cfg;
    }
    
    public enum TempType{
        DOC,HTML
    };
    
    /**
     * 
     * Description  ：对应模版生成文件
     *
     * @param tempName 模版名字
     * @param data 替换数据
     * @param type 生成模版类型
     * @throws Exception void
     * @exception   ：〈描述可能的异常〉
    
     * @author      ：Yisces(QQ)
     * @date        ：2016-8-8下午3:08:13
     */
    public String createTemplate(String tempName,Map<String, Object> data, String clientCode,TempType type) throws Exception{
        
        String returnPath = "../view/";
        
        //设置FreeMarket的模板文件夹位置
        getConfiguration().setDirectoryForTemplateLoading(new File(tempPath));
        
        //创建模板对象
        Template temp = getConfiguration().getTemplate(tempName + ".ftl","UTF-8");
                        
        String outFileName = "";
        String outName = "";
        
        switch(type){
            case DOC:
                outName = "doc_"+clientCode+".doc";
                outFileName = outPath + "/" + outName;
                break;
            case HTML:
                outName = "htm_"+clientCode+".html";
                outFileName = outPath + "/" + outName;
                break;
        }
        
        returnPath = returnPath + outName;
        
        //输出文件
        File outFile = new File(outFileName);
        
        //如果文件不存在则创建
        if(!outFile.getParentFile().exists()){
            outFile.getParentFile().mkdirs();
        }
        
        //将模板与数据模型合并生成文件
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),"UTF-8"));
        
        //在模板上执行插值的操作，并输出文件
        temp.process(data, out);
        
        //关闭流
        out.flush();
        out.close();
        
        return returnPath;
    }
}
