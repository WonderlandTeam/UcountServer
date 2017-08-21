package cn.edu.nju.wonderland.ucountserver.service.impl;

import cn.edu.nju.wonderland.ucountserver.entity.Account;
import cn.edu.nju.wonderland.ucountserver.entity.Alipay;
import cn.edu.nju.wonderland.ucountserver.entity.IcbcCard;
import cn.edu.nju.wonderland.ucountserver.entity.SchoolCard;
import cn.edu.nju.wonderland.ucountserver.entity.User;
import cn.edu.nju.wonderland.ucountserver.repository.AccountRepository;
import cn.edu.nju.wonderland.ucountserver.repository.AlipayRepository;
import cn.edu.nju.wonderland.ucountserver.repository.IcbcCardRepository;
import cn.edu.nju.wonderland.ucountserver.repository.SchoolCardRepository;
import cn.edu.nju.wonderland.ucountserver.repository.UserRepository;
import cn.edu.nju.wonderland.ucountserver.service.AccountService;
import cn.edu.nju.wonderland.ucountserver.vo.AccountAddVO;
import cn.edu.nju.wonderland.ucountserver.vo.AccountInfoVO;
import org.springframework.stereotype.Service;
import org.w3c.dom.ls.LSInput;

import java.util.ArrayList;
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
    		for ( int i = 0 ; i <icbcCards.size();i++){
    			if(icbcCards.get(i).getAccountAmountIncome() > 0){
    				accountInfoVO.income += icbcCards.get(i).getAccountAmountIncome();
    			}else{
    				accountInfoVO.expend += icbcCards.get(i).getAccountAmountExpense();
    			}
    		}
    		
    	}else if (schoolCardRepository.findByCardId(String.valueOf(accountId), null) != null) {
    		List<SchoolCard> schoolCards = schoolCardRepository.findByCardId(String.valueOf(accountId), null);
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
}
