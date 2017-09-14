package cn.edu.nju.wonderland.ucountserver.service.impl;

import cn.edu.nju.wonderland.ucountserver.entity.*;
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
import java.util.Random;

import static cn.edu.nju.wonderland.ucountserver.util.AutoAccountType.*;
import static cn.edu.nju.wonderland.ucountserver.util.BillFilter.ALIPAY_COMMODITY_FILTER;
import static cn.edu.nju.wonderland.ucountserver.util.BillFilter.ICBC_SUMMARY_FILTER;
import static cn.edu.nju.wonderland.ucountserver.util.BillType.OTHER_INCOME;
import static cn.edu.nju.wonderland.ucountserver.util.BillType.stringToBillType;

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

    // 成本 键名
    public static final String COST = "cost";
    // 市价 键名
    public static final String MARKET = "market";

    @Override
    public BalanceSheetVO getBalanceSheet(String username, String date) {
        BalanceSheetVO vo = new BalanceSheetVO();

        if (date == null) {
            date = DateHelper.getTodayDate();
        }
        Timestamp timestamp = DateHelper.toTimestampByDate(date);

        List<Account> accounts = accountRepository.findByUsername(username);

        double cash = 0;
        double deposit = 0;
        double creditCardLiabilities = 0;
        double personalLiabilities = 0;
        for (Account account : accounts) {
            String cardType = account.getCardType();
            if (cardType.equals(ALIPAY.accountType)) {
                List<Alipay> balanceList = alipayRepository.getBalance(account.getCardId(), timestamp);
                if (balanceList.size() == 0) {
                    continue;
                }
                double balance = balanceList.get(0).getBalance();
                if (balance > 0) {
                    deposit += balance;
                } else {
                    // 自用负债
                    personalLiabilities -= balance;
                }
            } else if (cardType.equals(ICBC_CARD.accountType)) {
                List<IcbcCard> balanceList = icbcCardRepository.getBalance(account.getCardId(), timestamp);
                if (balanceList.size() == 0) {
                    continue;
                }
                double balance = balanceList.get(0).getBalance();
                if (balance > 0) {
                    deposit += balance;
                } else {
                    // 信用卡负债
                    creditCardLiabilities -= balance;
                }
            } else if (cardType.equals(SCHOOL_CARD.accountType)) {
                List<SchoolCard> balanceList = schoolCardRepository.getBalance(account.getCardId(), timestamp);
                if (balanceList.size() > 0) {
                    deposit += balanceList.get(0).getBalance();
                }
            } else {
                List<ManualBilling> balanceList = manualBillingRepository.getBalance(username, account.getCardType(), account.getCardId(), timestamp);
                if (balanceList.size() == 0) {
                    continue;
                }
                double balance = balanceList.get(0).getBalance();
                if (balance > 0) {
                    // 现金
                    cash += balance;
                } else {
                    personalLiabilities -= balance;
                }
            }

        }
        // 资产项
        vo.cash.put(COST, cash);
        vo.cash.put(MARKET, cash);
        vo.deposit.put(COST, deposit);
        vo.deposit.put(MARKET, deposit);
        vo.currentAssets.put(COST, cash + deposit);
        vo.currentAssets.put(MARKET, cash + deposit);
        // TODO
        double mobilePhone = new Random().nextInt(3500) + 3000.0;
        double computer = new Random().nextInt(4000) + 6000.0;
        vo.mobilePhone.put(COST, mobilePhone);
        vo.mobilePhone.put(MARKET, mobilePhone);
        vo.computer.put(COST, computer);
        vo.computer.put(MARKET, computer);
        vo.totalAssets.put(COST, cash + deposit + mobilePhone + computer);
        vo.totalAssets.put(MARKET, cash + deposit + mobilePhone + computer);
        // 负债项
        vo.creditCardLiabilities = creditCardLiabilities;
        vo.personalLiabilities = personalLiabilities;
        vo.totalLiabilities = creditCardLiabilities + personalLiabilities;
        // 净值项
        double currentNetValue = vo.currentAssets.get(COST) - vo.consumerLiabilities;
        vo.currentNetValue.put(COST, currentNetValue);
        vo.currentNetValue.put(MARKET, currentNetValue);
        double investmentNetValue = vo.investmentAssets.get(COST) - vo.investmentLiabilities;
        vo.investmentNetValue.put(COST, investmentNetValue);
        vo.investmentNetValue.put(MARKET, investmentNetValue);
        double personalNetValue = vo.personalAssets.get(COST) - personalLiabilities;
        vo.personalNetValue.put(COST, personalNetValue);
        vo.personalNetValue.put(MARKET, personalNetValue);
        vo.totalNetValue.put(COST, currentNetValue + investmentNetValue + personalNetValue);
        vo.totalNetValue.put(MARKET, currentNetValue + investmentNetValue + personalNetValue);
        return vo;
    }

    /**
     * 利润表表项统计
     */
    private void countIncomeStatement(IncomeStatementVO vo, String consumeType, double value) {
        BillType billType = stringToBillType(consumeType);
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
    public IncomeStatementVO getIncomeStatement(String username, String beginDate, String endDate) {
        IncomeStatementVO vo = new IncomeStatementVO();

        if (endDate == null) {
            endDate = DateHelper.getTodayDate();
        }

        Timestamp start = DateHelper.toTimestampByDate(beginDate);
        Timestamp end = DateHelper.toTimestampByDate(endDate);

        List<Alipay> alipayList = alipayRepository.findByUsernameAndCreateTimeBetween(username, start, end);
        alipayList.forEach(a -> {
            // 支付宝账目过滤
            if (!ALIPAY_COMMODITY_FILTER.contains(a.getCommodity())) {
                countIncomeStatement(vo, a.getConsumeType(), a.getMoney());
            }
        });

        List<IcbcCard> icbcCardList = icbcCardRepository.findByUsernameAndTradeDateBetween(username, start, end);
        icbcCardList.forEach(i -> {
            // 工行卡账目过滤
            if (!ICBC_SUMMARY_FILTER.contains(i.getSummary())) {
                countIncomeStatement(vo, i.getConsumeType(), i.getAccountAmountIncome() + i.getAccountAmountExpense());
            }
        });

        List<SchoolCard> schoolCardList = schoolCardRepository.findByUsernameAndTimeBetween(username, start, end);

        schoolCardList.forEach(s -> {
            // 过滤校园卡收入
            if (!s.getConsumeType().equals(OTHER_INCOME.billType)) {
                countIncomeStatement(vo, s.getConsumeType(), Math.abs(s.getIncomeExpenditure()));
            }
        });

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

        if (endDate == null) {
            endDate = DateHelper.getTodayDate();
        }

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
