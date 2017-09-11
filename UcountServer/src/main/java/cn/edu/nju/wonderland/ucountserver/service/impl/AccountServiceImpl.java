package cn.edu.nju.wonderland.ucountserver.service.impl;

import cn.edu.nju.wonderland.ucountserver.entity.*;
import cn.edu.nju.wonderland.ucountserver.exception.ResourceConflictException;
import cn.edu.nju.wonderland.ucountserver.exception.ResourceNotFoundException;
import cn.edu.nju.wonderland.ucountserver.repository.*;
import cn.edu.nju.wonderland.ucountserver.service.AccountService;
import cn.edu.nju.wonderland.ucountserver.service.UserDetector;
import cn.edu.nju.wonderland.ucountserver.util.AutoAccountType;
import cn.edu.nju.wonderland.ucountserver.vo.AccountAddVO;
import cn.edu.nju.wonderland.ucountserver.vo.AccountInfoVO;
import cn.edu.nju.wonderland.ucountserver.vo.TotalAccountVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.edu.nju.wonderland.ucountserver.util.AutoAccountType.*;
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
    public TotalAccountVO getAccountByUserAndTime(String username, String time) {

        LocalDateTime starDate = LocalDateTime.parse(time, DATE_TIME_FORMATTER);
        LocalDateTime endDate = starDate.plus(1, ChronoUnit.MONTHS);

        Timestamp start = Timestamp.valueOf(starDate);
        Timestamp end = Timestamp.valueOf(endDate);
        TotalAccountVO totalAccountVO = new TotalAccountVO();

        List<Alipay> alipayList = alipayRepository.findByUsernameAndCreateTimeBetween(username, start, end);
        List<SchoolCard> schoolCardList = schoolCardRepository.findByUsernameAndTimeBetween(username, start, end);
        List<Account> accounts = accountRepository.findByUsername(username);
        Map<Integer, List<IcbcCard>> icbcCardmap = new HashMap<>();
        double income = 0;
        double expend = 0;
        double balance = 0;
        for (int i = 0; i < accounts.size(); i++) {
            if(!accounts.get(i).getCardType().equals(ICBC_CARD.accountType)){
                accounts.remove(i);
            }
            //获取所有银行卡当月账单
        }
        for(int i = 0 ; i < accounts.size() ; i++){
            icbcCardmap.put(i, icbcCardRepository.findByCardIdAndTradeDateBetween(accounts.get(i).getCardId(), start, end));
        }
        for (int k = 0; k < icbcCardmap.size(); k++) {
            List<IcbcCard> icbcCardList = icbcCardmap.get(0);
            for (int i = 0; i < icbcCardList.size(); i++) {
                System.out.println(i);
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
        for (int i = 0; i < alipayList.size(); i++) {
            if (alipayList.get(i).getIncomeExpenditureType().equals("收入")) {
                income += alipayList.get(i).getMoney();
            } else {
                expend += alipayList.get(i).getMoney();
            }
        }
        for (int i = 0; i < schoolCardList.size(); i++) {
            if (schoolCardList.get(i).getIncomeExpenditure() > 0) {
                income += schoolCardList.get(i).getIncomeExpenditure();
            } else {
                expend -= schoolCardList.get(i).getIncomeExpenditure();
            }
        }
        for (int k = 0; k < icbcCardmap.size(); k++) {
            List<IcbcCard> icbcCardList = icbcCardmap.get(k);
            for (int i = 0; i < icbcCardList.size(); i++) {
                if (icbcCardList.get(i).getAccountAmountIncome() > 0) {
                    income += icbcCardList.get(i).getAccountAmountIncome();
                } else {
                    expend += icbcCardList.get(i).getAccountAmountExpense();
                }
            }
        }
        totalAccountVO.setExpend(expend);
        totalAccountVO.setIncome(income);
        for(Account account:accounts){
            if(account.getCardType().equals(SCHOOL_CARD.accountType)){
                List<SchoolCard> schoolCard = schoolCardRepository.getBalance(account.getCardId(),end);
                if(schoolCard == null){
                    throw new ResourceConflictException("校园卡为空");
                }
                balance += schoolCard.get(0).getBalance();
            }else if(account.getCardType().equals(ALIPAY.accountType)){
                List<Alipay> alipay = alipayRepository.getBalance(account.getCardId(),end);
                if(alipay == null) {
                    throw new ResourceConflictException("支付宝为空");
                }
                balance += alipay.get(0).getBalance();

            }else if(account.getCardType().equals(ICBC_CARD.accountType)){
                List<IcbcCard> icbcCard = icbcCardRepository.getBalance(account.getCardId(),end);
                if(icbcCard == null) {
                    throw new ResourceConflictException("工行卡为空");
                }
                balance += icbcCard.get(0).getBalance();
            }else{
                List<ManualBilling> manualBilling  = manualBillingRepository.getBalance(username,account.getCardType(),account.getCardId(),end);
                if(manualBilling != null) {
                    balance += manualBilling.get(0).getBalance();
                }
            }
        }
        totalAccountVO.setBalance(balance);
        totalAccountVO.setUsername(username);
        return totalAccountVO;
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
        LocalDateTime starDate = LocalDateTime.parse(time, DATE_TIME_FORMATTER);
        LocalDateTime endDate = starDate.plusDays(1);

        Timestamp start = Timestamp.valueOf(starDate);
        Timestamp end = Timestamp.valueOf(endDate);
        double result = 0;
        List<Alipay> alipayList = alipayRepository.findByUsernameAndCreateTimeBetween(username, start, end);
        List<SchoolCard> schoolCardList = schoolCardRepository.findByUsernameAndTimeBetween(username, start, end);
        List<Account> accounts = accountRepository.findByUsername(username);
        Map<Integer, List<IcbcCard>> icbcCardmap = new HashMap<>();
        for (int i = 0; i < accounts.size(); i++) {
            if(!accounts.get(i).getCardType().equals(ICBC_CARD.accountType)){
                accounts.remove(i);
            }
            //获取所有工行卡
        }
        for(int i = 0 ; i < accounts.size() ; i++){
            icbcCardmap.put(i, icbcCardRepository.findByCardIdAndTradeDateBetween(accounts.get(i).getCardId(), start, end));
            //添加账单
        }
        for (int k = 0; k < icbcCardmap.size(); k++) {
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

        // TODO 根据用户名分别查找计算四种资产账户消费账目
         for ( int i = 0 ; i <alipayList.size();i++){
			if(alipayList.get(i).getIncomeExpenditureType().equals("支出")){
				result += Double.valueOf(alipayList.get(i).getIncomeExpenditureType());
			}
		}
		for ( int i = 0 ; i <schoolCardList.size();i++){
			if(schoolCardList.get(i).getIncomeExpenditure() < 0){
				result -= schoolCardList.get(i).getIncomeExpenditure();
			}
		}
		for(int k = 0; k < icbcCardmap.size() ;k++) {
			List<IcbcCard> icbcCardList = icbcCardmap.get(k);
			for (int i = 0; i < icbcCardList.size(); i++) {
				if (icbcCardList.get(i).getAccountAmountExpense() > 0) {
					result += icbcCardList.get(i).getAccountAmountExpense();
				}
			}
		}
		List<ManualBilling> manualBillings = manualBillingRepository.findByUsernameAndTimeBetween(username,start,end);
        for(ManualBilling manualBilling: manualBillings){
            result += manualBilling.getIncomeExpenditure();
        }
        return result;
    }
}
