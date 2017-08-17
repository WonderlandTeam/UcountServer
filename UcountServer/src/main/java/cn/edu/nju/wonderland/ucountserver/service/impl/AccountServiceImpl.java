package cn.edu.nju.wonderland.ucountserver.service.impl;

import cn.edu.nju.wonderland.ucountserver.service.AccountService;
import cn.edu.nju.wonderland.ucountserver.vo.AccountAddVO;
import cn.edu.nju.wonderland.ucountserver.vo.AccountInfoVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    @Override
    public AccountInfoVO getAccountById(Long accountId) {
        return null;
    }

    @Override
    public List<AccountInfoVO> getAccountsByUser(Long userId) {
        return null;
    }

    @Override
    public Long addAccount(AccountAddVO accountAddVO) {
        return null;
    }

    @Override
    public void deleteAccount(Long accountId) {

    }
}
