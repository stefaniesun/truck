package xyz.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xyz.filter.JSON;

public class PriceUtil {
	
	
	public static Map<String, String> PRICE_JSON_MAP=new HashMap<String, String>();
	public static Map<String, String> DEFAULT_PRICE_MAP=new HashMap<String, String>();
	
	public static BigDecimal getSalePrice(String channel,BigDecimal cost){
		
		if(!StringTool.isEmpty(PRICE_JSON_MAP.get(channel))){
			
			List<Map<String, String>> list=JSON.toObject(PRICE_JSON_MAP.get(channel), List.class);
			
			for(Map<String, String> map:list){
				BigDecimal min=new BigDecimal(map.get("minPrice"));
				BigDecimal max=new BigDecimal(map.get("maxPrice"));
				if(min.compareTo(cost)<=0&&max.compareTo(cost)>=0){
					BigDecimal price=new BigDecimal(map.get("price")).multiply(new BigDecimal(100));
					int temp=cost.divide(new BigDecimal(100)).intValue();
					temp+=price.intValue();
					temp=temp/100*100+99;
					return new BigDecimal(temp*100);
				}
			}
			
		}
		int temp=cost.divide(new BigDecimal(100)).intValue();
		if(temp<1998){
			temp=temp+100;
			temp=temp/100*100+99;
			return new BigDecimal(temp*100);
		}
		if(temp<2998){
			temp=temp+200;
			temp=temp/100*100+99;
			return new BigDecimal(temp*100);
		}	
		if(temp<4999){
			temp=temp+300;
			temp=temp/100*100+99;
			return new BigDecimal(temp*100);
		}
		if(temp<6999){
			temp=temp+400;
			temp=temp/100*100+99;
			return new BigDecimal(temp*100);
		}
		if(temp<9999){
			temp=temp+500;
			temp=temp/100*100+99;
			return new BigDecimal(temp*100);
		}
		if(temp<100000){
			temp=temp+1000;
			temp=temp/100*100+99;
			return new BigDecimal(temp*100);
		}
		
		return cost;
	}
	
	
	public static BigDecimal getNormalPrice(BigDecimal cost){
		
	
		int temp=cost.divide(new BigDecimal(100)).intValue();
		if(temp<1998){
			temp=temp+100;
			return new BigDecimal(temp*100);
		}
		if(temp<2998){
			temp=temp+200;
			return new BigDecimal(temp*100);
		}	
		if(temp<4999){
			temp=temp+300;
			return new BigDecimal(temp*100);
		}
		if(temp<6999){
			temp=temp+400;
			return new BigDecimal(temp*100);
		}
		if(temp<9999){
			temp=temp+500;
			return new BigDecimal(temp*100);
		}
		if(temp<100000){
			temp=temp+1000;
			return new BigDecimal(temp*100);
		}
		
		return cost;
	}
	
}
