package cn.edu.nju.wonderland.ucountserver.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 支付宝（用户名，账号，交易号 ，商户订单号，交易创建时间，付款时间，
 * 最近修改时间，交易来源地，交易类型，交易对方，商品名称 ，金额，
 * 收/支，交易状态，服务费，成功退款，备注，资金状态，消费类型，余额）
 * Created by green-cherry on 2017/8/16.
 */
@Entity
public class Alipay {

    private Long id;
    private String username;
    private String cardId;
    private String transactionId;
    private String orderId;
    private Timestamp createTime;
    private Timestamp payTime;
    private Timestamp lastUpdateTime;
    private String payFrom;
    private String payType;
    private String trader;
    private String commodity;
    private Double money;
    private String incomeExpenditureType;
    private String tradeState;
    private Double serviceCharge;
    private Double refund;
    private String remark;
    private String moneyState;
    private String consumeType;
    private Double balance;



    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
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
    @Column(name = "card_id", nullable = true, length = 45)
    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    @Basic
    @Column(name = "transaction_id", nullable = true, length = 45)
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Basic
    @Column(name = "order_id", nullable = true, length = 45)
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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
    @Column(name = "pay_time", nullable = true)
    public Timestamp getPayTime() {
        return payTime;
    }

    public void setPayTime(Timestamp payTime) {
        this.payTime = payTime;
    }

    @Basic
    @Column(name = "last_update_time", nullable = true)
    public Timestamp getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Timestamp lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Basic
    @Column(name = "pay_from", nullable = true, length = 45)
    public String getPayFrom() {
        return payFrom;
    }

    public void setPayFrom(String payFrom) {
        this.payFrom = payFrom;
    }

    @Basic
    @Column(name = "pay_type", nullable = true, length = 45)
    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    @Basic
    @Column(name = "trader", nullable = true, length = 45)
    public String getTrader() {
        return trader;
    }

    public void setTrader(String trader) {
        this.trader = trader;
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
    @Column(name = "money", nullable = true, precision = 0)
    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    @Basic
    @Column(name = "income_expenditure_type", nullable = true, length = 45)
    public String getIncomeExpenditureType() {
        return incomeExpenditureType;
    }

    public void setIncomeExpenditureType(String incomeExpenditureType) {
        this.incomeExpenditureType = incomeExpenditureType;
    }

    @Basic
    @Column(name = "trade_state", nullable = true, length = 45)
    public String getTradeState() {
        return tradeState;
    }

    public void setTradeState(String tradeState) {
        this.tradeState = tradeState;
    }

    @Basic
    @Column(name = "service_charge", nullable = true, precision = 0)
    public Double getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(Double serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    @Basic
    @Column(name = "refund", nullable = true, precision = 0)
    public Double getRefund() {
        return refund;
    }

    public void setRefund(Double refund) {
        this.refund = refund;
    }

    @Basic
    @Column(name = "remark", nullable = true, length = 45)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Basic
    @Column(name = "money_state", nullable = true, length = 45)
    public String getMoneyState() {
        return moneyState;
    }

    public void setMoneyState(String moneyState) {
        this.moneyState = moneyState;
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
    @Column(name = "balance", nullable = true)
    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

}
