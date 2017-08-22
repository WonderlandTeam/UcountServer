package cn.edu.nju.wonderland.ucountserver.vo;

/**
 * 用户账户总信息：用户名，收入，支出，剩余
 * Created by green-cherry on 2017/8/21.
 */
public class TotalAccountVO {
    private String username;
    private double income;
    private double expend;
    private double balance;

    public TotalAccountVO(String username, double income, double expend, double balance) {
        this.username = username;
        this.income = income;
        this.expend = expend;
        this.balance = balance;
    }
    public TotalAccountVO(){
    	
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getExpend() {
        return expend;
    }

    public void setExpend(double expend) {
        this.expend = expend;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
