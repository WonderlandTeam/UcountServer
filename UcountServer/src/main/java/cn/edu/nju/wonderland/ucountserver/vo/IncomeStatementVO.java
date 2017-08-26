package cn.edu.nju.wonderland.ucountserver.vo;

/**
 * 收支储蓄表（利润表）VO
 */
public class IncomeStatementVO {

    /************************** 收入部分 **************************/

    public double salary;               // 工资收入
    public double managementIncome;     // 理财收入
    public double alimony;              // 家庭补助
    public double otherIncome;          // 其他收入

    /* 收入合计 */
    public double totalIncome;          // 收入合计

    /************************** 支出部分 **************************/

    /* 生活必需 */
    public double commodity;            // 日用品
    public double utilities;            // 水电费（包括洗澡费用）
    public double communication;        // 通讯和网费
    public double diet;                 // 饮食（含校内外）
    public double electronic;           // 电子设备（手机、电脑等）
    public double traffic;              // 交通
    public double necessityTotal;       // 生活必需合计

    /* 妆服费 */
    public double clothing;             // 衣帽鞋包（含运动装备）
    public double cream;                // 护肤品
    public double cosmetics;            // 彩妆
    public double jewelry;              // 首饰
    public double adornTotal;           // 妆服费合计

    /* 学习 */
    public double training;             // 培训、考证费用
    public double book;                 // 书（一切实体书籍）
    public double stationery;           // 文具
    public double print;                // 打印资料、图像影印消费
    public double activity;             // 组织活动
    public double learningTotal;        // 学习费用合计

    /* 娱乐 */
    public double entertainment;        // 娱乐

    /* 理财支出 */
    public double managementExpenditure;// 理财支出

    /* 捐赠 */
    public double donation;             // 捐款
    public double otherDonation;        // 其他捐赠
    public double donationTotal;        // 捐赠合计

    /* 其他支出 */
    public double otherExpenditure;     // 其他支出

    /* 支出合计 */
    public double totalExpenditure;     // 支出合计

}
