package cn.edu.nju.wonderland.ucountserver.util;

import java.util.HashSet;
import java.util.Set;

public class BillFilter {
    /**
     * 计算利润表时需要过滤的支付宝账目commodity信息
     */
    public static final Set<String> ALIPAY_COMMODITY_FILTER = new HashSet<>();
    /**
     * 计算利润表时需要过滤的工行卡账目summary信息
     */
    public static final Set<String> ICBC_SUMMARY_FILTER = new HashSet<>();

    static {
        ALIPAY_COMMODITY_FILTER.add("转账到银行卡-转账");
        ALIPAY_COMMODITY_FILTER.add("提现-快速提现");
        ALIPAY_COMMODITY_FILTER.add("转出到网商银行");
        ALIPAY_COMMODITY_FILTER.add("网商银行转入");
        ALIPAY_COMMODITY_FILTER.add("余额宝-自动转入");
        ALIPAY_COMMODITY_FILTER.add("余额宝-单次转入");
        ALIPAY_COMMODITY_FILTER.add("余额宝-转出到银行卡");

        ICBC_SUMMARY_FILTER.add("ATM取款");
        ICBC_SUMMARY_FILTER.add("余额宝提现");
        ICBC_SUMMARY_FILTER.add("QQ钱包提现 DF");
        ICBC_SUMMARY_FILTER.add("微信零钱提现 DF");
        ICBC_SUMMARY_FILTER.add("DF提现");
        ICBC_SUMMARY_FILTER.add("校园");
    }

}
