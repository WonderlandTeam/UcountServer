package cn.edu.nju.wonderland.ucountserver.service;

import cn.edu.nju.wonderland.ucountserver.vo.BalanceSheetVO;
import cn.edu.nju.wonderland.ucountserver.vo.IncomeStatementVO;

public interface StatementService {

    BalanceSheetVO getBalanceSheet(String username, String beginDate, String endDate);

    IncomeStatementVO getIncomeStatement(String username, String beginDate, String endDate);

}
