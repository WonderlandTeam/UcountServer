package cn.edu.nju.wonderland.ucountserver.service.impl;

import cn.edu.nju.wonderland.ucountserver.entity.*;
import cn.edu.nju.wonderland.ucountserver.exception.ResourceNotFoundException;
import cn.edu.nju.wonderland.ucountserver.repository.*;
import cn.edu.nju.wonderland.ucountserver.service.BillService;
import cn.edu.nju.wonderland.ucountserver.util.DateHelper;
import cn.edu.nju.wonderland.ucountserver.vo.BillAddVO;
import cn.edu.nju.wonderland.ucountserver.vo.BillInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.edu.nju.wonderland.ucountserver.util.AccountType.*;
import static cn.edu.nju.wonderland.ucountserver.util.DateHelper.DATE_TIME_FORMATTER;

@Service
public class BillServiceImpl implements BillService {

    private IcbcCardRepository icbcCardRepository;
    private SchoolCardRepository schoolCardRepository;
    private AlipayRepository alipayRepository;
    private ManualBillingRepository manualBillingRepository;
    private AccountRepository accountRepository;

    @Autowired
    public BillServiceImpl(IcbcCardRepository icbcCardRepository, SchoolCardRepository schoolCardRepository, AlipayRepository alipayRepository, ManualBillingRepository manualBillingRepository, AccountRepository accountRepository) {
        this.icbcCardRepository = icbcCardRepository;
        this.schoolCardRepository = schoolCardRepository;
        this.alipayRepository = alipayRepository;
        this.manualBillingRepository = manualBillingRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * 支付宝账目记录转vo
     */
    private BillInfoVO alipayToVO(Alipay alipay) {
        BillInfoVO vo = new BillInfoVO();
        vo.type = alipay.getConsumeType();
        vo.amount = alipay.getMoney();
        vo.trader = alipay.getTrader();
        vo.time = DateHelper.toTimeByTimeStamp(alipay.getPayTime());
        return vo;
    }

    /**
     * 工行卡账目记录转vo
     */
    private BillInfoVO icbcCardToVO(IcbcCard icbcCard) {
        BillInfoVO vo = new BillInfoVO();
        vo.type = icbcCard.getConsumeType();
        vo.amount = icbcCard.getAccountAmountIncome() + icbcCard.getAccountAmountExpense();
        vo.trader = icbcCard.getLocation();
        vo.time = DateHelper.toTimeByTimeStamp(icbcCard.getTradeDate());
        return vo;
    }

    /**
     * 校园卡账目记录转vo
     */
    private BillInfoVO schoolCardToVO(SchoolCard schoolCard) {
        BillInfoVO vo = new BillInfoVO();
        vo.type = schoolCard.getConsumeType();
        vo.amount = Math.abs(schoolCard.getIncomeExpenditure());
        vo.time = DateHelper.toTimeByTimeStamp(schoolCard.getTime());
        vo.trader = schoolCard.getLocation();
        return vo;
    }

    /**
     * 手动记账账目记录转vo
     */
    private BillInfoVO manualBillingToVO(ManualBilling manualBilling) {
        BillInfoVO vo = new BillInfoVO();
        vo.type = manualBilling.getConsumeType();
        vo.amount = manualBilling.getIncomeExpenditure();
        vo.time = DateHelper.toTimeByTimeStamp(manualBilling.getTime());
        vo.trader = manualBilling.getCommodity();
        return vo;
    }

    @Override
    public BillInfoVO getBill(Long accountId, Long billId) {
        BillInfoVO billInfoVO = new BillInfoVO();
        Account account = accountRepository.findOne(accountId);
        if (account == null) {
            throw new ResourceNotFoundException("账户不存在");
        }
        String cardId = account.getCardId();
        String accountType = account.getCardType();

        if (accountType.equals(ALIPAY.accountType)) {                    // 支付宝
            Alipay alipay = alipayRepository.findByIdAndCardId(billId, cardId);
            if (alipay == null) {
                throw new ResourceNotFoundException("账目不存在");
            }
            billInfoVO = alipayToVO(alipay);
        } else if (accountType.equals(ICBC_CARD.accountType)) {          // 工行卡
            IcbcCard icbcCard = icbcCardRepository.findByIdAndCardId(billId, cardId);
            if (icbcCard == null) {
                throw new ResourceNotFoundException("账目不存在");
            }
            billInfoVO = icbcCardToVO(icbcCard);
        } else if (accountType.equals(SCHOOL_CARD.accountType)) {        // 校园卡
            SchoolCard schoolCard = schoolCardRepository.findByIdAndCardId(billId, cardId);
            if (schoolCard == null) {
                throw new ResourceNotFoundException("账目不存在");
            }
            billInfoVO = schoolCardToVO(schoolCard);
        } else if (accountType.equals(MANUAL.accountType)) {             // 手动记账
            ManualBilling manualBilling = manualBillingRepository.findByIdAndCardId(billId, cardId);
            if (manualBilling == null) {
                throw new ResourceNotFoundException("账目不存在");
            }
            billInfoVO = manualBillingToVO(manualBilling);
        }

        return billInfoVO;
    }

    @Override
    public List<BillInfoVO> getBillsByAccount(Long accountId, Pageable pageable) {
        List<BillInfoVO> bills = new ArrayList<>();
        Account account = accountRepository.findOne(accountId);
        if (account == null) {
            throw new ResourceNotFoundException("账户不存在");
        }
        String cardId = account.getCardId();
        String accountType = account.getCardType();
        if (accountType.equals(ALIPAY.accountType)) {
            // 支付宝
            Page<Alipay> alipayPage = alipayRepository.findByCardId(cardId, pageable);
            bills = alipayPage.map(this::alipayToVO).getContent();
        } else if (accountType.equals(ICBC_CARD.accountType)) {
            // 工行卡
            Page<IcbcCard> icbcCardPage = icbcCardRepository.findByCardId(cardId, pageable);
            bills = icbcCardPage.map(this::icbcCardToVO).getContent();
        } else if (accountType.equals(SCHOOL_CARD.accountType)) {
            // 校园卡
            Page<SchoolCard> schoolCardPage = schoolCardRepository.findByCardId(cardId, pageable);
            bills = schoolCardPage.map(this::schoolCardToVO).getContent();
        } else if (accountType.equals(MANUAL.accountType)) {
            // 手动记账
            Page<ManualBilling> manualBillingPage = manualBillingRepository.findByCardId(cardId, pageable);
            bills = manualBillingPage.map(this::manualBillingToVO).getContent();
        }
        return bills;
    }

    @Override
    public List<BillInfoVO> getMonthBillsByUser(String username, String month) {
        // TODO 月份处理

        List<BillInfoVO> billInfoVOList = new ArrayList<>();
        Map<Integer, List<IcbcCard>> icbcCardmap = new HashMap<>();
        /* 工行卡的表*/
        List<SchoolCard> schoolCardList = schoolCardRepository.findByUsername(username, null);
        List<Alipay> alipayList = alipayRepository.findByUsername(username, null);
        List<Account> accounts = accountRepository.findByUsername(username);
        for (int i = 0; i < accounts.size(); i++) {
            if (!accounts.get(i).getCardType().contains("银行卡")) {
                accounts.remove(i);//选出银行卡
            }
        }
        for (int i = 0; i < accounts.size(); i++) {
            icbcCardmap.put(i, icbcCardRepository.findByCardId(accounts.get(i).getCardId(), null).getContent());
        }
        for (int k = 0; k < accounts.size(); k++) {
            List<IcbcCard> icbcCardList = icbcCardmap.get(k);
            for (int i = 0; i < icbcCardList.size(); i++) {
                for (int j = 0; j < alipayList.size(); j++) {
                    if (icbcCardList.get(i).getAccountAmountExpense() == alipayList.get(j).getMoney()
                            && icbcCardList.get(i).getTradeDate() == alipayList.get(j).getPayTime()) {
                        if (alipayList.get(j).getCommodity().equals("充值-普通充值")) {
                            alipayList.remove(j);
                            icbcCardList.remove(i);//支付宝从卡转账到余额，
                        } else {
                            alipayRepository.delete(alipayList.get(j));
                            alipayList.remove(j);//支付宝用银行卡消费
                        }
                    } else if ((alipayList.get(j).getMoney() - icbcCardList.get(i).getAccountAmountIncome() < 10)
                            && (icbcCardList.get(i).getTradeDate().getTime() - alipayList.get(j).getPayTime().getTime() <= (2 * 60 * 1000))
                            && (alipayList.get(j).getCommodity().equals("提现-快速提现"))) {
                        //支付宝余额提现到银行卡
                        alipayList.remove(j);
                        icbcCardList.remove(i);
                        BillInfoVO billInfoVO = new BillInfoVO();
                        billInfoVO.trader = "支付宝提现";
                        billInfoVO.amount = alipayList.get(j).getMoney() - icbcCardList.get(i).getAccountAmountIncome();
                        billInfoVO.time = DateHelper.toTimeByTimeStamp(alipayList.get(i).getPayTime());
                        billInfoVO.type = "支付宝提现";
                        billInfoVOList.add(billInfoVO);
                    }
                }
                for (int j = 0; j < schoolCardList.size(); j++) {
                    if (icbcCardList.get(i).getAccountAmountExpense() == schoolCardList.get(j).getIncomeExpenditure() &&
                            icbcCardList.get(i).getTradeDate() == schoolCardList.get(j).getTime()) {
                        schoolCardList.remove(j);
                        icbcCardList.remove(i);//银行卡转账到校园卡
                    }
                }
            }
        }
        for (int k = 0; k < accounts.size(); k++) {
            List<IcbcCard> icbcCardList = icbcCardmap.get(k);
            for (int i = 0; i < icbcCardList.size(); i++) {
                BillInfoVO billInfoVO = new BillInfoVO();
                billInfoVO.trader = icbcCardList.get(i).getOtherAccount();
                billInfoVO.time = DateHelper.toTimeByTimeStamp(icbcCardList.get(i).getTradeDate());
                if (icbcCardList.get(i).getAccountAmountIncome() > 0) {
                    billInfoVO.type = "收入";
                    billInfoVO.amount = icbcCardList.get(i).getAccountAmountIncome();
                } else {
                    billInfoVO.type = "支出";
                    billInfoVO.amount = icbcCardList.get(i).getAccountAmountExpense();
                }
                billInfoVOList.add(billInfoVO);
            }
        }
        for (int i = 0; i < schoolCardList.size(); i++) {
            BillInfoVO billInfoVO = new BillInfoVO();
            billInfoVO.time = DateHelper.toTimeByTimeStamp(schoolCardList.get(i).getTime());
            billInfoVO.trader = schoolCardList.get(i).getLocation();
            billInfoVO.amount = schoolCardList.get(i).getIncomeExpenditure();
            if (billInfoVO.amount > 0) {
                billInfoVO.type = "收入";
            } else {
                billInfoVO.type = "支出";
            }
            billInfoVOList.add(billInfoVO);
        }
        for (int i = 0; i < alipayList.size(); i++) {
            BillInfoVO billInfoVO = new BillInfoVO();
            billInfoVO.amount = alipayList.get(i).getMoney();
            billInfoVO.type = alipayList.get(i).getIncomeExpenditureType();
            billInfoVO.time = DateHelper.toTimeByTimeStamp(alipayList.get(i).getCreateTime());
            billInfoVO.trader = alipayList.get(i).getTrader();
            billInfoVOList.add(billInfoVO);
        }
        return billInfoVOList;
    }

    @Override
    public Long addBillManually(Long accountId, BillAddVO billAddVO) {
        Account account = accountRepository.findOne(accountId);
        if (account == null) {
            throw new ResourceNotFoundException("账户不存在");
        }
        ManualBilling manualBilling = new ManualBilling();
        manualBilling.setCardId(String.valueOf(accountId));
        manualBilling.setCardType(billAddVO.cardType);
        manualBilling.setCommodity(billAddVO.commodity);
        manualBilling.setConsumeType(billAddVO.consumeType);
        manualBilling.setIncomeExpenditure(billAddVO.incomeExpenditure);
        manualBilling.setTime(DateHelper.toTimestampByMonth(billAddVO.time));
        // TODO 判断用户是否存在
        manualBilling.setUsername(billAddVO.username);
        manualBilling.setRemark(billAddVO.remark);
        return manualBillingRepository.save(manualBilling).getId();
    }

    @Override
    public List<BillInfoVO> addBillAutomatically(Long accountId) {
        // TODO 模拟数据生成
        return null;
    }

    @Override
    public void deleteBill(Long accountId, Long billId) {
        Account account = accountRepository.findOne(accountId);
        if (account == null) {
            throw new ResourceNotFoundException("账户不存在");
        }
        String cardId = account.getCardId();
        String accountType = account.getCardType();
        if (accountType.equals(ALIPAY.accountType)) {
            Alipay alipay = alipayRepository.findByIdAndCardId(billId, cardId);
            if (alipay == null) {
                throw new ResourceNotFoundException("账目不存在");
            }
            alipayRepository.delete(alipay);
        } else if (accountType.equals(ICBC_CARD.accountType)) {
            IcbcCard icbcCard = icbcCardRepository.findByIdAndCardId(billId, cardId);
            if (icbcCard == null) {
                throw new ResourceNotFoundException("账目不存在");
            }
            icbcCardRepository.delete(icbcCard);
        } else if (accountType.equals(SCHOOL_CARD.accountType)) {
            SchoolCard schoolCard = schoolCardRepository.findByIdAndCardId(billId, cardId);
            if (schoolCard == null) {
                throw new ResourceNotFoundException("账目不存在");
            }
            schoolCardRepository.delete(schoolCard);
        } else if (accountType.equals(MANUAL.accountType)) {
            ManualBilling manualBilling = manualBillingRepository.findByIdAndCardId(billId, cardId);
            if (manualBilling == null) {
                throw new ResourceNotFoundException("账目不存在");
            }
            manualBillingRepository.delete(manualBilling);
        }

    }

    @Override
    public double getConsumedMoneyByTypeAndTime(String username, String consumeType, String time) {

        LocalDateTime startDate = LocalDateTime.parse(time, DATE_TIME_FORMATTER);
        LocalDateTime endDate = startDate.plusMonths(1);

        Timestamp start = Timestamp.valueOf(startDate);
        Timestamp end = Timestamp.valueOf(endDate);
        double result = 0;
//        TotalAccountVO totalAccountVO = new TotalAccountVO();

        List<Alipay> alipays = alipayRepository.getMouthBill(username, start, end);
        for (int i = 0; i < alipays.size(); i++) {
            if (alipays.get(i).getConsumeType().equals(consumeType)) {
                result += alipays.get(i).getMoney();
            }
        }
        List<SchoolCard> schoolCards = schoolCardRepository.getMouthBill(username, start, end);
        for (int i = 0; i < schoolCards.size(); i++) {
            if (schoolCards.get(i).getConsumeType().equals(consumeType)) {
                if (schoolCards.get(i).getIncomeExpenditure() > 0) {
                    result += schoolCards.get(i).getIncomeExpenditure();
                } else {
                    result -= schoolCards.get(i).getIncomeExpenditure();
                }
            }
        }
        List<IcbcCard> icbcCards = icbcCardRepository.getMouthBill(username, start, end);
        for (int i = 0; i < icbcCards.size(); i++) {
            if (icbcCards.get(i).getConsumeType().equals(consumeType)) {
                if (icbcCards.get(i).getAccountAmountExpense() > 0) {
                    result += icbcCards.get(i).getAccountAmountExpense();
                } else {
                    result += icbcCards.get(i).getAccountAmountIncome();
                }
            }
        }
        return result;
    }
}
