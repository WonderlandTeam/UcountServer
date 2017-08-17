package cn.edu.nju.wonderland.ucountserver.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 *
 * 消费任务（用户名，消费类型，任务内容，消费上限，消费下限，建立时间，截止时间，任务状态）
 * Created by green-cherry on 2017/8/16.
 */
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String username;
    private Timestamp createTime;
    private String consumeType;
    private String taskContent;
    private Timestamp deadline;
    private String taskState;
    private Double upper;
    private Double lower;


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
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "consume_type", nullable = true, length = 45)
    public String getConsumeType() {
        return consumeType;
    }

    public void setConsumeType(String consumeType) {
        this.consumeType = consumeType;
    }

    @Basic
    @Column(name = "task_content", nullable = true, length = 100)
    public String getTaskContent() {
        return taskContent;
    }

    public void setTaskContent(String taskContent) {
        this.taskContent = taskContent;
    }

    @Basic
    @Column(name = "deadline", nullable = true)
    public Timestamp getDeadline() {
        return deadline;
    }

    public void setDeadline(Timestamp deadline) {
        this.deadline = deadline;
    }

    @Basic
    @Column(name = "task_state", nullable = true, length = 45)
    public String getTaskState() {
        return taskState;
    }

    public void setTaskState(String taskState) {
        this.taskState = taskState;
    }

    @Basic
    @Column(name = "upper", nullable = true, precision = 0)
    public Double getUpper() {
        return upper;
    }

    public void setUpper(Double upper) {
        this.upper = upper;
    }

    @Basic
    @Column(name = "lower", nullable = true, precision = 0)
    public Double getLower() {
        return lower;
    }

    public void setLower(Double lower) {
        this.lower = lower;
    }

}
