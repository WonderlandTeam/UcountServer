package cn.edu.nju.wonderland.ucountserver.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 预算（用户名，消费类型，消费金额，消费年月，建立时间）
 * Created by green-cherry on 2017/8/16.
 */
@Entity
public class Budget {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String username;
    private String consumeType;
    private Double consumeMoney;
    private Timestamp consumeTime;
    private Timestamp createTime;

    public Budget(String username, String consumeType, Double consumeMoney, Timestamp consumeTime, Timestamp createTime) {
        this.username = username;
        this.consumeType = consumeType;
        this.consumeMoney = consumeMoney;
        this.consumeTime = consumeTime;
        this.createTime = createTime;
    }

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
    @Column(name = "consume_type", nullable = true, length = 45)
    public String getConsumeType() {
        return consumeType;
    }

    public void setConsumeType(String consumeType) {
        this.consumeType = consumeType;
    }

    @Basic
    @Column(name = "consume_money", nullable = true, precision = 0)
    public Double getConsumeMoney() {
        return consumeMoney;
    }

    public void setConsumeMoney(Double consumeMoney) {
        this.consumeMoney = consumeMoney;
    }

    @Basic
    @Column(name = "consume_time", nullable = true)
    public Timestamp getConsumeTime() {
        return consumeTime;
    }

    public void setConsumeTime(Timestamp consumeTime) {
        this.consumeTime = consumeTime;
    }

    @Basic
    @Column(name = "create_time", nullable = false)
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }


}
