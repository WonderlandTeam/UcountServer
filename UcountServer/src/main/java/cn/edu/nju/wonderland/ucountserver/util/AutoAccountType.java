package cn.edu.nju.wonderland.ucountserver.util;

/**
 * 自动记账类型
 */
public enum AutoAccountType {
    ALIPAY      ("支付宝"),
    ICBC_CARD   ("工行卡"),
    SCHOOL_CARD ("校园卡"),
    ;

    public String accountType;

    AutoAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Override
    public String toString() {
        return accountType;
    }
}
