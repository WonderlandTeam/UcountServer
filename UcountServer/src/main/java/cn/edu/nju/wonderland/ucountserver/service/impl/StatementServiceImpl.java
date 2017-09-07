package cn.edu.nju.wonderland.ucountserver.service.impl;

import cn.edu.nju.wonderland.ucountserver.entity.Alipay;
import cn.edu.nju.wonderland.ucountserver.entity.IcbcCard;
import cn.edu.nju.wonderland.ucountserver.entity.ManualBilling;
import cn.edu.nju.wonderland.ucountserver.entity.SchoolCard;
import cn.edu.nju.wonderland.ucountserver.repository.*;
import cn.edu.nju.wonderland.ucountserver.service.StatementService;
import cn.edu.nju.wonderland.ucountserver.util.BillType;
import cn.edu.nju.wonderland.ucountserver.util.DateHelper;
import cn.edu.nju.wonderland.ucountserver.util.StringUtil;
import cn.edu.nju.wonderland.ucountserver.vo.BalanceSheetVO;
import cn.edu.nju.wonderland.ucountserver.vo.CashFlowItemVO;
import cn.edu.nju.wonderland.ucountserver.vo.IncomeStatementVO;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.List;

import static cn.edu.nju.wonderland.ucountserver.util.AutoAccountType.ALIPAY;
import static cn.edu.nju.wonderland.ucountserver.util.AutoAccountType.ICBC_CARD;

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

    /**
     * 利润表表项统计
     */
    private void countIncomeStatement(IncomeStatementVO vo, String consumeType, double value) {
        BillType billType = BillType.stringToBillType(consumeType);
        if (billType == null) {
            return;
        }
        Field field = IncomeStatementVO.getFieldByFieldName(StringUtil.lineToHump(billType.name()));
        if (field != null) {
            try {
                field.set(vo, (double) field.get(vo) + value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (billType.ordinal() < 4) {
                vo.totalIncome += value;
            } else {
                vo.totalExpenditure += value;
            }
        }
    }

    @Override
    public BalanceSheetVO getBalanceSheet(String username, String date) {
        // TODO
        return null;
    }

    @Override
    public IncomeStatementVO getIncomeStatement(String username, String beginDate, String endDate) {
        IncomeStatementVO vo = new IncomeStatementVO();
        Timestamp start = DateHelper.toTimestampByDate(beginDate);
        Timestamp end = DateHelper.toTimestampByDate(endDate);

        List<Alipay> alipayList = alipayRepository.findByUsernameAndCreateTimeBetween(username, start, end);
        alipayList.forEach(a -> countIncomeStatement(vo, a.getConsumeType(), a.getMoney()));

        List<IcbcCard> icbcCardList = icbcCardRepository.findByUsernameAndTradeDateBetween(username, start, end);
        icbcCardList.forEach(i -> countIncomeStatement(vo, i.getConsumeType(), i.getAccountAmountIncome() + i.getAccountAmountExpense()));

        List<SchoolCard> schoolCardList = schoolCardRepository.findByUsernameAndTimeBetween(username, start, end);

//        schoolCardList.forEach(s -> countIncomeStatement(vo, s.getConsumeType(), Math.abs(s.getIncomeExpenditure())));

        List<ManualBilling> manualBillingList = manualBillingRepository.findByUsernameAndTimeBetween(username, start, end);
        manualBillingList.forEach(m -> countIncomeStatement(vo, m.getConsumeType(), m.getIncomeExpenditure()));

        return vo;
    }

    private CashFlowItemVO alipayToCashFlowItem(Alipay alipay) {
        CashFlowItemVO vo = new CashFlowItemVO();

        vo.accountType = ALIPAY.accountType;
        vo.cardId = alipay.getCardId();
        vo.billType = alipay.getConsumeType();
        vo.money = alipay.getMoney();
        vo.time = DateHelper.toTimeByTimeStamp(alipay.getPayTime());

        return vo;
    }

    private CashFlowItemVO icbcToCashFlowItem(IcbcCard icbcCard) {
        CashFlowItemVO vo = new CashFlowItemVO();

        vo.accountType = ICBC_CARD.accountType;
        vo.cardId = icbcCard.getCardId();
        vo.billType = icbcCard.getConsumeType();

        // TODO
        return null;
    }

    private CashFlowItemVO schoolCardToCashFlowItem(SchoolCard schoolCard) {
        CashFlowItemVO vo = new CashFlowItemVO();
        // TODO
        return null;
    }

    private CashFlowItemVO manualToCashFlowItem(ManualBilling manualBilling) {
        CashFlowItemVO vo = new CashFlowItemVO();
        // TODO
        return null;
    }

    @Override
    public List<CashFlowItemVO> getCashFlows(String username, String beginDate, String endDate) {
        // TODO
        return null;
    }

}
