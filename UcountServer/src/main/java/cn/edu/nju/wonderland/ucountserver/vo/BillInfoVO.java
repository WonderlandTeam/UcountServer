package cn.edu.nju.wonderland.ucountserver.vo;

/**
 * 账目信息vo
 */
public class BillInfoVO {
	public Long billId;             //账目id
	public String type ; 			//交易类型
	public String trader ; 			//交易对象
	public double amount ; 			//交易金额
	public String time;				//交易时间
}
