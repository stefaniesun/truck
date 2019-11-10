package xyz.util;

import java.util.Map;

import xyz.filter.JSON;




public class StringUtil {
	private StringUtil(){}
	private static long criuse_num = System.currentTimeMillis()/100000000; //邮轮编号
	private static long shipment_num = System.currentTimeMillis()/1000;  //航期编号
	private static long orderNum = System.currentTimeMillis();//订单编号
	private static long clientCode = System.currentTimeMillis();//票号
	private static long travel_num = System.currentTimeMillis()/1000;
    private static long base_num = System.currentTimeMillis()/1000;
    private static long sku_price_num = System.currentTimeMillis()/1000;
	private static long resource = System.currentTimeMillis()/1000;
	private static long sku_num = System.currentTimeMillis()/1000;
	private static long ptview_num = System.currentTimeMillis()/1000;
	
    public static String get_new_criuse_num(){
        long curt = System.currentTimeMillis()/1000000;
        if(curt > criuse_num){
            criuse_num = curt;
        }else{
            curt = ++criuse_num;
        }
        
        //4位 Long.toString(long i, int b) 按b进制转换i
        String cClientCode = "Y" + Long.toString(curt, 36).toUpperCase(); 
        return cClientCode;
    }
    
    public static String get_new_shipment_num(){
        long curt = System.currentTimeMillis()/1000000;
        if(curt > shipment_num){
            shipment_num = curt;
        }else{
            curt = ++shipment_num;
        }
        
        String cClientCode = "H" + Long.toString(curt, 36).toUpperCase(); //6位 
        return cClientCode;
    }
	
	public synchronized static String get_new_productNum(){
		long curt = System.currentTimeMillis();
		if(curt>orderNum){
			orderNum=curt;
		}else{
			curt = ++orderNum;
		}
		String cOrderNum = "P"+Long.toString(curt, 36).toUpperCase();
		return cOrderNum;
	}
	
	public synchronized static String get_new_skuNum(){
		long curt = System.currentTimeMillis();
		if(curt>orderNum){
			orderNum=curt;
		}else{
			curt = ++orderNum;
		}
		String cOrderNum = "S"+Long.toString(curt, 36).toUpperCase();
		return cOrderNum;
	}
	
	public synchronized static String get_new_orderNum(String business){
		long curt = System.currentTimeMillis();
		if(curt>orderNum){
			orderNum=curt;
		}else{
			curt = ++orderNum;
		}
		business = business==null?"D":business;
		String cOrderNum = business+String.valueOf(curt+1000000000000l);
		return cOrderNum;
	}
	
	public synchronized static String get_new_clientCode(String business){
		long curt = System.currentTimeMillis();
		if(curt>clientCode){
			clientCode=curt;
		}else{
			curt = ++clientCode;
		}
		business = business==null?"":business;
		String cClientCode = business+"P"+String.valueOf(curt+3000000000000l);
		return cClientCode;
	}
	
	/**
     * 淘宝宝贝travel_item
     * @return
     */
	public synchronized static String get_new_taobao_item_out_num(){
        long curt = System.currentTimeMillis();
        if(curt > travel_num){
            travel_num = curt;
        }else{
            curt = ++travel_num;
        }
        
        String cClientCode = "ITEM"+String.valueOf(curt + 3000000000000l);
        return cClientCode;
    }
    
    /**
     * 淘宝宝贝OUT编号
     * @return
     */
    public synchronized static String get_new_taobao_base_num(){
        long curt = System.currentTimeMillis();
        if(curt > base_num){
            base_num = curt;
        }else{
            curt = ++base_num;
        }
        
        String cClientCode = "OUT"+String.valueOf(curt + 3000000000000l);
        return cClientCode;
    }
    
    /**
     * 淘宝宝贝SKU编号
     * @return
     */
    public synchronized static String get_new_taobao_sku_out_num(){
        long curt = System.currentTimeMillis();
        if(curt > sku_num){
            sku_num = curt;
        }else{
            curt = ++sku_num;
        }
        
        String cClientCode = "SKU"+String.valueOf(curt + 3000000000000l);
        return cClientCode;
    }
    
    /**
     * 淘宝宝贝price编号
     * @return
     */
    public synchronized static String get_new_taobao_price_out_num(){
        long curt = System.currentTimeMillis();
        if(curt > sku_price_num){
            sku_price_num = curt;
        }else{
            curt = ++sku_price_num;
        }
        
        String cClientCode = "PRICE"+String.valueOf(curt + 3000000000000l);
        return cClientCode;
    }
	
	public synchronized static String get_new_ptview(){
		long curt = System.currentTimeMillis()/1000;
		if(curt>resource){
			resource=curt;
		}else{
			curt = ++resource;
		}
		String cResource ="PT"+Long.toString(curt, 36).toUpperCase();
		return cResource;
	}
	
	public synchronized static String get_new_tkview(){
		long curt = System.currentTimeMillis()/1000;
		if(curt>resource){
			resource=curt;
		}else{
			curt = ++resource;
		}
		String cResource ="TK"+Long.toString(curt, 36).toUpperCase();
		return cResource;
	}
	
	public synchronized static String get_new_provider(){
		long curt = System.currentTimeMillis()/1000;
		if(curt>resource){
			resource=curt;
		}else{
			curt = ++resource;
		}
		String cResource ="PR"+Long.toString(curt, 36).toUpperCase();
		return cResource;
	}
	
	public static String getRandomStr(int scale){
		String temp = "";
		while(temp.length()<scale){
			temp = temp+(int)(Math.random()*10);
		}
		return temp;
	}
	
	public static String getScaleStr(int scale,int number){
		String temp = number+"";
		while(temp.length()<scale){
			temp = 0+temp;
		}
		return temp;
	}
	
	public static boolean yqqQueryExists(String queryJson,String key){
		key = key==null||"".equals(key)?"queryCore":key;
		if(queryJson!=null && !"".equals(queryJson)){
			@SuppressWarnings("unchecked")
			Map<String,Object> tt = JSON.toObject(queryJson, Map.class);
			if(tt.containsKey(key)){
				return true;
			}
		}
		return false;
	}
	
	public static String yqqQueryString(String queryJson,String key,int...isText){
		key = key==null||"".equals(key)?"queryCore":key;
		if(queryJson!=null && !"".equals(queryJson)){
			@SuppressWarnings("unchecked")
			Map<String,Map<String, Object>> tt = JSON.toObject(queryJson, Map.class);
			if(isText.length>0 && isText[0]==1){
				return tt.get(key).get("queryText")==null?"":tt.get(key).get("queryText").toString();
			}else{
				return tt.get(key).get("queryId")==null?"":tt.get(key).get("queryId").toString();

			}
		}
		return "";
	}
	
	public synchronized static String get_new_ptview_num(){
        long curt = System.currentTimeMillis()/1000;
        if(curt>ptview_num){
            ptview_num=curt;
        }else{
            curt = ++ptview_num;
        }
        String cResource ="PT"+Long.toString(curt, 36).toUpperCase();
        return cResource;
    }
	
	public synchronized static String get_new_ptview_sku(){
        long curt = System.currentTimeMillis()/1000;
        if(curt > resource){
            resource = curt;
        }else{
            curt = ++resource;
        }
        String cResource = "PS" + Long.toString(curt, 36).toUpperCase();
        
        return cResource;
    }
	
}