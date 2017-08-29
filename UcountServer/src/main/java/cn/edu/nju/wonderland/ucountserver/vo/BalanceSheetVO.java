package cn.edu.nju.wonderland.ucountserver.vo;

import java.util.Map;

/**
 * 资产负债表VO
 */
public class BalanceSheetVO {

    public String date;                                 // 日期


    /************************** 资产项目 **************************/

    /* 流动性资产 */
    public Map<String, Double> cash;                    // 现金
    public Map<String, Double> deposit;                 // 活存
    public Map<String, String> currentAssets;           // 流动性资产

    /* 投资性资产 */
    public Map<String, Double> foreignDeposit;          // 外币存款
    public Map<String, Double> stock;                   // 股票
    public Map<String, Double> fund;                    // 基金
    public Map<String, Double> bond;                    // 债券
    public Map<String, Double> investmentInsurance;     // 投资型保险
    public Map<String, Double> investmentAssets;        // 投资性资产

    /* 自用性资产 */
    public Map<String, Double> computer;                // 自用电脑
    public Map<String, Double> mobilePhone;             // 自用手机
    public Map<String, Double> personalAssets;          // 自用性资产

    /* 总资产 */
    public Map<String, Double> totalAssets;

    /************************** 负债项目 **************************/

    public double creditCardLiabilities;                // 信用卡负债
    public double consumerLiabilities;                  // 消费负债
    public double investmentLiabilities;                // 投资负债
    public double personalLiabilities;                  // 自用负债
    /* 总负债 */
    public double totalLiabilities;                     // 总负债

    /************************** 净值项目 **************************/

    public Map<String, Double> currentNetValue;         // 流动净值
    public Map<String, Double> investmentNetValue;      // 投资净值
    public Map<String, Double> personalNetValue;        // 自用净值
    /* 总净值 */
    public Map<String, Double> totalNetValue;

}
