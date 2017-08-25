package cn.edu.nju.wonderland.ucountserver.service.impl;

import cn.edu.nju.wonderland.ucountserver.entity.*;
import cn.edu.nju.wonderland.ucountserver.repository.*;
import cn.edu.nju.wonderland.ucountserver.service.AccountService;
import cn.edu.nju.wonderland.ucountserver.vo.AccountInfoVO;
import cn.edu.nju.wonderland.ucountserver.vo.TotalAccountVO;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
	private AccountRepository accountRepository;
	private UserRepository userRepository;
	private IcbcCardRepository icbcCardRepository;
	private SchoolCardRepository schoolCardRepository;
	private AlipayRepository alipayRepository;
    @Override
    public AccountInfoVO getAccountById(Long accountId) {
    	Account account = accountRepository.findById(accountId);
    	if(account == null){
    		return null;
    	}
    	AccountInfoVO accountInfoVO = new AccountInfoVO();
    	accountInfoVO.id = account.getId();
    	accountInfoVO.username = account.getUsername();
    	accountInfoVO.cardID = account.getCardId();
    	accountInfoVO.type = account.getCardType();
    	accountInfoVO.income = 0;
    	accountInfoVO.expend = 0;
    	if(icbcCardRepository.findByCardId(String.valueOf(account), null) != null){
    		List<IcbcCard> icbcCards = icbcCardRepository.findByCardId(String.valueOf(accountId), null);
    		IcbcCard icbcCard = icbcCardRepository.getBalance(String.valueOf(accountId));
    		accountInfoVO.balance = icbcCard.getBalance();
    		for ( int i = 0 ; i <icbcCards.size();i++){
    			if(icbcCards.get(i).getAccountAmountIncome() > 0){
    				accountInfoVO.income += icbcCards.get(i).getAccountAmountIncome();
    			}else{
    				accountInfoVO.expend += icbcCards.get(i).getAccountAmountExpense();
    			}
    		}
    		
    	}else if (schoolCardRepository.findByCardId(String.valueOf(accountId), null) != null) {
    		List<SchoolCard> schoolCards = schoolCardRepository.findByCardId(String.valueOf(accountId), null);
    		SchoolCard schoolCard = schoolCardRepository.getBalance(String.valueOf(accountId));
    		accountInfoVO.balance = schoolCard.getBalance();
    		for ( int i = 0 ; i <schoolCards.size();i++){
    			if(schoolCards.get(i).getIncomeExpenditure() > 0){
    				accountInfoVO.income += schoolCards.get(i).getIncomeExpenditure();
    			}else{
    				accountInfoVO.expend -= schoolCards.get(i).getIncomeExpenditure();
    			}
    		}
		}else {
    		List<Alipay> alipays = alipayRepository.findByCardId(String.valueOf(accountId), null);
    		for ( int i = 0 ; i <alipays.size();i++){
    			if(alipays.get(i).getIncomeExpenditureType().equals("收入")){
    				accountInfoVO.income += alipays.get(i).getMoney();
    			}else{
    				accountInfoVO.expend += alipays.get(i).getMoney();
    			}
    		}
		}
        return accountInfoVO;
    }

    @Override
    public List<AccountInfoVO> getAccountsByUser(String username) {
    	User user = userRepository.findByUsername(username);
    	List<Account> list = accountRepository.findByUsername( user.getUsername () );
    	List<AccountInfoVO> result = new ArrayList <AccountInfoVO> ();
    	for(int i = 0 ; i < list.size() ; i ++){
    		result.add(this.getAccountById(list.get(i).getId()));
    	}
        return result;
    }

    @Override
    public Long addAccount(AccountInfoVO accountInfoVO) {
    	if(accountRepository.findByCardId(accountInfoVO.cardID) != null){
    		return Long.valueOf(-1);
    	}
    	Account account = new Account();
    	account.setCardId(accountInfoVO.cardID);
    	account.setCardType(accountInfoVO.type);
    	account.setUsername(accountInfoVO.username);
    	account = accountRepository.save(account);
        return account.getId();
    }

    @Override
    public void deleteAccount(Long accountId) {
    	accountRepository.delete(accountId);;
    }

	@Override
	public TotalAccountVO getAccountByUserAndTime(String username, String time) {
		DateFormat sdf =  new  SimpleDateFormat("yyyy-MM-dd HH / mm / ss");
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
		List<Alipay> alipays = alipayRepository.getMouthBill(username, start, end);
		for ( int i = 0 ; i <alipays.size();i++){
			if(alipays.get(i).getIncomeExpenditureType().equals("收入")){
				totalAccountVO.setIncome(totalAccountVO.getIncome() + alipays.get(i).getMoney());
			}else{
				totalAccountVO.setExpend(totalAccountVO.getExpend() + alipays.get(i).getMoney());
			}
		}
		List<SchoolCard> schoolCards =schoolCardRepository.getMouthBill(username, start, end);
		for ( int i = 0 ; i <schoolCards.size();i++){
			if(schoolCards.get(i).getIncomeExpenditure() > 0){
				totalAccountVO.setIncome(totalAccountVO.getIncome() + schoolCards.get(i).getIncomeExpenditure());
			}else{
				totalAccountVO.setExpend(totalAccountVO.getExpend() - schoolCards.get(i).getIncomeExpenditure());
			}
		}
		List<IcbcCard> icbcCards = icbcCardRepository.getMouthBill(username, start, end);
		for ( int i = 0 ; i <icbcCards.size();i++){
			if(icbcCards.get(i).getAccountAmountIncome() > 0){
				totalAccountVO.setIncome(totalAccountVO.getIncome() + icbcCards.get(i).getAccountAmountIncome());
			}else{
				totalAccountVO.setExpend(totalAccountVO.getExpend() + icbcCards.get(i).getAccountAmountExpense());
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
			}
		}
		return 0;
	}

	@Override
	public double getConsumedMoneyByDateAndUser(String username, String time) {
		DateFormat sdf =  new  SimpleDateFormat("yyyy-MM-dd HH / mm / ss");
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
		List<Alipay> alipays = alipayRepository.getMouthBill(username, start, end);
		for ( int i = 0 ; i <alipays.size();i++){
			if(alipays.get(i).getIncomeExpenditureType().equals("支出")){
				result += Double.valueOf(alipays.get(i).getIncomeExpenditureType());
			}
		}
		List<SchoolCard> schoolCards =schoolCardRepository.getMouthBill(username, start, end);
		for ( int i = 0 ; i <schoolCards.size();i++){
			if(schoolCards.get(i).getIncomeExpenditure() < 0){
				result -= schoolCards.get(i).getIncomeExpenditure();
			}
		}
		List<IcbcCard> icbcCards = icbcCardRepository.getMouthBill(username, start, end);
		for ( int i = 0 ; i <icbcCards.size();i++){
			if(icbcCards.get(i).getAccountAmountExpense() > 0){
				result += icbcCards.get(i).getAccountAmountExpense();
			}
		}
		return result;
	}
}
