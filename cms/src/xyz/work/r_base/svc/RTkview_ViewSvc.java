package xyz.work.r_base.svc;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface RTkview_ViewSvc {
    /**
     * 查询皇家所有的邮轮
     */
    public Map<String, Object> queryRoyalCruiseList();
    
    /**
     * 根据邮轮获取所有舱型
     */
    public Map<String, Object> getRoyalCabinList(String cruise);
    
    /**
     * 根据舱型获取单品的月份和日期价格
     */
    public Map<String, Object> getRoyalTkviewDateList(String cruise , String cabin);
    
    /**
     * 根据单品编号获取单品及价格信息
     */
    public Map<String, Object> getRoyalTkviewPriceList(String cruise , String tkview , String date);
}