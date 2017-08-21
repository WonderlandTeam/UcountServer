package cn.edu.nju.wonderland.ucountserver.vo;

/**
 * 新建任务(用户名，攒钱内容，计划开始时间,预计完成时间，攒钱总额)
 * Created by green-cherry on 2017/8/21.
 */
public class TaskAddVO {
    private String username;
    private String taskContent;
    private String createTime;
    private String deadline;
    private Double upper;

    public TaskAddVO(String username, String taskContent, String createTime, String deadline, Double upper) {
        this.username = username;
        this.taskContent = taskContent;
        this.createTime = createTime;
        this.deadline = deadline;
        this.upper = upper;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTaskContent() {
        return taskContent;
    }

    public void setTaskContent(String taskContent) {
        this.taskContent = taskContent;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public Double getUpper() {
        return upper;
    }

    public void setUpper(Double upper) {
        this.upper = upper;
    }
}
