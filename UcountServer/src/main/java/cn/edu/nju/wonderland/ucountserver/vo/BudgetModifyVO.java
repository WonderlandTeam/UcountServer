package cn.edu.nju.wonderland.ucountserver.vo;

/**
 * 修改预算（预算金额）
 */
public class BudgetModifyVO {
    double money;

    public BudgetModifyVO(){}

    public BudgetModifyVO(double money) {
        this.money = money;
    }

    public double getMoney() {

        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
