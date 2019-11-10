package xyz.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import xyz.util.Constant;

@Component
public class RmiUtil{
	@SuppressWarnings("unchecked")
	public Object loadData(HttpServletRequest request, String url, Map<String, String> accessoryParam){
		try{
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Cookie", request.getHeader("Cookie"));
			httpPost.setHeader("X-Requested-With",request.getHeader("X-Requested-With"));
			
			Map<String,Object[]> parameters = request.getParameterMap();
			List<NameValuePair> parameterList = new ArrayList<NameValuePair>();
			for(String p : parameters.keySet()){
				Object[] parameter = parameters.get(p);
				if(parameter!=null){
					for(Object para : parameter){
						parameterList.add(new BasicNameValuePair(p, para.toString()));
					}
				}
			}
			
			if(accessoryParam!=null){
				for(String key : accessoryParam.keySet()){
					parameterList.add(new BasicNameValuePair(key,accessoryParam.get(key)));
				}
			}
			
			if(parameterList.size()>0){
				HttpEntity httpEntity = new UrlEncodedFormEntity(parameterList,"utf8");
				httpPost.setEntity(httpEntity);
			}else{
				StringEntity httpEntity =  new StringEntity(IOUtils.toString(request.getInputStream()), "utf-8");
				httpEntity.setContentType("text/plain;charset=utf-8");
				httpEntity.setContentEncoding("utf-8");
				httpPost.setEntity(httpEntity);
			}
			
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(240000).setConnectTimeout(1000).build();
			httpPost.setConfig(requestConfig);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			Object result = null;
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				HttpEntity httpEntity2 = httpResponse.getEntity();
				byte[] bytes = EntityUtils.toByteArray(httpEntity2);
				String resultStr = new String(bytes,"utf-8");
				resultStr = resultStr.trim();
				if("".equals(resultStr)){
					result = "";
				}else if("[".equals(resultStr.substring(0, 1))){
					result = JSON.toObject(resultStr,List.class);
				}else if("{".equals(resultStr.substring(0, 1))){
					result = JSON.toObject(resultStr,Map.class);
				}else{
					result = "";
				}
			}else{
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(Constant.result_status, 0);
				map.put(Constant.result_msg, "http连接错误("+httpResponse.getStatusLine().getStatusCode()+")");
				result = map;
			}
			httpClient.close();
			return result;
		}catch(UnsupportedEncodingException e) {
			e.printStackTrace();
			return ReturnUtil.returnMap(0,e.getMessage());
		}catch(IOException e) {
			e.printStackTrace();
			return ReturnUtil.returnMap(0,e.getMessage());
		}
	}
	
	public Object loadData(String url, Object accessoryParam){
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("X-Requested-With","XMLHttpRequest");
			
			if(accessoryParam instanceof Map){
				List<NameValuePair> parameterList = new ArrayList<NameValuePair>();
				@SuppressWarnings("unchecked")
				Map<String,String> tttt = (Map<String,String>)accessoryParam;
				for(String key : tttt.keySet()){
					parameterList.add(new BasicNameValuePair(key,tttt.get(key)));
				}
				HttpEntity httpEntity = new UrlEncodedFormEntity(parameterList,"utf8");
				httpPost.setEntity(httpEntity);
			}else if(accessoryParam instanceof String){
				StringEntity httpEntity =  new StringEntity((String)accessoryParam, "utf-8");
				httpEntity.setContentType("text/plain;charset=utf-8");
				httpEntity.setContentEncoding("utf-8");
				httpPost.setEntity(httpEntity);
			}else{
				return ReturnUtil.returnMap(0,"参数类型有误，请传入Map<String,String>或String类型！");
			}
			
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(240000).setConnectTimeout(1000).build();
			httpPost.setConfig(requestConfig);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			Object result = null;
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				HttpEntity httpEntity2 = httpResponse.getEntity();
				byte[] bytes = EntityUtils.toByteArray(httpEntity2);
				String resultStr = new String(bytes,"utf-8");
				resultStr = resultStr.trim();
				if("".equals(resultStr)){
					result = "";
				}else if("[".equals(resultStr.substring(0, 1))){
					result = JSON.toObject(resultStr,List.class);
				}else if("{".equals(resultStr.substring(0, 1))){
					result = JSON.toObject(resultStr,Map.class);
				}else{
					result = "";
				}
			}else{
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(Constant.result_status, 0);
				map.put(Constant.result_msg, "http连接错误("+httpResponse.getStatusLine().getStatusCode()+")");
				result = map;
			}
			httpClient.close();
			return result;
		}catch(UnsupportedEncodingException e) {
			e.printStackTrace();
			return ReturnUtil.returnMap(0,e.getMessage());
		}catch(IOException e) {
			e.printStackTrace();
			return ReturnUtil.returnMap(0,e.getMessage());
		}
	}
}
