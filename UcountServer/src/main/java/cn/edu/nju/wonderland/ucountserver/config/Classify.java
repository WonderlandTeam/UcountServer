package cn.edu.nju.wonderland.ucountserver.config;

import cn.edu.nju.wonderland.ucountserver.entity.Alipay;
import cn.edu.nju.wonderland.ucountserver.entity.IcbcCard;
import cn.edu.nju.wonderland.ucountserver.entity.SchoolCard;
import cn.edu.nju.wonderland.ucountserver.util.BillType;


/**
 * 收入／支出记录归类
 */
public class Classify {

    /**
     * 支付宝账目分类
     */
    public static BillType classifyAlipay(Alipay alipay) {

        //收入
        if (alipay.getMoneyState().contains("收入")) {

            if (alipay.getCommodity().contains("余额宝")) {
                return BillType.MANAGEMENT_INCOME;
            }

            if (alipay.getCommodity().contains("生活费")) {
                return BillType.ALIMONY;
            }

            return BillType.OTHER_INCOME;
        }

        //食品
        if (alipay.getTrader().contains("食") || alipay.getCommodity().contains("食") || alipay.getCommodity().contains("餐")
                || alipay.getCommodity().contains("茶") || alipay.getCommodity().contains("面") || alipay.getCommodity().contains("果")
                || alipay.getCommodity().contains("饭") || alipay.getCommodity().contains("外卖") || alipay.getCommodity().contains("CoCo")
                || alipay.getTrader().contains("饿了么") || alipay.getTrader().contains("美团") || alipay.getTrader().contains("肯德基")
                || alipay.getTrader().contains("饮料") || alipay.getTrader().contains("味") || alipay.getTrader().contains("麦当劳")
                || alipay.getCommodity().contains("鸡") || alipay.getCommodity().contains("肉") || alipay.getCommodity().contains("沙拉")
                || alipay.getCommodity().contains("猪") || alipay.getCommodity().contains("牛") || alipay.getCommodity().contains("羊")
                || alipay.getCommodity().contains("奶") || alipay.getCommodity().contains("火锅") || alipay.getCommodity().contains("烧烤")
                || alipay.getCommodity().contains("甜丫丫") || alipay.getCommodity().contains("日料") || alipay.getCommodity().contains("赛百味")
                || alipay.getCommodity().contains("菜") || alipay.getCommodity().contains("冰") || alipay.getCommodity().contains("外卖")
                || alipay.getCommodity().contains("味") || alipay.getCommodity().contains("鱼") || alipay.getCommodity().contains("避风塘")
                || alipay.getCommodity().contains("料理") || alipay.getCommodity().contains("麻辣烫") || alipay.getCommodity().contains("珑意堂")) {

            return BillType.DIET;
        }

        if (alipay.getCommodity().contains("书") || alipay.getCommodity().contains("出版社") || alipay.getCommodity().contains("教材")) {
            return BillType.BOOK;
        }

        if (alipay.getCommodity().contains("考试") || alipay.getCommodity().contains("托福") || alipay.getCommodity().contains("雅思")
                || alipay.getCommodity().contains("考研") || alipay.getCommodity().contains("出国") || alipay.getCommodity().contains("留学")
                || alipay.getCommodity().contains("培训") || alipay.getCommodity().contains("四六级")) {
            return BillType.TRAINING;
        }

        if (alipay.getCommodity().contains("手机") || alipay.getCommodity().contains("平板") || alipay.getCommodity().contains("电脑")
                || alipay.getCommodity().contains("电子") || alipay.getCommodity().contains("apple") || alipay.getCommodity().contains("三星")
                || alipay.getCommodity().contains("魅族") || alipay.getCommodity().contains("小米") || alipay.getCommodity().contains("华为")) {
            return BillType.ELECTRONIC;
        }

        if (alipay.getCommodity().contains("话费") || alipay.getCommodity().contains("充值")) {
            return BillType.COMMUNICATION;
        }

        if (alipay.getCommodity().contains("水费") || alipay.getCommodity().contains("电费")) {
            return BillType.UTILITIES;
        }

        if (alipay.getCommodity().contains("衣") || alipay.getCommodity().contains("服") || alipay.getCommodity().contains("衫")
                || alipay.getCommodity().contains("鞋") || alipay.getCommodity().contains("靴") || alipay.getCommodity().contains("包")
                || alipay.getCommodity().contains("箱") || alipay.getCommodity().contains("帽") || alipay.getCommodity().contains("手套")) {
            return BillType.CLOTHING;
        }

        if (alipay.getCommodity().contains("护肤") || alipay.getCommodity().contains("化妆") || alipay.getCommodity().contains("身体乳")
                || alipay.getCommodity().contains("保湿水") || alipay.getCommodity().contains("眼霜") || alipay.getCommodity().contains("洁面乳")
                || alipay.getCommodity().contains("洗面奶")) {
            return BillType.CREAM;
        }

        if (alipay.getCommodity().contains("口红") || alipay.getCommodity().contains("粉") || alipay.getCommodity().contains("眉笔")) {
            return BillType.COSMETICS;
        }

        if (alipay.getPayType().contains("支付宝担保交易") || alipay.getCommodity().contains("超市")) {
            return BillType.COMMODITY;
        }

        if (alipay.getTrader().contains("滴滴") || alipay.getCommodity().contains("车") || alipay.getCommodity().contains("铁路")
                || alipay.getCommodity().contains("机票") || alipay.getTrader().contains("携程") || alipay.getTrader().contains("去哪儿")) {
            return BillType.TRAFFIC;
        }
        return BillType.OTHER_EXPENDITURE;
    }

