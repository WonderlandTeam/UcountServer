package cn.edu.nju.wonderland.ucountserver.service.impl;

import cn.edu.nju.wonderland.ucountserver.service.StatementService;
import cn.edu.nju.wonderland.ucountserver.vo.BalanceSheetVO;
import cn.edu.nju.wonderland.ucountserver.vo.IncomeStatementVO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class StatementServiceImpl implements StatementService {

    @Override
    public BalanceSheetVO getBalanceSheet(String username, String date) {
        return null;
    }

    @Override
    public IncomeStatementVO getIncomeStatement(String username, String beginDate, String endDate) {
        return null;
    }

}
