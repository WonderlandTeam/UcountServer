package cn.edu.nju.wonderland.ucountserver.vo;

import cn.edu.nju.wonderland.ucountserver.entity.Budget;
import cn.edu.nju.wonderland.ucountserver.util.DateHelper;

/**
 * 预算信息（预算id，用户名，消费类别，预算金额，预算时间，已消费金额，剩余金额）
 * 其实时间格式为 yyyy-mm , 年-月
 */
public class BudgetInfoVO {
    private Long id;
    private String username;
    private String consumeType;
    private double budgetMoney;
    private String bugdetTime;
    private double consume;
    private double remain;

    public BudgetInfoVO(){}

    public BudgetInfoVO(Long id, String username, String consumeType, double budgetMoney, String bugdetTime, double consume, double remain) {
        this.id = id;
        this.username = username;
        this.consumeType = consumeType;
        this.budgetMoney = budgetMoney;
        this.bugdetTime = bugdetTime;
        this.consume = consume;
        this.remain = remain;
    }

    public BudgetInfoVO(Budget budget,double consume,double remain){
        id=budget.getId();
        username=budget.getUsername();
        consumeType=budget.getConsumeType();
        budgetMoney=budget.getConsumeMoney();
        bugdetTime= DateHelper.toMonthByTimeStamp(budget.getConsumeTime());
        this.consume = consume;
        this.remain = remain;
    }

    public String toString(){
        String s=id+" "+username+" "+consumeType+" "+budgetMoney+" "+bugdetTime+" "+consume+" "+remain;
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

    public String getConsumeType() {
        return consumeType;
    }

    public void setConsumeType(String consumeType) {
        this.consumeType = consumeType;
    }

    public double getBudgetMoney() {
        return budgetMoney;
    }

    public void setBudgetMoney(double budgetMoney) {
        this.budgetMoney = budgetMoney;
    }

    public String getBugdetTime() {
        return bugdetTime;
    }

    public void setBugdetTime(String bugdetTime) {
        this.bugdetTime = bugdetTime;
    }

    public double getConsume() {
        return consume;
    }

    public void setConsume(double consume) {
        this.consume = consume;
    }

    public double getRemain() {
        return remain;
    }

    public void setRemain(double remain) {
        this.remain = remain;
    }
}
