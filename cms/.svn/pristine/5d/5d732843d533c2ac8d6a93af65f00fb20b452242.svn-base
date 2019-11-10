package xyz.work.weixin.svc;


import java.util.Map;

import org.springframework.stereotype.Service;


@Service
public interface QueryDataSvc {
    /**
     * 1、首页
     * 
     * @author : 熊玲
     */
    public Map<String, Object> queryCruiseList();

    /**
     * 2、时间详情--产品：邮轮 + 日历格子
     * 
     * @param cruise
     *            邮轮编号
     * @author : 熊玲
     */
    public Map<String, Object> queryCruiseAndDate(String cruise);

    /**
     * 2、时间详情--产品：日历单元格点击时间
     * 
     * @param cruise
     *            邮轮编号
     * @param date
     *            出发日期
     * @author : 熊玲
     */
    public Map<String, Object> clickDateOper(String cruise , String date);

    /**
     * 4、产品列表--筛选条件
     * 
     * @param cruise
     * @author : 熊玲
     */
    public Map<String, Object> getPtviewSort();

    /**
     * 4、产品列表
     * 
     * @param date
     *            出行时间
     * @param cruise
     *            邮轮选择
     * @param port
     *            出港港口
     * @param sort
     *            排序(价格price，销量sales)
     * @param order
     *            排序方式(价格从高到低 DESC，价格从低到高 ASC，销量最多)
     * @author : 熊玲
     */
    public Map<String, Object> queryPtviewList(String date , String cruise , String port ,
                                               String sort , String order);

    /**
     * 5、产品详情
     * 
     * @param ptview
     *            产品编号
     * @author : 熊玲
     */
    public Map<String, Object> queryPtviewByNumber(String ptview);

}