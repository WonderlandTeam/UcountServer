package cn.edu.nju.wonderland.ucountserver.service.impl;

import cn.edu.nju.wonderland.ucountserver.entity.*;
import cn.edu.nju.wonderland.ucountserver.exception.ResourceConflictException;
import cn.edu.nju.wonderland.ucountserver.exception.ResourceNotFoundException;
import cn.edu.nju.wonderland.ucountserver.repository.*;
import cn.edu.nju.wonderland.ucountserver.service.AccountService;
import cn.edu.nju.wonderland.ucountserver.service.UserDetector;
import cn.edu.nju.wonderland.ucountserver.util.AutoAccountType;
import cn.edu.nju.wonderland.ucountserver.util.BillType;
import cn.edu.nju.wonderland.ucountserver.vo.AccountAddVO;
import cn.edu.nju.wonderland.ucountserver.vo.AccountInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static cn.edu.nju.wonderland.ucountserver.util.AutoAccountType.*;
import static cn.edu.nju.wonderland.ucountserver.util.BillFilter.ALIPAY_COMMODITY_FILTER;
import static cn.edu.nju.wonderland.ucountserver.util.BillFilter.ICBC_SUMMARY_FILTER;
import static cn.edu.nju.wonderland.ucountserver.util.BillType.OTHER_EXPENDITURE;
import static cn.edu.nju.wonderland.ucountserver.util.BillType.OTHER_INCOME;
import static cn.edu.nju.wonderland.ucountserver.util.DateHelper.DATE_TIME_FORMATTER;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;
    private IcbcCardRepository icbcCardRepository;
    private SchoolCardRepository schoolCardRepository;
    private AlipayRepository alipayRepository;
    private ManualBillingRepository manualBillingRepository;
    private UserDetector userDetector;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, IcbcCardRepository icbcCardRepository, SchoolCardRepository schoolCardRepository, AlipayRepository alipayRepository, ManualBillingRepository manualBillingRepository ,UserDetector userDetector) {
        this.accountRepository = accountRepository;
        this.icbcCardRepository = icbcCardRepository;
        this.schoolCardRepository = schoolCardRepository;
        this.alipayRepository = alipayRepository;
        this.manualBillingRepository = manualBillingRepository;
        this.userDetector = userDetector;
    }

    /**
     * 字符串转自动资产账户类型
     * 若无对应则返回null
     */
    private AutoAccountType stringToAccountType(String type) {
        if (type.equals(ALIPAY.accountType)) {
            return ALIPAY;
        }
        if (type.equals(ICBC_CARD.accountType)) {
            return ICBC_CARD;
        }
        if (type.equals(SCHOOL_CARD.accountType)) {
            return SCHOOL_CARD;
        }

        return null;
    }

    @Override
    public AccountInfoVO getAccountById(Long accountId) {
        Account account = accountRepository.findById(accountId);
        if (account == null) {
            throw new ResourceNotFoundException("账户不存在");
        }
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());

        AccountInfoVO accountInfoVO = new AccountInfoVO();
        accountInfoVO.username = account.getUsername();
        accountInfoVO.accountId = account.getId();
        accountInfoVO.cardID = account.getCardId();
        accountInfoVO.type = account.getCardType();
        accountInfoVO.income = 0;
        accountInfoVO.expend = 0;
