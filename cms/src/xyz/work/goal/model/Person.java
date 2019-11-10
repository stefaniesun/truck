package xyz.work.goal.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "person")
public class Person {
    @Id
    @Column(name="iidd", unique=true, nullable=false)
    @GeneratedValue(generator="paymentableGenerator")
    @GenericGenerator(name="paymentableGenerator", strategy="uuid")
    private String iidd;          //主键

    @Column(name="number_code", unique=true, nullable=false)
    private String numberCode;   //编号
    
    @Column(name="post")
    private int post;          //职位(0:后台   1:前端   2:UI)
    
    @Column(name="name_cn")
    private String nameCn;     //姓名
    
    @Column(name="sex")
    private String sex;      //性别

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

    public int getPost() {
        return post;
    }

    public void setPost(int post) {
        this.post = post;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
    
}