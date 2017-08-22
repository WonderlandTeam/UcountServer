package cn.edu.nju.wonderland.ucountserver.vo;

/**
 * 攒钱计划修改信息（攒钱金额）
 * Created by green-cherry on 2017/8/21.
 */
public class TaskModifyVO {
    double money;

    public TaskModifyVO(double money) {
        this.money = money;
    }

    public double getMoney() {

        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
