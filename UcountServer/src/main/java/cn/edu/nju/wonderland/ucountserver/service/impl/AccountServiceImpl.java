package cn.edu.nju.wonderland.ucountserver.service.impl;

import cn.edu.nju.wonderland.ucountserver.entity.Account;
import cn.edu.nju.wonderland.ucountserver.entity.User;
import cn.edu.nju.wonderland.ucountserver.repository.AccountRepository;
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
