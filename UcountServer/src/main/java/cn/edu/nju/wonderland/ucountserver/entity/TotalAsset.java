package cn.edu.nju.wonderland.ucountserver.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 账户总资产（用户名，账户类型，账号，时间，收/支，消费项目，消费类型，余额，流入累计，流出累计，备注）
 * Created by green-cherry on 2017/8/17.
 */
@Deprecated
@Entity
@Table(name = "total_asset", schema = "Ucount_data", catalog = "")
public class TotalAsset {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String username;
    private String cardType;
    private String cardId;
    private Timestamp time;
    private String incomeExpenditure;
    private Double totalAssetcol;
    private String commodity;
    private String consumeType;
    private Double remain;
    private Double inAsset;
    private Double outAsset;
    private String remark;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "username", nullable = true, length = 255)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "card_type", nullable = true, length = 255)
    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    @Basic
    @Column(name = "card_id", nullable = true, length = 255)
    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    @Basic
    @Column(name = "time", nullable = true)
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Basic
    @Column(name = "income_expenditure", nullable = true, length = 45)
    public String getIncomeExpenditure() {
        return incomeExpenditure;
    }

    public void setIncomeExpenditure(String incomeExpenditure) {
        this.incomeExpenditure = incomeExpenditure;
    }

    @Basic
    @Column(name = "total_assetcol", nullable = true, precision = 0)
    public Double getTotalAssetcol() {
        return totalAssetcol;
    }

    public void setTotalAssetcol(Double totalAssetcol) {
        this.totalAssetcol = totalAssetcol;
    }

    @Basic
    @Column(name = "commodity", nullable = true, length = 255)
    public String getCommodity() {
        return commodity;
    }

    public void setCommodity(String commodity) {
        this.commodity = commodity;
    }

    @Basic
    @Column(name = "consume_type", nullable = true, length = 255)
    public String getConsumeType() {
        return consumeType;
    }

    public void setConsumeType(String consumeType) {
        this.consumeType = consumeType;
    }

    @Basic
    @Column(name = "remain", nullable = true, precision = 0)
    public Double getRemain() {
        return remain;
    }

    public void setRemain(Double remain) {
        this.remain = remain;
    }

    @Basic
    @Column(name = "in_asset", nullable = true, precision = 0)
    public Double getInAsset() {
        return inAsset;
    }

    public void setInAsset(Double inAsset) {
        this.inAsset = inAsset;
    }

    @Basic
    @Column(name = "out_asset", nullable = true, precision = 0)
    public Double getOutAsset() {
        return outAsset;
    }

    public void setOutAsset(Double outAsset) {
        this.outAsset = outAsset;
    }

    @Basic
    @Column(name = "remark", nullable = true, length = 255)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
