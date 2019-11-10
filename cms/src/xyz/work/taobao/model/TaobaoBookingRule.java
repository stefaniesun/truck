package xyz.work.taobao.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 商品预定规则
 */
@Entity
@Table(name="taobao_booking_rule")
public class TaobaoBookingRule {
    
    @Id
    @Column(name="iidd",unique=true,nullable=false)
    @GeneratedValue(generator = "paymentableGenerator")       
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    private String iidd;   //主键
    
    @Column(name="number_code",unique=true,nullable=false)
    private String numberCode; //编号

    @Column(name="rule_desc",length=1500)
    private String ruleDesc; //描述(1500个字)
    
    /**
     * 规则类型
     * fee_included：费用包含,跟团游必填;
     * fee_excluded：费用不含,所有类目必填;
     * order_info：预定须知; 
     * extra_cost：其他费用,预留;
     */
    @Column(name="rule_type")
    private String ruleType; 
  
    @Column(name="taobao_travel_item")
    private String taobaoTravelItem; //用于关联
    
    @Column(name="add_date")
    private Date addDate;
    
    @Column(name="alter_date")
    private Date alterDate;
    
    public String getIidd() {
        return iidd;
    }

    public void setIidd(String iidd) {
        this.iidd = iidd;
    }

    public String getNumberCode() {
        return numberCode;
    }

    public void setNumberCode(String numberCode) {
        this.numberCode = numberCode;
    }

    public String getRuleDesc() {
        return ruleDesc;
    }

    public void setRuleDesc(String ruleDesc) {
        this.ruleDesc = ruleDesc;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public String getTaobaoTravelItem() {
        return taobaoTravelItem;
    }

    public void setTaobaoTravelItem(String taobaoTravelItem) {
        this.taobaoTravelItem = taobaoTravelItem;
    }

    public Date getAddDate() {
        return addDate;
    }

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }

    public Date getAlterDate() {
        return alterDate;
    }

    public void setAlterDate(Date alterDate) {
        this.alterDate = alterDate;
    }
    
}
