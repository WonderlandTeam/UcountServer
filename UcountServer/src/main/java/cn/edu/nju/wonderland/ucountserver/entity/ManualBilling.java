package cn.edu.nju.wonderland.ucountserver.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 手动记账（用户名，时间，收/支，消费项目，消费类型，备注，卡类型，账号）
 * Created by green-cherry on 2017/8/16.
 */
@Entity
@Table(name = "manual_billing", schema = "Ucount_data", catalog = "")
public class ManualBilling {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String username;
    private Timestamp time;
    private Double incomeExpenditure;
    private Double balance;
    private String commodity;
    private String consumeType;
    private String remark;
    private String cardType;
    private String cardId;

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
    @Column(name = "time", nullable = false)
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Basic
    @Column(name = "remark", nullable = true, length = 100)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Basic
    @Column(name = "income_expenditure", nullable = false, precision = 0)
    public Double getIncomeExpenditure() {
        return incomeExpenditure;
    }

    public void setIncomeExpenditure(Double incomeExpenditure) {
        this.incomeExpenditure = incomeExpenditure;
    }

    @Column(name = "balance", nullable = false)
    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @Basic
    @Column(name = "commodity", nullable = true, length = 45)
    public String getCommodity() {
        return commodity;
    }

    public void setCommodity(String commodity) {
        this.commodity = commodity;
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
    @Column(name = "card_type", nullable = false, length = 45)
    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    @Basic
    @Column(name = "card_id", nullable = false, length = 45)
    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

}
