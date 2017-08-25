package cn.edu.nju.wonderland.ucountserver.entity;

import javax.persistence.*;
import java.sql.Date;

/**
 *
 * 攒钱计划（用户名，攒钱内容，攒钱总额，建立时间，预计完成日期，计划状态，已攒金额）
 * Created by green-cherry on 2017/8/16.
 */
@Entity
public class Task {
    private Long id;
    private String username;
    private Date createTime;
    private String taskContent;
    private Date deadline;
    private Double upper;
    private String taskState;



    private Double savedMoney;

    public Task() {
    }

    public Task(String username, Date createTime, String taskContent, Date deadline, Double upper, String taskState) {
        this.username = username;
        this.createTime = createTime;
        this.taskContent = taskContent;
        this.deadline = deadline;
        this.upper = upper;
        this.taskState = taskState;
        this.savedMoney=0.0;
    }


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "username", nullable = false, length = 50)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "create_time", nullable = false)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    @Basic
    @Column(name = "task_content", nullable = false, length = 100)
    public String getTaskContent() {
        return taskContent;
    }

    public void setTaskContent(String taskContent) {
        this.taskContent = taskContent;
    }

    @Basic
    @Column(name = "deadline", nullable = false)
    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }


    @Basic
    @Column(name = "upper", nullable = false, precision = 0)
    public Double getUpper() {
        return upper;
    }

    public void setUpper(Double upper) {
        this.upper = upper;
    }

    @Basic
    @Column(name = "task_state", nullable = false, length = 100)
    public String getTaskState() {
        return taskState;
    }

    public void setTaskState(String taskState) {
        this.taskState = taskState;
    }


    @Basic
    @Column(name = "saved_money", nullable = false, precision = 0)
    public Double getSavedMoney() {
        return savedMoney;
    }

    public void setSavedMoney(Double savedMoney) {
        this.savedMoney = savedMoney;
    }

    public String toString(){
        String s=id+" "+username+" "+taskContent+" " +createTime+" "+deadline+" "+upper+" "+taskState+" "+savedMoney;
        return s;
    }

}
