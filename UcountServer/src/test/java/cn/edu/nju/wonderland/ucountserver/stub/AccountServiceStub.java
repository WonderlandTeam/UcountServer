package cn.edu.nju.wonderland.ucountserver.stub;

import cn.edu.nju.wonderland.ucountserver.service.AccountService;
import cn.edu.nju.wonderland.ucountserver.vo.AccountInfoVO;
import cn.edu.nju.wonderland.ucountserver.vo.TotalAccountVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by green-cherry on 2017/8/25.
 */
@Service
public class AccountServiceStub implements AccountService {
    @Override
    public AccountInfoVO getAccountById(Long accountId) {
        return null;
    }

    @Override
    public List<AccountInfoVO> getAccountsByUser(String username) {
        return null;
    }

    @Override
    public Long addAccount(AccountInfoVO accountInfoVO) {
        return null;
    }

    @Override
    public void deleteAccount(Long accountId) {

    }

    @Override
    public TotalAccountVO getAccountByUserAndTime(String username, String time) {
        return null;
    }

    @Override
    public double getBalanceByUser(String username) {
        return 1000;
    }

    @Override
    public double getConsumedMoneyByDateAndUser(String username, String time) {
        return 100;
    }
}