//        System.out.println(account.getCardId());
        if (account.getCardType().equals(ALIPAY.accountType)) {
            List<Alipay> alipays = alipayRepository.findByCardId(account.getCardId(), null).getContent();
            if(alipays.size() == 0) {
                throw new ResourceConflictException("没有此支付宝数据");
            }
            List<Alipay> alipay = alipayRepository.getBalance(account.getCardId(),timestamp);
            accountInfoVO.balance = alipay.get(0).getBalance();
            for (int i = 0; i < alipays.size(); i++) {
                if (alipays.get(i).getIncomeExpenditureType().equals("收入")) {
                    accountInfoVO.income += alipays.get(i).getMoney();
                } else {
                    accountInfoVO.expend += alipays.get(i).getMoney();
                }
            }
        } else if (account.getCardType().equals(ICBC_CARD.accountType)) {
            List<IcbcCard> icbcCards = icbcCardRepository.findByCardId(account.getCardId(), null).getContent();
            if(icbcCards.size() == 0){
                throw  new ResourceConflictException("没有此工行卡数据" + account.getCardId());
            }
            List<IcbcCard> icbcCard = icbcCardRepository.getBalance(account.getCardId(),timestamp);
            accountInfoVO.balance = icbcCard.get(0).getBalance();
            for (int i = 0; i < icbcCards.size(); i++) {
                if (icbcCards.get(i).getAccountAmountIncome() > 0) {
                    accountInfoVO.income += icbcCards.get(i).getAccountAmountIncome();
                } else {
                    accountInfoVO.expend += icbcCards.get(i).getAccountAmountExpense();
                }
            }
        } else if (account.getCardType().equals(SCHOOL_CARD.accountType)) {
            List<SchoolCard> schoolCards = schoolCardRepository.findByCardId(account.getCardId(), null).getContent();
            if(schoolCards.size() == 0 ){
                throw new ResourceConflictException("没有此校园卡数据");
            }
            List<SchoolCard> schoolCard = schoolCardRepository.getBalance(account.getCardId(),timestamp);
            if(schoolCard == null || schoolCard.size() == 0){
                throw new ResourceConflictException("账号" + account.getCardId() + "校园卡没有账目数据");
            }
            accountInfoVO.balance = schoolCard.get(0).getBalance();
            for (int i = 0; i < schoolCards.size(); i++) {
                if (schoolCards.get(i).getIncomeExpenditure() > 0) {
                    accountInfoVO.income += schoolCards.get(i).getIncomeExpenditure();
                } else {
                    accountInfoVO.expend -= schoolCards.get(i).getIncomeExpenditure();
                }
            }
        } else {
            List<ManualBilling> manualBillings = manualBillingRepository.findByUsernameAndCardTypeAndCardId(account.getUsername(), account.getCardType(), account.getCardId());
            // 手动记账处理
            List<ManualBilling> manualBilling = manualBillingRepository.getBalance(account.getUsername(),account.getCardType(),account.getCardId(),timestamp);
            if (manualBilling == null || manualBilling.size() == 0) {
                throw new ResourceNotFoundException("账户没有账目数据");
            }
            accountInfoVO.balance = manualBilling.get(0).getBalance();
            for (int i = 0; i < manualBillings.size(); i++) {
                if (manualBillings.get(i).getIncomeExpenditure() > 0) {
                    accountInfoVO.income += manualBillings.get(i).getIncomeExpenditure();
                } else {
                    accountInfoVO.expend -= manualBillings.get(i).getIncomeExpenditure();
                }
            }
        }

        return accountInfoVO;
    }

    @Override
    public List<AccountInfoVO> getAccountsByUser(String username) {
        List<Account> accounts = accountRepository.findByUsername(username);
        List<AccountInfoVO> accountInfoVOList = new ArrayList<>();
        for(Account account : accounts){
            accountInfoVOList.add(this.getAccountById(account.getId()));
        }
        return accountInfoVOList;
    }

    @Override
    public Long addAccount(AccountAddVO vo) {
        // 判断用户是否存在
        if(!userDetector.isUserExists(vo.username)) {
            throw  new ResourceConflictException("用户不存在");
        }
        // 判断用户是否已有该账户
        if (accountRepository.findByUsernameAndCardTypeAndCardId(vo.username, vo.accountType, vo.cardId) != null) {
            throw new ResourceConflictException("账户已存在");
        }

        Account account = new Account();
        account.setCardType(vo.accountType);
        account.setUsername(vo.username);
        account.setCardId(vo.cardId);

        Long accountId = accountRepository.save(account).getId();

        // 判断账户是否为自动记账账户
        boolean isAutoAccount = false;
        for (AutoAccountType autoAccountType : AutoAccountType.values()) {
            if (vo.accountType.equals(autoAccountType.accountType)) {
                isAutoAccount = true;
                break;
            }
        }
        if (!isAutoAccount) {
            ManualBilling manualBilling = new ManualBilling();
            manualBilling.setUsername(vo.username);
            manualBilling.setCardId(vo.cardId);
            manualBilling.setCardType(vo.accountType);
            manualBilling.setTime(Timestamp.valueOf(LocalDateTime.now()));
            manualBilling.setBalance(vo.balance);
            manualBilling.setIncomeExpenditure(vo.balance);
            manualBilling.setConsumeType(vo.balance >= 0 ? OTHER_INCOME.billType : OTHER_EXPENDITURE.billType);
            manualBilling.setCommodity("新建账户");
            manualBilling.setRemark("创建新账户，初始余额为：" + vo.balance);
            manualBillingRepository.save(manualBilling);
        }

        return accountId;
    }

    @Override
    @Transactional
    public void deleteAccount(Long accountId) {
        Account account = accountRepository.findOne(accountId);
        if (account == null) {
            throw new ResourceNotFoundException("账户不存在");
        }

        // 删除资产账户账目数据
        if (account.getCardType().equals(ALIPAY.accountType)) {
            if(alipayRepository.findByCardId(account.getCardId(),null) != null){
                alipayRepository.deleteByCardId(account.getCardId());
            }
        } else if (account.getCardType().equals(ICBC_CARD.accountType)) {
            if(icbcCardRepository.findByCardId(account.getCardId(),null) != null){
                icbcCardRepository.deleteByCardId(account.getCardId());
            }
        } else if (account.getCardType().equals(SCHOOL_CARD.accountType)) {
            if(schoolCardRepository.findByCardId(account.getCardId(),null).getSize() != 0){
                schoolCardRepository.deleteByCardId(account.getCardId());
            }
        } else {
            if(manualBillingRepository.findByUsernameAndCardTypeAndCardId(account.getUsername(), account.getCardType(), account.getCardId()).size() != 0){
                manualBillingRepository.deleteByUsernameAndCardTypeAndCardId(account.getUsername(), account.getCardType(), account.getCardId());
            }
        }
        accountRepository.delete(accountId);
    }

    @Override
    public double getBalanceByUser(String username) {
        double result = 0;
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        List<Account> accounts = accountRepository.findByUsername(username);
        for (Account account : accounts) {
            if(account.getCardType().equals(SCHOOL_CARD.accountType)){
                List<SchoolCard> schoolCard = schoolCardRepository.getBalance(account.getCardId(),timestamp);
                if(schoolCard == null){
                    throw new ResourceConflictException("校园卡为空");
                }
                result += schoolCard.get(0).getBalance();
            }else if(account.getCardType().equals(ALIPAY.accountType)){
                List<Alipay> alipay = alipayRepository.getBalance(account.getCardId(),timestamp);
                if(alipay == null) {
                    throw new ResourceConflictException("支付宝为空");
                }
                result += alipay.get(0).getBalance();

            }else if(account.getCardType().equals(ICBC_CARD.accountType)){
                List<IcbcCard> icbcCard = icbcCardRepository.getBalance(account.getCardId(),timestamp);
                if(icbcCard == null) {
                    throw new ResourceConflictException("工行卡为空");
                }
                result += icbcCard.get(0).getBalance();
            }else{
                List<ManualBilling> manualBilling  = manualBillingRepository.getBalance(username,account.getCardType(),account.getCardId(),timestamp);
                if(manualBilling != null) {
                    result += manualBilling.get(0).getBalance();
                }
            }
        }
        return result;
    }

    @Override
    public double getConsumedMoneyByDateAndUser(String username, String time) {
        if (!userDetector.isUserExists(username)) {
            throw new ResourceNotFoundException("用户不存在");
        }

        LocalDateTime starDate = LocalDateTime.parse(time, DATE_TIME_FORMATTER);
        LocalDateTime endDate = starDate.plusDays(1);

        Timestamp startTimestamp = Timestamp.valueOf(starDate);
        Timestamp endTimestamp = Timestamp.valueOf(endDate);

        double result = 0;

        List<Account> accounts = accountRepository.findByUsername(username);
        for (Account account : accounts) {
            String accountType = account.getCardType();
            if (accountType.equals(ALIPAY.accountType)) {
                List<Alipay> alipayList = alipayRepository.findByUsernameAndCreateTimeBetween(username, startTimestamp, endTimestamp);
                for (Alipay alipay : alipayList) {
                    BillType billType = BillType.stringToBillType(alipay.getConsumeType());
                    if (billType != null && billType.ordinal() >= 4
                            && !ALIPAY_COMMODITY_FILTER.contains(alipay.getCommodity())) {
                        result += alipay.getMoney();
                    }
                }
            } else if (accountType.equals(ICBC_CARD.accountType)) {
                List<IcbcCard> icbcCardList = icbcCardRepository.findByUsernameAndTradeDateBetween(username, startTimestamp, endTimestamp);
                for (IcbcCard icbcCard : icbcCardList) {
                    if (icbcCard.getAccountAmountExpense() > 0 && !ICBC_SUMMARY_FILTER.contains(icbcCard.getSummary())) {
                        result += icbcCard.getAccountAmountExpense();
                    }
                }
            } else if (accountType.equals(SCHOOL_CARD.accountType)) {
                List<SchoolCard> schoolCardList = schoolCardRepository.findByUsernameAndTimeBetween(username, startTimestamp, endTimestamp);
                for (SchoolCard schoolCard : schoolCardList) {
                    if (schoolCard.getIncomeExpenditure() < 0) {
                        result += - schoolCard.getIncomeExpenditure();
                    }
                }
            } else {
                List<ManualBilling> manualBillingList = manualBillingRepository.findByUsernameAndTimeBetween(username, startTimestamp, endTimestamp);
                for (ManualBilling manualBilling : manualBillingList) {
                    if (manualBilling.getIncomeExpenditure() < 0) {
                        result += - manualBilling.getIncomeExpenditure();
                    }
                }
            }
        }

        return result;
    }
}
