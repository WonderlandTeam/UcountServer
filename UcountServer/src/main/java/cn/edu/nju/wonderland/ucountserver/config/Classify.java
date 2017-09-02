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
        // TODO
        return null;
    }

    /**
     * 工行卡账目分类
     */
    public static BillType classifyICBC(IcbcCard icbcCard) {
        // TODO
        return null;
    }

    /**
     * 校园卡账目分类
     */
    public static BillType classifySchoolCard(SchoolCard schoolCard) {
        // TODO
        return null;
    }

}
