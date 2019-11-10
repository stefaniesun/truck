package xyz.work.excel.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "excel_log")
public class ExcelLog {

	public static final String TYPE_UPDATE="UPLOAD";
	public static final String TYPE_DOWNLOAD="DOWNLOAD";
	
	@Id
    @Column(name = "iidd" , unique = true , nullable = false)
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator" , strategy = "uuid")
    private String iidd;// 主键
    
    @Column(name = "number_code", unique = true , nullable = false)
    private String numberCode; 
    
    @Column(name="add_date")
    private Date addDate;
    
    @Column(name="username")
    private String username;
    
    @Column(name="type")
    private String type;
    
    @Column(name="file_url")
    private String fileUrl;
    
    @Column(name="change_count")
    private int changeCount;

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

	public Date getAddDate() {
		return addDate;
	}

	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public int getChangeCount() {
		return changeCount;
	}

	public void setChangeCount(int changeCount) {
		this.changeCount = changeCount;
	}
}
