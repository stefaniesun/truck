package xyz.work.base.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "backup")
public class Backup {

	 	@Id
	    @Column(name = "iidd", unique = true, nullable = false)
	    @GeneratedValue(generator = "paymentableGenerator")
	    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	    private String iidd;// 主键
	 	
	 	@Column(name = "number_code", unique = true, nullable = false)
 	    private String numberCode;  //编号
	 	
	 	@Column(name = "file_url")
 	    private String fileUrl;  //备份文件路径
	 	
		@Column(name = "count")
 	    private int count;  //备份数据总条数
		
		@Column(name = "file_size")
 	    private double fileSize;  //备份文件大小(KB为单位)
	 	
	 	@Column(name = "add_ate")
 	    private Date addDate;  //备份时间

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

		public String getFileUrl() {
			return fileUrl;
		}

		public void setFileUrl(String fileUrl) {
			this.fileUrl = fileUrl;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public Date getAddDate() {
			return addDate;
		}

		public void setAddDate(Date addDate) {
			this.addDate = addDate;
		}

		public double getFileSize() {
			return fileSize;
		}

		public void setFileSize(double fileSize) {
			this.fileSize = fileSize;
		}
		
}