package xyz.work.resources.svc.imp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alipay.api.internal.util.WebUtils;

import xyz.dao.CommonDao;
import xyz.exception.MyExceptionForXyz;
import xyz.filter.JSON;
import xyz.filter.ReturnUtil;
import xyz.util.Constant;
import xyz.util.ConstantMsg;
import xyz.util.PossessorUtil;
import xyz.util.PriceUtil;
import xyz.util.StringTool;
import xyz.util.UUIDUtil;
import xyz.work.resources.model.Channel;
import xyz.work.resources.model.Provider;
import xyz.work.resources.svc.ChannelSvc;

@Service
public class ChannelSvcImp implements ChannelSvc{

    @Autowired
    CommonDao commonDao;
    
    @Autowired
    PossessorUtil possessorUtil;
    
    @Override
    public Map<String, Object> queryChannelList(int offset , int pagesize , String numberCode ,
                                                String nameCn) {
        
        StringBuffer hql = new StringBuffer();
        hql.append("from Channel where 1=1 ");
        {
            hql.append(possessorUtil.getRelatesWhereHql(Constant.relate_type_channel));
        }
        if (StringTool.isNotNull(numberCode)) {
            hql.append(" and numberCode = '"+numberCode+"' ");
        }
        if (StringTool.isNotNull(nameCn)) {
            hql.append(" and nameCn like '%"+nameCn+"%' ");
        }
        
        Query countQuery = commonDao.getQuery("select count(*) " + hql.toString());
        Number tempNumber = (Number)countQuery.uniqueResult();
        int count = tempNumber == null ? 0 : tempNumber.intValue();
        Query query = commonDao.getQuery(hql.toString());
        query.setFirstResult(offset);
        query.setMaxResults(pagesize);

        @SuppressWarnings("unchecked")
        List<Channel> channelList = query.list();

        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", count);
        mapContent.put("rows", channelList);

        return ReturnUtil.returnMap(1, mapContent);
    }

