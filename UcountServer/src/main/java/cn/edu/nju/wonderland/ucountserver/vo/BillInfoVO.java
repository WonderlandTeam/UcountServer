package cn.edu.nju.wonderland.ucountserver.vo;

import java.sql.Timestamp;

public class BillInfoVO {
    // TODO 账目信息VO
	public String type ; //交易类型
	public String trader ; //交易对象
	public double amount ; //交易金额
	public Timestamp tradeDate ; //交易日期
}
