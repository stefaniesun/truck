package xyz.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/*
 * 亚索工具类
 *  此工具类必须依靠 apache-ant-zip.jar 这个Jar包 !
 */
public class ZipUtil {
    private static Logger log = LoggerFactory.getLogger(ZipUtil.class);

	/*
	 * 根据 文件的地址亚索成一个zip文件
	 * 参数为文件地址列表字符串(以","分割)
	 * 
	 */
	public static String getZipByURL(String fileUrlList){
        
		if(fileUrlList == null || "".equals(fileUrlList)){
			throw new RuntimeException("参数有误！");
		}
		String[] fileNames = fileUrlList.split(",");
		
        //获取项目绝对路径
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		String zipFloder = request.getSession().getServletContext().getRealPath("tempFile");
//        String zipFloder = "D:"+File.separator+"javaIo";
        
        String uuid = UUIDUtil.getUUIDStringFor32();
        String zipFileName = zipFloder + File.separator + uuid + ".zip";
        File zipFile = new File(zipFileName);
        
        InputStream input = null;
        try{
	        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
	        zipOut.setEncoding(System.getProperty("sun.jnu.encoding"));//更改编码格式, 避免中文名 乱码
	        // zip的名称为
	        zipOut.setComment(uuid);
	        
	        for (int i = 0; i < fileNames.length; ++i) {
	        	if(fileNames[i] == null || "".equals(fileNames[i])){
	        		continue;
	        	}
	        	URL url = new URL(fileNames[i]);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.connect();
				input = con.getInputStream();
				File f = new File(fileNames[i]);
	            zipOut.putNextEntry(new ZipEntry(f.getName()));
	            int temp = 0;
	            while ((temp = input.read()) != -1) {
	                zipOut.write(temp);
	            }
	            input.close();
	        }
	        zipOut.close();
	        log.info("压缩文件已生成 地址:"+zipFileName);
	        return uuid+".zip";
        }catch(IOException e){
        	e.printStackTrace();
        	throw new RuntimeException("IO异常！");
        }
	}
	
	
	/*
	 * 根据 文件的地址亚索成一个zip文件
	 * 参数为 zipName为键的Map + url 和 fileName 为键的Map 的List
	 * eg:  [ [{"zipName":"压缩包名称"}],[{"url":"http://www.baidu.com/images/xxxx.jpg"},{"fileName":"图片名称.jpg"}] ]
	 */
	public static String getZipByURLAndFileNames(String zipName,Map<String ,String> fileUrlAndNames){
		if(fileUrlAndNames == null || fileUrlAndNames.size()==0){
			throw new RuntimeException("无文件!");
		}
       
        //获取项目绝对路径
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		String zipFloder = request.getSession().getServletContext().getRealPath("tempFile");
//        String zipFloder = "D:"+File.separator+"javaIo";
        
        String zipFileName = zipFloder + File.separator + zipName + ".zip";
        File zipFile = new File(zipFileName);
        
        InputStream input = null;
        try{
	        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
	        zipOut.setEncoding(System.getProperty("sun.jnu.encoding"));//更改编码格式, 避免中文名 乱码
	        // zip的名称为
	        zipOut.setComment(zipName);
	        
	        for (String key : fileUrlAndNames.keySet()) {
	        	String fileName = key;
	        	String urlStr = fileUrlAndNames.get(key);
	        	
	        	if(urlStr == null || "".equals(urlStr)){
	        		continue;
	        	}
	        	if(fileName == null || "".equals(fileName)){
	        		continue;
	        	}
	        	
	        	URL url = new URL(urlStr);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.connect();
				input = con.getInputStream();
				File f = new File(fileName);
				zipOut.putNextEntry(new ZipEntry(f.getName()));
	            int temp = 0;
	            while ((temp = input.read()) != -1) {
	                zipOut.write(temp);
	            }
	            input.close();
	        }
	        zipOut.close();
	        log.info("压缩文件已生成 地址:"+zipFileName);
	        return zipName+".zip";
        }catch(IOException e){
        	e.printStackTrace();
        	throw new RuntimeException("IO异常！");
        }
	}
	
	public static String getZip(String fileList){
        
		if(fileList == null || "".equals(fileList)){
			throw new RuntimeException("参数有误！");
		}
		String[] fileNames = fileList.split(",");
		
        //获取项目绝对路径
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		String zipFloder = request.getSession().getServletContext().getRealPath("tempFile");
//        String zipFloder = "D:"+File.separator+"javaIo";
        
        String uuid = UUIDUtil.getUUIDStringFor32();
        String zipFileName = zipFloder + File.separator + uuid + ".zip";
        File zipFile = new File(zipFileName);
        
        InputStream input = null;
        try{
	        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
	        zipOut.setEncoding(System.getProperty("sun.jnu.encoding"));//更改编码格式, 避免中文名 乱码
	        // zip的名称为
	        zipOut.setComment(uuid);
	        
	        for (int i = 0; i < fileNames.length; ++i) {
	        	if(fileNames[i] == null || "".equals(fileNames[i])){
	        		continue;
	        	}
	        	File f = new File(fileNames[i]);
	        	input = new FileInputStream(f);
	            zipOut.putNextEntry(new ZipEntry(f.getName()));
	            int temp = 0;
	            while ((temp = input.read()) != -1) {
	                zipOut.write(temp);
	            }
	            input.close();
	        }
	        zipOut.close();
	        log.info("亚索文件已生成 地址:"+zipFileName);
	        return uuid+".zip";
        }catch(IOException e){
        	e.printStackTrace();
        	throw new RuntimeException("IO异常！");
        }
	}
	
	
	   /**
     * 解压缩ZIP文件，将ZIP文件里的内容解压到targetDIR目录下
     * @param zipName 待解压缩的ZIP文件名
     * @param targetBaseDirName  目标目录
     */
    public static List<File> upzipFile(String zipPath, String descDir) {
        return upzipFile( new File(zipPath) , descDir ) ;
    }
    
    
    private static byte[] _byte = new byte[1024] ;
    
    /**
     * 对.zip文件进行解压缩
     * @param zipFile  解压缩文件
     * @param descDir  压缩的目标地址，如：D:\\测试 或 /mnt/d/测试
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static List<File> upzipFile(File zipFile, String descDir) {
        List<File> _list = new ArrayList<File>() ;
        try {
            ZipFile _zipFile = new ZipFile(zipFile , "GBK") ;
            for( Enumeration entries = _zipFile.getEntries() ; entries.hasMoreElements() ; ){
                ZipEntry entry = (ZipEntry)entries.nextElement() ;
                File _file = new File(descDir + File.separator + entry.getName()) ;
                if( entry.isDirectory() ){
                    _file.mkdirs() ;
                }else{
                    File _parent = _file.getParentFile() ;
                    if( !_parent.exists() ){
                        _parent.mkdirs() ;
                    }
                    InputStream _in = _zipFile.getInputStream(entry);
                    OutputStream _out = new FileOutputStream(_file) ;
                    int len = 0 ;
                    while( (len = _in.read(_byte)) > 0){
                        _out.write(_byte, 0, len);
                    }
                    _in.close(); 
                    _out.flush();
                    _out.close();
                    _list.add(_file) ;
                }
            }
        } catch (IOException e) {
        }
        return _list ;
    }
}
