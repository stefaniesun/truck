package xyz.work.goal.model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;


/**
 * 计划目标
 * 
 * @author 熊玲
 * @version 2017-10-12下午4:51:21
 */
@Entity
@Table(name = "plan_goal")
public class PlanGoal {
    @Id
    @Column(name="iidd", unique=true, nullable=false)
    @GeneratedValue(generator="paymentableGenerator")
    @GenericGenerator(name="paymentableGenerator", strategy="uuid")
    private String iidd;          //主键

    @Column(name="number_code", unique=true, nullable=false)
    private String numberCode;   //编号
    
    @Column(name="type", nullable=false)
    private String type;      //类型(0:需求、1:bug、2:优化、3:其他)
    
    @Column(name="state")
    private int state;       //状态(0:完成、1:未完成)--开关,可以反复开关
    
    @Column(name="goal", length=999999999)
    private String goal;     //目标内容
    
    @Column(name="delay")
    private int delay;      //是否延后(0:是   1:否<默认> )
    
    @Column(name="founder")
    private String founder;  //发布人(编号)
    
    @Column(name="person")
    private String person;   //负责人(编号)
    
    @Transient
    private String founderName;  //发布人

    @Transient
    private String personName;   //负责人
    
    @Column(name="end_time")
    private Date endTime;   //完成时间
    
    @Column(name="add_date")
    private Date addDate;   //添加时间

    @Column(name="alter_date")
    private Date alterDate;  //修改时间
    
    @Column(name="remark", length=10000)
    private String remark;   //备注

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public String getFounder() {
        return founder;
    }

    public void setFounder(String founder) {
        this.founder = founder;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getFounderName() {
        return founderName;
    }

    public void setFounderName(String founderName) {
        this.founderName = founderName;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}