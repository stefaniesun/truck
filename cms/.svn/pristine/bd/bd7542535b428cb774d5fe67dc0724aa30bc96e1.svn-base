package xyz.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import xyz.filter.JSON;

public class CrawlerUtil {

	/*private static WebDriver webDriver=new PhantomJSDriver();
	
	public synchronized static WebDriver getWebDriver(){ 
		if(webDriver==null){
			//webDriver = new PhantomJSDriver();
			System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe");
			webDriver = new ChromeDriver();
 		}
		
		return webDriver; 
	} */
	
	public static Map<String, List<String>> getCompareData(String sourceCode){

		
		StringBuffer buffer=new StringBuffer(sourceCode);
		int index=buffer.indexOf("window.__INITIAL_STATE__ = ");
		StringBuffer temp=new StringBuffer(buffer.substring(index));
		
		String jsonData="";
		
		index=temp.indexOf("</script>");
		jsonData=temp.substring(27, index);
		
		jsonData=jsonData.replaceAll(" ","");
		jsonData=jsonData.substring(0,jsonData.length()-1);
		
		Map<String, Object> map=JSON.toObject(jsonData, Map.class);
		
		System.out.println(JSON.toJson(map));
		
		Map<String, Object> subMap=(Map<String, Object>) map.get("data");
		
		Map<String, Object> m=(Map<String, Object>) subMap.get("itemProps");
		
		Map<String, Object> subMap2=(Map<String, Object>) m.get("data");
		
		Map<String, Object> dataMap=(Map<String, Object>) subMap2.get("skuMap");
		
		Map<String, List<String>> result=new HashMap<String, List<String>>();
		
		for (Map.Entry<String, Object> entry : dataMap.entrySet()) {  
			
			List<Map<String, Object>> list= (List<Map<String, Object>>) entry.getValue();
			
			for(Map<String, Object> mapObj:list){
				
				String packageName=mapObj.get("packageName").toString();
				
				List<String> subList=(List<String>) result.get(packageName);
				if(subList==null){
					subList=new ArrayList<String>();
					subList.add(mapObj.get("date").toString()+"&"+mapObj.get("finalPrice").toString());
					result.put(packageName, subList);
				}else{
					subList.add(mapObj.get("date").toString()+"&"+mapObj.get("finalPrice").toString());
				}
				
			}
		
		}  
		
		System.out.println(JSON.toJson(result));
		
		return result;
		
		
		/*try {
			long a=System.currentTimeMillis();
			WebDriver driver=getWebDriver();
			driver.get("https://traveldetail.fliggy.com/item.htm?spm=a1z10.5-b.w4011-11778137643.65.SsR5Kq&id=543306089850&rn=2103b214219007634cbcbbb19d08d9c8&abbucket=8&smToken=6f3acaa3ef6d45c8ab56d6ba951f1c08&smSign=uo3nM2udg88K4HiHGlWOKQ%3D%3D");
				String pageString=driver.getPageSource();
			System.out.println(pageString);
			//waitForElementToLoad(driver, 10, By.id("J_ReviweTotalNum"));
		
			WebDriverWait wait=new WebDriverWait(webDriver, 15,1500);
			wait.until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver){
					return driver.findElement(By.id("J_ReviewTable")).isDisplayed()
							&&driver.findElement(By.className("mboth")).isDisplayed();
				}
			});
			long b=System.currentTimeMillis();
			System.out.println("用时:"+(b-a));
			//System.out.println(webDriver.getPageSource());
			
			StringBuffer buffer=new StringBuffer(webDriver.getPageSource());
			int index=buffer.indexOf("window.__INITIAL_STATE__ = ");
			StringBuffer temp=new StringBuffer(buffer.substring(index));
			index=temp.indexOf("</script>");
			
			String jsonData=temp.substring(27, index);
			jsonData=jsonData.replaceAll(" ","");
			jsonData=jsonData.substring(0,jsonData.length()-2);
			
			Map<String, Object> map=JSON.toObject(jsonData, Map.class);
			
			Map<String, Object> subMap=(Map<String, Object>) map.get("data");
			
			Map<String, Object> m=(Map<String, Object>) subMap.get("itemProps");
			
			Map<String, Object> subMap2=(Map<String, Object>) m.get("data");
			
			Map<String, Object> dataMap=(Map<String, Object>) subMap2.get("skuMap");
			
			Map<String, List<String>> result=new HashMap<String, List<String>>();
			
			for (Map.Entry<String, Object> entry : dataMap.entrySet()) {  
				
				List<Map<String, Object>> list= (List<Map<String, Object>>) entry.getValue();
				
				for(Map<String, Object> mapObj:list){
					
					String packageName=mapObj.get("packageName").toString();
					
					List<String> subList=(List<String>) result.get(packageName);
					if(subList==null){
						subList=new ArrayList<String>();
						subList.add(mapObj.get("date").toString()+"&"+mapObj.get("finalPrice").toString());
						result.put(packageName, subList);
					}else{
						subList.add(mapObj.get("date").toString()+"&"+mapObj.get("finalPrice").toString());
					}
					
				}
			
			}  
			
			System.out.println(JSON.toJson(result));
			
			return result;
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
		
		return null;*/
	
	}
}
