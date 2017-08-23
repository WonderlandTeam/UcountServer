package cn.edu.nju.wonderland.ucountserver.vo;

import cn.edu.nju.wonderland.ucountserver.entity.Budget;
import cn.edu.nju.wonderland.ucountserver.util.DateHelper;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * 添加预算（用户名，消费类型,消费金额，消费时间）
 * 其中消费时间格式为yyyy-MM
 */
public class BudgetAddVO {
    private String username;
    private String consumeType;
    private double consumeMoney;
    private String consumeTime;

    public BudgetAddVO(String username, String consumeType, double consumeMoney, String consumeTime) {
        this.username = username;
        this.consumeType = consumeType;
        this.consumeMoney = consumeMoney;
        this.consumeTime = consumeTime;
    }

    public Budget toBudgetEntity(){
        return new Budget(username,consumeType,consumeMoney, DateHelper.toTimestampByMonth(consumeTime),Timestamp.valueOf(LocalDateTime.now()));
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

    public double getConsumeMoney() {
        return consumeMoney;
    }

    public void setConsumeMoney(Double consumeMoney) {
        this.consumeMoney = consumeMoney;
    }

    public String getConsumeTime() {
        return consumeTime;
    }

    public void setConsumeTime(String consumeTime) {
        this.consumeTime = consumeTime;
    }
}
