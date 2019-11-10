package xyz.work.taobao.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 邮轮商品相关信息,发布邮轮商品时必填 
 */
@Entity
@Table(name="taobao_cruise_item_ext")
public class TaobaoCruiseItemExt {

    @Id
    @Column(name="iidd",unique=true,nullable=false)
    @GeneratedValue(generator = "paymentableGenerator")       
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
    private String iidd;   //主键
    
    @Column(name="number_code",unique=true,nullable=false)
    private String numberCode;      //编号
    
    @Column(name="taobao_travel_item")
    private String taobaoTravelItem; //用于关联

    @Column(name="cruise_company")
    private String cruiseCompany; //必填，邮轮公司

    @Column(name="ship_name")
    private String shipName; //必填，邮轮船名
    
    @Column(name="cruise_line")
    private String cruiseLine; //必填，邮轮线路
    
    /**
     * 选填，邮轮相关小费包含选项（一个或多个数字，以英文逗号分隔）。
     * 国内邮轮: 1-"船票"  2-"岸上观光费" 
     *          3-"导游"  4-"其他费用" ...... 
     * 国际邮轮 (目前只有国际邮轮): 1-"船票"         2-"港务费、邮轮税费" 
     *           3-"岸上观光费"   4-"签证费用" 
     *           5-"小费"  6-"领队费"  7-"其他费用"
     */
    @Column(name="ship_fee_include",length=1000)
    private String shipFeeInclude;
    
    @Column(name="ship_up")
    private String shipUp; //必填，邮轮上船地点

    @Column(name="ship_down")
    private String shipDown; //必填，邮轮下船地点
    
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

    public String getTaobaoTravelItem() {
        return taobaoTravelItem;
    }

    public void setTaobaoTravelItem(String taobaoTravelItem) {
        this.taobaoTravelItem = taobaoTravelItem;
    }

    public String getCruiseCompany() {
        return cruiseCompany;
    }

    public void setCruiseCompany(String cruiseCompany) {
        this.cruiseCompany = cruiseCompany;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getCruiseLine() {
        return cruiseLine;
    }

    public void setCruiseLine(String cruiseLine) {
        this.cruiseLine = cruiseLine;
    }

    public String getShipFeeInclude() {
        return shipFeeInclude;
    }

    public void setShipFeeInclude(String shipFeeInclude) {
        this.shipFeeInclude = shipFeeInclude;
    }

    public String getShipUp() {
        return shipUp;
    }

    public void setShipUp(String shipUp) {
        this.shipUp = shipUp;
    }

    public String getShipDown() {
        return shipDown;
    }

    public void setShipDown(String shipDown) {
        this.shipDown = shipDown;
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