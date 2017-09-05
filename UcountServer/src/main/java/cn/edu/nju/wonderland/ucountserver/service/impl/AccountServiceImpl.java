package cn.edu.nju.wonderland.ucountserver.service.impl;

import cn.edu.nju.wonderland.ucountserver.entity.*;
import cn.edu.nju.wonderland.ucountserver.exception.ResourceConflictException;
import cn.edu.nju.wonderland.ucountserver.exception.ResourceNotFoundException;
import cn.edu.nju.wonderland.ucountserver.repository.*;
import cn.edu.nju.wonderland.ucountserver.service.AccountService;
import cn.edu.nju.wonderland.ucountserver.util.AutoAccountType;
import cn.edu.nju.wonderland.ucountserver.vo.AccountAddVO;
import cn.edu.nju.wonderland.ucountserver.vo.AccountInfoVO;
import cn.edu.nju.wonderland.ucountserver.vo.TotalAccountVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.edu.nju.wonderland.ucountserver.util.AutoAccountType.*;
import static cn.edu.nju.wonderland.ucountserver.util.DateHelper.DATE_TIME_FORMATTER;

@Service
public class AccountServiceImpl implements AccountService {
    private AccountRepository accountRepository;
    private IcbcCardRepository icbcCardRepository;
    private SchoolCardRepository schoolCardRepository;
    private AlipayRepository alipayRepository;
    private ManualBillingRepository manualBillingRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, IcbcCardRepository icbcCardRepository, SchoolCardRepository schoolCardRepository, AlipayRepository alipayRepository, ManualBillingRepository manualBillingRepository) {
        this.accountRepository = accountRepository;
        this.icbcCardRepository = icbcCardRepository;
        this.schoolCardRepository = schoolCardRepository;
        this.alipayRepository = alipayRepository;
        this.manualBillingRepository = manualBillingRepository;
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
        AccountInfoVO accountInfoVO = new AccountInfoVO();
        accountInfoVO.id = account.getId();
        accountInfoVO.username = account.getUsername();
        accountInfoVO.cardID = account.getCardId();
        accountInfoVO.type = account.getCardType();
        accountInfoVO.income = 0;
        accountInfoVO.expend = 0;

        if (account.getCardType().equals(ALIPAY.accountType)) {
            List<Alipay> alipays = alipayRepository.findByCardId(account.getCardId(), null).getContent();
            Alipay alipay = alipayRepository.getBalance(account.getCardId());
            accountInfoVO.balance = alipay.getBalance();
            for (int i = 0; i < alipays.size(); i++) {
                if (alipays.get(i).getIncomeExpenditureType().equals("收入")) {
                    accountInfoVO.income += alipays.get(i).getMoney();
                } else {
                    accountInfoVO.expend += alipays.get(i).getMoney();
                }
            }
        } else if (account.getCardType().equals(ICBC_CARD.accountType)) {
            List<IcbcCard> icbcCards = icbcCardRepository.findByCardId(account.getCardId(), null).getContent();
            IcbcCard icbcCard = icbcCardRepository.getBalance(String.valueOf(accountId));
            accountInfoVO.balance = icbcCard.getBalance();
            for (int i = 0; i < icbcCards.size(); i++) {
                if (icbcCards.get(i).getAccountAmountIncome() > 0) {
                    accountInfoVO.income += icbcCards.get(i).getAccountAmountIncome();
                } else {
                    accountInfoVO.expend += icbcCards.get(i).getAccountAmountExpense();
                }
            }
        } else if (account.getCardType().equals(SCHOOL_CARD.accountType)) {
            List<SchoolCard> schoolCards = schoolCardRepository.findByCardId(account.getCardId(), null).getContent();
            SchoolCard schoolCard = schoolCardRepository.getBalance(String.valueOf(accountId));
            accountInfoVO.balance = schoolCard.getBalance();
            for (int i = 0; i < schoolCards.size(); i++) {
                if (schoolCards.get(i).getIncomeExpenditure() > 0) {
                    accountInfoVO.income += schoolCards.get(i).getIncomeExpenditure();
                } else {
                    accountInfoVO.expend -= schoolCards.get(i).getIncomeExpenditure();
                }
            }
        } else {
            List<ManualBilling> manualBillings = manualBillingRepository.findByUsernameAndCardTypeAndCardId(account.getUsername(), account.getCardType(), account.getCardId());
            // TODO 手动记账处理

        }

        return accountInfoVO;
    }

    @Override
    public List<AccountInfoVO> getAccountsByUser(String username) {
        List<Account> accounts = accountRepository.findByUsername(username);
        return accounts
                .stream()
                .map(e -> getAccountById(e.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public Long addAccount(AccountAddVO vo) {
        // TODO 判断用户是否存在

        // 判断用户是否已有该账户
        if (accountRepository.findByUsernameAndCardTypeAndCardId(vo.username, vo.accountType, vo.accountId) != null) {
            throw new ResourceConflictException("账户已存在");
        }

        Account account = new Account();
        account.setCardType(vo.accountType);
        account.setUsername(vo.username);
        account.setCardId(vo.accountId);

        return accountRepository.save(account).getId();
    }

    @Override
    public void deleteAccount(Long accountId) {
        Account account = accountRepository.findOne(accountId);
        if (account == null) {
            throw new ResourceNotFoundException("账户不存在");
        }

        // 删除资产账户账目数据
        if (account.getCardType().equals(ALIPAY.accountType)) {
            alipayRepository.deleteByCardId(account.getCardId());
        } else if (account.getCardType().equals(ICBC_CARD.accountType)) {
            icbcCardRepository.deleteByCardId(account.getCardId());
        } else if (account.getCardType().equals(SCHOOL_CARD.accountType)) {
            schoolCardRepository.deleteByCardId(account.getCardId());
        } else {
            manualBillingRepository.deleteByUsernameAndCardTypeAndCardId(account.getUsername(), account.getCardType(), account.getCardId());
        }

        accountRepository.delete(accountId);
    }

    @Override
    public TotalAccountVO getAccountByUserAndTime(String username, String time) {

        LocalDateTime starDate = LocalDateTime.parse(time, DATE_TIME_FORMATTER);
        LocalDateTime endDate = starDate.plusDays(1);

        Timestamp start = Timestamp.valueOf(starDate);
        Timestamp end = Timestamp.valueOf(endDate);
        TotalAccountVO totalAccountVO = new TotalAccountVO();

        List<Alipay> alipayList = alipayRepository.getMouthBill(username, start, end);
        List<SchoolCard> schoolCardList = schoolCardRepository.getMouthBill(username, start, end);
        List<Account> accounts = accountRepository.findByUsername(username);
        Map<Integer, List<IcbcCard>> icbcCardmap = new HashMap<>();
        for (int i = 0; i < accounts.size(); i++) {
            icbcCardmap.put(i, icbcCardRepository.getMouthBill(accounts.get(i).getCardId(), start, end));
            //获取所有银行卡当月账单
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
                totalAccountVO.setIncome(totalAccountVO.getIncome() + alipayList.get(i).getMoney());
            } else {
                totalAccountVO.setExpend(totalAccountVO.getExpend() + alipayList.get(i).getMoney());
            }
        }
        for (int i = 0; i < schoolCardList.size(); i++) {
            if (schoolCardList.get(i).getIncomeExpenditure() > 0) {
                totalAccountVO.setIncome(totalAccountVO.getIncome() + schoolCardList.get(i).getIncomeExpenditure());
            } else {
                totalAccountVO.setExpend(totalAccountVO.getExpend() - schoolCardList.get(i).getIncomeExpenditure());
            }
        }
        for (int k = 0; k < accounts.size(); k++) {
            List<IcbcCard> icbcCardList = icbcCardmap.get(k);
            for (int i = 0; i < icbcCardList.size(); i++) {
                if (icbcCardList.get(i).getAccountAmountIncome() > 0) {
                    totalAccountVO.setIncome(totalAccountVO.getIncome() + icbcCardList.get(i).getAccountAmountIncome());
                } else {
                    totalAccountVO.setExpend(totalAccountVO.getExpend() + icbcCardList.get(i).getAccountAmountExpense());
                }
            }
        }
        return totalAccountVO;
    }

    @Override
    public double getBalanceByUser(String username) {
        double result = 0;
        List<Account> accounts = accountRepository.findByUsername(username);
        for (Account account : accounts) {
            if (account.getCardType().equals(ALIPAY.accountType)) {
                result += alipayRepository.getBalance(account.getCardId()).getBalance();
            } else if (account.getCardType().equals(ICBC_CARD.accountType)) {
                result += icbcCardRepository.getBalance(account.getCardId()).getBalance();
            } else if (account.getCardType().equals(SCHOOL_CARD.accountType)) {
                result += schoolCardRepository.getBalance(account.getCardId()).getBalance();
            } else {
                // TODO 手动记账处理
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

        // TODO 根据用户名分别查找计算四种资产账户消费账目
//        List<Account> accounts = accountRepository.findByUsername(username);
//
//		List<Alipay> alipayList = alipayRepository.getMouthBill(username, start, end);
//		List<SchoolCard> schoolCardList = schoolCardRepository.getMouthBill(username, start, end);
//
//		Map<Integer,List<IcbcCard>> icbcCardmap = new HashMap<>();
//		for ( int i = 0 ; i < accounts.size();i++){
//			icbcCardmap.put(i,icbcCardRepository.getMouthBill(accounts.get(i).getCardId(),start,end));
//		}
//		for ( int i = 0 ; i <alipayList.size();i++){
//			if(alipayList.get(i).getIncomeExpenditureType().equals("支出")){
//				result += Double.valueOf(alipayList.get(i).getIncomeExpenditureType());
//			}
//		}
//		for ( int i = 0 ; i <schoolCardList.size();i++){
//			if(schoolCardList.get(i).getIncomeExpenditure() < 0){
//				result -= schoolCardList.get(i).getIncomeExpenditure();
//			}
//		}
//		for(int k = 0; k < accounts.size() ;k++) {
//			List<IcbcCard> icbcCardList = icbcCardmap.get(k);
//			for (int i = 0; i < icbcCardList.size(); i++) {
//				if (icbcCardList.get(i).getAccountAmountExpense() > 0) {
//					result += icbcCardList.get(i).getAccountAmountExpense();
//				}
//			}
//		}
        return result;
    }
}
