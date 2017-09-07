package cn.edu.nju.wonderland.ucountserver.util;

/**
 * 收入／支出类型枚举类
 */
public enum BillType {

    /************ 收入类型 ************/

    SALARY                  ("工资收入"),
    MANAGEMENT_INCOME       ("理财收入"),
    ALIMONY                 ("家庭补助"),
    OTHER_INCOME            ("其他收入"),


    /************ 支出类型 ************/

    /* 生活必需 */
    COMMODITY               ("日用品"),
    UTILITIES               ("水电费"),
    COMMUNICATION           ("通讯和网费"),
    DIET                    ("饮食"),
    ELECTRONIC              ("电子设备"),
    TRAFFIC                 ("交通"),

    /* 妆服费 */
    CLOTHING                ("衣帽鞋包"),
    CREAM                   ("护肤品"),
    COSMETICS               ("彩妆"),
    JEWELRY                 ("首饰"),

    /* 学习 */
    TRAINING                ("培训、考证费用"),
    BOOK                    ("书籍"),
    STATIONERY              ("文具"),
    PRINT                   ("打印资料"),
    ACTIVITY                ("组织活动"),

    /* 娱乐 */
    ENTERTAINMENT           ("娱乐"),
    /* 理财 */
    MANAGEMENT_EXPENDITURE  ("理财支出"),

    /* 捐赠 */
    DONATION                ("捐款"),
    OTHER_DONATION          ("其他捐赠"),

    /* 其他支出 */
    OTHER_EXPENDITURE       ("其他支出"),
    ;

    public String billType;

    BillType(String billType) {
        this.billType = billType;
    }

    /**
     * 字符串转枚举类型
     */
    public static BillType stringToBillType(String string) {
        for (BillType type : BillType.values()) {
            if (string.equals(type.billType)) {
                return type;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return billType;
    }

}
