package cn.edu.nju.wonderland.ucountserver.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 工行卡（用户名，账号，交易日期，摘要，交易场所，交易国家或地区简称，钞/汇，交易金额(收入)，交易金额(支出)，交易币种，记账金额(收入)，记账金额(支出)，记账币种，余额，对方户名，消费类型）
 * Created by green-cherry on 2017/8/16.
 */
@Entity
@Table(name = "ICBC_Card", schema = "Ucount_data", catalog = "")
public class IcbcCard {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String username;
    private String cardId;
    private Timestamp tradeDate;
    private String summary;
    private String location;
    private String tradeArea;
    private String cashRemit;
    private Double transactionAmountIncome;
    private Double transactionAmountExpense;
    private String transactionCurrency;
    private Double accountAmountIncome;
    private Double accountAmountExpense;
    private String accountCurrency;
    private Double balance;
    private String otherAccount;
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
    @Column(name = "card_id", nullable = true, length = 45)
    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    @Basic
    @Column(name = "trade_date", nullable = true)
    public Timestamp getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(Timestamp tradeDate) {
        this.tradeDate = tradeDate;
    }

    @Basic
    @Column(name = "summary", nullable = true, length = 45)
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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
    @Column(name = "trade_area", nullable = true, length = 45)
    public String getTradeArea() {
        return tradeArea;
    }

    public void setTradeArea(String tradeArea) {
        this.tradeArea = tradeArea;
    }

    @Basic
    @Column(name = "cash_remit", nullable = true, length = 45)
    public String getCashRemit() {
        return cashRemit;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }

    @Basic
    @Column(name = "transaction_amount_income", nullable = true, precision = 0)
    public Double getTransactionAmountIncome() {
        return transactionAmountIncome;
    }

    public void setTransactionAmountIncome(Double transactionAmountIncome) {
        this.transactionAmountIncome = transactionAmountIncome;
    }

    @Basic
    @Column(name = "transaction_amount_expense", nullable = true, precision = 0)
    public Double getTransactionAmountExpense() {
        return transactionAmountExpense;
    }

    public void setTransactionAmountExpense(Double transactionAmountExpense) {
        this.transactionAmountExpense = transactionAmountExpense;
    }

    @Basic
    @Column(name = "transaction_currency", nullable = true, length = 45)
    public String getTransactionCurrency() {
        return transactionCurrency;
    }

    public void setTransactionCurrency(String transactionCurrency) {
        this.transactionCurrency = transactionCurrency;
    }

    @Basic
    @Column(name = "account_amount_income", nullable = true, precision = 0)
    public Double getAccountAmountIncome() {
        return accountAmountIncome;
    }

    public void setAccountAmountIncome(Double accountAmountIncome) {
        this.accountAmountIncome = accountAmountIncome;
    }

    @Basic
    @Column(name = "account_amount_expense", nullable = true, precision = 0)
    public Double getAccountAmountExpense() {
        return accountAmountExpense;
    }

    public void setAccountAmountExpense(Double accountAmountExpense) {
        this.accountAmountExpense = accountAmountExpense;
    }

    @Basic
    @Column(name = "account_currency", nullable = true, length = 45)
    public String getAccountCurrency() {
        return accountCurrency;
    }

    public void setAccountCurrency(String accountCurrency) {
        this.accountCurrency = accountCurrency;
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
    @Column(name = "other_account", nullable = true, length = 45)
    public String getOtherAccount() {
        return otherAccount;
    }

    public void setOtherAccount(String otherAccount) {
        this.otherAccount = otherAccount;
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
