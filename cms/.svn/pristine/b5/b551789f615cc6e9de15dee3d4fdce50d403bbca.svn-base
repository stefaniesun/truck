package xyz.work.resources.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="stock")
public class Stock implements Comparable<Stock>{

	@Id
	@Column(name="iidd",unique=true,nullable=false)
	@GeneratedValue(generator = "paymentableGenerator")       
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	private String iidd;//主键
	
	@Column(name="number_code",unique=true,nullable=false)
    private String numberCode;  //编号
    
	@Column(name="tkview")
    private String tkview;   //所属单品
	
	@Column(name="tkview_name")
    private String tkviewName;   //所属单品
	
	@Column(name="nick_name")
    private String nickName;  //单品库存别名  (可填可不填)
    
	@Column(name="provider")
    private String provider; //供应商
	
	@Column(name = "grade")
    private int grade;      //供应商 等级: 1(等级最高)、2、3、4、5
   
	@Column(name="cost")
    private BigDecimal cost;// 整舱成本
    
	@Column(name="cost_remark",length = 1000)
    private String costRemark; //成本说明
    
	@Column(name="stock",scale=0)
    private BigDecimal stock;       //总库存(不算超卖)
    
	@Column(name="use_stock",scale=0)
    private BigDecimal useStock;   //使用库存
	
	@Column(name="rgb")
	private String rgb;    //表格背景色
	
	@Column(name="left_stock",scale=0)
    private BigDecimal leftStock;   //剩余库存
	
	@Column(name="surpass",scale=0)
    private BigDecimal surpass;   //总超卖数 
    
    @Column(name="use_surpass",scale=0)
    private BigDecimal useSurpass; //使用超卖数量
    
	@Column(name="over_date")
    private Date overDate; //库存有效期
	
	@Column(name="yu_yue")
	private String yuyue;//预约系统
	
	@Column(name="other_provider")
	private String otherProvider;//其余可询供应商
	
	@Column(name="special_cost")
	private String specialCost;//特殊结算价
    
	@Column(name="priority")
    private int priority; //优先级    数值越小,级别越高
	
	@Column(name="type")
	private int type;  //库存类型(1:实库;0:现询)
	
	@Column(name="remark",length = 1000)
    private String remark; //备注
    
	@Column(name="add_date")
    private Date addDate; //添加时间
    
	@Column(name="alter_date")
    private Date alterDate; //修改时间
	
	@Column(name="date")
    private Date date; //出发日期

    @Transient
    private String tkviewNameCn; //单品名称
    
    @Transient
    private String providerNameCn;  //供应商名称
    
    @Transient
    private String providerMark;  //供应商查询代码
    
    @Transient
    private String cruiseNameCn; 
    
    @Transient
    private String cabinNameCn; 
    
    @Transient
    private String airwayNameCn; 
    
    @Transient
    private int day;

    public String getIidd() {
        return iidd;
    }

    public String getTkviewName() {
		return tkviewName;
	}

	public void setTkviewName(String tkviewName) {
		this.tkviewName = tkviewName;
	}

	public void setIidd(String iidd) {
        this.iidd = iidd;
    }
    
    public BigDecimal getUseSurpass() {
        return useSurpass;
    }

    public void setUseSurpass(BigDecimal useSurpass) {
        this.useSurpass = useSurpass;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNumberCode() {
        return numberCode;
    }

    public void setNumberCode(String numberCode) {
        this.numberCode = numberCode;
    }

    public String getTkview() {
        return tkview;
    }

    public void setTkview(String tkview) {
        this.tkview = tkview;
    }

    public String getTkviewNameCn() {
        return tkviewNameCn;
    }

    public void setTkviewNameCn(String tkviewNameCn) {
        this.tkviewNameCn = tkviewNameCn;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRgb() {
		return rgb;
	}

	public void setRgb(String rgb) {
		this.rgb = rgb;
	}

	public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getProviderNameCn() {
        return providerNameCn;
    }

    public void setProviderNameCn(String providerNameCn) {
        this.providerNameCn = providerNameCn;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getCostRemark() {
        return costRemark;
    }

    public String getSpecialCost() {
		return specialCost;
	}

	public Date getDate() {
		return date;
	}

	public String getAirwayNameCn() {
		return airwayNameCn;
	}

	public void setAirwayNameCn(String airwayNameCn) {
		this.airwayNameCn = airwayNameCn;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public String getCabinNameCn() {
		return cabinNameCn;
	}

	public void setCabinNameCn(String cabinNameCn) {
		this.cabinNameCn = cabinNameCn;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setSpecialCost(String specialCost) {
		this.specialCost = specialCost;
	}

	public String getCruiseNameCn() {
		return cruiseNameCn;
	}

	public void setCruiseNameCn(String cruiseNameCn) {
		this.cruiseNameCn = cruiseNameCn;
	}

	public BigDecimal getLeftStock() {
		return leftStock;
	}

	public void setLeftStock(BigDecimal leftStock) {
		this.leftStock = leftStock;
	}

	public String getYuyue() {
		return yuyue;
	}

	public void setYuyue(String yuyue) {
		this.yuyue = yuyue;
	}

	public String getOtherProvider() {
		return otherProvider;
	}

	public void setOtherProvider(String otherProvider) {
		this.otherProvider = otherProvider;
	}

	public void setCostRemark(String costRemark) {
        this.costRemark = costRemark;
    }

    public BigDecimal getStock() {
        return stock;
    }

    public void setStock(BigDecimal stock) {
        this.stock = stock;
    }

    public BigDecimal getUseStock() {
        return useStock;
    }

    public void setUseStock(BigDecimal useStock) {
        this.useStock = useStock;
    }
    
    public BigDecimal getSurpass() {
        return surpass;
    }

    public void setSurpass(BigDecimal surpass) {
        this.surpass = surpass;
    }

    public Date getOverDate() {
        return overDate;
    }

    public void setOverDate(Date overDate) {
        this.overDate = overDate;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getProviderMark() {
        return providerMark;
    }

    public void setProviderMark(String providerMark) {
        this.providerMark = providerMark;
    }

    public BigDecimal getComparePriority(){
    	BigDecimal priority = new BigDecimal(1);
		
		if(stock!=null && stock.intValue()==0){
			priority = new BigDecimal(0);
		}
		return priority;
    }
    
	@Override
	public int compareTo(Stock stock) {
		int result = stock.getComparePriority().compareTo(getComparePriority());
		if(result == 0){
			return cost.compareTo(stock.getCost());
		}else{
			return result;
		}
	}
    
}