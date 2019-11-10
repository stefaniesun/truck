package xyz.work.resources.svc;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface ChannelSvc {

    public Map<String, Object> queryChannelList(int offset , int pagesize ,String numberCode,String nameCn);
    
    public Map<String, Object> addChannel(String nameCn,
                                        String code,
                                        String appKey,
                                        String appSecret,
                                        String redirectUri,
                                        String remark);
    
    public Map<String, Object> editChannel(String numberCode,
                                            String nameCn,
                                            String remark);
    
    public Map<String, Object> deleteChannel(String numberCodes);
    
    public Map<String, Object> setAuthorizeOper(String code,String appKey,String url,String numberCode);
    
    public Map<String, Object> queryPossessorChannelList(Boolean isTrue,String possessor,String numberCode,String name);
    
    public Map<String, Object> queryChannlListByNumberCode(String  numberCode);
    
    public Map<String, Object> editPriceStrategyByNumberCode(String  numberCode , String priceStrategy);
    
    public Map<String, Object> editSystemMemory();
    
}
