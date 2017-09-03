package cn.edu.nju.wonderland.ucountserver.vo;

import java.sql.Timestamp;

public class BillAddVO {
    // TODO 添加账目VO
    public String username; //用户名
    public Timestamp time; //交易时间
    public Double incomeExpenditure; //交易金额
    public String commodity; //商品名称
    public String consumeType; //交易类型
    public String remark;
    public String cardType; //账户类型
}
