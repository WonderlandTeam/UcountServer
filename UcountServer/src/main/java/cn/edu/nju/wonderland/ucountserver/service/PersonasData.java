package cn.edu.nju.wonderland.ucountserver.service;

public interface PersonasData {

    /**
     * 获取用户当前所有账户余额
     * @param username  用户名
     * @return          余额
     */
    double getCurrentTotalBalanceByUser(String username);

    /**
     * 获取用户日均消费
     * @param username  用户名
     * @return          日均消费
     */
    double getExpenditurePerDay(String username);

}
