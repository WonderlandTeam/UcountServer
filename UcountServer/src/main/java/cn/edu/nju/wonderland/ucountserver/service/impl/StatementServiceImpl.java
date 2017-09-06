package cn.edu.nju.wonderland.ucountserver.service.impl;

import cn.edu.nju.wonderland.ucountserver.entity.Alipay;
import cn.edu.nju.wonderland.ucountserver.entity.IcbcCard;
import cn.edu.nju.wonderland.ucountserver.entity.ManualBilling;
import cn.edu.nju.wonderland.ucountserver.entity.SchoolCard;
import cn.edu.nju.wonderland.ucountserver.repository.*;
import cn.edu.nju.wonderland.ucountserver.service.StatementService;
import cn.edu.nju.wonderland.ucountserver.vo.BalanceSheetVO;
import cn.edu.nju.wonderland.ucountserver.vo.BillInfoVO;
import cn.edu.nju.wonderland.ucountserver.vo.IncomeStatementVO;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class StatementServiceImpl implements StatementService {

    private AccountRepository accountRepository;
    private AlipayRepository alipayRepository;
    private IcbcCardRepository icbcCardRepository;
    private SchoolCardRepository schoolCardRepository;
    private ManualBillingRepository manualBillingRepository;

    public StatementServiceImpl(AccountRepository accountRepository, AlipayRepository alipayRepository, IcbcCardRepository icbcCardRepository, SchoolCardRepository schoolCardRepository, ManualBillingRepository manualBillingRepository) {
        this.accountRepository = accountRepository;
        this.alipayRepository = alipayRepository;
        this.icbcCardRepository = icbcCardRepository;
        this.schoolCardRepository = schoolCardRepository;
        this.manualBillingRepository = manualBillingRepository;
    }

    private void countAlipay(IncomeStatementVO vo, Alipay alipay) {
        
    }

    private void countICBC(IncomeStatementVO vo, IcbcCard icbcCard) {

    }

    private void countSchoolCard(IncomeStatementVO vo, SchoolCard schoolCard) {

    }

    private void countManual(IncomeStatementVO vo, ManualBilling manualBilling) {
        
    }

    @Override
    public BalanceSheetVO getBalanceSheet(String username, String date) {
        return null;
    }

    @Override
    public IncomeStatementVO getIncomeStatement(String username, String beginDate, String endDate) {
        List<Alipay> alipayList = alipayRepository.findByPayTimeBetween(Timestamp.valueOf(beginDate), Timestamp.valueOf(endDate));

        return null;
    }

    @Override
    public List<BillInfoVO> getCashFlows(String username, String beginDate, String endDate) {
        return null;
    }


}
