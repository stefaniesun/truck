package xyz.work.truck.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.dialect.Ingres10Dialect;

@Entity
@Table(name = "truck")
public class Truck implements Comparable<Truck>{

		public static int STATUS_SUBMIT=0;//已提交
		public static int STATUS_CHECK=1;//已审核
		public static int STATUS_UNCHECK=-1;//审核失败
	
	  	@Id
	    @Column(name = "iidd" , unique = true , nullable = false)
	    @GeneratedValue(generator = "paymentableGenerator")
	    @GenericGenerator(name = "paymentableGenerator" , strategy = "uuid")
	    private String iidd;// 主键

	    @Column(name = "number_code" , unique = true , nullable = false)
	    private String numberCode; 
	    
	    @Column(name="customer")
	    private String customer;
	    
	    @Column(name="customer_name")
	    private String customerName;
	    
	    @Column(name="customer_img")
	    private String customerImg;
	    
	    @Column(name="read_count")
	    private int readCount;
	    
	    @Column(name="title")
	    private String title;
	    
	    @Column(name="status")
	    private int status; //
	    
	    @Column(name="price")
	    private String price;
	    
	    @Column(name="is_guohu")
	    private int isGuohu;
	    
	    @Column(name="card_date")
	    private Date cardDate;//上牌时间
	    
	    @Column(name="check_date")
	    private Date checkDate;//年检时间
	    
	    @Column(name="insurance_date")
	    private Date insuranceDate;//交强险时间
	    
	    @Column(name="mile")
	    private String mile;
	    
	    @Column(name="year")
	    private String year;
	    
	    @Column(name="phone")
	    private String phone;
	    
	    @Column(name="type")
	    private String type;//0个人 1商家
	    
	    @Column(name="address")
	    private String address;//联系地址
	    
	    @Column(name="remark",length = 5000)
	    private String remark;
	    
	    @Column(name="images",length = 5000)
	    private String images;
	    
	    @Column(name="add_date")
	    private Date addDate;
	    
	    @Column(name="pass_date")
	    private Date passDate;
	    
	    @Transient
	    private String viewDate;
	    
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

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getPrice() {
			return price;
		}

		public void setPrice(String price) {
			this.price = price;
		}

		public int getIsGuohu() {
			return isGuohu;
		}

		public void setIsGuohu(int isGuohu) {
			this.isGuohu = isGuohu;
		}

		public Date getCardDate() {
			return cardDate;
		}

		public void setCardDate(Date cardDate) {
			this.cardDate = cardDate;
		}

		public Date getCheckDate() {
			return checkDate;
		}

		public void setCheckDate(Date checkDate) {
			this.checkDate = checkDate;
		}

		public Date getInsuranceDate() {
			return insuranceDate;
		}

		public void setInsuranceDate(Date insuranceDate) {
			this.insuranceDate = insuranceDate;
		}

		public String getMile() {
			return mile;
		}

		public void setMile(String mile) {
			this.mile = mile;
		}

		public String getYear() {
			return year;
		}

		public void setYear(String year) {
			this.year = year;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		public String getImages() {
			return images;
		}

		public void setImages(String images) {
			this.images = images;
		}

		public Date getAddDate() {
			return addDate;
		}

		public void setAddDate(Date addDate) {
			this.addDate = addDate;
		}

		public String getCustomer() {
			return customer;
		}

		public void setCustomer(String customer) {
			this.customer = customer;
		}

		public Date getPassDate() {
			return passDate;
		}

		public void setPassDate(Date passDate) {
			this.passDate = passDate;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public String getCustomerName() {
			return customerName;
		}

		public void setCustomerName(String customerName) {
			this.customerName = customerName;
		}

		public String getCustomerImg() {
			return customerImg;
		}

		public void setCustomerImg(String customerImg) {
			this.customerImg = customerImg;
		}

		public int getReadCount() {
			return readCount;
		}

		public void setReadCount(int readCount) {
			this.readCount = readCount;
		}

		public String getViewDate() {
			return viewDate;
		}

		public void setViewDate(String viewDate) {
			this.viewDate = viewDate;
		}

		@Override
		public int compareTo(Truck o) {
			return o.getViewDate().compareTo(viewDate);
		}
	    
}
