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
import java.util.ArrayList;
import java.util.List;

import static cn.edu.nju.wonderland.ucountserver.util.AutoAccountType.ALIPAY;
import static cn.edu.nju.wonderland.ucountserver.util.AutoAccountType.ICBC_CARD;
import static cn.edu.nju.wonderland.ucountserver.util.AutoAccountType.SCHOOL_CARD;

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
            // 4：枚举类收入类型数量
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

        schoolCardList.forEach(s -> countIncomeStatement(vo, s.getConsumeType(), Math.abs(s.getIncomeExpenditure())));

        List<ManualBilling> manualBillingList = manualBillingRepository.findByUsernameAndTimeBetween(username, start, end);
        manualBillingList.forEach(m -> countIncomeStatement(vo, m.getConsumeType(), m.getIncomeExpenditure()));

        // 计算部分支出分类合计
        vo.necessityTotal = vo.commodity + vo.utilities + vo.communication + vo.diet + vo.electronic + vo.traffic;
        vo.adornTotal = vo.clothing + vo.cream + vo.cosmetics + vo.jewelry;
        vo.learningTotal = vo.training + vo.book + vo.stationery + vo.print + vo.activity;
        vo.donationTotal = vo.donation + vo.otherDonation;

        return vo;
    }

    private CashFlowItemVO alipayToCashFlowItem(Alipay alipay) {
        CashFlowItemVO vo = new CashFlowItemVO();

        vo.accountType = ALIPAY.accountType;
        vo.cardId = alipay.getCardId();
        vo.billType = alipay.getConsumeType();
        vo.money = alipay.getMoney();
        vo.time = DateHelper.toTimeByTimeStamp(alipay.getCreateTime());

        return vo;
    }

    private CashFlowItemVO icbcToCashFlowItem(IcbcCard icbcCard) {
        CashFlowItemVO vo = new CashFlowItemVO();

        vo.accountType = ICBC_CARD.accountType;
        vo.cardId = icbcCard.getCardId();
        vo.billType = icbcCard.getConsumeType();
        vo.money = icbcCard.getAccountAmountExpense() + icbcCard.getAccountAmountIncome();
        vo.time = DateHelper.toTimeByTimeStamp(icbcCard.getTradeDate());

        return vo;
    }

    private CashFlowItemVO schoolCardToCashFlowItem(SchoolCard schoolCard) {
        CashFlowItemVO vo = new CashFlowItemVO();

        vo.accountType = SCHOOL_CARD.accountType;
        vo.cardId = schoolCard.getCardId();
        vo.billType = schoolCard.getConsumeType();
        vo.money = Math.abs(schoolCard.getIncomeExpenditure());
        vo.time = DateHelper.toTimeByTimeStamp(schoolCard.getTime());

        return vo;
    }

    private CashFlowItemVO manualToCashFlowItem(ManualBilling manualBilling) {
        CashFlowItemVO vo = new CashFlowItemVO();

        vo.accountType = manualBilling.getCardType();
        vo.cardId = manualBilling.getCardId();
        vo.billType = manualBilling.getConsumeType();
        vo.money = manualBilling.getIncomeExpenditure();
        vo.time = DateHelper.toTimeByTimeStamp(manualBilling.getTime());

        return vo;
    }

    @Override
    public List<CashFlowItemVO> getCashFlows(String username, String beginDate, String endDate) {
        List<CashFlowItemVO> cashFlows = new ArrayList<>();

        Timestamp start = DateHelper.toTimestampByDate(beginDate);
        Timestamp end = DateHelper.toTimestampByDate(endDate);

        alipayRepository
                .findByUsernameAndCreateTimeBetween(username, start, end)
                .forEach(a -> cashFlows.add(alipayToCashFlowItem(a)));

        icbcCardRepository
                .findByUsernameAndTradeDateBetween(username, start, end)
                .forEach(i -> cashFlows.add(icbcToCashFlowItem(i)));

        schoolCardRepository
                .findByUsernameAndTimeBetween(username, start, end)
                .forEach(s -> cashFlows.add(schoolCardToCashFlowItem(s)));

        manualBillingRepository
                .findByUsernameAndTimeBetween(username, start, end)
                .forEach(m -> cashFlows.add(manualToCashFlowItem(m)));

        // 根据时间逆序排序
        cashFlows.sort(((o1, o2) -> o2.time.compareTo(o1.time)));

        return cashFlows;
    }

}
