package cn.edu.nju.wonderland.ucountserver.vo;

/**
 * 添加账户vo
 */
public class AccountAddVO {
    public String username;         // 用户名
    public String accountType;      // 账户类型
    public String cardId;           // 账户id
    public double balance;          // 初始余额（手动账户须填写）
}
