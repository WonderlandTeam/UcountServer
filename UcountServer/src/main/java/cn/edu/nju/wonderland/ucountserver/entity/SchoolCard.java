package cn.edu.nju.wonderland.ucountserver.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 校园卡（用户名username，账号card_id，消费方式，地点location，消费顺序，时间，收/支income_expenditure，余额，消费类型consume_type）
 * Created by green-cherry on 2017/8/16.
 */
@Entity
public class SchoolCard {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String username;
    private String cardId;
    private String consumePattern;
    private String location;
    private String sequence;
    private Timestamp time;
    private Double incomeExpenditure;
    private Double balance;
    private String consumeType;

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
    @Column(name = "card_id", nullable = true, length = 9)
    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    @Basic
    @Column(name = "consume_pattern", nullable = true, length = 45)
    public String getConsumePattern() {
        return consumePattern;
    }

    public void setConsumePattern(String consumePattern) {
        this.consumePattern = consumePattern;
    }

    @Basic
    @Column(name = "location", nullable = true, length = 100)
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Basic
    @Column(name = "sequence", nullable = true, length = 45)
    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    @Basic
    @Column(name = "time", nullable = false)
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Basic
    @Column(name = "income_expenditure", nullable = true, precision = 0)
    public Double getIncomeExpenditure() {
        return incomeExpenditure;
    }

    public void setIncomeExpenditure(Double incomeExpenditure) {
        this.incomeExpenditure = incomeExpenditure;
    }

    @Basic
    @Column(name = "balance", nullable = true, precision = 0)
    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @Basic
    @Column(name = "consume_type", nullable = true, length = 45)
    public String getConsumeType() {
        return consumeType;
    }

    public void setConsumeType(String consumeType) {
        this.consumeType = consumeType;
    }

}
