package cn.edu.nju.wonderland.ucountserver.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 *
 * 消费任务（用户名，消费类型，任务内容，建立时间，截止时间，任务状态）
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (username != null ? !username.equals(task.username) : task.username != null) return false;
        if (createTime != null ? !createTime.equals(task.createTime) : task.createTime != null) return false;
        if (consumeType != null ? !consumeType.equals(task.consumeType) : task.consumeType != null) return false;
        if (taskContent != null ? !taskContent.equals(task.taskContent) : task.taskContent != null) return false;
        if (deadline != null ? !deadline.equals(task.deadline) : task.deadline != null) return false;
        if (taskState != null ? !taskState.equals(task.taskState) : task.taskState != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (consumeType != null ? consumeType.hashCode() : 0);
        result = 31 * result + (taskContent != null ? taskContent.hashCode() : 0);
        result = 31 * result + (deadline != null ? deadline.hashCode() : 0);
        result = 31 * result + (taskState != null ? taskState.hashCode() : 0);
        return result;
    }
}
