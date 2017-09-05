package cn.edu.nju.wonderland.ucountserver.service;

import cn.edu.nju.wonderland.ucountserver.vo.AccountAddVO;
import cn.edu.nju.wonderland.ucountserver.vo.AccountInfoVO;
import cn.edu.nju.wonderland.ucountserver.vo.TotalAccountVO;

import java.util.List;

public interface AccountService {

    /**
     * 获取账户信息
     * @param accountId             账户id
     * @return                      账户信息
     */
    AccountInfoVO getAccountById(Long accountId);

    /**
     * 获取用户所有账户信息
     * @param username              用户名
     * @return                      账户信息列表
     */
    List<AccountInfoVO> getAccountsByUser(String username);

    /**
     * 添加账户
     * @param vo                    账户添加信息vo
     * @return                      账户id
     */
    Long addAccount(AccountAddVO vo);

    /**
     * 删除账户
     * @param accountId             账户id
     */
    void deleteAccount(Long accountId);


    /**
     * 获取用户当月的收入和支出
     * @param username              用户名
     * @param time                  当月时间，格式：2017-08-01 00：00：00
     * @return
     */
    TotalAccountVO getAccountByUserAndTime(String username,String time);

    /**
     * 获取用户的所有的账户的总余额
     * @param username              用户名
     * @return
     */
    double getBalanceByUser(String username);

    /**
     * 获取用户某一天的已消费金额
     * @param username              用户名
     * @param time                  某天，格式：2017-08-01 00：00：00
     * @return
     */
    double getConsumedMoneyByDateAndUser(String username,String time);

}
