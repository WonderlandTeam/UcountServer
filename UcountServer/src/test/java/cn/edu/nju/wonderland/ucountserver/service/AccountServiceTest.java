package cn.edu.nju.wonderland.ucountserver.service;

import cn.edu.nju.wonderland.ucountserver.repository.AlipayRepository;
import cn.edu.nju.wonderland.ucountserver.repository.IcbcCardRepository;
import cn.edu.nju.wonderland.ucountserver.vo.AccountInfoVO;
import cn.edu.nju.wonderland.ucountserver.vo.TotalAccountVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceTest {

    @Autowired
    public AccountService accountService;
    @Autowired
    public IcbcCardRepository icbcCardRepository;
    @Autowired
    public AlipayRepository alipayRepository;
    @Test
    public void testGetAccountById() throws IllegalAccessException {
        AccountInfoVO vo = accountService.getAccountById(1L);
        for (Field field : vo.getClass().getFields()) {
            System.out.println(field.getName() + ":\t" + field.get(vo));
        }
    }
    @Test
    public void testGetAccountByUserAndTime(){
        TotalAccountVO totalAccountVO = accountService.getAccountByUserAndTime("bian","2017-07-01 00:00:00");
        System.out.println(totalAccountVO.getExpend());
        System.out.println(totalAccountVO.getIncome());
        System.out.println(totalAccountVO.getBalance());
    }
    @Test
    public void testGetAccountsByUser(){
        List<AccountInfoVO> accountInfoVOList = accountService.getAccountsByUser("sigma");
        System.out.println(accountInfoVOList.size());
    }
    @Test
    public void testDeleteAccpunt(){
        accountService.deleteAccount(Long.valueOf(24));
    }
}
