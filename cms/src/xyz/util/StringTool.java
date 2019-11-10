package xyz.util;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 字符串工具
 * @author 姚成成
 */
public final class StringTool {
	/*
	 * 计算字符串的匹配指数
	 */
	public static int matchString(String str1,String str2){
		if(str1==null || str2==null){
			return 0;
		}
		int count=0;
		char[] str1Array = str1.length()<str2.length()?str1.toCharArray():str2.toCharArray();
		char[] str2Array = str1.length()<str2.length()?str2.toCharArray():str1.toCharArray();
		for(int i=0;i<str1Array.length;i++){
			for(int j=0;j<str2Array.length;j++){
				if(str1Array[i]==str2Array[j]){
					count++;
					int mMax = (str1Array.length-i)<(str2Array.length-j)?(str1Array.length-i):(str2Array.length-j);
					for(int m=1;m<mMax;m++){
						if(str1Array[i+m]==str2Array[j+m]){
							count++;
						}
					}
				}
			}
		}
		return count;
	}
	
	/*
	 * list转成str用于sql中的in
	 */
	public static String listToSqlString(Collection<String> aa){
		StringBuffer tt = new StringBuffer();
		for(String str : aa){
			if(tt.length()>0){
				tt.append(",'");
			}else{
				tt.append("'");
			}
			tt.append(str);
			tt.append("'");
		}
		return tt.toString().equals("")?"''":tt.toString();
	}
	
	/*
	 * 逗号分割的str转成str用于sql中的in
	 */
	public static String StrToSqlString(String aa){
		return ("'"+aa+"'").replaceAll(",", "','");
	}
	//字符串数组排序
	public static String[] StringSort(String[] arr){
		Comparator<Object> cmp = Collator.getInstance();
		Arrays.sort(arr, cmp); 
		return arr;
	}
	
	/**
	 * @param arr 数组
	 * @param fengefu 分隔符
	 */
	public static String arrToString(String[] arr,String fengefu){
		 StringBuffer tempString = new StringBuffer();
		 fengefu = fengefu==null||"".equals(fengefu)?",":fengefu;
		 for(String tt : arr){
			 if(tt==null || "".equals(tt)){
				 continue;
			 }
			 if(tempString.length()>0){
				 tempString.append(fengefu);
			 }
			 tempString.append(tt);
		 }
		 return tempString.toString(); 
	}
	
	/**
	 * @param arr 数组
	 * @param fengefu 分隔符
	 */
	public static String listToString(Collection<String> arr,String fengefu){
		 StringBuffer tempString = new StringBuffer();
		 fengefu = fengefu==null||"".equals(fengefu)?",":fengefu;
		 for(String tt : arr){
			 if(tt==null || "".equals(tt)){
				 continue;
			 }
			 if(tempString.length()>0){
				 tempString.append(fengefu);
			 }
			 tempString.append(tt);
		 }
		 return tempString.toString(); 
	}
	
	public static String replaceForSms(Map<String, Object> map,String s){
		String result = s;
		char[] tt = s.toCharArray();
		List<Integer> indexStarts = new ArrayList<Integer>();
		List<Integer> indexEnds = new ArrayList<Integer>();
		for(int i=0;i<tt.length;i++){
			if(tt[i]=='[' && i+1<tt.length && tt[i+1]=='#'){
				indexStarts.add(i);
			}
			if(tt[i]=='#' && i+1<tt.length && tt[i+1]==']'){
				indexEnds.add(i);
			}
		}
		if(indexStarts.size()!=indexEnds.size()){
			return null;
		}
		for(int i=0;i<indexStarts.size();i++){
			int indexStart = indexStarts.get(i);
			int indexEnd = indexEnds.get(i);
			if(indexEnd-indexStart<3){
				return null;
			}
		}
		for(int i=0;i<indexStarts.size();i++){
			int indexStart = indexStarts.get(i);
			int indexEnd = indexEnds.get(i);
			String subS = s.substring(indexStart, indexEnd+2);
			String subSS = subS.substring(2, subS.length()-2);
			String subSSS = subSS.split("@")[0];
			result = result.replace(subS,map.get(subSSS).toString());
		}
		return result;
	}
	
	
	
	// 获取随机字符串
	public static String getRandomString(int length) { // length 字符串长度 
	    StringBuffer buffer = new StringBuffer("0123456789"); 
	    StringBuffer sb = new StringBuffer(); 
	    Random r = new Random(); 
	    int range = buffer.length(); 
	    for (int i = 0; i < length; i ++) { 
	        sb.append(buffer.charAt(r.nextInt(range))); 
	    } 
	    return sb.toString(); 
	}
	
	/**
	 * 在字符串数组中查找第一个匹配元素的所在下标
	 * @param array
	 * @param target 不允许为 null
	 * @return 不存在-1，下标位从0开始
	 */
	public static int findTargetInArray(String[] array, String target){
		if(target==null){
			return -1;
		}
		for(int i=0;i<array.length;i++){
			if(target.equals(array[i])){
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * 从字符串数组中删除匹配字符并返回新数组
	 * @param array
	 * @param target 不允许为 null
	 * @return
	 */
	public static String[] removeTargetInArray(String[] array, String target){
		List<String> list = new ArrayList<String>();
		Collections.addAll(list, array);
		for(int i=0;i<list.size();i++){
			if(target.equals(list.get(i))){
				list.remove(i);
			}
		}
		return list.toArray(new String[0]);
	}
	
    public static String getStringByMap(Map<String, Object> map, String key){
    	Object value = map.get(key);
    	return value==null?"":value.toString();
    }
		    
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getMapByMap(Map<String, Object> map, String key){
    	Object value = map.get(key);
    	return value==null?new HashMap<String, Object>():(Map<String, Object>)value;
    }
	
	public static boolean isNotNull(String key){
    	if(key!=null && !"".equals(key)){
    		return true;
    	}else{
    		return false;
    	}
    }
	
	public static boolean isEmpty(String key){
        if(key!=null && !"".equals(key)){
            return false;
        }else{
            return true;
        }
    }
	
	public static String StrToSqlDualTable(String a){
        if(a==null || "".equals(a)){
            return "SELECT 1 AS dualtemp FROM DUAL WHERE 1=0";
        }
        String[] aa = a.split(",");
        StringBuffer tt = new StringBuffer();
        for(String str : aa){
            if(tt.length()>0){
                tt.append(" union ALL SELECT '"+str+"'");
            }else{
                tt.append("SELECT '"+str+"' AS dualtemp");
            }
        }
        return tt.toString().equals("")?null:tt.toString();
    }
}