    /**
     * 工行卡账目分类
     */
    public static BillType classifyICBC(IcbcCard icbcCard) {

        //收入
        if (icbcCard.getAccountAmountIncome() > 0) {
            //工资
            if (icbcCard.getSummary().contains("工资")) {
                return BillType.SALARY;
            }

            if (icbcCard.getSummary().contains("转账") && icbcCard.getAccountAmountIncome() >= 100) {
                return BillType.ALIMONY;
            }

            return BillType.OTHER_INCOME;
        }

        //支出
        if (icbcCard.getLocation().contains("食") || icbcCard.getLocation().contains("餐") || icbcCard.getLocation().contains("外卖")
                || icbcCard.getLocation().contains("茶") || icbcCard.getLocation().contains("面") || icbcCard.getLocation().contains("果")
                || icbcCard.getLocation().contains("饭") || icbcCard.getLocation().contains("外卖") || icbcCard.getLocation().contains("CoCo")
                || icbcCard.getLocation().contains("饿了么") || icbcCard.getLocation().contains("美团") || icbcCard.getLocation().contains("肯德基")
                || icbcCard.getLocation().contains("饮料") || icbcCard.getLocation().contains("味") || icbcCard.getLocation().contains("麦当劳")
                || icbcCard.getLocation().contains("鸡") || icbcCard.getLocation().contains("肉") || icbcCard.getLocation().contains("沙拉")
                || icbcCard.getLocation().contains("猪") || icbcCard.getLocation().contains("牛") || icbcCard.getLocation().contains("羊")
                || icbcCard.getLocation().contains("奶")) {

            return BillType.DIET;
        }

        if (icbcCard.getLocation().contains("铁路") || icbcCard.getLocation().contains("飞机") || icbcCard.getLocation().contains("高铁")) {
            return BillType.TRAFFIC;
        }

        if (icbcCard.getLocation().contains("移动") || icbcCard.getLocation().contains("电信") || icbcCard.getLocation().contains("联通")
                || icbcCard.getLocation().contains("话费") || icbcCard.getLocation().contains("充值")) {
            return BillType.COMMUNICATION;
        }

        if (icbcCard.getSummary().contains("校园")) {
            return BillType.DIET;
        }

        return BillType.OTHER_EXPENDITURE;
    }

    /**
     * 校园卡账目分类
     */
    public static BillType classifySchoolCard(SchoolCard schoolCard) {

        //根据消费地点判断消费类型

        if (schoolCard.getIncomeExpenditure() > 0) {
            return BillType.OTHER_INCOME;
        }

        //饮食消费
        if (schoolCard.getLocation().contains("食堂") || schoolCard.getLocation().contains("教工") || schoolCard.getLocation().contains("清真")
                || schoolCard.getLocation().contains("品缘")) {
            return BillType.DIET;
        }

        //通信和网费
        if (schoolCard.getConsumePattern().contains("网")) {
            return BillType.COMMUNICATION;
        }

        //水电气
        if (schoolCard.getConsumePattern().contains("电") || schoolCard.getLocation().contains("浴") || schoolCard.getLocation().contains("开水")) {
            return BillType.UTILITIES;
        }

        //日用品
        if (schoolCard.getLocation().contains("超市")) {
            return BillType.COMMODITY;
        }

        //打印资料
        if (schoolCard.getLocation().contains("雅思园") || schoolCard.getLocation().contains("唐耀摄影") || schoolCard.getLocation().contains("打印")) {
            return BillType.PRINT;
        }

        //娱乐
        if (schoolCard.getLocation().contains("体育") || schoolCard.getLocation().contains("游泳馆")) {
            return BillType.ENTERTAINMENT;
        }

        return BillType.OTHER_EXPENDITURE;
    }

}
