package cn.edu.nju.wonderland.ucountserver.service;

import cn.edu.nju.wonderland.ucountserver.vo.BalanceSheetVO;
import cn.edu.nju.wonderland.ucountserver.vo.IncomeStatementVO;

public interface StatementService {

    /**
     * 生成资产负债表
     * @param username          用户名
     * @param date              日期
     * @return                  资产负债表vo
     */
    BalanceSheetVO getBalanceSheet(String username, String date);

    /**
     * 生成利润表
     * @param username          用户名
     * @param beginDate         开始日期
     * @param endDate           结束日期
     * @return                  利润表vo
     */
    IncomeStatementVO getIncomeStatement(String username, String beginDate, String endDate);

}
