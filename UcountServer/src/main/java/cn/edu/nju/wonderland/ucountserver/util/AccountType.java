package cn.edu.nju.wonderland.ucountserver.util;

public enum AccountType {
    ALIPAY      ("支付宝"),
    ICBC_CARD   ("工行卡"),
    SCHOOL_CARD ("校园卡"),
    MANUAL      ("手动"),

    ;
    String accountType;

    AccountType(String accountType) {
        this.accountType = accountType;
    }

    @Override
    public String toString() {
        return accountType;
    }
}