    @Override
    public Map<String, Object> addChannel(String nameCn,
                                        String code,
                                        String appKey,
                                        String appSecret,
                                        String redirectUri,
                                        String remark){
        
        
        if (!StringTool.isNotNull(nameCn)) {
            return ReturnUtil.returnMap(0, ConstantMsg.channel_name_cn_not_null);
        }
        
        if (!StringTool.isNotNull(appSecret)) {
            return ReturnUtil.returnMap(0, ConstantMsg.channel_app_secret_not_null);
        }
        
        Channel channelObj = new Channel();
        
        Date date = new Date();
        
        channelObj.setAddDate(date);
        channelObj.setAlterDate(date);
        channelObj.setNameCn(nameCn);
        channelObj.setNumberCode(UUIDUtil.getUUIDStringFor32());
        channelObj.setAppKey(appKey);
        channelObj.setAppSecret(appSecret);
        channelObj.setRedirectUri(redirectUri);
        channelObj.setRemark(remark);
        channelObj.setCode(code);
        channelObj.setIsAuthorize(0);
        
        commonDao.save(channelObj);
        
        possessorUtil.savePossessorRelates(Constant.relate_type_channel, channelObj.getNumberCode());
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> editChannel(String numberCode , String nameCn ,
                                           String remark) {
        
        Channel channelObj = (Channel)commonDao.getObjectByUniqueCode(Constant.channel, Constant.numberCode, numberCode);
        if (channelObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.channel_not_exist_error);
        }
        
        Date date = new Date();
        
        channelObj.setAlterDate(date);
        channelObj.setNameCn(nameCn);
        channelObj.setRemark(remark);

        commonDao.save(channelObj);
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> deleteChannel(String numberCodes) {
        
        String sql = "delete from channel where number_code in ("+StringTool.StrToSqlString(numberCodes)+")";
        commonDao.getSqlQuery(sql).executeUpdate();
        
        return ReturnUtil.returnMap(1, null);
    }

    public String getSessionKey(Channel channelObj) throws IOException{
        
        Map<String,String> props=new HashMap<String,String>();
        props.put(Constant.channel_grant_type,Constant.channel_authorization_code);
        props.put(Constant.channel_code,channelObj.getCode());
        props.put(Constant.channel_client_id,channelObj.getAppKey());
        props.put(Constant.channel_client_secret,channelObj.getAppSecret());
        props.put(Constant.channel_redirect_uri,channelObj.getRedirectUri());
        props.put(Constant.channel_view,Constant.channel_web);
        String s = WebUtils.doPost(Constant.channel_sessionKey_url, props, 30000, 30000);
        System.out.println(s);
        
        @SuppressWarnings("unchecked")
        Map<String, String> map = JSON.toObject(s, HashMap.class);
        System.out.println(map.get(Constant.channel_access_token).toString());
        return map.get(Constant.channel_access_token).toString();
    }
    
    @Override
    public Map<String, Object> setAuthorizeOper(String code,String appKey,String url,String numberCode) {
        
        if (!StringTool.isNotNull(code)) {
            return ReturnUtil.returnMap(0, ConstantMsg.channel_code_not_null);
        }
        if (!StringTool.isNotNull(appKey)) {
            return ReturnUtil.returnMap(0, ConstantMsg.channel_appKey_not_null);
        }
        if (!StringTool.isNotNull(url)) {
            return ReturnUtil.returnMap(0, ConstantMsg.channel_redirect_uri_not_null);
        }
        
        Channel channelObj = (Channel)commonDao.getObjectByUniqueCode(Constant.channel, Constant.numberCode, numberCode);
        if (channelObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.channel_not_exist_error);
        }
        
        channelObj.setAppKey(appKey);
        channelObj.setRedirectUri(url);
        channelObj.setCode(code);
        
        String sessionKey = "";
        try {
            sessionKey = getSessionKey(channelObj);
        }catch (IOException e) {
            throw new MyExceptionForXyz(ConstantMsg.channel_call_back_error+e.getMessage());
        }
        
        channelObj.setSessionKey(sessionKey);
        channelObj.setIsAuthorize(1);
        
        commonDao.update(channelObj);
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> queryPossessorChannelList(Boolean isTrue , String possessor,String numberCode,String name) {
        
        StringBuffer hqlSb = new StringBuffer();
        
        hqlSb.append("from Channel where 1=1");
        List<String> relates = possessorUtil.getDecideMapByPossessor(possessor,Constant.relate_type_channel).keySet().contains(Constant.relate_type_channel)?possessorUtil.getDecideMapByPossessor(possessor,Constant.relate_type_channel).get(Constant.relate_type_channel):new ArrayList<String>();
       
        if (relates == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.possessor_numberCode_error);
        }
        
        if (isTrue) {
            hqlSb.append(" and numberCode in ("+StringTool.listToSqlString(relates)+")");
        }else {
            hqlSb.append(" and numberCode not in ("+StringTool.listToSqlString(relates)+")");
        }

        if (StringTool.isNotNull(numberCode)) {
            hqlSb.append(" and numberCode = '"+numberCode+"'");
        }

        if (StringTool.isNotNull(name)) {
            hqlSb.append(" and nameCn like '%"+name+"%'");
        }
        
        @SuppressWarnings("unchecked")
        List<Provider> providerList = commonDao.queryByHql(hqlSb.toString());
        
        Map<String, Object> mapContent = new HashMap<String, Object>();
        mapContent.put("total", providerList.size());
        mapContent.put("rows", providerList);
        
        return ReturnUtil.returnMap(1, mapContent);
    }

    @Override
    public Map<String, Object> queryChannlListByNumberCode(String numberCode) {
        if(!StringTool.isNotNull(numberCode)){
            return ReturnUtil.returnMap(0, "渠道编号不能为空!");
        }
        Channel channelList = (Channel)commonDao.getObjectByUniqueCode("Channel","numberCode", numberCode);
        
        return ReturnUtil.returnMap(1, channelList);
    }

    @Override
    public Map<String, Object> editPriceStrategyByNumberCode(String numberCode ,
                                                             String priceStrategy) {
        
        Channel channelObj = (Channel)commonDao.getObjectByUniqueCode(Constant.channel, Constant.numberCode, numberCode);
        if (channelObj == null) {
            return ReturnUtil.returnMap(0, ConstantMsg.channel_not_exist_error);
        }
        
        Date date = new Date();
        channelObj.setAlterDate(date);
        channelObj.setPriceStrategy(priceStrategy);
        commonDao.save(channelObj);
        
        return ReturnUtil.returnMap(1, null);
    }

    @Override
    public Map<String, Object> editSystemMemory() {
        
    	@SuppressWarnings("unchecked")
		List<Channel> channels=commonDao.queryByHql("from Channel");
    	
    	PriceUtil.PRICE_JSON_MAP.clear();
    	for(Channel channel:channels){
    		PriceUtil.PRICE_JSON_MAP.put(channel.getNumberCode(), channel.getPriceStrategy());
    	}
    	
    	return ReturnUtil.returnMap(1, null);
    }
    
}
