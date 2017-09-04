package cn.edu.nju.wonderland.ucountserver.service.impl;

import cn.edu.nju.wonderland.ucountserver.entity.*;
import cn.edu.nju.wonderland.ucountserver.exception.InvalidRequestException;
import cn.edu.nju.wonderland.ucountserver.exception.ResourceConflictException;
import cn.edu.nju.wonderland.ucountserver.exception.ResourceNotFoundException;
import cn.edu.nju.wonderland.ucountserver.repository.*;
import cn.edu.nju.wonderland.ucountserver.service.AccountService;
import cn.edu.nju.wonderland.ucountserver.util.AccountType;
import cn.edu.nju.wonderland.ucountserver.vo.AccountInfoVO;
import cn.edu.nju.wonderland.ucountserver.vo.TotalAccountVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static cn.edu.nju.wonderland.ucountserver.util.AccountType.*;

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
     * 字符串转资产账户类型
     * 若无对应则返回null
     */
    private AccountType stringToAccountType(String type) {
        if (type.equals(ALIPAY.accountType)) {
            return ALIPAY;
        }
        if (type.equals(ICBC_CARD.accountType)) {
            return ICBC_CARD;
        }
        if (type.equals(SCHOOL_CARD.accountType)) {
            return SCHOOL_CARD;
        }
        if (type.equals(MANUAL.accountType)) {
            return MANUAL;
        }
        return null;
    }

    @Override
    public AccountInfoVO getAccountById(Long accountId) {
    	Account account = accountRepository.findById(accountId);
    	if(account == null){
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
                if(alipays.get(i).getIncomeExpenditureType().equals("收入")) {
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
    			if(schoolCards.get(i).getIncomeExpenditure() > 0) {
    				accountInfoVO.income += schoolCards.get(i).getIncomeExpenditure();
    			} else {
    				accountInfoVO.expend -= schoolCards.get(i).getIncomeExpenditure();
    			}
    		}
		} else if (account.getCardType().equals(MANUAL.accountType)) {
            List<ManualBilling> manualBillings = manualBillingRepository.findByCardId(account.getCardId(), null).getContent();
            // TODO
		}

        return accountInfoVO;
    }

    @Override
    public List<AccountInfoVO> getAccountsByUser(String username) {
    	List<Account> list = accountRepository.findByUsername(username);
    	List<AccountInfoVO> result = new ArrayList<>();
    	for (int i = 0; i < list.size(); i++) {
    		result.add(this.getAccountById(list.get(i).getId()));
    	}
        return result;
    }

    @Override
    public Long addAccount(AccountInfoVO accountInfoVO) {
        AccountType accountType = stringToAccountType(accountInfoVO.type);
        if (accountType == null) {
            throw new InvalidRequestException("不支持的账户类型");
        }

    	if(accountRepository.findByCardIdAndCardTypeAndUsername(accountInfoVO.cardID, accountType.accountType, accountInfoVO.username) != null) {
    		throw new ResourceConflictException("账户已存在");
    	}

    	Account account = new Account();
    	account.setCardId(accountInfoVO.cardID);
    	account.setCardType(accountInfoVO.type);
    	account.setUsername(accountInfoVO.username);
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
        } else if (account.getCardType().equals(MANUAL.accountType)) {
            manualBillingRepository.deleteByCardId(account.getCardId());
        }

    	accountRepository.delete(accountId);
    }

	@Override
	public TotalAccountVO getAccountByUserAndTime(String username, String time) {
		DateFormat sdf =  new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate  = new Date();
		Date endDate = new Date();
		Timestamp start = new Timestamp(0);
		Timestamp end = new Timestamp(0);
		TotalAccountVO totalAccountVO = new TotalAccountVO();
		try {
			startDate = sdf.parse(time);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startDate);
			calendar.add(Calendar.MONTH, 1);
			endDate = calendar.getTime();
			start = new Timestamp(startDate.getTime());
			end = new Timestamp(endDate.getTime());
		} catch (Exception e) {
			// TODO: handle exception
		}
		List<Alipay> alipayList = alipayRepository.getMouthBill(username, start, end);
		List<SchoolCard> schoolCardList =schoolCardRepository.getMouthBill(username, start, end);
		List<Account> accounts = accountRepository.findByUsername(username);
		Map<Integer,List<IcbcCard>> icbcCardmap = new HashMap<>();
		for ( int i = 0 ; i < accounts.size();i++){
			icbcCardmap.put(i,icbcCardRepository.getMouthBill(accounts.get(i).getCardId(),start,end));
			//获取所有银行卡当月账单
		}
		for(int k = 0 ; k < accounts.size();k++) {
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
		for ( int i = 0 ; i <alipayList.size();i++){
			if(alipayList.get(i).getIncomeExpenditureType().equals("收入")){
				totalAccountVO.setIncome(totalAccountVO.getIncome() + alipayList.get(i).getMoney());
			}else{
				totalAccountVO.setExpend(totalAccountVO.getExpend() + alipayList.get(i).getMoney());
			}
		}
		for ( int i = 0 ; i <schoolCardList.size();i++){
			if(schoolCardList.get(i).getIncomeExpenditure() > 0){
				totalAccountVO.setIncome(totalAccountVO.getIncome() + schoolCardList.get(i).getIncomeExpenditure());
			}else{
				totalAccountVO.setExpend(totalAccountVO.getExpend() - schoolCardList.get(i).getIncomeExpenditure());
			}
		}
		for(int k = 0 ; k <accounts.size();k ++ ) {
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
    	double result =  0;
    	List<Account> accounts = accountRepository.findByUsername(username);
		for(int i = 0 ; i < accounts.size() ;i ++ ) {
			if(icbcCardRepository.findByCardId(accounts.get(i).getCardId(),null) != null){
				result += icbcCardRepository.getBalance(accounts.get(i).getCardId()).getBalance();
			}else if (schoolCardRepository.findByCardId(accounts.get(i).getCardId(),null) != null) {
				result += schoolCardRepository.getBalance(accounts.get(i).getCardId()).getBalance();
			}else {
				result += alipayRepository.getBalance(accounts.get(i).getCardId()).getBalance();
			}
		}
		return result;
	}

	@Override
	public double getConsumedMoneyByDateAndUser(String username, String time) {
		DateFormat sdf =  new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate  = new Date();
		Date endDate = new Date();
		Timestamp start = new Timestamp(0);
		Timestamp end = new Timestamp(0);
		double result = 0 ;
		try {
			startDate = sdf.parse(time);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startDate);
			calendar.add(Calendar.DATE , 1);
			endDate = calendar.getTime();
			start = new Timestamp(startDate.getTime());
			end = new Timestamp(endDate.getTime());
		} catch (Exception e) {
			// TODO: handle exception
		}
		List<Alipay> alipayList = alipayRepository.getMouthBill(username, start, end);
		List<SchoolCard> schoolCardList =schoolCardRepository.getMouthBill(username, start, end);
		List<Account> accounts = accountRepository.findByUsername(username);
		Map<Integer,List<IcbcCard>> icbcCardmap = new HashMap<>();
		for ( int i = 0 ; i < accounts.size();i++){
			icbcCardmap.put(i,icbcCardRepository.getMouthBill(accounts.get(i).getCardId(),start,end));
		}
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
		for(int k = 0; k < accounts.size() ;k++) {
			List<IcbcCard> icbcCardList = icbcCardmap.get(k);
			for (int i = 0; i < icbcCardList.size(); i++) {
				if (icbcCardList.get(i).getAccountAmountExpense() > 0) {
					result += icbcCardList.get(i).getAccountAmountExpense();
				}
			}
		}
		return result;
	}
}
