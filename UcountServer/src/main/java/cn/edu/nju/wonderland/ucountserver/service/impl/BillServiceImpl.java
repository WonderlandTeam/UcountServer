package cn.edu.nju.wonderland.ucountserver.service.impl;

import cn.edu.nju.wonderland.ucountserver.entity.*;
import cn.edu.nju.wonderland.ucountserver.exception.InvalidRequestException;
import cn.edu.nju.wonderland.ucountserver.exception.ResourceNotFoundException;
import cn.edu.nju.wonderland.ucountserver.repository.*;
import cn.edu.nju.wonderland.ucountserver.service.BillService;
import cn.edu.nju.wonderland.ucountserver.service.UserDetector;
import cn.edu.nju.wonderland.ucountserver.util.BillType;
import cn.edu.nju.wonderland.ucountserver.util.DateHelper;
import cn.edu.nju.wonderland.ucountserver.vo.BillAddVO;
import cn.edu.nju.wonderland.ucountserver.vo.BillInfoVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    private UserDetector userDetector;

    public BillServiceImpl(IcbcCardRepository icbcCardRepository, SchoolCardRepository schoolCardRepository, AlipayRepository alipayRepository, ManualBillingRepository manualBillingRepository, AccountRepository accountRepository, UserDetector userDetector) {
        this.icbcCardRepository = icbcCardRepository;
        this.schoolCardRepository = schoolCardRepository;
        this.alipayRepository = alipayRepository;
        this.manualBillingRepository = manualBillingRepository;
        this.accountRepository = accountRepository;
        this.userDetector = userDetector;
    }

    /**
     * 支付宝账目记录转vo
     */
    private BillInfoVO alipayToVO(Alipay alipay) {
        BillInfoVO vo = new BillInfoVO();
        vo.billId = alipay.getId();
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
        vo.billId = icbcCard.getId();
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
        vo.billId = schoolCard.getId();
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
        vo.billId = manualBilling.getId();
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

        String accountType = account.getCardType();

        if (accountType.equals(ALIPAY.accountType)) {                   // 支付宝
            Alipay alipay = alipayRepository.findOne(billId);
            if (alipay == null) {
                throw new ResourceNotFoundException("账目不存在");
            }
            billInfoVO = alipayToVO(alipay);
        } else if (accountType.equals(ICBC_CARD.accountType)) {         // 工行卡
            IcbcCard icbcCard = icbcCardRepository.findOne(billId);
            if (icbcCard == null) {
                throw new ResourceNotFoundException("账目不存在");
            }
            billInfoVO = icbcCardToVO(icbcCard);
        } else if (accountType.equals(SCHOOL_CARD.accountType)) {       // 校园卡
            SchoolCard schoolCard = schoolCardRepository.findOne(billId);
            if (schoolCard == null) {
                throw new ResourceNotFoundException("账目不存在");
            }
            billInfoVO = schoolCardToVO(schoolCard);
        } else {                                                       // 手动记账
            ManualBilling manualBilling = manualBillingRepository.findOne(billId);
            if (manualBilling == null) {
                throw new ResourceNotFoundException("账目不存在");
            }
            billInfoVO = manualBillingToVO(manualBilling);
        }

        return billInfoVO;
    }

    @Override
    public void modifyBillConsumeType(Long accountId, Long billId, String consumeType) {
        Account account = accountRepository.findOne(accountId);
        if (account == null) {
            throw new ResourceNotFoundException("账户不存在");
        }
        // 消费类型检测
        BillType billType = BillType.stringToBillType(consumeType);
        if (billType == null) {
            throw new InvalidRequestException("消费类型不在可选范围内");
        }

        String accountType = account.getCardType();
        if (accountType.equals(ALIPAY.accountType)) {
            Alipay alipay = alipayRepository.findOne(billId);
            if (alipay == null) {
                throw new ResourceNotFoundException("账目不存在");
            }
            alipay.setConsumeType(billType.billType);
            alipayRepository.save(alipay);
        } else if (accountType.equals(ICBC_CARD.accountType)) {
            IcbcCard icbcCard = icbcCardRepository.findOne(billId);
            if (icbcCard == null) {
                throw new ResourceNotFoundException("账目不存在");
            }
            icbcCard.setConsumeType(billType.billType);
            icbcCardRepository.save(icbcCard);
        } else if (accountType.equals(SCHOOL_CARD.accountType)) {
            SchoolCard schoolCard = schoolCardRepository.findOne(billId);
            if (schoolCard == null) {
                throw new ResourceNotFoundException("账目不存在");
            }
            schoolCard.setConsumeType(billType.billType);
            schoolCardRepository.save(schoolCard);
        } else {
            ManualBilling manualBilling = manualBillingRepository.findOne(billId);
            if (manualBilling == null) {
                throw new ResourceNotFoundException("账目不存在");
            }
            manualBilling.setConsumeType(billType.billType);
            manualBillingRepository.save(manualBilling);
        }

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
        if (!userDetector.isUserExists(username)) {
            throw new ResourceNotFoundException("用户不存在");
        }
        if (month == null) {
            month = DateHelper.getTodayMonth();
        }
        LocalDateTime startDate = LocalDateTime.parse(month + "-01 00:00:00", DATE_TIME_FORMATTER);
        LocalDateTime endDate = startDate.plusMonths(1);
        Timestamp startStamp = Timestamp.valueOf(startDate);
        Timestamp endStamp = Timestamp.valueOf(endDate);

        List<BillInfoVO> result = new ArrayList<>();

        alipayRepository.findByUsernameAndCreateTimeBetween(username, startStamp, endStamp)
                .forEach(a -> result.add(alipayToVO(a)));

        icbcCardRepository
                .findByUsernameAndTradeDateBetween(username, startStamp, endStamp)
                .forEach(i -> result.add(icbcCardToVO(i)));
        schoolCardRepository
                .findByUsernameAndTimeBetween(username, startStamp, endStamp)
                .forEach(s -> result.add(schoolCardToVO(s)));
        manualBillingRepository
                .findByUsernameAndTimeBetween(username, startStamp, endStamp)
                .forEach(m -> result.add(manualBillingToVO(m)));

        // 按时间逆序排序
        result.sort((o1, o2) -> o2.time.compareTo(o1.time));

        return result;
    }

    /**
     * 账目添加vo转ManualBilling实体
     */
    private ManualBilling billAddVOToEntity(Account account, BillAddVO vo) {

        ManualBilling manualBilling = new ManualBilling();
        manualBilling.setUsername(account.getUsername());
        manualBilling.setCardType(account.getCardType());
        manualBilling.setCardId(account.getCardId());

        // 新余额计算
        Timestamp nowTimestamp = Timestamp.valueOf(LocalDateTime.now());
        double balance = manualBillingRepository.getBalance(account.getUsername(), account.getCardType(), account.getCardId(), nowTimestamp).get(0).getBalance();
        manualBilling.setBalance(balance + vo.incomeExpenditure);

        manualBilling.setCommodity(vo.commodity);
        manualBilling.setConsumeType(vo.consumeType);
        manualBilling.setIncomeExpenditure(vo.incomeExpenditure);
        Timestamp timestamp = Timestamp.valueOf(vo.time);
        manualBilling.setTime(timestamp);
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
        ManualBilling manualBilling;
        manualBilling = billAddVOToEntity(account, billAddVO);

        return manualBillingRepository.save(manualBilling).getId();
    }

    @Override
    public List<BillInfoVO> addBillAutomatically(Long accountId) {
        // 模拟数据生成
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
            Alipay alipay = alipayRepository.findOne(billId);
            if (alipay == null) {
                throw new ResourceNotFoundException("账目不存在");
            }
            alipayRepository.delete(alipay);
        } else if (accountType.equals(ICBC_CARD.accountType)) {
            IcbcCard icbcCard = icbcCardRepository.findOne(billId);
            if (icbcCard == null) {
                throw new ResourceNotFoundException("账目不存在");
            }
            icbcCardRepository.delete(icbcCard);
        } else if (accountType.equals(SCHOOL_CARD.accountType)) {
            SchoolCard schoolCard = schoolCardRepository.findOne(billId);
            if (schoolCard == null) {
                throw new ResourceNotFoundException("账目不存在");
            }
            schoolCardRepository.delete(schoolCard);
        } else {
            ManualBilling manualBilling = manualBillingRepository.findOne(billId);
            if (manualBilling == null) {
                throw new ResourceNotFoundException("账目不存在");
            }
            if (manualBillingRepository.countByUsernameAndCardTypeAndCardId(account.getUsername(), accountType, cardId) == 1) {
                throw new InvalidRequestException("手动记账账户至少需要有一条记录");
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
        for (Alipay alipay : alipays) {
            if (alipay.getConsumeType() != null && alipay.getConsumeType().equals(consumeType)) {
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
        for (Account account : accounts) {
            manualBillings.addAll(manualBillingRepository.findByUsernameAndCardTypeAndCardId(username, account.getCardType(), account.getCardId()));
        }
        for (ManualBilling manualBilling : manualBillings) {
            if (manualBilling.getConsumeType() != null && manualBilling.getCardType().equals(consumeType)) {
                result += Math.abs(manualBilling.getIncomeExpenditure());
            }
        }

        return result;
    }
}
