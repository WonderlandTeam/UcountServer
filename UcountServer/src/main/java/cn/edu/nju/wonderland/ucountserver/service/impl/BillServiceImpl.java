package cn.edu.nju.wonderland.ucountserver.service.impl;

import cn.edu.nju.wonderland.ucountserver.entity.*;
import cn.edu.nju.wonderland.ucountserver.exception.InvalidRequestException;
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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.edu.nju.wonderland.ucountserver.util.AutoAccountType.*;
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
        vo.time = DateHelper.toTimeByTimeStamp(alipay.getCreateTime());
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
        vo.amount = Math.abs(manualBilling.getIncomeExpenditure());
        vo.time = DateHelper.toTimeByTimeStamp(manualBilling.getTime());
        vo.trader = manualBilling.getCommodity();
        return vo;
    }

    @Override
    public BillInfoVO getBill(Long accountId, Long billId) {
        Account account = accountRepository.findOne(accountId);
        if (account == null) {
            throw new ResourceNotFoundException("账户不存在");
        }
        BillInfoVO billInfoVO;

        String cardId = account.getCardId();
        String accountType = account.getCardType();

        if (accountType.equals(ALIPAY.accountType)) {                   // 支付宝
            Alipay alipay = alipayRepository.findByIdAndCardId(billId, cardId);
            if (alipay == null) {
                throw new ResourceNotFoundException("账目不存在");
            }
            billInfoVO = alipayToVO(alipay);
        } else if (accountType.equals(ICBC_CARD.accountType)) {         // 工行卡
            IcbcCard icbcCard = icbcCardRepository.findByIdAndCardId(billId, cardId);
            if (icbcCard == null) {
                throw new ResourceNotFoundException("账目不存在");
            }
            billInfoVO = icbcCardToVO(icbcCard);
        } else if (accountType.equals(SCHOOL_CARD.accountType)) {       // 校园卡
            SchoolCard schoolCard = schoolCardRepository.findByIdAndCardId(billId, cardId);
            if (schoolCard == null) {
                throw new ResourceNotFoundException("账目不存在");
            }
            billInfoVO = schoolCardToVO(schoolCard);
        } else  {                                                       // 手动记账
            ManualBilling manualBilling = manualBillingRepository.findByUsernameAndCardTypeAndCardIdAndId(account.getUsername(), accountType, cardId, billId);
            if (manualBilling == null) {
                throw new ResourceNotFoundException("账目不存在");
            }
            billInfoVO = manualBillingToVO(manualBilling);
        }

        return billInfoVO;
    }

    @Override
    public List<BillInfoVO> getBillsByAccount(Long accountId, Pageable pageable) {
        Account account = accountRepository.findOne(accountId);
        if (account == null) {
            throw new ResourceNotFoundException("账户不存在");
        }
        List<BillInfoVO> bills;

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
        } else {
            // 手动记账
            List<ManualBilling> manualBillingPage = manualBillingRepository.findByUsernameAndCardTypeAndCardId(account.getUsername(), accountType, cardId);
            bills = manualBillingPage
                    .stream()
                    .map(this::manualBillingToVO)
                    .collect(Collectors.toList());
        }
        return bills;
    }

    @Override
    public List<BillInfoVO> getMonthBillsByUser(String username, String month) {
        // TODO 月份处理
        LocalDateTime startDate = LocalDateTime.parse(month, DATE_TIME_FORMATTER);
        LocalDateTime endDate = startDate.plus(1, ChronoUnit.MONTHS);
        Timestamp startStamp = Timestamp.valueOf(startDate);
        Timestamp endStamp = Timestamp.valueOf(endDate);
        List<BillInfoVO> billInfoVOList = new ArrayList<>();
        Map<Integer, List<IcbcCard>> icbcCardmap = new HashMap<>();
        /* 工行卡的表*/
        List<SchoolCard> schoolCardList = schoolCardRepository.findByUsernameAndTimeBetween(username,startStamp,endStamp);
        List<Alipay> alipayList = alipayRepository.findByUsernameAndCreateTimeBetween(username, startStamp,endStamp);
        List<Account> accounts = accountRepository.findByUsername(username);
        for (int i = 0; i < accounts.size(); i++) {
            if (!accounts.get(i).getCardType().contains("银行卡")) {
                accounts.remove(i);//选出银行卡
            }
        }
        for (int i = 0; i < accounts.size(); i++) {
            icbcCardmap.put(i, icbcCardRepository.findByCardIdAndTradeDateBetween(accounts.get(i).getCardId(),startStamp,endStamp));
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
                billInfoVO.type = icbcCardList.get(i).getConsumeType();
                if (icbcCardList.get(i).getAccountAmountIncome() > 0){
                    billInfoVO.amount = icbcCardList.get(i).getAccountAmountIncome();
                } else {
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
            billInfoVO.type = schoolCardList.get(i).getConsumeType();
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
        List<ManualBilling> manualBillingList = manualBillingRepository.findByUsernameAndTimeBetween(username,startStamp,endStamp);
        for(ManualBilling manualBilling : manualBillingList){
            BillInfoVO billInfoVO = new BillInfoVO();
            billInfoVO.amount = manualBilling.getIncomeExpenditure();
            billInfoVO.type = manualBilling.getConsumeType();
            billInfoVO.trader = manualBilling.getCommodity();
            billInfoVO.time = String.valueOf(manualBilling.getTime());
            billInfoVOList.add(billInfoVO);
        }
        return billInfoVOList;
    }

    /**
     * 账目添加vo转ManualBilling实体
     */
    private ManualBilling billAddVOToEntity(Account account, BillAddVO vo) {

        ManualBilling manualBilling = new ManualBilling();
        manualBilling.setUsername(account.getUsername());
        manualBilling.setCardType(account.getCardType());
        manualBilling.setCardId(account.getCardId());

        manualBilling.setCommodity(vo.commodity);
        manualBilling.setConsumeType(vo.consumeType);
        manualBilling.setIncomeExpenditure(vo.incomeExpenditure);
        manualBilling.setTime(DateHelper.toTimestampByMonth(vo.time));
        manualBilling.setRemark(vo.remark);

        return manualBilling;
    }

    @Override
    public Long addBillManually(Long accountId, BillAddVO billAddVO) {
        Account account = accountRepository.findOne(accountId);
        if (account == null) {
            throw new ResourceNotFoundException("账户不存在");
        }
        String accountType = account.getCardType();
        if (accountType.equals(ALIPAY.accountType) || accountType.equals(ICBC_CARD.accountType) || accountType.equals(SCHOOL_CARD.accountType)) {
            throw new InvalidRequestException("可同步账户无法添加账目");
        }

        ManualBilling manualBilling = billAddVOToEntity(account, billAddVO);

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
        } else {
            ManualBilling manualBilling = manualBillingRepository.findByUsernameAndCardTypeAndCardIdAndId(account.getUsername(), accountType, cardId, billId);
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

        List<Alipay> alipays = alipayRepository.findByUsernameAndCreateTimeBetween(username, start, end);
        for (Alipay alipay :alipays) {
            if (alipay.getConsumeType() != null && alipay.getConsumeType().equals(consumeType) ) {
                result += alipay.getMoney();
            }
        }
        List<SchoolCard> schoolCards = schoolCardRepository.findByUsernameAndTimeBetween(username, start, end);
        for (SchoolCard schoolCard : schoolCards) {
            if (schoolCard.getConsumeType() != null && schoolCard.getConsumeType().equals(consumeType)) {
                result += Math.abs(schoolCard.getIncomeExpenditure());
            }
        }
        List<IcbcCard> icbcCards = icbcCardRepository.findByUsernameAndTradeDateBetween(username, start, end);
        for (IcbcCard icbcCard : icbcCards) {
            if (icbcCard.getConsumeType() != null && icbcCard.getConsumeType().equals(consumeType)) {
                if (icbcCard.getAccountAmountExpense() > 0) {
                    result += icbcCard.getAccountAmountExpense();
                } else {
                    result += icbcCard.getAccountAmountIncome();
                }
            }
        }
        List<Account> accounts = accountRepository.findByUsername(username);
        List<ManualBilling> manualBillings = new ArrayList<>();
        for(Account account :accounts) {
            manualBillings.addAll(manualBillingRepository.findByUsernameAndCardTypeAndCardId(username,account.getCardType(),account.getCardId()));
        }
        for(ManualBilling manualBilling: manualBillings ) {
            if(manualBilling.getConsumeType() != null && manualBilling.getCardType().equals(consumeType) ) {
                result += Math.abs(manualBilling.getIncomeExpenditure());
            }
        }
        // TODO 手动记账

        return result;
    }
}
