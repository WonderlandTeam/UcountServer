package cn.edu.nju.wonderland.ucountserver.service.impl;

import cn.edu.nju.wonderland.ucountserver.repository.AccountRepository;
import cn.edu.nju.wonderland.ucountserver.repository.AlipayRepository;
import cn.edu.nju.wonderland.ucountserver.repository.IcbcCardRepository;
import cn.edu.nju.wonderland.ucountserver.repository.ManualBillingRepository;
import cn.edu.nju.wonderland.ucountserver.service.StatementService;
import cn.edu.nju.wonderland.ucountserver.vo.BalanceSheetVO;
import cn.edu.nju.wonderland.ucountserver.vo.BillInfoVO;
import cn.edu.nju.wonderland.ucountserver.vo.IncomeStatementVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatementServiceImpl implements StatementService {

    private AccountRepository accountRepository;
    private AlipayRepository alipayRepository;
    private IcbcCardRepository icbcCardRepository;
    private ManualBillingRepository manualBillingRepository;

    @Autowired
    public StatementServiceImpl(AccountRepository accountRepository, AlipayRepository alipayRepository, IcbcCardRepository icbcCardRepository, ManualBillingRepository manualBillingRepository) {
        this.accountRepository = accountRepository;
        this.alipayRepository = alipayRepository;
        this.icbcCardRepository = icbcCardRepository;
        this.manualBillingRepository = manualBillingRepository;
    }

    @Override
    public BalanceSheetVO getBalanceSheet(String username, String date) {
        return null;
    }

    @Override
    public IncomeStatementVO getIncomeStatement(String username, String beginDate, String endDate) {
        return null;
    }

    @Override
    public List<BillInfoVO> getCashFlows(String username, String beginDate, String endDate) {
        return null;
    }


}
