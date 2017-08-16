package cn.edu.nju.wonderland.ucountserver.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 手动记账（用户名，时间，收/支，消费项目，消费类型，备注）
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
    private String commodity;
    private String consumeType;
    private String remark;

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
    @Column(name = "income_expenditure", nullable = true, precision = 0)
    public Double getIncomeExpenditure() {
        return incomeExpenditure;
    }

    public void setIncomeExpenditure(Double incomeExpenditure) {
        this.incomeExpenditure = incomeExpenditure;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ManualBilling that = (ManualBilling) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (time != null ? !time.equals(that.time) : that.time != null) return false;
        if (incomeExpenditure != null ? !incomeExpenditure.equals(that.incomeExpenditure) : that.incomeExpenditure != null)
            return false;
        if (commodity != null ? !commodity.equals(that.commodity) : that.commodity != null) return false;
        if (consumeType != null ? !consumeType.equals(that.consumeType) : that.consumeType != null) return false;
        return remark != null ? remark.equals(that.remark) : that.remark == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (incomeExpenditure != null ? incomeExpenditure.hashCode() : 0);
        result = 31 * result + (commodity != null ? commodity.hashCode() : 0);
        result = 31 * result + (consumeType != null ? consumeType.hashCode() : 0);
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        return result;
    }
}
