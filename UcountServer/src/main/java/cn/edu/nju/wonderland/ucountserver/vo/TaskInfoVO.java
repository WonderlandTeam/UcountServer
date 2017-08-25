package cn.edu.nju.wonderland.ucountserver.vo;

import cn.edu.nju.wonderland.ucountserver.entity.Task;

/**
 * 计划信息（攒钱id，用户名，攒钱项目，攒钱总额，已攒金额，开始时间，预计完成日期，每日应攒金额,计划状态）
 * 时间格式为yyyy-MM-dd
 * Created by green-cherry on 2017/8/21.
 */
public class TaskInfoVO {
    private Long id;
    private String username;
    private String taskContent;
    private double upper;
    private double savedMoney;
    private String createTime;
    private String deadline;
    private double haveToSaveEveryday;
    private String taskState;


    public TaskInfoVO(Task task,double savedMoney,double haveToSaveEveryday){
        id=task.getId();
        username=task.getUsername();
        taskContent=task.getTaskContent();
        upper=task.getUpper();
        createTime=task.getCreateTime().toString();
        deadline=task.getDeadline().toString();
        taskState=task.getTaskState();
        this.savedMoney=savedMoney;
        this.haveToSaveEveryday=haveToSaveEveryday;
    }

    public String toString(){
        String s=id+" "+username+" "+taskContent+" " +createTime+" "+deadline+" "+upper+" "+taskState+" "+savedMoney+" "+haveToSaveEveryday;
        return s;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public double getUpper() {
        return upper;
    }

    public void setUpper(double upper) {
        this.upper = upper;
    }

    public double getSavedMoney() {
        return savedMoney;
    }

    public void setSavedMoney(double savedMoney) {
        this.savedMoney = savedMoney;
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

    public double getHaveToSaveEveryday() {
        return haveToSaveEveryday;
    }

    public void setHaveToSaveEveryday(double haveToSaveEveryday) {
        this.haveToSaveEveryday = haveToSaveEveryday;
    }
}
