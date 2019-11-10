package xyz.work.base.svc;


import java.util.Map;

import org.springframework.stereotype.Service;


@Service
public interface CompanySvc {
    /**
     * 查询所有的邮轮公司
     * 
     * @param offset
     * @param pageSize
     * @param nameCn
     *            邮轮公司名称
     * @author : 熊玲
     */
    public Map<String, Object> queryCompanyList(int offset , int pageSize , String nameCn);

    /**
     * 新增邮轮公司
     * 
     * @param nameCn
     *            邮轮公司名称
     * @param nameEn
     *            英文名
     * @param remark
     *            备注
     * @author : 熊玲
     */
    public Map<String, Object> addCompany(String nameCn , String nameEn , String websiteAddress , String remark);

    /**
     * 
       * 编辑邮轮公司
       *
       * @param numberCode 编号
       * @param nameCn 名称
       * @param nameEn 英文名
       * @param remark 备注
       * @author     : 熊玲
     */
    public Map<String, Object> editCompany(String numberCode , String nameCn , String nameEn ,
                                           String websiteAddress ,String remark);

    public Map<String, Object> deleteCompany(String numberCodes);

}